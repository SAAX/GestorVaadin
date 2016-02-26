package com.saax.gestorweb.presenter;

import com.saax.gestorweb.model.ChatSingletonModel;
import com.saax.gestorweb.model.EmpresaModel;
import com.saax.gestorweb.model.LixeiraModel;
import com.saax.gestorweb.model.RecurrencyModel;
import com.saax.gestorweb.model.TarefaModel;
import com.saax.gestorweb.model.datamodel.Anexo;
import com.saax.gestorweb.model.datamodel.ApontamentoTarefa;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.OrcamentoTarefa;
import com.saax.gestorweb.model.datamodel.Participante;
import com.saax.gestorweb.model.datamodel.PrioridadeTarefa;
import com.saax.gestorweb.model.datamodel.ProjecaoTarefa;
import com.saax.gestorweb.model.datamodel.RecurrencySet;
import com.saax.gestorweb.model.datamodel.StatusTarefa;
import static com.saax.gestorweb.model.datamodel.StatusTarefa.ADIADA;
import static com.saax.gestorweb.model.datamodel.StatusTarefa.AVALIADA;
import static com.saax.gestorweb.model.datamodel.StatusTarefa.BLOQUEADA;
import static com.saax.gestorweb.model.datamodel.StatusTarefa.CANCELADA;
import static com.saax.gestorweb.model.datamodel.StatusTarefa.CONCLUIDA;
import static com.saax.gestorweb.model.datamodel.StatusTarefa.EM_ANDAMENTO;
import static com.saax.gestorweb.model.datamodel.StatusTarefa.NAO_ACEITA;
import static com.saax.gestorweb.model.datamodel.StatusTarefa.NAO_INICIADA;
import static com.saax.gestorweb.model.datamodel.StatusTarefa.RECUSADA;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.TipoTarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.DateTimeConverters;
import com.saax.gestorweb.util.FormatterUtil;
import com.saax.gestorweb.view.TarefaView;
import com.saax.gestorweb.view.TarefaViewListener;
import com.saax.gestorweb.view.ChatView;
import com.saax.gestorweb.view.LixeiraView;
import com.saax.gestorweb.view.PopUpStatusListener;
import com.saax.gestorweb.view.PopUpStatusView;
import com.saax.gestorweb.view.RecurrencyView;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Presenter:
 * <p>
 * Listener de eventos da view do cadastro de tarefas
 *
 * @author rodrigo
 */
public class TarefaPresenter implements Serializable, TarefaViewListener, CallBackListener, PopUpStatusListener {

    // Todo Presenter mantem acesso à view e ao model
    private final transient TarefaView view;

    // Referencia ao recurso das mensagens:
    private final List<CallBackListener> callbackListeneres;
    private List<LocalDate> recurrentDates;
    private RecurrencyPresenter RecorrencyPresenter;
    private String recurrencyMessage;

    /**
     * Cria o Presenter ligando o Model ao View
     *
     * @param view
     */
    public TarefaPresenter(TarefaView view) {

        this.view = view;
        this.callbackListeneres = new ArrayList<>();
        view.setListener(this);

    }

    public List<CallBackListener> getCallbackListeneres() {
        return callbackListeneres;
    }

    /**
     * Abre o pop window do cadastro de tarefas para criação de uma sub tarefa
     *
     * @param proximasCategorias
     * @param tarefaPai
     */
    @Override
    public void criarNovaSubTarefa(List<HierarquiaProjetoDetalhe> proximasCategorias, Tarefa tarefaPai) {

        Tarefa tarefa;

        // Cria uma nova tarefa com valores default
        tarefa = new Tarefa();
        tarefa.setStatus(StatusTarefa.NAO_ACEITA);
        tarefa.setEmpresa(tarefaPai.getEmpresa());
        tarefa.setUsuarioInclusao(GestorPresenter.getUsuarioLogado());
        tarefa.setUsuarioSolicitante(GestorPresenter.getUsuarioLogado());
        tarefa.setDataHoraInclusao(LocalDateTime.now());

        // configuras as caracteristicas de sub-tarefa
        tarefa.setTarefaPai(tarefaPai);
        tarefa.setEmpresa(tarefaPai.getEmpresa());
        view.getEmpresaCombo().setEnabled(false);
        if (tarefaPai.getSubTarefas() == null) {
            tarefaPai.setSubTarefas(new ArrayList<>());
        }
        tarefaPai.getSubTarefas().add(tarefa);
//        view.getChatButton().setVisible(false);

        /**
         * COMENTADO: Projeção postergada para v2
         * view.getProjectionButton().setVisible(false);
         * tarefa.setProjecao(ProjecaoTarefa.NORMAL);
         */
        StringBuilder nomesProximasCategorias = new StringBuilder();
        for (HierarquiaProjetoDetalhe proximaCategoria : proximasCategorias) {
            ComboBox combo = view.getHierarquiaCombo();
            combo.addItem(proximaCategoria);
            combo.setItemCaption(proximaCategoria, proximaCategoria.getCategoria());
            nomesProximasCategorias.append(proximasCategorias.indexOf(proximaCategoria) == 0 ? "" : "/");
            nomesProximasCategorias.append(proximaCategoria.getCategoria());
        }

        // configura a categoria da sub-tarefa
        if (proximasCategorias.size() == 1) {
            tarefa.setHierarquia(proximasCategorias.get(0));
        }

        view.ocultaPopUpEvolucaoStatusEAndamento();

        view.setCaption(GestorPresenter.getMENSAGENS().getString("TarefaView.titulo.cadastro"));

        init(tarefa);

    }

