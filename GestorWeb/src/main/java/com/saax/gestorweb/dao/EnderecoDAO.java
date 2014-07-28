package com.saax.gestorweb.dao;

import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.saax.gestorweb.model.datamodel.Cidade;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.model.datamodel.Empresa;
import java.util.ArrayList;
import java.util.List;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.Endereco;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * DAO para o entity bean: Endereco <br><br>
 * 
 * @author rodrigo
 */
public class EnderecoDAO implements Serializable {

    public EnderecoDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Endereco endereco) {
        if (endereco.getEmpresaList() == null) {
            endereco.setEmpresaList(new ArrayList<Empresa>());
        }
        if (endereco.getEmpresaClienteList() == null) {
            endereco.setEmpresaClienteList(new ArrayList<EmpresaCliente>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cidade cidade = endereco.getCidade();
            if (cidade != null) {
                cidade = em.getReference(cidade.getClass(), cidade.getId());
                endereco.setCidade(cidade);
            }
            Usuario idusuarioinclusao = endereco.getIdusuarioinclusao();
            if (idusuarioinclusao != null) {
                idusuarioinclusao = em.getReference(idusuarioinclusao.getClass(), idusuarioinclusao.getId());
                endereco.setIdusuarioinclusao(idusuarioinclusao);
            }
            List<Empresa> attachedEmpresaList = new ArrayList<Empresa>();
            for (Empresa empresaListEmpresaToAttach : endereco.getEmpresaList()) {
                empresaListEmpresaToAttach = em.getReference(empresaListEmpresaToAttach.getClass(), empresaListEmpresaToAttach.getId());
                attachedEmpresaList.add(empresaListEmpresaToAttach);
            }
            endereco.setEmpresaList(attachedEmpresaList);
            List<EmpresaCliente> attachedEmpresaClienteList = new ArrayList<EmpresaCliente>();
            for (EmpresaCliente empresaClienteListEmpresaClienteToAttach : endereco.getEmpresaClienteList()) {
                empresaClienteListEmpresaClienteToAttach = em.getReference(empresaClienteListEmpresaClienteToAttach.getClass(), empresaClienteListEmpresaClienteToAttach.getId());
                attachedEmpresaClienteList.add(empresaClienteListEmpresaClienteToAttach);
            }
            endereco.setEmpresaClienteList(attachedEmpresaClienteList);
            em.persist(endereco);
            if (cidade != null) {
                cidade.getEnderecoList().add(endereco);
                cidade = em.merge(cidade);
            }
            if (idusuarioinclusao != null) {
                idusuarioinclusao.getEnderecoList().add(endereco);
                idusuarioinclusao = em.merge(idusuarioinclusao);
            }
            for (Empresa empresaListEmpresa : endereco.getEmpresaList()) {
                Endereco oldEnderecoOfEmpresaListEmpresa = empresaListEmpresa.getEndereco();
                empresaListEmpresa.setEndereco(endereco);
                empresaListEmpresa = em.merge(empresaListEmpresa);
                if (oldEnderecoOfEmpresaListEmpresa != null) {
                    oldEnderecoOfEmpresaListEmpresa.getEmpresaList().remove(empresaListEmpresa);
                    oldEnderecoOfEmpresaListEmpresa = em.merge(oldEnderecoOfEmpresaListEmpresa);
                }
            }
            for (EmpresaCliente empresaClienteListEmpresaCliente : endereco.getEmpresaClienteList()) {
                Endereco oldEnderecoOfEmpresaClienteListEmpresaCliente = empresaClienteListEmpresaCliente.getEndereco();
                empresaClienteListEmpresaCliente.setEndereco(endereco);
                empresaClienteListEmpresaCliente = em.merge(empresaClienteListEmpresaCliente);
                if (oldEnderecoOfEmpresaClienteListEmpresaCliente != null) {
                    oldEnderecoOfEmpresaClienteListEmpresaCliente.getEmpresaClienteList().remove(empresaClienteListEmpresaCliente);
                    oldEnderecoOfEmpresaClienteListEmpresaCliente = em.merge(oldEnderecoOfEmpresaClienteListEmpresaCliente);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Endereco endereco) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Endereco persistentEndereco = em.find(Endereco.class, endereco.getId());
            Cidade cidadeOld = persistentEndereco.getCidade();
            Cidade cidadeNew = endereco.getCidade();
            Usuario idusuarioinclusaoOld = persistentEndereco.getIdusuarioinclusao();
            Usuario idusuarioinclusaoNew = endereco.getIdusuarioinclusao();
            List<Empresa> empresaListOld = persistentEndereco.getEmpresaList();
            List<Empresa> empresaListNew = endereco.getEmpresaList();
            List<EmpresaCliente> empresaClienteListOld = persistentEndereco.getEmpresaClienteList();
            List<EmpresaCliente> empresaClienteListNew = endereco.getEmpresaClienteList();
            if (cidadeNew != null) {
                cidadeNew = em.getReference(cidadeNew.getClass(), cidadeNew.getId());
                endereco.setCidade(cidadeNew);
            }
            if (idusuarioinclusaoNew != null) {
                idusuarioinclusaoNew = em.getReference(idusuarioinclusaoNew.getClass(), idusuarioinclusaoNew.getId());
                endereco.setIdusuarioinclusao(idusuarioinclusaoNew);
            }
            List<Empresa> attachedEmpresaListNew = new ArrayList<Empresa>();
            for (Empresa empresaListNewEmpresaToAttach : empresaListNew) {
                empresaListNewEmpresaToAttach = em.getReference(empresaListNewEmpresaToAttach.getClass(), empresaListNewEmpresaToAttach.getId());
                attachedEmpresaListNew.add(empresaListNewEmpresaToAttach);
            }
            empresaListNew = attachedEmpresaListNew;
            endereco.setEmpresaList(empresaListNew);
            List<EmpresaCliente> attachedEmpresaClienteListNew = new ArrayList<EmpresaCliente>();
            for (EmpresaCliente empresaClienteListNewEmpresaClienteToAttach : empresaClienteListNew) {
                empresaClienteListNewEmpresaClienteToAttach = em.getReference(empresaClienteListNewEmpresaClienteToAttach.getClass(), empresaClienteListNewEmpresaClienteToAttach.getId());
                attachedEmpresaClienteListNew.add(empresaClienteListNewEmpresaClienteToAttach);
            }
            empresaClienteListNew = attachedEmpresaClienteListNew;
            endereco.setEmpresaClienteList(empresaClienteListNew);
            endereco = em.merge(endereco);
            if (cidadeOld != null && !cidadeOld.equals(cidadeNew)) {
                cidadeOld.getEnderecoList().remove(endereco);
                cidadeOld = em.merge(cidadeOld);
            }
            if (cidadeNew != null && !cidadeNew.equals(cidadeOld)) {
                cidadeNew.getEnderecoList().add(endereco);
                cidadeNew = em.merge(cidadeNew);
            }
            if (idusuarioinclusaoOld != null && !idusuarioinclusaoOld.equals(idusuarioinclusaoNew)) {
                idusuarioinclusaoOld.getEnderecoList().remove(endereco);
                idusuarioinclusaoOld = em.merge(idusuarioinclusaoOld);
            }
            if (idusuarioinclusaoNew != null && !idusuarioinclusaoNew.equals(idusuarioinclusaoOld)) {
                idusuarioinclusaoNew.getEnderecoList().add(endereco);
                idusuarioinclusaoNew = em.merge(idusuarioinclusaoNew);
            }
            for (Empresa empresaListOldEmpresa : empresaListOld) {
                if (!empresaListNew.contains(empresaListOldEmpresa)) {
                    empresaListOldEmpresa.setEndereco(null);
                    empresaListOldEmpresa = em.merge(empresaListOldEmpresa);
                }
            }
            for (Empresa empresaListNewEmpresa : empresaListNew) {
                if (!empresaListOld.contains(empresaListNewEmpresa)) {
                    Endereco oldEnderecoOfEmpresaListNewEmpresa = empresaListNewEmpresa.getEndereco();
                    empresaListNewEmpresa.setEndereco(endereco);
                    empresaListNewEmpresa = em.merge(empresaListNewEmpresa);
                    if (oldEnderecoOfEmpresaListNewEmpresa != null && !oldEnderecoOfEmpresaListNewEmpresa.equals(endereco)) {
                        oldEnderecoOfEmpresaListNewEmpresa.getEmpresaList().remove(empresaListNewEmpresa);
                        oldEnderecoOfEmpresaListNewEmpresa = em.merge(oldEnderecoOfEmpresaListNewEmpresa);
                    }
                }
            }
            for (EmpresaCliente empresaClienteListOldEmpresaCliente : empresaClienteListOld) {
                if (!empresaClienteListNew.contains(empresaClienteListOldEmpresaCliente)) {
                    empresaClienteListOldEmpresaCliente.setEndereco(null);
                    empresaClienteListOldEmpresaCliente = em.merge(empresaClienteListOldEmpresaCliente);
                }
            }
            for (EmpresaCliente empresaClienteListNewEmpresaCliente : empresaClienteListNew) {
                if (!empresaClienteListOld.contains(empresaClienteListNewEmpresaCliente)) {
                    Endereco oldEnderecoOfEmpresaClienteListNewEmpresaCliente = empresaClienteListNewEmpresaCliente.getEndereco();
                    empresaClienteListNewEmpresaCliente.setEndereco(endereco);
                    empresaClienteListNewEmpresaCliente = em.merge(empresaClienteListNewEmpresaCliente);
                    if (oldEnderecoOfEmpresaClienteListNewEmpresaCliente != null && !oldEnderecoOfEmpresaClienteListNewEmpresaCliente.equals(endereco)) {
                        oldEnderecoOfEmpresaClienteListNewEmpresaCliente.getEmpresaClienteList().remove(empresaClienteListNewEmpresaCliente);
                        oldEnderecoOfEmpresaClienteListNewEmpresaCliente = em.merge(oldEnderecoOfEmpresaClienteListNewEmpresaCliente);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = endereco.getId();
                if (findEndereco(id) == null) {
                    throw new NonexistentEntityException("The endereco with id " + id + " no longer exists.");
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
            Endereco endereco;
            try {
                endereco = em.getReference(Endereco.class, id);
                endereco.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The endereco with id " + id + " no longer exists.", enfe);
            }
            Cidade cidade = endereco.getCidade();
            if (cidade != null) {
                cidade.getEnderecoList().remove(endereco);
                cidade = em.merge(cidade);
            }
            Usuario idusuarioinclusao = endereco.getIdusuarioinclusao();
            if (idusuarioinclusao != null) {
                idusuarioinclusao.getEnderecoList().remove(endereco);
                idusuarioinclusao = em.merge(idusuarioinclusao);
            }
            List<Empresa> empresaList = endereco.getEmpresaList();
            for (Empresa empresaListEmpresa : empresaList) {
                empresaListEmpresa.setEndereco(null);
                empresaListEmpresa = em.merge(empresaListEmpresa);
            }
            List<EmpresaCliente> empresaClienteList = endereco.getEmpresaClienteList();
            for (EmpresaCliente empresaClienteListEmpresaCliente : empresaClienteList) {
                empresaClienteListEmpresaCliente.setEndereco(null);
                empresaClienteListEmpresaCliente = em.merge(empresaClienteListEmpresaCliente);
            }
            em.remove(endereco);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Endereco> findEnderecoEntities() {
        return findEnderecoEntities(true, -1, -1);
    }

    public List<Endereco> findEnderecoEntities(int maxResults, int firstResult) {
        return findEnderecoEntities(false, maxResults, firstResult);
    }

    private List<Endereco> findEnderecoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Endereco.class));
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

    public Endereco findEndereco(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Endereco.class, id);
        } finally {
            em.close();
        }
    }

    public int getEnderecoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Endereco> rt = cq.from(Endereco.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
