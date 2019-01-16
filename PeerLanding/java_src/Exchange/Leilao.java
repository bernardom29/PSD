package Exchange;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//TODO Add ativo?
public class Leilao {
    private float taxaMaxima;
    private int montanteTotal;
    private String empresa;
    private LocalDateTime data;
    private boolean sucesso;
    private List<Licitacao> licitacoes;

    public Leilao(float taxaMaxima, int montanteTotal, String empresa) {
        this.taxaMaxima = taxaMaxima;
        this.montanteTotal = montanteTotal;
        this.empresa = empresa;
        this.data = LocalDateTime.now();
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

    public float getTaxaLicitacaoMaxima () {
        return licitacoes.stream().map(a -> a.taxa).reduce(Float.MIN_VALUE,Float::max);
    }

    public boolean alocaInvestidores () {
        ArrayList<Licitacao> alocados = new ArrayList<Licitacao>();
        ArrayList<Licitacao> licitacaoSort = (ArrayList<Licitacao>) licitacoes.stream()
                        .sorted((o1, o2)->Float.compare(o1.taxa,o2.taxa))
                        .collect(Collectors.toList());
        int sum=0;
        for(Licitacao l : licitacaoSort) {
            sum+=l.quantia;
            alocados.add(l);
            if(sum>=montanteTotal) {
                break;
            }
        }

        if(this.licitacoes.size()==alocados.size()) {
            return false;
        } else {
            this.licitacoes=alocados;
            return true;
        }
    }

    public int getInvestimentoTotal () {
        return licitacoes.stream().map(a -> a.quantia).reduce(0,Integer::sum);
    }

    public List<Licitacao> getLicitacoes() {
        return licitacoes;
    }

    public void setLicitacoes(List<Licitacao> licitacoes) {
        this.licitacoes = licitacoes;
    }
}
