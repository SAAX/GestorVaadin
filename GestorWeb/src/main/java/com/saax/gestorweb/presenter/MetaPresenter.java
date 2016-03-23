package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.EmpresaModel;
import com.saax.gestorweb.model.MetaModel;
import com.saax.gestorweb.model.TarefaModel;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.Participante;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.FormatterUtil;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.util.SessionAttributesEnum;
import com.saax.gestorweb.view.MetaView;
import com.saax.gestorweb.view.MetaViewListener;
import com.saax.gestorweb.view.PopUpStatusListener;
import com.saax.gestorweb.model.LixeiraModel;
import com.saax.gestorweb.model.datamodel.RecurrencySet;
import com.saax.gestorweb.view.TarefaView;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * @author Rodrigo
 */
public class MetaPresenter implements Serializable, CallBackListener, MetaViewListener, PopUpStatusListener {

    // Todo presenter mantem acesso à view e ao model
    private final transient MetaView view;

    // recursos de aplicação
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final transient GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();
    private final List<CallBackListener> callbackListeneres;

    // usuario logado
    private final Usuario loggedUser;

    @Override
    public List<CallBackListener> getCallbackListeneres() {
        return callbackListeneres;
    }

    /**
     * Cria o presenter ligando o Model ao View
     *
     * @param view
     */
    public MetaPresenter(MetaView view) {

        this.view = view;
        this.callbackListeneres = new ArrayList<>();

        loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO);

        view.setListener(this);

    }

    /**
     * Cria uma nova meta na categoria informada
     *
     * @param categoria
     * @param empresa
     */
    public void criarNovaMeta(HierarquiaProjetoDetalhe categoria, Empresa empresa) {

        Meta meta = MetaModel.criarNovaMeta(categoria, loggedUser);

        meta.setEmpresa(empresa);
        init(meta);

        //bug do combo de hierarquia
        empresaSelecionada(empresa);
        //view.getHierarquiaCombo().setEnabled(false);

    }

    /**
     * Edita uma Meta existente
     *
     * @param meta a ser alterada
     *
     */
    @Override
    public void editarMeta(Meta meta) {

        meta = MetaModel.refresh(meta);

        // sets the title
        view.exibeTituloEdicao(meta);

        // inits the UI
        init(meta);

        view.getParticipantesContainer().addAll(meta.getParticipantes());
        for (Tarefa task : meta.getTarefas()) {
            addTaskInTable(task);
        }
        organizeTree(null, meta.getTarefas());

    }

    /**
     * Inicializa o formulário carregando a meta informada
     */
    private void init(Meta meta) {

        carregaComboEmpresa();
        GestorPresenter.carregaComboUsuarios(view.getResponsavelCombo(), meta.getEmpresa());
        GestorPresenter.carregaComboEmpresaCliente(view.getEmpresaClienteCombo());
        GestorPresenter.carregaComboUsuarios(view.getParticipantesCombo(), meta.getEmpresa());

        // Abre o formulário
        UI.getCurrent().addWindow(view);

        // liga (bind) o form com a meta
        view.setMeta(meta);

        configPermissions(meta);

        view.setCaption(mensagens.getString("CadastroMetaView.tituloBase") + meta.getCategoria().getCategoria());

    }

    /**
     * Carrega o combo de seleção da empresa com todas as empresas relacionadas
     * ao usuário logado
     */
    private void carregaComboEmpresa() {
        ComboBox empresaCombo = view.getEmpresaCombo();

        List<Empresa> empresas = EmpresaModel.listarEmpresasAtivasUsuarioLogado(loggedUser);
        for (Empresa empresa : empresas) {

            empresaCombo.addItem(empresa);
            empresaCombo.setItemCaption(empresa, empresa.getNome());

        }

    }

    /**
     * Carrega o combo de responsáveis com todos os usuários ativos da mesma
     * empresa do usuário logado
     */
    /**
     * Trata o evento disparado ao soelecionar uma empresa e carrega a lista de
     * departamentos
     *
     * @param empresaSelecionada
     */
    @Override
    public void empresaSelecionada(Empresa empresaSelecionada) {

        //Empresa empresaAnterior = view.getMeta().getEmpresa();
        boolean empresaSendoDeSelecionada = (empresaSelecionada == null);
        //boolean empresaSendoAlterada = (empresaSelecionada != null) && (!empresaSelecionada.equals(empresaAnterior));
        boolean empresaSendoAlterada = true;

        view.getMeta().setEmpresa(empresaSelecionada);

        if (empresaSendoDeSelecionada || empresaSendoAlterada) {

            GestorPresenter.resetaCombo(view.getResponsavelCombo());
            view.getMeta().setUsuarioResponsavel(null);

            GestorPresenter.resetaCombo(view.getParticipantesCombo(), view.getParticipantesContainer());
            view.getMeta().setParticipantes(new ArrayList<>());

            GestorPresenter.resetaCombo(view.getDepartamentoCombo());
            GestorPresenter.resetaCombo(view.getCentroCustoCombo());
            GestorPresenter.resetaCombo(view.getHierarquiaCombo());
        }

        if (empresaSendoAlterada) {

            GestorPresenter.carregaComboUsuarios(view.getResponsavelCombo(), empresaSelecionada);
            GestorPresenter.carregaComboUsuarios(view.getParticipantesCombo(), empresaSelecionada);
            GestorPresenter.carregaComboDepartamento(view.getDepartamentoCombo(), empresaSelecionada);
            GestorPresenter.carregaComboCentroCusto(view.getCentroCustoCombo(), empresaSelecionada);
            GestorPresenter.carregaComboCategoria(view.getHierarquiaCombo(), empresaSelecionada, 1);

        }

    }

    @Override
    public void gravarButtonClicked() {
        Meta meta = view.getMeta();

        if (meta.getUsuarioResponsavel() == null) {
            meta.setUsuarioResponsavel(meta.getUsuarioInclusao());
        }
        meta = MetaModel.gravarMeta(meta);

        for (Tarefa tarefa : meta.getTarefas()) {
            TarefaModel.gravarTarefa(tarefa);
        }

        // notica (se existir) algum listener interessado em saber que o cadastro foi finalizado.
        for (CallBackListener callbackListener : callbackListeneres) {
            callbackListener.atualizarApresentacaoMeta(meta);
        }
        view.close();
        Notification.show(meta.getCategoria().getCategoria() + mensagens.getString("CadastroMetaPresenter.mensagem.gravadoComSucesso"), Notification.Type.HUMANIZED_MESSAGE);
    }

    @Override
    public void cancelarButtonClicked() {
        UI.getCurrent().removeWindow(view);
    }

    /**
     * Handle the event thrown when the "addTask" button is clicked, indicating
     * that the user wants a new task to the Target. Creates and presents a new
     * form (presenter) to create a new Tarefa under this Target
     */
    @Override
    public void addTaskButtonClicked() {

        try {
            // commit and run validators
            view.setValidatorsVisible(true);
            view.getMetaFieldGroup().commit();

            // Creates the presenter that will handle the new task creation
            TarefaPresenter presenter = new TarefaPresenter(new TarefaView());

            // Configure this as the object to be called when the task creation was done
            presenter.addCallBackListener(this);

            // Gets the tasks categories from the Target category
            List<HierarquiaProjetoDetalhe> tasksCategories = MetaModel.getFirstsTaskCategories(view.getMeta().getCategoria());

            // Tells the presenter which is gonna be the Tarefa's category
            presenter.createTask(view.getMeta(), tasksCategories, null);

        }
        catch (FieldGroup.CommitException ex) {
            Notification.show(mensagens.getString("CadastroMetaPresenter.addTaskButtonClicked.commitException"), Notification.Type.HUMANIZED_MESSAGE);
        }
    }

    //Projeção será inserida na V2
    /**
     * Event thrown when the "chat" button is clicked to start or continue a
     * conversation between the stackholders of the Target
     */
