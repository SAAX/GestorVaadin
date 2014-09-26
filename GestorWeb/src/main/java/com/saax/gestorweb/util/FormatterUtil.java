package com.saax.gestorweb.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.text.MaskFormatter;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Rodrigo
 */
public class FormatterUtil {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public static String removeNonDigitChars(String value) {
        if (value == null) {
            return null;
        }

        return value.replaceAll("[^\\d]", "");
    }

    public static String formatString(String value, String pattern) throws ParseException {
        MaskFormatter mf;

        if (value == null || value.equals("")) {
            return null;
        }

        mf = new MaskFormatter(pattern);
        mf.setValueContainsLiteralCharacters(false);
        return mf.valueToString(removeNonDigitChars(value));
    }

    /**
     * Verifica se um CPF está no formato: ###.###.###-##
     * @param cpf
     * @return true se o CPF é válido
     */
    public static boolean validarCPF(String cpf) {

        if (StringUtils.isBlank(cpf)){
            return false;
        }
        
        String cpfRegExp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}";

        return cpf.matches(cpfRegExp);

    }

    /**
     * Verifica se um CNPJ está no formato: ##.###.###/####-##
     * @param cnpj
     * @return true se o CNPJ é válido
     */
    public static boolean validarCNPJ(String cnpj) {

        if (StringUtils.isBlank(cnpj)){
            return false;
        }
        
        String cnpjRegExp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}";

        return cnpj.matches(cnpjRegExp);

    }
    
    
    /**
     * Formata uma data para o padrão da localidade do usuario logado
     * @param date
     * @return 
     */
    public static String formatDate(LocalDate date){
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter); 
        
    }
    
   


}
