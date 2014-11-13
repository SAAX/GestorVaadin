package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.CadastroMetaModel;
import com.saax.gestorweb.model.ChatModel;
import com.saax.gestorweb.model.LoginModel;
import com.saax.gestorweb.model.PaginaInicialModel;
import com.saax.gestorweb.model.SignupModel;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.view.CadastroMetaView;
import com.saax.gestorweb.view.ChatView;
import com.saax.gestorweb.view.LoginView;
import com.saax.gestorweb.view.PaginaInicialView;
import com.saax.gestorweb.view.PaginaInicialViewListener;
import com.saax.gestorweb.view.SignupView;
import com.vaadin.ui.UI;
import java.io.Serializable;
import java.util.ResourceBundle;

/**
 * Presenter da Pagina Inicial Esta classe é responsável captar todos os eventos
 * que ocorrem na View e dar o devido tratamento, utilizando para isto o modelo
 *
 *
 * @author Rodrigo
 */
public class PaginaInicialPresenter implements PaginaInicialViewListener, Serializable {

    // Todo presenter mantem acesso à view e ao model
    private final PaginaInicialView view;
    private final PaginaInicialModel model;

    // Referencia ao recurso das mensagens:
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();

    /**
     * Cria o presenter ligando o Model ao View
     *
     * @param model
     * @param view
     */
    public PaginaInicialPresenter(PaginaInicialModel model,
            PaginaInicialView view) {

        this.model = model;
        this.view = view;

        view.setListener(this);
    }

    /**
     * Evento disparado ao ser acionado o botão "Login"
     */
    @Override
    public void loginButtonClicked() {

        // Cria o pop up de login (model e view)
        LoginModel loginModel = new LoginModel();
        LoginView loginView = new LoginView();

        // O presenter liga model e view
        new LoginPresenter(loginModel, loginView);

        // adiciona a visualização à UI
        UI.getCurrent().addWindow(loginView);
        

    }

    @Override
    public void signUpButtonClicked() {
        
        //Cria o pop up para registrar a conta (model e viw)
        SignupModel signupModel = new SignupModel();
        SignupView signupView = new SignupView();
        
       //o presenter liga model e view
        SignupPresenter signupPresenter;
        signupPresenter = new SignupPresenter(signupModel, signupView);
        //adiciona a visualização à UI
        UI.getCurrent().addWindow(signupView);
        signupPresenter.open();
    }
    
}
