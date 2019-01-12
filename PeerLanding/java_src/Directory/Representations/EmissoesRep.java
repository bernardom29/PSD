package Directory.Representations;

import Directory.Resources.Emissao;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class EmissoesRep{
    public List<Emissao> emissoes;

    @JsonCreator
    public EmissoesRep(@JsonProperty("emissoes") List<Emissao> emissoes){
        this.emissoes = emissoes;
    }

    public List<Emissao> getEmissoes() {
        return emissoes;
    }

    public void setEmissoes(List<Emissao> emissoes) {
        this.emissoes = emissoes;
    }
}