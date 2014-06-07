package com.saax.gestorweb.dao;

import com.saax.gestorweb.dao.exceptions.IllegalOrphanException;
import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import com.saax.gestorweb.model.datamodel.Usuario;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.saax.gestorweb.model.datamodel.UsuarioEmpresa;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * DAO para o entity bean: Usuario <br><br>
 * 
 * Classe gerada automaticamente pelo netbeans: NÃO ALTERAR<br>
 * 
 * ATENÇÃO: Não instanciar esta classe diretamente, mas sim sua subclasse substituta: UsuarioDAOCustom
 * 
 * @author rodrigo
 */
class UsuarioDAO implements Serializable {

    public UsuarioDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
        if (usuario.getEmpresas() == null) {
            usuario.setEmpresas(new ArrayList<UsuarioEmpresa>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<UsuarioEmpresa> attachedEmpresas = new ArrayList<UsuarioEmpresa>();
            for (UsuarioEmpresa empresasUsuarioEmpresaToAttach : usuario.getEmpresas()) {
                empresasUsuarioEmpresaToAttach = em.getReference(empresasUsuarioEmpresaToAttach.getClass(), empresasUsuarioEmpresaToAttach.getId());
                attachedEmpresas.add(empresasUsuarioEmpresaToAttach);
            }
            usuario.setEmpresas(attachedEmpresas);
            em.persist(usuario);
            for (UsuarioEmpresa empresasUsuarioEmpresa : usuario.getEmpresas()) {
                Usuario oldUsuarioOfEmpresasUsuarioEmpresa = empresasUsuarioEmpresa.getUsuario();
                empresasUsuarioEmpresa.setUsuario(usuario);
                empresasUsuarioEmpresa = em.merge(empresasUsuarioEmpresa);
                if (oldUsuarioOfEmpresasUsuarioEmpresa != null) {
                    oldUsuarioOfEmpresasUsuarioEmpresa.getEmpresas().remove(empresasUsuarioEmpresa);
                    oldUsuarioOfEmpresasUsuarioEmpresa = em.merge(oldUsuarioOfEmpresasUsuarioEmpresa);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getId());
            Collection<UsuarioEmpresa> empresasOld = persistentUsuario.getEmpresas();
            Collection<UsuarioEmpresa> empresasNew = usuario.getEmpresas();
            List<String> illegalOrphanMessages = null;
            for (UsuarioEmpresa empresasOldUsuarioEmpresa : empresasOld) {
                if (!empresasNew.contains(empresasOldUsuarioEmpresa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UsuarioEmpresa " + empresasOldUsuarioEmpresa + " since its usuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<UsuarioEmpresa> attachedEmpresasNew = new ArrayList<UsuarioEmpresa>();
            for (UsuarioEmpresa empresasNewUsuarioEmpresaToAttach : empresasNew) {
                empresasNewUsuarioEmpresaToAttach = em.getReference(empresasNewUsuarioEmpresaToAttach.getClass(), empresasNewUsuarioEmpresaToAttach.getId());
                attachedEmpresasNew.add(empresasNewUsuarioEmpresaToAttach);
            }
            empresasNew = attachedEmpresasNew;
            usuario.setEmpresas(empresasNew);
            usuario = em.merge(usuario);
            for (UsuarioEmpresa empresasNewUsuarioEmpresa : empresasNew) {
                if (!empresasOld.contains(empresasNewUsuarioEmpresa)) {
                    Usuario oldUsuarioOfEmpresasNewUsuarioEmpresa = empresasNewUsuarioEmpresa.getUsuario();
                    empresasNewUsuarioEmpresa.setUsuario(usuario);
                    empresasNewUsuarioEmpresa = em.merge(empresasNewUsuarioEmpresa);
                    if (oldUsuarioOfEmpresasNewUsuarioEmpresa != null && !oldUsuarioOfEmpresasNewUsuarioEmpresa.equals(usuario)) {
                        oldUsuarioOfEmpresasNewUsuarioEmpresa.getEmpresas().remove(empresasNewUsuarioEmpresa);
                        oldUsuarioOfEmpresasNewUsuarioEmpresa = em.merge(oldUsuarioOfEmpresasNewUsuarioEmpresa);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getId();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<UsuarioEmpresa> empresasOrphanCheck = usuario.getEmpresas();
            for (UsuarioEmpresa empresasOrphanCheckUsuarioEmpresa : empresasOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the UsuarioEmpresa " + empresasOrphanCheckUsuarioEmpresa + " in its empresas field has a non-nullable usuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
