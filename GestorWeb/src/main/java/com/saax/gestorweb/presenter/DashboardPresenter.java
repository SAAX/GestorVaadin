package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.ChatSingletonModel;
import com.saax.gestorweb.model.DashboardModel;
import com.saax.gestorweb.model.EmpresaModel;
import com.saax.gestorweb.model.LixeiraModel;
import com.saax.gestorweb.model.SignupModel;
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
import com.saax.gestorweb.view.TarefaView;
import com.saax.gestorweb.view.DashboardView;
import com.saax.gestorweb.view.DashboardViewListenter;
import com.vaadin.data.Item;
import com.saax.gestorweb.model.datamodel.RecurrencySet;
import com.saax.gestorweb.view.MetaView;
import com.saax.gestorweb.view.ChatView;
import com.saax.gestorweb.view.LixeiraView;
import com.saax.gestorweb.view.PopUpStatusListener;
import com.saax.gestorweb.view.PopUpStatusView;
import com.saax.gestorweb.view.SignupView;
import com.vaadin.ui.Button;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.UI;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.vaadin.hene.popupbutton.PopupButton;

/**
 * Presenter do Dashboard <br>
 * Esta classe é responsável captar todos os eventos que ocorrem na View e dar o
 * devido tratamento, utilizando para isto o modelo.
 *
 *
 * @author Rodrigo
 */
public class DashboardPresenter implements DashboardViewListenter, CallBackListener, PopUpStatusListener, Serializable {

