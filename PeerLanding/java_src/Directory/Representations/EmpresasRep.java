package Directory.Representations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class EmpresasRep{
    public List<String> empresas;

    @JsonCreator
    public EmpresasRep (@JsonProperty("empresas") List<String> empresas) {
        this.empresas = empresas;
    }

    public List<String> getEmpresas() {
        return empresas;
    }

    public void setEmpresas(List<String> empresas) {
        this.empresas = empresas;
    }
}