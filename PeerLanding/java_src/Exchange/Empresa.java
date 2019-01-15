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
        Emissao emissao = this.historicoEmissoes.lastElement();
        Leilao leilao = this.historicoLeiloes.lastElement();
        if(leilao!=null && emissao!=null) {
            if (leilao.getData().isAfter(emissao.getData()) && leilao.isSucesso()){
                return leilao.getTaxaLicitacaoMaxima();
            }
            else if (!leilao.getData().isAfter(emissao.getData()) && emissao.isSucesso()){
                return emissao.getTaxa();
            }
            else if (!leilao.getData().isAfter(emissao.getData()) && !emissao.isSucesso()){
                return (float) (emissao.getTaxa() * 1.10);
            }
        } else if (leilao != null) {
            if(leilao.isSucesso()) {
                return leilao.getTaxaLicitacaoMaxima();
            }
        }

        return 0;
    }
}
