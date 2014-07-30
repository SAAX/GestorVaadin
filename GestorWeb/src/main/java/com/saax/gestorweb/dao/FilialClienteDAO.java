package com.saax.gestorweb.dao;

import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.FilialCliente;
import com.saax.gestorweb.model.datamodel.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * DAO para o entity bean: FilialCliente <br><br>
 * 
 * @author rodrigo
 */
public class FilialClienteDAO implements Serializable {

    public FilialClienteDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(FilialCliente filialCliente) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EmpresaCliente empresaCliente = filialCliente.getEmpresaCliente();
            if (empresaCliente != null) {
                empresaCliente = em.getReference(empresaCliente.getClass(), empresaCliente.getId());
                filialCliente.setEmpresaCliente(empresaCliente);
            }
            Usuario idusuarioinclusao = filialCliente.getIdUsuarioInclusao();
            if (idusuarioinclusao != null) {
                idusuarioinclusao = em.getReference(idusuarioinclusao.getClass(), idusuarioinclusao.getId());
                filialCliente.setIdUsuarioInclusao(idusuarioinclusao);
            }
            em.persist(filialCliente);
            if (empresaCliente != null) {
                empresaCliente.getFiliais().add(filialCliente);
                empresaCliente = em.merge(empresaCliente);
            }
            if (idusuarioinclusao != null) {
                idusuarioinclusao.getFilialClienteList().add(filialCliente);
                idusuarioinclusao = em.merge(idusuarioinclusao);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(FilialCliente filialCliente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FilialCliente persistentFilialCliente = em.find(FilialCliente.class, filialCliente.getId());
            EmpresaCliente empresaClienteOld = persistentFilialCliente.getEmpresaCliente();
            EmpresaCliente empresaClienteNew = filialCliente.getEmpresaCliente();
            Usuario idusuarioinclusaoOld = persistentFilialCliente.getIdUsuarioInclusao();
            Usuario idusuarioinclusaoNew = filialCliente.getIdUsuarioInclusao();
            if (empresaClienteNew != null) {
                empresaClienteNew = em.getReference(empresaClienteNew.getClass(), empresaClienteNew.getId());
                filialCliente.setEmpresaCliente(empresaClienteNew);
            }
            if (idusuarioinclusaoNew != null) {
                idusuarioinclusaoNew = em.getReference(idusuarioinclusaoNew.getClass(), idusuarioinclusaoNew.getId());
                filialCliente.setIdUsuarioInclusao(idusuarioinclusaoNew);
            }
            filialCliente = em.merge(filialCliente);
            if (empresaClienteOld != null && !empresaClienteOld.equals(empresaClienteNew)) {
                empresaClienteOld.getFiliais().remove(filialCliente);
                empresaClienteOld = em.merge(empresaClienteOld);
            }
            if (empresaClienteNew != null && !empresaClienteNew.equals(empresaClienteOld)) {
                empresaClienteNew.getFiliais().add(filialCliente);
                empresaClienteNew = em.merge(empresaClienteNew);
            }
            if (idusuarioinclusaoOld != null && !idusuarioinclusaoOld.equals(idusuarioinclusaoNew)) {
                idusuarioinclusaoOld.getFilialClienteList().remove(filialCliente);
                idusuarioinclusaoOld = em.merge(idusuarioinclusaoOld);
            }
            if (idusuarioinclusaoNew != null && !idusuarioinclusaoNew.equals(idusuarioinclusaoOld)) {
                idusuarioinclusaoNew.getFilialClienteList().add(filialCliente);
                idusuarioinclusaoNew = em.merge(idusuarioinclusaoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = filialCliente.getId();
                if (findFilialCliente(id) == null) {
                    throw new NonexistentEntityException("The filialCliente with id " + id + " no longer exists.");
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
            FilialCliente filialCliente;
            try {
                filialCliente = em.getReference(FilialCliente.class, id);
                filialCliente.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The filialCliente with id " + id + " no longer exists.", enfe);
            }
            EmpresaCliente empresaCliente = filialCliente.getEmpresaCliente();
            if (empresaCliente != null) {
                empresaCliente.getFiliais().remove(filialCliente);
                empresaCliente = em.merge(empresaCliente);
            }
            Usuario idusuarioinclusao = filialCliente.getIdUsuarioInclusao();
            if (idusuarioinclusao != null) {
                idusuarioinclusao.getFilialClienteList().remove(filialCliente);
                idusuarioinclusao = em.merge(idusuarioinclusao);
            }
            em.remove(filialCliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<FilialCliente> findFilialClienteEntities() {
        return findFilialClienteEntities(true, -1, -1);
    }

    public List<FilialCliente> findFilialClienteEntities(int maxResults, int firstResult) {
        return findFilialClienteEntities(false, maxResults, firstResult);
    }

    private List<FilialCliente> findFilialClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FilialCliente.class));
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

    public FilialCliente findFilialCliente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FilialCliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getFilialClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FilialCliente> rt = cq.from(FilialCliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
