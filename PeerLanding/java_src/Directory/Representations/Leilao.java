package Directory.Representations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Leilao {
    @JsonProperty("id") public int id;
    @JsonProperty("taxaMaxima") public float taxaMaxima;
    @JsonProperty("montanteTotal") public int montanteTotal;
    @JsonProperty("empresa") public String empresa;
    @JsonProperty("data") public LocalDateTime data;
    @JsonProperty("sucesso") public boolean sucesso;
    @JsonProperty("ativo") public boolean ativo;
    @JsonProperty("licitacoes") public List<Licitacao> licitacoes;

    @JsonCreator
    public Leilao(@JsonProperty("id") int id,
            @JsonProperty("taxaMaxima") float taxaMaxima,
    @JsonProperty("montanteTotal") int montanteTotal,
    @JsonProperty("empresa") String empresa,
    @JsonProperty("data") LocalDateTime data,
    @JsonProperty("sucesso") boolean sucesso,
    @JsonProperty("ativo") boolean ativo,
    @JsonProperty("licitacoes") List<Licitacao> licitacoes) {
        this.id = id;
        this.taxaMaxima = taxaMaxima;
        this.montanteTotal = montanteTotal;
        this.empresa = empresa;
        this.data = data;
        this.sucesso = sucesso;
        this.ativo=ativo;
        this.licitacoes = licitacoes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getTaxaMaxima() {
        return taxaMaxima;
    }

    public void setTaxaMaxima(float taxaMaxima) {
        this.taxaMaxima = taxaMaxima;
    }

    public int getMontanteTotal() {
        return montanteTotal;
    }

    public void setMontanteTotal(int montanteTotal) {
        this.montanteTotal = montanteTotal;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public void setSucesso(boolean sucesso) {
        this.sucesso = sucesso;
    }

    public List<Licitacao> getLicitacoes() {
        return licitacoes;
    }

    public void setLicitacoes(List<Licitacao> licitacoes) {
        this.licitacoes = licitacoes;
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
                .append(DateTimeFormatter.ofPattern("ss:mm:HH dd-MM-yyyy").format(this.data)).append("\n")
                .append("sucesso: ")
                .append(this.sucesso).append("\n")
                .append("ativo: ")
                .append(this.ativo).append("\n");
        for(Licitacao licitacao : this.licitacoes){
            sb.append(licitacao.toString());
        }
        return sb.toString();
    }

    public Licitacao getLicitacao(int idLic) {
        return licitacoes.get(idLic);
    }
}