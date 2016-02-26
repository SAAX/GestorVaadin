package com.saax.gestorweb.view;

import com.saax.gestorweb.model.datamodel.Anexo;
import com.saax.gestorweb.model.datamodel.ApontamentoTarefa;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.OrcamentoTarefa;
import com.saax.gestorweb.model.datamodel.Participante;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.presenter.CallBackListener;
import com.saax.gestorweb.presenter.TarefaPresenter;
import com.saax.gestorweb.presenter.PopUpStatusPresenter;
import com.saax.gestorweb.presenter.GestorPresenter;
import com.saax.gestorweb.util.ErrorUtils;
import com.saax.gestorweb.util.FormatterUtil;
import com.saax.gestorweb.view.PopUpStatusListener;
import com.saax.gestorweb.view.PopUpStatusView;
import com.saax.gestorweb.view.TarefaViewListener;

import com.saax.gestorweb.view.converter.DateToLocalDateConverter;
import com.saax.gestorweb.view.validator.DataFimValidator;
import com.saax.gestorweb.view.validator.DataInicioValidator;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.StringToBigDecimalConverter;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable.Unit;
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
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.hene.popupbutton.PopupButton;

/**
 * Cadastro/Edição de tarefas.<br>
 *
 * @author rodrigo
 */
public class TarefaView extends Window {

    // The view maintains access to the listener (Presenter) to notify events
    // This access is through an interface to maintain the abstraction layers
    private TarefaViewListener listener;

    // List of all the fields that have validation
    private final List<AbstractField> camposObrigatorios;

    private Accordion accordion;

    // -------------------------------------------------------------------------
    // Header components
    // -------------------------------------------------------------------------
    @PropertyId("apontamentoHoras")
    private CheckBox apontamentoHorasCheckBox;

    @PropertyId("orcamentoControlado")
    private CheckBox controleOrcamentoChechBox;

    @PropertyId("template")
    private CheckBox templateCheckBox;

    private HorizontalLayout barraSuperiorLayout;
    private Button adicionarSubtarefaButton;
    private Button removerTarefaButton;
    private Button chatButton;
    private Label caminhoTarefaLabel;
    /**
     * COMENTADO: Projeção postergada para v2 private Button projectionButton;
     */

    // -----------------------------------------------------------------------------------
    // Bean Bidingset
    // -----------------------------------------------------------------------------------
    private BeanItem<Tarefa> tarefaBeanItem;
    private FieldGroup tarefaFieldGroup;

    // -------------------------------------------------------------------------
    // Dados básicos da tarefa
    // -------------------------------------------------------------------------
    @PropertyId("empresa")
    private ComboBox empresaCombo;

    @PropertyId("hierarquia")
    private ComboBox hierarquiaCombo;

    @PropertyId("nome")
    private TextField nomeTarefaTextField;

    @PropertyId("dataInicio")
    private PopupDateField dataInicioDateField;

    @PropertyId("dataFim")
    private PopupDateField dataFimDateField;

    @PropertyId("recurrencyMessage")
    private Label mensagemRecorrenciaLabel;

    @PropertyId("prioridade")
    private ComboBox prioridadeCombo;

    private PopupButton statusTarefaPopUpButton;
    private Button controleRecorrenciaButton;

    // -------------------------------------------------------------------------
    // Descrição, responsável e participantes
    // -------------------------------------------------------------------------
    @PropertyId("usuarioResponsavel")
    private ComboBox usuarioResponsavelCombo;

    @PropertyId("descricao")
    private RichTextArea descricaoTextArea;

    private ComboBox participantesCombo;

    private BeanItemContainer<Participante> participantesContainer;
    private Table participantesTable;

    @PropertyId("empresaCliente")
    private ComboBox empresaClienteCombo;

    // -------------------------------------------------------------------------
    // Componentes da Aba Detalhes
    // -------------------------------------------------------------------------
    @PropertyId("departamento")
    private ComboBox departamentoCombo;

    @PropertyId("centroCusto")
    private ComboBox centroCustoCombo;

    private Upload adicionarAnexoUploadButton;
    private BeanItemContainer<Anexo> anexosContainer;
    private Table anexosTarefaTable;

    // -------------------------------------------------------------------------
    // Controle de horas (apontamentos)
    // -------------------------------------------------------------------------
    private VerticalLayout controleHorasLayout;

    @PropertyId("custoHoraApontamento")
    private TextField custoHoraApontamentoTextField;

    private TextField horasApontadasTextField;
    private TextField observacaoApontamentoTextField;
    private Button adicionarApontamentoButton;
    private Button alteraCustoHoraButton;
    private BeanItem<ApontamentoTarefa> apontamentoTarefaBeanItem;
    private BeanItemContainer<ApontamentoTarefa> apontamentoTarefaContainer;
    private FieldGroup apontamentoTarefaFieldGroup;
    private Table apontamentosTable;

    // -------------------------------------------------------------------------
    // Controle de Orçamento
    // -------------------------------------------------------------------------
    private VerticalLayout controleOrcamentoLayout;
    private TextField valorOrcadoRealizadoTextField;
    private TextField observacaoOrcamentoTextField;
    private Button imputarOrcamentoButton;
    private BeanItemContainer<OrcamentoTarefa> orcamentoContainer;
    private BeanItem<OrcamentoTarefa> orcamentoBeanItem;
    private FieldGroup orcamentoFieldGroup;
    private Table orcamentoTable;

    // -------------------------------------------------------------------------
    // Components subtask Tab
    // -------------------------------------------------------------------------
    private TreeTable subTarefasTable;
    private HorizontalLayout uploadHorizontalLayout;
    private ProgressBar attachProgressBar;

    // -------------------------------------------------------------------------
    // Barra de botões inferior
    // -------------------------------------------------------------------------
    private Button gravarButton;
    private Button cancelarButton;

    private boolean editAllowed = true;

    /**
     * Construtor
     *
     */
    public TarefaView() {
        super();

        camposObrigatorios = new ArrayList();

        setModal(true);
        setWidth(1000, Unit.PIXELS);
        setHeight(600, Unit.PIXELS);

        // Main Container, which will store all other containers
        VerticalLayout mainContainer = buildMainContainer();
        mainContainer.setSpacing(true);
        setContent(mainContainer);

        center();

        setValidatorsVisible(false);

    }

