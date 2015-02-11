package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.CadastroMetaModel;
import com.saax.gestorweb.model.CadastroTarefaModel;
import com.saax.gestorweb.model.ChatSingletonModel;

import com.saax.gestorweb.model.DashboardModel;
import com.saax.gestorweb.model.EmpresaModel;
import com.saax.gestorweb.model.PopUpEvolucaoStatusModel;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.FilialEmpresa;
import com.saax.gestorweb.model.datamodel.HierarquiaProjeto;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.ProjecaoTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.FormatterUtil;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.view.TaskCreationCallBackListener;
import com.saax.gestorweb.view.CadastroTarefaView;
import com.saax.gestorweb.view.DashboardView;
import com.saax.gestorweb.view.DashboardViewListenter;
import com.vaadin.data.Item;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.view.CadastroMetaCallBackListener;
import com.saax.gestorweb.view.CadastroMetaView;
import com.saax.gestorweb.view.ChatView;
import com.saax.gestorweb.view.PopUpEvolucaoStatusView;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
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
public class DashboardPresenter implements DashboardViewListenter, TaskCreationCallBackListener, CadastroMetaCallBackListener, Serializable {

    // Todo presenter mantem acesso à view e ao model
    private final transient DashboardView view;
    private final transient DashboardModel model;

    // Referencia ao recurso das mensagens:
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();
    private Usuario loggedUser;

    /**
     * Inicializa o presenter
     */
    @Override
    public void init() {

        loggedUser = (Usuario) GestorSession.getAttribute("loggedUser");

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

    /**
     * Callback notificando que o cadastro de uma nova tarefa foi concluido
     *
     * @param tarefaCriada
     */
    @Override
    public void taskCreationDone(Tarefa tarefaCriada) {

        adicionarTarefaTable(tarefaCriada);
        organizeTree(view.getTaskTable(), tarefaCriada, tarefaCriada.getSubTarefas());
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
        link.setStyleName("link");
        TaskCreationCallBackListener callback = this;
        link.addClickListener((Button.ClickEvent event) -> {
            view.getTaskTable().setValue(tarefa);
            CadastroTarefaPresenter presenter = new CadastroTarefaPresenter(new CadastroTarefaModel(), new CadastroTarefaView());
            presenter.setCallBackListener(callback);
            presenter.editar(tarefa);
        });
        return link;
    }

    private void atualizarTarefaTable(Tarefa tarefa) {
        Item it = view.getTaskTable().getItem(tarefa);

        it.getItemProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaCod")).setValue(buildButtonEditarTarefa(tarefa, tarefa.getGlobalID()));
        it.getItemProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaTitulo")).setValue(buildButtonEditarTarefa(tarefa, tarefa.getHierarquia().getCategoria()));
        it.getItemProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaNome")).setValue(buildButtonEditarTarefa(tarefa, tarefa.getNome()));
        it.getItemProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaEmpresaFilial")).setValue(tarefa.getEmpresa().getNome()
                + (tarefa.getFilialEmpresa() != null ? "/" + tarefa.getFilialEmpresa().getNome() : ""));
        it.getItemProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaSolicitante")).setValue(tarefa.getUsuarioSolicitante().getNome());
        it.getItemProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaResponsavel")).setValue(tarefa.getUsuarioResponsavel().getNome());
        it.getItemProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaDataInicio")).setValue(FormatterUtil.formatDate(tarefa.getDataInicio()));
        it.getItemProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaDataFim")).setValue(FormatterUtil.formatDate(tarefa.getDataInicio()));
        it.getItemProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaStatus")).setValue(buildPopUpEvolucaoStatusEAndamento(tarefa));
        it.getItemProperty(mensagens.getString("CadastroTarefaView.subTarefasTable.colunaProjecao")).setValue(tarefa.getProjecao().toString().charAt(0));
        it.getItemProperty("Email").setValue(new Button("E"));
        it.getItemProperty("Chat").setValue(new Button("C"));

        // se a tarefa possui subs, chama recursivamente
        for (Tarefa subTarefa : tarefa.getSubTarefas()) {
            if (view.getTaskTable().getItemIds().contains(subTarefa)) {
                atualizarTarefaTable(subTarefa);
            } else {
                adicionarTarefaTable(subTarefa);
            }
        }

    }

