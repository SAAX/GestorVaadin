/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.dao;

import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import com.saax.gestorweb.model.datamodel.CentroCusto;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.Meta;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author rodrigo
 */
public class CentroCustoJpaController implements Serializable {

    public CentroCustoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CentroCusto centroCusto) {
        if (centroCusto.getMetas() == null) {
            centroCusto.setMetas(new ArrayList<Meta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa empresa = centroCusto.getEmpresa();
            if (empresa != null) {
                empresa = em.getReference(empresa.getClass(), empresa.getIdEmpresa());
                centroCusto.setEmpresa(empresa);
            }
            Collection<Meta> attachedMetas = new ArrayList<Meta>();
            for (Meta metasMetaToAttach : centroCusto.getMetas()) {
                metasMetaToAttach = em.getReference(metasMetaToAttach.getClass(), metasMetaToAttach.getIdMeta());
                attachedMetas.add(metasMetaToAttach);
            }
            centroCusto.setMetas(attachedMetas);
            em.persist(centroCusto);
            if (empresa != null) {
                empresa.getCentrosDeCusto().add(centroCusto);
                empresa = em.merge(empresa);
            }
            for (Meta metasMeta : centroCusto.getMetas()) {
                CentroCusto oldIdCentroCustoOfMetasMeta = metasMeta.getCentroCusto();
                metasMeta.setCentroCusto(centroCusto);
                metasMeta = em.merge(metasMeta);
                if (oldIdCentroCustoOfMetasMeta != null) {
                    oldIdCentroCustoOfMetasMeta.getMetas().remove(metasMeta);
                    oldIdCentroCustoOfMetasMeta = em.merge(oldIdCentroCustoOfMetasMeta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CentroCusto centroCusto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CentroCusto persistentCentroCusto = em.find(CentroCusto.class, centroCusto.getIdCentroCusto());
            Empresa empresaOld = persistentCentroCusto.getEmpresa();
            Empresa empresaNew = centroCusto.getEmpresa();
            Collection<Meta> metasOld = persistentCentroCusto.getMetas();
            Collection<Meta> metasNew = centroCusto.getMetas();
            if (empresaNew != null) {
                empresaNew = em.getReference(empresaNew.getClass(), empresaNew.getIdEmpresa());
                centroCusto.setEmpresa(empresaNew);
            }
            Collection<Meta> attachedMetasNew = new ArrayList<Meta>();
            for (Meta metasNewMetaToAttach : metasNew) {
                metasNewMetaToAttach = em.getReference(metasNewMetaToAttach.getClass(), metasNewMetaToAttach.getIdMeta());
                attachedMetasNew.add(metasNewMetaToAttach);
            }
            metasNew = attachedMetasNew;
            centroCusto.setMetas(metasNew);
            centroCusto = em.merge(centroCusto);
            if (empresaOld != null && !empresaOld.equals(empresaNew)) {
                empresaOld.getCentrosDeCusto().remove(centroCusto);
                empresaOld = em.merge(empresaOld);
            }
            if (empresaNew != null && !empresaNew.equals(empresaOld)) {
                empresaNew.getCentrosDeCusto().add(centroCusto);
                empresaNew = em.merge(empresaNew);
            }
            for (Meta metasOldMeta : metasOld) {
                if (!metasNew.contains(metasOldMeta)) {
                    metasOldMeta.setCentroCusto(null);
                    metasOldMeta = em.merge(metasOldMeta);
                }
            }
            for (Meta metasNewMeta : metasNew) {
                if (!metasOld.contains(metasNewMeta)) {
                    CentroCusto oldIdCentroCustoOfMetasNewMeta = metasNewMeta.getCentroCusto();
                    metasNewMeta.setCentroCusto(centroCusto);
                    metasNewMeta = em.merge(metasNewMeta);
                    if (oldIdCentroCustoOfMetasNewMeta != null && !oldIdCentroCustoOfMetasNewMeta.equals(centroCusto)) {
                        oldIdCentroCustoOfMetasNewMeta.getMetas().remove(metasNewMeta);
                        oldIdCentroCustoOfMetasNewMeta = em.merge(oldIdCentroCustoOfMetasNewMeta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = centroCusto.getIdCentroCusto();
                if (findCentroCusto(id) == null) {
                    throw new NonexistentEntityException("The centroCusto with id " + id + " no longer exists.");
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
            CentroCusto centroCusto;
            try {
                centroCusto = em.getReference(CentroCusto.class, id);
                centroCusto.getIdCentroCusto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The centroCusto with id " + id + " no longer exists.", enfe);
            }
            Empresa empresa = centroCusto.getEmpresa();
            if (empresa != null) {
                empresa.getCentrosDeCusto().remove(centroCusto);
                empresa = em.merge(empresa);
            }
            Collection<Meta> metas = centroCusto.getMetas();
            for (Meta metasMeta : metas) {
                metasMeta.setCentroCusto(null);
                metasMeta = em.merge(metasMeta);
            }
            em.remove(centroCusto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CentroCusto> findCentroCustoEntities() {
        return findCentroCustoEntities(true, -1, -1);
    }

    public List<CentroCusto> findCentroCustoEntities(int maxResults, int firstResult) {
        return findCentroCustoEntities(false, maxResults, firstResult);
    }

    private List<CentroCusto> findCentroCustoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CentroCusto.class));
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

    public CentroCusto findCentroCusto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CentroCusto.class, id);
        } finally {
            em.close();
        }
    }

    public int getCentroCustoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CentroCusto> rt = cq.from(CentroCusto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