    /**
     * |Configura o presenter (listener)
     *
     * @param listener
     */
    public void setListener(TarefaViewListener listener) {
        this.listener = listener;
    }

    /**
     * Liga o objeto interno da tarefa aos componentes visuais
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
     * Builds the main container that will hold all other
     *
     * @return
     */
    private VerticalLayout buildMainContainer() {

        VerticalLayout principalContainer = new VerticalLayout();
        principalContainer.setMargin(true);
        principalContainer.setSpacing(true);
        principalContainer.setSizeFull();

        // Add the upper buttons bar (chat buttons, add sub, etc.)
        principalContainer.addComponent(buildBarraBotoesSuperior());
        principalContainer.setComponentAlignment(barraSuperiorLayout, Alignment.MIDDLE_RIGHT);

        // Label of the task path
        caminhoTarefaLabel = new Label();
        principalContainer.addComponent(caminhoTarefaLabel);

        // Create the accordion tabs and add tabs
        accordion = new Accordion();
        accordion.setWidth("100%");
        // Add the tab of initial data
        accordion.addTab(buildInitialTaskDataSheet(), GestorPresenter.getMENSAGENS().getString("TarefaView.InitialTaskData.title"), null);
        // Add the description tab and responsible
        accordion.addTab(buildDescriptionAndAssigneeSheet(), GestorPresenter.getMENSAGENS().getString("TarefaView.DescriptionAndAssignee.title"), null);
        // Add the task detail tab
        accordion.addTab(buildDetailsSheet(), GestorPresenter.getMENSAGENS().getString("TarefaView.DetailsSheet.title"), null);
        // Add the optional tab hours control
        accordion.addTab(buildPointingHoursSheet(), GestorPresenter.getMENSAGENS().getString("TarefaView.PointingHours.title"), null);
        // Add the optional tab budget control
        accordion.addTab(buildBudgetSheet(), GestorPresenter.getMENSAGENS().getString("TarefaView.BudgetSheet.title"), null);
        // adiciona a aba sub tarefas
        accordion.addTab(buildSubTasksSheet(), GestorPresenter.getMENSAGENS().getString("TarefaView.SubTasks.tittle"), null);

        principalContainer.addComponent(accordion);
        principalContainer.setExpandRatio(accordion, 1);

        // Add the lower buttons bar with buttons to save and gates
        Component bottonBar = buildBarraBotoesInferior();
        principalContainer.addComponent(bottonBar);
        principalContainer.setComponentAlignment(bottonBar, Alignment.MIDDLE_CENTER);

        return principalContainer;

    }

    /**
     * Hide or display validations
     *
     * @param visible
     */
    public void setValidatorsVisible(boolean visible) {
        camposObrigatorios.stream().forEach((campo) -> {
            campo.setValidationVisible(visible);
        });
    }

    /**
     * Constructs tab of initial data:
     *
     * @return
     */
    private Component buildInitialTaskDataSheet() {

        // Combo: Company
        empresaCombo = new ComboBox(GestorPresenter.getMENSAGENS().getString("TarefaView.companyCombo.label"));
        empresaCombo.addValueChangeListener((Property.ValueChangeEvent event) -> {
            listener.empresaSelecionada(tarefaBeanItem.getBean(), (Empresa) event.getProperty().getValue());
        });
        empresaCombo.setWidth("100%");
        empresaCombo.addValidator(new BeanValidator(Tarefa.class, "empresa"));
        camposObrigatorios.add(empresaCombo);

        // Combo: Hierarchy
        hierarquiaCombo = new ComboBox(GestorPresenter.getMENSAGENS().getString("TarefaView.hierarchyCombo.label"));
        hierarquiaCombo.setWidth("140px");
        hierarquiaCombo.setTextInputAllowed(false);

        hierarquiaCombo.addValidator(new BeanValidator(Tarefa.class, "hierarquia"));
        hierarquiaCombo.addValueChangeListener((Property.ValueChangeEvent event) -> {
            listener.hierarquiaSelecionada((HierarquiaProjetoDetalhe) event.getProperty().getValue());
        });
        camposObrigatorios.add(hierarquiaCombo);

        // TextField: task Name
        nomeTarefaTextField = new TextField(GestorPresenter.getMENSAGENS().getString("TarefaView.nomeTarefaTextField.label"));
        nomeTarefaTextField.setWidth("100%");
        nomeTarefaTextField.setInputPrompt(GestorPresenter.getMENSAGENS().getString("TarefaView.nomeTarefaTextField.inputPrompt"));
        nomeTarefaTextField.setNullRepresentation("");
        nomeTarefaTextField.addValidator(new BeanValidator(Tarefa.class, "nome"));
        camposObrigatorios.add(nomeTarefaTextField);

        // TextField: Start Date
        dataInicioDateField = new PopupDateField(GestorPresenter.getMENSAGENS().getString("TarefaView.startDateTextField.label"));
        dataInicioDateField.setWidth("100%");
        dataInicioDateField.setInputPrompt(GestorPresenter.getMENSAGENS().getString("TarefaView.dataInicioDateField.inputPrompt"));
        dataInicioDateField.setConverter(new DateToLocalDateConverter());
        dataInicioDateField.addValidator(new BeanValidator(Tarefa.class, "dataInicio"));
        //dataInicioDateField.addValidator(new DataFuturaValidator(true, "Data de Início"));
        camposObrigatorios.add(dataInicioDateField);

        // Button Recurrence
        controleRecorrenciaButton = new Button(GestorPresenter.getMENSAGENS().getString("TarefaView.tipoRecorrenciaCombo.label"), (Button.ClickEvent event) -> {
            listener.recurrenceClicked(tarefaBeanItem.getBean());
        });
        controleRecorrenciaButton.setWidth("100%");

        // Combo Priority
        prioridadeCombo = new ComboBox(GestorPresenter.getMENSAGENS().getString("TarefaView.prioridadeCombo.label"));
        prioridadeCombo.setTextInputAllowed(false);
        prioridadeCombo.setWidth("100%");

        // task status pop-up
        statusTarefaPopUpButton = new PopupButton();
        statusTarefaPopUpButton.setWidth("100%");

        // TextField: End Date
        dataFimDateField = new PopupDateField(GestorPresenter.getMENSAGENS().getString("TarefaView.dataFimTextField.label"));
        dataFimDateField.setWidth("100%");
        dataFimDateField.setConverter(new DateToLocalDateConverter());

        dataInicioDateField.addValidator(new DataInicioValidator(dataFimDateField, "Data Inicio"));
        dataFimDateField.addValidator(new DataFimValidator(dataInicioDateField, "Data Fim"));

        mensagemRecorrenciaLabel = new Label();
        mensagemRecorrenciaLabel.setEnabled(false);
        mensagemRecorrenciaLabel.setWidth("100%");

        // configura o layout usando uma grid
        GridLayout grid = new GridLayout(3, 4);
        grid.setSpacing(true);
        grid.setMargin(true);
        grid.setWidth("100%");
        grid.setHeight(null);

        HorizontalLayout categoriaENomeContainer = new HorizontalLayout();
        categoriaENomeContainer.setSpacing(true);
        categoriaENomeContainer.setSizeFull();
        categoriaENomeContainer.addComponent(hierarquiaCombo);
        categoriaENomeContainer.addComponent(nomeTarefaTextField);
        categoriaENomeContainer.setExpandRatio(nomeTarefaTextField, 1);
        categoriaENomeContainer.setComponentAlignment(hierarquiaCombo, Alignment.BOTTOM_CENTER);

        grid.addComponent(empresaCombo);
        grid.addComponent(categoriaENomeContainer, 1, 0, 2, 0);
        grid.addComponent(dataInicioDateField);
        grid.addComponent(controleRecorrenciaButton);
        grid.setComponentAlignment(controleRecorrenciaButton, Alignment.BOTTOM_CENTER);
        grid.addComponent(prioridadeCombo);
        grid.addComponent(dataFimDateField);
        grid.addComponent(statusTarefaPopUpButton);
        grid.setComponentAlignment(statusTarefaPopUpButton, Alignment.BOTTOM_CENTER);
        grid.addComponent(new Label()); // leave it empty
        grid.addComponent(mensagemRecorrenciaLabel, 0, 3, 2, 3);

        return grid;
    }

