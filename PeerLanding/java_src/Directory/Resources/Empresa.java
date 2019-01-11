package Directory.Resources;

import Directory.Resources.Emissao;
import Directory.Resources.Leilao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

public class Empresa {
    String nome;
    List<Leilao> historicoLeiloes;
    List<Emissao> historicoEmissoes;

    public Empresa(){
        String nome = null;
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

    public void addLeilao(int id, int taxaMaxima, int montanteTotal, String empresa, Date data, boolean sucesso, List<Licitacao> licitacoes){
        Leilao leilao = new Leilao(id,taxaMaxima,montanteTotal,this.nome,data,sucesso,licitacoes);

        historicoLeiloes.add(leilao);
    }

    public void addEmissao(int id, int taxa, int montanteTotal, String name, boolean sucesso, List<Licitacao> licitacoes) {
        Emissao emissao = new Emissao(id,taxa,montanteTotal,this.nome,sucesso,licitacoes);

        historicoEmissoes.add(emissao);
    }
}