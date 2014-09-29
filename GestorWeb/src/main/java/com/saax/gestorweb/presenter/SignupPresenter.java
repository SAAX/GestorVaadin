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
import com.saax.gestorweb.util.GestorException;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.view.SignupView;
import com.saax.gestorweb.view.SignupViewListener;
import com.vaadin.data.Item;
import com.vaadin.server.VaadinSession;
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * valida a aba de dados do usuário princiapal para cadastro
     *
     * @return true se os dados forem validos
     */
    private boolean validaDadosUsuarioPrincipal() {

        //String login = ""; // @TODO: obter email do usuário principal
        String login = (String) view.getEmailUsuarioTextField().getValue(); // @TODO: Obter da view
        //System.out.println("usuario " + login);

        // verifica se o usuário informado existe (login)
        if (model.verificaLoginExistente(login)) {

            // Exibe uma mensagem de erro indicando que este login (email) já 
            //existe no sistema e pergunta ao usuário se ele não quer recuperar sua senha
            view.apresentaAviso("SignupPresenter.mensagem.loginPreExistente");
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

        if (view.getTipoPessoaOptionGroup().getValue() == "Pessoa Física") {
            tipoPessoa = 'F';
        } else if (view.getTipoPessoaOptionGroup().getValue() == "Pessoa Jurídica") {
            tipoPessoa = 'J';
        } else {
            return false;
        }

        String cpf_cnpj = view.getCnpjCpfTextField().getValue();

        try {
            if (model.verificaEmpresaExistente(cpf_cnpj, tipoPessoa)) {

                // Exibe uma mensagem de erro indicando que esta empresa (pelo cnpj/cpf) já existe no sistema
                view.apresentaAviso("SignupPresenter.mensagem.empresaPreExistente", cpf_cnpj);
                return false;
            }
        } catch (GestorException ex) {
            Logger.getLogger(SignupPresenter.class.getName()).log(Level.SEVERE, null, ex);
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

        final Table empresasColigadasTable = view.getColigadasTable(); 
        Set<String> identificadoresEmpresasColigadas = new HashSet<>();

        for (Object itemID : empresasColigadasTable.getItemIds()) {

            Item linha = empresasColigadasTable.getItem(itemID);
            
            String cpfCnpjSubEmpresa = (String) linha.getItemProperty(mensagens.getString("SignupView.coligadasTable.email")).getValue(); // @ATENCAO
            String nomeSubEmpresa = (String) linha.getItemProperty(mensagens.getString("SignupView.coligadasTable.nome")).getValue(); // @ATENCAO
            char tipoPessoaSubEmpresa = 'J'; // @TODO: Obter da view
            if (cpfCnpjSubEmpresa != null) {
                try {
                    if (model.verificaEmpresaExistente(cpfCnpjSubEmpresa, tipoPessoaSubEmpresa)) {

                        // Exibe uma mensagem de erro indicando que esta empresa (pelo cnpj/cpf) já existe no sistema
                        view.apresentaAviso(mensagens.getString("SignupPresenter.mensagem.empresaPreExistente"), cpfCnpjSubEmpresa);
                        return false;
                    }
                } catch (GestorException ex) {
                    Logger.getLogger(SignupPresenter.class.getName()).log(Level.SEVERE, null, ex);
                    return false;
                }
            }
            // Valida se a sub empresa já não faz parte desta mesma empresa principal
            if (identificadoresEmpresasColigadas.contains(nomeSubEmpresa)) {
                // Exibe uma mensagem de erro indicando que esta sub empresa (pelo cnpj/cpf) 
                // foi cadastrada em duplicidade
                view.apresentaAviso(mensagens.getString("SignupPresenter.mensagem.empresaColigadaDuplicada"), cpfCnpjSubEmpresa);
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

        final Table filiaisTable = view.getFiliaisTable(); // @TODO: Obter a tabela de sub empresas
        Set<String> identificadoresFiliais = new HashSet<>();
        Set<String> nomesFiliais = new HashSet<>();

        for (Object itemID : filiaisTable.getItemIds()) {

            Item linha = filiaisTable.getItem(itemID);

            // Valida se a filial já não está cadastrada no sistema
            String cnpjFilial = (String) linha.getItemProperty(mensagens.getString("SignupView.filiaisTable.cnpj")).getValue(); // @ATENCAO
            String nomeFilial = (String) linha.getItemProperty(mensagens.getString("SignupView.filiaisTable.nome")).getValue(); // @ATENCAO

            if (StringUtils.isNotBlank(cnpjFilial)) {
                try {
                    if (model.verificaFilialExistente(cnpjFilial)) {

                        // Exibe uma mensagem de erro indicando que esta filial (pelo cnpj) já existe no sistema
                        view.apresentaAviso(mensagens.getString("SignupPresenter.mensagem.empresaPreExistente"), cnpjFilial);
                        return false;
                    }
                } catch (GestorException ex) {
                    Logger.getLogger(SignupPresenter.class.getName()).log(Level.SEVERE, null, ex);
                    return false;
                }

                // Valida se a filial já não faz parte desta mesma empresa principal pelo CNPJ
                if (identificadoresFiliais.contains(cnpjFilial)) {
                    // Exibe uma mensagem de erro indicando que esta filial (pelo cnpj/cpf) 
                    // foi cadastrada em duplicidade
                    view.apresentaAviso(mensagens.getString("SignupPresenter.mensagem.filialDuplicada"), cnpjFilial);
                    return false;

                }
                identificadoresFiliais.add(cnpjFilial);
            } else {
                // Valida se a filial já não faz parte desta mesma empresa principal pelo nome
                if (nomesFiliais.contains(nomeFilial)) {
                    // Exibe uma mensagem de erro indicando que esta filial (pelo nome) 
                    // foi cadastrada em duplicidade
                    view.apresentaAviso(mensagens.getString("SignupPresenter.mensagem.filialDuplicada"), nomeFilial);
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

        Table usuariosTable = view.getUsuariosTable(); 
        Set<String> identificadoresUsuarios = new HashSet<>();

        for (Object itemID : usuariosTable.getItemIds()) {

            Item linha = usuariosTable.getItem(itemID);

            // Valida se o usuários já não está cadastrado no sistema
            String emailUsuario = (String) linha.getItemProperty(mensagens.getString("SignupView.usuariosTable.email")).getValue(); // @ATENCAO
            System.out.println("usuario " + emailUsuario);
            if (model.verificaLoginExistente(emailUsuario)) {

                // Exibe uma mensagem de erro indicando que este usuario ja existe no sistema
                view.apresentaErroUsuarioExistente(mensagens.getString("SignupPresenter.mensagem.usuarioExistente"), emailUsuario);
                return false;
            }

            // Valida se o usuário já não está relacionado a esta mesma empresa
            if (identificadoresUsuarios.contains(emailUsuario)) {
                // Exibe uma mensagem de erro indicando que este usuario esta duplicado
                view.apresentaAviso(mensagens.getString("SignupPresenter.mensagem.usuarioDuplicado"), emailUsuario);
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
                view.getNomeTextField().getValue(),
                view.getSobrenomeTextField().getValue(),
                view.getEmailUsuarioTextField().getValue(),
                view.getSenhaTextField().getValue(), 
                null
        );
        

        // ---------------------------------------------------------------------
        // cria a empresa principal
        // ---------------------------------------------------------------------
        String nomeFantasia = view.getNomeFantasiaTextField().getValue();
        String razaosocial = view.getRazaoSocialTextField().getValue();
        String cpfCnpj = view.getCnpjCpfTextField().getValue();
        char tipoPessoa = '\0';   
        if (view.getTipoPessoaOptionGroup().getValue() == mensagens.getString("SignupView.pessoaFisicaCheckBox.label")) { // @ATENCAO
            tipoPessoa = 'F'; 
        } else if (view.getTipoPessoaOptionGroup().getValue() == mensagens.getString("SignupView.pessoaJuridicaCheckBox.label")) { // @ATENCAO
            tipoPessoa = 'J';
        } else {
            return null;
        }

        empresaPrincipal = model.criarNovaEmpresa(nomeFantasia, razaosocial, cpfCnpj, tipoPessoa, usuarioADM);

        // cria o endereco
        String logradouro = view.getLogradouroTextField().getValue();
        String numero = view.getNumeroTextField().getValue();
        String complemento = view.getComplementoTextField().getValue();
        String cep = view.getCepTextField().getValue();
        Cidade cidade = (Cidade) view.getCidadeComboBox().getValue();
        if (StringUtils.isNotBlank(logradouro)) {
            Endereco endereco = model.criarEndereco(logradouro, numero, complemento, cep, cidade, usuarioADM);
            model.relacionarEmpresaEndereco(empresaPrincipal, endereco);
        }

        model.relacionarUsuarioEmpresa(usuarioADM, empresaPrincipal, true, usuarioADM);
        

        // ---------------------------------------------------------------------
        // cria a lista de sub empresas 
        // ---------------------------------------------------------------------
        Table empresasColigadasTable = view.getColigadasTable();

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
        Table filiaisTable = view.getFiliaisTable();
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
        Table usuariosTable = view.getUsuariosTable(); 
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
        } catch (GestorException ex) {
            Logger.getLogger(SignupPresenter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        VaadinSession.getCurrent().setAttribute("usuarioLogado", empresa.getUsuarioInclusao());
        

    }

    /**
     * Evento disparado ao ser acionado o botão para efetuar a inclusão do
     * Usuário na tabela Obtém o nome, sobrenome e e-mail
     */
    public void addUsuarioButtonClicked() {

        String nomeUsuario = view.getNomeUsuarioTextField().getValue();
        String sobrenomeUsuario = view.getSobrenomeUsuarioTextField().getValue();
        String email = view.getEmailTextField().getValue();

    }

    /**
     * Evento disparado ao ser acionado o botão para efetuar a inclusão do
     * Usuário na grid Obtém o nome, sobrenome e e-mail
     */
    @Override
    public void incluirUsuario() {

        String nomeUsuario = view.getNomeUsuarioTextField().getValue();
        String sobrenomeUsuario = view.getSobrenomeUsuarioTextField().getValue();
        String email = view.getEmailTextField().getValue();

        //Verifica se Usuário é Administrador ou não
        Boolean usuarioAdm = view.getUsuarioAdmCheckBox().getValue();
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

            view.getUsuariosTable().removeItem(nomeUsuarioBotao);
            view.getUsuariosTable().refreshRowCache(); 
            Notification.show((mensagens.getString("Notificacao.Sucesso")), (mensagens.getString("Notificacao.ItemExcluidoSucesso")), Notification.TYPE_HUMANIZED_MESSAGE);// @ATENCAO

        });

        view.getUsuariosTable().addItem(new Object[]{nomeUsuario, sobrenomeUsuario, email, adm, removerUsuarioButton}, nomeUsuario);

        view.getNomeUsuarioTextField().setValue("");
        view.getSobrenomeUsuarioTextField().setValue("");
        view.getEmailTextField().setValue("");
        view.getConfirmaEmailTextField().setValue("");
        view.getUsuarioAdmCheckBox().setValue(false);

    }

    /**
     * Evento disparado ao ser acionado o botão para efetuar a inclusão das
     * Coligadas na grid Obtém o nome, sobrenome e e-mail
     */
    @Override
    public void incluirColigadas() {

        String nomeColigada = view.getNomeColigadaTextField().getValue();
        String cnpjColigada = view.getCnpjColigadaTextField().getValue();

        Button removerColigadasButton = new Button(mensagens.getString("SignupPresenter.removerButton.label"));
        removerColigadasButton.setId(nomeColigada);
        removerColigadasButton.addClickListener((Button.ClickEvent event) -> {
            String nomeColigadaBotao = event.getButton().getId();

            view.getColigadasTable().removeItem(nomeColigadaBotao);
            view.getColigadasTable().refreshRowCache();
            Notification.show((mensagens.getString("Notificacao.Sucesso")), (mensagens.getString("Notificacao.ItemExcluidoSucesso")), Notification.TYPE_HUMANIZED_MESSAGE); // @ATENCAO

        });

        view.getColigadasTable().addItem(new Object[]{nomeColigada, cnpjColigada, removerColigadasButton}, nomeColigada);

        view.getNomeColigadaTextField().setValue("");
        view.getCnpjColigadaTextField().setValue("");

    }

    /**
     * Evento disparado ao ser acionado o botão para efetuar a inclusão das
     * Coligadas na grid Obtém o nome, sobrenome e e-mail
     */
    @Override
    public void incluirFiliais() {

        String nomeFilial = view.getNomeFilialTextField().getValue();
        String cnpjFilial = view.getCnpjFilialTextField().getValue();

        Button removerFiliaisButton = new Button(mensagens.getString("SignupPresenter.removerButton.label"));
        removerFiliaisButton.setId(nomeFilial);
        removerFiliaisButton.addClickListener((Button.ClickEvent event) -> {
            String nomeFilialBotao = event.getButton().getId();

            view.getFiliaisTable().removeItem(nomeFilialBotao);
            view.getFiliaisTable().refreshRowCache();
            Notification.show((mensagens.getString("Notificacao.Sucesso")), (mensagens.getString("Notificacao.ItemExcluidoSucesso")), Notification.TYPE_HUMANIZED_MESSAGE);// @ATENCAO

        });

        view.getFiliaisTable().addItem(new Object[]{nomeFilial, cnpjFilial, removerFiliaisButton}, nomeFilial);

        view.getNomeFilialTextField().setValue("");
        view.getCnpjFilialTextField().setValue("");

    }
    
    /**
     * Evento disparado ao ser acionado o botão selecionar o Estado da Empresa a ser cadastrada
     * 
     */ 
    private void carregaComboEstado() {

        ComboBox estadoCombo = view.getEstadoComboBox();

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        List<Estado> estados = em.createNamedQuery("Estado.findAll").getResultList();

        for (Estado estado : estados) {

            estadoCombo.addItem(estado);
            estadoCombo.setItemCaption(estado, estado.getUf());

        }
    }

    /**
     * Evento disparado após ser selecionado o Estado da empresa a ser cadastrada
     * e carrega o comboBox com as cidades pertencentes a este estado
     */    
    @Override
    public void estadoSelecionado() {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        ComboBox cidadeCombo = view.getCidadeComboBox();

        cidadeCombo.removeAllItems();

        List<Cidade> cidades = em.createNamedQuery("Cidade.findByEstado")
                .setParameter("estado", view.getEstadoComboBox().getValue())
                .getResultList();

        for (Cidade cidade : cidades) {
            cidadeCombo.addItem(cidade);
            cidadeCombo.setItemCaption(cidade, cidade.getNome());
        }

    }

}
