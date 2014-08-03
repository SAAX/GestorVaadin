package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.dao.UsuarioDAO;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.util.PostgresConnection;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.ResourceBundle;

/**
 * Pagina Incial View Esta classe é responsável por construir os componentes
 * visuais da tela incial
 *
 * @author Rodrigo
 */
public class PaginaInicialView extends HorizontalLayout {

    // Referencia ao recurso das mensagens:
    private final ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getUserData().getMensagens();
    private final GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getUserData().getImagens();

    // A view mantem acesso ao listener (Presenter) para notificar os eventos
    // Este acesso se dá por uma interface para manter a abstração das camadas
    private PaginaInicialViewListener listener;

    public void setListener(PaginaInicialViewListener listener) {
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
    public PaginaInicialView() {

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
        final Button signUpButton = new Button(mensagens.getString("PaginaInicialView.signUpButton.label"), (Button.ClickEvent event) -> {
            listener.signUpButtonClicked();
        });

        // botão para Login
        final Button loginButton = new Button(mensagens.getString("PaginaInicialView.loginButton.label"), (Button.ClickEvent event) -> {
            listener.loginButtonClicked();
        });

        // botão para preview do dashboard
        final Button previewDashboardButton = new Button("dashboard preview", (Button.ClickEvent event) -> {
            UsuarioDAO dao = new UsuarioDAO(PostgresConnection.getInstance().getEntityManagerFactory());
            Usuario usuarioTeste = dao.findByLogin("teste-user@gmail.com");
            ((GestorMDI) UI.getCurrent()).getUserData().setUsuarioLogado(usuarioTeste);
            ((GestorMDI) UI.getCurrent()).carregarDashBoard();
        });

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
