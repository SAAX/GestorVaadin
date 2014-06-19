/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Classe gerenciadora da conexão com banco de testes
 * @author rodrigo
 */
public class DBConnect {

    private static DBConnect instance;
    
    private DBConnect(){
        
    }

    public static DBConnect getInstance() {
        if(instance==null) instance = new DBConnect();
        return instance;
    }
    
    
    
    /**
     * Conecta com a unidade de persistencia de teste
     */
    public void assertConnection() {

        PostgresConnection connection = PostgresConnection.getInstance();
        
        EntityManagerFactory emf = connection.getEntityManagerFactory();
        
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("GestorWebTestPU");
            connection.setEntityManagerFactory(emf);
        }

    }

    /**
     * Desconecta da unidade de persistencia de teste
     */
    public void disconnect() {

        PostgresConnection connection = PostgresConnection.getInstance();

        EntityManagerFactory emf = connection.getEntityManagerFactory();
        if (emf != null) {
            emf.close();
        }
        
        connection.setEntityManagerFactory(null);
        

    }
}
