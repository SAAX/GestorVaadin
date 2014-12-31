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
 * Pop-up Window para recorrência A visualização será com três check-box para escolha do tipo
 * de recorrência:
 * <br>
 * <ol>
 * <li>Semanal</li>
 * <li>Mensal</li>
 * <li>Anual</li>
 * </ol>
 *
 *
 * @author fernando
 */
public class RecorrenciaView extends Window {

// Referencia ao recurso das mensagens:
private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
private final GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();
    
// A view mantem acesso ao listener (Presenter) para notificar os eventos
// Este acesso se dá por uma interface para manter a abstração das camadas
private RecorrenciaViewListener listener;

private Accordion accordion;

private Label tituloLabel;
private HorizontalLayout barraSuperior;
    
// -------------------------------------------------------------------------
// Componentes ref. as opções de recorrência
// -------------------------------------------------------------------------     

private CheckBox semanalCheckBox;
private CheckBox mensalCheckBox;
private CheckBox anualCheckBox;
    
// -------------------------------------------------------------------------
// Componentes ref. a recorrencia semanal 
// -------------------------------------------------------------------------   
private VerticalLayout semanalAba;        
private CheckBox segundaCheckBox;
private CheckBox tercaCheckBox;
private CheckBox quartaCheckBox;
private CheckBox quintaCheckBox;
private CheckBox sextaCheckBox;
private CheckBox sabadoCheckBox;
private CheckBox domingoCheckBox;
private ComboBox qtdeSemanasCombo;
private PopupDateField dataInicioSemanalDateField;
private PopupDateField dataFimSemanalDateField;
 
// -------------------------------------------------------------------------
// Componentes ref. a recorrencia mensal 
// -------------------------------------------------------------------------  
private VerticalLayout mensalAba;    
private ComboBox diaMesCombo;    
private ComboBox qtdeMesesCombo;
private ComboBox tipoDiaMensalCombo;
private PopupDateField dataInicioMensalDateField;
private PopupDateField dataFimMensalDateField;

// -------------------------------------------------------------------------
// Componentes ref. a recorrencia Anual 
// -------------------------------------------------------------------------  
private VerticalLayout anualAba;        
private ComboBox diaAnualCombo;
private ComboBox tipoDiaAnualCombo;
private ComboBox mesAnualCombo;
private ComboBox anoAnualCombo;
    
// -------------------------------------------------------------------------
// Barra de botoes inferior
// -------------------------------------------------------------------------
private Button okButton;
private Button cancelarButton;

/**
     * Cria a view e todos os componentes
     *
     */
    public RecorrenciaView() {
        super();

        setModal(true);
        setWidth("70%");
        setHeight("50%");

        // Container principal, que armazenará todos os demais containeres 
        VerticalLayout containerPrincipal = buildContainerPrincipal();
        containerPrincipal.setSpacing(true);
        setContent(containerPrincipal);

        center();

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

        // label com caminho da tarefa
        tituloLabel = new Label("Escolha o tipo de Recorrência");
        containerPrincipal.addComponent(tituloLabel);
                
        // adiciona a barra de botoes superior (botões de chat, adicionar sub, etc)
        containerPrincipal.addComponent(buildBarraSuperior());
        containerPrincipal.setComponentAlignment(barraSuperior, Alignment.MIDDLE_RIGHT);

        // cria o acordeon de abas e adiciona as abas
        accordion = new Accordion();
        accordion.setWidth("100%");
        // adiciona a aba de dados iniciais
        accordion.addTab(buildAbaSemanal(), mensagens.getString("RecorrenciaView.AbaRecorrenciaSemanal.titulo"), null);
        // adiciona a aba de descrição e responsáveis
        accordion.addTab(buildAbaMensal(), mensagens.getString("RecorrenciaView.AbaRecorrenciaMensal.titulo"), null);
        // adiciona a aba de detalhes da tarefa
        accordion.addTab(buildAbaAnual(), mensagens.getString("RecorrenciaView.AbaRecorrenciaAnual.titulo"), null);

        containerPrincipal.addComponent(accordion);
        containerPrincipal.setExpandRatio(accordion, 1);

        // adiciona a barra de botoes inferior, com os botões de salvar e cancelas
        Component barraInferior = buildBarraBotoesInferior();
        containerPrincipal.addComponent(barraInferior);
        containerPrincipal.setComponentAlignment(barraInferior, Alignment.MIDDLE_CENTER);

        return containerPrincipal;

    }
    
    
    
