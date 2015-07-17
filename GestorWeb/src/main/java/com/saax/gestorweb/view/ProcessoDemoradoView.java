/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.view;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 *
 * @author rodrigo
 */
public class ProcessoDemoradoView  extends Window {
    
    private final Button interromperButton;
    private final Label mensagem;
    private ProcessoDemoradoViewListener listener;

    public void setListener(ProcessoDemoradoViewListener listener) {
        this.listener = listener;
    }
    
    

    public ProcessoDemoradoView() {
        setCaption("Aguarde. Processo demorado em andamento.");
        setWidth("350");
        setHeight("100");
        setModal(true);
    
        VerticalLayout container = new VerticalLayout();
        
        container.setSizeFull();
        
        mensagem =  new Label("Gerando relatório XYZ");
        
        container.addComponent(mensagem);
        
        interromperButton = new Button("Interromper", (Button.ClickEvent event) -> {
            listener.interromper();
        });
        
        container.addComponent(interromperButton);
        
        setContent(container);
                
        
    }
    
    
    
}
