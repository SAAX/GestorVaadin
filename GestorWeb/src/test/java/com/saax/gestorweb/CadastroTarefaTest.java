/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb;

import com.saax.gestorweb.model.TarefaModel;
import com.saax.gestorweb.model.LoginModel;
import com.saax.gestorweb.model.datamodel.ApontamentoTarefa;
import com.saax.gestorweb.model.datamodel.CentroCusto;
import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.HierarquiaProjeto;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.HistoricoTarefa;
import com.saax.gestorweb.model.datamodel.OrcamentoTarefa;
import com.saax.gestorweb.model.datamodel.PrioridadeTarefa;
import com.saax.gestorweb.model.datamodel.StatusTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.TipoTarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.presenter.TarefaPresenter;
import com.saax.gestorweb.presenter.PopUpStatusPresenter;
import com.saax.gestorweb.util.DBConnect;
import com.saax.gestorweb.util.DAOAleatorio;
import com.saax.gestorweb.util.DateTimeConverters;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.PostgresConnection;
import com.saax.gestorweb.util.SessionAttributesEnum;
import com.saax.gestorweb.view.TaskView;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.UI;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.time.DateUtils;
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
public class CadastroTarefaTest {

    TaskView view;
    TarefaModel model;
    TarefaPresenter presenter;
    private EntityManager em;
    private static List<Tarefa> tarefasCadastradas;
    private static ResourceBundle mensagens = null;

    @BeforeClass
    public static void setUpClass() {

        try {
            // connect to database
            DBConnect.getInstance().assertConnection();
            EntityManager em = PostgresConnection.getInstance().getEntityManagerFactory().createEntityManager();
            GestorEntityManagerProvider.setCurrentEntityManager(em);

            // set logged user
            Usuario usuario = (Usuario) em.createNamedQuery("Usuario.findByLogin").setParameter("login", "teste-user@gmail.com").getSingleResult();
            GestorSession.setAttribute("loggedUser", usuario);
            usuario.setEmpresaAtiva(new LoginModel().getEmpresaUsuarioLogado());

            // creates UI
            GestorMDI gestor = new GestorMDI();
            UI.setCurrent(gestor);
            gestor.init(null);

            mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
            tarefasCadastradas = new ArrayList<>();

            // se assegura que nao existem tarefas ja cadastradas
            Usuario loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO.getAttributeName());
            GestorEntityManagerProvider.getEntityManager().getTransaction().begin();
            List<Tarefa> tarefas = em.createNamedQuery("Tarefa.findAll")
                    .setParameter("empresa", loggedUser.getEmpresaAtiva()).getResultList();
            for (Tarefa tarefa : tarefas) {
                if (tarefa.getTarefaPai() == null) {
                    tarefa = GestorEntityManagerProvider.getEntityManager().getReference(Tarefa.class, tarefa.getId());
                    GestorEntityManagerProvider.getEntityManager().remove(tarefa);
                }
            }
            for (Empresa sub : loggedUser.getEmpresaAtiva().getSubEmpresas()) {
                tarefas = em.createNamedQuery("Tarefa.findAll")
                        .setParameter("empresa", sub).getResultList();
                for (Tarefa tarefa : tarefas) {
                    if (tarefa.getTarefaPai() == null) {
                        tarefa = GestorEntityManagerProvider.getEntityManager().getReference(Tarefa.class, tarefa.getId());
                        GestorEntityManagerProvider.getEntityManager().remove(tarefa);
                    }
                }

            }
            GestorEntityManagerProvider.getEntityManager().getTransaction().commit();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @AfterClass
    public static void tearDownClass() {

        
        // limpar tarefas cadastradas
        GestorEntityManagerProvider.getEntityManager().getTransaction().begin();
        // limpar tarefas cadastradas
        for (Tarefa tarefa : tarefasCadastradas) {
            if (tarefa.getTarefaPai() == null) {
                tarefa = GestorEntityManagerProvider.getEntityManager().find(Tarefa.class, tarefa.getId());
                GestorEntityManagerProvider.getEntityManager().remove(tarefa);
            }
        }
        GestorEntityManagerProvider.getEntityManager().getTransaction().commit();

        // disconnect
        GestorEntityManagerProvider.getEntityManager().close();

    }

    @Before
    public void setUp() {

        view = new TaskView();
        model = new TarefaModel();
        presenter = new TarefaPresenter(model, view);

        em = GestorEntityManagerProvider.getEntityManager();
    }

    /**
     * Testa o cadastro de uma tarefa simples, onde só os campos obrigatórios
     * são preenchidos
     */
    @Test
    public void cadastrarTarefaSimples() {

        System.out.println("Testando cadastro simples de tarefa");

        Usuario loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO.getAttributeName());

        HierarquiaProjetoDetalhe categoriaDefaultMeta = model.getCategoriaDefaultTarefa();
        presenter.createTask(categoriaDefaultMeta);

        String nome = "Teste Cadastro Tarefa #1";
        view.getTaskNameTextField().setValue(nome);
        //view.getTipoRecorrenciaCombo().select(TipoTarefa.RECORRENTE);
        view.getRecurrencyButton().getCaption().equals("RECORRENTE");
        view.getPriorityCombo().setValue(PrioridadeTarefa.ALTA);
        view.getStartDateDateField().setValue(new Date());
        view.getCompanyCombo().setValue(loggedUser.getEmpresaAtiva());
        try {
            view.getTaskFieldGroup().commit();
        } catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        presenter.gravarButtonClicked();

        Tarefa t = (Tarefa) em.createNamedQuery("Tarefa.findByNome")
                .setParameter("nome", nome)
                .setParameter("empresa", loggedUser.getEmpresaAtiva())
                .getSingleResult();

        tarefasCadastradas.add(t);

        Assert.assertEquals(nome, t.getNome());

        Assert.assertEquals(StatusTarefa.NAO_INICIADA, t.getStatus());

        Assert.assertNotNull(t.getDataHoraInclusao());

    }

