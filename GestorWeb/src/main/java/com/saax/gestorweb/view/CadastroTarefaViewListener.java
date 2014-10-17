package com.saax.gestorweb.view;

import com.saax.gestorweb.model.datamodel.AnexoTarefa;
import com.saax.gestorweb.model.datamodel.ApontamentoTarefa;
import com.saax.gestorweb.model.datamodel.OrcamentoTarefa;
import com.saax.gestorweb.model.datamodel.ParticipanteTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.vaadin.data.Property;
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

    public void anexoAdicionado();

    public void imputarHorasClicked();

    public void imputarOrcamentoClicked();

    public void editar(Tarefa tarefaToEdit);

    public void anexoAdicionado(Upload.FinishedEvent event);

    public void solicitacaoParaAdicionarAnexo(Upload.StartedEvent event);

    public void apontamentoHorasSwitched(Property.ValueChangeEvent event);

    public void controleOrcamentoSwitched(Property.ValueChangeEvent event);
    
    public void setCallBackListener(CadastroTarefaCallBackListener presenter);
    
    public void criarNovaTarefa();
    
    public void criarSubTarefa(Tarefa tarefaPai);

    public void removerApontamentoHoras(ApontamentoTarefa apontamentoTarefa);

    public void removerParticipante(ParticipanteTarefa participanteTarefa);

    public void adicionarParticipante(ParticipanteTarefa participanteTarefa);

    public void removerAnexo(AnexoTarefa anexoTarefa);

    public void removerRegistroOrcamento(OrcamentoTarefa orcamentoTarefa);
}
