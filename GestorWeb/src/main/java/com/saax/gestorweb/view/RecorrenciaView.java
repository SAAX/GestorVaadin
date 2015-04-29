package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.util.FormatterUtil;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.view.converter.DateToLocalDateConverter;
import com.saax.gestorweb.view.validator.DataFimValidator;
import com.saax.gestorweb.view.validator.DataInicioValidator;
import com.vaadin.data.Property;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import org.vaadin.dialogs.ConfirmDialog;

/**
 * Pop-up Window for recurrence Viewing will be three check-box to choose the
 * type of recurrence:
 * <br>
 * <ol>
 * <li>weekly</li>
 * <li>monthly</li>
 * <li>annual</li>
 * </ol>
 *
 *
 * @author fernando
 */
public class RecorrenciaView extends Window {

// Reference to the use of messages:
    private final transient ResourceBundle messages = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final transient GestorWebImagens images = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();

// The view maintains access to the listener (Presenter) to notify events
// This access is through an interface to maintain the abstraction layers
    private RecorrenciaViewListener listener;

    private Accordion accordion;

    private Label titleLabel;
    private HorizontalLayout superiorBar;

// -------------------------------------------------------------------------
// Components ref. The recurrence options
// -------------------------------------------------------------------------     
    private CheckBox weeklyCheckBox;
    private CheckBox monthlyCheckBox;
    private CheckBox annualCheckBox;

// -------------------------------------------------------------------------
// Components ref. the weekly recurrence 
// -------------------------------------------------------------------------   
    private VerticalLayout weeklyTab;
    private CheckBox mondayCheckBox;
    private CheckBox tuesdayCheckBox;
    private CheckBox wednesdayCheckBox;
    private CheckBox thursdayCheckBox;
    private CheckBox fridayCheckBox;
    private CheckBox saturdayCheckBox;
    private CheckBox sundayCheckBox;
    private ComboBox numberWeeksCombo;
    private PopupDateField startDateWeeklyDateField;
    private PopupDateField endDateWeeklyDateField;

// -------------------------------------------------------------------------
// Components ref. the monthly recurrence
// -------------------------------------------------------------------------  
    private GridLayout monthlyTab;
    private ComboBox daysMonthlyCombo;
    private ComboBox numberMonthsCombo;
    private ComboBox kindDayMonthlyCombo;
    private PopupDateField startDateMonthlyDateField;
    private PopupDateField endDateMonthlyDateField;

// -------------------------------------------------------------------------
// Components ref. Annual recurrence
// -------------------------------------------------------------------------  
    private VerticalLayout annualTab;
    private ComboBox dayAnnualCombo;
    private ComboBox kindDayAnnualCombo;
    private ComboBox monthAnnualCombo;
    private ComboBox yearAnnualCombo;

// -------------------------------------------------------------------------
// Buttons bar below
// -------------------------------------------------------------------------
    private Button okButton;
    private Button cancelButton;

    /**
     * Lock acesso to check boxes, alowing only on event. Prevent one event to
     * throw other
     */
    private boolean locked;
    private Button removeAllRecurrentTasksButton;
    private Button removeAllNextRecurrentTasksButton;

    /**
     * Sets the check boxes locked/unlocked Must be synchronized
     *
     * @param locked
     */
    public synchronized void setLocked(boolean locked) {
        this.locked = locked;
    }

    /**
     *
     * @return the lock state (lock/unlock)
     */
    public synchronized boolean isLocked() {
        return locked;
    }

    /**
     * Create a view and all components
     *
     * @param isRecurrent true if the task is already a recurrent task.
     */
    public RecorrenciaView(boolean isRecurrent) {
        super();

        setModal(true);
        setWidth("710px");
        setHeight("220px");

        // Container main, which will store all other containers
        VerticalLayout containerPrincipal = null;
        
        if (isRecurrent){
            containerPrincipal = buildRemoveRecurrencyContainer();
            
        } else {
            containerPrincipal = buildRecurrencyParametersContainer();
            
        }
        
        containerPrincipal.setSpacing(true);
        setContent(containerPrincipal);

        center();

    }

