package Directory;

import Directory.Representations.*;
import Directory.Resources.Empresa;
import Directory.Resources.Licitacao;
import com.codahale.metrics.annotation.Timed;
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
        System.out.println(empresas);
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
    @Path("/empresas/{nome}/leiloes/{id}/{taxaMaxima}/{montanteTotal}/{data}/{sucesso}")
    public Response postLeilao(
            @PathParam("nome") String nome,
            @PathParam("id") int id,
            @PathParam("taxaMaxima") int taxaMaxima,
            @PathParam("montanteTotal") int montanteTotal,
            @PathParam("data") long data,
            @PathParam("sucesso") boolean sucesso
                        )
    {

        LocalDateTime date =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(data), ZoneId.systemDefault());
        empresas.get(nome).addLeilao(id,taxaMaxima,montanteTotal,nome, date,sucesso);
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
    @Path("/empresas/{nome}/emissoes/{id}/{taxa}/{montanteTotal}/{sucesso}/")
    public Response put(
            @PathParam("nome") String nome,
            @PathParam("id") int id,
            @PathParam("taxa") int taxa,
            @PathParam("montanteTotal") int montanteTotal,
            @PathParam("sucesso") boolean sucesso){

        empresas.get(nome).addEmissao(id,taxa,montanteTotal,nome,sucesso,null);

        return Response.status(201).build();
    }

    @POST
    @Path("/empresas/{nome}/emissoes/{idL}/{id}/{investidor}⁄{taxa}⁄{quantia}")
    public Response postEmissaoLicitacao(
            @PathParam("nome") String nome,
            @PathParam("idL") int idL,
            @PathParam("id") int id,
            @PathParam("investidor") String investidor,
            @PathParam("taxa") float taxa,
            @PathParam("quantia") int quantia
    )
    {
        empresas.get(nome).historicoEmissoes.get(idL).licitacoes.add(
                new Licitacao(id,investidor,taxa,quantia));
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
        System.out.println(empresas.get(nome).getLeilao(idL));
        System.out.println(empresas.get(nome).getLeilao(idL).getLicitacao(id));
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
