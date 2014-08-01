package com.saax.gestorweb.dao;

import com.saax.gestorweb.dao.exceptions.IllegalOrphanException;
import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.saax.gestorweb.model.datamodel.CentroCusto;
import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.model.datamodel.FavoritosTarefaMeta;
import java.util.ArrayList;
import java.util.List;
import com.saax.gestorweb.model.datamodel.AvaliacaoMetaTarefa;
import com.saax.gestorweb.model.datamodel.Meta;
import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * DAO para o entity bean: Meta <br><br>
 * 
 * @author rodrigo
 */
public class MetaDAO implements Serializable {

    public MetaDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * metodo padrao modificado para gravar data/hora de inclusao
     * @param meta 
     */
    public void create(Meta meta) {
        meta.setDataHoraInclusao(LocalDateTime.now());
        if (meta.getFavoritados() == null) {
            meta.setFavoritados(new ArrayList<FavoritosTarefaMeta>());
        }
        if (meta.getAvaliacoes() == null) {
            meta.setAvaliacoes(new ArrayList<AvaliacaoMetaTarefa>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CentroCusto centroCusto = meta.getCentroCusto();
            if (centroCusto != null) {
                centroCusto = em.getReference(centroCusto.getClass(), centroCusto.getId());
                meta.setCentroCusto(centroCusto);
            }
            Departamento departamento = meta.getDepartamento();
            if (departamento != null) {
                departamento = em.getReference(departamento.getClass(), departamento.getId());
                meta.setDepartamento(departamento);
            }
            Empresa empresa = meta.getEmpresa();
            if (empresa != null) {
                empresa = em.getReference(empresa.getClass(), empresa.getId());
                meta.setEmpresa(empresa);
            }
            EmpresaCliente cliente = meta.getCliente();
            if (cliente != null) {
                cliente = em.getReference(cliente.getClass(), cliente.getId());
                meta.setCliente(cliente);
            }
            Usuario usuarioResponsavel = meta.getUsuarioResponsavel();
            if (usuarioResponsavel != null) {
                usuarioResponsavel = em.getReference(usuarioResponsavel.getClass(), usuarioResponsavel.getId());
                meta.setUsuarioResponsavel(usuarioResponsavel);
            }
            Usuario usuarioInclusao = meta.getUsuarioInclusao();
            if (usuarioInclusao != null) {
                usuarioInclusao = em.getReference(usuarioInclusao.getClass(), usuarioInclusao.getId());
                meta.setUsuarioInclusao(usuarioInclusao);
            }
            List<FavoritosTarefaMeta> attachedFavoritados = new ArrayList<FavoritosTarefaMeta>();
            for (FavoritosTarefaMeta favoritadosFavoritosTarefaMetaToAttach : meta.getFavoritados()) {
                favoritadosFavoritosTarefaMetaToAttach = em.getReference(favoritadosFavoritosTarefaMetaToAttach.getClass(), favoritadosFavoritosTarefaMetaToAttach.getId());
                attachedFavoritados.add(favoritadosFavoritosTarefaMetaToAttach);
            }
            meta.setFavoritados(attachedFavoritados);
            List<AvaliacaoMetaTarefa> attachedAvaliacoes = new ArrayList<AvaliacaoMetaTarefa>();
            for (AvaliacaoMetaTarefa avaliacoesAvaliacaoMetaTarefaToAttach : meta.getAvaliacoes()) {
                avaliacoesAvaliacaoMetaTarefaToAttach = em.getReference(avaliacoesAvaliacaoMetaTarefaToAttach.getClass(), avaliacoesAvaliacaoMetaTarefaToAttach.getId());
                attachedAvaliacoes.add(avaliacoesAvaliacaoMetaTarefaToAttach);
            }
            meta.setAvaliacoes(attachedAvaliacoes);
            em.persist(meta);
            if (centroCusto != null) {
                centroCusto.getMetas().add(meta);
                centroCusto = em.merge(centroCusto);
            }
            if (departamento != null) {
                departamento.getMetas().add(meta);
                departamento = em.merge(departamento);
            }
            if (empresa != null) {
                empresa.getMetas().add(meta);
                empresa = em.merge(empresa);
            }
            if (cliente != null) {
                cliente.getMetas().add(meta);
                cliente = em.merge(cliente);
            }
            if (usuarioResponsavel != null) {
                usuarioResponsavel.getMetasSobResponsabilidade().add(meta);
                usuarioResponsavel = em.merge(usuarioResponsavel);
            }
            if (usuarioInclusao != null) {
                usuarioInclusao.getMetasSobResponsabilidade().add(meta);
                usuarioInclusao = em.merge(usuarioInclusao);
            }
            for (FavoritosTarefaMeta favoritadosFavoritosTarefaMeta : meta.getFavoritados()) {
                Meta oldMetaOfFavoritadosFavoritosTarefaMeta = favoritadosFavoritosTarefaMeta.getMeta();
                favoritadosFavoritosTarefaMeta.setMeta(meta);
                favoritadosFavoritosTarefaMeta = em.merge(favoritadosFavoritosTarefaMeta);
                if (oldMetaOfFavoritadosFavoritosTarefaMeta != null) {
                    oldMetaOfFavoritadosFavoritosTarefaMeta.getFavoritados().remove(favoritadosFavoritosTarefaMeta);
                    oldMetaOfFavoritadosFavoritosTarefaMeta = em.merge(oldMetaOfFavoritadosFavoritosTarefaMeta);
                }
            }
            for (AvaliacaoMetaTarefa avaliacoesAvaliacaoMetaTarefa : meta.getAvaliacoes()) {
                Meta oldMetaOfAvaliacoesAvaliacaoMetaTarefa = avaliacoesAvaliacaoMetaTarefa.getMeta();
                avaliacoesAvaliacaoMetaTarefa.setMeta(meta);
                avaliacoesAvaliacaoMetaTarefa = em.merge(avaliacoesAvaliacaoMetaTarefa);
                if (oldMetaOfAvaliacoesAvaliacaoMetaTarefa != null) {
                    oldMetaOfAvaliacoesAvaliacaoMetaTarefa.getAvaliacoes().remove(avaliacoesAvaliacaoMetaTarefa);
                    oldMetaOfAvaliacoesAvaliacaoMetaTarefa = em.merge(oldMetaOfAvaliacoesAvaliacaoMetaTarefa);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Meta meta) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Meta persistentMeta = em.find(Meta.class, meta.getId());
            CentroCusto centroCustoOld = persistentMeta.getCentroCusto();
            CentroCusto centroCustoNew = meta.getCentroCusto();
            Departamento departamentoOld = persistentMeta.getDepartamento();
            Departamento departamentoNew = meta.getDepartamento();
            Empresa empresaOld = persistentMeta.getEmpresa();
            Empresa empresaNew = meta.getEmpresa();
            EmpresaCliente clienteOld = persistentMeta.getCliente();
            EmpresaCliente clienteNew = meta.getCliente();
            Usuario usuarioResponsavelOld = persistentMeta.getUsuarioResponsavel();
            Usuario usuarioResponsavelNew = meta.getUsuarioResponsavel();
            Usuario usuarioInclusaoOld = persistentMeta.getUsuarioInclusao();
            Usuario usuarioInclusaoNew = meta.getUsuarioInclusao();
            List<FavoritosTarefaMeta> favoritadosOld = persistentMeta.getFavoritados();
            List<FavoritosTarefaMeta> favoritadosNew = meta.getFavoritados();
            List<AvaliacaoMetaTarefa> avaliacoesOld = persistentMeta.getAvaliacoes();
            List<AvaliacaoMetaTarefa> avaliacoesNew = meta.getAvaliacoes();
            List<String> illegalOrphanMessages = null;
            for (FavoritosTarefaMeta favoritadosOldFavoritosTarefaMeta : favoritadosOld) {
                if (!favoritadosNew.contains(favoritadosOldFavoritosTarefaMeta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain FavoritosTarefaMeta " + favoritadosOldFavoritosTarefaMeta + " since its meta field is not nullable.");
                }
            }
            for (AvaliacaoMetaTarefa avaliacoesOldAvaliacaoMetaTarefa : avaliacoesOld) {
                if (!avaliacoesNew.contains(avaliacoesOldAvaliacaoMetaTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AvaliacaoMetaTarefa " + avaliacoesOldAvaliacaoMetaTarefa + " since its meta field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (centroCustoNew != null) {
                centroCustoNew = em.getReference(centroCustoNew.getClass(), centroCustoNew.getId());
                meta.setCentroCusto(centroCustoNew);
            }
            if (departamentoNew != null) {
                departamentoNew = em.getReference(departamentoNew.getClass(), departamentoNew.getId());
                meta.setDepartamento(departamentoNew);
            }
            if (empresaNew != null) {
                empresaNew = em.getReference(empresaNew.getClass(), empresaNew.getId());
                meta.setEmpresa(empresaNew);
            }
            if (clienteNew != null) {
                clienteNew = em.getReference(clienteNew.getClass(), clienteNew.getId());
                meta.setCliente(clienteNew);
            }
            if (usuarioResponsavelNew != null) {
                usuarioResponsavelNew = em.getReference(usuarioResponsavelNew.getClass(), usuarioResponsavelNew.getId());
                meta.setUsuarioResponsavel(usuarioResponsavelNew);
            }
            if (usuarioInclusaoNew != null) {
                usuarioInclusaoNew = em.getReference(usuarioInclusaoNew.getClass(), usuarioInclusaoNew.getId());
                meta.setUsuarioInclusao(usuarioInclusaoNew);
            }
            List<FavoritosTarefaMeta> attachedFavoritadosNew = new ArrayList<FavoritosTarefaMeta>();
            for (FavoritosTarefaMeta favoritadosNewFavoritosTarefaMetaToAttach : favoritadosNew) {
                favoritadosNewFavoritosTarefaMetaToAttach = em.getReference(favoritadosNewFavoritosTarefaMetaToAttach.getClass(), favoritadosNewFavoritosTarefaMetaToAttach.getId());
                attachedFavoritadosNew.add(favoritadosNewFavoritosTarefaMetaToAttach);
            }
            favoritadosNew = attachedFavoritadosNew;
            meta.setFavoritados(favoritadosNew);
            List<AvaliacaoMetaTarefa> attachedAvaliacoesNew = new ArrayList<AvaliacaoMetaTarefa>();
            for (AvaliacaoMetaTarefa avaliacoesNewAvaliacaoMetaTarefaToAttach : avaliacoesNew) {
                avaliacoesNewAvaliacaoMetaTarefaToAttach = em.getReference(avaliacoesNewAvaliacaoMetaTarefaToAttach.getClass(), avaliacoesNewAvaliacaoMetaTarefaToAttach.getId());
                attachedAvaliacoesNew.add(avaliacoesNewAvaliacaoMetaTarefaToAttach);
            }
            avaliacoesNew = attachedAvaliacoesNew;
            meta.setAvaliacoes(avaliacoesNew);
            meta = em.merge(meta);
            if (centroCustoOld != null && !centroCustoOld.equals(centroCustoNew)) {
                centroCustoOld.getMetas().remove(meta);
                centroCustoOld = em.merge(centroCustoOld);
            }
            if (centroCustoNew != null && !centroCustoNew.equals(centroCustoOld)) {
                centroCustoNew.getMetas().add(meta);
                centroCustoNew = em.merge(centroCustoNew);
            }
            if (departamentoOld != null && !departamentoOld.equals(departamentoNew)) {
                departamentoOld.getMetas().remove(meta);
                departamentoOld = em.merge(departamentoOld);
            }
            if (departamentoNew != null && !departamentoNew.equals(departamentoOld)) {
                departamentoNew.getMetas().add(meta);
                departamentoNew = em.merge(departamentoNew);
            }
            if (empresaOld != null && !empresaOld.equals(empresaNew)) {
                empresaOld.getMetas().remove(meta);
                empresaOld = em.merge(empresaOld);
            }
            if (empresaNew != null && !empresaNew.equals(empresaOld)) {
                empresaNew.getMetas().add(meta);
                empresaNew = em.merge(empresaNew);
            }
            if (clienteOld != null && !clienteOld.equals(clienteNew)) {
                clienteOld.getMetas().remove(meta);
                clienteOld = em.merge(clienteOld);
            }
            if (clienteNew != null && !clienteNew.equals(clienteOld)) {
                clienteNew.getMetas().add(meta);
                clienteNew = em.merge(clienteNew);
            }
            if (usuarioResponsavelOld != null && !usuarioResponsavelOld.equals(usuarioResponsavelNew)) {
                usuarioResponsavelOld.getMetasSobResponsabilidade().remove(meta);
                usuarioResponsavelOld = em.merge(usuarioResponsavelOld);
            }
            if (usuarioResponsavelNew != null && !usuarioResponsavelNew.equals(usuarioResponsavelOld)) {
                usuarioResponsavelNew.getMetasSobResponsabilidade().add(meta);
                usuarioResponsavelNew = em.merge(usuarioResponsavelNew);
            }
            if (usuarioInclusaoOld != null && !usuarioInclusaoOld.equals(usuarioInclusaoNew)) {
                usuarioInclusaoOld.getMetasSobResponsabilidade().remove(meta);
                usuarioInclusaoOld = em.merge(usuarioInclusaoOld);
            }
            if (usuarioInclusaoNew != null && !usuarioInclusaoNew.equals(usuarioInclusaoOld)) {
                usuarioInclusaoNew.getMetasSobResponsabilidade().add(meta);
                usuarioInclusaoNew = em.merge(usuarioInclusaoNew);
            }
            for (FavoritosTarefaMeta favoritadosNewFavoritosTarefaMeta : favoritadosNew) {
                if (!favoritadosOld.contains(favoritadosNewFavoritosTarefaMeta)) {
                    Meta oldMetaOfFavoritadosNewFavoritosTarefaMeta = favoritadosNewFavoritosTarefaMeta.getMeta();
                    favoritadosNewFavoritosTarefaMeta.setMeta(meta);
                    favoritadosNewFavoritosTarefaMeta = em.merge(favoritadosNewFavoritosTarefaMeta);
                    if (oldMetaOfFavoritadosNewFavoritosTarefaMeta != null && !oldMetaOfFavoritadosNewFavoritosTarefaMeta.equals(meta)) {
                        oldMetaOfFavoritadosNewFavoritosTarefaMeta.getFavoritados().remove(favoritadosNewFavoritosTarefaMeta);
                        oldMetaOfFavoritadosNewFavoritosTarefaMeta = em.merge(oldMetaOfFavoritadosNewFavoritosTarefaMeta);
                    }
                }
            }
            for (AvaliacaoMetaTarefa avaliacoesNewAvaliacaoMetaTarefa : avaliacoesNew) {
                if (!avaliacoesOld.contains(avaliacoesNewAvaliacaoMetaTarefa)) {
                    Meta oldMetaOfAvaliacoesNewAvaliacaoMetaTarefa = avaliacoesNewAvaliacaoMetaTarefa.getMeta();
                    avaliacoesNewAvaliacaoMetaTarefa.setMeta(meta);
                    avaliacoesNewAvaliacaoMetaTarefa = em.merge(avaliacoesNewAvaliacaoMetaTarefa);
                    if (oldMetaOfAvaliacoesNewAvaliacaoMetaTarefa != null && !oldMetaOfAvaliacoesNewAvaliacaoMetaTarefa.equals(meta)) {
                        oldMetaOfAvaliacoesNewAvaliacaoMetaTarefa.getAvaliacoes().remove(avaliacoesNewAvaliacaoMetaTarefa);
                        oldMetaOfAvaliacoesNewAvaliacaoMetaTarefa = em.merge(oldMetaOfAvaliacoesNewAvaliacaoMetaTarefa);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = meta.getId();
                if (findMeta(id) == null) {
                    throw new NonexistentEntityException("The meta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Meta meta;
            try {
                meta = em.getReference(Meta.class, id);
                meta.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The meta with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<FavoritosTarefaMeta> favoritadosOrphanCheck = meta.getFavoritados();
            for (FavoritosTarefaMeta favoritadosOrphanCheckFavoritosTarefaMeta : favoritadosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Meta (" + meta + ") cannot be destroyed since the FavoritosTarefaMeta " + favoritadosOrphanCheckFavoritosTarefaMeta + " in its favoritados field has a non-nullable meta field.");
            }
            List<AvaliacaoMetaTarefa> avaliacoesOrphanCheck = meta.getAvaliacoes();
            for (AvaliacaoMetaTarefa avaliacoesOrphanCheckAvaliacaoMetaTarefa : avaliacoesOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Meta (" + meta + ") cannot be destroyed since the AvaliacaoMetaTarefa " + avaliacoesOrphanCheckAvaliacaoMetaTarefa + " in its avaliacoes field has a non-nullable meta field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            CentroCusto centroCusto = meta.getCentroCusto();
            if (centroCusto != null) {
                centroCusto.getMetas().remove(meta);
                centroCusto = em.merge(centroCusto);
            }
            Departamento departamento = meta.getDepartamento();
            if (departamento != null) {
                departamento.getMetas().remove(meta);
                departamento = em.merge(departamento);
            }
            Empresa empresa = meta.getEmpresa();
            if (empresa != null) {
                empresa.getMetas().remove(meta);
                empresa = em.merge(empresa);
            }
            EmpresaCliente cliente = meta.getCliente();
            if (cliente != null) {
                cliente.getMetas().remove(meta);
                cliente = em.merge(cliente);
            }
            Usuario usuarioResponsavel = meta.getUsuarioResponsavel();
            if (usuarioResponsavel != null) {
                usuarioResponsavel.getMetasSobResponsabilidade().remove(meta);
                usuarioResponsavel = em.merge(usuarioResponsavel);
            }
            Usuario usuarioInclusao = meta.getUsuarioInclusao();
            if (usuarioInclusao != null) {
                usuarioInclusao.getMetasSobResponsabilidade().remove(meta);
                usuarioInclusao = em.merge(usuarioInclusao);
            }
            em.remove(meta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Meta> findMetaEntities() {
        return findMetaEntities(true, -1, -1);
    }

    public List<Meta> findMetaEntities(int maxResults, int firstResult) {
        return findMetaEntities(false, maxResults, firstResult);
    }

    private List<Meta> findMetaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Meta.class));
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

    public Meta findMeta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Meta.class, id);
        } finally {
            em.close();
        }
    }

    public int getMetaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Meta> rt = cq.from(Meta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
