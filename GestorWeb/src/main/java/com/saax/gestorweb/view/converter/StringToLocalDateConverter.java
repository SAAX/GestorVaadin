package com.saax.gestorweb.view.converter;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.Converter.ConversionException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * 
 * @author rodrigo
 */
public class StringToLocalDateConverter implements Converter<String, LocalDate> {

    @Override
    public Class<LocalDate> getModelType() {
        return LocalDate.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }

    @Override
    public LocalDate convertToModel(String value, Class<? extends LocalDate> targetType, Locale locale) throws ConversionException {
        if (value == null) {
            return null;
        }
        
        return LocalDate.parse(value, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
    }

    @Override
    public String convertToPresentation(LocalDate value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        if (value == null) {
            return null;
        } else {
            return value.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
        }
    }

}