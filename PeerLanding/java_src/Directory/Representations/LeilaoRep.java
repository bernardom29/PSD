package Directory.Representations;

import Directory.Resources.Leilao;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public class LeilaoRep {
    public Leilao leilao;

    @JsonCreator
    public LeilaoRep(@JsonProperty("leilao") Leilao leilao){
        this.leilao = leilao;
    }

    public Leilao getLeilao() {
        return leilao;
    }

    public void setLeilao(Leilao leilao) {
        this.leilao = leilao;
    }
}
