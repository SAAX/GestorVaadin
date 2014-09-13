package com.saax.gestorweb.model;

import com.saax.gestorweb.dao.GenericDAO;
import com.saax.gestorweb.dao.UsuarioDAO;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.PostgresConnection;
import java.util.Iterator;
import java.util.List;

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
        
        List<Usuario> usuarios = null;
        try{
            usuarios = genericDAO.listByNamedQuery("Usuario.findByLogin", "login", login);
        }catch(Exception e){
            e.printStackTrace();
        }
            
        return (!usuarios.isEmpty());
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
