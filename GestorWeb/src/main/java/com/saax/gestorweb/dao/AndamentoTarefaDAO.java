/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.dao;

import com.saax.gestorweb.model.datamodel.AndamentoTarefa;
import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author rodrigo
 */
public class AndamentoTarefaDAO implements Serializable {

    public AndamentoTarefaDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AndamentoTarefa andamentoTarefa) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(andamentoTarefa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AndamentoTarefa andamentoTarefa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            andamentoTarefa = em.merge(andamentoTarefa);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = andamentoTarefa.getId();
                if (findAndamentoTarefa(id) == null) {
                    throw new NonexistentEntityException("The andamentoTarefa with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AndamentoTarefa andamentoTarefa;
            try {
                andamentoTarefa = em.getReference(AndamentoTarefa.class, id);
                andamentoTarefa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The andamentoTarefa with id " + id + " no longer exists.", enfe);
            }
            em.remove(andamentoTarefa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AndamentoTarefa> findAndamentoTarefaEntities() {
        return findAndamentoTarefaEntities(true, -1, -1);
    }

    public List<AndamentoTarefa> findAndamentoTarefaEntities(int maxResults, int firstResult) {
        return findAndamentoTarefaEntities(false, maxResults, firstResult);
    }

    private List<AndamentoTarefa> findAndamentoTarefaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AndamentoTarefa.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public AndamentoTarefa findAndamentoTarefa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AndamentoTarefa.class, id);
        } finally {
            em.close();
        }
    }

    public int getAndamentoTarefaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AndamentoTarefa> rt = cq.from(AndamentoTarefa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
