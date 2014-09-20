package com.saax.gestorweb.util;


import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.TextField;

/** classe valores monetarios */
public class MaskedField extends TextField implements TextChangeListener{    
    private static final long serialVersionUID = 1L;        
    private final StringBuilder CNPJ_FORMAT = new StringBuilder();


    public MaskedField(){
        setWidth("20%");
        setValue("");
        setMaxLength(18);

        setTextChangeEventMode(TextChangeEventMode.EAGER);        
        addTextChangeListener(this);
        selectAll();
    }

    @Override
    public void textChange(TextChangeEvent event) {
        if(!event.getText().trim().isEmpty()){
            if(event.getText().length() == 2){
                CNPJ_FORMAT.setLength(0);
                CNPJ_FORMAT.append(event.getText());                    
                CNPJ_FORMAT.insert(2, ".");
            }else if(event.getText().length() == 6){
                CNPJ_FORMAT.setLength(0);
                CNPJ_FORMAT.append(event.getText());
                CNPJ_FORMAT.insert(6,".");
            }else if(event.getText().length() == 10){
                CNPJ_FORMAT.setLength(0);
                CNPJ_FORMAT.append(event.getText());
                CNPJ_FORMAT.insert(10,"/");
            }else if(event.getText().length() == 15){
                CNPJ_FORMAT.setLength(0);
                CNPJ_FORMAT.append(event.getText());
                CNPJ_FORMAT.insert(15,"-");
            }else{
                CNPJ_FORMAT.setLength(0);
                CNPJ_FORMAT.append(event.getText());
            }
        }else{
            CNPJ_FORMAT.setLength(0);
            setValue("");
        }
        setValue(CNPJ_FORMAT.toString());
    }

   

}