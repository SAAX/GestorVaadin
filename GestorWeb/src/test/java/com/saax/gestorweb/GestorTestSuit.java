package com.saax.gestorweb;

import com.saax.gestorweb.model.LoginModelTest;
import com.saax.gestorweb.util.DBConnect;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Suite principal de testes do Gestor
 * Todas as classes de teste unitário devem estar aqui.
 * @author rodrigo
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    LoginModelTest.class
})
public class GestorTestSuit {

    @BeforeClass
    public static void setUpClass() throws Exception {
        DBConnect.getInstance().assertConnection();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        DBConnect.getInstance().disconnect();
    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

}
