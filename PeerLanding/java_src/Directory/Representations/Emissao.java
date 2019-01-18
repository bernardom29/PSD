package Directory.Representations;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Emissao{
    @JsonProperty("id") public int id;
    @JsonProperty("taxa") public float taxa;
    @JsonProperty("montanteTotal") public int montanteTotal;
    @JsonProperty("empresa") public String empresa;
    @JsonProperty("sucesso") public boolean sucesso;
    @JsonProperty("ativo") public boolean ativo;
    @JsonProperty("licitacoes") public List<Licitacao> licitacoes;



    public Emissao(int id, float taxaMaxima, int montanteTotal, String empresa, boolean sucesso, List<Licitacao> licitacoes){
        this.id = id;
        this.taxa = taxaMaxima;
        this.montanteTotal = montanteTotal;
        this.empresa = empresa;
        this.sucesso = sucesso;
        this.licitacoes = licitacoes;
        this.ativo=true;
    }

    @JsonCreator
    public Emissao(
            @JsonProperty("id") int id,
            @JsonProperty("taxa") float taxa,
    @JsonProperty("montanteTotal") int montanteTotal,
    @JsonProperty("empresa") String empresa,
    @JsonProperty("sucesso") boolean sucesso,
    @JsonProperty("ativo") boolean ativo,
    @JsonProperty("licitacoes") List<Licitacao> licitacoes
    ) {
        this.id = id;
        this.taxa = taxa;
        this.montanteTotal = montanteTotal;
        this.empresa = empresa;
        this.sucesso = sucesso;
        this.ativo = ativo;
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

    public Emissao clone(){
        return new Emissao(this.id, this.taxa, this.montanteTotal, this.empresa,this.sucesso, this.ativo, this.licitacoes);
    }
}