package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;


import java.text.MessageFormat;

import java.util.ResourceBundle;

/**
 * Janela de criação de nova conta, com dados do usuáiro, billing, empresas, etc.
 *
 * @author Rodrigo / Fernando
 */
public class SignupView extends Window {
//teste commit
    // Referencia ao recurso das mensagens:
    private ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getUserData().getMensagens();

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
    private TextField emailUsuarioTextField;
    private TextField confirmaEmailUsuarioTextField;
    private TextField senhaTextField;
    private CheckBox aceitaTermosCheckBox;
    
    
    
    // ABA #2 : Dados de billing (será feito fururamente)
    
    // ABA #3 : Dados de empresas e filiais
     private TextField razaoSocialTextField;
     private TextField nomeFantasiaTextField;
     private OptionGroup tipoPessoaOptionGroup;
     private TextField cnpjCpfTextField;
     private TextField logradouroTextField;
     private TextField numeroTextField;
     private TextField complementoTextField;
     private TextField bairroTextField;
     private ComboBox cidadeComboBox;
     private ComboBox estadoComboBox;
     private TextField cepTextField;
     private Table empresasTable;
     private CheckBox empresaAtivaCheckBox;
     

    // ABA #4 : Adicionar mais usuários à empresa
     private TextField nomeUsuarioTextField;
     private TextField sobrenomeUsuarioTextField;
     private TextField emailTextField;
     private TextField confirmaEmailTextField;
     private Table usuariosTable;
     private CheckBox usuarioAdmCheckBox;
     
