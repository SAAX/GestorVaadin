package com.saax.gestorweb.view;

import com.saax.gestorweb.model.datamodel.AnexoTarefa;
import com.saax.gestorweb.model.datamodel.ApontamentoTarefa;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.OrcamentoTarefa;
import com.saax.gestorweb.model.datamodel.ParticipanteTarefa;
import com.saax.gestorweb.model.datamodel.Task;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.vaadin.data.Property;
import com.vaadin.ui.Upload;
import java.io.File;

/**
 *
 * @author rodrigo
 */
public interface TaskViewListener {

    public void avisoButtonClicked();

    public void addSubButtonClicked();

    public void chatButtonClicked();

    public void projecaoButtonClicked();

    public void gravarButtonClicked();

    public void cancelarButtonClicked();

    public void imputarHorasClicked();

    public void imputarOrcamentoClicked();

    public void editar(Task tarefaToEdit);   
    
    public void solicitacaoParaAdicionarAnexo(Upload.StartedEvent event);

    public void apontamentoHorasSwitched(Property.ValueChangeEvent event);
    
    public void controleOrcamentoSwitched(Property.ValueChangeEvent event);
    
    public void setCallBackListener(TaskCreationCallBackListener presenter);
    
    public void criarNovaSubTarefa(Task tarefaPai);

    public void removePointingTime(ApontamentoTarefa apontamentoTarefa);

    public void removerParticipante(ParticipanteTarefa participanteTarefa);

    public void adicionarParticipante(Usuario participanteTarefa);

    public void removerAnexo(AnexoTarefa anexoTarefa);

    public void removerRegistroOrcamento(OrcamentoTarefa orcamentoTarefa);

    public void anexoAdicionado(File anexo);

    public void hierarquiaSelecionada(HierarquiaProjetoDetalhe hierarquiaProjetoDetalhe);

    public void empresaSelecionada(Empresa empresa);
    
    public void recurrenceClicked();   

    public void assigneeUserChanged(Usuario usuario);

}