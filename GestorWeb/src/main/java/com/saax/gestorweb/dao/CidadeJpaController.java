/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.dao;

import com.saax.gestorweb.dao.exceptions.IllegalOrphanException;
import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import com.saax.gestorweb.model.datamodel.Cidade;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.saax.gestorweb.model.datamodel.Estado;
import com.saax.gestorweb.model.datamodel.Endereco;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author rodrigo
 */
public class CidadeJpaController implements Serializable {

    public CidadeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cidade cidade) {
        if (cidade.getEnderecoList() == null) {
            cidade.setEnderecoList(new ArrayList<Endereco>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estado idestado = cidade.getIdestado();
            if (idestado != null) {
                idestado = em.getReference(idestado.getClass(), idestado.getIdestado());
                cidade.setIdestado(idestado);
            }
            List<Endereco> attachedEnderecoList = new ArrayList<Endereco>();
            for (Endereco enderecoListEnderecoToAttach : cidade.getEnderecoList()) {
                enderecoListEnderecoToAttach = em.getReference(enderecoListEnderecoToAttach.getClass(), enderecoListEnderecoToAttach.getIdendereco());
                attachedEnderecoList.add(enderecoListEnderecoToAttach);
            }
            cidade.setEnderecoList(attachedEnderecoList);
            em.persist(cidade);
            if (idestado != null) {
                idestado.getCidadeList().add(cidade);
                idestado = em.merge(idestado);
            }
            for (Endereco enderecoListEndereco : cidade.getEnderecoList()) {
                Cidade oldIdcidadeOfEnderecoListEndereco = enderecoListEndereco.getIdcidade();
                enderecoListEndereco.setIdcidade(cidade);
                enderecoListEndereco = em.merge(enderecoListEndereco);
                if (oldIdcidadeOfEnderecoListEndereco != null) {
                    oldIdcidadeOfEnderecoListEndereco.getEnderecoList().remove(enderecoListEndereco);
                    oldIdcidadeOfEnderecoListEndereco = em.merge(oldIdcidadeOfEnderecoListEndereco);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cidade cidade) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cidade persistentCidade = em.find(Cidade.class, cidade.getIdcidade());
            Estado idestadoOld = persistentCidade.getIdestado();
            Estado idestadoNew = cidade.getIdestado();
            List<Endereco> enderecoListOld = persistentCidade.getEnderecoList();
            List<Endereco> enderecoListNew = cidade.getEnderecoList();
            List<String> illegalOrphanMessages = null;
            for (Endereco enderecoListOldEndereco : enderecoListOld) {
                if (!enderecoListNew.contains(enderecoListOldEndereco)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Endereco " + enderecoListOldEndereco + " since its idcidade field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idestadoNew != null) {
                idestadoNew = em.getReference(idestadoNew.getClass(), idestadoNew.getIdestado());
                cidade.setIdestado(idestadoNew);
            }
            List<Endereco> attachedEnderecoListNew = new ArrayList<Endereco>();
            for (Endereco enderecoListNewEnderecoToAttach : enderecoListNew) {
                enderecoListNewEnderecoToAttach = em.getReference(enderecoListNewEnderecoToAttach.getClass(), enderecoListNewEnderecoToAttach.getIdendereco());
                attachedEnderecoListNew.add(enderecoListNewEnderecoToAttach);
            }
            enderecoListNew = attachedEnderecoListNew;
            cidade.setEnderecoList(enderecoListNew);
            cidade = em.merge(cidade);
            if (idestadoOld != null && !idestadoOld.equals(idestadoNew)) {
                idestadoOld.getCidadeList().remove(cidade);
                idestadoOld = em.merge(idestadoOld);
            }
            if (idestadoNew != null && !idestadoNew.equals(idestadoOld)) {
                idestadoNew.getCidadeList().add(cidade);
                idestadoNew = em.merge(idestadoNew);
            }
            for (Endereco enderecoListNewEndereco : enderecoListNew) {
                if (!enderecoListOld.contains(enderecoListNewEndereco)) {
                    Cidade oldIdcidadeOfEnderecoListNewEndereco = enderecoListNewEndereco.getIdcidade();
                    enderecoListNewEndereco.setIdcidade(cidade);
                    enderecoListNewEndereco = em.merge(enderecoListNewEndereco);
                    if (oldIdcidadeOfEnderecoListNewEndereco != null && !oldIdcidadeOfEnderecoListNewEndereco.equals(cidade)) {
                        oldIdcidadeOfEnderecoListNewEndereco.getEnderecoList().remove(enderecoListNewEndereco);
                        oldIdcidadeOfEnderecoListNewEndereco = em.merge(oldIdcidadeOfEnderecoListNewEndereco);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cidade.getIdcidade();
                if (findCidade(id) == null) {
                    throw new NonexistentEntityException("The cidade with id " + id + " no longer exists.");
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
            Cidade cidade;
            try {
                cidade = em.getReference(Cidade.class, id);
                cidade.getIdcidade();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cidade with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Endereco> enderecoListOrphanCheck = cidade.getEnderecoList();
            for (Endereco enderecoListOrphanCheckEndereco : enderecoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cidade (" + cidade + ") cannot be destroyed since the Endereco " + enderecoListOrphanCheckEndereco + " in its enderecoList field has a non-nullable idcidade field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Estado idestado = cidade.getIdestado();
            if (idestado != null) {
                idestado.getCidadeList().remove(cidade);
                idestado = em.merge(idestado);
            }
            em.remove(cidade);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cidade> findCidadeEntities() {
        return findCidadeEntities(true, -1, -1);
    }

    public List<Cidade> findCidadeEntities(int maxResults, int firstResult) {
        return findCidadeEntities(false, maxResults, firstResult);
    }

    private List<Cidade> findCidadeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cidade.class));
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

    public Cidade findCidade(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cidade.class, id);
        } finally {
            em.close();
        }
    }

    public int getCidadeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cidade> rt = cq.from(Cidade.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