    /**
     * Builds the main container that will hold all other
     * Its the main container to remove (reset) the recurrency.
     * It is called when the task is already a recurrent task
     * @return
     */
    private VerticalLayout buildRemoveRecurrencyContainer() {

        VerticalLayout containerPrincipal = new VerticalLayout();
        containerPrincipal.setMargin(true);
        containerPrincipal.setSpacing(true);
        containerPrincipal.setSizeFull();

        titleLabel = new Label("Esta é uma tarefa recorrente:");
        containerPrincipal.addComponent(titleLabel);

        // options
        removeAllRecurrentTasksButton = new Button("Redefinir a recorrencia (remover todas as tarefas)", (Button.ClickEvent event) -> {
            listener.removeAllRecurrency();
        });
        
        removeAllNextRecurrentTasksButton = new Button("Remover todas as tarefas seguintes", (Button.ClickEvent event) -> {
            listener.removeAllNextRecurrency();
        });
        
        containerPrincipal.addComponent(removeAllRecurrentTasksButton);
        containerPrincipal.addComponent(removeAllNextRecurrentTasksButton);

        return containerPrincipal;

    }

    /**
     * Builds the main container that will hold all other
     * Its the main container to set the recurrency parameters
     * @return
     */
    private VerticalLayout buildRecurrencyParametersContainer() {

        VerticalLayout containerPrincipal = new VerticalLayout();
        containerPrincipal.setMargin(true);
        containerPrincipal.setSpacing(true);
        containerPrincipal.setSizeFull();

        titleLabel = new Label("Escolha o tipo de Recorrência");
        containerPrincipal.addComponent(titleLabel);

        containerPrincipal.addComponent(buildBarraSuperior());
        containerPrincipal.setComponentAlignment(superiorBar, Alignment.MIDDLE_RIGHT);

        // creates the accordion tabs and add tabs
        accordion = new Accordion();
        accordion.setWidth("100%");
        accordion.addTab(buildAbaSemanal(), messages.getString("RecorrenciaView.AbaRecorrenciaSemanal.titulo"), null);
        accordion.addTab(buildAbaMensal(), messages.getString("RecorrenciaView.AbaRecorrenciaMensal.titulo"), null);
        accordion.addTab(buildAbaAnual(), messages.getString("RecorrenciaView.AbaRecorrenciaAnual.titulo"), null);

        containerPrincipal.addComponent(accordion);
        containerPrincipal.setExpandRatio(accordion, 1);

        // adds the lower buttons bar with buttons to save and cancel
        Component barraInferior = buildBarraBotoesInferior();
        containerPrincipal.addComponent(barraInferior);
        containerPrincipal.setComponentAlignment(barraInferior, Alignment.MIDDLE_CENTER);

        setAbaMensalVisible(false);
        setAbaSemanalVisible(false);
        setAbaAnualVisible(false);
        setValidatorsVisible(false);
        
        return containerPrincipal;

    }

    /**
     * Constructs and returns to the top button bar
     *
     * @return
     */
    private Component buildBarraSuperior() {
        superiorBar = new HorizontalLayout();
        superiorBar.setSpacing(true);
        superiorBar.setSizeUndefined();

        weeklyCheckBox = new CheckBox(messages.getString("RecorrenciaView.semanalCheckBox.caption"));
        monthlyCheckBox = new CheckBox(messages.getString("RecorrenciaView.mensalCheckBox.caption"));
        annualCheckBox = new CheckBox(messages.getString("RecorrenciaView.anualCheckBox.caption"));

        superiorBar.addComponent(weeklyCheckBox);

        weeklyCheckBox.addValueChangeListener((Property.ValueChangeEvent event) -> {
            // only handle the event if it's not nocked
            if (!isLocked()) {
                setLocked(true);
                listener.recorrenciaSemanal(event);
                setLocked(false);
            }
        });

        superiorBar.addComponent(monthlyCheckBox);

        monthlyCheckBox.addValueChangeListener((Property.ValueChangeEvent event) -> {

            // only handle the event if it's not nocked
            if (!isLocked()) {
                setLocked(true);
                listener.recorrenciaMensal(event);
                setLocked(false);
            }
        });

        superiorBar.addComponent(annualCheckBox);

        annualCheckBox.addValueChangeListener((Property.ValueChangeEvent event) -> {
            // only handle the event if it's not nocked
            if (!isLocked()) {
                setLocked(true);
                listener.recorrenciaAnual(event);
                setLocked(false);

            }
        });

        return superiorBar;
    }

