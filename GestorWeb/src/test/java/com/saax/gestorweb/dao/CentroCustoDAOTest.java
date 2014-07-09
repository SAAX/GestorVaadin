/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.dao;

import com.saax.gestorweb.model.datamodel.CentroCusto;
import com.saax.gestorweb.util.DBConnect;
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
public class CentroCustoDAOTest {
    
    public CentroCustoDAOTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        DBConnect.getInstance().assertConnection();
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
     * Test of getEntityManager method, of class CentroCustoDAO.
     */
    @Test
    public void testGetEntityManager() {
        System.out.println("getEntityManager");
        CentroCustoDAO instance = null;
        EntityManager result = instance.getEntityManager();
        assertNotNull(result);
    }

    /**
     * Test of create method, of class CentroCustoDAO.
     */
    @Test
    public void testCreate() {
        System.out.println("create");
        CentroCusto centroCusto = null;
        CentroCustoDAO instance = null;
        instance.create(centroCusto);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of edit method, of class CentroCustoDAO.
     */
    @Test
    public void testEdit() throws Exception {
        System.out.println("edit");
        CentroCusto centroCusto = null;
        CentroCustoDAO instance = null;
        instance.edit(centroCusto);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of destroy method, of class CentroCustoDAO.
     */
    @Test
    public void testDestroy() throws Exception {
        System.out.println("destroy");
        Integer id = null;
        CentroCustoDAO instance = null;
        instance.destroy(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findCentroCustoEntities method, of class CentroCustoDAO.
     */
    @Test
    public void testFindCentroCustoEntities_0args() {
        System.out.println("findCentroCustoEntities");
        CentroCustoDAO instance = null;
        List<CentroCusto> expResult = null;
        List<CentroCusto> result = instance.findCentroCustoEntities();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findCentroCustoEntities method, of class CentroCustoDAO.
     */
    @Test
    public void testFindCentroCustoEntities_int_int() {
        System.out.println("findCentroCustoEntities");
        int maxResults = 0;
        int firstResult = 0;
        CentroCustoDAO instance = null;
        List<CentroCusto> expResult = null;
        List<CentroCusto> result = instance.findCentroCustoEntities(maxResults, firstResult);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findCentroCusto method, of class CentroCustoDAO.
     */
    @Test
    public void testFindCentroCusto() {
        System.out.println("findCentroCusto");
        Integer id = null;
        CentroCustoDAO instance = null;
        CentroCusto expResult = null;
        CentroCusto result = instance.findCentroCusto(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCentroCustoCount method, of class CentroCustoDAO.
     */
    @Test
    public void testGetCentroCustoCount() {
        System.out.println("getCentroCustoCount");
        CentroCustoDAO instance = null;
        int expResult = 0;
        int result = instance.getCentroCustoCount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
