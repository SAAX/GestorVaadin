/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.dao;

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
public class DepartamentoDAOCustomTest {
    
    public DepartamentoDAOCustomTest() {
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
     * Test of obterDepartamentosPorEmpresa method, of class DepartamentoDAOCustom.
     */
    @Test
    public void testObterDepartamentosPorEmpresa() {
        System.out.println("obterDepartamentosPorEmpresa");
        Empresa empresa = null;
        DepartamentoDAOCustom instance = null;
        List<Departamento> expResult = null;
        List<Departamento> result = instance.obterDepartamentosPorEmpresa(empresa);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
