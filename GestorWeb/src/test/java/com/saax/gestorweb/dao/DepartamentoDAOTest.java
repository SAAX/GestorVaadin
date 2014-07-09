/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.dao;

import com.saax.gestorweb.model.datamodel.Departamento;
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
public class DepartamentoDAOTest {
    
    public DepartamentoDAOTest() {
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
     * Test of getEntityManager method, of class DepartamentoDAO.
     */
    @Test
    public void testGetEntityManager() {
        System.out.println("getEntityManager");
        DepartamentoDAO instance = null;
        EntityManager expResult = null;
        EntityManager result = instance.getEntityManager();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of create method, of class DepartamentoDAO.
     */
    @Test
    public void testCreate() {
        System.out.println("create");
        Departamento departamento = null;
        DepartamentoDAO instance = null;
        instance.create(departamento);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of edit method, of class DepartamentoDAO.
     */
    @Test
    public void testEdit() throws Exception {
        System.out.println("edit");
        Departamento departamento = null;
        DepartamentoDAO instance = null;
        instance.edit(departamento);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of destroy method, of class DepartamentoDAO.
     */
    @Test
    public void testDestroy() throws Exception {
        System.out.println("destroy");
        Integer id = null;
        DepartamentoDAO instance = null;
        instance.destroy(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findDepartamentoEntities method, of class DepartamentoDAO.
     */
    @Test
    public void testFindDepartamentoEntities_0args() {
        System.out.println("findDepartamentoEntities");
        DepartamentoDAO instance = null;
        List<Departamento> expResult = null;
        List<Departamento> result = instance.findDepartamentoEntities();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findDepartamentoEntities method, of class DepartamentoDAO.
     */
    @Test
    public void testFindDepartamentoEntities_int_int() {
        System.out.println("findDepartamentoEntities");
        int maxResults = 0;
        int firstResult = 0;
        DepartamentoDAO instance = null;
        List<Departamento> expResult = null;
        List<Departamento> result = instance.findDepartamentoEntities(maxResults, firstResult);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findDepartamento method, of class DepartamentoDAO.
     */
    @Test
    public void testFindDepartamento() {
        System.out.println("findDepartamento");
        Integer id = null;
        DepartamentoDAO instance = null;
        Departamento expResult = null;
        Departamento result = instance.findDepartamento(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDepartamentoCount method, of class DepartamentoDAO.
     */
    @Test
    public void testGetDepartamentoCount() {
        System.out.println("getDepartamentoCount");
        DepartamentoDAO instance = null;
        int expResult = 0;
        int result = instance.getDepartamentoCount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
