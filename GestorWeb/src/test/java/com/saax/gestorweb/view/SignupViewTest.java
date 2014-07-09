/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.view;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.TextField;
import java.util.ResourceBundle;
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
public class SignupViewTest {
    
    public SignupViewTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
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
     * Test of setListener method, of class SignupView.
     */
    @Test
    public void testSetListener() {
        System.out.println("setListener");
        SignupViewListener listener = null;
        SignupView instance = new SignupView();
        instance.setListener(listener);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMensagens method, of class SignupView.
     */
    @Test
    public void testGetMensagens() {
        System.out.println("getMensagens");
        SignupView instance = new SignupView();
        ResourceBundle expResult = null;
        ResourceBundle result = instance.getMensagens();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setMensagens method, of class SignupView.
     */
    @Test
    public void testSetMensagens() {
        System.out.println("setMensagens");
        ResourceBundle mensagens = null;
        SignupView instance = new SignupView();
        instance.setMensagens(mensagens);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getListener method, of class SignupView.
     */
    @Test
    public void testGetListener() {
        System.out.println("getListener");
        SignupView instance = new SignupView();
        SignupViewListener expResult = null;
        SignupViewListener result = instance.getListener();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNomeTextField method, of class SignupView.
     */
    @Test
    public void testGetNomeTextField() {
        System.out.println("getNomeTextField");
        SignupView instance = new SignupView();
        TextField expResult = null;
        TextField result = instance.getNomeTextField();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setNomeTextField method, of class SignupView.
     */
    @Test
    public void testSetNomeTextField() {
        System.out.println("setNomeTextField");
        TextField nomeTextField = null;
        SignupView instance = new SignupView();
        instance.setNomeTextField(nomeTextField);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSobrenomeTextField method, of class SignupView.
     */
    @Test
    public void testGetSobrenomeTextField() {
        System.out.println("getSobrenomeTextField");
        SignupView instance = new SignupView();
        TextField expResult = null;
        TextField result = instance.getSobrenomeTextField();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSobrenomeTextField method, of class SignupView.
     */
    @Test
    public void testSetSobrenomeTextField() {
        System.out.println("setSobrenomeTextField");
        TextField sobrenomeTextField = null;
        SignupView instance = new SignupView();
        instance.setSobrenomeTextField(sobrenomeTextField);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSenhaTextField method, of class SignupView.
     */
    @Test
    public void testGetSenhaTextField() {
        System.out.println("getSenhaTextField");
        SignupView instance = new SignupView();
        TextField expResult = null;
        TextField result = instance.getSenhaTextField();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSenhaTextField method, of class SignupView.
     */
    @Test
    public void testSetSenhaTextField() {
        System.out.println("setSenhaTextField");
        TextField senhaTextField = null;
        SignupView instance = new SignupView();
        instance.setSenhaTextField(senhaTextField);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAceitaTermosCheckBox method, of class SignupView.
     */
    @Test
    public void testGetAceitaTermosCheckBox() {
        System.out.println("getAceitaTermosCheckBox");
        SignupView instance = new SignupView();
        CheckBox expResult = null;
        CheckBox result = instance.getAceitaTermosCheckBox();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setAceitaTermosCheckBox method, of class SignupView.
     */
    @Test
    public void testSetAceitaTermosCheckBox() {
        System.out.println("setAceitaTermosCheckBox");
        CheckBox aceitaTermosCheckBox = null;
        SignupView instance = new SignupView();
        instance.setAceitaTermosCheckBox(aceitaTermosCheckBox);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRazaoSocialTextField method, of class SignupView.
     */
    @Test
    public void testGetRazaoSocialTextField() {
        System.out.println("getRazaoSocialTextField");
        SignupView instance = new SignupView();
        TextField expResult = null;
        TextField result = instance.getRazaoSocialTextField();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setRazaoSocialTextField method, of class SignupView.
     */
    @Test
    public void testSetRazaoSocialTextField() {
        System.out.println("setRazaoSocialTextField");
        TextField razaoSocialTextField = null;
        SignupView instance = new SignupView();
        instance.setRazaoSocialTextField(razaoSocialTextField);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNomeFantasiaTextField method, of class SignupView.
     */
    @Test
    public void testGetNomeFantasiaTextField() {
        System.out.println("getNomeFantasiaTextField");
        SignupView instance = new SignupView();
        TextField expResult = null;
        TextField result = instance.getNomeFantasiaTextField();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setNomeFantasiaTextField method, of class SignupView.
     */
    @Test
    public void testSetNomeFantasiaTextField() {
        System.out.println("setNomeFantasiaTextField");
        TextField nomeFantasiaTextField = null;
        SignupView instance = new SignupView();
        instance.setNomeFantasiaTextField(nomeFantasiaTextField);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCnpjTextField method, of class SignupView.
     */
    @Test
    public void testGetCnpjTextField() {
        System.out.println("getCnpjTextField");
        SignupView instance = new SignupView();
        TextField expResult = null;
        TextField result = instance.getCnpjTextField();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCnpjTextField method, of class SignupView.
     */
    @Test
    public void testSetCnpjTextField() {
        System.out.println("setCnpjTextField");
        TextField cnpjTextField = null;
        SignupView instance = new SignupView();
        instance.setCnpjTextField(cnpjTextField);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getEnderecoTextField method, of class SignupView.
     */
    @Test
    public void testGetEnderecoTextField() {
        System.out.println("getEnderecoTextField");
        SignupView instance = new SignupView();
        TextField expResult = null;
        TextField result = instance.getEnderecoTextField();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setEnderecoTextField method, of class SignupView.
     */
    @Test
    public void testSetEnderecoTextField() {
        System.out.println("setEnderecoTextField");
        TextField enderecoTextField = null;
        SignupView instance = new SignupView();
        instance.setEnderecoTextField(enderecoTextField);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNomeUsuarioTextField method, of class SignupView.
     */
    @Test
    public void testGetNomeUsuarioTextField() {
        System.out.println("getNomeUsuarioTextField");
        SignupView instance = new SignupView();
        TextField expResult = null;
        TextField result = instance.getNomeUsuarioTextField();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setNomeUsuarioTextField method, of class SignupView.
     */
    @Test
    public void testSetNomeUsuarioTextField() {
        System.out.println("setNomeUsuarioTextField");
        TextField nomeUsuarioTextField = null;
        SignupView instance = new SignupView();
        instance.setNomeUsuarioTextField(nomeUsuarioTextField);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getEmailTextField method, of class SignupView.
     */
    @Test
    public void testGetEmailTextField() {
        System.out.println("getEmailTextField");
        SignupView instance = new SignupView();
        TextField expResult = null;
        TextField result = instance.getEmailTextField();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setEmailTextField method, of class SignupView.
     */
    @Test
    public void testSetEmailTextField() {
        System.out.println("setEmailTextField");
        TextField emailTextField = null;
        SignupView instance = new SignupView();
        instance.setEmailTextField(emailTextField);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getConfirmaEmailTextField method, of class SignupView.
     */
    @Test
    public void testGetConfirmaEmailTextField() {
        System.out.println("getConfirmaEmailTextField");
        SignupView instance = new SignupView();
        TextField expResult = null;
        TextField result = instance.getConfirmaEmailTextField();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setConfirmaEmailTextField method, of class SignupView.
     */
    @Test
    public void testSetConfirmaEmailTextField() {
        System.out.println("setConfirmaEmailTextField");
        TextField confirmaEmailTextField = null;
        SignupView instance = new SignupView();
        instance.setConfirmaEmailTextField(confirmaEmailTextField);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of validate method, of class SignupView.
     */
    @Test
    public void testValidate() {
        System.out.println("validate");
        SignupView instance = new SignupView();
        instance.validate();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of apresentaAviso method, of class SignupView.
     */
    @Test
    public void testApresentaAviso() {
        System.out.println("apresentaAviso");
        String chave = "";
        SignupView instance = new SignupView();
        instance.apresentaAviso(chave);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTipoPessoa method, of class SignupView.
     */
    @Test
    public void testGetTipoPessoa() {
        System.out.println("getTipoPessoa");
        SignupView instance = new SignupView();
        char expResult = ' ';
        char result = instance.getTipoPessoa();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCpfTextField method, of class SignupView.
     */
    @Test
    public void testGetCpfTextField() {
        System.out.println("getCpfTextField");
        SignupView instance = new SignupView();
        TextField expResult = null;
        TextField result = instance.getCpfTextField();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