//    @Override
//    public void forecastButtonClickedd() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    private void organizeTree(Tarefa parentTask, List<Tarefa> subTasks) {

        for (Tarefa subTask : subTasks) {
            view.getTarefasTable().setParent(subTask, parentTask);
            if (subTask.getSubTarefas() != null && !subTask.getSubTarefas().isEmpty()) {
                organizeTree(subTask, subTask.getSubTarefas());
            }
        }
    }

    private void addTaskInTable(Tarefa task) {
        Object[] gridRow = new Object[]{
            GestorPresenter.buildButtonEditarTarefa(this, view.getTarefasTable(), task, task.getGlobalID()),
            GestorPresenter.buildButtonEditarTarefa(this, view.getTarefasTable(), task, task.getHierarquia().getCategoria()),
            GestorPresenter.buildButtonEditarTarefa(this, view.getTarefasTable(), task, task.getNome()),
            task.getEmpresa().getNome()
            + (task.getFilialEmpresa() != null ? "/" + task.getFilialEmpresa().getNome() : ""),
            task.getUsuarioSolicitante().getNome(),
            task.getUsuarioResponsavel().getNome(),
            FormatterUtil.formatDate(task.getDataInicio()),
            FormatterUtil.formatDate(task.getDataFim()),
            GestorPresenter.buildPopUpStatusTarefa(view.getTarefasTable(), task, this),
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
     * Trata o evento disparado via callback quando uma tarefa é removida
     *
     * @param tarefaRemovida
     */
    @Override
    public void tarefaRemovida(Tarefa tarefaRemovida) {
        // Se a tarefa removida for uma sub da que está sendo editada
        if (view.getTarefasTable().getItemIds().contains(tarefaRemovida)) {
            // remove a sub da lista
            view.getTarefasTable().getItemIds().remove(tarefaRemovida);
        }
    }

    /**
     * Trata o evento disparado ao concluir a criação de uma nova tarefa ou
     * alteração de uma
     *
     * @param tarefaCriada
     */
    @Override
    public void atualizarApresentacaoTarefa(Tarefa tarefaCriada) {

        if (view.getTarefasTable().getItemIds().contains(tarefaCriada)) {
            atualizarTarefaTable(tarefaCriada);
        } else {
            addTaskInTable(tarefaCriada);

        }
        organizeTree(tarefaCriada, tarefaCriada.getSubTarefas());

    }

    private void atualizarTarefaTable(Tarefa task) {
        Item it = view.getTarefasTable().getItem(task);

        it.getItemProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaCod")).setValue(GestorPresenter.buildButtonEditarTarefa(this, view.getTarefasTable(), task, task.getGlobalID()));
        it.getItemProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaTitulo")).setValue(GestorPresenter.buildButtonEditarTarefa(this, view.getTarefasTable(), task, task.getHierarquia().getCategoria()));
        it.getItemProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaNome")).setValue(GestorPresenter.buildButtonEditarTarefa(this, view.getTarefasTable(), task, task.getNome()));
        it.getItemProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaEmpresaFilial")).setValue(task.getEmpresa().getNome()
                + (task.getFilialEmpresa() != null ? "/" + task.getFilialEmpresa().getNome() : ""));
        it.getItemProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaSolicitante")).setValue(task.getUsuarioSolicitante().getNome());
        it.getItemProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaResponsavel")).setValue(task.getUsuarioResponsavel().getNome());
        it.getItemProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaDataInicio")).setValue(FormatterUtil.formatDate(task.getDataInicio()));
        it.getItemProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaDataFim")).setValue(FormatterUtil.formatDate(task.getDataInicio()));
        it.getItemProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaStatus")).setValue(GestorPresenter.buildPopUpStatusTarefa(view.getTarefasTable(), task, this));
        it.getItemProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaProjecao")).setValue(task.getProjecao().toString().charAt(0));
        it.getItemProperty("[E]").setValue(new Button("E"));
        it.getItemProperty("[C]").setValue(new Button("C"));

        for (Tarefa subTarefa : task.getSubTarefas()) {
            addTaskInTable(subTarefa);
        }

    }

    @Override
    public void taskStatusChanged(Tarefa task) {
        atualizarTarefaTable(task);
    }

    @Override
    public void adicionarParticipante(Usuario usuario) {
        if (usuario.equals(view.getResponsavelCombo().getValue()) || usuario.equals(loggedUser)) {
            Notification.show(GestorPresenter.getMENSAGENS().
                    getString("Notificacao.ParticipanteUsuarioResponsavel"),
                    Notification.TYPE_WARNING_MESSAGE);
        } else {
            Participante participante = MetaModel.criarParticipante(usuario, view.getMeta());
            view.getParticipantesContainer().addBean(participante);
            Meta meta = view.getMeta();

            if (meta.getParticipantes() == null) {
                meta.setParticipantes(new ArrayList<>());
            }

            meta.getParticipantes().add(participante);
        }
    }

    @Override
    public void removerParticipante(Participante participante) {
        if (!participante.getUsuarioInclusao().equals(GestorPresenter.getUsuarioLogado())) {
            Notification.show("Ops, apenas " + participante.getUsuarioInclusao().getNome() + " pode remover este participante.", Notification.Type.WARNING_MESSAGE);
        } else {
            view.getParticipantesContainer().removeItem(participante);
            Meta meta = view.getMeta();
            meta.getParticipantes().remove(participante);
        }
    }

    private void configPermissions(Meta meta) {

        boolean usuarioLogadoEhOResponsavel = meta.getUsuarioResponsavel() != null && meta.getUsuarioResponsavel().equals(loggedUser);
        boolean usuarioLogadoEhOSolicitante = meta.getUsuarioSolicitante() != null && meta.getUsuarioSolicitante().equals(loggedUser);

        view.setEditAllowed(usuarioLogadoEhOSolicitante || usuarioLogadoEhOResponsavel);
        view.getResponsavelCombo().setEnabled(usuarioLogadoEhOSolicitante);

        view.getRemoverMetaButton().setEnabled(LixeiraModel.verificaPermissaoAcessoRemocaoMeta(meta, GestorPresenter.getUsuarioLogado()));
    }

    @Override
    public void removerMetaButtonClicked(Meta meta) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void metaRemovida(Meta meta) {
        view.close();
    }

    @Override
    public void recurrencyCreationDone(RecurrencySet recurrencySet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void recurrencyRemoved(Tarefa task) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addCallbackListener(CallBackListener callBackListener) {
        this.callbackListeneres.add(callBackListener);
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

}
