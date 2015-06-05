package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.TaskModel;
import com.saax.gestorweb.model.ChatSingletonModel;
import com.saax.gestorweb.model.CompanyModel;
import com.saax.gestorweb.model.PopUpStatusModel;
import com.saax.gestorweb.model.RecurrencyModel;
import com.saax.gestorweb.model.datamodel.AndamentoTarefa;
import com.saax.gestorweb.model.datamodel.AnexoTarefa;
import com.saax.gestorweb.model.datamodel.ApontamentoTarefa;
import com.saax.gestorweb.model.datamodel.CentroCusto;
import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.OrcamentoTarefa;
import com.saax.gestorweb.model.datamodel.ParticipanteTarefa;
import com.saax.gestorweb.model.datamodel.PrioridadeTarefa;
import com.saax.gestorweb.model.datamodel.ProjecaoTarefa;
import com.saax.gestorweb.model.datamodel.RecurrencySet;
import com.saax.gestorweb.model.datamodel.StatusTarefa;
import com.saax.gestorweb.model.datamodel.Task;
import com.saax.gestorweb.model.datamodel.TipoTarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.DateTimeConverters;
import com.saax.gestorweb.util.FormatterUtil;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.view.TaskCreationCallBackListener;
import com.saax.gestorweb.view.TaskView;
import com.saax.gestorweb.view.TaskViewListener;
import com.saax.gestorweb.view.ChatView;
import com.saax.gestorweb.view.PopUpStatusListener;
import com.saax.gestorweb.view.PopUpStatusView;
import com.saax.gestorweb.view.RecurrencyView;
import com.saax.gestorweb.view.RecurrencyDoneCallBackListener;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.converters.DateTimeConverter;

/**
 * Presenter:
 * <p>
 * Listener de eventos da view do cadastro de tarefas
 *
 * @author rodrigo
 */
public class TaskPresenter implements Serializable, TaskViewListener, TaskCreationCallBackListener, RecurrencyDoneCallBackListener, PopUpStatusListener {

    // Todo presenterPopUpStatus mantem acesso à view e ao model
    private final transient TaskView view;
    private final transient TaskModel model;

    // Referencia ao recurso das mensagens:
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final transient GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();
    private TaskCreationCallBackListener callbackListener;
    private final Usuario loggedUser;
    private PopUpStatusPresenter presenterPopUpStatus;
    private List<LocalDate> recurrentDates;
    private RecurrencyPresenter RecorrencyPresenter;
    private String recurrencyMessage;

    /**
     * Cria o presenterPopUpStatus ligando o Model ao View
     *
     * @param model
     * @param view
     */
    public TaskPresenter(TaskModel model,
            TaskView view) {

        this.model = model;
        this.view = view;

        view.setListener(this);

        loggedUser = (Usuario) GestorSession.getAttribute("loggedUser");

    }

    /**
     * Abre o pop window do cadastro de tarefas para criação de uma sub tarefa
     *
     * @param tarefaPai
     */
    @Override
    public void criarNovaSubTarefa(Task tarefaPai) {

        Task tarefa;

        // Cria uma nova tarefa com valores default
        tarefa = new Task();
        tarefa.setStatus(StatusTarefa.NAO_ACEITA);
        tarefa.setEmpresa(loggedUser.getEmpresaAtiva());
        tarefa.setUsuarioInclusao(loggedUser);
        tarefa.setUsuarioSolicitante(loggedUser);
        tarefa.setDataHoraInclusao(LocalDateTime.now());

        // configuras as caracteristicas de sub-tarefa
        tarefa.setTarefaPai(tarefaPai);
        tarefa.setEmpresa(tarefaPai.getEmpresa());
        view.getCompanyCombo().setEnabled(false);
        if (tarefaPai.getSubTarefas() == null) {
            tarefaPai.setSubTarefas(new ArrayList<>());
        }
        tarefaPai.getSubTarefas().add(tarefa);
        view.getChatButton().setVisible(false);
        view.getProjectionButton().setVisible(false);

        // ajuste ate a projecao ser implementada
        tarefa.setProjecao(ProjecaoTarefa.NORMAL);

        // configura a categoria da sub-tarefa
        List<HierarquiaProjetoDetalhe> proximasCategorias = model.getProximasCategorias(tarefaPai);
        StringBuilder nomesProximasCategorias = new StringBuilder();
        for (HierarquiaProjetoDetalhe proximaCategoria : proximasCategorias) {
            ComboBox combo = view.getHierarchyCombo();
            combo.addItem(proximaCategoria);
            combo.setItemCaption(proximaCategoria, proximaCategoria.getCategoria());
            nomesProximasCategorias.append(proximasCategorias.indexOf(proximaCategoria) == 0 ? "" : "/");
            nomesProximasCategorias.append(proximaCategoria.getCategoria());
        }
        // caso seja apenas uma, já seta para facilitar ao usuario
        if (proximasCategorias.size() == 1) {
            tarefa.setHierarquia(proximasCategorias.get(0));
        }

        view.ocultaPopUpEvolucaoStatusEAndamento();

        view.setCaption(mensagens.getString("TaskView.titulo.cadastro") + nomesProximasCategorias);

        init(tarefa);

    }

