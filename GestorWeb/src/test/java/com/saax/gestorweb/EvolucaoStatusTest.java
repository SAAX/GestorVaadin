/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb;

import com.saax.gestorweb.model.TarefaModel;
import com.saax.gestorweb.model.PopUpStatusModel;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.PrioridadeTarefa;
import com.saax.gestorweb.model.datamodel.StatusTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.presenter.TarefaPresenter;
import com.saax.gestorweb.presenter.PopUpStatusPresenter;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.SessionAttributesEnum;
import com.saax.gestorweb.util.TestUtils;
import com.saax.gestorweb.view.PopUpStatusView;
import com.saax.gestorweb.view.TarefaView;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.UI;
import java.util.Date;
import java.util.ResourceBundle;
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
public class EvolucaoStatusTest {

    PopUpStatusView view;
    PopUpStatusModel model;
    PopUpStatusPresenter presenter;
    private static ResourceBundle mensagens = null;

    @BeforeClass
    public static void setUpClass() {

        try {

            TestUtils.connectDB();

            TestUtils.setUsuarioLogado(getUsuarioSolicitante());

            TestUtils.createGestorMDI();

            mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();

            // se assegura que nao existem tarefas ja cadastradas
            TestUtils.limpaBase();

            // ------------------------------------------------------------------------------------------------------------------
            // Preparação : Criar uma tarefa com usuário "fernando" como responsavel e "rodrigo" como solicitante
            // ------------------------------------------------------------------------------------------------------------------
            String nome = getNomeTarefa();

            // usuarios
            Usuario usuarioResponsavel = getUsuarioResponsavel();
            Usuario usuarioSolicitante = getUsuarioSolicitante();

            // usuario logado = solicitante
            GestorSession.setAttribute(SessionAttributesEnum.USUARIO_LOGADO, usuarioSolicitante);
            Usuario loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO);

            // apos mudar o usuário logado é necessario resetar o presenter
            TarefaView view = new TarefaView();
            TarefaPresenter presenter = new TarefaPresenter(view);

            // abre o presenter para criação da tarefa
            HierarquiaProjetoDetalhe categoriaDefaultTarefa = TarefaModel.getCategoriaDefaultTarefa();
            presenter.createTask(categoriaDefaultTarefa, TestUtils.getUsuarioLogado().getEmpresas().get(0).getEmpresa());

            view.getNomeTarefaTextField().setValue(nome);
            view.getPrioridadeCombo().setValue(PrioridadeTarefa.ALTA);
            view.getDataInicioDateField().setValue(new Date());
            view.getEmpresaCombo().setValue(loggedUser.getEmpresas().get(0).getEmpresa());
            view.getUsuarioResponsavelCombo().select(usuarioResponsavel);
            try {
                view.getTarefaFieldGroup().commit();
            } catch (FieldGroup.CommitException ex) {
                fail(ex.getMessage());
            }
            view.getGravarButton().click();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @AfterClass
    public static void tearDownClass() {

        // limpar tarefas cadastradas
        TestUtils.limpaBase();

    }

    @Before
    public void setUp() {

        view = new PopUpStatusView();
        model = new PopUpStatusModel();

    }

    // ---------------------------------------------------------------------------------------
    // Metodos utilitarios
    // ---------------------------------------------------------------------------------------
    private static Usuario getUsuarioResponsavel() {
        return TestUtils.getUsuarioFernando();
    }

    private static Usuario getUsuarioSolicitante() {
        return TestUtils.getUsuarioRodrigo();
    }

    private static String getNomeTarefa() {
        return "Tarefa test: Evolucao Status";
    }

    private Tarefa getTarefa() {

        Tarefa t = (Tarefa) GestorEntityManagerProvider.getEntityManager().createNamedQuery("Tarefa.findByNome")
                .setParameter("nome", getNomeTarefa())
                .setParameter("empresa", TestUtils.getUsuarioLogado().getEmpresas().get(0).getEmpresa())
                .getSingleResult();
        return t;

    }

    /**
     * verifica se a preparação foi bem sucedida
     */
    @Test
    public void verificaPreparacao() {

        TestUtils.setUsuarioLogado(getUsuarioSolicitante());
        Usuario loggedUser = TestUtils.getUsuarioLogado();

        Tarefa t = (Tarefa) GestorEntityManagerProvider.getEntityManager().createNamedQuery("Tarefa.findByNome")
                .setParameter("nome", getNomeTarefa())
                .setParameter("empresa", loggedUser.getEmpresas().get(0).getEmpresa())
                .getSingleResult();

        Assert.assertNotNull(t);
        Assert.assertEquals(getUsuarioResponsavel(), t.getUsuarioResponsavel());
        Assert.assertEquals(getUsuarioSolicitante(), t.getUsuarioSolicitante());

    }

    /**
     * Testa o aceiteTest de uma tarefa
     */
    @Test
    public void aceiteTest() {

        System.out.println("Testando aceite de tarefa");

        // ------------------------------------------------------------------------------------------------------------------
        // Preparação: colocar a tarefa no status: NAO_ACEITA
        // ------------------------------------------------------------------------------------------------------------------
        Tarefa t = getTarefa();
        t.setStatus(StatusTarefa.NAO_ACEITA);
        GestorEntityManagerProvider.getEntityManager().getTransaction().begin();
        GestorEntityManagerProvider.getEntityManager().merge(t);
        GestorEntityManagerProvider.getEntityManager().getTransaction().commit();

        // é necessário trocar o usuário logado para o responsavel
        // usuario logado = responsavel
        TestUtils.setUsuarioLogado(getUsuarioResponsavel());        

        // ------------------------------------------------------------------------------------------------------------------
        // Ação : Aceitar a tarefa
        // ------------------------------------------------------------------------------------------------------------------

        // aceitar a tarefa
        presenter = new PopUpStatusPresenter(view);
        presenter.load(t, null, null);

        presenter.aceitarTarefaClicked();
                

        // ------------------------------------------------------------------------------------------------------------------
        // Verificação : Aceite da tarefa
        // ------------------------------------------------------------------------------------------------------------------
        t = getTarefa();

        Assert.assertEquals(StatusTarefa.NAO_INICIADA, t.getStatus());

    }

    /**
     * Testa a recusa de uma tarefa
     */
    @Test
    public void recusaTest() {

        System.out.println("Testando recusa de tarefa");

        // ------------------------------------------------------------------------------------------------------------------
        // Preparação: colocar a tarefa no status: NAO_ACEITA
        // ------------------------------------------------------------------------------------------------------------------
        Tarefa t = getTarefa();
        t.setStatus(StatusTarefa.NAO_ACEITA);
        GestorEntityManagerProvider.getEntityManager().getTransaction().begin();
        GestorEntityManagerProvider.getEntityManager().merge(t);
        GestorEntityManagerProvider.getEntityManager().getTransaction().commit();

        // é necessário trocar o usuário logado para o responsavel
        // usuario logado = responsavel
        TestUtils.setUsuarioLogado(getUsuarioResponsavel());        

        // ------------------------------------------------------------------------------------------------------------------
        // Ação : Recusar a tarefa
        // ------------------------------------------------------------------------------------------------------------------
        // aceitar a tarefa
        presenter = new PopUpStatusPresenter(view);
        presenter.load(t, null, null);

        presenter.recusarTarefaClicked();
        view.getMotivoRecusaTextArea().setValue("teste");
        view.getConfirmarRecusa().click();

        // ------------------------------------------------------------------------------------------------------------------
        // Verificação : Aceite da recusa
        // ------------------------------------------------------------------------------------------------------------------
        t = getTarefa();

        Assert.assertEquals(StatusTarefa.RECUSADA, t.getStatus());

    }

    /**
     * Testa a recusa de uma tarefa
     */
    @Test
    public void recusaCanceladaTest() {

        System.out.println("Testando recusa cancelada de tarefa");

        // ------------------------------------------------------------------------------------------------------------------
        // Preparação: colocar a tarefa no status: NAO_ACEITA
        // ------------------------------------------------------------------------------------------------------------------
        Tarefa t = getTarefa();
        t.setStatus(StatusTarefa.NAO_ACEITA);
        GestorEntityManagerProvider.getEntityManager().getTransaction().begin();
        GestorEntityManagerProvider.getEntityManager().merge(t);
        GestorEntityManagerProvider.getEntityManager().getTransaction().commit();

        // é necessário trocar o usuário logado para o responsavel
        // usuario logado = responsavel
        TestUtils.setUsuarioLogado(getUsuarioResponsavel());        
        
        // ------------------------------------------------------------------------------------------------------------------
        // Ação : Recusar a tarefa (CANCELANDO)
        // ------------------------------------------------------------------------------------------------------------------
        presenter = new PopUpStatusPresenter(view);
        presenter.load(t, null, null);
        
        presenter.recusarTarefaClicked();
        view.getMotivoRecusaTextArea().setValue("Motivo da recusa");
        view.getCancelarRecusa().click();

        // ------------------------------------------------------------------------------------------------------------------
        // Verificação : Tarefa continua no status NAO ACEITA
        // ------------------------------------------------------------------------------------------------------------------
        t = getTarefa();

        Assert.assertEquals(StatusTarefa.NAO_ACEITA, t.getStatus());

    }

    /**
     * Testa o registro do andamento da tarefa em 50%
     */
    @Test
    public void andamento50Test() {

        System.out.println("Testando registro do andamento da tarefa em 50%");

        // ------------------------------------------------------------------------------------------------------------------
        // Preparação: colocar a tarefa no status: NAO_INICIADA
        // ------------------------------------------------------------------------------------------------------------------
        Tarefa t = getTarefa();
        t.setStatus(StatusTarefa.NAO_INICIADA);
        GestorEntityManagerProvider.getEntityManager().getTransaction().begin();
        GestorEntityManagerProvider.getEntityManager().merge(t);
        GestorEntityManagerProvider.getEntityManager().getTransaction().commit();

        // é necessário trocar o usuário logado para o responsavel
        // usuario logado = responsavel
        TestUtils.setUsuarioLogado(getUsuarioResponsavel());        

        // ------------------------------------------------------------------------------------------------------------------
        // Ação : Registrar andamento
        // ------------------------------------------------------------------------------------------------------------------
        presenter = new PopUpStatusPresenter(view);
        presenter.load(t, null, null);
        
        presenter.getStatusButton().setPopupVisible(true);
        view.getAndamentoTarefaCombo().setValue(50);
        view.getComentarioAndamento().setValue("Teste");
        presenter.processarAlteracaoAndamento();
        
        // ------------------------------------------------------------------------------------------------------------------
        // Verificação : Aceite da tarefa
        // ------------------------------------------------------------------------------------------------------------------
        t = getTarefa();

        Assert.assertEquals(StatusTarefa.EM_ANDAMENTO, t.getStatus());
        Assert.assertEquals(50, t.getAndamento());

        
        
    }
    /**
     * Testa o registro da conlusao da tarefa em 100%
     */
    @Test
    public void conclusaoTest() {

        System.out.println("Testando o registro da conlusao da tarefa em 100%");

        // ------------------------------------------------------------------------------------------------------------------
        // Preparação: colocar a tarefa no status: NAO_INICIADA
        // ------------------------------------------------------------------------------------------------------------------
        Tarefa t = getTarefa();
        t.setStatus(StatusTarefa.EM_ANDAMENTO);
        GestorEntityManagerProvider.getEntityManager().getTransaction().begin();
        GestorEntityManagerProvider.getEntityManager().merge(t);
        GestorEntityManagerProvider.getEntityManager().getTransaction().commit();

        // é necessário trocar o usuário logado para o responsavel
        // usuario logado = responsavel
        TestUtils.setUsuarioLogado(getUsuarioResponsavel());        

        // ------------------------------------------------------------------------------------------------------------------
        // Ação : Registrar andamento
        // ------------------------------------------------------------------------------------------------------------------
        presenter = new PopUpStatusPresenter(view);
        presenter.load(t, null, null);
        
        presenter.getStatusButton().setPopupVisible(true);
        view.getAndamentoTarefaCombo().setValue(100);
        view.getComentarioAndamento().setValue("Teste");
        presenter.processarAlteracaoAndamento();
        
        // ------------------------------------------------------------------------------------------------------------------
        // Verificação : Aceite da tarefa
        // ------------------------------------------------------------------------------------------------------------------
        t = getTarefa();

        Assert.assertEquals(StatusTarefa.CONCLUIDA, t.getStatus());
        Assert.assertEquals(100, t.getAndamento());

        
        
    }
    
    
}
