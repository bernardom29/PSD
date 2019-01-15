package Directory.Resources;

public class Licitacao {
    public int id;
    public String investidor;
    public float taxa;
    public int quantia;

    public Licitacao(int id, String investidor, float taxa, int quantia) {
        this.id = id;
        this.investidor = investidor;
        this.taxa = taxa;
        this.quantia = quantia;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("id: ")
                .append(this.id).append("\n")
                .append("investidor: ")
                .append(this.investidor).append("\n")
                .append("taxa: ")
                .append(this.taxa).append("\n")
                .append("quantia: ")
                .append(this.quantia).append("\n");
        return sb.toString();
    }
}
