package Exchange;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClients;
import org.zeromq.ZMQ;

import java.io.IOException;
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
        String uri = "http://localhost:8080/empresas/" + empresa + "/leiloes/" + idL + "/";
        HttpPut httpput;

        Leilao leilao = leiloes.remove(this.empresa);
        if(leilao.getMontanteTotal()<=leilao.getInvestimentoTotal()) {
            leilao.setSucesso(true);
            System.out.println("O Leilão foi um sucesso.");
            httpput= new HttpPut(uri+ true + "/" + false);
        } else {
            System.out.println("O Leilão não foi um sucesso.");
            httpput= new HttpPut(uri+ false + "/" + false);
        }

        Empresa empresa = empresas.get(this.empresa);
        empresa.historicoLeiloes.add(leilao);
        System.out.println(empresa);
        synchronized (this.pub){
            this.pub.send("leilao-"+this.empresa+"-Terminado");
        }

        try {
            //send post
            HttpResponse response = httpclient.execute(httpput);

            //receive response
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                System.out.println("Resposta recebida " + entity.toString());
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}
