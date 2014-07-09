/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.dao;

import com.saax.gestorweb.model.datamodel.Usuario;
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
public class UsuarioDAOCustomTest {
    
    public UsuarioDAOCustomTest() {
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
     * Test of findByLogin method, of class UsuarioDAOCustom.
     */
    @Test
    public void testFindByLogin() {
        System.out.println("findByLogin");
        String login = "";
        UsuarioDAOCustom instance = null;
        Usuario expResult = null;
        Usuario result = instance.findByLogin(login);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
