package Exchange;


import java.util.Vector;

public class Empresa {
    String nome;
    Vector<Leilao> historicoLeiloes;
    Vector<Emissao> historicoEmissoes;

    public Empresa(String nome) {
        this.nome = nome;
        historicoEmissoes = new Vector<Emissao>();
        historicoLeiloes = new Vector<Leilao>();
    }

    public float taxaEmissao(){
        int emissaoIdx = this.historicoEmissoes.size();
        Emissao emissao = this.historicoEmissoes.get(emissaoIdx);
        int leilaoIdx = this.historicoLeiloes.size();
        Emissao leilao = this.historicoEmissoes.get(leilaoIdx);
        float taxaMaxima = 0;
        if (leilao.data.after(emissao.data) && leilao.sucesso){
            taxaMaxima = leilao.taxa;
            return taxaMaxima;
        }
        else if (!leilao.data.after(emissao.data) && emissao.sucesso){
            taxaMaxima = emissao.licitacoes.stream()
                    .map(e -> e.taxa)
                    .reduce(Integer.MIN_VALUE, (a,b) -> Integer.max(a,b));
            return taxaMaxima;
        }
        else if (!leilao.data.after(emissao.data) && !emissao.sucesso){
            taxaMaxima = (float) (emissao.taxa * 1.10);
            return taxaMaxima;
        }
        return taxaMaxima;
    }
}