    /**
     * Constructs and returns to the top button bar
     *
     * @return
     */
    private Component buildBarraBotoesSuperior() {

        barraSuperiorLayout = new HorizontalLayout();
        barraSuperiorLayout.setSpacing(true);
        barraSuperiorLayout.setSizeUndefined();

        templateCheckBox = new CheckBox(GestorPresenter.getMENSAGENS().getString("TarefaView.templateCheckBox.caption"));

        barraSuperiorLayout.addComponent(templateCheckBox);

        apontamentoHorasCheckBox = new CheckBox(GestorPresenter.getMENSAGENS().getString("TarefaView.apontamentoHorasCheckBox.caption"));
        apontamentoHorasCheckBox.addValueChangeListener((Property.ValueChangeEvent event) -> {
            listener.apontamentoHorasSwitched(event);
        });

        barraSuperiorLayout.addComponent(apontamentoHorasCheckBox);

        controleOrcamentoChechBox = new CheckBox(GestorPresenter.getMENSAGENS().getString("TarefaView.orcamentoControladoCheckBox.caption"));
        controleOrcamentoChechBox.addValueChangeListener((Property.ValueChangeEvent event) -> {
            listener.controleOrcamentoSwitched(event);
        });

        barraSuperiorLayout.addComponent(controleOrcamentoChechBox);

        adicionarSubtarefaButton = new Button("[Add Sub]", (Button.ClickEvent event) -> {
            listener.addSubButtonClicked(tarefaBeanItem.getBean());
        });
        barraSuperiorLayout.addComponent(adicionarSubtarefaButton);

        removerTarefaButton = new Button("Remover");
        removerTarefaButton.setEnabled(false);
        removerTarefaButton.addClickListener((ClickEvent event) -> {
            listener.removerTarefaButtonClicked(tarefaBeanItem.getBean());
        });

        removerTarefaButton.setIcon(FontAwesome.TRASH_O);
        barraSuperiorLayout.addComponent(removerTarefaButton);

        chatButton = new Button("[Chat]", (Button.ClickEvent event) -> {
            listener.chatButtonClicked(tarefaBeanItem.getBean());
        });
        barraSuperiorLayout.addComponent(chatButton);

//Projeção será feita para a V2        
//        projectionButton = new Button("[Projeção]", (Button.ClickEvent event) -> {
//            listener.projecaoButtonClicked();
//        });
//        barraSuperiorLayout.addComponent(projectionButton);
        return barraSuperiorLayout;
    }

    /**
     * Constructs and returns to the top button bar
     *
     * @return
     */
    private Component buildBarraBotoesInferior() {

        HorizontalLayout lowerButtonsBar = new HorizontalLayout();
        lowerButtonsBar.setSizeUndefined();
        lowerButtonsBar.setSpacing(true);

        gravarButton = new Button(GestorPresenter.getMENSAGENS().getString("TarefaView.gravarButton.caption"), (Button.ClickEvent event) -> {
            try {
                setValidatorsVisible(true);
                tarefaFieldGroup.commit();
                listener.gravarButtonClicked(tarefaBeanItem.getBean());
            }
            catch (RuntimeException ex) {
                Notification notification = new Notification("Erro", (ex.getMessage() == null ? GestorPresenter.getMENSAGENS().getString("ErrorUtils.errogenerico") : ex.getMessage()),
                        Notification.Type.WARNING_MESSAGE, true);

                notification.show(Page.getCurrent());
                Logger.getLogger(TarefaView.class.getName()).log(Level.WARNING, null, ex);

            }
            catch (Exception ex) {
                ErrorUtils.showComponentErrors(this.tarefaFieldGroup.getFields());
                Logger.getLogger(TarefaView.class.getName()).log(Level.WARNING, null, ex);
            }
        });

        lowerButtonsBar.addComponent(gravarButton);

        cancelarButton = new Button(GestorPresenter.getMENSAGENS().getString("TarefaView.cancelarButton.caption"), (Button.ClickEvent event) -> {
            listener.cancelarButtonClicked();
        });
        lowerButtonsBar.addComponent(cancelarButton);

        return lowerButtonsBar;
    }

