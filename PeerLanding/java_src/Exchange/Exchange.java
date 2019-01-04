package Exchange;
import Client.Protocolo.*;
import com.google.protobuf.InvalidProtocolBufferException;
import org.zeromq.ZMQ;

import java.io.IOException;

public class Exchange {

    ZMQ.Socket pull;
    ZMQ.Socket push;
    ZMQ.Socket pub;
    ZMQ.Context context;

    public Exchange(int port) {
        this.context = ZMQ.context(1);
        this.pub = context.socket(ZMQ.PUB);
        this.push = context.socket(ZMQ.PUSH);
        this.pull = context.socket(ZMQ.PULL);
        this.pub.bind("tcp://*:2001");
        this.push.connect("tcp://*:"+(port+1000));
        this.pull.bind("tcp://*:"+port);

    }

    public boolean licitar(String empresa, String investidor, int quantia, int juro){}
    public boolean emprestimo(String empresa, String investidor, int quantia){}
    public boolean emissao(String empresa, int quantia){}
    public boolean leilao(String empresa, int quantia, int juro){}
    public boolean notificacao(Mensagem mensagem){}


    public void run(){
        while(true)
        {
            byte[] packet = this.recv();
            try {
                ExchangeRequest pedido = ExchangeRequest.parseFrom(packet);
                Mensagem mensagem = pedido.getMensagem();
                String tipo = mensagem.getTipo();
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
                this.push.send(resposta.toByteArray());
                if (sucesso)
                    notificacao(mensagem);
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
        }
    }


    public byte[] recv(){
        byte[] tmp;
        int len = 0;
        tmp = this.pull.recv();
        len = tmp.length;
        byte[] response = new byte[len];
        for(int i = 0; i < len; i++)
            response[i] = tmp[i];
        return response;

    }



    public static void main(String[] args){
        for (int i = 0; i < 5; i++) {
            new Exchange(4000+i);
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
