/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.view;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.OptionGroup;
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
     * Test of getNome method, of class SignupView.
     */
    @Test
    public void testGetNome() {
        System.out.println("getNome");
        SignupView instance = new SignupView();
        String expResult = "";
        String result = instance.getNome();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSobrenome method, of class SignupView.
     */
    @Test
    public void testGetSobrenome() {
        System.out.println("getSobrenome");
        SignupView instance = new SignupView();
        String expResult = "";
        String result = instance.getSobrenome();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSenha method, of class SignupView.
     */
    @Test
    public void testGetSenha() {
        System.out.println("getSenha");
        SignupView instance = new SignupView();
        String expResult = "";
        String result = instance.getSenha();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAceitaTermos method, of class SignupView.
     */
    @Test
    public void testGetAceitaTermos() {
        System.out.println("getAceitaTermos");
        SignupView instance = new SignupView();
        CheckBox expResult = null;
        CheckBox result = instance.getAceitaTermos();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCnpjCpfTextField method, of class SignupView.
     */
    @Test
    public void testGetCnpjCpfTextField() {
        System.out.println("getCnpjCpfTextField");
        SignupView instance = new SignupView();
        TextField expResult = null;
        TextField result = instance.getCnpjCpfTextField();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCnpjCpfTextField method, of class SignupView.
     */
    @Test
    public void testSetCnpjCpfTextField() {
        System.out.println("setCnpjCpfTextField");
        TextField cnpjTextField = null;
        SignupView instance = new SignupView();
        instance.setCnpjCpfTextField(cnpjTextField);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLogradouroTextField method, of class SignupView.
     */
    @Test
    public void testGetLogradouroTextField() {
        System.out.println("getLogradouroTextField");
        SignupView instance = new SignupView();
        TextField expResult = null;
        TextField result = instance.getLogradouroTextField();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setLogradouroTextField method, of class SignupView.
     */
    @Test
    public void testSetLogradouroTextField() {
        System.out.println("setLogradouroTextField");
        TextField enderecoTextField = null;
        SignupView instance = new SignupView();
        instance.setLogradouroTextField(enderecoTextField);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNumeroTextField method, of class SignupView.
     */
    @Test
    public void testGetNumeroTextField() {
        System.out.println("getNumeroTextField");
        SignupView instance = new SignupView();
        TextField expResult = null;
        TextField result = instance.getNumeroTextField();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setNumeroTextField method, of class SignupView.
     */
    @Test
    public void testSetNumeroTextField() {
        System.out.println("setNumeroTextField");
        TextField numeroTextField = null;
        SignupView instance = new SignupView();
        instance.setNumeroTextField(numeroTextField);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getComplementoTextField method, of class SignupView.
     */
    @Test
    public void testGetComplementoTextField() {
        System.out.println("getComplementoTextField");
        SignupView instance = new SignupView();
        TextField expResult = null;
        TextField result = instance.getComplementoTextField();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setComplementoTextField method, of class SignupView.
     */
    @Test
    public void testSetComplementoTextField() {
        System.out.println("setComplementoTextField");
        TextField complementoTextField = null;
        SignupView instance = new SignupView();
        instance.setComplementoTextField(complementoTextField);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBairroTextField method, of class SignupView.
     */
    @Test
    public void testGetBairroTextField() {
        System.out.println("getBairroTextField");
        SignupView instance = new SignupView();
        TextField expResult = null;
        TextField result = instance.getBairroTextField();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setBairroTextField method, of class SignupView.
     */
    @Test
    public void testSetBairroTextField() {
        System.out.println("setBairroTextField");
        TextField bairroTextField = null;
        SignupView instance = new SignupView();
        instance.setBairroTextField(bairroTextField);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }


   
    /**
     * Test of getCepTextField method, of class SignupView.
     */
    @Test
    public void testGetCepTextField() {
        System.out.println("getCepTextField");
        SignupView instance = new SignupView();
        TextField expResult = null;
        TextField result = instance.getCepTextField();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCepTextField method, of class SignupView.
     */
    @Test
    public void testSetCepTextField() {
        System.out.println("setCepTextField");
        TextField cepTextField = null;
        SignupView instance = new SignupView();
        instance.setCepTextField(cepTextField);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUsuarioAdmCheckBox method, of class SignupView.
     */
    @Test
    public void testGetUsuarioAdmCheckBox() {
        System.out.println("getUsuarioAdmCheckBox");
        SignupView instance = new SignupView();
        CheckBox expResult = null;
        CheckBox result = instance.getUsuarioAdmCheckBox();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setUsuarioAdmCheckBox method, of class SignupView.
     */
    @Test
    public void testSetUsuarioAdmCheckBox() {
        System.out.println("setUsuarioAdmCheckBox");
        CheckBox usuarioAdmCheckBox = null;
        SignupView instance = new SignupView();
        instance.setUsuarioAdmCheckBox(usuarioAdmCheckBox);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getEmailUsuarioTextField method, of class SignupView.
     */
    @Test
    public void testGetEmailUsuarioTextField() {
        System.out.println("getEmailUsuarioTextField");
        SignupView instance = new SignupView();
        TextField expResult = null;
        TextField result = instance.getEmailUsuarioTextField();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setEmailUsuarioTextField method, of class SignupView.
     */
    @Test
    public void testSetEmailUsuarioTextField() {
        System.out.println("setEmailUsuarioTextField");
        TextField emailUsuarioTextField = null;
        SignupView instance = new SignupView();
        instance.setEmailUsuarioTextField(emailUsuarioTextField);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getConfirmaEmailUsuarioTextField method, of class SignupView.
     */
    @Test
    public void testGetConfirmaEmailUsuarioTextField() {
        System.out.println("getConfirmaEmailUsuarioTextField");
        SignupView instance = new SignupView();
        TextField expResult = null;
        TextField result = instance.getConfirmaEmailUsuarioTextField();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setConfirmaEmailUsuarioTextField method, of class SignupView.
     */
    @Test
    public void testSetConfirmaEmailUsuarioTextField() {
        System.out.println("setConfirmaEmailUsuarioTextField");
        TextField confirmaEmailUsuarioTextField = null;
        SignupView instance = new SignupView();
        instance.setConfirmaEmailUsuarioTextField(confirmaEmailUsuarioTextField);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTipoPessoaOptionGroup method, of class SignupView.
     */
    @Test
    public void testGetTipoPessoaOptionGroup() {
        System.out.println("getTipoPessoaOptionGroup");
        SignupView instance = new SignupView();
        OptionGroup expResult = null;
        OptionGroup result = instance.getTipoPessoaOptionGroup();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTipoPessoaOptionGroup method, of class SignupView.
     */
    @Test
    public void testSetTipoPessoaOptionGroup() {
        System.out.println("setTipoPessoaOptionGroup");
        OptionGroup tipoPessoaOptionGroup = null;
        SignupView instance = new SignupView();
        instance.setTipoPessoaOptionGroup(tipoPessoaOptionGroup);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCidadeComboBox method, of class SignupView.
     */
    @Test
    public void testGetCidadeComboBox() {
        System.out.println("getCidadeComboBox");
        SignupView instance = new SignupView();
        ComboBox expResult = null;
        ComboBox result = instance.getCidadeComboBox();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCidadeComboBox method, of class SignupView.
     */
    @Test
    public void testSetCidadeComboBox() {
        System.out.println("setCidadeComboBox");
        ComboBox cidadeComboBox = null;
        SignupView instance = new SignupView();
        instance.setCidadeComboBox(cidadeComboBox);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getEstadoComboBox method, of class SignupView.
     */
    @Test
    public void testGetEstadoComboBox() {
        System.out.println("getEstadoComboBox");
        SignupView instance = new SignupView();
        ComboBox expResult = null;
        ComboBox result = instance.getEstadoComboBox();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setEstadoComboBox method, of class SignupView.
     */
    @Test
    public void testSetEstadoComboBox() {
        System.out.println("setEstadoComboBox");
        ComboBox estadoComboBox = null;
        SignupView instance = new SignupView();
        instance.setEstadoComboBox(estadoComboBox);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNomeColigadaTextField method, of class SignupView.
     */
    @Test
    public void testGetNomeColigadaTextField() {
        System.out.println("getNomeColigadaTextField");
        SignupView instance = new SignupView();
        TextField expResult = null;
        TextField result = instance.getNomeColigadaTextField();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setNomeColigadaTextField method, of class SignupView.
     */
    @Test
    public void testSetNomeColigadaTextField() {
        System.out.println("setNomeColigadaTextField");
        TextField nomeColigadaTextField = null;
        SignupView instance = new SignupView();
        instance.setNomeColigadaTextField(nomeColigadaTextField);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCnpjColigadaTextField method, of class SignupView.
     */
    @Test
    public void testGetCnpjColigadaTextField() {
        System.out.println("getCnpjColigadaTextField");
        SignupView instance = new SignupView();
        TextField expResult = null;
        TextField result = instance.getCnpjColigadaTextField();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCnpjColigadaTextField method, of class SignupView.
     */
    @Test
    public void testSetCnpjColigadaTextField() {
        System.out.println("setCnpjColigadaTextField");
        TextField cnpjColigadaTextField = null;
        SignupView instance = new SignupView();
        instance.setCnpjColigadaTextField(cnpjColigadaTextField);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSobrenomeUsuarioTextField method, of class SignupView.
     */
    @Test
    public void testGetSobrenomeUsuarioTextField() {
        System.out.println("getSobrenomeUsuarioTextField");
        SignupView instance = new SignupView();
        TextField expResult = null;
        TextField result = instance.getSobrenomeUsuarioTextField();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSobrenomeUsuarioTextField method, of class SignupView.
     */
    @Test
    public void testSetSobrenomeUsuarioTextField() {
        System.out.println("setSobrenomeUsuarioTextField");
        TextField sobrenomeUsuarioTextField = null;
        SignupView instance = new SignupView();
        instance.setSobrenomeUsuarioTextField(sobrenomeUsuarioTextField);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getEmpresaAtivaCheckBox method, of class SignupView.
     */
    @Test
    public void testGetEmpresaAtivaCheckBox() {
        System.out.println("getEmpresaAtivaCheckBox");
        SignupView instance = new SignupView();
        CheckBox expResult = null;
        CheckBox result = instance.getEmpresaAtivaCheckBox();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setEmpresaAtivaCheckBox method, of class SignupView.
     */
    @Test
    public void testSetEmpresaAtivaCheckBox() {
        System.out.println("setEmpresaAtivaCheckBox");
        CheckBox empresaAtivaCheckBox = null;
        SignupView instance = new SignupView();
        instance.setEmpresaAtivaCheckBox(empresaAtivaCheckBox);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
