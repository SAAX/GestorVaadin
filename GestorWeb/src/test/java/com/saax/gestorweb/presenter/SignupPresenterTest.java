/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.presenter;

import com.saax.gestorweb.dao.EmpresaDAO;
import com.saax.gestorweb.model.SignupModel;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.util.DBConnect;
import com.saax.gestorweb.util.GestorException;
import com.saax.gestorweb.util.PostgresConnection;
import com.saax.gestorweb.util.TestUtils;
import com.saax.gestorweb.view.SignupView;
import com.vaadin.data.Validator;
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
public class SignupPresenterTest {

    public SignupPresenterTest() {
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
     * Test of cancelButtonClicked method, of class SignupPresenter.
     */
    @Test
    public void testCancelButtonClicked() {
        System.out.println("cancelButtonClicked");
        SignupPresenter instance = null;
        instance.cancelButtonClicked();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of okButtonClicked method, of class SignupPresenter.
     */
    @Test
    public void testOkButtonClicked() throws GestorException {
        System.out.println("okButtonClicked");
        

        Object expResult; // Resultado esperado
        Object result; // Resultado real
        
        // ---------------------------------------------------------------------
        // Teste: Pressionar o botão OK sem preencher nada
        //      Resultado Esperado: Deverá ser disparada exceção: InvalidValueException 
        //          pelo metodo validate()
        // ---------------------------------------------------------------------
        
        // Configurações iniciais do teste
        SignupModel signupModel = new SignupModel();
        SignupView signupView = new SignupView();
        
        //o presenter liga model e view
        SignupPresenter instance = new SignupPresenter(signupModel, signupView);
        
        //adiciona a visualização à UI
        UI.getCurrent().addWindow(signupView);

        expResult = "Exceção foi disparada";
        
        try {
            
            instance.okButtonClicked();
            result = "Exceção NÃO foi disparada";
        } catch (Validator.InvalidValueException e) {
            result = "Exceção foi disparada";
        }
        
        assertEquals(expResult, result);

        // ---------------------------------------------------------------------
        // Teste: Pressionar o botão OK preenchendo:
        //              - nome
        //            E deixando em branco
        //              - sobrenomeTextField;
        //              - emailUsuarioTextField;
        //              - confirmaEmailUsuarioTextField;
        //              - senhaTextField;
        //              - aceitaTermosCheckBox;
        //
        //      Resultado Esperado: Deverá ser disparada exceção: InvalidValueException 
        //          pelo metodo validate()
        // ---------------------------------------------------------------------
        
        // Configurações iniciais do teste
        signupModel = new SignupModel();
        signupView = new SignupView();
        
        //o presenter liga model e view
        instance = new SignupPresenter(signupModel, signupView);
        
        //adiciona a visualização à UI
        UI.getCurrent().addWindow(signupView);

        expResult = "Exceção foi disparada";
        signupView.getNomeTextField().setValue("Rodrigo");
        
        try {
            
            instance.okButtonClicked();
            result = "Exceção NÃO foi disparada";
        } catch (Validator.InvalidValueException e) {
            result = "Exceção foi disparada";
        }
        
        assertEquals(expResult, result);

        // ---------------------------------------------------------------------
        // Teste: Pressionar o botão OK preenchendo:
        //              - nome
        //              - sobrenomeTextField;
        //            E deixando em branco
        //              - emailUsuarioTextField;
        //              - confirmaEmailUsuarioTextField;
        //              - senhaTextField;
        //              - aceitaTermosCheckBox;
        //
        //      Resultado Esperado: Deverá ser disparada exceção: InvalidValueException 
        //          pelo metodo validate()
        // ---------------------------------------------------------------------
        
        // Configurações iniciais do teste
        signupModel = new SignupModel();
        signupView = new SignupView();
        
        //o presenter liga model e view
        instance = new SignupPresenter(signupModel, signupView);
        
        //adiciona a visualização à UI
        UI.getCurrent().addWindow(signupView);

        expResult = "Exceção foi disparada";
        
        signupView.getNomeTextField().setValue("Rodrigo");
        signupView.getSobrenomeTextField().setValue("Moreira");
        
        try {
            
            instance.okButtonClicked();
            result = "Exceção NÃO foi disparada";
        } catch (Validator.InvalidValueException e) {
            result = "Exceção foi disparada";
        }
        
        assertEquals(expResult, result);
        
        // ---------------------------------------------------------------------
        // Teste: Pressionar o botão OK preenchendo:
        //              - nome
        //              - sobrenomeTextField;
        //              - emailUsuarioTextField; 
        //            E deixando em branco
        //              - confirmaEmailUsuarioTextField;
        //              - senhaTextField;
        //              - aceitaTermosCheckBox;
        //
        //      Resultado Esperado: Deverá ser disparada exceção: InvalidValueException 
        //          pelo metodo validate()
        // ---------------------------------------------------------------------
        
        // Configurações iniciais do teste
        signupModel = new SignupModel();
        signupView = new SignupView();
        
        //o presenter liga model e view
        instance = new SignupPresenter(signupModel, signupView);
        
        //adiciona a visualização à UI
        UI.getCurrent().addWindow(signupView);

        expResult = "Exceção foi disparada";
        
        signupView.getNomeTextField().setValue("Rodrigo");
        signupView.getSobrenomeTextField().setValue("Moreira");
        signupView.getEmailTextField().setValue("rodrigo.ccn2005@gmail.com");
        
        try {
            
            instance.okButtonClicked();
            result = "Exceção NÃO foi disparada";
        } catch (Validator.InvalidValueException e) {
            result = "Exceção foi disparada";
        }
        
        assertEquals(expResult, result);
        
        
       
        // @TODO: coloca testes para os demais campos
        
        // ---------------------------------------------------------------------
        // Teste: Pressionar o botão OK preenchendo todos os campos, mas 
        //              com email invalido
        //
        //      Resultado Esperado: Deverá ser disparada exceção: InvalidValueException 
        //          pelo metodo validate()
        // ---------------------------------------------------------------------
        
        // Configurações iniciais do teste
        signupModel = new SignupModel();
        signupView = new SignupView();
        
        //o presenter liga model e view
        instance = new SignupPresenter(signupModel, signupView);
        
        //adiciona a visualização à UI
        UI.getCurrent().addWindow(signupView);

        expResult = "Exceção foi disparada";
        
        // preencher todos os campos obrigatorios
        signupView.getNomeTextField().setValue("Rodrigo");
        signupView.getSobrenomeTextField().setValue("Moreira");
        signupView.getEmailTextField().setValue("rodrigo.ccn2005@gmail.com");
        signupView.getConfirmaEmailTextField().setValue("rodrigo.ccn2005@gmail.com");
        signupView.getSenhaTextField().setValue("123456");
        signupView.getAceitaTermosCheckBox().setValue(true);
        
        // setando um email invalido: sem @
        signupView.getEmailTextField().setValue("rodrigo.ccn2005gmail.com");
        
        try {
            
            instance.okButtonClicked();
            result = "Exceção NÃO foi disparada";
        } catch (Validator.InvalidValueException e) {
            result = "Exceção foi disparada";
        }
        
        assertEquals(expResult, result);
        
        // ---------------------------------------------------------------------
        // Teste: Pressionar o botão OK preenchendo todos os campos, mas 
        //              com email diferente do de confirmaçao
        //
        //      Resultado Esperado: Deverá ser disparada exceção: InvalidValueException 
        //          pelo metodo validate()
        // ---------------------------------------------------------------------
        
        // Configurações iniciais do teste
        signupModel = new SignupModel();
        signupView = new SignupView();
        
        //o presenter liga model e view
        instance = new SignupPresenter(signupModel, signupView);
        
        //adiciona a visualização à UI
        UI.getCurrent().addWindow(signupView);

        expResult = "Exceção foi disparada";
        
        // preencher todos os campos obrigatorios
        signupView.getNomeTextField().setValue("Rodrigo");
        signupView.getSobrenomeTextField().setValue("Moreira");
        signupView.getEmailTextField().setValue("rodrigo.ccn2005@gmail.com");
        signupView.getConfirmaEmailTextField().setValue("rodrigo.ccn2005@gmail.com");
        signupView.getSenhaTextField().setValue("123456");
        signupView.getAceitaTermosCheckBox().setValue(true);

        // email de confirmaçao errado
        signupView.getConfirmaEmailTextField().setValue("rodrigo.123456@gmail.com");
        
        try {
            
            instance.okButtonClicked();
            result = "Exceção NÃO foi disparada";
        } catch (Validator.InvalidValueException e) {
            result = "Exceção foi disparada";
        }
        
        assertEquals(expResult, result);
        
        
        // @TODO: tente imaginar mais testes possiveis, como nao aceitar os termos, ou informar o mesmo CNPJ para duas coligadas, etc.
        
        // ---------------------------------------------------------------------
        // Teste: Pressionar o botão OK preenchendo todos os campos, mas 
        //              com cnpj já existente
        //
        //      Resultado Esperado: Deverá ser disparada exceção: InvalidValueException 
        //          pelo metodo validate()
        // ---------------------------------------------------------------------
        
        // Configurações iniciais do teste
        signupModel = new SignupModel();
        signupView = new SignupView();
        
        //o presenter liga model e view
        instance = new SignupPresenter(signupModel, signupView);
        
        //adiciona a visualização à UI
        UI.getCurrent().addWindow(signupView);

        String cnpjRepetido = "12.123.345/0001-10";
        
        // 1o passo: cadastrar um empresa com o cnpj
        EmpresaDAO empresaDAO = new EmpresaDAO(PostgresConnection.getInstance().getEntityManagerFactory());

        Empresa e = new Empresa();
        e.setId(1);
        e.setCnpj(cnpjRepetido);
        e.setTipoPessoa('J');
        
        empresaDAO.create(e);

        
        // 2o. passo: preencher um cadastro com o mesmo cnpj
        // 1a. aba
        signupView.getNomeTextField().setValue("Rodrigo");
        signupView.getSobrenomeTextField().setValue("Moreira");
        signupView.getEmailTextField().setValue("rodrigo.ccn2005@gmail.com");
        signupView.getConfirmaEmailTextField().setValue("rodrigo.ccn2005@gmail.com");
        signupView.getSenhaTextField().setValue("123456");
        signupView.getAceitaTermosCheckBox().setValue(true);
        // 2a. aba
        signupView.getRazaoSocialTextField().setValue("SAAX");
        signupView.getNomeFantasiaTextField().setValue("SAAX");
        signupView.getTipoPessoaOptionGroup().select("Pessoa Jurídica");
        signupView.getCnpjCpfTextField().setValue(cnpjRepetido);
        signupView.getLogradouroTextField().setValue("Avenida Teste");
        
        // 3o. passo acionar o botão OK
        instance.okButtonClicked();

        // resultado esperado: a nova empresa não foi gravada
        expResult = "Nova empresa NÃO foi cadastrada";
        
        if (empresaDAO.findByRazaoSocial("SAAX")!=null){
            result = "Nova empresa foi cadastrada";
        } else {
            result = "Nova empresa NÃO foi cadastrada";
            
        }
        
        assertEquals(expResult, result);
        
        
    }

}
