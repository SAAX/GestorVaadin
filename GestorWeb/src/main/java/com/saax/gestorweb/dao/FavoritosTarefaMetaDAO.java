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

    public void create(FavoritosTarefaMeta favoritosTarefaMeta) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Meta idmeta = favoritosTarefaMeta.getIdmeta();
            if (idmeta != null) {
                idmeta = em.getReference(idmeta.getClass(), idmeta.getId());
                favoritosTarefaMeta.setIdmeta(idmeta);
            }
            Tarefa idtarefa = favoritosTarefaMeta.getIdtarefa();
            if (idtarefa != null) {
                idtarefa = em.getReference(idtarefa.getClass(), idtarefa.getId());
                favoritosTarefaMeta.setIdtarefa(idtarefa);
            }
            Usuario idusuarioinclusao = favoritosTarefaMeta.getIdusuarioinclusao();
            if (idusuarioinclusao != null) {
                idusuarioinclusao = em.getReference(idusuarioinclusao.getClass(), idusuarioinclusao.getId());
                favoritosTarefaMeta.setIdusuarioinclusao(idusuarioinclusao);
            }
            em.persist(favoritosTarefaMeta);
            if (idmeta != null) {
                idmeta.getFavoritosTarefaMetaList().add(favoritosTarefaMeta);
                idmeta = em.merge(idmeta);
            }
            if (idtarefa != null) {
                idtarefa.getFavoritosTarefaMetaList().add(favoritosTarefaMeta);
                idtarefa = em.merge(idtarefa);
            }
            if (idusuarioinclusao != null) {
                idusuarioinclusao.getFavoritosTarefaMetaList().add(favoritosTarefaMeta);
                idusuarioinclusao = em.merge(idusuarioinclusao);
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
            Meta idmetaOld = persistentFavoritosTarefaMeta.getIdmeta();
            Meta idmetaNew = favoritosTarefaMeta.getIdmeta();
            Tarefa idtarefaOld = persistentFavoritosTarefaMeta.getIdtarefa();
            Tarefa idtarefaNew = favoritosTarefaMeta.getIdtarefa();
            Usuario idusuarioinclusaoOld = persistentFavoritosTarefaMeta.getIdusuarioinclusao();
            Usuario idusuarioinclusaoNew = favoritosTarefaMeta.getIdusuarioinclusao();
            if (idmetaNew != null) {
                idmetaNew = em.getReference(idmetaNew.getClass(), idmetaNew.getId());
                favoritosTarefaMeta.setIdmeta(idmetaNew);
            }
            if (idtarefaNew != null) {
                idtarefaNew = em.getReference(idtarefaNew.getClass(), idtarefaNew.getId());
                favoritosTarefaMeta.setIdtarefa(idtarefaNew);
            }
            if (idusuarioinclusaoNew != null) {
                idusuarioinclusaoNew = em.getReference(idusuarioinclusaoNew.getClass(), idusuarioinclusaoNew.getId());
                favoritosTarefaMeta.setIdusuarioinclusao(idusuarioinclusaoNew);
            }
            favoritosTarefaMeta = em.merge(favoritosTarefaMeta);
            if (idmetaOld != null && !idmetaOld.equals(idmetaNew)) {
                idmetaOld.getFavoritosTarefaMetaList().remove(favoritosTarefaMeta);
                idmetaOld = em.merge(idmetaOld);
            }
            if (idmetaNew != null && !idmetaNew.equals(idmetaOld)) {
                idmetaNew.getFavoritosTarefaMetaList().add(favoritosTarefaMeta);
                idmetaNew = em.merge(idmetaNew);
            }
            if (idtarefaOld != null && !idtarefaOld.equals(idtarefaNew)) {
                idtarefaOld.getFavoritosTarefaMetaList().remove(favoritosTarefaMeta);
                idtarefaOld = em.merge(idtarefaOld);
            }
            if (idtarefaNew != null && !idtarefaNew.equals(idtarefaOld)) {
                idtarefaNew.getFavoritosTarefaMetaList().add(favoritosTarefaMeta);
                idtarefaNew = em.merge(idtarefaNew);
            }
            if (idusuarioinclusaoOld != null && !idusuarioinclusaoOld.equals(idusuarioinclusaoNew)) {
                idusuarioinclusaoOld.getFavoritosTarefaMetaList().remove(favoritosTarefaMeta);
                idusuarioinclusaoOld = em.merge(idusuarioinclusaoOld);
            }
            if (idusuarioinclusaoNew != null && !idusuarioinclusaoNew.equals(idusuarioinclusaoOld)) {
                idusuarioinclusaoNew.getFavoritosTarefaMetaList().add(favoritosTarefaMeta);
                idusuarioinclusaoNew = em.merge(idusuarioinclusaoNew);
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
            Meta idmeta = favoritosTarefaMeta.getIdmeta();
            if (idmeta != null) {
                idmeta.getFavoritosTarefaMetaList().remove(favoritosTarefaMeta);
                idmeta = em.merge(idmeta);
            }
            Tarefa idtarefa = favoritosTarefaMeta.getIdtarefa();
            if (idtarefa != null) {
                idtarefa.getFavoritosTarefaMetaList().remove(favoritosTarefaMeta);
                idtarefa = em.merge(idtarefa);
            }
            Usuario idusuarioinclusao = favoritosTarefaMeta.getIdusuarioinclusao();
            if (idusuarioinclusao != null) {
                idusuarioinclusao.getFavoritosTarefaMetaList().remove(favoritosTarefaMeta);
                idusuarioinclusao = em.merge(idusuarioinclusao);
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
