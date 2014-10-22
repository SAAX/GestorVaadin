package com.saax.gestorweb.model.datamodel;

import java.time.Duration;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author rodrigo
 */
@Converter(autoApply = true)
public class DurationConverter implements AttributeConverter<Duration, Long> {
    
    
@Override
    public Long convertToDatabaseColumn(Duration entityValue) {
        if (entityValue == null)  return null;
        
        return entityValue.toMinutes();
    }

    @Override
    public Duration convertToEntityAttribute(Long databaseValue) {
        if (databaseValue == null)  return null;
        return Duration.ofMinutes(databaseValue);
    }
}