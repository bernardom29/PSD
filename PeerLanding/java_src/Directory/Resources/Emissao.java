package Directory.Resources;


import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

public class Emissao{
    int id;
    int taxa;
    int montanteTotal;
    String empresa;
    boolean sucesso;
    List<Licitacao> licitacoes;

    public Emissao(){
        this.id = 0;
        this.taxa = 0;
        this.montanteTotal = 0;
        this.empresa = null;
        this.sucesso = false;
        this.licitacoes = null;
    }

    public Emissao(int id, int taxaMaxima, int montanteTotal, String empresa, boolean sucesso, List<Licitacao> licitacoes){
        this.id = id;
        this.taxa = taxa;
        this.montanteTotal = montanteTotal;
        this.empresa = empresa;
        this.sucesso = sucesso;
        this.licitacoes = licitacoes;
    }
}