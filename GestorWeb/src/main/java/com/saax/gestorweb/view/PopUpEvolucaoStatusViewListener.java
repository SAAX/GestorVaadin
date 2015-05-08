
package com.saax.gestorweb.view;

import com.saax.gestorweb.model.datamodel.HistoricoTarefa;
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
    
    public void confirmarRecusaClicked();

    public void adiarTarefaClicked();

    public void cancelarTarefaClicked();

    public void removerBloqueioTarefaClicked();
    
    public void removerRecusaTarefaClicked();

    public void aceitarTarefaClicked();
    
    public void recusarTarefaClicked();

    public void reabrirTarefaClicked();

    public void reativarTarefaClicked();

    public void processarAvaliacao();

    public void editarHistorico(HistoricoTarefa historicoTarefa);

    public boolean historicoEditavel(HistoricoTarefa historicoTarefa);

    public void confirmarAlteracaoHistoricoClicked(HistoricoTarefa historicoTarefa);
    
}
