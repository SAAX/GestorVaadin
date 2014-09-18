package com.saax.gestorweb.view;
//teste de commit -> volta
import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.util.GestorWebImagens;
import com.vaadin.data.Property;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.UserError;
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
 * Janela de criaÃ§Ã£o de nova conta, com dados do usuÃ¡iro, billing, empresas, etc.
 *
 * @author Rodrigo / Fernando
 */
public class SignupView extends Window {

    // Referencia ao recurso das mensagens:
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();

    // A view mantem acesso ao listener (Presenter) para notificar os eventos
    // Este acesso se dÃ¡ por uma interface para manter a abstraÃ§Ã£o das camadas
    private SignupViewListener listener;

    public void setListener(SignupViewListener listener) {
        this.listener = listener;
    }

    // componentes visuais da view
    
    // ABA #1 : Dados do usuÃ¡rio
    private TextField nomeTextField;
    private TextField sobrenomeTextField;
    private TextField emailUsuarioTextField;
    private TextField confirmaEmailUsuarioTextField;
    private TextField senhaTextField;
    private CheckBox aceitaTermosCheckBox;
    
    
    
    // ABA #2 : Dados de billing (serÃ¡ feito fururamente)
    
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
     

    // ABA #4 : Adicionar mais usuÃ¡rios Ã  empresa
     private TextField nomeUsuarioTextField;
     private TextField sobrenomeUsuarioTextField;
     private TextField emailTextField;
     private TextField confirmaEmailTextField;
     private Table usuariosTable;
     private CheckBox usuarioAdmCheckBox;
     
    //ABA #5: Adicionar Empresas Coligadas
    private TextField nomeColigadaTextField;
    private TextField cnpjColigadaTextField;
    private CheckBox coligadaAtivaCheckBox;
    private Table coligadasTable;
    
