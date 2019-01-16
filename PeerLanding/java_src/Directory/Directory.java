package Directory;

import Directory.Representations.*;
import Directory.Resources.Emissao;
import Directory.Resources.Empresa;
import Directory.Resources.Leilao;
import Directory.Resources.Licitacao;
import com.codahale.metrics.annotation.Timed;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Path("/")
public class Directory {
    ConcurrentHashMap<String, Empresa> empresas;

    //TODO Utilizar QueryParam para fazer parsing dos argumentos
    //TODO Alterar pedidos do lado das Exchanges para passar os argumentos direito
    public Directory() {
        this.empresas = new ConcurrentHashMap<String, Empresa>();
        this.empresas.put("SapatoLda", new Empresa("SapatoLda"));
        this.empresas.put("IsqueiroLda", new Empresa("IsqueiroLda"));
        this.empresas.put("MesasLda", new Empresa("MesasLda"));
        this.empresas.put("AguaLda", new Empresa("AguaLda"));
        this.empresas.put("VinhoLda", new Empresa("VinhoLda"));
        this.empresas.put("SandesLda", new Empresa("SandesLda"));
        this.empresas.put("OreoLda", new Empresa("OreoLda"));
        this.empresas.put("MongoLda", new Empresa("MongoLda"));
        this.empresas.put("RelogioLda", new Empresa("RelogioLda"));
        this.empresas.put("CanecaLda", new Empresa("CanecaLda"));
    }

    public Directory(ConcurrentHashMap<String,Empresa> empresas){
        this.empresas = empresas;
    }

    @GET
    @Path("/empresas")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public List<EmpresaRep> getEmps() {
        List<EmpresaRep> lista = new ArrayList<>();
        for ( Map.Entry<String, Empresa> obj : empresas.entrySet()) {
            lista.add(new EmpresaRep(obj.getValue()));
        }
        return lista;
    }

    @GET
    @Path("/empresas/{nome}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public EmpresaRep getEmp(@PathParam("nome") String nome){
        return new EmpresaRep(nome, this.empresas.get(nome));
    }

    @GET
    @Path("/empresas/{nome}/leiloes")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public LeiloesRep getLeiloes(@PathParam("nome") String nome){
        return new LeiloesRep(empresas.get(nome).getLeiloes());
    }

    @GET
    @Path("/empresas/{nome}/leiloes/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public LeilaoRep getLeilao(
            @PathParam("nome") String nome,
            @PathParam("id") int id){
        return new LeilaoRep(empresas.get(nome).getLeilao(id));
    }

    @POST
    @Path("/empresas/{nome}/leiloes/{idL}/{id}/{investidor}⁄{taxa}⁄{quantia}")
    public Response postLeilaoLicitacao(
            @PathParam("nome") String nome,
            @PathParam("idL") int idL,
            @PathParam("id") int id,
            @PathParam("investidor") String investidor,
            @PathParam("taxa") float taxa,
            @PathParam("quantia") int quantia
    )
    {
        empresas.get(nome).historicoLeiloes.get(idL).licitacoes.add(
                new Licitacao(id,investidor,taxa,quantia));
        return Response.status(201).build();
    }

    @POST
    @Path("/empresas/{nome}/leiloes/{id}/{taxaMaxima}/{montanteTotal}")
    public Response postLeilao(
            @PathParam("nome") String nome,
            @PathParam("id") int id,
            @PathParam("taxaMaxima") float taxaMaxima,
            @PathParam("montanteTotal") int montanteTotal
    ){

        LocalDateTime date = LocalDateTime.now();
        empresas.get(nome).addLeilao(id,taxaMaxima,montanteTotal, date,false);
        return Response.status(201).build();
    }

