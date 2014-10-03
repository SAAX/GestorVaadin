/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.view.converter;

import com.vaadin.data.util.converter.Converter;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

public class DateToLocalDateConverter implements Converter<Date, LocalDate> {

    private static final long serialVersionUID = 1L;

    @Override
    public LocalDate convertToModel(Date value, Class<? extends LocalDate> targetType, Locale locale) throws Converter.ConversionException {
        if (value == null) {
            return null;
        }
        Instant instant = Instant.ofEpochMilli(value.getTime());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
    }

    @Override
    public Date convertToPresentation(LocalDate value, Class<? extends Date> targetType, Locale locale)
            throws Converter.ConversionException {
        if (value == null) {
            return null;
        }
        Instant instant = value.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    @Override
    public Class<LocalDate> getModelType() {
        return LocalDate.class;
    }

    @Override
    public Class<Date> getPresentationType() {
        return Date.class;
    }
}
