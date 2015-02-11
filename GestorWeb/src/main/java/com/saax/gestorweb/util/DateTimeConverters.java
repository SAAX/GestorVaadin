package com.saax.gestorweb.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * Classe utilitária para conversores de data e hora
 * @author rodrigo
 */
public class DateTimeConverters {

    
    /**
     * Converte LocalDate para Date
     * @param localDate
     * @return Date ou null
     */
    public static Date toDate(LocalDate localDate) {
        if (localDate==null) return null;
        Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Date res = Date.from(instant);
        return res;
    }
    
    
    
    
    /**
     * Converte Date para LocalDate
     * @param date
     * @return LocalDate ou null
     */
    public static LocalDate toLocalDate(Date date) {
        if (date==null) return null;
        
        Instant instant = Instant.ofEpochMilli(date.getTime());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();

    }
    
    
}
