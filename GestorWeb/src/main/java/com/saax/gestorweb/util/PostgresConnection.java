/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.util;


import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Rodrigo
 */
public class PostgresConnection {

    private static PostgresConnection instance;
    private EntityManagerFactory entityManagerFactory;
    
    private PostgresConnection(){
        
    }
    
    public static PostgresConnection getInstance(){
    
        if (instance==null) {
            instance = new PostgresConnection();
        }
        
        return instance;
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
    
    
    
}
