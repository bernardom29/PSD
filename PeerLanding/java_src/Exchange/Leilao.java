package Exchange;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Leilao {
    private int taxaMaxima;
    private int montanteTotal;
    private String empresa;
    private Date data;
    private boolean sucesso;
    private List<Licitacao> licitacoes;

    public Leilao(int taxaMaxima, int montanteTotal, String empresa, boolean sucesso) {
        this.taxaMaxima = taxaMaxima;
        this.montanteTotal = montanteTotal;
        this.empresa = empresa;
        this.data = new Date();
        this.sucesso = sucesso;
        this.licitacoes = new ArrayList<Licitacao>();
    }

    public int getTaxaMaxima() {
        return taxaMaxima;
    }

    public void setTaxaMaxima(int taxaMaxima) {
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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
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
}
