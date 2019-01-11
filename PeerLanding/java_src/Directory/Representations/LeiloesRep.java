package Directory.Representations;

import Directory.Resources.Leilao;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LeiloesRep{
    private List<Leilao> leiloes;

    @JsonCreator
    public LeiloesRep(@JsonProperty("leiloes") List<Leilao> leiloes){
        this.leiloes = leiloes;
    }
}