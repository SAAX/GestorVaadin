package com.saax.gestorweb.model.datamodel;

import com.saax.gestorweb.GestorMDI;
import com.vaadin.ui.UI;
import java.util.ResourceBundle;

/**
 * Enumeração dos tipos das tarefas
 *
 * @author rodrigo
 */
public enum TipoTarefa {

    UNICA,
    RECORRENTE;
    
    
    public String getLocalizedString() {
        ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();

        switch (this) {
            case UNICA:
                return mensagens.getString("TipoTarefa.UNICA");
            case RECORRENTE:
                return mensagens.getString("TipoTarefa.RECORRENTE");
            default:
                return this.toString();
        }

    }

}