    @PUT
    @Path("/empresas/{nome}/leiloes/{id}/{sucesso}/{ativo}")
    public Response putLeilaoSucesso(
            @PathParam("nome") String nome,
            @PathParam("id") int id,
            @PathParam("sucesso") boolean sucesso,
            @PathParam("ativo") boolean ativo
    ){
        Leilao leilao = empresas.get(nome).getLeilao(id);
        leilao.sucesso = sucesso;
        leilao.ativo=ativo;
        return Response.status(201).build();
    }

    @PUT
    @Path("/empresas/{nome}/emissoes/{id}/{sucesso}/{ativo}")
    public Response putEmissaoSucesso(
            @PathParam("nome") String nome,
            @PathParam("id") int id,
            @PathParam("sucesso") boolean sucesso,
            @PathParam("ativo") boolean ativo
    ){
        Emissao emissao = empresas.get(nome).getEmissao(id);
        emissao.sucesso = sucesso;
        emissao.ativo=ativo;
        return Response.status(201).build();
    }

    @PUT
    @Path("/empresas/{nome}/leiloes/{idL}/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putLicitadoresAlocados(
            @PathParam("nome") String nome,
            @PathParam("idL") int id
    ){
        return Response.status(201).build();
    }

    @GET
    @Path("/empresas/{nome}/emissoes")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public EmissoesRep getEmissoes(@PathParam("nome") String nome){
        return new EmissoesRep(empresas.get(nome).getEmissoes());
    }

    @GET
    @Path("/empresas/{nome}/emissoes/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public EmissaoRep getEmissao(
            @PathParam("nome") String nome,
            @PathParam("id") int id){
        return new EmissaoRep(empresas.get(nome).getEmissao(id));
    }

    @POST
    @Path("/empresas/{nome}/emissoes/{id}/{taxaMaxima}/{montanteTotal}/")
    public Response post(
            @PathParam("nome") String nome,
            @PathParam("id") int id,
            @PathParam("taxaMaxima") float taxaMaxima,
            @PathParam("montanteTotal") int montanteTotal
            ){

        empresas.get(nome).addEmissao(id, taxaMaxima,montanteTotal,false,new ArrayList<>());

        return Response.status(201).build();
    }

    @POST
    @Path("/empresas/{nome}/emissoes/{idL}/{id}/{investidor}⁄{quantia}")
    public Response postEmissaoLicitacao(
            @PathParam("nome") String nome,
            @PathParam("idL") int idL,
            @PathParam("id") int id,
            @PathParam("investidor") String investidor,
            @PathParam("quantia") int quantia
    )
    {
        empresas.get(nome).historicoEmissoes.get(idL).licitacoes.add(
                new Licitacao(id,investidor,quantia));
        return Response.status(201).build();
    }

    @GET
    @Path("/empresas/{nome}/leiloes/{id}/licitacoes")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public LicitacoesRep getLicitacoesLeilao(
            @PathParam("nome") String nome,
            @PathParam("id") int id){
        return new LicitacoesRep(empresas.get(nome).getLeilao(id).getLicitacoes());
    }

    @GET
    @Path("/empresas/{nome}/emissoes/{id}/licitacoes")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public LicitacoesRep getLicitacoesEmissao(
            @PathParam("nome") String nome,
            @PathParam("id") int id){
        return new LicitacoesRep(empresas.get(nome).getEmissao(id).getLicitacoes());
    }

    @GET
    @Path("/empresas/{nome}/leiloes/{idL}/licitacoes/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public LicitacaoRep getLicitacaoLeilao(
            @PathParam("nome") String nome,
            @PathParam("idL")  int idL,
            @PathParam("id")   int id){
        return new LicitacaoRep(empresas.get(nome).getLeilao(idL).getLicitacao(id));
    }

    @GET
    @Path("/empresas/{nome}/emissoes/{idE}/licitacoes/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public LicitacaoRep getLicitacaoEmissao(
            @PathParam("nome") String nome,
            @PathParam("idE") int idE,
            @PathParam("id") int id){
        return new LicitacaoRep(empresas.get(nome).getEmissao(idE).getLicitacao(id));
    }
}
