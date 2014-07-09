/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
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
public class CadastroMetasModelTest {
    
    public CadastroMetasModelTest() {
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
     * Test of obterListaDepartamentosAtivos method, of class CadastroMetasModel.
     */
    @Test
    public void testObterListaDepartamentosAtivos() throws Exception {
        System.out.println("obterListaDepartamentosAtivos");
        Empresa empresa = null;
        CadastroMetasModel instance = new CadastroMetasModel();
        List<Departamento> expResult = null;
        List<Departamento> result = instance.obterListaDepartamentosAtivos(empresa);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of obterListaEmpresasUsuarioLogado method, of class CadastroMetasModel.
     */
    @Test
    public void testObterListaEmpresasUsuarioLogado() {
        System.out.println("obterListaEmpresasUsuarioLogado");
        CadastroMetasModel instance = new CadastroMetasModel();
        List<Empresa> expResult = null;
        List<Empresa> result = instance.obterListaEmpresasUsuarioLogado();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