    /**
     * Constructs tab of recurrence Weekly:
     *
     * @return
     */
    private Component buildAbaSemanal() {

        AbstractValidator<Boolean> weekDayValidator = new AbstractValidator<Boolean>("Select the weekdays.") {

            @Override
            protected boolean isValidValue(Boolean value) {
                return mondayCheckBox.getValue()
                        || tuesdayCheckBox.getValue()
                        || wednesdayCheckBox.getValue()
                        || thursdayCheckBox.getValue()
                        || fridayCheckBox.getValue()
                        || saturdayCheckBox.getValue()
                        || sundayCheckBox.getValue();
            }

            @Override
            public Class<Boolean> getType() {
                return Boolean.class;
            }

        };
        mondayCheckBox = new CheckBox(messages.getString("RecorrenciaView.segundaCheckBox.caption"));
        mondayCheckBox.addValidator(weekDayValidator);
        tuesdayCheckBox = new CheckBox(messages.getString("RecorrenciaView.tercaCheckBox.caption"));
        tuesdayCheckBox.addValidator(weekDayValidator);
        wednesdayCheckBox = new CheckBox(messages.getString("RecorrenciaView.quartaCheckBox.caption"));
        wednesdayCheckBox.addValidator(weekDayValidator);
        thursdayCheckBox = new CheckBox(messages.getString("RecorrenciaView.quintaCheckBox.caption"));
        thursdayCheckBox.addValidator(weekDayValidator);
        fridayCheckBox = new CheckBox(messages.getString("RecorrenciaView.sextaCheckBox.caption"));
        fridayCheckBox.addValidator(weekDayValidator);
        saturdayCheckBox = new CheckBox(messages.getString("RecorrenciaView.sabadoCheckBox.caption"));
        saturdayCheckBox.addValidator(weekDayValidator);
        sundayCheckBox = new CheckBox(messages.getString("RecorrenciaView.domingoCheckBox.caption"));
        sundayCheckBox.addValidator(weekDayValidator);

        numberWeeksCombo = new ComboBox(messages.getString("RecorrenciaView.qtdeSemanasCombo.label"));
        numberWeeksCombo.addValidator(new NullValidator(messages.getString("RecorrenciaView.numberWeeksCombo.inputValidatorMessage"), false));
        numberWeeksCombo.addItem("1");
        numberWeeksCombo.addItem("2");
        numberWeeksCombo.addItem("3");
        numberWeeksCombo.addItem("4");

        startDateWeeklyDateField = new PopupDateField(messages.getString("RecorrenciaView.dataInicioSemanalDateField.label"));
        startDateWeeklyDateField.setConverter(new DateToLocalDateConverter());

        endDateWeeklyDateField = new PopupDateField(messages.getString("RecorrenciaView.dataFimSemanalDateField.label"));
        endDateWeeklyDateField.setConverter(new DateToLocalDateConverter());

        startDateWeeklyDateField.addValidator(new DataInicioValidator(endDateWeeklyDateField, messages.getString("RecorrenciaView.dataInicioSemanalDateField.label")));
        endDateWeeklyDateField.addValidator(new DataFimValidator(startDateWeeklyDateField, messages.getString("RecorrenciaView.dataFimSemanalDateField.label")));
        startDateWeeklyDateField.addValidator(new NullValidator(messages.getString("RecorrenciaView.startDateWeeklyDateField.erroMessage"), false));
        endDateWeeklyDateField.addValidator(new NullValidator(messages.getString("RecorrenciaView.dataFimSemanalDateField.erroMessage"), false));

        HorizontalLayout diasContainer = new HorizontalLayout();
        diasContainer.setSpacing(true);
        diasContainer.setSizeFull();

        diasContainer.addComponent(mondayCheckBox);
        diasContainer.addComponent(tuesdayCheckBox);
        diasContainer.addComponent(wednesdayCheckBox);
        diasContainer.addComponent(thursdayCheckBox);
        diasContainer.addComponent(fridayCheckBox);
        diasContainer.addComponent(saturdayCheckBox);
        diasContainer.addComponent(sundayCheckBox);

        HorizontalLayout datasContainer = new HorizontalLayout();
        datasContainer.setSpacing(true);
        datasContainer.setSizeFull();

        datasContainer.addComponent(numberWeeksCombo);
        datasContainer.addComponent(startDateWeeklyDateField);
        datasContainer.addComponent(endDateWeeklyDateField);

        weeklyTab = new VerticalLayout();
        weeklyTab.setSpacing(true);
        weeklyTab.setMargin(true);
        weeklyTab.setSizeFull();

        weeklyTab.addComponent(diasContainer);
        weeklyTab.addComponent(datasContainer);

        return weeklyTab;
    }

