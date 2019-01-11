package Directory.Representations;

import Directory.Resources.Emissao;
import Directory.Resources.Licitacao;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EmissaoRep {
    private Emissao emissao;

    @JsonCreator
    public EmissaoRep(@JsonProperty("emissao") Emissao emissao){
        this.emissao = emissao;
    }
}
