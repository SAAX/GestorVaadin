package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.CadastroMetaModel;
import com.saax.gestorweb.model.CadastroTarefaModel;
import com.saax.gestorweb.model.EmpresaModel;
import com.saax.gestorweb.model.PopUpEvolucaoStatusModel;
import com.saax.gestorweb.model.datamodel.CentroCusto;
import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.FormatterUtil;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.view.CadastroMetaCallBackListener;
import com.saax.gestorweb.view.CadastroMetaView;
import com.saax.gestorweb.view.CadastroMetaViewListener;
import com.saax.gestorweb.view.TaskCreationCallBackListener;
import com.saax.gestorweb.view.CadastroTarefaView;
import com.saax.gestorweb.view.PopUpEvolucaoStatusView;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import org.vaadin.hene.popupbutton.PopupButton;

/**
 *
 * @author Rodrigo
 */
public class CadastroMetaPresenter implements Serializable, CadastroMetaViewListener, TaskCreationCallBackListener {

    // Todo presenter mantem acesso à view e ao model
    private final transient CadastroMetaView view;
    private final transient CadastroMetaModel model;

    // recursos de aplicação
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final transient GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();
    
    // usuario logado
    private final Usuario loggedUser;
    private CadastroMetaCallBackListener callbackListener;
    private Meta target;

    /**
     * Cria o presenter ligando o Model ao View
     *
     * @param model
     * @param view
     */
    public CadastroMetaPresenter(CadastroMetaModel model,
            CadastroMetaView view) {

        this.model = model;
        this.view = view;

        loggedUser = (Usuario) GestorSession.getAttribute("loggedUser");

        view.setListener(this);

    }

    /**
     * Cria uma nova meta na categoria informada
     *
     * @param categoria
     */
    public void criarNovaMeta(HierarquiaProjetoDetalhe categoria) {

        target = model.criarNovaMeta(categoria, loggedUser);
        

        // configura a categoria
        ComboBox combo = view.getHierarquiaCombo();
        combo.addItem(target.getCategoria());
        combo.setItemCaption(target.getCategoria(), target.getCategoria().getCategoria());

        init(target);

        view.getHierarquiaCombo().setEnabled(false);
        view.setCaption(mensagens.getString("CadastroMetaView.tituloBase") + categoria.getCategoria());
        
    }

    /**
     * Inicializa o formulário carregando a meta informada
     */
    private void init(Meta target) {

        carregaComboEmpresa();
        carregaComboResponsavel();
        carregaComboEmpresaCliente();

        // Abre o formulário
        UI.getCurrent().addWindow(view);

        // liga (bind) o form com a meta
        view.setMeta(target);
        
        this.target = target;

    }

    /**
     * Carrega o combo de seleção da empresa com todas as empresas relacionadas
     * ao usuário logado
     */
    private void carregaComboEmpresa() {
        ComboBox empresaCombo = view.getEmpresaCombo();

        EmpresaModel empresaModel = new EmpresaModel();

        List<Empresa> empresas = empresaModel.listarEmpresasParaSelecao(loggedUser);
        for (Empresa empresa : empresas) {

            empresaCombo.addItem(empresa);
            empresaCombo.setItemCaption(empresa, empresa.getNome());

        }

    }

    /**
     * Loads the department's combobox with all active company's department or 
     * disable the combo if there is not any active department for this company.
     */
    private void loadDepartmentCombo(Empresa company) {
            
        // Retrieves the combo reference
        ComboBox department = view.getDepartamentoCombo();
            

        // Verify if the company is already set
        if (company != null) {
            
            // Retrieves the list of active departments for this company
            List<Departamento> departmentList = model.obterListaDepartamentosAtivos(company);

            if (departmentList.isEmpty()) {
                
                // if there is not any department: disable and empty the combo
                department.removeAllItems();
                department.setEnabled(false);

            } else {

                // loads the department's list into the combo
                for (Departamento depto : departmentList) {
                    department.addItem(depto);
                    department.setItemCaption(depto, depto.getDepartamento());
                }
            }
        } else {
            
            // if there conmpany has not been setted: disable and empty the combo
            department.removeAllItems();
            department.setEnabled(false);

        }

    }
    
