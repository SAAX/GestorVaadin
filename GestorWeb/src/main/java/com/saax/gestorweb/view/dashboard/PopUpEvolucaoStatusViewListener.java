
package com.saax.gestorweb.view.dashboard;

import com.saax.gestorweb.model.datamodel.Tarefa;
import org.vaadin.hene.popupbutton.PopupButton;

/**
 *
 * @author rodrigo
 */
public interface PopUpEvolucaoStatusViewListener {

    public void load(Tarefa tarefa, PopupButton statusButton);
    
    public void load(Tarefa tarefa);

    public void processarAlteracaoAndamento();

    public void bloquearTarefaClicked();

    public void historicoTarefaClicked();

    public void confirmarBloqueioClicked();

    public void adiarTarefaClicked();

    public void cancelarTarefaClicked();

    public void removerBloqueioTarefaClicked();

    public void aceitarTarefaClicked();

    public void reabrirTarefaClicked();

    public void reativarTarefaClicked();

    public void processarAvaliacao();
    
}
