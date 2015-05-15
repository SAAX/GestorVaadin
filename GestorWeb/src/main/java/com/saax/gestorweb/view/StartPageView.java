package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.TaskModel;
import com.saax.gestorweb.model.LoginModel;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.presenter.TaskPresenter;
import com.saax.gestorweb.util.GestorWebImagens;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.ResourceBundle;

/**
 * This class is responsible for show the visual components of the start page
 *
 * @author Rodrigo
 */
public class StartPageView extends HorizontalLayout {

    // Referencia ao recurso das mensagens:
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final transient GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();

    // A view mantem acesso ao listener (Presenter) para notificar os eventos
    // Este acesso se dá por uma interface para manter a abstração das camadas
    private StartPageViewListener listener;

    public void setListener(StartPageViewListener listener) {
        this.listener = listener;
    }

    // container que armazenará a caixa com os botões de login / sign-up
    VerticalLayout containerDireito;
    // container que armazenará a imagem de fundo
    VerticalLayout containerEsquerdo;

    /**
     * Constroi a pagina inicial da aplicação com as imagens de fundo e os
     * botões para cadastro / login Este container (layout) terá dois outros
     * containers dentro, um para a imagem de fundo e outro para a caixa com os
     * botões de login / sign-up
     */
    public StartPageView() {

        setSizeFull();

        // container que armazenará a imagem de fundo
        containerEsquerdo = new VerticalLayout();
        containerEsquerdo.setHeight(450, Unit.PIXELS);

        // container que armazenará a caixa com os botões de login / sign-up
        containerDireito = new VerticalLayout();
        containerDireito.setHeight(450, Unit.PIXELS);

        // @ TODO:
        Image logo = imagens.getPAGINAINICIAL_LOGO();
        containerEsquerdo.addComponent(logo);
        containerEsquerdo.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);

        // botão para SignUP
        final Button signUpButton = new Button(mensagens.getString("startPageView.signUpButton.label"), (Button.ClickEvent event) -> {
            listener.signUpButtonClicked();
        });

        // botão para Login
        final Button loginButton = new Button(mensagens.getString("startPageView.loginButton.label"), (Button.ClickEvent event) -> {
            listener.loginButtonClicked();
        });

        // botão para preview do dashboard
        final Button previewDashboardButton = new Button("dashboard preview", (Button.ClickEvent event) -> {
            Usuario usuarioTeste = new LoginModel().getUsuario("teste-user@gmail.com");
            getSession().setAttribute("loggedUser", usuarioTeste);
            usuarioTeste.setEmpresaAtiva(new LoginModel().getEmpresaUsuarioLogado());
            getSession().setAttribute("loggedUser", usuarioTeste);
            ((GestorMDI) UI.getCurrent()).carregarDashBoard();
        });

        
        // botão para preview de nova aba
        BrowserWindowOpener opener = new BrowserWindowOpener("http://google.com");// 
        opener.setWindowName("_blank");// _new, _blank, _top, etc.

        final Button previewNewTabButton = new Button("Nova Aba");
        opener.extend(previewNewTabButton);

        // barra dos botoes
        HorizontalLayout barraBotoes = new HorizontalLayout();
        containerDireito.addComponent(barraBotoes);
        containerDireito.setComponentAlignment(barraBotoes, Alignment.MIDDLE_CENTER);

        barraBotoes.addComponent(signUpButton);
        barraBotoes.addComponent(loginButton);
        barraBotoes.addComponent(previewDashboardButton);

        // Adicona os dois containers, lado-a-lado
        addComponent(containerEsquerdo);
        setComponentAlignment(containerEsquerdo, Alignment.MIDDLE_LEFT);
        addComponent(containerDireito);
        setComponentAlignment(containerDireito, Alignment.MIDDLE_RIGHT);

    }

}
