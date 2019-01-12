package Exchange;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Emissao {
    private float taxa;
    private int montanteTotal;
    private String empresa;
    private boolean sucesso;
    private Date data;
    private List<Licitacao> licitacoes;

    public Emissao(float taxa, int montanteTotal, String empresa, boolean sucesso) {
        this.taxa = taxa;
        this.montanteTotal = montanteTotal;
        this.empresa = empresa;
        this.sucesso = sucesso;
        this.data = new Date();
        this.licitacoes = new ArrayList<Licitacao>();
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
