package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

/**
 * Classe de negócios do Login Esta classe é responsável por implementar todas
 * as regras de negócio do Login para isto ela acessa (pede ajuda) as classes de
 * DAO
 *
 * @author Rodrigo
 */
public class LoginModel {

    /**
     * Verifica se um usuario está cadastrado pelo seu login
     *
     * @param login
     * @return
     */
    public static Boolean verificaLoginExistente(String login) {

        List<Usuario> usuarios = null;

        EntityManager em = GestorEntityManagerProvider.getEntityManager();
        try {
            usuarios = em.createNamedQuery("Usuario.findByLogin")
                    .setParameter("login", login)
                    .getResultList();

            return (!usuarios.isEmpty());

        }
        catch (Exception e) {

            Logger.getLogger(LoginModel.class.getName()).log(Level.SEVERE, "", e);

        }

        return null;

    }

    /**
     * Verifica se é o primeiro login do usuário, para fazer a confirmação da
     * senha
     *
     * @param login
     * @return
     */
    public static Boolean verificaPrimeiroLogin(String login) {
        List<Usuario> usuarios = null;
        EntityManager em = GestorEntityManagerProvider.getEntityManager();
        
        try {
            usuarios = em.createNamedQuery("Usuario.findPrimeiroLoginByLogin")
                    .setParameter("login", login)
                    .getResultList();

            return (!usuarios.isEmpty());
        }
        catch (Exception e) {

            Logger.getLogger(LoginModel.class.getName()).log(Level.SEVERE, "", e);

        }
        return null;
    }

    /**
     * Obtém um usuário pelo seu login
     *
     * @param login
     * @return
     */
    public static Usuario getUsuario(String login) {

        return UsuarioModel.findByLogin(login);

    }

}
