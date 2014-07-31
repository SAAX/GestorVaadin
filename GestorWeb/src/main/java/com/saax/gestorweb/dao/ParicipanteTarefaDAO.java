package com.saax.gestorweb.dao;

import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import com.saax.gestorweb.model.datamodel.ParicipanteTarefa;
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
 * DAO para o entity bean: ParicipanteTarefa <br><br>
 * 
 * @author rodrigo
 */
public class ParicipanteTarefaDAO implements Serializable {

    public ParicipanteTarefaDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ParicipanteTarefa paricipanteTarefa) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarefa tarefa = paricipanteTarefa.getTarefa();
            if (tarefa != null) {
                tarefa = em.getReference(tarefa.getClass(), tarefa.getId());
                paricipanteTarefa.setTarefa(tarefa);
            }
            Usuario usuarioInclusao = paricipanteTarefa.getUsuarioInclusao();
            if (usuarioInclusao != null) {
                usuarioInclusao = em.getReference(usuarioInclusao.getClass(), usuarioInclusao.getId());
                paricipanteTarefa.setUsuarioInclusao(usuarioInclusao);
            }
            Usuario usuarioParticipante = paricipanteTarefa.getUsuarioParticipante();
            if (usuarioParticipante != null) {
                usuarioParticipante = em.getReference(usuarioParticipante.getClass(), usuarioParticipante.getId());
                paricipanteTarefa.setUsuarioParticipante(usuarioParticipante);
            }
            em.persist(paricipanteTarefa);
            if (tarefa != null) {
                tarefa.getParicipantes().add(paricipanteTarefa);
                tarefa = em.merge(tarefa);
            }
            if (usuarioInclusao != null) {
                usuarioInclusao.getParicipacoesIncluidas().add(paricipanteTarefa);
                usuarioInclusao = em.merge(usuarioInclusao);
            }
            if (usuarioParticipante != null) {
                usuarioParticipante.getParicipacoesIncluidas().add(paricipanteTarefa);
                usuarioParticipante = em.merge(usuarioParticipante);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ParicipanteTarefa paricipanteTarefa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ParicipanteTarefa persistentParicipanteTarefa = em.find(ParicipanteTarefa.class, paricipanteTarefa.getId());
            Tarefa tarefaOld = persistentParicipanteTarefa.getTarefa();
            Tarefa tarefaNew = paricipanteTarefa.getTarefa();
            Usuario usuarioInclusaoOld = persistentParicipanteTarefa.getUsuarioInclusao();
            Usuario usuarioInclusaoNew = paricipanteTarefa.getUsuarioInclusao();
            Usuario usuarioParticipanteOld = persistentParicipanteTarefa.getUsuarioParticipante();
            Usuario usuarioParticipanteNew = paricipanteTarefa.getUsuarioParticipante();
            if (tarefaNew != null) {
                tarefaNew = em.getReference(tarefaNew.getClass(), tarefaNew.getId());
                paricipanteTarefa.setTarefa(tarefaNew);
            }
            if (usuarioInclusaoNew != null) {
                usuarioInclusaoNew = em.getReference(usuarioInclusaoNew.getClass(), usuarioInclusaoNew.getId());
                paricipanteTarefa.setUsuarioInclusao(usuarioInclusaoNew);
            }
            if (usuarioParticipanteNew != null) {
                usuarioParticipanteNew = em.getReference(usuarioParticipanteNew.getClass(), usuarioParticipanteNew.getId());
                paricipanteTarefa.setUsuarioParticipante(usuarioParticipanteNew);
            }
            paricipanteTarefa = em.merge(paricipanteTarefa);
            if (tarefaOld != null && !tarefaOld.equals(tarefaNew)) {
                tarefaOld.getParicipantes().remove(paricipanteTarefa);
                tarefaOld = em.merge(tarefaOld);
            }
            if (tarefaNew != null && !tarefaNew.equals(tarefaOld)) {
                tarefaNew.getParicipantes().add(paricipanteTarefa);
                tarefaNew = em.merge(tarefaNew);
            }
            if (usuarioInclusaoOld != null && !usuarioInclusaoOld.equals(usuarioInclusaoNew)) {
                usuarioInclusaoOld.getParicipacoesIncluidas().remove(paricipanteTarefa);
                usuarioInclusaoOld = em.merge(usuarioInclusaoOld);
            }
            if (usuarioInclusaoNew != null && !usuarioInclusaoNew.equals(usuarioInclusaoOld)) {
                usuarioInclusaoNew.getParicipacoesIncluidas().add(paricipanteTarefa);
                usuarioInclusaoNew = em.merge(usuarioInclusaoNew);
            }
            if (usuarioParticipanteOld != null && !usuarioParticipanteOld.equals(usuarioParticipanteNew)) {
                usuarioParticipanteOld.getParicipacoesIncluidas().remove(paricipanteTarefa);
                usuarioParticipanteOld = em.merge(usuarioParticipanteOld);
            }
            if (usuarioParticipanteNew != null && !usuarioParticipanteNew.equals(usuarioParticipanteOld)) {
                usuarioParticipanteNew.getParicipacoesIncluidas().add(paricipanteTarefa);
                usuarioParticipanteNew = em.merge(usuarioParticipanteNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = paricipanteTarefa.getId();
                if (findParicipanteTarefa(id) == null) {
                    throw new NonexistentEntityException("The paricipanteTarefa with id " + id + " no longer exists.");
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
            ParicipanteTarefa paricipanteTarefa;
            try {
                paricipanteTarefa = em.getReference(ParicipanteTarefa.class, id);
                paricipanteTarefa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The paricipanteTarefa with id " + id + " no longer exists.", enfe);
            }
            Tarefa tarefa = paricipanteTarefa.getTarefa();
            if (tarefa != null) {
                tarefa.getParicipantes().remove(paricipanteTarefa);
                tarefa = em.merge(tarefa);
            }
            Usuario usuarioInclusao = paricipanteTarefa.getUsuarioInclusao();
            if (usuarioInclusao != null) {
                usuarioInclusao.getParicipacoesIncluidas().remove(paricipanteTarefa);
                usuarioInclusao = em.merge(usuarioInclusao);
            }
            Usuario usuarioParticipante = paricipanteTarefa.getUsuarioParticipante();
            if (usuarioParticipante != null) {
                usuarioParticipante.getParicipacoesIncluidas().remove(paricipanteTarefa);
                usuarioParticipante = em.merge(usuarioParticipante);
            }
            em.remove(paricipanteTarefa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ParicipanteTarefa> findParicipanteTarefaEntities() {
        return findParicipanteTarefaEntities(true, -1, -1);
    }

    public List<ParicipanteTarefa> findParicipanteTarefaEntities(int maxResults, int firstResult) {
        return findParicipanteTarefaEntities(false, maxResults, firstResult);
    }

    private List<ParicipanteTarefa> findParicipanteTarefaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ParicipanteTarefa.class));
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

    public ParicipanteTarefa findParicipanteTarefa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ParicipanteTarefa.class, id);
        } finally {
            em.close();
        }
    }

    public int getParicipanteTarefaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ParicipanteTarefa> rt = cq.from(ParicipanteTarefa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
