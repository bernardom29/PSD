package Exchange;
import Protos.Protocolo.ExchangeReply;
import Protos.Protocolo.Mensagem;
import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.zeromq.ZMQ;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.System.arraycopy;
import static java.lang.System.out;

public class Exchange implements Runnable {
    private ConcurrentHashMap<String, Empresa> empresas;
    private ConcurrentHashMap<String, Leilao> leiloes;
    private ConcurrentHashMap<String, Emissao> emissoes;
    private ZMQ.Socket rep;
    private final ZMQ.Socket pub;
    private ZMQ.Context context;


    public Exchange(int port, ConcurrentHashMap<String, Empresa> empresas) {
        this.context = ZMQ.context(1);
        this.rep  = context.socket(ZMQ.REP);
        this.pub  = context.socket(ZMQ.PUB);
        this.rep.bind("tcp://localhost:"+port);
        this.pub.connect("tcp://localhost:2001");
        this.emissoes = new ConcurrentHashMap<String, Emissao>();
        this.leiloes = new ConcurrentHashMap<String, Leilao>();
        this.empresas = empresas;
    }

    private boolean licitar(String empresa, String investidor, int quantia, float juro){
        Leilao leilaoAtual = leiloes.get(empresa);
        if (leilaoAtual != null && juro < leilaoAtual.getTaxaMaxima() &&  quantia % 100 == 0) {
            Licitacao licitacao = new Licitacao(investidor,juro,quantia);
            leilaoAtual.getLicitacoes().add(licitacao);
            synchronized (this.pub) {
                pub.send("leilao-" + empresa + "-nova licitação de: " + investidor + " juro: " + juro + " quantia: " + quantia);
            }
            this.postDiretorioInvestidor("licitar", empresa,empresas.get(empresa).historicoLeiloes.size(), leilaoAtual.getLicitacoes().size()-1, investidor, juro, quantia);
            return true;
        }else{
            return false;
        }
    }

    private boolean emprestimo(String empresa, String investidor, int quantia){
        Emissao emissaoAtual = emissoes.get(empresa);
        if (emissaoAtual != null  &&  quantia % 100 == 0) {
            Licitacao licitacao = new Licitacao(investidor,quantia);
            emissaoAtual.getLicitacoes().add(licitacao);
            synchronized (this.pub) {
                pub.send("emissao-" + empresa + "-nova licitação de: " + investidor + " quantia: " + quantia);
            }
            this.postDiretorioInvestidor("emprestimo", empresa,empresas.get(empresa).historicoEmissoes.size(), emissaoAtual.getLicitacoes().size()-1, investidor, 0, quantia);
            return true;
        } else {
            return false;
        }
    }

    public boolean emissao(String empresa, int quantia){
        Empresa empresaObj = empresas.get(empresa);
        if (empresaObj != null && empresaObj.historicoLeiloes.size() > 0){
            Leilao ultimoLeilao = empresaObj.historicoLeiloes.lastElement();
            if(ultimoLeilao.isSucesso() &&  quantia % 1000 == 0) {
                float taxaMaxima = empresaObj.taxaEmissao();
                int idL = empresas.get(empresa).historicoEmissoes.size();
                Emissao novaEmissao = new Emissao(taxaMaxima,quantia,empresa);
                emissoes.put(empresa,novaEmissao);
                new Thread(new TerminaEmissao(empresa,empresas,emissoes, pub,idL)).start();
                synchronized (this.pub) {
                    this.pub.send("emissao-"+empresa+"-Começou uma emissao");
                }
                this.postDiretorioEmpresa("emissao", empresa,idL, taxaMaxima, quantia);
                return true;
            }
        }
        return false;
    }


    public boolean leilao(String empresa, int quantia, float juro){
        Leilao leilaoAtual = leiloes.getOrDefault(empresa,null);
        if (leilaoAtual == null &&  quantia % 1000 == 0){
            Leilao leilao = new Leilao(juro,quantia,empresa);
            this.leiloes.put(empresa,leilao);
            int idL = empresas.get(empresa).historicoLeiloes.size();
            new Thread(new TerminaLeilao(empresa,empresas,leiloes, pub,idL)).start();
            synchronized (this.pub) {
                this.pub.send("leilao-"+empresa+"-Começou um leilão");
            }
            this.postDiretorioEmpresa("leilao", empresa,idL, juro, quantia);
            return true;
        }
        return false;
    }