    /**
     * Constrói e retorna a barra de botões superior
     *
     * @return
     */
    private Component buildBarraSuperior() {
        barraSuperior = new HorizontalLayout();
        barraSuperior.setSpacing(true);
        barraSuperior.setSizeUndefined();
        
        semanalCheckBox = new CheckBox(mensagens.getString("RecorrenciaView.semanalCheckBox.caption"));
        mensalCheckBox = new CheckBox(mensagens.getString("RecorrenciaView.mensalCheckBox.caption"));
        anualCheckBox = new CheckBox(mensagens.getString("RecorrenciaView.anualCheckBox.caption"));
        
        barraSuperior.addComponent(semanalCheckBox);

        semanalCheckBox.addValueChangeListener((Property.ValueChangeEvent event) -> {
            listener.recorrenciaSemanal(event);
        });
        
        barraSuperior.addComponent(mensalCheckBox);

        mensalCheckBox.addValueChangeListener((Property.ValueChangeEvent event) -> {
            listener.recorrenciaMensal(event);
        });
        
        barraSuperior.addComponent(anualCheckBox);

        anualCheckBox.addValueChangeListener((Property.ValueChangeEvent event) -> {
            listener.recorrenciaAnual(event);
        });
        
        return barraSuperior;
    }
    
    /**
     * Constroi a aba de recorrencia Semanal:
     *
     * @return
     */
    private Component buildAbaSemanal() {

        segundaCheckBox = new CheckBox(mensagens.getString("RecorrenciaView.segundaCheckBox.caption"));
        tercaCheckBox = new CheckBox(mensagens.getString("RecorrenciaView.tercaCheckBox.caption"));
        quartaCheckBox = new CheckBox(mensagens.getString("RecorrenciaView.quartaCheckBox.caption"));
        quintaCheckBox = new CheckBox(mensagens.getString("RecorrenciaView.quintaCheckBox.caption"));
        sextaCheckBox = new CheckBox(mensagens.getString("RecorrenciaView.sextaCheckBox.caption"));
        sabadoCheckBox = new CheckBox(mensagens.getString("RecorrenciaView.sabadoCheckBox.caption"));
        domingoCheckBox = new CheckBox(mensagens.getString("RecorrenciaView.domingoCheckBox.caption"));
        
        qtdeSemanasCombo = new ComboBox(mensagens.getString("RecorrenciaView.qtdeSemanasCombo.label"));
        qtdeSemanasCombo.addItem("1");
        qtdeSemanasCombo.addItem("2");
        qtdeSemanasCombo.addItem("3");
        qtdeSemanasCombo.addItem("4");
        
        dataInicioSemanalDateField = new PopupDateField(mensagens.getString("RecorrenciaView.dataInicioSemanalDateField.label"));
        dataFimSemanalDateField = new PopupDateField(mensagens.getString("RecorrenciaView.dataFimSemanalDateField.label"));
        
        HorizontalLayout diasContainer = new HorizontalLayout();
        diasContainer.setSpacing(true);
        diasContainer.setSizeFull();
        
        diasContainer.addComponent(segundaCheckBox);
        diasContainer.addComponent(tercaCheckBox);
        diasContainer.addComponent(quartaCheckBox);
        diasContainer.addComponent(quintaCheckBox);
        diasContainer.addComponent(sextaCheckBox);
        diasContainer.addComponent(sabadoCheckBox);
        diasContainer.addComponent(domingoCheckBox);
        
        
        HorizontalLayout datasContainer = new HorizontalLayout();
        datasContainer.setSpacing(true);
        datasContainer.setSizeFull();
        
        datasContainer.addComponent(qtdeSemanasCombo);
        datasContainer.addComponent(dataInicioSemanalDateField);
        datasContainer.addComponent(dataFimSemanalDateField);
        

        semanalAba = new VerticalLayout();
        semanalAba.setSpacing(true);
        semanalAba.setMargin(true);
        semanalAba.setWidth("100%");
        semanalAba.setHeight(null);

        semanalAba.addComponent(diasContainer);
        semanalAba.addComponent(datasContainer);

        return semanalAba;
    }
    
