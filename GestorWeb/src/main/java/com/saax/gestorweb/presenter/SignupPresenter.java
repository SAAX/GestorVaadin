package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.SignupModel;
import com.saax.gestorweb.model.datamodel.Cidade;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.Endereco;
import com.saax.gestorweb.model.datamodel.FilialEmpresa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.GestorException;
import com.saax.gestorweb.view.SignupView;
import com.saax.gestorweb.view.SignupViewListener;
import com.vaadin.data.Item;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getUserData().getMensagens();

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

    @Override
    public void cancelButtonClicked() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void okButtonClicked() {

        // valida o preenchimento dos campos obrigatórios
        view.validate();

        // ---------------------------------------------------------------------
        // valida a aba de dados do usuário princiapal
        // ---------------------------------------------------------------------
        String login = ""; // @TODO: obter email do usuário principal

        // verifica se o usuário informado existe (login)
        if (model.verificaLoginExistente(login)) {

            // Exibe uma mensagem de erro indicando que este login (email) já 
            //existe no sistema e pergunta ao usuário se ele não quer recuperar sua senha
            view.apresentaAviso("SignupPresenter.mensagem.loginPreExistente");
            return;
        }

        // ---------------------------------------------------------------------
        // valida dados da conta (empresa principal)
        // ---------------------------------------------------------------------
        // verifica se a empresa (conta) informada já não existe no cadastro
        char tipoPessoa;
        if (view.getPessoaFisicaCheckBox().getValue()) {
            tipoPessoa = 'F';
        } else if (view.getPessoaJuridicaCheckBox().getValue()) {
            tipoPessoa = 'J';
        } else {
            return;
        }

        String cpf_cnpj = view.getCnpjCpfTextField().getValue();

        try {
            if (model.verificaEmpresaExistente(cpf_cnpj, tipoPessoa)) {

                // Exibe uma mensagem de erro indicando que esta empresa (pelo cnpj/cpf) já existe no sistema
                view.apresentaAviso("SignupPresenter.mensagem.empresaPreExistente", cpf_cnpj);
                return;
            }
        } catch (GestorException ex) {
            Logger.getLogger(SignupPresenter.class.getName()).log(Level.SEVERE, null, ex);
        }

        // ---------------------------------------------------------------------
        // valida dados das sub empresas coligadas a empresa principal
        // ---------------------------------------------------------------------
        final Table filiaist = null; // @TODO: Obter a tabela de sub empresas
        Set<String> identificadoresEmpresasColigadas = new HashSet<>();

        filiaist.getItemIds().stream().forEach((itemID) -> {

            Item linha = filiaist.getItem(itemID);

            // Valida se a sub empresa já não está cadastrada no sistema
            String cpfCnpjSubEmpresa = (String) linha.getItemProperty("?????????").getValue(); // @TODO: Obter da view
            char tipoPessoaSubEmpresa = 'J'; // @TODO: Obter da view
            try {
                if (model.verificaEmpresaExistente(cpfCnpjSubEmpresa, tipoPessoaSubEmpresa)) {

                    // Exibe uma mensagem de erro indicando que esta empresa (pelo cnpj/cpf) já existe no sistema
                    view.apresentaAviso("SignupPresenter.mensagem.empresaPreExistente", cpfCnpjSubEmpresa);
                    return;
                }
            } catch (GestorException ex) {
                Logger.getLogger(SignupPresenter.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Valida se a sub empresa já não faz parte desta mesma empresa principal
            if (identificadoresEmpresasColigadas.contains(cpfCnpjSubEmpresa)) {
                // Exibe uma mensagem de erro indicando que esta sub empresa (pelo cnpj/cpf) 
                // foi cadastrada em duplicidade
                view.apresentaAviso("SignupPresenter.mensagem.empresaColigadaDuplicada", cpfCnpjSubEmpresa);
                return;

            }
            identificadoresEmpresasColigadas.add(cpfCnpjSubEmpresa);

        });

        // ---------------------------------------------------------------------
        // valida dados das filiais à empresa principal
        // ---------------------------------------------------------------------
        final Table filiaisTable = null; // @TODO: Obter a tabela de sub empresas
        Set<String> identificadoresFiliais = new HashSet<>();
        Set<String> nomesFiliais = new HashSet<>();

        filiaisTable.getItemIds().stream().forEach((itemID) -> {

            Item linha = filiaisTable.getItem(itemID);

            // Valida se a filial já não está cadastrada no sistema
            String cnpjFilial = (String) linha.getItemProperty("?????????").getValue(); // @TODO: Obter da view
            String nomeFilial = (String) linha.getItemProperty("?????????").getValue(); // @TODO: Obter da view

            if (StringUtils.isNotBlank(cnpjFilial)) {
                try {
                    if (model.verificaFilialExistente(cnpjFilial)) {

                        // Exibe uma mensagem de erro indicando que esta filial (pelo cnpj) já existe no sistema
                        view.apresentaAviso("SignupPresenter.mensagem.empresaPreExistente", cnpjFilial);
                        return;
                    }
                } catch (GestorException ex) {
                    Logger.getLogger(SignupPresenter.class.getName()).log(Level.SEVERE, null, ex);
                }

                // Valida se a filial já não faz parte desta mesma empresa principal pelo CNPJ
                if (identificadoresFiliais.contains(cnpjFilial)) {
                    // Exibe uma mensagem de erro indicando que esta filial (pelo cnpj/cpf) 
                    // foi cadastrada em duplicidade
                    view.apresentaAviso("SignupPresenter.mensagem.filialDuplicada", cnpjFilial);
                    return;

                }
                identificadoresFiliais.add(cnpjFilial);
            } else {
                // Valida se a filial já não faz parte desta mesma empresa principal pelo nome
                if (nomesFiliais.contains(nomeFilial)) {
                    // Exibe uma mensagem de erro indicando que esta filial (pelo nome) 
                    // foi cadastrada em duplicidade
                    view.apresentaAviso("SignupPresenter.mensagem.filialDuplicada", nomeFilial);
                    return;

                }
                nomesFiliais.add(nomeFilial);

            }
        });

        // ---------------------------------------------------------------------
        // valida dados dos usuarios adicionados
        // ---------------------------------------------------------------------
        Table usuariosTable = null; // @TODO: Obter a tabela de sub empresas
        Set<String> identificadoresUsuarios = new HashSet<>();

        usuariosTable.getItemIds().stream().forEach((itemID) -> {

            Item linha = usuariosTable.getItem(itemID);

            // Valida se o usuários já não está cadastrado no sistema
            String emailUsuario = (String) linha.getItemProperty("?????????").getValue(); // @TODO: Obter da view

            if (model.verificaLoginExistente(emailUsuario)) {

                // Exibe uma mensagem de erro indicando que esta filial (pelo cnpj) já existe no sistema
                view.apresentaAviso("SignupPresenter.mensagem.usuarioExistente", emailUsuario);
                return;
            }

            // Valida se o usuário já não está relacionado a esta mesma empresa
            if (identificadoresUsuarios.contains(emailUsuario)) {
                // Exibe uma mensagem de erro indicando que este usuario esta duplicado
                view.apresentaAviso("SignupPresenter.mensagem.usuarioDuplicado", emailUsuario);
                return;

            }
            identificadoresUsuarios.add(emailUsuario);
        });

        // ---------------------------------------------------------------------
        // Validações bem sucedidas !!!
        // procede com a gravação dos dados
        // ---------------------------------------------------------------------
        
        
        // ---------------------------------------------------------------------
        // cria o usuario principal
        // ---------------------------------------------------------------------
        Usuario usuarioADM = model.criarNovoUsuario(
                view.getNomeTextField().getValue(),
                view.getSobrenomeTextField().getValue(),
                view.getEmailTextField().getValue(),
                view.getSenhaTextField().getValue()
        );

        // ---------------------------------------------------------------------
        // cria a empresa principal
        // ---------------------------------------------------------------------
        String nomeFantasia = view.getNomeFantasiaTextField().getValue();
        String razaosocial = view.getRazaoSocialTextField().getValue();
        String cpfCnpj = view.getCnpjCpfTextField().getValue();
        if (view.getPessoaFisicaCheckBox().getValue()) {
            tipoPessoa = 'F';
        } else if (view.getPessoaJuridicaCheckBox().getValue()) {
            tipoPessoa = 'J';
        } else {
            return;
        }

        Empresa empresa = model.criarNovaEmpresa(nomeFantasia, razaosocial, cpfCnpj, tipoPessoa);

        // cria o endereco
        String logradouro = view.getLogradouroTextField().getValue();
        String numero = view.getNumeroTextField().getValue();
        String complemento = view.getComplementoTextField().getValue();
        String cep = view.getCepTextField().getValue();
        Cidade cidade = null; // @TODO: cidade deve ser um combo
        if (StringUtils.isNotBlank(logradouro)) {
            Endereco endereco = model.criarEndereco(logradouro, numero, complemento, cep, cidade);
            model.relacionarEmpresaEndereco(empresa, endereco);
        }
        model.relacionarUsuarioEmpresa(usuarioADM, empresa, true);

        // ---------------------------------------------------------------------
        // cria a lista de sub empresas 
        // ---------------------------------------------------------------------
        filiaist.getItemIds().stream().forEach((itemID) -> {

            Item linhaEmpresaColigada = filiaist.getItem(itemID);

            String nomeFantasiaEmpresaColigada = (String) linhaEmpresaColigada.getItemProperty("?????????").getValue(); // @TODO: Obter da view
            String razaosocialEmpresaColigada = (String) linhaEmpresaColigada.getItemProperty("?????????").getValue(); // @TODO: Obter da view
            String cpfCnpjEmpresaColigada = (String) linhaEmpresaColigada.getItemProperty("?????????").getValue(); // @TODO: Obter da view
            char tipoPessoaEmpresaColigada = (char) linhaEmpresaColigada.getItemProperty("?????????").getValue(); // @TODO: Obter da view

            Empresa subempresa = model.criarNovaEmpresa(nomeFantasiaEmpresaColigada,
                    razaosocialEmpresaColigada, cpfCnpjEmpresaColigada, tipoPessoaEmpresaColigada);
            model.relacionarEmpresaColigada(empresa, subempresa);

        });

        // ---------------------------------------------------------------------
        // cria a lista de filiais
        // ---------------------------------------------------------------------
        filiaisTable.getItemIds().stream().forEach((itemID) -> {

            Item linha = filiaisTable.getItem(itemID);

            String nomeFilial = (String) linha.getItemProperty("?????????").getValue(); // @TODO: Obter da view
            String cnpjFilial = (String) linha.getItemProperty("?????????").getValue(); // @TODO: Obter da view

            FilialEmpresa filialEmpresa = model.criarFilialEmpresa(nomeFilial, cnpjFilial);
            model.relacionarEmpresaFilial(empresa, filialEmpresa);

        });

        // ---------------------------------------------------------------------
        // cria a lista de usuarios
        // ---------------------------------------------------------------------
        
        usuariosTable.getItemIds().stream().forEach((itemID) -> {

            Item linha = usuariosTable.getItem(itemID);
            
            String nome = (String) linha.getItemProperty("Nome").getValue();
            String sobreNome = (String) linha.getItemProperty("Sobrenome").getValue();
            String email = (String) linha.getItemProperty("E-mail").getValue();
            boolean administrador = ((String) linha.getItemProperty("Administrador").getValue()).equals("SIM");

            Usuario usuario = model.criarNovoUsuario(nome, sobreNome, email);
            model.relacionarUsuarioEmpresa(usuario, empresa, administrador);

        });

        // grava todos os dados (fazer em uma unica chamada para mater a transação)
        model.cadatrarNovoUsuario(usuarioADM);

        // Configura o usuáio logado na seção
        ((GestorMDI) UI.getCurrent()).getUserData().setUsuarioLogado(usuarioADM);

        view.close();

        ((GestorMDI) UI.getCurrent()).carregarDashBoard();
    }

}
