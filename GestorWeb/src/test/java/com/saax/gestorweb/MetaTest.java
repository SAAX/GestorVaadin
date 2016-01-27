/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb;

import com.saax.gestorweb.model.LoginModel;
import com.saax.gestorweb.model.MetaModel;
import com.saax.gestorweb.model.TarefaModel;
import com.saax.gestorweb.model.datamodel.HierarquiaProjeto;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.PrioridadeTarefa;
import com.saax.gestorweb.model.datamodel.StatusTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.TipoTarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.presenter.MetaPresenter;
import com.saax.gestorweb.presenter.TarefaPresenter;
import com.saax.gestorweb.util.DBConnect;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.PostgresConnection;
import com.saax.gestorweb.util.TestUtils;
import com.saax.gestorweb.view.MetaView;
import com.saax.gestorweb.view.TarefaView;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.persistence.EntityManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author rodrigo
 */
public class MetaTest {

    private static ResourceBundle mensagens;
    private MetaView view;
    private MetaModel model;
    private MetaPresenter presenter;

    @BeforeClass
    public static void setUpClass() {

            TestUtils.connectDB();

            TestUtils.setUsuarioLogado(TestUtils.getUsuarioTeste());

            TestUtils.createGestorMDI();

            mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
            
            TestUtils.limpaBase();
            
        
    }

    @AfterClass
    public static void tearDownClass() {

            TestUtils.limpaBase();

    }

    @Before
    public void setUp() {

        view = new MetaView();
        presenter = new MetaPresenter(view);

        
    }

    @After
    public void tearDown() {

    }
    
    
    
    @Test
    public void criarNovaMetaSimples(){
        
        // prepare
        String nomeEsperado = "Teste Meta: criarNovaMetaSimples";
        HierarquiaProjeto h = (HierarquiaProjeto) PostgresConnection.getInstance().getEntityManagerFactory().createEntityManager().createNamedQuery("HierarquiaProjeto.findByNome")
                .setParameter("nome", "Projeto")
                .getSingleResult();
        
        HierarquiaProjetoDetalhe categoria = null;
        for (HierarquiaProjetoDetalhe c : h.getCategorias()) {
            if (c.getNivel()==1){
                categoria = c;
            }
        }
        
        presenter.criarNovaMeta(categoria);
        
        view.getEmpresaCombo().setValue(TestUtils.getUsuarioLogado().getEmpresas().get(0).getEmpresa());
        view.getNomeMetaTextField().setValue(nomeEsperado);
        view.getDataInicioDateField().setValue(new Date());
        
        // commit 
        try {
            view.getMetaFieldGroup().commit();
        } catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        
        presenter.gravarButtonClicked();

        
        
        
        // gets the persisted Target (to compare)
        Meta m = (Meta) PostgresConnection.getInstance().getEntityManagerFactory().createEntityManager()
                .createNamedQuery("Meta.findByNome")
                .setParameter("nome", nomeEsperado)
                .setParameter("empresa", TestUtils.getUsuarioLogado().getEmpresas().get(0).getEmpresa())
                .getSingleResult();


//        // asserts over the retrieved target 
        Assert.assertEquals(nomeEsperado, m.getNome());
        Assert.assertNotNull(m.getDataHoraInclusao());
//
        
    }

    
    /**
     * Tests the creation of a new Task (by the button: addTask) under a Target being created.
     */
    @Test
    public void criaNovaTarefaDentroDeMeta(){

        
        // Get some target type ...
        HierarquiaProjeto h = (HierarquiaProjeto) PostgresConnection.getInstance().getEntityManagerFactory().createEntityManager()
                .createNamedQuery("HierarquiaProjeto.findByNome")
                .setParameter("nome", "Projeto")
                .getSingleResult();
        
        HierarquiaProjetoDetalhe categoria = null;
        for (HierarquiaProjetoDetalhe c : h.getCategorias()) {
            if (c.getNivel()==1){
                categoria = c;
            }
        }

        // open the Target Presenter to create a new Target
        presenter.criarNovaMeta(categoria);
        Assert.assertEquals(2, view.getEmpresaCombo().getItemIds().size());

        // selects a company
        view.getEmpresaCombo().setValue(TestUtils.getUsuarioLogado().getEmpresas().get(0).getEmpresa());
        
        // fills the required fields
        view.getNomeMetaTextField().setValue("Target Test: createsNewTaskUnderTheTarget");
        view.getDataInicioDateField().setValue(new Date());
        
        // commit 
        try {
            view.getMetaFieldGroup().commit();
        } catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        
        // open a presenter to create a task under the target
        TarefaView TarefaView = new TarefaView();
        TarefaPresenter taskPresenter = new TarefaPresenter(TarefaView);
        
        // sets the taskPresenter's call back to the targetPresenter
        taskPresenter.addCallBackListener(presenter);

        // Gets the tasks categories from the Target category
        List<HierarquiaProjetoDetalhe> tasksCategories = model.getFirstsTaskCategories(view.getMeta().getCategoria());
            
        // Tells the presenter which is gonna be the Task's category
        taskPresenter.createTask(view.getMeta(), tasksCategories);
            
        // fills the required fields of Task
        TarefaView.getNomeTarefaTextField().setValue("Task under a target");
        //TarefaView.getTipoRecorrenciaCombo().select(TipoTarefa.RECORRENTE);
        TarefaView.getControleRecorrenciaButton().getCaption().equals("RECORRENTE");
        TarefaView.getPrioridadeCombo().setValue(PrioridadeTarefa.ALTA);
        TarefaView.getDataInicioDateField().setValue(new Date());
        TarefaView.getHierarquiaCombo().setValue(TarefaView.getHierarquiaCombo().getItemIds().toArray()[0]);
        
        // commits the task
        try {
            TarefaView.getTarefaFieldGroup().commit();
        } catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        
        // save the task
        TarefaView.getGravarButton().click();

        // saves the target
        try {
            view.getMetaFieldGroup().commit();
        } catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        presenter.gravarButtonClicked();

        // gets the persisted Target (to compare)
        Meta m = (Meta) PostgresConnection.getInstance().getEntityManagerFactory().createEntityManager()
                .createNamedQuery("Meta.findByNome")
                .setParameter("nome", "Target Test: createsNewTaskUnderTheTarget")
                .setParameter("empresa", TestUtils.getUsuarioLogado().getEmpresas().get(0).getEmpresa())
                .getSingleResult();


//        // asserts over the retrieved target 
        Assert.assertEquals("Target Test: createsNewTaskUnderTheTarget", m.getNome());
        Assert.assertNotNull(m.getDataHoraInclusao());
//
//        // asserts over the task under the target
        Tarefa task = m.getTarefas().get(0);
        Assert.assertEquals(1, m.getTarefas().size());
        Assert.assertEquals("Task under a target", task.getNome());
        Assert.assertEquals(StatusTarefa.NAO_INICIADA, task.getStatus());
        Assert.assertEquals(TipoTarefa.UNICA, task.getTipoRecorrencia());
        Assert.assertEquals(PrioridadeTarefa.ALTA, task.getPrioridade());
        Assert.assertNotNull(task.getDataHoraInclusao());
        Assert.assertNotNull(task.getDataInicio());

        
    }
    
}
