
package com.saax.gestorweb.view;

import com.saax.gestorweb.model.datamodel.HistoricoTarefa;
import com.saax.gestorweb.model.datamodel.ParametroAndamentoTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import java.util.List;
import org.vaadin.hene.popupbutton.PopupButton;

/**
 *
 * @author rodrigo
 */
public interface PopUpStatusViewListener {

    public void load(Tarefa tarefa, PopupButton statusButton, PopUpStatusListener listener);
    
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

    public List<ParametroAndamentoTarefa> listAndamento();
    
}