    /**
     * Constructs tab of recurrence Monthly:
     *
     * @return
     */
    private Component buildAbaMensal() {

        daysMonthlyCombo = new ComboBox(messages.getString("RecorrenciaView.diaMesCombo.label"));
        daysMonthlyCombo.addItem(messages.getString("RecorrenciaView.primeiroDiaUtil"));
        daysMonthlyCombo.addItem(messages.getString("RecorrenciaView.ultimoDiaMes"));
        daysMonthlyCombo.addValidator(new NullValidator(messages.getString("RecorrenciaView.daysMonthlyCombo.inputValidatorMessage"), false));

        for (int i = 1; i <= 31; i++) {
            daysMonthlyCombo.addItem(FormatterUtil.getDecimalFormat00().format(i));
        }

        numberMonthsCombo = new ComboBox(messages.getString("RecorrenciaView.qtdeMesesCombo.label"));
        numberMonthsCombo.addValidator(new NullValidator(messages.getString("RecorrenciaView.numberMonthsCombo.inputValidatorMessage"), false));
        numberMonthsCombo.setPageLength(12);
        for (int i = 1; i <= 12; i++) {
            numberMonthsCombo.addItem(FormatterUtil.getDecimalFormat00().format(i));
        }

        kindDayMonthlyCombo = new ComboBox(messages.getString("RecorrenciaView.tipoDiaMensalCombo.label"));
        kindDayMonthlyCombo.addValidator(new NullValidator(messages.getString("RecorrenciaView.kindDayMonthlyCombo.inputValidatorMessage"), false));
        kindDayMonthlyCombo.addItem(messages.getString("RecorrenciaView.diaCorrido"));
        kindDayMonthlyCombo.addItem(messages.getString("RecorrenciaView.diaUtil"));
        kindDayMonthlyCombo.addItem(messages.getString("RecorrenciaView.diaUtilSabado"));

        startDateMonthlyDateField = new PopupDateField(messages.getString("RecorrenciaView.dataInicioSemanalDateField.label"));
        startDateMonthlyDateField.setConverter(new DateToLocalDateConverter());
        endDateMonthlyDateField = new PopupDateField(messages.getString("RecorrenciaView.dataFimSemanalDateField.label"));
        endDateMonthlyDateField.setConverter(new DateToLocalDateConverter());

        startDateMonthlyDateField.addValidator(new DataInicioValidator(endDateMonthlyDateField, messages.getString("RecorrenciaView.dataInicioSemanalDateField.label")));
        endDateMonthlyDateField.addValidator(new DataFimValidator(startDateMonthlyDateField, messages.getString("RecorrenciaView.dataFimSemanalDateField.label")));
        startDateMonthlyDateField.addValidator(new NullValidator(messages.getString("RecorrenciaView.startDateWeeklyDateField.erroMessage"), false));
        endDateMonthlyDateField.addValidator(new NullValidator(messages.getString("RecorrenciaView.dataFimSemanalDateField.erroMessage"), false));

        monthlyTab = new GridLayout(3, 2);
        monthlyTab.setSpacing(true);
        monthlyTab.setMargin(true);
        monthlyTab.setWidth("100%");
        monthlyTab.setHeight(null);

        monthlyTab.addComponent(daysMonthlyCombo, 0, 0);
        monthlyTab.addComponent(numberMonthsCombo, 1, 0);
        monthlyTab.addComponent(kindDayMonthlyCombo, 2, 0);
        monthlyTab.addComponent(startDateMonthlyDateField, 0, 1);
        monthlyTab.addComponent(endDateMonthlyDateField, 1, 1);


        return monthlyTab;
    }

