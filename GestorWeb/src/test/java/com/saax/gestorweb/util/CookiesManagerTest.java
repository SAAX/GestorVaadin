/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.util;

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
public class CookiesManagerTest {
    
    public CookiesManagerTest() {
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
     * Test of setCookie method, of class CookiesManager.
     */
    @Test
    public void testSetCookie() {
        System.out.println("setCookie");
        CookiesManager.GestorWebCookieEnum gestorWebCookie = null;
        String valor = "";
        CookiesManager instance = new CookiesManager();
        instance.setCookie(gestorWebCookie, valor);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCookieValue method, of class CookiesManager.
     */
    @Test
    public void testGetCookieValue() {
        System.out.println("getCookieValue");
        CookiesManager.GestorWebCookieEnum gestorWebCookie = null;
        CookiesManager instance = new CookiesManager();
        String expResult = "";
        String result = instance.getCookieValue(gestorWebCookie);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of destroyCookie method, of class CookiesManager.
     */
    @Test
    public void testDestroyCookie() {
        System.out.println("destroyCookie");
        CookiesManager.GestorWebCookieEnum gestorWebCookieEnum = null;
        CookiesManager instance = new CookiesManager();
        instance.destroyCookie(gestorWebCookieEnum);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
