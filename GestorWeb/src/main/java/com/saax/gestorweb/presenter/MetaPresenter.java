package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.MetaModel;
import com.saax.gestorweb.model.TarefaModel;
import com.saax.gestorweb.model.EmpresaModel;
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
import com.saax.gestorweb.view.CadastroMetaCallBackListener;
import com.saax.gestorweb.view.MetaView;
import com.saax.gestorweb.view.MetaViewListener;
import com.saax.gestorweb.view.PopUpStatusListener;
import com.saax.gestorweb.view.TarefaCallBackListener;
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
public class MetaPresenter implements Serializable, MetaViewListener, TarefaCallBackListener, PopUpStatusListener {

    // Todo presenter mantem acesso à view e ao model
    private final transient MetaView view;
    private final transient MetaModel model;

    // recursos de aplicação
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final transient GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();

    // usuario logado
    private final Usuario loggedUser;
    private CadastroMetaCallBackListener callbackListener;

    /**
     * Cria o presenter ligando o Model ao View
     *
     * @param model
     * @param view
     */
    public MetaPresenter(MetaModel model,
            MetaView view) {

        this.model = model;
        this.view = view;

        loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO);

        view.setListener(this);

    }

    /**
     * Cria uma nova meta na categoria informada
     *
     * @param categoria
     */
    public void criarNovaMeta(HierarquiaProjetoDetalhe categoria) {

        Meta meta = model.criarNovaMeta(categoria, loggedUser);

        init(meta);

        view.getHierarquiaCombo().setEnabled(false);
        

    }

    /**
     * Edita uma Meta existente
     *
     * @param meta a ser alterada
     * 
     */
    @Override
    public void editarMeta(Meta meta) {

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

        carregaComboCategoria(meta);
        carregaComboEmpresa();
        carregaComboResponsavel();
        PresenterUtils.carregaComboEmpresaCliente(view.getEmpresaClienteCombo());
        carregaComboParticipante();

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

        EmpresaModel empresaModel = new EmpresaModel();

        List<Empresa> empresas = empresaModel.listarEmpresasParaSelecao(loggedUser);
        for (Empresa empresa : empresas) {

            empresaCombo.addItem(empresa);
            empresaCombo.setItemCaption(empresa, empresa.getNome());

        }

    }

    /**
     * Carrega o combo de seleção da empresa com todas as empresas relacionadas
     * ao usuário logado
     */
    private void carregaComboCategoria(Meta meta) {
        // configura a categoria
        ComboBox combo = view.getHierarquiaCombo();
        combo.addItem(meta.getCategoria());
        combo.setItemCaption(meta.getCategoria(), meta.getCategoria().getCategoria());

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
     * Trata o evento disparado ao soelecionar uma empresa e carrega a lista de
     * departamentos
     *
     * @param empresa
     */
    @Override
    public void empresaSelecionada(Empresa empresa) {
        PresenterUtils.carregaComboDepartamento(view.getDepartamentoCombo(), empresa);
        PresenterUtils.carregaComboCentroCusto(view.getCentroCustoCombo(), empresa);
        
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
        Meta meta = view.getMeta();

        if (meta.getUsuarioResponsavel() == null) {
            meta.setUsuarioResponsavel(meta.getUsuarioInclusao());
        }
        boolean novaMeta = meta.getId() == null;
        meta = model.gravarMeta(meta);

        TarefaModel tarefaModel = new TarefaModel();
        for (Tarefa tarefa : meta.getTarefas()) {
            tarefaModel.gravarTarefa(tarefa);
        }

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
            TarefaPresenter presenter = new TarefaPresenter(new TarefaModel(), new TarefaView());

            // Configure this as the object to be called when the task creation was done
            presenter.setCallBackListener(this);

            // Gets the tasks categories from the Target category
            List<HierarquiaProjetoDetalhe> tasksCategories = model.getFirstsTaskCategories(view.getMeta().getCategoria());

            // Tells the presenter which is gonna be the Tarefa's category
            presenter.createTask(view.getMeta(), tasksCategories);

        } catch (FieldGroup.CommitException ex) {
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
            TarefaView.buildButtonOpenTask(this, view.getTarefasTable(), task, task.getGlobalID()),
            TarefaView.buildButtonOpenTask(this, view.getTarefasTable(), task, task.getHierarquia().getCategoria()),
            TarefaView.buildButtonOpenTask(this, view.getTarefasTable(), task, task.getNome()),
            task.getEmpresa().getNome()
            + (task.getFilialEmpresa() != null ? "/" + task.getFilialEmpresa().getNome() : ""),
            task.getUsuarioSolicitante().getNome(),
            task.getUsuarioResponsavel().getNome(),
            FormatterUtil.formatDate(task.getDataInicio()),
            FormatterUtil.formatDate(task.getDataFim()),
            TarefaView.buildPopUpStatusProgressTask(view.getTarefasTable(), task, this),
            task.getProjecao().toString().charAt(0),
            new Button("E"),
            new Button("C")

        };
        view.getTarefasTable().addItem(gridRow, task);

        for (Tarefa subTarefa : task.getSubTarefas()) {
            addTaskInTable(subTarefa);
        }

    }

        @Override
    public void tarefaCriadaOuAtualizada(Tarefa tarefa) {

        if (view.getTarefasTable().getItemIds().contains(tarefa)){
            atualizarTarefaTable(tarefa);
        } else {
            addTaskInTable(tarefa);
            
        }
        organizeTree(tarefa, tarefa.getSubTarefas());

    }

    private void atualizarTarefaTable(Tarefa task) {
        Item it = view.getTarefasTable().getItem(task);

        it.getItemProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaCod")).setValue(TarefaView.buildButtonOpenTask(this, view.getTarefasTable(), task, task.getGlobalID()));
        it.getItemProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaTitulo")).setValue(TarefaView.buildButtonOpenTask(this, view.getTarefasTable(), task, task.getHierarquia().getCategoria()));
        it.getItemProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaNome")).setValue(TarefaView.buildButtonOpenTask(this, view.getTarefasTable(), task, task.getNome()));
        it.getItemProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaEmpresaFilial")).setValue(task.getEmpresa().getNome()
                + (task.getFilialEmpresa() != null ? "/" + task.getFilialEmpresa().getNome() : ""));
        it.getItemProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaSolicitante")).setValue(task.getUsuarioSolicitante().getNome());
        it.getItemProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaResponsavel")).setValue(task.getUsuarioResponsavel().getNome());
        it.getItemProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaDataInicio")).setValue(FormatterUtil.formatDate(task.getDataInicio()));
        it.getItemProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaDataFim")).setValue(FormatterUtil.formatDate(task.getDataInicio()));
        it.getItemProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaStatus")).setValue(TarefaView.buildPopUpStatusProgressTask(view.getTarefasTable(), task, this));
        it.getItemProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaProjecao")).setValue(task.getProjecao().toString().charAt(0));
        it.getItemProperty("[E]").setValue(new Button("E"));
        it.getItemProperty("[C]").setValue(new Button("C"));

        for (Tarefa subTarefa : task.getSubTarefas()) {
            addTaskInTable(subTarefa);
        }

    }

    /**
     * Carrega o combo de participante com todos os usuários ativos da mesma
     * empresa do usuário logado
     */
    private void carregaComboParticipante() {
        ComboBox participante = view.getParticipantesCombo();
        for (Usuario usuario : model.listarUsuariosEmpresa()) {
            participante.addItem(usuario);
            participante.setItemCaption(usuario, usuario.getNome());

        }
    }

    
    @Override
    public void taskStatusChanged(Tarefa task) {
        atualizarTarefaTable(task);
    }

    @Override
    public void adicionarParticipante(Usuario usuario) {
        if (usuario.equals(view.getResponsavelCombo().getValue()) || usuario.equals(loggedUser)) {
            Notification.show(mensagens.getString("Notificacao.ParticipanteUsuarioResponsavel"));
        } else {
            Participante participante = model.criarParticipante(usuario, view.getMeta());
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
        view.getParticipantesContainer().removeItem(participante);
        Meta meta = view.getMeta();
        meta.getParticipantes().remove(participante);

    }

    private void configPermissions(Meta meta) {

        boolean usuarioLogadoEhOResponsavel = meta.getUsuarioResponsavel() != null && meta.getUsuarioResponsavel().equals(loggedUser);
        boolean usuarioLogadoEhOSolicitante = meta.getUsuarioSolicitante() != null && meta.getUsuarioSolicitante().equals(loggedUser);

        view.setEditAllowed(usuarioLogadoEhOSolicitante || usuarioLogadoEhOResponsavel);
        view.getResponsavelCombo().setEnabled(usuarioLogadoEhOSolicitante);
    }

}