    /**
     * Builds the tab Annual recurrence:
     *
     * @return
     */
    private Component buildAbaAnual() {

        dayAnnualCombo = new ComboBox(messages.getString("RecorrenciaView.diaAnualCombo.label"));
        dayAnnualCombo.addItem(messages.getString("RecorrenciaView.primeiroDiaUtil"));
        dayAnnualCombo.addItem(messages.getString("RecorrenciaView.ultimoDiaMes"));
        dayAnnualCombo.addValidator(new NullValidator(messages.getString("RecorrenciaView.dayAnnualCombo.inputValidatorMessage"), false));

        for (int i = 1; i <= 31; i++) {
            dayAnnualCombo.addItem(FormatterUtil.getDecimalFormat00().format(i));
        }

        kindDayAnnualCombo = new ComboBox(messages.getString("RecorrenciaView.tipoDiaAnualCombo.label"));
        kindDayAnnualCombo.addItem(messages.getString("RecorrenciaView.diaCorrido"));
        kindDayAnnualCombo.addItem(messages.getString("RecorrenciaView.diaUtil"));
        kindDayAnnualCombo.addItem(messages.getString("RecorrenciaView.diaUtilSabado"));
        kindDayAnnualCombo.addValidator(new NullValidator(messages.getString("RecorrenciaView.kindDayMonthlyCombo.inputValidatorMessage"), false));

        monthAnnualCombo = new ComboBox(messages.getString("RecorrenciaView.mesAnualCombo.label"));
        monthAnnualCombo.addItem(1);
        monthAnnualCombo.addItem(2);
        monthAnnualCombo.addItem(3);
        monthAnnualCombo.addItem(4);
        monthAnnualCombo.addItem(5);
        monthAnnualCombo.addItem(6);
        monthAnnualCombo.addItem(7);
        monthAnnualCombo.addItem(8);
        monthAnnualCombo.addItem(9);
        monthAnnualCombo.addItem(10);
        monthAnnualCombo.addItem(11);
        monthAnnualCombo.addItem(12);
        
        monthAnnualCombo.setItemCaption(1, messages.getString("RecorrenciaView.janeiro"));
        monthAnnualCombo.setItemCaption(2, messages.getString("RecorrenciaView.fevereiro"));
        monthAnnualCombo.setItemCaption(3, messages.getString("RecorrenciaView.marco"));
        monthAnnualCombo.setItemCaption(4, messages.getString("RecorrenciaView.abril"));
        monthAnnualCombo.setItemCaption(5, messages.getString("RecorrenciaView.maio"));
        monthAnnualCombo.setItemCaption(6, messages.getString("RecorrenciaView.junho"));
        monthAnnualCombo.setItemCaption(7, messages.getString("RecorrenciaView.julho"));
        monthAnnualCombo.setItemCaption(8, messages.getString("RecorrenciaView.agosto"));
        monthAnnualCombo.setItemCaption(9, messages.getString("RecorrenciaView.setembro"));
        monthAnnualCombo.setItemCaption(10, messages.getString("RecorrenciaView.outubro"));
        monthAnnualCombo.setItemCaption(11, messages.getString("RecorrenciaView.novembro"));
        monthAnnualCombo.setItemCaption(12, messages.getString("RecorrenciaView.dezembro"));
        monthAnnualCombo.addValidator(new NullValidator(messages.getString("RecorrenciaView.monthAnnualCombo.inputValidatorMessage"), false));

        yearAnnualCombo = new ComboBox(messages.getString("RecorrenciaView.anoAnualCombo.label"));
        final int startYear = Calendar.getInstance().get(Calendar.YEAR);
        // rolls 7 years from startYear
        for (int i = startYear; i <= (startYear + 7); i++) {
            yearAnnualCombo.addItem(String.valueOf(i));
        }
        yearAnnualCombo.addValidator(new NullValidator(messages.getString("RecorrenciaView.anoAnualCombo.inputValidatorMessage"), false));

        HorizontalLayout anoContainer = new HorizontalLayout();
        anoContainer.setSpacing(true);
        anoContainer.setSizeFull();

        anoContainer.addComponent(dayAnnualCombo);
        anoContainer.addComponent(kindDayAnnualCombo);

        HorizontalLayout anoContainer2 = new HorizontalLayout();
        anoContainer2.setSpacing(true);
        anoContainer2.setSizeFull();

        anoContainer2.addComponent(monthAnnualCombo);
        anoContainer2.addComponent(yearAnnualCombo);

        annualTab = new VerticalLayout();
        annualTab.setSpacing(true);
        annualTab.setMargin(true);
        annualTab.setSizeFull();

        annualTab.addComponent(anoContainer);
        annualTab.addComponent(anoContainer2);

        return annualTab;
    }