    /**
     * Testa o cadastro de uma tarefa simples com arquivo anexo
     */
    @Test
    public void cadastrarTarefaSimplesComAnexo() {

        System.out.println("Testando cadastro de tarefa com anexo");

        Usuario loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO.getAttributeName());

        HierarquiaProjetoDetalhe categoriaDefaultMeta = model.getCategoriaDefaultTarefa();
        presenter.createTask(categoriaDefaultMeta);

        String nome = "Teste Cadastro Tarefa com Anexo";
        view.getTaskNameTextField().setValue(nome);
        //view.getTipoRecorrenciaCombo().select(TipoTarefa.RECORRENTE);
        view.getRecurrencyButton().getCaption().equals("RECORRENTE");
        view.getPriorityCombo().setValue(PrioridadeTarefa.ALTA);
        view.getStartDateDateField().setValue(new Date());
        view.getCompanyCombo().setValue(loggedUser.getEmpresaAtiva());

        File anexoTeste = new File(System.getProperty("user.dir") + "/anexoTeste.pdf");
        try {
            anexoTeste.createNewFile();
            presenter.anexoAdicionado(anexoTeste);
        } catch (IOException ex) {
            fail(ex.getMessage());
        }

        try {
            view.getTaskFieldGroup().commit();
        } catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        presenter.gravarButtonClicked();

        Tarefa t = (Tarefa) em.createNamedQuery("Tarefa.findByNome")
                .setParameter("nome", nome)
                .setParameter("empresa", loggedUser.getEmpresaAtiva())
                .getSingleResult();

        tarefasCadastradas.add(t);

        Assert.assertEquals(nome, t.getNome());

        Assert.assertEquals(StatusTarefa.NAO_INICIADA, t.getStatus());

