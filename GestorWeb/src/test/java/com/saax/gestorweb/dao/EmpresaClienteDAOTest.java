/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.dao;

import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import java.util.List;
import javax.persistence.EntityManager;
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
public class EmpresaClienteDAOTest {
    
    public EmpresaClienteDAOTest() {
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
     * Test of getEntityManager method, of class EmpresaClienteDAO.
     */
    @Test
    public void testGetEntityManager() {
        System.out.println("getEntityManager");
        EmpresaClienteDAO instance = null;
        EntityManager expResult = null;
        EntityManager result = instance.getEntityManager();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of create method, of class EmpresaClienteDAO.
     */
    @Test
    public void testCreate() {
        System.out.println("create");
        EmpresaCliente empresaCliente = null;
        EmpresaClienteDAO instance = null;
        instance.create(empresaCliente);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of edit method, of class EmpresaClienteDAO.
     */
    @Test
    public void testEdit() throws Exception {
        System.out.println("edit");
        EmpresaCliente empresaCliente = null;
        EmpresaClienteDAO instance = null;
        instance.edit(empresaCliente);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of destroy method, of class EmpresaClienteDAO.
     */
    @Test
    public void testDestroy() throws Exception {
        System.out.println("destroy");
        Integer id = null;
        EmpresaClienteDAO instance = null;
        instance.destroy(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findEmpresaClienteEntities method, of class EmpresaClienteDAO.
     */
    @Test
    public void testFindEmpresaClienteEntities_0args() {
        System.out.println("findEmpresaClienteEntities");
        EmpresaClienteDAO instance = null;
        List<EmpresaCliente> expResult = null;
        List<EmpresaCliente> result = instance.findEmpresaClienteEntities();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findEmpresaClienteEntities method, of class EmpresaClienteDAO.
     */
    @Test
    public void testFindEmpresaClienteEntities_int_int() {
        System.out.println("findEmpresaClienteEntities");
        int maxResults = 0;
        int firstResult = 0;
        EmpresaClienteDAO instance = null;
        List<EmpresaCliente> expResult = null;
        List<EmpresaCliente> result = instance.findEmpresaClienteEntities(maxResults, firstResult);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findEmpresaCliente method, of class EmpresaClienteDAO.
     */
    @Test
    public void testFindEmpresaCliente() {
        System.out.println("findEmpresaCliente");
        Integer id = null;
        EmpresaClienteDAO instance = null;
        EmpresaCliente expResult = null;
        EmpresaCliente result = instance.findEmpresaCliente(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getEmpresaClienteCount method, of class EmpresaClienteDAO.
     */
    @Test
    public void testGetEmpresaClienteCount() {
        System.out.println("getEmpresaClienteCount");
        EmpresaClienteDAO instance = null;
        int expResult = 0;
        int result = instance.getEmpresaClienteCount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
