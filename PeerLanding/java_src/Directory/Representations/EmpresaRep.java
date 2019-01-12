package Directory.Representations;

import Directory.Resources.Empresa;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class EmpresaRep{
    public Empresa emp;

    @JsonCreator
    public EmpresaRep (@JsonProperty("empresa") Empresa emp) {
        this.emp = emp;
    }

    public Empresa getEmpresa() {
        return emp;
    }

    public void setEmpresa(Empresa emp) {
        this.emp = emp;
    }

}