    //ABA #6: Adicionar Empresas Filiais
    private TextField nomeFilialTextField;
    private TextField cnpjFilialTextField;
    private CheckBox filialAtivaCheckBox;
    private Table filiaisTable;
   
    
    /**
     * Cria o pop-up de login, com campos para usuÃ¡rio e senha
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
        
        // botÃ£o para Confirmar
        final Button okButton = new Button(getMensagens().getString("SignupView.okButton.label"), new Button.ClickListener() {
             @Override
            public void buttonClick(Button.ClickEvent event) {
            listener.okButtonClicked();
            }
        });
        
                      
        // botÃ£o para cancelar
        final Button cancelButton = new Button(getMensagens().getString("SignupView.cancelButton.label"), (Button.ClickEvent event) -> {
            getListener().cancelButtonClicked();
        });

        barraBotoes.addComponent(okButton);
        barraBotoes.addComponent(cancelButton);
        
        return barraBotoes;
    }
    
    
    /**
     * Cria o painel com ABAs que terÃ¡ os paineis de: <br>
     * 1. Cadastro do usuario <br>
     * 2. Billing <br>
     * 3. Cadastro da empresa (conta) <br>
     * 4. Adcionar mais usuÃ¡rios Ã  empresa <br>
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
     * @return aba de cadastro de usuÃ¡rios
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
        nomeTextField.setValidationVisible(false);
        getNomeTextField().addValidator(new StringLengthValidator(mensagens.getString("SignupView.nomeTextField.erro.nomeNaoInformado"),1, 100, false));

        
        
                
        // text field: Sobrenome
        setSobrenomeTextField(new TextField());
        containerAba1.addComponent(getSobrenomeTextField());
        getSobrenomeTextField().setInputPrompt(getMensagens().getString("SignupView.sobrenomeTextField.label"));
        sobrenomeTextField.setWidth("300px");
        sobrenomeTextField.setValidationVisible(false);
        getSobrenomeTextField().addValidator(new StringLengthValidator(mensagens.getString("SignupView.sobrenomeTextField.erro.nomeNaoInformado"),1, 100, false));
        
        // text field: Email
        setEmailUsuarioTextField(new TextField());
        containerAba1.addComponent(getEmailUsuarioTextField());
        getEmailUsuarioTextField().setInputPrompt(getMensagens().getString("SignupView.emailTextField.label"));
        emailUsuarioTextField.setWidth("300px");
        emailUsuarioTextField.setValidationVisible(false);
        getEmailUsuarioTextField().addValidator(new EmailValidator(mensagens.getString("SignupView.emailUsuarioTextField.erro.emailNaoInformado")));
        
        // text field: ConfirmaEmail
        setConfirmaEmailUsuarioTextField(new TextField());
        containerAba1.addComponent(getConfirmaEmailUsuarioTextField());
        getConfirmaEmailUsuarioTextField().setInputPrompt(getMensagens().getString("SignupView.confirmaEmailTextField.label"));
        confirmaEmailUsuarioTextField.setWidth("300px");
        confirmaEmailUsuarioTextField.setValidationVisible(false);
        getConfirmaEmailUsuarioTextField().addValidator(new EmailValidator(mensagens.getString("SignupView.emailUsuarioTextField.erro.emailNaoInformado")));
        
         // text field: Senha
        setSenhaTextField(new TextField());
        containerAba1.addComponent(getSenhaTextField());
        getSenhaTextField().setInputPrompt(getMensagens().getString("SignupView.senhaTextField.label"));
        senhaTextField.setValidationVisible(false);
        getSenhaTextField().addValidator(new StringLengthValidator(mensagens.getString("SignupView.senhaTextField.erro.nomeNaoInformado"),1, 100, false));
        
        // check box : aceita termos
        setAceitaTermosCheckBox(new CheckBox(getMensagens().getString("SignupView.aceitaTermosCheckBox.label")));
        containerAba1.addComponent(getAceitaTermosCheckBox());
        aceitaTermosCheckBox.setValidationVisible(false);
        
           return containerAba1;
    }

    /**
     * Cria e retorna a 2a. aba com os campos para o billing
     * @TODO: SerÃ¡ feito futuramente
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
        getRazaoSocialTextField().addValidator(new StringLengthValidator(mensagens.getString("SignupView.razaoSocialTextField.erro.RazaoSocialNaoInformada"),1, 100, false));
        razaoSocialTextField.setValidationVisible(false);

        // text field: Nome Fantasia
        setNomeFantasiaTextField(new TextField());
        containerAba3.addComponent(getNomeFantasiaTextField());
        getNomeFantasiaTextField().setInputPrompt(getMensagens().getString("SignupView.nomeFantasiaTextField.label"));
        nomeFantasiaTextField.setWidth("300px");
        getNomeFantasiaTextField().addValidator(new StringLengthValidator(mensagens.getString("SignupView.nomeFantasiaTextField.erro.NomeFantasiaNaoInformado"),1, 100, false));
        nomeFantasiaTextField.setValidationVisible(false);
        
        HorizontalLayout containerHorizontal1 = new HorizontalLayout();
        containerHorizontal1.setSpacing(true); // coloca um espaÃ§amento entre os elementos internos (30px)
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
        
        cnpjCpfTextField.setValidationVisible(false);
         
        getCnpjCpfTextField().addValidator(new StringLengthValidator(mensagens.getString("SignupView.cnpjCpfTextField.erro.cnpjCpfNaoInformado"),1, 100, false));
        
        
        HorizontalLayout containerHorizontal = new HorizontalLayout();
        containerHorizontal.setSpacing(true); // coloca um espaÃ§amento entre os elementos internos (30px)
        containerAba3.addComponent(containerHorizontal); // adiciona o container de datas no superior
        
        // text field: Lograoudo
        setLogradouroTextField(new TextField());
        containerHorizontal.addComponent(getLogradouroTextField());
        getLogradouroTextField().setInputPrompt(getMensagens().getString("SignupView.logradouroTextField.label"));
        logradouroTextField.setWidth("300px");
        logradouroTextField.setValidationVisible(false);
        getLogradouroTextField().addValidator(new StringLengthValidator(mensagens.getString("SignupView.logradouroTextField.erro.logradouroNaoInformado"),1, 100, false));
        
        // text field: Numero
        setNumeroTextField(new TextField());
        containerHorizontal.addComponent(getNumeroTextField());
        getNumeroTextField().setInputPrompt(getMensagens().getString("SignupView.numeroTextField.label"));
        numeroTextField.setWidth("100px");
        numeroTextField.setValidationVisible(false);
        getNumeroTextField().addValidator(new StringLengthValidator(mensagens.getString("SignupView.numeroTextField.erro.numeroNaoInformado"),1, 100, false));
        
        HorizontalLayout containerHorizontal2 = new HorizontalLayout();
        containerHorizontal2.setSpacing(true); // coloca um espaÃ§amento entre os elementos internos (30px)
        containerAba3.addComponent(containerHorizontal2); // adiciona o container de datas no superior
        
         // text field: Complemento
        setComplementoTextField(new TextField());
        containerHorizontal2.addComponent(getComplementoTextField());
        getComplementoTextField().setInputPrompt(getMensagens().getString("SignupView.complementoTextField.label"));
        complementoTextField.setWidth("300px");
        complementoTextField.setValidationVisible(false);
        getNumeroTextField().addValidator(new StringLengthValidator(mensagens.getString("SignupView.numeroTextField.erro.numeroNaoInformado"),1, 100, false));
        
         // text field: bairro
        setBairroTextField(new TextField());
        containerHorizontal2.addComponent(getBairroTextField());
        getBairroTextField().setInputPrompt(getMensagens().getString("SignupView.bairroTextField.label"));
        bairroTextField.setWidth("100px");
        bairroTextField.setValidationVisible(false);
        getBairroTextField().addValidator(new StringLengthValidator(mensagens.getString("SignupView.bairroTextField.erro.bairroNaoInformado"),1, 100, false));
        
        // text field: estado
        setEstadoComboBox(new ComboBox());
        containerAba3.addComponent(getEstadoComboBox());
        getEstadoComboBox().setInputPrompt(getMensagens().getString("SignupView.estadoComboBox.label"));
        estadoComboBox.setWidth("300px");
        estadoComboBox.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                listener.estadoSelecionado();
            }
        });
        
          // text field: cidade
        setCidadeComboBox(new ComboBox());
        containerAba3.addComponent(getCidadeComboBox());
        getCidadeComboBox().setInputPrompt(getMensagens().getString("SignupView.cidadeComboBox.label"));
        
        cidadeComboBox.setWidth("300px");
        
        
              
        // text field: cep
        setCepTextField(new TextField());
        containerAba3.addComponent(getCepTextField());
        getCepTextField().setInputPrompt(getMensagens().getString("SignupView.cepTextField.label"));
        cepTextField.setWidth("300px");
        
                  
         
        
        TabSheet tabSheetColigadas = new TabSheet();
        
        tabSheetColigadas.addTab(buildAba5Coligadas(), getMensagens().getString("SignupView.tabPanel.aba1.coligadas"));
        tabSheetColigadas.addTab(buildAba6Filiais(), getMensagens().getString("SignupView.tabPanel.aba1.filiais"));
        
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
        
        HorizontalLayout containerHorizontal = new HorizontalLayout();
        containerHorizontal.setSpacing(true); // coloca um espaÃ§amento entre os elementos internos (30px)
        containerAba5.addComponent(containerHorizontal); // adiciona o container de datas no superior
        
        // botÃ£o para Confirmar
        final Button adicionarColigadaButton = new Button(getMensagens().getString("SignupView.adicionarColigadaButton.label"), new Button.ClickListener() {
             @Override
            public void buttonClick(Button.ClickEvent event) {
               listener.incluirColigadas();
            }
        });
        
        
        
        containerHorizontal.addComponent(adicionarColigadaButton);
        adicionarColigadaButton.addStyleName("small default");
        
        coligadasTable = new Table();
        containerAba5.addComponent(coligadasTable);
        coligadasTable.setPageLength(5);
        coligadasTable.setSizeFull();
        
      
        coligadasTable.addContainerProperty(getMensagens().getString("SignupView.coligadasTable.nome"), String.class, null);
        coligadasTable.addContainerProperty(getMensagens().getString("SignupView.coligadasTable.cnpj"), String.class, null);
        coligadasTable.addContainerProperty(getMensagens().getString("SignupView.coligadasTable.remover"), Button.class, null);
        coligadasTable.setImmediate(true);
        coligadasTable.setSelectable(true);
        
        return containerAba5;
    }
    
     private VerticalLayout buildAba6Filiais(){
        VerticalLayout containerAba6 = new VerticalLayout();
        containerAba6.setMargin(true);
        containerAba6.setSpacing(true);
        
        // text field: nomeFilial
        setNomeFilialTextField(new TextField());
        containerAba6.addComponent(getNomeFilialTextField());
        getNomeFilialTextField().setInputPrompt(getMensagens().getString("SignupView.nomeFilialTextField.label"));
        nomeFilialTextField.setWidth("300px");
        
         // text field: cnpjColigada
        setCnpjFilialTextField(new TextField());
        containerAba6.addComponent(getCnpjFilialTextField());
        getCnpjFilialTextField().setInputPrompt(getMensagens().getString("SignupView.cnpjFilialTextField.label"));
        cnpjFilialTextField.setWidth("300px");
        
         HorizontalLayout containerHorizontal = new HorizontalLayout();
        containerHorizontal.setSpacing(true); // coloca um espaÃ§amento entre os elementos internos (30px)
        containerAba6.addComponent(containerHorizontal); // adiciona o container de datas no superior
        
       
        
        // botÃ£o para Confirmar
        final Button adicionarFilialButton = new Button(getMensagens().getString("SignupView.adicionarFilialButton.label"), new Button.ClickListener() {
             @Override
            public void buttonClick(Button.ClickEvent event) {
               listener.incluirFiliais();
            }
        });
        
              
          
        
       
        
        containerHorizontal.addComponent(adicionarFilialButton);
        adicionarFilialButton.addStyleName("small default");
        
        
        filiaisTable = new Table();
        containerAba6.addComponent(filiaisTable);
        filiaisTable.setPageLength(5);
        
       
        filiaisTable.addContainerProperty(getMensagens().getString("SignupView.filiaisTable.nome"), String.class, null);
        filiaisTable.addContainerProperty(getMensagens().getString("SignupView.filiaisTable.cnpj"), String.class, null);
        filiaisTable.addContainerProperty(getMensagens().getString("SignupView.filiaisTable.remover"), Button.class, null);
        filiaisTable.setImmediate(true);
        filiaisTable.setSelectable(true);
        
        return containerAba6;
    }
    /**
     * Cria e retorna a 4a. aba com os campos para adicionar novos usuÃ¡rios as empresas
     * @TODO: Fernando
     * @return aba de cadastro de usuÃ¡rio -> empresa
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
        
        // text field: Confirma e-mail
        setConfirmaEmailTextField(new TextField());
        containerAba4.addComponent(getConfirmaEmailTextField());
        getConfirmaEmailTextField().setInputPrompt(getMensagens().getString("SignupView.confirmaEmailTextField.label"));
        confirmaEmailTextField.setWidth("300px");
        
        HorizontalLayout containerHorizontal = new HorizontalLayout();
        containerHorizontal.setSpacing(true); // coloca um espaÃ§amento entre os elementos internos (30px)
        containerAba4.addComponent(containerHorizontal); // adiciona o container de datas no superior
        
         
        
        
        // check box : usuario adm
        setUsuarioAdmCheckBox(new CheckBox(getMensagens().getString("SignupView.usuarioAdmCheckBox.label")));
        containerHorizontal.addComponent(getUsuarioAdmCheckBox());
        
         // botÃ£o para Confirmar
        
        
                
        final Button adicionarUsuarioButton = new Button(getMensagens().getString("SignupView.adicionarUsuarioButton.label"), new Button.ClickListener() {
          

            @Override
            public void buttonClick(Button.ClickEvent event) {
               listener.incluirUsuario();
            }
        });
        
       
        
        
        containerHorizontal.addComponent(adicionarUsuarioButton);
        adicionarUsuarioButton.addStyleName("small default");
        
        usuariosTable = new Table();
        containerAba4.addComponent(usuariosTable);
        usuariosTable.setPageLength(5);
        usuariosTable.setSizeFull();
            
        
        usuariosTable.addContainerProperty(getMensagens().getString("SignupView.usuariosTable.nome"), String.class, null);
        usuariosTable.addContainerProperty(getMensagens().getString("SignupView.usuariosTable.sobrenome"), String.class, null);
        usuariosTable.addContainerProperty(getMensagens().getString("SignupView.usuariosTable.email"), String.class, null);
        usuariosTable.addContainerProperty(getMensagens().getString("SignupView.usuariosTable.administrador"), String.class, null);
        //usuariosTable.addContainerProperty("Editar", Button.class, null);
        usuariosTable.addContainerProperty(getMensagens().getString("SignupView.usuariosTable.remover"), Button.class, null);
        usuariosTable.setImmediate(true);
        usuariosTable.setSelectable(true);
         
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
     * @return the usuariosTable
     */
    public Table getUsuariosTable() {
        return usuariosTable;
    }

