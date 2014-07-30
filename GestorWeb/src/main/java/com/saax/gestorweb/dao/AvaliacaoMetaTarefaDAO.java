package com.saax.gestorweb.dao;

import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import com.saax.gestorweb.model.datamodel.AvaliacaoMetaTarefa;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * DAO para o entity bean: AvaliacaoMetaTarefa <br><br>
 * 
 * @author rodrigo
 */
public class AvaliacaoMetaTarefaDAO implements Serializable {

    public AvaliacaoMetaTarefaDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AvaliacaoMetaTarefa avaliacaoMetaTarefa) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Meta idmeta = avaliacaoMetaTarefa.getIdMeta();
            if (idmeta != null) {
                idmeta = em.getReference(idmeta.getClass(), idmeta.getId());
                avaliacaoMetaTarefa.setIdMeta(idmeta);
            }
            Tarefa idtarefa = avaliacaoMetaTarefa.getIdTarefa();
            if (idtarefa != null) {
                idtarefa = em.getReference(idtarefa.getClass(), idtarefa.getId());
                avaliacaoMetaTarefa.setIdTarefa(idtarefa);
            }
            Usuario idusuarioinclusao = avaliacaoMetaTarefa.getIdUsuarioInclusao();
            if (idusuarioinclusao != null) {
                idusuarioinclusao = em.getReference(idusuarioinclusao.getClass(), idusuarioinclusao.getId());
                avaliacaoMetaTarefa.setIdUsuarioInclusao(idusuarioinclusao);
            }
            Usuario idusuarioavaliador = avaliacaoMetaTarefa.getIdUsuarioAvaliador();
            if (idusuarioavaliador != null) {
                idusuarioavaliador = em.getReference(idusuarioavaliador.getClass(), idusuarioavaliador.getId());
                avaliacaoMetaTarefa.setIdUsuarioAvaliador(idusuarioavaliador);
            }
            Usuario idusuarioavaliado = avaliacaoMetaTarefa.getIdUsuarioAvaliado();
            if (idusuarioavaliado != null) {
                idusuarioavaliado = em.getReference(idusuarioavaliado.getClass(), idusuarioavaliado.getId());
                avaliacaoMetaTarefa.setIdUsuarioAvaliado(idusuarioavaliado);
            }
            em.persist(avaliacaoMetaTarefa);
            if (idmeta != null) {
                idmeta.getAvaliacoes().add(avaliacaoMetaTarefa);
                idmeta = em.merge(idmeta);
            }
            if (idtarefa != null) {
                idtarefa.getAvaliacoes().add(avaliacaoMetaTarefa);
                idtarefa = em.merge(idtarefa);
            }
            if (idusuarioinclusao != null) {
                idusuarioinclusao.getAvaliacaoMetaTarefaList().add(avaliacaoMetaTarefa);
                idusuarioinclusao = em.merge(idusuarioinclusao);
            }
            if (idusuarioavaliador != null) {
                idusuarioavaliador.getAvaliacaoMetaTarefaList().add(avaliacaoMetaTarefa);
                idusuarioavaliador = em.merge(idusuarioavaliador);
            }
            if (idusuarioavaliado != null) {
                idusuarioavaliado.getAvaliacaoMetaTarefaList().add(avaliacaoMetaTarefa);
                idusuarioavaliado = em.merge(idusuarioavaliado);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AvaliacaoMetaTarefa avaliacaoMetaTarefa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AvaliacaoMetaTarefa persistentAvaliacaoMetaTarefa = em.find(AvaliacaoMetaTarefa.class, avaliacaoMetaTarefa.getId());
            Meta idmetaOld = persistentAvaliacaoMetaTarefa.getIdMeta();
            Meta idmetaNew = avaliacaoMetaTarefa.getIdMeta();
            Tarefa idtarefaOld = persistentAvaliacaoMetaTarefa.getIdTarefa();
            Tarefa idtarefaNew = avaliacaoMetaTarefa.getIdTarefa();
            Usuario idusuarioinclusaoOld = persistentAvaliacaoMetaTarefa.getIdUsuarioInclusao();
            Usuario idusuarioinclusaoNew = avaliacaoMetaTarefa.getIdUsuarioInclusao();
            Usuario idusuarioavaliadorOld = persistentAvaliacaoMetaTarefa.getIdUsuarioAvaliador();
            Usuario idusuarioavaliadorNew = avaliacaoMetaTarefa.getIdUsuarioAvaliador();
            Usuario idusuarioavaliadoOld = persistentAvaliacaoMetaTarefa.getIdUsuarioAvaliado();
            Usuario idusuarioavaliadoNew = avaliacaoMetaTarefa.getIdUsuarioAvaliado();
            if (idmetaNew != null) {
                idmetaNew = em.getReference(idmetaNew.getClass(), idmetaNew.getId());
                avaliacaoMetaTarefa.setIdMeta(idmetaNew);
            }
            if (idtarefaNew != null) {
                idtarefaNew = em.getReference(idtarefaNew.getClass(), idtarefaNew.getId());
                avaliacaoMetaTarefa.setIdTarefa(idtarefaNew);
            }
            if (idusuarioinclusaoNew != null) {
                idusuarioinclusaoNew = em.getReference(idusuarioinclusaoNew.getClass(), idusuarioinclusaoNew.getId());
                avaliacaoMetaTarefa.setIdUsuarioInclusao(idusuarioinclusaoNew);
            }
            if (idusuarioavaliadorNew != null) {
                idusuarioavaliadorNew = em.getReference(idusuarioavaliadorNew.getClass(), idusuarioavaliadorNew.getId());
                avaliacaoMetaTarefa.setIdUsuarioAvaliador(idusuarioavaliadorNew);
            }
            if (idusuarioavaliadoNew != null) {
                idusuarioavaliadoNew = em.getReference(idusuarioavaliadoNew.getClass(), idusuarioavaliadoNew.getId());
                avaliacaoMetaTarefa.setIdUsuarioAvaliado(idusuarioavaliadoNew);
            }
            avaliacaoMetaTarefa = em.merge(avaliacaoMetaTarefa);
            if (idmetaOld != null && !idmetaOld.equals(idmetaNew)) {
                idmetaOld.getAvaliacoes().remove(avaliacaoMetaTarefa);
                idmetaOld = em.merge(idmetaOld);
            }
            if (idmetaNew != null && !idmetaNew.equals(idmetaOld)) {
                idmetaNew.getAvaliacoes().add(avaliacaoMetaTarefa);
                idmetaNew = em.merge(idmetaNew);
            }
            if (idtarefaOld != null && !idtarefaOld.equals(idtarefaNew)) {
                idtarefaOld.getAvaliacoes().remove(avaliacaoMetaTarefa);
                idtarefaOld = em.merge(idtarefaOld);
            }
            if (idtarefaNew != null && !idtarefaNew.equals(idtarefaOld)) {
                idtarefaNew.getAvaliacoes().add(avaliacaoMetaTarefa);
                idtarefaNew = em.merge(idtarefaNew);
            }
            if (idusuarioinclusaoOld != null && !idusuarioinclusaoOld.equals(idusuarioinclusaoNew)) {
                idusuarioinclusaoOld.getAvaliacaoMetaTarefaList().remove(avaliacaoMetaTarefa);
                idusuarioinclusaoOld = em.merge(idusuarioinclusaoOld);
            }
            if (idusuarioinclusaoNew != null && !idusuarioinclusaoNew.equals(idusuarioinclusaoOld)) {
                idusuarioinclusaoNew.getAvaliacaoMetaTarefaList().add(avaliacaoMetaTarefa);
                idusuarioinclusaoNew = em.merge(idusuarioinclusaoNew);
            }
            if (idusuarioavaliadorOld != null && !idusuarioavaliadorOld.equals(idusuarioavaliadorNew)) {
                idusuarioavaliadorOld.getAvaliacaoMetaTarefaList().remove(avaliacaoMetaTarefa);
                idusuarioavaliadorOld = em.merge(idusuarioavaliadorOld);
            }
            if (idusuarioavaliadorNew != null && !idusuarioavaliadorNew.equals(idusuarioavaliadorOld)) {
                idusuarioavaliadorNew.getAvaliacaoMetaTarefaList().add(avaliacaoMetaTarefa);
                idusuarioavaliadorNew = em.merge(idusuarioavaliadorNew);
            }
            if (idusuarioavaliadoOld != null && !idusuarioavaliadoOld.equals(idusuarioavaliadoNew)) {
                idusuarioavaliadoOld.getAvaliacaoMetaTarefaList().remove(avaliacaoMetaTarefa);
                idusuarioavaliadoOld = em.merge(idusuarioavaliadoOld);
            }
            if (idusuarioavaliadoNew != null && !idusuarioavaliadoNew.equals(idusuarioavaliadoOld)) {
                idusuarioavaliadoNew.getAvaliacaoMetaTarefaList().add(avaliacaoMetaTarefa);
                idusuarioavaliadoNew = em.merge(idusuarioavaliadoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = avaliacaoMetaTarefa.getId();
                if (findAvaliacaoMetaTarefa(id) == null) {
                    throw new NonexistentEntityException("The avaliacaoMetaTarefa with id " + id + " no longer exists.");
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
            AvaliacaoMetaTarefa avaliacaoMetaTarefa;
            try {
                avaliacaoMetaTarefa = em.getReference(AvaliacaoMetaTarefa.class, id);
                avaliacaoMetaTarefa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The avaliacaoMetaTarefa with id " + id + " no longer exists.", enfe);
            }
            Meta idmeta = avaliacaoMetaTarefa.getIdMeta();
            if (idmeta != null) {
                idmeta.getAvaliacoes().remove(avaliacaoMetaTarefa);
                idmeta = em.merge(idmeta);
            }
            Tarefa idtarefa = avaliacaoMetaTarefa.getIdTarefa();
            if (idtarefa != null) {
                idtarefa.getAvaliacoes().remove(avaliacaoMetaTarefa);
                idtarefa = em.merge(idtarefa);
            }
            Usuario idusuarioinclusao = avaliacaoMetaTarefa.getIdUsuarioInclusao();
            if (idusuarioinclusao != null) {
                idusuarioinclusao.getAvaliacaoMetaTarefaList().remove(avaliacaoMetaTarefa);
                idusuarioinclusao = em.merge(idusuarioinclusao);
            }
            Usuario idusuarioavaliador = avaliacaoMetaTarefa.getIdUsuarioAvaliador();
            if (idusuarioavaliador != null) {
                idusuarioavaliador.getAvaliacaoMetaTarefaList().remove(avaliacaoMetaTarefa);
                idusuarioavaliador = em.merge(idusuarioavaliador);
            }
            Usuario idusuarioavaliado = avaliacaoMetaTarefa.getIdUsuarioAvaliado();
            if (idusuarioavaliado != null) {
                idusuarioavaliado.getAvaliacaoMetaTarefaList().remove(avaliacaoMetaTarefa);
                idusuarioavaliado = em.merge(idusuarioavaliado);
            }
            em.remove(avaliacaoMetaTarefa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AvaliacaoMetaTarefa> findAvaliacaoMetaTarefaEntities() {
        return findAvaliacaoMetaTarefaEntities(true, -1, -1);
    }

    public List<AvaliacaoMetaTarefa> findAvaliacaoMetaTarefaEntities(int maxResults, int firstResult) {
        return findAvaliacaoMetaTarefaEntities(false, maxResults, firstResult);
    }

    private List<AvaliacaoMetaTarefa> findAvaliacaoMetaTarefaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AvaliacaoMetaTarefa.class));
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

    public AvaliacaoMetaTarefa findAvaliacaoMetaTarefa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AvaliacaoMetaTarefa.class, id);
        } finally {
            em.close();
        }
    }

    public int getAvaliacaoMetaTarefaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AvaliacaoMetaTarefa> rt = cq.from(AvaliacaoMetaTarefa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
