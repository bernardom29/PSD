package Directory.Representations;

import Directory.Resources.Empresa;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class EmpresaRep{
    public String nome;
    public List<LeilaoRep> leiloes;
    public List<EmissaoRep> emissoes;

    @JsonCreator
    public EmpresaRep (@JsonProperty("nome") String nome, Empresa empresa) {
        this.nome = nome;
        this.leiloes = empresa.historicoLeiloes.stream().map(e -> new LeilaoRep(e)).collect(Collectors.toList());
        this.emissoes = empresa.historicoEmissoes.stream().map(e -> new EmissaoRep(e)).collect(Collectors.toList());
    }

    @JsonCreator
    public EmpresaRep(Empresa value) {
        this.nome = value.nome;
        this.leiloes = value.historicoLeiloes.stream().map(e -> new LeilaoRep(e)).collect(Collectors.toList());
        this.emissoes = value.historicoEmissoes.stream().map(e -> new EmissaoRep(e)).collect(Collectors.toList());
    }
}
