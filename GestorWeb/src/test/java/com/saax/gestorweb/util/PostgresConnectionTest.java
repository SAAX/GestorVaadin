/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.util;

import javax.persistence.EntityManagerFactory;
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
public class PostgresConnectionTest {
    
    public PostgresConnectionTest() {
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
     * Test of getInstance method, of class PostgresConnection.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        PostgresConnection expResult = null;
        PostgresConnection result = PostgresConnection.getInstance();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setEntityManagerFactory method, of class PostgresConnection.
     */
    @Test
    public void testSetEntityManagerFactory() {
        System.out.println("setEntityManagerFactory");
        EntityManagerFactory entityManagerFactory = null;
        PostgresConnection instance = null;
        instance.setEntityManagerFactory(entityManagerFactory);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getEntityManagerFactory method, of class PostgresConnection.
     */
    @Test
    public void testGetEntityManagerFactory() {
        System.out.println("getEntityManagerFactory");
        PostgresConnection instance = null;
        EntityManagerFactory expResult = null;
        EntityManagerFactory result = instance.getEntityManagerFactory();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
