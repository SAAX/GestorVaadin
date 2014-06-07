package com.saax.gestorweb.dao;

import com.saax.gestorweb.model.datamodel.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Customização do DAO de Usuario criado pelo netbeans <br>
 * Esta classe substitui o DAO principal.
 * 
 * @author rodrigo
 */
public class UsuarioDAOCustom extends UsuarioDAO{

    public UsuarioDAOCustom(EntityManagerFactory emf) {
        super(emf);
    }
    
    /**
     * Busca um Usuairo pelo seu Login
     * @param login
     * @return 
     */
    public Usuario findByLogin(String login) {
        EntityManager em = getEntityManager();

        try {
            return (Usuario) em.createNamedQuery("Usuario.findByLogin")
                    .setParameter("login", login)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }


    
}
