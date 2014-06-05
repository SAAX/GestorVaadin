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
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.RelacionamentoEmpresaCliente;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author rodrigo
 */
public class RelacionamentoEmpresaClienteJpaController implements Serializable {

    public RelacionamentoEmpresaClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RelacionamentoEmpresaCliente relacionamentoEmpresaCliente) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa empresaGestora = relacionamentoEmpresaCliente.getEmpresaGestora();
            if (empresaGestora != null) {
                empresaGestora = em.getReference(empresaGestora.getClass(), empresaGestora.getIdEmpresa());
                relacionamentoEmpresaCliente.setEmpresaGestora(empresaGestora);
            }
            Empresa empresaCliente = relacionamentoEmpresaCliente.getEmpresaCliente();
            if (empresaCliente != null) {
                empresaCliente = em.getReference(empresaCliente.getClass(), empresaCliente.getIdEmpresa());
                relacionamentoEmpresaCliente.setEmpresaCliente(empresaCliente);
            }
            em.persist(relacionamentoEmpresaCliente);
            if (empresaGestora != null) {
                empresaGestora.getEmpresasGestoras().add(relacionamentoEmpresaCliente);
                empresaGestora = em.merge(empresaGestora);
            }
            if (empresaCliente != null) {
                empresaCliente.getEmpresasGestoras().add(relacionamentoEmpresaCliente);
                empresaCliente = em.merge(empresaCliente);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RelacionamentoEmpresaCliente relacionamentoEmpresaCliente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RelacionamentoEmpresaCliente persistentRelacionamentoEmpresaCliente = em.find(RelacionamentoEmpresaCliente.class, relacionamentoEmpresaCliente.getIdRelacionamentoEmpresaCliente());
            Empresa empresaGestoraOld = persistentRelacionamentoEmpresaCliente.getEmpresaGestora();
            Empresa empresaGestoraNew = relacionamentoEmpresaCliente.getEmpresaGestora();
            Empresa empresaClienteOld = persistentRelacionamentoEmpresaCliente.getEmpresaCliente();
            Empresa empresaClienteNew = relacionamentoEmpresaCliente.getEmpresaCliente();
            if (empresaGestoraNew != null) {
                empresaGestoraNew = em.getReference(empresaGestoraNew.getClass(), empresaGestoraNew.getIdEmpresa());
                relacionamentoEmpresaCliente.setEmpresaGestora(empresaGestoraNew);
            }
            if (empresaClienteNew != null) {
                empresaClienteNew = em.getReference(empresaClienteNew.getClass(), empresaClienteNew.getIdEmpresa());
                relacionamentoEmpresaCliente.setEmpresaCliente(empresaClienteNew);
            }
            relacionamentoEmpresaCliente = em.merge(relacionamentoEmpresaCliente);
            if (empresaGestoraOld != null && !empresaGestoraOld.equals(empresaGestoraNew)) {
                empresaGestoraOld.getEmpresasGestoras().remove(relacionamentoEmpresaCliente);
                empresaGestoraOld = em.merge(empresaGestoraOld);
            }
            if (empresaGestoraNew != null && !empresaGestoraNew.equals(empresaGestoraOld)) {
                empresaGestoraNew.getEmpresasGestoras().add(relacionamentoEmpresaCliente);
                empresaGestoraNew = em.merge(empresaGestoraNew);
            }
            if (empresaClienteOld != null && !empresaClienteOld.equals(empresaClienteNew)) {
                empresaClienteOld.getEmpresasGestoras().remove(relacionamentoEmpresaCliente);
                empresaClienteOld = em.merge(empresaClienteOld);
            }
            if (empresaClienteNew != null && !empresaClienteNew.equals(empresaClienteOld)) {
                empresaClienteNew.getEmpresasGestoras().add(relacionamentoEmpresaCliente);
                empresaClienteNew = em.merge(empresaClienteNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = relacionamentoEmpresaCliente.getIdRelacionamentoEmpresaCliente();
                if (findRelacionamentoEmpresaCliente(id) == null) {
                    throw new NonexistentEntityException("The relacionamentoEmpresaCliente with id " + id + " no longer exists.");
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
            RelacionamentoEmpresaCliente relacionamentoEmpresaCliente;
            try {
                relacionamentoEmpresaCliente = em.getReference(RelacionamentoEmpresaCliente.class, id);
                relacionamentoEmpresaCliente.getIdRelacionamentoEmpresaCliente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The relacionamentoEmpresaCliente with id " + id + " no longer exists.", enfe);
            }
            Empresa empresaGestora = relacionamentoEmpresaCliente.getEmpresaGestora();
            if (empresaGestora != null) {
                empresaGestora.getEmpresasGestoras().remove(relacionamentoEmpresaCliente);
                empresaGestora = em.merge(empresaGestora);
            }
            Empresa empresaCliente = relacionamentoEmpresaCliente.getEmpresaCliente();
            if (empresaCliente != null) {
                empresaCliente.getEmpresasGestoras().remove(relacionamentoEmpresaCliente);
                empresaCliente = em.merge(empresaCliente);
            }
            em.remove(relacionamentoEmpresaCliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RelacionamentoEmpresaCliente> findRelacionamentoEmpresaClienteEntities() {
        return findRelacionamentoEmpresaClienteEntities(true, -1, -1);
    }

    public List<RelacionamentoEmpresaCliente> findRelacionamentoEmpresaClienteEntities(int maxResults, int firstResult) {
        return findRelacionamentoEmpresaClienteEntities(false, maxResults, firstResult);
    }

    private List<RelacionamentoEmpresaCliente> findRelacionamentoEmpresaClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RelacionamentoEmpresaCliente.class));
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

    public RelacionamentoEmpresaCliente findRelacionamentoEmpresaCliente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RelacionamentoEmpresaCliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getRelacionamentoEmpresaClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RelacionamentoEmpresaCliente> rt = cq.from(RelacionamentoEmpresaCliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
