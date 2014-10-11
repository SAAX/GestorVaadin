package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.datamodel.ApontamentoTarefa;
import com.saax.gestorweb.model.datamodel.ParticipanteTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.view.converter.DateToLocalDateConverter;
import com.saax.gestorweb.view.validator.DataFuturaValidator;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.hene.popupbutton.PopupButton;

/**
 * Pop-up Window do cadastro de tarefas
 * <p>
 * A visualização será em uma estrutura de accordion com tres abas:
 * <br>
 * <ol>
 * <li>Dados inicias da tarefa</li>
 * <li>Descrição e responsáveis</li>
 * <li>Controle de horas (opcional) </li>
 * <li>Controle de orçamento (opcional) </li>
 * <li>Subtarefas </li>
 * </ol>
 *
 *
 * @author rodrigo
 */
public class CadastroTarefaView extends Window {

    // Referencia ao recurso das mensagens:
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();

    // A view mantem acesso ao listener (Presenter) para notificar os eventos
    // Este acesso se dá por uma interface para manter a abstração das camadas
    private CadastroTarefaViewListener listener;

    // lista com todos os campos que possuem validação
    private final List<AbstractField> camposObrigatorios;
    
    private Accordion accordion;
    
    // -------------------------------------------------------------------------
    // Componentes do cabeçalho
    // -------------------------------------------------------------------------
    
    @PropertyId("apontamentoHoras")
    private CheckBox apontamentoHorasCheckBox;
    
    @PropertyId("orcamentoControlado")
    private CheckBox orcamentoControladoCheckBox;

    private HorizontalLayout barraBotoesSuperior;
    private Button addSubButton;
    private Button chatButton;
    private Button projecaoButton;


    // -------------------------------------------------------------------------
    // Componentes da Aba de dados basicos
    // -------------------------------------------------------------------------
    
    private BeanItem<Tarefa> tarefaBeanItem;
    private FieldGroup tarefaFieldGroup;

    @PropertyId("empresa")
    private ComboBox empresaCombo;

    @PropertyId("nome")
    private TextField nomeTarefaTextField;

    @PropertyId("dataInicio")
    private PopupDateField dataInicioDateField;

    @PropertyId("dataFim")
    private PopupDateField dataFimDateField;

    @PropertyId("tipoRecorrencia")
    private ComboBox tipoRecorrenciaCombo;

    @PropertyId("prioridade")
    private ComboBox prioridadeCombo;

    private Button avisoButton;
    
    private PopupButton statusTarefaPopUpButton;

    // -------------------------------------------------------------------------
    // Componentes da Aba de Descrição e Responsável
    // -------------------------------------------------------------------------
    
    @PropertyId("usuarioResponsavel")
    private ComboBox usuarioResponsavelCombo;

    @PropertyId("descricao")
    private RichTextArea descricaoTarefaTextArea;

    private ComboBox participantesCombo;
    
    private BeanItemContainer<ParticipanteTarefa> participantesContainer;
    
    @PropertyId("empresaCliente")
    private ComboBox empresaClienteCombo;

    // -------------------------------------------------------------------------
    // Componentes da Aba Detalhes
    // -------------------------------------------------------------------------
    
    @PropertyId("departamento")
    private ComboBox departamentoCombo;
    
    @PropertyId("centroCusto")
    private ComboBox centroCustoCombo;
    
    private Upload adicionarAnexoButton;

    
    // -------------------------------------------------------------------------
    // Componentes da Aba Controle de Horas
    // -------------------------------------------------------------------------

    private VerticalLayout controleHorasAba;
    
    private TextField custoHoraTextField;
    private TextField imputarHorasTextField;
    private TextField observacaoHorasTextField;
    private Button imputarHorasButton;
    private BeanItem<ApontamentoTarefa> apontamentoTarefaBeanItem;
    private BeanItemContainer<ApontamentoTarefa> controleHorasContainer;
    private FieldGroup apontamentoTarefaFieldGroup;
    
    
    // -------------------------------------------------------------------------
    // Componentes da Aba de orçamento
    // -------------------------------------------------------------------------

    private VerticalLayout controleOrcamentoAba;
    private TextField imputarOrcamentoTextField;
    private TextField observacaoOrcamentoTextField;
    private Button imputarOrcamentoButton;
    private Button gravarButton;
    private Button cancelarButton;
    
    // -------------------------------------------------------------------------
    // Componentes da Aba de SubTarefa
    // -------------------------------------------------------------------------

    private TreeTable subTarefasTable;

    
    
    
    
