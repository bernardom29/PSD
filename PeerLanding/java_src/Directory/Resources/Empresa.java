package Directory.Resources;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Empresa {
    public String nome;
    public List<Leilao> historicoLeiloes;
    public List<Emissao> historicoEmissoes;

    public Empresa(String nome){
        this.nome = nome;
        this.historicoLeiloes = new ArrayList<>();
        this.historicoEmissoes = new ArrayList<>();
    }

    public Empresa(String nome, List<Leilao> leiloes, List<Emissao> emissoes){
        this.nome = nome;
        this.historicoLeiloes = leiloes;
        this.historicoEmissoes = emissoes;
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
        Leilao leilao = new Leilao(id,taxaMaxima,montanteTotal,this.nome,data,sucesso);

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