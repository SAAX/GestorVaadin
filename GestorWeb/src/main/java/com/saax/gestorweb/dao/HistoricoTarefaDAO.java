/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.dao;

import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import com.saax.gestorweb.model.datamodel.HistoricoTarefa;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.saax.gestorweb.model.datamodel.Tarefa;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author rodrigo
 */
public class HistoricoTarefaDAO implements Serializable {

    public HistoricoTarefaDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(HistoricoTarefa historicoTarefa) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarefa tarefa = historicoTarefa.getTarefa();
            if (tarefa != null) {
                tarefa = em.getReference(tarefa.getClass(), tarefa.getId());
                historicoTarefa.setTarefa(tarefa);
            }
            em.persist(historicoTarefa);
            if (tarefa != null) {
                tarefa.getHistorico().add(historicoTarefa);
                tarefa = em.merge(tarefa);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(HistoricoTarefa historicoTarefa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            HistoricoTarefa persistentHistoricoTarefa = em.find(HistoricoTarefa.class, historicoTarefa.getId());
            Tarefa tarefaOld = persistentHistoricoTarefa.getTarefa();
            Tarefa tarefaNew = historicoTarefa.getTarefa();
            if (tarefaNew != null) {
                tarefaNew = em.getReference(tarefaNew.getClass(), tarefaNew.getId());
                historicoTarefa.setTarefa(tarefaNew);
            }
            historicoTarefa = em.merge(historicoTarefa);
            if (tarefaOld != null && !tarefaOld.equals(tarefaNew)) {
                tarefaOld.getHistorico().remove(historicoTarefa);
                tarefaOld = em.merge(tarefaOld);
            }
            if (tarefaNew != null && !tarefaNew.equals(tarefaOld)) {
                tarefaNew.getHistorico().add(historicoTarefa);
                tarefaNew = em.merge(tarefaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = historicoTarefa.getId();
                if (findHistoricoTarefa(id) == null) {
                    throw new NonexistentEntityException("The historicoTarefa with id " + id + " no longer exists.");
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
            HistoricoTarefa historicoTarefa;
            try {
                historicoTarefa = em.getReference(HistoricoTarefa.class, id);
                historicoTarefa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The historicoTarefa with id " + id + " no longer exists.", enfe);
            }
            Tarefa tarefa = historicoTarefa.getTarefa();
            if (tarefa != null) {
                tarefa.getHistorico().remove(historicoTarefa);
                tarefa = em.merge(tarefa);
            }
            em.remove(historicoTarefa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<HistoricoTarefa> findHistoricoTarefaEntities() {
        return findHistoricoTarefaEntities(true, -1, -1);
    }

    public List<HistoricoTarefa> findHistoricoTarefaEntities(int maxResults, int firstResult) {
        return findHistoricoTarefaEntities(false, maxResults, firstResult);
    }

    private List<HistoricoTarefa> findHistoricoTarefaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(HistoricoTarefa.class));
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

    public HistoricoTarefa findHistoricoTarefa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(HistoricoTarefa.class, id);
        } finally {
            em.close();
        }
    }

    public int getHistoricoTarefaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<HistoricoTarefa> rt = cq.from(HistoricoTarefa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
