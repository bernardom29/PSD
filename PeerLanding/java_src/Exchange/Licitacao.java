package Exchange;

public class Licitacao {
    String investidor;
    float taxa;
    int quantia;

    public Licitacao(String investidor, float taxa, int quantia) {
        this.investidor = investidor;
        this.taxa = taxa;
        this.quantia = quantia;
    }

    public Licitacao(String investidor, int quantia) {
        this.investidor = investidor;
        this.quantia = quantia;
    }

    public Licitacao (){
        this.investidor = null;
        this.taxa = 0;
        this.quantia = 0;
    }
}
