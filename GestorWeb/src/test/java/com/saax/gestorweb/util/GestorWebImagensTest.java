/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.util;

import com.vaadin.ui.Image;
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
public class GestorWebImagensTest {
    
    public GestorWebImagensTest() {
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
     * Test of getPAGINAINICIAL_LOGO method, of class GestorWebImagens.
     */
    @Test
    public void testGetPAGINAINICIAL_LOGO() {
        System.out.println("getPAGINAINICIAL_LOGO");
        GestorWebImagens instance = new GestorWebImagens();
        Image expResult = null;
        Image result = instance.getPAGINAINICIAL_LOGO();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
