package com.saax.gestorweb.view;

import com.saax.gestorweb.model.datamodel.Tarefa;
import com.vaadin.ui.Upload;

/**
 *
 * @author rodrigo
 */
public interface CadastroTarefaViewListener {

    public void avisoButtonClicked();

    public void addSubButtonClicked();

    public void chatButtonClicked();

    public void projecaoButtonClicked();

    public void gravarButtonClicked();

    public void cancelarButtonClicked();

    public void addParticipante();

    public void anexoAdicionado();

    public void imputarHorasClicked();

    public void imputarOrcamentoClicked();

    public void open(Tarefa tarega);

    public void anexoAdicionado(Upload.FinishedEvent event);

    public void solicitacaoParaAdicionarAnexo(Upload.StartedEvent event);
    
}
