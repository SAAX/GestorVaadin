package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.util.GestorWebImagens;
import com.vaadin.data.Property;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.ResourceBundle;

/**
 * New account creation window, with user data, billing, business, etc.
 *
 * @author Rodrigo / Fernando
 */
public class SignupView extends Window {

    // Reference to the use of the messages:
    private final transient ResourceBundle messages = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final transient GestorWebImagens images = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();

    // The view maintains access to the listener (Presenter) to notify events
    // This access is by Gives an interface to maintain the abstraction layers
    private SignupViewListener listener;

    public void setListener(SignupViewListener listener) {
        this.listener = listener;
    }

    // Visual components of view
    // Tab 1: User Data
    private TextField nameTextField;
    private TextField surnameTextField;
    private TextField userEmailTextField;
    private TextField confirmeUserEmailTextField;
    private PasswordField passwordTextField;
    private PasswordField confirmationPasswordTextField;
    private CheckBox acceptTermsCheckBox;

    // Tab 2: billing data (will be done in the future)
    // Tab 3: Data companies and subsidiaries
    private TextField companyNameTextField;
    private OptionGroup personTypeOptionGroup;
    private TextField nationalEntityRegistrationCodeTextField;

    // Tab 4: Add more users to the company
    private TextField userNameTextField;
    private TextField userSurnameTextField;
    private TextField emailTextField;
    private TextField emailConfirmTextField;
    private Table usersTable;
    private CheckBox userAdmCheckBox;

    // Tab 5: Add Associated Companies
    private TextField associatedNameTextField;
    private TextField nationalEntityRegistrationAssociatedTextField;
    private Table associatedTable;

    // Tab 6: Add Subsidiaries
    private TextField subsidiaryNameTextField;
    private TextField nationalEntityRegistrationSubsidiaryTextField;
    private CheckBox activeSubsidiaryCheckBox;
    private Table subsidiariesTable;

    /**
     * Creates the pop-up login with fields for username and password
     *
     * @param isUsuarioAdm
     */
    public SignupView(boolean isUsuarioAdm) {
        super();

        setCaption(messages.getString("SignupView.titulo"));
        setModal(true);
        setWidth(500, Unit.PIXELS);
        setHeight(500, Unit.PIXELS);

        // Container that stores the visual elements (login and password fields)
        VerticalLayout container = new VerticalLayout();
        container.setMargin(true);
        setContent(container);

        // Add: visual components
        container.addComponent(buildTabPanel(isUsuarioAdm));

        // Buttons Bar
        HorizontalLayout barraBotoes = buildBarraBotoes();
        container.addComponent(barraBotoes);
        container.setComponentAlignment(barraBotoes, Alignment.MIDDLE_CENTER);

        center();
    }