    /**
     * Configura o listener de eventos da view
     *
     * @param listener
     */
    public void setListener(CadastroTarefaViewListener listener) {
        this.listener = listener;
    }

    /**
     * Cria a view e todos os componentes
     *
     */
    public CadastroTarefaView() {
        super();

        camposObrigatorios = new ArrayList();

        setCaption(mensagens.getString("CadastroTarefaView.titulo"));
        setModal(true);
        setWidth(1000, Unit.PIXELS);
        setHeight(800, Unit.PIXELS);

        // Container principal, que armazenará todos os demais containeres 
        VerticalLayout containerPrincipal = buildContainerPrincipal();
        containerPrincipal.setSpacing(true);
        setContent(containerPrincipal);

        center();

        setValidatorsVisible(false);

    }

    /**
     * Bind (liga) a tarefa ao formulário
     *
     * @param tarefa
     */
    public void setTarefa(Tarefa tarefa) {

        tarefaBeanItem = new BeanItem<>(tarefa);
        tarefaFieldGroup = new FieldGroup(tarefaBeanItem);

        tarefaFieldGroup.bindMemberFields(this);

    }

    /**
     * Obtem a tarefa ligada (binding) ao form
     *
     * @return
     */
    public Tarefa getTarefa() {
        return tarefaBeanItem.getBean();
    }

    /**
     * Constroi o container principal que armazenará todos os demais
     *
     * @return
     */
    private VerticalLayout buildContainerPrincipal() {

        VerticalLayout containerPrincipal = new VerticalLayout();
        containerPrincipal.setMargin(true);
        containerPrincipal.setSpacing(true);
        containerPrincipal.setSizeFull();

        // adiciona a barra de botoes superior (botões de chat, adicionar sub, etc)
        containerPrincipal.addComponent(buildBarraBotoesSuperior());
        containerPrincipal.setComponentAlignment(barraBotoesSuperior, Alignment.MIDDLE_RIGHT);

        // cria o acordeon de abas e adiciona as abas
        accordion = new Accordion();
        accordion.setWidth("100%");
        // adiciona a aba de dados iniciais
        accordion.addTab(buildAbaDadosIniciais(), mensagens.getString("CadastroTarefaView.AbaDadosIniciais.titulo"), null);
        // adiciona a aba de descrição e responsáveis
        accordion.addTab(buildAbaDescricaoEResponsaveis(), mensagens.getString("CadastroTarefaView.AbaDescricaoEResponsaveis.titulo"), null);
        // adiciona a aba de detalhes da tarefa
        accordion.addTab(buildAbaDetalhes(), mensagens.getString("CadastroTarefaView.AbaDetalhes.titulo"), null);
        // adiciona a aba opcional de controle de horas
        accordion.addTab(buildAbaControleHoras(), mensagens.getString("CadastroTarefaView.AbaControleHoras.titulo"), null);
        // adiciona a aba opcional de controle de orçamento
        accordion.addTab(buildAbaOrcamento(), mensagens.getString("CadastroTarefaView.AbaOrcamento.titulo"), null);
        // adiciona a aba sub tarefas
        accordion.addTab(buildAbaSubTarefas(), mensagens.getString("CadastroTarefaView.AbaSubTarefas.titulo"), null);

        containerPrincipal.addComponent(accordion);
        containerPrincipal.setExpandRatio(accordion, 1);

        // adiciona a barra de botoes inferior, com os botões de salvar e cancelas
        Component barraInferior = buildBarraBotoesInferior();
        containerPrincipal.addComponent(barraInferior);
        containerPrincipal.setComponentAlignment(barraInferior, Alignment.MIDDLE_CENTER);

        return containerPrincipal;

    }

    /**
     * Ocultar ou exibir validações
     *
     * @param visible
     */
    public void setValidatorsVisible(boolean visible) {
        camposObrigatorios.stream().forEach((campo) -> {
            campo.setValidationVisible(visible);
        });
    }

