package com.saax.gestorweb.view.validator;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.util.DateTimeConverters;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.UI;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Validator que certifica que data de fim é posterior ou igual a data de inicio
 * de um dado período
 *
 * @author rodrigo
 */
public class DataFimValidator implements Validator {

    private final String dataFimCaption;
    private final PopupDateField dataInicioDateField;
    private final transient ResourceBundle messages = ((GestorMDI) UI.getCurrent()).getMensagens();

    /**
     * Cria o validador
     *
     * @param dataInicioDateField data inicio para comparacao
     * @param dataFimCaption nome do campo data Fim 
     */
    public DataFimValidator(PopupDateField dataInicioDateField, String dataFimCaption) {
        this.dataFimCaption = dataFimCaption;
        this.dataInicioDateField = dataInicioDateField;
    }

    @Override
    public void validate(Object value) throws InvalidValueException {

        if (value == null) {
            return;
        }

        LocalDate dataFim = (LocalDate) value;

        if (dataInicioDateField != null && dataInicioDateField.getValue() != null) {
            LocalDate dataInicio = DateTimeConverters.toLocalDate(dataInicioDateField.getValue());

            // se a data for menor ou igual que a data atual
            if (dataInicio.isAfter(dataFim)) {
                throw new InvalidValueException(dataFimCaption + messages.getString("DataFimValidator.errorMessage") + dataInicio);
            }

        }
    }
}
