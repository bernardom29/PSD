package Exchange;
import Protos.Protocolo.ExchangeReply;
import Protos.Protocolo.Mensagem;
import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.zeromq.ZMQ;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static java.lang.System.*;

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
            ///empresas/{nome}/leiloes/{idL}/{id}/{investidor}⁄{taxa}⁄{quantia}
            this.postDiretorioInvestidor("licitar", empresa,empresas.get(empresa).historicoLeiloes.size() + 1, leilaoAtual.getLicitacoes().size() + 1, investidor, juro, quantia);
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
            ///empresas/{nome}/emissoes/{idL}/{id}/{investidor}⁄{quantia}
            this.postDiretorioInvestidor("emprestimo", empresa,empresas.get(empresa).historicoEmissoes.size() + 1, emissaoAtual.getLicitacoes().size() +1, investidor, 0, quantia);
            return true;
        } else {
            return false;
        }
    }

    public boolean emissao(String empresa, int quantia){
        Empresa empresaObj = empresas.get(empresa);
        if (empresaObj != null){
            Leilao ultimoLeilao = empresaObj.historicoLeiloes.lastElement();
            if(ultimoLeilao.isSucesso() &&  quantia % 1000 == 0) {
                float taxaMaxima = empresaObj.taxaEmissao();
                Emissao novaEmissao = new Emissao(taxaMaxima,quantia,empresa);
                emissoes.put(empresa,novaEmissao);
                new Thread(new TerminaEmissao(empresa,empresas,emissoes, pub)).start();
                synchronized (this.pub) {
                    this.pub.send("emissao-"+empresa+"-Começou uma emissao");
                }
                ///empresas/{nome}/emissoes/{id}/{taxaMaxima}/{montanteTotal}/
                this.postDiretorioEmpresa("emissao", empresa,empresas.get(empresa).historicoEmissoes.size() + 1, taxaMaxima, quantia);
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
            new Thread(new TerminaLeilao(empresa,empresas,leiloes, pub)).start();
            synchronized (this.pub) {
                this.pub.send("leilao-"+empresa+"-Começou um leilão");
            }
            this.postDiretorioEmpresa("leilao", empresa,empresas.get(empresa).historicoLeiloes.size() + 1, juro, quantia);
            return true;
        }
        return false;
    }

    private void postDiretorioEmpresa (String tipo, String empresa, int idL, float taxaMaxima, int quantia) {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost=null;
        List<NameValuePair> params = new ArrayList<>();

        if(tipo.equals("leilao")) {
            httppost = new HttpPost("http://localhost/empresas/{nome}/leiloes/{id}/{taxaMaxima}/{montanteTotal}");
        } else if (tipo.equals("emissao")) {
            httppost = new HttpPost("http://localhost/empresas/{nome}/emissoes/{id}/{taxaMaxima}/{montanteTotal}");
        }

        params.add(new BasicNameValuePair("nome", empresa));
        params.add(new BasicNameValuePair("id", Integer.toString(idL)));
        params.add(new BasicNameValuePair("taxaMaxima", Float.toString(taxaMaxima)));
        params.add(new BasicNameValuePair("montanteTotal", Integer.toString(quantia)));

        sendPost(httpclient,httppost,params);

    }

    private void postDiretorioInvestidor(String tipo, String empresa, int idL, int id, String investidor, float juro, int quantia) {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost=null;
        List<NameValuePair> params = new ArrayList<>();
        if(tipo.equals("licitar")) {
            httppost = new HttpPost("http://localhost/empresas/{nome}/leiloes/{idL}/{id}/{investidor}⁄{taxa}⁄{quantia}");
            params.add(new BasicNameValuePair("taxa", Float.toString(juro)));
        } else if (tipo.equals("emprestimo")) {
            httppost = new HttpPost("http://localhost/empresas/{nome}/leiloes/{idL}/{id}/{investidor}⁄{quantia}");
        }

        params.add(new BasicNameValuePair("nome", empresa));
        params.add(new BasicNameValuePair("idL", Integer.toString(idL)));
        params.add(new BasicNameValuePair("id", Integer.toString(id)));
        params.add(new BasicNameValuePair("quantia", Integer.toString(quantia)));
        params.add(new BasicNameValuePair("investidor", investidor));

        sendPost(httpclient,httppost,params);
    }

    private void sendPost(HttpClient httpclient, HttpPost httppost, List<NameValuePair> params) {
        try {
            //send post
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse response = httpclient.execute(httppost);

            //receive response
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                System.out.println("Resposta recebida " + entity.toString());
            }
        } catch (IOException | NullPointerException e) {
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