    /**
     * Creates and returns the buttons bar
     *
     * @return bar with the buttons OK and Cancel
     */
    private HorizontalLayout buildBarraBotoes() {

        HorizontalLayout barraBotoes = new HorizontalLayout();

        final Button okButton = new Button(getMessages().getString("SignupView.okButton.label"), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                listener.okButtonClicked();
            }
        });

        final Button cancelButton = new Button(getMessages().getString("SignupView.cancelButton.label"), (Button.ClickEvent event) -> {
            getListener().cancelButtonClicked();
        });

        barraBotoes.addComponent(okButton);
        barraBotoes.addComponent(cancelButton);

        return barraBotoes;
    }

    /**
     * Creates the tabbed panel that will have the panels of: <br>
     * 1. User's registration <br>
     * 2. Billing <br>
     * 3. Registration of the company (account) <br>
     * 4. Later add more users to the company <br>
     */
    private TabSheet buildTabPanel(boolean isUsuarioAdm) {

        TabSheet tabSheet = new TabSheet();

        tabSheet.addTab(buildAba1CadastroUsuario(), getMessages().getString("SignupView.tabPanel.aba1.titulo"));
        //tabSheet.addTab(buildAba2Billing(), messages.getString("SignupView.tabPanel.aba2.titulo"));
        tabSheet.addTab(buildAba3CadastroEmpresas(), getMessages().getString("SignupView.tabPanel.aba3.titulo"));
        tabSheet.addTab(buildAba4UsuarioEmpresa(), getMessages().getString("SignupView.tabPanel.aba4.titulo"));

        if (!isUsuarioAdm) {
            tabSheet.getTab(1).setEnabled(false); //2
            tabSheet.getTab(2).setEnabled(false); //3
        }

        return tabSheet;
    }
    private final Label labelNome = new Label("Nome");
    private final Label labelSobrenome = new Label("Sobrenome");
    private final Label labelEmail = new Label("E-mail");
    private final Label labelConfirmEmail = new Label("Confirma E-mail");
    private final Label labelPasswd = new Label("Senha");
    private final Label labelConfirmPasswd = new Label("Confirma Senha");

    private final VerticalLayout containerLabel = new VerticalLayout();

    /**
     * Creates and returns to first. tab with the fields for User registration
     *
     * @return User registration tab
     */
    private HorizontalLayout buildAba1CadastroUsuario() {

        HorizontalLayout containerPrincipal = new HorizontalLayout();
        containerPrincipal.setSizeUndefined();

        VerticalLayout containerComponents = new VerticalLayout();
        containerComponents.setMargin(true);
        containerComponents.setSpacing(true);

        containerLabel.setSizeFull();
        containerLabel.setMargin(true);

        containerLabel.addComponent(labelNome);
        containerLabel.addComponent(labelSobrenome);
        containerLabel.addComponent(labelEmail);
        containerLabel.addComponent(labelConfirmEmail);
        containerLabel.addComponent(labelPasswd);
        containerLabel.addComponent(labelConfirmPasswd);

        setNameTextField(new TextField());
        nameTextField.setInputPrompt(getMessages().getString("SignupView.nomeTextField.label"));
        nameTextField.setWidth("300px");
        nameTextField.setValidationVisible(false);
        nameTextField.addValidator(new StringLengthValidator(messages.getString("SignupView.nomeTextField.erro.nomeNaoInformado"), 1, 100, false));
        containerComponents.addComponent(nameTextField);

        surnameTextField = new TextField();
        surnameTextField.setInputPrompt(getMessages().getString("SignupView.sobrenomeTextField.label"));
        surnameTextField.setWidth("300px");
        surnameTextField.setValidationVisible(false);
        surnameTextField.addValidator(new StringLengthValidator(messages.getString("SignupView.sobrenomeTextField.erro.nomeNaoInformado"), 1, 100, false));
        containerComponents.addComponent(surnameTextField);

        userEmailTextField = new TextField();
        userEmailTextField.setInputPrompt(getMessages().getString("SignupView.emailTextField.label"));
        userEmailTextField.setWidth("300px");
        userEmailTextField.setValidationVisible(false);
        userEmailTextField.addValidator(new EmailValidator(messages.getString("SignupView.emailUsuarioTextField.erro.emailNaoInformado")));
        containerComponents.addComponent(userEmailTextField);

        confirmeUserEmailTextField = new TextField();
        containerComponents.addComponent(getConfirmeUserEmailTextField());
        confirmeUserEmailTextField.setInputPrompt(getMessages().getString("SignupView.confirmaEmailTextField.label"));
        confirmeUserEmailTextField.setWidth("300px");
        confirmeUserEmailTextField.setValidationVisible(false);
        confirmeUserEmailTextField.addValidator(new EmailValidator(messages.getString("SignupView.emailUsuarioTextField.erro.emailNaoInformado")));
        containerComponents.addComponent(confirmeUserEmailTextField);

        passwordTextField = new PasswordField();
        containerComponents.addComponent(getPasswordTextField());
        passwordTextField.setInputPrompt(getMessages().getString("SignupView.senhaTextField.label"));
        passwordTextField.setValidationVisible(false);
        passwordTextField.addValidator(new StringLengthValidator(messages.getString("SignupView.senhaTextField.erro.nomeNaoInformado"), 1, 100, false));
        containerComponents.addComponent(passwordTextField);

        confirmationPasswordTextField = new PasswordField();
        containerComponents.addComponent(getConfirmationPasswordTextField());
        confirmationPasswordTextField.setInputPrompt(getMessages().getString("SignupView.senhaTextField.label"));
        confirmationPasswordTextField.setValidationVisible(false);
        confirmationPasswordTextField.addValidator(new StringLengthValidator(messages.getString("SignupView.senhaTextField.erro.nomeNaoInformado"), 1, 100, false));
        containerComponents.addComponent(confirmationPasswordTextField);

        setAcceptTermsCheckBox(new CheckBox(getMessages().getString("SignupView.aceitaTermosCheckBox.label")));
        containerComponents.addComponent(getAcceptTermsCheckBox());
        acceptTermsCheckBox.setValidationVisible(false);

        containerPrincipal.addComponent(containerLabel);
        containerPrincipal.addComponent(containerComponents);

        return containerPrincipal;
    }

    /**
     * Creates and returns the second. tab with fields for the billing
     *
     * @return tab with fields for the billing
     */
    private VerticalLayout buildAba2Billing() {
        return new VerticalLayout();
    }

    /**
     * Creates and returns the third. tab with the fields for the registration
     * of the company and its affiliates and subsidiaries
     *
     * @return registration tab companies
     */
    private VerticalLayout buildAba3CadastroEmpresas() {
        VerticalLayout containerAba3 = new VerticalLayout();
        containerAba3.setMargin(true);
        containerAba3.setSpacing(true);

        HorizontalLayout containerHorizontaLabelNome = new HorizontalLayout();
        containerHorizontaLabelNome.setSpacing(true);
        containerAba3.addComponent(containerHorizontaLabelNome);

        Label labelNome = new Label("Nome");
        containerHorizontaLabelNome.addComponent(labelNome);
        companyNameTextField = new TextField();
        containerHorizontaLabelNome.addComponent(companyNameTextField);
        containerHorizontaLabelNome.setComponentAlignment(labelNome, Alignment.MIDDLE_CENTER);
        companyNameTextField.setInputPrompt(getMessages().getString("SignupView.razaoSocialTextField.label"));
        companyNameTextField.setWidth("272px");
        companyNameTextField.addValidator(new StringLengthValidator(messages.getString("SignupView.razaoSocialTextField.erro.RazaoSocialNaoInformada"), 1, 100, false));
        companyNameTextField.setValidationVisible(false);

        setPersonTypeOptionGroup(new OptionGroup());
        personTypeOptionGroup.addItem("Pessoa Física");
        personTypeOptionGroup.addItem("Pessoa Jurídica");
        personTypeOptionGroup.addValueChangeListener((Property.ValueChangeEvent event) -> {
            //listener.personTypeSelected(event.getProperty().getValue());
        });

        HorizontalLayout containerHorizontal1 = new HorizontalLayout();
        containerHorizontal1.setSpacing(true);
        containerAba3.addComponent(containerHorizontal1);

        containerHorizontal1.addComponent(personTypeOptionGroup);
        setNationalEntityRegistrationCodeTextField(new TextField());
        containerHorizontal1.addComponent(getNationalEntityRegistrationCodeTextField());
        containerHorizontal1.setComponentAlignment(personTypeOptionGroup, Alignment.MIDDLE_CENTER);
        getNationalEntityRegistrationCodeTextField().setInputPrompt(getMessages().getString("SignupView.cnpjCpfTextField.label"));
        nationalEntityRegistrationCodeTextField.setWidth("180px");
        nationalEntityRegistrationCodeTextField.setValidationVisible(true);

        TabSheet tabSheetColigadas = new TabSheet();

        tabSheetColigadas.addTab(buildAba5Coligadas(), getMessages().getString("SignupView.tabPanel.aba1.coligadas"));
        tabSheetColigadas.addTab(buildAba6Filiais(), getMessages().getString("SignupView.tabPanel.aba1.filiais"));

        containerAba3.addComponent(tabSheetColigadas);

        return containerAba3;
    }

    private VerticalLayout buildAba5Coligadas() {
        VerticalLayout containerAba5 = new VerticalLayout();
        containerAba5.setMargin(true);
        containerAba5.setSpacing(true);

        setAssociatedNameTextField(new TextField());
        containerAba5.addComponent(getAssociatedNameTextField());
        getAssociatedNameTextField().setInputPrompt(getMessages().getString("SignupView.nomeColigadaTextField.label"));
        associatedNameTextField.setWidth("300px");

        setNationalEntityRegistrationAssociatedTextField(new TextField());
        containerAba5.addComponent(getNationalEntityRegistrationAssociatedTextField());
        getNationalEntityRegistrationAssociatedTextField().setInputPrompt(getMessages().getString("SignupView.cnpjColigadaTextField.label"));
        nationalEntityRegistrationAssociatedTextField.setWidth("300px");

        HorizontalLayout containerHorizontal = new HorizontalLayout();
        containerHorizontal.setSpacing(true); // coloca um espaÃ§amento entre os elementos internos (30px)
        containerAba5.addComponent(containerHorizontal); // adiciona o container de datas no superior

        final Button adicionarColigadaButton = new Button(getMessages().getString("SignupView.adicionarColigadaButton.label"), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                String nomeColigada = getAssociatedNameTextField().getValue();
                getNationalEntityRegistrationAssociatedTextField().commit();
                String cnpjColigada = getNationalEntityRegistrationAssociatedTextField().getValue();
                String identificadorLinha = "TEMP_" + Math.round(Math.random() * 1000000);
                listener.incluirColigada(nomeColigada, cnpjColigada, identificadorLinha);
            }
        });

        containerHorizontal.addComponent(adicionarColigadaButton);
        adicionarColigadaButton.addStyleName("small default");

        associatedTable = new Table();
        containerAba5.addComponent(associatedTable);
        associatedTable.setPageLength(5);
        associatedTable.setSizeFull();

        associatedTable.addContainerProperty(getMessages().getString("SignupView.coligadasTable.nome"), String.class, null);
        associatedTable.addContainerProperty(getMessages().getString("SignupView.coligadasTable.cnpj"), String.class, null);
        associatedTable.addContainerProperty(getMessages().getString("SignupView.coligadasTable.remover"), Button.class, null);
        associatedTable.setImmediate(true);
        associatedTable.setSelectable(true);

        return containerAba5;
    }

    private VerticalLayout buildAba6Filiais() {
        VerticalLayout containerAba6 = new VerticalLayout();
        containerAba6.setMargin(true);
        containerAba6.setSpacing(true);

        setSubsidiaryNameTextField(new TextField());
        containerAba6.addComponent(getSubsidiaryNameTextField());
        getSubsidiaryNameTextField().setInputPrompt(getMessages().getString("SignupView.nomeFilialTextField.label"));
        subsidiaryNameTextField.setWidth("300px");

        setNationalEntityRegistrationSubsidiaryTextField(new TextField());
        containerAba6.addComponent(getNationalEntityRegistrationSubsidiaryTextField());
        getNationalEntityRegistrationSubsidiaryTextField().setInputPrompt(getMessages().getString("SignupView.cnpjFilialTextField.label"));
        nationalEntityRegistrationSubsidiaryTextField.setWidth("300px");

        HorizontalLayout containerHorizontal = new HorizontalLayout();
        containerHorizontal.setSpacing(true); // coloca um espaÃ§amento entre os elementos internos (30px)
        containerAba6.addComponent(containerHorizontal); // adiciona o container de datas no superior

        final Button adicionarFilialButton = new Button(getMessages().getString("SignupView.adicionarFilialButton.label"), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                String nomeFilial = getSubsidiaryNameTextField().getValue();
                String cnpjFilial = getNationalEntityRegistrationSubsidiaryTextField().getValue();
                String identificadorLinha = "TEMP_" + Math.round(Math.random() * 1000000);

                listener.incluirFilial(nomeFilial, cnpjFilial, identificadorLinha);
            }
        });

        containerHorizontal.addComponent(adicionarFilialButton);
        adicionarFilialButton.addStyleName("small default");

        subsidiariesTable = new Table();
        containerAba6.addComponent(subsidiariesTable);
        subsidiariesTable.setPageLength(5);

        subsidiariesTable.addContainerProperty(getMessages().getString("SignupView.filiaisTable.nome"), String.class, null);
        subsidiariesTable.addContainerProperty(getMessages().getString("SignupView.filiaisTable.cnpj"), String.class, null);
        subsidiariesTable.addContainerProperty(getMessages().getString("SignupView.filiaisTable.remover"), Button.class, null);
        subsidiariesTable.setImmediate(true);
        subsidiariesTable.setSelectable(true);

        return containerAba6;
    }

    /**
     * Creates and returns the 4th. tab with the fields to add new users to
     * businesses
     *
     * @TODO: Fernando
     * @return user registration tab -> Business
     */
    private VerticalLayout buildAba4UsuarioEmpresa() {
        VerticalLayout containerAba4 = new VerticalLayout();
        containerAba4.setMargin(true);
        containerAba4.setSpacing(true);

        userNameTextField = new TextField();
        containerAba4.addComponent(userNameTextField);
        userNameTextField.setInputPrompt(getMessages().getString("SignupView.nomeTextField.label"));
        userNameTextField.setWidth("300px");

        userSurnameTextField = new TextField();
        containerAba4.addComponent(userSurnameTextField);
        userSurnameTextField.setInputPrompt(getMessages().getString("SignupView.sobrenomeTextField.label"));
        userSurnameTextField.setWidth("300px");

        emailTextField = new TextField();
        containerAba4.addComponent(emailTextField);
        emailTextField.setInputPrompt(getMessages().getString("SignupView.emailTextField.label"));
        emailTextField.setWidth("300px");

        emailConfirmTextField = new TextField();
        containerAba4.addComponent(emailConfirmTextField);
        emailConfirmTextField.setInputPrompt(getMessages().getString("SignupView.confirmaEmailTextField.label"));
        emailConfirmTextField.setWidth("300px");

        HorizontalLayout containerHorizontal = new HorizontalLayout();
        containerHorizontal.setSpacing(true); // coloca um espaÃ§amento entre os elementos internos (30px)
        containerAba4.addComponent(containerHorizontal); // adiciona o container de datas no superior

        userAdmCheckBox = new CheckBox(getMessages().getString("SignupView.usuarioAdmCheckBox.label"));
        containerHorizontal.addComponent(userAdmCheckBox);

        final Button adicionarUsuarioButton = new Button(getMessages().getString("SignupView.adicionarUsuarioButton.label"), new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {

                String identificadorLinha = "TEMP_" + Math.round(Math.random() * 1000000);

                listener.incluirUsuario(getUserNameTextField().getValue(),
                        getUserSurnameTextField().getValue(),
                        getEmailTextField().getValue(),
                        !getUserAdmCheckBox().isEmpty(),
                        identificadorLinha);
            }
        });

        containerHorizontal.addComponent(adicionarUsuarioButton);
        adicionarUsuarioButton.addStyleName("small default");

        usersTable = new Table();
        containerAba4.addComponent(usersTable);
        usersTable.setPageLength(5);
        usersTable.setSizeFull();

        usersTable.addContainerProperty(getMessages().getString("SignupView.usuariosTable.nome"), String.class, null);
        usersTable.addContainerProperty(getMessages().getString("SignupView.usuariosTable.sobrenome"), String.class, null);
        usersTable.addContainerProperty(getMessages().getString("SignupView.usuariosTable.email"), String.class, null);
        usersTable.addContainerProperty(getMessages().getString("SignupView.usuariosTable.administrador"), String.class, null);
        usersTable.addContainerProperty(getMessages().getString("SignupView.usuariosTable.remover"), Button.class, null);
        usersTable.setImmediate(true);
        usersTable.setSelectable(true);

        return containerAba4;
    }

    public String getNome() {
        return getNameTextField().getValue();
    }

    public String getSobrenome() {
        return getSurnameTextField().getValue();
    }

    public String getSenha() {
        return getPasswordTextField().getValue();
    }

    public CheckBox getAceitaTermos() {
        return getAcceptTermsCheckBox();
    }

    /**
     * @return the messages
     */
    public ResourceBundle getMessages() {
        return messages;
    }

    /**
     * @return the listener
     */
    public SignupViewListener getListener() {
        return listener;
    }

    /**
     * @return the nameTextField
     */
    public TextField getNameTextField() {
        return nameTextField;
    }

    /**
     * @param nameTextField the nameTextField to set
     */
    public void setNameTextField(TextField nameTextField) {
        this.nameTextField = nameTextField;
    }

    /**
     * @return the userEmailTextField
     */
    public TextField getUserEmailTextField() {
        return userEmailTextField;
    }

    /**
     * @param userEmailTextField the userEmailTextField to set
     */
    public void setUserEmailTextField(TextField userEmailTextField) {
        this.userEmailTextField = userEmailTextField;
    }

    /**
     * @return the confirmeUserEmailTextField
     */
    public TextField getConfirmeUserEmailTextField() {
        return confirmeUserEmailTextField;
    }

    /**
     * @param confirmeUserEmailTextField the confirmeUserEmailTextField to set
     */
    public void setConfirmeUserEmailTextField(TextField confirmeUserEmailTextField) {
        this.confirmeUserEmailTextField = confirmeUserEmailTextField;
    }

    /**
     * @return the surnameTextField
     */
    public TextField getSurnameTextField() {
        return surnameTextField;
    }

    /**
     * @param surnameTextField the surnameTextField to set
     */
    public void setSurnameTextField(TextField surnameTextField) {
        this.surnameTextField = surnameTextField;
    }

    /**
     * @return the passwordTextField
     */
    public PasswordField getPasswordTextField() {
        return passwordTextField;
    }

    /**
     * @param passwordTextField the passwordTextField to set
     */
    public void setPasswordTextField(PasswordField passwordTextField) {
        this.passwordTextField = passwordTextField;
    }

    /**
     * @return the passwordTextField
     */
    public PasswordField getConfirmationPasswordTextField() {
        return confirmationPasswordTextField;
    }

    /**
     * @param confirmationPasswordTextField
     */
    public void setConfirmationPasswordTextField(PasswordField confirmationPasswordTextField) {
        this.confirmationPasswordTextField = confirmationPasswordTextField;
    }

    /**
     * @return the usersTable
     */
    public Table getUsersTable() {
        return usersTable;
    }

    /**
     * @param usersTable the UsuariosTable to set
     */
    public void setUsersTable(Table usersTable) {
        this.usersTable = usersTable;
    }

    /**
     * @return the associatedTable
     */
    public Table getAssociatedTable() {
        return associatedTable;
    }

    /**
     * @param associatedTable the ColigadasTable to set
     */
    public void setAssociatedTable(Table associatedTable) {
        this.associatedTable = associatedTable;
    }

    /**
     * @return the subsidiariesTable
     */
    public Table getSubsidiariesTable() {
        return subsidiariesTable;
    }

    /**
     * @param subsidiariesTable the FiliaisTable to set
     */
    public void setSubsidiariesTable(Table subsidiariesTable) {
        this.subsidiariesTable = subsidiariesTable;
    }

    /**
     * @return the acceptTermsCheckBox
     */
    public CheckBox getAcceptTermsCheckBox() {
        return acceptTermsCheckBox;
    }

    /**
     * @param acceptTermsCheckBox the acceptTermsCheckBox to set
     */
    public void setAcceptTermsCheckBox(CheckBox acceptTermsCheckBox) {
        this.acceptTermsCheckBox = acceptTermsCheckBox;
    }

    /**
     * @return the companyNameTextField
     */
    public TextField getCompanyNameTextField() {
        return companyNameTextField;
    }

    /**
     * @param companyNameTextField the companyNameTextField to set
     */
    public void setCompanyNameTextField(TextField companyNameTextField) {
        this.companyNameTextField = companyNameTextField;
    }

    /**
     * @return the personTypeOptionGroup
     */
    public OptionGroup getPersonTypeOptionGroup() {
        return personTypeOptionGroup;
    }

    public char getPersonType() {
        System.out.println(personTypeOptionGroup.getValue().toString().charAt(7));
        return personTypeOptionGroup.getValue().toString().charAt(7);
    }

    /**
     * @param personTypeOptionGroup the personTypeOptionGroup to set
     */
    public void setPersonTypeOptionGroup(OptionGroup personTypeOptionGroup) {
        this.personTypeOptionGroup = personTypeOptionGroup;
    }

    /**
     * @return the cnpjTextField
     */
    public TextField getNationalEntityRegistrationCodeTextField() {
        return nationalEntityRegistrationCodeTextField;
    }

    /**
     * @param cnpjTextField the cnpjTextField to set
     */
    public void setNationalEntityRegistrationCodeTextField(TextField cnpjTextField) {
        this.nationalEntityRegistrationCodeTextField = cnpjTextField;
    }

    /**
     * @return the associatedNameTextField
     */
    public TextField getAssociatedNameTextField() {
        return associatedNameTextField;
    }

    /**
     * @param associatedNameTextField the associatedNameTextField to set
     */
    public void setAssociatedNameTextField(TextField associatedNameTextField) {
        this.associatedNameTextField = associatedNameTextField;
    }

    /**
     * @return the subsidiaryNameTextField
     */
    public TextField getSubsidiaryNameTextField() {
        return subsidiaryNameTextField;
    }

    /**
     * @param subsidiaryNameTextField the subsidiaryNameTextField to set
     */
    public void setSubsidiaryNameTextField(TextField subsidiaryNameTextField) {
        this.subsidiaryNameTextField = subsidiaryNameTextField;
    }

    /**
     * @return the nationalEntityRegistrationAssociatedTextField
     */
    public TextField getNationalEntityRegistrationAssociatedTextField() {
        return nationalEntityRegistrationAssociatedTextField;
    }

    /**
     * @param nationalEntityRegistrationAssociatedTextField the
     * nationalEntityRegistrationAssociatedTextField to set
     */
    public void setNationalEntityRegistrationAssociatedTextField(TextField nationalEntityRegistrationAssociatedTextField) {
        this.nationalEntityRegistrationAssociatedTextField = nationalEntityRegistrationAssociatedTextField;
    }

    /**
     * @return the nationalEntityRegistrationSubsidiaryTextField
     */
    public TextField getNationalEntityRegistrationSubsidiaryTextField() {
        return nationalEntityRegistrationSubsidiaryTextField;
    }

    /**
     * @param nationalEntityRegistrationSubsidiaryTextField the
     * nationalEntityRegistrationSubsidiaryTextField to set
     */
    public void setNationalEntityRegistrationSubsidiaryTextField(TextField nationalEntityRegistrationSubsidiaryTextField) {
        this.nationalEntityRegistrationSubsidiaryTextField = nationalEntityRegistrationSubsidiaryTextField;
    }

    /**
     * @return the userNameTextField
     */
    public TextField getUserNameTextField() {
        return userNameTextField;
    }

    /**
     * @param userNameTextField the userNameTextField to set
     */
    public void setUserNameTextField(TextField userNameTextField) {
        this.userNameTextField = userNameTextField;
    }

    /**
     * @return the userSurnameTextField
     */
    public TextField getUserSurnameTextField() {
        return userSurnameTextField;
    }

    /**
     * @param userSurnameTextField the userSurnameTextField to set
     */
    public void setUserSurnameTextField(TextField userSurnameTextField) {
        this.userSurnameTextField = userSurnameTextField;
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
     * @return the emailConfirmTextField
     */
    public TextField getEmailConfirmTextField() {
        return emailConfirmTextField;
    }

    /**
     * @param emailConfirmTextField the emailConfirmTextField to set
     */
    public void setEmailConfirmTextField(TextField emailConfirmTextField) {
        this.emailConfirmTextField = emailConfirmTextField;
    }

    /**
     * @return the userAdmCheckBox
     */
    public CheckBox getUserAdmCheckBox() {
        return userAdmCheckBox;
    }

    /**
     * @param userAdmCheckBox the userAdmCheckBox to set
     */
    public void setUserAdmCheckBox(CheckBox userAdmCheckBox) {
        this.userAdmCheckBox = userAdmCheckBox;
    }

    /**
     * @return the activeSubsidiaryCheckBox
     */
    public CheckBox getActiveSubsidiaryCheckBox() {
        return activeSubsidiaryCheckBox;
    }

    /**
     * @param activeSubsidiaryCheckBox the activeSubsidiaryCheckBox to set
     */
    public void setActiveSubsidiaryCheckBox(CheckBox activeSubsidiaryCheckBox) {
        this.activeSubsidiaryCheckBox = activeSubsidiaryCheckBox;
    }

    /**
     * Performs validation of methods required fields
     */
    public void validate() {
        nameTextField.setValidationVisible(true);
        surnameTextField.setValidationVisible(true);
        userEmailTextField.setValidationVisible(true);
        confirmeUserEmailTextField.setValidationVisible(true);
        passwordTextField.setValidationVisible(true);
        confirmationPasswordTextField.setValidationVisible(true);
        acceptTermsCheckBox.setValidationVisible(true);

        nameTextField.validate();
        surnameTextField.validate();
        userEmailTextField.validate();
        confirmeUserEmailTextField.validate();
        passwordTextField.validate();
        acceptTermsCheckBox.validate();

        companyNameTextField.setValidationVisible(true);
        personTypeOptionGroup.setValidationVisible(true);
        nationalEntityRegistrationCodeTextField.setValidationVisible(true);

        companyNameTextField.validate();
        personTypeOptionGroup.validate();
        nationalEntityRegistrationCodeTextField.validate();

    }

    public void removerPassword() {

        getPasswordTextField().removeAllValidators();
        getPasswordTextField().setVisible(false);

        getConfirmationPasswordTextField().removeAllValidators();
        getConfirmationPasswordTextField().setVisible(false);

        containerLabel.removeComponent(labelPasswd);
        containerLabel.removeComponent(labelConfirmPasswd);

    }

}
