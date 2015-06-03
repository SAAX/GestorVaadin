package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.RecorrencyModel;
import com.saax.gestorweb.model.datamodel.RecurrencyEnums;
import com.saax.gestorweb.model.datamodel.Task;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.DateTimeConverters;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.view.RecorrencyView;
import com.saax.gestorweb.view.RecorrencyViewListener;
import com.saax.gestorweb.view.RecurrencyDoneCallBackListener;
import com.vaadin.data.Property;
import com.vaadin.ui.UI;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import org.vaadin.dialogs.ConfirmDialog;

/**
 * Presenter class of Recurrency component
 *
 * @author rodrigo
 */
public class RecorrencyPresenter implements Serializable, RecorrencyViewListener {

    /**
     * View tier
     */
    private final transient RecorrencyView view;

    /**
     * Model tier
     */
    private final transient RecorrencyModel model;

    /**
     * Messages resource bundle
     */
    private final transient ResourceBundle messages = ((GestorMDI) UI.getCurrent()).getMensagens();

    /**
     * Images resource
     */
    private final transient GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();

    /**
     * The logged user reference
     */
    private final Usuario loggedUser;

    /**
     * The task being created or edited
     */
    private Task task;

    /**
     * Listener to be called back when the recurrency task creation is done
     */
    private RecurrencyDoneCallBackListener callBackListener;
    private final LocalDate startDate;

    public void setCallBackListener(RecurrencyDoneCallBackListener callBackListener) {
        this.callBackListener = callBackListener;
    }

    /**
     * Cria o presenter PopUpStatusigando o Model ao View
     *
     * @param model
     * @param view
     * @param task
     * @param startDate
     */
    public RecorrencyPresenter(RecorrencyModel model,
            RecorrencyView view,
            Task task,
            LocalDate startDate) {

        this.model = model;
        this.view = view;
        this.task = task;
        this.startDate = startDate;

        view.setListener(this);

        loggedUser = (Usuario) GestorSession.getAttribute("loggedUser");

    }

    /**
     * Handles the event thrown when the user checks the weekly recurrency
     * checkbox
     *
     * @param event
     */
    @Override
    public void weeklyRecurrencyChecked(Property.ValueChangeEvent event) {
        view.setValidatorsVisible(false);
        view.setAbaSemanalVisible((boolean) event.getProperty().getValue());
        view.setAbaMensalVisible(false);
        view.getMonthlyCheckBox().setValue(Boolean.FALSE);
        view.setAbaAnualVisible(false);
        view.getAnnualCheckBox().setValue(Boolean.FALSE);
    }

    /**
     * Handles the event thrown when the user checks the monthly recurrency
     * checkbox
     *
     * @param event
     */
    @Override
    public void monthlyRecurrencyChecked(Property.ValueChangeEvent event) {
        view.setValidatorsVisible(false);
        view.setAbaMensalVisible((boolean) event.getProperty().getValue());
        view.setAbaSemanalVisible(false);
        view.getWeeklyCheckBox().setValue(Boolean.FALSE);
        view.setAbaAnualVisible(false);
        view.getAnnualCheckBox().setValue(Boolean.FALSE);
    }

    /**
     * Handles the event thrown when the user checks the yearly recurrency
     * checkbox
     *
     * @param event
     */
    @Override
    public void yearlyRecurrencyChecked(Property.ValueChangeEvent event) {
        view.setValidatorsVisible(false);
        view.setAbaAnualVisible((boolean) event.getProperty().getValue());
        view.setAbaSemanalVisible(false);
        view.getWeeklyCheckBox().setValue(Boolean.FALSE);
        view.setAbaMensalVisible(false);
        view.getMonthlyCheckBox().setValue(Boolean.FALSE);
    }

