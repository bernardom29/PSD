package Exchange;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Leilao {
    private float taxaMaxima;
    private int montanteTotal;
    private String empresa;
    private Date data;
    private boolean sucesso;
    private List<Licitacao> licitacoes;

    public Leilao(float taxaMaxima, int montanteTotal, String empresa) {
        this.taxaMaxima = taxaMaxima;
        this.montanteTotal = montanteTotal;
        this.empresa = empresa;
        this.data = new Date();
        this.sucesso = false;
        this.licitacoes = new ArrayList<Licitacao>();
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

    public float getTaxaLicitacaoMaxima () {
        return licitacoes.stream().map(a -> a.taxa).reduce(Float.MIN_VALUE,Float::max);
    }

    public int getInvestimentoTotal () {
        return licitacoes.stream().map(a -> a.quantia).reduce(Integer::sum).get();
    }

    public List<Licitacao> getLicitacoes() {
        return licitacoes;
    }

    public void setLicitacoes(List<Licitacao> licitacoes) {
        this.licitacoes = licitacoes;
    }
}
