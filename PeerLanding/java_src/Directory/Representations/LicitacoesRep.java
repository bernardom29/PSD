package Directory.Representations;

import Directory.Resources.Licitacao;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LicitacoesRep {
    public List<Licitacao> licitacoes;

    @JsonCreator
    public LicitacoesRep(@JsonProperty("licitacoes") List<Licitacao> licitacoes){
        this.licitacoes = licitacoes;
    }

    public List<Licitacao> getLicitacoes() {
        return licitacoes;
    }

    public void setLicitacoes(List<Licitacao> licitacoes) {
        this.licitacoes = licitacoes;
    }
}
