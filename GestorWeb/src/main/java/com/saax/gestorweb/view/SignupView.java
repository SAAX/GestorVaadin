package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.ResourceBundle;

/**
 * Janela de criação de nova conta, com dados do usuáiro, billing, empresas, etc.
 *
 * @author Rodrigo / Fernando
 */
public class SignupView extends Window {

    // Referencia ao recurso das mensagens:
    ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getUserData().getMensagens();

    // A view mantem acesso ao listener (Presenter) para notificar os eventos
    // Este acesso se dá por uma interface para manter a abstração das camadas
    private SignupViewListener listener;

    public void setListener(SignupViewListener listener) {
        this.listener = listener;
    }

    // componentes visuais da view
    
    // ABA #1 : Dados do usuário
    private TextField nomeTextField;
    private TextField sobrenomeTextField;
    private TextField senhaTextField;
    private CheckBox aceitaTermosCheckBox;
    
    // ABA #2 : Dados de billing (será feito fururamente)
    
    // ABA #3 : Dados de empresas e filiais
    
    // ABA #4 : Adicionar mais usuários à empresa
    
    
    /**
     * Cria o pop-up de login, com campos para usuário e senha
     *
     */
    public SignupView() {
        super();

        setCaption(mensagens.getString("SignupView.titulo"));
        setModal(true);

        // Container que armazena os elementos visuais (campos de login e senha)       
        VerticalLayout container = new VerticalLayout();
        container.setMargin(true);
        setContent(container);

        // Adicionar: componentes visuais
        container.addComponent(buildTabPanel());

        // barra dos botoes
        HorizontalLayout barraBotoes = buildBarraBotoes();
        container.addComponent(barraBotoes);
        container.setComponentAlignment(barraBotoes, Alignment.MIDDLE_CENTER);


        center();
    }

    
    /**
     * Cria e retorna a barra de botoes
     * @return barra com os botoes OK e Cancelar
     */
    private HorizontalLayout buildBarraBotoes(){
        
        HorizontalLayout barraBotoes = new HorizontalLayout();
        
        // botão para Confirmar
        final Button okButton = new Button(mensagens.getString("SignupView.okButton.label"), (Button.ClickEvent event) -> {
            listener.okButtonClicked();
        });
        
        // botão para cancelar
        final Button cancelButton = new Button(mensagens.getString("SignupView.cancelButton.label"), (Button.ClickEvent event) -> {
            listener.cancelButtonClicked();
        });

        barraBotoes.addComponent(okButton);
        barraBotoes.addComponent(cancelButton);
        
        return barraBotoes;
    }
    
    
    /**
     * Cria o painel com ABAs que terá os paineis de: <br>
     * 1. Cadastro do usuario <br>
     * 2. Billing <br>
     * 3. Cadastro da empresa (conta) <br>
     * 4. Adcionar mais usuários à empresa <br>
     * @TODO: Fernando
     */
    private TabSheet buildTabPanel() {

        // Fernando: criar e retornar o painel de abas
        TabSheet tabSheet = new TabSheet();
        
        tabSheet.addTab(buildAba1CadastroUsuario(), mensagens.getString("SignupView.tabPanel.aba1.titulo"));
        tabSheet.addTab(buildAba2Billing(), mensagens.getString("SignupView.tabPanel.aba2.titulo"));
        tabSheet.addTab(buildAba3CadastroEmpresas(), mensagens.getString("SignupView.tabPanel.aba3.titulo"));
        tabSheet.addTab(buildAba4UsuarioEmpresa(), mensagens.getString("SignupView.tabPanel.aba4.titulo"));
        
        return tabSheet;
    }

    /**
     * Cria e retorna a 1a. aba com os campos para o cadastro de usuario
     * @TODO: Fernando
     * @return aba de cadastro de usuários
     */
    private VerticalLayout buildAba1CadastroUsuario(){
        
        // text field: Nome
        nomeTextField = new TextField(mensagens.getString("SignupView.nomeTextField.label"));
        
        // text field: Sobrenome
        sobrenomeTextField = new TextField(mensagens.getString("SignupView.sobrenomeTextField.label"));
        
         // text field: Senha
        senhaTextField = new TextField(mensagens.getString("SignupView.senhaTextField.label"));
        
        // check box : aceita termos
        aceitaTermosCheckBox = new CheckBox(mensagens.getString("SignupView.aceitaTermosCheckBox.label"));
        
        return null;
    }

    /**
     * Cria e retorna a 2a. aba com os campos para o billing
     * @TODO: Será feito futuramente
     * @return aba com os campos para o billing
     */
    private VerticalLayout buildAba2Billing(){
        return new VerticalLayout();
    }
    
    /**
     * Cria e retorna a 3a. aba com os campos para o cadastro da empresa e suas empresas coligadas e filiais
     * @TODO: Fernando
     * @return aba de cadastro de empresas
     */
    private VerticalLayout buildAba3CadastroEmpresas(){
        return null;
    }
    
    /**
     * Cria e retorna a 4a. aba com os campos para adicionar novos usuários as empresas
     * @TODO: Fernando
     * @return aba de cadastro de usuário -> empresa
     */
    private VerticalLayout buildAba4UsuarioEmpresa(){
        return null;
    }
    
    
    public String getNome() {
        return nomeTextField.getValue();
    }

    public String getSobrenome() {
        return sobrenomeTextField.getValue();
    }

    public String getSenha() {
        return senhaTextField.getValue();
    }

    public CheckBox getAceitaTermos() {
        return aceitaTermosCheckBox;
    }
}
