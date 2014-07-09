/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.dao;

import com.saax.gestorweb.model.datamodel.Cidade;
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
public class CidadeDAOTest {
    
    public CidadeDAOTest() {
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
     * Test of getEntityManager method, of class CidadeDAO.
     */
    @Test
    public void testGetEntityManager() {
        System.out.println("getEntityManager");
        CidadeDAO instance = null;
        EntityManager expResult = null;
        EntityManager result = instance.getEntityManager();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of create method, of class CidadeDAO.
     */
    @Test
    public void testCreate() {
        System.out.println("create");
        Cidade cidade = null;
        CidadeDAO instance = null;
        instance.create(cidade);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of edit method, of class CidadeDAO.
     */
    @Test
    public void testEdit() throws Exception {
        System.out.println("edit");
        Cidade cidade = null;
        CidadeDAO instance = null;
        instance.edit(cidade);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of destroy method, of class CidadeDAO.
     */
    @Test
    public void testDestroy() throws Exception {
        System.out.println("destroy");
        Integer id = null;
        CidadeDAO instance = null;
        instance.destroy(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findCidadeEntities method, of class CidadeDAO.
     */
    @Test
    public void testFindCidadeEntities_0args() {
        System.out.println("findCidadeEntities");
        CidadeDAO instance = null;
        List<Cidade> expResult = null;
        List<Cidade> result = instance.findCidadeEntities();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findCidadeEntities method, of class CidadeDAO.
     */
    @Test
    public void testFindCidadeEntities_int_int() {
        System.out.println("findCidadeEntities");
        int maxResults = 0;
        int firstResult = 0;
        CidadeDAO instance = null;
        List<Cidade> expResult = null;
        List<Cidade> result = instance.findCidadeEntities(maxResults, firstResult);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findCidade method, of class CidadeDAO.
     */
    @Test
    public void testFindCidade() {
        System.out.println("findCidade");
        Integer id = null;
        CidadeDAO instance = null;
        Cidade expResult = null;
        Cidade result = instance.findCidade(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCidadeCount method, of class CidadeDAO.
     */
    @Test
    public void testGetCidadeCount() {
        System.out.println("getCidadeCount");
        CidadeDAO instance = null;
        int expResult = 0;
        int result = instance.getCidadeCount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
