package com.saax.gestorweb.model;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.dao.UsuarioDAOCustom;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.PostgresConnection;
import com.vaadin.ui.UI;
import java.util.ResourceBundle;

/**
 * Classe de negócios do Login
 * Esta classe é responsável por implementar todas as regras de negócio do Login
 * para isto ela acessa (pede ajuda) as classes de DAO
 * @author Rodrigo
 */
public class LoginModel {

    // Referencia ao recurso das mensagens:
    private final ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getUserData().getMensagens();
    
    private final UsuarioDAOCustom usuarioDAO;

    public LoginModel() {
        usuarioDAO = new UsuarioDAOCustom(PostgresConnection.getInstance().getEntityManagerFactory());
    }
    
    public boolean verificaLoginExistente(String login) {
        Usuario u = usuarioDAO.findByLogin(login);
        return (u!=null);
    }

    public Usuario getUsuario(String login) {
        return usuarioDAO.findByLogin(login);
    }
    
}
