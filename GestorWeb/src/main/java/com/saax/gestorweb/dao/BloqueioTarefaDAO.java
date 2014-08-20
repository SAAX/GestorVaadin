/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.dao;

import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import com.saax.gestorweb.model.datamodel.BloqueioTarefa;
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
public class BloqueioTarefaDAO implements Serializable {

    public BloqueioTarefaDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BloqueioTarefa bloqueioTarefa) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarefa idtarefa = bloqueioTarefa.getTarefa();
            if (idtarefa != null) {
                idtarefa = em.getReference(idtarefa.getClass(), idtarefa.getId());
                bloqueioTarefa.setTarefa(idtarefa);
            }
            em.persist(bloqueioTarefa);
            if (idtarefa != null) {
                idtarefa.getBloqueios().add(bloqueioTarefa);
                idtarefa = em.merge(idtarefa);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BloqueioTarefa bloqueioTarefa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BloqueioTarefa persistentBloqueioTarefa = em.find(BloqueioTarefa.class, bloqueioTarefa.getId());
            Tarefa idtarefaOld = persistentBloqueioTarefa.getTarefa();
            Tarefa idtarefaNew = bloqueioTarefa.getTarefa();
            if (idtarefaNew != null) {
                idtarefaNew = em.getReference(idtarefaNew.getClass(), idtarefaNew.getId());
                bloqueioTarefa.setTarefa(idtarefaNew);
            }
            bloqueioTarefa = em.merge(bloqueioTarefa);
            if (idtarefaOld != null && !idtarefaOld.equals(idtarefaNew)) {
                idtarefaOld.getBloqueios().remove(bloqueioTarefa);
                idtarefaOld = em.merge(idtarefaOld);
            }
            if (idtarefaNew != null && !idtarefaNew.equals(idtarefaOld)) {
                idtarefaNew.getBloqueios().add(bloqueioTarefa);
                idtarefaNew = em.merge(idtarefaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = bloqueioTarefa.getId();
                if (findBloqueioTarefa(id) == null) {
                    throw new NonexistentEntityException("The bloqueioTarefa with id " + id + " no longer exists.");
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
            BloqueioTarefa bloqueioTarefa;
            try {
                bloqueioTarefa = em.getReference(BloqueioTarefa.class, id);
                bloqueioTarefa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The bloqueioTarefa with id " + id + " no longer exists.", enfe);
            }
            Tarefa idtarefa = bloqueioTarefa.getTarefa();
            if (idtarefa != null) {
                idtarefa.getBloqueios().remove(bloqueioTarefa);
                idtarefa = em.merge(idtarefa);
            }
            em.remove(bloqueioTarefa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BloqueioTarefa> findBloqueioTarefaEntities() {
        return findBloqueioTarefaEntities(true, -1, -1);
    }

    public List<BloqueioTarefa> findBloqueioTarefaEntities(int maxResults, int firstResult) {
        return findBloqueioTarefaEntities(false, maxResults, firstResult);
    }

    private List<BloqueioTarefa> findBloqueioTarefaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BloqueioTarefa.class));
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

    public BloqueioTarefa findBloqueioTarefa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BloqueioTarefa.class, id);
        } finally {
            em.close();
        }
    }

    public int getBloqueioTarefaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BloqueioTarefa> rt = cq.from(BloqueioTarefa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
