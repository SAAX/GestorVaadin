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
        return Time.valueOf(entityValue);
    }

    @Override
    public LocalTime convertToEntityAttribute(Time databaseValue) {
        return databaseValue.toLocalTime();
    }
}