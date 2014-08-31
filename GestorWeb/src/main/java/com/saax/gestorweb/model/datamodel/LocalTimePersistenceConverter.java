package com.saax.gestorweb.model.datamodel;

import java.sql.Time;
import java.time.LocalTime;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author rodrigo
 */
@Converter(autoApply = true)
public class LocalTimePersistenceConverter implements AttributeConverter<LocalTime, Time> {
    
    
@Override
    public Time convertToDatabaseColumn(LocalTime entityValue) {
        if (entityValue == null)  return null;
        return Time.valueOf(entityValue);
    }

    @Override
    public LocalTime convertToEntityAttribute(Time databaseValue) {
        if (databaseValue == null)  return null;
        return databaseValue.toLocalTime();
    }
}