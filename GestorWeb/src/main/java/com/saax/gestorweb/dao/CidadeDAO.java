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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * DAO para o entity bean: Cidade <br><br>
 * 
 * @author rodrigo
 */
public class CidadeDAO implements Serializable {

    public CidadeDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    
    public void create(Cidade cidade) {
        
        if (cidade.getEnderecos() == null) {
            cidade.setEnderecos(new ArrayList<Endereco>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estado estado = cidade.getEstado();
            if (estado != null) {
                estado = em.getReference(estado.getClass(), estado.getId());
                cidade.setEstado(estado);
            }
            List<Endereco> attachedEnderecos = new ArrayList<Endereco>();
            for (Endereco enderecosEnderecoToAttach : cidade.getEnderecos()) {
                enderecosEnderecoToAttach = em.getReference(enderecosEnderecoToAttach.getClass(), enderecosEnderecoToAttach.getId());
                attachedEnderecos.add(enderecosEnderecoToAttach);
            }
            cidade.setEnderecos(attachedEnderecos);
            em.persist(cidade);
            if (estado != null) {
                estado.getCidades().add(cidade);
                estado = em.merge(estado);
            }
            for (Endereco enderecosEndereco : cidade.getEnderecos()) {
                Cidade oldCidadeOfEnderecosEndereco = enderecosEndereco.getCidade();
                enderecosEndereco.setCidade(cidade);
                enderecosEndereco = em.merge(enderecosEndereco);
                if (oldCidadeOfEnderecosEndereco != null) {
                    oldCidadeOfEnderecosEndereco.getEnderecos().remove(enderecosEndereco);
                    oldCidadeOfEnderecosEndereco = em.merge(oldCidadeOfEnderecosEndereco);
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
            Cidade persistentCidade = em.find(Cidade.class, cidade.getId());
            Estado estadoOld = persistentCidade.getEstado();
            Estado estadoNew = cidade.getEstado();
            List<Endereco> enderecosOld = persistentCidade.getEnderecos();
            List<Endereco> enderecosNew = cidade.getEnderecos();
            List<String> illegalOrphanMessages = null;
            for (Endereco enderecosOldEndereco : enderecosOld) {
                if (!enderecosNew.contains(enderecosOldEndereco)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Endereco " + enderecosOldEndereco + " since its cidade field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (estadoNew != null) {
                estadoNew = em.getReference(estadoNew.getClass(), estadoNew.getId());
                cidade.setEstado(estadoNew);
            }
            List<Endereco> attachedEnderecosNew = new ArrayList<Endereco>();
            for (Endereco enderecosNewEnderecoToAttach : enderecosNew) {
                enderecosNewEnderecoToAttach = em.getReference(enderecosNewEnderecoToAttach.getClass(), enderecosNewEnderecoToAttach.getId());
                attachedEnderecosNew.add(enderecosNewEnderecoToAttach);
            }
            enderecosNew = attachedEnderecosNew;
            cidade.setEnderecos(enderecosNew);
            cidade = em.merge(cidade);
            if (estadoOld != null && !estadoOld.equals(estadoNew)) {
                estadoOld.getCidades().remove(cidade);
                estadoOld = em.merge(estadoOld);
            }
            if (estadoNew != null && !estadoNew.equals(estadoOld)) {
                estadoNew.getCidades().add(cidade);
                estadoNew = em.merge(estadoNew);
            }
            for (Endereco enderecosNewEndereco : enderecosNew) {
                if (!enderecosOld.contains(enderecosNewEndereco)) {
                    Cidade oldCidadeOfEnderecosNewEndereco = enderecosNewEndereco.getCidade();
                    enderecosNewEndereco.setCidade(cidade);
                    enderecosNewEndereco = em.merge(enderecosNewEndereco);
                    if (oldCidadeOfEnderecosNewEndereco != null && !oldCidadeOfEnderecosNewEndereco.equals(cidade)) {
                        oldCidadeOfEnderecosNewEndereco.getEnderecos().remove(enderecosNewEndereco);
                        oldCidadeOfEnderecosNewEndereco = em.merge(oldCidadeOfEnderecosNewEndereco);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cidade.getId();
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
                cidade.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cidade with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Endereco> enderecosOrphanCheck = cidade.getEnderecos();
            for (Endereco enderecosOrphanCheckEndereco : enderecosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cidade (" + cidade + ") cannot be destroyed since the Endereco " + enderecosOrphanCheckEndereco + " in its enderecos field has a non-nullable cidade field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Estado estado = cidade.getEstado();
            if (estado != null) {
                estado.getCidades().remove(cidade);
                estado = em.merge(estado);
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
