package com.saax.gestorweb.dao;

import com.saax.gestorweb.dao.exceptions.IllegalOrphanException;
import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.Endereco;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.model.datamodel.FilialCliente;
import java.util.ArrayList;
import java.util.List;
import com.saax.gestorweb.model.datamodel.Tarefa;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * DAO para o entity bean: EmpresaCliente <br><br>
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
        if (empresaCliente.getFiliais() == null) {
            empresaCliente.setFiliais(new ArrayList<FilialCliente>());
        }
        if (empresaCliente.getTarefaList() == null) {
            empresaCliente.setTarefaList(new ArrayList<Tarefa>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa empresa = empresaCliente.getEmpresa();
            if (empresa != null) {
                empresa = em.getReference(empresa.getClass(), empresa.getId());
                empresaCliente.setEmpresa(empresa);
            }
            Endereco endereco = empresaCliente.getEndereco();
            if (endereco != null) {
                endereco = em.getReference(endereco.getClass(), endereco.getId());
                empresaCliente.setEndereco(endereco);
            }
            Usuario idusuarioinclusao = empresaCliente.getIdusuarioinclusao();
            if (idusuarioinclusao != null) {
                idusuarioinclusao = em.getReference(idusuarioinclusao.getClass(), idusuarioinclusao.getId());
                empresaCliente.setIdusuarioinclusao(idusuarioinclusao);
            }
            List<FilialCliente> attachedFiliais = new ArrayList<FilialCliente>();
            for (FilialCliente filiaisFilialClienteToAttach : empresaCliente.getFiliais()) {
                filiaisFilialClienteToAttach = em.getReference(filiaisFilialClienteToAttach.getClass(), filiaisFilialClienteToAttach.getId());
                attachedFiliais.add(filiaisFilialClienteToAttach);
            }
            empresaCliente.setFiliais(attachedFiliais);
            List<Tarefa> attachedTarefaList = new ArrayList<Tarefa>();
            for (Tarefa tarefaListTarefaToAttach : empresaCliente.getTarefaList()) {
                tarefaListTarefaToAttach = em.getReference(tarefaListTarefaToAttach.getClass(), tarefaListTarefaToAttach.getId());
                attachedTarefaList.add(tarefaListTarefaToAttach);
            }
            empresaCliente.setTarefaList(attachedTarefaList);
            em.persist(empresaCliente);
            if (empresa != null) {
                empresa.getEmpresaClienteList().add(empresaCliente);
                empresa = em.merge(empresa);
            }
            if (endereco != null) {
                endereco.getEmpresaClienteList().add(empresaCliente);
                endereco = em.merge(endereco);
            }
            if (idusuarioinclusao != null) {
                idusuarioinclusao.getEmpresaClienteList().add(empresaCliente);
                idusuarioinclusao = em.merge(idusuarioinclusao);
            }
            for (FilialCliente filiaisFilialCliente : empresaCliente.getFiliais()) {
                EmpresaCliente oldEmpresaClienteOfFiliaisFilialCliente = filiaisFilialCliente.getEmpresaCliente();
                filiaisFilialCliente.setEmpresaCliente(empresaCliente);
                filiaisFilialCliente = em.merge(filiaisFilialCliente);
                if (oldEmpresaClienteOfFiliaisFilialCliente != null) {
                    oldEmpresaClienteOfFiliaisFilialCliente.getFiliais().remove(filiaisFilialCliente);
                    oldEmpresaClienteOfFiliaisFilialCliente = em.merge(oldEmpresaClienteOfFiliaisFilialCliente);
                }
            }
            for (Tarefa tarefaListTarefa : empresaCliente.getTarefaList()) {
                EmpresaCliente oldIdempresaclienteOfTarefaListTarefa = tarefaListTarefa.getIdempresacliente();
                tarefaListTarefa.setIdempresacliente(empresaCliente);
                tarefaListTarefa = em.merge(tarefaListTarefa);
                if (oldIdempresaclienteOfTarefaListTarefa != null) {
                    oldIdempresaclienteOfTarefaListTarefa.getTarefaList().remove(tarefaListTarefa);
                    oldIdempresaclienteOfTarefaListTarefa = em.merge(oldIdempresaclienteOfTarefaListTarefa);
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
            Empresa empresaOld = persistentEmpresaCliente.getEmpresa();
            Empresa empresaNew = empresaCliente.getEmpresa();
            Endereco enderecoOld = persistentEmpresaCliente.getEndereco();
            Endereco enderecoNew = empresaCliente.getEndereco();
            Usuario idusuarioinclusaoOld = persistentEmpresaCliente.getIdusuarioinclusao();
            Usuario idusuarioinclusaoNew = empresaCliente.getIdusuarioinclusao();
            List<FilialCliente> filiaisOld = persistentEmpresaCliente.getFiliais();
            List<FilialCliente> filiaisNew = empresaCliente.getFiliais();
            List<Tarefa> tarefaListOld = persistentEmpresaCliente.getTarefaList();
            List<Tarefa> tarefaListNew = empresaCliente.getTarefaList();
            List<String> illegalOrphanMessages = null;
            for (FilialCliente filiaisOldFilialCliente : filiaisOld) {
                if (!filiaisNew.contains(filiaisOldFilialCliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain FilialCliente " + filiaisOldFilialCliente + " since its empresaCliente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (empresaNew != null) {
                empresaNew = em.getReference(empresaNew.getClass(), empresaNew.getId());
                empresaCliente.setEmpresa(empresaNew);
            }
            if (enderecoNew != null) {
                enderecoNew = em.getReference(enderecoNew.getClass(), enderecoNew.getId());
                empresaCliente.setEndereco(enderecoNew);
            }
            if (idusuarioinclusaoNew != null) {
                idusuarioinclusaoNew = em.getReference(idusuarioinclusaoNew.getClass(), idusuarioinclusaoNew.getId());
                empresaCliente.setIdusuarioinclusao(idusuarioinclusaoNew);
            }
            List<FilialCliente> attachedFiliaisNew = new ArrayList<FilialCliente>();
            for (FilialCliente filiaisNewFilialClienteToAttach : filiaisNew) {
                filiaisNewFilialClienteToAttach = em.getReference(filiaisNewFilialClienteToAttach.getClass(), filiaisNewFilialClienteToAttach.getId());
                attachedFiliaisNew.add(filiaisNewFilialClienteToAttach);
            }
            filiaisNew = attachedFiliaisNew;
            empresaCliente.setFiliais(filiaisNew);
            List<Tarefa> attachedTarefaListNew = new ArrayList<Tarefa>();
            for (Tarefa tarefaListNewTarefaToAttach : tarefaListNew) {
                tarefaListNewTarefaToAttach = em.getReference(tarefaListNewTarefaToAttach.getClass(), tarefaListNewTarefaToAttach.getId());
                attachedTarefaListNew.add(tarefaListNewTarefaToAttach);
            }
            tarefaListNew = attachedTarefaListNew;
            empresaCliente.setTarefaList(tarefaListNew);
            empresaCliente = em.merge(empresaCliente);
            if (empresaOld != null && !empresaOld.equals(empresaNew)) {
                empresaOld.getEmpresaClienteList().remove(empresaCliente);
                empresaOld = em.merge(empresaOld);
            }
            if (empresaNew != null && !empresaNew.equals(empresaOld)) {
                empresaNew.getEmpresaClienteList().add(empresaCliente);
                empresaNew = em.merge(empresaNew);
            }
            if (enderecoOld != null && !enderecoOld.equals(enderecoNew)) {
                enderecoOld.getEmpresaClienteList().remove(empresaCliente);
                enderecoOld = em.merge(enderecoOld);
            }
            if (enderecoNew != null && !enderecoNew.equals(enderecoOld)) {
                enderecoNew.getEmpresaClienteList().add(empresaCliente);
                enderecoNew = em.merge(enderecoNew);
            }
            if (idusuarioinclusaoOld != null && !idusuarioinclusaoOld.equals(idusuarioinclusaoNew)) {
                idusuarioinclusaoOld.getEmpresaClienteList().remove(empresaCliente);
                idusuarioinclusaoOld = em.merge(idusuarioinclusaoOld);
            }
            if (idusuarioinclusaoNew != null && !idusuarioinclusaoNew.equals(idusuarioinclusaoOld)) {
                idusuarioinclusaoNew.getEmpresaClienteList().add(empresaCliente);
                idusuarioinclusaoNew = em.merge(idusuarioinclusaoNew);
            }
            for (FilialCliente filiaisNewFilialCliente : filiaisNew) {
                if (!filiaisOld.contains(filiaisNewFilialCliente)) {
                    EmpresaCliente oldEmpresaClienteOfFiliaisNewFilialCliente = filiaisNewFilialCliente.getEmpresaCliente();
                    filiaisNewFilialCliente.setEmpresaCliente(empresaCliente);
                    filiaisNewFilialCliente = em.merge(filiaisNewFilialCliente);
                    if (oldEmpresaClienteOfFiliaisNewFilialCliente != null && !oldEmpresaClienteOfFiliaisNewFilialCliente.equals(empresaCliente)) {
                        oldEmpresaClienteOfFiliaisNewFilialCliente.getFiliais().remove(filiaisNewFilialCliente);
                        oldEmpresaClienteOfFiliaisNewFilialCliente = em.merge(oldEmpresaClienteOfFiliaisNewFilialCliente);
                    }
                }
            }
            for (Tarefa tarefaListOldTarefa : tarefaListOld) {
                if (!tarefaListNew.contains(tarefaListOldTarefa)) {
                    tarefaListOldTarefa.setIdempresacliente(null);
                    tarefaListOldTarefa = em.merge(tarefaListOldTarefa);
                }
            }
            for (Tarefa tarefaListNewTarefa : tarefaListNew) {
                if (!tarefaListOld.contains(tarefaListNewTarefa)) {
                    EmpresaCliente oldIdempresaclienteOfTarefaListNewTarefa = tarefaListNewTarefa.getIdempresacliente();
                    tarefaListNewTarefa.setIdempresacliente(empresaCliente);
                    tarefaListNewTarefa = em.merge(tarefaListNewTarefa);
                    if (oldIdempresaclienteOfTarefaListNewTarefa != null && !oldIdempresaclienteOfTarefaListNewTarefa.equals(empresaCliente)) {
                        oldIdempresaclienteOfTarefaListNewTarefa.getTarefaList().remove(tarefaListNewTarefa);
                        oldIdempresaclienteOfTarefaListNewTarefa = em.merge(oldIdempresaclienteOfTarefaListNewTarefa);
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
            List<FilialCliente> filiaisOrphanCheck = empresaCliente.getFiliais();
            for (FilialCliente filiaisOrphanCheckFilialCliente : filiaisOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EmpresaCliente (" + empresaCliente + ") cannot be destroyed since the FilialCliente " + filiaisOrphanCheckFilialCliente + " in its filiais field has a non-nullable empresaCliente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Empresa empresa = empresaCliente.getEmpresa();
            if (empresa != null) {
                empresa.getEmpresaClienteList().remove(empresaCliente);
                empresa = em.merge(empresa);
            }
            Endereco endereco = empresaCliente.getEndereco();
            if (endereco != null) {
                endereco.getEmpresaClienteList().remove(empresaCliente);
                endereco = em.merge(endereco);
            }
            Usuario idusuarioinclusao = empresaCliente.getIdusuarioinclusao();
            if (idusuarioinclusao != null) {
                idusuarioinclusao.getEmpresaClienteList().remove(empresaCliente);
                idusuarioinclusao = em.merge(idusuarioinclusao);
            }
            List<Tarefa> tarefaList = empresaCliente.getTarefaList();
            for (Tarefa tarefaListTarefa : tarefaList) {
                tarefaListTarefa.setIdempresacliente(null);
                tarefaListTarefa = em.merge(tarefaListTarefa);
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
