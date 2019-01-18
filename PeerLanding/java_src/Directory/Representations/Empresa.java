package Directory.Representations;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Empresa {
    @JsonProperty("nome") public String nome;
    @JsonProperty("leiloes") public List<Leilao> historicoLeiloes;
    @JsonProperty("emissoes") public List<Emissao> historicoEmissoes;

    public Empresa(String nome){
        this.nome = nome;
        this.historicoLeiloes = new ArrayList<>();
        this.historicoEmissoes = new ArrayList<>();
    }

    @JsonCreator
    public Empresa(@JsonProperty("nome") String nome,
            @JsonProperty("leiloes") List<Leilao> historicoLeiloes,
    @JsonProperty("emissoes") List<Emissao> historicoEmissoes) {
        this.nome = nome;
        this.historicoLeiloes = historicoLeiloes;
        this.historicoEmissoes = historicoEmissoes;
    }

    public List<Leilao> getLeiloes(){
        return this.historicoLeiloes;
    }

    public List<Emissao> getEmissoes(){
        return this.historicoEmissoes;
    }

    public Leilao getLeilao(int id){
        return historicoLeiloes.get(id);
    }

    public Emissao getEmissao(int id){
        return historicoEmissoes.get(id);
    }

    public void addLeilao(int id, float taxaMaxima, int montanteTotal, LocalDateTime data, boolean sucesso){
        Leilao leilao = new Leilao(id,taxaMaxima,montanteTotal,this.nome,data,sucesso,true, new ArrayList<>());

        historicoLeiloes.add(leilao);
    }

    public void addEmissao(int id, float taxa, int montanteTotal, boolean sucesso, List<Licitacao> licitacoes) {
        Emissao emissao = new Emissao(id,taxa,montanteTotal,this.nome,sucesso,licitacoes);

        historicoEmissoes.add(emissao);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("nome: ")
                .append(this.nome).append("\n");
        for(Leilao leilao : this.historicoLeiloes){
            sb.append(leilao.toString());
        }
        sb.append("\n");
        for(Emissao emissao : this.historicoEmissoes){
            sb.append(emissao.toString());
        }
        return sb.toString();
    }
}