package Directory;

import Directory.Representations.Empresa;

import io.dropwizard.Configuration;

import java.util.HashMap;


public class DirectoryConf extends Configuration{
    public HashMap<String, Empresa> empresas;
}
