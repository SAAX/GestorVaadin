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
import java.time.LocalDateTime;
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

    /**
     * metodo padrao modificado para gravar data/hora de inclusao
     * @param orcamentoTarefa 
     */
    public void create(OrcamentoTarefa orcamentoTarefa) {
        orcamentoTarefa.setDataHoraInclusao(LocalDateTime.now());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarefa tarefa = orcamentoTarefa.getTarefa();
            if (tarefa != null) {
                tarefa = em.getReference(tarefa.getClass(), tarefa.getId());
                orcamentoTarefa.setTarefa(tarefa);
            }
            Usuario usuarioInclusao = orcamentoTarefa.getUsuarioInclusao();
            if (usuarioInclusao != null) {
                usuarioInclusao = em.getReference(usuarioInclusao.getClass(), usuarioInclusao.getId());
                orcamentoTarefa.setUsuarioInclusao(usuarioInclusao);
            }
            em.persist(orcamentoTarefa);
            if (tarefa != null) {
                tarefa.getOrcamentos().add(orcamentoTarefa);
                tarefa = em.merge(tarefa);
            }
            if (usuarioInclusao != null) {
                usuarioInclusao.getOrcamentosIncluidos().add(orcamentoTarefa);
                usuarioInclusao = em.merge(usuarioInclusao);
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
            Tarefa tarefaOld = persistentOrcamentoTarefa.getTarefa();
            Tarefa tarefaNew = orcamentoTarefa.getTarefa();
            Usuario usuarioInclusaoOld = persistentOrcamentoTarefa.getUsuarioInclusao();
            Usuario usuarioInclusaoNew = orcamentoTarefa.getUsuarioInclusao();
            if (tarefaNew != null) {
                tarefaNew = em.getReference(tarefaNew.getClass(), tarefaNew.getId());
                orcamentoTarefa.setTarefa(tarefaNew);
            }
            if (usuarioInclusaoNew != null) {
                usuarioInclusaoNew = em.getReference(usuarioInclusaoNew.getClass(), usuarioInclusaoNew.getId());
                orcamentoTarefa.setUsuarioInclusao(usuarioInclusaoNew);
            }
            orcamentoTarefa = em.merge(orcamentoTarefa);
            if (tarefaOld != null && !tarefaOld.equals(tarefaNew)) {
                tarefaOld.getOrcamentos().remove(orcamentoTarefa);
                tarefaOld = em.merge(tarefaOld);
            }
            if (tarefaNew != null && !tarefaNew.equals(tarefaOld)) {
                tarefaNew.getOrcamentos().add(orcamentoTarefa);
                tarefaNew = em.merge(tarefaNew);
            }
            if (usuarioInclusaoOld != null && !usuarioInclusaoOld.equals(usuarioInclusaoNew)) {
                usuarioInclusaoOld.getOrcamentosIncluidos().remove(orcamentoTarefa);
                usuarioInclusaoOld = em.merge(usuarioInclusaoOld);
            }
            if (usuarioInclusaoNew != null && !usuarioInclusaoNew.equals(usuarioInclusaoOld)) {
                usuarioInclusaoNew.getOrcamentosIncluidos().add(orcamentoTarefa);
                usuarioInclusaoNew = em.merge(usuarioInclusaoNew);
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
            Tarefa tarefa = orcamentoTarefa.getTarefa();
            if (tarefa != null) {
                tarefa.getOrcamentos().remove(orcamentoTarefa);
                tarefa = em.merge(tarefa);
            }
            Usuario usuarioInclusao = orcamentoTarefa.getUsuarioInclusao();
            if (usuarioInclusao != null) {
                usuarioInclusao.getOrcamentosIncluidos().remove(orcamentoTarefa);
                usuarioInclusao = em.merge(usuarioInclusao);
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
