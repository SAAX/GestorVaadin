/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.dao;

import com.saax.gestorweb.model.datamodel.Empresa;
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
public class EmpresaDAOTest {
    
    public EmpresaDAOTest() {
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
     * Test of getEntityManager method, of class EmpresaDAO.
     */
    @Test
    public void testGetEntityManager() {
        System.out.println("getEntityManager");
        EmpresaDAO instance = null;
        EntityManager expResult = null;
        EntityManager result = instance.getEntityManager();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of create method, of class EmpresaDAO.
     */
    @Test
    public void testCreate() {
        System.out.println("create");
        Empresa empresa = null;
        EmpresaDAO instance = null;
        instance.create(empresa);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of edit method, of class EmpresaDAO.
     */
    @Test
    public void testEdit() throws Exception {
        System.out.println("edit");
        Empresa empresa = null;
        EmpresaDAO instance = null;
        instance.edit(empresa);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of destroy method, of class EmpresaDAO.
     */
    @Test
    public void testDestroy() throws Exception {
        System.out.println("destroy");
        Integer id = null;
        EmpresaDAO instance = null;
        instance.destroy(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findEmpresaEntities method, of class EmpresaDAO.
     */
    @Test
    public void testFindEmpresaEntities_0args() {
        System.out.println("findEmpresaEntities");
        EmpresaDAO instance = null;
        List<Empresa> expResult = null;
        List<Empresa> result = instance.findEmpresaEntities();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findEmpresaEntities method, of class EmpresaDAO.
     */
    @Test
    public void testFindEmpresaEntities_int_int() {
        System.out.println("findEmpresaEntities");
        int maxResults = 0;
        int firstResult = 0;
        EmpresaDAO instance = null;
        List<Empresa> expResult = null;
        List<Empresa> result = instance.findEmpresaEntities(maxResults, firstResult);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findEmpresa method, of class EmpresaDAO.
     */
    @Test
    public void testFindEmpresa() {
        System.out.println("findEmpresa");
        Integer id = null;
        EmpresaDAO instance = null;
        Empresa expResult = null;
        Empresa result = instance.findEmpresa(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getEmpresaCount method, of class EmpresaDAO.
     */
    @Test
    public void testGetEmpresaCount() {
        System.out.println("getEmpresaCount");
        EmpresaDAO instance = null;
        int expResult = 0;
        int result = instance.getEmpresaCount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
