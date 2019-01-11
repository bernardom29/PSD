package Directory;

import Directory.Representations.*;
import Directory.Resources.*;
import com.fasterxml.jackson.core.JsonProcessingException;

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

    @GET
    @Path("/empresa/{nome}")
    public EmpresaRep getEmp(@PathParam("nome") String nomeEmp){
        return new EmpresaRep(nomeEmp);
    }

    @GET
    @Path("/empresa/{nome}/leiloes")
    public LeiloesRep getLeiloes(@PathParam("nome") String nome){
        return new LeiloesRep(empresas.get(nome).getLeiloes());
    }

    @GET
    @Path("/empresa/{nome}/leilao/{id}")
    public LeilaoRep getLeilao(@PathParam("nome") String nome, @PathParam("id") int id){
        return new LeilaoRep(empresas.get(nome).getLeilao(id));
    }

    @POST
    @Path("/empresa/{nome}/leilao/{id}/{taxaMaxima}/{montanteTotal}/{data}/{sucesso}/{licitacoes}")
    public Response put(@PathParam("nome") String nome, @PathParam("id") int id, @PathParam("taxaMaxima") int taxaMaxima,
                        @PathParam("montanteTotal") int montanteTotal, @PathParam("data") Date data, @PathParam("sucesso") boolean sucesso,
                        @PathParam("licitacoes") List<Licitacao> licitacoes){

        empresas.get(nome).addLeilao(id,taxaMaxima,montanteTotal,nome,data,sucesso,licitacoes);

        return Response.status(201).build();
    }

    @GET
    @Path("/empresa/{nome}/emissoes")
    public EmissoesRep getEmissoes(@PathParam("nome") String nome){
        return new EmissoesRep(empresas.get(nome).getEmissoes());
    }

    @GET
    @Path("/empresa/{nome}/emissao/{id}")
    public EmissaoRep getEmissao(@PathParam("nome") String nome, @PathParam("id") int id){
        return new EmissaoRep(empresas.get(nome).getEmissao(id));
    }

    @POST
    @Path("/empresa/{nome}/emissao/{id}/{taxa}/{montanteTotal}/{sucesso}/{licitacoes}")
    public Response put(@PathParam("nome") String nome, @PathParam("id") int id, @PathParam("taxa") int taxa,
                        @PathParam("montanteTotal") int montanteTotal, @PathParam("sucesso") boolean sucesso,
                        @PathParam("licitacoes") List<Licitacao> licitacoes){

        empresas.get(nome).addEmissao(id,taxa,montanteTotal,nome,sucesso,licitacoes);

        return Response.status(201).build();
    }

    @GET
    @Path("/empresa/{nome}/leilao/{id}/licitacoes")
    public LicitacoesRep getLicitacoesLeilao(@PathParam("nome") String nome, @PathParam("id") int id){
        return new LicitacoesRep(empresas.get(nome).getLeilao(id).getLicitacoes());
    }

    @GET
    @Path("/empresa/{nome}/emissao/{id}/licitacoes")
    public LicitacoesRep getLicitacoesEmissao(@PathParam("nome") String nome, @PathParam("id") int id){
        return new LicitacoesRep(empresas.get(nome).getEmissao(id).getLicitacoes());
    }

    @GET
    @Path("/empresa/{nome}/leilao/{idL}/licitacao/{id}")
    public LicitacaoRep getLicitacaoLeilao(@PathParam("nome") String nome, @PathParam("idL") int idL, @PathParam("id") int id){
        return new LicitacaoRep(empresas.get(nome).getLeilao(idL).getLicitacao(id));
    }

    @GET
    @Path("/empresa/{nome}/emissao/{idE}/licitacao/{id}")
    public LicitacaoRep getLicitacaoEmissao(@PathParam("nome") String nome, @PathParam("idE") int idE, @PathParam("id") int id){
        return new LicitacaoRep(empresas.get(nome).getEmissao(idE).getLicitacao(id));
    }

    public static void main(String[] args) throws JsonProcessingException {

    }
}
