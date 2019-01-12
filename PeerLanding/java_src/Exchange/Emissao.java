package Exchange;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Emissao {
    float taxa;
    int montanteTotal;
    String empresa;
    boolean sucesso;
    Date data;
    List<Licitacao> licitacoes;

    public Emissao(float taxa, int montanteTotal, String empresa, boolean sucesso) {
        this.taxa = taxa;
        this.montanteTotal = montanteTotal;
        this.empresa = empresa;
        this.sucesso = sucesso;
        this.data = new Date();
        this.licitacoes = new ArrayList<Licitacao>();
    }
}
