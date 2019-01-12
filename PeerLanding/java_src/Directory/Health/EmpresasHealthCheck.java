package Directory.Health;

import Directory.Resources.Empresa;
import Directory.Resources.Historico;
import com.codahale.metrics.health.HealthCheck;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;

public class EmpresasHealthCheck extends HealthCheck{
    private HashMap<String, Empresa> empresas;

    public EmpresasHealthCheck(HashMap<String, Empresa> empresas) {
        this.empresas = empresas;
    }

    protected Result check() throws Exception {
        /*WebTarget webTarge = empresas.target("http://localhost:8080/empresas")
        Invocation.Builder invocationBuilder = webTarge.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();

        HashMap<String, Empresa> empresas = response.readEntity(Historico.class);

        if(emps != null && emps.size() > 0){
            return Result.healthy();
        }
        */
        return Result.unhealthy("API Failed");
    }
}
