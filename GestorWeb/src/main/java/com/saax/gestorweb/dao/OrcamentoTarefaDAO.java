package com.saax.gestorweb.dao;

import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import com.saax.gestorweb.model.datamodel.OrcamentoTarefa;
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
 * DAO para o entity bean: OrcamentoTarefa <br><br>
 * 
 * @author rodrigo
 */
public class OrcamentoTarefaDAO implements Serializable {

    public OrcamentoTarefaDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(OrcamentoTarefa orcamentoTarefa) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarefa idtarefa = orcamentoTarefa.getIdTarefa();
            if (idtarefa != null) {
                idtarefa = em.getReference(idtarefa.getClass(), idtarefa.getId());
                orcamentoTarefa.setIdTarefa(idtarefa);
            }
            Usuario idusuarioinclusao = orcamentoTarefa.getIdUsuarioInclusao();
            if (idusuarioinclusao != null) {
                idusuarioinclusao = em.getReference(idusuarioinclusao.getClass(), idusuarioinclusao.getId());
                orcamentoTarefa.setIdUsuarioInclusao(idusuarioinclusao);
            }
            em.persist(orcamentoTarefa);
            if (idtarefa != null) {
                idtarefa.getOrcamentos().add(orcamentoTarefa);
                idtarefa = em.merge(idtarefa);
            }
            if (idusuarioinclusao != null) {
                idusuarioinclusao.getOrcamentoTarefaList().add(orcamentoTarefa);
                idusuarioinclusao = em.merge(idusuarioinclusao);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(OrcamentoTarefa orcamentoTarefa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            OrcamentoTarefa persistentOrcamentoTarefa = em.find(OrcamentoTarefa.class, orcamentoTarefa.getId());
            Tarefa idtarefaOld = persistentOrcamentoTarefa.getIdTarefa();
            Tarefa idtarefaNew = orcamentoTarefa.getIdTarefa();
            Usuario idusuarioinclusaoOld = persistentOrcamentoTarefa.getIdUsuarioInclusao();
            Usuario idusuarioinclusaoNew = orcamentoTarefa.getIdUsuarioInclusao();
            if (idtarefaNew != null) {
                idtarefaNew = em.getReference(idtarefaNew.getClass(), idtarefaNew.getId());
                orcamentoTarefa.setIdTarefa(idtarefaNew);
            }
            if (idusuarioinclusaoNew != null) {
                idusuarioinclusaoNew = em.getReference(idusuarioinclusaoNew.getClass(), idusuarioinclusaoNew.getId());
                orcamentoTarefa.setIdUsuarioInclusao(idusuarioinclusaoNew);
            }
            orcamentoTarefa = em.merge(orcamentoTarefa);
            if (idtarefaOld != null && !idtarefaOld.equals(idtarefaNew)) {
                idtarefaOld.getOrcamentos().remove(orcamentoTarefa);
                idtarefaOld = em.merge(idtarefaOld);
            }
            if (idtarefaNew != null && !idtarefaNew.equals(idtarefaOld)) {
                idtarefaNew.getOrcamentos().add(orcamentoTarefa);
                idtarefaNew = em.merge(idtarefaNew);
            }
            if (idusuarioinclusaoOld != null && !idusuarioinclusaoOld.equals(idusuarioinclusaoNew)) {
                idusuarioinclusaoOld.getOrcamentoTarefaList().remove(orcamentoTarefa);
                idusuarioinclusaoOld = em.merge(idusuarioinclusaoOld);
            }
            if (idusuarioinclusaoNew != null && !idusuarioinclusaoNew.equals(idusuarioinclusaoOld)) {
                idusuarioinclusaoNew.getOrcamentoTarefaList().add(orcamentoTarefa);
                idusuarioinclusaoNew = em.merge(idusuarioinclusaoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = orcamentoTarefa.getId();
                if (findOrcamentoTarefa(id) == null) {
                    throw new NonexistentEntityException("The orcamentoTarefa with id " + id + " no longer exists.");
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
            OrcamentoTarefa orcamentoTarefa;
            try {
                orcamentoTarefa = em.getReference(OrcamentoTarefa.class, id);
                orcamentoTarefa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The orcamentoTarefa with id " + id + " no longer exists.", enfe);
            }
            Tarefa idtarefa = orcamentoTarefa.getIdTarefa();
            if (idtarefa != null) {
                idtarefa.getOrcamentos().remove(orcamentoTarefa);
                idtarefa = em.merge(idtarefa);
            }
            Usuario idusuarioinclusao = orcamentoTarefa.getIdUsuarioInclusao();
            if (idusuarioinclusao != null) {
                idusuarioinclusao.getOrcamentoTarefaList().remove(orcamentoTarefa);
                idusuarioinclusao = em.merge(idusuarioinclusao);
            }
            em.remove(orcamentoTarefa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<OrcamentoTarefa> findOrcamentoTarefaEntities() {
        return findOrcamentoTarefaEntities(true, -1, -1);
    }

    public List<OrcamentoTarefa> findOrcamentoTarefaEntities(int maxResults, int firstResult) {
        return findOrcamentoTarefaEntities(false, maxResults, firstResult);
    }

    private List<OrcamentoTarefa> findOrcamentoTarefaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(OrcamentoTarefa.class));
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

    public OrcamentoTarefa findOrcamentoTarefa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(OrcamentoTarefa.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrcamentoTarefaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<OrcamentoTarefa> rt = cq.from(OrcamentoTarefa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
