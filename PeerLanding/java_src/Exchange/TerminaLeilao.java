package Exchange;

import org.zeromq.ZMQ;

import java.util.concurrent.ConcurrentHashMap;

public class TerminaLeilao implements Runnable {
    String empresa;
    ConcurrentHashMap<String, Empresa> empresas;
    ConcurrentHashMap<String, Leilao> leiloes;
    ZMQ.Socket pub;

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
        Leilao leilao = leiloes.remove(this.empresa);
        //TODO
        //fazer verificação do sucesso
        Empresa empresa = empresas.get(this.empresa);
        empresa.historicoLeiloes.add(leilao);
        this.pub.send("leilao-"+empresa+"-Terminado");
    }
}
