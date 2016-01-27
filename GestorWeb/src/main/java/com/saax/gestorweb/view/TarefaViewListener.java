package com.saax.gestorweb.view;

import com.saax.gestorweb.model.datamodel.Anexo;
import com.saax.gestorweb.model.datamodel.ApontamentoTarefa;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.OrcamentoTarefa;
import com.saax.gestorweb.model.datamodel.Participante;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.presenter.TarefaPresenter;
import com.vaadin.data.Property;
import com.vaadin.ui.Upload;
import java.io.File;
import java.util.List;

/**
 *
 * @author rodrigo
 */
public interface TarefaViewListener {

    public void addSubButtonClicked(Tarefa tarefa);

    public void chatButtonClicked(Tarefa tarefa);

    public void projecaoButtonClicked();

    public void gravarButtonClicked(Tarefa tarefa);

    public void cancelarButtonClicked();

    public void imputarHorasClicked(Tarefa tarefa);

    public void imputarOrcamentoClicked(Tarefa tarefa);

    public void editar(Tarefa tarefaToEdit);   
    
    public void solicitacaoParaAdicionarAnexo(Upload.StartedEvent event);

    public void apontamentoHorasSwitched(Property.ValueChangeEvent event);
    
    public void controleOrcamentoSwitched(Property.ValueChangeEvent event);
    
    public void criarNovaSubTarefa(List<HierarquiaProjetoDetalhe> proximasCategorias, Tarefa tarefaPai);

    public void removePointingTime(ApontamentoTarefa apontamentoTarefa);

    public void removerParticipante(Participante participanteTarefa);

    public void adicionarParticipante(Tarefa tarefa, Usuario participanteTarefa);

    public void removerAnexo(Anexo anexoTarefa);

    public void removerRegistroOrcamento(OrcamentoTarefa orcamentoTarefa);

    public void anexoAdicionado(Tarefa tarefa, File anexo);

    public void hierarquiaSelecionada(HierarquiaProjetoDetalhe hierarquiaProjetoDetalhe);

    public void empresaSelecionada(Tarefa tarefa, Empresa empresa);
    
    public void recurrenceClicked(Tarefa tarefa);   

    public void assigneeUserChanged(Tarefa task, Usuario usuario);

    public void removerTarefaButtonClicked(Tarefa tarefa);

    public void alteraCustoHoraClicked(Tarefa tarefa);

    public void custoHoraApontamentoValueChage();

}
