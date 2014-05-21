package com.saax.gestorweb.model;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.dao.UsuarioDAO;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.Cipher;
import com.saax.gestorweb.util.GestorException;
import com.saax.gestorweb.util.PostgresConnection;
import com.vaadin.ui.UI;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe de negócios do Login
 * Esta classe é responsável por implementar todas as regras de negócio do Login
 * para isto ela acessa (pede ajuda) as classes de DAO
 * @author Rodrigo
 */
public class LoginModel {

    // Referencia ao recurso das mensagens:
    private final ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    
    private final UsuarioDAO usuarioDAO;

    public LoginModel() {
        usuarioDAO = new UsuarioDAO(PostgresConnection.getInstance().getEntityManagerFactory());
    }
    
    
    
    /**
     * Autentica usuário e senha
     * @param login
     * @param senha
     * @throws GestorException 
     */
    public void autenticarUsuario(String login, String senha) throws GestorException {

        Usuario u = usuarioDAO.findUsuarioByLogin(login);
        if (u==null) {
            throw new GestorException(mensagens.getString("LoginModel.falhaAutenticacao.loginInvalido.erro"));
        }
        String senhaCriptografada = null;
        try {
            senhaCriptografada = new Cipher().md5Sum(senha);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LoginModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (!senhaCriptografada.equals(u.getSenha())){
            throw new GestorException(mensagens.getString("LoginModel.falhaAutenticacao.senhaInvalida.erro"));
        }
        
        // Configura o usuáio logado na seção
        ((GestorMDI) UI.getCurrent()).setUsuarioLogado(u);
        
        
    }
    
}