    /**
     * Constructs and returns the description tab and responsible
     *
     * @return
     */
    private Component buildDescriptionAndAssigneeSheet() {

        descricaoTextArea = new RichTextArea(GestorPresenter.getMENSAGENS().getString("TarefaView.descricaoTarefaTextArea.caption"));
        descricaoTextArea.setNullRepresentation("");

        usuarioResponsavelCombo = new ComboBox(GestorPresenter.getMENSAGENS().getString("TarefaView.responsavelCombo.label"));
        usuarioResponsavelCombo.addValueChangeListener((Property.ValueChangeEvent event) -> {
            if (event.getProperty().getValue() != null) {
                listener.assigneeUserChanged(tarefaBeanItem.getBean(), (Usuario) event.getProperty().getValue());
            }
        });

        camposObrigatorios.add(usuarioResponsavelCombo);

        participantesCombo = new ComboBox(GestorPresenter.getMENSAGENS().getString("TarefaView.participantesCombo.label"));

        participantesCombo.addValueChangeListener((Property.ValueChangeEvent event) -> {
            if (event.getProperty().getValue() != null) {
                listener.adicionarParticipante(tarefaBeanItem.getBean(), (Usuario) event.getProperty().getValue());
            }
        });

        participantesContainer = new BeanItemContainer<>(Participante.class);

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
        participantesTable.setColumnHeader("usuarioParticipante", GestorPresenter.getMENSAGENS().getString("TarefaView.participantesTable.colunaParticipante"));

        participantesTable.setVisibleColumns("usuarioParticipante");

        // Adicionar coluna do botão "remover"
        participantesTable.addGeneratedColumn(GestorPresenter.getMENSAGENS().getString("TarefaView.participantesTable.colunaBotaoRemover"), (Table source, final Object itemId, Object columnId) -> {
            Button removeButton = new Button(GestorPresenter.getMENSAGENS().getString("TarefaView.participantesTable.colunaBotaoRemover"));
            removeButton.addClickListener((ClickEvent event) -> {
                listener.removerParticipante((Participante) itemId);
            });

            removeButton.setEnabled(editAllowed);
            return removeButton;
        });

        participantesTable.setSelectable(true);
        participantesTable.setImmediate(true);
        participantesTable.setWidth("100%");
        participantesTable.setPageLength(3);

        Label labelCliente = new Label("<b>" + GestorPresenter.getMENSAGENS().getString("TarefaView.empresaClienteCombo.label") + "</b>", ContentMode.HTML);
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
        layout.addComponent(descricaoTextArea, 2, 0, 2, 2);

        return layout;
    }

    /**
     * builds and returns the Details tab
     *
     * @return
     */
    private Component buildDetailsSheet() {

        // department Combo
        departamentoCombo = new ComboBox(GestorPresenter.getMENSAGENS().getString("TarefaView.departamentoCombo.caption"));

        // Cost Center combo
        centroCustoCombo = new ComboBox(GestorPresenter.getMENSAGENS().getString("TarefaView.centroCustoCombo.caption"));

        // upload Progress Bar
        uploadHorizontalLayout = new HorizontalLayout();
        uploadHorizontalLayout.setWidth("100%");

        attachProgressBar = new ProgressBar();
        attachProgressBar.setWidth("100%");

        adicionarAnexoUploadButton = new Upload(GestorPresenter.getMENSAGENS().getString("TarefaView.adicionarAnexoButton.filechooser.caption"), (String filename, String mimeType) -> {
            FileOutputStream fos = null;
            try {
                if (!new File(System.getProperty("user.home") + System.getProperty("file.separator") + "tmp").canWrite() || !new File(System.getProperty("user.home") + System.getProperty("file.separator") + "tmp").isDirectory()) {
                    new File(System.getProperty("user.home") + System.getProperty("file.separator") + "tmp").mkdir();
                }

                File randomFolder = new File(System.getProperty("user.home") + System.getProperty("file.separator") + "tmp" + System.getProperty("file.separator") + String.valueOf(Math.abs(new Random().nextInt())));
                randomFolder.mkdirs();

                File file = new File(randomFolder, filename);
                fos = new FileOutputStream(file);

                adicionarAnexoUploadButton.setData(file);

            }
            catch (FileNotFoundException e) {
                Notification.show(e.getMessage(), Notification.Type.WARNING_MESSAGE);
                return null;
            }
            return fos;
        });
        adicionarAnexoUploadButton.setButtonCaption(GestorPresenter.getMENSAGENS().getString("TarefaView.adicionarAnexoButton.caption"));

        adicionarAnexoUploadButton.addSucceededListener((Upload.SucceededEvent event) -> {
            listener.anexoAdicionado(tarefaBeanItem.getBean(), (File) event.getUpload().getData());
        });
        adicionarAnexoUploadButton.addProgressListener((long readBytes, long contentLength) -> {
            UI.getCurrent().access(() -> {
                float newValue = readBytes / (float) contentLength;
                attachProgressBar.setValue(newValue);
            });
        });
        adicionarAnexoUploadButton.addFinishedListener((Upload.FinishedEvent event) -> {
            uploadHorizontalLayout.removeComponent(attachProgressBar);
            Notification.show(GestorPresenter.getMENSAGENS().getString("TarefaView.adicionarAnexoButton.uploadConcluido.mensagem"), Notification.Type.TRAY_NOTIFICATION);
        });
        adicionarAnexoUploadButton.addStartedListener((Upload.StartedEvent event) -> {
            uploadHorizontalLayout.addComponent(attachProgressBar);
        });

        uploadHorizontalLayout.addComponent(adicionarAnexoUploadButton);

        anexosContainer = new BeanItemContainer<>(Anexo.class);

        anexosTarefaTable = new Table();
        anexosTarefaTable.setContainerDataSource(anexosContainer);

        anexosTarefaTable.setColumnWidth("nome", 350);

        anexosTarefaTable.setColumnHeader("nome", GestorPresenter.getMENSAGENS().getString("TarefaView.anexosAdicionadosTable.colunaNome"));

        anexosTarefaTable.setVisibleColumns("nome");

        // Adicionar coluna do botão "download"
        anexosTarefaTable.addGeneratedColumn(GestorPresenter.getMENSAGENS().getString("TarefaView.anexosAdicionadosTable.colunaBotaoDownload"), (Table source, final Object itemId, Object columnId) -> {
            Button downloadButton = new Button(GestorPresenter.getMENSAGENS().getString("TarefaView.anexosAdicionadosTable.colunaBotaoDownload"));
            Anexo anexoTarefa = (Anexo) itemId;
            FileDownloader fd = new FileDownloader(new FileResource(new File(anexoTarefa.getCaminhoCompleto())));

            fd.extend(downloadButton);
            downloadButton.setEnabled(true);
            return downloadButton;
        });
        anexosTarefaTable.setColumnWidth(GestorPresenter.getMENSAGENS().getString("TarefaView.anexosAdicionadosTable.colunaBotaoDownload"), 50);

        anexosTarefaTable.addGeneratedColumn(GestorPresenter.getMENSAGENS().getString("TarefaView.anexosAdicionadosTable.colunaBotaoRemover"), (Table source, final Object itemId, Object columnId) -> {
            Button removeButton = new Button(GestorPresenter.getMENSAGENS().getString("TarefaView.anexosAdicionadosTable.colunaBotaoRemover"));
            removeButton.addClickListener((ClickEvent event) -> {
                listener.removerAnexo((Anexo) itemId);
            });
            removeButton.setEnabled(true);
            return removeButton;
        });
        anexosTarefaTable.setColumnWidth(GestorPresenter.getMENSAGENS().getString("TarefaView.anexosAdicionadosTable.colunaBotaoRemover"), 50);
        anexosTarefaTable.setSelectable(true);
        anexosTarefaTable.setImmediate(true);
        anexosTarefaTable.setWidth("100%");
        anexosTarefaTable.setHeight("150px");
        anexosTarefaTable.setPageLength(3);

        GridLayout layout = new GridLayout(2, 2);
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.setWidth("100%");
        layout.setHeight(null);

        layout.addComponent(departamentoCombo, 0, 0);
        layout.addComponent(centroCustoCombo, 0, 1);
        layout.addComponent(uploadHorizontalLayout, 1, 0);
        layout.addComponent(anexosTarefaTable, 1, 1);

        layout.setRowExpandRatio(0, 0);
        layout.setRowExpandRatio(1, 1);

        layout.setColumnExpandRatio(0, 0);
        layout.setColumnExpandRatio(1, 1);

        return layout;
    }

