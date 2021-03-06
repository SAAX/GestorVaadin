/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.view.validator;

import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import java.time.LocalDate;

/**
 *
 * @author rodrigo
 */
public class DataFuturaValidator implements Validator {

    private final String campo;
    private final boolean incluirHoje;
    private final LocalDate startDate;

    /**
     * Cria um validator para datas do tipo LocalDate
     * @param incluirHoje se true permite data maior ou igual a hoje
     * @param campo nome do campo
     */
    public DataFuturaValidator(LocalDate startDate, boolean incluirHoje, String campo) {
        this.campo = campo;
        this.incluirHoje = incluirHoje;
        if (startDate==null){
            this.startDate = LocalDate.now();
        }else{
            this.startDate = startDate;
        }
    }

    @Override
    public void validate(Object value) throws InvalidValueException {

        if (value == null) {
            return;
        }

        LocalDate data = (LocalDate) value;

        if (incluirHoje){
            // se a data for menor que a data atual
            if (data.isBefore(startDate)) {
                throw new InvalidValueException(campo + " deve ser maior ou igual a " + LocalDate.now());
            }
            
        } else {
            // se a data for menor ou igual que a data atual
            if (data.isBefore(startDate)||data.isEqual(startDate)) {
                throw new InvalidValueException(campo + " deve ser maior que " + LocalDate.now());
            }
            
        }
        
    }
}
