package com.saax.gestorweb.dao;

import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import com.saax.gestorweb.model.datamodel.Departamento;
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
 * DAO para o entity bean: Departamento <br><br>
 * 
 * 
 * @author rodrigo
 */
public class DepartamentoDAO implements Serializable {

    public DepartamentoDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Departamento departamento) {
        if (departamento.getMetas() == null) {
            departamento.setMetas(new ArrayList<Meta>());
        }
        if (departamento.getTarefas() == null) {
            departamento.setTarefas(new ArrayList<Tarefa>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa empresa = departamento.getEmpresa();
            if (empresa != null) {
                empresa = em.getReference(empresa.getClass(), empresa.getId());
                departamento.setEmpresa(empresa);
            }
            Usuario usuarioInclusao = departamento.getUsuarioInclusao();
            if (usuarioInclusao != null) {
                usuarioInclusao = em.getReference(usuarioInclusao.getClass(), usuarioInclusao.getId());
                departamento.setUsuarioInclusao(usuarioInclusao);
            }
            Collection<Meta> attachedMetas = new ArrayList<Meta>();
            for (Meta metasMetaToAttach : departamento.getMetas()) {
                metasMetaToAttach = em.getReference(metasMetaToAttach.getClass(), metasMetaToAttach.getId());
                attachedMetas.add(metasMetaToAttach);
            }
            departamento.setMetas(attachedMetas);
            List<Tarefa> attachedTarefas = new ArrayList<Tarefa>();
            for (Tarefa tarefasTarefaToAttach : departamento.getTarefas()) {
                tarefasTarefaToAttach = em.getReference(tarefasTarefaToAttach.getClass(), tarefasTarefaToAttach.getId());
                attachedTarefas.add(tarefasTarefaToAttach);
            }
            departamento.setTarefas(attachedTarefas);
            em.persist(departamento);
            if (empresa != null) {
                empresa.getDepartamentos().add(departamento);
                empresa = em.merge(empresa);
            }
            if (usuarioInclusao != null) {
                usuarioInclusao.getDepartamentosIncluidos().add(departamento);
                usuarioInclusao = em.merge(usuarioInclusao);
            }
            for (Meta metasMeta : departamento.getMetas()) {
                Departamento oldDepartamentoOfMetasMeta = metasMeta.getDepartamento();
                metasMeta.setDepartamento(departamento);
                metasMeta = em.merge(metasMeta);
                if (oldDepartamentoOfMetasMeta != null) {
                    oldDepartamentoOfMetasMeta.getMetas().remove(metasMeta);
                    oldDepartamentoOfMetasMeta = em.merge(oldDepartamentoOfMetasMeta);
                }
            }
            for (Tarefa tarefasTarefa : departamento.getTarefas()) {
                Departamento oldDepartamentoOfTarefasTarefa = tarefasTarefa.getDepartamento();
                tarefasTarefa.setDepartamento(departamento);
                tarefasTarefa = em.merge(tarefasTarefa);
                if (oldDepartamentoOfTarefasTarefa != null) {
                    oldDepartamentoOfTarefasTarefa.getTarefas().remove(tarefasTarefa);
                    oldDepartamentoOfTarefasTarefa = em.merge(oldDepartamentoOfTarefasTarefa);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Departamento departamento) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departamento persistentDepartamento = em.find(Departamento.class, departamento.getId());
            Empresa empresaOld = persistentDepartamento.getEmpresa();
            Empresa empresaNew = departamento.getEmpresa();
            Usuario usuarioInclusaoOld = persistentDepartamento.getUsuarioInclusao();
            Usuario usuarioInclusaoNew = departamento.getUsuarioInclusao();
            Collection<Meta> metasOld = persistentDepartamento.getMetas();
            Collection<Meta> metasNew = departamento.getMetas();
            List<Tarefa> tarefasOld = persistentDepartamento.getTarefas();
            List<Tarefa> tarefasNew = departamento.getTarefas();
            if (empresaNew != null) {
                empresaNew = em.getReference(empresaNew.getClass(), empresaNew.getId());
                departamento.setEmpresa(empresaNew);
            }
            if (usuarioInclusaoNew != null) {
                usuarioInclusaoNew = em.getReference(usuarioInclusaoNew.getClass(), usuarioInclusaoNew.getId());
                departamento.setUsuarioInclusao(usuarioInclusaoNew);
            }
            Collection<Meta> attachedMetasNew = new ArrayList<Meta>();
            for (Meta metasNewMetaToAttach : metasNew) {
                metasNewMetaToAttach = em.getReference(metasNewMetaToAttach.getClass(), metasNewMetaToAttach.getId());
                attachedMetasNew.add(metasNewMetaToAttach);
            }
            metasNew = attachedMetasNew;
            departamento.setMetas(metasNew);
            List<Tarefa> attachedTarefasNew = new ArrayList<Tarefa>();
            for (Tarefa tarefasNewTarefaToAttach : tarefasNew) {
                tarefasNewTarefaToAttach = em.getReference(tarefasNewTarefaToAttach.getClass(), tarefasNewTarefaToAttach.getId());
                attachedTarefasNew.add(tarefasNewTarefaToAttach);
            }
            tarefasNew = attachedTarefasNew;
            departamento.setTarefas(tarefasNew);
            departamento = em.merge(departamento);
            if (empresaOld != null && !empresaOld.equals(empresaNew)) {
                empresaOld.getDepartamentos().remove(departamento);
                empresaOld = em.merge(empresaOld);
            }
            if (empresaNew != null && !empresaNew.equals(empresaOld)) {
                empresaNew.getDepartamentos().add(departamento);
                empresaNew = em.merge(empresaNew);
            }
            if (usuarioInclusaoOld != null && !usuarioInclusaoOld.equals(usuarioInclusaoNew)) {
                usuarioInclusaoOld.getDepartamentosIncluidos().remove(departamento);
                usuarioInclusaoOld = em.merge(usuarioInclusaoOld);
            }
            if (usuarioInclusaoNew != null && !usuarioInclusaoNew.equals(usuarioInclusaoOld)) {
                usuarioInclusaoNew.getDepartamentosIncluidos().add(departamento);
                usuarioInclusaoNew = em.merge(usuarioInclusaoNew);
            }
            for (Meta metasOldMeta : metasOld) {
                if (!metasNew.contains(metasOldMeta)) {
                    metasOldMeta.setDepartamento(null);
                    metasOldMeta = em.merge(metasOldMeta);
                }
            }
            for (Meta metasNewMeta : metasNew) {
                if (!metasOld.contains(metasNewMeta)) {
                    Departamento oldDepartamentoOfMetasNewMeta = metasNewMeta.getDepartamento();
                    metasNewMeta.setDepartamento(departamento);
                    metasNewMeta = em.merge(metasNewMeta);
                    if (oldDepartamentoOfMetasNewMeta != null && !oldDepartamentoOfMetasNewMeta.equals(departamento)) {
                        oldDepartamentoOfMetasNewMeta.getMetas().remove(metasNewMeta);
                        oldDepartamentoOfMetasNewMeta = em.merge(oldDepartamentoOfMetasNewMeta);
                    }
                }
            }
            for (Tarefa tarefasOldTarefa : tarefasOld) {
                if (!tarefasNew.contains(tarefasOldTarefa)) {
                    tarefasOldTarefa.setDepartamento(null);
                    tarefasOldTarefa = em.merge(tarefasOldTarefa);
                }
            }
            for (Tarefa tarefasNewTarefa : tarefasNew) {
                if (!tarefasOld.contains(tarefasNewTarefa)) {
                    Departamento oldDepartamentoOfTarefasNewTarefa = tarefasNewTarefa.getDepartamento();
                    tarefasNewTarefa.setDepartamento(departamento);
                    tarefasNewTarefa = em.merge(tarefasNewTarefa);
                    if (oldDepartamentoOfTarefasNewTarefa != null && !oldDepartamentoOfTarefasNewTarefa.equals(departamento)) {
                        oldDepartamentoOfTarefasNewTarefa.getTarefas().remove(tarefasNewTarefa);
                        oldDepartamentoOfTarefasNewTarefa = em.merge(oldDepartamentoOfTarefasNewTarefa);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = departamento.getId();
                if (findDepartamento(id) == null) {
                    throw new NonexistentEntityException("The departamento with id " + id + " no longer exists.");
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
            Departamento departamento;
            try {
                departamento = em.getReference(Departamento.class, id);
                departamento.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The departamento with id " + id + " no longer exists.", enfe);
            }
            Empresa empresa = departamento.getEmpresa();
            if (empresa != null) {
                empresa.getDepartamentos().remove(departamento);
                empresa = em.merge(empresa);
            }
            Usuario usuarioInclusao = departamento.getUsuarioInclusao();
            if (usuarioInclusao != null) {
                usuarioInclusao.getDepartamentosIncluidos().remove(departamento);
                usuarioInclusao = em.merge(usuarioInclusao);
            }
            Collection<Meta> metas = departamento.getMetas();
            for (Meta metasMeta : metas) {
                metasMeta.setDepartamento(null);
                metasMeta = em.merge(metasMeta);
            }
            List<Tarefa> tarefas = departamento.getTarefas();
            for (Tarefa tarefasTarefa : tarefas) {
                tarefasTarefa.setDepartamento(null);
                tarefasTarefa = em.merge(tarefasTarefa);
            }
            em.remove(departamento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Departamento> findDepartamentoEntities() {
        return findDepartamentoEntities(true, -1, -1);
    }

    public List<Departamento> findDepartamentoEntities(int maxResults, int firstResult) {
        return findDepartamentoEntities(false, maxResults, firstResult);
    }

    private List<Departamento> findDepartamentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Departamento.class));
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

    public Departamento findDepartamento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Departamento.class, id);
        } finally {
            em.close();
        }
    }

    public int getDepartamentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Departamento> rt = cq.from(Departamento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    /**
     * Obtém a lista de departamentos cadastrados para a empresa
     * @param empresa
     * @return lista de departamentos
     */
    public List<Departamento> obterDepartamentosPorEmpresa(Empresa empresa) {
            EntityManager em = getEntityManager();

        try {
            return (List<Departamento>) em.createNamedQuery("Departamento.findByEmpresa")
                    .setParameter("empresa", empresa)
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<Departamento>();
        }

    }

    
}
