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

    public class(){
        this.id = 0;
        this.taxa = 0;
        this.montanteTotal = 0;
        this.empressa = null;
        this.sucesso = false;
        this.licitacoes = null;
    }
}