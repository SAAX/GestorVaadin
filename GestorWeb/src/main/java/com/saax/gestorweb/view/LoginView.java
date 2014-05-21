package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.ResourceBundle;

/**
 * Pop-up de login 
 * Esta classe é responsável por construir os componentes visuais do pop-up de login
 *
 * @author Rodrigo
 */
public class LoginView extends Window {

    // Referencia ao recurso das mensagens:
    ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();

    // A view mantem acesso ao listener (Presenter) para notificar os eventos
    // Este acesso se dá por uma interface para manter a abstração das camadas
    private LoginViewListener listener;
    
    // componentes visuais da view
    private final TextField loginTextField;
    private final TextField senhaTextField;
    private final Label labelMensagens;
    
    public void setListener(LoginViewListener listener) {
        this.listener = listener;
    }
    /**
     * Cria o pop-up de login, com campos para usuário e senha
     *
     */
    public LoginView() {
        super();
        
        setCaption(mensagens.getString("LoginView.titulo"));
        setModal(true);
        
        // Container que armazena os elementos visuais (campos de login e senha)       
        VerticalLayout container = new VerticalLayout();
        container.setMargin(true);
        setContent(container);
        
        // Adicionar: componentes visuais
        
        // text field: Login
        loginTextField = new TextField(mensagens.getString("LoginView.loginTextField.label"));
        container.addComponent(loginTextField);
        
        // text field: Senha
        senhaTextField = new TextField(mensagens.getString("LoginView.senhaTextField.label"));
        container.addComponent(senhaTextField);
        
        // lablel: Mensagens
        labelMensagens = new Label();
        container.addComponent(labelMensagens);
        
        // botão para Login
        final Button doLoginButton = new Button(mensagens.getString("LoginView.doLoginButton.label"), new Button.ClickListener() {

            // notifica o listener que o botão foi acionado para que este dê o devido tratamento
            @Override
            public void buttonClick(Button.ClickEvent event) {
                listener.loginButtonClicked();
            }
        });
        container.addComponent(doLoginButton);
        
        center();
    }

    public String getLogin() {
        return loginTextField.getValue();
    }

    public String getSenha() {
        return senhaTextField.getValue();
    }

    public void apresentaMensagemErro(String message) {
        labelMensagens.setValue(message);
        // @TODO: colocar um estilo
        // labelMensagens.setStyleName(estilo);
    }

}
