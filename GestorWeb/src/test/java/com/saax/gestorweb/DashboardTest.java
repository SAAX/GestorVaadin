package com.saax.gestorweb;

import com.saax.gestorweb.model.DashboardModel;
import com.saax.gestorweb.model.LixeiraModel;
import com.saax.gestorweb.model.datamodel.HierarquiaProjeto;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.presenter.DashboardPresenter;
import com.saax.gestorweb.presenter.TarefaPresenter;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import com.saax.gestorweb.util.TestUtils;
import com.saax.gestorweb.view.DashboardView;
import com.saax.gestorweb.view.TarefaView;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import java.util.List;
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
public class DashboardTest {

    @BeforeClass
    public static void setUpClass() {

        TestUtils.connectDB();

        TestUtils.setUsuarioLogado(TestUtils.getUsuarioTeste());

        TestUtils.createGestorMDI();
        
        TestUtils.limpaBase();

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
        TestUtils.limpaBase();
    }

    
    /**
     * Verifica se uma tarefa recem cadastrada aparece no dash do usuario
     */
    @Test
    public void listaTarefaTest(){
    
        // arrange
        
        // criar uma tarefa pai
        Tarefa expected = TestUtils.cadastrarTarefaSimples("listaTarefaTest");
        
        // act 
        DashboardView view = new DashboardView();
        DashboardPresenter presenter = new DashboardPresenter(view);
        
        presenter.init();
        

        // assert
        Assert.assertTrue(view.getTarefaTable().getItemIds().contains(expected));
        
        
        
        
    }
    
    
    @Test
    public void getHierarquiasProjetoTest() {

        List<HierarquiaProjeto> l = DashboardModel.getHierarquiasProjeto();

        Assert.assertFalse("l está vazio", l.isEmpty());

        List<HierarquiaProjetoDetalhe> todasCatetorias = new ArrayList<>();

        for (HierarquiaProjeto h : l) {
            todasCatetorias.addAll(h.getCategorias());
        }

    }
    
    
    
    @Test
    public void criarTarefaPorTemplateTest(){
        
        // -------------------------------------------------------------------------------------------------
        // Preparação
        // -------------------------------------------------------------------------------------------------
        DashboardView view = new DashboardView();
        DashboardPresenter presenter = new DashboardPresenter(view);
        
        String nomeTemplate = "Tarefa Teste: criarTarefaPorTemplateTest (template)";
        String nomeTarefa = "Tarefa Teste: criarTarefaPorTemplateTest (tarefa)";

        // cadastrar uma tarefa como template        
        Tarefa templateCriado = TestUtils.cadastrarTarefaSimples(nomeTemplate);
        templateCriado.setTemplate(true);
        GestorEntityManagerProvider.getEntityManager().getTransaction().begin();
        GestorEntityManagerProvider.getEntityManager().merge(templateCriado);
        GestorEntityManagerProvider.getEntityManager().getTransaction().commit();
        
        // -------------------------------------------------------------------------------------------------
        // Ação
        // -------------------------------------------------------------------------------------------------
        
        presenter.createsNewTaskByTemplate();
        
        // seleciona o primeiro template da lista
        Tarefa templateSelecionado = (Tarefa) view.getListaTemplates().getItemIds().iterator().next();
        view.getListaTemplates().select(templateSelecionado);

        Tarefa tarefaCriada = presenter.criarTarefaPorTemplate(templateSelecionado);
        
        TarefaView tarefaView = new TarefaView();
        TarefaPresenter tarefaPresenter = new TarefaPresenter(tarefaView);
        
        tarefaPresenter.editar(tarefaCriada);
        tarefaView.getNomeTarefaTextField().setValue(nomeTarefa);
        try {
            tarefaView.getTarefaFieldGroup().commit();
        } catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        
        tarefaView.getGravarButton().click();

        // -------------------------------------------------------------------------------------------------
        // Verificação
        // -------------------------------------------------------------------------------------------------
        
        Assert.assertEquals(templateCriado, templateSelecionado);

        Tarefa t = (Tarefa) GestorEntityManagerProvider.getEntityManager().createNamedQuery("Tarefa.findByNome")
                .setParameter("nome", nomeTarefa)
                .setParameter("empresa", templateSelecionado.getEmpresa())
                .getSingleResult();
        
        // Nome
        Assert.assertEquals(nomeTarefa, t.getNome());
        
    }

}
