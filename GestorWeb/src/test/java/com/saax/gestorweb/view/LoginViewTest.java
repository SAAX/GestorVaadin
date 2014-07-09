/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.view;

import com.saax.gestorweb.model.LoginModel;
import com.saax.gestorweb.presenter.LoginPresenter;
import com.saax.gestorweb.util.TestUtils;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rodrigo
 */
public class LoginViewTest {
    
    public LoginViewTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    
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
     * Test of setListener method, of class LoginView.
     */
    @Test
    public void testSetListener() {
        
        System.out.println("setListener");
        
        // Teste #1 : listener nulo
        LoginViewListener listener = null;
        LoginView instance = new LoginView();
        instance.setListener(listener);
        
        assertNull(instance.getListener());
        
        // Teste #2 : listener valido
        instance.setListener(new LoginPresenter(new LoginModel(), instance));
        
        assertNotNull(instance.getListener());
        
    }

    /**
     * Test of getLogin method, of class LoginView.
     */
    @Test
    public void testGetLogin() {
        
        System.out.println("getLogin");
        
        LoginView instance = new LoginView();
        
        
        // Teste #1 : Login valido
        String expResult = "joao@uol.com.br";
        instance.setLogin(expResult);
        String result = instance.getLoginTextField().getValue();
        
        assertEquals(expResult, result);
        
        // Teste #2 : Login nulo
        instance.setLogin(null);
        result = instance.getLoginTextField().getValue();
        
        assertNull(result);
        
    }

    /**
     * Test of getSenha method, of class LoginView.
     */
    @Test
    public void testGetSenha() {
        
        System.out.println("getSenha");
        
        LoginView instance = new LoginView();
        
        // Teste #1 : senha valida
        String expResult = "123";
        instance.setSenha(expResult);
        String result = instance.getSenhaTextField().getValue();
        
        assertEquals(expResult, result);
        
        // Teste #2 : senha nula
        instance.setSenha(null);
        result = instance.getSenhaTextField().getValue();
        
        assertNull(result);
        
    }

    /**
     * Test of getLembrarLoginCheckBox method, of class LoginView.
     */
    @Test
    public void testGetLembrarLoginCheckBox() {

        System.out.println("getLembrarLoginCheckBox");
        
        LoginView instance = new LoginView();
        
        // Teste #1 : lembrar sessao valida
        boolean expResult = true;
        instance.setLembrarSessao(expResult);
        boolean result = instance.getLembrarLoginCheckBox().getValue();
        assertEquals(expResult, result);
        

    }

    /**
     * Test of setSenha method, of class LoginView.
     */
    @Test
    public void testSetSenha() {

        System.out.println("setSenha");
        
        String senha = "123";
        LoginView instance = new LoginView();
        instance.setSenha(senha);
        
        assertEquals(senha, instance.getSenhaTextField().getValue());
    }

    /**
     * Test of apresentaErroUsuarioNaoExiste method, of class LoginView.
     */
    @Test
    public void testApresentaErroUsuarioNaoExiste() {
        System.out.println("apresentaErroUsuarioNaoExiste");
        LoginView instance = new LoginView();
        instance.apresentaErroUsuarioNaoExiste();
        
        assertNotNull(instance.getLoginTextField().getComponentError());
        
        
    }

    /**
     * Test of validate method, of class LoginView.
     */
    @Test
    public void testValidate() {

        System.out.println("validate");
       
        LoginView instance = new LoginView();
        
        // teste #1 : login invalido e senha valida
        // login deve ser um email
        // senha deve ter 3 ou mais caracteres e não ser nula
        instance.setLogin("abc");
        instance.setSenha("123");
        try {
            instance.validate();
            fail("Deveria lançar exceção de validação");
        } catch (Exception e) {
        }
        
        
        // teste #2 : login valido e senha valida
        instance.setLogin("abc@uol.com.br");
        instance.setSenha("123");
        try {
            instance.validate();
        } catch (Exception e) {
            System.out.println(instance.getLoginTextField().getErrorMessage().getFormattedHtmlMessage());
            fail("Nao deveria lançar exceção de validação");
            
        }
        
        // teste #3 : senha invalida e login valido
        instance.setLogin("abc@uol.com.br");
        instance.setSenha("ab");
        try {
            instance.validate();
            fail("Deveria lançar exceção de validação");
        } catch (Exception e) {
        }
        
        // teste #4 : senha invalida 2 e login valido
        instance.setLogin("abc@uol.com.br");
        instance.setSenha(null);
        try {
            instance.validate();
            fail("Deveria lançar exceção de validação");
        } catch (Exception e) {
        }
        
        // teste #5 : senha valida e login valido
        instance.setLogin("abc@uol.com.br");
        instance.setSenha("abc");
        try {
            instance.validate();
        } catch (Exception e) {
            fail("Nao deveria lançar exceção de validação");
        }
        
                
    }

    /**
     * Test of apresentaErroSenhaInvalida method, of class LoginView.
     */
    @Test
    public void testApresentaErroSenhaInvalida() {
        System.out.println("apresentaErroSenhaInvalida");
        LoginView instance = new LoginView();
        instance.apresentaErroSenhaInvalida();
        assertNotNull(instance.getSenhaTextField().getComponentError());
        
    }

    /**
     * Test of setLogin method, of class LoginView.
     */
    @Test
    public void testSetLogin() {
        System.out.println("setLogin");
        String login = "joao@uo.com.br";
        LoginView instance = new LoginView();
        instance.setLogin(login);
        assertEquals(login, instance.getLoginTextField().getValue());
    }

    /**
     * Test of setLembrarSessao method, of class LoginView.
     */
    @Test
    public void testSetLembrarSessao() {
        System.out.println("setLembrarSessao");
        boolean b = false;
        LoginView instance = new LoginView();
        instance.setLembrarSessao(b);
        assertEquals(false, instance.getLembrarLoginCheckBox().getValue());
    }

    /**
     * Test of getListener method, of class LoginView.
     */
    @Test
    public void testGetListener() {
        System.out.println("getListener");
        LoginView instance = new LoginView();
        LoginViewListener expResult = null;
        LoginViewListener result = instance.getListener();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLoginTextField method, of class LoginView.
     */
    @Test
    public void testGetLoginTextField() {
        System.out.println("getLoginTextField");
        LoginView instance = new LoginView();
        TextField expResult = null;
        TextField result = instance.getLoginTextField();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSenhaTextField method, of class LoginView.
     */
    @Test
    public void testGetSenhaTextField() {
        System.out.println("getSenhaTextField");
        LoginView instance = new LoginView();
        TextField expResult = null;
        TextField result = instance.getSenhaTextField();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
    
