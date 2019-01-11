package Directory.Resources;

public class Licitacao {
    String investidor;
    int taxa;
    int quantia;

    public Licitacao(){
        this.investidor = null;
        this.taxa = 0;
        this.quantia = 0;
    }

    public Licitacao(String investidor, int taxa, int quantia){
        this.investidor = investidor;
        this.taxa = taxa;
        this.quantia = taxa;
    }
}
