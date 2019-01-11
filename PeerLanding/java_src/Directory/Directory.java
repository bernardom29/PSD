package Directory;

import Directory.Representations.*;
import Directory.Resources.*;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class Directory {
    HashMap<String, Empresa> empresas;


    public Directory() {
        this.empresas = new HashMap<String, Empresa>();
    }

    public Directory(HashMap<String,Empresa> empresas){
        this.empresas = empresas;
    }

    @GET
    @Path("/empresas")
    public EmpresasRep getEmps() {
        List<String> nomes = null;

        for ( String key : empresas.keySet() ) {
            nomes.add(key);
        }

        return new EmpresasRep(nomes);
    }
/*
    @GET
    @Path("/empresa/{name}")
    public EmpresaRep getEmp(@PathParam("name") String nameEmp){
        return new EmpresaRep(nameEmp);
    }


    @GET
    @Path("/empresa/{name}/leiloes")
    public LeiloesRep getLeiloes(@PathParam("name") String name){
        return new LeiloesRep(empresas.get(name).getLeiloes());
    }

    @GET
    @Path("/empresa/{name}/leilao/{id}")
    public LeilaoRep getLeilao(@PathParam("name") String name, @PathParam("id") int id){
        return new LeilaoRep(empresas.get(name).getLeilao(id));
    }*/

    @POST
    @Path("/empresa/{name}/leilao/{id}/{taxaMaxima}/{montanteTotal}/{data}/{sucesso}/{licitacoes}")
    public Response put(@PathParam("name") String name, @PathParam("id") int id, @PathParam("taxaMaxima") int taxaMaxima,
                        @PathParam("montanteTotal") int montanteTotal, @PathParam("data") Date data, @PathParam("sucesso") boolean sucesso,
                        @PathParam("licitacoes") List<Licitacao> licitacoes){

        empresas.get(name).addLeilao(id,taxaMaxima,montanteTotal,name,data,sucesso,licitacoes);

        return Response.status(201).build();
    }/*

    @GET
    @Path("/empresa/{name}/emissoes")
    public LeiloesRep getEmissoes(@PathParam("name") String name){
        return new LeiloesRep(empresas.get(name).getEmissoes());
    }

    @GET
    @Path("/empresa/{name}/emissao/{id}")
    public EmissaoRep getEmissao(@PathParam("name") String name, @PathParam("id") int id){
        return new EmissaoRep(empresas.get(name).getEmissao(id));
    }*/

    @POST
    @Path("/empresa/{name}/emissao/{id}/{taxa}/{montanteTotal}/{sucesso}/{licitacoes}")
    public Response put(@PathParam("name") String name, @PathParam("id") int id, @PathParam("taxa") int taxa,
                        @PathParam("montanteTotal") int montanteTotal, @PathParam("sucesso") boolean sucesso,
                        @PathParam("licitacoes") List<Licitacao> licitacoes){

        empresas.get(name).addEmissao(id,taxa,montanteTotal,name,sucesso,licitacoes);

        return Response.status(201).build();
    }
}
