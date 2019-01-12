package Directory.Resources;

public class Licitacao {
    public int id;
    public String investidor;
    public int taxa;
    public int quantia;

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
