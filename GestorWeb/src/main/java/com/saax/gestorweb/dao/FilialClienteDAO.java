package com.saax.gestorweb.dao;

import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.FilialCliente;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * DAO para o entity bean: FilialCliente <br><br>
 * 
 * Classe gerada automaticamente pelo netbeans: NÃO ALTERAR<br>
 * Caso seja necessária alguma customização, estender esta classe<br>
 * 
 * @author rodrigo
 */
public class FilialClienteDAO implements Serializable {

    public FilialClienteDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(FilialCliente filialCliente) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EmpresaCliente matriz = filialCliente.getMatriz();
            if (matriz != null) {
                matriz = em.getReference(matriz.getClass(), matriz.getId());
                filialCliente.setMatriz(matriz);
            }
            em.persist(filialCliente);
            if (matriz != null) {
                matriz.getFiliais().add(filialCliente);
                matriz = em.merge(matriz);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(FilialCliente filialCliente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FilialCliente persistentFilialCliente = em.find(FilialCliente.class, filialCliente.getId());
            EmpresaCliente matrizOld = persistentFilialCliente.getMatriz();
            EmpresaCliente matrizNew = filialCliente.getMatriz();
            if (matrizNew != null) {
                matrizNew = em.getReference(matrizNew.getClass(), matrizNew.getId());
                filialCliente.setMatriz(matrizNew);
            }
            filialCliente = em.merge(filialCliente);
            if (matrizOld != null && !matrizOld.equals(matrizNew)) {
                matrizOld.getFiliais().remove(filialCliente);
                matrizOld = em.merge(matrizOld);
            }
            if (matrizNew != null && !matrizNew.equals(matrizOld)) {
                matrizNew.getFiliais().add(filialCliente);
                matrizNew = em.merge(matrizNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = filialCliente.getId();
                if (findFilialCliente(id) == null) {
                    throw new NonexistentEntityException("The filialCliente with id " + id + " no longer exists.");
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
            FilialCliente filialCliente;
            try {
                filialCliente = em.getReference(FilialCliente.class, id);
                filialCliente.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The filialCliente with id " + id + " no longer exists.", enfe);
            }
            EmpresaCliente matriz = filialCliente.getMatriz();
            if (matriz != null) {
                matriz.getFiliais().remove(filialCliente);
                matriz = em.merge(matriz);
            }
            em.remove(filialCliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<FilialCliente> findFilialClienteEntities() {
        return findFilialClienteEntities(true, -1, -1);
    }

    public List<FilialCliente> findFilialClienteEntities(int maxResults, int firstResult) {
        return findFilialClienteEntities(false, maxResults, firstResult);
    }

    private List<FilialCliente> findFilialClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FilialCliente.class));
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

    public FilialCliente findFilialCliente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FilialCliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getFilialClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FilialCliente> rt = cq.from(FilialCliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
