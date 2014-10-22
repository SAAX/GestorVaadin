/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb;

import com.saax.gestorweb.model.CadastroTarefaModel;
import com.saax.gestorweb.model.LoginModel;
import com.saax.gestorweb.model.datamodel.ApontamentoTarefa;
import com.saax.gestorweb.model.datamodel.CentroCusto;
import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.PrioridadeTarefa;
import com.saax.gestorweb.model.datamodel.StatusTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.TipoTarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.presenter.CadastroTarefaPresenter;
import com.saax.gestorweb.util.DBConnect;
import com.saax.gestorweb.util.DAOAleatorio;
import com.saax.gestorweb.util.DateTimeConverters;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.PostgresConnection;
import com.saax.gestorweb.view.CadastroTarefaView;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.UI;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.time.DateUtils;
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
public class CadastroTarefaTest {

    CadastroTarefaView view;
    CadastroTarefaModel model;
    CadastroTarefaPresenter presenter;
    private EntityManager em;
    private static List<Tarefa> tarefasCadastradas;

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

        
        tarefasCadastradas = new ArrayList<>();

    }

    @AfterClass
    public static void tearDownClass() {

        // limpar tarefas cadastradas
        List<Tarefa> tarefas = tarefasCadastradas;
        GestorEntityManagerProvider.getEntityManager().getTransaction().begin();
        for (Tarefa tarefa : tarefas) {
            GestorEntityManagerProvider.getEntityManager().remove(tarefa);
        }
        GestorEntityManagerProvider.getEntityManager().getTransaction().commit();

        // disconnect
        GestorEntityManagerProvider.getEntityManager().close();

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

    /**
     * Testa o cadastro de uma tarefa simples, onde só os campos obrigatórios são preenchidos
     */
    @Test
    public void cadastrarTarefaSimples() {

        System.out.println("Testando cadastro simples de tarefa");

        Usuario usuarioLogado = (Usuario) GestorSession.getAttribute("usuarioLogado");

        // se assegura que nao existem tarefas ja cadastradas
        List<Tarefa> tarefas = em.createNamedQuery("Tarefa.findAll").setParameter("empresa", usuarioLogado.getEmpresaAtiva()).getResultList();
        Assert.assertArrayEquals(new ArrayList().toArray(), tarefas.toArray());

        presenter.criarNovaTarefa();

        String nome = "Teste Cadastro Tarefa #1";
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

        tarefasCadastradas.add(t);
        
        Assert.assertEquals(nome,t.getNome());
        
        Assert.assertEquals(StatusTarefa.NAO_ACEITA, t.getStatus());
        
        Assert.assertNotNull(t.getDataHoraInclusao());

    }

    /**
     * Testa o cadastro completo de uma tarefa, com todos os campos disponiveis na 
     */
    @Test
    public void cadastrarTarefaCompleta() {

        System.out.println("Testando cadastro completo de uma tarefa");
        
        Usuario usuarioLogado = (Usuario) GestorSession.getAttribute("usuarioLogado");
        Usuario usuarioResponsavel = (Usuario) em.createNamedQuery("Usuario.findByLogin").setParameter("login", "rodrigo.ccn2005@gmail.com").getSingleResult();

        presenter.criarNovaTarefa();

        String nome = "Teste Cadastro Tarefa #2";
           
        // ---------------------------------------------------------------------
        // Setando os campos
        //        private Integer id;
        //        private int nivel;
        //        private String titulo;
        //        private String nome;
        view.getNomeTarefaTextField().setValue(nome);
        //        private PrioridadeTarefa prioridade;
        view.getPrioridadeCombo().setValue(PrioridadeTarefa.ALTA);
        //        private StatusTarefa status;
        //        private ProjecaoTarefa projecao;
        //        private int andamento;
        //        private String descricao;
        view.getDescricaoTarefaTextArea().setValue("Descrição da Tarefa #2");
        //        private boolean apontamentoHoras;
        view.getApontamentoHorasCheckBox().setValue(Boolean.TRUE);
        //        private boolean orcamentoControlado;
        view.getOrcamentoControladoCheckBox().setValue(Boolean.FALSE);
        //        private CentroCusto centroCusto;
        CentroCusto centroCusto = DAOAleatorio.getCentroCustoAleatorio(em);
        view.getCentroCustoCombo().setValue(centroCusto);
        //        private Departamento departamento;
        Departamento departamento = DAOAleatorio.getDepartamentoAleatorio(em, usuarioLogado.getEmpresaAtiva());
        view.getDepartamentoCombo().setValue(departamento);
        //        private Empresa empresa;
        view.getEmpresaCombo().setValue(usuarioLogado.getEmpresaAtiva().getSubEmpresas().get(0));
        //        private FilialEmpresa filialEmpresa;
        //        private EmpresaCliente empresaCliente;
        EmpresaCliente empresaCliente = DAOAleatorio.getEmpresaClienteAleatoria(em, usuarioLogado.getEmpresaAtiva());
        view.getEmpresaClienteCombo().setValue(empresaCliente);
        //        private List<Tarefa> subTarefas;
        //        private Tarefa proximaTarefa;
        //        private TipoTarefa tipoRecorrencia;
        view.getTipoRecorrenciaCombo().select(TipoTarefa.RECORRENTE);
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
        Usuario usuarioParticipante_0 = (Usuario) em.createNamedQuery("Usuario.findByLogin").setParameter("login", "fernando.saax@gmail.com").getSingleResult();
        view.getParticipantesCombo().setValue(usuarioParticipante_0);
        Usuario usuarioParticipante_1 = (Usuario) em.createNamedQuery("Usuario.findByLogin").setParameter("login", "danielstavale@gmail.com").getSingleResult();
        view.getParticipantesCombo().setValue(usuarioParticipante_1);
        //        private List<AvaliacaoMetaTarefa> avaliacoes;
        //        private List<OrcamentoTarefa> orcamentos;
        //        private List<ApontamentoTarefa> apontamentos;
        view.getImputarHorasTextField().setValue("135:00");
        ApontamentoTarefa apontamento_0 = new ApontamentoTarefa();
        try {
            apontamento_0 = view.getApontamentoTarefa();
        } catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        view.getImputarHorasButton().click();
        
        view.getImputarHorasTextField().setValue("214:30");
        ApontamentoTarefa apontamento_1 = new ApontamentoTarefa();
        try {
            apontamento_1 = view.getApontamentoTarefa();
        } catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        view.getImputarHorasButton().click();
        //        private List<AnexoTarefa> anexos;
        //        private List<AndamentoTarefa> andamentos;
        //        private List<BloqueioTarefa> bloqueios;
        //        private List<HistoricoTarefa> historico;
        //        private LocalDateTime dataHoraInclusao;

        view.getGravarButton().click();

        Tarefa t = (Tarefa) em.createNamedQuery("Tarefa.findByNome")
                .setParameter("nome", nome)
                .setParameter("empresa", usuarioLogado.getEmpresaAtiva().getSubEmpresas().get(0))
                .getSingleResult();

        tarefasCadastradas.add(t);
        
        // ---------------------------------------------------------------------
        // Conferindo resultado
        // ---------------------------------------------------------------------
        
        //        private Integer id;
        //        private int nivel;
        Assert.assertEquals(1, t.getNivel());
        //        private String titulo;
        Assert.assertEquals("Tarefa", t.getTitulo());
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
        Assert.assertEquals(usuarioLogado.getEmpresaAtiva().getSubEmpresas().get(0), t.getEmpresa());
        //        private FilialEmpresa filialEmpresa;
        //        private EmpresaCliente empresaCliente;
        Assert.assertEquals(empresaCliente, t.getEmpresaCliente());
        //        private List<Tarefa> subTarefas;
        //        private Tarefa proximaTarefa;
        //        private TipoTarefa tipoRecorrencia;
        Assert.assertEquals(TipoTarefa.RECORRENTE, t.getTipoRecorrencia());
        //        private Tarefa tarefaPai;
        Assert.assertNull(t.getTarefaPai());
        //        private LocalDate dataTermino;
        //        private LocalDate dataInicio;
        Assert.assertEquals(DateUtils.truncate(dataInicio, Calendar.DATE), DateTimeConverters.toDate(t.getDataInicio()));
        //        private LocalDate dataFim;
        Assert.assertEquals(DateUtils.truncate(dataFim, Calendar.DATE), DateTimeConverters.toDate(t.getDataFim()));
        //        private Usuario usuarioInclusao;
        Assert.assertEquals(usuarioLogado, t.getUsuarioInclusao());
        //        private Usuario usuarioSolicitante;
        Assert.assertEquals(usuarioLogado, t.getUsuarioSolicitante());
        //        private Usuario usuarioResponsavel;
        Assert.assertEquals(usuarioResponsavel, t.getUsuarioResponsavel());
        //        private List<ParticipanteTarefa> participantes;
        Assert.assertEquals(usuarioParticipante_0, t.getParticipantes().get(0).getUsuarioParticipante());
        Assert.assertEquals(usuarioParticipante_1, t.getParticipantes().get(1).getUsuarioParticipante());
        //        private List<AvaliacaoMetaTarefa> avaliacoes;
        //        private List<OrcamentoTarefa> orcamentos;
        //        private List<ApontamentoTarefa> apontamentos;
        Assert.assertEquals(apontamento_0.getCreditoHoras(), t.getApontamentos().get(0).getCreditoHoras());
        Assert.assertEquals(apontamento_0.getSaldoHoras(), t.getApontamentos().get(0).getSaldoHoras());
        Assert.assertEquals(apontamento_1.getCreditoHoras(), t.getApontamentos().get(1).getCreditoHoras());
        Assert.assertEquals(apontamento_1.getSaldoHoras(), t.getApontamentos().get(1).getSaldoHoras());
        //        private List<AnexoTarefa> anexos;
        //        private List<AndamentoTarefa> andamentos;
        //        private List<BloqueioTarefa> bloqueios;
        //        private List<HistoricoTarefa> historico;
        //        private LocalDateTime dataHoraInclusao;
        Assert.assertNotNull(t.getDataHoraInclusao());
        

    }

    @Test
    public void editarTarefa() {

        LocalTime lt = LocalTime.now();
        
        
        System.out.println(lt.getHour());
        lt.plus(25L, ChronoUnit.HOURS);
        
        System.out.println(lt.getHour());
    }

}
