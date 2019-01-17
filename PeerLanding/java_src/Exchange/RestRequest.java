package Exchange;


import java.util.ArrayList;
import java.util.List;

public class RestRequest {
    private final String nome;
    private final int id;
    private final float taxaMaxima;
    private final int montanteTotal;
    private final int idL;
    private final String investidor;
    private final float juro;
    private final int quantia;
    private final boolean sucesso;
    private final boolean ativo;
    private final List<Licitacao> licitacaos;

    public RestRequest(
             String nome,
             int id,
             float taxaMaxima,
             int montanteTotal) {
        this.nome = nome;
        this.id = id;
        this.taxaMaxima = taxaMaxima;
        this.montanteTotal = montanteTotal;
        this.idL = -1;
        this.investidor = null;
        this.juro = 0;
        this.quantia = 0;
        this.ativo = false;
        this.sucesso = false;
        this.licitacaos = null;
    }

    public RestRequest(
             String empresa,
             int id,
             int idL,
             String investidor,
             float juro,
             int quantia) {
        this.nome = empresa;
        this.id = id;
        this.taxaMaxima = 0;
        this.montanteTotal = 0;
        this.idL = idL;
        this.investidor = investidor;
        this.juro = juro;
        this.quantia = quantia;
        this.ativo = false;
        this.sucesso = false;
        this.licitacaos = null;
    }

    public RestRequest(
             String empresa,
             int id,
             int idL,
             String investidor,
             int quantia) {
        this.nome = empresa;
        this.id = id;
        this.taxaMaxima = 0;
        this.montanteTotal = 0;
        this.idL = idL;
        this.investidor = investidor;
        this.juro = 0;
        this.quantia = quantia;
        this.ativo = false;
        this.sucesso = false;
        this.licitacaos = null;
    }


    public RestRequest(
            String empresa,
            int id,
            boolean ativo,
            boolean sucesso) {
        this.nome = empresa;
        this.id = id;
        this.taxaMaxima = 0;
        this.montanteTotal = 0;
        this.idL = -1;
        this.investidor = null;
        this.juro = 0;
        this.quantia = 0;
        this.ativo = ativo;
        this.sucesso = sucesso;
        this.licitacaos = null;
    }

    public RestRequest(
            String empresa,
            int id,
            boolean ativo,
            boolean sucesso,
            List<Licitacao> licitacaos) {
        this.nome = empresa;
        this.id = id;
        this.taxaMaxima = 0;
        this.montanteTotal = 0;
        this.idL = -1;
        this.investidor = null;
        this.juro = 0;
        this.quantia = 0;
        this.ativo = ativo;
        this.sucesso = sucesso;
        this.licitacaos = licitacaos;
    }

    public String getEmpresa() {
        return nome;
    }

    public int getId() {
        return id;
    }

    public float getTaxaMaxima() {
        return taxaMaxima;
    }

    public int getMontanteTotal() {
        return montanteTotal;
    }

    public int getIdL() {
        return idL;
    }

    public String getInvestidor() {
        return investidor;
    }

    public float getJuro() {
        return juro;
    }

    public int getQuantia() {
        return quantia;
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public List<Licitacao> getLicitacaos() {
        return licitacaos;
    }
}
