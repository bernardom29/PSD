package Directory.Representations;

import Directory.Resources.Empresa;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;


public class EmpresaRep{
    public String nome;
    public List<LeilaoRep> leiloes;
    public List<EmissaoRep> emissoes;

    @JsonCreator
    public EmpresaRep (@JsonProperty("nome") String nome, Empresa empresa) {
        this.nome = nome;
        this.leiloes = new ArrayList<>();
        this.emissoes = new ArrayList<>();
    }
}
