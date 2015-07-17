/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.presenter;

import com.saax.gestorweb.model.ProcessoDemoradoModel;
import com.saax.gestorweb.view.ProcessoDemoradoView;
import com.saax.gestorweb.view.ProcessoDemoradoViewListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 *
 * @author rodrigo
 */
public class ProcessoDemoradoPresenter implements ProcessoDemoradoViewListener {

    // Todo presenter mantem acesso à view e ao model
    private final transient ProcessoDemoradoView view;
    private final transient ProcessoDemoradoModel model;

    public ProcessoDemoradoPresenter(ProcessoDemoradoView view, ProcessoDemoradoModel model) {
        this.view = view;
        this.model = model;
        view.setListener(this);
    }

    
    
    private final Thread executaProcessoDemoradoNoServidor = new Thread() {
            public void run() {
                model.executarProcessoDemoradoNoServidor();
                UI.getCurrent().removeWindow(view);
            }
        };


    public void executarProcessoDemorado() {
        
        VaadinSession.getCurrent().setAttribute("interromper", Boolean.FALSE);
        
        
        UI.getCurrent().addWindow(view);
        executaProcessoDemoradoNoServidor.start();
        
    }

    @Override
    public void interromper(){
        VaadinSession.getCurrent().setAttribute("interromper", Boolean.TRUE);
        UI.getCurrent().removeWindow(view);
    }
}
