package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.LoginModel;
import com.saax.gestorweb.util.GestorException;
import com.saax.gestorweb.view.LoginView;
import com.saax.gestorweb.view.LoginViewListener;
import com.vaadin.ui.UI;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Login Presenter
 * Esta classe é responsável captar todos os eventos que ocorrem na View (LoginView)
 * e dar o devido tratamento, utilizando para isto o modelo (LoginModel)
 * @author Rodrigo
 */
public class LoginPresenter implements LoginViewListener {
   
    // Todo presenter mantem acesso à view e ao model
    private final LoginView view;
    private final LoginModel model;

    /**
     * Cria o presenter ligando o Model ao View
     * @param model
     * @param view 
     */
    public LoginPresenter(LoginModel model, LoginView view) {
        
        this.model = model;
        this.view = view;
        
        view.setListener(this);
        
    }

    /**
     * Evento disparado ao ser acionado o botão para efetuar o login
     * Obtém o login e senha e autentica no model
     */
    @Override
    public void loginButtonClicked() {
        
        String login = view.getLogin();
        String senha = view.getSenha();
        
        try {
            model.autenticarUsuario(login, senha);
        
            // se a autenticação for bem sucedida:
            // fecha o pop-up
            view.close();
            
        } catch (GestorException ex) {
            view.apresentaMensagemErro(ex.getMessage());
            Logger.getLogger(LoginPresenter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
    }
    
    
    
    
}
