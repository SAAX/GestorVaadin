package com.saax.gestorweb.dao;

import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import com.saax.gestorweb.model.datamodel.ApontamentoTarefa;
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
 * DAO para o entity bean: ApontamentoTarefa <br><br>
 * 
 * @author rodrigo
 */
public class ApontamentoTarefaDAO implements Serializable {

    public ApontamentoTarefaDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ApontamentoTarefa apontamentoTarefa) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarefa idtarefa = apontamentoTarefa.getIdTarefa();
            if (idtarefa != null) {
                idtarefa = em.getReference(idtarefa.getClass(), idtarefa.getId());
                apontamentoTarefa.setIdTarefa(idtarefa);
            }
            Usuario idusuarioinclusao = apontamentoTarefa.getIdUsuarioInclusao();
            if (idusuarioinclusao != null) {
                idusuarioinclusao = em.getReference(idusuarioinclusao.getClass(), idusuarioinclusao.getId());
                apontamentoTarefa.setIdUsuarioInclusao(idusuarioinclusao);
            }
            em.persist(apontamentoTarefa);
            if (idtarefa != null) {
                idtarefa.getApontamentoTarefaList().add(apontamentoTarefa);
                idtarefa = em.merge(idtarefa);
            }
            if (idusuarioinclusao != null) {
                idusuarioinclusao.getApontamentoTarefaList().add(apontamentoTarefa);
                idusuarioinclusao = em.merge(idusuarioinclusao);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ApontamentoTarefa apontamentoTarefa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ApontamentoTarefa persistentApontamentoTarefa = em.find(ApontamentoTarefa.class, apontamentoTarefa.getId());
            Tarefa idtarefaOld = persistentApontamentoTarefa.getIdTarefa();
            Tarefa idtarefaNew = apontamentoTarefa.getIdTarefa();
            Usuario idusuarioinclusaoOld = persistentApontamentoTarefa.getIdUsuarioInclusao();
            Usuario idusuarioinclusaoNew = apontamentoTarefa.getIdUsuarioInclusao();
            if (idtarefaNew != null) {
                idtarefaNew = em.getReference(idtarefaNew.getClass(), idtarefaNew.getId());
                apontamentoTarefa.setIdTarefa(idtarefaNew);
            }
            if (idusuarioinclusaoNew != null) {
                idusuarioinclusaoNew = em.getReference(idusuarioinclusaoNew.getClass(), idusuarioinclusaoNew.getId());
                apontamentoTarefa.setIdUsuarioInclusao(idusuarioinclusaoNew);
            }
            apontamentoTarefa = em.merge(apontamentoTarefa);
            if (idtarefaOld != null && !idtarefaOld.equals(idtarefaNew)) {
                idtarefaOld.getApontamentoTarefaList().remove(apontamentoTarefa);
                idtarefaOld = em.merge(idtarefaOld);
            }
            if (idtarefaNew != null && !idtarefaNew.equals(idtarefaOld)) {
                idtarefaNew.getApontamentoTarefaList().add(apontamentoTarefa);
                idtarefaNew = em.merge(idtarefaNew);
            }
            if (idusuarioinclusaoOld != null && !idusuarioinclusaoOld.equals(idusuarioinclusaoNew)) {
                idusuarioinclusaoOld.getApontamentoTarefaList().remove(apontamentoTarefa);
                idusuarioinclusaoOld = em.merge(idusuarioinclusaoOld);
            }
            if (idusuarioinclusaoNew != null && !idusuarioinclusaoNew.equals(idusuarioinclusaoOld)) {
                idusuarioinclusaoNew.getApontamentoTarefaList().add(apontamentoTarefa);
                idusuarioinclusaoNew = em.merge(idusuarioinclusaoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = apontamentoTarefa.getId();
                if (findApontamentoTarefa(id) == null) {
                    throw new NonexistentEntityException("The apontamentoTarefa with id " + id + " no longer exists.");
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
            ApontamentoTarefa apontamentoTarefa;
            try {
                apontamentoTarefa = em.getReference(ApontamentoTarefa.class, id);
                apontamentoTarefa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The apontamentoTarefa with id " + id + " no longer exists.", enfe);
            }
            Tarefa idtarefa = apontamentoTarefa.getIdTarefa();
            if (idtarefa != null) {
                idtarefa.getApontamentoTarefaList().remove(apontamentoTarefa);
                idtarefa = em.merge(idtarefa);
            }
            Usuario idusuarioinclusao = apontamentoTarefa.getIdUsuarioInclusao();
            if (idusuarioinclusao != null) {
                idusuarioinclusao.getApontamentoTarefaList().remove(apontamentoTarefa);
                idusuarioinclusao = em.merge(idusuarioinclusao);
            }
            em.remove(apontamentoTarefa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ApontamentoTarefa> findApontamentoTarefaEntities() {
        return findApontamentoTarefaEntities(true, -1, -1);
    }

    public List<ApontamentoTarefa> findApontamentoTarefaEntities(int maxResults, int firstResult) {
        return findApontamentoTarefaEntities(false, maxResults, firstResult);
    }

    private List<ApontamentoTarefa> findApontamentoTarefaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ApontamentoTarefa.class));
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

    public ApontamentoTarefa findApontamentoTarefa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ApontamentoTarefa.class, id);
        } finally {
            em.close();
        }
    }

    public int getApontamentoTarefaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ApontamentoTarefa> rt = cq.from(ApontamentoTarefa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
