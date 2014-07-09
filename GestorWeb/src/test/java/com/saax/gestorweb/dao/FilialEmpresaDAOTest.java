/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.dao;

import com.saax.gestorweb.model.datamodel.FilialEmpresa;
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
public class FilialEmpresaDAOTest {
    
    public FilialEmpresaDAOTest() {
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
     * Test of getEntityManager method, of class FilialEmpresaDAO.
     */
    @Test
    public void testGetEntityManager() {
        System.out.println("getEntityManager");
        FilialEmpresaDAO instance = null;
        EntityManager expResult = null;
        EntityManager result = instance.getEntityManager();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of create method, of class FilialEmpresaDAO.
     */
    @Test
    public void testCreate() {
        System.out.println("create");
        FilialEmpresa filialEmpresa = null;
        FilialEmpresaDAO instance = null;
        instance.create(filialEmpresa);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of edit method, of class FilialEmpresaDAO.
     */
    @Test
    public void testEdit() throws Exception {
        System.out.println("edit");
        FilialEmpresa filialEmpresa = null;
        FilialEmpresaDAO instance = null;
        instance.edit(filialEmpresa);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of destroy method, of class FilialEmpresaDAO.
     */
    @Test
    public void testDestroy() throws Exception {
        System.out.println("destroy");
        Integer id = null;
        FilialEmpresaDAO instance = null;
        instance.destroy(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findFilialEmpresaEntities method, of class FilialEmpresaDAO.
     */
    @Test
    public void testFindFilialEmpresaEntities_0args() {
        System.out.println("findFilialEmpresaEntities");
        FilialEmpresaDAO instance = null;
        List<FilialEmpresa> expResult = null;
        List<FilialEmpresa> result = instance.findFilialEmpresaEntities();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findFilialEmpresaEntities method, of class FilialEmpresaDAO.
     */
    @Test
    public void testFindFilialEmpresaEntities_int_int() {
        System.out.println("findFilialEmpresaEntities");
        int maxResults = 0;
        int firstResult = 0;
        FilialEmpresaDAO instance = null;
        List<FilialEmpresa> expResult = null;
        List<FilialEmpresa> result = instance.findFilialEmpresaEntities(maxResults, firstResult);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findFilialEmpresa method, of class FilialEmpresaDAO.
     */
    @Test
    public void testFindFilialEmpresa() {
        System.out.println("findFilialEmpresa");
        Integer id = null;
        FilialEmpresaDAO instance = null;
        FilialEmpresa expResult = null;
        FilialEmpresa result = instance.findFilialEmpresa(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFilialEmpresaCount method, of class FilialEmpresaDAO.
     */
    @Test
    public void testGetFilialEmpresaCount() {
        System.out.println("getFilialEmpresaCount");
        FilialEmpresaDAO instance = null;
        int expResult = 0;
        int result = instance.getFilialEmpresaCount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