        Assert.assertNotNull(t.getDataHoraInclusao());

    }

    /**
     * Testa o cadastro completo de uma tarefa, com todos os campos disponiveis
     * na
     */
    @Test
    public void cadastrarTarefaCompleta() {

        System.out.println("Testando cadastro completo de uma tarefa");

        Usuario loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO.getAttributeName());
        Usuario usuarioResponsavel = (Usuario) em.createNamedQuery("Usuario.findByLogin").setParameter("login", "rodrigo.ccn2005@gmail.com").getSingleResult();

        HierarquiaProjetoDetalhe categoriaDefaultMeta = model.getCategoriaDefaultTarefa();
        presenter.createTask(categoriaDefaultMeta);

        String nome = "Teste Cadastro Tarefa #2";

        // ---------------------------------------------------------------------
        // Setando os campos
        //        private Integer id;
        //        private HierarquiaProjetoDetalhe hierarquia;
        HierarquiaProjeto hierarquiaProjetoDefault = (HierarquiaProjeto) em.createNamedQuery("HierarquiaProjeto.findByNome")
                .setParameter("nome", "Norma")
                .getSingleResult();

        for (HierarquiaProjetoDetalhe categoria : hierarquiaProjetoDefault.getCategorias()) {
            if (categoria.getNivel() == 2) {
                view.getHierarchyCombo().setValue(categoria);
            }
        }
        //        private Empresa empresa;
        view.getCompanyCombo().setValue(loggedUser.getEmpresaAtiva());
        //        private String nome;
        view.getTaskNameTextField().setValue(nome);
        //        private PrioridadeTarefa prioridade;
        view.getPriorityCombo().setValue(PrioridadeTarefa.ALTA);
        //        private StatusTarefa status;
        //        private ProjecaoTarefa projecao;
        //        private int andamento;
        //        private String descricao;
        view.getTaskDescriptionTextArea().setValue("Descrição da Tarefa #2");
        //        private boolean apontamentoHoras;
        view.getApontamentoHorasCheckBox().setValue(Boolean.TRUE);
        //        private boolean orcamentoControlado;
        view.getBudgetControlCheckBox().setValue(Boolean.FALSE);
        //        private CentroCusto centroCusto;
        CentroCusto centroCusto = DAOAleatorio.getCentroCustoAleatorio(em);
        view.getCostCenterCombo().setValue(centroCusto);
        //        private Departamento departamento;
        Departamento departamento = DAOAleatorio.getDepartamentoAleatorio(em, loggedUser.getEmpresaAtiva());
        view.getDepartamentCombo().setValue(departamento);
        //        private FilialEmpresa filialEmpresa;
        //        private EmpresaCliente empresaCliente;
        EmpresaCliente empresaCliente = DAOAleatorio.getEmpresaClienteAleatoria(em, loggedUser.getEmpresaAtiva());
        view.getCustomerCompanyCombo().setValue(empresaCliente);
        //        private List<Tarefa> subTarefas;
        //        private Tarefa proximaTarefa;
        //        private TipoTarefa tipoRecorrencia;
        view.getRecurrencyButton().getCaption().equals("RECORRENTE");
        //view.getTipoRecorrenciaCombo().select(TipoTarefa.RECORRENTE);
        //        private Tarefa tarefaPai;
        //        private LocalDate dataInicio;
        Date dataInicio = DAOAleatorio.getDataByOffset(20, true); // hoje + 20 dias
        view.getStartDateDateField().setValue(dataInicio);
        //        private LocalDate dataTermino;
        //        private LocalDate dataFim;
        Date dataFim = DAOAleatorio.getDataByOffset(30, true); // hoje + 30 dias
        view.getEndDateDateField().setValue(dataFim);
        //        private Usuario usuarioInclusao;
        //        private Usuario usuarioSolicitante;
        //        private Usuario usuarioResponsavel;
        view.getAssigneeUserCombo().setValue(usuarioResponsavel);
        //        private List<ParticipanteTarefa> participantes;
        Usuario usuarioParticipante_0 = (Usuario) em.createNamedQuery("Usuario.findByLogin").setParameter("login", "fernando.saax@gmail.com").getSingleResult();
        view.getFollowersCombo().setValue(usuarioParticipante_0);
        Usuario usuarioParticipante_1 = (Usuario) em.createNamedQuery("Usuario.findByLogin").setParameter("login", "danielstavale@gmail.com").getSingleResult();
        view.getFollowersCombo().setValue(usuarioParticipante_1);
        //        private List<AvaliacaoMetaTarefa> avaliacoes;
        //        private List<OrcamentoTarefa> orcamentos;
        view.getBudgetAddTextField().setValue("123.34");
        view.getObservationBudgetTextField().setValue("v0");
        OrcamentoTarefa orcamentoTarefa_0 = new OrcamentoTarefa();
        try {
            orcamentoTarefa_0 = view.getOrcamentoTarefa();
        } catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        view.getBudgetAddButton().click();

        view.getBudgetAddTextField().setValue("254.67");
        view.getObservationBudgetTextField().setValue("v1");
        OrcamentoTarefa orcamentoTarefa_1 = new OrcamentoTarefa();
        try {
            orcamentoTarefa_1 = view.getOrcamentoTarefa();
        } catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        view.getBudgetAddButton().click();
        //        private List<ApontamentoTarefa> apontamentos;
        view.getHoursAddTextField().setValue("135:00");
        view.getHourCostTextField().setValue("14.36"); // 1938.6
        ApontamentoTarefa apontamento_0 = new ApontamentoTarefa();
        try {
            apontamento_0 = view.getApontamentoTarefa();
        } catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        view.getHoursAddButton().click();

        view.getHoursAddTextField().setValue("214:30"); // 3080.22
        ApontamentoTarefa apontamento_1 = new ApontamentoTarefa();
        try {
            apontamento_1 = view.getApontamentoTarefa();
        } catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        view.getHoursAddButton().click();
        //        private List<AnexoTarefa> anexos;
        //        private List<AndamentoTarefa> andamentos;
        //        private List<BloqueioTarefa> bloqueios;
        //        private List<HistoricoTarefa> historico;
        //        private LocalDateTime dataHoraInclusao;

        try {
            view.getTaskFieldGroup().commit();
        } catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        presenter.gravarButtonClicked();

        Tarefa t = (Tarefa) em.createNamedQuery("Tarefa.findByNome")
                .setParameter("nome", nome)
                .setParameter("empresa", loggedUser.getEmpresaAtiva())
                .getSingleResult();

        tarefasCadastradas.add(t);

        // ---------------------------------------------------------------------
        // Conferindo resultado
        // ---------------------------------------------------------------------
        //        private Integer id;
        //        private int nivel;
        Assert.assertEquals(2, t.getHierarquia().getNivel());
        //        private String titulo;
        Assert.assertEquals("Tarefa", t.getHierarquia().getCategoria());
        //        private String nome;
        Assert.assertEquals(nome, t.getNome());
        //        private PrioridadeTarefa prioridade;
        Assert.assertEquals(PrioridadeTarefa.ALTA, t.getPrioridade());
        //        private StatusTarefa status;
        Assert.assertEquals(StatusTarefa.NAO_ACEITA, t.getStatus());
        //        private ProjecaoTarefa projecao;
        //        private int andamento;
        Assert.assertEquals(0, t.getAndamento());
        //        private String descricao;
        Assert.assertEquals("Descrição da Tarefa #2", t.getDescricao());
        //        private boolean apontamentoHoras;
        Assert.assertEquals(true, t.isApontamentoHoras());
        //        private boolean orcamentoControlado;
        Assert.assertEquals(false, t.isOrcamentoControlado());
        //        private CentroCusto centroCusto;
        Assert.assertEquals(centroCusto, t.getCentroCusto());
        //        private Departamento departamento;
        Assert.assertEquals(departamento, t.getDepartamento());
        //        private Empresa empresa;
        Assert.assertEquals(loggedUser.getEmpresaAtiva(), t.getEmpresa());
        //        private FilialEmpresa filialEmpresa;
        //        private EmpresaCliente empresaCliente;
        Assert.assertEquals(empresaCliente, t.getEmpresaCliente());
        //        private List<Tarefa> subTarefas;
        //        private Tarefa proximaTarefa;
        //        private TipoTarefa tipoRecorrencia;
        Assert.assertEquals(TipoTarefa.UNICA, t.getTipoRecorrencia());
        //        private Tarefa tarefaPai;
        Assert.assertNull(t.getTarefaPai());
        //        private LocalDate dataTermino;
        //        private LocalDate dataInicio;
        Assert.assertEquals(DateUtils.truncate(dataInicio, Calendar.DATE), DateTimeConverters.toDate(t.getDataInicio()));
        //        private LocalDate dataFim;
        Assert.assertEquals(DateUtils.truncate(dataFim, Calendar.DATE), DateTimeConverters.toDate(t.getDataFim()));
        //        private Usuario usuarioInclusao;
        Assert.assertEquals(loggedUser, t.getUsuarioInclusao());
        //        private Usuario usuarioSolicitante;
        Assert.assertEquals(loggedUser, t.getUsuarioSolicitante());
        //        private Usuario usuarioResponsavel;
        Assert.assertEquals(usuarioResponsavel, t.getUsuarioResponsavel());
        //        private List<ParticipanteTarefa> participantes;
        Assert.assertEquals(usuarioParticipante_0, t.getParticipantes().get(0).getUsuarioParticipante());
        Assert.assertEquals(usuarioParticipante_1, t.getParticipantes().get(1).getUsuarioParticipante());
        //        private List<AvaliacaoMetaTarefa> avaliacoes;
        //        private List<OrcamentoTarefa> orcamentos;
        Assert.assertEquals(orcamentoTarefa_0.getCredito(), t.getOrcamentos().get(0).getCredito());
        Assert.assertEquals(orcamentoTarefa_0.getSaldo(), t.getOrcamentos().get(0).getSaldo());
        Assert.assertEquals(orcamentoTarefa_1.getCredito(), t.getOrcamentos().get(1).getCredito());
        Assert.assertEquals(orcamentoTarefa_1.getSaldo(), t.getOrcamentos().get(1).getSaldo());
        //        private List<ApontamentoTarefa> apontamentos;
        Assert.assertEquals(apontamento_0.getCreditoHoras(), t.getApontamentos().get(0).getCreditoHoras());
        Assert.assertEquals(apontamento_0.getCreditoValor(), t.getApontamentos().get(0).getCreditoValor());
        Assert.assertEquals(apontamento_0.getSaldoHoras(), t.getApontamentos().get(0).getSaldoHoras());
        Assert.assertEquals(apontamento_1.getCreditoHoras(), t.getApontamentos().get(1).getCreditoHoras());
        Assert.assertEquals(apontamento_1.getCreditoValor(), t.getApontamentos().get(1).getCreditoValor());
        Assert.assertEquals(apontamento_1.getSaldoHoras(), t.getApontamentos().get(1).getSaldoHoras());
        //        private List<AnexoTarefa> anexos;
        //        private List<AndamentoTarefa> andamentos;
        //        private List<BloqueioTarefa> bloqueios;
        //        private List<HistoricoTarefa> historico;
        //        private LocalDateTime dataHoraInclusao;
        Assert.assertNotNull(t.getDataHoraInclusao());

    }

    @Test
    public void cadastrarMultiplasSubTarefas() {

        System.out.println("Testando cadastro multiplas sub tarefas");

        Usuario loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO.getAttributeName());

        // -------------------------------------------------------------------------------------
        // Tarefa:  Teste Multiplos Niveis
        // -------------------------------------------------------------------------------------
        HierarquiaProjetoDetalhe categoriaDefaultTarefa = null;
        HierarquiaProjeto hierarquiaProjetoDefault = (HierarquiaProjeto) em.createNamedQuery("HierarquiaProjeto.findByNome")
                .setParameter("nome", "Norma c/ Tarefa")
                .getSingleResult();

        for (HierarquiaProjetoDetalhe categoria : hierarquiaProjetoDefault.getCategorias()) {
            if (categoria.getNivel() == 2) {
                categoriaDefaultTarefa = categoria;
            }
        }

        presenter.createTask(categoriaDefaultTarefa);

        String nome_principal = "Teste Multiplos Niveis";
        view.getTaskNameTextField().setValue(nome_principal);
        //view.getTipoRecorrenciaCombo().select(TipoTarefa.RECORRENTE);
        view.getRecurrencyButton().getCaption().equals("RECORRENTE");
        view.getPriorityCombo().setValue(PrioridadeTarefa.ALTA);
        view.getStartDateDateField().setValue(new Date());
        view.getCompanyCombo().setValue(loggedUser.getEmpresaAtiva());

        // -------------------------------------------------------------------------------------
        // Tarefa:  Teste Multiplos Niveis -> Sub 1
        // -------------------------------------------------------------------------------------
        try {
            view.getTaskFieldGroup().commit();
        } catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        TaskView view_sub1 = new TaskView();
        TarefaModel model_sub1 = new TarefaModel();

        TarefaPresenter presenter_sub1 = new TarefaPresenter(model_sub1, view_sub1);
        presenter_sub1.setCallBackListener(presenter);

        presenter_sub1.criarNovaSubTarefa(view.getTarefa());

        String nome_sub1 = "Sub 1";
        view_sub1.getTaskNameTextField().setValue(nome_sub1);
        //view_sub1.getTipoRecorrenciaCombo().select(TipoTarefa.RECORRENTE);
        view_sub1.getRecurrencyButton().getCaption().equals("RECORRENTE");
        view_sub1.getPriorityCombo().setValue(PrioridadeTarefa.ALTA);
        view_sub1.getStartDateDateField().setValue(new Date());
        view_sub1.getHierarchyCombo().setValue(view_sub1.getHierarchyCombo().getItemIds().toArray()[0]);

        // -------------------------------------------------------------------------------------
        // Tarefa:  Teste Multiplos Niveis -> Sub 1 -> Sub 2
        // -------------------------------------------------------------------------------------
        try {
            view_sub1.getTaskFieldGroup().commit();
        } catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        TaskView view_sub2 = new TaskView();
        TarefaModel model_sub2 = new TarefaModel();

        TarefaPresenter presenter_sub2 = new TarefaPresenter(model_sub2, view_sub2);
        presenter_sub2.setCallBackListener(presenter_sub1);

        presenter_sub2.criarNovaSubTarefa(view_sub1.getTarefa());

        String nome_sub2 = "Sub 2";
        view_sub2.getTaskNameTextField().setValue(nome_sub2);
        //view_sub2.getTipoRecorrenciaCombo().select(TipoTarefa.RECORRENTE);
        view_sub2.getRecurrencyButton().getCaption().equals("RECORRENTE");
        view_sub2.getPriorityCombo().setValue(PrioridadeTarefa.ALTA);
        view_sub2.getStartDateDateField().setValue(new Date());
        view_sub2.getHierarchyCombo().setValue(view_sub2.getHierarchyCombo().getItemIds().toArray()[0]);

        // Grava a sub nivel 2
        try {
            view_sub2.getTaskFieldGroup().commit();
        } catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        presenter_sub2.gravarButtonClicked();

        // Grava a sub nivel 1
        try {
            view_sub1.getTaskFieldGroup().commit();
        } catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        presenter_sub1.gravarButtonClicked();

        // Grava a principal
        try {
            view.getTaskFieldGroup().commit();
        } catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        presenter.gravarButtonClicked();

        Tarefa t = (Tarefa) em.createNamedQuery("Tarefa.findByNome")
                .setParameter("nome", nome_principal)
                .setParameter("empresa", loggedUser.getEmpresaAtiva())
                .getSingleResult();

        tarefasCadastradas.add(t);
        Tarefa sub1 = t.getSubTarefas().get(0);
        tarefasCadastradas.add(sub1);
        Tarefa sub2 = sub1.getSubTarefas().get(0);
        tarefasCadastradas.add(sub2);

        // Valida resultados da principal
        Assert.assertEquals(nome_principal, t.getNome());
        Assert.assertEquals(StatusTarefa.NAO_INICIADA, t.getStatus());
        Assert.assertNotNull(t.getDataHoraInclusao());

        // Valida resultados da sub nivel 1
        Assert.assertEquals(sub1.getTarefaPai(), t);
        Assert.assertEquals(nome_sub1, sub1.getNome());
        Assert.assertEquals(StatusTarefa.NAO_INICIADA, sub1.getStatus());
        Assert.assertNotNull(sub1.getDataHoraInclusao());

        // Valida resultados da sub nivel 2
        Assert.assertEquals(sub2.getTarefaPai(), sub1);
        Assert.assertEquals(nome_sub2, sub2.getNome());
        Assert.assertEquals(StatusTarefa.NAO_INICIADA, sub2.getStatus());
        Assert.assertNotNull(sub2.getDataHoraInclusao());

    }

    /**
     * Testa a edição da tarefa completa
     *
     */
    @Test
    public void editarTarefaCompleta() {

        System.out.println("Testando a edição (alteração) da tarefa complesta");

        Usuario loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO.getAttributeName());
        Usuario usuarioResponsavel = (Usuario) em.createNamedQuery("Usuario.findByLogin").setParameter("login", "danielstavale@gmail.com").getSingleResult();

        String nome = "Teste Cadastro Tarefa #2";
        String novonome = "Teste Cadastro Tarefa #2 - Alterada";

        Tarefa t = (Tarefa) em.createNamedQuery("Tarefa.findByNome")
                .setParameter("nome", nome)
                .setParameter("empresa", loggedUser.getEmpresaAtiva())
                .getSingleResult();

        Assert.assertNotNull(t);
        presenter.editar(t);

        // ---------------------------------------------------------------------
        // Alterando os campos
        //        private String nome;
        view.getTaskNameTextField().setValue(novonome);
        view.getTaskNameTextField().commit();
        //        private PrioridadeTarefa prioridade;
        view.getPriorityCombo().setValue(PrioridadeTarefa.BAIXA);
        //        private StatusTarefa status;
        view.getTaskStatusPopUpButton().click();
        PopUpStatusPresenter presenterPopUP = presenter.getPresenterPopUpStatus();
        presenterPopUP.aceitarTarefaClicked(); // Tarefa ficará com status = aceita
        //        private ProjecaoTarefa projecao;
        //        private int andamento;
        //        private String descricao;
        view.getTaskDescriptionTextArea().setValue("Descrição da Tarefa #2 - Alterada");
        //        private boolean apontamentoHoras;
        view.getApontamentoHorasCheckBox().setValue(Boolean.FALSE);
        //        private boolean orcamentoControlado;
        view.getBudgetControlCheckBox().setValue(Boolean.TRUE);
        //        private CentroCusto centroCusto;
        CentroCusto centroCusto = DAOAleatorio.getCentroCustoAleatorio(em);
        view.getCostCenterCombo().setValue(centroCusto);
        //        private Departamento departamento;
        Departamento departamento = DAOAleatorio.getDepartamentoAleatorio(em, loggedUser.getEmpresaAtiva());
        view.getDepartamentCombo().setValue(departamento);
        //        private Empresa empresa;
        view.getCompanyCombo().setValue(loggedUser.getEmpresaAtiva());
        //        private FilialEmpresa filialEmpresa;
        //        private EmpresaCliente empresaCliente;
        EmpresaCliente empresaCliente = DAOAleatorio.getEmpresaClienteAleatoria(em, loggedUser.getEmpresaAtiva());
        view.getCustomerCompanyCombo().setValue(empresaCliente);
        //        private List<Tarefa> subTarefas;
        //        private Tarefa proximaTarefa;
        //        private TipoTarefa tipoRecorrencia;
        //view.getTipoRecorrenciaCombo().select(TipoTarefa.UNICA);
        view.getRecurrencyButton().getCaption().equals("ÚNICA");
        //        private Tarefa tarefaPai;
        //        private LocalDate dataInicio;
        Date dataInicio = DAOAleatorio.getDataByOffset(10, true); // hoje + 10 dias
        view.getStartDateDateField().setValue(dataInicio);
        //        private LocalDate dataTermino;
        //        private LocalDate dataFim;
        Date dataFim = DAOAleatorio.getDataByOffset(35, true); // hoje + 35 dias
        view.getEndDateDateField().setValue(dataFim);
        //        private Usuario usuarioInclusao;
        //        private Usuario usuarioSolicitante;
        //        private Usuario usuarioResponsavel;
        view.getAssigneeUserCombo().setValue(usuarioResponsavel);
        //        private List<ParticipanteTarefa> participantes;
        presenter.removerParticipante(t.getParticipantes().get(1));
        Usuario usuarioParticipante_0 = (Usuario) em.createNamedQuery("Usuario.findByLogin").setParameter("login", "fernando.saax@gmail.com").getSingleResult();
        Usuario usuarioParticipante_1 = (Usuario) em.createNamedQuery("Usuario.findByLogin").setParameter("login", "rodrigo.ccn2005@gmail.com").getSingleResult();
        view.getFollowersCombo().setValue(usuarioParticipante_1); // insere o participante rodrigo
        //        private List<AvaliacaoMetaTarefa> avaliacoes;
        //        private List<OrcamentoTarefa> orcamentos;
        presenter.removerRegistroOrcamento(t.getOrcamentos().get(0));
        OrcamentoTarefa orcamentoTarefa_0 = t.getOrcamentos().get(0);

        view.getBudgetAddTextField().setValue("472.17");
        view.getObservationBudgetTextField().setValue("v1");
        OrcamentoTarefa orcamentoTarefa_1 = new OrcamentoTarefa();
        try {
            orcamentoTarefa_1 = view.getOrcamentoTarefa();
        } catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        view.getBudgetAddButton().click();
