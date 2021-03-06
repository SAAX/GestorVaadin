/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb;

import com.saax.gestorweb.model.TarefaModel;
import com.saax.gestorweb.model.datamodel.ApontamentoTarefa;
import com.saax.gestorweb.model.datamodel.CentroCusto;
import com.saax.gestorweb.model.datamodel.Departamento;
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
import com.saax.gestorweb.util.DAOAleatorio;
import com.saax.gestorweb.util.DateTimeConverters;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.SessionAttributesEnum;
import com.saax.gestorweb.util.TestUtils;
import com.saax.gestorweb.view.TarefaView;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.UI;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
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
public class TarefaTest {

    TarefaView view;
    TarefaModel model;
    TarefaPresenter presenter;
    private static ResourceBundle mensagens = null;

    @BeforeClass
    public static void setUpClass() {

        try {

            TestUtils.connectDB();

            TestUtils.setUsuarioLogado(TestUtils.getUsuarioTeste());

            TestUtils.createGestorMDI();

            mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();

            // se assegura que nao existem tarefas ja cadastradas
            TestUtils.limpaBase();

        }
        catch (Exception ex) {
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

        view = new TarefaView();
        presenter = new TarefaPresenter(view);

    }

    /**
     * Testa o cadastro de uma tarefa simples, onde só os campos obrigatórios
     * são preenchidos
     */
    @Test
    public void cadastrarTarefaSimples() {

        System.out.println("Testando cadastro simples de tarefa");

        Usuario loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO);

        HierarquiaProjetoDetalhe categoriaDefaultMeta = model.getCategoriaDefaultTarefa();
        presenter.createTask(categoriaDefaultMeta, loggedUser.getEmpresas().get(0).getEmpresa());

        String nome = "Teste Cadastro Tarefa #1";
        view.getNomeTarefaTextField().setValue(nome);
        //view.getTipoRecorrenciaCombo().select(TipoTarefa.RECORRENTE);
        view.getControleRecorrenciaButton().getCaption().equals("RECORRENTE");
        view.getPrioridadeCombo().setValue(PrioridadeTarefa.ALTA);
        view.getDataInicioDateField().setValue(new Date());
        view.getEmpresaCombo().setValue(loggedUser.getEmpresas().get(0).getEmpresa());
        try {
            view.getTarefaFieldGroup().commit();
        }
        catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        view.getGravarButton().click();

        Tarefa t = (Tarefa) GestorEntityManagerProvider.getEntityManager().createNamedQuery("Tarefa.findByNome")
                .setParameter("nome", nome)
                .setParameter("empresa", loggedUser.getEmpresas().get(0).getEmpresa())
                .getSingleResult();

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

        Usuario loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO);

        HierarquiaProjetoDetalhe categoriaDefaultMeta = model.getCategoriaDefaultTarefa();
        presenter.createTask(categoriaDefaultMeta, loggedUser.getEmpresas().get(0).getEmpresa());

        String nome = "Teste Cadastro Tarefa com Anexo";
        view.getNomeTarefaTextField().setValue(nome);
        //view.getTipoRecorrenciaCombo().select(TipoTarefa.RECORRENTE);
        view.getControleRecorrenciaButton().getCaption().equals("RECORRENTE");
        view.getPrioridadeCombo().setValue(PrioridadeTarefa.ALTA);
        view.getDataInicioDateField().setValue(new Date());
        view.getEmpresaCombo().setValue(loggedUser.getEmpresas().get(0).getEmpresa());

        File anexoTeste = new File(System.getProperty("user.home") + System.getProperty("file.separator") + "anexoTeste.pdf");
        try {
            anexoTeste.createNewFile();
            view.getAdicionarAnexoUploadButton().submitUpload();
        }
        catch (IOException ex) {
            fail(ex.getMessage());
        }

        try {
            view.getTarefaFieldGroup().commit();
        }
        catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        view.getGravarButton().click();

