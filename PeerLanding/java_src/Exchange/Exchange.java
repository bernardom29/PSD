package Exchange;
import Protos.Protocolo.ExchangeReply;
import Protos.Protocolo.Mensagem;
import com.google.protobuf.InvalidProtocolBufferException;
import org.zeromq.ZMQ;

import java.util.concurrent.*;

public class Exchange implements Runnable {
    ConcurrentHashMap<String, Empresa> empresas;
    ConcurrentHashMap<String, Leilao> leiloes;
    ConcurrentHashMap<String, Emissao> emissoes;
    ZMQ.Socket rep;
    ZMQ.Socket pub;
    ZMQ.Context context;


    public Exchange(int port) {
        this.context = ZMQ.context(1);
        this.rep  = context.socket(ZMQ.REP);
        this.pub  = context.socket(ZMQ.PUB);
        this.rep.bind("tcp://localhost:"+port);
        this.pub.connect("tcp://localhost:2001");
        this.emissoes = new ConcurrentHashMap<String, Emissao>();
        this.leiloes = new ConcurrentHashMap<String, Leilao>();
        this.empresas = new ConcurrentHashMap<String, Empresa>();
    }



    public boolean licitar(String empresa, String investidor, int quantia, int juro){
        Leilao leilaoAtual = leiloes.get(empresa);
        if (leilaoAtual != null && juro < leilaoAtual.taxaMaxima) {
            Licitacao licitacao = new Licitacao(investidor,juro,quantia);
            leilaoAtual.licitacoes.add(licitacao);
            pub.send("leilao-"+empresa+"-nova licitação de: "+investidor+ " juro: "+juro + " quantia: "+quantia);
            return true;
        }else{
            return false;
        }
    }
    public boolean emprestimo(String empresa, String investidor, int quantia){
        Emissao emissaoAtual = emissoes.get(empresa);
        if (emissaoAtual != null) {
            Licitacao licitacao = new Licitacao(investidor,quantia);
            emissaoAtual.licitacoes.add(licitacao);
            pub.send("emissao-"+empresa+"-nova licitação de: "+investidor+ " quantia: "+quantia);
            return true;
        }else{
            return false;
        }
    }



    public boolean emissao(String empresa, int quantia){
        Empresa empresaObj = empresas.get(empresa);
        if (empresaObj != null){
            float taxaMaxima = empresaObj.taxaEmissao();
            Emissao novaEmissao = new Emissao(taxaMaxima,quantia,empresa,false);
            emissoes.put(empresa,novaEmissao);
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.schedule(new TerminaEmissao(empresa,empresas,emissoes,pub),60, TimeUnit.SECONDS);
            this.pub.send("emissao-"+empresa+"-Começou uma emissao");
            return true;
        }
        return false;
    }
    public boolean leilao(String empresa, int quantia, int juro){
        Leilao leilaoAtual = leiloes.getOrDefault(empresa,null);
        if (leilaoAtual == null &&  quantia % 1000 == 0){
            Leilao leilao = new Leilao(juro,quantia,empresa,false);
            this.leiloes.put(empresa,leilao);
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.schedule(new TerminaLeilao(empresa,empresas,leiloes, pub),60, TimeUnit.SECONDS);

            this.pub.send("leilao-"+empresa+"-Começou um leilão");
            return true;
        }
        return false;
    }
    public boolean notificacao(Mensagem mensagem){
        System.out.println("notifica");
        return true;
    };


    public void run(){
        while(true)
        {
            byte[] packet = this.recv(this.rep);
            try {
                Mensagem mensagem = Mensagem.parseFrom(packet);
                String tipo = mensagem.getTipo();
                System.out.println(mensagem);
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
            }
        }
    }


    public byte[] recv(ZMQ.Socket socket){
        byte[] tmp;
        int len = 0;
        tmp = socket.recv();
        System.out.println("Exchange: Pedido recebido");
        len = tmp.length;
        byte[] response = new byte[len];
        for(int i = 0; i < len; i++)
            response[i] = tmp[i];
        return response;

    }

    public static void main(String[] args){
        for (int i = 0; i < 5; i++) {
            new Thread(new Exchange(4000+i)).start();
        }
    }
    /*
     * socket zeromq pull(receber pedido) e push(enviar resposta)
     * thread1 que faz continuamente recv de pull e inserir numa mailbox
     * thread2 que processa pedidos da cabeça da mailbox,
     *   fazer send pelo socket de push
     *   dependendo do pedido fazer alterações no diretorio
     * */
}
