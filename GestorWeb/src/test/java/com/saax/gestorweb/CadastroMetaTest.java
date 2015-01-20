/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb;

import com.saax.gestorweb.model.CadastroMetaModel;
import com.saax.gestorweb.model.CadastroTarefaModel;
import com.saax.gestorweb.model.LoginModel;
import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.HierarquiaProjeto;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.PrioridadeTarefa;
import com.saax.gestorweb.model.datamodel.StatusTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.TipoTarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.presenter.CadastroMetaPresenter;
import com.saax.gestorweb.presenter.CadastroTarefaPresenter;
import com.saax.gestorweb.util.DBConnect;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.PostgresConnection;
import com.saax.gestorweb.view.CadastroMetaView;
import com.saax.gestorweb.view.CadastroTarefaView;
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
public class CadastroMetaTest {

    private static ResourceBundle mensagens;
    private static Usuario loggedUser;
    private CadastroMetaView view;
    private CadastroMetaModel model;
    private CadastroMetaPresenter presenter;
    private EntityManager em;
    private static List<Meta> persistedTargets;

    @BeforeClass
    public static void setUpClass() {

        // connect to database
        DBConnect.getInstance().assertConnection();
        EntityManager em = PostgresConnection.getInstance().getEntityManagerFactory().createEntityManager();
        GestorEntityManagerProvider.setCurrentEntityManager(em);

        // set logged user
        loggedUser = (Usuario) em.createNamedQuery("Usuario.findByLogin").setParameter("login", "teste-user@gmail.com").getSingleResult();
        GestorSession.setAttribute("loggedUser", loggedUser);
        loggedUser.setEmpresaAtiva(new LoginModel().getEmpresaUsuarioLogado());

        // creates UI
        GestorMDI gestor = new GestorMDI();
        UI.setCurrent(gestor);
        gestor.init(null);

        mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
        
        persistedTargets = new ArrayList<>();
        
        

    }

    @AfterClass
    public static void tearDownClass() {

    }

    @Before
    public void setUp() {

        view = new CadastroMetaView();
        model = new CadastroMetaModel();
        presenter = new CadastroMetaPresenter(model, view);

        
    }

    @After
    public void tearDown() {

        // limpar tarefas cadastradas
        List<Meta> targets = persistedTargets;
        GestorEntityManagerProvider.getEntityManager().getTransaction().begin();
        for (Meta meta : targets) {
            meta = GestorEntityManagerProvider.getEntityManager().find(Meta.class, meta.getId());
            GestorEntityManagerProvider.getEntityManager().remove(meta);
        }
        GestorEntityManagerProvider.getEntityManager().getTransaction().commit();

        // disconnect
        GestorEntityManagerProvider.getEntityManager().close();
    }
    
    
    
    @Test
    public void criarNovaMetaSimples(){
        
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
        
        Assert.assertEquals(2, view.getEmpresaCombo().getItemIds().size());
        
        view.getEmpresaCombo().setValue(loggedUser.getEmpresaAtiva());
        
        List<Departamento> departamentos = PostgresConnection.getInstance().getEntityManagerFactory().createEntityManager().createNamedQuery("Departamento.findByEmpresaAtivo")
                .setParameter("empresa", loggedUser.getEmpresaAtiva())
                .getResultList();
        
        Assert.assertArrayEquals(departamentos.toArray(), view.getDepartamentoCombo().getItemIds().toArray());
        
        
        
    }

    
    /**
     * Tests the creation of a new Task (by the button: addTask) under a Target being created.
     */
    @Test
    public void createsNewTaskUnderTheTarget(){

        
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
        view.getEmpresaCombo().setValue(loggedUser.getEmpresaAtiva());
        
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
        CadastroTarefaView taskView = new CadastroTarefaView();
        CadastroTarefaModel taskModel = new CadastroTarefaModel();
        CadastroTarefaPresenter taskPresenter = new CadastroTarefaPresenter(taskModel, taskView);
        
        // sets the taskPresenter's call back to the targetPresenter
        taskPresenter.setCallBackListener(presenter);

        // Gets the tasks categories from the Target category
        List<HierarquiaProjetoDetalhe> tasksCategories = model.getFirstsTaskCategories(view.getMeta().getCategoria());
            
        // Tells the presenter which is gonna be the Task's category
        taskPresenter.createTask(view.getMeta(), tasksCategories);
            
        // fills the required fields of Task
        taskView.getNomeTarefaTextField().setValue("Task under a target");
        //taskView.getTipoRecorrenciaCombo().select(TipoTarefa.RECORRENTE);
        taskView.getTipoRecorrenciaButton().getCaption().equals("RECORRENTE");
        taskView.getPrioridadeCombo().setValue(PrioridadeTarefa.ALTA);
        taskView.getDataInicioDateField().setValue(new Date());
        taskView.getHierarquiaCombo().setValue(taskView.getHierarquiaCombo().getItemIds().toArray()[0]);
        
        // commits the task
        try {
            taskView.getTarefaFieldGroup().commit();
        } catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        
        // save the task
        taskPresenter.gravarButtonClicked();

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
                .setParameter("empresa", loggedUser.getEmpresaAtiva())
                .getSingleResult();

        persistedTargets.add(m);

//        // asserts over the retrieved target 
        Assert.assertEquals("Target Test: createsNewTaskUnderTheTarget", m.getNome());
        Assert.assertNotNull(m.getDataHoraInclusao());
//
//        // asserts over the task under the target
        Tarefa task = m.getTarefas().get(0);
        Assert.assertEquals(1, m.getTarefas().size());
        Assert.assertEquals("Task under a target", task.getNome());
        Assert.assertEquals(StatusTarefa.NAO_ACEITA, task.getStatus());
        Assert.assertEquals(TipoTarefa.RECORRENTE, task.getTipoRecorrencia());
        Assert.assertEquals(PrioridadeTarefa.ALTA, task.getPrioridade());
        Assert.assertNotNull(task.getDataHoraInclusao());
        Assert.assertNotNull(task.getDataInicio());

        
    }
    
}
