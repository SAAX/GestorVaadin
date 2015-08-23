package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.MetaModel;
import com.saax.gestorweb.model.TarefaModel;
import com.saax.gestorweb.model.ChatSingletonModel;

import com.saax.gestorweb.model.DashboardModel;
import com.saax.gestorweb.model.EmpresaModel;
import com.saax.gestorweb.model.PopUpStatusModel;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.FilialEmpresa;
import com.saax.gestorweb.model.datamodel.HierarquiaProjeto;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.ProjecaoTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.TipoTarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.FormatterUtil;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.view.TarefaCallBackListener;
import com.saax.gestorweb.view.TaskView;
import com.saax.gestorweb.view.DashboardView;
import com.saax.gestorweb.view.DashboardViewListenter;
import com.vaadin.data.Item;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.SessionAttributesEnum;
import com.saax.gestorweb.view.CadastroMetaCallBackListener;
import com.saax.gestorweb.view.MetaView;
import com.saax.gestorweb.view.ChatView;
import com.saax.gestorweb.view.PopUpStatusListener;
import com.saax.gestorweb.view.PopUpStatusView;
import com.vaadin.ui.Button;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.UI;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import org.vaadin.hene.popupbutton.PopupButton;

/**
 * Presenter do Dashboard <br>
 * Esta classe é responsável captar todos os eventos que ocorrem na View e dar o
 * devido tratamento, utilizando para isto o modelo.
 *
 *
 * @author Rodrigo
 */
public class DashboardPresenter implements DashboardViewListenter, TarefaCallBackListener, CadastroMetaCallBackListener, PopUpStatusListener, Serializable {

    // Todo presenter mantem acesso à view e ao model
    private final transient DashboardView view;
    private final transient DashboardModel model;

    // Referencia ao recurso das mensagens:
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final transient GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();
    private Usuario loggedUser;
    private TarefaPresenter tarefaPresenter;

    /**
     * Inicializa o presenter
     */
    @Override
    public void init() {

        loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO.getAttributeName());

