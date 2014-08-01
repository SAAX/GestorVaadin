package com.saax.gestorweb.dao;

import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.model.datamodel.UsuarioEmpresa;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * DAO para o entity bean: UsuarioEmpresa <br><br>
 * 
 * @author rodrigo
 */
public class UsuarioEmpresaDAO implements Serializable {

    public UsuarioEmpresaDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * metodo padrao modificado para gravar data/hora de inclusao
     * @param usuarioEmpresa 
     */
    public void create(UsuarioEmpresa usuarioEmpresa) {
        usuarioEmpresa.setDataHoraInclusao(LocalDateTime.now());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa empresa = usuarioEmpresa.getEmpresa();
            if (empresa != null) {
                empresa = em.getReference(empresa.getClass(), empresa.getId());
                usuarioEmpresa.setEmpresa(empresa);
            }
            Usuario usuario = usuarioEmpresa.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getId());
                usuarioEmpresa.setUsuario(usuario);
            }
            Usuario idusuarioinclusao = usuarioEmpresa.getIdUsuarioInclusao();
            if (idusuarioinclusao != null) {
                idusuarioinclusao = em.getReference(idusuarioinclusao.getClass(), idusuarioinclusao.getId());
                usuarioEmpresa.setIdUsuarioInclusao(idusuarioinclusao);
            }
            em.persist(usuarioEmpresa);
            if (empresa != null) {
                empresa.getUsuarios().add(usuarioEmpresa);
                empresa = em.merge(empresa);
            }
            if (usuario != null) {
                usuario.getEmpresas().add(usuarioEmpresa);
                usuario = em.merge(usuario);
            }
            if (idusuarioinclusao != null) {
                idusuarioinclusao.getEmpresas().add(usuarioEmpresa);
                idusuarioinclusao = em.merge(idusuarioinclusao);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UsuarioEmpresa usuarioEmpresa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsuarioEmpresa persistentUsuarioEmpresa = em.find(UsuarioEmpresa.class, usuarioEmpresa.getId());
            Empresa empresaOld = persistentUsuarioEmpresa.getEmpresa();
            Empresa empresaNew = usuarioEmpresa.getEmpresa();
            Usuario usuarioOld = persistentUsuarioEmpresa.getUsuario();
            Usuario usuarioNew = usuarioEmpresa.getUsuario();
            Usuario idusuarioinclusaoOld = persistentUsuarioEmpresa.getIdUsuarioInclusao();
            Usuario idusuarioinclusaoNew = usuarioEmpresa.getIdUsuarioInclusao();
            if (empresaNew != null) {
                empresaNew = em.getReference(empresaNew.getClass(), empresaNew.getId());
                usuarioEmpresa.setEmpresa(empresaNew);
            }
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getId());
                usuarioEmpresa.setUsuario(usuarioNew);
            }
            if (idusuarioinclusaoNew != null) {
                idusuarioinclusaoNew = em.getReference(idusuarioinclusaoNew.getClass(), idusuarioinclusaoNew.getId());
                usuarioEmpresa.setIdUsuarioInclusao(idusuarioinclusaoNew);
            }
            usuarioEmpresa = em.merge(usuarioEmpresa);
            if (empresaOld != null && !empresaOld.equals(empresaNew)) {
                empresaOld.getUsuarios().remove(usuarioEmpresa);
                empresaOld = em.merge(empresaOld);
            }
            if (empresaNew != null && !empresaNew.equals(empresaOld)) {
                empresaNew.getUsuarios().add(usuarioEmpresa);
                empresaNew = em.merge(empresaNew);
            }
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getEmpresas().remove(usuarioEmpresa);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getEmpresas().add(usuarioEmpresa);
                usuarioNew = em.merge(usuarioNew);
            }
            if (idusuarioinclusaoOld != null && !idusuarioinclusaoOld.equals(idusuarioinclusaoNew)) {
                idusuarioinclusaoOld.getEmpresas().remove(usuarioEmpresa);
                idusuarioinclusaoOld = em.merge(idusuarioinclusaoOld);
            }
            if (idusuarioinclusaoNew != null && !idusuarioinclusaoNew.equals(idusuarioinclusaoOld)) {
                idusuarioinclusaoNew.getEmpresas().add(usuarioEmpresa);
                idusuarioinclusaoNew = em.merge(idusuarioinclusaoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuarioEmpresa.getId();
                if (findUsuarioEmpresa(id) == null) {
                    throw new NonexistentEntityException("The usuarioEmpresa with id " + id + " no longer exists.");
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
            UsuarioEmpresa usuarioEmpresa;
            try {
                usuarioEmpresa = em.getReference(UsuarioEmpresa.class, id);
                usuarioEmpresa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarioEmpresa with id " + id + " no longer exists.", enfe);
            }
            Empresa empresa = usuarioEmpresa.getEmpresa();
            if (empresa != null) {
                empresa.getUsuarios().remove(usuarioEmpresa);
                empresa = em.merge(empresa);
            }
            Usuario usuario = usuarioEmpresa.getUsuario();
            if (usuario != null) {
                usuario.getEmpresas().remove(usuarioEmpresa);
                usuario = em.merge(usuario);
            }
            Usuario idusuarioinclusao = usuarioEmpresa.getIdUsuarioInclusao();
            if (idusuarioinclusao != null) {
                idusuarioinclusao.getEmpresas().remove(usuarioEmpresa);
                idusuarioinclusao = em.merge(idusuarioinclusao);
            }
            em.remove(usuarioEmpresa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UsuarioEmpresa> findUsuarioEmpresaEntities() {
        return findUsuarioEmpresaEntities(true, -1, -1);
    }

    public List<UsuarioEmpresa> findUsuarioEmpresaEntities(int maxResults, int firstResult) {
        return findUsuarioEmpresaEntities(false, maxResults, firstResult);
    }

    private List<UsuarioEmpresa> findUsuarioEmpresaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UsuarioEmpresa.class));
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

    public UsuarioEmpresa findUsuarioEmpresa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UsuarioEmpresa.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioEmpresaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UsuarioEmpresa> rt = cq.from(UsuarioEmpresa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
