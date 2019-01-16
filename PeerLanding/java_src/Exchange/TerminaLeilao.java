package Exchange;


import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.zeromq.ZMQ;

import java.util.concurrent.ConcurrentHashMap;

public class TerminaLeilao implements Runnable {
    private String empresa;
    private ConcurrentHashMap<String, Empresa> empresas;
    private ConcurrentHashMap<String, Leilao> leiloes;
    private final ZMQ.Socket pub;
    private int idL;

    public TerminaLeilao(String empresa,
                         ConcurrentHashMap<String, Empresa> empresas,
                         ConcurrentHashMap<String, Leilao> leiloes,
                         ZMQ.Socket pub,
                         int idL)
    {
        this.empresa = empresa;
        this.empresas = empresas;
        this.leiloes = leiloes;
        this.idL=idL;
        this.pub = pub;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Update Diretorio
        HttpClient httpclient = HttpClients.createDefault();
        String uri = "http://localhost/empresas/" + empresa + "/leiloes/" + idL + "/";
        HttpPost httppost;

        Leilao leilao = leiloes.remove(this.empresa);
        if(leilao.getMontanteTotal()<=leilao.getInvestimentoTotal()) {
            leilao.setSucesso(true);
            System.out.println("O Leilão foi um sucesso.");
            httppost= new HttpPost(uri+ true + "/" + false);
        } else {
            System.out.println("O Leilão não foi um sucesso.");
            httppost= new HttpPost(uri+ false + "/" + false);
        }

        Empresa empresa = empresas.get(this.empresa);
        empresa.historicoLeiloes.add(leilao);
        System.out.println(empresa);
        synchronized (this.pub){
            this.pub.send("leilao-"+this.empresa+"-Terminado");
        }

        //TODO Isto está mal?
        Exchange.sendPost(httpclient,httppost);
    }
}