//        //        private List<ApontamentoTarefa> apontamentos;
//        view.getImputarHorasTextField().setValue("135:00");
//        view.getCustoHoraTextField().setValue("14.36"); // 1938.6
//        ApontamentoTarefa apontamento_0 = new ApontamentoTarefa();
//        try {
//            apontamento_0 = view.getApontamentoTarefa();
//        } catch (FieldGroup.CommitException ex) {
//            fail(ex.getMessage());
//        }
//        view.getImputarHorasButton().click();
//
//        view.getImputarHorasTextField().setValue("214:30"); // 3080.22
//        ApontamentoTarefa apontamento_1 = new ApontamentoTarefa();
//        try {
//            apontamento_1 = view.getApontamentoTarefa();
//        } catch (FieldGroup.CommitException ex) {
//            fail(ex.getMessage());
//        }
//        view.getImputarHorasButton().click();
//        //        private List<AnexoTarefa> anexos;
//        //        private List<AndamentoTarefa> andamentos;
//        //        private List<BloqueioTarefa> bloqueios;
//        //        private List<HistoricoTarefa> historico;
//        //        private LocalDateTime dataHoraInclusao;

        try {
            view.getTaskFieldGroup().commit();
        } catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        view.getSaveButton().click();

        // obtem novamente a tarefa do banco
        t = (Tarefa) em.createNamedQuery("Tarefa.findByNome")
                .setParameter("nome", novonome)
                .setParameter("empresa", loggedUser.getEmpresaAtiva())
                .getSingleResult();

        // ---------------------------------------------------------------------
        // Conferindo resultado
        // ---------------------------------------------------------------------
        //        private Integer id;
        //        private int nivel;
        Assert.assertEquals(2, t.getHierarquia().getNivel());
        //        private String titulo;
        Assert.assertEquals("Tarefa", t.getHierarquia().getCategoria());
        //        private String nome;
        Assert.assertEquals(novonome, t.getNome());
        //        private PrioridadeTarefa prioridade;
        Assert.assertEquals(PrioridadeTarefa.ALTA, t.getPrioridade());
        //        private StatusTarefa status;
        Assert.assertEquals(StatusTarefa.NAO_INICIADA, t.getStatus());
        //        private ProjecaoTarefa projecao;
        //        private int andamento;
        Assert.assertEquals(0, t.getAndamento());
        //        private String descricao;
        Assert.assertEquals("Descrição da Tarefa #2 - Alterada", t.getDescricao());
        //        private boolean apontamentoHoras;
        Assert.assertEquals(false, t.isApontamentoHoras());
        //        private boolean orcamentoControlado;
        Assert.assertEquals(true, t.isOrcamentoControlado());
        //        private CentroCusto centroCusto;
        Assert.assertEquals(centroCusto, t.getCentroCusto());
        //        private Departamento departamento;
        Assert.assertEquals(departamento, t.getDepartamento());
        //        private Empresa empresa;
        Assert.assertEquals(loggedUser.getEmpresaAtiva(), t.getEmpresa());
        //        private FilialEmpresa filialEmpresa;
        //        private EmpresaCliente empresaCliente;
        Assert.assertEquals(empresaCliente, t.getEmpresaCliente());
        //        private List<Tarefa> subTarefas;
        //        private Tarefa proximaTarefa;
        //        private TipoTarefa tipoRecorrencia;
        Assert.assertEquals(TipoTarefa.UNICA, t.getTipoRecorrencia());
        //        private Tarefa tarefaPai;
        Assert.assertNull(t.getTarefaPai());
        //        private LocalDate dataTermino;
        //        private LocalDate dataInicio;
        Assert.assertEquals(DateUtils.truncate(dataInicio, Calendar.DATE), DateTimeConverters.toDate(t.getDataInicio()));
        //        private LocalDate dataFim;
        Assert.assertEquals(DateUtils.truncate(dataFim, Calendar.DATE), DateTimeConverters.toDate(t.getDataFim()));
        //        private Usuario usuarioInclusao;
        Assert.assertEquals(loggedUser, t.getUsuarioInclusao());
        //        private Usuario usuarioSolicitante;
        Assert.assertEquals(loggedUser, t.getUsuarioSolicitante());
        //        private Usuario usuarioResponsavel;
        Assert.assertEquals(usuarioResponsavel, t.getUsuarioResponsavel());
        //        private List<ParticipanteTarefa> participantes;
        Assert.assertEquals(usuarioParticipante_0, t.getParticipantes().get(0).getUsuarioParticipante());
        Assert.assertEquals(usuarioParticipante_1, t.getParticipantes().get(1).getUsuarioParticipante());
        //        private List<AvaliacaoMetaTarefa> avaliacoes;
        //        private List<OrcamentoTarefa> orcamentos;
        Assert.assertEquals(orcamentoTarefa_0.getCredito(), t.getOrcamentos().get(0).getCredito());
        Assert.assertEquals(orcamentoTarefa_0.getSaldo(), t.getOrcamentos().get(0).getSaldo());
        Assert.assertEquals(orcamentoTarefa_1.getCredito(), t.getOrcamentos().get(1).getCredito());
        Assert.assertEquals(orcamentoTarefa_1.getSaldo(), t.getOrcamentos().get(1).getSaldo());
