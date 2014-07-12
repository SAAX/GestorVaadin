package com.saax.gestorweb.dao;

import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.FilialEmpresa;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * DAO para o entity bean: FilialEmpresa <br><br>
 * 
 * Classe gerada automaticamente pelo netbeans: NÃO ALTERAR<br>
 * Caso seja necessária alguma customização, estender esta classe<br>
 * 
 * @author rodrigo
 */
class FilialEmpresaDAO implements Serializable {

    public FilialEmpresaDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(FilialEmpresa filialEmpresa) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa matriz = filialEmpresa.getMatriz();
            if (matriz != null) {
                matriz = em.getReference(matriz.getClass(), matriz.getId());
                filialEmpresa.setMatriz(matriz);
            }
            em.persist(filialEmpresa);
            if (matriz != null) {
                matriz.getFiliais().add(filialEmpresa);
                matriz = em.merge(matriz);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(FilialEmpresa filialEmpresa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FilialEmpresa persistentFilialEmpresa = em.find(FilialEmpresa.class, filialEmpresa.getId());
            Empresa matrizOld = persistentFilialEmpresa.getMatriz();
            Empresa matrizNew = filialEmpresa.getMatriz();
            if (matrizNew != null) {
                matrizNew = em.getReference(matrizNew.getClass(), matrizNew.getId());
                filialEmpresa.setMatriz(matrizNew);
            }
            filialEmpresa = em.merge(filialEmpresa);
            if (matrizOld != null && !matrizOld.equals(matrizNew)) {
                matrizOld.getFiliais().remove(filialEmpresa);
                matrizOld = em.merge(matrizOld);
            }
            if (matrizNew != null && !matrizNew.equals(matrizOld)) {
                matrizNew.getFiliais().add(filialEmpresa);
                matrizNew = em.merge(matrizNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = filialEmpresa.getId();
                if (findFilialEmpresa(id) == null) {
                    throw new NonexistentEntityException("The filialEmpresa with id " + id + " no longer exists.");
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
            FilialEmpresa filialEmpresa;
            try {
                filialEmpresa = em.getReference(FilialEmpresa.class, id);
                filialEmpresa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The filialEmpresa with id " + id + " no longer exists.", enfe);
            }
            Empresa matriz = filialEmpresa.getMatriz();
            if (matriz != null) {
                matriz.getFiliais().remove(filialEmpresa);
                matriz = em.merge(matriz);
            }
            em.remove(filialEmpresa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<FilialEmpresa> findFilialEmpresaEntities() {
        return findFilialEmpresaEntities(true, -1, -1);
    }

    public List<FilialEmpresa> findFilialEmpresaEntities(int maxResults, int firstResult) {
        return findFilialEmpresaEntities(false, maxResults, firstResult);
    }

    private List<FilialEmpresa> findFilialEmpresaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FilialEmpresa.class));
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

    public FilialEmpresa findFilialEmpresa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FilialEmpresa.class, id);
        } finally {
            em.close();
        }
    }

    public int getFilialEmpresaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FilialEmpresa> rt = cq.from(FilialEmpresa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
