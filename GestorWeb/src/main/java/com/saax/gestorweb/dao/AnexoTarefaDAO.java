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

    public void create(AnexoTarefa anexoTarefa) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarefa idtarefa = anexoTarefa.getIdTarefa();
            if (idtarefa != null) {
                idtarefa = em.getReference(idtarefa.getClass(), idtarefa.getId());
                anexoTarefa.setIdTarefa(idtarefa);
            }
            Usuario idusuarioinclusao = anexoTarefa.getIdUsuarioInclusao();
            if (idusuarioinclusao != null) {
                idusuarioinclusao = em.getReference(idusuarioinclusao.getClass(), idusuarioinclusao.getId());
                anexoTarefa.setIdUsuarioInclusao(idusuarioinclusao);
            }
            em.persist(anexoTarefa);
            if (idtarefa != null) {
                idtarefa.getAnexoTarefaList().add(anexoTarefa);
                idtarefa = em.merge(idtarefa);
            }
            if (idusuarioinclusao != null) {
                idusuarioinclusao.getAnexoTarefaList().add(anexoTarefa);
                idusuarioinclusao = em.merge(idusuarioinclusao);
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
            Tarefa idtarefaOld = persistentAnexoTarefa.getIdTarefa();
            Tarefa idtarefaNew = anexoTarefa.getIdTarefa();
            Usuario idusuarioinclusaoOld = persistentAnexoTarefa.getIdUsuarioInclusao();
            Usuario idusuarioinclusaoNew = anexoTarefa.getIdUsuarioInclusao();
            if (idtarefaNew != null) {
                idtarefaNew = em.getReference(idtarefaNew.getClass(), idtarefaNew.getId());
                anexoTarefa.setIdTarefa(idtarefaNew);
            }
            if (idusuarioinclusaoNew != null) {
                idusuarioinclusaoNew = em.getReference(idusuarioinclusaoNew.getClass(), idusuarioinclusaoNew.getId());
                anexoTarefa.setIdUsuarioInclusao(idusuarioinclusaoNew);
            }
            anexoTarefa = em.merge(anexoTarefa);
            if (idtarefaOld != null && !idtarefaOld.equals(idtarefaNew)) {
                idtarefaOld.getAnexoTarefaList().remove(anexoTarefa);
                idtarefaOld = em.merge(idtarefaOld);
            }
            if (idtarefaNew != null && !idtarefaNew.equals(idtarefaOld)) {
                idtarefaNew.getAnexoTarefaList().add(anexoTarefa);
                idtarefaNew = em.merge(idtarefaNew);
            }
            if (idusuarioinclusaoOld != null && !idusuarioinclusaoOld.equals(idusuarioinclusaoNew)) {
                idusuarioinclusaoOld.getAnexoTarefaList().remove(anexoTarefa);
                idusuarioinclusaoOld = em.merge(idusuarioinclusaoOld);
            }
            if (idusuarioinclusaoNew != null && !idusuarioinclusaoNew.equals(idusuarioinclusaoOld)) {
                idusuarioinclusaoNew.getAnexoTarefaList().add(anexoTarefa);
                idusuarioinclusaoNew = em.merge(idusuarioinclusaoNew);
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
            Tarefa idtarefa = anexoTarefa.getIdTarefa();
            if (idtarefa != null) {
                idtarefa.getAnexoTarefaList().remove(anexoTarefa);
                idtarefa = em.merge(idtarefa);
            }
            Usuario idusuarioinclusao = anexoTarefa.getIdUsuarioInclusao();
            if (idusuarioinclusao != null) {
                idusuarioinclusao.getAnexoTarefaList().remove(anexoTarefa);
                idusuarioinclusao = em.merge(idusuarioinclusao);
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