    //ABA #5: Adicionar Empresas Coligadas
    private TextField nomeColigadaTextField;
    private TextField cnpjColigadaTextField;
   
    
    /**
     * Cria o pop-up de login, com campos para usuário e senha
     *
     */
    public SignupView() {
        super();

        setCaption(mensagens.getString("SignupView.titulo"));
        setModal(true);
        setWidth(500, Unit.PIXELS);
        setHeight(500, Unit.PIXELS);

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
        nomeTextField.setWidth("300px");
        
        // text field: Sobrenome
        setSobrenomeTextField(new TextField());
        containerAba1.addComponent(getSobrenomeTextField());
        getSobrenomeTextField().setInputPrompt(getMensagens().getString("SignupView.sobrenomeTextField.label"));
        sobrenomeTextField.setWidth("300px");
        
        // text field: Email
        setEmailUsuarioTextField(new TextField());
        containerAba1.addComponent(getEmailUsuarioTextField());
        getEmailUsuarioTextField().setInputPrompt(getMensagens().getString("SignupView.emailTextField.label"));
        emailUsuarioTextField.setWidth("300px");
        
        // text field: ConfirmaEmail
        setConfirmaEmailUsuarioTextField(new TextField());
        containerAba1.addComponent(getConfirmaEmailUsuarioTextField());
        getConfirmaEmailUsuarioTextField().setInputPrompt(getMensagens().getString("SignupView.confirmaEmailTextField.label"));
        confirmaEmailUsuarioTextField.setWidth("300px");
        
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
        razaoSocialTextField.setWidth("300px");

        // text field: Nome Fantasia
        setNomeFantasiaTextField(new TextField());
        containerAba3.addComponent(getNomeFantasiaTextField());
        getNomeFantasiaTextField().setInputPrompt(getMensagens().getString("SignupView.nomeFantasiaTextField.label"));
        nomeFantasiaTextField.setWidth("300px");
        
        HorizontalLayout containerHorizontal1 = new HorizontalLayout();
        containerHorizontal1.setSpacing(true); // coloca um espaçamento entre os elementos internos (30px)
        containerAba3.addComponent(containerHorizontal1); // adiciona o container de datas no superior

        setTipoPessoaOptionGroup(new OptionGroup());
        tipoPessoaOptionGroup.addItem("Pessoa Física");
        tipoPessoaOptionGroup.addItem("Pessoa Jurídica");
        containerHorizontal1.addComponent(tipoPessoaOptionGroup);
        
        
         // text field: Cnpj
        setCnpjCpfTextField(new TextField());
        containerHorizontal1.addComponent(getCnpjCpfTextField());
        getCnpjCpfTextField().setInputPrompt(getMensagens().getString("SignupView.cnpjCpfTextField.label"));
        cnpjCpfTextField.setWidth("180px");
        
        // text field: Lograoudo
        setLogradouroTextField(new TextField());
        containerAba3.addComponent(getLogradouroTextField());
        getLogradouroTextField().setInputPrompt(getMensagens().getString("SignupView.logradouroTextField.label"));
        logradouroTextField.setWidth("300px");
        
        // text field: Numero
        setNumeroTextField(new TextField());
        containerAba3.addComponent(getNumeroTextField());
        getNumeroTextField().setInputPrompt(getMensagens().getString("SignupView.numeroTextField.label"));
        numeroTextField.setWidth("300px");
        
         // text field: Complemento
        setComplementoTextField(new TextField());
        containerAba3.addComponent(getComplementoTextField());
        getComplementoTextField().setInputPrompt(getMensagens().getString("SignupView.complementoTextField.label"));
        complementoTextField.setWidth("300px");
        
         // text field: bairro
        setBairroTextField(new TextField());
        containerAba3.addComponent(getBairroTextField());
        getBairroTextField().setInputPrompt(getMensagens().getString("SignupView.bairroTextField.label"));
        bairroTextField.setWidth("300px");
        
         // text field: cidade
        setCidadeComboBox(new ComboBox());
        containerAba3.addComponent(getCidadeComboBox());
        getCidadeComboBox().setInputPrompt(getMensagens().getString("SignupView.cidadeComboBox.label"));
        cidadeComboBox.setWidth("300px");
        
         // text field: estado
        setEstadoComboBox(new ComboBox());
        containerAba3.addComponent(getEstadoComboBox());
        getEstadoComboBox().setInputPrompt(getMensagens().getString("SignupView.estadoComboBox.label"));
        estadoComboBox.setWidth("300px");
        
        
        HorizontalLayout containerHorizontal = new HorizontalLayout();
        containerHorizontal.setSpacing(true); // coloca um espaçamento entre os elementos internos (30px)
        containerAba3.addComponent(containerHorizontal); // adiciona o container de datas no superior
        
        // text field: cep
        setCepTextField(new TextField());
        containerHorizontal.addComponent(getCepTextField());
        getCepTextField().setInputPrompt(getMensagens().getString("SignupView.cepTextField.label"));
        cepTextField.setWidth("300px");
        
        
        
        // botão para Confirmar
        final Button adicionarEmpresaButton = new Button(getMensagens().getString("SignupView.adicionarEmpresaButton.label"), (Button.ClickEvent event) -> {
            getListener().okButtonClicked();
        });
        
        containerHorizontal.addComponent(adicionarEmpresaButton);
        adicionarEmpresaButton.addStyleName("small default");
        
                        
         // check box : empresa Ativa
        setEmpresaAtivaCheckBox(new CheckBox(getMensagens().getString("SignupView.empresaAtivaCheckBox.label")));
        containerAba3.addComponent(getEmpresaAtivaCheckBox());
        
        TabSheet tabSheetColigadas = new TabSheet();
        
        tabSheetColigadas.addTab(buildAba5Coligadas(), getMensagens().getString("SignupView.tabPanel.aba1.coligadas"));
        tabSheetColigadas.addTab(buildAba5Coligadas(), getMensagens().getString("SignupView.tabPanel.aba1.filiais"));
        
        containerAba3.addComponent(tabSheetColigadas);
        
        
        
           return containerAba3;
    }
    private VerticalLayout buildAba5Coligadas(){
        VerticalLayout containerAba5 = new VerticalLayout();
        containerAba5.setMargin(true);
        containerAba5.setSpacing(true);
        
        // text field: nomeColigada
        setNomeColigadaTextField(new TextField());
        containerAba5.addComponent(getNomeColigadaTextField());
        getNomeColigadaTextField().setInputPrompt(getMensagens().getString("SignupView.nomeColigadaTextField.label"));
        nomeColigadaTextField.setWidth("300px");
        
         // text field: cnpjColigada
        setCnpjColigadaTextField(new TextField());
        containerAba5.addComponent(getCnpjColigadaTextField());
        getCnpjColigadaTextField().setInputPrompt(getMensagens().getString("SignupView.cnpjColigadaTextField.label"));
        cnpjColigadaTextField.setWidth("300px");
        
        empresasTable = new Table();
        containerAba5.addComponent(empresasTable);
        empresasTable.setSizeFull();
        
        empresasTable.addContainerProperty("Cod", Integer.class, null);
        empresasTable.addContainerProperty("Nome", String.class, null);
        empresasTable.addContainerProperty("CNPJ", String.class, null);
        empresasTable.addContainerProperty("Editar", String.class, null);
        empresasTable.addContainerProperty("Remover", String.class, null);
        
        return containerAba5;
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
       
          // text field: Nome
        setNomeUsuarioTextField(new TextField());
        containerAba4.addComponent(getNomeUsuarioTextField());
        getNomeUsuarioTextField().setInputPrompt(getMensagens().getString("SignupView.nomeTextField.label"));
        nomeUsuarioTextField.setWidth("300px");

        // text field: Sobrenome
        setSobrenomeUsuarioTextField(new TextField());
        containerAba4.addComponent(getSobrenomeUsuarioTextField());
        getSobrenomeUsuarioTextField().setInputPrompt(getMensagens().getString("SignupView.sobrenomeTextField.label"));
        sobrenomeUsuarioTextField.setWidth("300px");
        
        // text field: E-mail
        setEmailTextField(new TextField());
        containerAba4.addComponent(getEmailTextField());
        getEmailTextField().setInputPrompt(getMensagens().getString("SignupView.emailTextField.label"));
        emailTextField.setWidth("300px");
        
        HorizontalLayout containerHorizontal = new HorizontalLayout();
        containerHorizontal.setSpacing(true); // coloca um espaçamento entre os elementos internos (30px)
        containerAba4.addComponent(containerHorizontal); // adiciona o container de datas no superior
        
         // text field: Confirma e-mail
        setConfirmaEmailTextField(new TextField());
        containerHorizontal.addComponent(getConfirmaEmailTextField());
        getConfirmaEmailTextField().setInputPrompt(getMensagens().getString("SignupView.confirmaEmailTextField.label"));
        confirmaEmailTextField.setWidth("300px");
        
        
        // check box : usuario adm
        setUsuarioAdmCheckBox(new CheckBox(getMensagens().getString("SignupView.usuarioAdmCheckBox.label")));
        containerAba4.addComponent(getUsuarioAdmCheckBox());
        
         // botão para Confirmar
        final Button adicionarUsuarioButton = new Button(getMensagens().getString("SignupView.adicionarUsuarioButton.label"), (Button.ClickEvent event) -> {
            getListener().okButtonClicked();
        });
        
        containerHorizontal.addComponent(adicionarUsuarioButton);
        adicionarUsuarioButton.addStyleName("small default");
        
        usuariosTable = new Table();
        containerAba4.addComponent(usuariosTable);
        usuariosTable.setSizeFull();
        
        usuariosTable.addContainerProperty("Cod", Integer.class, null);
        usuariosTable.addContainerProperty("Nome", String.class, null);
        usuariosTable.addContainerProperty("E-mail", String.class, null);
        usuariosTable.addContainerProperty("Administrador", String.class, null);
        usuariosTable.addContainerProperty("Editar", String.class, null);
        usuariosTable.addContainerProperty("Remover", String.class, null);
          
         
        return containerAba4;
    }
    
    
    public String getNome() {
        return getNomeTextField().getValue();
    }

