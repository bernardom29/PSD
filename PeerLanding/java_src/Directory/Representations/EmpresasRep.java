package Directory.Representations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class EmpresasRep{
    private List<String> nomes = null;

    @JsonCreator
    public EmpresasRep (@JsonProperty("nomes") List<String> nomes) {
        this.nomes = nomes;
    }
}