    /**
     * Constructs and returns the lower button bar
     *
     * @return
     */
    private Component buildBarraBotoesInferior() {

        HorizontalLayout barraBotoesInferior = new HorizontalLayout();
        barraBotoesInferior.setSizeUndefined();
        barraBotoesInferior.setSpacing(true);

        okButton = new Button(messages.getString("RecorrenciaView.okButton.caption"));
        okButton.addClickListener((Button.ClickEvent event) -> {
            listener.okButtonClicked();
        });

        barraBotoesInferior.addComponent(okButton);

        cancelButton = new Button(messages.getString("RecorrenciaView.cancelarButton.caption"));
        cancelButton.addClickListener((Button.ClickEvent event) -> {
            listener.cancelButtonClicked();
        });

        barraBotoesInferior.addComponent(cancelButton);

        return barraBotoesInferior;
    }

    /**
     * Sets the listener's view events
     *
     * @param listener
     */
    public void setListener(RecorrenciaViewListener listener) {
        this.listener = listener;
    }

    /**
     * Hides / shows the weekly recurrence tab
     *
     * @param visible
     */
    public void setAbaSemanalVisible(boolean visible) {

        TabSheet.Tab tab = accordion.getTab(weeklyTab);
        tab.setVisible(visible);

    }

    /**
     * Hides / shows the monthly recurrence tab
     *
     * @param visible
     */
    public void setAbaMensalVisible(boolean visible) {

        TabSheet.Tab tab = accordion.getTab(monthlyTab);
        tab.setVisible(visible);

    }

    /**
     * Hides / shows the annual recurrence tab
     *
     * @param visible
     */
    public void setAbaAnualVisible(boolean visible) {

        TabSheet.Tab tab = accordion.getTab(annualTab);
        tab.setVisible(visible);

    }

    public ComboBox getKindDayMonthlyCombo() {
        return kindDayMonthlyCombo;
    }

    public void setKindDayMonthlyCombo(ComboBox kindDayMonthlyCombo) {
        this.kindDayMonthlyCombo = kindDayMonthlyCombo;
    }

    public ComboBox getKindDayAnnualCombo() {
        return kindDayAnnualCombo;
    }