    /**
     * Creates a new default Tarefa, with a specific category Just overloaded:
     * createTask(List). <br>
     *
     * If empresa isn't null, it's setted in the task.
     *
     * @param category in wich the task'll be created
     * @param empresa
     */
    public void createTask(HierarquiaProjetoDetalhe category, Empresa empresa) {
        // builds a list to use the main method: createTask
        List<HierarquiaProjetoDetalhe> categories = new ArrayList<>();
        categories.add(category);
        // call the main method to create a task

        createTask(null, categories, empresa);
    }

    /**
     * Creates a new default Tarefa, with given possible categories
     *
     * @param target the main target to be attached with the new task
     * @param possibleCategories
     * @param empresa if not null, setted as initial Empresa
     */
    public void createTask(Meta target, List<HierarquiaProjetoDetalhe> possibleCategories, Empresa empresa) {

        Tarefa tarefa;

        // Cria uma nova tarefa com valores default
        tarefa = new Tarefa();

        tarefa.setStatus(StatusTarefa.NAO_ACEITA);
        if (empresa != null) {
            tarefa.setEmpresa(empresa);
        } else if (target != null) {
            tarefa.setEmpresa(target.getEmpresa());
        }

        //Ver com o Rodrigo qual deve deixar
        tarefa.setUsuarioInclusao(GestorPresenter.getUsuarioLogado());
        tarefa.setUsuarioSolicitante(GestorPresenter.getUsuarioLogado());
        tarefa.setDataHoraInclusao(LocalDateTime.now());
        tarefa.setSubTarefas(new ArrayList<>());
        if (possibleCategories.size() == 1) {
            tarefa.setHierarquia(possibleCategories.get(0));
        }

        TarefaModel.attachTaskToTarget(tarefa, target);

        // ajuste ate a projecao ser implementada
        tarefa.setProjecao(ProjecaoTarefa.NORMAL);

        // configura a categoria
        ComboBox combo = view.getHierarquiaCombo();
        for (HierarquiaProjetoDetalhe categoria : possibleCategories) {
            combo.addItem(categoria);
            combo.setItemCaption(categoria, categoria.getCategoria());
        }

        view.ocultaPopUpEvolucaoStatusEAndamento();

        if (possibleCategories.size() == 1) {
            view.setCaption(GestorPresenter.getMENSAGENS().getString("TarefaView.titulo.cadastro") + possibleCategories.get(0).getCategoria());
            view.getHierarquiaCombo().setEnabled(false);
        }

        view.getChatButton().setVisible(false);
        /**
         * COMENTADO: Projeção postergada para v2
         * view.getProjectionButton().setVisible(false);
         */

        init(tarefa);

    }

    /**
     * Abre o pop window do cadastro de tarefas para edição da tarefa informada
     *
     * @param taskToEdit
     */
    @Override
    public void editar(Tarefa taskToEdit) {

        if (!TarefaModel.verificaAcesso(GestorPresenter.getUsuarioLogado(), taskToEdit)) {
            Notification.show(GestorPresenter.getMENSAGENS().getString("TarefaView.accessDenied"), Notification.Type.WARNING_MESSAGE);
            return;
        }

        taskToEdit = TarefaModel.refresh(taskToEdit);

        init(taskToEdit);

        for (Tarefa sub : taskToEdit.getSubTarefas()) {
            adicionarSubTarefa(sub);
        }

        if (taskToEdit.getUsuarioResponsavel().equals(taskToEdit.getUsuarioSolicitante())) {
            view.getChatButton().setEnabled(false);
        }

        //Caso usuário logado não seja o solicitante, não deixar que ele clique nos controle de horas
        //e apontamento da tarefa
        if (!GestorPresenter.getUsuarioLogado().equals(taskToEdit.getUsuarioSolicitante())) {
            view.getApontamentoHorasCheckBox().setReadOnly(true);
            view.getControleOrcamentoChechBox().setReadOnly(true);

        }

        organizeTree(taskToEdit, taskToEdit.getSubTarefas());

        // configura a categoria
        ComboBox combo = view.getHierarquiaCombo();
        combo.addItem(taskToEdit.getHierarquia());
        combo.setItemCaption(taskToEdit.getHierarquia(), taskToEdit.getHierarquia().getCategoria());

        view.getParticipantesContainer().addAll(taskToEdit.getParticipantes());
        view.getAnexosContainer().addAll(taskToEdit.getAnexos());
        view.getApontamentoTarefaContainer().addAll(taskToEdit.getApontamentos());
        view.getApontamentoTarefaContainer().sort(new String[]{"dataHoraInclusao"}, new boolean[]{true});
        view.getOrcamentoContainer().addAll(taskToEdit.getOrcamentos());
        view.getOrcamentoContainer().sort(new String[]{"dataHoraInclusao"}, new boolean[]{true});

        configPermissions(taskToEdit);

        view.setCaption(GestorPresenter.getMENSAGENS().getString("TarefaView.titulo.edicao") + taskToEdit.getHierarquia().getCategoria());

    }