    // Todo presenter mantem acesso à view e ao model
    private final transient DashboardView view;
    private final List<CallBackListener> callbackListeneres;

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
        callbackListeneres.add(callBackListener);
    }

    @Override
    public void atualizarApresentacaoMeta(Meta meta) {
        if (view.getTargetTable().getItemIds().contains(meta)) {
            updateTargetTable(meta);

        } else {
            adicionarMetaTable(meta);

        }
        organizeTree(view.getTargetTable(), meta, meta.getTarefas());
    }

    @Override
    public void tarefaRestaurada(Tarefa tarefa) {
        reload();
    }

    @Override
    public void metaRestaurada(Meta meta) {
        reload();
    }

    @Override
    public void configContaClicked(Empresa empresa) {

        //Cria o pop up para registrar a conta (model e viw)
        SignupModel signupModel = new SignupModel();
        SignupView signupView = new SignupView();

        //o presenter liga model e view
        SignupPresenter signupPresenter;
        signupPresenter = new SignupPresenter(signupModel, signupView, false);
        //adiciona a visualização à UI
        UI.getCurrent().addWindow(signupView);
        signupPresenter.open(empresa);
    }

    // enumeracao do tipo de pesquisa
    public enum TipoPesquisa {

        INCLUSIVA_OU, EXCLUSIVA_E
    };

    @Override
    public List<CallBackListener> getCallbackListeneres() {
        return callbackListeneres;
    }

    /**
     * Cria o presenter ligando o Model ao View
     *
     * @param view
     */
    public DashboardPresenter(DashboardView view) {

        this.view = view;
        this.callbackListeneres = new ArrayList<>();

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
     * Inicializa o presenter
     */
    @Override
    public void init() {

        adicionarHierarquiasProjeto();
        carregaVisualizacaoInicial();
    }

    /**
     * Obtem as hierarquias de projeto customizadas e adiciona no menu "Criar"
     */
    private void adicionarHierarquiasProjeto() {

        // obtem as hierarquias customizadas
        List<HierarquiaProjeto> hierarquias = DashboardModel.getHierarquiasProjeto();
        List<Empresa> listarEmpresasAtivasUsuarioLogado = EmpresaModel.listarEmpresasAtivasUsuarioLogado(GestorPresenter.getUsuarioLogado(), false);

        // menu "Criar"
        Map<Empresa, MenuBar.MenuItem> mapEmpresasMenuCriar = view.getMapEmpresasMenuItemCriar();

        for (Empresa empresa : listarEmpresasAtivasUsuarioLogado) {

            // adiciona cada hierarquia customizada
            for (HierarquiaProjeto hierarquia : hierarquias) {

                MenuBar.MenuItem menuProjeto = mapEmpresasMenuCriar.get(empresa).
                        addItemBefore(hierarquia.getNome(), null, null, view.getCreateNewByTemplate());

                Collections.sort(hierarquia.getCategorias());

                for (HierarquiaProjetoDetalhe categoria : hierarquia.getCategorias()) {
                    menuProjeto.addItem(categoria.getCategoria(), (MenuBar.MenuItem selectedItem) -> {
                        criarNova(categoria, empresa);
                    });
                }
            }

        }

        //Pra nenhuma empresa, identifiquei o menu com a key null no map
        for (HierarquiaProjeto hierarquia : hierarquias) {
            MenuBar.MenuItem menuProjeto = mapEmpresasMenuCriar.get(null).
                    addItemBefore(hierarquia.getNome(), null, null, view.getCreateNewByTemplate());

            Collections.sort(hierarquia.getCategorias());

            for (HierarquiaProjetoDetalhe categoria : hierarquia.getCategorias()) {
                menuProjeto.addItem(categoria.getCategoria(), (MenuBar.MenuItem selectedItem) -> {
                    criarNova(categoria);
                });
            }
        }
    }

    /**
     * Trata o evento ao ser pressionado o botão da lixeira <br>
     * Apresenta a visualização da lixeira <br>
     * Nesta visualização são listadas as tarefas removidas pelo usuário e elas
     * podem ser restauradas
     */
    @Override
    public void trashButtonPressed() {

        LixeiraView lixeiraView = new LixeiraView();
        LixeiraPresenter lixeiraPresenter = new LixeiraPresenter(lixeiraView);
        lixeiraPresenter.addTarefaCallBackListener(this);
        lixeiraPresenter.aprentarLixeira();

    }

    private void organizeTree(TreeTable table, Object parentTaskOrTarget, List<Tarefa> subTasks) {

        for (Tarefa subTask : subTasks) {
            table.setParent(subTask, parentTaskOrTarget);
            if (subTask.getSubTarefas() != null && !subTask.getSubTarefas().isEmpty()) {
                organizeTree(table, subTask, subTask.getSubTarefas());
            }
        }
    }

    private void atualizarTarefaTable(Tarefa tarefa) {

        if (tarefa == null || tarefa.getGlobalID() == null) {
            throw new IllegalArgumentException("Tarefa nula");
        }

        Item it = view.getTarefaTable().getItem(tarefa);

        if (it != null) {

            it.getItemProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.taskTable.cod")).setValue(GestorPresenter.buildButtonEditarTarefa(view.getTarefaTable(), this, tarefa, tarefa.getGlobalID()));
            it.getItemProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.taskTable.title")).setValue(GestorPresenter.buildButtonEditarTarefa(view.getTarefaTable(), this, tarefa, tarefa.getHierarquia().getCategoria()));
            it.getItemProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.taskTable.name")).setValue(GestorPresenter.buildButtonEditarTarefa(view.getTarefaTable(), this, tarefa, tarefa.getNome()));
            it.getItemProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.taskTable.company")).setValue(tarefa.getEmpresa().getNome()
                    + (tarefa.getFilialEmpresa() != null ? "/" + tarefa.getFilialEmpresa().getNome() : ""));
            it.getItemProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.taskTable.requestor")).setValue(tarefa.getUsuarioSolicitante().getNome());
            it.getItemProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.taskTable.assingee")).setValue(tarefa.getUsuarioResponsavel().getNome());
            it.getItemProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.taskTable.startDate")).setValue(FormatterUtil.formatDate(tarefa.getDataInicio()));
            it.getItemProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.taskTable.endDate")).setValue(FormatterUtil.formatDate(tarefa.getDataFim()));
            it.getItemProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.taskTable.state")).setValue(buildPopUpEvolucaoStatusEAndamento(tarefa));
            /**
             * COMENTADO: Projeção postergada para v2
             * it.getItemProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.taskTable.forecast")).setValue(tarefa.getProjecao().toString().charAt(0));
             */

//            it.getItemProperty(GestorPresenter.getMENSAGENS().
//                    getString("DashboardView.taskTable.email")).setValue(new Button("E"));
            it.getItemProperty(GestorPresenter.getMENSAGENS().
                    getString("DashboardView.taskTable.chat")).
                    setValue(new Button("Chat", (Button.ClickEvent event) -> {
                        chatButtonClicked(tarefa);
                    }));

            // se a tarefa possui subs, chama recursivamente
            for (Tarefa subTarefa : tarefa.getSubTarefas()) {
                if (view.getTarefaTable().getItemIds().contains(subTarefa)) {
                    atualizarTarefaTable(subTarefa);
                } else {
                    adicionarTarefaTable(subTarefa);
                }
            }
        }

    }

    private void updateTargetTable(Meta target) {
        Item it = view.getTargetTable().getItem(target);

        it.getItemProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.cod")).setValue(buildButtonEditarMeta(target, target.getGlobalID()));
        it.getItemProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.title")).setValue(buildButtonEditarMeta(target, target.getCategoria().getCategoria()));
        it.getItemProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.name")).setValue(buildButtonEditarMeta(target, target.getNome()));
        it.getItemProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.company")).setValue(target.getEmpresa().getNome()
                + (target.getFilialEmpresa() != null ? "/" + target.getFilialEmpresa().getNome() : ""));
        it.getItemProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.requestor")).setValue(target.getUsuarioSolicitante().getNome());
        it.getItemProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.assingee")).setValue(target.getUsuarioResponsavel().getNome());
        it.getItemProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.startDate")).setValue(FormatterUtil.formatDate(target.getDataInicio()));
        it.getItemProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.endDate")).setValue(FormatterUtil.formatDate(target.getDataFim()));