    public void setKindDayAnnualCombo(ComboBox kindDayAnnualCombo) {
        this.kindDayAnnualCombo = kindDayAnnualCombo;
    }

    public ComboBox getMonthAnnualCombo() {
        return monthAnnualCombo;
    }

    public void setMonthAnnualCombo(ComboBox monthAnnualCombo) {
        this.monthAnnualCombo = monthAnnualCombo;
    }

    public ComboBox getYearAnnualCombo() {
        return yearAnnualCombo;
    }

    public void setYearAnnualCombo(ComboBox yearAnnualCombo) {
        this.yearAnnualCombo = yearAnnualCombo;
    }

    /**
     * @return the titleLabel
     */
    public Label getTitleLabel() {
        return titleLabel;
    }

    /**
     * @return the superiorBar
     */
    public HorizontalLayout getSuperiorBar() {
        return superiorBar;
    }

    /**
     * @return the weeklyCheckBox
     */
    public CheckBox getWeeklyCheckBox() {
        return weeklyCheckBox;
    }

    /**
     * @return the monthlyCheckBox
     */
    public CheckBox getMonthlyCheckBox() {
        return monthlyCheckBox;
    }

    /**
     * @return the annualCheckBox
     */
    public CheckBox getAnnualCheckBox() {
        return annualCheckBox;
    }

    /**
     * @return the weeklyTab
     */
    public VerticalLayout getWeeklyTab() {
        return weeklyTab;
    }

    /**
     * @return the mondayCheckBox
     */
    public CheckBox getMondayCheckBox() {
        return mondayCheckBox;
    }

    /**
     * @return the tuesdayCheckBox
     */
    public CheckBox getTuesdayCheckBox() {
        return tuesdayCheckBox;
    }

    /**
     * @return the wednesdayCheckBox
     */
    public CheckBox getWednesdayCheckBox() {
        return wednesdayCheckBox;
    }

    /**
     * @return the thursdayCheckBox
     */
    public CheckBox getThursdayCheckBox() {
        return thursdayCheckBox;
    }

    /**
     * @return the fridayCheckBox
     */
    public CheckBox getFridayCheckBox() {
        return fridayCheckBox;
    }

    /**
     * @return the saturdayCheckBox
     */
    public CheckBox getSaturdayCheckBox() {
        return saturdayCheckBox;
    }

    /**
     * @return the sundayCheckBox
     */
    public CheckBox getSundayCheckBox() {
        return sundayCheckBox;
    }

    /**
     * @return the numberWeeksCombo
     */
    public ComboBox getNumberWeeksCombo() {
        return numberWeeksCombo;
    }

    /**
     * @return the startDateWeeklyDateField
     */
    public PopupDateField getStartDateWeeklyDateField() {
        return startDateWeeklyDateField;
    }

    /**
     * @return the endDateWeeklyDateField
     */
    public PopupDateField getEndDateWeeklyDateField() {
        return endDateWeeklyDateField;
    }

    /**
     * @return the monthlyTab
     */
    public GridLayout getMonthlyTab() {
        return monthlyTab;
    }

    /**
     * @return the daysMonthlyCombo
     */
    public ComboBox getDaysMonthlyCombo() {
        return daysMonthlyCombo;
    }

    /**
     * @return the startDateMonthlyDateField
     */
    public PopupDateField getStartDateMonthlyDateField() {
        return startDateMonthlyDateField;
    }

    /**
     * @return the endDateMonthlyDateField
     */
    public PopupDateField getEndDateMonthlyDateField() {
        return endDateMonthlyDateField;
    }

    /**
     * @return the annualTab
     */
    public VerticalLayout getAnnualTab() {
        return annualTab;
    }

    /**
     * @return the dayAnnualCombo
     */
    public ComboBox getDayAnnualCombo() {
        return dayAnnualCombo;
    }

    /**
     * @return the okButton
     */
    public Button getOkButton() {
        return okButton;
    }

    /**
     * @return the cancelButton
     */
    public Button getCancelButton() {
        return cancelButton;
    }

