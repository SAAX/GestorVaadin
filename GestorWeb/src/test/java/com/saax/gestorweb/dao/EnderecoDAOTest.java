/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.dao;

import com.saax.gestorweb.model.datamodel.Endereco;
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
public class EnderecoDAOTest {
    
    public EnderecoDAOTest() {
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
     * Test of getEntityManager method, of class EnderecoDAO.
     */
    @Test
    public void testGetEntityManager() {
        System.out.println("getEntityManager");
        EnderecoDAO instance = null;
        EntityManager expResult = null;
        EntityManager result = instance.getEntityManager();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of create method, of class EnderecoDAO.
     */
    @Test
    public void testCreate() {
        System.out.println("create");
        Endereco endereco = null;
        EnderecoDAO instance = null;
        instance.create(endereco);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of edit method, of class EnderecoDAO.
     */
    @Test
    public void testEdit() throws Exception {
        System.out.println("edit");
        Endereco endereco = null;
        EnderecoDAO instance = null;
        instance.edit(endereco);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of destroy method, of class EnderecoDAO.
     */
    @Test
    public void testDestroy() throws Exception {
        System.out.println("destroy");
        Integer id = null;
        EnderecoDAO instance = null;
        instance.destroy(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findEnderecoEntities method, of class EnderecoDAO.
     */
    @Test
    public void testFindEnderecoEntities_0args() {
        System.out.println("findEnderecoEntities");
        EnderecoDAO instance = null;
        List<Endereco> expResult = null;
        List<Endereco> result = instance.findEnderecoEntities();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findEnderecoEntities method, of class EnderecoDAO.
     */
    @Test
    public void testFindEnderecoEntities_int_int() {
        System.out.println("findEnderecoEntities");
        int maxResults = 0;
        int firstResult = 0;
        EnderecoDAO instance = null;
        List<Endereco> expResult = null;
        List<Endereco> result = instance.findEnderecoEntities(maxResults, firstResult);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findEndereco method, of class EnderecoDAO.
     */
    @Test
    public void testFindEndereco() {
        System.out.println("findEndereco");
        Integer id = null;
        EnderecoDAO instance = null;
        Endereco expResult = null;
        Endereco result = instance.findEndereco(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getEnderecoCount method, of class EnderecoDAO.
     */
    @Test
    public void testGetEnderecoCount() {
        System.out.println("getEnderecoCount");
        EnderecoDAO instance = null;
        int expResult = 0;
        int result = instance.getEnderecoCount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
