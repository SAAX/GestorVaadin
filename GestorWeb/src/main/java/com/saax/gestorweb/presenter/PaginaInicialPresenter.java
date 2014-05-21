package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.LoginModel;
import com.saax.gestorweb.model.PaginaInicialModel;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.view.LoginView;
import com.saax.gestorweb.view.PaginaInicialView;
import com.saax.gestorweb.view.PaginaInicialViewListener;
import com.vaadin.ui.UI;
import java.util.ResourceBundle;

/**
 * Presenter da Pagina Inicial Esta classe é responsável captar todos os eventos
 * que ocorrem na View e dar o devido tratamento, utilizando para isto o modelo
 *
 *
 * @author Rodrigo
 */
public class PaginaInicialPresenter implements PaginaInicialViewListener {

    // Todo presenter mantem acesso à view e ao model
    private final PaginaInicialView view;
    private final PaginaInicialModel model;

    // Referencia ao recurso das mensagens:
    private final ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();

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

        ((GestorMDI) UI.getCurrent()).carregarDashBoard();

    }

    @Override
    public void signUpButtonClicked() {

    }

}
