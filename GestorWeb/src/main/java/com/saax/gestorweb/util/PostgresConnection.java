package com.saax.gestorweb.util;


import javax.persistence.EntityManagerFactory;

/**
 * Singleton responsável por armazenar o entityManagerFactory do hibernate
 * 
 * @author Rodrigo
 */
public class PostgresConnection {

    private static PostgresConnection instance;
    private EntityManagerFactory entityManagerFactory;
    
    /** 
     * Construtor privado para garantir singularidade
     */
    private PostgresConnection(){
        
    }

    /**
     * Cria a instancia do singleton, caso não exista, e retorna
     * @return a única instancia de PostgresConnection
     */
    public static PostgresConnection getInstance(){
    
        if (instance==null) {
            instance = new PostgresConnection();
        }
        
        return instance;
    }

    /**
     * Setter da propriedade encapsulada: entityManagerFactory 
     * @param entityManagerFactory 
     */
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    /**
     * Getter da propriedade encapsulada: entityManagerFactory 
     * @return entityManagerFactory
     */
    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
    
    
    
}
