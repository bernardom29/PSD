package Directory.Representations;

import Directory.Resources.Leilao;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public class LeilaoRep {
    private Leilao leilao;

    @JsonCreator
    public LeilaoRep(@JsonProperty("leilao") Leilao leilao){
        this.leilao = leilao;
    }
}
