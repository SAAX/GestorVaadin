/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.dao;

import com.saax.gestorweb.model.datamodel.Usuario;
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
public class UsuarioDAOTest {
    
    public UsuarioDAOTest() {
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
     * Test of getEntityManager method, of class UsuarioDAO.
     */
    @Test
    public void testGetEntityManager() {
        System.out.println("getEntityManager");
        UsuarioDAO instance = null;
        EntityManager expResult = null;
        EntityManager result = instance.getEntityManager();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of create method, of class UsuarioDAO.
     */
    @Test
    public void testCreate() {
        System.out.println("create");
        Usuario usuario = null;
        UsuarioDAO instance = null;
        instance.create(usuario);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of edit method, of class UsuarioDAO.
     */
    @Test
    public void testEdit() throws Exception {
        System.out.println("edit");
        Usuario usuario = null;
        UsuarioDAO instance = null;
        instance.edit(usuario);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of destroy method, of class UsuarioDAO.
     */
    @Test
    public void testDestroy() throws Exception {
        System.out.println("destroy");
        Integer id = null;
        UsuarioDAO instance = null;
        instance.destroy(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findUsuarioEntities method, of class UsuarioDAO.
     */
    @Test
    public void testFindUsuarioEntities_0args() {
        System.out.println("findUsuarioEntities");
        UsuarioDAO instance = null;
        List<Usuario> expResult = null;
        List<Usuario> result = instance.findUsuarioEntities();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findUsuarioEntities method, of class UsuarioDAO.
     */
    @Test
    public void testFindUsuarioEntities_int_int() {
        System.out.println("findUsuarioEntities");
        int maxResults = 0;
        int firstResult = 0;
        UsuarioDAO instance = null;
        List<Usuario> expResult = null;
        List<Usuario> result = instance.findUsuarioEntities(maxResults, firstResult);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findUsuario method, of class UsuarioDAO.
     */
    @Test
    public void testFindUsuario() {
        System.out.println("findUsuario");
        Integer id = null;
        UsuarioDAO instance = null;
        Usuario expResult = null;
        Usuario result = instance.findUsuario(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUsuarioCount method, of class UsuarioDAO.
     */
    @Test
    public void testGetUsuarioCount() {
        System.out.println("getUsuarioCount");
        UsuarioDAO instance = null;
        int expResult = 0;
        int result = instance.getUsuarioCount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
