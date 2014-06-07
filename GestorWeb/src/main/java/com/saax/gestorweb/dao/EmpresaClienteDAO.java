package com.saax.gestorweb.dao;

import com.saax.gestorweb.dao.exceptions.IllegalOrphanException;
import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.Meta;
import java.util.ArrayList;
import java.util.Collection;
import com.saax.gestorweb.model.datamodel.FilialCliente;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * DAO para o entity bean: EmpresaCliente <br><br>
 * 
 * Classe gerada automaticamente pelo netbeans: NÃO ALTERAR<br>
 * Caso seja necessária alguma customização, estender esta classe<br>
 * 
 * @author rodrigo
 */
public class EmpresaClienteDAO implements Serializable {

    public EmpresaClienteDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EmpresaCliente empresaCliente) {
        if (empresaCliente.getMetas() == null) {
            empresaCliente.setMetas(new ArrayList<Meta>());
        }
        if (empresaCliente.getFiliais() == null) {
            empresaCliente.setFiliais(new ArrayList<FilialCliente>());
        }
        if (empresaCliente.getSubEmpresas() == null) {
            empresaCliente.setSubEmpresas(new ArrayList<EmpresaCliente>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EmpresaCliente empresaPrincipal = empresaCliente.getEmpresaPrincipal();
            if (empresaPrincipal != null) {
                empresaPrincipal = em.getReference(empresaPrincipal.getClass(), empresaPrincipal.getId());
                empresaCliente.setEmpresaPrincipal(empresaPrincipal);
            }
            Collection<Meta> attachedMetas = new ArrayList<Meta>();
            for (Meta metasMetaToAttach : empresaCliente.getMetas()) {
                metasMetaToAttach = em.getReference(metasMetaToAttach.getClass(), metasMetaToAttach.getId());
                attachedMetas.add(metasMetaToAttach);
            }
            empresaCliente.setMetas(attachedMetas);
            Collection<FilialCliente> attachedFiliais = new ArrayList<FilialCliente>();
            for (FilialCliente filiaisFilialClienteToAttach : empresaCliente.getFiliais()) {
                filiaisFilialClienteToAttach = em.getReference(filiaisFilialClienteToAttach.getClass(), filiaisFilialClienteToAttach.getId());
                attachedFiliais.add(filiaisFilialClienteToAttach);
            }
            empresaCliente.setFiliais(attachedFiliais);
            Collection<EmpresaCliente> attachedSubEmpresas = new ArrayList<EmpresaCliente>();
            for (EmpresaCliente subEmpresasEmpresaClienteToAttach : empresaCliente.getSubEmpresas()) {
                subEmpresasEmpresaClienteToAttach = em.getReference(subEmpresasEmpresaClienteToAttach.getClass(), subEmpresasEmpresaClienteToAttach.getId());
                attachedSubEmpresas.add(subEmpresasEmpresaClienteToAttach);
            }
            empresaCliente.setSubEmpresas(attachedSubEmpresas);
            em.persist(empresaCliente);
            if (empresaPrincipal != null) {
                empresaPrincipal.getSubEmpresas().add(empresaCliente);
                empresaPrincipal = em.merge(empresaPrincipal);
            }
            for (Meta metasMeta : empresaCliente.getMetas()) {
                EmpresaCliente oldClienteOfMetasMeta = metasMeta.getCliente();
                metasMeta.setCliente(empresaCliente);
                metasMeta = em.merge(metasMeta);
                if (oldClienteOfMetasMeta != null) {
                    oldClienteOfMetasMeta.getMetas().remove(metasMeta);
                    oldClienteOfMetasMeta = em.merge(oldClienteOfMetasMeta);
                }
            }
            for (FilialCliente filiaisFilialCliente : empresaCliente.getFiliais()) {
                EmpresaCliente oldMatrizOfFiliaisFilialCliente = filiaisFilialCliente.getMatriz();
                filiaisFilialCliente.setMatriz(empresaCliente);
                filiaisFilialCliente = em.merge(filiaisFilialCliente);
                if (oldMatrizOfFiliaisFilialCliente != null) {
                    oldMatrizOfFiliaisFilialCliente.getFiliais().remove(filiaisFilialCliente);
                    oldMatrizOfFiliaisFilialCliente = em.merge(oldMatrizOfFiliaisFilialCliente);
                }
            }
            for (EmpresaCliente subEmpresasEmpresaCliente : empresaCliente.getSubEmpresas()) {
                EmpresaCliente oldEmpresaPrincipalOfSubEmpresasEmpresaCliente = subEmpresasEmpresaCliente.getEmpresaPrincipal();
                subEmpresasEmpresaCliente.setEmpresaPrincipal(empresaCliente);
                subEmpresasEmpresaCliente = em.merge(subEmpresasEmpresaCliente);
                if (oldEmpresaPrincipalOfSubEmpresasEmpresaCliente != null) {
                    oldEmpresaPrincipalOfSubEmpresasEmpresaCliente.getSubEmpresas().remove(subEmpresasEmpresaCliente);
                    oldEmpresaPrincipalOfSubEmpresasEmpresaCliente = em.merge(oldEmpresaPrincipalOfSubEmpresasEmpresaCliente);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EmpresaCliente empresaCliente) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EmpresaCliente persistentEmpresaCliente = em.find(EmpresaCliente.class, empresaCliente.getId());
            EmpresaCliente empresaPrincipalOld = persistentEmpresaCliente.getEmpresaPrincipal();
            EmpresaCliente empresaPrincipalNew = empresaCliente.getEmpresaPrincipal();
            Collection<Meta> metasOld = persistentEmpresaCliente.getMetas();
            Collection<Meta> metasNew = empresaCliente.getMetas();
            Collection<FilialCliente> filiaisOld = persistentEmpresaCliente.getFiliais();
            Collection<FilialCliente> filiaisNew = empresaCliente.getFiliais();
            Collection<EmpresaCliente> subEmpresasOld = persistentEmpresaCliente.getSubEmpresas();
            Collection<EmpresaCliente> subEmpresasNew = empresaCliente.getSubEmpresas();
            List<String> illegalOrphanMessages = null;
            for (Meta metasOldMeta : metasOld) {
                if (!metasNew.contains(metasOldMeta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Meta " + metasOldMeta + " since its cliente field is not nullable.");
                }
            }
            for (FilialCliente filiaisOldFilialCliente : filiaisOld) {
                if (!filiaisNew.contains(filiaisOldFilialCliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain FilialCliente " + filiaisOldFilialCliente + " since its matriz field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (empresaPrincipalNew != null) {
                empresaPrincipalNew = em.getReference(empresaPrincipalNew.getClass(), empresaPrincipalNew.getId());
                empresaCliente.setEmpresaPrincipal(empresaPrincipalNew);
            }
            Collection<Meta> attachedMetasNew = new ArrayList<Meta>();
            for (Meta metasNewMetaToAttach : metasNew) {
                metasNewMetaToAttach = em.getReference(metasNewMetaToAttach.getClass(), metasNewMetaToAttach.getId());
                attachedMetasNew.add(metasNewMetaToAttach);
            }
            metasNew = attachedMetasNew;
            empresaCliente.setMetas(metasNew);
            Collection<FilialCliente> attachedFiliaisNew = new ArrayList<FilialCliente>();
            for (FilialCliente filiaisNewFilialClienteToAttach : filiaisNew) {
                filiaisNewFilialClienteToAttach = em.getReference(filiaisNewFilialClienteToAttach.getClass(), filiaisNewFilialClienteToAttach.getId());
                attachedFiliaisNew.add(filiaisNewFilialClienteToAttach);
            }
            filiaisNew = attachedFiliaisNew;
            empresaCliente.setFiliais(filiaisNew);
            Collection<EmpresaCliente> attachedSubEmpresasNew = new ArrayList<EmpresaCliente>();
            for (EmpresaCliente subEmpresasNewEmpresaClienteToAttach : subEmpresasNew) {
                subEmpresasNewEmpresaClienteToAttach = em.getReference(subEmpresasNewEmpresaClienteToAttach.getClass(), subEmpresasNewEmpresaClienteToAttach.getId());
                attachedSubEmpresasNew.add(subEmpresasNewEmpresaClienteToAttach);
            }
            subEmpresasNew = attachedSubEmpresasNew;
            empresaCliente.setSubEmpresas(subEmpresasNew);
            empresaCliente = em.merge(empresaCliente);
            if (empresaPrincipalOld != null && !empresaPrincipalOld.equals(empresaPrincipalNew)) {
                empresaPrincipalOld.getSubEmpresas().remove(empresaCliente);
                empresaPrincipalOld = em.merge(empresaPrincipalOld);
            }
            if (empresaPrincipalNew != null && !empresaPrincipalNew.equals(empresaPrincipalOld)) {
                empresaPrincipalNew.getSubEmpresas().add(empresaCliente);
                empresaPrincipalNew = em.merge(empresaPrincipalNew);
            }
            for (Meta metasNewMeta : metasNew) {
                if (!metasOld.contains(metasNewMeta)) {
                    EmpresaCliente oldClienteOfMetasNewMeta = metasNewMeta.getCliente();
                    metasNewMeta.setCliente(empresaCliente);
                    metasNewMeta = em.merge(metasNewMeta);
                    if (oldClienteOfMetasNewMeta != null && !oldClienteOfMetasNewMeta.equals(empresaCliente)) {
                        oldClienteOfMetasNewMeta.getMetas().remove(metasNewMeta);
                        oldClienteOfMetasNewMeta = em.merge(oldClienteOfMetasNewMeta);
                    }
                }
            }
            for (FilialCliente filiaisNewFilialCliente : filiaisNew) {
                if (!filiaisOld.contains(filiaisNewFilialCliente)) {
                    EmpresaCliente oldMatrizOfFiliaisNewFilialCliente = filiaisNewFilialCliente.getMatriz();
                    filiaisNewFilialCliente.setMatriz(empresaCliente);
                    filiaisNewFilialCliente = em.merge(filiaisNewFilialCliente);
                    if (oldMatrizOfFiliaisNewFilialCliente != null && !oldMatrizOfFiliaisNewFilialCliente.equals(empresaCliente)) {
                        oldMatrizOfFiliaisNewFilialCliente.getFiliais().remove(filiaisNewFilialCliente);
                        oldMatrizOfFiliaisNewFilialCliente = em.merge(oldMatrizOfFiliaisNewFilialCliente);
                    }
                }
            }
            for (EmpresaCliente subEmpresasOldEmpresaCliente : subEmpresasOld) {
                if (!subEmpresasNew.contains(subEmpresasOldEmpresaCliente)) {
                    subEmpresasOldEmpresaCliente.setEmpresaPrincipal(null);
                    subEmpresasOldEmpresaCliente = em.merge(subEmpresasOldEmpresaCliente);
                }
            }
            for (EmpresaCliente subEmpresasNewEmpresaCliente : subEmpresasNew) {
                if (!subEmpresasOld.contains(subEmpresasNewEmpresaCliente)) {
                    EmpresaCliente oldEmpresaPrincipalOfSubEmpresasNewEmpresaCliente = subEmpresasNewEmpresaCliente.getEmpresaPrincipal();
                    subEmpresasNewEmpresaCliente.setEmpresaPrincipal(empresaCliente);
                    subEmpresasNewEmpresaCliente = em.merge(subEmpresasNewEmpresaCliente);
                    if (oldEmpresaPrincipalOfSubEmpresasNewEmpresaCliente != null && !oldEmpresaPrincipalOfSubEmpresasNewEmpresaCliente.equals(empresaCliente)) {
                        oldEmpresaPrincipalOfSubEmpresasNewEmpresaCliente.getSubEmpresas().remove(subEmpresasNewEmpresaCliente);
                        oldEmpresaPrincipalOfSubEmpresasNewEmpresaCliente = em.merge(oldEmpresaPrincipalOfSubEmpresasNewEmpresaCliente);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = empresaCliente.getId();
                if (findEmpresaCliente(id) == null) {
                    throw new NonexistentEntityException("The empresaCliente with id " + id + " no longer exists.");
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
            EmpresaCliente empresaCliente;
            try {
                empresaCliente = em.getReference(EmpresaCliente.class, id);
                empresaCliente.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empresaCliente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Meta> metasOrphanCheck = empresaCliente.getMetas();
            for (Meta metasOrphanCheckMeta : metasOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EmpresaCliente (" + empresaCliente + ") cannot be destroyed since the Meta " + metasOrphanCheckMeta + " in its metas field has a non-nullable cliente field.");
            }
            Collection<FilialCliente> filiaisOrphanCheck = empresaCliente.getFiliais();
            for (FilialCliente filiaisOrphanCheckFilialCliente : filiaisOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EmpresaCliente (" + empresaCliente + ") cannot be destroyed since the FilialCliente " + filiaisOrphanCheckFilialCliente + " in its filiais field has a non-nullable matriz field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            EmpresaCliente empresaPrincipal = empresaCliente.getEmpresaPrincipal();
            if (empresaPrincipal != null) {
                empresaPrincipal.getSubEmpresas().remove(empresaCliente);
                empresaPrincipal = em.merge(empresaPrincipal);
            }
            Collection<EmpresaCliente> subEmpresas = empresaCliente.getSubEmpresas();
            for (EmpresaCliente subEmpresasEmpresaCliente : subEmpresas) {
                subEmpresasEmpresaCliente.setEmpresaPrincipal(null);
                subEmpresasEmpresaCliente = em.merge(subEmpresasEmpresaCliente);
            }
            em.remove(empresaCliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EmpresaCliente> findEmpresaClienteEntities() {
        return findEmpresaClienteEntities(true, -1, -1);
    }

    public List<EmpresaCliente> findEmpresaClienteEntities(int maxResults, int firstResult) {
        return findEmpresaClienteEntities(false, maxResults, firstResult);
    }

    private List<EmpresaCliente> findEmpresaClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EmpresaCliente.class));
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

    public EmpresaCliente findEmpresaCliente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EmpresaCliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpresaClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EmpresaCliente> rt = cq.from(EmpresaCliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
