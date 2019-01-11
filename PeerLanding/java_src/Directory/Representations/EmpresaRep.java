package Directory.Representations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class EmpresaRep{
    private String nome;

    @JsonCreator
    public EmpresaRep (@JsonProperty("nome") String nome) {
        this.nome = nome;
    }
}
