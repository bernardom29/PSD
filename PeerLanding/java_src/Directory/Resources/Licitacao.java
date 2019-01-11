package Directory.Resources;

public class Licitacao {
    int id;
    String investidor;
    int taxa;
    int quantia;

    public Licitacao(){
        this.id = 0;
        this.investidor = null;
        this.taxa = 0;
        this.quantia = 0;
    }

    public Licitacao(int id, String investidor, int taxa, int quantia){
        this.id = id;
        this.investidor = investidor;
        this.taxa = taxa;
        this.quantia = taxa;
    }
}
