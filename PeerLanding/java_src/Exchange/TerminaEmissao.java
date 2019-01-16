package Exchange;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.zeromq.ZMQ;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

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
        synchronized (this.pub){
            pub.send("emissao-"+this.empresa+"-Terminado");
        }

        //Update Diretorio
        HttpClient httpclient = HttpClients.createDefault();
        String uri = "http://localhost/empresas/" + empresa + "/emissoes/" + idL + "/" + true + "/" + false;
        HttpPost httppost= new HttpPost(uri);
        //TODO Isto está mal?
        Exchange.sendPost(httpclient,httppost);
    }
}