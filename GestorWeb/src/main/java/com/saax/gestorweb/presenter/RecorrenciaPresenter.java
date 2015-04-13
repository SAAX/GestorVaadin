package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.RecorrenciaModel;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.view.RecorrenciaView;
import com.saax.gestorweb.view.RecorrenciaViewListener;
import com.vaadin.data.Property;
import com.vaadin.ui.UI;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Presenter:
 * <p>
 * Listener de eventos da view das recorrências
 *
 * @author rodrigo
 */
public class RecorrenciaPresenter implements Serializable, RecorrenciaViewListener {

    // Todo presenterPopUpStatus mantem acesso à view e ao model
    private final transient RecorrenciaView view;
    private final transient RecorrenciaModel model;

    // Referencia ao recurso das mensagens:
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final transient GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();
    private final Usuario loggedUser;
    private List<LocalDate> recurrentDates;

    /**
     * Cria o presenter PopUpStatusigando o Model ao View
     *
     * @param model
     * @param view
     */
    public RecorrenciaPresenter(RecorrenciaModel model,
            RecorrenciaView view) {

        this.model = model;
        this.view = view;

        view.setListener(this);

        loggedUser = (Usuario) GestorSession.getAttribute("loggedUser");

    }

    public void open() {
        view.setAbaMensalVisible(false);
        view.setAbaSemanalVisible(false);
        view.setAbaAnualVisible(false);
        view.setValidatorsVisible(false);
    }

    @Override
    public void recorrenciaSemanal(Property.ValueChangeEvent event) {
        view.setValidatorsVisible(false);
        view.setAbaSemanalVisible((boolean) event.getProperty().getValue());
        view.setAbaMensalVisible(false);
        view.getMonthlyCheckBox().setValue(Boolean.FALSE);
        view.setAbaAnualVisible(false);
        view.getAnnualCheckBox().setValue(Boolean.FALSE);
    }

    @Override
    public void recorrenciaMensal(Property.ValueChangeEvent event) {
        view.setValidatorsVisible(false);
        view.setAbaMensalVisible((boolean) event.getProperty().getValue());
        view.setAbaSemanalVisible(false);
        view.getWeeklyCheckBox().setValue(Boolean.FALSE);
        view.setAbaAnualVisible(false);
        view.getAnnualCheckBox().setValue(Boolean.FALSE);
    }

    @Override
    public void recorrenciaAnual(Property.ValueChangeEvent event) {
        view.setValidatorsVisible(false);
        view.setAbaAnualVisible((boolean) event.getProperty().getValue());
        view.setAbaSemanalVisible(false);
        view.getWeeklyCheckBox().setValue(Boolean.FALSE);
        view.setAbaMensalVisible(false);
        view.getMonthlyCheckBox().setValue(Boolean.FALSE);
    }

    /**
     * Handles the event OK. 
     */
    @Override
    public void okButtonClicked() {
        
        view.setValidatorsVisible(false);
        view.setValidatorsVisible(true);
        
        // verify if all validators in view are OK
        // return if they aren't
        if (!view.isValid()) return ;
        
        List<LocalDate> tarefasRecorrentes = null;
        
        // if its is a WEEKLY recurrence:
        if (view.getWeeklyCheckBox().getValue()) {

            Set<Integer> weekDays = getSelectedWeekDays();
            
            tarefasRecorrentes = model.createWeeklyRecurrence(
                    weekDays,
                    Integer.parseInt(view.getNumberWeeksCombo().getValue().toString()),
                    view.getStartDateWeeklyDateField().getValue(),
                    view.getEndDateWeeklyDateField().getValue());
            
            view.showConfirmCreateRecurrentTasks(tarefasRecorrentes);
            
        } else if (view.getMonthlyCheckBox().getValue()) {
            // if its is a MONTHLY recurrence:
            model.createMonthlyRecurrence(
                    view.getDaysMonthlyCombo().getValue(), 
                    view.getNumberMonthsCombo().getValue(), 
                    view.getKindDayMonthlyCombo().getValue(),
                    view.getStartDateMonthlyDateField().getValue(),
                    view.getEndDateMonthlyDateField().getValue());
        
        } else if (view.getAnnualCheckBox().getValue()) {
            // if its is a ANNUAL recurrence:
            model.createAnnualRecurrence(
                    view.getDayAnnualCombo().getValue(),
                    view.getKindDayAnnualCombo().getValue(),
                    view.getMonthAnnualCombo().getValue(),
                    view.getYearAnnualCombo().getValue()
            );
            
        } else {
            throw new RuntimeException("Select the recurrence type.");
        }
    }

    /**
     * Gets the user selected week days and build an SET
     *
     * @return the set with the week days
     */
    private Set<Integer> getSelectedWeekDays() {
        Set<Integer> weekDays = new HashSet<>();

        if (view.getMondayCheckBox().getValue()) {
            weekDays.add(Calendar.MONDAY);
        }
        if (view.getTuesdayCheckBox().getValue()) {
            weekDays.add(Calendar.TUESDAY);
        }
        if (view.getWednesdayCheckBox().getValue()) {
            weekDays.add(Calendar.WEDNESDAY);
        }
        if (view.getThursdayCheckBox().getValue()) {
            weekDays.add(Calendar.THURSDAY);
        }
        if (view.getFridayCheckBox().getValue()) {
            weekDays.add(Calendar.FRIDAY);
        }
        if (view.getSaturdayCheckBox().getValue()) {
            weekDays.add(Calendar.SATURDAY);
        }
        if (view.getSundayCheckBox().getValue()) {
            weekDays.add(Calendar.SUNDAY);
        }

        return weekDays;
    }

    @Override
    public void cancelButtonClicked() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Configure the recurrent dates where there will be created the recurrent tasks
     * @param recurrentDates 
     */
    @Override
    public void setRecurrentDates(List<LocalDate> recurrentDates) {
        this.recurrentDates = recurrentDates;
}

    /**
     * Retrieves the recurrent dates list
     * @return      
     */
    public List<LocalDate> getRecurrentDates() {
        return recurrentDates;
    }
    
    
}
