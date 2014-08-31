package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.dao.UsuarioDAO;
import com.saax.gestorweb.model.LoginModel;
import com.saax.gestorweb.model.LoginModelTest;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.Cipher;
import com.saax.gestorweb.util.CookiesManager;
import com.saax.gestorweb.util.DBConnect;
import com.saax.gestorweb.util.PostgresConnection;
import com.saax.gestorweb.util.TestUtils;
import com.saax.gestorweb.view.LoginView;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;

/**
 *
 * @author rodrigo
 */
public class LoginPresenterTest {

    public LoginPresenterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        DBConnect.getInstance().assertConnection();

    }

    @AfterClass
    public static void tearDownClass() {
    }

    UI ui = null;

    @Before
    public void setUp() {
        ui = new TestUtils().configureUI();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of loginButtonClicked method, of class LoginPresenter.
     */
    @Test
    public void testLoginButtonClicked() {
        System.out.println("loginButtonClicked");

        // Cria um usuario para teste
        UsuarioDAO dao = new UsuarioDAO(PostgresConnection.getInstance().getEntityManagerFactory());
        try {
            Usuario u = new Usuario(1);
            u.setLogin("joao@uol.com.br");
            u.setNome("Joao");
            u.setSobrenome("da Silva");
            u.setSenha(new Cipher().md5Sum("123"));
            dao.create(u);
            
            
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LoginModelTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Cria o pop up de login (model e view)
        LoginModel loginModel = new LoginModel();
        LoginView loginView = new LoginView();

        // O presenter liga model e view
        LoginPresenter instance = new LoginPresenter(loginModel, loginView);

        // ---------------------------------------------------------------------
        // Teste #1: login sem informar usuário e senha
        //      exp: usuario não é setado
        // ---------------------------------------------------------------------
        instance.loginButtonClicked();
        Usuario result = (Usuario) VaadinSession.getCurrent().getAttribute("usuarioLogado");

        Assert.assertNull(result);

        // ---------------------------------------------------------------------
        // Teste #2: login sem informar senha
        //      exp: usuario não é setado
        // ---------------------------------------------------------------------
        loginView.setLogin("joao@uol.com.br");
        loginView.setSenha(null);

        instance.loginButtonClicked();
        result = (Usuario) VaadinSession.getCurrent().getAttribute("usuarioLogado");

        Assert.assertNull(result);

        // ---------------------------------------------------------------------
        // Teste #3: login sem informar login
        //      exp: usuario não é setado
        // ---------------------------------------------------------------------
        loginView.setLogin(null);
        loginView.setSenha("123456");

        instance.loginButtonClicked();
        result = (Usuario) VaadinSession.getCurrent().getAttribute("usuarioLogado");

        Assert.assertNull(result);

        // ---------------------------------------------------------------------
        // Teste #4: login com usuario inexistente
        //      exp: usuario não é setado
        // ---------------------------------------------------------------------
        loginView.setLogin("abc@uol.com.br");
        instance.loginButtonClicked();
        result = (Usuario) VaadinSession.getCurrent().getAttribute("usuarioLogado");

        Assert.assertNull(result);

        // ---------------------------------------------------------------------
        // Teste #5: login com senha inválida
        //      exp: usuario não é setado
        // ---------------------------------------------------------------------
        loginView.setLogin("joao@uol.com.br");
        loginView.setSenha("124");
        instance.loginButtonClicked();
        result = (Usuario) VaadinSession.getCurrent().getAttribute("usuarioLogado");

        Assert.assertNull(result);

        // ---------------------------------------------------------------------
        // Teste #6: login bem sucedido
        //      exp: usuario é setado
        // ---------------------------------------------------------------------
        loginView.setLogin("joao@uol.com.br");
        loginView.setSenha("123");
        instance.loginButtonClicked();
        result = (Usuario) VaadinSession.getCurrent().getAttribute("usuarioLogado");

        Assert.assertNotNull(result);

    }

    /**
     * Test of loginPopUpAberto method, of class LoginPresenter.
     */
    @Ignore
    @Test
    public void testLoginPopUpAberto() {
        System.out.println("loginPopUpAberto");
        
        // Cria o pop up de login (model e view)
        LoginModel loginModel = new LoginModel();
        LoginView loginView = new LoginView();

        // O presenter liga model e view
        LoginPresenter instance = new LoginPresenter(loginModel, loginView);
        
        String expResult = "abc@uol.com";
        
        // coloca um login no cookie
        CookiesManager cookieManager = (CookiesManager) VaadinSession.getCurrent().getAttribute("cookieManager");
        cookieManager.setCookie(CookiesManager.GestorWebCookieEnum.NOME_USUARIO, expResult);
        
        instance.loginPopUpAberto();

        String result = loginView.getLoginTextField().getValue();
        Assert.assertEquals(expResult, result);
        
    }

}
