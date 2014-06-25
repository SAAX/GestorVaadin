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
import com.saax.gestorweb.model.datamodel.Cidade;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.Endereco;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author rodrigo
 */
public class EnderecoJpaController implements Serializable {

    public EnderecoJpaController(EntityManagerFactory emf) {
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
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cidade idcidade = endereco.getCidade();
            if (idcidade != null) {
                idcidade = em.getReference(idcidade.getClass(), idcidade.getIdCidade());
                endereco.setCidade(idcidade);
            }
            List<Empresa> attachedEmpresaList = new ArrayList<Empresa>();
            for (Empresa empresaListEmpresaToAttach : endereco.getEmpresaList()) {
                empresaListEmpresaToAttach = em.getReference(empresaListEmpresaToAttach.getClass(), empresaListEmpresaToAttach.getId());
                attachedEmpresaList.add(empresaListEmpresaToAttach);
            }
            endereco.setEmpresaList(attachedEmpresaList);
            em.persist(endereco);
            if (idcidade != null) {
                idcidade.getEnderecoList().add(endereco);
                idcidade = em.merge(idcidade);
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
            Endereco persistentEndereco = em.find(Endereco.class, endereco.getIdEndereco());
            Cidade idcidadeOld = persistentEndereco.getCidade();
            Cidade idcidadeNew = endereco.getCidade();
            List<Empresa> empresaListOld = persistentEndereco.getEmpresaList();
            List<Empresa> empresaListNew = endereco.getEmpresaList();
            if (idcidadeNew != null) {
                idcidadeNew = em.getReference(idcidadeNew.getClass(), idcidadeNew.getIdCidade());
                endereco.setCidade(idcidadeNew);
            }
            List<Empresa> attachedEmpresaListNew = new ArrayList<Empresa>();
            for (Empresa empresaListNewEmpresaToAttach : empresaListNew) {
                empresaListNewEmpresaToAttach = em.getReference(empresaListNewEmpresaToAttach.getClass(), empresaListNewEmpresaToAttach.getId());
                attachedEmpresaListNew.add(empresaListNewEmpresaToAttach);
            }
            empresaListNew = attachedEmpresaListNew;
            endereco.setEmpresaList(empresaListNew);
            endereco = em.merge(endereco);
            if (idcidadeOld != null && !idcidadeOld.equals(idcidadeNew)) {
                idcidadeOld.getEnderecoList().remove(endereco);
                idcidadeOld = em.merge(idcidadeOld);
            }
            if (idcidadeNew != null && !idcidadeNew.equals(idcidadeOld)) {
                idcidadeNew.getEnderecoList().add(endereco);
                idcidadeNew = em.merge(idcidadeNew);
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
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = endereco.getIdEndereco();
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
                endereco.getIdEndereco();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The endereco with id " + id + " no longer exists.", enfe);
            }
            Cidade idcidade = endereco.getCidade();
            if (idcidade != null) {
                idcidade.getEnderecoList().remove(endereco);
                idcidade = em.merge(idcidade);
            }
            List<Empresa> empresaList = endereco.getEmpresaList();
            for (Empresa empresaListEmpresa : empresaList) {
                empresaListEmpresa.setEndereco(null);
                empresaListEmpresa = em.merge(empresaListEmpresa);
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
