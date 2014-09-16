package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.CadastroTarefaModel;
import com.saax.gestorweb.model.dashboard.PopUpEvolucaoStatusModel;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.presenter.dashboard.PopUpEvolucaoStatusPresenter;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.view.CadastroTarefaView;
import com.saax.gestorweb.view.CadastroTarefaViewListener;
import com.saax.gestorweb.view.dashboard.PopUpEvolucaoStatusView;
import com.vaadin.ui.ComboBox;
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

    // 
    private Tarefa tarefa;
    
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
     * @param tarefa
     */
    public void open(Tarefa tarefa){
        
        this.tarefa = (tarefa == null) ? new Tarefa() : tarefa;
        
        
        // Carrega os combos de seleção
        carregaComboEmpresa();
        carregaComboTipoRecorrenciaTarefa();
        carregaComboPrioridade();
        carregaComboResponsavel();
        carregaComboParticipante();
        carregaComboEmpresaCliente();
        
        // configura o componente de status e andamento
        setPopUpEvolucaoStatusEAndamento();
        
        // 
        carregaTabelaParticipantes();

        view.ocultarAbaControleHoras();
        
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
        
        ComboBox tipo = view.getTipoRecorrenciaCombo();
        // TODO: ...
    
        
    }

    private void carregaComboEmpresa() {
        ComboBox empresa = view.getEmpresaCombo();
        // TODO: ...
    }

    private void carregaComboPrioridade() {
        ComboBox prioridade = view.getPrioridadeCombo();
        // TODO: ...
    }

    private void carregaComboResponsavel() {
        ComboBox responsavel = view.getResponsavelCombo();
        // TODO: ...
    }
   
    private void carregaComboParticipante() {
        ComboBox participante = view.getParticipantesCombo();
        // TODO: ...
    }
   
    private void carregaTabelaParticipantes(){
        
    }
    /**
     * Constrói o pop up de alteração de status e/ou andamento de tarefas neste
     * PopUP o usuario poderá alterar (evoluir ou regredir) um status de tarefa
     * ou indicar seu andamento.
     *
     * @return
     */
    private void setPopUpEvolucaoStatusEAndamento() {

        // comportmento e regras:
        PopUpEvolucaoStatusView viewPopUP = new PopUpEvolucaoStatusView();
        PopUpEvolucaoStatusModel modelPopUP = new PopUpEvolucaoStatusModel();

        PopUpEvolucaoStatusPresenter presenter = new PopUpEvolucaoStatusPresenter(viewPopUP, modelPopUP);

        presenter.load(tarefa, view.getStatusTarefaPopUpButton());

    }

    private void carregaComboEmpresaCliente() {
            ComboBox empresaCliente = view.getEmpresaClienteCombo();
        // TODO: ...
    }

    @Override
    public void avisoButtonClicked() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addSubButtonClicked() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void chatButtonClicked() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void projecaoButtonClicked() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void gravarButtonClicked() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cancelarButtonClicked() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addParticipante() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