        adicionarHierarquiasProjeto();
        carregaVisualizacaoInicial();
    }

    /**
     * Obtem as hierarquias de projeto customizadas e adiciona no menu "Criar"
     */
    private void adicionarHierarquiasProjeto() {

        // obtem as hierarquias customizadas
        List<HierarquiaProjeto> hierarquias = model.getHierarquiasProjeto();

        // menu "Criar"
        MenuBar.MenuItem menuCriar = view.getCreateNewByCategoryMenuItem();

        // adiciona cada hierarquia customizada
        for (HierarquiaProjeto hierarquia : hierarquias) {
            MenuBar.MenuItem menuProjeto = menuCriar.addItemBefore(hierarquia.getNome(), null, null, view.getCreateNewByTemplate());
            Collections.sort(hierarquia.getCategorias());
            for (HierarquiaProjetoDetalhe categoria : hierarquia.getCategorias()) {
                menuProjeto.addItem(categoria.getCategoria(), (MenuBar.MenuItem selectedItem) -> {
                    criarNova(categoria);
                });
            }

        }
    }

    private void organizeTree(TreeTable table, Object parentTaskOrTarget, List<Tarefa> subTasks) {

        for (Tarefa subTask : subTasks) {
            table.setParent(subTask, parentTaskOrTarget);
            if (subTask.getSubTarefas() != null && !subTask.getSubTarefas().isEmpty()) {
                organizeTree(table, subTask, subTask.getSubTarefas());
            }
        }
    }

    private Button buildButtonEditarTarefa(Tarefa tarefa, String caption) {
        Button link = new Button(caption);
        link.setStyleName("quiet");
        TarefaCallBackListener callback = this;
        link.addClickListener((Button.ClickEvent event) -> {
            view.getTaskTable().setValue(tarefa);
            TarefaPresenter presenter = new TarefaPresenter(new TarefaModel(), new TaskView());
            presenter.setCallBackListener(callback);
            presenter.editar(tarefa);
        });
        return link;
    }

    private void atualizarTarefaTable(Tarefa tarefa) {

        if (tarefa == null || tarefa.getGlobalID() == null) {
            throw new IllegalArgumentException("Tarefa nula");
        }

        Item it = view.getTaskTable().getItem(tarefa);

        if (it != null) {

            it.getItemProperty(mensagens.getString("DashboardView.taskTable.cod")).setValue(buildButtonEditarTarefa(tarefa, tarefa.getGlobalID()));
            it.getItemProperty(mensagens.getString("DashboardView.taskTable.title")).setValue(buildButtonEditarTarefa(tarefa, tarefa.getHierarquia().getCategoria()));
            it.getItemProperty(mensagens.getString("DashboardView.taskTable.name")).setValue(buildButtonEditarTarefa(tarefa, tarefa.getNome()));
            it.getItemProperty(mensagens.getString("DashboardView.taskTable.company")).setValue(tarefa.getEmpresa().getNome()
                    + (tarefa.getFilialEmpresa() != null ? "/" + tarefa.getFilialEmpresa().getNome() : ""));
            it.getItemProperty(mensagens.getString("DashboardView.taskTable.requestor")).setValue(tarefa.getUsuarioSolicitante().getNome());
            it.getItemProperty(mensagens.getString("DashboardView.taskTable.assingee")).setValue(tarefa.getUsuarioResponsavel().getNome());
            it.getItemProperty(mensagens.getString("DashboardView.taskTable.startDate")).setValue(FormatterUtil.formatDate(tarefa.getDataInicio()));
            it.getItemProperty(mensagens.getString("DashboardView.taskTable.endDate")).setValue(FormatterUtil.formatDate(tarefa.getDataFim()));
            it.getItemProperty(mensagens.getString("DashboardView.taskTable.state")).setValue(buildPopUpEvolucaoStatusEAndamento(tarefa));
            it.getItemProperty(mensagens.getString("DashboardView.taskTable.forecast")).setValue(tarefa.getProjecao().toString().charAt(0));
            it.getItemProperty(mensagens.getString("DashboardView.taskTable.email")).setValue(new Button("E"));
            it.getItemProperty(mensagens.getString("DashboardView.taskTable.chat")).setValue(new Button("C"));

            // se a tarefa possui subs, chama recursivamente
            for (Tarefa subTarefa : tarefa.getSubTarefas()) {
                if (view.getTaskTable().getItemIds().contains(subTarefa)) {
                    atualizarTarefaTable(subTarefa);
                } else {
                    adicionarTarefaTable(subTarefa);
                }
            }
        }

    }

    private void updateTargetTable(Meta target) {
        Item it = view.getTargetTable().getItem(target);

        it.getItemProperty(mensagens.getString("DashboardView.targetTable.cod")).setValue(buildButtonEditarMeta(target, target.getGlobalID()));
        it.getItemProperty(mensagens.getString("DashboardView.targetTable.title")).setValue(buildButtonEditarMeta(target, target.getCategoria().getCategoria()));
        it.getItemProperty(mensagens.getString("DashboardView.targetTable.name")).setValue(buildButtonEditarMeta(target, target.getNome()));
        it.getItemProperty(mensagens.getString("DashboardView.targetTable.company")).setValue(target.getEmpresa().getNome()
                + (target.getFilialEmpresa() != null ? "/" + target.getFilialEmpresa().getNome() : ""));
        it.getItemProperty(mensagens.getString("DashboardView.targetTable.requestor")).setValue(target.getUsuarioSolicitante().getNome());
        it.getItemProperty(mensagens.getString("DashboardView.targetTable.assingee")).setValue(target.getUsuarioResponsavel().getNome());
        it.getItemProperty(mensagens.getString("DashboardView.targetTable.startDate")).setValue(FormatterUtil.formatDate(target.getDataInicio()));
        it.getItemProperty(mensagens.getString("DashboardView.targetTable.endDate")).setValue(FormatterUtil.formatDate(target.getDataFim()));
        it.getItemProperty(mensagens.getString("DashboardView.targetTable.forecast")).setValue('F');
        it.getItemProperty(mensagens.getString("DashboardView.targetTable.email")).setValue(new Button("E"));

        // se a meta possui tarefas, chama recursivamente
        for (Tarefa subTarefa : target.getTarefas()) {
            addTaskInTargetTable(subTarefa);
        }

    }

    /**
     * Callback notificando que o cadastro de uma nova tarefa foi concluido
     *
     * @param tarefa
     */
    @Override
    public void tarefaCriadaOuAtualizada(Tarefa tarefa) {

        if (tarefa.isRemovida()) {
            
            if (view.getTaskTable().getItemIds().contains(tarefa)) {
                view.getTaskTable().removeItem(tarefa);
            }
            
        } else {

            if (view.getTaskTable().getItemIds().contains(tarefa)) {
                atualizarTarefaTable(tarefa);

            } else {
                adicionarTarefaTable(tarefa);

            }

            organizeTree(view.getTaskTable(), tarefa, tarefa.getSubTarefas());
            
            if (tarefa.getTipoRecorrencia() == TipoTarefa.RECORRENTE) {

                Tarefa next = tarefa.getProximaTarefa();
                
                if (next != null) {
                    tarefaCriadaOuAtualizada(next);
                }
            }
        }
    }

    @Override
    public void createsNewTaskByTemplate() {
        List<Tarefa> templates = model.getTarefasTemplate();
        view.abrirPopUpSelecaoTemplates(templates);

    }

    private void criarNova(HierarquiaProjetoDetalhe categoria) {

        if (categoria.getNivel() == 1) {
            MetaPresenter presenter = new MetaPresenter(new MetaModel(), new MetaView());
            presenter.setCallBackListener(this);
            presenter.criarNovaMeta(categoria);
        } else if (categoria.getNivel() == 2) {
            TarefaPresenter presenter = new TarefaPresenter(new TarefaModel(), new TaskView());
            presenter.setCallBackListener(this);
            presenter.createTask(categoria);
        }

    }

    @Override
    public void cadastroMetaConcluido(Meta metaCriada) {
        adicionarMetaTable(metaCriada);
        organizeTree(view.getTargetTable(), metaCriada, metaCriada.getTarefas());
    }

    @Override
    public void edicaoMetaConcluida(Meta meta) {
        updateTargetTable(meta);
        organizeTree(view.getTargetTable(), meta, meta.getTarefas());
    }

    /**
     * Opção disponível apenas para testes, onde é possível alterar o usuário
     * logado
     */
    @Override
    public void usuarioLogadoAlteradoAPENASTESTE() {
        this.loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO.getAttributeName());
        carregaVisualizacaoInicial();
    }

    @Override
    public void taskStatusChanged(Tarefa task) {
        atualizarTarefaTable(task);
    }

    public void openTask(Tarefa taskToOpen) {
        tarefaPresenter = new TarefaPresenter(new TarefaModel(), new TaskView());
        tarefaPresenter.setCallBackListener(this);
        tarefaPresenter.editar(taskToOpen);

    }

    @Override
    public void criarTarefaPorTemplate(Tarefa template) {

        if (template != null) {
            tarefaPresenter = new TarefaPresenter(new TarefaModel(), new TaskView());
            tarefaPresenter.setCallBackListener(this);

            Tarefa novaTarefa;
            novaTarefa = model.criarNovaTarefaPeloTemplate(template);
            tarefaPresenter.editar(novaTarefa);

        }

    }

    // enumeracao do tipo de pesquisa
    public enum TipoPesquisa {

        INCLUSIVA_OU, EXCLUSIVA_E
    };

    /**
     * Cria o presenter ligando o Model ao View
     *
     * @param model
     * @param view
     */
    public DashboardPresenter(DashboardModel model,
            DashboardView view) {

        this.model = model;
        this.view = view;

        view.setListener(this);

    }

    /**
     * Chama o Logout geral do MDI
     */
    @Override
    public void logout() {

        ((GestorMDI) UI.getCurrent()).logout();
    }

    /**
     * Carrega os campos necessarios ao abrir a visualização
     */
    private void carregaVisualizacaoInicial() {

        carregarListaTarefasUsuarioLogado();
        carregarListaMetasUsuarioLogado();
        carregarFiltrosPesquisa();
        carregarListaTarefasPrincipais();
    }

    /**
     * Carregar os filtros de pesquisa
     */
    public void carregarFiltrosPesquisa() {

        List<Usuario> usuarios = model.listarUsuariosEmpresa();
        for (Usuario usuario : usuarios) {
            view.getAssigneesFilterOptionGroup().addItem(usuario);
            view.getAssigneesFilterOptionGroup().setItemCaption(usuario, usuario.getNome());

            view.getRequestorsFilterOptionGroup().addItem(usuario);
            view.getRequestorsFilterOptionGroup().setItemCaption(usuario, usuario.getNome());

            view.getFollowersFilterOptionGroup().addItem(usuario);
            view.getFollowersFilterOptionGroup().setItemCaption(usuario, usuario.getNome());

        }

        EmpresaModel empresaModel = new EmpresaModel();
        List<Empresa> empresas = empresaModel.listarEmpresasParaSelecao(loggedUser);
        for (Empresa empresa : empresas) {

            view.getCompanyFilterOptionGroup().addItem(empresa);
            view.getCompanyFilterOptionGroup().setItemCaption(empresa, empresa.getNome());

            for (FilialEmpresa filial : empresa.getFiliais()) {
                view.getCompanyFilterOptionGroup().addItem(filial);
                view.getCompanyFilterOptionGroup().setItemCaption(filial, "Filial: " + filial.getNome());

            }
        }

        for (ProjecaoTarefa projecao : ProjecaoTarefa.values()) {
            view.getForecastFilterOptionGroup().addItem(projecao);
            view.getForecastFilterOptionGroup().setItemCaption(projecao, projecao.toString());
        }

        view.getSwitchAndOrFilters().addItem(TipoPesquisa.EXCLUSIVA_E);
        view.getSwitchAndOrFilters().setItemCaption(TipoPesquisa.EXCLUSIVA_E, "Todos os filtros");
        view.getSwitchAndOrFilters().addItem(TipoPesquisa.INCLUSIVA_OU);
        view.getSwitchAndOrFilters().setItemCaption(TipoPesquisa.INCLUSIVA_OU, "Pelo menos um filtro");

    }

    /**
     * Carrega a lista de tarefas sob responsabilidade do usuario logado
     */
    @Override
    public void carregarListaTarefasUsuarioLogado() {

        List<Tarefa> listaTarefas = model.listarTarefas(loggedUser);

        exibirListaTarefas(listaTarefas);

    }

    /**
     * Carrega o box inferior esquerdo com as tarefas principais
     */
    private void carregarListaTarefasPrincipais() {

        List<Tarefa> tarefasPrincipais = model.listarTarefasPrincipais(loggedUser);

        view.getBottomTasksContainer().removeAllComponents();
        for (int i = 0; i < tarefasPrincipais.size() && i < 5; i++) {
            Tarefa tarefa = tarefasPrincipais.get(i);

            Button tarefaButton = buildButtonEditarTarefa(tarefa, tarefa.getNome().length() > 25 ? tarefa.getNome().substring(0, 25) : tarefa.getNome());
            tarefaButton.setStyleName("v-button-link");
            view.getBottomTasksContainer().addComponent(tarefaButton);
        }

    }

    /**
     * Constrói o pop up de alteração de status e/ou andamento de tarefas neste
     * PopUP o usuario poderá alterar (evoluir ou regredir) um status de tarefa
     * ou indicar seu andamento.
     *
     * @param tarefa
     * @return
     */
    private PopupButton buildPopUpEvolucaoStatusEAndamento(Tarefa tarefa) {

        // comportmento e regras:
        PopUpStatusView viewPopUP = new PopUpStatusView();
        PopUpStatusModel modelPopUP = new PopUpStatusModel();

        PopUpStatusPresenter presenter = new PopUpStatusPresenter(viewPopUP, modelPopUP);

        presenter.load(tarefa, null, this);

        // evento disparado quando o pop-up se torna visivel:
        // seleciona a linha correta na tabela
        presenter.getStatusButton().addPopupVisibilityListener((PopupButton.PopupVisibilityEvent event) -> {
            if (event.isPopupVisible()) {
                // selecionar a linha clicada:
                Tarefa tarefaEditada = (Tarefa) event.getPopupButton().getData();
                this.view.getTaskTable().setValue(tarefa);
            }
        });

        return presenter.getStatusButton();
    }

    /**
     * Carrega a lista de tarefas na tabela
     *
     * @param listaTarefas
     */
    public void exibirListaTarefas(List<Tarefa> listaTarefas) {

        view.getTaskTable().removeAllItems();

        Object[] linha;
        listaTarefas.stream().forEach((tarefa) -> {
            adicionarTarefaTable(tarefa);
            organizeTree(view.getTaskTable(), tarefa, tarefa.getSubTarefas());
        });

    }

    /**
     * Adiciona a tarefa na tree table
     *
     * @param tarefa
     */
    private void adicionarTarefaTable(Tarefa tarefa) {

        Object[] linha = new Object[]{
            buildButtonEditarTarefa(tarefa, tarefa.getGlobalID()),
            buildButtonEditarTarefa(tarefa, tarefa.getHierarquia().getCategoria()),
            buildButtonEditarTarefa(tarefa, tarefa.getNome()),
            tarefa.getEmpresa().getNome()
            + (tarefa.getFilialEmpresa() != null ? "/" + tarefa.getFilialEmpresa().getNome() : ""),
            tarefa.getUsuarioSolicitante().getNome(),
            tarefa.getUsuarioResponsavel().getNome(),
            FormatterUtil.formatDate(tarefa.getDataInicio()),
            FormatterUtil.formatDate(tarefa.getDataFim()),
            buildPopUpEvolucaoStatusEAndamento(tarefa),
            tarefa.getProjecao().toString().charAt(0),
            new Button("E"),
            new Button("Chat", (Button.ClickEvent event) -> {
                chatButtonClicked(tarefa);
            })
        };

        view.getTaskTable().addItem(linha, tarefa);

        // se a tarefa possui subs, chama recursivamente
        for (Tarefa subTarefa : tarefa.getSubTarefas()) {
            adicionarTarefaTable(subTarefa);
        }

    }

    /**
     * Adiciona a meta na tree table
     *
     * @param meta
     */
    private void adicionarMetaTable(Meta meta) {

        Object[] linha = new Object[]{
            buildButtonEditarMeta(meta, meta.getGlobalID()),
            buildButtonEditarMeta(meta, meta.getCategoria().getCategoria()),
            buildButtonEditarMeta(meta, meta.getNome()),
            meta.getEmpresa().getNome()
            + (meta.getFilialEmpresa() != null ? "/" + meta.getFilialEmpresa().getNome() : ""),
            meta.getUsuarioSolicitante().getNome(),
            meta.getUsuarioResponsavel().getNome(),
            FormatterUtil.formatDate(meta.getDataInicio()),
            FormatterUtil.formatDate(meta.getDataFim()),
            'A',
            new Button("E"),};

        view.getTargetTable().addItem(linha, meta);

        // se a meta possui tarefas, chama recursivamente
        for (Tarefa subTarefa : meta.getTarefas()) {
            addTaskInTargetTable(subTarefa);
        }

    }

    private void addTaskInTargetTable(Tarefa taskToInsert) {

        Object[] linha = new Object[]{
            buildButtonEditarTarefa(taskToInsert, taskToInsert.getGlobalID()),
            buildButtonEditarTarefa(taskToInsert, taskToInsert.getHierarquia().getCategoria()),
            buildButtonEditarTarefa(taskToInsert, taskToInsert.getNome()),
            taskToInsert.getEmpresa().getNome()
            + (taskToInsert.getFilialEmpresa() != null ? "/" + taskToInsert.getFilialEmpresa().getNome() : ""),
            taskToInsert.getUsuarioSolicitante().getNome(),
            taskToInsert.getUsuarioResponsavel().getNome(),
            FormatterUtil.formatDate(taskToInsert.getDataInicio()),
            FormatterUtil.formatDate(taskToInsert.getDataFim()),
            taskToInsert.getProjecao().toString().charAt(0),
            new Button("E"), // new Button("C")
        };

        view.getTargetTable().addItem(linha, taskToInsert);

        // se a meta possui tarefas, chama recursivamente
        for (Tarefa subTarefa : taskToInsert.getSubTarefas()) {
            addTaskInTargetTable(subTarefa);
        }

    }

    /**
     * Aplicar filtros de pesquisa selecionado
     *
     */
    @Override
    public void applyAutoFilter() {

        TipoPesquisa tipoPesquisa = (TipoPesquisa) view.getSwitchAndOrFilters().getValue();

        if (tipoPesquisa == null) {
            view.getSwitchAndOrFilters().setValue(TipoPesquisa.INCLUSIVA_OU);
        } else {
            aplicarFiltroPesquisa(tipoPesquisa);
        }
    }

    /**
     * Aplicar filtros de pesquisa selecionado
     *
     * @param tipoPesquisa
     */
    public void aplicarFiltroPesquisa(TipoPesquisa tipoPesquisa) {

        // Obtem os filtros selecionados pelo usuario
        // usuarios selecionados
        List<Usuario> usuariosResponsaveis = new ArrayList<>((Collection<Usuario>) view.getAssigneesFilterOptionGroup().getValue());

        List<Usuario> usuariosSolicitantes = new ArrayList<>((Collection<Usuario>) view.getRequestorsFilterOptionGroup().getValue());

        List<Usuario> usuariosParticipantes = new ArrayList<>((Collection<Usuario>) view.getFollowersFilterOptionGroup().getValue());

        // Empresas selecionadas
        List<Empresa> empresas = new ArrayList<>();
        List<FilialEmpresa> filiais = new ArrayList<>();

        for (Object empresaFilial : (Iterable<? extends Object>) view.getCompanyFilterOptionGroup().getValue()) {
            if (empresaFilial instanceof Empresa) {
                Empresa e = (Empresa) empresaFilial;
                empresas.add(e);

            } else if (empresaFilial instanceof FilialEmpresa) {
                FilialEmpresa f = (FilialEmpresa) empresaFilial;
                filiais.add(f);

            }
        }

        LocalDate dataFim = null;

        // Data Fim
        Date dataFimDate = view.getEndDateFilterDateField().getValue();
        if (dataFimDate != null) {
            Instant instant = Instant.ofEpochMilli(dataFimDate.getTime());
            dataFim = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
        }

        // Projecoes
        List<ProjecaoTarefa> projecoes = new ArrayList<>((Collection<ProjecaoTarefa>) view.getForecastFilterOptionGroup().getValue());

        // recarrega a visualizacao
        List<Tarefa> listaTarefas = model.filtrarTarefas(tipoPesquisa, usuariosResponsaveis,
                usuariosSolicitantes, usuariosParticipantes, empresas, filiais, dataFim, projecoes, loggedUser);

        exibirListaTarefas(listaTarefas);

        List<Meta> listaMetas = model.filtrarMetas(tipoPesquisa, usuariosResponsaveis,
                usuariosSolicitantes, usuariosParticipantes, empresas, filiais, dataFim, projecoes, loggedUser);

        exibirListaMetas(listaMetas);

        view.getCleanFiltersButton().setVisible(true);
        view.getSwitchAndOrFilters().setVisible(true);

    }

    /**
     * Remove todos os filtros de pesquisa e recarrega a visualização
     */
    @Override
    public void removerFiltrosPesquisa() {

        view.getFollowersFilterOptionGroup().setValue(null);
        view.getRequestorsFilterOptionGroup().setValue(null);
        view.getAssigneesFilterOptionGroup().setValue(null);
        view.getCompanyFilterOptionGroup().setValue(null);
        view.getEndDateFilterDateField().setValue(null);
        view.getForecastFilterOptionGroup().setValue(null);

        carregarListaTarefasUsuarioLogado();

        view.getCleanFiltersButton().setVisible(false);
        view.getSwitchAndOrFilters().setVisible(false);
        view.getSwitchAndOrFilters().setValue(null);

    }

    private Button buildButtonEditarMeta(Meta meta, String caption) {
        Button link = new Button(caption);
        link.setStyleName("quiet");
        CadastroMetaCallBackListener callback = this;
        link.addClickListener((Button.ClickEvent event) -> {
            view.getTargetTable().setValue(meta);
            MetaPresenter presenter = new MetaPresenter(new MetaModel(), new MetaView());
            presenter.setCallBackListener(callback);
            presenter.editarMeta(meta);
        });
        return link;
    }

    /**
     * Carrega a lista de metas sob responsabilidade do usuario logado
     */
    @Override
    public void carregarListaMetasUsuarioLogado() {

        // Usuario logado
        Usuario loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO.getAttributeName());

        List<Meta> listaMetas = model.listarMetas(loggedUser);

        exibirListaMetas(listaMetas);

    }

    /**
     * Carrega a lista de metas na tabela
     *
     * @param listaMetas
     */
    public void exibirListaMetas(List<Meta> listaMetas) {

        view.getTargetTable().removeAllItems();

        Object[] linha;
        listaMetas.stream().forEach((meta) -> {
            adicionarMetaTable(meta);
            organizeTree(view.getTargetTable(), meta, meta.getTarefas());
        });

    }

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

    public TarefaPresenter getTarefaPresenter() {
        return tarefaPresenter;
    }

}
