/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.view;

import com.vaadin.data.Property;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Fernando
 */
public interface RecorrencyViewListener {

    public void weeklyRecurrencyChecked(Property.ValueChangeEvent event);

    public void monthlyRecurrencyChecked(Property.ValueChangeEvent event);

    public void yearlyRecurrencyChecked(Property.ValueChangeEvent event);

    public void okButtonClicked();

    public void cancelButtonClicked();

    public void confirmRecurrencyCreation(List<LocalDate> tarefasRecorrentes);

    public void removeAllRecurrency();

    public void removeAllNextRecurrency();
}
