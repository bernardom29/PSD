package Exchange;

import org.zeromq.ZMQ;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class TerminaEmissao implements Runnable {
    private String empresa;
    private ConcurrentHashMap<String, Empresa> empresas;
    private ConcurrentHashMap<String, Emissao> emissoes;
    private final ZMQ.Socket pub;

    public TerminaEmissao(String empresa, ConcurrentHashMap<String, Empresa> empresas,
                          ConcurrentHashMap<String, Emissao> emissoes, ZMQ.Socket pub) {
        this.empresa = empresa;
        this.empresas = empresas;
        this.emissoes = emissoes;
        this.pub = pub;
        System.out.println("A emissao termina em 60s");
    }

    @Override
    public void run() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Emissao emissao = emissoes.remove(this.empresa);
        Empresa empresa = empresas.get(this.empresa);
        empresa.historicoEmissoes.add(emissao);
        synchronized (this.pub){
            pub.send("emissao-"+this.empresa+"-Terminado");
        }
    }
}