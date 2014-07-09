/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.view;

import com.saax.gestorweb.model.datamodel.Meta;
import java.util.List;
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
public class DashBoardViewTest {
    
    public DashBoardViewTest() {
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
     * Test of setListener method, of class DashBoardView.
     */
    @Test
    public void testSetListener() {
        System.out.println("setListener");
        DashboardViewListenter listener = null;
        DashBoardView instance = new DashBoardView();
        instance.setListener(listener);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of exibirMetas method, of class DashBoardView.
     */
    @Test
    public void testExibirMetas() {
        System.out.println("exibirMetas");
        List<Meta> metas = null;
        DashBoardView instance = new DashBoardView();
        instance.exibirMetas(metas);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
