package com.saax.gestorweb.model;

import com.saax.gestorweb.dao.GenericDAO;
import com.saax.gestorweb.dao.UsuarioDAO;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.PostgresConnection;
import java.util.Iterator;

/**
 * Classe de negócios do Login
 * Esta classe é responsável por implementar todas as regras de negócio do Login
 * para isto ela acessa (pede ajuda) as classes de DAO
 * @author Rodrigo
 */
public class LoginModel {

    private final UsuarioDAO usuarioDAO;
    private final GenericDAO genericDAO;

    /**
     * Cria o model e conecta ao DAO
     */
    public LoginModel() {
        usuarioDAO = new UsuarioDAO(PostgresConnection.getInstance().getEntityManagerFactory());
        genericDAO = new GenericDAO();
    }

    /**
     * Verifica se um usuario está cadastrado pelo seu login
     * @param login
     * @return 
     */
    public boolean verificaLoginExistente(String login) {
        System.out.println("entrou no metodo " + login);
        
        Usuario u = (Usuario) genericDAO.findByNamedQuery("Usuario.findByLogin", "login", login);

        return (u!=null);
    }

    /**
     * Obtém um usuário pelo seu login
     * @param login
     * @return 
     */
    public Usuario getUsuario(String login) {
        return (Usuario) genericDAO.listByNamedQuery("Usuario.findByLogin", "login", login).iterator().next();
    }
    
}
