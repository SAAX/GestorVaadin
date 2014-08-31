package com.saax.gestorweb.model.datamodel;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


@Converter(autoApply = true)
public class LocalDateTimePersistenceConverter implements   AttributeConverter<LocalDateTime, Timestamp> {
    @Override
    public java.sql.Timestamp convertToDatabaseColumn(LocalDateTime entityValue) {
        if (entityValue == null)  return null;
        return Timestamp.valueOf(entityValue);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(java.sql.Timestamp databaseValue) {
        if (databaseValue == null)  return null;
        return databaseValue.toLocalDateTime();
    }
}