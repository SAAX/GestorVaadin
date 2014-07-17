package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
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
    ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getUserData().getMensagens();

    // A view mantem acesso ao listener (Presenter) para notificar os eventos
    // Este acesso se dá por uma interface para manter a abstração das camadas
    private LoginViewListener listener;
    
    // componentes visuais da view
    private final TextField loginTextField;
    private final TextField senhaTextField;
    private final CheckBox lembrarLoginCheckBox;
    
    public void setListener(LoginViewListener listener) {
        this.listener = listener;
    }

    public LoginViewListener getListener() {
        return listener;
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
        loginTextField.addValidator(new EmailValidator(
                mensagens.getString("LoginView.loginTextField.erro.loginNaoInformado")));
        container.addComponent(loginTextField);
        loginTextField.setValidationVisible(false);
        
        // text field: Senha
        senhaTextField = new TextField(mensagens.getString("LoginView.senhaTextField.label"));
        senhaTextField.addValidator(new StringLengthValidator(
                mensagens.getString("LoginView.senhaTextField.erro.senhaNaoInformada"),
                3, null, false));
        container.addComponent(senhaTextField);
        senhaTextField.setValidationVisible(false);

        // check box : remember-me
        lembrarLoginCheckBox = new CheckBox(mensagens.getString("LoginView.lembrarLoginCheckBox.label"));
        container.addComponent(lembrarLoginCheckBox);
        
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

    
    public CheckBox getLembrarLoginCheckBox() {
        return lembrarLoginCheckBox;
    }
    
    public void setSenha(String login){
        senhaTextField.setValue(login);
    }
    

   

    /**
     * Apresenta mensagem de erro no campo de texto de login indicado que o login informado não existe
     */
    public void apresentaErroUsuarioNaoExiste() {
        loginTextField.setComponentError(new UserError(mensagens.getString("LoginView.loginTextField.erro.loginNaoExiste")));        
    }

    public TextField getLoginTextField() {
        return loginTextField;
    }

    public TextField getSenhaTextField() {
        return senhaTextField;
    }
    

    /**
     * Executa os metodos de validações dos campos informados 
     */
    public void validate() {
        
        senhaTextField.setValidationVisible(true);
        loginTextField.setValidationVisible(true);

        loginTextField.validate();
        senhaTextField.validate();
    }

    public void apresentaErroSenhaInvalida() {
        senhaTextField.setComponentError(new UserError(mensagens.getString("LoginView.senhaTextField.erro.senhaNaoAutenticou")));        
        
    }

    public void setLogin(String login) {
        loginTextField.setValue(login);
        senhaTextField.focus();
    }

    public void setLembrarSessao(boolean b) {
        lembrarLoginCheckBox.setValue(b);
    }
    
}
