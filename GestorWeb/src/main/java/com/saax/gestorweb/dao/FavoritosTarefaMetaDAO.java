package com.saax.gestorweb.dao;

import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import com.saax.gestorweb.model.datamodel.FavoritosTarefaMeta;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * DAO para o entity bean: FavoritosTarefaMeta <br><br>
 * 
 * @author rodrigo
 */
public class FavoritosTarefaMetaDAO implements Serializable {

    public FavoritosTarefaMetaDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * metodo padrao modificado para gravar data/hora de inclusao
     * @param favoritosTarefaMeta 
     */
    public void create(FavoritosTarefaMeta favoritosTarefaMeta) {
        favoritosTarefaMeta.setDataHoraInclusao(LocalDateTime.now());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Meta meta = favoritosTarefaMeta.getMeta();
            if (meta != null) {
                meta = em.getReference(meta.getClass(), meta.getId());
                favoritosTarefaMeta.setMeta(meta);
            }
            Tarefa tarefa = favoritosTarefaMeta.getTarefa();
            if (tarefa != null) {
                tarefa = em.getReference(tarefa.getClass(), tarefa.getId());
                favoritosTarefaMeta.setTarefa(tarefa);
            }
            Usuario usuarioInclusao = favoritosTarefaMeta.getUsuarioInclusao();
            if (usuarioInclusao != null) {
                usuarioInclusao = em.getReference(usuarioInclusao.getClass(), usuarioInclusao.getId());
                favoritosTarefaMeta.setUsuarioInclusao(usuarioInclusao);
            }
            em.persist(favoritosTarefaMeta);
            if (meta != null) {
                meta.getFavoritados().add(favoritosTarefaMeta);
                meta = em.merge(meta);
            }
            if (tarefa != null) {
                tarefa.getFavoritados().add(favoritosTarefaMeta);
                tarefa = em.merge(tarefa);
            }
            if (usuarioInclusao != null) {
                usuarioInclusao.getFavoritosIncluidos().add(favoritosTarefaMeta);
                usuarioInclusao = em.merge(usuarioInclusao);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(FavoritosTarefaMeta favoritosTarefaMeta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FavoritosTarefaMeta persistentFavoritosTarefaMeta = em.find(FavoritosTarefaMeta.class, favoritosTarefaMeta.getId());
            Meta metaOld = persistentFavoritosTarefaMeta.getMeta();
            Meta metaNew = favoritosTarefaMeta.getMeta();
            Tarefa tarefaOld = persistentFavoritosTarefaMeta.getTarefa();
            Tarefa tarefaNew = favoritosTarefaMeta.getTarefa();
            Usuario usuarioInclusaoOld = persistentFavoritosTarefaMeta.getUsuarioInclusao();
            Usuario usuarioInclusaoNew = favoritosTarefaMeta.getUsuarioInclusao();
            if (metaNew != null) {
                metaNew = em.getReference(metaNew.getClass(), metaNew.getId());
                favoritosTarefaMeta.setMeta(metaNew);
            }
            if (tarefaNew != null) {
                tarefaNew = em.getReference(tarefaNew.getClass(), tarefaNew.getId());
                favoritosTarefaMeta.setTarefa(tarefaNew);
            }
            if (usuarioInclusaoNew != null) {
                usuarioInclusaoNew = em.getReference(usuarioInclusaoNew.getClass(), usuarioInclusaoNew.getId());
                favoritosTarefaMeta.setUsuarioInclusao(usuarioInclusaoNew);
            }
            favoritosTarefaMeta = em.merge(favoritosTarefaMeta);
            if (metaOld != null && !metaOld.equals(metaNew)) {
                metaOld.getFavoritados().remove(favoritosTarefaMeta);
                metaOld = em.merge(metaOld);
            }
            if (metaNew != null && !metaNew.equals(metaOld)) {
                metaNew.getFavoritados().add(favoritosTarefaMeta);
                metaNew = em.merge(metaNew);
            }
            if (tarefaOld != null && !tarefaOld.equals(tarefaNew)) {
                tarefaOld.getFavoritados().remove(favoritosTarefaMeta);
                tarefaOld = em.merge(tarefaOld);
            }
            if (tarefaNew != null && !tarefaNew.equals(tarefaOld)) {
                tarefaNew.getFavoritados().add(favoritosTarefaMeta);
                tarefaNew = em.merge(tarefaNew);
            }
            if (usuarioInclusaoOld != null && !usuarioInclusaoOld.equals(usuarioInclusaoNew)) {
                usuarioInclusaoOld.getFavoritosIncluidos().remove(favoritosTarefaMeta);
                usuarioInclusaoOld = em.merge(usuarioInclusaoOld);
            }
            if (usuarioInclusaoNew != null && !usuarioInclusaoNew.equals(usuarioInclusaoOld)) {
                usuarioInclusaoNew.getFavoritosIncluidos().add(favoritosTarefaMeta);
                usuarioInclusaoNew = em.merge(usuarioInclusaoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = favoritosTarefaMeta.getId();
                if (findFavoritosTarefaMeta(id) == null) {
                    throw new NonexistentEntityException("The favoritosTarefaMeta with id " + id + " no longer exists.");
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
            FavoritosTarefaMeta favoritosTarefaMeta;
            try {
                favoritosTarefaMeta = em.getReference(FavoritosTarefaMeta.class, id);
                favoritosTarefaMeta.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The favoritosTarefaMeta with id " + id + " no longer exists.", enfe);
            }
            Meta meta = favoritosTarefaMeta.getMeta();
            if (meta != null) {
                meta.getFavoritados().remove(favoritosTarefaMeta);
                meta = em.merge(meta);
            }
            Tarefa tarefa = favoritosTarefaMeta.getTarefa();
            if (tarefa != null) {
                tarefa.getFavoritados().remove(favoritosTarefaMeta);
                tarefa = em.merge(tarefa);
            }
            Usuario usuarioInclusao = favoritosTarefaMeta.getUsuarioInclusao();
            if (usuarioInclusao != null) {
                usuarioInclusao.getFavoritosIncluidos().remove(favoritosTarefaMeta);
                usuarioInclusao = em.merge(usuarioInclusao);
            }
            em.remove(favoritosTarefaMeta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<FavoritosTarefaMeta> findFavoritosTarefaMetaEntities() {
        return findFavoritosTarefaMetaEntities(true, -1, -1);
    }

    public List<FavoritosTarefaMeta> findFavoritosTarefaMetaEntities(int maxResults, int firstResult) {
        return findFavoritosTarefaMetaEntities(false, maxResults, firstResult);
    }

    private List<FavoritosTarefaMeta> findFavoritosTarefaMetaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FavoritosTarefaMeta.class));
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

    public FavoritosTarefaMeta findFavoritosTarefaMeta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FavoritosTarefaMeta.class, id);
        } finally {
            em.close();
        }
    }

    public int getFavoritosTarefaMetaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FavoritosTarefaMeta> rt = cq.from(FavoritosTarefaMeta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
