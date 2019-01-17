package Exchange;


import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.zeromq.ZMQ;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.System.out;

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

        String uri = "http://localhost:8080/empresa/leilao";
        RestRequest rr = null;
        Leilao leilao = leiloes.remove(this.empresa);
        if(leilao.getMontanteTotal()<=leilao.getInvestimentoTotal()) {
            leilao.setSucesso(true);
            System.out.println("O Leilão foi um sucesso.");
            if(leilao.alocaInvestidores()) {
                rr = new RestRequest(this.empresa, this.idL,false,true, leilao.getLicitacoes());
            } else {
                rr = new RestRequest(this.empresa, this.idL, false, true);
            }
        } else {
            System.out.println("O Leilão não foi um sucesso.");
            rr = new RestRequest(this.empresa, this.idL,false,false);
        }

        Empresa empresa = empresas.get(this.empresa);
        empresa.historicoLeiloes.add(leilao);
        System.out.println(empresa);
        synchronized (this.pub){
            this.pub.send("leilao-"+this.empresa+"-Terminado");
        }

    }

    private void sendPut(String postUrl, String data) {
        URL url = null;
        out.println(data);
        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpPut put = new HttpPut(postUrl);

            put.setHeader("Content-Type","application/json");
            put.setEntity(new StringEntity(data));
            HttpResponse response = httpclient.execute(put);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
