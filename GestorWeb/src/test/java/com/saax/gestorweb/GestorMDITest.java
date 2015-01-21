/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb;

import com.saax.gestorweb.util.DBConnect;
import com.saax.gestorweb.view.DashboardView;
import com.vaadin.server.VaadinRequest;
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
public class GestorMDITest {
    
    public GestorMDITest() {
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
     * Test of carregarDashBoard method, of class GestorMDI.
     */
    @Test
    public void testCarregarDashBoard() {
        System.out.println("carregarDashBoard");
        GestorMDI instance = new GestorMDI();
        instance.carregarDashBoard();
        
        Object result = instance.getContent();
        
        assertTrue(result instanceof DashboardView);

    }

  
}
