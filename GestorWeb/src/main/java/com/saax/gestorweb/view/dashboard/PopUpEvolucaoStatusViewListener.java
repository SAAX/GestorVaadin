
package com.saax.gestorweb.view.dashboard;

import com.saax.gestorweb.model.datamodel.Tarefa;

/**
 *
 * @author rodrigo
 */
public interface PopUpEvolucaoStatusViewListener {
    
    public void load(Tarefa tarefa);

    public void processarAlteracaoAndamento();

    public void concluirTarefaClicked();

    public void bloquearTarefaClicked();

    public void historicoTarefaClicked();
    
}