    /**
     * Constroi a aba de recorrencia Mensal:
     *
     * @return
     */
    private Component buildAbaMensal() {

        diaMesCombo = new ComboBox(mensagens.getString("RecorrenciaView.diaMesCombo.label"));
        diaMesCombo.addItem("Primeiro Dia Útil");
        diaMesCombo.addItem("Último Dia do Mês");
        diaMesCombo.addItem("01");
        diaMesCombo.addItem("02");
        diaMesCombo.addItem("03");
        diaMesCombo.addItem("04");
        diaMesCombo.addItem("05");
        diaMesCombo.addItem("06");
        diaMesCombo.addItem("07");
        diaMesCombo.addItem("08");
        diaMesCombo.addItem("09");
        diaMesCombo.addItem("10");
        diaMesCombo.addItem("11");
        diaMesCombo.addItem("12");
        diaMesCombo.addItem("13");
        diaMesCombo.addItem("14");
        diaMesCombo.addItem("15");
        diaMesCombo.addItem("16");
        diaMesCombo.addItem("17");
        diaMesCombo.addItem("18");
        diaMesCombo.addItem("19");
        diaMesCombo.addItem("20");
        diaMesCombo.addItem("21");
        diaMesCombo.addItem("22");
        diaMesCombo.addItem("23");
        diaMesCombo.addItem("24");
        diaMesCombo.addItem("25");
        diaMesCombo.addItem("26");
        diaMesCombo.addItem("27");
        diaMesCombo.addItem("28");
        diaMesCombo.addItem("29");
        diaMesCombo.addItem("30");
        diaMesCombo.addItem("31");
        
        qtdeMesesCombo = new ComboBox(mensagens.getString("RecorrenciaView.qtdeMesesCombo.label"));
        qtdeMesesCombo.addItem("01");
        qtdeMesesCombo.addItem("02");
        qtdeMesesCombo.addItem("03");
        qtdeMesesCombo.addItem("04");
        qtdeMesesCombo.addItem("05");
        qtdeMesesCombo.addItem("06");
        qtdeMesesCombo.addItem("07");
        qtdeMesesCombo.addItem("08");
        qtdeMesesCombo.addItem("09");
        qtdeMesesCombo.addItem("10");
        qtdeMesesCombo.addItem("11");
        qtdeMesesCombo.addItem("12");
        
        tipoDiaMensalCombo = new ComboBox(mensagens.getString("RecorrenciaView.tipoDiaMensalCombo.label"));
        tipoDiaMensalCombo.addItem("Corrido");
        tipoDiaMensalCombo.addItem("Útil");        
        tipoDiaMensalCombo.addItem("Útil c/ Sábado");        
        
        dataInicioMensalDateField = new PopupDateField(mensagens.getString("RecorrenciaView.dataInicioSemanalDateField.label"));
        dataFimMensalDateField = new PopupDateField(mensagens.getString("RecorrenciaView.dataFimSemanalDateField.label"));
        
        HorizontalLayout mesContainer = new HorizontalLayout();
        mesContainer.setSpacing(true);
        mesContainer.setSizeFull();
        
        mesContainer.addComponent(diaMesCombo);
        mesContainer.addComponent(qtdeMesesCombo);
        mesContainer.addComponent(tipoDiaMensalCombo);
        
        HorizontalLayout datasMensalContainer = new HorizontalLayout();
        datasMensalContainer.setSpacing(true);
        datasMensalContainer.setSizeFull();
        

        mensalAba = new VerticalLayout();
        mensalAba.setSpacing(true);
        mensalAba.setMargin(true);
        mensalAba.setWidth("100%");
        mensalAba.setHeight(null);

        mensalAba.addComponent(mesContainer);
        mensalAba.addComponent(datasMensalContainer);

        return mensalAba;
    }
    
