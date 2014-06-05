/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.dao;

import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.saax.gestorweb.model.datamodel.CentroCusto;
import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author rodrigo
 */
public class MetaJpaController implements Serializable {

    public MetaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Meta meta) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CentroCusto centroCusto = meta.getCentroCusto();
            if (centroCusto != null) {
                centroCusto = em.getReference(centroCusto.getClass(), centroCusto.getIdCentroCusto());
                meta.setCentroCusto(centroCusto);
            }
            Departamento departamento = meta.getDepartamento();
            if (departamento != null) {
                departamento = em.getReference(departamento.getClass(), departamento.getIdDepartamento());
                meta.setDepartamento(departamento);
            }
            Empresa empresa = meta.getEmpresa();
            if (empresa != null) {
                empresa = em.getReference(empresa.getClass(), empresa.getIdEmpresa());
                meta.setEmpresa(empresa);
            }
            Empresa empresaCliente = meta.getEmpresaCliente();
            if (empresaCliente != null) {
                empresaCliente = em.getReference(empresaCliente.getClass(), empresaCliente.getIdEmpresa());
                meta.setEmpresaCliente(empresaCliente);
            }
            Usuario usuarioResponsavel = meta.getUsuarioResponsavel();
            if (usuarioResponsavel != null) {
                usuarioResponsavel = em.getReference(usuarioResponsavel.getClass(), usuarioResponsavel.getIdUsuario());
                meta.setUsuarioResponsavel(usuarioResponsavel);
            }
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
                empresa.getMetasProprietarias().add(meta);
                empresa = em.merge(empresa);
            }
            if (empresaCliente != null) {
                empresaCliente.getMetasProprietarias().add(meta);
                empresaCliente = em.merge(empresaCliente);
            }
            if (usuarioResponsavel != null) {
                usuarioResponsavel.getMetas().add(meta);
                usuarioResponsavel = em.merge(usuarioResponsavel);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Meta meta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Meta persistentMeta = em.find(Meta.class, meta.getIdMeta());
            CentroCusto centroCustoOld = persistentMeta.getCentroCusto();
            CentroCusto centroCustoNew = meta.getCentroCusto();
            Departamento departamentoOld = persistentMeta.getDepartamento();
            Departamento departamentoNew = meta.getDepartamento();
            Empresa empresaOld = persistentMeta.getEmpresa();
            Empresa empresaNew = meta.getEmpresa();
            Empresa empresaClienteOld = persistentMeta.getEmpresaCliente();
            Empresa empresaClienteNew = meta.getEmpresaCliente();
            Usuario usuarioResponsavelOld = persistentMeta.getUsuarioResponsavel();
            Usuario usuarioResponsavelNew = meta.getUsuarioResponsavel();
            if (centroCustoNew != null) {
                centroCustoNew = em.getReference(centroCustoNew.getClass(), centroCustoNew.getIdCentroCusto());
                meta.setCentroCusto(centroCustoNew);
            }
            if (departamentoNew != null) {
                departamentoNew = em.getReference(departamentoNew.getClass(), departamentoNew.getIdDepartamento());
                meta.setDepartamento(departamentoNew);
            }
            if (empresaNew != null) {
                empresaNew = em.getReference(empresaNew.getClass(), empresaNew.getIdEmpresa());
                meta.setEmpresa(empresaNew);
            }
            if (empresaClienteNew != null) {
                empresaClienteNew = em.getReference(empresaClienteNew.getClass(), empresaClienteNew.getIdEmpresa());
                meta.setEmpresaCliente(empresaClienteNew);
            }
            if (usuarioResponsavelNew != null) {
                usuarioResponsavelNew = em.getReference(usuarioResponsavelNew.getClass(), usuarioResponsavelNew.getIdUsuario());
                meta.setUsuarioResponsavel(usuarioResponsavelNew);
            }
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
                empresaOld.getMetasProprietarias().remove(meta);
                empresaOld = em.merge(empresaOld);
            }
            if (empresaNew != null && !empresaNew.equals(empresaOld)) {
                empresaNew.getMetasProprietarias().add(meta);
                empresaNew = em.merge(empresaNew);
            }
            if (empresaClienteOld != null && !empresaClienteOld.equals(empresaClienteNew)) {
                empresaClienteOld.getMetasProprietarias().remove(meta);
                empresaClienteOld = em.merge(empresaClienteOld);
            }
            if (empresaClienteNew != null && !empresaClienteNew.equals(empresaClienteOld)) {
                empresaClienteNew.getMetasProprietarias().add(meta);
                empresaClienteNew = em.merge(empresaClienteNew);
            }
            if (usuarioResponsavelOld != null && !usuarioResponsavelOld.equals(usuarioResponsavelNew)) {
                usuarioResponsavelOld.getMetas().remove(meta);
                usuarioResponsavelOld = em.merge(usuarioResponsavelOld);
            }
            if (usuarioResponsavelNew != null && !usuarioResponsavelNew.equals(usuarioResponsavelOld)) {
                usuarioResponsavelNew.getMetas().add(meta);
                usuarioResponsavelNew = em.merge(usuarioResponsavelNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = meta.getIdMeta();
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

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Meta meta;
            try {
                meta = em.getReference(Meta.class, id);
                meta.getIdMeta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The meta with id " + id + " no longer exists.", enfe);
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
                empresa.getMetasProprietarias().remove(meta);
                empresa = em.merge(empresa);
            }
            Empresa empresaCliente = meta.getEmpresaCliente();
            if (empresaCliente != null) {
                empresaCliente.getMetasProprietarias().remove(meta);
                empresaCliente = em.merge(empresaCliente);
            }
            Usuario usuarioResponsavel = meta.getUsuarioResponsavel();
            if (usuarioResponsavel != null) {
                usuarioResponsavel.getMetas().remove(meta);
                usuarioResponsavel = em.merge(usuarioResponsavel);
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
