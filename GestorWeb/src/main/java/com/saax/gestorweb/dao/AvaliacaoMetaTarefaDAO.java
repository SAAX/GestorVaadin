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
import java.time.LocalDateTime;
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

    /**
     * metodo padrao modificado para gravar data/hora de inclusao
     * @param avaliacaoMetaTarefa 
     */
    public void create(AvaliacaoMetaTarefa avaliacaoMetaTarefa) {
        avaliacaoMetaTarefa.setDataHoraInclusao(LocalDateTime.now());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Meta meta = avaliacaoMetaTarefa.getMeta();
            if (meta != null) {
                meta = em.getReference(meta.getClass(), meta.getId());
                avaliacaoMetaTarefa.setMeta(meta);
            }
            Tarefa tarefa = avaliacaoMetaTarefa.getTarefa();
            if (tarefa != null) {
                tarefa = em.getReference(tarefa.getClass(), tarefa.getId());
                avaliacaoMetaTarefa.setTarefa(tarefa);
            }
            Usuario usuarioInclusao = avaliacaoMetaTarefa.getUsuarioInclusao();
            if (usuarioInclusao != null) {
                usuarioInclusao = em.getReference(usuarioInclusao.getClass(), usuarioInclusao.getId());
                avaliacaoMetaTarefa.setUsuarioInclusao(usuarioInclusao);
            }
            Usuario usuarioAvaliador = avaliacaoMetaTarefa.getUsuarioAvaliador();
            if (usuarioAvaliador != null) {
                usuarioAvaliador = em.getReference(usuarioAvaliador.getClass(), usuarioAvaliador.getId());
                avaliacaoMetaTarefa.setUsuarioAvaliador(usuarioAvaliador);
            }
            Usuario usuarioAvaliado = avaliacaoMetaTarefa.getUsuarioAvaliado();
            if (usuarioAvaliado != null) {
                usuarioAvaliado = em.getReference(usuarioAvaliado.getClass(), usuarioAvaliado.getId());
                avaliacaoMetaTarefa.setUsuarioAvaliado(usuarioAvaliado);
            }
            em.persist(avaliacaoMetaTarefa);
            if (meta != null) {
                meta.getAvaliacoes().add(avaliacaoMetaTarefa);
                meta = em.merge(meta);
            }
            if (tarefa != null) {
                tarefa.getAvaliacoes().add(avaliacaoMetaTarefa);
                tarefa = em.merge(tarefa);
            }
            if (usuarioInclusao != null) {
                usuarioInclusao.getAvaliacoesIncluidas().add(avaliacaoMetaTarefa);
                usuarioInclusao = em.merge(usuarioInclusao);
            }
            if (usuarioAvaliador != null) {
                usuarioAvaliador.getAvaliacoesIncluidas().add(avaliacaoMetaTarefa);
                usuarioAvaliador = em.merge(usuarioAvaliador);
            }
            if (usuarioAvaliado != null) {
                usuarioAvaliado.getAvaliacoesIncluidas().add(avaliacaoMetaTarefa);
                usuarioAvaliado = em.merge(usuarioAvaliado);
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
            Meta metaOld = persistentAvaliacaoMetaTarefa.getMeta();
            Meta metaNew = avaliacaoMetaTarefa.getMeta();
            Tarefa tarefaOld = persistentAvaliacaoMetaTarefa.getTarefa();
            Tarefa tarefaNew = avaliacaoMetaTarefa.getTarefa();
            Usuario usuarioInclusaoOld = persistentAvaliacaoMetaTarefa.getUsuarioInclusao();
            Usuario usuarioInclusaoNew = avaliacaoMetaTarefa.getUsuarioInclusao();
            Usuario usuarioAvaliadorOld = persistentAvaliacaoMetaTarefa.getUsuarioAvaliador();
            Usuario usuarioAvaliadorNew = avaliacaoMetaTarefa.getUsuarioAvaliador();
            Usuario usuarioAvaliadoOld = persistentAvaliacaoMetaTarefa.getUsuarioAvaliado();
            Usuario usuarioAvaliadoNew = avaliacaoMetaTarefa.getUsuarioAvaliado();
            if (metaNew != null) {
                metaNew = em.getReference(metaNew.getClass(), metaNew.getId());
                avaliacaoMetaTarefa.setMeta(metaNew);
            }
            if (tarefaNew != null) {
                tarefaNew = em.getReference(tarefaNew.getClass(), tarefaNew.getId());
                avaliacaoMetaTarefa.setTarefa(tarefaNew);
            }
            if (usuarioInclusaoNew != null) {
                usuarioInclusaoNew = em.getReference(usuarioInclusaoNew.getClass(), usuarioInclusaoNew.getId());
                avaliacaoMetaTarefa.setUsuarioInclusao(usuarioInclusaoNew);
            }
            if (usuarioAvaliadorNew != null) {
                usuarioAvaliadorNew = em.getReference(usuarioAvaliadorNew.getClass(), usuarioAvaliadorNew.getId());
                avaliacaoMetaTarefa.setUsuarioAvaliador(usuarioAvaliadorNew);
            }
            if (usuarioAvaliadoNew != null) {
                usuarioAvaliadoNew = em.getReference(usuarioAvaliadoNew.getClass(), usuarioAvaliadoNew.getId());
                avaliacaoMetaTarefa.setUsuarioAvaliado(usuarioAvaliadoNew);
            }
            avaliacaoMetaTarefa = em.merge(avaliacaoMetaTarefa);
            if (metaOld != null && !metaOld.equals(metaNew)) {
                metaOld.getAvaliacoes().remove(avaliacaoMetaTarefa);
                metaOld = em.merge(metaOld);
            }
            if (metaNew != null && !metaNew.equals(metaOld)) {
                metaNew.getAvaliacoes().add(avaliacaoMetaTarefa);
                metaNew = em.merge(metaNew);
            }
            if (tarefaOld != null && !tarefaOld.equals(tarefaNew)) {
                tarefaOld.getAvaliacoes().remove(avaliacaoMetaTarefa);
                tarefaOld = em.merge(tarefaOld);
            }
            if (tarefaNew != null && !tarefaNew.equals(tarefaOld)) {
                tarefaNew.getAvaliacoes().add(avaliacaoMetaTarefa);
                tarefaNew = em.merge(tarefaNew);
            }
            if (usuarioInclusaoOld != null && !usuarioInclusaoOld.equals(usuarioInclusaoNew)) {
                usuarioInclusaoOld.getAvaliacoesIncluidas().remove(avaliacaoMetaTarefa);
                usuarioInclusaoOld = em.merge(usuarioInclusaoOld);
            }
            if (usuarioInclusaoNew != null && !usuarioInclusaoNew.equals(usuarioInclusaoOld)) {
                usuarioInclusaoNew.getAvaliacoesIncluidas().add(avaliacaoMetaTarefa);
                usuarioInclusaoNew = em.merge(usuarioInclusaoNew);
            }
            if (usuarioAvaliadorOld != null && !usuarioAvaliadorOld.equals(usuarioAvaliadorNew)) {
                usuarioAvaliadorOld.getAvaliacoesIncluidas().remove(avaliacaoMetaTarefa);
                usuarioAvaliadorOld = em.merge(usuarioAvaliadorOld);
            }
            if (usuarioAvaliadorNew != null && !usuarioAvaliadorNew.equals(usuarioAvaliadorOld)) {
                usuarioAvaliadorNew.getAvaliacoesIncluidas().add(avaliacaoMetaTarefa);
                usuarioAvaliadorNew = em.merge(usuarioAvaliadorNew);
            }
            if (usuarioAvaliadoOld != null && !usuarioAvaliadoOld.equals(usuarioAvaliadoNew)) {
                usuarioAvaliadoOld.getAvaliacoesIncluidas().remove(avaliacaoMetaTarefa);
                usuarioAvaliadoOld = em.merge(usuarioAvaliadoOld);
            }
            if (usuarioAvaliadoNew != null && !usuarioAvaliadoNew.equals(usuarioAvaliadoOld)) {
                usuarioAvaliadoNew.getAvaliacoesIncluidas().add(avaliacaoMetaTarefa);
                usuarioAvaliadoNew = em.merge(usuarioAvaliadoNew);
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
            Meta meta = avaliacaoMetaTarefa.getMeta();
            if (meta != null) {
                meta.getAvaliacoes().remove(avaliacaoMetaTarefa);
                meta = em.merge(meta);
            }
            Tarefa tarefa = avaliacaoMetaTarefa.getTarefa();
            if (tarefa != null) {
                tarefa.getAvaliacoes().remove(avaliacaoMetaTarefa);
                tarefa = em.merge(tarefa);
            }
            Usuario usuarioInclusao = avaliacaoMetaTarefa.getUsuarioInclusao();
            if (usuarioInclusao != null) {
                usuarioInclusao.getAvaliacoesIncluidas().remove(avaliacaoMetaTarefa);
                usuarioInclusao = em.merge(usuarioInclusao);
            }
            Usuario usuarioAvaliador = avaliacaoMetaTarefa.getUsuarioAvaliador();
            if (usuarioAvaliador != null) {
                usuarioAvaliador.getAvaliacoesIncluidas().remove(avaliacaoMetaTarefa);
                usuarioAvaliador = em.merge(usuarioAvaliador);
            }
            Usuario usuarioAvaliado = avaliacaoMetaTarefa.getUsuarioAvaliado();
            if (usuarioAvaliado != null) {
                usuarioAvaliado.getAvaliacoesIncluidas().remove(avaliacaoMetaTarefa);
                usuarioAvaliado = em.merge(usuarioAvaliado);
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
