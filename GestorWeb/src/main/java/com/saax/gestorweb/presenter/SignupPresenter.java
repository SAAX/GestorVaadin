package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.SignupModel;
import com.saax.gestorweb.model.datamodel.Cidade;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.Endereco;
import com.saax.gestorweb.model.datamodel.Estado;
import com.saax.gestorweb.model.datamodel.FilialEmpresa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.view.SignupView;
import com.saax.gestorweb.view.SignupViewListener;
import com.vaadin.data.Item;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.SessionAttributesEnum;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;

/**
 * SignUP Presenter <br>
 * This class is responsible capture all events that occur in View <br>
 * and provide appropriate treatment, using this model
 *
 *
 * @author Rodrigo
 */
public class SignupPresenter implements Serializable, SignupViewListener {

    // Reference to the use of the messages:
    private final transient ResourceBundle messages = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final transient GestorWebImagens images = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();

    // Every presenter keeps access to view and model
    private final transient SignupView view;
    private final transient SignupModel model;
    
    private final Usuario loggedUser;

    /**
     * Creates the presenter linking the Model View
     *
     * @param model
     * @param view
     */
    public SignupPresenter(SignupModel model, SignupView view) {

        this.model = model;
        this.view = view;
        
        loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO);

        view.setListener(this);

    }

    public void open() {
        // Load the state combos
        carregaComboEstado();

    }

    @Override
    public void cancelButtonClicked() {
        ((GestorMDI) UI.getCurrent()).logout();
    }

    /**
     * validates user data tab mainly be to register
     *
     * @return true
     */
    private boolean validaDadosUsuarioPrincipal() {

        //String login = ""; // @TODO: get email the primary user
        String login = view.getUserEmailTextField().getValue(); // @TODO: get from view

        // Check if the user informed there (login)
        if (model.verificaLoginExistente(login)) {

            // Displays an error message indicating that this login (email) already
            // exists in the system and asks the user if he does not recover your password
            Notification.show(messages.getString("SignupPresenter.mensagem.loginPreExistente"), Notification.Type.WARNING_MESSAGE);

            return false;
        }

        return true;
    }

    /**
     * validates account information (parent company)
     *
     * @return true if the data is valid
     */
    private boolean validaDadosEmpresa() {

        // checks whether the company (account) informed no longer exists in the record
        char tipoPessoa = '\0';

        if (view.getPersonTypeOptionGroup().getValue() == "Pessoa Física") {
            tipoPessoa = 'F';
        } else if (view.getPersonTypeOptionGroup().getValue() == "Pessoa Jurídica") {
            tipoPessoa = 'J';
        } else {
            return false;
        }

        String cpf_cnpj = view.getNationalEntityRegistrationCodeTextField().getValue();

        if (model.verificaEmpresaExistente(cpf_cnpj, tipoPessoa)) {

            // Displays an error message indicating that this company exists in the system
            Notification.show(messages.getString("SignupPresenter.mensagem.empresaPreExistente"), Notification.Type.WARNING_MESSAGE);

            return false;
        }

        return true;
    }

    /**
     * validates data from sub affiliates the main company
     *
     * @return true if the data is valid
     */
    private boolean validaDadosEmpresasColigadas() {

        final Table empresasColigadasTable = view.getAssociatedTable();
        Set<String> identificadoresEmpresasColigadas = new HashSet<>();

        for (Object itemID : empresasColigadasTable.getItemIds()) {

            Item linha = empresasColigadasTable.getItem(itemID);

            String cpfCnpjSubEmpresa = (String) linha.getItemProperty(messages.getString("SignupView.coligadasTable.email")).getValue(); // @ATENCAO
            String nomeSubEmpresa = (String) linha.getItemProperty(messages.getString("SignupView.coligadasTable.nome")).getValue(); // @ATENCAO
            char tipoPessoaSubEmpresa = 'J'; // @TODO: Get from view
            if (cpfCnpjSubEmpresa != null) {
                if (model.verificaEmpresaExistente(cpfCnpjSubEmpresa, tipoPessoaSubEmpresa)) {

                    // Displays an error message indicating that this company exists in the system
                    Notification.show(messages.getString("SignupPresenter.mensagem.empresaPreExistente"), Notification.Type.WARNING_MESSAGE);

                    return false;
                }
            }
            // Validates the sub company is no longer part of the same parent company
            if (identificadoresEmpresasColigadas.contains(nomeSubEmpresa)) {
                // Displays an error message indicating that this sub company
                // Was registered in duplicate
                Notification.show(messages.getString("SignupPresenter.mensagem.empresaColigadaDuplicada"), Notification.Type.WARNING_MESSAGE);

                return false;

            }

            identificadoresEmpresasColigadas.add(nomeSubEmpresa);
        }

        return true;

    }

    /**
     * validates data from subsidiaries to parent company
     *
     * @return true if the data is valid
     */
    private boolean validaDadosFiliais() {

        final Table filiaisTable = view.getSubsidiariesTable();
        Set<String> identificadoresFiliais = new HashSet<>();
        Set<String> nomesFiliais = new HashSet<>();

        for (Object itemID : filiaisTable.getItemIds()) {

            Item linha = filiaisTable.getItem(itemID);

            // Validates if the branch is no longer registered in the system
            String cnpjFilial = (String) linha.getItemProperty(messages.getString("SignupView.filiaisTable.cnpj")).getValue(); // @ATENCAO
            String nomeFilial = (String) linha.getItemProperty(messages.getString("SignupView.filiaisTable.nome")).getValue(); // @ATENCAO

            if (StringUtils.isNotBlank(cnpjFilial)) {
                if (model.verificaFilialExistente(cnpjFilial)) {

                    // Displays an error message indicating that this branch exists in the system
                    Notification.show(messages.getString("SignupPresenter.mensagem.empresaPreExistente"), Notification.Type.WARNING_MESSAGE);

                    return false;
                }

                // Validates that the subsidiary is no longer part of the same parent company
                if (identificadoresFiliais.contains(cnpjFilial)) {
                    // Displays an error message indicating that this branch
                    // Was registered in duplicate
                    Notification.show(messages.getString("SignupPresenter.mensagem.filialDuplicada"), Notification.Type.WARNING_MESSAGE);

                    return false;

                }
                identificadoresFiliais.add(cnpjFilial);
            } else {
                // Validates that the subsidiary is no longer part of the same parent company by name
                if (nomesFiliais.contains(nomeFilial)) {
                    // Displays an error message indicating that this branch (by name) 
                    // Was registered in duplicate
                    Notification.show(messages.getString("SignupPresenter.mensagem.filialDuplicada"), Notification.Type.WARNING_MESSAGE);

                    return false;

                }
                nomesFiliais.add(nomeFilial);

            }
        }

        return true;
    }

    /**
     * validates data from other users
     *
     * @return true if date is valid
     */
    private boolean validaDadosDemaisUsuarios() {

        Table usuariosTable = view.getUsersTable();
        Set<String> identificadoresUsuarios = new HashSet<>();

        for (Object itemID : usuariosTable.getItemIds()) {

            Item linha = usuariosTable.getItem(itemID);

            // Validates if the users are not already registered in the system
            String emailUsuario = (String) linha.getItemProperty(messages.getString("SignupView.usuariosTable.email")).getValue(); // @ATENCAO
            System.out.println("usuario " + emailUsuario);
            if (model.verificaLoginExistente(emailUsuario)) {

                // Displays an error message indicating that this User already exists in the system
                Notification.show(messages.getString("SignupPresenter.mensagem.usuarioExistente"), Notification.Type.WARNING_MESSAGE);

                return false;
            }

            // Validates if the user is no longer related to the same enterprise
            if (identificadoresUsuarios.contains(emailUsuario)) {
                // Displays an error message indicating that this User is duplicated
                Notification.show(messages.getString("SignupPresenter.mensagem.usuarioDuplicado"), Notification.Type.WARNING_MESSAGE);

                return false;

            }
            identificadoresUsuarios.add(emailUsuario);
        }

        return true;

    }

    /**
     * Fetch all view of the data needed to set the initial registration
     * with the main company subsidiaries and affiliates + users
     *
     */
    private Empresa buildConta() {

        Empresa empresaPrincipal;

        // ---------------------------------------------------------------------
        // creates the main User
        // ---------------------------------------------------------------------
        Usuario usuarioADM = model.criarNovoUsuario(
                view.getNameTextField().getValue(),
                view.getSurnameTextField().getValue(),
                view.getUserEmailTextField().getValue(),
                view.getPasswordTextField().getValue(),
                null
        );

        // ---------------------------------------------------------------------
        // creates the main company
        // ---------------------------------------------------------------------
        String nomeFantasia = view.getFancyNameTextField().getValue();
        String razaosocial = view.getCompanyNameTextField().getValue();
        String cpfCnpj = view.getNationalEntityRegistrationCodeTextField().getValue();
        
        char tipoPessoa = '\0';
        if (view.getPersonTypeOptionGroup().getValue().equals(messages.getString("SignupView.pessoaFisicaCheckBox.label"))) { // @ATENCAO
            tipoPessoa = 'F';
        } else if (view.getPersonTypeOptionGroup().getValue().equals(messages.getString("SignupView.pessoaJuridicaCheckBox.label"))) { // @ATENCAO
            tipoPessoa = 'J';
        } else {
            return null;
        }

        empresaPrincipal = model.criarNovaEmpresa(nomeFantasia, razaosocial, cpfCnpj, tipoPessoa, usuarioADM);

        String logradouro = view.getAdressTextField().getValue();
        String numero = view.getNumberTextField().getValue();
        String complemento = view.getComplementTextField().getValue();
        String cep = view.getZipCodeTextField().getValue();
        Cidade cidade = (Cidade) view.getCityComboBox().getValue();
        if (StringUtils.isNotBlank(logradouro)) {
            Endereco endereco = model.criarEndereco(logradouro, numero, complemento, cep, cidade, usuarioADM);
            model.relacionarEmpresaEndereco(empresaPrincipal, endereco);
        }

        model.relacionarUsuarioEmpresa(usuarioADM, empresaPrincipal, true, usuarioADM);

        // ---------------------------------------------------------------------
        // creates the list of sub companies 
        // ---------------------------------------------------------------------
        Table empresasColigadasTable = view.getAssociatedTable();

        empresasColigadasTable.getItemIds().stream().forEach((itemID) -> {

            Item linhaEmpresaColigada = empresasColigadasTable.getItem(itemID);

            String nomeFantasiaEmpresaColigada = (String) linhaEmpresaColigada.getItemProperty(messages.getString("SignupView.coligadasTable.nome")).getValue();
            String cpfCnpjEmpresaColigada = (String) linhaEmpresaColigada.getItemProperty(messages.getString("SignupView.coligadasTable.cnpj")).getValue();

            Empresa subempresa = model.criarNovaEmpresaColigada(nomeFantasiaEmpresaColigada, cpfCnpjEmpresaColigada, usuarioADM);

            model.relacionarEmpresaColigada(empresaPrincipal, subempresa);

        });

        // ---------------------------------------------------------------------
            // creates the branch list
        // ---------------------------------------------------------------------
        Table filiaisTable = view.getSubsidiariesTable();
        filiaisTable.getItemIds().stream().forEach((itemID) -> {

            Item linha = filiaisTable.getItem(itemID);

            String nomeFilial = (String) linha.getItemProperty(messages.getString("SignupView.filiaisTable.nome")).getValue();
            String cnpjFilial = (String) linha.getItemProperty(messages.getString("SignupView.filiaisTable.cnpj")).getValue();

            FilialEmpresa filialEmpresa = model.criarFilialEmpresa(nomeFilial, cnpjFilial, usuarioADM);
            model.relacionarEmpresaFilial(empresaPrincipal, filialEmpresa);

        });

        // ---------------------------------------------------------------------
        // creates a list of users
        // ---------------------------------------------------------------------
        Table usuariosTable = view.getUsersTable();
        usuariosTable.getItemIds().stream().forEach((itemID) -> {

            Item linha = usuariosTable.getItem(itemID);

            String nome = (String) linha.getItemProperty(messages.getString("SignupView.usuariosTable.nome")).getValue();
            String sobreNome = (String) linha.getItemProperty(messages.getString("SignupView.usuariosTable.sobrenome")).getValue();
            String email = (String) linha.getItemProperty(messages.getString("SignupView.usuariosTable.email")).getValue();
            boolean administrador = linha.getItemProperty(messages.getString("SignupView.usuariosTable.administrador")).getValue().equals("SIM");

            Usuario usuario = model.criarNovoUsuario(nome, sobreNome, email, usuarioADM);
            model.relacionarUsuarioEmpresa(usuario, empresaPrincipal, administrador, usuarioADM);

        });

        return empresaPrincipal;

    }

    @Override
    public void okButtonClicked() {

        // Validate filling the required fields
        view.validate();

        if (!validaDadosUsuarioPrincipal()) {
            return;
        }

        if (!validaDadosEmpresa()) {
            return;
        }

        if (!validaDadosEmpresasColigadas()) {
            return;
        }

        if (!validaDadosFiliais()) {
            return;
        }

        if (!validaDadosDemaisUsuarios()) {
            return;
        }

        // ---------------------------------------------------------------------
        // Validations successful !!!
        // proceeds with the recording of data
        // ---------------------------------------------------------------------
        // Get all data from view and assembles objects
        Empresa empresa = buildConta();

        try {

            model.criarNovaConta(empresa);
            view.close();
            ((GestorMDI) UI.getCurrent()).carregarDashBoard();
        } catch (RuntimeException ex) {
            // This is the top of the call stack (the highest try catch) bearing this must log the exception:
            Logger.getLogger(SignupPresenter.class.getName()).log(Level.SEVERE, null, ex);
            Notification.show(messages.getString("Gestor.mensagemErroGenerica"), Notification.Type.WARNING_MESSAGE);

        }

        //fernando: Precisei comentar pq não há usuário logado até então
        //GestorSession.setAttribute("loggedUser", empresa.getUsuarioInclusao());

    }

    /**
     * Event fired when clicked the button to make the inclusion of
     * User in grid Gets the name, surname and email
     */
    @Override
    public void incluirUsuario() {

        String nomeUsuario = view.getUserNameTextField().getValue();
        String sobrenomeUsuario = view.getUserSurnameTextField().getValue();
        String email = view.getEmailTextField().getValue();

        // Check if User is Administrator or not
        Boolean usuarioAdm = view.getUserAdmCheckBox().getValue();
        String adm = "";
        if (usuarioAdm == true) {
            adm = "SIM";
        } else {
            adm = "NÃO";
        }

        Button removerUsuarioButton = new Button(messages.getString("SignupPresenter.removerButton.label"));
        removerUsuarioButton.setId(nomeUsuario);
        removerUsuarioButton.addClickListener((Button.ClickEvent event) -> {
            String nomeUsuarioBotao = event.getButton().getId();

            view.getUsersTable().removeItem(nomeUsuarioBotao);
            view.getUsersTable().refreshRowCache();
            Notification.show((messages.getString("Notificacao.Sucesso")), (messages.getString("Notificacao.ItemExcluidoSucesso")), Notification.TYPE_HUMANIZED_MESSAGE);// @ATENCAO

        });

        view.getUsersTable().addItem(new Object[]{nomeUsuario, sobrenomeUsuario, email, adm, removerUsuarioButton}, nomeUsuario);

        view.getUserNameTextField().setValue("");
        view.getUserSurnameTextField().setValue("");
        view.getEmailTextField().setValue("");
        view.getEmailConfirmTextField().setValue("");
        view.getUserAdmCheckBox().setValue(false);

    }

    /**
     * Event fired when clicked the button to make the inclusion of
     * Related in the grid Gets the name, surname and email
     */
    @Override
    public void incluirColigadas() {

        String nomeColigada = view.getAssociatedNameTextField().getValue();
        view.getNationalEntityRegistrationAssociatedTextField().commit();
        String cnpjColigada = view.getNationalEntityRegistrationAssociatedTextField().getValue();

        System.out.println("cnpj: " + cnpjColigada);

        Button removerColigadasButton = new Button(messages.getString("SignupPresenter.removerButton.label"));
        removerColigadasButton.setId(nomeColigada);
        removerColigadasButton.addClickListener((Button.ClickEvent event) -> {
            String nomeColigadaBotao = event.getButton().getId();

            view.getAssociatedTable().removeItem(nomeColigadaBotao);
            view.getAssociatedTable().refreshRowCache();
            Notification.show((messages.getString("Notificacao.Sucesso")), (messages.getString("Notificacao.ItemExcluidoSucesso")), Notification.TYPE_HUMANIZED_MESSAGE); // @ATENCAO

        });

        view.getAssociatedTable().addItem(new Object[]{nomeColigada, cnpjColigada, removerColigadasButton}, nomeColigada);

        view.getAssociatedNameTextField().setValue("");
        view.getNationalEntityRegistrationAssociatedTextField().setValue("");

    }

    /**
     * Event fired when clicked the button to make the inclusion of
     * Related in the grid Gets the name, surname and email
     */
    @Override
    public void incluirFiliais() {

        String nomeFilial = view.getSubsidiaryNameTextField().getValue();
        String cnpjFilial = view.getNationalEntityRegistrationSubsidiaryTextField().getValue();

        Button removerFiliaisButton = new Button(messages.getString("SignupPresenter.removerButton.label"));
        removerFiliaisButton.setId(nomeFilial);
        removerFiliaisButton.addClickListener((Button.ClickEvent event) -> {
            String nomeFilialBotao = event.getButton().getId();

            view.getSubsidiariesTable().removeItem(nomeFilialBotao);
            view.getSubsidiariesTable().refreshRowCache();
            Notification.show((messages.getString("Notificacao.Sucesso")), (messages.getString("Notificacao.ItemExcluidoSucesso")), Notification.TYPE_HUMANIZED_MESSAGE);// @ATENCAO

        });

        view.getSubsidiariesTable().addItem(new Object[]{nomeFilial, cnpjFilial, removerFiliaisButton}, nomeFilial);

        view.getSubsidiaryNameTextField().setValue("");
        view.getNationalEntityRegistrationSubsidiaryTextField().setValue("");

    }

    /**
     * Event fired when clicked the button to select the state of the Company
     * be registered
     *
     */
    private void carregaComboEstado() {

        ComboBox estadoCombo = view.getStateComboBox();

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        List<Estado> estados = em.createNamedQuery("Estado.findAll").getResultList();

        for (Estado estado : estados) {

            estadoCombo.addItem(estado);
            estadoCombo.setItemCaption(estado, estado.getUf());

        }
    }

    /**
     * Event fired after being selected the company's state to be
     * registered and carries the comboBox with belonging to this state cities
     */
    @Override
    public void estadoSelecionado() {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        ComboBox cidadeCombo = view.getCityComboBox();

        cidadeCombo.removeAllItems();

        List<Cidade> cidades = em.createNamedQuery("Cidade.findByEstado")
                .setParameter("estado", view.getStateComboBox().getValue())
                .getResultList();

        for (Cidade cidade : cidades) {
            cidadeCombo.addItem(cidade);
            cidadeCombo.setItemCaption(cidade, cidade.getNome());
        }

    }

    @Override
    public void personTypeSelected() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
