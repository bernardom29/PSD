package Directory;

import Directory.Representations.*;
import Directory.Resources.Empresa;
import Directory.Resources.Licitacao;
import Directory.Resources.Emissao;
import Directory.Resources.Leilao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.*;


public class Directory {

    public static void main(String[] args) throws JsonProcessingException {
        /*Licitacao lic1 = new Licitacao(1,"psa",20,500);
        Licitacao lic2 = new Licitacao(2,"juan",30,100);
        List<Licitacao> lics = new ArrayList<>();
        Licitacao lic3 = new Licitacao(1,"m",30,200);
        List<Licitacao> lics2 = new ArrayList<>();
        lics.add(lic1);
        lics.add(lic2);
        lics2.add(lic3);
        Emissao em1 = new Emissao(1,30,540,"naosei",true,lics);
        Emissao em2 = new Emissao(2,43,520,"naosei",true,lics2);
        List<Emissao> ems = new ArrayList<>();
        ems.add(em1);
        ems.add(em2);
        List<Leilao> lls = new ArrayList<>();

        Empresa emp = new Empresa("naosei",lls,ems);

        HashMap<String, Empresa> empresas = new HashMap<>();

        empresas.put("naosei", emp);

        ObjectMapper objectMapper = new ObjectMapper();
        String value = objectMapper.writeValueAsString(empresas);

        System.out.print(value);*/
    }
}