    public ComboBox getNumberMonthsCombo() {
        return numberMonthsCombo;
    }

    /**
     * Set all required fields validators visible or not
     * @param visible 
     */
    public void setValidatorsVisible(boolean visible) {

        mondayCheckBox.setValidationVisible(visible);
        tuesdayCheckBox.setValidationVisible(visible);
        wednesdayCheckBox.setValidationVisible(visible);
        thursdayCheckBox.setValidationVisible(visible);
        fridayCheckBox.setValidationVisible(visible);
        saturdayCheckBox.setValidationVisible(visible);
        sundayCheckBox.setValidationVisible(visible);

        numberWeeksCombo.setValidationVisible(visible);
        startDateWeeklyDateField.setValidationVisible(visible);
        endDateWeeklyDateField.setValidationVisible(visible);
        daysMonthlyCombo.setValidationVisible(visible);

        numberMonthsCombo.setValidationVisible(visible);
        kindDayMonthlyCombo.setValidationVisible(visible);
        startDateMonthlyDateField.setValidationVisible(visible);
        endDateMonthlyDateField.setValidationVisible(visible);
        dayAnnualCombo.setValidationVisible(visible);
        kindDayAnnualCombo.setValidationVisible(visible);

        monthAnnualCombo.setValidationVisible(visible);
        dayAnnualCombo.setValidationVisible(visible);
        yearAnnualCombo.setValidationVisible(visible);

    }

    /**
     * Verify if all required fields ar correctly fullfilled
     * @return true if all validators is OK
     */
    public boolean isValidForWeeklyRecurrency() {

        return mondayCheckBox.isValid()
                && tuesdayCheckBox.isValid()
                && wednesdayCheckBox.isValid()
                && thursdayCheckBox.isValid()
                && fridayCheckBox.isValid()
                && saturdayCheckBox.isValid()
                && sundayCheckBox.isValid()
                && numberWeeksCombo.isValid()
                && startDateWeeklyDateField.isValid()
                && endDateWeeklyDateField.isValid();

    }

    /**
     * Verify if all required fields ar correctly fullfilled
     * @return true if all validators is OK
     */
    public boolean isValidForMonthlyRecurrency() {

        return daysMonthlyCombo.isValid()
                && numberMonthsCombo.isValid()
                && kindDayMonthlyCombo.isValid()
                && startDateMonthlyDateField.isValid()
                && endDateMonthlyDateField.isValid();

    }

    /**
     * Verify if all required fields ar correctly fullfilled
     * @return true if all validators is OK
     */
    public boolean isValidForAnualRecurrency() {

        return dayAnnualCombo.isValid()
                && kindDayAnnualCombo.isValid()
                && monthAnnualCombo.isValid()
                && dayAnnualCombo.isValid() ;

    }

    /**
     * Show a confirmation dialog with de recurrent tasks dates If the user do
     * confirm: the listener is called to creat the tasks
     *
     * @param tarefasRecorrentes
     */
    public void showConfirmCreateRecurrentTasks(List<LocalDate> tarefasRecorrentes) {

        StringBuilder dates = new StringBuilder(messages.getString("RecorrenciaView.showConfirmCreateRecurrentTasks.text"));
        dates.append("\n");
        for (LocalDate tarefasRecorrente : tarefasRecorrentes) {
            dates.append("\n\t * ");
            dates.append(FormatterUtil.formatDate(tarefasRecorrente));
        }

        ConfirmDialog.show(UI.getCurrent(), messages.getString("RecorrenciaView.showConfirmCreateRecurrentTasks.title"), dates.toString(),
                messages.getString("RecorrenciaView.showConfirmCreateRecurrentTasks.OKButton"), 
                messages.getString("RecorrenciaView.showConfirmCreateRecurrentTasks.CancelButton"), (ConfirmDialog dialog) -> {
                    if (dialog.isConfirmed()) {
                        listener.confirmRecurrencyCreation(tarefasRecorrentes);
                    } else {
                        listener.confirmRecurrencyCreation(null);
                    }
                });
    }


}
