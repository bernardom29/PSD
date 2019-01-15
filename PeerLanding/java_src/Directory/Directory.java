package Directory;

import Directory.Representations.*;
import Directory.Resources.Empresa;
import Directory.Resources.Licitacao;
import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Path("/")
public class Directory {
    ConcurrentHashMap<String, Empresa> empresas;


    public Directory() {
        this.empresas = new ConcurrentHashMap<String, Empresa>();
        this.empresas.put("CanecaLda", new Empresa("CanecaLda"));
        this.empresas.put("SapatoLda", new Empresa("SapatoLda"));
        this.empresas.put("IsqueiroLda", new Empresa("IsqueiroLda"));
        this.empresas.put("MesasLda", new Empresa("MesasLda"));
        this.empresas.put("AguaLda", new Empresa("AguaLda"));
        this.empresas.put("VinhoLda", new Empresa("VinhoLda"));
        this.empresas.put("SandesLda", new Empresa("SandesLda"));
        this.empresas.put("OreoLda", new Empresa("OreoLda"));
        this.empresas.put("MongoLda", new Empresa("MongoLda"));
        this.empresas.put("RelogioLda", new Empresa("RelogioLda"));

    }

    public Directory(ConcurrentHashMap<String,Empresa> empresas){
        this.empresas = empresas;
    }

    @GET
    @Path("/empresas")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public EmpresasRep getEmps() {
        List<String> nomes = new ArrayList<>();
        System.out.println(empresas);
        for ( String key : empresas.keySet() ) {
            nomes.add(key);
        }
        return new EmpresasRep(nomes);
    }

    @GET
    @Path("/empresa/{nome}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public EmpresaRep getEmp(@PathParam("nome") String nome){
        return new EmpresaRep(nome, this.empresas.get(nome));
    }

    @GET
    @Path("/empresa/{nome}/leiloes")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public LeiloesRep getLeiloes(@PathParam("nome") String nome){
        return new LeiloesRep(empresas.get(nome).getLeiloes());
    }

    @GET
    @Path("/empresa/{nome}/leilao/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public LeilaoRep getLeilao(@PathParam("nome") String nome, @PathParam("id") int id){
        return new LeilaoRep(empresas.get(nome).getLeilao(id));
    }

    @POST
    @Path("/empresa/{nome}/leilao/{id}/{taxaMaxima}/{montanteTotal}/{data}/{sucesso}/{licitacoes}")
    public Response put(@PathParam("nome") String nome, @PathParam("id") int id, @PathParam("taxaMaxima") int taxaMaxima,
                        @PathParam("montanteTotal") int montanteTotal, @PathParam("data") Date data, @PathParam("sucesso") boolean sucesso
                        )
    {

        //empresas.get(nome).addLeilao(id,taxaMaxima,montanteTotal,nome,data,sucesso,licitacoes);

        return Response.status(201).build();
    }

    @GET
    @Path("/empresa/{nome}/emissoes")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public EmissoesRep getEmissoes(@PathParam("nome") String nome){
        return new EmissoesRep(empresas.get(nome).getEmissoes());
    }

    @GET
    @Path("/empresa/{nome}/emissao/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public EmissaoRep getEmissao(@PathParam("nome") String nome, @PathParam("id") int id){
        return new EmissaoRep(empresas.get(nome).getEmissao(id));
    }

    @POST
    @Path("/empresa/{nome}/emissao/{id}/{taxa}/{montanteTotal}/{sucesso}/")
    public Response put(@PathParam("nome") String nome, @PathParam("id") int id, @PathParam("taxa") int taxa,
                        @PathParam("montanteTotal") int montanteTotal, @PathParam("sucesso") boolean sucesso){

        empresas.get(nome).addEmissao(id,taxa,montanteTotal,nome,sucesso,null);

        return Response.status(201).build();
    }

    @GET
    @Path("/empresa/{nome}/leilao/{id}/licitacoes")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public LicitacoesRep getLicitacoesLeilao(@PathParam("nome") String nome, @PathParam("id") int id){
        return new LicitacoesRep(empresas.get(nome).getLeilao(id).getLicitacoes());
    }

    @GET
    @Path("/empresa/{nome}/emissao/{id}/licitacoes")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public LicitacoesRep getLicitacoesEmissao(@PathParam("nome") String nome, @PathParam("id") int id){
        return new LicitacoesRep(empresas.get(nome).getEmissao(id).getLicitacoes());
    }

    @GET
    @Path("/empresa/{nome}/leilao/{idL}/licitacao/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public LicitacaoRep getLicitacaoLeilao(@PathParam("nome") String nome, @PathParam("idL") int idL, @PathParam("id") int id){
        return new LicitacaoRep(empresas.get(nome).getLeilao(idL).getLicitacao(id));
    }

    @GET
    @Path("/empresa/{nome}/emissao/{idE}/licitacao/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public LicitacaoRep getLicitacaoEmissao(@PathParam("nome") String nome, @PathParam("idE") int idE, @PathParam("id") int id){
        return new LicitacaoRep(empresas.get(nome).getEmissao(idE).getLicitacao(id));
    }
    /*
    public static void main(String[] args) throws JsonProcessingException {
        Licitacao lic1 = new Licitacao(1,"psa",20,500);
        Licitacao lic2 = new Licitacao(2,"juan",30,100);
        List<Licitacao> lics = new ArrayList<>();
        Licitacao lic3 = new Licitacao(1,"m",30,200);
        List<Licitacao> lics2 = new ArrayList<>();
        lics.add(lic1);
        lics.add(lic2);
        lics2.add(lic3);
        Emissao em1 = new Emissao(1,30,540,"naosei",true,lics);
        Emissao em2 = new Emissao(2,43,520,"naosei",true,lics2);
        List<Emissao> ems = new ArrayList<>();
        ems.add(em1);
        ems.add(em2);
        List<Leilao> lls = new ArrayList<>();

        Empresa emp = new Empresa("naosei",lls,ems);

        HashMap<String, Empresa> empresas = new HashMap<>();

        empresas.put("naosei", emp);


        ObjectMapper objectMapper = new ObjectMapper();
        String value = objectMapper.writeValueAsString(empresas);

        System.out.print(value);
    }*/
}
