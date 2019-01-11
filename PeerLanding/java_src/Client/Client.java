package Client;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import Protos.Protocolo.*;

public class Client {
    Notifier notifier;
    Socket socket;
    InputStream is;
    OutputStream os;
    Scanner input;
    boolean auth;
    int type;

    public Client() throws IOException {
        this.socket = new Socket("localhost", 3000);
        this.is = socket.getInputStream();
        this.os = socket.getOutputStream();
        this.auth = false;
        this.input  = new Scanner(System.in);
    }

    public void autenticacao()
    {
        while(!auth)
        {
            System.out.println("username: ");
            String username = this.input.next();
            System.out.println("password: ");
            String password = this.input.next();
            AuthReq authReq = AuthReq.newBuilder().setUsername(username).setPassword(password).build();
            AuthRep authRep = null;
            try {
                os.write(authReq.toByteArray());
                os.flush();
                byte [] response = this.recv();
                authRep = AuthRep.parseFrom(response);
                if (authRep.getSucesso()) {
                    this.auth = true;
                    this.type = authRep.getTipo();
                    break;
                }
                else
                    System.out.println("Login invalido");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public void empresa(){


        boolean flag = true;
        while(flag){
            System.out.println(
                    "1- Criar leilão\n" +
                            "2- Emissão de taxa fixa\n" +
                            "3- Notificações\n" +
                            "0- Logout"

            );
            String userInput = this.input.next();
            switch (userInput) {
                case "1":
                    this.leilao();
                    break;
                case "2":
                    this.emissao();
                    break;
                case "3":
                    this.notificacoes();
                    break;
                case "0":
                    flag = false;
                    break;
                default:
                    break;
            }
        }

    }


    public void leilao(){
        System.out.println("Juro máximo: ");
        String juro = this.input.next();
        System.out.println("Quantia total: ");
        String quantia = this.input.next();
        Reply rep = null;
        Mensagem leilaoReq = Mensagem.newBuilder()
                .setTipo("leilao")
                .setJuro(Integer.parseInt(juro))
                .setQuantia(Integer.parseInt(quantia)).build();
        try {
            os.write(leilaoReq.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        rep = getReply();
        if(rep != null && rep.getSucesso()) {
            System.out.println("Sucesso no leilão");
        }
    }



    public void emissao(){
        System.out.println("Quantia: ");
        String quantia = this.input.next();
        Reply rep = null;
        Mensagem emissaoReq = Mensagem.newBuilder()
                .setTipo("emissao")
                .setQuantia(Integer.parseInt(quantia)).build();
        try {
            os.write(emissaoReq.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        rep = getReply();
        if(rep != null && rep.getSucesso()) {
            System.out.println("Sucesso na emissao");
        }
    }


    public void investidor() {
        boolean flag = true;
        while(flag){
            System.out.println(
                    "1- Licitar leilão\n" +
                            "2- Subscrição de empréstimo a taxa fixa\n" +
                            "3- Notificações\n" +
                            "0- Logout\n"
            );
            String userInput = this.input.next();
            switch (userInput) {
                case "1":
                    this.licitar();
                    break;
                case "2":
                    this.emprestimo();
                    break;
                case "3":
                    this.notificacoes();
                    break;
                case "0":
                    this.logout();
                    flag = false;
                    break;
                default:
                    break;
            }
            System.out.println(flag);
        }
    }

    public void licitar(){
        System.out.println("Empresa: ");
        String empresa = this.input.next();
        System.out.println("Juro: ");
        String juro = this.input.next();
        System.out.println("Quantia: ");
        String quantia = this.input.next();
        Reply rep = null;
        Mensagem licitarReq = Mensagem.newBuilder()
                .setTipo("licitar")
                .setEmpresa(empresa)
                .setJuro(Integer.parseInt(juro))
                .setQuantia(Integer.parseInt(quantia)).build();
        try {
            os.write(licitarReq.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        rep = getReply();
        if(rep != null && rep.getSucesso()) {
            System.out.println("Sucesso na licitação");
        }
    }

    public void emprestimo(){
        System.out.println("Empresa: ");
        String empresa = this.input.next();
        System.out.println("Quantia: ");
        String quantia = this.input.next();
        Reply rep = null;
        Mensagem emprestimoReq = Mensagem.newBuilder()
                .setTipo("emprestimo")
                .setEmpresa(empresa)
                .setQuantia(Integer.parseInt(quantia)).build();
        try {
            os.write(emprestimoReq.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        rep = getReply();
        if(rep != null && rep.getSucesso()) {
            System.out.println("Sucesso no emprestimo");
        }
    }

    public void logout()
    {
        Mensagem logoutReq = Mensagem.newBuilder()
                .setTipo("logout")
                .build();
        System.out.println("logout");
        try {
            os.write(logoutReq.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void notificacoes(){
    }

    public void run(){


        this.autenticacao();
        this.notifier = new Notifier();
        new Thread(this.notifier).start();
        if (this.type == 1)
        {
            investidor();
        }
        else if (this.type == 2){
            empresa();
        }
        try {
            this.is.close();
            this.os.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Reply getReply() {
        Reply rep = null;
        try {
            byte[] response = this.recv();
            rep = Reply.parseFrom(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rep;
    }

    public byte[] recv(){
        byte[] tmp = new byte[4096];
        int len = 0;
        try {
            len = is.read(tmp);
            byte[] response = new byte[len];

            for(int i = 0; i < len; i++)
                response[i] = tmp[i];
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args)
    {
        try {
            Client client = new Client();
            client.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
