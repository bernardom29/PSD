package Directory.Resources;

public class Licitacao {
    public int id;
    public String investidor;
    public float taxa;
    public int quantia;

    public Licitacao(int id, String investidor, int quantia) {
        this.id = id;
        this.investidor = investidor;
        this.quantia = quantia;
    }

    public Licitacao(int id, String investidor, float taxa, int quantia) {
        this.id = id;
        this.investidor = investidor;
        this.taxa = taxa;
        this.quantia = quantia;
    }

    public String toString(){
        String sb = "id: " +
                this.id + "\n" +
                "investidor: " +
                this.investidor + "\n" +
                "taxa: " +
                this.taxa + "\n" +
                "quantia: " +
                this.quantia + "\n";
        return sb;
    }
}