    /**
     * Constroi a aba de dados inicias:
     *
     * @return
     */
    private Component buildAbaDadosIniciais() {

        // Combo: Empresa
        empresaCombo = new ComboBox(mensagens.getString("CadastroTarefaView.empresaCombo.label"));
        empresaCombo.setWidth("100%");
        empresaCombo.addValidator(new BeanValidator(Tarefa.class, "empresa"));
        camposObrigatorios.add(empresaCombo);

        // TextField: Nome da Tarefa
        nomeTarefaTextField = new TextField(mensagens.getString("CadastroTarefaView.nomeTarefaTextField.label"));
        nomeTarefaTextField.setWidth("100%");
        nomeTarefaTextField.setInputPrompt(mensagens.getString("CadastroTarefaView.nomeTarefaTextField.inputPrompt"));
        nomeTarefaTextField.setNullRepresentation("");
        nomeTarefaTextField.addValidator(new BeanValidator(Tarefa.class, "nome"));
        camposObrigatorios.add(nomeTarefaTextField);

        // TextField: Data de Inicio 
        dataInicioDateField = new PopupDateField(mensagens.getString("CadastroTarefaView.dataInicioTextField.label"));
        dataInicioDateField.setWidth("100%");
        dataInicioDateField.setConverter(new DateToLocalDateConverter());
        dataInicioDateField.addValidator(new BeanValidator(Tarefa.class, "dataInicio"));
        dataInicioDateField.addValidator(new DataFuturaValidator(true, "Data de Início"));
        camposObrigatorios.add(dataInicioDateField);

        // Combo Recorrencia
        tipoRecorrenciaCombo = new ComboBox(mensagens.getString("CadastroTarefaView.tipoRecorrenciaCombo.label"));
        tipoRecorrenciaCombo.setWidth("100%");
        tipoRecorrenciaCombo.addValidator(new BeanValidator(Tarefa.class, "tipoRecorrencia"));
        camposObrigatorios.add(tipoRecorrenciaCombo);

        // Combo Prioridade
        prioridadeCombo = new ComboBox(mensagens.getString("CadastroTarefaView.prioridadeCombo.label"));
        prioridadeCombo.setWidth("100%");
        prioridadeCombo.addValidator(new BeanValidator(Tarefa.class, "prioridade"));
        camposObrigatorios.add(prioridadeCombo);

        // Pop-up de status
        statusTarefaPopUpButton = new PopupButton();
        statusTarefaPopUpButton.setWidth("100%");

        // TextField: Data Fim
        dataFimDateField = new PopupDateField(mensagens.getString("CadastroTarefaView.dataFimTextField.label"));
        dataFimDateField.setWidth("100%");

        // Componente de aviso
        avisoButton = new Button(mensagens.getString("CadastroTarefaView.avisoButton.caption"), (Button.ClickEvent event) -> {
            listener.avisoButtonClicked();
        });
        avisoButton.setWidth("100%");

        // configura o layout usando uma grid
        GridLayout grid = new GridLayout(3, 3);
        grid.setSpacing(true);
        grid.setMargin(true);
        grid.setWidth("100%");
        grid.setHeight(null);

        grid.addComponent(empresaCombo);
        grid.addComponent(nomeTarefaTextField, 1, 0, 2, 0);
        grid.addComponent(dataInicioDateField);
        grid.addComponent(tipoRecorrenciaCombo);
        grid.addComponent(prioridadeCombo);
        grid.addComponent(dataFimDateField);
        grid.addComponent(statusTarefaPopUpButton);
        grid.addComponent(avisoButton);

        return grid;
    }

    /**
     * Constrói e retorna a barra de botões superior
     *
     * @return
     */
    private Component buildBarraBotoesSuperior() {

        barraBotoesSuperior = new HorizontalLayout();
        barraBotoesSuperior.setSizeUndefined();

        apontamentoHorasCheckBox = new CheckBox(mensagens.getString("CadastroTarefaView.apontamentoHorasCheckBox.caption"));
        apontamentoHorasCheckBox.addValueChangeListener((Property.ValueChangeEvent event) -> {
            listener.apontamentoHorasSwitched(event);
        });

        barraBotoesSuperior.addComponent(apontamentoHorasCheckBox);

        orcamentoControladoCheckBox = new CheckBox(mensagens.getString("CadastroTarefaView.orcamentoControladoCheckBox.caption"));
        orcamentoControladoCheckBox.addValueChangeListener((Property.ValueChangeEvent event) -> {
            listener.controleOrcamentoSwitched(event);
        });

        barraBotoesSuperior.addComponent(orcamentoControladoCheckBox);

        addSubButton = new Button("[Add Sub]", (Button.ClickEvent event) -> {
            listener.addSubButtonClicked();
        });
        barraBotoesSuperior.addComponent(addSubButton);

        chatButton = new Button("[Chat]", (Button.ClickEvent event) -> {
            listener.chatButtonClicked();
        });
        barraBotoesSuperior.addComponent(chatButton);

        projecaoButton = new Button("[Projeção]", (Button.ClickEvent event) -> {
            listener.projecaoButtonClicked();
        });
        barraBotoesSuperior.addComponent(projecaoButton);

        return barraBotoesSuperior;
    }

