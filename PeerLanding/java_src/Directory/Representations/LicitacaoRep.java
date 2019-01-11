package Directory.Representations;

import Directory.Resources.Licitacao;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LicitacaoRep {
    private Licitacao licitacao;

    @JsonCreator
    public LicitacaoRep(@JsonProperty("licitacao") Licitacao licitacao){
        this.licitacao = licitacao;
    }
}
