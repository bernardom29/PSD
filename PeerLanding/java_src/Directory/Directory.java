package Directory;

import Directory.Representations.*;

import Directory.Resources.Empresa;
import Directory.Resources.RestRequest;
import Directory.Resources.Emissao;
import Directory.Resources.Leilao;
import Directory.Resources.Licitacao;

import com.codahale.metrics.annotation.Timed;


import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Directory {
    private ConcurrentHashMap<String, Empresa> empresas;

    //TODO Utilizar QueryParam para fazer parsing dos argumentos
    //TODO Alterar pedidos do lado das Exchanges para passar os argumentos direito
    public Directory() {
        this.empresas = new ConcurrentHashMap<>();
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
    public Response getEmps() {
        List<EmpresaRep> lista = new ArrayList<>();
        for ( Map.Entry<String, Empresa> obj : empresas.entrySet()) {
            lista.add(new EmpresaRep(obj.getValue()));
        }
        return Response.ok().entity(lista).build();
    }

    @GET
    @Path("/empresa")
    public Response getEmp(@QueryParam("nome") String nome){

        if (nome == null) {
            return Response.status(404).build();
        }

        return Response.ok().entity(new EmpresaRep(nome, this.empresas.get(nome))).build();
    }

    @GET
    @Path("/empresa/leiloes")
    public Response getLeiloes(
            @QueryParam("nome") String nome)
    {
        if (nome == null){
           return Response.status(404).build();
        }
        return Response.ok().entity(new LeiloesRep(empresas.get(nome).getLeiloes())).build();
    }

    @GET
    @Path("/empresa/leilao/")
    @Timed
    public Response getLeilao(
            @QueryParam("nome") String nome,
            @QueryParam("id") Integer id)
    {
        if (nome == null || id >= empresas.get(nome).historicoLeiloes.size()){
            return Response.status(404).build();
        }
        Object obj = new LeilaoRep(empresas.get(nome).getLeilao(id.intValue()));
        return Response.ok().entity(obj).build();
    }



    @GET
    @Path("/empresa/emissoes")
    @Timed
    public Response getEmissoes(@QueryParam("nome") String nome){
        if (nome == null){
            return Response.status(404).build();
        }
        return Response.ok().entity(new EmissoesRep(empresas.get(nome).getEmissoes())).build();
    }



    @GET
    @Path("/empresa/emissao/")
    @Timed
    public Response getEmissao(
            @QueryParam("nome") String nome,
            @QueryParam("id") int id){
        if (nome == null || id >= empresas.get(nome).historicoEmissoes.size()){
            return Response.status(404).build();
        }
        Object obj = new EmissaoRep(empresas.get(nome).getEmissao(id));
        return Response.ok().entity(obj).build();
    }



    @GET
    @Path("/empresa/leilao/licitacoes")
    @Timed
    public Response getLicitacoesLeilao(
            @QueryParam("nome") String nome,
            @QueryParam("id") int id){
        if (nome == null || id >= empresas.get(nome).historicoLeiloes.size()){
            return Response.status(404).build();
        }
        Object obj = empresas.get(nome).getLeiloes().get(id).licitacoes;
        return Response.ok().entity(obj).build();
    }

    @GET
    @Path("/empresa/emissao/licitacoes/")
    @Timed
    public Response getLicitacoesEmissao(
            @QueryParam("nome") String nome,
            @QueryParam("id") int id){
        if (nome == null || id >= empresas.get(nome).historicoEmissoes.size()){
            return Response.status(404).build();
        }
        Object obj = empresas.get(nome).getEmissoes().get(id).licitacoes;
        return Response.ok().entity(obj).build();
    }



    @GET
    @Path("/empresa/leilao/licitacao/")
    @Timed
    public Response getLicitacaoLeilao(
            @QueryParam("nome") String nome,
            @QueryParam("idL")  int idL,
            @QueryParam("id")   int id){
        if (nome == null
                || idL >= empresas.get(nome).historicoLeiloes.size()
                || id  >=empresas.get(nome).historicoLeiloes.get(idL).licitacoes.size()){
            return Response.status(404).build();
        }
        Object obj = empresas.get(nome).getLeiloes().get(idL).licitacoes.get(id);
        return Response.ok().entity(obj).build();
    }

    @GET
    @Path("/empresa/emissao/licitacao")
    @Timed
    public Response getLicitacaoEmissao(
            @QueryParam("nome") String nome,
            @QueryParam("idE") int idE,
            @QueryParam("id") int id){
        if (nome == null
                || idE >= empresas.get(nome).historicoEmissoes.size()
                || id >= empresas.get(nome).historicoEmissoes.get(idE).licitacoes.size()){
            return Response.status(404).build();
        }
        Object obj = empresas.get(nome).getEmissoes().get(idE).licitacoes.get(id);
        return Response.ok().entity(obj).build();
    }


















    @POST
    @Path("/empresa/leilao/")
    public Response postLeilao(
            @NotNull RestRequest restRequest
    ){
        System.out.println(restRequest);
        LocalDateTime date = LocalDateTime.now();
        empresas.get(restRequest.getNome()).addLeilao(restRequest.getId(),restRequest.getJuro(),restRequest.getMontanteTotal(), date,false);
        return Response.status(201).build();
    }

    @POST
    @Path("/empresa/leilao/licitacao")
    public Response postLeilaoLicitacao(
            @NotNull RestRequest restRequest
    )
    {
        empresas.get(restRequest.getNome()).historicoLeiloes.get(restRequest.getId()).licitacoes.add(
                new Licitacao(restRequest.getIdL(),restRequest.getInvestidor(),restRequest.getTaxaMaxima(), restRequest.getQuantia()));
        return Response.status(201).build();
    }

    @PUT
    @Path("/empresa/leilao")
    public Response putLeilaoSucesso(
            @NotNull RestRequest restRequest
            ){
        Leilao leilao = empresas.get(restRequest.getNome()).getLeilao(restRequest.getId());
        leilao.sucesso = restRequest.isSucesso();
        leilao.ativo = restRequest.isAtivo();
        return Response.status(201).build();
    }










    @POST
    @Path("/empresa/emissao")
    public Response post(
            @NotNull RestRequest restRequest
            ){

        empresas.get(restRequest.getNome()).addEmissao(restRequest.getId(), restRequest.getTaxaMaxima(), restRequest.getMontanteTotal(),false,new ArrayList<>());

        return Response.status(201).build();
    }

    @POST
    @Path("/empresa/emissao/licitacao")
    public Response postEmissaoLicitacao(
            @NotNull RestRequest restRequest
    )
    {
        empresas.get(restRequest.getNome()).historicoEmissoes.get(restRequest.getId()).licitacoes.add(
                new Licitacao(restRequest.getIdL(),restRequest.getInvestidor(),restRequest.getQuantia()));
        return Response.status(201).build();
    }


    @PUT
    @Path("/empresa/emissao")
    public Response putEmissaoSucesso(
            @NotNull RestRequest restRequest
    ){
        Emissao emissao = empresas.get(restRequest.getNome()).getEmissao(restRequest.getIdL());
        emissao.sucesso = restRequest.isSucesso();
        emissao.ativo= restRequest.isAtivo();
        return Response.status(201).build();
    }


}

