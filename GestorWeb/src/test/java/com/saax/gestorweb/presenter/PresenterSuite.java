/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.presenter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author rodrigo
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({com.saax.gestorweb.presenter.LoginPresenterTest.class, com.saax.gestorweb.presenter.SignupPresenterTest.class, com.saax.gestorweb.presenter.CadastroMetasPresenterTest.class, com.saax.gestorweb.presenter.PaginaInicialPresenterTest.class, com.saax.gestorweb.presenter.DashboardPresenterTest.class})
public class PresenterSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}