    /**
     * Creates a new default Task, with a specific category Just overloaded:
     * createTask(List) <br>
     *
     *
     * @param category in wich the task'll be created
     */
    public void createTask(HierarquiaProjetoDetalhe category) {
        // builds a list to use the main method: createTask
        List<HierarquiaProjetoDetalhe> categories = new ArrayList<>();
        categories.add(category);
        // call the main method to create a task
        createTask(null, categories);
    }

    /**
     * Creates a new default Task, with given possible categories
     *
     * @param target the main target to be attached with the new task
     * @param possibleCategories
     */
    public void createTask(Meta target, List<HierarquiaProjetoDetalhe> possibleCategories) {

        Task tarefa;

        // Cria uma nova tarefa com valores default
        tarefa = new Task();
        tarefa.setStatus(StatusTarefa.NAO_ACEITA);
        tarefa.setEmpresa(loggedUser.getEmpresaAtiva());
        tarefa.setUsuarioInclusao(loggedUser);
        tarefa.setUsuarioSolicitante(loggedUser);
        tarefa.setDataHoraInclusao(LocalDateTime.now());
        tarefa.setSubTarefas(new ArrayList<>());
        if (possibleCategories.size() == 1) {
            tarefa.setHierarquia(possibleCategories.get(0));
        }

        model.attachTaskToTarget(tarefa, target);

        // ajuste ate a projecao ser implementada
        tarefa.setProjecao(ProjecaoTarefa.NORMAL);

        // configura a categoria
        ComboBox combo = view.getHierarchyCombo();
        for (HierarquiaProjetoDetalhe categoria : possibleCategories) {
            combo.addItem(categoria);
            combo.setItemCaption(categoria, categoria.getCategoria());
        }

        view.ocultaPopUpEvolucaoStatusEAndamento();

        if (possibleCategories.size() == 1) {
            view.setCaption(mensagens.getString("TaskView.titulo.cadastro") + possibleCategories.get(0).getCategoria());
            view.getHierarchyCombo().setEnabled(false);
        }

        view.getChatButton().setVisible(false);
        view.getProjectionButton().setVisible(false);

        init(tarefa);

    }

    /**
     * Abre o pop window do cadastro de tarefas para edição da tarefa informada
     *
     * @param taskToEdit
     */
    @Override
    public void editar(Task taskToEdit) {

        if (!model.userHasAccessToTask(loggedUser, taskToEdit)) {
            Notification.show(mensagens.getString("TaskView.accessDenied"), Notification.Type.WARNING_MESSAGE);
            return;
        }

        taskToEdit = model.refresh(taskToEdit);

        init(taskToEdit);

        for (Task sub : taskToEdit.getSubTarefas()) {
            adicionarSubTarefa(sub);
        }

        if (taskToEdit.getUsuarioResponsavel().equals(taskToEdit.getUsuarioSolicitante())) {
            view.getChatButton().setEnabled(false);
        }

        //Caso usuário logado não seja o solicitante, não deixar que ele clique nos controle de horas
        //e apontamento da tarefa
        if (!loggedUser.equals(taskToEdit.getUsuarioSolicitante())) {
            view.getApontamentoHorasCheckBox().setReadOnly(true);
            view.getBudgetControlCheckBox().setReadOnly(true);

        }

        organizeTree(taskToEdit, taskToEdit.getSubTarefas());

        // configura a categoria
        ComboBox combo = view.getHierarchyCombo();
        combo.addItem(taskToEdit.getHierarquia());
        combo.setItemCaption(taskToEdit.getHierarquia(), taskToEdit.getHierarquia().getCategoria());

        view.getFollowersContainer().addAll(taskToEdit.getParticipantes());
        view.getTaskAttachContainer().addAll(taskToEdit.getAnexos());
        view.getHoursControlContainer().addAll(taskToEdit.getApontamentos());
        view.getBudgetContainer().addAll(taskToEdit.getOrcamentos());

        configPermissions(taskToEdit);

        view.setCaption(mensagens.getString("TaskView.titulo.edicao") + taskToEdit.getHierarquia().getCategoria());

    }

