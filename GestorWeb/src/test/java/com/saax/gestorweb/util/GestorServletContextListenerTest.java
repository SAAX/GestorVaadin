/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.util;

import javax.servlet.ServletContextEvent;
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
public class GestorServletContextListenerTest {
    
    public GestorServletContextListenerTest() {
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
     * Test of contextInitialized method, of class GestorServletContextListener.
     */
    @Test
    public void testContextInitialized() {
        System.out.println("contextInitialized");
        ServletContextEvent e = null;
        GestorServletContextListener instance = new GestorServletContextListener();
        instance.contextInitialized(e);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of contextDestroyed method, of class GestorServletContextListener.
     */
    @Test
    public void testContextDestroyed() {
        System.out.println("contextDestroyed");
        ServletContextEvent e = null;
        GestorServletContextListener instance = new GestorServletContextListener();
        instance.contextDestroyed(e);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