    /**
     * Loads the cost center's combobox with all active company's cc or 
     * disable the combo if there is not any active cost-center for this company.
     */
    private void loadCostCenterCombo(Empresa company) {
            
        // Retrieves the combo reference
        ComboBox costCenterCombo = view.getCentroCustoCombo();
            

        // Verify if the company is already set
        if (company != null) {
            
            // Retrieves the list of active departments for this company
            List<CentroCusto> costCenterList = model.obterListaCentroCustosAtivos(company);

            if (costCenterList.isEmpty()) {
                
                // if there is not any cost center: disable and empty the combo
                costCenterCombo.removeAllItems();
                costCenterCombo.setEnabled(false);

            } else {

                // loads the cost center list into the combo
                for (CentroCusto cc : costCenterList) {
                    costCenterCombo.addItem(cc);
                    costCenterCombo.setItemCaption(cc, cc.getCentroCusto());
                }
            }
        } else {
            
            // if there conmpany has not been setted: disable and empty the combo
            costCenterCombo.removeAllItems();
            costCenterCombo.setEnabled(false);

        }

    }

    /**
     * Carrega o combo de responsáveis com todos os usuários ativos da mesma
     * empresa do usuário logado
     */
    private void carregaComboResponsavel() {
        ComboBox responsavel = view.getResponsavelCombo();
        for (Usuario usuario : model.listarUsuariosEmpresa()) {
            responsavel.addItem(usuario);
            responsavel.setItemCaption(usuario, usuario.getNome());

        }
    }

    /**
     * Carrega o combo de clientes com todos os clientes ativos de todas as
     * empresas (empresa pricipal + subs ) do usuario logado
     */
    private void carregaComboEmpresaCliente() {
        ComboBox empresaCliente = view.getEmpresaClienteCombo();
        for (EmpresaCliente cliente : model.listarEmpresasCliente(loggedUser)) {
            empresaCliente.addItem(cliente);
            empresaCliente.setItemCaption(cliente, cliente.getNome());
        }

    }

    /**
     * Trata o evento disparado ao soelecionar uma empresa e carrega a lista de departamentos
     * @param empresa 
     */
    @Override
    public void empresaSelecionada(Empresa empresa) {
        loadDepartmentCombo(empresa);
        loadCostCenterCombo(empresa);
    }

    /**
     * Configura um listener para ser chamado quando o cadastro for concluido
     *
     * @param callback
     */
    @Override
    public void setCallBackListener(CadastroMetaCallBackListener callback) {
        this.callbackListener = callback;
    }

    @Override
    public void gravarButtonClicked() {
        Meta meta = (Meta) view.getMeta();

        if (meta.getUsuarioResponsavel() == null) {
            meta.setUsuarioResponsavel(meta.getUsuarioInclusao());
        }
        boolean novaMeta = meta.getId() == null;
        meta = model.gravarMeta(meta);

        // notica (se existir) algum listener interessado em saber que o cadastro foi finalizado.
        if (callbackListener != null) {
            if (novaMeta) {
                callbackListener.cadastroMetaConcluido(meta);
            } else {
                callbackListener.edicaoMetaConcluida(meta);
            }
        }
        view.close();
        Notification.show(meta.getCategoria().getCategoria() + mensagens.getString("CadastroMetaPresenter.mensagem.gravadoComSucesso"), Notification.Type.HUMANIZED_MESSAGE);
    }

    @Override
    public void cancelarButtonClicked() {
        UI.getCurrent().removeWindow(view);
    }

    /**
     * Opens the presenter in editing mode with the target to edit
     *
     * @param targetToEdit
     */
    @Override
    public void edit(Meta targetToEdit) {

        // sets the title
        view.exibeTituloEdicao(targetToEdit);

        // configura a categoria
        ComboBox combo = view.getHierarquiaCombo();
        combo.addItem(targetToEdit.getCategoria());
        combo.setItemCaption(targetToEdit.getCategoria(), targetToEdit.getCategoria().getCategoria());

        // inits the UI
        init(targetToEdit);
        
        for (Tarefa task : targetToEdit.getTarefas()) {
            addTaskInTable(task);
        }
        organizeTree(null, targetToEdit.getTarefas());
        view.setCaption(mensagens.getString("CadastroMetaView.tituloBase") + targetToEdit.getCategoria().getCategoria());


    }

    /**
     * Handle the event thrown when the "addTask" button is clicked, indicating that the user wants a new task to the Target.
     * Creates and presents a new form (presenter) to create a new Task under this Target
     */
    @Override
    public void addTaskButtonClicked() {
            
        try {
            // commit and run validators
            view.setValidatorsVisible(true);
            view.getMetaFieldGroup().commit();
            
            // Creates the presenter that will handle the new task creation
            CadastroTarefaPresenter presenter = new CadastroTarefaPresenter(new CadastroTarefaModel(), new CadastroTarefaView());
            
            // Configure this as the object to be called when the task creation was done
            presenter.setCallBackListener(this);
            
            // Gets the tasks categories from the Target category
            List<HierarquiaProjetoDetalhe> tasksCategories = model.getFirstsTaskCategories(view.getMeta().getCategoria());
            
            // Tells the presenter which is gonna be the Task's category
            presenter.createTask(target, tasksCategories);
            
        } catch (FieldGroup.CommitException ex) {
            Notification.show(mensagens.getString("CadastroMetaPresenter.addTaskButtonClicked.commitException"), Notification.Type.HUMANIZED_MESSAGE);
        }
    }

