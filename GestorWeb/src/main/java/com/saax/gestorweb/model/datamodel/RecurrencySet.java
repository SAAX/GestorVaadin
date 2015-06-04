/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.model.datamodel;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author rodrigo
 */
public class RecurrencySet {
    
    private List<LocalDate> recurrentDates;
    private String recurrencyMessage;
    private LocalDate firstTaskStartDate;
    private LocalDate firstTaskEndDate;

    /**
     * @return the recurrentDates
     */
    public List<LocalDate> getRecurrentDates() {
        return recurrentDates;
    }

    /**
     * @param recurrentDates the recurrentDates to set
     */
    public void setRecurrentDates(List<LocalDate> recurrentDates) {
        this.recurrentDates = recurrentDates;
    }

    /**
     * @return the recurrencyMessage
     */
    public String getRecurrencyMessage() {
        return recurrencyMessage;
    }

    /**
     * @param recurrencyMessage the recurrencyMessage to set
     */
    public void setRecurrencyMessage(String recurrencyMessage) {
        this.recurrencyMessage = recurrencyMessage;
    }

    /**
     * @return the firstTaskStartDate
     */
    public LocalDate getFirstTaskStartDate() {
        return firstTaskStartDate;
    }

    /**
     * @param firstTaskStartDate the firstTaskStartDate to set
     */
    public void setFirstTaskStartDate(LocalDate firstTaskStartDate) {
        this.firstTaskStartDate = firstTaskStartDate;
    }

    /**
     * @return the firstTaskEndDate
     */
    public LocalDate getFirstTaskEndDate() {
        return firstTaskEndDate;
    }

    /**
     * @param firstTaskEndDate the firstTaskEndDate to set
     */
    public void setFirstTaskEndDate(LocalDate firstTaskEndDate) {
        this.firstTaskEndDate = firstTaskEndDate;
    }
    
    
    
}
