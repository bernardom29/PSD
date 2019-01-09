package Directory;

import org.zeromq.ZMQ;

import java.util.HashMap;

public class Directory {
    HashMap<String, Empresa> empresas;
    HashMap<String, Leilao> leiloes;
    HashMap<String, Emissao> emissoes;
    ZMQ.Socket rep;
    ZMQ.Context context;


    public Directory() {
        this.empresas = new HashMap<String, Empresa>();
        this.leiloes = new HashMap<String, Leilao>();
        this.emissoes = new HashMap<String, Emissao>();
        this.context = ZMQ.context(1);
        this.rep = context.socket(ZMQ.REP);
        this.rep.bind("tcp://*:3000");
    }

    public void run(){
        byte[] req = rep.recv();

    }

}
