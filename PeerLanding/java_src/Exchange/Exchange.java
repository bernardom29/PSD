package Exchange;
import Protos.Protocolo.*;
import com.google.protobuf.InvalidProtocolBufferException;
import org.zeromq.ZMQ;

import java.io.IOException;
import java.util.HashMap;

public class Exchange implements Runnable{
    HashMap<String, Empresa> empresas;
    HashMap<String, Leilao> leiloes;
    HashMap<String, Emissao> emissoes;
    ZMQ.Socket rep;
    ZMQ.Socket pub;
    ZMQ.Context context;


    public Exchange(int port) {
        this.context = ZMQ.context(1);
        this.rep  = context.socket(ZMQ.REP);
        this.pub  = context.socket(ZMQ.PUB);
        this.rep.bind("tcp://localhost:"+port);
        this.pub.connect("tcp://localhost:2000");
    }



    public boolean licitar(String empresa, String investidor, int quantia, int juro){
        //Verificar se o juro <= taxa maxima do leilao
        //se sim
        // atualizar dir
        // notificar subs
        System.out.println("licitar");
        return true;
    }
    public boolean emprestimo(String empresa, String investidor, int quantia){
        // atualizar dir
        // notificar subs
        System.out.println("emprestimo");
        return true;
    }
    public boolean emissao(String empresa, int quantia){
        //Verificar se a ultima acao foi leilao ou emissao
        //se leilao && se terminou leilao com sucesso
            //obter taxa max alocada nesse leilao
        //se emissao && se emissao completa
            //obter taxa da emissao
        //se emissao && emissao incompleta
            //taxa da emissao * 1.1
        //executors para timeout da emissao
        //atualizar diretorio
        //Notificar subs
        System.out.println("emissao");
        return true;
    }
    public boolean leilao(String empresa, int quantia, int juro){
        //verificar se emp tem leilao em curso
        //quantia multiplo de 1000
        //executors para timeout do leilao
        //atualizar diretorio
        //Notificar subs
        System.out.println("leilao");
        return true;
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
                ExchangeRequest pedido = ExchangeRequest.parseFrom(packet);
                Mensagem mensagem = pedido.getMensagem();
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
                        .setPid(pedido.getPid())
                        .setSucesso(sucesso)
                        .build();
                this.rep.send(resposta.toByteArray());
                if (sucesso)
                    notificacao(mensagem);
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
