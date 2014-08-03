/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.model;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.DBConnect;
import com.saax.gestorweb.util.TestUtils;
import com.vaadin.ui.UI;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author rodrigo
 */
public class DashboardModelTest {

    public DashboardModelTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        DBConnect.getInstance().assertConnection();
    }

    @AfterClass
    public static void tearDownClass() {
    }

    UI ui = null;

    Usuario usuarioLogado = null;
    DashboardModel instance = new DashboardModel();

    @Before
    public void setUp() {
        ui = new TestUtils().configureUI();
        usuarioLogado = ((GestorMDI) UI.getCurrent()).getUserData().getUsuarioLogado();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of listarTarefas method, of class DashboardModel.
     */
    @Test
    public void testListarTarefas() {
        System.out.println("listarTarefas");

        List<Tarefa> tarefas = instance.listarTarefas(usuarioLogado);
        for (Tarefa tarefa : tarefas) {

            System.out.println(tarefa.getNome());

        }

    }

    /**
     * Test of listarUsuariosEmpresa method, of class DashboardModel.
     * @throws java.lang.Exception
     */
    @Test
    public void testListarUsuariosEmpresa() throws Exception {
        System.out.println("listarUsuariosEmpresa");
        DashboardModel instance = new DashboardModel();
        List<Usuario> expResult = null;
        List<Usuario> result = instance.listarUsuariosEmpresa();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
