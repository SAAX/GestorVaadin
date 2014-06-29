package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.dao.UsuarioDAOCustom;
import com.saax.gestorweb.model.LoginModel;
import com.saax.gestorweb.model.LoginModelTest;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.Cipher;
import com.saax.gestorweb.util.DBConnect;
import com.saax.gestorweb.util.PostgresConnection;
import com.saax.gestorweb.util.TestUtils;
import com.saax.gestorweb.view.LoginView;
import com.vaadin.data.Validator;
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
import org.junit.Rule;
import org.junit.rules.ExpectedException;

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
        TestUtils.assertUIisSetted();
        

    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
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
        UsuarioDAOCustom dao = new UsuarioDAOCustom(PostgresConnection.getInstance().getEntityManagerFactory());
        try {
            dao.create(new Usuario(1, "Joao", "da Silva","joao@uol.com.br", new Cipher().md5Sum("123")));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LoginModelTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        // Cria o pop up de login (model e view)
        LoginModel loginModel = new LoginModel();
        LoginView loginView = new LoginView();
        
        TestUtils.assertUIisSetted();

        // O presenter liga model e view
        LoginPresenter instance = new LoginPresenter(loginModel, loginView);

        // ---------------------------------------------------------------------
        // Teste #1: login sem informar usuário e senha
        //      exp: usuario não é setado
        // ---------------------------------------------------------------------
        instance.loginButtonClicked();
        Usuario result = ((GestorMDI) UI.getCurrent()).getUserData().getUsuarioLogado();
        
        Assert.assertNull(result);
        
        // ---------------------------------------------------------------------
        // Teste #2: login sem informar senha
        //      exp: usuario não é setado
        // ---------------------------------------------------------------------

        loginView.setLogin("joao@uol.com.br");
        
        instance.loginButtonClicked();
        result = ((GestorMDI) UI.getCurrent()).getUserData().getUsuarioLogado();
        
        Assert.assertNull(result);
        
        // ---------------------------------------------------------------------
        // Teste #3: login com usuario inexistente
        //      exp: usuario não é setado
        // ---------------------------------------------------------------------
        loginView.setLogin("abc@uol.com.br");
        instance.loginButtonClicked();
        result = ((GestorMDI) UI.getCurrent()).getUserData().getUsuarioLogado();
        
        Assert.assertNull(result);
        
        // ---------------------------------------------------------------------
        // Teste #4: login com senha inválida
        //      exp: usuario não é setado
        // ---------------------------------------------------------------------
        loginView.setLogin("joao@uol.com.br");
        loginView.setSenha("124");
        instance.loginButtonClicked();
        result = ((GestorMDI) UI.getCurrent()).getUserData().getUsuarioLogado();
        
        Assert.assertNull(result);
        
        // TODO: colocar mais testes de falha.
        
        // ---------------------------------------------------------------------
        // Teste #9: login bem sucedido
        //      exp: usuario não é setado
        // ---------------------------------------------------------------------
        loginView.setLogin("joao@uol.com.br");
        loginView.setSenha("123");
        instance.loginButtonClicked();
        result = ((GestorMDI) UI.getCurrent()).getUserData().getUsuarioLogado();
        
        Assert.assertNotNull(result);
        

        

        

    }

    /**
     * Test of loginPopUpAberto method, of class LoginPresenter.
     */
    @Ignore
    @Test
    public void testLoginPopUpAberto() {
        System.out.println("loginPopUpAberto");
        LoginPresenter instance = null;
//        instance.loginPopUpAberto();
        // TODO review the generated test code and remove the default call to fail.
  //      fail("The test case is a prototype.");
    }
    
}
