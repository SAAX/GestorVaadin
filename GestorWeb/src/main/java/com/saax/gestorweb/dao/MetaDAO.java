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
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * DAO para o entity bean: Meta <br><br>
 * 
 * Classe gerada automaticamente pelo netbeans: NÃO ALTERAR<br>
 * Caso seja necessária alguma customização, estender esta classe<br>
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

    public void destroy(Integer id) throws NonexistentEntityException {
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
