/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.model;

import com.saax.gestorweb.dao.TarefaDAO;
import com.saax.gestorweb.dao.UsuarioDAO;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.FilialEmpresa;
import com.saax.gestorweb.model.datamodel.ProjecaoTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.DBConnect;
import com.saax.gestorweb.util.PostgresConnection;
import com.saax.gestorweb.util.TestUtils;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author rodrigo
 */
public class DashboardModelTest {

    private UsuarioDAO usuarioDAO;
    private TarefaDAO tarefaDAO;

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
    List<Usuario> usuariosList = null;

    @Before
    public void setUp() {
        ui = new TestUtils().configureUI();
        usuarioLogado = (Usuario) VaadinSession.getCurrent().getAttribute("usuarioLogado");
        usuarioDAO = new UsuarioDAO(PostgresConnection.getInstance().getEntityManagerFactory());
        usuariosList = usuarioDAO.findUsuarioEntities();
        tarefaDAO = new TarefaDAO(PostgresConnection.getInstance().getEntityManagerFactory());
        /*
        Tarefa t0 = new Tarefa();
        t0.setNome("Tarefa 0");
        t0.setUsuarioResponsavel(usuarioDAO.findByLogin("teste-user@gmail.com"));
        tarefaDAO.create(t0);
        
        Tarefa t1 = new Tarefa();
        t1.setNome("Tarefa 1");
        t1.setUsuarioResponsavel(usuarioDAO.findByLogin("teste-user@gmail.com"));
        tarefaDAO.create(t1);
        
        Tarefa t2 = new Tarefa();
        t2.setNome("Tarefa 2");
        t2.setUsuarioResponsavel(usuarioDAO.findByLogin("rodrigo.ccn2005@gmail.com"));
        tarefaDAO.create(t2);
        */
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of listarTarefas method, of class DashboardModel.
     */
    @Test
    public void testListarTarefasPorUsuarioResponsavel() {
        System.out.println("listarTarefas");

        List<Usuario> usuariosResponsaveis = new ArrayList<>();
       // usuariosResponsaveis.add(usuarioDAO.findByLogin("teste-user@gmail.com"));

        List<Usuario> usuariosSolicitantes = null;
        List<Usuario> usuariosParticipantes = null;
        List<Empresa> empresas = null;
        List<FilialEmpresa> filiais = null;
        LocalDate dataFim = null;
        List<ProjecaoTarefa> projecoes = null;
        
        List<Tarefa> expResult = null;
        List<Tarefa> result = instance.listarTarefas(usuariosResponsaveis, usuariosSolicitantes, usuariosParticipantes, empresas, filiais, dataFim, projecoes);
        
        if (result!=null)
        for (Tarefa tarefa : result) {
            
            System.out.println(tarefa.getNome());
        }
        
        
    }

}
