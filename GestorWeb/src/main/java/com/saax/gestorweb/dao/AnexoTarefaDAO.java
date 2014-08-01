package com.saax.gestorweb.dao;

import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import com.saax.gestorweb.model.datamodel.AnexoTarefa;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * DAO para o entity bean: AnexoTarefa <br><br>
 * 
 * @author rodrigo
 */
public class AnexoTarefaDAO implements Serializable {

    public AnexoTarefaDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * metodo padrao modificado para gravar data/hora de inclusao
     * @param anexoTarefa 
     */
    public void create(AnexoTarefa anexoTarefa) {
        anexoTarefa.setDataHoraInclusao(LocalDateTime.now());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarefa tarefa = anexoTarefa.getTarefa();
            if (tarefa != null) {
                tarefa = em.getReference(tarefa.getClass(), tarefa.getId());
                anexoTarefa.setTarefa(tarefa);
            }
            Usuario usuarioInclusao = anexoTarefa.getUsuarioInclusao();
            if (usuarioInclusao != null) {
                usuarioInclusao = em.getReference(usuarioInclusao.getClass(), usuarioInclusao.getId());
                anexoTarefa.setUsuarioInclusao(usuarioInclusao);
            }
            em.persist(anexoTarefa);
            if (tarefa != null) {
                tarefa.getAnexos().add(anexoTarefa);
                tarefa = em.merge(tarefa);
            }
            if (usuarioInclusao != null) {
                usuarioInclusao.getAnexosTarefaIncluidos().add(anexoTarefa);
                usuarioInclusao = em.merge(usuarioInclusao);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AnexoTarefa anexoTarefa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AnexoTarefa persistentAnexoTarefa = em.find(AnexoTarefa.class, anexoTarefa.getId());
            Tarefa tarefaOld = persistentAnexoTarefa.getTarefa();
            Tarefa tarefaNew = anexoTarefa.getTarefa();
            Usuario usuarioInclusaoOld = persistentAnexoTarefa.getUsuarioInclusao();
            Usuario usuarioInclusaoNew = anexoTarefa.getUsuarioInclusao();
            if (tarefaNew != null) {
                tarefaNew = em.getReference(tarefaNew.getClass(), tarefaNew.getId());
                anexoTarefa.setTarefa(tarefaNew);
            }
            if (usuarioInclusaoNew != null) {
                usuarioInclusaoNew = em.getReference(usuarioInclusaoNew.getClass(), usuarioInclusaoNew.getId());
                anexoTarefa.setUsuarioInclusao(usuarioInclusaoNew);
            }
            anexoTarefa = em.merge(anexoTarefa);
            if (tarefaOld != null && !tarefaOld.equals(tarefaNew)) {
                tarefaOld.getAnexos().remove(anexoTarefa);
                tarefaOld = em.merge(tarefaOld);
            }
            if (tarefaNew != null && !tarefaNew.equals(tarefaOld)) {
                tarefaNew.getAnexos().add(anexoTarefa);
                tarefaNew = em.merge(tarefaNew);
            }
            if (usuarioInclusaoOld != null && !usuarioInclusaoOld.equals(usuarioInclusaoNew)) {
                usuarioInclusaoOld.getAnexosTarefaIncluidos().remove(anexoTarefa);
                usuarioInclusaoOld = em.merge(usuarioInclusaoOld);
            }
            if (usuarioInclusaoNew != null && !usuarioInclusaoNew.equals(usuarioInclusaoOld)) {
                usuarioInclusaoNew.getAnexosTarefaIncluidos().add(anexoTarefa);
                usuarioInclusaoNew = em.merge(usuarioInclusaoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = anexoTarefa.getId();
                if (findAnexoTarefa(id) == null) {
                    throw new NonexistentEntityException("The anexoTarefa with id " + id + " no longer exists.");
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
            AnexoTarefa anexoTarefa;
            try {
                anexoTarefa = em.getReference(AnexoTarefa.class, id);
                anexoTarefa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The anexoTarefa with id " + id + " no longer exists.", enfe);
            }
            Tarefa tarefa = anexoTarefa.getTarefa();
            if (tarefa != null) {
                tarefa.getAnexos().remove(anexoTarefa);
                tarefa = em.merge(tarefa);
            }
            Usuario usuarioInclusao = anexoTarefa.getUsuarioInclusao();
            if (usuarioInclusao != null) {
                usuarioInclusao.getAnexosTarefaIncluidos().remove(anexoTarefa);
                usuarioInclusao = em.merge(usuarioInclusao);
            }
            em.remove(anexoTarefa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AnexoTarefa> findAnexoTarefaEntities() {
        return findAnexoTarefaEntities(true, -1, -1);
    }

    public List<AnexoTarefa> findAnexoTarefaEntities(int maxResults, int firstResult) {
        return findAnexoTarefaEntities(false, maxResults, firstResult);
    }

    private List<AnexoTarefa> findAnexoTarefaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AnexoTarefa.class));
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

    public AnexoTarefa findAnexoTarefa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AnexoTarefa.class, id);
        } finally {
            em.close();
        }
    }

    public int getAnexoTarefaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AnexoTarefa> rt = cq.from(AnexoTarefa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
