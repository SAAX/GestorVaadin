package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
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
    private ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getUserData().getMensagens();

    // A view mantem acesso ao listener (Presenter) para notificar os eventos
    // Este acesso se dá por uma interface para manter a abstração das camadas
    private SignupViewListener listener;

    public void setListener(SignupViewListener listener) {
        this.listener = listener;
    }

    // componentes visuais da view
    private Label mensagemAviso;
    
    // ABA #1 : Dados do usuário
    private TextField nomeTextField;
    private TextField sobrenomeTextField;
    private TextField senhaTextField;
    private CheckBox aceitaTermosCheckBox;
    
    // ABA #2 : Dados de billing (será feito fururamente)
    
    // ABA #3 : Dados de empresas e filiais
     private TextField razaoSocialTextField;
     private TextField nomeFantasiaTextField;
     private TextField cnpjTextField;
     private TextField enderecoTextField;
    
    // ABA #4 : Adicionar mais usuários à empresa
     private TextField nomeUsuarioTextField;
     private TextField emailTextField;
     private TextField confirmaEmailTextField;
    
    
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

        // adicionar label de mensagens
        mensagemAviso = new Label("");
        // @TODO: Fernando, customizar o estilo do aviso
        // mensagemAviso.setStyleName("");
        container.addComponent(mensagemAviso);

        center();
    }

    
    /**
     * Cria e retorna a barra de botoes
     * @return barra com os botoes OK e Cancelar
     */
    private HorizontalLayout buildBarraBotoes(){
        
        HorizontalLayout barraBotoes = new HorizontalLayout();
        
        // botão para Confirmar
        final Button okButton = new Button(getMensagens().getString("SignupView.okButton.label"), (Button.ClickEvent event) -> {
            getListener().okButtonClicked();
        });
        
        // botão para cancelar
        final Button cancelButton = new Button(getMensagens().getString("SignupView.cancelButton.label"), (Button.ClickEvent event) -> {
            getListener().cancelButtonClicked();
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
        
        tabSheet.addTab(buildAba1CadastroUsuario(), getMensagens().getString("SignupView.tabPanel.aba1.titulo"));
        //tabSheet.addTab(buildAba2Billing(), mensagens.getString("SignupView.tabPanel.aba2.titulo"));
        tabSheet.addTab(buildAba3CadastroEmpresas(), getMensagens().getString("SignupView.tabPanel.aba3.titulo"));
        tabSheet.addTab(buildAba4UsuarioEmpresa(), getMensagens().getString("SignupView.tabPanel.aba4.titulo"));
        
        return tabSheet;
    }

    /**
     * Cria e retorna a 1a. aba com os campos para o cadastro de usuario
     * @TODO: Fernando
     * @return aba de cadastro de usuários
     */
    private VerticalLayout buildAba1CadastroUsuario(){
        
        VerticalLayout containerAba1 = new VerticalLayout();
        containerAba1.setMargin(true);
        containerAba1.setSpacing(true);
       
  
        
        // text field: Nome
        setNomeTextField(new TextField());
        containerAba1.addComponent(getNomeTextField());
        getNomeTextField().setInputPrompt(getMensagens().getString("SignupView.nomeTextField.label"));
        // text field: Sobrenome
        setSobrenomeTextField(new TextField());
        containerAba1.addComponent(getSobrenomeTextField());
        getSobrenomeTextField().setInputPrompt(getMensagens().getString("SignupView.sobrenomeTextField.label"));
        
         // text field: Senha
        setSenhaTextField(new TextField());
        containerAba1.addComponent(getSenhaTextField());
        getSenhaTextField().setInputPrompt(getMensagens().getString("SignupView.senhaTextField.label"));
        
        // check box : aceita termos
        setAceitaTermosCheckBox(new CheckBox(getMensagens().getString("SignupView.aceitaTermosCheckBox.label")));
        containerAba1.addComponent(getAceitaTermosCheckBox());
        
           return containerAba1;
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
        VerticalLayout containerAba3 = new VerticalLayout();
        containerAba3.setMargin(true);
        containerAba3.setSpacing(true);
       
          // text field: Razao Social
        setRazaoSocialTextField(new TextField());
        containerAba3.addComponent(getRazaoSocialTextField());
        getRazaoSocialTextField().setInputPrompt(getMensagens().getString("SignupView.razaoSocialTextField.label"));

        // text field: Nome Fantasia
        setNomeFantasiaTextField(new TextField());
        containerAba3.addComponent(getNomeFantasiaTextField());
        getNomeFantasiaTextField().setInputPrompt(getMensagens().getString("SignupView.nomeFantasiaTextField.label"));
        
         // text field: Cnpj
        setCnpjTextField(new TextField());
        containerAba3.addComponent(getCnpjTextField());
        getCnpjTextField().setInputPrompt(getMensagens().getString("SignupView.cnpjTextField.label"));
        
        // text field: Endereço
        setEnderecoTextField(new TextField());
        containerAba3.addComponent(getEnderecoTextField());
        getEnderecoTextField().setInputPrompt(getMensagens().getString("SignupView.enderecoTextField.label"));
        
           return containerAba3;
    }
    
    /**
     * Cria e retorna a 4a. aba com os campos para adicionar novos usuários as empresas
     * @TODO: Fernando
     * @return aba de cadastro de usuário -> empresa
     */
    private VerticalLayout buildAba4UsuarioEmpresa(){
        VerticalLayout containerAba4 = new VerticalLayout();
        containerAba4.setMargin(true);
        containerAba4.setSpacing(true);
       
          // text field: Razao Social
        setNomeTextField(new TextField());
        containerAba4.addComponent(getNomeTextField());
        getNomeTextField().setInputPrompt(getMensagens().getString("SignupView.nomeTextField.label"));

        // text field: Nome Fantasia
        setEmailTextField(new TextField());
        containerAba4.addComponent(getEmailTextField());
        getEmailTextField().setInputPrompt(getMensagens().getString("SignupView.emailTextField.label"));
        
         // text field: Cnpj
        setConfirmaEmailTextField(new TextField());
        containerAba4.addComponent(getConfirmaEmailTextField());
        getConfirmaEmailTextField().setInputPrompt(getMensagens().getString("SignupView.confirmaEmailTextField.label"));
        
        return containerAba4;
    }
    
    
    /**
     * @return the mensagens
     */
    public ResourceBundle getMensagens() {
        return mensagens;
    }

    /**
     * @param mensagens the mensagens to set
     */
    public void setMensagens(ResourceBundle mensagens) {
        this.mensagens = mensagens;
    }

    /**
     * @return the listener
     */
    public SignupViewListener getListener() {
        return listener;
    }

    /**
     * @return the nomeTextField
     */
    public TextField getNomeTextField() {
        return nomeTextField;
    }

    /**
     * @param nomeTextField the nomeTextField to set
     */
    public void setNomeTextField(TextField nomeTextField) {
        this.nomeTextField = nomeTextField;
    }

    /**
     * @return the sobrenomeTextField
     */
    public TextField getSobrenomeTextField() {
        return sobrenomeTextField;
    }

    /**
     * @param sobrenomeTextField the sobrenomeTextField to set
     */
    public void setSobrenomeTextField(TextField sobrenomeTextField) {
        this.sobrenomeTextField = sobrenomeTextField;
    }

    /**
     * @return the senhaTextField
     */
    public TextField getSenhaTextField() {
        return senhaTextField;
    }

    /**
     * @param senhaTextField the senhaTextField to set
     */
    public void setSenhaTextField(TextField senhaTextField) {
        this.senhaTextField = senhaTextField;
    }

    /**
     * @return the aceitaTermosCheckBox
     */
    public CheckBox getAceitaTermosCheckBox() {
        return aceitaTermosCheckBox;
    }

    /**
     * @param aceitaTermosCheckBox the aceitaTermosCheckBox to set
     */
    public void setAceitaTermosCheckBox(CheckBox aceitaTermosCheckBox) {
        this.aceitaTermosCheckBox = aceitaTermosCheckBox;
    }

    /**
     * @return the razaoSocialTextField
     */
    public TextField getRazaoSocialTextField() {
        return razaoSocialTextField;
    }

    /**
     * @param razaoSocialTextField the razaoSocialTextField to set
     */
    public void setRazaoSocialTextField(TextField razaoSocialTextField) {
        this.razaoSocialTextField = razaoSocialTextField;
    }

    /**
     * @return the nomeFantasiaTextField
     */
    public TextField getNomeFantasiaTextField() {
        return nomeFantasiaTextField;
    }

    /**
     * @param nomeFantasiaTextField the nomeFantasiaTextField to set
     */
    public void setNomeFantasiaTextField(TextField nomeFantasiaTextField) {
        this.nomeFantasiaTextField = nomeFantasiaTextField;
    }

    /**
     * @return the cnpjTextField
     */
    public TextField getCnpjTextField() {
        return cnpjTextField;
    }

    /**
     * @param cnpjTextField the cnpjTextField to set
     */
    public void setCnpjTextField(TextField cnpjTextField) {
        this.cnpjTextField = cnpjTextField;
    }

    /**
     * @return the enderecoTextField
     */
    public TextField getEnderecoTextField() {
        return enderecoTextField;
    }

    /**
     * @param enderecoTextField the enderecoTextField to set
     */
    public void setEnderecoTextField(TextField enderecoTextField) {
        this.enderecoTextField = enderecoTextField;
    }

    /**
     * @return the nomeUsuarioTextField
     */
    public TextField getNomeUsuarioTextField() {
        return nomeUsuarioTextField;
    }

    /**
     * @param nomeUsuarioTextField the nomeUsuarioTextField to set
     */
    public void setNomeUsuarioTextField(TextField nomeUsuarioTextField) {
        this.nomeUsuarioTextField = nomeUsuarioTextField;
    }

    /**
     * @return the emailTextField
     */
    public TextField getEmailTextField() {
        return emailTextField;
    }

    /**
     * @param emailTextField the emailTextField to set
     */
    public void setEmailTextField(TextField emailTextField) {
        this.emailTextField = emailTextField;
    }

    /**
     * @return the confirmaEmailTextField
     */
    public TextField getConfirmaEmailTextField() {
        return confirmaEmailTextField;
    }

    /**
     * @param confirmaEmailTextField the confirmaEmailTextField to set
     */
    public void setConfirmaEmailTextField(TextField confirmaEmailTextField) {
        this.confirmaEmailTextField = confirmaEmailTextField;
    }

    /**
     * Executa os metodos de validações dos campos de preenchimento obrigatorio
     * @TODO: fernando
     */
    public void validate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    /**
     * Exibe uma mensagem de erro indicando que este login (email) já 
     * existe no sistema e pergunta ao usuário se ele não quer recuperar sua senha
     * 
     * @TODO: fernando
     * @param chave chave do resource
     */
    public void apresentaAviso(String chave) {
        mensagemAviso.setValue(mensagens.getString(chave));
    }

    /**
     * Retorna o tipo de pessoa (Fisica/Jurica) da conta (empresa)
     * 
     * @return char F / J
     * @TODO: fernando
     */
    public char getTipoPessoa() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Retorna o cpf da empresa (conta) se pessoa fisica
     * 
     * @return char F / J
     * @TODO: fernando
     */
    public TextField getCpfTextField() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
