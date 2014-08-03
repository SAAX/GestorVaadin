package com.saax.gestorweb.model.datamodel;

import java.sql.Date;
import java.time.LocalDate;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


@Converter(autoApply = true)
public class LocalDatePersistenceConverter implements AttributeConverter<LocalDate, Date> {
    
    
@Override
    public java.sql.Date convertToDatabaseColumn(LocalDate entityValue) {
        if (entityValue == null)  return null;
        return java.sql.Date.valueOf(entityValue);
    }

    @Override
    public LocalDate convertToEntityAttribute(java.sql.Date databaseValue) {
        if (databaseValue == null)  return null;
        return databaseValue.toLocalDate();
    }
}