    /**
     * Config the view with the access permissions
     *
     * @param tarefaToEdit
     */
    private void configPermissions(Tarefa task) {

        boolean responsavel = task.getUsuarioResponsavel() != null && task.getUsuarioResponsavel().equals(GestorPresenter.getUsuarioLogado());
        boolean solicitante = task.getUsuarioSolicitante() != null && task.getUsuarioSolicitante().equals(GestorPresenter.getUsuarioLogado());

        switch (task.getStatus()) {

            case ADIADA:
                view.setEditAllowed(solicitante);

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
                view.setEditAllowed(solicitante || responsavel);

                configuraPermissoesExclusivasSolicitante(solicitante);

                break;

            case NAO_ACEITA:
                view.setEditAllowed(solicitante);

                break;

            case NAO_INICIADA:
                view.setEditAllowed(solicitante || responsavel);

                configuraPermissoesExclusivasSolicitante(solicitante);

                break;

            case RECUSADA:
                view.setEditAllowed(false);

                break;

            default:
                throw new RuntimeException("Falha ao identificar status.");
        }

        view.getRemoverTarefaButton().setEnabled(LixeiraModel.verificaPermissaoAcessoRemocaoTarefa(task, GestorPresenter.getUsuarioLogado()));

    }

    private void configuraPermissoesExclusivasSolicitante(boolean solicitante) {
        view.getUsuarioResponsavelCombo().setEnabled(solicitante);
        view.getCustoHoraApontamentoTextField().setEnabled(solicitante);
        view.getAlteraCustoHoraButton().setEnabled(solicitante);
    }

    /**
     * inicializa a gui
     */
    private void init(Tarefa tarefa) {
        // Carrega os combos de seleção
        carregaComboEmpresa();
        carregaComboPrioridade();
        GestorPresenter.carregaComboUsuarios(view.getUsuarioResponsavelCombo(), tarefa.getEmpresa());
        GestorPresenter.carregaComboUsuarios(view.getParticipantesCombo(), tarefa.getEmpresa());
        GestorPresenter.carregaComboEmpresaCliente(view.getEmpresaClienteCombo());
        GestorPresenter.carregaComboDepartamento(view.getDepartamentoCombo(), tarefa.getEmpresa());
        GestorPresenter.carregaComboCentroCusto(view.getCentroCustoCombo(), tarefa.getEmpresa());

        setPopUpEvolucaoStatusEAndamento(tarefa);
        view.getMensagemRecorrenciaLabel().setVisible(tarefa.getTipoRecorrencia() == TipoTarefa.RECORRENTE);

        // Configuras os beans de 1-N
        view.setApontamentoTarefa(new ApontamentoTarefa(tarefa, GestorPresenter.getUsuarioLogado()));
        view.setOrcamentoTarefa(new OrcamentoTarefa(tarefa, GestorPresenter.getUsuarioLogado()));

        view.setAbaControleHorasVisible(tarefa.isApontamentoHoras());
        view.setAbaControleOrcamentoVisible(tarefa.isOrcamentoControlado());

        UI.getCurrent().addWindow(view);

        view.setTarefa(tarefa);

    }
//Projeção será inserida na V2

