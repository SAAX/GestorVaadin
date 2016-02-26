package com.saax.gestorweb.view.validator;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.presenter.GestorPresenter;
import com.saax.gestorweb.util.DateTimeConverters;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.UI;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Validator que certifica que data de inicio é anterior ou igual a data de fim
 * de um dado período
 *
 * @author rodrigo
 */
public class DataInicioValidator implements Validator {

    private final String dataInicioCaption;
    private final PopupDateField dataFimDateField;
    private final transient ResourceBundle messages = GestorPresenter.getMENSAGENS();

    /**
     * Cria o validador
     *
     * @param dataFim data fim para comparacao
     * @param dataInicioCaption nome do campo
     */
    public DataInicioValidator(PopupDateField dataFim, String dataInicioCaption) {
        this.dataInicioCaption = dataInicioCaption;
        this.dataFimDateField = dataFim;
    }

    @Override
    public void validate(Object value) throws InvalidValueException {

        if (value == null) {
            return;
        }

        LocalDate dataInicio = (LocalDate) value;

        if (dataFimDateField != null && dataFimDateField.getValue() != null) {
            LocalDate dataFim = DateTimeConverters.toLocalDate(dataFimDateField.getValue());

            // se a data for menor ou igual que a data atual
            if (dataInicio.isAfter(dataFim)) {
                throw new InvalidValueException(dataInicioCaption + messages.getString("DataInicioValidator.errorMessage") + dataFim);
            }

        }
    }
}
