/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.CadastroMetasModel;
import com.saax.gestorweb.view.CadastroMetasView;
import com.saax.gestorweb.view.CadastroMetasViewListener;
import com.vaadin.ui.UI;
import java.util.ResourceBundle;

/**
 *
 * @author Rodrigo
 */
public class CadastroMetasPresenter implements CadastroMetasViewListener {
    
    
    // Todo presenter mantem acesso à view e ao model
    private final CadastroMetasView view;
    private final CadastroMetasModel model;

    // Referencia ao recurso das mensagens:
    private final ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();

    /**
     * Cria o presenter ligando o Model ao View
     *
     * @param model
     * @param view
     */
    public CadastroMetasPresenter(CadastroMetasModel model,
            CadastroMetasView view) {

        this.model = model;
        this.view = view;

        view.setListener(this);
    }
}