    /**
     * Constrói e retorna a barra de botões superior
     *
     * @return
     */
    private Component buildBarraBotoesInferior() {

        HorizontalLayout barraBotoesInferior = new HorizontalLayout();
        barraBotoesInferior.setSizeUndefined();
        barraBotoesInferior.setSpacing(true);

        gravarButton = new Button(mensagens.getString("CadastroTarefaView.gravarButton.caption"), (Button.ClickEvent event) -> {
            try {
                setValidatorsVisible(true);
                tarefaFieldGroup.commit();
                listener.gravarButtonClicked();
            } catch (FieldGroup.CommitException ex) {
                Notification.show(ex.getLocalizedMessage(), Notification.Type.ERROR_MESSAGE);
                Logger.getLogger(CadastroTarefaView.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        barraBotoesInferior.addComponent(gravarButton);

        cancelarButton = new Button(mensagens.getString("CadastroTarefaView.cancelarButton.caption"), (Button.ClickEvent event) -> {
            listener.cancelarButtonClicked();
        });
        barraBotoesInferior.addComponent(cancelarButton);

        return barraBotoesInferior;
    }

    /**
     * Constrói e retorna a aba de descrição e responsáveis
     *
     * @return
     */
    private Component buildAbaDescricaoEResponsaveis() {

        descricaoTarefaTextArea = new RichTextArea(mensagens.getString("CadastroTarefaView.descricaoTarefaTextArea.caption"));
        descricaoTarefaTextArea.setNullRepresentation("");

        usuarioResponsavelCombo = new ComboBox(mensagens.getString("CadastroTarefaView.responsavelCombo.label"));
        usuarioResponsavelCombo.addValidator(new BeanValidator(Tarefa.class, "usuarioResponsavel"));
        camposObrigatorios.add(usuarioResponsavelCombo);
        
        participantesCombo = new ComboBox(mensagens.getString("CadastroTarefaView.participantesCombo.label"));

        participantesCombo.addValueChangeListener((Property.ValueChangeEvent event) -> {
            participantesContainer.addBean((ParticipanteTarefa) event.getProperty().getValue());
        });

        participantesContainer = new BeanItemContainer<>(ParticipanteTarefa.class);
        

        Table participantesTable = new Table();
        participantesTable.setContainerDataSource(participantesContainer);
        
        participantesTable.setColumnWidth("usuarioParticipante", 120);
        participantesTable.setColumnHeader("usuarioParticipante", mensagens.getString("CadastroTarefaView.participantesTable.colunaParticipante"));
        
                
        participantesTable.setVisibleColumns("usuarioParticipante");

        // Adicionar coluna do botão "remover"
        participantesTable.addGeneratedColumn(mensagens.getString("CadastroTarefaView.participantesTable.colunaBotaoRemover"), (Table source, final Object itemId, Object columnId) -> {
            Button removeButton = new Button("x");
            removeButton.addClickListener((ClickEvent event) -> {
                participantesTable.removeItem(itemId);
            });
            return removeButton;
        });

        participantesTable.setSelectable(true);
        participantesTable.setImmediate(true);
        participantesTable.setWidth("100%");
        participantesTable.setPageLength(5);

        empresaClienteCombo = new ComboBox(mensagens.getString("CadastroTarefaView.empresaClienteCombo.label"));

        // do layout :
        GridLayout layout = new GridLayout(2, 4);
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setSizeFull();

        layout.addComponent(usuarioResponsavelCombo, 0, 0);
        layout.addComponent(participantesCombo, 0, 1);
        layout.addComponent(participantesTable, 0, 2);
        layout.addComponent(empresaClienteCombo, 0, 3);
        layout.addComponent(descricaoTarefaTextArea, 1, 0, 1, 3);
        
        return layout;
    }

    /**
     * constrói e retorna a aba de detalhes
     *
     * @return
     */
    private Component buildAbaDetalhes() {

        departamentoCombo = new ComboBox(mensagens.getString("CadastroTarefaView.departamentoCombo.caption"));

        centroCustoCombo = new ComboBox(mensagens.getString("CadastroTarefaView.centroCustoCombo.caption"));

        adicionarAnexoButton = new Upload();
        adicionarAnexoButton.addStartedListener((Upload.StartedEvent event) -> {
            listener.solicitacaoParaAdicionarAnexo(event);
        });
        adicionarAnexoButton.addFinishedListener((Upload.FinishedEvent event) -> {
            listener.anexoAdicionado(event);
        });

        Table anexosAdicionadosTable = new Table();
        anexosAdicionadosTable.addContainerProperty(mensagens.getString("CadastroTarefaView.anexosAdicionadosTable.colunaNome"), String.class, "");
        anexosAdicionadosTable.setColumnWidth(mensagens.getString("CadastroTarefaView.anexosAdicionadosTable.colunaNome"), 100);
        anexosAdicionadosTable.addContainerProperty(mensagens.getString("CadastroTarefaView.anexosAdicionadosTable.colunaArquivo"), String.class, "");
        anexosAdicionadosTable.setColumnWidth(mensagens.getString("CadastroTarefaView.anexosAdicionadosTable.colunaArquivo"), 70);
        anexosAdicionadosTable.addContainerProperty(mensagens.getString("CadastroTarefaView.anexosAdicionadosTable.colunaBotaoRemover"), Button.class, "");
        anexosAdicionadosTable.setColumnWidth(mensagens.getString("CadastroTarefaView.anexosAdicionadosTable.colunaBotaoRemover"), 20);
        anexosAdicionadosTable.setSelectable(true);
        anexosAdicionadosTable.setImmediate(true);
        anexosAdicionadosTable.setWidth("100%");
        anexosAdicionadosTable.setPageLength(5);

        // Do layout:
        GridLayout layout = new GridLayout(2, 2);
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.setSizeFull();

        layout.addComponent(departamentoCombo, 0, 0);
        layout.addComponent(centroCustoCombo, 0, 1);
        layout.addComponent(adicionarAnexoButton, 1, 0);
        layout.addComponent(anexosAdicionadosTable, 1, 1);
        layout.setComponentAlignment(anexosAdicionadosTable, Alignment.TOP_RIGHT);

        layout.setRowExpandRatio(0, 0);
        layout.setRowExpandRatio(1, 1);

        layout.setColumnExpandRatio(0, 0);
        layout.setColumnExpandRatio(1, 1);

        return layout;
    }

    /**
     * Bind (liga) um apontamento de tarefa (horas) ao formulário
     *
     * @param apontamentoTarefa
     */
    public void setApontamentoTarefa(ApontamentoTarefa apontamentoTarefa) {

        apontamentoTarefaBeanItem = new BeanItem<>(apontamentoTarefa);
        apontamentoTarefaFieldGroup = new FieldGroup(apontamentoTarefaBeanItem);

        apontamentoTarefaFieldGroup.bind(custoHoraTextField, "custoHora");
        apontamentoTarefaFieldGroup.bind(imputarHorasTextField, "inputHoras");
        apontamentoTarefaFieldGroup.bind(observacaoHorasTextField, "observacoes");

    }

    /**
     * Obtem o apontamento de tarefa (horas) ligada (binding) ao form
     *
     * @return
     * @throws com.vaadin.data.fieldgroup.FieldGroup.CommitException
     */
    public ApontamentoTarefa getApontamentoTarefa() throws FieldGroup.CommitException {
        apontamentoTarefaFieldGroup.commit();
        return apontamentoTarefaBeanItem.getBean();
    }

    /**
     * Constroi e retorna a aba de controle de horas
     *
     * @return
     */
    private Component buildAbaControleHoras() {

        // Campos do controle de horas
        custoHoraTextField = new TextField();
        custoHoraTextField.setInputPrompt(mensagens.getString("CadastroTarefaView.custoHoraTextField.inputPrompt"));
        custoHoraTextField.setNullRepresentation("");

        imputarHorasTextField = new TextField();
        imputarHorasTextField.setInputPrompt(mensagens.getString("CadastroTarefaView.imputarHorasTextField.inputPrompt"));
        imputarHorasTextField.setNullRepresentation("");

        observacaoHorasTextField = new TextField();
        observacaoHorasTextField.setInputPrompt(mensagens.getString("CadastroTarefaView.observacaoHorasTextField.inputPrompt"));
        observacaoHorasTextField.setNullRepresentation("");

        imputarHorasButton = new Button(mensagens.getString("CadastroTarefaView.imputarHorasButton.caption"));
        imputarHorasButton.addClickListener((Button.ClickEvent event) -> {
            listener.imputarHorasClicked();
        });

        controleHorasContainer = new BeanItemContainer<>(ApontamentoTarefa.class);

        Table controleHorasTable = new Table();
        controleHorasTable.setContainerDataSource(controleHorasContainer);

        controleHorasTable.setColumnWidth("dataHoraInclusao", 80);
        controleHorasTable.setColumnHeader("dataHoraInclusao", mensagens.getString("CadastroTarefaView.controleHorasTable.colunaData"));
        controleHorasTable.setColumnWidth("observacoes", 150);
        controleHorasTable.setColumnHeader("observacoes", mensagens.getString("CadastroTarefaView.controleHorasTable.colunaObservacoes"));
        controleHorasTable.setColumnWidth("creditoHoras", 80);
        controleHorasTable.setColumnHeader("creditoHoras", mensagens.getString("CadastroTarefaView.controleHorasTable.colunaCreditoHoras"));
        controleHorasTable.setColumnWidth("debitoHoras", 80);
        controleHorasTable.setColumnHeader("debitoHoras", mensagens.getString("CadastroTarefaView.controleHorasTable.colunaDebitoHoras"));
        controleHorasTable.setColumnWidth("saldoHoras", 80);
        controleHorasTable.setColumnHeader("saldoHoras", mensagens.getString("CadastroTarefaView.controleHorasTable.colunaSaldoHoras"));
        controleHorasTable.setColumnWidth("creditoValor", 80);
        controleHorasTable.setColumnHeader("creditoValor", mensagens.getString("CadastroTarefaView.controleHorasTable.colunaCreditoValor"));
        controleHorasTable.setColumnWidth("debitoValor", 80);
        controleHorasTable.setColumnHeader("debitoValor", mensagens.getString("CadastroTarefaView.controleHorasTable.colunaDebitoValor"));
        controleHorasTable.setColumnWidth("saldoValor", 80);
        controleHorasTable.setColumnHeader("saldoValor", mensagens.getString("CadastroTarefaView.controleHorasTable.colunaSaldoValor"));

        controleHorasTable.setVisibleColumns("dataHoraInclusao", "observacoes", "creditoHoras", "debitoHoras", "saldoHoras");
        // Adicionar coluna do botão "remover"
        controleHorasTable.addGeneratedColumn("Remove", (Table source, final Object itemId, Object columnId) -> {
            Button removeButton = new Button("x");
            removeButton.addClickListener((ClickEvent event) -> {
                controleHorasTable.removeItem(itemId);
            });
            return removeButton;
        });

        controleHorasTable.setSelectable(true);
        controleHorasTable.setImmediate(true);
        controleHorasTable.setPageLength(5);
        controleHorasTable.setWidth("100%");

        // Do layout:
        HorizontalLayout controleHorasLayout = new HorizontalLayout();
        controleHorasLayout.setSpacing(true);
        controleHorasLayout.addComponent(custoHoraTextField);
        controleHorasLayout.addComponent(imputarHorasTextField);
        controleHorasLayout.addComponent(observacaoHorasTextField);
        controleHorasLayout.addComponent(imputarHorasButton);

        controleHorasAba = new VerticalLayout();
        controleHorasAba.setMargin(true);
        controleHorasAba.setSpacing(true);

        controleHorasAba.addComponent(controleHorasLayout);
        controleHorasAba.addComponent(controleHorasTable);

        return controleHorasAba;
    }

    /**
     * Constrói e retorna a aba de controle de orçamento
     *
     * @return
     */
    private Component buildAbaOrcamento() {

        // Campos do controle de orçamento
        imputarOrcamentoTextField = new TextField();
        imputarOrcamentoTextField.setInputPrompt(mensagens.getString("CadastroTarefaView.imputarOrcamentoTextField.inputPrompt"));

        observacaoOrcamentoTextField = new TextField();
        observacaoOrcamentoTextField.setInputPrompt(mensagens.getString("CadastroTarefaView.observacaoOrcamentoTextField.inputPrompt"));

        imputarOrcamentoButton = new Button(mensagens.getString("CadastroTarefaView.imputarOrcamentoButton.caption"), (Button.ClickEvent event) -> {
            listener.imputarOrcamentoClicked();
        });

        Table controleOrcamentoTable = new Table();
        controleOrcamentoTable.addContainerProperty(mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaData"), String.class, "");
        controleOrcamentoTable.setColumnWidth(mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaData"), 80);
        controleOrcamentoTable.addContainerProperty(mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaObservacoes"), String.class, "");
        controleOrcamentoTable.setColumnWidth(mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaObservacoes"), 150);
        controleOrcamentoTable.addContainerProperty(mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaCredito"), String.class, "");
        controleOrcamentoTable.setColumnWidth(mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaCredito"), 80);
        controleOrcamentoTable.addContainerProperty(mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaDebito"), String.class, "");
        controleOrcamentoTable.setColumnWidth(mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaDebito"), 80);
        controleOrcamentoTable.addContainerProperty(mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaSaldo"), String.class, "");
        controleOrcamentoTable.setColumnWidth(mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaSaldo"), 80);

        controleOrcamentoTable.addContainerProperty(mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaBotaoRemover"), Button.class, "");
        controleOrcamentoTable.setColumnWidth(mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaBotaoRemover"), 20);
        controleOrcamentoTable.setSelectable(true);
        controleOrcamentoTable.setImmediate(true);
        controleOrcamentoTable.setPageLength(5);
        controleOrcamentoTable.setWidth("100%");

        // Do layout:
        HorizontalLayout orcamentoContainer = new HorizontalLayout();
        orcamentoContainer.setSpacing(true);
        orcamentoContainer.addComponent(imputarOrcamentoTextField);
        orcamentoContainer.addComponent(observacaoOrcamentoTextField);
        orcamentoContainer.addComponent(imputarOrcamentoButton);

        controleOrcamentoAba = new VerticalLayout();
        controleOrcamentoAba.setMargin(true);
        controleOrcamentoAba.setSpacing(true);

        controleOrcamentoAba.addComponent(orcamentoContainer);
        controleOrcamentoAba.addComponent(controleOrcamentoTable);

        return controleOrcamentoAba;
    }

    /**
     * Oculta / revela a aba de controle de horas
     *
     * @param visible
     */
    public void setAbaControleHorasVisible(boolean visible) {

        TabSheet.Tab tab = accordion.getTab(controleHorasAba);
        tab.setVisible(visible);

    }

    /**
     * Oculta / revela a aba de controle de Orcamento
     *
     * @param visible
     */
    public void setAbaControleOrcamentoVisible(boolean visible) {

        TabSheet.Tab tab = accordion.getTab(controleOrcamentoAba);
        tab.setVisible(visible);

    }

    /**
     *
     * @return
     */
    private Component buildAbaSubTarefas() {

        subTarefasTable = new TreeTable();
        subTarefasTable.setWidth("100%");
        subTarefasTable.addContainerProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaCod"), String.class, "");
        subTarefasTable.setColumnWidth(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaCod"), 70);
        subTarefasTable.addContainerProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaTitulo"), String.class, "");
        subTarefasTable.setColumnWidth(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaTitulo"), 50);
        subTarefasTable.addContainerProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaNome"), String.class, "");
        subTarefasTable.setColumnWidth(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaNome"), 250);
        subTarefasTable.addContainerProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaEmpresaFilial"), String.class, "");
        subTarefasTable.setColumnWidth(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaEmpresaFilial"), 200);
        subTarefasTable.addContainerProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaSolicitante"), String.class, "");
        subTarefasTable.setColumnWidth(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaSolicitante"), 80);
        subTarefasTable.addContainerProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaResponsavel"), String.class, "");
        subTarefasTable.setColumnWidth(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaResponsavel"), 80);
        subTarefasTable.addContainerProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaDataInicio"), String.class, "");
        subTarefasTable.setColumnWidth(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaDataInicio"), 80);
        subTarefasTable.addContainerProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaDataFim"), String.class, "");
        subTarefasTable.setColumnWidth(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaDataFim"), 80);
        subTarefasTable.addContainerProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaStatus"), PopupButton.class, "");
        subTarefasTable.setColumnWidth(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaStatus"), 200);
        subTarefasTable.addContainerProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaProjecao"), Character.class, "");
        subTarefasTable.setColumnWidth(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaProjecao"), 30);
        subTarefasTable.addContainerProperty("[Email]", Button.class, "");
        subTarefasTable.setColumnWidth("[Email]", 30);
        subTarefasTable.addContainerProperty("[Chat]", Button.class, "");
        subTarefasTable.setColumnWidth("[Chat]", 30);

        subTarefasTable.setPageLength(4);
        subTarefasTable.setSelectable(true);
        subTarefasTable.setImmediate(true);

        return subTarefasTable;
    }

    /**
     * @return the mensagens
     */
    public ResourceBundle getMensagens() {
        return mensagens;
    }

    /**
     * @return the imagens
     */
    public GestorWebImagens getImagens() {
        return imagens;
    }

    /**
     * @return the listener
     */
    public CadastroTarefaViewListener getListener() {
        return listener;
    }

    /**
     * @return the nomeTarefaTextField
     */
    public TextField getNomeTarefaTextField() {
        return nomeTarefaTextField;
    }

    /**
     * @return the empresaCombo
     */
    public ComboBox getEmpresaCombo() {
        return empresaCombo;
    }

    /**
     * @return the barraBotoesSuperior
     */
    public HorizontalLayout getBarraBotoesSuperior() {
        return barraBotoesSuperior;
    }

    /**
     * @return the addSubButton
     */
    public Button getAddSubButton() {
        return addSubButton;
    }

    /**
     * @return the chatButton
     */
    public Button getChatButton() {
        return chatButton;
    }

    /**
     * @return the projecaoButton
     */
    public Button getProjecaoButton() {
        return projecaoButton;
    }

    /**
     * @return the dataInicioDateField
     */
    public PopupDateField getDataInicioDateField() {
        return dataInicioDateField;
    }

    /**
     * @return the dataFimDateField
     */
    public PopupDateField getDataFimDateField() {
        return dataFimDateField;
    }

    /**
     * @return the tipoRecorrenciaCombo
     */
    public ComboBox getTipoRecorrenciaCombo() {
        return tipoRecorrenciaCombo;
    }

    /**
     * @return the prioridadeCombo
     */
    public ComboBox getPrioridadeCombo() {
        return prioridadeCombo;
    }

    /**
     * @return the avisoButton
     */
    public Button getAvisoButton() {
        return avisoButton;
    }

    /**
     * @return the usuarioResponsavelCombo
     */
    public ComboBox getUsuarioResponsavelCombo() {
        return usuarioResponsavelCombo;
    }

    /**
     * @return the participantesCombo
     */
    public ComboBox getParticipantesCombo() {
        return participantesCombo;
    }

    /**
     * @return the empresaClienteCombo
     */
    public ComboBox getEmpresaClienteCombo() {
        return empresaClienteCombo;
    }

    /**
     * @return the descricaoTarefaTextArea
     */
    public RichTextArea getDescricaoTarefaTextArea() {
        return descricaoTarefaTextArea;
    }

    /**
     * @return the departamentoCombo
     */
    public ComboBox getDepartamentoCombo() {
        return departamentoCombo;
    }

    /**
     * @return the centroCustoCombo
     */
    public ComboBox getCentroCustoCombo() {
        return centroCustoCombo;
    }

    /**
     * @return the adicionarAnexoButton
     */
    public Upload getAdicionarAnexoButton() {
        return adicionarAnexoButton;
    }

    /**
     * @return the custoHoraTextField
     */
    public TextField getCustoHoraTextField() {
        return custoHoraTextField;
    }

    /**
     * @return the imputarHorasTextField
     */
    public TextField getImputarHorasTextField() {
        return imputarHorasTextField;
    }

    /**
     * @return the observacaoHorasTextField
     */
    public TextField getObservacaoHorasTextField() {
        return observacaoHorasTextField;
    }

    /**
     * @return the imputarHorasButton
     */
    public Button getImputarHorasButton() {
        return imputarHorasButton;
    }

    /**
     * @return the imputarOrcamentoTextField
     */
    public TextField getImputarOrcamentoTextField() {
        return imputarOrcamentoTextField;
    }

    /**
     * @return the observacaoOrcamentoTextField
     */
    public TextField getObservacaoOrcamentoTextField() {
        return observacaoOrcamentoTextField;
    }

    /**
     * @return the imputarOrcamentoButton
     */
    public Button getImputarOrcamentoButton() {
        return imputarOrcamentoButton;
    }

    /**
     * @return the gravarButton
     */
    public Button getGravarButton() {
        return gravarButton;
    }

    /**
     * @return the cancelarButton
     */
    public Button getCancelarButton() {
        return cancelarButton;
    }

    /**
     * @return the subTarefasTable
     */
    public TreeTable getSubTarefasTable() {
        return subTarefasTable;
    }

    /**
     * @return the statusTarefaPopUpButton
     */
    public PopupButton getStatusTarefaPopUpButton() {
        return statusTarefaPopUpButton;
    }

    /**
     * @return the accordion
     */
    public Accordion getAccordion() {
        return accordion;
    }

    /**
     * @return the controleHorasAba
     */
    public VerticalLayout getControleHorasAba() {
        return controleHorasAba;
    }

    /**
     * @return the controleOrcamentoAba
     */
    public VerticalLayout getControleOrcamentoAba() {
        return controleOrcamentoAba;
    }

    public void ocultaPopUpEvolucaoStatusEAndamento() {
        statusTarefaPopUpButton.setVisible(false);
    }

    public void exibeTituloCadastro() {
        setCaption(mensagens.getString("CadastroTarefaView.titulo.cadastro"));
    }

    public void exibeTituloEdicao() {
        setCaption(mensagens.getString("CadastroTarefaView.titulo.edicao"));
    }

    public BeanItemContainer<ApontamentoTarefa> getControleHorasContainer() {
        return controleHorasContainer;
    }

    public BeanItemContainer<ParticipanteTarefa> getParticipantesContainer() {
        return participantesContainer;
    }

    
}
