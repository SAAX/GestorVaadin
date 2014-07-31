package com.saax.gestorweb.dao;

import com.saax.gestorweb.model.datamodel.StatusTarefa;
import com.saax.gestorweb.dao.exceptions.IllegalOrphanException;
import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import com.saax.gestorweb.dao.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.saax.gestorweb.model.datamodel.Tarefa;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * DAO para o entity bean: StatusTarefa <br><br>
 * 
 * @author rodrigo
 */
public class StatusTarefaDAO implements Serializable {

    public StatusTarefaDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(StatusTarefa statustarefa) throws PreexistingEntityException, Exception {
        if (statustarefa.getTarefas() == null) {
            statustarefa.setTarefas(new HashSet<Tarefa>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Set<Tarefa> attachedTarefaSet = new HashSet<Tarefa>();
            for (Tarefa tarefaSetTarefaToAttach : statustarefa.getTarefas()) {
                tarefaSetTarefaToAttach = em.getReference(tarefaSetTarefaToAttach.getClass(), tarefaSetTarefaToAttach.getId());
                attachedTarefaSet.add(tarefaSetTarefaToAttach);
            }
            statustarefa.setTarefas(attachedTarefaSet);
            em.persist(statustarefa);
            for (Tarefa tarefaSetTarefa : statustarefa.getTarefas()) {
                StatusTarefa oldStatusOfTarefaSetTarefa = tarefaSetTarefa.getStatus();
                tarefaSetTarefa.setStatus(statustarefa);
                tarefaSetTarefa = em.merge(tarefaSetTarefa);
                if (oldStatusOfTarefaSetTarefa != null) {
                    oldStatusOfTarefaSetTarefa.getTarefas().remove(tarefaSetTarefa);
                    oldStatusOfTarefaSetTarefa = em.merge(oldStatusOfTarefaSetTarefa);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findStatustarefa(statustarefa.getStatustarefa()) != null) {
                throw new PreexistingEntityException("Statustarefa " + statustarefa + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(StatusTarefa statustarefa) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            StatusTarefa persistentStatustarefa = em.find(StatusTarefa.class, statustarefa.getStatustarefa());
            Set<Tarefa> tarefaSetOld = persistentStatustarefa.getTarefas();
            Set<Tarefa> tarefaSetNew = statustarefa.getTarefas();
            List<String> illegalOrphanMessages = null;
            for (Tarefa tarefaSetOldTarefa : tarefaSetOld) {
                if (!tarefaSetNew.contains(tarefaSetOldTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tarefa " + tarefaSetOldTarefa + " since its status field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Set<Tarefa> attachedTarefaSetNew = new HashSet<Tarefa>();
            for (Tarefa tarefaSetNewTarefaToAttach : tarefaSetNew) {
                tarefaSetNewTarefaToAttach = em.getReference(tarefaSetNewTarefaToAttach.getClass(), tarefaSetNewTarefaToAttach.getId());
                attachedTarefaSetNew.add(tarefaSetNewTarefaToAttach);
            }
            tarefaSetNew = attachedTarefaSetNew;
            statustarefa.setTarefas(tarefaSetNew);
            statustarefa = em.merge(statustarefa);
            for (Tarefa tarefaSetNewTarefa : tarefaSetNew) {
                if (!tarefaSetOld.contains(tarefaSetNewTarefa)) {
                    StatusTarefa oldStatusOfTarefaSetNewTarefa = tarefaSetNewTarefa.getStatus();
                    tarefaSetNewTarefa.setStatus(statustarefa);
                    tarefaSetNewTarefa = em.merge(tarefaSetNewTarefa);
                    if (oldStatusOfTarefaSetNewTarefa != null && !oldStatusOfTarefaSetNewTarefa.equals(statustarefa)) {
                        oldStatusOfTarefaSetNewTarefa.getTarefas().remove(tarefaSetNewTarefa);
                        oldStatusOfTarefaSetNewTarefa = em.merge(oldStatusOfTarefaSetNewTarefa);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = statustarefa.getStatustarefa();
                if (findStatustarefa(id) == null) {
                    throw new NonexistentEntityException("The statustarefa with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            StatusTarefa statustarefa;
            try {
                statustarefa = em.getReference(StatusTarefa.class, id);
                statustarefa.getStatustarefa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The statustarefa with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Set<Tarefa> tarefaSetOrphanCheck = statustarefa.getTarefas();
            for (Tarefa tarefaSetOrphanCheckTarefa : tarefaSetOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Statustarefa (" + statustarefa + ") cannot be destroyed since the Tarefa " + tarefaSetOrphanCheckTarefa + " in its tarefaSet field has a non-nullable status field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(statustarefa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<StatusTarefa> findStatustarefaEntities() {
        return findStatustarefaEntities(true, -1, -1);
    }

    public List<StatusTarefa> findStatustarefaEntities(int maxResults, int firstResult) {
        return findStatustarefaEntities(false, maxResults, firstResult);
    }

    private List<StatusTarefa> findStatustarefaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(StatusTarefa.class));
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

    public StatusTarefa findStatustarefa(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(StatusTarefa.class, id);
        } finally {
            em.close();
        }
    }

    public int getStatustarefaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<StatusTarefa> rt = cq.from(StatusTarefa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
