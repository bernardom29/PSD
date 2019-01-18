package Directory.Representations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Licitacao {
    @JsonProperty("id") public int id;
    @JsonProperty("investidor") public String investidor;
    @JsonProperty("taxa") public float taxa;
    @JsonProperty("quantia") public int quantia;

    @JsonCreator
    public Licitacao(@JsonProperty("id") int id,
                     @JsonProperty("investidor") String investidor,
                     @JsonProperty("taxa") float taxa,
                     @JsonProperty("quantia") int quantia)
    {
        this.id = id;
        this.investidor = investidor;
        this.taxa = taxa;
        this.quantia = quantia;
    }

    public String toString(){
        String sb = "id: " +
                this.id + "\n" +
                "investidor: " +
                this.investidor + "\n" +
                "taxa: " +
                this.taxa + "\n" +
                "quantia: " +
                this.quantia + "\n";
        return sb;
    }
}
