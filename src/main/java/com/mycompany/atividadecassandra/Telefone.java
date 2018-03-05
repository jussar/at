/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.atividadecassandra;

import com.datastax.driver.mapping.annotations.UDT;

/**
 *
 * @author Jussara
 */
@UDT(keyspace = "usuario", name = "telefone")
public class Telefone {
    
    private String numero;

    public Telefone(String numero) {
        this.numero = numero;
    }

    public Telefone() {
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
    
    
    
}