    public String getSobrenome() {
        return getSobrenomeTextField().getValue();
    }

    public String getSenha() {
        return getSenhaTextField().getValue();
    }

    public CheckBox getAceitaTermos() {
        return getAceitaTermosCheckBox();
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
     * @return the emailUsuarioTextField
     */
    public TextField getEmailUsuarioTextField() {
        return emailUsuarioTextField;
    }

    /**
     * @param emailUsuarioTextField the emailUsuarioTextField to set
     */
    public void setEmailUsuarioTextField(TextField emailUsuarioTextField) {
        this.emailUsuarioTextField = emailUsuarioTextField;
    }
    
     /**
     * @return the confirmaEmailUsuarioTextField
     */
    public TextField getConfirmaEmailUsuarioTextField() {
        return confirmaEmailUsuarioTextField;
    }

    /**
     * @param confirmaEmailUsuarioTextField the confirmaEmailUsuarioTextField to set
     */
    public void setConfirmaEmailUsuarioTextField(TextField confirmaEmailUsuarioTextField) {
        this.confirmaEmailUsuarioTextField = confirmaEmailUsuarioTextField;
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
     * @return the tipoPessoaOptionGroup
     */
    public OptionGroup getTipoPessoaOptionGroup() {
        return tipoPessoaOptionGroup;
    }

    /**
     * @param tipoPessoaOptionGroup the tipoPessoaOptionGroup to set
     */
    public void setTipoPessoaOptionGroup(OptionGroup tipoPessoaOptionGroup) {
        this.tipoPessoaOptionGroup = tipoPessoaOptionGroup;
    }
    
    /**
     * @return the cnpjTextField
     */
    public TextField getCnpjCpfTextField() {
        return cnpjCpfTextField;
    }

    /**
     * @param cnpjTextField the cnpjTextField to set
     */
    public void setCnpjCpfTextField(TextField cnpjTextField) {
        this.cnpjCpfTextField = cnpjTextField;
    }

    /**
     * @return the enderecoTextField
     */
    public TextField getLogradouroTextField() {
        return logradouroTextField;
    }

    /**
     * @param enderecoTextField the enderecoTextField to set
     */
    public void setLogradouroTextField(TextField enderecoTextField) {
        this.logradouroTextField = enderecoTextField;
    }
    
    /**
     * @return the numeroTextField
     */
    public TextField getNumeroTextField() {
        return numeroTextField;
    }

    /**
     * @param numeroTextField the enderecoTextField to set
     */
    public void setNumeroTextField(TextField numeroTextField) {
        this.numeroTextField = numeroTextField;
    }
    
       /**
     * @return the complementoTextField
     */
    public TextField getComplementoTextField() {
        return complementoTextField;
    }

    /**
     * @param complementoTextField the enderecoTextField to set
     */
    public void setComplementoTextField(TextField complementoTextField) {
        this.complementoTextField = complementoTextField;
    }
    
        /**
     * @return the bairroTextField
     */
    public TextField getBairroTextField() {
        return bairroTextField;
    }

    /**
     * @param bairroTextField the enderecoTextField to set
     */
    public void setBairroTextField(TextField bairroTextField) {
        this.bairroTextField = bairroTextField;
    }
    
         /**
     * @return the cidadeComboBox
     */
    public ComboBox getCidadeComboBox() {
        return cidadeComboBox;
    }

    /**
     * @param cidadeComboBox the cidadeComboBox to set
     */
    public void setCidadeComboBox(ComboBox cidadeComboBox) {
        this.cidadeComboBox = cidadeComboBox;
    }
    
          /**
     * @return the estadoComboBox
     */
    public ComboBox getEstadoComboBox() {
        return estadoComboBox;
    }

    /**
     * @param estadoComboBox the cidadeComboBox to set
     */
    public void setEstadoComboBox(ComboBox estadoComboBox) {
        this.estadoComboBox = estadoComboBox;
    }
    
          /**
     * @return the cepTextField
     */
    public TextField getCepTextField() {
        return cepTextField;
    }

    /**
     * @param cepTextField the cepTextField to set
     */
    public void setCepTextField(TextField cepTextField) {
        this.cepTextField = cepTextField;
    }
    
    /**
     * @return the nomeColigadaTextField
     */
    public TextField getNomeColigadaTextField() {
        return nomeColigadaTextField;
    }

    /**
     * @param nomeColigadaTextField the nomeColigadaTextField to set
     */
    public void setNomeColigadaTextField(TextField nomeColigadaTextField) {
        this.nomeColigadaTextField = nomeColigadaTextField;
    }
    
    /**
     * @return the cnpjColigadaTextField
     */
    public TextField getCnpjColigadaTextField() {
        return cnpjColigadaTextField;
    }

    /**
     * @param cnpjColigadaTextField the cnpjColigadaTextField to set
     */
    public void setCnpjColigadaTextField(TextField cnpjColigadaTextField) {
        this.cnpjColigadaTextField = cnpjColigadaTextField;
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
     * @return the sobrenomeUsuarioTextField
     */
    public TextField getSobrenomeUsuarioTextField() {
        return sobrenomeUsuarioTextField;
    }

    /**
     * @param sobrenomeUsuarioTextField the sobrenomeUsuarioTextField to set
     */
    public void setSobrenomeUsuarioTextField(TextField sobrenomeUsuarioTextField) {
        this.sobrenomeUsuarioTextField = sobrenomeUsuarioTextField;
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
     * @return the usuarioAdmCheckBox
     */
    public CheckBox getUsuarioAdmCheckBox() {
        return usuarioAdmCheckBox;
    }

    /**
     * @param usuarioAdmCheckBox the usuarioAdmCheckBox to set
     */
    public void setUsuarioAdmCheckBox(CheckBox usuarioAdmCheckBox) {
        this.usuarioAdmCheckBox = usuarioAdmCheckBox;
    }
    
    /**
     * @return the empresaAtivaCheckBox
     */
    public CheckBox getEmpresaAtivaCheckBox() {
        return empresaAtivaCheckBox;
    }

    /**
     * @param empresaAtivaCheckBox the empresaAtivaCheckBox to set
     */
    public void setEmpresaAtivaCheckBox(CheckBox empresaAtivaCheckBox) {
        this.empresaAtivaCheckBox = empresaAtivaCheckBox;
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
     * @param chave chave do resource
     */
    private Label mensagemAviso = new Label();
    public void apresentaAviso(String chave, Object ... params) {
        String mensagem = MessageFormat.format(mensagens.getString(chave), params);
        mensagemAviso.setValue(mensagem);
    }


    


    
}