    private void postDiretorioEmpresa (String tipo, String empresa, int idL, float taxaMaxima, int quantia) {
        String urlRequest = null;
        RestRequest rr = new RestRequest(empresa, idL, taxaMaxima, quantia);
        String data = new Gson().toJson(rr);
        if(tipo.equals("leilao")) {
            urlRequest = "http://localhost:8080/empresa/leilao";
        } else if (tipo.equals("emissao")) {
            urlRequest = "http://localhost:8080/empresa/emissao";
        }
        sendPost(urlRequest, data);
    }

    private void postDiretorioInvestidor(String tipo, String empresa, int idL, int id, String investidor, float juro, int quantia) {
        String urlRequest = null;
        RestRequest rr = null;
        if(tipo.equals("licitar")) {
            urlRequest = "http://localhost:8080/empresa/leilao/licitacao";
            rr = new RestRequest(empresa, idL, id, investidor,juro, quantia);
        } else if (tipo.equals("emprestimo")) {
            rr = new RestRequest(empresa, idL, id, investidor, quantia);
            urlRequest = "http://localhost:8080/empresa/emissao/licitacao";
        }
        String data = new Gson().toJson(rr);
        sendPost(urlRequest, data);
    }

    private void sendPost(String postUrl, String data) {
        URL url = null;
        out.println(data);
        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost post = new HttpPost(postUrl);

            post.setHeader("Content-Type","application/json");
            post.setEntity(new StringEntity(data));
            HttpResponse response = httpclient.execute(post);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public byte[] recv(ZMQ.Socket socket) {
        byte[] tmp;
        int len;
        tmp = socket.recv();
        out.println("Exchange: Pedido recebido");
        len = tmp.length;
        byte[] response = new byte[len];
        arraycopy(tmp, 0, response, 0, len);
        return response;
    }

    public void run() {
        while(true)
        {
            byte[] packet = this.recv(this.rep);
            try {
                Mensagem mensagem = Mensagem.parseFrom(packet);
                String tipo = mensagem.getTipo();
                out.println(mensagem);
                boolean sucesso;
                switch (tipo){
                    case "licitar":
                        sucesso = this.licitar(
                                mensagem.getEmpresa(),
                                mensagem.getInvestidor(),
                                mensagem.getQuantia(),
                                mensagem.getJuro()
                        );
                        break;
                    case "emprestimo":
                        sucesso = this.emprestimo(
                                mensagem.getEmpresa(),
                                mensagem.getInvestidor(),
                                mensagem.getQuantia()
                        );
                        break;
                    case "emissao":
                        sucesso = this.emissao(
                                mensagem.getEmpresa(),
                                mensagem.getQuantia()
                        );
                        break;
                    case "leilao":
                        sucesso = this.leilao(
                                mensagem.getEmpresa(),
                                mensagem.getQuantia(),
                                mensagem.getJuro()
                        );
                        break;
                    default:
                        sucesso = false;
                        break;
                }
                ExchangeReply resposta = ExchangeReply.newBuilder()
                        .setSucesso(sucesso)
                        .build();
                this.rep.send(resposta.toByteArray());
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
                break;
            }
        }
    }


    public static void main(String[] args){
        ArrayList<ConcurrentHashMap<String, Empresa>> lmap = new ArrayList<>();
        ConcurrentHashMap<String, Empresa> map1 = new ConcurrentHashMap<String, Empresa>();
        ConcurrentHashMap<String, Empresa> map2 = new ConcurrentHashMap<String, Empresa>();
        ConcurrentHashMap<String, Empresa> map3 = new ConcurrentHashMap<String, Empresa>();
        ConcurrentHashMap<String, Empresa> map4 = new ConcurrentHashMap<String, Empresa>();
        ConcurrentHashMap<String, Empresa> map5 = new ConcurrentHashMap<String, Empresa>();
        map1.put("CanecaLda", new Empresa("CanecaLda"));
        map1.put("SapatoLda", new Empresa("SapatoLda"));
        map2.put("IsqueiroLda", new Empresa("IsqueiroLda"));
        map3.put("MesasLda", new Empresa("MesasLda"));
        map3.put("AguaLda", new Empresa("AguaLda"));
        map3.put("VinhoLda", new Empresa("VinhoLda"));
        map4.put("SandesLda", new Empresa("SandesLda"));
        map4.put("OreoLda", new Empresa("OreoLda"));
        map5.put("MongoLda", new Empresa("MongoLda"));
        map5.put("RelogioLda", new Empresa("RelogioLda"));
        lmap.add(map1);
        lmap.add(map2);
        lmap.add(map3);
        lmap.add(map4);
        lmap.add(map5);
        for (int i = 0; i < 5; i++) {
            new Thread(new Exchange(4000+i, lmap.get(i))).start();
        }
    }
}