        Tarefa t = (Tarefa) GestorEntityManagerProvider.getEntityManager().createNamedQuery("Tarefa.findByNome")
                .setParameter("nome", nome)
                .setParameter("empresa", loggedUser.getEmpresas().get(0).getEmpresa())
                .getSingleResult();

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

        Usuario loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO);
        Usuario usuarioResponsavel = (Usuario) GestorEntityManagerProvider.getEntityManager().createNamedQuery("Usuario.findByLogin").setParameter("login", "rodrigo.ccn2005@gmail.com").getSingleResult();

        HierarquiaProjetoDetalhe categoriaDefaultMeta = model.getCategoriaDefaultTarefa();
        presenter.createTask(categoriaDefaultMeta, loggedUser.getEmpresas().get(0).getEmpresa());

        String nome = "Teste Cadastro Tarefa #2";

        // ---------------------------------------------------------------------
        // Setando os campos
        //        private Integer id;
        //        private HierarquiaProjetoDetalhe hierarquia;
        HierarquiaProjeto hierarquiaProjetoDefault = (HierarquiaProjeto) GestorEntityManagerProvider.getEntityManager().createNamedQuery("HierarquiaProjeto.findByNome")
                .setParameter("nome", "Norma")
                .getSingleResult();

        for (HierarquiaProjetoDetalhe categoria : hierarquiaProjetoDefault.getCategorias()) {
            if (categoria.getNivel() == 2) {
                view.getHierarquiaCombo().setValue(categoria);
            }
        }
        //        private Empresa empresa;
        view.getEmpresaCombo().setValue(loggedUser.getEmpresas().get(0).getEmpresa());
        //        private String nome;
        view.getNomeTarefaTextField().setValue(nome);
        //        private PrioridadeTarefa prioridade;
        view.getPrioridadeCombo().setValue(PrioridadeTarefa.ALTA);
        //        private StatusTarefa status;
        //        private ProjecaoTarefa projecao;
        //        private int andamento;
        //        private String descricao;
        view.getDescricaoTextArea().setValue("Descrição da Tarefa #2");
        //        private boolean apontamentoHoras;
        view.getApontamentoHorasCheckBox().setValue(Boolean.TRUE);
        //        private boolean orcamentoControlado;
        view.getControleOrcamentoChechBox().setValue(Boolean.FALSE);
        //        private CentroCusto centroCusto;
        CentroCusto centroCusto = DAOAleatorio.getCentroCustoAleatorio(GestorEntityManagerProvider.getEntityManager());
        view.getCentroCustoCombo().setValue(centroCusto);
        //        private Departamento departamento;
        Departamento departamento = DAOAleatorio.getDepartamentoAleatorio(GestorEntityManagerProvider.getEntityManager(), loggedUser.getEmpresas().get(0).getEmpresa());
        view.getDepartamentoCombo().setValue(departamento);
        //        private FilialEmpresa filialEmpresa;
        //        private EmpresaCliente empresaCliente;
        EmpresaCliente empresaCliente = DAOAleatorio.getEmpresaClienteAleatoria(GestorEntityManagerProvider.getEntityManager(), loggedUser.getEmpresas().get(0).getEmpresa());
        view.getEmpresaClienteCombo().setValue(empresaCliente);
        //        private List<Tarefa> subTarefas;
        //        private Tarefa proximaTarefa;
        //        private TipoTarefa tipoRecorrencia;
        view.getControleRecorrenciaButton().getCaption().equals("RECORRENTE");
        //view.getTipoRecorrenciaCombo().select(TipoTarefa.RECORRENTE);
        //        private Tarefa tarefaPai;
        //        private LocalDate dataInicio;
        Date dataInicio = DAOAleatorio.getDataByOffset(20, true); // hoje + 20 dias
        view.getDataInicioDateField().setValue(dataInicio);
        //        private LocalDate dataTermino;
        //        private LocalDate dataFim;
        Date dataFim = DAOAleatorio.getDataByOffset(30, true); // hoje + 30 dias
        view.getDataFimDateField().setValue(dataFim);
        //        private Usuario usuarioInclusao;
        //        private Usuario usuarioSolicitante;
        //        private Usuario usuarioResponsavel;
        view.getUsuarioResponsavelCombo().setValue(usuarioResponsavel);
        //        private List<ParticipanteTarefa> participantes;
        Usuario usuarioParticipante_0 = (Usuario) GestorEntityManagerProvider.getEntityManager().createNamedQuery("Usuario.findByLogin").setParameter("login", "fernando.saax@gmail.com").getSingleResult();
        view.getParticipantesCombo().setValue(usuarioParticipante_0);
        Usuario usuarioParticipante_1 = (Usuario) GestorEntityManagerProvider.getEntityManager().createNamedQuery("Usuario.findByLogin").setParameter("login", "danielstavale@gmail.com").getSingleResult();
        view.getParticipantesCombo().setValue(usuarioParticipante_1);
        //        private List<AvaliacaoMetaTarefa> avaliacoes;
        //        private List<OrcamentoTarefa> orcamentos;
        view.getValorOrcadoRealizadoTextField().setValue("123.34");
        view.getObservacaoOrcamentoTextField().setValue("v0");
        OrcamentoTarefa orcamentoTarefa_0 = new OrcamentoTarefa();
        try {
            orcamentoTarefa_0 = view.getOrcamentoTarefa();
        }
        catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        view.getImputarOrcamentoButton().click();

        view.getValorOrcadoRealizadoTextField().setValue("254.67");
        view.getObservacaoOrcamentoTextField().setValue("v1");
        OrcamentoTarefa orcamentoTarefa_1 = new OrcamentoTarefa();
        try {
            orcamentoTarefa_1 = view.getOrcamentoTarefa();
        }
        catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        view.getImputarOrcamentoButton().click();
        //        private List<ApontamentoTarefa> apontamentos;
        view.getHorasApontadasTextField().setValue("135:00");
        view.getCustoHoraApontamentoTextField().setValue("14.36"); // 1938.6
        ApontamentoTarefa apontamento_0 = new ApontamentoTarefa();
        try {
            apontamento_0 = view.getApontamentoTarefa();
        }
        catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        view.getAdicionarApontamentoButton().click();

        view.getHorasApontadasTextField().setValue("214:30"); // 3080.22
        ApontamentoTarefa apontamento_1 = new ApontamentoTarefa();
        try {
            apontamento_1 = view.getApontamentoTarefa();
        }
        catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        view.getAdicionarApontamentoButton().click();
        //        private List<AnexoTarefa> anexos;
        //        private List<AndamentoTarefa> andamentos;
        //        private List<BloqueioTarefa> bloqueios;
        //        private List<HistoricoTarefa> historico;
        //        private LocalDateTime dataHoraInclusao;

        try {
            view.getTarefaFieldGroup().commit();
        }
        catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        view.getGravarButton().click();

        Tarefa t = (Tarefa) GestorEntityManagerProvider.getEntityManager().createNamedQuery("Tarefa.findByNome")
                .setParameter("nome", nome)
                .setParameter("empresa", loggedUser.getEmpresas().get(0).getEmpresa())
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
        Assert.assertEquals(loggedUser.getEmpresas().get(0).getEmpresa(), t.getEmpresa());
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
/*
    @Test
    public void cadastrarMultiplasSubTarefas() {

        System.out.println("Testando cadastro multiplas sub tarefas");

        Usuario loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO);

        // -------------------------------------------------------------------------------------
        // Tarefa:  Teste Multiplos Niveis
        // -------------------------------------------------------------------------------------
        HierarquiaProjetoDetalhe categoriaDefaultTarefa = null;
        HierarquiaProjeto hierarquiaProjetoDefault = (HierarquiaProjeto) GestorEntityManagerProvider.getEntityManager().createNamedQuery("HierarquiaProjeto.findByNome")
                .setParameter("nome", "Norma c/ Tarefa")
                .getSingleResult();

        for (HierarquiaProjetoDetalhe categoria : hierarquiaProjetoDefault.getCategorias()) {
            if (categoria.getNivel() == 2) {
                categoriaDefaultTarefa = categoria;
            }
        }

        presenter.createTask(categoriaDefaultTarefa);

        String nome_principal = "Teste Multiplos Niveis";
        view.getNomeTarefaTextField().setValue(nome_principal);
        //view.getTipoRecorrenciaCombo().select(TipoTarefa.RECORRENTE);
        view.getControleRecorrenciaButton().getCaption().equals("RECORRENTE");
        view.getPrioridadeCombo().setValue(PrioridadeTarefa.ALTA);
        view.getDataInicioDateField().setValue(new Date());
        view.getEmpresaCombo().setValue(loggedUser.getEmpresas().get(0).getEmpresa());

        // -------------------------------------------------------------------------------------
        // Tarefa:  Teste Multiplos Niveis -> Sub 1
        // -------------------------------------------------------------------------------------
        try {
            view.getTarefaFieldGroup().commit();
        }
        catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        view.getGravarButton().click();

        Tarefa tarefa_principal = (Tarefa) GestorEntityManagerProvider.getEntityManager().createNamedQuery("Tarefa.findByNome")
                .setParameter("nome", nome_principal)
                .setParameter("empresa", loggedUser.getEmpresas().get(0).getEmpresa())
                .getSingleResult();

        TarefaView view_sub1 = new TarefaView();
        TarefaPresenter presenter_sub1 = new TarefaPresenter(view_sub1);

        presenter_sub1.addCallBackListener(presenter);

        presenter_sub1.criarNovaSubTarefa(tarefa_principal);

        view_sub1 = presenter_sub1.getView();
        String nome_sub1 = "Sub 1";
        view_sub1.getNomeTarefaTextField().setValue(nome_sub1);
        //view_sub1.getTipoRecorrenciaCombo().select(TipoTarefa.RECORRENTE);
        view_sub1.getControleRecorrenciaButton().getCaption().equals("RECORRENTE");
        view_sub1.getPrioridadeCombo().setValue(PrioridadeTarefa.ALTA);
        view_sub1.getDataInicioDateField().setValue(new Date());
        view_sub1.getEmpresaCombo().setValue(loggedUser.getEmpresas().get(0).getEmpresa());
        view_sub1.getHierarquiaCombo().setValue(view_sub1.getHierarquiaCombo().getItemIds().toArray()[0]);

        // -------------------------------------------------------------------------------------
        // Tarefa:  Teste Multiplos Niveis -> Sub 1 -> Sub 2
        // -------------------------------------------------------------------------------------
        try {
            view_sub1.getTarefaFieldGroup().commit();
        }
        catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());

        }

        TarefaView view_sub2 = new TarefaView();
        TarefaPresenter presenter_sub2 = new TarefaPresenter(view_sub2);
        presenter_sub2.addCallBackListener(presenter_sub1);
        List<HierarquiaProjetoDetalhe> proximasCategorias = TarefaModel.getProximasCategorias(tarefa);
        presenter_sub1.criarNovaSubTarefa(proximasCategorias, tarefa);

        view_sub1.getAdicionarSubtarefaButton().click();

        String nome_sub2 = "Sub 2";
        view_sub2.getNomeTarefaTextField().setValue(nome_sub2);
        //view_sub2.getTipoRecorrenciaCombo().select(TipoTarefa.RECORRENTE);
        view_sub2.getControleRecorrenciaButton().getCaption().equals("RECORRENTE");
        view_sub2.getPrioridadeCombo().setValue(PrioridadeTarefa.ALTA);
        view_sub2.getDataInicioDateField().setValue(new Date());
        view_sub2.getEmpresaCombo().select(loggedUser.getEmpresas().get(0).getEmpresa());
        view_sub2.getHierarquiaCombo().setValue(view_sub2.getHierarquiaCombo().getItemIds().toArray()[0]);

        // Grava a sub nivel 2
        try {
            view_sub2.getTarefaFieldGroup().commit();
        }
        catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        view_sub2.getGravarButton().click();

        // Grava a sub nivel 1
        try {
            view_sub1.getTarefaFieldGroup().commit();
        }
        catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        view_sub1.getGravarButton().click();

        // Grava a principal
        try {
            view.getTarefaFieldGroup().commit();
        }
        catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        view.getGravarButton().click();

        Tarefa t = (Tarefa) GestorEntityManagerProvider.getEntityManager().createNamedQuery("Tarefa.findByNome")
                .setParameter("nome", nome_principal)
                .setParameter("empresa", loggedUser.getEmpresas().get(0).getEmpresa())
                .getSingleResult();

        Tarefa sub1 = t.getSubTarefas().get(0);
        Tarefa sub2 = sub1.getSubTarefas().get(0);

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
*/
    /**
     * Testa a edição da tarefa completa
     *
     */
    @Test
    public void editarTarefaCompleta() {

        System.out.println("Testando a edição (alteração) da tarefa completa");

        Usuario loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO);
        Usuario usuarioResponsavel = (Usuario) GestorEntityManagerProvider.getEntityManager().createNamedQuery("Usuario.findByLogin").setParameter("login", "danielstavale@gmail.com").getSingleResult();

        String nome = "editarTarefaCompleta";
        TestUtils.cadastrarTarefaSimples(nome);

        String novonome = "Teste Cadastro Tarefa #2 - Alterada";

        Tarefa t = (Tarefa) GestorEntityManagerProvider.getEntityManager().createNamedQuery("Tarefa.findByNome")
                .setParameter("nome", nome)
                .setParameter("empresa", loggedUser.getEmpresas().get(0).getEmpresa())
                .getSingleResult();

        Assert.assertNotNull(t);
        presenter.editar(t);

        // ---------------------------------------------------------------------
        // Alterando os campos
        //        private String nome;
        view.getNomeTarefaTextField().setValue(novonome);
        view.getNomeTarefaTextField().commit();
        //        private PrioridadeTarefa prioridade;
        view.getPrioridadeCombo().setValue(PrioridadeTarefa.BAIXA);
        //        private StatusTarefa status;
        view.getStatusTarefaPopUpButton().click();
        //        private ProjecaoTarefa projecao;
        //        private int andamento;
        //        private String descricao;
        view.getDescricaoTextArea().setValue("Descrição da Tarefa #2 - Alterada");
        //        private boolean apontamentoHoras;
        view.getApontamentoHorasCheckBox().setValue(Boolean.FALSE);
        //        private boolean orcamentoControlado;
        view.getControleOrcamentoChechBox().setValue(Boolean.TRUE);
        //        private CentroCusto centroCusto;
        CentroCusto centroCusto = DAOAleatorio.getCentroCustoAleatorio(GestorEntityManagerProvider.getEntityManager());
        view.getCentroCustoCombo().setValue(centroCusto);
        //        private Departamento departamento;
        Departamento departamento = DAOAleatorio.getDepartamentoAleatorio(GestorEntityManagerProvider.getEntityManager(), loggedUser.getEmpresas().get(0).getEmpresa());
        view.getDepartamentoCombo().setValue(departamento);
        //        private Empresa empresa;
        view.getEmpresaCombo().setValue(loggedUser.getEmpresas().get(0).getEmpresa());
        //        private FilialEmpresa filialEmpresa;
        //        private EmpresaCliente empresaCliente;
        EmpresaCliente empresaCliente = DAOAleatorio.getEmpresaClienteAleatoria(GestorEntityManagerProvider.getEntityManager(), loggedUser.getEmpresas().get(0).getEmpresa());
        view.getEmpresaClienteCombo().setValue(empresaCliente);
        //        private List<Tarefa> subTarefas;
        //        private Tarefa proximaTarefa;
        //        private TipoTarefa tipoRecorrencia;
        //view.getTipoRecorrenciaCombo().select(TipoTarefa.UNICA);
        view.getControleRecorrenciaButton().getCaption().equals("ÚNICA");
        //        private Tarefa tarefaPai;
        //        private LocalDate dataInicio;
        Date dataInicio = DAOAleatorio.getDataByOffset(10, true); // hoje + 10 dias
        view.getDataInicioDateField().setValue(dataInicio);
        //        private LocalDate dataTermino;
        //        private LocalDate dataFim;
        Date dataFim = DAOAleatorio.getDataByOffset(35, true); // hoje + 35 dias
        view.getDataFimDateField().setValue(dataFim);
        //        private Usuario usuarioInclusao;
        //        private Usuario usuarioSolicitante;
        //        private Usuario usuarioResponsavel;
        view.getUsuarioResponsavelCombo().setValue(usuarioResponsavel);

        try {
            view.getTarefaFieldGroup().commit();
        }
        catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        view.getGravarButton().click();

        // obtem novamente a tarefa do banco
        t = (Tarefa) GestorEntityManagerProvider.getEntityManager().createNamedQuery("Tarefa.findByNome")
                .setParameter("nome", novonome)
                .setParameter("empresa", loggedUser.getEmpresas().get(0).getEmpresa())
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
        Assert.assertEquals(PrioridadeTarefa.BAIXA, t.getPrioridade());
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
        Assert.assertEquals(loggedUser.getEmpresas().get(0).getEmpresa(), t.getEmpresa());
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

    }

    /**
     * Cadastra uma tarefa simples e adiciona um histórico
     */
    @Test
    public void tarefaComHistorico() {

        System.out.println("Testando cadastro simples de tarefa");

        Usuario loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO);

        HierarquiaProjetoDetalhe categoriaDefaultMeta = model.getCategoriaDefaultTarefa();
        presenter.createTask(categoriaDefaultMeta, loggedUser.getEmpresas().get(0).getEmpresa());

        String nome = "Teste Cadastro Tarefa Com Historico";
        view.getNomeTarefaTextField().setValue(nome);
        //view.getTipoRecorrenciaCombo().select(TipoTarefa.RECORRENTE);
        view.getControleRecorrenciaButton().getCaption().equals("RECORRENTE");
        view.getPrioridadeCombo().setValue(PrioridadeTarefa.ALTA);
        view.getDataInicioDateField().setValue(new Date());
        view.getEmpresaCombo().setValue(loggedUser.getEmpresas().get(0).getEmpresa());
        try {
            view.getTarefaFieldGroup().commit();
        }
        catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        view.getGravarButton().click();

        Tarefa t = (Tarefa) GestorEntityManagerProvider.getEntityManager().createNamedQuery("Tarefa.findByNome")
                .setParameter("nome", nome)
                .setParameter("empresa", loggedUser.getEmpresas().get(0).getEmpresa())
                .getSingleResult();

        t.addHistorico(new HistoricoTarefa("teste", "comentario", loggedUser, t, LocalDateTime.now()));

        GestorEntityManagerProvider.getEntityManager().persist(t);

        t = (Tarefa) GestorEntityManagerProvider.getEntityManager().createNamedQuery("Tarefa.findByNome")
                .setParameter("nome", nome)
                .setParameter("empresa", loggedUser.getEmpresas().get(0).getEmpresa())
                .getSingleResult();

        Assert.assertEquals(1, t.getHistorico().size());

        GestorEntityManagerProvider.getEntityManager().remove(t);

    }

}
