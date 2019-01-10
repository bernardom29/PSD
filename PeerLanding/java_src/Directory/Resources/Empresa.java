import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

public class Empresa {
    String nome;
    List<Leilao> historicoLeiloes;
    List<Emissao> historicoEmissoes;

    public Empresa(){
        String nome = null;
        this.historicoLeiloes = new ArrayList<Leilao>();
        this.historicoEmissoes = new ArrayList<Emissao>();
    }

    public Empresa(String nome){
        this.nome = nome;
        this.historicoLeiloes = new ArrayList<Leilao>();
        this.historicoEmissoes = new ArrayList<Emissao>();
    }

    public List<Leilao> getLeiloes(){
        return this.historicoLeiloes;
    }

    public List<Emissao> getEmissoes(){
        return this.historicoEmissoes;
    }
}