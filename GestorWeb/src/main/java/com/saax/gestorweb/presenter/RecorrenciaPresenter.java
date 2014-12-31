/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.RecorrenciaModel;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.view.RecorrenciaView;
import com.saax.gestorweb.view.RecorrenciaViewListener;
import com.vaadin.data.Property;
import com.vaadin.ui.UI;
import java.util.ResourceBundle;

/**
 * Presenter:
 * <p>
 * Listener de eventos da view das recorrências
 *
 * @author rodrigo
 */
public class RecorrenciaPresenter implements RecorrenciaViewListener {
    
    // Todo presenterPopUpStatus mantem acesso à view e ao model
    private final transient RecorrenciaView view;
    private final transient RecorrenciaModel model;

    // Referencia ao recurso das mensagens:
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();
    private final Usuario usuarioLogado;
/**
     * Cria o presenter PopUpStatusigando o Model ao View
     *
     * @param model
     * @param view
     */
    public RecorrenciaPresenter(RecorrenciaModel model,
            RecorrenciaView view) {

        this.model = model;
        this.view = view;

        view.setListener(this);

        usuarioLogado = (Usuario) GestorSession.getAttribute("usuarioLogado");

    }  
    
     public void open() {
        view.setAbaMensalVisible(false);
        view.setAbaSemanalVisible(false);
        view.setAbaAnualVisible(false);
    }
    
    @Override
    public void recorrenciaSemanal(Property.ValueChangeEvent event) {
        view.setAbaSemanalVisible((boolean) event.getProperty().getValue());
        view.setAbaMensalVisible(false);
        view.getMensalCheckBox().setValue(Boolean.FALSE);
        view.setAbaAnualVisible(false);
        view.getAnualCheckBox().setValue(Boolean.FALSE);
    }
    
    @Override
    public void recorrenciaMensal(Property.ValueChangeEvent event) {
        view.setAbaMensalVisible((boolean) event.getProperty().getValue());
        view.setAbaSemanalVisible(false);
        view.getSemanalCheckBox().setValue(Boolean.FALSE);
        view.setAbaAnualVisible(false);
        view.getAnualCheckBox().setValue(Boolean.FALSE);
    }
    
    @Override
    public void recorrenciaAnual(Property.ValueChangeEvent event) {
        view.setAbaAnualVisible((boolean) event.getProperty().getValue());
         view.setAbaSemanalVisible(false);
         view.getSemanalCheckBox().setValue(Boolean.FALSE);
         view.setAbaMensalVisible(false);
          view.getMensalCheckBox().setValue(Boolean.FALSE);
    }
}
