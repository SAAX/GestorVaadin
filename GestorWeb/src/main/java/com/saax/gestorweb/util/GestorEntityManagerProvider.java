/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.util;

import javax.persistence.EntityManager;

/**
 *
 * @author rodrigo
 */
public class GestorEntityManagerProvider {

    private static final ThreadLocal<EntityManager> entityManagerThreadLocal = new ThreadLocal<>();

    public static EntityManager getEntityManager() {
//        if (entityManagerThreadLocal.get()==null){
//            throw new GestorException("Entity Manager está NULO!");
//        }
//        if (!entityManagerThreadLocal.get().isOpen()){
//            throw new GestorException("Entity Manager está FECHADO!");
//        }
        return entityManagerThreadLocal.get();
    }

    public static void setCurrentEntityManager(EntityManager em) {
//        if (em==null){
//            throw new GestorException("Entity Manager está NULO!");
//        }
//        if (!em.isOpen()){
//            throw new GestorException("Entity Manager está FECHADO!");
//        }
        entityManagerThreadLocal.set(em);
        
    }
    
    public static void remove(){
        entityManagerThreadLocal.remove();
    }
}