    /**
     * Bind (on) a task appointment (hours) to form
     *
     * @param taskAppointment
     */
    public void setApontamentoTarefa(ApontamentoTarefa taskAppointment) {

        apontamentoTarefaBeanItem = new BeanItem<>(taskAppointment);
        apontamentoTarefaFieldGroup = new FieldGroup(apontamentoTarefaBeanItem);

        apontamentoTarefaFieldGroup.bind(custoHoraApontamentoTextField, "custoHora");
        apontamentoTarefaFieldGroup.bind(horasApontadasTextField, "inputHoras");
        apontamentoTarefaFieldGroup.bind(observacaoApontamentoTextField, "observacoes");

    }

    /**
     * Get the task of pointing (hours) on (binding) to form
     *
     * @return
     * @throws com.vaadin.data.fieldgroup.FieldGroup.CommitException
     */
    public ApontamentoTarefa getApontamentoTarefa() throws FieldGroup.CommitException {
        apontamentoTarefaFieldGroup.commit();
        return apontamentoTarefaBeanItem.getBean();
    }

    /**
     * Constructs and returns the hour control tab
     *
     * @return
     */
    private Component buildPointingHoursSheet() {

        // Hours control fields
        custoHoraApontamentoTextField = new TextField();
        custoHoraApontamentoTextField.setInputPrompt(GestorPresenter.getMENSAGENS().getString("TarefaView.custoHoraTextField.inputPrompt"));
        custoHoraApontamentoTextField.setNullRepresentation("");
        custoHoraApontamentoTextField.setConverter(new StringToBigDecimalConverter());
        custoHoraApontamentoTextField.addValueChangeListener((Property.ValueChangeEvent event) -> {
            listener.custoHoraApontamentoValueChage();
        });

        horasApontadasTextField = new TextField();
        horasApontadasTextField.setInputPrompt(GestorPresenter.getMENSAGENS().getString("TarefaView.imputarHorasTextField.inputPrompt"));
        horasApontadasTextField.setNullRepresentation("");

        observacaoApontamentoTextField = new TextField();
        observacaoApontamentoTextField.setInputPrompt(GestorPresenter.getMENSAGENS().getString("TarefaView.observacaoHorasTextField.inputPrompt"));
        observacaoApontamentoTextField.setNullRepresentation("");

        adicionarApontamentoButton = new Button(GestorPresenter.getMENSAGENS().getString("TarefaView.imputarHorasButton.caption"));
        adicionarApontamentoButton.addClickListener((Button.ClickEvent event) -> {
            listener.imputarHorasClicked(tarefaBeanItem.getBean());
        });

        alteraCustoHoraButton = new Button("Alterar Custo");
        alteraCustoHoraButton.addClickListener((Button.ClickEvent event) -> {
            listener.alteraCustoHoraClicked(tarefaBeanItem.getBean());
        });

        apontamentoTarefaContainer = new BeanItemContainer<>(ApontamentoTarefa.class);

        apontamentosTable = new Table() {
            @Override
            protected String formatPropertyValue(Object rowId,
                    Object colId, Property property) {

                if (property.getType() == LocalDateTime.class) {

                    return FormatterUtil.formatDateTime((LocalDateTime) property.getValue());

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

                    return df.format(property.getValue());
                }

                return super.formatPropertyValue(rowId, colId, property);
            }
        };

        apontamentosTable.setContainerDataSource(apontamentoTarefaContainer);

        apontamentosTable.setColumnWidth("dataHoraInclusao", 150);
        apontamentosTable.setColumnHeader("dataHoraInclusao", GestorPresenter.getMENSAGENS().getString("TarefaView.controleHorasTable.colunaData"));
        apontamentosTable.setColumnWidth("observacoes", 150);
        apontamentosTable.setColumnHeader("observacoes", GestorPresenter.getMENSAGENS().getString("TarefaView.controleHorasTable.colunaObservacoes"));
        apontamentosTable.setColumnWidth("creditoHoras", 80);
        apontamentosTable.setColumnHeader("creditoHoras", GestorPresenter.getMENSAGENS().getString("TarefaView.controleHorasTable.colunaCreditoHoras"));
        apontamentosTable.setColumnWidth("debitoHoras", 80);
        apontamentosTable.setColumnHeader("debitoHoras", GestorPresenter.getMENSAGENS().getString("TarefaView.controleHorasTable.colunaDebitoHoras"));
        apontamentosTable.setColumnWidth("saldoHoras", 80);
        apontamentosTable.setColumnHeader("saldoHoras", GestorPresenter.getMENSAGENS().getString("TarefaView.controleHorasTable.colunaSaldoHoras"));
        apontamentosTable.setColumnWidth("creditoValor", 80);
        apontamentosTable.setColumnAlignment("creditoValor", Table.Align.RIGHT);
        apontamentosTable.setColumnHeader("creditoValor", GestorPresenter.getMENSAGENS().getString("TarefaView.controleHorasTable.colunaCreditoValor"));
        apontamentosTable.setColumnWidth("debitoValor", 80);
        apontamentosTable.setColumnAlignment("debitoValor", Table.Align.RIGHT);
        apontamentosTable.setColumnHeader("debitoValor", GestorPresenter.getMENSAGENS().getString("TarefaView.controleHorasTable.colunaDebitoValor"));
        apontamentosTable.setColumnWidth("saldoValor", 80);
        apontamentosTable.setColumnAlignment("saldoValor", Table.Align.RIGHT);
        apontamentosTable.setColumnHeader("saldoValor", GestorPresenter.getMENSAGENS().getString("TarefaView.controleHorasTable.colunaSaldoValor"));
        apontamentosTable.setColumnAlignment("custoHora", Table.Align.RIGHT);
        apontamentosTable.setColumnHeader("custoHora", GestorPresenter.getMENSAGENS().getString("TarefaView.controleHorasTable.colunaCustoHora"));
        apontamentosTable.setColumnWidth("custoHora", 80);

        apontamentosTable.setVisibleColumns("dataHoraInclusao", "observacoes", "creditoHoras", "debitoHoras", "saldoHoras", "creditoValor", "debitoValor", "saldoValor", "custoHora");
        apontamentosTable.setSortContainerPropertyId("dataHoraInclusao");
        apontamentosTable.setSortEnabled(false);

        // Adicionar coluna do botão "remover"
        apontamentosTable.addGeneratedColumn("Remove", (Table source, final Object itemId, Object columnId) -> {
            Button removeButton = new Button("x");
            removeButton.addClickListener((ClickEvent event) -> {
                listener.removePointingTime((ApontamentoTarefa) itemId);
            });
            removeButton.setEnabled(editAllowed);
            return removeButton;
        });

        apontamentosTable.setSelectable(true);
        apontamentosTable.setImmediate(true);
        apontamentosTable.setPageLength(5);
        apontamentosTable.setWidth("100%");

        HorizontalLayout controleHorasLayout = new HorizontalLayout();

        HorizontalLayout inputApontamentoLayout = new HorizontalLayout();
        inputApontamentoLayout.setSpacing(true);
        inputApontamentoLayout.addComponent(horasApontadasTextField);
        inputApontamentoLayout.addComponent(observacaoApontamentoTextField);
        inputApontamentoLayout.addComponent(adicionarApontamentoButton);

        HorizontalLayout alteraCustoHoraLayout = new HorizontalLayout();
        alteraCustoHoraLayout.setSpacing(true);
        alteraCustoHoraLayout.addComponent(custoHoraApontamentoTextField);
        alteraCustoHoraLayout.addComponent(alteraCustoHoraButton);

        controleHorasLayout.addComponent(inputApontamentoLayout);
        controleHorasLayout.setComponentAlignment(inputApontamentoLayout, Alignment.MIDDLE_LEFT);

        controleHorasLayout.addComponent(alteraCustoHoraLayout);
        controleHorasLayout.setComponentAlignment(alteraCustoHoraLayout, Alignment.MIDDLE_RIGHT);

        this.controleHorasLayout = new VerticalLayout();
        this.controleHorasLayout.setSpacing(true);
        this.controleHorasLayout.setMargin(true);
        this.controleHorasLayout.setWidth("100%");
        this.controleHorasLayout.setHeight(null);

        this.controleHorasLayout.addComponent(controleHorasLayout);
        this.controleHorasLayout.addComponent(apontamentosTable);

        return this.controleHorasLayout;
    }

