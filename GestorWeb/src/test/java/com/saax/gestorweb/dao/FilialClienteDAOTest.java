/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.dao;

import com.saax.gestorweb.model.datamodel.FilialCliente;
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
public class FilialClienteDAOTest {
    
    public FilialClienteDAOTest() {
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
     * Test of getEntityManager method, of class FilialClienteDAO.
     */
    @Test
    public void testGetEntityManager() {
        System.out.println("getEntityManager");
        FilialClienteDAO instance = null;
        EntityManager expResult = null;
        EntityManager result = instance.getEntityManager();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of create method, of class FilialClienteDAO.
     */
    @Test
    public void testCreate() {
        System.out.println("create");
        FilialCliente filialCliente = null;
        FilialClienteDAO instance = null;
        instance.create(filialCliente);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of edit method, of class FilialClienteDAO.
     */
    @Test
    public void testEdit() throws Exception {
        System.out.println("edit");
        FilialCliente filialCliente = null;
        FilialClienteDAO instance = null;
        instance.edit(filialCliente);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of destroy method, of class FilialClienteDAO.
     */
    @Test
    public void testDestroy() throws Exception {
        System.out.println("destroy");
        Integer id = null;
        FilialClienteDAO instance = null;
        instance.destroy(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findFilialClienteEntities method, of class FilialClienteDAO.
     */
    @Test
    public void testFindFilialClienteEntities_0args() {
        System.out.println("findFilialClienteEntities");
        FilialClienteDAO instance = null;
        List<FilialCliente> expResult = null;
        List<FilialCliente> result = instance.findFilialClienteEntities();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findFilialClienteEntities method, of class FilialClienteDAO.
     */
    @Test
    public void testFindFilialClienteEntities_int_int() {
        System.out.println("findFilialClienteEntities");
        int maxResults = 0;
        int firstResult = 0;
        FilialClienteDAO instance = null;
        List<FilialCliente> expResult = null;
        List<FilialCliente> result = instance.findFilialClienteEntities(maxResults, firstResult);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findFilialCliente method, of class FilialClienteDAO.
     */
    @Test
    public void testFindFilialCliente() {
        System.out.println("findFilialCliente");
        Integer id = null;
        FilialClienteDAO instance = null;
        FilialCliente expResult = null;
        FilialCliente result = instance.findFilialCliente(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFilialClienteCount method, of class FilialClienteDAO.
     */
    @Test
    public void testGetFilialClienteCount() {
        System.out.println("getFilialClienteCount");
        FilialClienteDAO instance = null;
        int expResult = 0;
        int result = instance.getFilialClienteCount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
