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
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.FilialCliente;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
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
            EmpresaCliente idempresacliente = filialCliente.getEmpresaCliente();
            if (idempresacliente != null) {
                idempresacliente = em.getReference(idempresacliente.getClass(), idempresacliente.getIdEmpresaCliente());
                filialCliente.setEmpresaCliente(idempresacliente);
            }
            em.persist(filialCliente);
            if (idempresacliente != null) {
                idempresacliente.getFiliais().add(filialCliente);
                idempresacliente = em.merge(idempresacliente);
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
            FilialCliente persistentFilialCliente = em.find(FilialCliente.class, filialCliente.getIdFilialCliente());
            EmpresaCliente idempresaclienteOld = persistentFilialCliente.getEmpresaCliente();
            EmpresaCliente idempresaclienteNew = filialCliente.getEmpresaCliente();
            if (idempresaclienteNew != null) {
                idempresaclienteNew = em.getReference(idempresaclienteNew.getClass(), idempresaclienteNew.getIdEmpresaCliente());
                filialCliente.setEmpresaCliente(idempresaclienteNew);
            }
            filialCliente = em.merge(filialCliente);
            if (idempresaclienteOld != null && !idempresaclienteOld.equals(idempresaclienteNew)) {
                idempresaclienteOld.getFiliais().remove(filialCliente);
                idempresaclienteOld = em.merge(idempresaclienteOld);
            }
            if (idempresaclienteNew != null && !idempresaclienteNew.equals(idempresaclienteOld)) {
                idempresaclienteNew.getFiliais().add(filialCliente);
                idempresaclienteNew = em.merge(idempresaclienteNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = filialCliente.getIdFilialCliente();
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
                filialCliente.getIdFilialCliente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The filialCliente with id " + id + " no longer exists.", enfe);
            }
            EmpresaCliente idempresacliente = filialCliente.getEmpresaCliente();
            if (idempresacliente != null) {
                idempresacliente.getFiliais().remove(filialCliente);
                idempresacliente = em.merge(idempresacliente);
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