    /**
     * Config the view with the access permissions
     *
     * @param tarefaToEdit
     */
    private void configPermissions(Task task) {

        boolean loggedUserIsTheAssignee = task.getUsuarioResponsavel() != null && task.getUsuarioResponsavel().equals(loggedUser);
        boolean loggedUserIsTheRequestor = task.getUsuarioSolicitante() != null && task.getUsuarioSolicitante().equals(loggedUser);

        switch (task.getStatus()) {

            case ADIADA:
                view.setEditAllowed(loggedUserIsTheRequestor);

                break;

            case AVALIADA:
                view.setEditAllowed(false);

                break;

            case BLOQUEADA:
                view.setEditAllowed(false);

                break;

            case CANCELADA:
                view.setEditAllowed(false);

                break;

            case CONCLUIDA:
                view.setEditAllowed(false);

                break;

            case EM_ANDAMENTO:
                view.setEditAllowed(loggedUserIsTheRequestor || loggedUserIsTheAssignee);

                break;

            case NAO_ACEITA:
                view.setEditAllowed(loggedUserIsTheRequestor);

                break;

            case NAO_INICIADA:
                view.setEditAllowed(loggedUserIsTheRequestor || loggedUserIsTheAssignee);

                break;

            case RECUSADA:
                view.setEditAllowed(false);

                break;

            default:
                throw new RuntimeException("Falha ao identificar status.");
        }
    }

    /**
     * inicializa a gui
     */
    private void init(Task tarefa) {
        // Carrega os combos de seleção
        carregaComboEmpresa();
        carregaComboPrioridade();
        carregaComboResponsavel();
        carregaComboParticipante();
        carregaComboEmpresaCliente();
        setPopUpEvolucaoStatusEAndamento(tarefa);
        carregaApontamento(tarefa);
        view.getRecurrencyMessage().setVisible(tarefa.getTipoRecorrencia() == TipoTarefa.RECORRENTE);
        view.getRecurrencyMessage().setEnabled(false);

        // Configuras os beans de 1-N
        view.setApontamentoTarefa(new ApontamentoTarefa(tarefa, loggedUser));
        view.setOrcamentoTarefa(new OrcamentoTarefa(tarefa, loggedUser));

        view.setAbaControleHorasVisible(tarefa.isApontamentoHoras());
        view.setAbaControleOrcamentoVisible(tarefa.isOrcamentoControlado());

        UI.getCurrent().addWindow(view);

        view.setTarefa(tarefa);

    }

    /**
     * Carrega os apontamentos e executa o método para o cálculo da projeção
     */
    private void carregaApontamento(Task tarefa) {
        List<AndamentoTarefa> andamentos = tarefa.getAndamentos();
        if (andamentos.size() != 0) {
            //Buscando o andamento da tarefa    
            int andamento = tarefa.getAndamento();
            System.out.println("Andamento Atual " + tarefa.getAndamento());

            Date inicio = DateTimeConverters.toDate(tarefa.getDataInicio());
            Date fim = DateTimeConverters.toDate(tarefa.getDataFim());
            Date hoje = DateTimeConverters.toDate(LocalDate.now());

            int diasRealizar = contarDias(inicio, fim);
            System.out.println("Dias para realizar :" + diasRealizar);

            int diasCorridos = contarDias(inicio, hoje);
            System.out.println("Dias corridos :" + diasCorridos);

            //Porcentagem ideal até o momento
            Double porcIdeal = ((100.00 / diasRealizar) * (diasCorridos));
            System.out.println("Porcentagem ideal é: " + porcIdeal);

            if (andamento == 0) {
                System.out.println("Não Iniciada");
                view.getProjectionButton().setCaption("Não Iniciada");
            } else if (andamento < porcIdeal) {
                System.out.println("Andamento Baixo");
                view.getProjectionButton().setCaption("Andamento Baixo");
            } else if (andamento == porcIdeal) {
                System.out.println("Ideal");
                view.getProjectionButton().setCaption("Ideal");
            } else if (andamento > porcIdeal) {
                System.out.println("Andamento Alto");
                view.getProjectionButton().setCaption("Andamento Alto");
            } else if (andamento == 100) {
                System.out.println("Finalizado");
                view.getProjectionButton().setCaption("Finalizado");
            }

        }
    }

    public int contarDias(Date anterior, Date prox) {

        if (anterior == null || prox == null) {
            return 0;
        }

        Calendar ant = Calendar.getInstance();
        Calendar dep = Calendar.getInstance();
        int dias = 0;
        int resultado = 0;

        ant.setTime(anterior);
        dep.setTime(prox);

        if (ant.before(dep)) {
            while (ant.before(dep)) {
                dias++;
                ant.add(Calendar.DATE, 1);
            }
            dias = dias;
        }
        if (dep.before(ant)) {
            while (dep.before(ant)) {
                dias++;
                dep.add(Calendar.DATE, 1);
            }
            dias = (dias * -1);
        }
        resultado = dias;
        return resultado;
    }

