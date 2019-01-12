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
        int leilaoIdx = this.historicoLeiloes.size();


        Emissao emissao = null;
        emissao = this.historicoEmissoes.get(emissaoIdx);

        Leilao leilao = null;
        leilao = this.historicoLeiloes.get(leilaoIdx);
        float taxaMaxima = 0;
        if (leilao.getData().after(emissao.getData()) && leilao.isSucesso()){
            taxaMaxima = leilao.getTaxaMaxima();
            return taxaMaxima;
        }
        else if (!leilao.getData().after(emissao.getData()) && emissao.isSucesso()){
            taxaMaxima = emissao.getLicitacoes().stream()
                    .map(e -> e.taxa)
                    .reduce(Integer.MIN_VALUE, Integer::max);
            return taxaMaxima;
        }
        else if (!leilao.getData().after(emissao.getData()) && !emissao.isSucesso()){
            taxaMaxima = (float) (emissao.getTaxa() * 1.10);
            return taxaMaxima;
        }
        return taxaMaxima;
    }
}
