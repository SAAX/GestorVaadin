/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.dao;

import com.saax.gestorweb.model.datamodel.UsuarioEmpresa;
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
public class UsuarioEmpresaDAOTest {
    
    public UsuarioEmpresaDAOTest() {
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
     * Test of getEntityManager method, of class UsuarioEmpresaDAO.
     */
    @Test
    public void testGetEntityManager() {
        System.out.println("getEntityManager");
        UsuarioEmpresaDAO instance = null;
        EntityManager expResult = null;
        EntityManager result = instance.getEntityManager();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of create method, of class UsuarioEmpresaDAO.
     */
    @Test
    public void testCreate() {
        System.out.println("create");
        UsuarioEmpresa usuarioEmpresa = null;
        UsuarioEmpresaDAO instance = null;
        instance.create(usuarioEmpresa);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of edit method, of class UsuarioEmpresaDAO.
     */
    @Test
    public void testEdit() throws Exception {
        System.out.println("edit");
        UsuarioEmpresa usuarioEmpresa = null;
        UsuarioEmpresaDAO instance = null;
        instance.edit(usuarioEmpresa);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of destroy method, of class UsuarioEmpresaDAO.
     */
    @Test
    public void testDestroy() throws Exception {
        System.out.println("destroy");
        Integer id = null;
        UsuarioEmpresaDAO instance = null;
        instance.destroy(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findUsuarioEmpresaEntities method, of class UsuarioEmpresaDAO.
     */
    @Test
    public void testFindUsuarioEmpresaEntities_0args() {
        System.out.println("findUsuarioEmpresaEntities");
        UsuarioEmpresaDAO instance = null;
        List<UsuarioEmpresa> expResult = null;
        List<UsuarioEmpresa> result = instance.findUsuarioEmpresaEntities();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findUsuarioEmpresaEntities method, of class UsuarioEmpresaDAO.
     */
    @Test
    public void testFindUsuarioEmpresaEntities_int_int() {
        System.out.println("findUsuarioEmpresaEntities");
        int maxResults = 0;
        int firstResult = 0;
        UsuarioEmpresaDAO instance = null;
        List<UsuarioEmpresa> expResult = null;
        List<UsuarioEmpresa> result = instance.findUsuarioEmpresaEntities(maxResults, firstResult);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findUsuarioEmpresa method, of class UsuarioEmpresaDAO.
     */
    @Test
    public void testFindUsuarioEmpresa() {
        System.out.println("findUsuarioEmpresa");
        Integer id = null;
        UsuarioEmpresaDAO instance = null;
        UsuarioEmpresa expResult = null;
        UsuarioEmpresa result = instance.findUsuarioEmpresa(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUsuarioEmpresaCount method, of class UsuarioEmpresaDAO.
     */
    @Test
    public void testGetUsuarioEmpresaCount() {
        System.out.println("getUsuarioEmpresaCount");
        UsuarioEmpresaDAO instance = null;
        int expResult = 0;
        int result = instance.getUsuarioEmpresaCount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