    /**
     * Event thrown when the "chat" button is clicked to start or continue a conversation between the stackholders of the Target
     */
    @Override
    public void chatButtonClicked() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Event thrown when the "chat" button is clicked to start or continue a conversation between the stackholders of the Target
     */
    @Override
    public void forecastButtonClickedd() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    /**
     * Handles the event thrown when the sub window is done with a Task creation 
     * @param createdTask 
     * @see TaskCreationCallBackListener
     */
    @Override
    public void taskCreationDone(Tarefa createdTask) {

        addTaskInTable(createdTask);
        organizeTree(createdTask, createdTask.getSubTarefas());
    }

    private void organizeTree(Tarefa parentTask, List<Tarefa> subTasks) {

        
        for (Tarefa subTask : subTasks) {
            view.getTarefasTable().setParent(subTask, parentTask);
            if (subTask.getSubTarefas()!=null && !subTask.getSubTarefas().isEmpty()){
                organizeTree(subTask, subTask.getSubTarefas());
            }
        }
    }

    private void addTaskInTable(Tarefa task){
        Object[] gridRow = new Object[]{
            CadastroTarefaView.buildButtonOpenTask(this, view.getTarefasTable(), task, task.getGlobalID()),
            CadastroTarefaView.buildButtonOpenTask(this, view.getTarefasTable(), task, task.getHierarquia().getCategoria()),
            CadastroTarefaView.buildButtonOpenTask(this, view.getTarefasTable(), task, task.getNome()),
            task.getEmpresa().getNome()
            + (task.getFilialEmpresa() != null ? "/" + task.getFilialEmpresa().getNome() : ""),
            task.getUsuarioSolicitante().getNome(),
            task.getUsuarioResponsavel().getNome(),
            FormatterUtil.formatDate(task.getDataInicio()),
            FormatterUtil.formatDate(task.getDataFim()),
            CadastroTarefaView.buildPopUpStatusProgressTask(view.getTarefasTable(), task),
            task.getProjecao().toString().charAt(0),
            new Button("E"),
            new Button("C")

        };
        view.getTarefasTable().addItem(gridRow, task);
        
        
        for (Tarefa subTarefa : task.getSubTarefas()) {
            addTaskInTable(subTarefa);
        }
        
    }
    

    /**
     * @see TaskCreationCallBackListener
     */
    @Override
    public void taskUpdateDone(Tarefa updatedTask) {
        
        atualizarTarefaTable(updatedTask);
        organizeTree(updatedTask, updatedTask.getSubTarefas());

    }
    
    private void atualizarTarefaTable(Tarefa task) {
        Item it = view.getTarefasTable().getItem(task);

        it.getItemProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaCod")).setValue(CadastroTarefaView.buildButtonOpenTask(this, view.getTarefasTable(), task, task.getGlobalID()));
        it.getItemProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaTitulo")).setValue(CadastroTarefaView.buildButtonOpenTask(this, view.getTarefasTable(), task, task.getHierarquia().getCategoria()));
        it.getItemProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaNome")).setValue(CadastroTarefaView.buildButtonOpenTask(this, view.getTarefasTable(), task, task.getNome()));
        it.getItemProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaEmpresaFilial")).setValue(task.getEmpresa().getNome()
                + (task.getFilialEmpresa() != null ? "/" + task.getFilialEmpresa().getNome() : ""));
        it.getItemProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaSolicitante")).setValue(task.getUsuarioSolicitante().getNome());
        it.getItemProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaResponsavel")).setValue(task.getUsuarioResponsavel().getNome());
        it.getItemProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaDataInicio")).setValue(FormatterUtil.formatDate(task.getDataInicio()));
        it.getItemProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaDataFim")).setValue(FormatterUtil.formatDate(task.getDataInicio()));
        it.getItemProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaStatus")).setValue(CadastroTarefaView.buildPopUpStatusProgressTask(view.getTarefasTable(), task));
        it.getItemProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaProjecao")).setValue(task.getProjecao().toString().charAt(0));
        it.getItemProperty("[E]").setValue(new Button("E"));
        it.getItemProperty("[C]").setValue(new Button("C"));

        for (Tarefa subTarefa : task.getSubTarefas()) {
            addTaskInTable(subTarefa);
        }

    }


}