    /**
     * Constroi a aba de recorrencia Anual:
     *
     * @return
     */
    private Component buildAbaAnual() {

        diaAnualCombo = new ComboBox(mensagens.getString("RecorrenciaView.diaAnualCombo.label"));
        diaAnualCombo.addItem("Primeiro Dia Útil");
        diaAnualCombo.addItem("Último Dia do Mês");
        diaAnualCombo.addItem("01");
        diaAnualCombo.addItem("02");
        diaAnualCombo.addItem("03");
        diaAnualCombo.addItem("04");
        diaAnualCombo.addItem("05");
        diaAnualCombo.addItem("06");
        diaAnualCombo.addItem("07");
        diaAnualCombo.addItem("08");
        diaAnualCombo.addItem("09");
        diaAnualCombo.addItem("10");
        diaAnualCombo.addItem("11");
        diaAnualCombo.addItem("12");
        diaAnualCombo.addItem("13");
        diaAnualCombo.addItem("14");
        diaAnualCombo.addItem("15");
        diaAnualCombo.addItem("16");
        diaAnualCombo.addItem("17");
        diaAnualCombo.addItem("18");
        diaAnualCombo.addItem("19");
        diaAnualCombo.addItem("20");
        diaAnualCombo.addItem("21");
        diaAnualCombo.addItem("22");
        diaAnualCombo.addItem("23");
        diaAnualCombo.addItem("24");
        diaAnualCombo.addItem("25");
        diaAnualCombo.addItem("26");
        diaAnualCombo.addItem("27");
        diaAnualCombo.addItem("28");
        diaAnualCombo.addItem("29");
        diaAnualCombo.addItem("30");
        diaAnualCombo.addItem("31");
        
        
        tipoDiaAnualCombo = new ComboBox(mensagens.getString("RecorrenciaView.tipoDiaAnualCombo.label"));
        tipoDiaAnualCombo.addItem("Corrido");
        tipoDiaAnualCombo.addItem("Útil");        
        tipoDiaAnualCombo.addItem("Útil c/ Sábado");     
        
        
        mesAnualCombo = new ComboBox(mensagens.getString("RecorrenciaView.mesAnualCombo.label"));
        mesAnualCombo.addItem("Janeiro");
        mesAnualCombo.addItem("Fevereiro");
        mesAnualCombo.addItem("Março");
        mesAnualCombo.addItem("Abril");
        mesAnualCombo.addItem("Maio");
        mesAnualCombo.addItem("Junho");
        mesAnualCombo.addItem("Julho");
        mesAnualCombo.addItem("Agosto");
        mesAnualCombo.addItem("Setembro");
        mesAnualCombo.addItem("Outubro");
        mesAnualCombo.addItem("Novembro");
        mesAnualCombo.addItem("Dezembro");

        anoAnualCombo = new ComboBox(mensagens.getString("RecorrenciaView.anoAnualCombo.label"));
        anoAnualCombo.addItem("2015");
        anoAnualCombo.addItem("2016");
        anoAnualCombo.addItem("2017");
        anoAnualCombo.addItem("2018");
        anoAnualCombo.addItem("2019");
        anoAnualCombo.addItem("2020");
        
        HorizontalLayout anoContainer = new HorizontalLayout();
        anoContainer.setSpacing(true);
        anoContainer.setSizeFull();
        
        anoContainer.addComponent(diaAnualCombo);
        anoContainer.addComponent(tipoDiaAnualCombo);
        
        HorizontalLayout anoContainer2 = new HorizontalLayout();
        anoContainer2.setSpacing(true);
        anoContainer2.setSizeFull();
        
        anoContainer2.addComponent(mesAnualCombo);
        anoContainer2.addComponent(anoAnualCombo);
        

        anualAba = new VerticalLayout();
        anualAba.setSpacing(true);
        anualAba.setMargin(true);
        anualAba.setWidth("100%");
        anualAba.setHeight(null);

        anualAba.addComponent(anoContainer);
        anualAba.addComponent(anoContainer2);

        return anualAba;
    }
    
    /**
     * Constrói e retorna a barra de botões inferiores
     *
     * @return
     */
    private Component buildBarraBotoesInferior() {

        HorizontalLayout barraBotoesInferior = new HorizontalLayout();
        barraBotoesInferior.setSizeUndefined();
        barraBotoesInferior.setSpacing(true);

        okButton = new Button(mensagens.getString("RecorrenciaView.okButton.caption"));

        barraBotoesInferior.addComponent(okButton);

        cancelarButton = new Button(mensagens.getString("RecorrenciaView.cancelarButton.caption"));
        
        barraBotoesInferior.addComponent(cancelarButton);

        return barraBotoesInferior;
    }
    
    /**
     * Configura o listener de eventos da view
     *
     * @param listener
     */
    public void setListener(RecorrenciaViewListener listener) {
        this.listener = listener;
    }
    
    /**
     * Oculta / revela a aba de recorrencia semanal
     *
     * @param visible
     */
    public void setAbaSemanalVisible(boolean visible) {

        TabSheet.Tab tab = accordion.getTab(semanalAba);
        tab.setVisible(visible);

    }
    
    /**
     * Oculta / revela a aba de recorrencia mensal
     *
     * @param visible
     */
    public void setAbaMensalVisible(boolean visible) {

        TabSheet.Tab tab = accordion.getTab(mensalAba);
        tab.setVisible(visible);

    }
    
