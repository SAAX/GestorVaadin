package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.LoginModel;
import com.saax.gestorweb.model.UsuarioModel;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.Cipher;
import com.saax.gestorweb.util.CookiesManager;
import com.saax.gestorweb.view.LoginView;
import com.saax.gestorweb.view.LoginViewListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.SessionAttributesEnum;
import com.vaadin.ui.UI;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Login Presenter Esta classe é responsável captar todos os eventos que ocorrem
 * na View (LoginView) e dar o devido tratamento, utilizando para isto o modelo
 * (LoginModel)
 *
 * @author Rodrigo
 */
public class LoginPresenter implements Serializable, LoginViewListener {

    // Todo presenter mantem acesso à view e ao model
    private final transient LoginView view;
    private Tarefa taskToOpenWhenSucess;

    /**
     * Cria o presenter ligando o Model ao View
     *
     * @param view
     */
    public LoginPresenter(LoginView view) {

        this.view = view;

        view.setListener(this);

        loginPopUpAberto();

    }

    /**
     * Evento disparado ao ser acionado o botão para efetuar o login Obtém o
     * login e senha e autentica usando metodos do model
     */
    @Override
    public void loginButtonClicked() {

        String login = view.getLoginTextField().getValue();
        String senha = view.getSenhaTextField().getValue();

        try {

            // chama as validações adicionadas na criação dos campos
            view.validate();

        }
        catch (InvalidValueException e) {
            Logger.getLogger(LoginModel.class.getName()).log(Level.INFO, "Falha na validação de login", e);
            return;
        }

        // verifica se o usuário informado existe (login)
        if (!LoginModel.verificaLoginExistente(login)) {
            view.apresentaErroUsuarioNaoExiste();
            return;
        }

        Usuario u = LoginModel.getUsuario(login);

        // criptografa a senha informada, para comparação
        String senhaCriptografada;
        try {
            senhaCriptografada = new Cipher().md5Sum(senha);
        }
        catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LoginModel.class.getName()).log(Level.SEVERE, "Não encontrado algorítimo MD5", ex);
            return;
        }

        // verifica se a senha corresponde corretamente a senha cadastrada
        if (!senhaCriptografada.equals(u.getSenha())) {
            view.apresentaErroSenhaInvalida();
            return;
        }

        if (LoginModel.verificaPrimeiroLogin(login)) {
            view.apresentaConfirmacaoSenha();
            return;
        }

        // LOGIN BEM SUCEDIDO!
        // Configura o usuáio logado na seção
        GestorSession.setAttribute(SessionAttributesEnum.USUARIO_LOGADO, u);

        // verifica se o usuário quer gravar o login na sessão e grava o cookie
        if (view.getLembrarLoginCheckBox().getValue()) {
//        
//            CookiesManager cookieManager = (CookiesManager) GestorSession.getAttribute(SessionAttributesEnum.COOKIES_MANAGER);
//            cookieManager.setCookie(CookiesManager.GestorWebCookieEnum.NOME_USUARIO, view.getLoginTextField().getValue());

        } else {
//            CookiesManager cookieManager = (CookiesManager) GestorSession.getAttribute(SessionAttributesEnum.COOKIES_MANAGER);
//            cookieManager.destroyCookie(CookiesManager.GestorWebCookieEnum.NOME_USUARIO);
        }

        view.close();

        if (taskToOpenWhenSucess != null) {
            ((GestorMDI) UI.getCurrent()).carregarDashBoard(taskToOpenWhenSucess);
        } else {
            ((GestorMDI) UI.getCurrent()).carregarDashBoard();

        }

    }

    /**
     * Metodo executado ao exibir a tela (popup) de login
     */
    @Override
    public void loginPopUpAberto() {

        // verifica se o usuário já tem o login gravado em cookie
        CookiesManager cookieManager = (CookiesManager) GestorSession.getAttribute(SessionAttributesEnum.COOKIES_MANAGER);
        String login = cookieManager.getCookieValue(CookiesManager.GestorWebCookieEnum.NOME_USUARIO);

        if (login != null) {
            view.setLogin(login);
            view.setLembrarSessao(true);
        }
    }

    public void openTaskOnSucessLogin(Tarefa taskToOpenWhenSucess) {
        this.taskToOpenWhenSucess = taskToOpenWhenSucess;
    }

    @Override
    public void confirmaSenhaButtonClicked() {
        String login = view.getLoginTextField().getValue();
        String passwd = view.getSenhaField().getValue();
        String confirmPasswd = view.getConfirmacaoSenhaField().getValue();

        if (!passwd.equals(confirmPasswd)) {
            view.apresentaErroSenhasNaoCorrespondem();
            return;
        }

        UsuarioModel.alteraSenhaUsuario(login, passwd);
        UI.getCurrent().removeWindow(view.getSubWindowConfirmacaoSenha());
    }

}
