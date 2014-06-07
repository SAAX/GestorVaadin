package com.saax.gestorweb.dao;

import com.saax.gestorweb.dao.exceptions.IllegalOrphanException;
import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.FilialEmpresa;
import java.util.ArrayList;
import java.util.Collection;
import com.saax.gestorweb.model.datamodel.Meta;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * DAO para o entity bean: Empresa <br><br>
 * 
 * Classe gerada automaticamente pelo netbeans: NÃO ALTERAR<br>
 * Caso seja necessária alguma customização, estender esta classe<br>
 * 
 * @author rodrigo
 */
public class EmpresaDAO implements Serializable {

    public EmpresaDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empresa empresa) {
        if (empresa.getFiliais() == null) {
            empresa.setFiliais(new ArrayList<FilialEmpresa>());
        }
        if (empresa.getMetas() == null) {
            empresa.setMetas(new ArrayList<Meta>());
        }
        if (empresa.getSubEmpresas() == null) {
            empresa.setSubEmpresas(new ArrayList<Empresa>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa empresaPrincipal = empresa.getEmpresaPrincipal();
            if (empresaPrincipal != null) {
                empresaPrincipal = em.getReference(empresaPrincipal.getClass(), empresaPrincipal.getId());
                empresa.setEmpresaPrincipal(empresaPrincipal);
            }
            Collection<FilialEmpresa> attachedFiliais = new ArrayList<FilialEmpresa>();
            for (FilialEmpresa filiaisFilialEmpresaToAttach : empresa.getFiliais()) {
                filiaisFilialEmpresaToAttach = em.getReference(filiaisFilialEmpresaToAttach.getClass(), filiaisFilialEmpresaToAttach.getId());
                attachedFiliais.add(filiaisFilialEmpresaToAttach);
            }
            empresa.setFiliais(attachedFiliais);
            Collection<Meta> attachedMetas = new ArrayList<Meta>();
            for (Meta metasMetaToAttach : empresa.getMetas()) {
                metasMetaToAttach = em.getReference(metasMetaToAttach.getClass(), metasMetaToAttach.getId());
                attachedMetas.add(metasMetaToAttach);
            }
            empresa.setMetas(attachedMetas);
            Collection<Empresa> attachedSubEmpresas = new ArrayList<Empresa>();
            for (Empresa subEmpresasEmpresaToAttach : empresa.getSubEmpresas()) {
                subEmpresasEmpresaToAttach = em.getReference(subEmpresasEmpresaToAttach.getClass(), subEmpresasEmpresaToAttach.getId());
                attachedSubEmpresas.add(subEmpresasEmpresaToAttach);
            }
            empresa.setSubEmpresas(attachedSubEmpresas);
            em.persist(empresa);
            if (empresaPrincipal != null) {
                empresaPrincipal.getSubEmpresas().add(empresa);
                empresaPrincipal = em.merge(empresaPrincipal);
            }
            for (FilialEmpresa filiaisFilialEmpresa : empresa.getFiliais()) {
                Empresa oldMatrizOfFiliaisFilialEmpresa = filiaisFilialEmpresa.getMatriz();
                filiaisFilialEmpresa.setMatriz(empresa);
                filiaisFilialEmpresa = em.merge(filiaisFilialEmpresa);
                if (oldMatrizOfFiliaisFilialEmpresa != null) {
                    oldMatrizOfFiliaisFilialEmpresa.getFiliais().remove(filiaisFilialEmpresa);
                    oldMatrizOfFiliaisFilialEmpresa = em.merge(oldMatrizOfFiliaisFilialEmpresa);
                }
            }
            for (Meta metasMeta : empresa.getMetas()) {
                Empresa oldEmpresaOfMetasMeta = metasMeta.getEmpresa();
                metasMeta.setEmpresa(empresa);
                metasMeta = em.merge(metasMeta);
                if (oldEmpresaOfMetasMeta != null) {
                    oldEmpresaOfMetasMeta.getMetas().remove(metasMeta);
                    oldEmpresaOfMetasMeta = em.merge(oldEmpresaOfMetasMeta);
                }
            }
            for (Empresa subEmpresasEmpresa : empresa.getSubEmpresas()) {
                Empresa oldEmpresaPrincipalOfSubEmpresasEmpresa = subEmpresasEmpresa.getEmpresaPrincipal();
                subEmpresasEmpresa.setEmpresaPrincipal(empresa);
                subEmpresasEmpresa = em.merge(subEmpresasEmpresa);
                if (oldEmpresaPrincipalOfSubEmpresasEmpresa != null) {
                    oldEmpresaPrincipalOfSubEmpresasEmpresa.getSubEmpresas().remove(subEmpresasEmpresa);
                    oldEmpresaPrincipalOfSubEmpresasEmpresa = em.merge(oldEmpresaPrincipalOfSubEmpresasEmpresa);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empresa empresa) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa persistentEmpresa = em.find(Empresa.class, empresa.getId());
            Empresa empresaPrincipalOld = persistentEmpresa.getEmpresaPrincipal();
            Empresa empresaPrincipalNew = empresa.getEmpresaPrincipal();
            Collection<FilialEmpresa> filiaisOld = persistentEmpresa.getFiliais();
            Collection<FilialEmpresa> filiaisNew = empresa.getFiliais();
            Collection<Meta> metasOld = persistentEmpresa.getMetas();
            Collection<Meta> metasNew = empresa.getMetas();
            Collection<Empresa> subEmpresasOld = persistentEmpresa.getSubEmpresas();
            Collection<Empresa> subEmpresasNew = empresa.getSubEmpresas();
            List<String> illegalOrphanMessages = null;
            for (FilialEmpresa filiaisOldFilialEmpresa : filiaisOld) {
                if (!filiaisNew.contains(filiaisOldFilialEmpresa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain FilialEmpresa " + filiaisOldFilialEmpresa + " since its matriz field is not nullable.");
                }
            }
            for (Meta metasOldMeta : metasOld) {
                if (!metasNew.contains(metasOldMeta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Meta " + metasOldMeta + " since its empresa field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (empresaPrincipalNew != null) {
                empresaPrincipalNew = em.getReference(empresaPrincipalNew.getClass(), empresaPrincipalNew.getId());
                empresa.setEmpresaPrincipal(empresaPrincipalNew);
            }
            Collection<FilialEmpresa> attachedFiliaisNew = new ArrayList<FilialEmpresa>();
            for (FilialEmpresa filiaisNewFilialEmpresaToAttach : filiaisNew) {
                filiaisNewFilialEmpresaToAttach = em.getReference(filiaisNewFilialEmpresaToAttach.getClass(), filiaisNewFilialEmpresaToAttach.getId());
                attachedFiliaisNew.add(filiaisNewFilialEmpresaToAttach);
            }
            filiaisNew = attachedFiliaisNew;
            empresa.setFiliais(filiaisNew);
            Collection<Meta> attachedMetasNew = new ArrayList<Meta>();
            for (Meta metasNewMetaToAttach : metasNew) {
                metasNewMetaToAttach = em.getReference(metasNewMetaToAttach.getClass(), metasNewMetaToAttach.getId());
                attachedMetasNew.add(metasNewMetaToAttach);
            }
            metasNew = attachedMetasNew;
            empresa.setMetas(metasNew);
            Collection<Empresa> attachedSubEmpresasNew = new ArrayList<Empresa>();
            for (Empresa subEmpresasNewEmpresaToAttach : subEmpresasNew) {
                subEmpresasNewEmpresaToAttach = em.getReference(subEmpresasNewEmpresaToAttach.getClass(), subEmpresasNewEmpresaToAttach.getId());
                attachedSubEmpresasNew.add(subEmpresasNewEmpresaToAttach);
            }
            subEmpresasNew = attachedSubEmpresasNew;
            empresa.setSubEmpresas(subEmpresasNew);
            empresa = em.merge(empresa);
            if (empresaPrincipalOld != null && !empresaPrincipalOld.equals(empresaPrincipalNew)) {
                empresaPrincipalOld.getSubEmpresas().remove(empresa);
                empresaPrincipalOld = em.merge(empresaPrincipalOld);
            }
            if (empresaPrincipalNew != null && !empresaPrincipalNew.equals(empresaPrincipalOld)) {
                empresaPrincipalNew.getSubEmpresas().add(empresa);
                empresaPrincipalNew = em.merge(empresaPrincipalNew);
            }
            for (FilialEmpresa filiaisNewFilialEmpresa : filiaisNew) {
                if (!filiaisOld.contains(filiaisNewFilialEmpresa)) {
                    Empresa oldMatrizOfFiliaisNewFilialEmpresa = filiaisNewFilialEmpresa.getMatriz();
                    filiaisNewFilialEmpresa.setMatriz(empresa);
                    filiaisNewFilialEmpresa = em.merge(filiaisNewFilialEmpresa);
                    if (oldMatrizOfFiliaisNewFilialEmpresa != null && !oldMatrizOfFiliaisNewFilialEmpresa.equals(empresa)) {
                        oldMatrizOfFiliaisNewFilialEmpresa.getFiliais().remove(filiaisNewFilialEmpresa);
                        oldMatrizOfFiliaisNewFilialEmpresa = em.merge(oldMatrizOfFiliaisNewFilialEmpresa);
                    }
                }
            }
            for (Meta metasNewMeta : metasNew) {
                if (!metasOld.contains(metasNewMeta)) {
                    Empresa oldEmpresaOfMetasNewMeta = metasNewMeta.getEmpresa();
                    metasNewMeta.setEmpresa(empresa);
                    metasNewMeta = em.merge(metasNewMeta);
                    if (oldEmpresaOfMetasNewMeta != null && !oldEmpresaOfMetasNewMeta.equals(empresa)) {
                        oldEmpresaOfMetasNewMeta.getMetas().remove(metasNewMeta);
                        oldEmpresaOfMetasNewMeta = em.merge(oldEmpresaOfMetasNewMeta);
                    }
                }
            }
            for (Empresa subEmpresasOldEmpresa : subEmpresasOld) {
                if (!subEmpresasNew.contains(subEmpresasOldEmpresa)) {
                    subEmpresasOldEmpresa.setEmpresaPrincipal(null);
                    subEmpresasOldEmpresa = em.merge(subEmpresasOldEmpresa);
                }
            }
            for (Empresa subEmpresasNewEmpresa : subEmpresasNew) {
                if (!subEmpresasOld.contains(subEmpresasNewEmpresa)) {
                    Empresa oldEmpresaPrincipalOfSubEmpresasNewEmpresa = subEmpresasNewEmpresa.getEmpresaPrincipal();
                    subEmpresasNewEmpresa.setEmpresaPrincipal(empresa);
                    subEmpresasNewEmpresa = em.merge(subEmpresasNewEmpresa);
                    if (oldEmpresaPrincipalOfSubEmpresasNewEmpresa != null && !oldEmpresaPrincipalOfSubEmpresasNewEmpresa.equals(empresa)) {
                        oldEmpresaPrincipalOfSubEmpresasNewEmpresa.getSubEmpresas().remove(subEmpresasNewEmpresa);
                        oldEmpresaPrincipalOfSubEmpresasNewEmpresa = em.merge(oldEmpresaPrincipalOfSubEmpresasNewEmpresa);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = empresa.getId();
                if (findEmpresa(id) == null) {
                    throw new NonexistentEntityException("The empresa with id " + id + " no longer exists.");
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
            Empresa empresa;
            try {
                empresa = em.getReference(Empresa.class, id);
                empresa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empresa with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<FilialEmpresa> filiaisOrphanCheck = empresa.getFiliais();
            for (FilialEmpresa filiaisOrphanCheckFilialEmpresa : filiaisOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empresa (" + empresa + ") cannot be destroyed since the FilialEmpresa " + filiaisOrphanCheckFilialEmpresa + " in its filiais field has a non-nullable matriz field.");
            }
            Collection<Meta> metasOrphanCheck = empresa.getMetas();
            for (Meta metasOrphanCheckMeta : metasOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empresa (" + empresa + ") cannot be destroyed since the Meta " + metasOrphanCheckMeta + " in its metas field has a non-nullable empresa field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Empresa empresaPrincipal = empresa.getEmpresaPrincipal();
            if (empresaPrincipal != null) {
                empresaPrincipal.getSubEmpresas().remove(empresa);
                empresaPrincipal = em.merge(empresaPrincipal);
            }
            Collection<Empresa> subEmpresas = empresa.getSubEmpresas();
            for (Empresa subEmpresasEmpresa : subEmpresas) {
                subEmpresasEmpresa.setEmpresaPrincipal(null);
                subEmpresasEmpresa = em.merge(subEmpresasEmpresa);
            }
            em.remove(empresa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empresa> findEmpresaEntities() {
        return findEmpresaEntities(true, -1, -1);
    }

    public List<Empresa> findEmpresaEntities(int maxResults, int firstResult) {
        return findEmpresaEntities(false, maxResults, firstResult);
    }

    private List<Empresa> findEmpresaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empresa.class));
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

    public Empresa findEmpresa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empresa.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpresaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empresa> rt = cq.from(Empresa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
