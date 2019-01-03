package Exchange;

public class Exchange {

    /*
    * socket zeromq pull(receber pedido) e push(enviar resposta)
    * thread1 que faz continuamente recv de pull e inserir numa mailbox
    * thread2 que processa pedidos da cabeça da mailbox,
    *   fazer send pelo socket de push
    *   dependendo do pedido fazer alterações no diretorio
    * */
}