    /**
     * Carrega o combo de seleçao com os status possiveis para a tarefa
     */
//    private void carregaComboTipoRecorrenciaTarefa() {
//
//        ComboBox tipo = view.getTipoRecorrenciaCombo();
//        for (TipoTarefa tipoTarefaValue : TipoTarefa.values()) {
//            tipo.addItem(tipoTarefaValue);
//            tipo.setItemCaption(tipoTarefaValue, tipoTarefaValue.getLocalizedString());
//        }
//    }
    /**
     * Carrega o combo de seleção da empresa com todas as empresas relacionadas
     * ao usuário logado
     */
    private void carregaComboEmpresa() {
        ComboBox empresaCombo = view.getCompanyCombo();

        CompanyModel empresaModel = new CompanyModel();

        List<Empresa> empresas = empresaModel.listarEmpresasParaSelecao(loggedUser);
        for (Empresa empresa : empresas) {

            empresaCombo.addItem(empresa);
            empresaCombo.setItemCaption(empresa, empresa.getNome());

        }
    }

    /**
     * Carrega o combo de prioridades (alta, normal, baixa) com os valores da
     * enumeração
     */
    private void carregaComboPrioridade() {
        ComboBox prioridade = view.getPriorityCombo();
        for (PrioridadeTarefa prioridadeValue : PrioridadeTarefa.values()) {
            prioridade.addItem(prioridadeValue);
            prioridade.setItemCaption(prioridadeValue, prioridadeValue.getLocalizedString());
        }
    }

//    private void carregaComboCategorias(HierarquiaProjetoDetalhe categoriaPai) {
//        ComboBox combo = view.getHierarquiaCombo();
//
//        // se não houver categoria pai, diponibiliza para selecao as categorias do nivel 2 = tarefa
//        if (categoriaPai == null) {
//            List<HierarquiaProjetoDetalhe> categorias = model.listaCategoriasNivelTarefa();
//            for (HierarquiaProjetoDetalhe categoria : categorias) {
//                combo.addItem(categoria);
//                combo.setItemCaption(categoria, categoria.getCategoria());
//            }
//
//        } else {
//
//            // senao, obtem a proxima categoria e deixa fixa
//            int proximoNivel = categoriaPai.getNivel() + 1;
//            for (HierarquiaProjetoDetalhe categoria : categoriaPai.getHierarquia().getCategorias()) {
//                if (categoria.getNivel() == proximoNivel) {
//                    combo.addItem(categoria);
//                    combo.setItemCaption(categoria, categoria.getCategoria());
//                    combo.setEnabled(false);
//                }
//            }
//        }
//    }
    /**
     * Carrega o combo de responsáveis com todos os usuários ativos da mesma
     * empresa do usuário logado
     */
    private void carregaComboResponsavel() {
        ComboBox responsavel = view.getAssigneeUserCombo();
        for (Usuario usuario : model.listarUsuariosEmpresa()) {
            responsavel.addItem(usuario);
            responsavel.setItemCaption(usuario, usuario.getNome());

        }
    }

    /**
     *
     */
    private void organizeTree(Task parentTask, List<Task> subTasks) {

        for (Task subTask : subTasks) {
            view.getSubTasksTable().setParent(subTask, parentTask);
            if (subTask.getSubTarefas() != null && !subTask.getSubTarefas().isEmpty()) {
                organizeTree(subTask, subTask.getSubTarefas());
            }
        }
    }

    /**
     * Carrega o combo de participante com todos os usuários ativos da mesma
     * empresa do usuário logado
     */
    private void carregaComboParticipante() {
        ComboBox participante = view.getFollowersCombo();
        for (Usuario usuario : model.listarUsuariosEmpresa()) {
            participante.addItem(usuario);
            participante.setItemCaption(usuario, usuario.getNome());

        }
    }

    /**
     * Constrói o pop up de alteração de status e/ou andamento de tarefas neste
     * PopUP o usuario poderá alterar (evoluir ou regredir) um status de tarefa
     * ou indicar seu andamento.
     *
     * @return
     */
    private void setPopUpEvolucaoStatusEAndamento(Task tarefa) {

        // comportmento e regras:
        PopUpStatusView viewPopUP = new PopUpStatusView();
        PopUpStatusModel modelPopUP = new PopUpStatusModel();

        presenterPopUpStatus = new PopUpStatusPresenter(viewPopUP, modelPopUP);

        presenterPopUpStatus.load(tarefa, view.getTaskStatusPopUpButton(), this);

    }

    public PopUpStatusPresenter getPresenterPopUpStatus() {
        return presenterPopUpStatus;
    }

