package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.TarefaModel;
import com.saax.gestorweb.model.PopUpStatusModel;
import com.saax.gestorweb.model.datamodel.AnexoTarefa;
import com.saax.gestorweb.model.datamodel.ApontamentoTarefa;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.OrcamentoTarefa;
import com.saax.gestorweb.model.datamodel.Participante;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.presenter.TarefaPresenter;
import com.saax.gestorweb.presenter.PopUpStatusPresenter;
import com.saax.gestorweb.util.ErrorUtils;
import com.saax.gestorweb.util.FormatterUtil;
import com.saax.gestorweb.util.GestorWebImagens;
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
import com.vaadin.ui.TextArea;
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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.hene.popupbutton.PopupButton;

/**
 * Pop-up Window task manager.<br>
 * The display will be in a accordion structure with three tabs:
 * <br>
 * <ol>
 * <li>Initial task data</li>
 * <li>Description and responsible</li>
 * <li>Time tracking (optional) </li>
 * <li>Budget control (optional)</li>
 * <li>subtasks</li>
 * </ol>
 *
 *
 * @author rodrigo
 */
public class TaskView extends Window {

    // Reference to the use of the messages:
    private final transient ResourceBundle messages = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final transient GestorWebImagens images = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();

    // The view maintains access to the listener (Presenter) to notify events
    // This access is through an interface to maintain the abstraction layers
    private TaskViewListener listener;

    // List of all the fields that have validation
    private final List<AbstractField> requiredFields;

    private Accordion accordion;

    // -------------------------------------------------------------------------
    // Header components
    // -------------------------------------------------------------------------
    @PropertyId("apontamentoHoras")
    private CheckBox pointingHoursCheckBox;

    @PropertyId("orcamentoControlado")
    private CheckBox budgetControlCheckBox;

    @PropertyId("template")
    private CheckBox templateCheckBox;

    private HorizontalLayout buttonsSuperiorBar;
    private Button addSubButton;
    private Button chatButton;
    private Button projectionButton;

    // -----------------------------------------------------------------------------------
    // Bean Biding
    // -----------------------------------------------------------------------------------
    private BeanItem<Tarefa> taskBeanItem;
    private FieldGroup taskFieldGroup;

    // -------------------------------------------------------------------------
    // Data Tab basic components
    // -------------------------------------------------------------------------
    @PropertyId("empresa")
    private ComboBox companyCombo;

    @PropertyId("hierarquia")
    private ComboBox hierarchyCombo;

    @PropertyId("nome")
    private TextField taskNameTextField;

    @PropertyId("dataInicio")
    private PopupDateField startDateDateField;

    @PropertyId("dataFim")
    private PopupDateField endDateDateField;

    private Button recurrencyButton;
    
    @PropertyId("recurrencyMessage")
    private Label recurrencyMessage;
    
    @PropertyId("prioridade")
    private ComboBox priorityCombo;

    private PopupButton taskStatusPopUpButton;

    // -------------------------------------------------------------------------
    // Description tab of components and Responsible
    // -------------------------------------------------------------------------
    @PropertyId("usuarioResponsavel")
    private ComboBox assigneeUserCombo;

    @PropertyId("descricao")
    private RichTextArea taskDescriptionTextArea;

    private ComboBox followersCombo;

    private BeanItemContainer<Participante> followersContainer;

    @PropertyId("empresaCliente")
    private ComboBox customerCompanyCombo;

    // -------------------------------------------------------------------------
    // Componentes da Aba Detalhes
    // -------------------------------------------------------------------------
    @PropertyId("departamento")
    private ComboBox departamentCombo;

    @PropertyId("centroCusto")
    private ComboBox costCenterCombo;

    private Upload addAttach;
    private BeanItemContainer<AnexoTarefa> taskAttachContainer;

    // -------------------------------------------------------------------------
    // Tab Hours Control Components
    // -------------------------------------------------------------------------
    private VerticalLayout hoursControlTab;

    private TextField hourCostTextField;
    private TextField hoursAddTextField;
    private TextField hoursObservationTextField;
    private Button hoursAddButton;
    private BeanItem<ApontamentoTarefa> taskAppointmentBeanItem;
    private BeanItemContainer<ApontamentoTarefa> hoursControlContainer;
    private FieldGroup taskAppointmentFieldGroup;

    // -------------------------------------------------------------------------
    // Components of the budget tab
    // -------------------------------------------------------------------------
    private VerticalLayout budgetControlTab;
    private TextField budgetAddTextField;
    private TextField observationBudgetTextField;
    private Button budgetAddButton;
    private BeanItemContainer<OrcamentoTarefa> budgetContainer;
    private BeanItem<OrcamentoTarefa> taskBudgetBeanItem;
    private FieldGroup tableBudgetFieldGroup;

    // -------------------------------------------------------------------------
    // Components subtask Tab
    // -------------------------------------------------------------------------
    private TreeTable subTasksTable;
    private Table pointingTimeTable;
    private Table followersTable;
    private Table attachmentsAddedTable;
    private Table budgetControlTable;
    private Label pathTaskLabel;
    private HorizontalLayout uploadHorizontalLayout;
    private ProgressBar attachProgressBar;

    // -------------------------------------------------------------------------
    // Buttons bar below
    // -------------------------------------------------------------------------
    private Button saveButton;
    private Button cancelButton;

    private boolean editAllowed = true;

