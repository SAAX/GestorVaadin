package com.saax.gestorweb.util;

import com.vaadin.data.Validator;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.text.MaskFormatter;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Rodrigo
 */
public class FormatterUtil {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final DecimalFormat decimalFormater00 = new DecimalFormat("00");

    public static SimpleDateFormat getDateFormat() {
        return dateFormat;
    }
    
    public static DecimalFormat getDecimalFormat00(){
        return decimalFormater00;
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
     *
     * @param cpf
     * @return true se o CPF é válido
     */
    public static boolean validarCPF(String cpf) {

        if (StringUtils.isBlank(cpf)) {
            return false;
        }

        String cpfRegExp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}";

        return cpf.matches(cpfRegExp);

    }

    /**
     * Verifica se um CNPJ está no formato: ##.###.###/####-##
     *
     * @param cnpj
     * @return true se o CNPJ é válido
     */
    public static boolean validarCNPJ(String cnpj) {

        if (StringUtils.isBlank(cnpj)) {
            return false;
        }

        String cnpjRegExp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}";

        return cnpj.matches(cnpjRegExp);

    }

    /**
     * Formata uma data para o padrão da localidade do usuario logado
     *
     * @param date
     * @return
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);

    }

    /**
     * Formata uma data e hora para o padrão da localidade do usuario logado
     *
     * @param dateTime
     * @return
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dateTime.format(formatter);

    }

    public static String extrairMensagemValidacao(Exception ex) {
        String mensagem = "";
        if (ex.getCause() instanceof Validator.InvalidValueException) {
            Validator.InvalidValueException validationException = (Validator.InvalidValueException) ex.getCause();
            for (Validator.InvalidValueException cause : validationException.getCauses()) {
                mensagem += cause.getMessage() + "\n";
            }
        } else {
            mensagem = ex.getLocalizedMessage();
        }

        if (mensagem == null || mensagem.equals("")) {
            mensagem = "Verifique os campos obrigatórios";
        }

        return mensagem;
        
        
    }

}
