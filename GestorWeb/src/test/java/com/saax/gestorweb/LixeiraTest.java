package com.saax.gestorweb;

import com.saax.gestorweb.model.DashboardModel;
import com.saax.gestorweb.model.LixeiraModel;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.presenter.LixeiraPresenter;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import com.saax.gestorweb.util.TestUtils;
import com.saax.gestorweb.view.LixeiraView;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author rodrigo
 */
public class LixeiraTest {

    @BeforeClass
    public static void setUpClass() {

        TestUtils.connectDB();

        TestUtils.setUsuarioLogado(TestUtils.getUsuarioTeste());

        TestUtils.createGestorMDI();
        
        TestUtils.limpaBase();

    }

    @AfterClass
    public static void tearDownClass() {
        
        TestUtils.limpaBase();
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void recuperarTarefaTest(){
        
        // -------------------------------------------------------------------------------------------------
        // Preparação
        // -------------------------------------------------------------------------------------------------

        String nomeTarefa = "Tarefa Teste: recuperarTarefaTest";
        
        TestUtils.setUsuarioLogado(TestUtils.getUsuarioRodrigo());

        // criar uma terefa
        Tarefa tarefaCriada = TestUtils.cadastrarTarefaSimples(nomeTarefa);
        tarefaCriada.setUsuarioResponsavel(TestUtils.getUsuarioRodrigo());
        GestorEntityManagerProvider.getEntityManager().getTransaction().begin();
        GestorEntityManagerProvider.getEntityManager().merge(tarefaCriada);
        GestorEntityManagerProvider.getEntityManager().getTransaction().commit();
        
        //remove-la
        LixeiraModel.removerTarefa(tarefaCriada, TestUtils.getUsuarioRodrigo());
        
        // -------------------------------------------------------------------------------------------------
        // Ação
        // -------------------------------------------------------------------------------------------------

        // recuperar a tarefa removida
        LixeiraView view = new LixeiraView();
        LixeiraPresenter presenter = new LixeiraPresenter(view);
        presenter.aprentarLixeira();
        // aciona comando para restaurar
        presenter.restaurarTarefaConfirmada(tarefaCriada);

        // -------------------------------------------------------------------------------------------------
        // Verificação
        // -------------------------------------------------------------------------------------------------
    
        Tarefa t = (Tarefa) GestorEntityManagerProvider.getEntityManager().find(Tarefa.class, tarefaCriada.getId());
        
        Assert.assertNull(t.getDataHoraRemocao());
        Assert.assertNull(t.getUsuarioRemocao());

        List<Tarefa> lista = DashboardModel.listarTarefas(TestUtils.getUsuarioRodrigo());
        List<Tarefa> listaRemovidas = LixeiraModel.listarTarefasRemovidas(TestUtils.getUsuarioRodrigo());

        for (Tarefa tlista : listaRemovidas) {
            if (tlista.equals(t)){
                Assert.fail("Tarefa foi restaurada e nao deveria estar na lista de removidas");
            }
            
        }
        
        boolean existe = false;
        for (Tarefa tlista : lista) {
            if (tlista.equals(t)){
                existe = true;
            }
            
        }
        Assert.assertTrue("Tarefa foi restaurada e deveria estar na lista de tarefas", existe);
        
    }
    
    @Test
    public void removerTarefaTest(){
        
        // -------------------------------------------------------------------------------------------------
        // Preparação
        // -------------------------------------------------------------------------------------------------
        
        String nomeTarefa = "Tarefa Teste: removerTarefaTest";

        // cadastrar uma tarefa 
        Tarefa tarefaCriada = TestUtils.cadastrarTarefaSimples(nomeTarefa);
        tarefaCriada.setUsuarioResponsavel(TestUtils.getUsuarioRodrigo());
        GestorEntityManagerProvider.getEntityManager().getTransaction().begin();
        GestorEntityManagerProvider.getEntityManager().merge(tarefaCriada);
        GestorEntityManagerProvider.getEntityManager().getTransaction().commit();
        
        // -------------------------------------------------------------------------------------------------
        // Ação
        // -------------------------------------------------------------------------------------------------
        
        LixeiraModel.removerTarefa(tarefaCriada, TestUtils.getUsuarioRodrigo());
        List<Tarefa> lista = DashboardModel.listarTarefas(TestUtils.getUsuarioRodrigo());
        List<Tarefa> listaRemovidas = LixeiraModel.listarTarefasRemovidas(TestUtils.getUsuarioRodrigo());

        // -------------------------------------------------------------------------------------------------
        // Verificação
        // -------------------------------------------------------------------------------------------------

        Tarefa t = (Tarefa) GestorEntityManagerProvider.getEntityManager().find(Tarefa.class, tarefaCriada.getId());
        
        Assert.assertNotNull(t.getDataHoraRemocao());
        Assert.assertEquals(TestUtils.getUsuarioRodrigo(), t.getUsuarioRemocao());

        for (Tarefa tlista : lista) {
            if (tlista.equals(t)){
                Assert.fail("Tarefa foi removida e nao deveria estar na lista");
            }
            
        }
        
        boolean existe = false;
        for (Tarefa tlista : listaRemovidas) {
            if (tlista.equals(t)){
                existe = true;
            }
            
        }
        Assert.assertTrue("Tarefa foi removida e deveria estar na lista de tarefas removidas", existe);
        
        
    }

    @Test
    public void removerMetaTest(){
        
        // -------------------------------------------------------------------------------------------------
        // Preparação
        // -------------------------------------------------------------------------------------------------
        
        String nomeMeta = "Meta Teste: removerMetaTest";

        // cadastrar uma meta
        Meta metaCriada = TestUtils.cadastrarMetaSimples(nomeMeta);
        metaCriada.setUsuarioResponsavel(TestUtils.getUsuarioRodrigo());
        // colocar Rodrigo como usuário responsável
        GestorEntityManagerProvider.getEntityManager().getTransaction().begin();
        GestorEntityManagerProvider.getEntityManager().merge(metaCriada);
        GestorEntityManagerProvider.getEntityManager().getTransaction().commit();
        
        // -------------------------------------------------------------------------------------------------
        // Ação
        // -------------------------------------------------------------------------------------------------
        
        LixeiraModel.removerMeta(metaCriada, TestUtils.getUsuarioRodrigo());
        List<Meta> lista = DashboardModel.listarMetas(TestUtils.getUsuarioRodrigo());
        List<Meta> listaRemovidas = LixeiraModel.listarMetasRemovidas(TestUtils.getUsuarioRodrigo());

        // -------------------------------------------------------------------------------------------------
        // Verificação
        // -------------------------------------------------------------------------------------------------

        Meta m = (Meta) GestorEntityManagerProvider.getEntityManager().find(Meta.class, metaCriada.getId());
        
        Assert.assertNotNull(m.getDataHoraRemocao());
        Assert.assertEquals(TestUtils.getUsuarioRodrigo(), m.getUsuarioRemocao());

        for (Meta tlista : lista) {
            if (tlista.equals(m)){
                Assert.fail("Meta foi removida e nao deveria estar na lista");
            }
            
        }
        
        boolean existe = false;
        for (Meta tlista : listaRemovidas) {
            if (tlista.equals(m)){
                existe = true;
            }
            
        }
        Assert.assertTrue("Meta foi removida e deveria estar na lista de tarefas removidas", existe);
        
        
    }

    
    @Test
    public void recuperarMetaTest(){
        
        // -------------------------------------------------------------------------------------------------
        // Preparação
        // -------------------------------------------------------------------------------------------------

        String nomeMeta = "Meta Teste: recuperarMetaTest";
        
        TestUtils.setUsuarioLogado(TestUtils.getUsuarioRodrigo());

        // criar uma terefa
        Meta metaCriada = TestUtils.cadastrarMetaSimples(nomeMeta);
        metaCriada.setUsuarioResponsavel(TestUtils.getUsuarioRodrigo());
        GestorEntityManagerProvider.getEntityManager().getTransaction().begin();
        GestorEntityManagerProvider.getEntityManager().merge(metaCriada);
        GestorEntityManagerProvider.getEntityManager().getTransaction().commit();
        
        //remove-la
        LixeiraModel.removerMeta(metaCriada, TestUtils.getUsuarioRodrigo());
        
        // -------------------------------------------------------------------------------------------------
        // Ação
        // -------------------------------------------------------------------------------------------------

        // recuperar a tarefa removida
        LixeiraView view = new LixeiraView();
        LixeiraPresenter presenter = new LixeiraPresenter(view);
        presenter.aprentarLixeira();
        // aciona comando para restaurar
        presenter.restaurarMetaConfirmada(metaCriada);

        // -------------------------------------------------------------------------------------------------
        // Verificação
        // -------------------------------------------------------------------------------------------------
    
        Meta m = (Meta) GestorEntityManagerProvider.getEntityManager().find(Meta.class, metaCriada.getId());
        
        Assert.assertNull(m.getDataHoraRemocao());
        Assert.assertNull(m.getUsuarioRemocao());

        List<Meta> lista = DashboardModel.listarMetas(TestUtils.getUsuarioRodrigo());
        List<Meta> listaRemovidas = LixeiraModel.listarMetasRemovidas(TestUtils.getUsuarioRodrigo());

        for (Meta tlista : listaRemovidas) {
            if (tlista.equals(m)){
                Assert.fail("Meta foi restaurada e nao deveria estar na lista de removidas");
            }
            
        }
        
        boolean existe = false;
        for (Meta tlista : lista) {
            if (tlista.equals(m)){
                existe = true;
            }
            
        }
        Assert.assertTrue("Meta foi restaurada e deveria estar na lista de tarefas", existe);
        
    }
    
}
