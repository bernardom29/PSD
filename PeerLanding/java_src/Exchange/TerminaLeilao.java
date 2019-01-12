package Exchange;

import org.zeromq.ZMQ;

import java.util.concurrent.ConcurrentHashMap;

public class TerminaLeilao implements Runnable {
    private String empresa;
    private ConcurrentHashMap<String, Empresa> empresas;
    private ConcurrentHashMap<String, Leilao> leiloes;
    private final ZMQ.Socket pub;

    public TerminaLeilao(String empresa,
                         ConcurrentHashMap<String, Empresa> empresas,
                         ConcurrentHashMap<String, Leilao> leiloes,
                         ZMQ.Socket pub)
    {
        this.empresa = empresa;
        this.empresas = empresas;
        this.leiloes = leiloes;
        this.pub = pub;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Leilao leilao = leiloes.remove(this.empresa);
        //TODO
        //fazer verificação do sucesso
        Empresa empresa = empresas.get(this.empresa);
        empresa.historicoLeiloes.add(leilao);
        System.out.println(empresa);
        synchronized (this.pub){
            this.pub.send("leilao-"+this.empresa+"-Terminado");
        }
    }
}
