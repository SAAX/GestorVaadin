/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb;

import com.saax.gestorweb.model.CadastroTarefaModel;
import com.saax.gestorweb.model.LoginModel;
import com.saax.gestorweb.model.datamodel.PrioridadeTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.TipoTarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.presenter.CadastroTarefaPresenter;
import com.saax.gestorweb.util.DBConnect;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.PostgresConnection;
import com.saax.gestorweb.view.CadastroTarefaView;
import com.vaadin.ui.UI;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author rodrigo
 */
public class CadastroTarefaTest   {

    CadastroTarefaView view;
    CadastroTarefaModel model;
    CadastroTarefaPresenter presenter;
    private EntityManager em;

    @BeforeClass
    public static void setUpClass() {
        
        // connect to database
        DBConnect.getInstance().assertConnection();
        EntityManager em = PostgresConnection.getInstance().getEntityManagerFactory().createEntityManager();
        GestorEntityManagerProvider.setCurrentEntityManager(em);
        
        // set logged user
        Usuario usuario = (Usuario) em.createNamedQuery("Usuario.findByLogin").setParameter("login", "teste-user@gmail.com").getSingleResult();
        GestorSession.setAttribute("usuarioLogado", usuario);
        usuario.setEmpresaAtiva(new LoginModel().getEmpresaUsuarioLogado());
        
        // creates UI
        GestorMDI gestor = new GestorMDI();
        UI.setCurrent(gestor);
        gestor.init(null);
        
        // limpar tarefas cadastradas
        List<Tarefa> tarefas = em.createNamedQuery("Tarefa.findAll").setParameter("empresa", usuario.getEmpresaAtiva()).getResultList();
        for (Tarefa tarefa : tarefas) {
            em.remove(tarefa);
        }
        

    }

    @AfterClass
    public static void tearDownClass() {
        // disconnect
        GestorEntityManagerProvider.getEntityManager().close();
        GestorEntityManagerProvider.setCurrentEntityManager(null);
        
    }


    @Before
    public void setUp() {

        view = new CadastroTarefaView();
        model = new CadastroTarefaModel();
        presenter = new CadastroTarefaPresenter(model, view);

        em = GestorEntityManagerProvider.getEntityManager();
    }

    @After
    public void tearDown() {

        
    }

    @Test
    public void cadastrarTarefaSimples() {

        Usuario usuarioLogado = (Usuario) GestorSession.getAttribute("usuarioLogado");
        
        presenter.criarNovaTarefa();

        String nome = "Teste Cadastro Tarefa #11";
        view.getNomeTarefaTextField().setValue(nome);
        view.getTipoRecorrenciaCombo().select(TipoTarefa.RECORRENTE);
        view.getPrioridadeCombo().setValue(PrioridadeTarefa.ALTA);
        view.getDataInicioDateField().setValue(new Date());
        view.getEmpresaCombo().setValue(usuarioLogado.getEmpresaAtiva());
        view.getGravarButton().click();
        
        Tarefa t = (Tarefa) em.createNamedQuery("Tarefa.findByNome")
                .setParameter("nome", nome)
                .setParameter("empresa", usuarioLogado.getEmpresaAtiva())
                .getSingleResult();
        Assert.assertEquals(t.getNome(), nome);
        

    }

    @Ignore
    @Test
    public void cadastrarTarefaCompleta() {

        Usuario usuarioLogado = (Usuario) GestorSession.getAttribute("usuarioLogado");
        Usuario usuarioResponsavel = (Usuario) em.createNamedQuery("Usuario.findByLogin").setParameter("login", "rodrigo.ccn2005@gmail.com").getSingleResult();
        
        presenter.criarNovaTarefa();

        String nome = "Teste Cadastro Tarefa #2";
        view.getNomeTarefaTextField().setValue(nome);
        view.getPrioridadeCombo().setValue(PrioridadeTarefa.ALTA);
        
        view.getTipoRecorrenciaCombo().select(TipoTarefa.RECORRENTE);
        view.getDataInicioDateField().setValue(new Date());
        view.getUsuarioResponsavelCombo().setValue(usuarioLogado);
        
        
        view.getGravarButton().click();
        
        
        Tarefa t = (Tarefa) em.createNamedQuery("Tarefa.findByNome")
                .setParameter("nome", nome)
                .setParameter("empresa", usuarioLogado.getEmpresaAtiva())
                .getSingleResult();
        Assert.assertEquals(t.getNome(), nome);
        

    }

    public void editarTarefa(){
        
    }  
    
}
