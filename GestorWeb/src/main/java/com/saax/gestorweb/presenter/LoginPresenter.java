package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.LoginModel;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.Cipher;
import com.saax.gestorweb.util.CookiesManager;
import com.saax.gestorweb.view.LoginView;
import com.saax.gestorweb.view.LoginViewListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.UI;
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
public final class LoginPresenter implements LoginViewListener {

    // Todo presenter mantem acesso à view e ao model
    private final LoginView view;
    private final LoginModel model;

    /**
     * Cria o presenter ligando o Model ao View
     *
     * @param model
     * @param view
     */
    public LoginPresenter(LoginModel model, LoginView view) {

        this.model = model;
        this.view = view;

        view.setListener(this);
        
        loginPopUpAberto();

    }

    /**
     * Evento disparado ao ser acionado o botão para efetuar o login 
     * Obtém o login e senha e autentica usando metodos do model
     */
    @Override
    public void loginButtonClicked() {

        String login = view.getLogin();
        String senha = view.getSenha();

        try {
            
            // chama as validações adicionadas na criação dos campos
            view.validate();

        } catch (InvalidValueException e) {
            Logger.getLogger(LoginModel.class.getName()).log(Level.INFO, "Falha na validação de login", e);
            return;
        }

        // verifica se o usuário informado existe (login)
        if (!model.verificaLoginExistente(login)) {
            view.apresentaErroUsuarioNaoExiste();
            return;
        }
        
        Usuario u = model.getUsuario(login);

        
        // criptografa a senha informada, para comparação
        String senhaCriptografada;
        try {
            senhaCriptografada = new Cipher().md5Sum(senha);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LoginModel.class.getName()).log(Level.SEVERE, "Não encontrado algorítimo MD5", ex);
            return ;
        }

        // verifica se a senha corresponde corretamente a senha cadastrada
        if (!senhaCriptografada.equals(u.getSenha())) {
            view.apresentaErroSenhaInvalida();
            return ;
        }

        // LOGIN BEM SUCEDIDO!
        
        // Configura o usuáio logado na seção
        ((GestorMDI) UI.getCurrent()).getUserData().setUsuarioLogado(u);

        // verifica se o usuário quer gravar o login na sessão e grava o cookie
        if (view.getLembrarLoginCheckBox().getValue()){
            
            CookiesManager cookieManager = ((GestorMDI) UI.getCurrent()).getUserData().getCookies();
            cookieManager.setCookie(CookiesManager.GestorWebCookieEnum.NOME_USUARIO, view.getLogin());

        } else {
            CookiesManager cookieManager = ((GestorMDI) UI.getCurrent()).getUserData().getCookies();
            cookieManager.destroyCookie(CookiesManager.GestorWebCookieEnum.NOME_USUARIO);
        }
        
        view.close();


    }

    /**
     * Metodo executado ao exibir a tela (popup) de login
     */
    @Override
    public void loginPopUpAberto() {
        
        // verifica se o usuário já tem o login gravado em cookie
        CookiesManager cookieManager = ((GestorMDI) UI.getCurrent()).getUserData().getCookies();
        String login = cookieManager.getCookieValue(CookiesManager.GestorWebCookieEnum.NOME_USUARIO);
        
        if (login!=null){
            view.setLogin(login);
            view.setLembrarSessao(true);
        }
    }

}