    @Override
    public void taskUpdateDone(Tarefa tarefa) {
        atualizarTarefaTable(tarefa);
        organizeTree(view.getTaskTable(), tarefa, tarefa.getSubTarefas());
    }

    public Layout buildChooseTemplatePopUp(Window window) {

        VerticalLayout content = new VerticalLayout();
        content.setMargin(true);
        content.setSpacing(true);
        content.setSizeUndefined();

        ListSelect listaTemplates = new ListSelect();
        listaTemplates.setMultiSelect(false);
        listaTemplates.setWidth("300px");

        List<Tarefa> templates = model.getTarefasTemplate();

        for (Tarefa template : templates) {
            listaTemplates.addItem(template);
            listaTemplates.setItemCaption(template, template.getNome());
        }

        content.addComponent(listaTemplates);

        Button cancelar = new Button("Cancelar", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                window.close();
            }
        });
        DashboardPresenter callback = this;
        Button criar = new Button("Criar Tarefa", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                Tarefa template = (Tarefa) listaTemplates.getValue();
                if (template != null) {
                    CadastroTarefaPresenter presenter = new CadastroTarefaPresenter(new CadastroTarefaModel(), new CadastroTarefaView());
                    presenter.setCallBackListener(callback);

                    Tarefa novaTarefa = template.clone();
                    novaTarefa.setTarefaPai(null);
                    novaTarefa.setTemplate(false);
                    presenter.editar(novaTarefa);

                }
            }
        }
        );
        content.addComponent(new HorizontalLayout(cancelar, criar));

        return content;
    }

    @Override
    public void createsNewTaskByTemplate() {

        Window templatesWindow = new Window("Escolha a tarefa modelo");
        templatesWindow.setModal(true);

        templatesWindow.setContent(buildChooseTemplatePopUp(templatesWindow));

        templatesWindow.center();

        UI.getCurrent().addWindow(templatesWindow);

    }

    private void criarNova(HierarquiaProjetoDetalhe categoria) {

        if (categoria.getNivel() == 1) {
            CadastroMetaPresenter presenter = new CadastroMetaPresenter(new CadastroMetaModel(), new CadastroMetaView());
            presenter.setCallBackListener(this);
            presenter.criarNovaMeta(categoria);
        } else if (categoria.getNivel() == 2) {
            CadastroTarefaPresenter presenter = new CadastroTarefaPresenter(new CadastroTarefaModel(), new CadastroTarefaView());
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Opção disponível apenas para testes, onde é possível alterar o usuário
     * logado
     */
    @Override
    public void usuarioLogadoAlteradoAPENASTESTE() {
        this.loggedUser = (Usuario) GestorSession.getAttribute("loggedUser");
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
            
            Button tarefaButton = buildButtonEditarTarefa(tarefa, tarefa.getNome().length()>25 ? tarefa.getNome().substring(0, 25) : tarefa.getNome());
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
        PopUpEvolucaoStatusView viewPopUP = new PopUpEvolucaoStatusView();
        PopUpEvolucaoStatusModel modelPopUP = new PopUpEvolucaoStatusModel();

        PopUpEvolucaoStatusPresenter presenter = new PopUpEvolucaoStatusPresenter(viewPopUP, modelPopUP);

        presenter.load(tarefa);

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
            new Button("E"), 
        };

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
            //buildPopUpEvolucaoStatusEAndamento(meta),
            // meta.getProjecao().toString().charAt(0),
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

        for (Object empresaFilial : (Collection<Object>) view.getCompanyFilterOptionGroup().getValue()) {
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
        link.setStyleName("link");
        CadastroMetaCallBackListener callback = this;
        link.addClickListener((Button.ClickEvent event) -> {
            view.getTargetTable().setValue(meta);
            CadastroMetaPresenter presenter = new CadastroMetaPresenter(new CadastroMetaModel(), new CadastroMetaView());
            presenter.setCallBackListener(callback);
            presenter.edit(meta);
        });
        return link;
    }

    /**
     * Carrega a lista de metas sob responsabilidade do usuario logado
     */
    @Override
    public void carregarListaMetasUsuarioLogado() {

        // Usuario logado
        Usuario loggedUser = (Usuario) GestorSession.getAttribute("loggedUser");

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

}
