package com.saax.gestorweb.dao;

import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import com.saax.gestorweb.model.datamodel.CentroCusto;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.model.datamodel.Meta;
import java.util.ArrayList;
import java.util.Collection;
import com.saax.gestorweb.model.datamodel.Tarefa;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * DAO para o entity bean: CentroCusto <br><br>
 * 
 * @author rodrigo
 */
public class CentroCustoDAO implements Serializable {

    public CentroCustoDAO(EntityManagerFactory emf) {
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
        if (centroCusto.getTarefas() == null) {
            centroCusto.setTarefas(new ArrayList<Tarefa>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa empresa = centroCusto.getEmpresa();
            if (empresa != null) {
                empresa = em.getReference(empresa.getClass(), empresa.getId());
                centroCusto.setEmpresa(empresa);
            }
            Usuario idusuarioinclusao = centroCusto.getIdUsuarioInclusao();
            if (idusuarioinclusao != null) {
                idusuarioinclusao = em.getReference(idusuarioinclusao.getClass(), idusuarioinclusao.getId());
                centroCusto.setIdUsuarioInclusao(idusuarioinclusao);
            }
            Collection<Meta> attachedMetas = new ArrayList<Meta>();
            for (Meta metasMetaToAttach : centroCusto.getMetas()) {
                metasMetaToAttach = em.getReference(metasMetaToAttach.getClass(), metasMetaToAttach.getId());
                attachedMetas.add(metasMetaToAttach);
            }
            centroCusto.setMetas(attachedMetas);
            List<Tarefa> attachedTarefaList = new ArrayList<Tarefa>();
            for (Tarefa tarefaListTarefaToAttach : centroCusto.getTarefas()) {
                tarefaListTarefaToAttach = em.getReference(tarefaListTarefaToAttach.getClass(), tarefaListTarefaToAttach.getId());
                attachedTarefaList.add(tarefaListTarefaToAttach);
            }
            centroCusto.setTarefas(attachedTarefaList);
            em.persist(centroCusto);
            if (empresa != null) {
                empresa.getCentrosDeCusto().add(centroCusto);
                empresa = em.merge(empresa);
            }
            if (idusuarioinclusao != null) {
                idusuarioinclusao.getCentroCustoList().add(centroCusto);
                idusuarioinclusao = em.merge(idusuarioinclusao);
            }
            for (Meta metasMeta : centroCusto.getMetas()) {
                CentroCusto oldCentroCustoOfMetasMeta = metasMeta.getCentroCusto();
                metasMeta.setCentroCusto(centroCusto);
                metasMeta = em.merge(metasMeta);
                if (oldCentroCustoOfMetasMeta != null) {
                    oldCentroCustoOfMetasMeta.getMetas().remove(metasMeta);
                    oldCentroCustoOfMetasMeta = em.merge(oldCentroCustoOfMetasMeta);
                }
            }
            for (Tarefa tarefaListTarefa : centroCusto.getTarefas()) {
                CentroCusto oldIdcentrocustoOfTarefaListTarefa = tarefaListTarefa.getIdCentroCusto();
                tarefaListTarefa.setIdCentroCusto(centroCusto);
                tarefaListTarefa = em.merge(tarefaListTarefa);
                if (oldIdcentrocustoOfTarefaListTarefa != null) {
                    oldIdcentrocustoOfTarefaListTarefa.getTarefas().remove(tarefaListTarefa);
                    oldIdcentrocustoOfTarefaListTarefa = em.merge(oldIdcentrocustoOfTarefaListTarefa);
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
            CentroCusto persistentCentroCusto = em.find(CentroCusto.class, centroCusto.getId());
            Empresa empresaOld = persistentCentroCusto.getEmpresa();
            Empresa empresaNew = centroCusto.getEmpresa();
            Usuario idusuarioinclusaoOld = persistentCentroCusto.getIdUsuarioInclusao();
            Usuario idusuarioinclusaoNew = centroCusto.getIdUsuarioInclusao();
            Collection<Meta> metasOld = persistentCentroCusto.getMetas();
            Collection<Meta> metasNew = centroCusto.getMetas();
            List<Tarefa> tarefaListOld = persistentCentroCusto.getTarefas();
            List<Tarefa> tarefaListNew = centroCusto.getTarefas();
            if (empresaNew != null) {
                empresaNew = em.getReference(empresaNew.getClass(), empresaNew.getId());
                centroCusto.setEmpresa(empresaNew);
            }
            if (idusuarioinclusaoNew != null) {
                idusuarioinclusaoNew = em.getReference(idusuarioinclusaoNew.getClass(), idusuarioinclusaoNew.getId());
                centroCusto.setIdUsuarioInclusao(idusuarioinclusaoNew);
            }
            Collection<Meta> attachedMetasNew = new ArrayList<Meta>();
            for (Meta metasNewMetaToAttach : metasNew) {
                metasNewMetaToAttach = em.getReference(metasNewMetaToAttach.getClass(), metasNewMetaToAttach.getId());
                attachedMetasNew.add(metasNewMetaToAttach);
            }
            metasNew = attachedMetasNew;
            centroCusto.setMetas(metasNew);
            List<Tarefa> attachedTarefaListNew = new ArrayList<Tarefa>();
            for (Tarefa tarefaListNewTarefaToAttach : tarefaListNew) {
                tarefaListNewTarefaToAttach = em.getReference(tarefaListNewTarefaToAttach.getClass(), tarefaListNewTarefaToAttach.getId());
                attachedTarefaListNew.add(tarefaListNewTarefaToAttach);
            }
            tarefaListNew = attachedTarefaListNew;
            centroCusto.setTarefas(tarefaListNew);
            centroCusto = em.merge(centroCusto);
            if (empresaOld != null && !empresaOld.equals(empresaNew)) {
                empresaOld.getCentrosDeCusto().remove(centroCusto);
                empresaOld = em.merge(empresaOld);
            }
            if (empresaNew != null && !empresaNew.equals(empresaOld)) {
                empresaNew.getCentrosDeCusto().add(centroCusto);
                empresaNew = em.merge(empresaNew);
            }
            if (idusuarioinclusaoOld != null && !idusuarioinclusaoOld.equals(idusuarioinclusaoNew)) {
                idusuarioinclusaoOld.getCentroCustoList().remove(centroCusto);
                idusuarioinclusaoOld = em.merge(idusuarioinclusaoOld);
            }
            if (idusuarioinclusaoNew != null && !idusuarioinclusaoNew.equals(idusuarioinclusaoOld)) {
                idusuarioinclusaoNew.getCentroCustoList().add(centroCusto);
                idusuarioinclusaoNew = em.merge(idusuarioinclusaoNew);
            }
            for (Meta metasOldMeta : metasOld) {
                if (!metasNew.contains(metasOldMeta)) {
                    metasOldMeta.setCentroCusto(null);
                    metasOldMeta = em.merge(metasOldMeta);
                }
            }
            for (Meta metasNewMeta : metasNew) {
                if (!metasOld.contains(metasNewMeta)) {
                    CentroCusto oldCentroCustoOfMetasNewMeta = metasNewMeta.getCentroCusto();
                    metasNewMeta.setCentroCusto(centroCusto);
                    metasNewMeta = em.merge(metasNewMeta);
                    if (oldCentroCustoOfMetasNewMeta != null && !oldCentroCustoOfMetasNewMeta.equals(centroCusto)) {
                        oldCentroCustoOfMetasNewMeta.getMetas().remove(metasNewMeta);
                        oldCentroCustoOfMetasNewMeta = em.merge(oldCentroCustoOfMetasNewMeta);
                    }
                }
            }
            for (Tarefa tarefaListOldTarefa : tarefaListOld) {
                if (!tarefaListNew.contains(tarefaListOldTarefa)) {
                    tarefaListOldTarefa.setIdCentroCusto(null);
                    tarefaListOldTarefa = em.merge(tarefaListOldTarefa);
                }
            }
            for (Tarefa tarefaListNewTarefa : tarefaListNew) {
                if (!tarefaListOld.contains(tarefaListNewTarefa)) {
                    CentroCusto oldIdcentrocustoOfTarefaListNewTarefa = tarefaListNewTarefa.getIdCentroCusto();
                    tarefaListNewTarefa.setIdCentroCusto(centroCusto);
                    tarefaListNewTarefa = em.merge(tarefaListNewTarefa);
                    if (oldIdcentrocustoOfTarefaListNewTarefa != null && !oldIdcentrocustoOfTarefaListNewTarefa.equals(centroCusto)) {
                        oldIdcentrocustoOfTarefaListNewTarefa.getTarefas().remove(tarefaListNewTarefa);
                        oldIdcentrocustoOfTarefaListNewTarefa = em.merge(oldIdcentrocustoOfTarefaListNewTarefa);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = centroCusto.getId();
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
                centroCusto.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The centroCusto with id " + id + " no longer exists.", enfe);
            }
            Empresa empresa = centroCusto.getEmpresa();
            if (empresa != null) {
                empresa.getCentrosDeCusto().remove(centroCusto);
                empresa = em.merge(empresa);
            }
            Usuario idusuarioinclusao = centroCusto.getIdUsuarioInclusao();
            if (idusuarioinclusao != null) {
                idusuarioinclusao.getCentroCustoList().remove(centroCusto);
                idusuarioinclusao = em.merge(idusuarioinclusao);
            }
            Collection<Meta> metas = centroCusto.getMetas();
            for (Meta metasMeta : metas) {
                metasMeta.setCentroCusto(null);
                metasMeta = em.merge(metasMeta);
            }
            List<Tarefa> tarefaList = centroCusto.getTarefas();
            for (Tarefa tarefaListTarefa : tarefaList) {
                tarefaListTarefa.setIdCentroCusto(null);
                tarefaListTarefa = em.merge(tarefaListTarefa);
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
