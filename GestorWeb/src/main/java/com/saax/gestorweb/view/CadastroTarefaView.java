package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.datamodel.AnexoTarefa;
import com.saax.gestorweb.model.datamodel.ApontamentoTarefa;
import com.saax.gestorweb.model.datamodel.OrcamentoTarefa;
import com.saax.gestorweb.model.datamodel.ParticipanteTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.view.converter.DateToLocalDateConverter;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.StringToBigDecimalConverter;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.shared.ui.label.ContentMode;
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
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

    @PropertyId("template")
    private CheckBox templateCheckBox;

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
    private BeanItemContainer<AnexoTarefa> anexoTarefaContainer;
    ;
    

    
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
    private BeanItemContainer<OrcamentoTarefa> orcamentoContainer;
    private BeanItem<OrcamentoTarefa> orcamentoTarefaBeanItem;
    private FieldGroup orcamentoTarefaFieldGroup;

    // -------------------------------------------------------------------------
    // Componentes da Aba de SubTarefa
    // -------------------------------------------------------------------------
    private TreeTable subTarefasTable;
    private Table controleHorasTable;
    private Table participantesTable;
    private Table anexosAdicionadosTable;
    private Table controleOrcamentoTable;
    private Label caminhoTarefaLabel;
    private HorizontalLayout uploadHorizontalLayout;
    private ProgressBar anexoProgressBar;

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

        setModal(true);
        setWidth(1000, Unit.PIXELS);
        setHeight(600, Unit.PIXELS);

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

        caminhoTarefaLabel.setValue(getCaminhoTarefa(tarefa));
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

        // label com caminho da tarefa
        caminhoTarefaLabel = new Label();
        containerPrincipal.addComponent(caminhoTarefaLabel);

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
        dataInicioDateField.setInputPrompt(mensagens.getString("CadastroTarefaView.dataInicioDateField.inputPrompt"));
        dataInicioDateField.setConverter(new DateToLocalDateConverter());
        dataInicioDateField.addValidator(new BeanValidator(Tarefa.class, "dataInicio"));
        //dataInicioDateField.addValidator(new DataFuturaValidator(true, "Data de Início"));
        camposObrigatorios.add(dataInicioDateField);

        // Combo Recorrencia
        tipoRecorrenciaCombo = new ComboBox(mensagens.getString("CadastroTarefaView.tipoRecorrenciaCombo.label"));
        tipoRecorrenciaCombo.setWidth("100%");
        tipoRecorrenciaCombo.setInputPrompt(mensagens.getString("CadastroTarefaView.tipoRecorrenciaCombo.inputPrompt"));
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
        dataFimDateField.setConverter(new DateToLocalDateConverter());

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
        grid.setComponentAlignment(statusTarefaPopUpButton, Alignment.BOTTOM_CENTER);
        grid.addComponent(avisoButton);
        grid.setComponentAlignment(avisoButton, Alignment.BOTTOM_CENTER);

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

        templateCheckBox = new CheckBox(mensagens.getString("CadastroTarefaView.templateCheckBox.caption"));

        barraBotoesSuperior.addComponent(templateCheckBox);

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
                String mensagem = "";
                if (ex.getCause() instanceof Validator.InvalidValueException) {
                    Validator.InvalidValueException validationException = (Validator.InvalidValueException) ex.getCause();
                    for (Validator.InvalidValueException cause : validationException.getCauses()) {
                        mensagem += cause.getMessage() + "\n";
                    }

                } else {
                    mensagem = ex.getLocalizedMessage();
                }
                Notification.show(mensagem, Notification.Type.ERROR_MESSAGE);
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
        camposObrigatorios.add(usuarioResponsavelCombo);

        participantesCombo = new ComboBox(mensagens.getString("CadastroTarefaView.participantesCombo.label"));

        participantesCombo.addValueChangeListener((Property.ValueChangeEvent event) -> {
            listener.adicionarParticipante((Usuario) event.getProperty().getValue());

        });

        participantesContainer = new BeanItemContainer<>(ParticipanteTarefa.class);

        participantesTable = new Table() {
            @Override
            protected String formatPropertyValue(Object rowId,
                    Object colId, Property property) {
                // Format by property type
                if (property.getType() == Usuario.class) {

                    return ((Usuario) property.getValue()).getNome() + " " + ((Usuario) property.getValue()).getSobrenome();

                }

                return super.formatPropertyValue(rowId, colId, property);
            }
        };

        participantesTable.setContainerDataSource(participantesContainer);

        participantesTable.setColumnWidth("usuarioParticipante", 120);
        participantesTable.setColumnHeader("usuarioParticipante", mensagens.getString("CadastroTarefaView.participantesTable.colunaParticipante"));

        participantesTable.setVisibleColumns("usuarioParticipante");

        // Adicionar coluna do botão "remover"
        participantesTable.addGeneratedColumn(mensagens.getString("CadastroTarefaView.participantesTable.colunaBotaoRemover"), (Table source, final Object itemId, Object columnId) -> {
            Button removeButton = new Button(mensagens.getString("CadastroTarefaView.participantesTable.colunaBotaoRemover"));
            removeButton.addClickListener((ClickEvent event) -> {
                listener.removerParticipante((ParticipanteTarefa) itemId);
            });
            return removeButton;
        });

        participantesTable.setSelectable(true);
        participantesTable.setImmediate(true);
        participantesTable.setWidth("100%");
        participantesTable.setPageLength(3);

        Label labelCliente = new Label("<b>" + mensagens.getString("CadastroTarefaView.empresaClienteCombo.label") + "</b>", ContentMode.HTML);
        empresaClienteCombo = new ComboBox();

        // do layout :
        GridLayout layout = new GridLayout(3, 3);
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.setWidth("100%");
        layout.setHeight(null);

        layout.addComponent(usuarioResponsavelCombo, 0, 0);
        layout.addComponent(participantesCombo, 1, 0);
        layout.addComponent(participantesTable, 0, 1, 1, 1);
        layout.addComponent(labelCliente, 0, 2);
        layout.addComponent(empresaClienteCombo, 1, 2);
        layout.addComponent(descricaoTarefaTextArea, 2, 0, 2, 2);

        return layout;
    }

    /**
     * constrói e retorna a aba de detalhes
     *
     * @return
     */
    private Component buildAbaDetalhes() {

        // Combo de departamento
        departamentoCombo = new ComboBox(mensagens.getString("CadastroTarefaView.departamentoCombo.caption"));

        // Centro custo combo
        centroCustoCombo = new ComboBox(mensagens.getString("CadastroTarefaView.centroCustoCombo.caption"));

        // Barra de progresso do upload
        uploadHorizontalLayout = new HorizontalLayout();
        uploadHorizontalLayout.setWidth("100%");
        anexoProgressBar = new ProgressBar();

        adicionarAnexoButton = new Upload("", new Upload.Receiver() {

            @Override
            public OutputStream receiveUpload(String filename, String mimeType) {
                FileOutputStream fos = null;
                try {
                    File randomFolder = new File(String.valueOf(Math.abs(new Random().nextInt())));
                    randomFolder.mkdir();

                    File file = new File(randomFolder, filename);
                    fos = new FileOutputStream(file);

                    // cria uma pasta temporaria para gravação
                    adicionarAnexoButton.setData(file);

                } catch (FileNotFoundException e) {
                    Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
                    return null;
                }
                return fos;
            }

        });
        adicionarAnexoButton.addSucceededListener(new Upload.SucceededListener() {

            @Override
            public void uploadSucceeded(Upload.SucceededEvent event) {
                listener.anexoAdicionado(event);
            }
        });
        adicionarAnexoButton.addProgressListener(new Upload.ProgressListener() {

            @Override
            public void updateProgress(long readBytes, long contentLength) {
                UI.getCurrent().access(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // let's slow down upload a bit
                            Thread.sleep(100);
                            Logger.getLogger(CadastroTarefaView.class.getName()).log(Level.WARNING, "Remover sleep");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        float newValue = readBytes / (float) contentLength;
                        anexoProgressBar.setValue(newValue);
                        UI.getCurrent().push();
                    }
                });
            }
        });
        adicionarAnexoButton.addFinishedListener(new Upload.FinishedListener() {

            @Override
            public void uploadFinished(Upload.FinishedEvent event) {
                uploadHorizontalLayout.removeComponent(anexoProgressBar);
                Notification.show("Upload finished.", Notification.Type.TRAY_NOTIFICATION);
            }
        });
        adicionarAnexoButton.addStartedListener(new Upload.StartedListener() {

            @Override
            public void uploadStarted(Upload.StartedEvent event) {
                uploadHorizontalLayout.addComponent(anexoProgressBar);
                Notification.show("Upload started.", Notification.Type.TRAY_NOTIFICATION);

            }
        });
        //uploadHorizontalLayout.addComponent(anexoProgressBar);

        anexoTarefaContainer = new BeanItemContainer<>(AnexoTarefa.class);

        anexosAdicionadosTable = new Table();
        anexosAdicionadosTable.setContainerDataSource(anexoTarefaContainer);

        anexosAdicionadosTable.setColumnWidth("nome", 120);
        anexosAdicionadosTable.setColumnHeader("nome", mensagens.getString("CadastroTarefaView.anexosAdicionadosTable.colunaNome"));

        anexosAdicionadosTable.setVisibleColumns("nome");

        // Adicionar coluna do botão "remover"
        anexosAdicionadosTable.addGeneratedColumn(mensagens.getString("CadastroTarefaView.anexosAdicionadosTable.colunaBotaoRemover"), (Table source, final Object itemId, Object columnId) -> {
            Button removeButton = new Button(mensagens.getString("CadastroTarefaView.anexosAdicionadosTable.colunaBotaoRemover"));
            removeButton.addClickListener((ClickEvent event) -> {
                listener.removerAnexo((AnexoTarefa) itemId);

            });
            return removeButton;
        });

        anexosAdicionadosTable.setSelectable(true);
        anexosAdicionadosTable.setImmediate(true);
        anexosAdicionadosTable.setWidth("100%");
        anexosAdicionadosTable.setPageLength(3);

        // Do layout:
        GridLayout layout = new GridLayout(2, 3);
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.setWidth("100%");
        layout.setHeight(null);

        layout.addComponent(departamentoCombo, 0, 0);
        layout.addComponent(centroCustoCombo, 0, 1);
        layout.addComponent(adicionarAnexoButton, 1, 0);
        layout.addComponent(uploadHorizontalLayout, 1, 1);
        layout.addComponent(anexosAdicionadosTable, 1, 2);
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
        custoHoraTextField.setConverter(new StringToBigDecimalConverter());

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

        controleHorasTable = new Table() {
            @Override
            protected String formatPropertyValue(Object rowId,
                    Object colId, Property property) {
                // Format by property type
                if (property.getType() == LocalDateTime.class) {

                    return ((LocalDateTime) property.getValue()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

                } else if (property.getType() == Duration.class) {

                    if ((Duration) property.getValue() == null) {
                        return null;
                    }

                    DecimalFormat df = new DecimalFormat("00");
                    long hour = ((Duration) property.getValue()).toHours();
                    long minute = ((Duration) property.getValue()).toMinutes() % 60;

                    return new StringBuilder().append(df.format(hour)).append(":").append(df.format(minute)).toString();
                } else if (property.getType() == BigDecimal.class) {

                    if (property.getValue() == null) {
                        return null;
                    }

                    DecimalFormat df = new DecimalFormat("¤ #,##0.00");

                    return df.format(((BigDecimal) property.getValue()));
                }

                return super.formatPropertyValue(rowId, colId, property);
            }
        };

        controleHorasTable.setContainerDataSource(controleHorasContainer);

        controleHorasTable.setColumnWidth("dataHoraInclusao", 150);
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
        controleHorasTable.setColumnAlignment("creditoValor", Table.Align.RIGHT);
        controleHorasTable.setColumnHeader("creditoValor", mensagens.getString("CadastroTarefaView.controleHorasTable.colunaCreditoValor"));
        controleHorasTable.setColumnWidth("debitoValor", 80);
        controleHorasTable.setColumnAlignment("debitoValor", Table.Align.RIGHT);
        controleHorasTable.setColumnHeader("debitoValor", mensagens.getString("CadastroTarefaView.controleHorasTable.colunaDebitoValor"));
        controleHorasTable.setColumnWidth("saldoValor", 80);
        controleHorasTable.setColumnAlignment("saldoValor", Table.Align.RIGHT);
        controleHorasTable.setColumnHeader("saldoValor", mensagens.getString("CadastroTarefaView.controleHorasTable.colunaSaldoValor"));

        controleHorasTable.setVisibleColumns("dataHoraInclusao", "observacoes", "creditoHoras", "debitoHoras", "saldoHoras", "creditoValor", "debitoValor", "saldoValor");
        // Adicionar coluna do botão "remover"
        controleHorasTable.addGeneratedColumn("Remove", (Table source, final Object itemId, Object columnId) -> {
            Button removeButton = new Button("x");
            removeButton.addClickListener((ClickEvent event) -> {
                listener.removerApontamentoHoras((ApontamentoTarefa) itemId);

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
        controleHorasAba.setSpacing(true);
        controleHorasAba.setMargin(true);
        controleHorasAba.setWidth("100%");
        controleHorasAba.setHeight(null);

        controleHorasAba.addComponent(controleHorasLayout);
        controleHorasAba.addComponent(controleHorasTable);

        return controleHorasAba;
    }

    /**
     * Bind (liga) um registro de orçamento de tarefa ao formulário
     *
     * @param orcamentoTarefa
     */
    public void setOrcamentoTarefa(OrcamentoTarefa orcamentoTarefa) {

        orcamentoTarefaBeanItem = new BeanItem<>(orcamentoTarefa);
        orcamentoTarefaFieldGroup = new FieldGroup(orcamentoTarefaBeanItem);

        orcamentoTarefaFieldGroup.bind(imputarOrcamentoTextField, "inputValor");
        orcamentoTarefaFieldGroup.bind(observacaoOrcamentoTextField, "observacoes");

    }

    /**
     * Obtem o controle de orçamento ligado (binding) ao form
     *
     * @return
     * @throws com.vaadin.data.fieldgroup.FieldGroup.CommitException
     */
    public OrcamentoTarefa getOrcamentoTarefa() throws FieldGroup.CommitException {
        orcamentoTarefaFieldGroup.commit();
        return orcamentoTarefaBeanItem.getBean();
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
        imputarOrcamentoTextField.setNullRepresentation("");

        observacaoOrcamentoTextField = new TextField();
        observacaoOrcamentoTextField.setInputPrompt(mensagens.getString("CadastroTarefaView.observacaoOrcamentoTextField.inputPrompt"));
        observacaoOrcamentoTextField.setNullRepresentation("");

        imputarOrcamentoButton = new Button(mensagens.getString("CadastroTarefaView.imputarOrcamentoButton.caption"), (Button.ClickEvent event) -> {
            listener.imputarOrcamentoClicked();
        });

        orcamentoContainer = new BeanItemContainer<>(OrcamentoTarefa.class);

        controleOrcamentoTable = new Table() {
            @Override
            protected String formatPropertyValue(Object rowId,
                    Object colId, Property property) {

                // Format by property type
                if (property.getType() == LocalDateTime.class) {
                    return ((LocalDateTime) property.getValue()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                }

                return super.formatPropertyValue(rowId, colId, property);
            }
        };
        controleOrcamentoTable.setContainerDataSource(orcamentoContainer);
        controleOrcamentoTable.setColumnWidth("dataHoraInclusao", 150);
        controleOrcamentoTable.setColumnHeader("dataHoraInclusao", mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaData"));
        controleOrcamentoTable.setColumnWidth("observacoes", 150);
        controleOrcamentoTable.setColumnHeader("observacoes", mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaObservacoes"));
        controleOrcamentoTable.setColumnWidth("credito", 80);
        controleOrcamentoTable.setColumnHeader("credito", mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaCredito"));
        controleOrcamentoTable.setColumnAlignment("credito", Table.Align.RIGHT);
        controleOrcamentoTable.setColumnWidth("debito", 80);
        controleOrcamentoTable.setColumnHeader("debito", mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaDebito"));
        controleOrcamentoTable.setColumnAlignment("debito", Table.Align.RIGHT);
        controleOrcamentoTable.setColumnWidth("saldo", 80);
        controleOrcamentoTable.setColumnHeader("saldo", mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaSaldo"));
        controleOrcamentoTable.setColumnAlignment("saldo", Table.Align.RIGHT);

        controleOrcamentoTable.setVisibleColumns("dataHoraInclusao", "credito", "debito", "saldo", "observacoes");

        // Adicionar coluna do botão "remover"
        controleOrcamentoTable.addGeneratedColumn(mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaBotaoRemover"), (Table source, final Object itemId, Object columnId) -> {
            Button removeButton = new Button(mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaBotaoRemover"));
            removeButton.addClickListener((ClickEvent event) -> {
                listener.removerRegistroOrcamento((OrcamentoTarefa) itemId);
            });
            return removeButton;
        });

        controleOrcamentoTable.setSelectable(true);
        controleOrcamentoTable.setImmediate(true);
        controleOrcamentoTable.setPageLength(5);
        controleOrcamentoTable.setWidth("100%");

        // Do layout:
        HorizontalLayout orcamentoLayout = new HorizontalLayout();
        orcamentoLayout.setSpacing(true);
        orcamentoLayout.addComponent(imputarOrcamentoTextField);
        orcamentoLayout.addComponent(observacaoOrcamentoTextField);
        orcamentoLayout.addComponent(imputarOrcamentoButton);

        controleOrcamentoAba = new VerticalLayout();
        controleOrcamentoAba.setSpacing(true);
        controleOrcamentoAba.setMargin(true);
        controleOrcamentoAba.setWidth("100%");
        controleOrcamentoAba.setHeight(null);

        controleOrcamentoAba.addComponent(orcamentoLayout);
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
        subTarefasTable.addContainerProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaCod"), Button.class, "");
        subTarefasTable.setColumnWidth(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaCod"), 70);
        subTarefasTable.addContainerProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaTitulo"), Button.class, "");
        subTarefasTable.setColumnWidth(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaTitulo"), 50);
        subTarefasTable.addContainerProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaNome"), Button.class, "");
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
        subTarefasTable.addContainerProperty("[E]", Button.class, "");
        subTarefasTable.setColumnWidth("[E]", 30);
        subTarefasTable.addContainerProperty("[C]", Button.class, "");
        subTarefasTable.setColumnWidth("[C]", 30);

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

    private String getCaminhoTarefa(Tarefa tarefa) {
        if (tarefa.getTarefaPai() != null) {
            return getCaminhoTarefa(tarefa.getTarefaPai()) + " >> " + tarefa.getNome() == null ? "[NOVA]" : tarefa.getNome();
        } else {
            return tarefa.getNome() == null ? "[NOVA]" : tarefa.getNome();
        }
    }

    public void exibeTituloCadastro(Tarefa tarefapai) {
        setCaption(mensagens.getString("CadastroTarefaView.titulo.cadastro"));
    }

    public void exibeTituloEdicao(Tarefa tarefapai) {
        setCaption(mensagens.getString("CadastroTarefaView.titulo.edicao"));
    }

    public BeanItemContainer<ApontamentoTarefa> getControleHorasContainer() {
        return controleHorasContainer;
    }

    public BeanItemContainer<ParticipanteTarefa> getParticipantesContainer() {
        return participantesContainer;
    }

    public BeanItemContainer<OrcamentoTarefa> getOrcamentoContainer() {
        return orcamentoContainer;
    }

    public Table getControleHorasTable() {
        return controleHorasTable;
    }

    public Table getParticipantesTable() {
        return participantesTable;
    }

    public BeanItemContainer<AnexoTarefa> getAnexoTarefaContainer() {
        return anexoTarefaContainer;
    }

    public Table getAnexosAdicionadosTable() {
        return anexosAdicionadosTable;
    }

    public Table getControleOrcamentoTable() {
        return controleOrcamentoTable;
    }

    public CheckBox getOrcamentoControladoCheckBox() {
        return orcamentoControladoCheckBox;
    }

    public CheckBox getApontamentoHorasCheckBox() {
        return apontamentoHorasCheckBox;
    }

    public void apresentarCorFundoSubTarefa() {
        addStyleName("subtarefa");
    }

    public Label getCaminhoTarefaLabel() {
        return caminhoTarefaLabel;
    }

    public FieldGroup getTarefaFieldGroup() {
        return tarefaFieldGroup;
    }

}
