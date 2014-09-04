/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.dao;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.UsuarioModel;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.util.GestorException;
import com.saax.gestorweb.util.PostgresConnection;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author rodrigo
 */
public class GenericDAO implements Serializable {

    private final EntityManagerFactory emf;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public GenericDAO() {
        this.emf = PostgresConnection.getInstance().getEntityManagerFactory();
    }

    public <T> List<T> listByQuery(String sql, Object parameterValue) {
        EntityManager em = getEntityManager();
        List<T> list;

        Query q = em.createQuery(sql);
        
        // obtem os registros para a empresa principal (pre filtro obrigatorio) + filtro informado
        list = (List<T>) q.getResultList();

        return list;

    }

    
    public <T> List<T> listByNamedQuery(String namedQuery, String parameterName, Object parameterValue) {
        EntityManager em = getEntityManager();
        List<T> list;

        // obtem os registros para a empresa principal (pre filtro obrigatorio) + filtro informado
        list = (List<T>) em.createNamedQuery(namedQuery)
                .setParameter(parameterName, parameterValue)
                .getResultList();

        return list;

    }

    
    
    public <T> List<T> listByNamedQueryEmpresa(String namedQuery, String parameterName, Object parameterValue) {
        EntityManager em = getEntityManager();
        List<T> list;
        List<T> returnList;
        try {

            returnList = new ArrayList<>();

            // identifica a empresa principal do usuario logado
            Empresa empresa = new UsuarioModel().getEmpresaUsuarioLogado();

            // obtem os registros para a empresa principal (pre filtro obrigatorio) + filtro informado
            list = (List<T>) em.createNamedQuery(namedQuery)
                    .setParameter("empresa", empresa)
                    .setParameter(parameterName, parameterValue)
                    .getResultList();

            if (list != null) {
                returnList.addAll(list);
            }

            // obtem os registros das subempresas
            for (Empresa subEmpresa : empresa.getSubEmpresas()) {
                list = (List<T>) em.createNamedQuery(namedQuery)
                        .setParameter("empresa", subEmpresa)
                        .setParameter(parameterName, parameterValue)
                        .getResultList();

                if (list != null) {
                    returnList.addAll(list);
                }

            }

            return returnList;

        } catch (GestorException e) {
            Logger.getLogger(GestorMDI.class.getName()).log(Level.SEVERE, e.getMessage());

            return null;
        }
    }

    public <T> T merge(T entityBean) {
        EntityManager em = getEntityManager();
        try {
            return (T) em.merge(entityBean);
        } finally {
            em.close();
        }
    }

    public Query createQuery(String sql) {
        return getEntityManager().createQuery(sql);
    }

}