//        it.getItemProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.state")).setValue(target.getStatus());
//        it.getItemProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.email")).setValue(new Button("E"));

        // se a meta possui tarefas, chama recursivamente
        for (Tarefa subTarefa : target.getTarefas()) {
            addTaskInTargetTable(subTarefa);
        }

    }

    private void reload() {
        // se a view estiver com um filtro de pequisa customizado
        if (view.getSwitchAndOrFilters().isVisible()) {
            // reexecuta a pesquina nos mesmos filtros
            TipoPesquisa tipoPesquisa = (TipoPesquisa) view.getSwitchAndOrFilters().getValue();
            aplicarFiltroPesquisa(tipoPesquisa);

        } else {
            // reexecuta a pesquina padrão
            carregarListaTarefasUsuarioLogado();
            carregarListaMetasUsuarioLogado();
        }

    }

    @Override
    public void metaRemovida(Meta meta) {
        reload();
    }

    /**
     * Trata o evento disparado via callback quando uma tarefa é removida <br>
     * recarrega a lista de tarefas em exibição <br>
     * PS: não é possível remover itens de uma treetable A propria busca vai
     * filtrar a removida
     *
     * @param tarefaRemovida
     */
    @Override
    public void tarefaRemovida(Tarefa tarefaRemovida) {
        reload();
    }

    /**
     * Callback notificando que o cadastro de uma nova tarefa foi concluido
     *
     * @param tarefa
     */
    @Override
    public void atualizarApresentacaoTarefa(Tarefa tarefa) {

        if (view.getTarefaTable().getItemIds().contains(tarefa)) {
            atualizarTarefaTable(tarefa);

        } else {
            adicionarTarefaTable(tarefa);

        }

        organizeTree(view.getTarefaTable(), tarefa, tarefa.getSubTarefas());

        if (tarefa.getTipoRecorrencia() == TipoTarefa.RECORRENTE) {

            Tarefa next = tarefa.getProximaTarefa();

            if (next != null) {
                atualizarApresentacaoTarefa(next);
            }
        }

        for (Tarefa sub : tarefa.getSubTarefas()) {
            atualizarApresentacaoTarefa(sub);
        }
    }

    @Override
    public void createsNewTaskByTemplate() {
        List<Tarefa> templates = DashboardModel.getTarefasTemplate();
        view.abrirPopUpSelecaoTemplates(templates);

    }

    private void criarNova(HierarquiaProjetoDetalhe categoria) {

        if (categoria.getNivel() == 1) {
            MetaPresenter presenter = new MetaPresenter(new MetaView());
            presenter.addCallbackListener(this);
            presenter.criarNovaMeta(categoria, null);
        } else if (categoria.getNivel() == 2) {
            TarefaPresenter presenter = new TarefaPresenter(new TarefaView());
            presenter.addCallBackListener(this);
            presenter.createTask(categoria, null);
        }

    }

    private void criarNova(HierarquiaProjetoDetalhe categoria, Empresa empresa) {

        if (categoria.getNivel() == 1) {
            MetaPresenter presenter = new MetaPresenter(new MetaView());
            presenter.addCallbackListener(this);
            presenter.criarNovaMeta(categoria, empresa);

        } else if (categoria.getNivel() == 2) {
            TarefaPresenter presenter = new TarefaPresenter(new TarefaView());
            presenter.addCallBackListener(this);
            presenter.createTask(categoria, empresa);
        }

    }

    /**
     * Opção disponível apenas para testes, onde é possível alterar o usuário
     * logado
     */
    @Override
    public void usuarioLogadoAlteradoAPENASTESTE() {
        carregaVisualizacaoInicial();
    }

    @Override
    public void taskStatusChanged(Tarefa task) {
        atualizarTarefaTable(task);
    }

    public void openTask(Tarefa taskToOpen) {
        TarefaPresenter tarefaPresenter = new TarefaPresenter(new TarefaView());
        tarefaPresenter.addCallBackListener(this);
        tarefaPresenter.editar(taskToOpen);

    }

    @Override
    public Tarefa criarTarefaPorTemplate(Tarefa template) {

        if (template == null) {
            throw new InvalidParameterException("Template Invalido.");
        }
        TarefaPresenter tarefaPresenter = new TarefaPresenter(new TarefaView());
        tarefaPresenter.addCallBackListener(this);

        Tarefa novaTarefa;
        novaTarefa = DashboardModel.criarNovaTarefaPeloTemplate(template);
        tarefaPresenter.editar(novaTarefa);

        return novaTarefa;

    }

    /**
     * Trata o evento disparado ao ser acionado o comando para remover uma
     * tarefa pelo botão de remoção em cada tarefa da tabela
     *
     * @param tarefa
     */
    @Override
    public void removerTarefaButtonClicked(Tarefa tarefa) {

        LixeiraPresenter popUpRemocaoTarefaPresenter = new LixeiraPresenter(new LixeiraView());
        popUpRemocaoTarefaPresenter.addTarefaCallBackListener(this);
        popUpRemocaoTarefaPresenter.apresentaConfirmacaoRemocaoTarefa(tarefa);

    }

    @Override
    public boolean verificaPermissaoAcessoRemocaoTarefa(Tarefa tarefa) {

        return LixeiraModel.verificaPermissaoAcessoRemocaoTarefa(tarefa, GestorPresenter.getUsuarioLogado());
    }

    @Override
    public void removerMetaButtonClicked(Meta meta) {
        LixeiraPresenter popUpRemocaoTarefaPresenter = new LixeiraPresenter(new LixeiraView());
        popUpRemocaoTarefaPresenter.addTarefaCallBackListener(this);
        popUpRemocaoTarefaPresenter.apresentaConfirmacaoRemocaoMeta(meta);
    }

    @Override
    public boolean verificaPermissaoAcessoRemocaoMeta(Meta meta) {
        return LixeiraModel.verificaPermissaoAcessoRemocaoMeta(meta, GestorPresenter.getUsuarioLogado());
    }

    /**
     * Carrega os campos necessarios ao abrir a visualização
     */
    private void carregaVisualizacaoInicial() {

        carregarListaTarefasUsuarioLogado();
        carregarListaMetasUsuarioLogado();
        carregarFiltrosPesquisa();
        carregarListaTarefasPrincipais();
        carregarConvitesPrincipais();
    }

    /**
     * Carregar os filtros de pesquisa
     */
    public void carregarFiltrosPesquisa() {

        List<Usuario> usuarios = new ArrayList<>();

        for (Empresa empresa : EmpresaModel.listarEmpresasAtivasUsuarioLogado(GestorPresenter.getUsuarioLogado())) {
            usuarios.addAll(EmpresaModel.listarUsariosAtivos(empresa));

        }

        for (Usuario usuario : usuarios) {
            view.getAssigneesFilterOptionGroup().addItem(usuario);
            view.getAssigneesFilterOptionGroup().setItemCaption(usuario, usuario.getNome());

            view.getRequestorsFilterOptionGroup().addItem(usuario);
            view.getRequestorsFilterOptionGroup().setItemCaption(usuario, usuario.getNome());

            view.getFollowersFilterOptionGroup().addItem(usuario);
            view.getFollowersFilterOptionGroup().setItemCaption(usuario, usuario.getNome());

        }

        EmpresaModel empresaModel = new EmpresaModel();
        List<Empresa> empresas = empresaModel.listarEmpresasAtivasUsuarioLogado(GestorPresenter.getUsuarioLogado());
        for (Empresa empresa : empresas) {

            view.getCompanyFilterOptionGroup().addItem(empresa);
            view.getCompanyFilterOptionGroup().setItemCaption(empresa, empresa.getNome());

            for (FilialEmpresa filial : empresa.getFiliais()) {
                view.getCompanyFilterOptionGroup().addItem(filial);
                view.getCompanyFilterOptionGroup().setItemCaption(filial, "Filial: " + filial.getNome());

            }
        }
        /**
         * COMENTADO: Projeção postergada para v2 Button projecaoButton; for
         * (ProjecaoTarefa projecao : ProjecaoTarefa.values()) {
         * view.getForecastFilterOptionGroup().addItem(projecao);
         * view.getForecastFilterOptionGroup().setItemCaption(projecao,
         * projecao.toString()); }
         */

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

        List<Tarefa> listaTarefas = DashboardModel.listarTarefas(GestorPresenter.getUsuarioLogado());

        exibirListaTarefas(listaTarefas);

    }

    /**
     * Carrega o box inferior esquerdo com as tarefas principais
     */
    private void carregarListaTarefasPrincipais() {

        List<Tarefa> tarefasPrincipais = DashboardModel.listarTarefasPrincipais(GestorPresenter.getUsuarioLogado());

        
        
        view.removeComponentsTasksBottomContainer();
        for (int i = 0; i < tarefasPrincipais.size(); i++) {
            Tarefa tarefa = tarefasPrincipais.get(i);

            Button tarefaButton = GestorPresenter.buildButtonEditarTarefa(view.getTarefaTable(), this, tarefa, tarefa.getNome().length() > 25 ? tarefa.getNome().substring(0, 25) : tarefa.getNome());
            tarefaButton.setStyleName("v-button-link");
            view.getBottomTasksContainer().addComponent(tarefaButton);
        }

    }

    /**
     * Carrega o box inferior direito com as principais tarefas a serem aceitas
     */
    private void carregarConvitesPrincipais() {

        List<Tarefa> tarefasAguardandoAceite = DashboardModel.listarTarefasAguardandoAceite(GestorPresenter.getUsuarioLogado());

        view.removeComponentsInvitesBottomContainer();
        for (int i = 0; i < tarefasAguardandoAceite.size(); i++) {
            Tarefa tarefa = tarefasAguardandoAceite.get(i);

            Button tarefaButton = GestorPresenter.buildButtonEditarTarefa(view.getTarefaTable(), this, tarefa, tarefa.getNome().length() > 25 ? tarefa.getNome().substring(0, 25) : tarefa.getNome());
            tarefaButton.setStyleName("v-button-link");
            view.getBottomInvitesContainer().addComponent(tarefaButton);
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

        PopUpStatusPresenter presenter = new PopUpStatusPresenter(viewPopUP);

        presenter.load(tarefa, null, this);

        // evento disparado quando o pop-up se torna visivel:
        // seleciona a linha correta na tabela
        presenter.getStatusButton().addPopupVisibilityListener((PopupButton.PopupVisibilityEvent event) -> {
            if (event.isPopupVisible()) {
                // selecionar a linha clicada:
                Tarefa tarefaEditada = (Tarefa) event.getPopupButton().getData();
                this.view.getTarefaTable().setValue(tarefa);
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

        view.getTarefaTable().removeAllItems();

        listaTarefas.stream().forEach((tarefa) -> {
            adicionarTarefaTable(tarefa);
            organizeTree(view.getTarefaTable(), tarefa, tarefa.getSubTarefas());
        });

    }

    /**
     * Adiciona a tarefa na tree table
     *
     * @param tarefa
     */
    private void adicionarTarefaTable(Tarefa tarefa) {

        Object[] linha = new Object[]{
            GestorPresenter.buildButtonEditarTarefa(view.getTarefaTable(), this, tarefa, tarefa.getGlobalID()),
            GestorPresenter.buildButtonEditarTarefa(view.getTarefaTable(), this, tarefa, tarefa.getHierarquia().getCategoria()),
            GestorPresenter.buildButtonEditarTarefa(view.getTarefaTable(), this, tarefa, tarefa.getNome()),
            tarefa.getEmpresa().getNome()
            + (tarefa.getFilialEmpresa() != null ? "/" + tarefa.getFilialEmpresa().getNome() : ""),
            tarefa.getUsuarioSolicitante().getNome(),
            tarefa.getUsuarioResponsavel().getNome(),
            FormatterUtil.formatDate(tarefa.getDataInicio()),
            FormatterUtil.formatDate(tarefa.getDataFim()),
            buildPopUpEvolucaoStatusEAndamento(tarefa),
            /**
             * Projecao: Contingenciado para V2
             * tarefa.getProjecao().toString().charAt(0),
             */
            //            new Button("E"),
            new Button("Chat", (Button.ClickEvent event) -> {
                chatButtonClicked(tarefa);
            })
        };

        view.getTarefaTable().addItem(linha, tarefa);

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
            FormatterUtil.formatDate(meta.getDataFim()), //            new Button("E"),
        };

        view.getTargetTable().addItem(linha, meta);

        // se a meta possui tarefas, chama recursivamente
        for (Tarefa subTarefa : meta.getTarefas()) {
            addTaskInTargetTable(subTarefa);
        }

    }

    private void addTaskInTargetTable(Tarefa taskToInsert) {

        Object[] linha = new Object[]{
            GestorPresenter.buildButtonEditarTarefa(view.getTarefaTable(), this, taskToInsert, taskToInsert.getGlobalID()),
            GestorPresenter.buildButtonEditarTarefa(view.getTarefaTable(), this, taskToInsert, taskToInsert.getHierarquia().getCategoria()),
            GestorPresenter.buildButtonEditarTarefa(view.getTarefaTable(), this, taskToInsert, taskToInsert.getNome()),
            taskToInsert.getEmpresa().getNome()
            + (taskToInsert.getFilialEmpresa() != null ? "/" + taskToInsert.getFilialEmpresa().getNome() : ""),
            taskToInsert.getUsuarioSolicitante().getNome(),
            taskToInsert.getUsuarioResponsavel().getNome(),
            FormatterUtil.formatDate(taskToInsert.getDataInicio()),
            FormatterUtil.formatDate(taskToInsert.getDataFim()), //            new Button("E"), // new Button("C")
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
    public void aplicarFiltroPesquisa() {

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
        /**
         * Postergado para V2
         *
         * List<ProjecaoTarefa> projecoes = new
         * ArrayList<>((Collection<ProjecaoTarefa>)
         * view.getForecastFilterOptionGroup().getValue());
         */
        // recarrega a visualizacao
        List<ProjecaoTarefa> projecoes = new ArrayList<>();
        List<Tarefa> listaTarefas = DashboardModel.filtrarTarefas(tipoPesquisa, usuariosResponsaveis,
                usuariosSolicitantes, usuariosParticipantes, empresas, filiais, dataFim, projecoes, GestorPresenter.getUsuarioLogado());

        exibirListaTarefas(listaTarefas);

        List<Meta> listaMetas = DashboardModel.filtrarMetas(tipoPesquisa, usuariosResponsaveis,
                usuariosSolicitantes, usuariosParticipantes, empresas, filiais, dataFim, projecoes, GestorPresenter.getUsuarioLogado());

        exibirListaMetas(listaMetas);

        view.getCleanFiltersButton().setVisible(true);
        view.getCleanFiltersButton().setEnabled(true);
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
        /**
         * Postergado para V2
         *
         * view.getForecastFilterOptionGroup().setValue(null);
         */

        carregarListaTarefasUsuarioLogado();

        view.getCleanFiltersButton().setVisible(false);
        view.getSwitchAndOrFilters().setVisible(false);
        view.getSwitchAndOrFilters().setValue(null);

    }

    private Button buildButtonEditarMeta(Meta meta, String caption) {
        Button link = new Button(caption);
        link.setStyleName("quiet");
        CallBackListener callback = this;
        link.addClickListener((Button.ClickEvent event) -> {
            view.getTargetTable().setValue(meta);
            MetaPresenter presenter = new MetaPresenter(new MetaView());
            presenter.addCallbackListener(callback);
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
        List<Meta> listaMetas = DashboardModel.listarMetas(GestorPresenter.getUsuarioLogado());

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
