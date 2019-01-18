package Exchange;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.zeromq.ZMQ;

import java.util.concurrent.ConcurrentHashMap;

import static java.lang.System.out;

public class TerminaEmissao implements Runnable {
    private String empresa;
    private ConcurrentHashMap<String, Empresa> empresas;
    private ConcurrentHashMap<String, Emissao> emissoes;
    private final ZMQ.Socket pub;
    private int idL;

    public TerminaEmissao(String empresa, ConcurrentHashMap<String, Empresa> empresas,
                          ConcurrentHashMap<String, Emissao> emissoes, ZMQ.Socket pub, int idL) {
        this.empresa = empresa;
        this.empresas = empresas;
        this.emissoes = emissoes;
        this.pub = pub;
        this.idL=idL;
        System.out.println("A emissao termina em 60s");
    }

    @Override
    public void run() {
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("A emissão terminou.");
        Emissao emissao = emissoes.remove(this.empresa);
        emissao.setSucesso(true);
        Empresa empresa = empresas.get(this.empresa);
        empresa.historicoEmissoes.add(emissao);

        //Update Diretorio
        String uri = "http://localhost:8080/empresa/emissao";
        synchronized (this.pub){
            pub.send("emissao-"+this.empresa+"-Terminado");
        }
        RestRequest rr = null;

        if(emissao.getMontanteTotal()<=emissao.getInvestimentoTotal()) {
            emissao.setSucesso(true);
            System.out.println("A emissão foi um sucesso.");
            rr = new RestRequest(this.empresa, this.idL, false, true);
        } else {
            emissao.setSucesso(false);
            System.out.println("A emissão não foi um sucesso.");
            rr = new RestRequest(this.empresa, this.idL, false, false);
        }
        String data = new Gson().toJson(rr);
        sendPut(uri,data);
    }
    private void sendPut(String postUrl, String data) {
        out.println(data);
        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpPut put = new HttpPut(postUrl);

            put.setHeader("Content-Type","application/json");
            put.setEntity(new StringEntity(data));
            HttpResponse response = httpclient.execute(put);
            out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}