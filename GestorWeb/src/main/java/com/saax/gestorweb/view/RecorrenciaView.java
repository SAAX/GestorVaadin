/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.util.GestorWebImagens;
import com.vaadin.data.Property;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.ResourceBundle;

/**
 * Pop-up Window for recurrence Viewing will be three check-box to choose the type
 * of recurrence:
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
private final GestorWebImagens images = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();
    
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
private VerticalLayout monthlyTab;    
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
     * Create a view and all components
     *
     */
    public RecorrenciaView() {
        super();

        setModal(true);
        setWidth("70%");
        setHeight("50%");

        // Container main, which will store all other containers
        VerticalLayout containerPrincipal = buildContainerPrincipal();
        containerPrincipal.setSpacing(true);
        setContent(containerPrincipal);

        center();

    }
    
    /**
     * Builds the main container that will hold all other
     *
     * @return
     */
    private VerticalLayout buildContainerPrincipal() {

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
            listener.recorrenciaSemanal(event);
        });
        
        superiorBar.addComponent(monthlyCheckBox);

        monthlyCheckBox.addValueChangeListener((Property.ValueChangeEvent event) -> {
            listener.recorrenciaMensal(event);
        });
        
        superiorBar.addComponent(annualCheckBox);

        annualCheckBox.addValueChangeListener((Property.ValueChangeEvent event) -> {
            listener.recorrenciaAnual(event);
        });
        
        return superiorBar;
    }
    
    /**
     * Constructs tab of recurrence Weekly:
     *
     * @return
     */
    private Component buildAbaSemanal() {

        mondayCheckBox = new CheckBox(messages.getString("RecorrenciaView.segundaCheckBox.caption"));
        tuesdayCheckBox = new CheckBox(messages.getString("RecorrenciaView.tercaCheckBox.caption"));
        wednesdayCheckBox = new CheckBox(messages.getString("RecorrenciaView.quartaCheckBox.caption"));
        thursdayCheckBox = new CheckBox(messages.getString("RecorrenciaView.quintaCheckBox.caption"));
        fridayCheckBox = new CheckBox(messages.getString("RecorrenciaView.sextaCheckBox.caption"));
        saturdayCheckBox = new CheckBox(messages.getString("RecorrenciaView.sabadoCheckBox.caption"));
        sundayCheckBox = new CheckBox(messages.getString("RecorrenciaView.domingoCheckBox.caption"));
        
        numberWeeksCombo = new ComboBox(messages.getString("RecorrenciaView.qtdeSemanasCombo.label"));
        numberWeeksCombo.addItem("1");
        numberWeeksCombo.addItem("2");
        numberWeeksCombo.addItem("3");
        numberWeeksCombo.addItem("4");
        
        startDateWeeklyDateField = new PopupDateField(messages.getString("RecorrenciaView.dataInicioSemanalDateField.label"));
        endDateWeeklyDateField = new PopupDateField(messages.getString("RecorrenciaView.dataFimSemanalDateField.label"));
        
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
        weeklyTab.setWidth("100%");
        weeklyTab.setHeight(null);

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
        daysMonthlyCombo.addItem("01");
        daysMonthlyCombo.addItem("02");
        daysMonthlyCombo.addItem("03");
        daysMonthlyCombo.addItem("04");
        daysMonthlyCombo.addItem("05");
        daysMonthlyCombo.addItem("06");
        daysMonthlyCombo.addItem("07");
        daysMonthlyCombo.addItem("08");
        daysMonthlyCombo.addItem("09");
        daysMonthlyCombo.addItem("10");
        daysMonthlyCombo.addItem("11");
        daysMonthlyCombo.addItem("12");
        daysMonthlyCombo.addItem("13");
        daysMonthlyCombo.addItem("14");
        daysMonthlyCombo.addItem("15");
        daysMonthlyCombo.addItem("16");
        daysMonthlyCombo.addItem("17");
        daysMonthlyCombo.addItem("18");
        daysMonthlyCombo.addItem("19");
        daysMonthlyCombo.addItem("20");
        daysMonthlyCombo.addItem("21");
        daysMonthlyCombo.addItem("22");
        daysMonthlyCombo.addItem("23");
        daysMonthlyCombo.addItem("24");
        daysMonthlyCombo.addItem("25");
        daysMonthlyCombo.addItem("26");
        daysMonthlyCombo.addItem("27");
        daysMonthlyCombo.addItem("28");
        daysMonthlyCombo.addItem("29");
        daysMonthlyCombo.addItem("30");
        daysMonthlyCombo.addItem("31");
        
        numberMonthsCombo = new ComboBox(messages.getString("RecorrenciaView.qtdeMesesCombo.label"));
        numberMonthsCombo.addItem("01");
        numberMonthsCombo.addItem("02");
        numberMonthsCombo.addItem("03");
        numberMonthsCombo.addItem("04");
        numberMonthsCombo.addItem("05");
        numberMonthsCombo.addItem("06");
        numberMonthsCombo.addItem("07");
        numberMonthsCombo.addItem("08");
        numberMonthsCombo.addItem("09");
        numberMonthsCombo.addItem("10");
        numberMonthsCombo.addItem("11");
        numberMonthsCombo.addItem("12");
        
        kindDayMonthlyCombo = new ComboBox(messages.getString("RecorrenciaView.tipoDiaMensalCombo.label"));
        kindDayMonthlyCombo.addItem(messages.getString("RecorrenciaView.diaCorrido"));
        kindDayMonthlyCombo.addItem(messages.getString("RecorrenciaView.diaUtil"));
        kindDayMonthlyCombo.addItem(messages.getString("RecorrenciaView.diaUtilSabado"));
        
        startDateMonthlyDateField = new PopupDateField(messages.getString("RecorrenciaView.dataInicioSemanalDateField.label"));
        endDateMonthlyDateField = new PopupDateField(messages.getString("RecorrenciaView.dataFimSemanalDateField.label"));
        
        HorizontalLayout mesContainer = new HorizontalLayout();
        mesContainer.setSpacing(true);
        mesContainer.setSizeFull();
        
        mesContainer.addComponent(daysMonthlyCombo);
        mesContainer.addComponent(numberMonthsCombo);
        mesContainer.addComponent(kindDayMonthlyCombo);
        
        HorizontalLayout datasMensalContainer = new HorizontalLayout();
        datasMensalContainer.setSpacing(true);
        datasMensalContainer.setSizeFull();
        

        monthlyTab = new VerticalLayout();
        monthlyTab.setSpacing(true);
        monthlyTab.setMargin(true);
        monthlyTab.setWidth("100%");
        monthlyTab.setHeight(null);

        monthlyTab.addComponent(mesContainer);
        monthlyTab.addComponent(datasMensalContainer);

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
        dayAnnualCombo.addItem("01");
        dayAnnualCombo.addItem("02");
        dayAnnualCombo.addItem("03");
        dayAnnualCombo.addItem("04");
        dayAnnualCombo.addItem("05");
        dayAnnualCombo.addItem("06");
        dayAnnualCombo.addItem("07");
        dayAnnualCombo.addItem("08");
        dayAnnualCombo.addItem("09");
        dayAnnualCombo.addItem("10");
        dayAnnualCombo.addItem("11");
        dayAnnualCombo.addItem("12");
        dayAnnualCombo.addItem("13");
        dayAnnualCombo.addItem("14");
        dayAnnualCombo.addItem("15");
        dayAnnualCombo.addItem("16");
        dayAnnualCombo.addItem("17");
        dayAnnualCombo.addItem("18");
        dayAnnualCombo.addItem("19");
        dayAnnualCombo.addItem("20");
        dayAnnualCombo.addItem("21");
        dayAnnualCombo.addItem("22");
        dayAnnualCombo.addItem("23");
        dayAnnualCombo.addItem("24");
        dayAnnualCombo.addItem("25");
        dayAnnualCombo.addItem("26");
        dayAnnualCombo.addItem("27");
        dayAnnualCombo.addItem("28");
        dayAnnualCombo.addItem("29");
        dayAnnualCombo.addItem("30");
        dayAnnualCombo.addItem("31");
        
        
        kindDayAnnualCombo = new ComboBox(messages.getString("RecorrenciaView.tipoDiaAnualCombo.label"));
        kindDayAnnualCombo.addItem(messages.getString("RecorrenciaView.diaCorrido"));
        kindDayAnnualCombo.addItem(messages.getString("RecorrenciaView.diaUtil"));
        kindDayAnnualCombo.addItem(messages.getString("RecorrenciaView.diaUtilSabado"));
        
        //Fernando: Preciso fazer a internacionalização dos meses
        monthAnnualCombo = new ComboBox(messages.getString("RecorrenciaView.mesAnualCombo.label"));
        monthAnnualCombo.addItem("Janeiro");
        monthAnnualCombo.addItem("Fevereiro");
        monthAnnualCombo.addItem("Março");
        monthAnnualCombo.addItem("Abril");
        monthAnnualCombo.addItem("Maio");
        monthAnnualCombo.addItem("Junho");
        monthAnnualCombo.addItem("Julho");
        monthAnnualCombo.addItem("Agosto");
        monthAnnualCombo.addItem("Setembro");
        monthAnnualCombo.addItem("Outubro");
        monthAnnualCombo.addItem("Novembro");
        monthAnnualCombo.addItem("Dezembro");

        yearAnnualCombo = new ComboBox(messages.getString("RecorrenciaView.anoAnualCombo.label"));
        yearAnnualCombo.addItem("2015");
        yearAnnualCombo.addItem("2016");
        yearAnnualCombo.addItem("2017");
        yearAnnualCombo.addItem("2018");
        yearAnnualCombo.addItem("2019");
        yearAnnualCombo.addItem("2020");
        
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
        annualTab.setWidth("100%");
        annualTab.setHeight(null);

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

        barraBotoesInferior.addComponent(okButton);

        cancelButton = new Button(messages.getString("RecorrenciaView.cancelarButton.caption"));
        
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

    public CheckBox getSemanalCheckBox() {
        return weeklyCheckBox;
    }

    public void setSemanalCheckBox(CheckBox semanalCheckBox) {
        this.weeklyCheckBox = semanalCheckBox;
    }

    public CheckBox getMensalCheckBox() {
        return monthlyCheckBox;
    }

    public void setMensalCheckBox(CheckBox mensalCheckBox) {
        this.monthlyCheckBox = mensalCheckBox;
    }

    public CheckBox getAnualCheckBox() {
        return annualCheckBox;
    }

    public void setAnualCheckBox(CheckBox anualCheckBox) {
        this.annualCheckBox = anualCheckBox;
    }

    public CheckBox getSegundaCheckBox() {
        return mondayCheckBox;
    }

    public void setSegundaCheckBox(CheckBox segundaCheckBox) {
        this.mondayCheckBox = segundaCheckBox;
    }

    public CheckBox getTercaCheckBox() {
        return tuesdayCheckBox;
    }

    public void setTercaCheckBox(CheckBox tercaCheckBox) {
        this.tuesdayCheckBox = tercaCheckBox;
    }

    public CheckBox getQuartaCheckBox() {
        return wednesdayCheckBox;
    }

    public void setQuartaCheckBox(CheckBox quartaCheckBox) {
        this.wednesdayCheckBox = quartaCheckBox;
    }

    public CheckBox getQuintaCheckBox() {
        return thursdayCheckBox;
    }

    public void setQuintaCheckBox(CheckBox quintaCheckBox) {
        this.thursdayCheckBox = quintaCheckBox;
    }

    public CheckBox getSextaCheckBox() {
        return fridayCheckBox;
    }

    public void setSextaCheckBox(CheckBox sextaCheckBox) {
        this.fridayCheckBox = sextaCheckBox;
    }

    public CheckBox getSabadoCheckBox() {
        return saturdayCheckBox;
    }

    public void setSabadoCheckBox(CheckBox sabadoCheckBox) {
        this.saturdayCheckBox = sabadoCheckBox;
    }

    public CheckBox getDomingoCheckBox() {
        return sundayCheckBox;
    }

    public void setDomingoCheckBox(CheckBox domingoCheckBox) {
        this.sundayCheckBox = domingoCheckBox;
    }

    public ComboBox getQtdeSemanasCombo() {
        return numberWeeksCombo;
    }

    public void setQtdeSemanasCombo(ComboBox qtdeSemanasCombo) {
        this.numberWeeksCombo = qtdeSemanasCombo;
    }

    public ComboBox getDiaMesCombo() {
        return daysMonthlyCombo;
    }

    public void setDiaMesCombo(ComboBox diaMesCombo) {
        this.daysMonthlyCombo = diaMesCombo;
    }

    public ComboBox getNumberMonthsCombo() {
        return numberMonthsCombo;
    }

    public void setNumberMonthsCombo(ComboBox numberMonthsCombo) {
        this.numberMonthsCombo = numberMonthsCombo;
    }

    public ComboBox getKindDayMonthlyCombo() {
        return kindDayMonthlyCombo;
    }

    public void setKindDayMonthlyCombo(ComboBox kindDayMonthlyCombo) {
        this.kindDayMonthlyCombo = kindDayMonthlyCombo;
    }

    public ComboBox getDiaAnualCombo() {
        return dayAnnualCombo;
    }

    public void setDiaAnualCombo(ComboBox diaAnualCombo) {
        this.dayAnnualCombo = diaAnualCombo;
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
    


}
