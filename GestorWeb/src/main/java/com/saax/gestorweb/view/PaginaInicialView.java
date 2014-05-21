package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.ResourceBundle;
import sun.security.krb5.Config;

/**
 * Pagina Incial View
 * Esta classe é responsável por construir os componentes visuais da tela incial
 * 
 * @author Rodrigo
 */
public class PaginaInicialView extends Panel {
    
    // Referencia ao recurso das mensagens:
    private final ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    
    // A view mantem acesso ao listener (Presenter) para notificar os eventos
    // Este acesso se dá por uma interface para manter a abstração das camadas
    private PaginaInicialViewListener listener;
    public void setListener(PaginaInicialViewListener listener) {
        this.listener = listener;
    }

    // container que armazenará a caixa com os botões de login / sign-up
    Layout containerDireito;
    private Label nomeUsuarioLogado;
    
    /**
     * Constroi a pagina inicial da aplicação com as imagens de fundo e os 
     * botões para cadastro / login
     * Este container (layout) terá dois outros containers dentro,
     * um para a imagem de fundo e outro para a caixa com os botões de login / sign-up
     */
    public PaginaInicialView(){
        
        
        // container que armazenará a imagem de fundo
        Layout containerEsquerdo = new VerticalLayout();
        containerEsquerdo.setWidth(50, Unit.PERCENTAGE);
        
        // container que armazenará a caixa com os botões de login / sign-up
        containerDireito = new VerticalLayout();
        containerDireito.setWidth(50, Unit.PERCENTAGE);
        
        // @ TODO:
        containerEsquerdo.addComponent(new Label("<h1>Colocar uma imagem aqui</h1>", ContentMode.HTML));
        
        // botão para SignUP
        final Button signUpButton = new Button(mensagens.getString("PaginaInicialView.signUpButton.label"), new Button.ClickListener() {

            // notifica o listener que o botão foi acionado para que este dê o devido tratamento
            @Override
            public void buttonClick(Button.ClickEvent event) {
                listener.signUpButtonClicked();
            }
        });
        containerDireito.addComponent(signUpButton);

        // botão para Login
        final Button loginButton = new Button(mensagens.getString("PaginaInicialView.loginButton.label"), new Button.ClickListener() {

            // notifica o listener que o botão foi acionado para que este dê o devido tratamento
            @Override
            public void buttonClick(Button.ClickEvent event) {
                listener.loginButtonClicked();
            }
        });
        containerDireito.addComponent(loginButton);

        
        // Adicona os dois containers, lado-a-lado
        setContent(new HorizontalLayout(containerEsquerdo,containerDireito));
        
        setSizeFull();


    }

    
}
