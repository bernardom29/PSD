package Exchange;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Emissao {
    private float taxa;
    private int montanteTotal;
    private String empresa;
    private boolean sucesso;
    private LocalDateTime data;
    private List<Licitacao> licitacoes;

    public Emissao(float taxa, int montanteTotal, String empresa) {
        this.taxa = taxa;
        this.montanteTotal = montanteTotal;
        this.empresa = empresa;
        this.sucesso = false;
        this.data = LocalDateTime.now();
        this.licitacoes = new ArrayList<Licitacao>();
    }

    public void setSucesso(boolean sucesso) {
        this.sucesso = sucesso;
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

    public float getTaxa() {
        return taxa;
    }

    public void setTaxa(float taxa) {
        this.taxa = taxa;
    }

    public List<Licitacao> getLicitacoes() {
        return licitacoes;
    }
}
