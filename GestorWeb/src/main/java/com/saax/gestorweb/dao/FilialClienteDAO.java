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
import java.time.LocalDateTime;
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

    /**
     * metodo padrao modificado para gravar data/hora de inclusao
     * @param filialCliente 
     */
    public void create(FilialCliente filialCliente) {
        filialCliente.setDataHoraInclusao(LocalDateTime.now());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EmpresaCliente empresaCliente = filialCliente.getEmpresaCliente();
            if (empresaCliente != null) {
                empresaCliente = em.getReference(empresaCliente.getClass(), empresaCliente.getId());
                filialCliente.setEmpresaCliente(empresaCliente);
            }
            Usuario usuarioInclusao = filialCliente.getUsuarioInclusao();
            if (usuarioInclusao != null) {
                usuarioInclusao = em.getReference(usuarioInclusao.getClass(), usuarioInclusao.getId());
                filialCliente.setUsuarioInclusao(usuarioInclusao);
            }
            em.persist(filialCliente);
            if (empresaCliente != null) {
                empresaCliente.getFiliais().add(filialCliente);
                empresaCliente = em.merge(empresaCliente);
            }
            if (usuarioInclusao != null) {
                usuarioInclusao.getFiliaisClientesIncluidas().add(filialCliente);
                usuarioInclusao = em.merge(usuarioInclusao);
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
            Usuario usuarioInclusaoOld = persistentFilialCliente.getUsuarioInclusao();
            Usuario usuarioInclusaoNew = filialCliente.getUsuarioInclusao();
            if (empresaClienteNew != null) {
                empresaClienteNew = em.getReference(empresaClienteNew.getClass(), empresaClienteNew.getId());
                filialCliente.setEmpresaCliente(empresaClienteNew);
            }
            if (usuarioInclusaoNew != null) {
                usuarioInclusaoNew = em.getReference(usuarioInclusaoNew.getClass(), usuarioInclusaoNew.getId());
                filialCliente.setUsuarioInclusao(usuarioInclusaoNew);
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
            if (usuarioInclusaoOld != null && !usuarioInclusaoOld.equals(usuarioInclusaoNew)) {
                usuarioInclusaoOld.getFiliaisClientesIncluidas().remove(filialCliente);
                usuarioInclusaoOld = em.merge(usuarioInclusaoOld);
            }
            if (usuarioInclusaoNew != null && !usuarioInclusaoNew.equals(usuarioInclusaoOld)) {
                usuarioInclusaoNew.getFiliaisClientesIncluidas().add(filialCliente);
                usuarioInclusaoNew = em.merge(usuarioInclusaoNew);
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
            Usuario usuarioInclusao = filialCliente.getUsuarioInclusao();
            if (usuarioInclusao != null) {
                usuarioInclusao.getFiliaisClientesIncluidas().remove(filialCliente);
                usuarioInclusao = em.merge(usuarioInclusao);
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