    /**
     * @param usuariosTable the UsuariosTable to set
     */
    public void setUsuariosTable(Table usuariosTable) {
        this.usuariosTable = usuariosTable;
    }
    
    /**
     * @return the coligadasTable
     */
    public Table getColigadasTable() {
        return coligadasTable;
    }

    /**
     * @param coligadasTable the ColigadasTable to set
     */
    public void setColigadasTable(Table coligadasTable) {
        this.coligadasTable = coligadasTable;
    }
    
    /**
     * @return the filiaisTable
     */
    public Table getFiliaisTable() {
        return filiaisTable;
    }

    /**
     * @param filiaisTable the FiliaisTable to set
     */
    public void setFiliaisTable(Table filiaisTable) {
        this.filiaisTable = filiaisTable;
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
     * @return the nomeFilialTextField
     */
    public TextField getNomeFilialTextField() {
        return nomeFilialTextField;
    }

    /**
     * @param nomeFilialTextField the nomeFilialTextField to set
     */
    public void setNomeFilialTextField(TextField nomeFilialTextField) {
        this.nomeFilialTextField = nomeFilialTextField;
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
     * @return the cnpjFilialTextField
     */
    public TextField getCnpjFilialTextField() {
        return cnpjFilialTextField;
    }

    /**
     * @param cnpjFilialTextField the cnpjFilialTextField to set
     */
    public void setCnpjFilialTextField(TextField cnpjFilialTextField) {
        this.cnpjFilialTextField = cnpjFilialTextField;
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
     * @return the coligadaAtivaCheckBox
     */
    public CheckBox getColigadaAtivaCheckBox() {
        return coligadaAtivaCheckBox;
    }

    /**
     * @param coligadaAtivaCheckBox the coligadaAtivaCheckBox to set
     */
    public void setColigadaAtivaCheckBox(CheckBox coligadaAtivaCheckBox) {
        this.coligadaAtivaCheckBox = coligadaAtivaCheckBox;
    }
    
    /**
     * @return the filialAtivaCheckBox
     */
    public CheckBox getFilialAtivaCheckBox() {
        return filialAtivaCheckBox;
    }

    /**
     * @param filialAtivaCheckBox the filialAtivaCheckBox to set
     */
    public void setFilialAtivaCheckBox(CheckBox filialAtivaCheckBox) {
        this.filialAtivaCheckBox = filialAtivaCheckBox;
    }

    /**
     * Executa os metodos de validaÃ§Ãµes dos campos de preenchimento obrigatorio
     * @TODO: fernando
     */
    public void validate() {
     nomeTextField.setValidationVisible(true);
     sobrenomeTextField.setValidationVisible(true);
     emailUsuarioTextField.setValidationVisible(true);
     confirmaEmailUsuarioTextField.setValidationVisible(true);
     senhaTextField.setValidationVisible(true);
     aceitaTermosCheckBox.setValidationVisible(true);
        
     //nomeTextField.validate();
     //sobrenomeTextField.validate();
     //emailUsuarioTextField.validate();
     //confirmaEmailUsuarioTextField.validate();
     //senhaTextField.validate();
     //aceitaTermosCheckBox.validate();
        
        
     razaoSocialTextField.setValidationVisible(true);
     nomeFantasiaTextField.setValidationVisible(true);
     tipoPessoaOptionGroup.setValidationVisible(true);
     cnpjCpfTextField.setValidationVisible(true);
     logradouroTextField.setValidationVisible(true);
     numeroTextField.setValidationVisible(true);
     complementoTextField.setValidationVisible(true);
     bairroTextField.setValidationVisible(true);
     cidadeComboBox.setValidationVisible(true);
     estadoComboBox.setValidationVisible(true);
     cepTextField.setValidationVisible(true);
     
     razaoSocialTextField.validate();
     nomeFantasiaTextField.validate();
     tipoPessoaOptionGroup.validate();
     cnpjCpfTextField.validate();
     logradouroTextField.validate();
     numeroTextField.validate();
     complementoTextField.validate();
     bairroTextField.validate();
     cidadeComboBox.validate();
     estadoComboBox.validate();
     cepTextField.validate();
     
     
     
     
        
        
    }

    
    /**
     * Exibe uma mensagem de erro indicando que este login (email) jÃ¡ 
     * existe no sistema e pergunta ao usuÃ¡rio se ele nÃ£o quer recuperar sua senha
     * 
     * @param chave chave do resource
     */
    private Label mensagemAviso = new Label();
    public void apresentaAviso(String chave, Object ... params) {
        String mensagem = MessageFormat.format(mensagens.getString(chave), params);
        mensagemAviso.setValue(mensagem);
    }
    
    public void apresentaErroUsuarioExistente(String chave, Object ... params) {
        String mensagem = MessageFormat.format(mensagens.getString(chave), params);
        nomeFilialTextField.setComponentError(new UserError(mensagem));        
    }




    
}
