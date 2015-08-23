/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb;

import com.saax.gestorweb.model.TarefaModel;
import com.saax.gestorweb.model.LoginModel;
import com.saax.gestorweb.model.UsuarioModel;
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
import com.saax.gestorweb.view.TaskView;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    TaskView view;
    TarefaModel model;
    TarefaPresenter presenter;
    private static ResourceBundle mensagens = null;

    @BeforeClass
    public static void setUpClass() {

        try {

            TestUtils.connectDB();

            TestUtils.setUsuarioLogado(getUsuarioSolicitante());

            TestUtils.createGestorMDI();

            mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();

            // se assegura que nao existem tarefas ja cadastradas
            TestUtils.removeTodasTarefas();

            // ------------------------------------------------------------------------------------------------------------------
            // Preparação : Criar uma tarefa com usuário "fernando" como responsavel e "rodrigo" como solicitante
            // ------------------------------------------------------------------------------------------------------------------
            String nome = getNomeTarefa();

            // usuarios
            Usuario usuarioResponsavel = getUsuarioResponsavel();
            Usuario usuarioSolicitante = getUsuarioSolicitante();

            // usuario logado = solicitante
            GestorSession.setAttribute(SessionAttributesEnum.USUARIO_LOGADO.getAttributeName(), usuarioSolicitante);
            Usuario loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO.getAttributeName());

            // apos mudar o usuário logado é necessario resetar o presenter
            TaskView view = new TaskView();
            TarefaModel model = new TarefaModel();
            TarefaPresenter presenter = new TarefaPresenter(model, view);

            // abre o presenter para criação da tarefa
            HierarquiaProjetoDetalhe categoriaDefaultTarefa = model.getCategoriaDefaultTarefa();
            presenter.createTask(categoriaDefaultTarefa);

            view.getTaskNameTextField().setValue(nome);
            view.getPriorityCombo().setValue(PrioridadeTarefa.ALTA);
            view.getStartDateDateField().setValue(new Date());
            view.getCompanyCombo().setValue(loggedUser.getEmpresaAtiva());
            view.getAssigneeUserCombo().select(usuarioResponsavel);
            try {
                view.getTaskFieldGroup().commit();
            } catch (FieldGroup.CommitException ex) {
                fail(ex.getMessage());
            }
            presenter.gravarButtonClicked();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @AfterClass
    public static void tearDownClass() {

        // limpar tarefas cadastradas
        TestUtils.removeTodasTarefas();

    }

    @Before
    public void setUp() {

        view = new TaskView();
        model = new TarefaModel();
        presenter = new TarefaPresenter(model, view);

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
                .setParameter("empresa", TestUtils.getUsuarioLogado().getEmpresaAtiva())
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
                .setParameter("empresa", loggedUser.getEmpresaAtiva())
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

        // ------------------------------------------------------------------------------------------------------------------
        // Ação : Aceitar a tarefa
        // ------------------------------------------------------------------------------------------------------------------
        // 1o. é necessário trocar o usuário logado para o responsavel
        // usuario logado = responsavel
        TestUtils.setUsuarioLogado(getUsuarioResponsavel());

        // apos mudar o usuário logado é necessario resetar o presenter
        view = new TaskView();
        model = new TarefaModel();
        presenter = new TarefaPresenter(model, view);

        // aceitar a tarefa
        presenter.editar(t);

        presenter.getPopUpStatusPresenter().aceitarTarefaClicked();

        presenter.gravarButtonClicked();

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

        // ------------------------------------------------------------------------------------------------------------------
        // Ação : Recusar a tarefa
        // ------------------------------------------------------------------------------------------------------------------
        // 1o. é necessário trocar o usuário logado para o responsavel
        // usuario logado = responsavel
        TestUtils.setUsuarioLogado(getUsuarioResponsavel());

        // apos mudar o usuário logado é necessario resetar o presenter
        view = new TaskView();
        model = new TarefaModel();
        presenter = new TarefaPresenter(model, view);

        // aceitar a tarefa
        presenter.editar(t);

        PopUpStatusPresenter popUpPresenter = presenter.getPopUpStatusPresenter();
        popUpPresenter.recusarTarefaClicked();
        presenter.getPopUpStatusView().getMotivoRecusaTextArea().setValue("Motivo da recusa");
        popUpPresenter.confirmarRecusaClicked();

        presenter.gravarButtonClicked();

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

        // ------------------------------------------------------------------------------------------------------------------
        // Ação : Recusar a tarefa (CANCELANDO)
        // ------------------------------------------------------------------------------------------------------------------
        // 1o. é necessário trocar o usuário logado para o responsavel
        // usuario logado = responsavel
        TestUtils.setUsuarioLogado(getUsuarioResponsavel());

        // apos mudar o usuário logado é necessario resetar o presenter
        view = new TaskView();
        model = new TarefaModel();
        presenter = new TarefaPresenter(model, view);

        // aceitar a tarefa
        presenter.editar(t);

        PopUpStatusPresenter popUpPresenter = presenter.getPopUpStatusPresenter();
        popUpPresenter.recusarTarefaClicked();
        presenter.getPopUpStatusView().getMotivoRecusaTextArea().setValue("Motivo da recusa");
        presenter.getPopUpStatusView().getCancelarRecusa().click();

        presenter.gravarButtonClicked();

        // ------------------------------------------------------------------------------------------------------------------
        // Verificação : Tarefa continua no status NAO ACEITA
        // ------------------------------------------------------------------------------------------------------------------
        t = getTarefa();

        Assert.assertEquals(StatusTarefa.NAO_ACEITA, t.getStatus());

    }

}
