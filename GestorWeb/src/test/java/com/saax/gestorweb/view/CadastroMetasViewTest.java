/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.view;

import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import java.util.Collection;
import java.util.List;
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
public class CadastroMetasViewTest {
    
    public CadastroMetasViewTest() {
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
     * Test of setListener method, of class CadastroMetasView.
     */
    @Test
    public void testSetListener() {
        System.out.println("setListener");
        CadastroMetasViewListener listener = null;
        CadastroMetasView instance = new CadastroMetasView();
        instance.setListener(listener);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of carregarComboEmpresas method, of class CadastroMetasView.
     */
    @Test
    public void testCarregarComboEmpresas() {
        System.out.println("carregarComboEmpresas");
        Collection<Empresa> empresas = null;
        CadastroMetasView instance = new CadastroMetasView();
        instance.carregarComboEmpresas(empresas);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of selecionarEmpresa method, of class CadastroMetasView.
     */
    @Test
    public void testSelecionarEmpresa() {
        System.out.println("selecionarEmpresa");
        Empresa empresa = null;
        CadastroMetasView instance = new CadastroMetasView();
        instance.selecionarEmpresa(empresa);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of desabilitarSelecaoEmpresas method, of class CadastroMetasView.
     */
    @Test
    public void testDesabilitarSelecaoEmpresas() {
        System.out.println("desabilitarSelecaoEmpresas");
        CadastroMetasView instance = new CadastroMetasView();
        instance.desabilitarSelecaoEmpresas();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of carregarComboDepartamentos method, of class CadastroMetasView.
     */
    @Test
    public void testCarregarComboDepartamentos() {
        System.out.println("carregarComboDepartamentos");
        List<Departamento> departamentos = null;
        CadastroMetasView instance = new CadastroMetasView();
        instance.carregarComboDepartamentos(departamentos);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of exibirMensagemComboDepartamentos method, of class CadastroMetasView.
     */
    @Test
    public void testExibirMensagemComboDepartamentos() {
        System.out.println("exibirMensagemComboDepartamentos");
        String mensagem = "";
        CadastroMetasView instance = new CadastroMetasView();
        instance.exibirMensagemComboDepartamentos(mensagem);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of desabilitarSelecaoDepartamentos method, of class CadastroMetasView.
     */
    @Test
    public void testDesabilitarSelecaoDepartamentos() {
        System.out.println("desabilitarSelecaoDepartamentos");
        CadastroMetasView instance = new CadastroMetasView();
        instance.desabilitarSelecaoDepartamentos();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
