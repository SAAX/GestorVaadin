/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.util;

import com.saax.gestorweb.model.datamodel.Tarefa;
import java.text.SimpleDateFormat;
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
public class FormatterUtilTest {
    
    public FormatterUtilTest() {
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
     * Test of getDateFormat method, of class FormatterUtil.
     */
    @Test
    public void testGetDateFormat() {
        System.out.println("getDateFormat");
        SimpleDateFormat expResult = null;
        SimpleDateFormat result = FormatterUtil.getDateFormat();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeNonDigitChars method, of class FormatterUtil.
     */
    @Test
    public void testRemoveNonDigitChars() {
        System.out.println("removeNonDigitChars");
        String value = "";
        String expResult = "";
        String result = FormatterUtil.removeNonDigitChars(value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of formatString method, of class FormatterUtil.
     */
    @Test
    public void testFormatString() throws Exception {
        System.out.println("formatString");
        String value = "";
        String pattern = "";
        String expResult = "";
        String result = FormatterUtil.formatString(value, pattern);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of validarCPF method, of class FormatterUtil.
     */
    @Test
    public void testValidarCPF() {
        System.out.println("validarCPF");
        String cpf = "";
        boolean expResult = false;
        boolean result = FormatterUtil.validarCPF(cpf);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of validarCNPJ method, of class FormatterUtil.
     */
    @Test
    public void testValidarCNPJ() {
        System.out.println("validarCNPJ");
        String cnpj = "";
        boolean expResult = false;
        boolean result = FormatterUtil.validarCNPJ(cnpj);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of formatID method, of class FormatterUtil.
     */
    @Test
    public void testFormatID() {
        System.out.println("formatID");
        Integer id = 123456789;
        Class type = Tarefa.class;
        String expResult = "T123456789";
        String result = FormatterUtil.formatID(id, type);
        System.out.println(result);
        assertEquals(expResult, result);
    }
    
}
