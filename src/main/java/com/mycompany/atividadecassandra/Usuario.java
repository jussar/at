/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atividadecassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.BuiltStatement;
import static com.datastax.driver.core.schemabuilder.SchemaBuilder.frozen;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Frozen;
import com.datastax.driver.mapping.annotations.FrozenKey;
import com.datastax.driver.mapping.annotations.FrozenValue;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jussara
 */
@Table(keyspace = "usuario", name = "pessoa")
public class Usuario {

    private int id;
    private String nome;
    @Frozen
    Map<String, Telefone> maptel;

    public Usuario() {

        maptel = new HashMap<>();

    }

    public Usuario(int id, String nome) {
        this.id = id;
        this.nome = nome;
        maptel = new HashMap<>();
    }

    @PartitionKey
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Map<String, Telefone> getMaptel() {
        return maptel;
    }

    public void setMaptel(Map<String, Telefone> maptel) {
        this.maptel = maptel;
    }

    public void addtel(String cv, Telefone tel) {

        maptel.put(cv, tel);

    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", nome=" + nome + ", maptel=" + maptel + '}';
    }

    public static void main(String[] args) {

        Cluster cluster = null;
        try {

            cluster = Cluster.builder().addContactPoint("127.0.0.1").withPort(9042).build();
            Session session = cluster.connect();
            MappingManager manager = new MappingManager(session);
            session.execute("CREATE KEYSPACE IF NOT EXISTS usuario WITH replication"
                    + "= {'class':'SimpleStrategy', 'replication_factor':1};");
            session.execute("CREATE TYPE IF NOT EXISTS usuario.telefone (numero text);");
            session.execute("CREATE TABLE IF NOT EXISTS usuario.pessoa ("
                    + "id int,"
                    + "nome text,"
                    + "maptel map<text,frozen<telefone>>,"
                    + "primary key(id));");
            Telefone t = new Telefone("9999999999");
            Telefone t1 = new Telefone("8888888888");
            Usuario u = new Usuario(0, "cassandra");
            Usuario z = new Usuario(1, "fatima");
            z.addtel("cv2", t1);
            u.addtel("cv1", new Telefone("999999999999999"));
            Mapper<Usuario> mapper = manager.mapper(Usuario.class, "usuario");
            //adiciona
            mapper.save(u);
            mapper.save(z);
            //deleta
            mapper.delete(0);
            //busca o usuario por a pk
            Usuario x = mapper.get(0);
            System.out.println(x.toString());
         
            //update
            session.execute("UPDATE usuario.pessoa set nome = 'rubi' where id = 0");

        } finally {

            if (cluster != null) {
                cluster.close();
            }

        }

    }

}
