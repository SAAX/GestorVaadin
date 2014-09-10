package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.CadastroTarefaModel;
import com.saax.gestorweb.model.datamodel.StatusTarefa;
import com.saax.gestorweb.model.datamodel.TipoTarefa;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.view.CadastroTarefaView;
import com.saax.gestorweb.view.CadastroTarefaViewListener;
import com.vaadin.ui.UI;
import java.util.ResourceBundle;

/**
 *
 * @author rodrigo
 */
public class CadastroTarefaPresenter implements CadastroTarefaViewListener {

    // Todo presenter mantem acesso à view e ao model
    private final transient CadastroTarefaView view;
    private final transient CadastroTarefaModel model;

    // Referencia ao recurso das mensagens:
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();

    /**
     * Cria o presenter ligando o Model ao View
     *
     * @param model
     * @param view
     */
    public CadastroTarefaPresenter(CadastroTarefaModel model,
            CadastroTarefaView view) {

        this.model = model;
        this.view = view;

        view.setListener(this);

    }
    
    /**
     * Abre o pop window do cadastro de tarefas
     */
    public void open(){
        
        // Carrega os combos de seleção
        carregaComboStatusTarefa();
        
        UI.getCurrent().addWindow(view);
    }
    
    /**
     * Carrega o combo de seleçao do tipo
     */
    private void carregaComboStatusTarefa(){
        
    }
    
    /**
     * Carrega o combo de seleçao com os status possiveis para a tarefa
     */
    private void carregaComboTipoRecorrenciaTarefa(){
        
        view.getStatusTarefa().addItem(StatusTarefa.ADIADA);
        view.getStatusTarefa().setItemCaption(StatusTarefa.ADIADA, "Única");
    
        view.getStatusTarefa().addItem(TipoTarefa.RECORRENTE);
        view.getStatusTarefa().setItemCaption(TipoTarefa.RECORRENTE, "Recorrente");
    
        
    }
   
    
}