    /**
     * validates the required fields for the selected recurrency type choosed
     *
     * @return
     */
    private boolean validate() {

        if (view.getWeeklyCheckBox().getValue()) {

            return view.isValidForWeeklyRecurrency();

        } else if (view.getMonthlyCheckBox().getValue()) {

            return view.isValidForMonthlyRecurrency();

        } else if (view.getAnnualCheckBox().getValue()) {

            return view.isValidForAnualRecurrency();

        } else {
            throw new RuntimeException("Select the recurrence type.");
        }

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
        if (!validate()) {
            return;
        }

        List<LocalDate> tarefasRecorrentes = null;

        String recurrencyMessage;

        // if its is a WEEKLY recurrence:
        if (view.getWeeklyCheckBox().getValue()) {

            Set<Integer> weekDays = getSelectedWeekDays();

            tarefasRecorrentes = model.createWeeklyRecurrence(
                    weekDays,
                    Integer.parseInt(view.getNumberWeeksCombo().getValue().toString()),
                    DateTimeConverters.toDate(task.getDataInicio()),
                    view.getEndDateWeeklyDateField().getValue());

            recurrencyMessage = model.formatRecurrencyWeeklyMessage(
                    weekDays,
                    Integer.parseInt(view.getNumberWeeksCombo().getValue().toString()),
                    DateTimeConverters.toDate(task.getDataInicio()),
                    view.getEndDateWeeklyDateField().getValue());

        } else if (view.getMonthlyCheckBox().getValue()) {
            // if its is a MONTHLY recurrence:
            tarefasRecorrentes = model.createMonthlyRecurrence(view.getDaysMonthlyCombo().getValue(),
                    Integer.parseInt(view.getNumberMonthsCombo().getValue().toString()),
                    (RecurrencyEnums.WorkingDayType) view.getKindDayMonthlyCombo().getValue(),
                    DateTimeConverters.toDate(task.getDataInicio()),
                    view.getEndDateMonthlyDateField().getValue());

            recurrencyMessage = model.formatRecurrencyMonthlyMessage(
                    (String) view.getDaysMonthlyCombo().getValue(),
                    Integer.parseInt(view.getNumberMonthsCombo().getValue().toString()),
                    (RecurrencyEnums.WorkingDayType) view.getKindDayMonthlyCombo().getValue(),
                    DateTimeConverters.toDate(task.getDataInicio()),
                    view.getEndDateMonthlyDateField().getValue());

        } else if (view.getAnnualCheckBox().getValue()) {
            // if its is a ANNUAL recurrence:
            tarefasRecorrentes = model.createAnnualRecurrence(
                    (String) view.getDayAnnualCombo().getValue(),
                    (RecurrencyEnums.WorkingDayType) view.getKindDayAnnualCombo().getValue(),
                    view.getMonthAnnualCombo().getValue().toString(),
                    (String) view.getYearAnnualCombo().getValue()
            );

            recurrencyMessage = model.formatRecurrencyAnnualMessage(
                    (String) view.getDayAnnualCombo().getValue(),
                    (RecurrencyEnums.WorkingDayType) view.getKindDayAnnualCombo().getValue(),
                    view.getMonthAnnualCombo().getValue().toString(),
                    (String) view.getYearAnnualCombo().getValue());

        } else {
            throw new RuntimeException("Select the recurrence type.");
        }

        view.showConfirmCreateRecurrentTasks(tarefasRecorrentes, recurrencyMessage);

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
        UI.getCurrent().removeWindow(view);
    }

    /**
     * Configure the recurrent dates where there will be created the recurrent
     * tasks
     *
     * @param recurrentDates
     * @param recurrencyMessage
     */
    @Override
    public void confirmRecurrencyCreation(List<LocalDate> recurrentDates, String recurrencyMessage) {
        callBackListener.recurrencyCreationDone(recurrentDates, recurrencyMessage);
        UI.getCurrent().removeWindow(view);
    }

    /**
     * Show a confirmation dialog to remove all recurrent tasks. If the user DO
     * confirm, call the listener to remove the tasks
     */
    @Override
    public void removeAllRecurrency() {

        ConfirmDialog.show(UI.getCurrent(), messages.getString("RecorrencyPresenter.removeAllRecurrency.title"),
                messages.getString("RecorrencyPresenter.removeAllRecurrency.text"),
                messages.getString("RecorrencyPresenter.removeAllRecurrency.OKButton"),
                messages.getString("RecorrencyPresenter.removeAllRecurrency.CancelButton"), (ConfirmDialog dialog) -> {
                    if (dialog.isConfirmed()) {
                        task = model.removeAllRecurrency(task, loggedUser);
                        UI.getCurrent().removeWindow(view);
                        callBackListener.recurrencyRemoved(task);
                    }
                });
    }

    /**
     * Show a confirmation dialog to remove all next recurrent tasks. If the
     * user DO confirm, call the listener to remove the tasks
     */
    @Override
    public void removeAllNextRecurrency() {
        ConfirmDialog.show(UI.getCurrent(),
                messages.getString("RecorrencyPresenter.removeAllNextRecurrency.title"),
                messages.getString("RecorrencyPresenter.removeAllNextRecurrency.text"),
                messages.getString("RecorrencyPresenter.removeAllNextRecurrency.OKButton"),
                messages.getString("RecorrencyPresenter.removeAllNextRecurrency.CancelButton"), (ConfirmDialog dialog) -> {
                    if (dialog.isConfirmed()) {
                        task = model.removeAllNextRecurrency(task, loggedUser);
                        UI.getCurrent().removeWindow(view);
                        callBackListener.recurrencyRemoved(task);
                    }
                });

    }

}
