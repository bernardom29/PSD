package Exchange;
import Protos.Protocolo.ExchangeReply;
import Protos.Protocolo.Mensagem;
import com.google.protobuf.InvalidProtocolBufferException;
import org.zeromq.ZMQ;

import java.util.ArrayList;
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
            //TODO Fazer post de licitação
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
            //TODO Fazer post de licitação
            return true;
        }else{
            return false;
        }
    }



    public boolean emissao(String empresa, int quantia){
        Empresa empresaObj = empresas.get(empresa);
        if (empresaObj != null){
            Leilao ultimoLeilao = empresaObj.historicoLeiloes.lastElement();
            if(ultimoLeilao.isSucesso() &&  quantia % 1000 == 0) {
                float taxaMaxima = empresaObj.taxaEmissao();
                Emissao novaEmissao = new Emissao(taxaMaxima,quantia,empresa,false);
                emissoes.put(empresa,novaEmissao);
                new Thread(new TerminaEmissao(empresa,empresas,emissoes, pub)).start();
                synchronized (this.pub) {
                    this.pub.send("emissao-"+empresa+"-Começou uma emissao");
                }
                //TODO Fazer post de emissao
                return true;
            }
        }
        return false;
    }

    public boolean leilao(String empresa, int quantia, float juro){
        Leilao leilaoAtual = leiloes.getOrDefault(empresa,null);
        if (leilaoAtual == null &&  quantia % 1000 == 0){
            Leilao leilao = new Leilao(juro,quantia,empresa,false);
            this.leiloes.put(empresa,leilao);
            new Thread(new TerminaLeilao(empresa,empresas,leiloes, pub)).start();
            synchronized (this.pub) {
                this.pub.send("leilao-"+empresa+"-Começou um leilão");
            }
            //TODO Fazer post de leilao
            return true;
        }
        return false;
    }

    public byte[] recv(ZMQ.Socket socket){
        byte[] tmp;
        int len;
        tmp = socket.recv();
        out.println("Exchange: Pedido recebido");
        len = tmp.length;
        byte[] response = new byte[len];
        arraycopy(tmp, 0, response, 0, len);
        return response;
    }

    public void run(){
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
