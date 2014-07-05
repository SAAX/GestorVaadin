package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.SignupModel;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.FilialEmpresa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.GestorException;
import com.saax.gestorweb.view.SignupView;
import com.saax.gestorweb.view.SignupViewListener;
import com.vaadin.ui.UI;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

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

        String login = view.getEmailTextField().getValue();

        // verifica se o usuário informado existe (login)
        if (model.verificaLoginExistente(login)) {

            // Exibe uma mensagem de erro indicando que este login (email) já 
            //existe no sistema e pergunta ao usuário se ele não quer recuperar sua senha
            view.apresentaAviso("SignupPresenter.mensagem.loginPreExistente");
            return;
        }

        // verifica se a empresa (conta) informada já não existe no cadastro
        char tipoPessoa = view.getTipoPessoa();
        String cpf_cnpj = null;
        if (tipoPessoa == 'J') {
            cpf_cnpj = view.getCnpjTextField().getValue();

        } else if (tipoPessoa == 'F'){
            cpf_cnpj = view.getCpfTextField().getValue();
        }

        try {
            if (model.verificaEmpresaExistente(cpf_cnpj,tipoPessoa)) {
                
                // Exibe uma mensagem de erro indicando que esta empresa (pelo cnpj/cpf) já existe no sistema
                view.apresentaAviso("SignupPresenter.mensagem.empresaPreExistente");
                return;
            }
        } catch (GestorException ex) {
            Logger.getLogger(SignupPresenter.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Validações bem sucedidas !!!
        // procede com a gravação dos dados
        // cria o usuario
        Usuario usuarioADM = model.criarNovoUsuario(
                view.getNomeTextField().getValue(),
                view.getSobrenomeTextField().getValue(),
                view.getEmailTextField().getValue(),
                view.getSenhaTextField().getValue()
        );

        // cria as empresas
        // TODO:
        /*
        Empresa empresa = model.criarNovaEmpresa(login, login, cnpj, cnpj);
        model.relacionarUsuarioEmpresa(usuarioADM, empresa, true);

        // para cada sub empresa:
        Empresa subempresa = model.criarNovaEmpresa(login, login, cnpj, cnpj);
        model.relacionarEmpresaColigada(empresa, subempresa);

        // para cada filial:
        FilialEmpresa filialEmpresa = model.criarFilialEmpresa(cnpj);
        model.relacionarEmpresaFilial(empresa, filialEmpresa);

        // para cada usuario adicionado na grid
        Usuario usuario = model.criarNovoUsuario("", "", "", "");
        model.relacionarUsuarioEmpresa(usuario, empresa, true);

        // grava todos os dados (fazer em uma unica chamada para mater a transação)
        model.cadatrarNovoUsuario(usuarioADM);

        // Configura o usuáio logado na seção
        ((GestorMDI) UI.getCurrent()).getUserData().setUsuarioLogado(usuarioADM);

        view.close();

        ((GestorMDI) UI.getCurrent()).carregarDashBoard();
*/
    }

}
