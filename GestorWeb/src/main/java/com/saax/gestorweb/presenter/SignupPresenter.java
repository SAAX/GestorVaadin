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

    /**
     * valida a aba de dados do usuário princiapal para cadastro
     *
     * @return true se os dados forem validos
     */
    private boolean validaDadosUsuarioPrincipal() {

        String login = ""; // @TODO: obter email do usuário principal

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
        
//        if (view.getPessoaFisicaCheckBox().getValue()) {
//            tipoPessoa = 'F';
//        } else if (view.getPessoaJuridicaCheckBox().getValue()) {
//            tipoPessoa = 'J';
//        } else {
//            return false;
//        }

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
    private boolean validaDadosEmpresasColigadas(){
                
        
        final Table empresasColigadasTable = null; // @TODO: Obter a tabela de sub empresas
        Set<String> identificadoresEmpresasColigadas = new HashSet<>();

        for (Object itemID : empresasColigadasTable.getItemIds()) {
            
            Item linha = empresasColigadasTable.getItem(itemID);
            // Valida se a sub empresa já não está cadastrada no sistema
            String cpfCnpjSubEmpresa = (String) linha.getItemProperty(view.getCnpjCpfTextField()).getValue(); // @TODO: Obter da view
            char tipoPessoaSubEmpresa = 'J'; // @TODO: Obter da view
            try {
                if (model.verificaEmpresaExistente(cpfCnpjSubEmpresa, tipoPessoaSubEmpresa)) {

                    // Exibe uma mensagem de erro indicando que esta empresa (pelo cnpj/cpf) já existe no sistema
                    view.apresentaAviso("SignupPresenter.mensagem.empresaPreExistente", cpfCnpjSubEmpresa);
                    return false;
                }
            } catch (GestorException ex) {
                Logger.getLogger(SignupPresenter.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }

            // Valida se a sub empresa já não faz parte desta mesma empresa principal
            if (identificadoresEmpresasColigadas.contains(cpfCnpjSubEmpresa)) {
                // Exibe uma mensagem de erro indicando que esta sub empresa (pelo cnpj/cpf) 
                // foi cadastrada em duplicidade
                view.apresentaAviso("SignupPresenter.mensagem.empresaColigadaDuplicada", cpfCnpjSubEmpresa);
                return false;

            }
            identificadoresEmpresasColigadas.add(cpfCnpjSubEmpresa);
        }
        
        return true;
        
    }

    /**
     * valida dados das filiais a empresa principal
     * 
     * @return true se os dados forem validos
     */
    private boolean validaDadosFiliais(){
       

        final Table filiaisTable = null; // @TODO: Obter a tabela de sub empresas
        Set<String> identificadoresFiliais = new HashSet<>();
        Set<String> nomesFiliais = new HashSet<>();

        for (Object itemID : filiaisTable.getItemIds()) {
            
            Item linha = filiaisTable.getItem(itemID);

            // Valida se a filial já não está cadastrada no sistema
            String cnpjFilial = (String) linha.getItemProperty(view.getCnpjFilialTextField()).getValue(); // @TODO: Obter da view
            String nomeFilial = (String) linha.getItemProperty(view.getNomeFilialTextField()).getValue(); // @TODO: Obter da view

            if (StringUtils.isNotBlank(cnpjFilial)) {
                try {
                    if (model.verificaFilialExistente(cnpjFilial)) {

                        // Exibe uma mensagem de erro indicando que esta filial (pelo cnpj) já existe no sistema
                        view.apresentaAviso("SignupPresenter.mensagem.empresaPreExistente", cnpjFilial);
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
                    view.apresentaAviso("SignupPresenter.mensagem.filialDuplicada", cnpjFilial);
                    return false;

                }
                identificadoresFiliais.add(cnpjFilial);
            } else {
                // Valida se a filial já não faz parte desta mesma empresa principal pelo nome
                if (nomesFiliais.contains(nomeFilial)) {
                    // Exibe uma mensagem de erro indicando que esta filial (pelo nome) 
                    // foi cadastrada em duplicidade
                    view.apresentaAviso("SignupPresenter.mensagem.filialDuplicada", nomeFilial);
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
    private boolean validaDadosDemaisUsuarios(){
        
        Table usuariosTable = null; // @TODO: Obter a tabela de sub empresas
        Set<String> identificadoresUsuarios = new HashSet<>();

        for (Object itemID : usuariosTable.getItemIds()) {
            

            Item linha = usuariosTable.getItem(itemID);

            // Valida se o usuários já não está cadastrado no sistema
            String emailUsuario = (String) linha.getItemProperty(view.getEmailUsuarioTextField()).getValue(); // @TODO: Obter da view

            if (model.verificaLoginExistente(emailUsuario)) {

                // Exibe uma mensagem de erro indicando que esta filial (pelo cnpj) já existe no sistema
                view.apresentaAviso("SignupPresenter.mensagem.usuarioExistente", emailUsuario);
                return false;
            }

            // Valida se o usuário já não está relacionado a esta mesma empresa
            if (identificadoresUsuarios.contains(emailUsuario)) {
                // Exibe uma mensagem de erro indicando que este usuario esta duplicado
                view.apresentaAviso("SignupPresenter.mensagem.usuarioDuplicado", emailUsuario);
                return false;

            }
            identificadoresUsuarios.add(emailUsuario);
        }
        
        return true;

    }
    
    
    /**
     * Obtem todos os dados da view necessários para montar o cadastro inicial
     * com a empresa principal + coligadas e filiais + usuarios
     * @return objeto empresa
     */
    private Empresa buildConta(){
        
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
        char tipoPessoa = '\0';
//        if (view.getPessoaFisicaCheckBox().getValue()) {
//            tipoPessoa = 'F';
//        } else if (view.getPessoaJuridicaCheckBox().getValue()) {
//            tipoPessoa = 'J';
//        } else { 
//            return null;
//        }

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
        Table empresasColigadasTable = null; // @TODO: pegar da view
        empresasColigadasTable.getItemIds().stream().forEach((itemID) -> {

            Item linhaEmpresaColigada = empresasColigadasTable.getItem(itemID);

            String nomeFantasiaEmpresaColigada = (String) linhaEmpresaColigada.getItemProperty("?????????").getValue(); // @TODO: Obter da view
            String razaosocialEmpresaColigada = (String) linhaEmpresaColigada.getItemProperty("?????????").getValue(); // @TODO: Obter da view
            String cpfCnpjEmpresaColigada = (String) linhaEmpresaColigada.getItemProperty(view.getCnpjColigadaTextField()).getValue(); // @TODO: Obter da view
            char tipoPessoaEmpresaColigada = (char) linhaEmpresaColigada.getItemProperty("?????????").getValue(); // @TODO: Obter da view

            Empresa subempresa = model.criarNovaEmpresa(nomeFantasiaEmpresaColigada,
                    razaosocialEmpresaColigada, cpfCnpjEmpresaColigada, tipoPessoaEmpresaColigada);
            model.relacionarEmpresaColigada(empresa, subempresa);

        });

        // ---------------------------------------------------------------------
        // cria a lista de filiais
        // ---------------------------------------------------------------------
        Table filiaisTable = null; // @TODO: pegar da view
        filiaisTable.getItemIds().stream().forEach((itemID) -> {

            Item linha = filiaisTable.getItem(itemID);

            String nomeFilial = (String) linha.getItemProperty(view.getNomeFilialTextField()).getValue(); // @TODO: Obter da view
            String cnpjFilial = (String) linha.getItemProperty(view.getCnpjFilialTextField()).getValue(); // @TODO: Obter da view

            FilialEmpresa filialEmpresa = model.criarFilialEmpresa(nomeFilial, cnpjFilial);
            model.relacionarEmpresaFilial(empresa, filialEmpresa);

        });

        // ---------------------------------------------------------------------
        // cria a lista de usuarios
        // ---------------------------------------------------------------------
        Table usuariosTable = null; // @TODO: pegar da view
        usuariosTable.getItemIds().stream().forEach((itemID) -> {

            Item linha = usuariosTable.getItem(itemID);

            String nome = (String) linha.getItemProperty("Nome").getValue();
            String sobreNome = (String) linha.getItemProperty("Sobrenome").getValue();
            String email = (String) linha.getItemProperty("E-mail").getValue();
            boolean administrador = ((String) linha.getItemProperty("Administrador").getValue()).equals("SIM");

            Usuario usuario = model.criarNovoUsuario(nome, sobreNome, email);
            model.relacionarUsuarioEmpresa(usuario, empresa, administrador);

        });
        
        return empresa;
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
        Empresa conta = buildConta();

        // grava todos os dados (fazer em uma unica chamada para mater a transação)
        model.criarNovaConta(conta);
/*
        // Configura o usuáio logado na seção
        ((GestorMDI) UI.getCurrent()).getUserData().setUsuarioLogado(usuarioADM);
*/
        view.close();

        ((GestorMDI) UI.getCurrent()).carregarDashBoard();
    }
    /**
     * Evento disparado ao ser acionado o botão para efetuar a inclusão do Usuário na tabela 
     * Obtém o nome, sobrenome e e-mail
     */
    
    public void addUsuarioButtonClicked() {
        
        String nomeUsuario = view.getNomeUsuarioTextField().getValue();
        String sobrenomeUsuario = view.getSobrenomeUsuarioTextField().getValue();
        String email = view.getEmailTextField().getValue();
        
      
         
    }
    
     /**
     * Evento disparado ao ser acionado o botão para efetuar a inclusão do Usuário na grid 
     * Obtém o nome, sobrenome e e-mail
     */
    @Override
    public void incluirUsuario() {
        
        String nomeUsuario = view.getNomeUsuarioTextField().getValue();
        String sobrenomeUsuario = view.getSobrenomeUsuarioTextField().getValue();
        String email = view.getEmailTextField().getValue();
        
        //Verifica se Usuário é Administrador ou não
        Boolean usuarioAdm =  view.getUsuarioAdmCheckBox().getValue();
        String Adm ="";
        if (usuarioAdm == true){
            Adm = "SIM";
        } else{
            Adm = "NÃO";
        }
        //
                                       
        view.getUsuariosTable().addItem(new Object[] {1, nomeUsuario,sobrenomeUsuario,email, Adm, "Editar", "Remover"}, new Integer(1));
       
         
    }
    
     /**
     * Evento disparado ao ser acionado o botão para efetuar a inclusão das Coligadas na grid 
     * Obtém o nome, sobrenome e e-mail
     */
    //@Override
    public void incluirColigadas() {
        
        String nomeColigada = view.getNomeColigadaTextField().getValue();
        String cnpjColigada = view.getCnpjColigadaTextField().getValue();
       
                                       
        view.getColigadasTable().addItem(new Object[] {nomeColigada,cnpjColigada,"Editar", "Remover"}, new Integer(1));
       
         
    }
    
      /**
     * Evento disparado ao ser acionado o botão para efetuar a inclusão das Coligadas na grid 
     * Obtém o nome, sobrenome e e-mail
     */
    //@Override
    public void incluirFiliais() {
        
        String nomeFilial = view.getNomeFilialTextField().getValue();
        String cnpjFilial = view.getCnpjFilialTextField().getValue();
       
                                       
        view.getFiliaisTable().addItem(new Object[] {nomeFilial,cnpjFilial,"Editar", "Remover"}, new Integer(1));
        
       
         
    }

     
   
}
