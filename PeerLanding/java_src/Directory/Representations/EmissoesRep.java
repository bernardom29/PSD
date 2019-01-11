package Directory.Representations;

import Directory.Resources.Emissao;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class EmissoesRep{
    private List<Emissao> emissoes;

    @JsonCreator
    public EmissoesRep(@JsonProperty("emissoes") List<Emissao> emissoes){
        this.emissoes = emissoes;
    }
}