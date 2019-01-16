package Directory.Resources;


import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

public class Emissao{
    public int id;
    public float taxa;
    public int montanteTotal;
    public String empresa;
    public boolean sucesso;
    public boolean ativo;
    public List<Licitacao> licitacoes;


    public Emissao(int id, float taxaMaxima, int montanteTotal, String empresa, boolean sucesso, List<Licitacao> licitacoes){
        this.id = id;
        this.taxa = taxaMaxima;
        this.montanteTotal = montanteTotal;
        this.empresa = empresa;
        this.sucesso = sucesso;
        this.licitacoes = licitacoes;
        this.ativo=true;
    }

    public List<Licitacao> getLicitacoes(){
        return this.licitacoes;
    }

    public Licitacao getLicitacao(int id){
        return this.licitacoes.get(id);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("id: ")
                .append(this.id).append("\n")
                .append("taxa:")
                .append(this.taxa).append("\n")
                .append("montanteTotal: ")
                .append(this.montanteTotal).append("\n")
                .append("empresa: ")
                .append(this.empresa).append("\n")
                .append("sucesso: ")
                .append(this.sucesso).append("\n")
                .append("ativo: ")
                .append(this.ativo).append("\n");
        for(Licitacao licitacao : this.licitacoes){
            sb.append(licitacao.toString());
        }
        return sb.toString();
    }
}