package Directory.Representations;

import Directory.Resources.Licitacao;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LicitacoesRep {
    private List<Licitacao> licitacoes;

    @JsonCreator
    public LicitacoesRep(@JsonProperty("licitacoes") List<Licitacao> licitacoes){
        this.licitacoes = licitacoes;
    }
}
