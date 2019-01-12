package Directory.Representations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class EmpresaRep{
    public String nome;

    @JsonCreator
    public EmpresaRep (@JsonProperty("nome") String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
