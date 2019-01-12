package Exchange;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Leilao {
    int taxaMaxima;
    int montanteTotal;
    String empresa;
    Date data;
    boolean sucesso;
    List<Licitacao> licitacoes;

    public Leilao(int taxaMaxima, int montanteTotal, String empresa, boolean sucesso) {
        this.taxaMaxima = taxaMaxima;
        this.montanteTotal = montanteTotal;
        this.empresa = empresa;
        this.data = new Date();
        this.sucesso = sucesso;
        this.licitacoes = new ArrayList<Licitacao>();
    }
}
