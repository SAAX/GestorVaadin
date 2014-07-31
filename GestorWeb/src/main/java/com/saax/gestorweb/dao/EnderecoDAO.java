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
        if (endereco.getEmpresas() == null) {
            endereco.setEmpresas(new ArrayList<Empresa>());
        }
        if (endereco.getEmpresasCliente() == null) {
            endereco.setEmpresasCliente(new ArrayList<EmpresaCliente>());
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
            Usuario usuarioInclusao = endereco.getUsuarioInclusao();
            if (usuarioInclusao != null) {
                usuarioInclusao = em.getReference(usuarioInclusao.getClass(), usuarioInclusao.getId());
                endereco.setUsuarioInclusao(usuarioInclusao);
            }
            List<Empresa> attachedEmpresas = new ArrayList<Empresa>();
            for (Empresa empresasEmpresaToAttach : endereco.getEmpresas()) {
                empresasEmpresaToAttach = em.getReference(empresasEmpresaToAttach.getClass(), empresasEmpresaToAttach.getId());
                attachedEmpresas.add(empresasEmpresaToAttach);
            }
            endereco.setEmpresas(attachedEmpresas);
            List<EmpresaCliente> attachedEmpresasCliente = new ArrayList<EmpresaCliente>();
            for (EmpresaCliente empresasClienteEmpresaClienteToAttach : endereco.getEmpresasCliente()) {
                empresasClienteEmpresaClienteToAttach = em.getReference(empresasClienteEmpresaClienteToAttach.getClass(), empresasClienteEmpresaClienteToAttach.getId());
                attachedEmpresasCliente.add(empresasClienteEmpresaClienteToAttach);
            }
            endereco.setEmpresasCliente(attachedEmpresasCliente);
            em.persist(endereco);
            if (cidade != null) {
                cidade.getEnderecos().add(endereco);
                cidade = em.merge(cidade);
            }
            if (usuarioInclusao != null) {
                usuarioInclusao.getEnderecosIncluidos().add(endereco);
                usuarioInclusao = em.merge(usuarioInclusao);
            }
            for (Empresa empresasEmpresa : endereco.getEmpresas()) {
                Endereco oldEnderecoOfEmpresasEmpresa = empresasEmpresa.getEndereco();
                empresasEmpresa.setEndereco(endereco);
                empresasEmpresa = em.merge(empresasEmpresa);
                if (oldEnderecoOfEmpresasEmpresa != null) {
                    oldEnderecoOfEmpresasEmpresa.getEmpresas().remove(empresasEmpresa);
                    oldEnderecoOfEmpresasEmpresa = em.merge(oldEnderecoOfEmpresasEmpresa);
                }
            }
            for (EmpresaCliente empresasClienteEmpresaCliente : endereco.getEmpresasCliente()) {
                Endereco oldEnderecoOfEmpresasClienteEmpresaCliente = empresasClienteEmpresaCliente.getEndereco();
                empresasClienteEmpresaCliente.setEndereco(endereco);
                empresasClienteEmpresaCliente = em.merge(empresasClienteEmpresaCliente);
                if (oldEnderecoOfEmpresasClienteEmpresaCliente != null) {
                    oldEnderecoOfEmpresasClienteEmpresaCliente.getEmpresasCliente().remove(empresasClienteEmpresaCliente);
                    oldEnderecoOfEmpresasClienteEmpresaCliente = em.merge(oldEnderecoOfEmpresasClienteEmpresaCliente);
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
            Usuario usuarioInclusaoOld = persistentEndereco.getUsuarioInclusao();
            Usuario usuarioInclusaoNew = endereco.getUsuarioInclusao();
            List<Empresa> empresasOld = persistentEndereco.getEmpresas();
            List<Empresa> empresasNew = endereco.getEmpresas();
            List<EmpresaCliente> empresasClienteOld = persistentEndereco.getEmpresasCliente();
            List<EmpresaCliente> empresasClienteNew = endereco.getEmpresasCliente();
            if (cidadeNew != null) {
                cidadeNew = em.getReference(cidadeNew.getClass(), cidadeNew.getId());
                endereco.setCidade(cidadeNew);
            }
            if (usuarioInclusaoNew != null) {
                usuarioInclusaoNew = em.getReference(usuarioInclusaoNew.getClass(), usuarioInclusaoNew.getId());
                endereco.setUsuarioInclusao(usuarioInclusaoNew);
            }
            List<Empresa> attachedEmpresasNew = new ArrayList<Empresa>();
            for (Empresa empresasNewEmpresaToAttach : empresasNew) {
                empresasNewEmpresaToAttach = em.getReference(empresasNewEmpresaToAttach.getClass(), empresasNewEmpresaToAttach.getId());
                attachedEmpresasNew.add(empresasNewEmpresaToAttach);
            }
            empresasNew = attachedEmpresasNew;
            endereco.setEmpresas(empresasNew);
            List<EmpresaCliente> attachedEmpresasClienteNew = new ArrayList<EmpresaCliente>();
            for (EmpresaCliente empresasClienteNewEmpresaClienteToAttach : empresasClienteNew) {
                empresasClienteNewEmpresaClienteToAttach = em.getReference(empresasClienteNewEmpresaClienteToAttach.getClass(), empresasClienteNewEmpresaClienteToAttach.getId());
                attachedEmpresasClienteNew.add(empresasClienteNewEmpresaClienteToAttach);
            }
            empresasClienteNew = attachedEmpresasClienteNew;
            endereco.setEmpresasCliente(empresasClienteNew);
            endereco = em.merge(endereco);
            if (cidadeOld != null && !cidadeOld.equals(cidadeNew)) {
                cidadeOld.getEnderecos().remove(endereco);
                cidadeOld = em.merge(cidadeOld);
            }
            if (cidadeNew != null && !cidadeNew.equals(cidadeOld)) {
                cidadeNew.getEnderecos().add(endereco);
                cidadeNew = em.merge(cidadeNew);
            }
            if (usuarioInclusaoOld != null && !usuarioInclusaoOld.equals(usuarioInclusaoNew)) {
                usuarioInclusaoOld.getEnderecosIncluidos().remove(endereco);
                usuarioInclusaoOld = em.merge(usuarioInclusaoOld);
            }
            if (usuarioInclusaoNew != null && !usuarioInclusaoNew.equals(usuarioInclusaoOld)) {
                usuarioInclusaoNew.getEnderecosIncluidos().add(endereco);
                usuarioInclusaoNew = em.merge(usuarioInclusaoNew);
            }
            for (Empresa empresasOldEmpresa : empresasOld) {
                if (!empresasNew.contains(empresasOldEmpresa)) {
                    empresasOldEmpresa.setEndereco(null);
                    empresasOldEmpresa = em.merge(empresasOldEmpresa);
                }
            }
            for (Empresa empresasNewEmpresa : empresasNew) {
                if (!empresasOld.contains(empresasNewEmpresa)) {
                    Endereco oldEnderecoOfEmpresasNewEmpresa = empresasNewEmpresa.getEndereco();
                    empresasNewEmpresa.setEndereco(endereco);
                    empresasNewEmpresa = em.merge(empresasNewEmpresa);
                    if (oldEnderecoOfEmpresasNewEmpresa != null && !oldEnderecoOfEmpresasNewEmpresa.equals(endereco)) {
                        oldEnderecoOfEmpresasNewEmpresa.getEmpresas().remove(empresasNewEmpresa);
                        oldEnderecoOfEmpresasNewEmpresa = em.merge(oldEnderecoOfEmpresasNewEmpresa);
                    }
                }
            }
            for (EmpresaCliente empresasClienteOldEmpresaCliente : empresasClienteOld) {
                if (!empresasClienteNew.contains(empresasClienteOldEmpresaCliente)) {
                    empresasClienteOldEmpresaCliente.setEndereco(null);
                    empresasClienteOldEmpresaCliente = em.merge(empresasClienteOldEmpresaCliente);
                }
            }
            for (EmpresaCliente empresasClienteNewEmpresaCliente : empresasClienteNew) {
                if (!empresasClienteOld.contains(empresasClienteNewEmpresaCliente)) {
                    Endereco oldEnderecoOfEmpresasClienteNewEmpresaCliente = empresasClienteNewEmpresaCliente.getEndereco();
                    empresasClienteNewEmpresaCliente.setEndereco(endereco);
                    empresasClienteNewEmpresaCliente = em.merge(empresasClienteNewEmpresaCliente);
                    if (oldEnderecoOfEmpresasClienteNewEmpresaCliente != null && !oldEnderecoOfEmpresasClienteNewEmpresaCliente.equals(endereco)) {
                        oldEnderecoOfEmpresasClienteNewEmpresaCliente.getEmpresasCliente().remove(empresasClienteNewEmpresaCliente);
                        oldEnderecoOfEmpresasClienteNewEmpresaCliente = em.merge(oldEnderecoOfEmpresasClienteNewEmpresaCliente);
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
                cidade.getEnderecos().remove(endereco);
                cidade = em.merge(cidade);
            }
            Usuario usuarioInclusao = endereco.getUsuarioInclusao();
            if (usuarioInclusao != null) {
                usuarioInclusao.getEnderecosIncluidos().remove(endereco);
                usuarioInclusao = em.merge(usuarioInclusao);
            }
            List<Empresa> empresas = endereco.getEmpresas();
            for (Empresa empresasEmpresa : empresas) {
                empresasEmpresa.setEndereco(null);
                empresasEmpresa = em.merge(empresasEmpresa);
            }
            List<EmpresaCliente> empresasCliente = endereco.getEmpresasCliente();
            for (EmpresaCliente empresasClienteEmpresaCliente : empresasCliente) {
                empresasClienteEmpresaCliente.setEndereco(null);
                empresasClienteEmpresaCliente = em.merge(empresasClienteEmpresaCliente);
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
