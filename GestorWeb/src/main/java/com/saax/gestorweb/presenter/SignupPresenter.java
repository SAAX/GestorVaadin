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
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import java.util.ArrayList;
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
 * Esta classe é responsável captar todos os eventos que ocorrem na View <br>
 * e dar o devido tratamento, utilizando para isto o modelo
 *
 *
 * @author Rodrigo
 */
public class SignupPresenter implements SignupViewListener {

    // Referencia ao recurso das mensagens:
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();

    // Todo presenter mantem acesso à view e ao model
    private final SignupView view;
    private final SignupModel model;

    /**
     * Cria o presenter ligando o Model ao View
     *
     * @param model
     * @param view
     */
    public SignupPresenter(SignupModel model, SignupView view) {

        this.model = model;
        this.view = view;

        view.setListener(this);

    }

    public void open() {
        // Carrega os combos de seleção
        carregaComboEstado();

    }

    @Override
    public void cancelButtonClicked() {
        ((GestorMDI) UI.getCurrent()).logout();
    }

    /**
     * valida a aba de dados do usuário princiapal para cadastro
     *
     * @return true se os dados forem validos
     */
    private boolean validaDadosUsuarioPrincipal() {

        //String login = ""; // @TODO: obter email do usuário principal
        String login = (String) view.getUserEmailTextField().getValue(); // @TODO: Obter da view
        //System.out.println("usuario " + login);

        // verifica se o usuário informado existe (login)
        if (model.verificaLoginExistente(login)) {

            // Exibe uma mensagem de erro indicando que este login (email) já 
            //existe no sistema e pergunta ao usuário se ele não quer recuperar sua senha
            Notification.show(mensagens.getString("SignupPresenter.mensagem.loginPreExistente"), Notification.Type.WARNING_MESSAGE);

            return false;
        }

        return true;
    }

    /**
     * valida dados da conta (empresa principal)
     *
     * @return true se os dados forem validos
     */
    private boolean validaDadosEmpresa() {

        // verifica se a empresa (conta) informada já não existe no cadastro
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

            // Exibe uma mensagem de erro indicando que esta empresa (pelo cnpj/cpf) já existe no sistema
            Notification.show(mensagens.getString("SignupPresenter.mensagem.empresaPreExistente"), Notification.Type.WARNING_MESSAGE);

            return false;
        }