    /**
     * Bind (on) a task budget record to form
     *
     * @param taskBudget
     */
    public void setOrcamentoTarefa(OrcamentoTarefa taskBudget) {

        orcamentoBeanItem = new BeanItem<>(taskBudget);
        orcamentoFieldGroup = new FieldGroup(orcamentoBeanItem);

        orcamentoFieldGroup.bind(valorOrcadoRealizadoTextField, "inputValor");
        orcamentoFieldGroup.bind(observacaoOrcamentoTextField, "observacoes");

    }

    /**
     * Get the budget control on (binding) to form
     *
     * @return
     * @throws com.vaadin.data.fieldgroup.FieldGroup.CommitException
     */
    public OrcamentoTarefa getOrcamentoTarefa() throws FieldGroup.CommitException {
        orcamentoFieldGroup.commit();
        return orcamentoBeanItem.getBean();
    }

    /**
     * Constructs and returns the budget control tab
     *
     * @return
     */
    private Component buildBudgetSheet() {

        valorOrcadoRealizadoTextField = new TextField();
        valorOrcadoRealizadoTextField.setInputPrompt(GestorPresenter.getMENSAGENS().getString("TarefaView.imputarOrcamentoTextField.inputPrompt"));
        valorOrcadoRealizadoTextField.setNullRepresentation("");
        valorOrcadoRealizadoTextField.setConverter(new StringToBigDecimalConverter());

        observacaoOrcamentoTextField = new TextField();
        observacaoOrcamentoTextField.setInputPrompt(GestorPresenter.getMENSAGENS().getString("TarefaView.observacaoOrcamentoTextField.inputPrompt"));
        observacaoOrcamentoTextField.setNullRepresentation("");

        imputarOrcamentoButton = new Button(GestorPresenter.getMENSAGENS().getString("TarefaView.imputarOrcamentoButton.caption"), (Button.ClickEvent event) -> {
            listener.imputarOrcamentoClicked(tarefaBeanItem.getBean());
        });

        orcamentoContainer = new BeanItemContainer<>(OrcamentoTarefa.class);

        orcamentoTable = new Table() {
            @Override
            protected String formatPropertyValue(Object rowId,
                    Object colId, Property property) {

                if (property.getType() == LocalDateTime.class) {

                    return FormatterUtil.formatDateTime((LocalDateTime) property.getValue());

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

                    return df.format(property.getValue());
                }

                return super.formatPropertyValue(rowId, colId, property);
            }

        };
        orcamentoTable.setContainerDataSource(orcamentoContainer);
        orcamentoTable.setColumnWidth("dataHoraInclusao", 150);
        orcamentoTable.setColumnHeader("dataHoraInclusao", GestorPresenter.getMENSAGENS().getString("TarefaView.controleOrcamentoTable.colunaData"));
        orcamentoTable.setColumnWidth("observacoes", 150);
        orcamentoTable.setColumnHeader("observacoes", GestorPresenter.getMENSAGENS().getString("TarefaView.controleOrcamentoTable.colunaObservacoes"));
        orcamentoTable.setColumnWidth("credito", 80);
        orcamentoTable.setColumnHeader("credito", GestorPresenter.getMENSAGENS().getString("TarefaView.controleOrcamentoTable.colunaCredito"));
        orcamentoTable.setColumnAlignment("credito", Table.Align.RIGHT);
        orcamentoTable.setColumnWidth("debito", 80);
        orcamentoTable.setColumnHeader("debito", GestorPresenter.getMENSAGENS().getString("TarefaView.controleOrcamentoTable.colunaDebito"));
        orcamentoTable.setColumnAlignment("debito", Table.Align.RIGHT);
        orcamentoTable.setColumnWidth("saldo", 80);
        orcamentoTable.setColumnHeader("saldo", GestorPresenter.getMENSAGENS().getString("TarefaView.controleOrcamentoTable.colunaSaldo"));
        orcamentoTable.setColumnAlignment("saldo", Table.Align.RIGHT);

        orcamentoTable.setVisibleColumns("dataHoraInclusao", "credito", "debito", "saldo", "observacoes");
        orcamentoTable.setSortContainerPropertyId("dataHoraInclusao");
        orcamentoTable.setSortEnabled(false);

        orcamentoTable.addGeneratedColumn(GestorPresenter.getMENSAGENS().getString("TarefaView.controleOrcamentoTable.colunaBotaoRemover"), (Table source, final Object itemId, Object columnId) -> {
            Button removeButton = new Button(GestorPresenter.getMENSAGENS().getString("TarefaView.controleOrcamentoTable.colunaBotaoRemover"));
            removeButton.addClickListener((ClickEvent event) -> {
                listener.removerRegistroOrcamento((OrcamentoTarefa) itemId);
            });
            removeButton.setEnabled(editAllowed);
            return removeButton;
        });

        orcamentoTable.setSelectable(true);
        orcamentoTable.setImmediate(true);
        orcamentoTable.setPageLength(5);
        orcamentoTable.setWidth("100%");

        HorizontalLayout orcamentoLayout = new HorizontalLayout();
        orcamentoLayout.setSpacing(true);
        orcamentoLayout.addComponent(valorOrcadoRealizadoTextField);
        orcamentoLayout.addComponent(observacaoOrcamentoTextField);
        orcamentoLayout.addComponent(imputarOrcamentoButton);

        controleOrcamentoLayout = new VerticalLayout();
        controleOrcamentoLayout.setSpacing(true);
        controleOrcamentoLayout.setMargin(true);
        controleOrcamentoLayout.setWidth("100%");
        controleOrcamentoLayout.setHeight(null);

        controleOrcamentoLayout.addComponent(orcamentoLayout);
        controleOrcamentoLayout.addComponent(orcamentoTable);

        return controleOrcamentoLayout;
    }