     /**
     * Oculta / revela a aba de recorrencia anual
     *
     * @param visible
     */
    public void setAbaAnualVisible(boolean visible) {

        TabSheet.Tab tab = accordion.getTab(anualAba);
        tab.setVisible(visible);

    }

    public CheckBox getSemanalCheckBox() {
        return semanalCheckBox;
    }

    public void setSemanalCheckBox(CheckBox semanalCheckBox) {
        this.semanalCheckBox = semanalCheckBox;
    }

    public CheckBox getMensalCheckBox() {
        return mensalCheckBox;
    }

    public void setMensalCheckBox(CheckBox mensalCheckBox) {
        this.mensalCheckBox = mensalCheckBox;
    }

    public CheckBox getAnualCheckBox() {
        return anualCheckBox;
    }

    public void setAnualCheckBox(CheckBox anualCheckBox) {
        this.anualCheckBox = anualCheckBox;
    }

    public CheckBox getSegundaCheckBox() {
        return segundaCheckBox;
    }

    public void setSegundaCheckBox(CheckBox segundaCheckBox) {
        this.segundaCheckBox = segundaCheckBox;
    }

    public CheckBox getTercaCheckBox() {
        return tercaCheckBox;
    }

    public void setTercaCheckBox(CheckBox tercaCheckBox) {
        this.tercaCheckBox = tercaCheckBox;
    }

    public CheckBox getQuartaCheckBox() {
        return quartaCheckBox;
    }

    public void setQuartaCheckBox(CheckBox quartaCheckBox) {
        this.quartaCheckBox = quartaCheckBox;
    }

    public CheckBox getQuintaCheckBox() {
        return quintaCheckBox;
    }

    public void setQuintaCheckBox(CheckBox quintaCheckBox) {
        this.quintaCheckBox = quintaCheckBox;
    }

    public CheckBox getSextaCheckBox() {
        return sextaCheckBox;
    }

    public void setSextaCheckBox(CheckBox sextaCheckBox) {
        this.sextaCheckBox = sextaCheckBox;
    }

    public CheckBox getSabadoCheckBox() {
        return sabadoCheckBox;
    }

    public void setSabadoCheckBox(CheckBox sabadoCheckBox) {
        this.sabadoCheckBox = sabadoCheckBox;
    }

    public CheckBox getDomingoCheckBox() {
        return domingoCheckBox;
    }

    public void setDomingoCheckBox(CheckBox domingoCheckBox) {
        this.domingoCheckBox = domingoCheckBox;
    }

    public ComboBox getQtdeSemanasCombo() {
        return qtdeSemanasCombo;
    }

    public void setQtdeSemanasCombo(ComboBox qtdeSemanasCombo) {
        this.qtdeSemanasCombo = qtdeSemanasCombo;
    }

    public ComboBox getDiaMesCombo() {
        return diaMesCombo;
    }

    public void setDiaMesCombo(ComboBox diaMesCombo) {
        this.diaMesCombo = diaMesCombo;
    }

    public ComboBox getQtdeMesesCombo() {
        return qtdeMesesCombo;
    }

    public void setQtdeMesesCombo(ComboBox qtdeMesesCombo) {
        this.qtdeMesesCombo = qtdeMesesCombo;
    }

    public ComboBox getTipoDiaMensalCombo() {
        return tipoDiaMensalCombo;
    }

    public void setTipoDiaMensalCombo(ComboBox tipoDiaMensalCombo) {
        this.tipoDiaMensalCombo = tipoDiaMensalCombo;
    }

    public ComboBox getDiaAnualCombo() {
        return diaAnualCombo;
    }

    public void setDiaAnualCombo(ComboBox diaAnualCombo) {
        this.diaAnualCombo = diaAnualCombo;
    }

    public ComboBox getTipoDiaAnualCombo() {
        return tipoDiaAnualCombo;
    }

    public void setTipoDiaAnualCombo(ComboBox tipoDiaAnualCombo) {
        this.tipoDiaAnualCombo = tipoDiaAnualCombo;
    }

    public ComboBox getMesAnualCombo() {
        return mesAnualCombo;
    }

    public void setMesAnualCombo(ComboBox mesAnualCombo) {
        this.mesAnualCombo = mesAnualCombo;
    }

    public ComboBox getAnoAnualCombo() {
        return anoAnualCombo;
    }

    public void setAnoAnualCombo(ComboBox anoAnualCombo) {
        this.anoAnualCombo = anoAnualCombo;
    }
    


}