//        //        private List<ApontamentoTarefa> apontamentos;
//        Assert.assertEquals(apontamento_0.getCreditoHoras(), t.getApontamentos().get(0).getCreditoHoras());
//        Assert.assertEquals(apontamento_0.getCreditoValor(), t.getApontamentos().get(0).getCreditoValor());
//        Assert.assertEquals(apontamento_0.getSaldoHoras(), t.getApontamentos().get(0).getSaldoHoras());
//        Assert.assertEquals(apontamento_1.getCreditoHoras(), t.getApontamentos().get(1).getCreditoHoras());
//        Assert.assertEquals(apontamento_1.getCreditoValor(), t.getApontamentos().get(1).getCreditoValor());
//        Assert.assertEquals(apontamento_1.getSaldoHoras(), t.getApontamentos().get(1).getSaldoHoras());
//        //        private List<AnexoTarefa> anexos;
//        //        private List<AndamentoTarefa> andamentos;
//        //        private List<BloqueioTarefa> bloqueios;
//        //        private List<HistoricoTarefa> historico;
//        //        private LocalDateTime dataHoraInclusao;
//        Assert.assertNotNull(t.getDataHoraInclusao());

    }

    @Test
    public void tarefaComHistorico() {

        System.out.println("Testando cadastro simples de tarefa");

        Usuario loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO.getAttributeName());

        HierarquiaProjetoDetalhe categoriaDefaultMeta = model.getCategoriaDefaultTarefa();
        presenter.createTask(categoriaDefaultMeta);

        String nome = "Teste Cadastro Tarefa Com Historico";
        view.getTaskNameTextField().setValue(nome);
        //view.getTipoRecorrenciaCombo().select(TipoTarefa.RECORRENTE);
        view.getRecurrencyButton().getCaption().equals("RECORRENTE");
        view.getPriorityCombo().setValue(PrioridadeTarefa.ALTA);
        view.getStartDateDateField().setValue(new Date());
        view.getCompanyCombo().setValue(loggedUser.getEmpresaAtiva());
        try {
            view.getTaskFieldGroup().commit();
        } catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        presenter.gravarButtonClicked();

        Tarefa t = (Tarefa) em.createNamedQuery("Tarefa.findByNome")
                .setParameter("nome", nome)
                .setParameter("empresa", loggedUser.getEmpresaAtiva())
                .getSingleResult();

        t.addHistorico(new HistoricoTarefa("teste", "comentario", loggedUser, t, LocalDateTime.now()));

        em.persist(t);

        t = (Tarefa) em.createNamedQuery("Tarefa.findByNome")
                .setParameter("nome", nome)
                .setParameter("empresa", loggedUser.getEmpresaAtiva())
                .getSingleResult();

        Assert.assertEquals(1, t.getHistorico().size());

        em.remove(t);

    }

}
