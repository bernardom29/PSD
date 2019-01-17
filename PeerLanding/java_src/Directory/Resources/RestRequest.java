package Directory.Resources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RestRequest {
    @JsonProperty("nome") private final String nome;
    @JsonProperty("id") private final int id;
    @JsonProperty("taxaMaxima") private final float taxaMaxima;
    @JsonProperty("montanteTotal") private final int montanteTotal;
    @JsonProperty("idL") private final int idL;
    @JsonProperty("investidor") private final String investidor;
    @JsonProperty("juro") private final float juro;
    @JsonProperty("quantia") private final int quantia;
    @JsonProperty("sucesso") private final boolean sucesso;
    @JsonProperty("ativo") private final boolean ativo;

    @JsonCreator
    public RestRequest(
            @JsonProperty("nome") String nome,
            @JsonProperty("id") int id,
            @JsonProperty("taxaMaxima") float taxaMaxima,
            @JsonProperty("montanteTotal") int montanteTotal,
            @JsonProperty("idL") int idL,
            @JsonProperty("investidor") String investidor,
            @JsonProperty("juro") float juro,
            @JsonProperty("quantia") int quantia,
            @JsonProperty("sucesso") boolean sucesso,
            @JsonProperty("ativo") boolean ativo)
    {
        this.nome = nome;
        this.id = id;
        this.taxaMaxima = taxaMaxima;
        this.montanteTotal = montanteTotal;
        this.idL = idL;
        this.investidor = investidor;
        this.juro = juro;
        this.quantia = quantia;
        this.sucesso = sucesso;
        this.ativo = ativo;
    }

    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }

    public float getTaxaMaxima() {
        return taxaMaxima;
    }

    public int getMontanteTotal() {
        return montanteTotal;
    }

    public int getIdL() {
        return idL;
    }

    public String getInvestidor() {
        return investidor;
    }

    public float getJuro() {
        return juro;
    }

    public int getQuantia() {
        return quantia;
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public boolean isAtivo() {
        return ativo;
    }
}
