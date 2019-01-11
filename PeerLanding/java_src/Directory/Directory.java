package Directory;
import Exchange.Emissao;
import Exchange.Empresa;
import Exchange.Leilao;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;


public class Directory {
    HashMap<String, Empresa> empresas;


    public Directory() {
        this.empresas = new HashMap<String, Empresa>();
    }

    /*public Pear_lending(HashMap<String,Empresa> empresas){
        this.empresas = empresas;
    }*/

    @GET
    @Path("/empresas")
    public List<Empresa> getEmps() {
        return new EmpresasRep();
    }


    @GET
    @Path("/empresa/{name}")
    public Empresa getEmp(@PathParam("name") String nameEmp){
        return new EmpresaRep(nameEmp);
    }

    @POST
    @Path("/empresa/{name}")
    public Response addEmp(@PathParam("name") String nameEmp){
        Empresa emp = new Empresa(nameEmp);
        return Response.ok().build();
    }

    @GET
    @Path("/empresa/{name}/leiloes")
    public List<Leilao> getLeiloes(@PathParam("name") String name){
        return new LeiloesRep(empresas.getLeiloes());
    }

    @GET
    @Path("/empresa/{name}/leilao/{id}")
    public Leilao getLeilao(@PathParam("name") String name, @PathParam("id") int id){
        return new LeilaoRep(empresas.getLeiloes().getLeilao())
    }

    @GET
    @Path("/empresa/{name}/emissoes")
    public List<Emissao> getEmissoes(@PathParam("name") String name){
        return new LeiloesRep(empresas.getEmissoes())
    }

}
