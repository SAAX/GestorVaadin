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

    public void create(Meta meta) {
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
            Usuario responsavel = meta.getResponsavel();
            if (responsavel != null) {
                responsavel = em.getReference(responsavel.getClass(), responsavel.getId());
                meta.setResponsavel(responsavel);
            }
            Usuario idusuarioinclusao = meta.getIdUsuarioInclusao();
            if (idusuarioinclusao != null) {
                idusuarioinclusao = em.getReference(idusuarioinclusao.getClass(), idusuarioinclusao.getId());
                meta.setIdUsuarioInclusao(idusuarioinclusao);
            }
            List<FavoritosTarefaMeta> attachedFavoritosTarefaMetaList = new ArrayList<FavoritosTarefaMeta>();
            for (FavoritosTarefaMeta favoritosTarefaMetaListFavoritosTarefaMetaToAttach : meta.getFavoritados()) {
                favoritosTarefaMetaListFavoritosTarefaMetaToAttach = em.getReference(favoritosTarefaMetaListFavoritosTarefaMetaToAttach.getClass(), favoritosTarefaMetaListFavoritosTarefaMetaToAttach.getId());
                attachedFavoritosTarefaMetaList.add(favoritosTarefaMetaListFavoritosTarefaMetaToAttach);
            }
            meta.setFavoritados(attachedFavoritosTarefaMetaList);
            List<AvaliacaoMetaTarefa> attachedAvaliacaoMetaTarefaList = new ArrayList<AvaliacaoMetaTarefa>();
            for (AvaliacaoMetaTarefa avaliacaoMetaTarefaListAvaliacaoMetaTarefaToAttach : meta.getAvaliacoes()) {
                avaliacaoMetaTarefaListAvaliacaoMetaTarefaToAttach = em.getReference(avaliacaoMetaTarefaListAvaliacaoMetaTarefaToAttach.getClass(), avaliacaoMetaTarefaListAvaliacaoMetaTarefaToAttach.getId());
                attachedAvaliacaoMetaTarefaList.add(avaliacaoMetaTarefaListAvaliacaoMetaTarefaToAttach);
            }
            meta.setAvaliacoes(attachedAvaliacaoMetaTarefaList);
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
            if (responsavel != null) {
                responsavel.getMetasResponsaveis().add(meta);
                responsavel = em.merge(responsavel);
            }
            if (idusuarioinclusao != null) {
                idusuarioinclusao.getMetasResponsaveis().add(meta);
                idusuarioinclusao = em.merge(idusuarioinclusao);
            }
            for (FavoritosTarefaMeta favoritosTarefaMetaListFavoritosTarefaMeta : meta.getFavoritados()) {
                Meta oldIdmetaOfFavoritosTarefaMetaListFavoritosTarefaMeta = favoritosTarefaMetaListFavoritosTarefaMeta.getIdMeta();
                favoritosTarefaMetaListFavoritosTarefaMeta.setIdMeta(meta);
                favoritosTarefaMetaListFavoritosTarefaMeta = em.merge(favoritosTarefaMetaListFavoritosTarefaMeta);
                if (oldIdmetaOfFavoritosTarefaMetaListFavoritosTarefaMeta != null) {
                    oldIdmetaOfFavoritosTarefaMetaListFavoritosTarefaMeta.getFavoritados().remove(favoritosTarefaMetaListFavoritosTarefaMeta);
                    oldIdmetaOfFavoritosTarefaMetaListFavoritosTarefaMeta = em.merge(oldIdmetaOfFavoritosTarefaMetaListFavoritosTarefaMeta);
                }
            }
            for (AvaliacaoMetaTarefa avaliacaoMetaTarefaListAvaliacaoMetaTarefa : meta.getAvaliacoes()) {
                Meta oldIdmetaOfAvaliacaoMetaTarefaListAvaliacaoMetaTarefa = avaliacaoMetaTarefaListAvaliacaoMetaTarefa.getIdMeta();
                avaliacaoMetaTarefaListAvaliacaoMetaTarefa.setIdMeta(meta);
                avaliacaoMetaTarefaListAvaliacaoMetaTarefa = em.merge(avaliacaoMetaTarefaListAvaliacaoMetaTarefa);
                if (oldIdmetaOfAvaliacaoMetaTarefaListAvaliacaoMetaTarefa != null) {
                    oldIdmetaOfAvaliacaoMetaTarefaListAvaliacaoMetaTarefa.getAvaliacoes().remove(avaliacaoMetaTarefaListAvaliacaoMetaTarefa);
                    oldIdmetaOfAvaliacaoMetaTarefaListAvaliacaoMetaTarefa = em.merge(oldIdmetaOfAvaliacaoMetaTarefaListAvaliacaoMetaTarefa);
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
            Usuario responsavelOld = persistentMeta.getResponsavel();
            Usuario responsavelNew = meta.getResponsavel();
            Usuario idusuarioinclusaoOld = persistentMeta.getIdUsuarioInclusao();
            Usuario idusuarioinclusaoNew = meta.getIdUsuarioInclusao();
            List<FavoritosTarefaMeta> favoritosTarefaMetaListOld = persistentMeta.getFavoritados();
            List<FavoritosTarefaMeta> favoritosTarefaMetaListNew = meta.getFavoritados();
            List<AvaliacaoMetaTarefa> avaliacaoMetaTarefaListOld = persistentMeta.getAvaliacoes();
            List<AvaliacaoMetaTarefa> avaliacaoMetaTarefaListNew = meta.getAvaliacoes();
            List<String> illegalOrphanMessages = null;
            for (FavoritosTarefaMeta favoritosTarefaMetaListOldFavoritosTarefaMeta : favoritosTarefaMetaListOld) {
                if (!favoritosTarefaMetaListNew.contains(favoritosTarefaMetaListOldFavoritosTarefaMeta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain FavoritosTarefaMeta " + favoritosTarefaMetaListOldFavoritosTarefaMeta + " since its idmeta field is not nullable.");
                }
            }
            for (AvaliacaoMetaTarefa avaliacaoMetaTarefaListOldAvaliacaoMetaTarefa : avaliacaoMetaTarefaListOld) {
                if (!avaliacaoMetaTarefaListNew.contains(avaliacaoMetaTarefaListOldAvaliacaoMetaTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AvaliacaoMetaTarefa " + avaliacaoMetaTarefaListOldAvaliacaoMetaTarefa + " since its idmeta field is not nullable.");
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
            if (responsavelNew != null) {
                responsavelNew = em.getReference(responsavelNew.getClass(), responsavelNew.getId());
                meta.setResponsavel(responsavelNew);
            }
            if (idusuarioinclusaoNew != null) {
                idusuarioinclusaoNew = em.getReference(idusuarioinclusaoNew.getClass(), idusuarioinclusaoNew.getId());
                meta.setIdUsuarioInclusao(idusuarioinclusaoNew);
            }
            List<FavoritosTarefaMeta> attachedFavoritosTarefaMetaListNew = new ArrayList<FavoritosTarefaMeta>();
            for (FavoritosTarefaMeta favoritosTarefaMetaListNewFavoritosTarefaMetaToAttach : favoritosTarefaMetaListNew) {
                favoritosTarefaMetaListNewFavoritosTarefaMetaToAttach = em.getReference(favoritosTarefaMetaListNewFavoritosTarefaMetaToAttach.getClass(), favoritosTarefaMetaListNewFavoritosTarefaMetaToAttach.getId());
                attachedFavoritosTarefaMetaListNew.add(favoritosTarefaMetaListNewFavoritosTarefaMetaToAttach);
            }
            favoritosTarefaMetaListNew = attachedFavoritosTarefaMetaListNew;
            meta.setFavoritados(favoritosTarefaMetaListNew);
            List<AvaliacaoMetaTarefa> attachedAvaliacaoMetaTarefaListNew = new ArrayList<AvaliacaoMetaTarefa>();
            for (AvaliacaoMetaTarefa avaliacaoMetaTarefaListNewAvaliacaoMetaTarefaToAttach : avaliacaoMetaTarefaListNew) {
                avaliacaoMetaTarefaListNewAvaliacaoMetaTarefaToAttach = em.getReference(avaliacaoMetaTarefaListNewAvaliacaoMetaTarefaToAttach.getClass(), avaliacaoMetaTarefaListNewAvaliacaoMetaTarefaToAttach.getId());
                attachedAvaliacaoMetaTarefaListNew.add(avaliacaoMetaTarefaListNewAvaliacaoMetaTarefaToAttach);
            }
            avaliacaoMetaTarefaListNew = attachedAvaliacaoMetaTarefaListNew;
            meta.setAvaliacoes(avaliacaoMetaTarefaListNew);
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
            if (responsavelOld != null && !responsavelOld.equals(responsavelNew)) {
                responsavelOld.getMetasResponsaveis().remove(meta);
                responsavelOld = em.merge(responsavelOld);
            }
            if (responsavelNew != null && !responsavelNew.equals(responsavelOld)) {
                responsavelNew.getMetasResponsaveis().add(meta);
                responsavelNew = em.merge(responsavelNew);
            }
            if (idusuarioinclusaoOld != null && !idusuarioinclusaoOld.equals(idusuarioinclusaoNew)) {
                idusuarioinclusaoOld.getMetasResponsaveis().remove(meta);
                idusuarioinclusaoOld = em.merge(idusuarioinclusaoOld);
            }
            if (idusuarioinclusaoNew != null && !idusuarioinclusaoNew.equals(idusuarioinclusaoOld)) {
                idusuarioinclusaoNew.getMetasResponsaveis().add(meta);
                idusuarioinclusaoNew = em.merge(idusuarioinclusaoNew);
            }
            for (FavoritosTarefaMeta favoritosTarefaMetaListNewFavoritosTarefaMeta : favoritosTarefaMetaListNew) {
                if (!favoritosTarefaMetaListOld.contains(favoritosTarefaMetaListNewFavoritosTarefaMeta)) {
                    Meta oldIdmetaOfFavoritosTarefaMetaListNewFavoritosTarefaMeta = favoritosTarefaMetaListNewFavoritosTarefaMeta.getIdMeta();
                    favoritosTarefaMetaListNewFavoritosTarefaMeta.setIdMeta(meta);
                    favoritosTarefaMetaListNewFavoritosTarefaMeta = em.merge(favoritosTarefaMetaListNewFavoritosTarefaMeta);
                    if (oldIdmetaOfFavoritosTarefaMetaListNewFavoritosTarefaMeta != null && !oldIdmetaOfFavoritosTarefaMetaListNewFavoritosTarefaMeta.equals(meta)) {
                        oldIdmetaOfFavoritosTarefaMetaListNewFavoritosTarefaMeta.getFavoritados().remove(favoritosTarefaMetaListNewFavoritosTarefaMeta);
                        oldIdmetaOfFavoritosTarefaMetaListNewFavoritosTarefaMeta = em.merge(oldIdmetaOfFavoritosTarefaMetaListNewFavoritosTarefaMeta);
                    }
                }
            }
            for (AvaliacaoMetaTarefa avaliacaoMetaTarefaListNewAvaliacaoMetaTarefa : avaliacaoMetaTarefaListNew) {
                if (!avaliacaoMetaTarefaListOld.contains(avaliacaoMetaTarefaListNewAvaliacaoMetaTarefa)) {
                    Meta oldIdmetaOfAvaliacaoMetaTarefaListNewAvaliacaoMetaTarefa = avaliacaoMetaTarefaListNewAvaliacaoMetaTarefa.getIdMeta();
                    avaliacaoMetaTarefaListNewAvaliacaoMetaTarefa.setIdMeta(meta);
                    avaliacaoMetaTarefaListNewAvaliacaoMetaTarefa = em.merge(avaliacaoMetaTarefaListNewAvaliacaoMetaTarefa);
                    if (oldIdmetaOfAvaliacaoMetaTarefaListNewAvaliacaoMetaTarefa != null && !oldIdmetaOfAvaliacaoMetaTarefaListNewAvaliacaoMetaTarefa.equals(meta)) {
                        oldIdmetaOfAvaliacaoMetaTarefaListNewAvaliacaoMetaTarefa.getAvaliacoes().remove(avaliacaoMetaTarefaListNewAvaliacaoMetaTarefa);
                        oldIdmetaOfAvaliacaoMetaTarefaListNewAvaliacaoMetaTarefa = em.merge(oldIdmetaOfAvaliacaoMetaTarefaListNewAvaliacaoMetaTarefa);
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
            List<FavoritosTarefaMeta> favoritosTarefaMetaListOrphanCheck = meta.getFavoritados();
            for (FavoritosTarefaMeta favoritosTarefaMetaListOrphanCheckFavoritosTarefaMeta : favoritosTarefaMetaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Meta (" + meta + ") cannot be destroyed since the FavoritosTarefaMeta " + favoritosTarefaMetaListOrphanCheckFavoritosTarefaMeta + " in its favoritosTarefaMetaList field has a non-nullable idmeta field.");
            }
            List<AvaliacaoMetaTarefa> avaliacaoMetaTarefaListOrphanCheck = meta.getAvaliacoes();
            for (AvaliacaoMetaTarefa avaliacaoMetaTarefaListOrphanCheckAvaliacaoMetaTarefa : avaliacaoMetaTarefaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Meta (" + meta + ") cannot be destroyed since the AvaliacaoMetaTarefa " + avaliacaoMetaTarefaListOrphanCheckAvaliacaoMetaTarefa + " in its avaliacaoMetaTarefaList field has a non-nullable idmeta field.");
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
            Usuario responsavel = meta.getResponsavel();
            if (responsavel != null) {
                responsavel.getMetasResponsaveis().remove(meta);
                responsavel = em.merge(responsavel);
            }
            Usuario idusuarioinclusao = meta.getIdUsuarioInclusao();
            if (idusuarioinclusao != null) {
                idusuarioinclusao.getMetasResponsaveis().remove(meta);
                idusuarioinclusao = em.merge(idusuarioinclusao);
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
