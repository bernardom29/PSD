package Directory.Resources;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Leilao {
    public int id;
    public float taxaMaxima;
    public int montanteTotal;
    public String empresa;
    public LocalDateTime data;
    public boolean sucesso;
    public boolean ativo;
    public List<Licitacao> licitacoes;

    public Leilao(int id, float taxaMaxima, int montanteTotal, String empresa, LocalDateTime data, boolean sucesso) {
        this.id = id;
        this.taxaMaxima = taxaMaxima;
        this.montanteTotal = montanteTotal;
        this.empresa = empresa;
        this.data = data;
        this.sucesso = sucesso;
        this.ativo=true;
        this.licitacoes = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getTaxaMaxima() {
        return taxaMaxima;
    }

    public void setTaxaMaxima(float taxaMaxima) {
        this.taxaMaxima = taxaMaxima;
    }

    public int getMontanteTotal() {
        return montanteTotal;
    }

    public void setMontanteTotal(int montanteTotal) {
        this.montanteTotal = montanteTotal;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public void setSucesso(boolean sucesso) {
        this.sucesso = sucesso;
    }

    public List<Licitacao> getLicitacoes() {
        return licitacoes;
    }

    public void setLicitacoes(List<Licitacao> licitacoes) {
        this.licitacoes = licitacoes;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("id: ")
                .append(this.id).append("\n")
                .append("taxaMaxima: ")
                .append(this.taxaMaxima).append("\n")
                .append("montanteTotal: ")
                .append(this.montanteTotal).append("\n")
                .append("empresa: ")
                .append(this.empresa).append("\n")
                .append("data: ")
                .append(DateTimeFormatter.ofPattern("ss:mm:HH dd-MM-yyyy").format(this.data)).append("\n")
                .append("sucesso: ")
                .append(this.sucesso).append("\n")
                .append("ativo: ")
                .append(this.ativo).append("\n");
        for(Licitacao licitacao : this.licitacoes){
            sb.append(licitacao.toString());
        }
        return sb.toString();
    }

    public Licitacao getLicitacao(int idLic) {
        return licitacoes.get(idLic);
    }
}