        return true;
    }

    /**
     * valida dados das sub empresas coligadas a empresa principal
     *
     * @return true se os dados forem validos
     */
    private boolean validaDadosEmpresasColigadas() {

        final Table empresasColigadasTable = view.getAssociatedTable();
        Set<String> identificadoresEmpresasColigadas = new HashSet<>();

        for (Object itemID : empresasColigadasTable.getItemIds()) {

            Item linha = empresasColigadasTable.getItem(itemID);

            String cpfCnpjSubEmpresa = (String) linha.getItemProperty(mensagens.getString("SignupView.coligadasTable.email")).getValue(); // @ATENCAO
            String nomeSubEmpresa = (String) linha.getItemProperty(mensagens.getString("SignupView.coligadasTable.nome")).getValue(); // @ATENCAO
            char tipoPessoaSubEmpresa = 'J'; // @TODO: Obter da view
            if (cpfCnpjSubEmpresa != null) {
                if (model.verificaEmpresaExistente(cpfCnpjSubEmpresa, tipoPessoaSubEmpresa)) {

                    // Exibe uma mensagem de erro indicando que esta empresa (pelo cnpj/cpf) já existe no sistema
                    Notification.show(mensagens.getString("SignupPresenter.mensagem.empresaPreExistente"), Notification.Type.WARNING_MESSAGE);

                    return false;
                }
            }
            // Valida se a sub empresa já não faz parte desta mesma empresa principal
            if (identificadoresEmpresasColigadas.contains(nomeSubEmpresa)) {
                // Exibe uma mensagem de erro indicando que esta sub empresa (pelo cnpj/cpf) 
                // foi cadastrada em duplicidade
                Notification.show(mensagens.getString("SignupPresenter.mensagem.empresaColigadaDuplicada"), Notification.Type.WARNING_MESSAGE);

                return false;

            }

            identificadoresEmpresasColigadas.add(nomeSubEmpresa);
        }

        return true;

    }

    /**
     * valida dados das filiais a empresa principal
     *
     * @return true se os dados forem validos
     */
    private boolean validaDadosFiliais() {

        final Table filiaisTable = view.getSubsidiariesTable(); // @TODO: Obter a tabela de sub empresas
        Set<String> identificadoresFiliais = new HashSet<>();
        Set<String> nomesFiliais = new HashSet<>();

        for (Object itemID : filiaisTable.getItemIds()) {

            Item linha = filiaisTable.getItem(itemID);

            // Valida se a filial já não está cadastrada no sistema
            String cnpjFilial = (String) linha.getItemProperty(mensagens.getString("SignupView.filiaisTable.cnpj")).getValue(); // @ATENCAO
            String nomeFilial = (String) linha.getItemProperty(mensagens.getString("SignupView.filiaisTable.nome")).getValue(); // @ATENCAO

            if (StringUtils.isNotBlank(cnpjFilial)) {
                if (model.verificaFilialExistente(cnpjFilial)) {

                    // Exibe uma mensagem de erro indicando que esta filial (pelo cnpj) já existe no sistema
                    Notification.show(mensagens.getString("SignupPresenter.mensagem.empresaPreExistente"), Notification.Type.WARNING_MESSAGE);

                    return false;
                }

                // Valida se a filial já não faz parte desta mesma empresa principal pelo CNPJ
                if (identificadoresFiliais.contains(cnpjFilial)) {
                    // Exibe uma mensagem de erro indicando que esta filial (pelo cnpj/cpf) 
                    // foi cadastrada em duplicidade
                    Notification.show(mensagens.getString("SignupPresenter.mensagem.filialDuplicada"), Notification.Type.WARNING_MESSAGE);

                    return false;

                }
                identificadoresFiliais.add(cnpjFilial);
            } else {
                // Valida se a filial já não faz parte desta mesma empresa principal pelo nome
                if (nomesFiliais.contains(nomeFilial)) {
                    // Exibe uma mensagem de erro indicando que esta filial (pelo nome) 
                    // foi cadastrada em duplicidade
                    Notification.show(mensagens.getString("SignupPresenter.mensagem.filialDuplicada"), Notification.Type.WARNING_MESSAGE);

                    return false;

                }
                nomesFiliais.add(nomeFilial);

            }
        }

        return true;
    }

    /**
     * valida dados dos demais usuarios
     *
     * @return true se os dados forem validos
     */
    private boolean validaDadosDemaisUsuarios() {

        Table usuariosTable = view.getUsersTable();
        Set<String> identificadoresUsuarios = new HashSet<>();

        for (Object itemID : usuariosTable.getItemIds()) {

            Item linha = usuariosTable.getItem(itemID);

            // Valida se o usuários já não está cadastrado no sistema
            String emailUsuario = (String) linha.getItemProperty(mensagens.getString("SignupView.usuariosTable.email")).getValue(); // @ATENCAO
            System.out.println("usuario " + emailUsuario);
            if (model.verificaLoginExistente(emailUsuario)) {

                // Exibe uma mensagem de erro indicando que este usuario ja existe no sistema
                Notification.show(mensagens.getString("SignupPresenter.mensagem.usuarioExistente"), Notification.Type.WARNING_MESSAGE);

                return false;
            }

            // Valida se o usuário já não está relacionado a esta mesma empresa
            if (identificadoresUsuarios.contains(emailUsuario)) {
                // Exibe uma mensagem de erro indicando que este usuario esta duplicado
                Notification.show(mensagens.getString("SignupPresenter.mensagem.usuarioDuplicado"), Notification.Type.WARNING_MESSAGE);

                return false;

            }
            identificadoresUsuarios.add(emailUsuario);
        }

        return true;

    }

    /**
     * Obtem todos os dados da view necessários para montar o cadastro inicial
     * com a empresa principal + coligadas e filiais + usuarios
     *
     */
    private Empresa buildConta() {

        Empresa empresaPrincipal;

        // ---------------------------------------------------------------------
        // cria o usuario principal
        // ---------------------------------------------------------------------
        Usuario usuarioADM = model.criarNovoUsuario(
                view.getNameTextField().getValue(),
                view.getSurnameTextField().getValue(),
                view.getUserEmailTextField().getValue(),
                view.getPasswordTextField().getValue(),
                null
        );

        // ---------------------------------------------------------------------
        // cria a empresa principal
        // ---------------------------------------------------------------------
        String nomeFantasia = view.getFancyNameTextField().getValue();
        String razaosocial = view.getCompanyNameTextField().getValue();
        String cpfCnpj = view.getNationalEntityRegistrationCodeTextField().getValue();
        char tipoPessoa = '\0';
        if (view.getPersonTypeOptionGroup().getValue() == mensagens.getString("SignupView.pessoaFisicaCheckBox.label")) { // @ATENCAO
            tipoPessoa = 'F';
        } else if (view.getPersonTypeOptionGroup().getValue() == mensagens.getString("SignupView.pessoaJuridicaCheckBox.label")) { // @ATENCAO
            tipoPessoa = 'J';
        } else {
            return null;
        }

        empresaPrincipal = model.criarNovaEmpresa(nomeFantasia, razaosocial, cpfCnpj, tipoPessoa, usuarioADM);

        // cria o endereco
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
        // cria a lista de sub empresas 
        // ---------------------------------------------------------------------
        Table empresasColigadasTable = view.getAssociatedTable();

        empresasColigadasTable.getItemIds().stream().forEach((itemID) -> {

            Item linhaEmpresaColigada = empresasColigadasTable.getItem(itemID);

            // Fer: usar o mesmo criterio abaixo para todas as tabelas:
            String nomeFantasiaEmpresaColigada = (String) linhaEmpresaColigada.getItemProperty(mensagens.getString("SignupView.coligadasTable.nome")).getValue();
            String cpfCnpjEmpresaColigada = (String) linhaEmpresaColigada.getItemProperty(mensagens.getString("SignupView.coligadasTable.cnpj")).getValue();

            Empresa subempresa = model.criarNovaEmpresaColigada(nomeFantasiaEmpresaColigada, cpfCnpjEmpresaColigada, usuarioADM);

            model.relacionarEmpresaColigada(empresaPrincipal, subempresa);

        });

        // ---------------------------------------------------------------------
        // cria a lista de filiais
        // ---------------------------------------------------------------------
        Table filiaisTable = view.getSubsidiariesTable();
        filiaisTable.getItemIds().stream().forEach((itemID) -> {

            Item linha = filiaisTable.getItem(itemID);

            String nomeFilial = (String) linha.getItemProperty(mensagens.getString("SignupView.filiaisTable.nome")).getValue();
            String cnpjFilial = (String) linha.getItemProperty(mensagens.getString("SignupView.filiaisTable.cnpj")).getValue();

            FilialEmpresa filialEmpresa = model.criarFilialEmpresa(nomeFilial, cnpjFilial, usuarioADM);
            model.relacionarEmpresaFilial(empresaPrincipal, filialEmpresa);

        });

        // ---------------------------------------------------------------------
        // cria a lista de usuarios
        // ---------------------------------------------------------------------
        Table usuariosTable = view.getUsersTable();
        usuariosTable.getItemIds().stream().forEach((itemID) -> {

            Item linha = usuariosTable.getItem(itemID);

            String nome = (String) linha.getItemProperty(mensagens.getString("SignupView.usuariosTable.nome")).getValue();
            String sobreNome = (String) linha.getItemProperty(mensagens.getString("SignupView.usuariosTable.sobrenome")).getValue();
            String email = (String) linha.getItemProperty(mensagens.getString("SignupView.usuariosTable.email")).getValue();
            boolean administrador = ((String) linha.getItemProperty(mensagens.getString("SignupView.usuariosTable.administrador")).getValue()).equals("SIM");

            Usuario usuario = model.criarNovoUsuario(nome, sobreNome, email, usuarioADM);
            model.relacionarUsuarioEmpresa(usuario, empresaPrincipal, administrador, usuarioADM);

        });

        return empresaPrincipal;

    }

    @Override
    public void okButtonClicked() {

        // valida o preenchimento dos campos obrigatórios
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
        // Validações bem sucedidas !!!
        // procede com a gravação dos dados
        // ---------------------------------------------------------------------
        // obtem todos os dados da view e monta os objetos
        Empresa empresa = buildConta();

        try {

            model.criarNovaConta(empresa);
            view.close();
            ((GestorMDI) UI.getCurrent()).carregarDashBoard();
        } catch (RuntimeException ex) {
            // Este é o topo da pilha de chamada ( o try catch mais alto) portante este deve logar a exceção:
            Logger.getLogger(SignupPresenter.class.getName()).log(Level.SEVERE, null, ex);
            Notification.show(mensagens.getString("Gestor.mensagemErroGenerica"), Notification.Type.WARNING_MESSAGE);

        }

        GestorSession.setAttribute("loggedUser", empresa.getUsuarioInclusao());

    }

    /**
     * Evento disparado ao ser acionado o botão para efetuar a inclusão do
     * Usuário na grid Obtém o nome, sobrenome e e-mail
     */
    @Override
    public void incluirUsuario() {

        String nomeUsuario = view.getUserNameTextField().getValue();
        String sobrenomeUsuario = view.getUserSurnameTextField().getValue();
        String email = view.getEmailTextField().getValue();

        //Verifica se Usuário é Administrador ou não
        Boolean usuarioAdm = view.getUserAdmCheckBox().getValue();
        String adm = "";
        if (usuarioAdm == true) {
            adm = "SIM";
        } else {
            adm = "NÃO";
        }

        Button removerUsuarioButton = new Button(mensagens.getString("SignupPresenter.removerButton.label"));
        removerUsuarioButton.setId(nomeUsuario);
        removerUsuarioButton.addClickListener((Button.ClickEvent event) -> {
            String nomeUsuarioBotao = event.getButton().getId();

            view.getUsersTable().removeItem(nomeUsuarioBotao);
            view.getUsersTable().refreshRowCache();
            Notification.show((mensagens.getString("Notificacao.Sucesso")), (mensagens.getString("Notificacao.ItemExcluidoSucesso")), Notification.TYPE_HUMANIZED_MESSAGE);// @ATENCAO

        });

        view.getUsersTable().addItem(new Object[]{nomeUsuario, sobrenomeUsuario, email, adm, removerUsuarioButton}, nomeUsuario);

        view.getUserNameTextField().setValue("");
        view.getUserSurnameTextField().setValue("");
        view.getEmailTextField().setValue("");
        view.getEmailConfirmTextField().setValue("");
        view.getUserAdmCheckBox().setValue(false);

    }

    /**
     * Evento disparado ao ser acionado o botão para efetuar a inclusão das
     * Coligadas na grid Obtém o nome, sobrenome e e-mail
     */
    @Override
    public void incluirColigadas() {

        String nomeColigada = view.getAssociatedNameTextField().getValue();
        view.getNationalEntityRegistrationAssociatedTextField().commit(); // Veja se dá certo
        view.getNationalEntityRegistrationAssociatedTextField().setValue("teste");
        String cnpjColigada = view.getNationalEntityRegistrationAssociatedTextField().getValue();

        System.out.println("cnpj: " + cnpjColigada);

        Button removerColigadasButton = new Button(mensagens.getString("SignupPresenter.removerButton.label"));
        removerColigadasButton.setId(nomeColigada);
        removerColigadasButton.addClickListener((Button.ClickEvent event) -> {
            String nomeColigadaBotao = event.getButton().getId();

            view.getAssociatedTable().removeItem(nomeColigadaBotao);
            view.getAssociatedTable().refreshRowCache();
            Notification.show((mensagens.getString("Notificacao.Sucesso")), (mensagens.getString("Notificacao.ItemExcluidoSucesso")), Notification.TYPE_HUMANIZED_MESSAGE); // @ATENCAO

        });

        view.getAssociatedTable().addItem(new Object[]{nomeColigada, cnpjColigada, removerColigadasButton}, nomeColigada);

        view.getAssociatedNameTextField().setValue("");
        view.getNationalEntityRegistrationAssociatedTextField().setValue("");

    }

    /**
     * Evento disparado ao ser acionado o botão para efetuar a inclusão das
     * Coligadas na grid Obtém o nome, sobrenome e e-mail
     */
    @Override
    public void incluirFiliais() {

        String nomeFilial = view.getSubsidiaryNameTextField().getValue();
        String cnpjFilial = view.getNationalEntityRegistrationSubsidiaryTextField().getValue();

        Button removerFiliaisButton = new Button(mensagens.getString("SignupPresenter.removerButton.label"));
        removerFiliaisButton.setId(nomeFilial);
        removerFiliaisButton.addClickListener((Button.ClickEvent event) -> {
            String nomeFilialBotao = event.getButton().getId();

            view.getSubsidiariesTable().removeItem(nomeFilialBotao);
            view.getSubsidiariesTable().refreshRowCache();
            Notification.show((mensagens.getString("Notificacao.Sucesso")), (mensagens.getString("Notificacao.ItemExcluidoSucesso")), Notification.TYPE_HUMANIZED_MESSAGE);// @ATENCAO

        });

        view.getSubsidiariesTable().addItem(new Object[]{nomeFilial, cnpjFilial, removerFiliaisButton}, nomeFilial);

        view.getSubsidiaryNameTextField().setValue("");
        view.getNationalEntityRegistrationSubsidiaryTextField().setValue("");

    }

    /**
     * Evento disparado ao ser acionado o botão selecionar o Estado da Empresa a
     * ser cadastrada
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
     * Evento disparado após ser selecionado o Estado da empresa a ser
     * cadastrada e carrega o comboBox com as cidades pertencentes a este estado
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

}