    /**
     * Carrega o combo de clientes com todos os clientes ativos de todas as
     * empresas (empresa pricipal + subs ) do usuario logado
     */
    private void carregaComboEmpresaCliente() {
        ComboBox empresaCliente = view.getCustomerCompanyCombo();
        for (EmpresaCliente cliente : model.listarEmpresasCliente(loggedUser)) {
            empresaCliente.addItem(cliente);
            empresaCliente.setItemCaption(cliente, cliente.getNome());
        }

    }

    /**
     * Loads the department's combobox with all active company's department or
     * disable the combo if there is not any active department for this company.
     */
    private void loadDepartmentCombo(Empresa company) {

        // Retrieves the combo reference
        ComboBox department = view.getDepartamentCombo();

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
     * Loads the cost center's combobox with all active company's cc or disable
     * the combo if there is not any active cost-center for this company.
     */
    private void loadCostCenterCombo(Empresa company) {

        // Retrieves the combo reference
        ComboBox costCenterCombo = view.getCostCenterCombo();

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

    @Override
    public void avisoButtonClicked() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * abre um novo presenterPopUpStatus para o cadastro de uma sub tarefa desta
     * tarefa
     */
    @Override
    public void addSubButtonClicked() {

        try {
            view.getTaskFieldGroup().commit();
            TaskPresenter presenter = new TaskPresenter(new TaskModel(), new TaskView());
            presenter.setCallBackListener(this);
            presenter.criarNovaSubTarefa(view.getTarefa());
        } catch (FieldGroup.CommitException ex) {
            Notification.show("Preencha os campos obrigatórios da tarefa antes de criar uma sub.", Notification.Type.HUMANIZED_MESSAGE);
        }
    }

    @Override
    public void chatButtonClicked() {
        //Cria o pop up para registrar a conta (model e viw)
        ChatSingletonModel chatModel = ChatSingletonModel.getInstance();
        ChatView chatView = new ChatView();

        //o presenter liga model e view
        ChatPresenter chatPresenter;
        chatPresenter = new ChatPresenter(chatModel, chatView);

        //adiciona a visualização à UI
        UI.getCurrent().addWindow(chatView);
        chatPresenter.open(view.getTarefa());
    }

    @Override
    public void projecaoButtonClicked() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Properly handles the event triggered by the user: Save the task
     *
     */
    @Override
    public void gravarButtonClicked() {

        Task task = view.getTarefa();

        boolean itIsANewTask = task.getId() == null;

        // if there is not an specified responsible user, the logged user will be the responsible
        if (task.getUsuarioResponsavel() == null) {
            task.setUsuarioResponsavel(task.getUsuarioInclusao());
        }

        // if the user has not selected a task's priority, the system do select the LOW priority
        if (task.getPrioridade() == null) {
            task.setPrioridade(PrioridadeTarefa.BAIXA);
        }

        // if the user has not selected a task's type, the system do select UNIQUE
        if (task.getTipoRecorrencia() == null) {
            task.setTipoRecorrencia(TipoTarefa.UNICA);
        }

        // compare the Task's initial and final dates with its parent Task (only if there is a parent Task)
        if (task.getTarefaPai() != null) {
            if (task.getDataInicio().isBefore(task.getTarefaPai().getDataInicio())) {
                throw new RuntimeException(mensagens.getString("CadastroTarefaPresenter.mensagem.dataInicio"));
            }
            if (task.getDataFim() != null && task.getTarefaPai().getDataFim() != null && task.getDataFim().isAfter(task.getTarefaPai().getDataFim())) {
                throw new RuntimeException(mensagens.getString("CadastroTarefaPresenter.mensagem.dataFim"));
            }
        }

        if (task.getUsuarioResponsavel().equals(task.getUsuarioSolicitante())) {

            // if it is an own task auto accept the task (switches from NOT ACCEPTED to NOT STARTED)
            if (task.getStatus() == StatusTarefa.NAO_ACEITA) {
                task.setStatus(StatusTarefa.NAO_INICIADA);
            }
        }

        /**
         * when creating a new task only persist in the parent task or target
         * when editing an existing task, save anyway
         */
        boolean itIsAParentTask = task.getMeta() == null && task.getTarefaPai() == null;

        if (itIsANewTask && itIsAParentTask || !itIsANewTask) {
            if (recurrentDates != null) {
                if (task.getDataFim() == null) {
                    throw new RuntimeException("Informe a data de término.");
                }
                RecurrencyModel recorrenciaModel = new RecurrencyModel();
                task = recorrenciaModel.createRecurrentTasks(task, recurrentDates, recurrencyMessage);
            }
            task = model.saveTask(task);
        }

        // Notifies the call back listener that the create/update is done
        if (callbackListener != null) {
            if (itIsANewTask) {
                callbackListener.taskCreationDone(task);
            } else {
                callbackListener.taskUpdateDone(task);
            }
        }
        view.close();

    }

    @Override
    public void cancelarButtonClicked() {
        UI.getCurrent().removeWindow(view);
    }

    /**
     * Tratamento para o evento disparado ao acionar o comando para imputar
     * horas no controle de horas da tarefa
     */
    @Override
    public void imputarHorasClicked() {

        try {
            ApontamentoTarefa apontamentoTarefa = view.getApontamentoTarefa();

            apontamentoTarefa = model.configuraApontamento(apontamentoTarefa);

            view.getHoursControlContainer().addItem(apontamentoTarefa);
            // se o usuário informou um custo / hora, congela este custo para todos os futuros apontamentos
            if (apontamentoTarefa.getCustoHora() != null) {
                view.getTarefa().setCustoHoraApontamento(apontamentoTarefa.getCustoHora());
            }
            // criar um novo apontamento em branco para o usuario adicionar um novo:
            view.setApontamentoTarefa(new ApontamentoTarefa(view.getTarefa(), loggedUser));

        } catch (Exception ex) {
            Notification.show(ex.getLocalizedMessage(), Notification.Type.WARNING_MESSAGE);
            Logger.getLogger(TaskPresenter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Remove a pointing time
     *
     * @param taskPointingTime
     */
    @Override
    public void removePointingTime(ApontamentoTarefa taskPointingTime) {

        if (!taskPointingTime.getUsuarioInclusao().equals(loggedUser)) {
            Notification.show("Ops, apenas " + taskPointingTime.getUsuarioInclusao().getNome() + " pode remover este apontamento.", Notification.Type.WARNING_MESSAGE);
        } else {
            view.getPointingTimeTable().removeItem(taskPointingTime);
            model.removerApontamentoHoras(taskPointingTime);
            model.recalculaSaldoApontamentoHoras(view.getTarefa().getApontamentos());
            view.getPointingTimeTable().refreshRowCache();
        }
    }

    /**
     * Tratamento para o evento disparado ao acionar o comando para imputar
     * valores no controle de orçamento da tarefa
     */
    @Override
    public void imputarOrcamentoClicked() {

        try {
            OrcamentoTarefa orcamentoTarefa = view.getOrcamentoTarefa();
            orcamentoTarefa = model.configuraInputOrcamento(orcamentoTarefa);
            view.getBudgetContainer().addItem(orcamentoTarefa);

            // criar um novo apontamento de orçamento em branco para o usuario adicionar um novo:
            view.setOrcamentoTarefa(new OrcamentoTarefa(view.getTarefa(), loggedUser));

        } catch (Exception ex) {
            Notification.show(ex.getLocalizedMessage(), Notification.Type.WARNING_MESSAGE);
            Logger.getLogger(TaskPresenter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void removerRegistroOrcamento(OrcamentoTarefa orcamentoTarefa) {

        if (!orcamentoTarefa.getUsuarioInclusao().equals(loggedUser)) {
            Notification.show("Ops, apenas " + orcamentoTarefa.getUsuarioInclusao().getNome() + " pode remover este apontamento.", Notification.Type.WARNING_MESSAGE);
        } else {
            view.getBudgetControlTable().removeItem(orcamentoTarefa);
            model.removerOrcamentoTarefa(orcamentoTarefa);
            model.recalculaSaldoOrcamento(view.getTarefa().getOrcamentos());
            view.getBudgetControlTable().refreshRowCache();
        }
    }

    @Override
    public void removerAnexo(AnexoTarefa anexoTarefa) {
        if (!(anexoTarefa.getUsuarioInclusao().equals(loggedUser))) {
            Notification.show("Ops, apenas " + anexoTarefa.getUsuarioInclusao().getNome() + " pode remover este apontamento.", Notification.Type.WARNING_MESSAGE);
        } else {
            view.getAttachmentsAddedTable().removeItem(anexoTarefa);
            Task tarefa = view.getTarefa();
            tarefa.getAnexos().remove(anexoTarefa);
        }

    }

    /**
     * Evento disparado quando o usuário seleciona o arquivo para upload Visa
     * validar o arquivo, aceitando ou regeitando-o.
     *
     * NOTA AO FERNANDAO: Este é o metodo do client. É ele que deve pegar a
     * exceção e tratar.
     *
     * @param event
     */
    @Override
    public void solicitacaoParaAdicionarAnexo(Upload.StartedEvent event) {

        try {
            // chama o metodo de validação de arquivo 
            // que pode lançar duas exceções não verificadas (filhas de RuntimeException )
            // ou uma verificada: FileNotFoundException
            model.validarArquivo(event);

        } catch (FileNotFoundException | RuntimeException ex) {
            // caso alguma exceção ocorra, loga:
            Logger.getLogger(TaskPresenter.class
                    .getName()).log(Level.SEVERE, null, ex);
            // e exibe ao usuario:
            Notification.show(ex.getMessage(), Notification.Type.WARNING_MESSAGE);
        }

    }

    @Override
    public void apontamentoHorasSwitched(Property.ValueChangeEvent event) {
        view.setAbaControleHorasVisible((boolean) event.getProperty().getValue());
    }

    @Override
    public void controleOrcamentoSwitched(Property.ValueChangeEvent event) {
        view.setAbaControleOrcamentoVisible((boolean) event.getProperty().getValue());
    }

    /**
     * Configura um listener para ser chamado quando o cadastro for concluido
     *
     * @param callback
     */
    @Override
    public void setCallBackListener(TaskCreationCallBackListener callback) {
        this.callbackListener = callback;
    }

    private Button buildButtonEditarTarefa(Task subTarefa, String caption) {
        Button link = new Button(caption);
        link.setStyleName("quiet");
        TaskCreationCallBackListener callback = this;
        link.addClickListener((Button.ClickEvent event) -> {
            view.getSubTasksTable().setValue(subTarefa);
            TaskPresenter presenter = new TaskPresenter(new TaskModel(), new TaskView());
            presenter.setCallBackListener(callback);
            presenter.editar(subTarefa);
        });
        return link;
    }

    private void adicionarSubTarefa(Task sub) {

        // monta os dados para adicionar na grid
        Object[] linha = new Object[]{
            buildButtonEditarTarefa(sub, sub.getGlobalID()),
            buildButtonEditarTarefa(sub, sub.getHierarquia().getCategoria()),
            buildButtonEditarTarefa(sub, sub.getNome()),
            sub.getEmpresa().getNome()
            + (sub.getFilialEmpresa() != null ? "/" + sub.getFilialEmpresa().getNome() : ""),
            sub.getUsuarioSolicitante().getNome(),
            sub.getUsuarioResponsavel().getNome(),
            FormatterUtil.formatDate(sub.getDataInicio()),
            FormatterUtil.formatDate(sub.getDataFim()),
            TaskView.buildPopUpStatusProgressTask(view.getSubTasksTable(), sub, this),
            sub.getProjecao().toString().charAt(0),
            new Button("E"),
            new Button("C")

        };
        view.getSubTasksTable().addItem(linha, sub);

        for (Task subTarefa : sub.getSubTarefas()) {
            adicionarSubTarefa(subTarefa);
        }

    }

    /**
     * metodo chamado quando uma subtarefa foi criada
     *
     * @param tarefa
     */
    @Override
    public void taskCreationDone(Task tarefa) {

        adicionarSubTarefa(tarefa);
        organizeTree(tarefa, tarefa.getSubTarefas());

    }

    @Override
    public void taskUpdateDone(Task tarefa) {

        Item it = view.getSubTasksTable().getItem(tarefa);

        it.getItemProperty(mensagens.getString("TaskView.subTarefasTable.colunaCod")).setValue(buildButtonEditarTarefa(tarefa, tarefa.getGlobalID()));
        it.getItemProperty(mensagens.getString("TaskView.subTarefasTable.colunaTitulo")).setValue(buildButtonEditarTarefa(tarefa, tarefa.getHierarquia().getCategoria()));
        it.getItemProperty(mensagens.getString("TaskView.subTarefasTable.colunaNome")).setValue(buildButtonEditarTarefa(tarefa, tarefa.getNome()));
        it.getItemProperty(mensagens.getString("TaskView.subTarefasTable.colunaEmpresaFilial")).setValue(tarefa.getEmpresa().getNome()
                + (tarefa.getFilialEmpresa() != null ? "/" + tarefa.getFilialEmpresa().getNome() : ""));
        it.getItemProperty(mensagens.getString("TaskView.subTarefasTable.colunaSolicitante")).setValue(tarefa.getUsuarioSolicitante().getNome());
        it.getItemProperty(mensagens.getString("TaskView.subTarefasTable.colunaResponsavel")).setValue(tarefa.getUsuarioResponsavel().getNome());
        it.getItemProperty(mensagens.getString("TaskView.subTarefasTable.colunaDataInicio")).setValue(FormatterUtil.formatDate(tarefa.getDataInicio()));
        it.getItemProperty(mensagens.getString("TaskView.subTarefasTable.colunaDataFim")).setValue(FormatterUtil.formatDate(tarefa.getDataInicio()));
        it.getItemProperty(mensagens.getString("TaskView.subTarefasTable.colunaStatus")).setValue(TaskView.buildPopUpStatusProgressTask(view.getSubTasksTable(), tarefa, this));
        it.getItemProperty(mensagens.getString("TaskView.subTarefasTable.colunaProjecao")).setValue(tarefa.getProjecao().toString().charAt(0));
        it.getItemProperty("[E]").setValue(new Button("E"));
        it.getItemProperty("[C]").setValue(new Button("C"));

    }

    @Override
    public void removerParticipante(ParticipanteTarefa participanteTarefa) {
        view.getFollowersTable().removeItem(participanteTarefa);
        Task tarefa = view.getTarefa();
        tarefa.getParticipantes().remove(participanteTarefa);
    }

    @Override
    public void adicionarParticipante(Usuario usuario) {

        if (usuario.equals(view.getAssigneeUserCombo().getValue()) || usuario.equals(view.getAssigneeUserCombo().getValue())) {
            Notification.show(mensagens.getString("Notificacao.ParticipanteUsuarioResponsavel"));
        } else {
            ParticipanteTarefa participanteTarefa = model.criarParticipante(usuario, view.getTarefa());
            view.getFollowersContainer().addBean(participanteTarefa);
            Task tarefa = view.getTarefa();

            if (tarefa.getParticipantes() == null) {
                tarefa.setParticipantes(new ArrayList<>());
            }

            tarefa.getParticipantes().add(participanteTarefa);
        }

    }

    @Override
    public void anexoAdicionado(File anexo) {
        AnexoTarefa anexoTarefa = new AnexoTarefa();
        anexoTarefa.setNome(anexo.getName());
        anexoTarefa.setDataHoraInclusao(LocalDateTime.now());
        anexoTarefa.setUsuarioInclusao(loggedUser);
        anexoTarefa.setTarefa(view.getTarefa());
        anexoTarefa.setArquivoTemporario(anexo);
        anexoTarefa.setCaminhoCompleto(anexo.getAbsolutePath());
        if (view.getTarefa().getAnexos() == null) {
            view.getTarefa().setAnexos(new ArrayList<>());
        }
        view.getTarefa().getAnexos().add(anexoTarefa);
        view.getTaskAttachContainer().addBean(anexoTarefa);

    }

    @Override
    public void hierarquiaSelecionada(HierarquiaProjetoDetalhe hierarquiaProjetoDetalhe) {

        if (hierarquiaProjetoDetalhe != null) {
            //verifica se será possivel criar sub
            if (model.getProximasCategorias(hierarquiaProjetoDetalhe).isEmpty()) {
                view.getAddSubButton().setEnabled(false);
            }

            view.setCaption(mensagens.getString("TaskView.titulo.cadastro") + hierarquiaProjetoDetalhe.getCategoria());
        }
    }

    @Override
    public void empresaSelecionada(Empresa empresa) {
        loadDepartmentCombo(empresa);
        loadCostCenterCombo(empresa);
    }

    private boolean isStartAndEndDateValidForRecurrency() {

        // validates the initial and end date
        Date startDate = view.getStartDateDateField().getValue();
        Date endDate = view.getEndDateDateField().getValue();

        if (startDate == null || endDate == null) {
            Notification.show("Informe as datas de início e término da tarefa para recorrencia");
            return false;
        }

        if (startDate.after(endDate)) {
            Notification.show("A data de término deve ser maior que a data de início");
            return false;
        }

        return true;
    }

    @Override
    public void recurrenceClicked() {

        if (!isStartAndEndDateValidForRecurrency()) {
            return;
        }

        //Cria o pop up para registrar a conta (model e view)
        RecurrencyModel recorrenciaModel = new RecurrencyModel();

        RecurrencyView RecorrencyView = new RecurrencyView(view.getTarefa());

        //o presenter liga model e view
        RecorrencyPresenter = new RecurrencyPresenter(recorrenciaModel, RecorrencyView, view.getTarefa(),
                DateTimeConverters.toLocalDate(view.getStartDateDateField().getValue()),
                DateTimeConverters.toLocalDate(view.getEndDateDateField().getValue())
        );
        RecorrencyPresenter.setCallBackListener(this);

        //adiciona a visualização à UI
        UI.getCurrent().addWindow(RecorrencyView);

    }

    @Override
    public void recurrencyCreationDone(RecurrencySet recurrencySet) {
        this.recurrentDates = recurrencySet.getRecurrentDates();
        this.recurrencyMessage = recurrencySet.getRecurrencyMessage();
        view.getStartDateDateField().setValue(DateTimeConverters.toDate(recurrencySet.getFirstTaskStartDate()));
        view.getEndDateDateField().setValue(DateTimeConverters.toDate(recurrencySet.getFirstTaskEndDate()));
        view.getRecurrencyMessage().setVisible(true);
        view.getRecurrencyMessage().setValue(recurrencyMessage);
    }

    @Override
    public void recurrencyRemoved(Task task) {
        view.close();
        callbackListener.taskUpdateDone(task);
    }

    @Override
    public void assigneeUserChanged(Task task, Usuario assignee) {
        task.setUsuarioResponsavel(assignee);
        configPermissions(task);

    }

    @Override
    public void taskStatusChanged(Task task) {
        view.setTarefa(task);
    }

}
