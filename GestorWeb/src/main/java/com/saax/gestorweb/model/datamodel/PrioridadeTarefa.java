package com.saax.gestorweb.model.datamodel;

import com.saax.gestorweb.GestorMDI;
import com.vaadin.ui.UI;
import java.util.ResourceBundle;

/**
 * Enumeração das prioridades das tarefas
 *
 * @author rodrigo
 */
public enum PrioridadeTarefa {

    BAIXA,
    NORMAL,
    ALTA;

    public String getLocalizedString() {
        ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();

        switch (this) {
            case ALTA:
                return mensagens.getString("PrioridadeTarefa.ALTA");
            case NORMAL:
                return mensagens.getString("PrioridadeTarefa.NORMAL");
            case BAIXA:
                return mensagens.getString("PrioridadeTarefa.BAIXA");
            default:
                return this.toString();
        }

    }
}
