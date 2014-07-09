/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.dao;

import com.saax.gestorweb.model.datamodel.Estado;
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
public class EstadoDAOTest {
    
    public EstadoDAOTest() {
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
     * Test of getEntityManager method, of class EstadoDAO.
     */
    @Test
    public void testGetEntityManager() {
        System.out.println("getEntityManager");
        EstadoDAO instance = null;
        EntityManager expResult = null;
        EntityManager result = instance.getEntityManager();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of create method, of class EstadoDAO.
     */
    @Test
    public void testCreate() {
        System.out.println("create");
        Estado estado = null;
        EstadoDAO instance = null;
        instance.create(estado);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of edit method, of class EstadoDAO.
     */
    @Test
    public void testEdit() throws Exception {
        System.out.println("edit");
        Estado estado = null;
        EstadoDAO instance = null;
        instance.edit(estado);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of destroy method, of class EstadoDAO.
     */
    @Test
    public void testDestroy() throws Exception {
        System.out.println("destroy");
        Integer id = null;
        EstadoDAO instance = null;
        instance.destroy(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findEstadoEntities method, of class EstadoDAO.
     */
    @Test
    public void testFindEstadoEntities_0args() {
        System.out.println("findEstadoEntities");
        EstadoDAO instance = null;
        List<Estado> expResult = null;
        List<Estado> result = instance.findEstadoEntities();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findEstadoEntities method, of class EstadoDAO.
     */
    @Test
    public void testFindEstadoEntities_int_int() {
        System.out.println("findEstadoEntities");
        int maxResults = 0;
        int firstResult = 0;
        EstadoDAO instance = null;
        List<Estado> expResult = null;
        List<Estado> result = instance.findEstadoEntities(maxResults, firstResult);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findEstado method, of class EstadoDAO.
     */
    @Test
    public void testFindEstado() {
        System.out.println("findEstado");
        Integer id = null;
        EstadoDAO instance = null;
        Estado expResult = null;
        Estado result = instance.findEstado(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getEstadoCount method, of class EstadoDAO.
     */
    @Test
    public void testGetEstadoCount() {
        System.out.println("getEstadoCount");
        EstadoDAO instance = null;
        int expResult = 0;
        int result = instance.getEstadoCount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
