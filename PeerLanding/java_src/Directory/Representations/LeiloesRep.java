package Directory.Representations;

import Directory.Resources.Leilao;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LeiloesRep{
    public List<Leilao> leiloes;

    @JsonCreator
    public LeiloesRep(@JsonProperty("leiloes") List<Leilao> leiloes){
        this.leiloes = leiloes;
    }

    public List<Leilao> getLeiloes() {
        return leiloes;
    }

    public void setLeiloes(List<Leilao> leiloes) {
        this.leiloes = leiloes;
    }
}