    /**
     * Carrega os apontamentos e executa o método para o cálculo da projeção
     */
//    private void carregaApontamento(Tarefa tarefa) {
//        List<AndamentoTarefa> andamentos = tarefa.getAndamentos();
//        if (andamentos.size() != 0) {
//            //Buscando o andamento da tarefa    
//            int andamento = tarefa.getAndamento();
//            System.out.println("Andamento Atual " + tarefa.getAndamento());
//
//            Date inicio = DateTimeConverters.toDate(tarefa.getDataInicio());
//            Date fim = DateTimeConverters.toDate(tarefa.getDataFim());
//            Date hoje = DateTimeConverters.toDate(LocalDate.now());
//
//            int diasRealizar = contarDias(inicio, fim);
//            System.out.println("Dias para realizar :" + diasRealizar);
//
//            int diasCorridos = contarDias(inicio, hoje);
//            System.out.println("Dias corridos :" + diasCorridos);
//
//            //Porcentagem ideal até o momento
//            Double porcIdeal = ((100.00 / diasRealizar) * (diasCorridos));
//            System.out.println("Porcentagem ideal é: " + porcIdeal);
//
//            if (diasCorridos < 0) {
//                if (andamento == 0) {
//                    System.out.println("Não Iniciada");
//                    view.getProjectionButton().setCaption("Não Iniciada");
//                    tarefa.setProjecao(ProjecaoTarefa.DESCENDENTE);
//                } else if (andamento > 0 && andamento < 100) {
//                    System.out.println("Tarefa Atrasada");
//                    view.getProjectionButton().setCaption("Tarefa Atrasada");
//                    tarefa.setProjecao(ProjecaoTarefa.DESCENDENTE);
//                } else if (andamento == 100) {
//                    System.out.println("Concluída");
//                    view.getProjectionButton().setCaption("Tarefa Finalizada");
//                    tarefa.setProjecao(ProjecaoTarefa.ASCENDENTE);
//                }
//            } else {
//                if (andamento == 0) {
//                    System.out.println("Não Iniciada");
//                    view.getProjectionButton().setCaption("Não Iniciada");
//                    tarefa.setProjecao(ProjecaoTarefa.DESCENDENTE);
//                } else if (andamento < porcIdeal) {
//                    System.out.println("Andamento Baixo");
//                    view.getProjectionButton().setCaption("Andamento Baixo");
//                    tarefa.setProjecao(ProjecaoTarefa.DESCENDENTE);
//                } else if (andamento == porcIdeal) {
//                    System.out.println("Ideal");
//                    view.getProjectionButton().setCaption("Ideal");
//                    tarefa.setProjecao(ProjecaoTarefa.NORMAL);
//                } else if (andamento > porcIdeal) {
//                    System.out.println("Andamento Alto");
//                    view.getProjectionButton().setCaption("Andamento Alto");
//                    tarefa.setProjecao(ProjecaoTarefa.ASCENDENTE);
//                } else if (andamento == 100) {
//                    System.out.println("Finalizada");
//                    view.getProjectionButton().setCaption("Finalizada");
//                    tarefa.setProjecao(ProjecaoTarefa.ASCENDENTE);
//                }
//
//            }
//        }else{
//        System.out.println("Não Iniciada");
//        view.getProjectionButton().setCaption("Não Iniciada");
//        tarefa.setProjecao(ProjecaoTarefa.DESCENDENTE);
//        }
//    }
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
     * Carrega o combo de seleção da empresa com todas as empresas relacionadas
     * ao usuário logado
     */
    private void carregaComboEmpresa() {
        ComboBox empresaCombo = view.getEmpresaCombo();

        EmpresaModel empresaModel = new EmpresaModel();

        List<Empresa> empresas = empresaModel.listarEmpresasAtivasUsuarioLogado(GestorPresenter.getUsuarioLogado());
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
        ComboBox prioridade = view.getPrioridadeCombo();
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
//            List<HierarquiaProjetoDetalhe> categorias = TarefaModel.listaCategoriasNivelTarefa();
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
     *
     */
    private void organizeTree(Tarefa parentTask, List<Tarefa> subTasks) {

        for (Tarefa subTask : subTasks) {
            view.getSubTarefasTable().setParent(subTask, parentTask);
            if (subTask.getSubTarefas() != null && !subTask.getSubTarefas().isEmpty()) {
                organizeTree(subTask, subTask.getSubTarefas());
            }
        }
    }

    /**
     * Constrói o pop up de alteração de status e/ou andamento de tarefas neste
     * PopUP o usuario poderá alterar (evoluir ou regredir) um status de tarefa
     * ou indicar seu andamento.
     *
     * @return
     */
    private void setPopUpEvolucaoStatusEAndamento(Tarefa tarefa) {

        // comportmento e regras:
        PopUpStatusView popUpStatusView = new PopUpStatusView();

        PopUpStatusPresenter popUpStatusPresenter = new PopUpStatusPresenter(popUpStatusView);

        popUpStatusPresenter.load(tarefa, view.getStatusTarefaPopUpButton(), this);

    }

    /**
     * Carrega o combo de clientes com todos os clientes ativos de todas as
     * empresas (empresa pricipal + subs ) do usuario logado
     */
    private void carregaComboEmpresaCliente() {
        ComboBox empresaCliente = view.getEmpresaClienteCombo();
        for (EmpresaCliente cliente : TarefaModel.listarEmpresasCliente(GestorPresenter.getUsuarioLogado())) {
            empresaCliente.addItem(cliente);
            empresaCliente.setItemCaption(cliente, cliente.getNome());
        }

    }

    /**
     * abre um novo popUpStatusPresenter para o cadastro de uma sub tarefa desta
     * tarefa
     *
     * @param tarefa
     */
    @Override
    public void addSubButtonClicked(Tarefa tarefa) {

        try {
            view.getTarefaFieldGroup().commit();
            TarefaPresenter presenter = new TarefaPresenter(new TarefaView());
            presenter.addCallBackListener(this);

            List<HierarquiaProjetoDetalhe> proximasCategorias = TarefaModel.getProximasCategorias(tarefa);

            presenter.criarNovaSubTarefa(proximasCategorias, tarefa);

        }
        catch (FieldGroup.CommitException ex) {
            Notification.show("Preencha os campos obrigatórios da tarefa antes de criar uma sub.", Notification.Type.HUMANIZED_MESSAGE);
        }
    }

    @Override
    public void chatButtonClicked(Tarefa tarefa) {
        //Cria o pop up para registrar a conta (model e viw)
        ChatSingletonModel chatModel = ChatSingletonModel.getInstance();
        ChatView chatView = new ChatView();

        //o presenter liga model e view
        ChatPresenter chatPresenter;
        chatPresenter = new ChatPresenter(chatModel, chatView);

        //adiciona a visualização à UI
        UI.getCurrent().addWindow(chatView);
        chatPresenter.open(tarefa);
    }

    @Override
    public void projecaoButtonClicked() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Properly handles the event triggered by the user: Save the task
     *
     * @param tarefa
     */
    @Override
    public void gravarButtonClicked(Tarefa tarefa) {

        boolean itIsANewTask = tarefa.getId() == null;

        // if there is not an specified responsible user, the logged user will be the responsible
        if (tarefa.getUsuarioResponsavel() == null) {
            tarefa.setUsuarioResponsavel(tarefa.getUsuarioInclusao());

        }

        // if the user has not selected a task's priority, the system do select the LOW priority
        if (tarefa.getPrioridade() == null) {
            tarefa.setPrioridade(PrioridadeTarefa.BAIXA);
        }

        // if the user has not selected a task's type, the system do select UNIQUE
        if (tarefa.getTipoRecorrencia() == null) {
            tarefa.setTipoRecorrencia(TipoTarefa.UNICA);
        }

        // compare the Tarefa's initial and final dates with its parent Tarefa (only if there is a parent Tarefa)
        if (tarefa.getTarefaPai() != null) {
            if (tarefa.getDataInicio().isBefore(tarefa.getTarefaPai().getDataInicio())) {
                throw new RuntimeException(GestorPresenter.getMENSAGENS().getString("CadastroTarefaPresenter.mensagem.dataInicio"));
            }
            if (tarefa.getDataFim() != null && tarefa.getTarefaPai().getDataFim() != null && tarefa.getDataFim().isAfter(tarefa.getTarefaPai().getDataFim())) {
                throw new RuntimeException(GestorPresenter.getMENSAGENS().getString("CadastroTarefaPresenter.mensagem.dataFim"));
            }
        }

        TarefaModel.aceitarAutomaticamenteTarefaPropria(tarefa);

        /**
         * when creating a new task only persist in the parent task or target
         * when editing an existing task, save anyway
         */
        boolean itIsAParentTask = tarefa.getMeta() == null && tarefa.getTarefaPai() == null;

        if (itIsANewTask && itIsAParentTask || !itIsANewTask) {
            if (recurrentDates != null) {
                if (tarefa.getDataFim() == null) {
                    throw new RuntimeException("Informe a data de término.");
                }
                tarefa = RecurrencyModel.createRecurrentTasks(tarefa, recurrentDates, recurrencyMessage);
            }
            tarefa = TarefaModel.gravarTarefa(tarefa);
        }

        // Notifies the call back listener that the create/update is done
        for (CallBackListener callbackListener : callbackListeneres) {
            callbackListener.atualizarApresentacaoTarefa(tarefa);
        }
        view.close();

    }

    @Override
    public void cancelarButtonClicked() {
        UI.getCurrent().removeWindow(view);
    }

    @Override
    public void alteraCustoHoraClicked(Tarefa tarefa) {
        view.getApontamentoTarefaContainer().removeAllItems();
        TarefaModel.recalculaSaldoApontamentoHoras(tarefa.getApontamentos());
        view.getApontamentoTarefaContainer().addAll(tarefa.getApontamentos());
    }

    /**
     * Tratamento para o evento disparado ao acionar o comando para imputar
     * horas no controle de horas da tarefa
     */
    @Override
    public void imputarHorasClicked(Tarefa tarefa) {

        try {
            ApontamentoTarefa apontamentoTarefa = view.getApontamentoTarefa();

            apontamentoTarefa = TarefaModel.configuraApontamento(apontamentoTarefa);

            view.getApontamentoTarefaContainer().addItem(apontamentoTarefa);
            // se o usuário informou um custo / hora, congela este custo para todos os futuros apontamentos
            if (apontamentoTarefa.getCustoHora() != null) {
                tarefa.setCustoHoraApontamento(apontamentoTarefa.getCustoHora());
            }
            // criar um novo apontamento em branco para o usuario adicionar um novo:
            view.setApontamentoTarefa(new ApontamentoTarefa(tarefa, GestorPresenter.getUsuarioLogado()));

        }
        catch (Exception ex) {
            Notification.show(ex.getLocalizedMessage(), Notification.Type.WARNING_MESSAGE);
            Logger.getLogger(TarefaPresenter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Remove a pointing time
     *
     * @param apontamentoTarefa
     */
    @Override
    public void removePointingTime(ApontamentoTarefa apontamentoTarefa) {

        if (!apontamentoTarefa.getUsuarioInclusao().equals(GestorPresenter.getUsuarioLogado())) {
            Notification.show("Ops, apenas " + apontamentoTarefa.getUsuarioInclusao().getNome() + " pode remover este apontamento.", Notification.Type.WARNING_MESSAGE);
        } else {
            view.getApontamentosTable().removeItem(apontamentoTarefa);
            TarefaModel.removerApontamentoHoras(apontamentoTarefa);
            TarefaModel.recalculaSaldoApontamentoHoras(apontamentoTarefa.getTarefa().getApontamentos());
            view.getApontamentosTable().refreshRowCache();
        }
    }

    /**
     * Tratamento para o evento disparado ao acionar o comando para imputar
     * valores no controle de orçamento da tarefa
     */
    @Override
    public void imputarOrcamentoClicked(Tarefa tarefa) {

        try {
            OrcamentoTarefa orcamentoTarefa = view.getOrcamentoTarefa();
            orcamentoTarefa = TarefaModel.configuraInputOrcamento(orcamentoTarefa);
            view.getOrcamentoContainer().addItem(orcamentoTarefa);

            // criar um novo apontamento de orçamento em branco para o usuario adicionar um novo:
            view.setOrcamentoTarefa(new OrcamentoTarefa(tarefa, GestorPresenter.getUsuarioLogado()));

        }
        catch (Exception ex) {
            Notification.show(ex.getLocalizedMessage(), Notification.Type.WARNING_MESSAGE);
            Logger.getLogger(TarefaPresenter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void removerRegistroOrcamento(OrcamentoTarefa orcamentoTarefa) {

        if (!orcamentoTarefa.getUsuarioInclusao().equals(GestorPresenter.getUsuarioLogado())) {
            Notification.show("Ops, apenas " + orcamentoTarefa.getUsuarioInclusao().getNome() + " pode remover este apontamento.", Notification.Type.WARNING_MESSAGE);
        } else {
            view.getOrcamentoTable().removeItem(orcamentoTarefa);
            TarefaModel.removerOrcamentoTarefa(orcamentoTarefa);
            TarefaModel.recalculaSaldoOrcamento(orcamentoTarefa.getTarefa().getOrcamentos());
            view.getOrcamentoTable().refreshRowCache();
        }
    }

    @Override
    public void removerAnexo(Anexo anexoTarefa) {
        if (!(anexoTarefa.getUsuarioInclusao().equals(GestorPresenter.getUsuarioLogado()))) {
            Notification.show("Ops, apenas " + anexoTarefa.getUsuarioInclusao().getNome() + " pode remover este apontamento.", Notification.Type.WARNING_MESSAGE);
        } else {
            view.getAnexosTarefaTable().removeItem(anexoTarefa);
            Tarefa tarefa = anexoTarefa.getTarefa();
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
            TarefaModel.validarArquivo(event);

        }
        catch (FileNotFoundException | RuntimeException ex) {
            // caso alguma exceção ocorra, loga:
            Logger.getLogger(TarefaPresenter.class
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
    public void addCallBackListener(CallBackListener callback) {
        this.callbackListeneres.add(callback);
    }

    private void adicionarSubTarefa(Tarefa sub) {

        // monta os dados para adicionar na grid
        Object[] linha = new Object[]{
            GestorPresenter.buildButtonEditarTarefa(view.getSubTarefasTable(), this, sub, sub.getGlobalID()),
            GestorPresenter.buildButtonEditarTarefa(view.getSubTarefasTable(), this, sub, sub.getHierarquia().getCategoria()),
            GestorPresenter.buildButtonEditarTarefa(view.getSubTarefasTable(), this, sub, sub.getNome()),
            sub.getEmpresa().getNome()
            + (sub.getFilialEmpresa() != null ? "/" + sub.getFilialEmpresa().getNome() : ""),
            sub.getUsuarioSolicitante().getNome(),
            sub.getUsuarioResponsavel().getNome(),
            FormatterUtil.formatDate(sub.getDataInicio()),
            FormatterUtil.formatDate(sub.getDataFim()),
            GestorPresenter.buildPopUpStatusTarefa(view.getSubTarefasTable(), sub, this),
            /**
             * COMENTADO: Projeção postergada para v2
             * sub.getProjecao().toString().charAt(0),
             */
            new Button("E"),
            new Button("C")

        };
        view.getSubTarefasTable().addItem(linha, sub);

        for (Tarefa subTarefa : sub.getSubTarefas()) {
            adicionarSubTarefa(subTarefa);
        }

    }

    /**
     * Trata o evento disparado via callback quando uma tarefa é removida <br>
     * recarrega a lista de sub tarefas em exibição  <br>
     * PS: não é possível remover itens de uma treetable A propria busca vai
     * filtrar a removida
     *
     * @param tarefaRemovida
     */
    @Override
    public void tarefaRemovida(Tarefa tarefaRemovida) {

        // Se a tarefa removida for uma sub da que está sendo editada
        if (view.getSubTarefasTable().getItemIds().contains(tarefaRemovida)) {

            // recarrega a lista de subs
            view.getSubTarefasTable().removeAllItems();

            view.setTarefa(TarefaModel.refresh(tarefaRemovida.getTarefaPai()));

            for (Tarefa sub : tarefaRemovida.getTarefaPai().getSubTarefas()) {

                if (sub.getDataHoraRemocao() == null) {
                    adicionarSubTarefa(sub);
                    organizeTree(sub, sub.getSubTarefas());
                }

            }

        } else {
            // caso contrario a propria tarefa em edição foi removida
            // neste caso, apenas fecha a janela
            view.close();
        }

    }

    /**
     * Metodo chamado por callback quando a edição de uma tarefa é concluida
     *
     * @param tarefa
     */
    @Override
    public void atualizarApresentacaoTarefa(Tarefa tarefa) {

        if (view.getSubTarefasTable().getItemIds().contains(tarefa)) {

            atualizarsubTarefa(tarefa);
            organizeTree(tarefa, tarefa.getSubTarefas());

        } else {
            adicionarSubTarefa(tarefa);
            organizeTree(tarefa, tarefa.getSubTarefas());

        }

    }

    public void atualizarsubTarefa(Tarefa tarefa) {

        Item it = view.getSubTarefasTable().getItem(tarefa);

        it.getItemProperty(GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaCod")).setValue(GestorPresenter.buildButtonEditarTarefa(view.getSubTarefasTable(), this, tarefa, tarefa.getGlobalID()));
        it.getItemProperty(GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaTitulo")).setValue(GestorPresenter.buildButtonEditarTarefa(view.getSubTarefasTable(), this, tarefa, tarefa.getHierarquia().getCategoria()));
        it.getItemProperty(GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaNome")).setValue(GestorPresenter.buildButtonEditarTarefa(view.getSubTarefasTable(), this, tarefa, tarefa.getNome()));
        it.getItemProperty(GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaEmpresaFilial")).setValue(tarefa.getEmpresa().getNome()
                + (tarefa.getFilialEmpresa() != null ? "/" + tarefa.getFilialEmpresa().getNome() : ""));
        it.getItemProperty(GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaSolicitante")).setValue(tarefa.getUsuarioSolicitante().getNome());
        it.getItemProperty(GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaResponsavel")).setValue(tarefa.getUsuarioResponsavel().getNome());
        it.getItemProperty(GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaDataInicio")).setValue(FormatterUtil.formatDate(tarefa.getDataInicio()));
        it.getItemProperty(GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaDataFim")).setValue(FormatterUtil.formatDate(tarefa.getDataInicio()));
        it.getItemProperty(GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaStatus")).setValue(GestorPresenter.buildPopUpStatusTarefa(view.getSubTarefasTable(), tarefa, this));
        /**
         * COMENTADO: Projeção postergada para v2
         * it.getItemProperty(GestorPresenter.getMENSAGENS().getString("TarefaView.subTarefasTable.colunaProjecao")).setValue(tarefa.getProjecao().toString().charAt(0));
         */
        it.getItemProperty("[E]").setValue(new Button("E"));
        it.getItemProperty("[C]").setValue(new Button("C"));

    }

    @Override
    public void removerParticipante(Participante participanteTarefa) {

        if (!participanteTarefa.getUsuarioInclusao().equals(GestorPresenter.getUsuarioLogado())) {
            Notification.show("Ops, apenas " + participanteTarefa.getUsuarioInclusao().getNome() + " pode remover este participante.", Notification.Type.WARNING_MESSAGE);
        } else {

            view.getParticipantesTable().removeItem(participanteTarefa);
            Tarefa tarefa = participanteTarefa.getTarefa();
            tarefa.getParticipantes().remove(participanteTarefa);
        }
    }

    @Override
    public void adicionarParticipante(Tarefa tarefa, Usuario usuario) {

        /*
        Adicionei o Notification.TYPE_WARNING_MESSAGE ao método Notification.show()
        pois no default, a notificação sumia ao mover o mouse.
        Com Notification.TYPE_WARNING_MESSAGE como Type da Notification, 
        a mensagem é exibida por alguns instantes, independente do movimento do mouse.
         */
        if (usuario.equals(view.getUsuarioResponsavelCombo().getValue()) || usuario.equals(GestorPresenter.getUsuarioLogado())) {
            Notification.show(GestorPresenter.getMENSAGENS().getString("Notificacao.ParticipanteUsuarioResponsavel"), Notification.TYPE_WARNING_MESSAGE);
        } else {
            List<Participante> participantesTarefaList = tarefa.getParticipantes();

            /*Approach necessário para não permitir incluir 
            várias vezes o mesmo usuario como participante*/
            boolean repetindoParticipante = false;
            for (Participante participanteTarefa : participantesTarefaList) {
                if (participanteTarefa.getUsuarioParticipante().equals(usuario)) {
                    repetindoParticipante = true;
                }
            }

            if (repetindoParticipante) {
                Notification.show(GestorPresenter.getMENSAGENS().
                        getString("Notificacao.UsuarioJaParticipa"),
                        Notification.TYPE_WARNING_MESSAGE);
            } else {

                Participante participanteTarefa = TarefaModel.criarParticipante(usuario, tarefa);
                view.getParticipantesContainer().addBean(participanteTarefa);

                if (tarefa.getParticipantes() == null) {
                    tarefa.setParticipantes(new ArrayList<>());
                }

                tarefa.getParticipantes().add(participanteTarefa);
            }

        }

    }

    @Override
    public void anexoAdicionado(Tarefa tarefa, File anexo) {
        Anexo anexoTarefa = new Anexo();
        anexoTarefa.setNome(anexo.getName());
        anexoTarefa.setDataHoraInclusao(LocalDateTime.now());
        anexoTarefa.setUsuarioInclusao(GestorPresenter.getUsuarioLogado());
        anexoTarefa.setTarefa(tarefa);
        anexoTarefa.setArquivoTemporario(anexo);
        anexoTarefa.setCaminhoCompleto(anexo.getAbsolutePath());
        if (tarefa.getAnexos() == null) {
            tarefa.setAnexos(new ArrayList<>());
        }
        tarefa.getAnexos().add(anexoTarefa);
        view.getAnexosContainer().addBean(anexoTarefa);

    }

    @Override
    public void hierarquiaSelecionada(HierarquiaProjetoDetalhe hierarquiaProjetoDetalhe) {

        if (hierarquiaProjetoDetalhe != null) {
            //verifica se será possivel criar sub
            if (TarefaModel.getProximasCategorias(hierarquiaProjetoDetalhe).isEmpty()) {
                view.getAdicionarSubtarefaButton().setEnabled(false);
            }

            view.setCaption(GestorPresenter.getMENSAGENS().getString("TarefaView.titulo.cadastro") + hierarquiaProjetoDetalhe.getCategoria());
        }
    }

    @Override
    public void empresaSelecionada(Tarefa tarefa, Empresa empresaSelecionada) {

        Empresa empresaAnterior = tarefa.getEmpresa();

        boolean empresaSendoDeSelecionada = (empresaSelecionada == null);
        boolean empresaSendoAlterada = (empresaSelecionada != null) && (!empresaSelecionada.equals(empresaAnterior));

        tarefa.setEmpresa(empresaSelecionada);

        if (empresaSendoDeSelecionada || empresaSendoAlterada) {

            GestorPresenter.resetaCombo(view.getUsuarioResponsavelCombo());
            tarefa.setUsuarioResponsavel(null);

            GestorPresenter.resetaCombo(view.getParticipantesCombo(), view.getParticipantesContainer());
            tarefa.setParticipantes(new ArrayList<>());

            GestorPresenter.resetaCombo(view.getDepartamentoCombo());
            GestorPresenter.resetaCombo(view.getCentroCustoCombo());

        }

        if (empresaSendoAlterada) {

            GestorPresenter.carregaComboUsuarios(view.getUsuarioResponsavelCombo(), empresaSelecionada);
            GestorPresenter.carregaComboUsuarios(view.getParticipantesCombo(), empresaSelecionada);
            GestorPresenter.carregaComboDepartamento(view.getDepartamentoCombo(), empresaSelecionada);
            GestorPresenter.carregaComboCentroCusto(view.getCentroCustoCombo(), empresaSelecionada);

        }

    }

    private boolean isStartDateValidForRecurrency() {

        // validates the initial and end date
        Date startDate = view.getDataInicioDateField().getValue();

        if (startDate == null) {
            Notification.show("Informe a data de início para recorrencia");
            return false;
        }

        return true;
    }

    @Override
    public void recurrenceClicked(Tarefa tarefa) {

        if (!isStartDateValidForRecurrency()) {
            return;
        }

        //Cria o pop up para registrar a conta (model e view)
        RecurrencyView RecorrencyView = new RecurrencyView(tarefa);

        //o presenter liga model e view
        RecorrencyPresenter = new RecurrencyPresenter(RecorrencyView, tarefa,
                DateTimeConverters.toLocalDate(view.getDataInicioDateField().getValue())
        );
        RecorrencyPresenter.addCallBackListener(this);

        //adiciona a visualização à UI
        UI.getCurrent().addWindow(RecorrencyView);

    }

    @Override
    public void recurrencyCreationDone(RecurrencySet recurrencySet) {
        this.recurrentDates = recurrencySet.getRecurrentDates();
        this.recurrencyMessage = recurrencySet.getRecurrencyMessage();
        view.getDataInicioDateField().setValue(DateTimeConverters.toDate(recurrencySet.getFirstTaskStartDate()));
        view.getDataFimDateField().setValue(DateTimeConverters.toDate(recurrencySet.getFirstTaskEndDate()));
        view.getMensagemRecorrenciaLabel().setVisible(true);
        view.getMensagemRecorrenciaLabel().setValue(recurrencyMessage);
    }

    @Override
    public void recurrencyRemoved(Tarefa task) {
        view.close();

        // Notifies the call back listener that the create/update is done
        for (CallBackListener callbackListener : callbackListeneres) {
            callbackListener.atualizarApresentacaoTarefa(task);
        }

    }

    @Override
    public void assigneeUserChanged(Tarefa task, Usuario assignee) {
        task.setUsuarioResponsavel(assignee);
        configPermissions(task);

    }

    @Override
    public void taskStatusChanged(Tarefa task) {
        view.setTarefa(task);
    }

    /**
     * Trata o evento disparado ao ser acionado o comando para remover uma
     * tarefa pelo botão de remoção
     *
     * @param tarefa
     */
    @Override
    public void removerTarefaButtonClicked(Tarefa tarefa) {

        LixeiraPresenter popUpRemocaoTarefaPresenter = new LixeiraPresenter(new LixeiraView());

        for (CallBackListener callbackListener : callbackListeneres) {
            popUpRemocaoTarefaPresenter.addTarefaCallBackListener(callbackListener);
        }

        popUpRemocaoTarefaPresenter.addTarefaCallBackListener(this);
        popUpRemocaoTarefaPresenter.apresentaConfirmacaoRemocaoTarefa(tarefa);

    }

    @Override
    public void metaRemovida(Meta meta) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void addCallbackListener(CallBackListener callBackListener) {
        callbackListeneres.add(callBackListener);
    }

    @Override
    public void atualizarApresentacaoMeta(Meta meta) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void tarefaRestaurada(Tarefa tarefa) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void metaRestaurada(Meta meta) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void custoHoraApontamentoValueChage() {
        view.getAlteraCustoHoraButton().setEnabled(true);
    }

    public TarefaView getView() {
        return view;
    }

}
