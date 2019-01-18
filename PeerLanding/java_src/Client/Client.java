package Client;
import Protos.Protocolo.AuthRep;
import Protos.Protocolo.AuthReq;
import Protos.Protocolo.Mensagem;
import Protos.Protocolo.Reply;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.System.*;

public class Client {
    private Notifier notifier;
    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private Scanner input = new Scanner(in);
    private boolean auth;
    private int type;
    private String username;

    public Client() throws IOException {
        this.socket = new Socket("localhost", 3000);
        this.is = socket.getInputStream();
        this.os = socket.getOutputStream();
        this.auth = false;
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
        int len;
        try {
            len = is.read(tmp);
            byte[] response = new byte[len];

            arraycopy(tmp, 0, response, 0, len);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void enviar(Mensagem packet, String tipo, String empresa){
        try {
            os.write(packet.toByteArray());
            this.notifier.subscricao(tipo, empresa);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void subscrever(){
        out.println("Tipo: ");
        String tipo = this.input.next();
        out.println("Empresas: ");
        String empresas = this.input.next();
        this.notifier.subscricao(tipo, empresas);
    }


    private void notificacoes(){
        String str =this.notifier.mailbox();
        if(str.equals("")) out.println("Não existem notificações");
        else out.println(str);
    }

    private void resultadoOperacao(Reply rep, String mensagem){
        if(rep != null && rep.getSucesso()) {
            out.println("Sucesso: " + mensagem);
        }else out.println("Insucesso: " + mensagem);
    }

    public void leilao(){
        out.println("Juro máximo: ");
        String juro = this.input.next();
        out.println("Quantia total: ");
        String quantia = this.input.next();
        Reply rep;
        Mensagem req = Mensagem.newBuilder()
                .setTipo("leilao")
                .setJuro(Float.parseFloat(juro))
                .setQuantia(Integer.parseInt(quantia)).build();
        this.enviar(req, "leilao", this.username);
        rep = getReply();
        this.resultadoOperacao(rep, "leilão");
    }

    public void emissao(){
        Reply rep;
        Mensagem req;
        String quantia;
        out.println("Quantia: ");
        quantia = this.input.next();
        req = Mensagem.newBuilder()
                .setTipo("emissao")
                .setQuantia(Integer.parseInt(quantia)).build();
        this.enviar(req,"emissao", username);
        rep = getReply();
        this.resultadoOperacao(rep, "emissao");
    }

    private void licitar(){
        out.println("Empresa: ");
        String empresa = this.input.next();
        out.println("Juro: ");
        String juro = this.input.next();
        out.println("Quantia: ");
        String quantia = this.input.next();
        Reply rep;
        Mensagem req = Mensagem.newBuilder()
                .setTipo("licitar")
                .setEmpresa(empresa)
                .setJuro(Float.parseFloat(juro))
                .setQuantia(Integer.parseInt(quantia)).build();
        this.enviar(req, "leilao", empresa);
        rep = getReply();
        this.resultadoOperacao(rep, "leilao");
    }

    private void emprestimo(){
        out.println("Empresa: ");
        String empresa = this.input.next();
        out.println("Quantia: ");
        String quantia = this.input.next();
        Reply rep;
        Mensagem req = Mensagem.newBuilder()
                .setTipo("emprestimo")
                .setEmpresa(empresa)
                .setQuantia(Integer.parseInt(quantia)).build();
        this.enviar(req, "emissao", empresa);
        rep = getReply();
        this.resultadoOperacao(rep, "emprestimo");
    }

    private void autenticacao()
    {
        while(!auth)
        {
            out.println("username: ");
            this.username = this.input.next();
            out.println("password: ");
            String password = this.input.next();
            AuthReq authReq = AuthReq.newBuilder().setUsername(this.username).setPassword(password).build();
            AuthRep authRep;
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
                    out.println("Login invalido");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void logout()
    {
        Mensagem logoutReq = Mensagem.newBuilder()
                .setTipo("logout")
                .build();
        try {
            os.write(logoutReq.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void empresa(){
        boolean flag = true;
        while(flag){
            out.println(
                            "1- Criar leilão\n" +
                            "2- Criar emissão\n" +
                            "3- Subscrições\n" +
                            "4- Notificações\n" +
                            "0- Logout\n"

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
                    this.subscrever();
                    break;
                case "4":
                    this.notificacoes();
                    break;
                case "0":
                    this.logout();
                    flag = false;
                    break;
                default:
                    break;
            }
        }

    }

    private void investidor() {
        boolean flag = true;
        while(flag){
            out.println(
                            "1- Licitar leilão\n" +
                            "2- Subscrição de empréstimo a taxa fixa\n" +
                            "3- Subscrições\n" +
                            "4- Notificações\n" +
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
                    this.subscrever();
                    break;
                case "4":
                    this.notificacoes();
                    break;
                case "0":
                    this.logout();
                    flag = false;
                    break;
                default:
                    break;
            }
        }
    }


    public void run(){
        this.autenticacao();
        this.notifier = new Notifier();
        Thread tn = new Thread(this.notifier);
        tn.start();
        if (this.type == 1) {
            investidor();
        }
        else if (this.type == 2){
            empresa();
        }
        try {
            this.is.close();
            this.os.close();
            this.socket.close();
            this.input.close();
            tn.interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public static void main(String[] args)
    {
        try {
            Client client = new Client();
            client.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
        exit(0);
    }
}
