/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.util;

import com.saax.gestorweb.model.datamodel.Usuario;
import java.util.ResourceBundle;
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
public class UserDataTest {
    
    public UserDataTest() {
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
     * Test of getUsuarioLogado method, of class UserData.
     */
    @Test
    public void testGetUsuarioLogado() {
        System.out.println("getUsuarioLogado");
        UserData instance = new UserData();
        Usuario expResult = null;
        Usuario result = instance.getUsuarioLogado();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setUsuarioLogado method, of class UserData.
     */
    @Test
    public void testSetUsuarioLogado() {
        System.out.println("setUsuarioLogado");
        Usuario usuarioLogado = null;
        UserData instance = new UserData();
        instance.setUsuarioLogado(usuarioLogado);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMensagens method, of class UserData.
     */
    @Test
    public void testGetMensagens() {
        System.out.println("getMensagens");
        UserData instance = new UserData();
        ResourceBundle expResult = null;
        ResourceBundle result = instance.getMensagens();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setMensagens method, of class UserData.
     */
    @Test
    public void testSetMensagens() {
        System.out.println("setMensagens");
        ResourceBundle mensagens = null;
        UserData instance = new UserData();
        instance.setMensagens(mensagens);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCookies method, of class UserData.
     */
    @Test
    public void testGetCookies() {
        System.out.println("getCookies");
        UserData instance = new UserData();
        CookiesManager expResult = null;
        CookiesManager result = instance.getCookies();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCookies method, of class UserData.
     */
    @Test
    public void testSetCookies() {
        System.out.println("setCookies");
        CookiesManager cookies = null;
        UserData instance = new UserData();
        instance.setCookies(cookies);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getImagens method, of class UserData.
     */
    @Test
    public void testGetImagens() {
        System.out.println("getImagens");
        UserData instance = new UserData();
        GestorWebImagens expResult = null;
        GestorWebImagens result = instance.getImagens();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setImagens method, of class UserData.
     */
    @Test
    public void testSetImagens() {
        System.out.println("setImagens");
        GestorWebImagens imagens = null;
        UserData instance = new UserData();
        instance.setImagens(imagens);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
