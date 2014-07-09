/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.dao;

import com.saax.gestorweb.model.datamodel.Empresa;
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
public class EmpresaDAOCustomTest {
    
    public EmpresaDAOCustomTest() {
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
     * Test of findByCNPJ method, of class EmpresaDAOCustom.
     */
    @Test
    public void testFindByCNPJ() {
        System.out.println("findByCNPJ");
        String cnpj = "";
        EmpresaDAOCustom instance = null;
        Empresa expResult = null;
        Empresa result = instance.findByCNPJ(cnpj);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findByCPF method, of class EmpresaDAOCustom.
     */
    @Test
    public void testFindByCPF() {
        System.out.println("findByCPF");
        String cpf = "";
        EmpresaDAOCustom instance = null;
        Empresa expResult = null;
        Empresa result = instance.findByCPF(cpf);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
