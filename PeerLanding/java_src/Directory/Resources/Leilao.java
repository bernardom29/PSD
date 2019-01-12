package Directory.Resources;

import java.util.Date;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

public class Leilao {
    public int id;
    public int taxaMaxima;
    public int montanteTotal;
    public String empresa;
    public Date data;
    public boolean sucesso;
    public List<Licitacao> licitacoes;

    public Leilao(){
        this.id = 0;
        this.taxaMaxima = 0;
        this.montanteTotal = 0;
        this.empresa = null;
        this.data = null;
        this.sucesso = false;
        this.licitacoes = null;
    }

    public Leilao(int id, int taxaMaxima, int montanteTotal, String empresa, Date data, boolean sucesso, List<Licitacao> licitacoes){
        this.id = id;
        this.taxaMaxima = taxaMaxima;
        this.montanteTotal = montanteTotal;
        this.empresa = empresa;
        this.data = data;
        this.sucesso = sucesso;
        this.licitacoes = licitacoes;
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
                .append("taxaMaxima: ")
                .append(this.taxaMaxima).append("\n")
                .append("montanteTotal: ")
                .append(this.montanteTotal).append("\n")
                .append("empresa: ")
                .append(this.empresa).append("\n")
                .append("data: ")
                .append(this.data).append("\n")
                .append("sucesso: ")
                .append(this.sucesso).append("\n");
        for(Licitacao licitacao : this.licitacoes){
            sb.append(licitacao.toString());
        }
        return sb.toString();
    }
}