    /**
     * Create a view and all components
     *
     */
    public TaskView() {
        super();

        requiredFields = new ArrayList();

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
     * Sets the listener's view events
     *
     * @param listener
     */
    public void setListener(TaskViewListener listener) {
        this.listener = listener;
    }

    /**
     * Bind (on) the task to form
     *
     * @param task
     */
    public void setTarefa(Tarefa task) {

        taskBeanItem = new BeanItem<>(task);
        taskFieldGroup = new FieldGroup(taskBeanItem);

        taskFieldGroup.bindMemberFields(this);

        pathTaskLabel.setValue(getCaminhoTarefa(task));
    }

    /**
     * Gets linked task (binding) to form
     *
     * @return
     */
    public Tarefa getTarefa() {
        return taskBeanItem.getBean();
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
        principalContainer.setComponentAlignment(buttonsSuperiorBar, Alignment.MIDDLE_RIGHT);

        // Label of the task path
        pathTaskLabel = new Label();
        principalContainer.addComponent(pathTaskLabel);

        // Create the accordion tabs and add tabs
        accordion = new Accordion();
        accordion.setWidth("100%");
        // Add the tab of initial data
        accordion.addTab(buildInitialTaskDataSheet(), messages.getString("TaskView.InitialTaskData.title"), null);
        // Add the description tab and responsible
        accordion.addTab(buildDescriptionAndAssigneeSheet(), messages.getString("TaskView.DescriptionAndAssignee.title"), null);
        // Add the task detail tab
        accordion.addTab(buildDetailsSheet(), messages.getString("TaskView.DetailsSheet.title"), null);
        // Add the optional tab hours control
        accordion.addTab(buildPointingHoursSheet(), messages.getString("TaskView.PointingHours.title"), null);
        // Add the optional tab budget control
        accordion.addTab(buildBudgetSheet(), messages.getString("TaskView.BudgetSheet.title"), null);
        // adiciona a aba sub tarefas
        accordion.addTab(buildSubTasksSheet(), messages.getString("TaskView.SubTasks.tittle"), null);

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
        requiredFields.stream().forEach((campo) -> {
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
        companyCombo = new ComboBox(messages.getString("TaskView.companyCombo.label"));
        companyCombo.addValueChangeListener((Property.ValueChangeEvent event) -> {
            listener.empresaSelecionada((Empresa) event.getProperty().getValue());
        });
        companyCombo.setWidth("100%");
        companyCombo.addValidator(new BeanValidator(Tarefa.class, "empresa"));
        requiredFields.add(companyCombo);

        // Combo: Hierarchy
        hierarchyCombo = new ComboBox(messages.getString("TaskView.hierarchyCombo.label"));
        hierarchyCombo.setWidth("140px");
        hierarchyCombo.addValidator(new BeanValidator(Tarefa.class, "hierarquia"));
        hierarchyCombo.addValueChangeListener((Property.ValueChangeEvent event) -> {
            listener.hierarquiaSelecionada((HierarquiaProjetoDetalhe) event.getProperty().getValue());
        });
        requiredFields.add(hierarchyCombo);

        // TextField: task Name
        taskNameTextField = new TextField(messages.getString("TaskView.nomeTarefaTextField.label"));
        taskNameTextField.setWidth("100%");
        taskNameTextField.setInputPrompt(messages.getString("TaskView.nomeTarefaTextField.inputPrompt"));
        taskNameTextField.setNullRepresentation("");
        taskNameTextField.addValidator(new BeanValidator(Tarefa.class, "nome"));
        requiredFields.add(taskNameTextField);

        // TextField: Start Date
        startDateDateField = new PopupDateField(messages.getString("TaskView.startDateTextField.label"));
        startDateDateField.setWidth("100%");
        startDateDateField.setInputPrompt(messages.getString("TaskView.dataInicioDateField.inputPrompt"));
        startDateDateField.setConverter(new DateToLocalDateConverter());
        startDateDateField.addValidator(new BeanValidator(Tarefa.class, "dataInicio"));
        //dataInicioDateField.addValidator(new DataFuturaValidator(true, "Data de Início"));
        requiredFields.add(startDateDateField);

        // Button Recurrence
        recurrencyButton = new Button(messages.getString("TaskView.tipoRecorrenciaCombo.label"), (Button.ClickEvent event) -> {
            listener.recurrenceClicked();
        });
        recurrencyButton.setWidth("100%");

        // Combo Priority
        priorityCombo = new ComboBox(messages.getString("TaskView.prioridadeCombo.label"));
        priorityCombo.setWidth("100%");

        // task status pop-up
        taskStatusPopUpButton = new PopupButton();
        taskStatusPopUpButton.setWidth("100%");

        // TextField: End Date
        endDateDateField = new PopupDateField(messages.getString("TaskView.dataFimTextField.label"));
        endDateDateField.setWidth("100%");
        endDateDateField.setConverter(new DateToLocalDateConverter());

        startDateDateField.addValidator(new DataInicioValidator(endDateDateField, "Data Inicio"));
        endDateDateField.addValidator(new DataFimValidator(startDateDateField, "Data Fim"));

        recurrencyMessage = new Label();
        recurrencyMessage.setEnabled(false);        
        recurrencyMessage.setWidth("100%");
        
        // configura o layout usando uma grid
        GridLayout grid = new GridLayout(3, 4);
        grid.setSpacing(true);
        grid.setMargin(true);
        grid.setWidth("100%");
        grid.setHeight(null);

        HorizontalLayout categoriaENomeContainer = new HorizontalLayout();
        categoriaENomeContainer.setSpacing(true);
        categoriaENomeContainer.setSizeFull();
        categoriaENomeContainer.addComponent(hierarchyCombo);
        categoriaENomeContainer.addComponent(taskNameTextField);
        categoriaENomeContainer.setExpandRatio(taskNameTextField, 1);
        categoriaENomeContainer.setComponentAlignment(hierarchyCombo, Alignment.BOTTOM_CENTER);

        grid.addComponent(companyCombo);
        grid.addComponent(categoriaENomeContainer, 1, 0, 2, 0);
        grid.addComponent(startDateDateField);
        grid.addComponent(recurrencyButton);
        grid.setComponentAlignment(recurrencyButton, Alignment.BOTTOM_CENTER);
        grid.addComponent(priorityCombo);
        grid.addComponent(endDateDateField);
        grid.addComponent(taskStatusPopUpButton);
        grid.setComponentAlignment(taskStatusPopUpButton, Alignment.BOTTOM_CENTER);
        grid.addComponent(new Label()); // leave it empty
        grid.addComponent(recurrencyMessage, 0, 3, 2, 3);

        return grid;
    }

    /**
     * Constructs and returns to the top button bar
     *
     * @return
     */
    private Component buildBarraBotoesSuperior() {

        buttonsSuperiorBar = new HorizontalLayout();
        buttonsSuperiorBar.setSpacing(true);
        buttonsSuperiorBar.setSizeUndefined();

        templateCheckBox = new CheckBox(messages.getString("TaskView.templateCheckBox.caption"));

        buttonsSuperiorBar.addComponent(templateCheckBox);

        pointingHoursCheckBox = new CheckBox(messages.getString("TaskView.apontamentoHorasCheckBox.caption"));
        pointingHoursCheckBox.addValueChangeListener((Property.ValueChangeEvent event) -> {
            listener.apontamentoHorasSwitched(event);
        });

        buttonsSuperiorBar.addComponent(pointingHoursCheckBox);

        budgetControlCheckBox = new CheckBox(messages.getString("TaskView.orcamentoControladoCheckBox.caption"));
        budgetControlCheckBox.addValueChangeListener((Property.ValueChangeEvent event) -> {
            listener.controleOrcamentoSwitched(event);
        });

        buttonsSuperiorBar.addComponent(budgetControlCheckBox);

        addSubButton = new Button("[Add Sub]", (Button.ClickEvent event) -> {
            listener.addSubButtonClicked();
        });
        buttonsSuperiorBar.addComponent(addSubButton);

        chatButton = new Button("[Chat]", (Button.ClickEvent event) -> {
            listener.chatButtonClicked();
        });
        buttonsSuperiorBar.addComponent(chatButton);

//Projeção será feita para a V2        
//        projectionButton = new Button("[Projeção]", (Button.ClickEvent event) -> {
//            listener.projecaoButtonClicked();
//        });
//        buttonsSuperiorBar.addComponent(projectionButton);

        return buttonsSuperiorBar;
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

        saveButton = new Button(messages.getString("TaskView.gravarButton.caption"), (Button.ClickEvent event) -> {
            try {
                setValidatorsVisible(true);
                taskFieldGroup.commit();
                listener.gravarButtonClicked();
            } catch (Exception ex) {
                ErrorUtils.showComponentErrors(this.taskFieldGroup.getFields());
                Logger.getLogger(TaskView.class.getName()).log(Level.WARNING, null, ex);
            }
        });

        lowerButtonsBar.addComponent(saveButton);

        cancelButton = new Button(messages.getString("TaskView.cancelarButton.caption"), (Button.ClickEvent event) -> {
            listener.cancelarButtonClicked();
        });
        lowerButtonsBar.addComponent(cancelButton);

        return lowerButtonsBar;
    }

    /**
     * Constructs and returns the description tab and responsible
     *
     * @return
     */
    private Component buildDescriptionAndAssigneeSheet() {

        taskDescriptionTextArea = new RichTextArea(messages.getString("TaskView.descricaoTarefaTextArea.caption"));
        taskDescriptionTextArea.setNullRepresentation("");

        assigneeUserCombo = new ComboBox(messages.getString("TaskView.responsavelCombo.label"));
        assigneeUserCombo.addValueChangeListener((Property.ValueChangeEvent event) -> {
           listener.assigneeUserChanged(getTarefa(), (Usuario) event.getProperty().getValue());
        });

        requiredFields.add(assigneeUserCombo);

        followersCombo = new ComboBox(messages.getString("TaskView.participantesCombo.label"));

        followersCombo.addValueChangeListener((Property.ValueChangeEvent event) -> {
            listener.adicionarParticipante((Usuario) event.getProperty().getValue());
        });

        followersContainer = new BeanItemContainer<>(Participante.class);

        followersTable = new Table() {
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

        followersTable.setContainerDataSource(followersContainer);

        followersTable.setColumnWidth("usuarioParticipante", 120);
        followersTable.setColumnHeader("usuarioParticipante", messages.getString("TaskView.participantesTable.colunaParticipante"));

        followersTable.setVisibleColumns("usuarioParticipante");

        // Adicionar coluna do botão "remover"
        followersTable.addGeneratedColumn(messages.getString("TaskView.participantesTable.colunaBotaoRemover"), (Table source, final Object itemId, Object columnId) -> {
            Button removeButton = new Button(messages.getString("TaskView.participantesTable.colunaBotaoRemover"));
            removeButton.addClickListener((ClickEvent event) -> {
                listener.removerParticipante((Participante) itemId);
            });

            removeButton.setEnabled(editAllowed);
            return removeButton;
        });

        followersTable.setSelectable(true);
        followersTable.setImmediate(true);
        followersTable.setWidth("100%");
        followersTable.setPageLength(3);

        Label labelCliente = new Label("<b>" + messages.getString("TaskView.empresaClienteCombo.label") + "</b>", ContentMode.HTML);
        customerCompanyCombo = new ComboBox();

        // do layout :
        GridLayout layout = new GridLayout(3, 3);
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.setWidth("100%");
        layout.setHeight(null);

        layout.addComponent(assigneeUserCombo, 0, 0);
        layout.addComponent(followersCombo, 1, 0);
        layout.addComponent(followersTable, 0, 1, 1, 1);
        layout.addComponent(labelCliente, 0, 2);
        layout.addComponent(customerCompanyCombo, 1, 2);
        layout.addComponent(taskDescriptionTextArea, 2, 0, 2, 2);

        return layout;
    }

    /**
     * builds and returns the Details tab
     *
     * @return
     */
    private Component buildDetailsSheet() {

        // department Combo
        departamentCombo = new ComboBox(messages.getString("TaskView.departamentoCombo.caption"));

        // Cost Center combo
        costCenterCombo = new ComboBox(messages.getString("TaskView.centroCustoCombo.caption"));

        // upload Progress Bar
        uploadHorizontalLayout = new HorizontalLayout();
        uploadHorizontalLayout.setWidth("100%");

        attachProgressBar = new ProgressBar();
        attachProgressBar.setWidth("100%");

        addAttach = new Upload(messages.getString("TaskView.adicionarAnexoButton.filechooser.caption"), (String filename, String mimeType) -> {
            FileOutputStream fos = null;
            try {
                File randomFolder = new File(System.getProperty("user.dir") + "/tmp/" + String.valueOf(Math.abs(new Random().nextInt())));
                randomFolder.mkdirs();

                File file = new File(randomFolder, filename);
                fos = new FileOutputStream(file);

                addAttach.setData(file);

            } catch (FileNotFoundException e) {
                Notification.show(e.getMessage(), Notification.Type.WARNING_MESSAGE);
                return null;
            }
            return fos;
        });
        addAttach.setButtonCaption(messages.getString("TaskView.adicionarAnexoButton.caption"));

        addAttach.addSucceededListener((Upload.SucceededEvent event) -> {
            listener.anexoAdicionado((File) event.getUpload().getData());
        });
        addAttach.addProgressListener((long readBytes, long contentLength) -> {
            UI.getCurrent().access(() -> {
                float newValue = readBytes / (float) contentLength;
                attachProgressBar.setValue(newValue);
            });
        });
        addAttach.addFinishedListener((Upload.FinishedEvent event) -> {
            uploadHorizontalLayout.removeComponent(attachProgressBar);
            Notification.show(messages.getString("TaskView.adicionarAnexoButton.uploadConcluido.mensagem"), Notification.Type.TRAY_NOTIFICATION);
        });
        addAttach.addStartedListener((Upload.StartedEvent event) -> {
            uploadHorizontalLayout.addComponent(attachProgressBar);
        });

        uploadHorizontalLayout.addComponent(addAttach);

        taskAttachContainer = new BeanItemContainer<>(AnexoTarefa.class);

        attachmentsAddedTable = new Table();
        attachmentsAddedTable.setContainerDataSource(taskAttachContainer);

        attachmentsAddedTable.setColumnWidth("nome", 350);

        attachmentsAddedTable.setColumnHeader("nome", messages.getString("TaskView.anexosAdicionadosTable.colunaNome"));

        attachmentsAddedTable.setVisibleColumns("nome");

        // Adicionar coluna do botão "download"
        attachmentsAddedTable.addGeneratedColumn(messages.getString("TaskView.anexosAdicionadosTable.colunaBotaoDownload"), (Table source, final Object itemId, Object columnId) -> {
            Button downloadButton = new Button(messages.getString("TaskView.anexosAdicionadosTable.colunaBotaoDownload"));
            AnexoTarefa anexoTarefa = (AnexoTarefa) itemId;
            FileDownloader fd = new FileDownloader(new FileResource(new File(anexoTarefa.getCaminhoCompleto())));

            fd.extend(downloadButton);
           downloadButton.setEnabled(true);
            return downloadButton;
        });
        attachmentsAddedTable.setColumnWidth(messages.getString("TaskView.anexosAdicionadosTable.colunaBotaoDownload"), 50);

        attachmentsAddedTable.addGeneratedColumn(messages.getString("TaskView.anexosAdicionadosTable.colunaBotaoRemover"), (Table source, final Object itemId, Object columnId) -> {
            Button removeButton = new Button(messages.getString("TaskView.anexosAdicionadosTable.colunaBotaoRemover"));
            removeButton.addClickListener((ClickEvent event) -> {
                listener.removerAnexo((AnexoTarefa) itemId);
            });
           removeButton.setEnabled(true);
            return removeButton;
        });
        attachmentsAddedTable.setColumnWidth(messages.getString("TaskView.anexosAdicionadosTable.colunaBotaoRemover"), 50);
        attachmentsAddedTable.setSelectable(true);
        attachmentsAddedTable.setImmediate(true);
        attachmentsAddedTable.setWidth("100%");
        attachmentsAddedTable.setHeight("150px");
        attachmentsAddedTable.setPageLength(3);

        GridLayout layout = new GridLayout(2, 2);
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.setWidth("100%");
        layout.setHeight(null);

        layout.addComponent(departamentCombo, 0, 0);
        layout.addComponent(costCenterCombo, 0, 1);
        layout.addComponent(uploadHorizontalLayout, 1, 0);
        layout.addComponent(attachmentsAddedTable, 1, 1);

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

        taskAppointmentBeanItem = new BeanItem<>(taskAppointment);
        taskAppointmentFieldGroup = new FieldGroup(taskAppointmentBeanItem);

        taskAppointmentFieldGroup.bind(hourCostTextField, "custoHora");
        taskAppointmentFieldGroup.bind(hoursAddTextField, "inputHoras");
        taskAppointmentFieldGroup.bind(hoursObservationTextField, "observacoes");

    }

    /**
     * Get the task of pointing (hours) on (binding) to form
     *
     * @return
     * @throws com.vaadin.data.fieldgroup.FieldGroup.CommitException
     */
    public ApontamentoTarefa getApontamentoTarefa() throws FieldGroup.CommitException {
        taskAppointmentFieldGroup.commit();
        return taskAppointmentBeanItem.getBean();
    }

    /**
     * Constructs and returns the hour control tab
     *
     * @return
     */
    private Component buildPointingHoursSheet() {

        // Hours control fields
        hourCostTextField = new TextField();
        hourCostTextField.setInputPrompt(messages.getString("TaskView.custoHoraTextField.inputPrompt"));
        hourCostTextField.setNullRepresentation("");
        hourCostTextField.setConverter(new StringToBigDecimalConverter());

        hoursAddTextField = new TextField();
        hoursAddTextField.setInputPrompt(messages.getString("TaskView.imputarHorasTextField.inputPrompt"));
        hoursAddTextField.setNullRepresentation("");

        hoursObservationTextField = new TextField();
        hoursObservationTextField.setInputPrompt(messages.getString("TaskView.observacaoHorasTextField.inputPrompt"));
        hoursObservationTextField.setNullRepresentation("");

        hoursAddButton = new Button(messages.getString("TaskView.imputarHorasButton.caption"));
        hoursAddButton.addClickListener((Button.ClickEvent event) -> {
            listener.imputarHorasClicked();
        });

        hoursControlContainer = new BeanItemContainer<>(ApontamentoTarefa.class);

        pointingTimeTable = new Table() {
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

        pointingTimeTable.setContainerDataSource(hoursControlContainer);

        pointingTimeTable.setColumnWidth("dataHoraInclusao", 150);
        pointingTimeTable.setColumnHeader("dataHoraInclusao", messages.getString("TaskView.controleHorasTable.colunaData"));
        pointingTimeTable.setColumnWidth("observacoes", 150);
        pointingTimeTable.setColumnHeader("observacoes", messages.getString("TaskView.controleHorasTable.colunaObservacoes"));
        pointingTimeTable.setColumnWidth("creditoHoras", 80);
        pointingTimeTable.setColumnHeader("creditoHoras", messages.getString("TaskView.controleHorasTable.colunaCreditoHoras"));
        pointingTimeTable.setColumnWidth("debitoHoras", 80);
        pointingTimeTable.setColumnHeader("debitoHoras", messages.getString("TaskView.controleHorasTable.colunaDebitoHoras"));
        pointingTimeTable.setColumnWidth("saldoHoras", 80);
        pointingTimeTable.setColumnHeader("saldoHoras", messages.getString("TaskView.controleHorasTable.colunaSaldoHoras"));
        pointingTimeTable.setColumnWidth("creditoValor", 80);
        pointingTimeTable.setColumnAlignment("creditoValor", Table.Align.RIGHT);
        pointingTimeTable.setColumnHeader("creditoValor", messages.getString("TaskView.controleHorasTable.colunaCreditoValor"));
        pointingTimeTable.setColumnWidth("debitoValor", 80);
        pointingTimeTable.setColumnAlignment("debitoValor", Table.Align.RIGHT);
        pointingTimeTable.setColumnHeader("debitoValor", messages.getString("TaskView.controleHorasTable.colunaDebitoValor"));
        pointingTimeTable.setColumnWidth("saldoValor", 80);
        pointingTimeTable.setColumnAlignment("saldoValor", Table.Align.RIGHT);
        pointingTimeTable.setColumnHeader("saldoValor", messages.getString("TaskView.controleHorasTable.colunaSaldoValor"));
        pointingTimeTable.setColumnAlignment("custoHora", Table.Align.RIGHT);
        pointingTimeTable.setColumnHeader("custoHora", messages.getString("TaskView.controleHorasTable.colunaCustoHora"));
        pointingTimeTable.setColumnWidth("custoHora", 80);

        pointingTimeTable.setVisibleColumns("dataHoraInclusao", "observacoes", "creditoHoras", "debitoHoras", "saldoHoras", "creditoValor", "debitoValor", "saldoValor","custoHora");
        // Adicionar coluna do botão "remover"
        pointingTimeTable.addGeneratedColumn("Remove", (Table source, final Object itemId, Object columnId) -> {
            Button removeButton = new Button("x");
            removeButton.addClickListener((ClickEvent event) -> {
                listener.removePointingTime((ApontamentoTarefa) itemId);
            });
           removeButton.setEnabled(editAllowed);
            return removeButton;
        });

        pointingTimeTable.setSelectable(true);
        pointingTimeTable.setImmediate(true);
        pointingTimeTable.setPageLength(5);
        pointingTimeTable.setWidth("100%");

        HorizontalLayout controleHorasLayout = new HorizontalLayout();
        controleHorasLayout.setSpacing(true);
        controleHorasLayout.addComponent(hourCostTextField);
        controleHorasLayout.addComponent(hoursAddTextField);
        controleHorasLayout.addComponent(hoursObservationTextField);
        controleHorasLayout.addComponent(hoursAddButton);

        hoursControlTab = new VerticalLayout();
        hoursControlTab.setSpacing(true);
        hoursControlTab.setMargin(true);
        hoursControlTab.setWidth("100%");
        hoursControlTab.setHeight(null);

        hoursControlTab.addComponent(controleHorasLayout);
        hoursControlTab.addComponent(pointingTimeTable);

        return hoursControlTab;
    }

    /**
     * Bind (on) a task budget record to form
     *
     * @param taskBudget
     */
    public void setOrcamentoTarefa(OrcamentoTarefa taskBudget) {

        taskBudgetBeanItem = new BeanItem<>(taskBudget);
        tableBudgetFieldGroup = new FieldGroup(taskBudgetBeanItem);

        tableBudgetFieldGroup.bind(budgetAddTextField, "inputValor");
        tableBudgetFieldGroup.bind(observationBudgetTextField, "observacoes");

    }

    /**
     * Get the budget control on (binding) to form
     *
     * @return
     * @throws com.vaadin.data.fieldgroup.FieldGroup.CommitException
     */
    public OrcamentoTarefa getOrcamentoTarefa() throws FieldGroup.CommitException {
        tableBudgetFieldGroup.commit();
        return taskBudgetBeanItem.getBean();
    }

    /**
     * Constructs and returns the budget control tab
     *
     * @return
     */
    private Component buildBudgetSheet() {

        budgetAddTextField = new TextField();
        budgetAddTextField.setInputPrompt(messages.getString("TaskView.imputarOrcamentoTextField.inputPrompt"));
        budgetAddTextField.setNullRepresentation("");
        budgetAddTextField.setConverter(new StringToBigDecimalConverter());

        observationBudgetTextField = new TextField();
        observationBudgetTextField.setInputPrompt(messages.getString("TaskView.observacaoOrcamentoTextField.inputPrompt"));
        observationBudgetTextField.setNullRepresentation("");

        budgetAddButton = new Button(messages.getString("TaskView.imputarOrcamentoButton.caption"), (Button.ClickEvent event) -> {
            listener.imputarOrcamentoClicked();
        });

        budgetContainer = new BeanItemContainer<>(OrcamentoTarefa.class);

        budgetControlTable = new Table() {
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
//            @Override
//            protected String formatPropertyValue(Object rowId,
//                    Object colId, Property property) {
//
//                // Format by property type
//                if (property.getType() == LocalDateTime.class) {
//                    return ((LocalDateTime) property.getValue()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
//                }
//
//                return super.formatPropertyValue(rowId, colId, property);
//            }
        };
        budgetControlTable.setContainerDataSource(budgetContainer);
        budgetControlTable.setColumnWidth("dataHoraInclusao", 150);
        budgetControlTable.setColumnHeader("dataHoraInclusao", messages.getString("TaskView.controleOrcamentoTable.colunaData"));
        budgetControlTable.setColumnWidth("observacoes", 150);
        budgetControlTable.setColumnHeader("observacoes", messages.getString("TaskView.controleOrcamentoTable.colunaObservacoes"));
        budgetControlTable.setColumnWidth("credito", 80);
        budgetControlTable.setColumnHeader("credito", messages.getString("TaskView.controleOrcamentoTable.colunaCredito"));
        budgetControlTable.setColumnAlignment("credito", Table.Align.RIGHT);
        budgetControlTable.setColumnWidth("debito", 80);
        budgetControlTable.setColumnHeader("debito", messages.getString("TaskView.controleOrcamentoTable.colunaDebito"));
        budgetControlTable.setColumnAlignment("debito", Table.Align.RIGHT);
        budgetControlTable.setColumnWidth("saldo", 80);
        budgetControlTable.setColumnHeader("saldo", messages.getString("TaskView.controleOrcamentoTable.colunaSaldo"));
        budgetControlTable.setColumnAlignment("saldo", Table.Align.RIGHT);

        budgetControlTable.setVisibleColumns("dataHoraInclusao", "credito", "debito", "saldo", "observacoes");

        budgetControlTable.addGeneratedColumn(messages.getString("TaskView.controleOrcamentoTable.colunaBotaoRemover"), (Table source, final Object itemId, Object columnId) -> {
            Button removeButton = new Button(messages.getString("TaskView.controleOrcamentoTable.colunaBotaoRemover"));
            removeButton.addClickListener((ClickEvent event) -> {
                listener.removerRegistroOrcamento((OrcamentoTarefa) itemId);
            });
            removeButton.setEnabled(editAllowed);
            return removeButton;
        });

        budgetControlTable.setSelectable(true);
        budgetControlTable.setImmediate(true);
        budgetControlTable.setPageLength(5);
        budgetControlTable.setWidth("100%");

        HorizontalLayout orcamentoLayout = new HorizontalLayout();
        orcamentoLayout.setSpacing(true);
        orcamentoLayout.addComponent(budgetAddTextField);
        orcamentoLayout.addComponent(observationBudgetTextField);
        orcamentoLayout.addComponent(budgetAddButton);

        budgetControlTab = new VerticalLayout();
        budgetControlTab.setSpacing(true);
        budgetControlTab.setMargin(true);
        budgetControlTab.setWidth("100%");
        budgetControlTab.setHeight(null);

        budgetControlTab.addComponent(orcamentoLayout);
        budgetControlTab.addComponent(budgetControlTable);

        return budgetControlTab;
    }

    /**
     * Hides / shows the time control tab
     *
     * @param visible
     */
    public void setAbaControleHorasVisible(boolean visible) {

        TabSheet.Tab tab = accordion.getTab(hoursControlTab);
        tab.setVisible(visible);

    }

    /**
     * Hides / shows the budget control tab
     *
     * @param visible
     */
    public void setAbaControleOrcamentoVisible(boolean visible) {

        TabSheet.Tab tab = accordion.getTab(budgetControlTab);
        tab.setVisible(visible);

    }

    /**
     *
     * @return
     */
    private Component buildSubTasksSheet() {

        subTasksTable = new TreeTable();
        subTasksTable.setWidth("100%");
        subTasksTable.addContainerProperty(messages.getString("TaskView.subTarefasTable.colunaCod"), Button.class, "");
        subTasksTable.setColumnWidth(messages.getString("TaskView.subTarefasTable.colunaCod"), 70);
        subTasksTable.addContainerProperty(messages.getString("TaskView.subTarefasTable.colunaTitulo"), Button.class, "");
        subTasksTable.setColumnWidth(messages.getString("TaskView.subTarefasTable.colunaTitulo"), 50);
        subTasksTable.addContainerProperty(messages.getString("TaskView.subTarefasTable.colunaNome"), Button.class, "");
        subTasksTable.setColumnWidth(messages.getString("TaskView.subTarefasTable.colunaNome"), 250);
        subTasksTable.addContainerProperty(messages.getString("TaskView.subTarefasTable.colunaEmpresaFilial"), String.class, "");
        subTasksTable.setColumnWidth(messages.getString("TaskView.subTarefasTable.colunaEmpresaFilial"), 200);
        subTasksTable.addContainerProperty(messages.getString("TaskView.subTarefasTable.colunaSolicitante"), String.class, "");
        subTasksTable.setColumnWidth(messages.getString("TaskView.subTarefasTable.colunaSolicitante"), 80);
        subTasksTable.addContainerProperty(messages.getString("TaskView.subTarefasTable.colunaResponsavel"), String.class, "");
        subTasksTable.setColumnWidth(messages.getString("TaskView.subTarefasTable.colunaResponsavel"), 80);
        subTasksTable.addContainerProperty(messages.getString("TaskView.subTarefasTable.colunaDataInicio"), String.class, "");
        subTasksTable.setColumnWidth(messages.getString("TaskView.subTarefasTable.colunaDataInicio"), 80);
        subTasksTable.addContainerProperty(messages.getString("TaskView.subTarefasTable.colunaDataFim"), String.class, "");
        subTasksTable.setColumnWidth(messages.getString("TaskView.subTarefasTable.colunaDataFim"), 80);
        subTasksTable.addContainerProperty(messages.getString("TaskView.subTarefasTable.colunaStatus"), PopupButton.class, "");
        subTasksTable.setColumnWidth(messages.getString("TaskView.subTarefasTable.colunaStatus"), 200);
        subTasksTable.addContainerProperty(messages.getString("TaskView.subTarefasTable.colunaProjecao"), Character.class, "");
        subTasksTable.setColumnWidth(messages.getString("TaskView.subTarefasTable.colunaProjecao"), 30);
        subTasksTable.addContainerProperty("[E]", Button.class, "");
        subTasksTable.setColumnWidth("[E]", 30);
        subTasksTable.addContainerProperty("[C]", Button.class, "");
        subTasksTable.setColumnWidth("[C]", 30);

        subTasksTable.setPageLength(4);
        subTasksTable.setSelectable(true);
        subTasksTable.setImmediate(true);

        return subTasksTable;
    }

    /**
     * @return the messages
     */
    public ResourceBundle getMessages() {
        return messages;
    }

    /**
     * @return the images
     */
    public GestorWebImagens getImages() {
        return images;
    }

    /**
     * @return the listener
     */
    public TaskViewListener getListener() {
        return listener;
    }

    /**
     * @return the taskNameTextField
     */
    public TextField getTaskNameTextField() {
        return taskNameTextField;
    }

    /**
     * @return the companyCombo
     */
    public ComboBox getCompanyCombo() {
        return companyCombo;
    }

    /**
     * @return the buttonsSuperiorBar
     */
    public HorizontalLayout getButtonsSuperiorBar() {
        return buttonsSuperiorBar;
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
     * @return the projectionButton
     */
//    public Button getProjectionButton() {
//        return projectionButton;
//    }

    /**
     * @return the startDateDateField
     */
    public PopupDateField getStartDateDateField() {
        return startDateDateField;
    }

    /**
     * @return the endDateDateField
     */
    public PopupDateField getEndDateDateField() {
        return endDateDateField;
    }

    public Button getRecurrencyButton() {
        return recurrencyButton;
    }

    public void setRecurrencyButton(Button recurrencyButton) {
        this.recurrencyButton = recurrencyButton;
    }

    /**
     * @return the priorityCombo
     */
    public ComboBox getPriorityCombo() {
        return priorityCombo;
    }

    /**
     * @return the assigneeUserCombo
     */
    public ComboBox getAssigneeUserCombo() {
        return assigneeUserCombo;
    }

    /**
     * @return the followersCombo
     */
    public ComboBox getFollowersCombo() {
        return followersCombo;
    }

    /**
     * @return the customerCompanyCombo
     */
    public ComboBox getCustomerCompanyCombo() {
        return customerCompanyCombo;
    }

    /**
     * @return the taskDescriptionTextArea
     */
    public RichTextArea getTaskDescriptionTextArea() {
        return taskDescriptionTextArea;
    }

    /**
     * @return the departamentCombo
     */
    public ComboBox getDepartamentCombo() {
        return departamentCombo;
    }

    /**
     * @return the costCenterCombo
     */
    public ComboBox getCostCenterCombo() {
        return costCenterCombo;
    }

    /**
     * @return the addAttach
     */
    public Upload getAddAttach() {
        return addAttach;
    }

    /**
     * @return the hourCostTextField
     */
    public TextField getHourCostTextField() {
        return hourCostTextField;
    }

    /**
     * @return the hoursAddTextField
     */
    public TextField getHoursAddTextField() {
        return hoursAddTextField;
    }

    /**
     * @return the hoursObservationTextField
     */
    public TextField getHoursObservationTextField() {
        return hoursObservationTextField;
    }

    /**
     * @return the hoursAddButton
     */
    public Button getHoursAddButton() {
        return hoursAddButton;
    }

    /**
     * @return the budgetAddTextField
     */
    public TextField getBudgetAddTextField() {
        return budgetAddTextField;
    }

    /**
     * @return the observationBudgetTextField
     */
    public TextField getObservationBudgetTextField() {
        return observationBudgetTextField;
    }

    /**
     * @return the budgetAddButton
     */
    public Button getBudgetAddButton() {
        return budgetAddButton;
    }

    /**
     * @return the saveButton
     */
    public Button getSaveButton() {
        return saveButton;
    }

    /**
     * @return the cancelButton
     */
    public Button getCancelButton() {
        return cancelButton;
    }

    /**
     * @return the subTasksTable
     */
    public TreeTable getSubTasksTable() {
        return subTasksTable;
    }

    /**
     * @return the taskStatusPopUpButton
     */
    public PopupButton getTaskStatusPopUpButton() {
        return taskStatusPopUpButton;
    }

    /**
     * @return the accordion
     */
    public Accordion getAccordion() {
        return accordion;
    }

    /**
     * @return the hoursControlTab
     */
    public VerticalLayout getHoursControlTab() {
        return hoursControlTab;
    }

    /**
     * @return the budgetControlTab
     */
    public VerticalLayout getBudgetControlTab() {
        return budgetControlTab;
    }

    public void ocultaPopUpEvolucaoStatusEAndamento() {
        taskStatusPopUpButton.setVisible(false);
    }

    private String getCaminhoTarefa(Tarefa tarefa) {
        if (tarefa.getTarefaPai() != null) {
            return getCaminhoTarefa(tarefa.getTarefaPai()) + " >> " + tarefa.getNome() == null ? "[NOVA]" : tarefa.getNome();
        } else {
            return tarefa.getNome() == null ? "[NOVA]" : tarefa.getNome();
        }
    }

    public BeanItemContainer<ApontamentoTarefa> getHoursControlContainer() {
        return hoursControlContainer;
    }

    public BeanItemContainer<Participante> getFollowersContainer() {
        return followersContainer;
    }

    public BeanItemContainer<OrcamentoTarefa> getBudgetContainer() {
        return budgetContainer;
    }

    public Table getPointingTimeTable() {
        return pointingTimeTable;
    }

    public Table getFollowersTable() {
        return followersTable;
    }

    public BeanItemContainer<AnexoTarefa> getTaskAttachContainer() {
        return taskAttachContainer;
    }

    public Table getAttachmentsAddedTable() {
        return attachmentsAddedTable;
    }

    public Table getBudgetControlTable() {
        return budgetControlTable;
    }

    public CheckBox getBudgetControlCheckBox() {
        return budgetControlCheckBox;
    }

    public CheckBox getApontamentoHorasCheckBox() {
        return pointingHoursCheckBox;
    }

    public void apresentarCorFundoSubTarefa() {
        addStyleName("subtarefa");
    }

    public Label getPathTaskLabel() {
        return pathTaskLabel;
    }

    public FieldGroup getTaskFieldGroup() {
        return taskFieldGroup;
    }

    public ComboBox getHierarchyCombo() {
        return hierarchyCombo;
    }

    /**
     * Oculta/Exibe o status da tarefa
     *
     * @param visible
     */
    public void setStatusVisible(boolean visible) {
        taskStatusPopUpButton.setVisible(visible);
    }

    /**
     * Static method that builds a link button, that when clicked open a window
     * with the given task to edit
     *
     * @param callback the callback listener that must be called when the update
     * were done
     * @param table the view table (used to auto select the row)
     * @param task the task that will be openned
     * @param caption the button caption
     * @return
     */
    public static Button buildButtonOpenTask(TarefaCallBackListener callback, Table table, Tarefa task, String caption) {
        Button link = new Button(caption);
        link.setStyleName("quiet");
        link.addClickListener((Button.ClickEvent event) -> {
            table.setValue(task);
            TarefaPresenter presenter = new TarefaPresenter(new TarefaModel(), new TaskView());
            presenter.setCallBackListener(callback);
            presenter.editar(task);
        });
        return link;
    }

    /**
     * Builds a pop up painel to in which the user can set the status and/or the
     * progress of the task
     *
     * @param task the task tha will have the status/progress updated
     * @param table the view table (used to auto select the row)
     * @param listener
     * @return a popup button
     */
    public static PopupButton buildPopUpStatusProgressTask(Table table, Tarefa task, PopUpStatusListener listener) {

        PopUpStatusView viewPopUP = new PopUpStatusView();
        PopUpStatusModel modelPopUP = new PopUpStatusModel();

        PopUpStatusPresenter presenter = new PopUpStatusPresenter(viewPopUP, modelPopUP);

        presenter.load(task, null, listener);

        // Event fired when the pop-up becomes visible:
        presenter.getStatusButton().addPopupVisibilityListener((PopupButton.PopupVisibilityEvent event) -> {
            if (event.isPopupVisible()) {
                Tarefa tarefaEditada = (Tarefa) event.getPopupButton().getData();
                table.setValue(tarefaEditada);
            }
        });

        return presenter.getStatusButton();
    }

    public void setEditAllowed(boolean editAllowed) {

        this.editAllowed = editAllowed;

        // Header:
        templateCheckBox.setEnabled(editAllowed);
        pointingHoursCheckBox.setEnabled(editAllowed);
        budgetControlCheckBox.setEnabled(editAllowed);
        addSubButton.setEnabled(editAllowed);

        // Basic Data:
        companyCombo.setEnabled(editAllowed);
        hierarchyCombo.setEnabled(editAllowed);
        taskNameTextField.setEnabled(editAllowed);
        startDateDateField.setEnabled(editAllowed);
        endDateDateField.setEnabled(editAllowed);
        recurrencyButton.setEnabled(editAllowed);
        priorityCombo.setEnabled(editAllowed);

        // Description tab of components and Responsible
        assigneeUserCombo.setEnabled(editAllowed);
        taskDescriptionTextArea.setEnabled(editAllowed);
        followersCombo.setEnabled(editAllowed);
        customerCompanyCombo.setEnabled(editAllowed);

        // Componentes da Aba Detalhes
        departamentCombo.setEnabled(editAllowed);
        costCenterCombo.setEnabled(editAllowed);
        addAttach.setEnabled(true);
        attachmentsAddedTable.setEnabled(true);
        
        

        // Tab Hours Control Components
        hourCostTextField.setEnabled(editAllowed);
        hoursAddTextField.setEnabled(editAllowed);
        hoursObservationTextField.setEnabled(editAllowed);
        hoursAddButton.setEnabled(editAllowed);

        // Components of the budget tab
        budgetAddTextField.setEnabled(editAllowed);
        observationBudgetTextField.setEnabled(editAllowed);
        budgetAddButton.setEnabled(editAllowed);

    }

    public Label getRecurrencyMessage() {
        return recurrencyMessage;
    }

    
    
    
    
}
