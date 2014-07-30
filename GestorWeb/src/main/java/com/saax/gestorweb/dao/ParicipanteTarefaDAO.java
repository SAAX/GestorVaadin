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
            Tarefa idtarefa = paricipanteTarefa.getIdTarefa();
            if (idtarefa != null) {
                idtarefa = em.getReference(idtarefa.getClass(), idtarefa.getId());
                paricipanteTarefa.setIdTarefa(idtarefa);
            }
            Usuario idusuarioinclusao = paricipanteTarefa.getIdUsuarioInclusao();
            if (idusuarioinclusao != null) {
                idusuarioinclusao = em.getReference(idusuarioinclusao.getClass(), idusuarioinclusao.getId());
                paricipanteTarefa.setIdUsuarioInclusao(idusuarioinclusao);
            }
            Usuario idusuarioparticipante = paricipanteTarefa.getIdUsuarioParticipante();
            if (idusuarioparticipante != null) {
                idusuarioparticipante = em.getReference(idusuarioparticipante.getClass(), idusuarioparticipante.getId());
                paricipanteTarefa.setIdUsuarioParticipante(idusuarioparticipante);
            }
            em.persist(paricipanteTarefa);
            if (idtarefa != null) {
                idtarefa.getParicipantes().add(paricipanteTarefa);
                idtarefa = em.merge(idtarefa);
            }
            if (idusuarioinclusao != null) {
                idusuarioinclusao.getParicipanteTarefaList().add(paricipanteTarefa);
                idusuarioinclusao = em.merge(idusuarioinclusao);
            }
            if (idusuarioparticipante != null) {
                idusuarioparticipante.getParicipanteTarefaList().add(paricipanteTarefa);
                idusuarioparticipante = em.merge(idusuarioparticipante);
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
            Tarefa idtarefaOld = persistentParicipanteTarefa.getIdTarefa();
            Tarefa idtarefaNew = paricipanteTarefa.getIdTarefa();
            Usuario idusuarioinclusaoOld = persistentParicipanteTarefa.getIdUsuarioInclusao();
            Usuario idusuarioinclusaoNew = paricipanteTarefa.getIdUsuarioInclusao();
            Usuario idusuarioparticipanteOld = persistentParicipanteTarefa.getIdUsuarioParticipante();
            Usuario idusuarioparticipanteNew = paricipanteTarefa.getIdUsuarioParticipante();
            if (idtarefaNew != null) {
                idtarefaNew = em.getReference(idtarefaNew.getClass(), idtarefaNew.getId());
                paricipanteTarefa.setIdTarefa(idtarefaNew);
            }
            if (idusuarioinclusaoNew != null) {
                idusuarioinclusaoNew = em.getReference(idusuarioinclusaoNew.getClass(), idusuarioinclusaoNew.getId());
                paricipanteTarefa.setIdUsuarioInclusao(idusuarioinclusaoNew);
            }
            if (idusuarioparticipanteNew != null) {
                idusuarioparticipanteNew = em.getReference(idusuarioparticipanteNew.getClass(), idusuarioparticipanteNew.getId());
                paricipanteTarefa.setIdUsuarioParticipante(idusuarioparticipanteNew);
            }
            paricipanteTarefa = em.merge(paricipanteTarefa);
            if (idtarefaOld != null && !idtarefaOld.equals(idtarefaNew)) {
                idtarefaOld.getParicipantes().remove(paricipanteTarefa);
                idtarefaOld = em.merge(idtarefaOld);
            }
            if (idtarefaNew != null && !idtarefaNew.equals(idtarefaOld)) {
                idtarefaNew.getParicipantes().add(paricipanteTarefa);
                idtarefaNew = em.merge(idtarefaNew);
            }
            if (idusuarioinclusaoOld != null && !idusuarioinclusaoOld.equals(idusuarioinclusaoNew)) {
                idusuarioinclusaoOld.getParicipanteTarefaList().remove(paricipanteTarefa);
                idusuarioinclusaoOld = em.merge(idusuarioinclusaoOld);
            }
            if (idusuarioinclusaoNew != null && !idusuarioinclusaoNew.equals(idusuarioinclusaoOld)) {
                idusuarioinclusaoNew.getParicipanteTarefaList().add(paricipanteTarefa);
                idusuarioinclusaoNew = em.merge(idusuarioinclusaoNew);
            }
            if (idusuarioparticipanteOld != null && !idusuarioparticipanteOld.equals(idusuarioparticipanteNew)) {
                idusuarioparticipanteOld.getParicipanteTarefaList().remove(paricipanteTarefa);
                idusuarioparticipanteOld = em.merge(idusuarioparticipanteOld);
            }
            if (idusuarioparticipanteNew != null && !idusuarioparticipanteNew.equals(idusuarioparticipanteOld)) {
                idusuarioparticipanteNew.getParicipanteTarefaList().add(paricipanteTarefa);
                idusuarioparticipanteNew = em.merge(idusuarioparticipanteNew);
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
            Tarefa idtarefa = paricipanteTarefa.getIdTarefa();
            if (idtarefa != null) {
                idtarefa.getParicipantes().remove(paricipanteTarefa);
                idtarefa = em.merge(idtarefa);
            }
            Usuario idusuarioinclusao = paricipanteTarefa.getIdUsuarioInclusao();
            if (idusuarioinclusao != null) {
                idusuarioinclusao.getParicipanteTarefaList().remove(paricipanteTarefa);
                idusuarioinclusao = em.merge(idusuarioinclusao);
            }
            Usuario idusuarioparticipante = paricipanteTarefa.getIdUsuarioParticipante();
            if (idusuarioparticipante != null) {
                idusuarioparticipante.getParicipanteTarefaList().remove(paricipanteTarefa);
                idusuarioparticipante = em.merge(idusuarioparticipante);
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
