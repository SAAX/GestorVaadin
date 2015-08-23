package com.saax.gestorweb;

import com.saax.gestorweb.model.DashboardModel;
import com.saax.gestorweb.model.datamodel.HierarquiaProjeto;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.PrioridadeTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.presenter.DashboardPresenter;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import com.saax.gestorweb.util.TestUtils;
import com.saax.gestorweb.view.DashboardView;
import com.vaadin.data.fieldgroup.FieldGroup;
import java.util.ArrayList;
import java.util.Date;
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
        
        TestUtils.removeTodasTarefas();

    }

    @AfterClass
    public static void tearDownClass() {
        
        TestUtils.removeTodasTarefas();
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void getHierarquiasProjetoTest() {

        DashboardModel model = new DashboardModel();
        List<HierarquiaProjeto> l = model.getHierarquiasProjeto();

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
        DashboardModel model = new DashboardModel();
        DashboardPresenter presenter = new DashboardPresenter(model, view);
        
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

        templateSelecionado.setNome(nomeTarefa);
        presenter.criarTarefaPorTemplate(templateSelecionado);
        
        presenter.getTarefaPresenter().gravarButtonClicked();

        // -------------------------------------------------------------------------------------------------
        // Verificação
        // -------------------------------------------------------------------------------------------------
        
        Assert.assertEquals(templateCriado, templateSelecionado);

        Tarefa t = (Tarefa) GestorEntityManagerProvider.getEntityManager().createNamedQuery("Tarefa.findByNome")
                .setParameter("nome", nomeTarefa)
                .setParameter("empresa", templateSelecionado.getEmpresa())
                .getSingleResult();
        
        // Nome
        Assert.assertEquals(templateSelecionado.getNome(), t.getNome());
        
    }

}