    /**
     * Hides / shows the time control tab
     *
     * @param visible
     */
    public void setAbaControleHorasVisible(boolean visible) {

        TabSheet.Tab tab = accordion.getTab(controleHorasLayout);
        tab.setVisible(visible);

    }

    /**
     * Hides / shows the budget control tab
     *
     * @param visible
     */
    public void setAbaControleOrcamentoVisible(boolean visible) {

        TabSheet.Tab tab = accordion.getTab(controleOrcamentoLayout);
        tab.setVisible(visible);

    }

    /**
     *
     * @return
     */
    private Component buildSubTasksSheet() {

        subTarefasTable = new TreeTable() {
            {
                this.alwaysRecalculateColumnWidths = true;
            }
        };
        GestorPresenter.configuraExpansaoColunaCodigo(subTarefasTable, GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaCod"));

        subTarefasTable.setWidth("100%");
        subTarefasTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaCod"), Button.class, "");
        subTarefasTable.setColumnWidth(GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaCod"), 70);
        subTarefasTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaTitulo"), Button.class, "");
        subTarefasTable.setColumnWidth(GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaTitulo"), 50);
        subTarefasTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaNome"), Button.class, "");
        subTarefasTable.setColumnWidth(GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaNome"), 250);
        subTarefasTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaEmpresaFilial"), String.class, "");
        subTarefasTable.setColumnWidth(GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaEmpresaFilial"), 200);
        subTarefasTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaSolicitante"), String.class, "");
        subTarefasTable.setColumnWidth(GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaSolicitante"), 80);
        subTarefasTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaResponsavel"), String.class, "");
        subTarefasTable.setColumnWidth(GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaResponsavel"), 80);
        subTarefasTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaDataInicio"), String.class, "");
        subTarefasTable.setColumnWidth(GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaDataInicio"), 80);
        subTarefasTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaDataFim"), String.class, "");
        subTarefasTable.setColumnWidth(GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaDataFim"), 80);
        subTarefasTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaStatus"), PopupButton.class, "");
        subTarefasTable.setColumnWidth(GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaStatus"), 200);
        /**
         * COMENTADO: Projeção postergada para v2
         * subTarefasTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaProjecao"),
         * Character.class, "");
         */
        subTarefasTable.setColumnWidth(GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaProjecao"), 30);
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
     * @return the listener
     */
    public TarefaViewListener getListener() {
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
     * @return the barraSuperiorLayout
     */
    public HorizontalLayout getBarraSuperiorLayout() {
        return barraSuperiorLayout;
    }

    /**
     * @return the adicionarSubtarefaButton
     */
    public Button getAdicionarSubtarefaButton() {
        return adicionarSubtarefaButton;
    }

    /**
     * @return the chatButton
     */
    public Button getChatButton() {
        return chatButton;
    }

    /**
     * @return the projectionButton
     */
//    public Button getProjectionButton() {
//        return projectionButton;
//    }
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

    public Button getControleRecorrenciaButton() {
        return controleRecorrenciaButton;
    }

    public void setControleRecorrenciaButton(Button controleRecorrenciaButton) {
        this.controleRecorrenciaButton = controleRecorrenciaButton;
    }

    /**
     * @return the prioridadeCombo
     */
    public ComboBox getPrioridadeCombo() {
        return prioridadeCombo;
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
     * @return the descricaoTextArea
     */
    public RichTextArea getDescricaoTextArea() {
        return descricaoTextArea;
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
     * @return the adicionarAnexoUploadButton
     */
    public Upload getAdicionarAnexoUploadButton() {
        return adicionarAnexoUploadButton;
    }

    /**
     * @return the custoHoraApontamentoTextField
     */
    public TextField getCustoHoraApontamentoTextField() {
        return custoHoraApontamentoTextField;
    }

    /**
     * @return the horasApontadasTextField
     */
    public TextField getHorasApontadasTextField() {
        return horasApontadasTextField;
    }

    /**
     * @return the observacaoApontamentoTextField
     */
    public TextField getObservacaoApontamentoTextField() {
        return observacaoApontamentoTextField;
    }

    /**
     * @return the adicionarApontamentoButton
     */
    public Button getAdicionarApontamentoButton() {
        return adicionarApontamentoButton;
    }

    /**
     * @return the valorOrcadoRealizadoTextField
     */
    public TextField getValorOrcadoRealizadoTextField() {
        return valorOrcadoRealizadoTextField;
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
     * @return the controleHorasLayout
     */
    public VerticalLayout getControleHorasLayout() {
        return controleHorasLayout;
    }

    /**
     * @return the controleOrcamentoLayout
     */
    public VerticalLayout getControleOrcamentoLayout() {
        return controleOrcamentoLayout;
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

    public BeanItemContainer<ApontamentoTarefa> getApontamentoTarefaContainer() {
        return apontamentoTarefaContainer;
    }

    public BeanItemContainer<Participante> getParticipantesContainer() {
        return participantesContainer;
    }

    public BeanItemContainer<OrcamentoTarefa> getOrcamentoContainer() {
        return orcamentoContainer;
    }

    public Table getApontamentosTable() {
        return apontamentosTable;
    }

    public Table getParticipantesTable() {
        return participantesTable;
    }

    public BeanItemContainer<Anexo> getAnexosContainer() {
        return anexosContainer;
    }

    public Table getAnexosTarefaTable() {
        return anexosTarefaTable;
    }

    public Table getOrcamentoTable() {
        return orcamentoTable;
    }

    public CheckBox getControleOrcamentoChechBox() {
        return controleOrcamentoChechBox;
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

    public ComboBox getHierarquiaCombo() {
        return hierarquiaCombo;
    }

    /**
     * Oculta/Exibe o status da tarefa
     *
     * @param visible
     */
    public void setStatusVisible(boolean visible) {
        statusTarefaPopUpButton.setVisible(visible);
    }

    public void setEditAllowed(boolean editAllowed) {

        this.editAllowed = editAllowed;

        // Header:
        templateCheckBox.setEnabled(editAllowed);
        apontamentoHorasCheckBox.setEnabled(editAllowed);
        controleOrcamentoChechBox.setEnabled(editAllowed);
        adicionarSubtarefaButton.setEnabled(editAllowed);

        // Basic Data:
        empresaCombo.setEnabled(editAllowed);
        hierarquiaCombo.setEnabled(editAllowed);
        nomeTarefaTextField.setEnabled(editAllowed);
        dataInicioDateField.setEnabled(editAllowed);
        dataFimDateField.setEnabled(editAllowed);
        controleRecorrenciaButton.setEnabled(editAllowed);
        prioridadeCombo.setEnabled(editAllowed);

        // Description tab of components and Responsible
        usuarioResponsavelCombo.setEnabled(editAllowed);
        descricaoTextArea.setEnabled(editAllowed);
        participantesCombo.setEnabled(editAllowed);
        empresaClienteCombo.setEnabled(editAllowed);

        // Componentes da Aba Detalhes
        departamentoCombo.setEnabled(editAllowed);
        centroCustoCombo.setEnabled(editAllowed);
        adicionarAnexoUploadButton.setEnabled(true);
        anexosTarefaTable.setEnabled(true);

        // Tab Hours Control Components
        custoHoraApontamentoTextField.setEnabled(editAllowed);
        horasApontadasTextField.setEnabled(editAllowed);
        observacaoApontamentoTextField.setEnabled(editAllowed);
        adicionarApontamentoButton.setEnabled(editAllowed);

        // Components of the budget tab
        valorOrcadoRealizadoTextField.setEnabled(editAllowed);
        observacaoOrcamentoTextField.setEnabled(editAllowed);
        imputarOrcamentoButton.setEnabled(editAllowed);

    }

    public Label getMensagemRecorrenciaLabel() {
        return mensagemRecorrenciaLabel;
    }

    public Button getRemoverTarefaButton() {
        return removerTarefaButton;
    }

    public Button getAlteraCustoHoraButton() {
        return alteraCustoHoraButton;
    }

}
