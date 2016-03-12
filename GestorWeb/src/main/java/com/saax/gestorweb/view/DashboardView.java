package com.saax.gestorweb.view;

import com.saax.gestorweb.model.EmpresaModel;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.model.datamodel.UsuarioEmpresa;
import com.saax.gestorweb.presenter.GestorPresenter;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.SessionAttributesEnum;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.vaadin.hene.popupbutton.PopupButton;

/**
 * MVP View tier of the Dashboard. Static struture with vaadin visual components
 *
 * @author Rodrigo
 */
public class DashboardView extends VerticalLayout {

    // listener interface (presenter in MVP)
    private DashboardViewListenter listener;
    private ListSelect listaTemplates;

    public void setListener(DashboardViewListenter listener) {
        this.listener = listener;
    }

    // --------------------------------------------------------------------------------------------------
    // Menu bar components
    // --------------------------------------------------------------------------------------------------
    // component to store the menu bar
    private HorizontalLayout topContainer;
    // the menubar
    private MenuBar topMenu;
    // the menu item "create new <category>...".
    // it's is full filled in the presenter with all proper categories
    private Map<Empresa, MenuBar.MenuItem> mapEmpresasMenuItemCriar;
    // the menu item "create new by template".
    // open a pop-up to choose the template
    private MenuBar.MenuItem createNewByTemplate;

    // ---------------------------------------------------------------------------------------------------
    // Auto filter components
    // ---------------------------------------------------------------------------------------------------
    // main container of the auto filter
    private HorizontalLayout autoFiltersContainer;
    // left container
    private HorizontalLayout autoFilterLeftContainer;
    // left container components:
    private PopupButton userFilterPopupButton;
    private OptionGroup assigneeFilterOptionGroup;
    private OptionGroup requestorsFilterOptionGroup;
    private OptionGroup followersFilterOptionGroup;
    private PopupButton companyFilterPopupButton;
    private OptionGroup companyFilterOptionGroup;
    private PopupButton endDateFilterPopupButton;
    private InlineDateField endDateFilterDateField;
    /**
     * COMENTADO: Projeção postergada para v2 private PopupButton
     * forecastFilterButton; private OptionGroup forecastFilterOptionGroup;
     */
    private Button cleanFiltersButton;
    private OptionGroup switchAndOrFilters;
    // right container
    private VerticalLayout autoFilterSearchContainer;
    private Button trashButton;
    private TextField quickSeachTextField;
    private Button advancedSearchButton;

    // top label with the local date
    private HorizontalLayout currentDateContainer;
    private Label currentDateLabel;

    // ---------------------------------------------------------------------------------------------------
    // Tabseet with all data tables
    // ---------------------------------------------------------------------------------------------------
    private HorizontalLayout tabSheetContainer;
    private TabSheet tabSheet;
    private TreeTable tarefaTable;
    private TreeTable targetTable;

    // ---------------------------------------------------------------------------------------------------
    // bottom container
    // ---------------------------------------------------------------------------------------------------
    private HorizontalLayout bottomContainer;
    private VerticalLayout bottomTasksContainer;
    private VerticalLayout bottomForecastsContainer;
    private VerticalLayout bottomInvitesContainer;

    /**
     * Builds the view with all visual components
     */
    public DashboardView() {

        // initial parameters
        setMargin(true);
        setSpacing(true);
        setWidth("100%");
        setHeight(null);

        // adds each components (other containers):
        addComponent(buildSwitchUserCombo()); // in test only
        addComponent(buildTopContainer());
        addComponent(buildAutoFiltersContainer());
        addComponent(buildCurrentDateContainer());
        addComponent(buildTabSheetContainer());
        addComponent(buildBottomContainer());

    }

    /**
     * Builds a container with a special combo to change the logged user
     * Obviously its is only for test purpose
     *
     * @return the switchUserContainer
     */
    private Component buildSwitchUserCombo() {

        VerticalLayout switchUserContainer = new VerticalLayout();

        switchUserContainer.addComponent(new Label("ATENÇÃO ESTA OPÇÃO É APENAS PARA TESTE:"));

        ComboBox switchUserCombo = new ComboBox("Altere usuário logado:");

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        List<Usuario> userList = em.createNamedQuery("Usuario.findAll").getResultList();

        for (Usuario user : userList) {
            switchUserCombo.addItem(user);
            switchUserCombo.setItemCaption(user, user.getNome());

        }

        switchUserCombo.addValueChangeListener((Property.ValueChangeEvent event) -> {
            Usuario loggedUser = (Usuario) switchUserCombo.getValue();
            GestorSession.setAttribute(SessionAttributesEnum.USUARIO_LOGADO, loggedUser);
            listener.usuarioLogadoAlteradoAPENASTESTE();
            listener.removerFiltrosPesquisa();
        });
        switchUserContainer.addComponent(switchUserCombo);
        return switchUserContainer;

    }

    /**
     * Build the topContainer with the inner menu bar
     *
     * @return the topContainer
     */
    private Component buildTopContainer() {

        topContainer = new HorizontalLayout();
        topContainer.setSpacing(true);
        topContainer.setWidth("100%");
        topContainer.setHeight("50px");

        topMenu = new MenuBar();
        topMenu.setHeight("100%");
        topMenu.setHtmlContentAllowed(true);

        mapEmpresasMenuItemCriar = new HashMap<>();

        //Adiciono o menu sem empresas como null
        MenuBar.MenuItem empresaDefaultMenuItem = topMenu.addItem(GestorPresenter.getMENSAGENS().
                getString("DashboardView.createNewByCategoryMenuItem"), null, null);
        mapEmpresasMenuItemCriar.put(null, empresaDefaultMenuItem);

        //Adiciono "by template" pra empresa default
        createNewByTemplate = mapEmpresasMenuItemCriar.get(null).addItem(
                GestorPresenter.getMENSAGENS().getString("DashboardView.createNewByTemplate"),
                (MenuBar.MenuItem selectedItem) -> {
                    listener.createsNewTaskByTemplate();
                });
        List<Empresa> empresasAtivasUsuarioLogado = EmpresaModel.
                listarEmpresasAtivasUsuarioLogado(GestorPresenter.getUsuarioLogado(), false);

        for (Empresa empresa : empresasAtivasUsuarioLogado) {

            //Adiciono pras outras empresas que o usuário está ativo
            MenuBar.MenuItem empresaMenuItem = topMenu.
                    addItem(empresa.getNome(), null, null);
            mapEmpresasMenuItemCriar.put(empresa, empresaMenuItem);

            createNewByTemplate = mapEmpresasMenuItemCriar.get(empresa).addItem(
                    GestorPresenter.getMENSAGENS().getString(
                            "DashboardView.createNewByTemplate"), (MenuBar.MenuItem selectedItem) -> {
                        listener.createsNewTaskByTemplate();
                    });
        }

        /**
         * Postergado para V2
         *
         * MenuBar.MenuItem publicationsMenuItem = topMenu.addItem("<h3>" +
         * GestorPresenter.getMENSAGENS().getString("DashboardView.publicationsMenuItem")
         * + "</h3>", null, null); MenuBar.MenuItem reportsMenuItem =
         * topMenu.addItem("<h3>" +
         * GestorPresenter.getMENSAGENS().getString("DashboardView.reportsMenuItem")
         * + "</h3>", null, null);
         */
        MenuBar.MenuItem config = topMenu.addItem("<h3>Config</h3>", null, null);

        // para cada empresa onde o usuário é adm, cria um menu item para
        // configurar a conta
        for (UsuarioEmpresa usuarioEmpresa : GestorPresenter.getUsuarioLogado().getEmpresas()) {
            if (usuarioEmpresa.getAtivo() && usuarioEmpresa.getAdministrador()) {
                config.addItem("Configurar Conta: " + usuarioEmpresa.getEmpresa().getNome(), (MenuBar.MenuItem selectedItem) -> {
                    listener.configContaClicked(usuarioEmpresa.getEmpresa());
                });
            }
        }
        config.addItem("Config 3", null, null);

        topMenu.addItem("<h3>" + GestorPresenter.getMENSAGENS().getString("DashboardView.logoutMenuItem") + "</h3>", null, (MenuBar.MenuItem selectedItem) -> {
            listener.logout();
        });

        topContainer.addComponent(topMenu);
        topContainer.setComponentAlignment(topMenu, Alignment.MIDDLE_RIGHT);

        return topContainer;
    }

    /**
     * Builds the autoFiltersContainer with the options for autofilter
     *
     * @return the autoFiltersContainer
     */
    private Component buildAutoFiltersContainer() {

        // Container principal da barra de filtros
        autoFiltersContainer = new HorizontalLayout();
        autoFiltersContainer.setWidth("100%");
        autoFiltersContainer.setHeight(null);

        // lado esquerdo com filtros
        autoFilterLeftContainer = new HorizontalLayout();
        autoFilterLeftContainer.setSizeUndefined();

        // filtro por usuarios
        Accordion userAccordion = new Accordion();

        assigneeFilterOptionGroup = new OptionGroup();
        assigneeFilterOptionGroup.setMultiSelect(true);
        assigneeFilterOptionGroup.addValueChangeListener((Property.ValueChangeEvent event) -> {
            if (assigneeFilterOptionGroup.getValue() != null) {
                listener.aplicarFiltroPesquisa();
            }
        });

        requestorsFilterOptionGroup = new OptionGroup();
        requestorsFilterOptionGroup.setMultiSelect(true);
        requestorsFilterOptionGroup.addValueChangeListener((Property.ValueChangeEvent event) -> {
            if (requestorsFilterOptionGroup.getValue() != null) {
                listener.aplicarFiltroPesquisa();
            }
        });

        followersFilterOptionGroup = new OptionGroup();
        followersFilterOptionGroup.setMultiSelect(true);
        followersFilterOptionGroup.addValueChangeListener((Property.ValueChangeEvent event) -> {
            if (followersFilterOptionGroup.getValue() != null) {
                listener.aplicarFiltroPesquisa();
            }
        });

        userAccordion.addTab(assigneeFilterOptionGroup, GestorPresenter.getMENSAGENS().getString("DashboardView.assigneesFilterOptionGroup"));
        userAccordion.addTab(requestorsFilterOptionGroup, GestorPresenter.getMENSAGENS().getString("DashboardView.requestorsFilterOptionGroup"));
        userAccordion.addTab(followersFilterOptionGroup, GestorPresenter.getMENSAGENS().getString("DashboardView.followerFilterOptionGroup"));

        userFilterPopupButton = new PopupButton(GestorPresenter.getMENSAGENS().getString("DashboardView.userFilterPopupButton"));
        userFilterPopupButton.setContent(userAccordion);

        autoFilterLeftContainer.addComponent(userFilterPopupButton);

        // filtro por empresa
        companyFilterOptionGroup = new OptionGroup();
        companyFilterOptionGroup.setMultiSelect(true);
        companyFilterOptionGroup.setMultiSelect(true);
        companyFilterOptionGroup.addValueChangeListener((Property.ValueChangeEvent event) -> {
            if (companyFilterOptionGroup.getValue() != null) {
                listener.aplicarFiltroPesquisa();
            }
        });

        companyFilterPopupButton = new PopupButton(GestorPresenter.getMENSAGENS().getString("DashboardView.companyFilterPopupButton"));
        companyFilterPopupButton.setContent(companyFilterOptionGroup);
        autoFilterLeftContainer.addComponent(companyFilterPopupButton);

        // filtro por data fim 
        endDateFilterPopupButton = new PopupButton(GestorPresenter.getMENSAGENS().getString("DashboardView.endDateFilterPopupButton"));
        endDateFilterDateField = new InlineDateField();
        endDateFilterDateField.addValueChangeListener((Property.ValueChangeEvent event) -> {
            if (endDateFilterDateField.getValue() != null) {
                listener.aplicarFiltroPesquisa();
            }
        });

        endDateFilterPopupButton.setContent(endDateFilterDateField);
        autoFilterLeftContainer.addComponent(endDateFilterPopupButton);

        /**
         * COMENTADO: Projeção postergada para v2 forecastFilterOptionGroup =
         * new OptionGroup(); forecastFilterOptionGroup.setMultiSelect(true);
         * forecastFilterOptionGroup.addValueChangeListener((Property.ValueChangeEvent
         * event) -> { if (forecastFilterOptionGroup.getValue() != null) {
         * listener.aplicarFiltroPesquisa(); } });
         *
         * forecastFilterButton = new
         * PopupButton(GestorPresenter.getMENSAGENS().getString("DashboardView.forecastFilterButton"));
         * forecastFilterButton.setContent(forecastFilterOptionGroup);
         *
         * autoFilterLeftContainer.addComponent(forecastFilterButton);
         */
        switchAndOrFilters = new OptionGroup(GestorPresenter.getMENSAGENS().getString("DashboardView.switchAndOrFilters"));
        switchAndOrFilters.setMultiSelect(false);
        switchAndOrFilters.addStyleName("horizontal");;
        switchAndOrFilters.addValueChangeListener((Property.ValueChangeEvent event) -> {
            if (switchAndOrFilters.getValue() != null) {
                listener.aplicarFiltroPesquisa();
            }
        });

        autoFilterLeftContainer.addComponent(switchAndOrFilters);
        switchAndOrFilters.setVisible(false);

        cleanFiltersButton = new Button(GestorPresenter.getMENSAGENS().getString("DashboardView.cleanFiltersButton"), (Button.ClickEvent event) -> {
            listener.removerFiltrosPesquisa();
        });
        //cleanFiltersButton.setStyleName("link");
        autoFilterLeftContainer.addComponent(cleanFiltersButton);
        cleanFiltersButton.setVisible(false);

        autoFilterSearchContainer = new VerticalLayout();
        autoFilterSearchContainer.setSizeUndefined();

        quickSeachTextField = new TextField();
        quickSeachTextField.setInputPrompt(GestorPresenter.getMENSAGENS().getString("DashboardView.quickSeachTextField"));
        autoFilterSearchContainer.addComponent(quickSeachTextField);

        advancedSearchButton = new Button(GestorPresenter.getMENSAGENS().getString("DashboardView.advancedSearchButton"));
        advancedSearchButton.setStyleName("link");
        autoFilterSearchContainer.addComponent(advancedSearchButton);

        HorizontalLayout autoFilterRightContainer = new HorizontalLayout();
        autoFilterRightContainer.addComponent(autoFilterSearchContainer);

        trashButton = new Button(GestorPresenter.getMENSAGENS().getString("DashboardView.trashButton"));
        trashButton.addClickListener((ClickEvent event) -> {
            listener.trashButtonPressed();
        });
        trashButton.setIcon(FontAwesome.TRASH_O);
        autoFilterRightContainer.addComponent(trashButton);

        autoFiltersContainer.addComponent(autoFilterLeftContainer);
        autoFiltersContainer.setComponentAlignment(autoFilterLeftContainer, Alignment.MIDDLE_LEFT);
        autoFiltersContainer.addComponent(autoFilterRightContainer);
        autoFiltersContainer.setComponentAlignment(autoFilterRightContainer, Alignment.MIDDLE_RIGHT);

        return autoFiltersContainer;

    }

    /**
     * Builds the currentDateContainer with an inner label with the current date
     *
     * @return the currentDateContainer
     */
    private Component buildCurrentDateContainer() {

        currentDateContainer = new HorizontalLayout();
        currentDateContainer.setSpacing(true);
        currentDateContainer.setWidth("100%");
        currentDateContainer.setHeight(null);

        currentDateLabel = new Label("<h1>" + LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)) + "</h1>");
        currentDateLabel.setContentMode(ContentMode.HTML);

        currentDateContainer.addComponent(currentDateLabel);

        return currentDateContainer;
    }

    /**
     * Builds the tabSheetContainer with all inner tables (task and target)
     *
     * @return the tabSheetContainer
     */
    private Component buildTabSheetContainer() {

        tabSheetContainer = new HorizontalLayout();
        tabSheetContainer.setSpacing(true);
        tabSheetContainer.setWidth("100%");
        tabSheetContainer.setHeight(null);

        tabSheet = new TabSheet();
        tabSheet.setWidth("100%");
        tabSheet.setHeight("100%");
        tabSheet.addTab(buildTaskTable(), GestorPresenter.getMENSAGENS().getString("DashboardView.taskTab"));
        tabSheet.addTab(buildTargetTable(), GestorPresenter.getMENSAGENS().getString("DashboardView.targetTab"));
        tabSheet.addTab(new HorizontalLayout(), GestorPresenter.getMENSAGENS().getString("DashboardView.publicationsTab"));

        tabSheetContainer.addComponent(tabSheet);

        return tabSheetContainer;
    }

    /**
     * Build the task table with its columns and initial parameters
     *
     * @return the task table
     */
    private Table buildTaskTable() {

        tarefaTable = new TreeTable() {
            {
                this.alwaysRecalculateColumnWidths = true;
            }
        };
        tarefaTable.setWidth(null);

        GestorPresenter.configuraExpansaoColunaCodigo(tarefaTable, GestorPresenter.getMENSAGENS().getString("DashboardView.taskTable.cod"));

        tarefaTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.taskTable.cod"), Button.class, "");
        tarefaTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.taskTable.title"), Button.class, "");
        tarefaTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.taskTable.name"), Button.class, "");
        tarefaTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.taskTable.company"), String.class, "");
        tarefaTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.taskTable.requestor"), String.class, "");
        tarefaTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.taskTable.assingee"), String.class, "");
        tarefaTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.taskTable.startDate"), String.class, "");
        tarefaTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.taskTable.endDate"), String.class, "");
        tarefaTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.taskTable.state"), PopupButton.class, "");
        tarefaTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.taskTable.email"), Button.class, "");
        tarefaTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.taskTable.chat"), Button.class, "");

        tarefaTable.addGeneratedColumn(GestorPresenter.getMENSAGENS().getString("DashboardView.taskTable.colunaBotaoRemover"), (Table source, final Object itemId, Object columnId) -> {
            Button removeButton = new Button();
            removeButton.addClickListener((ClickEvent event) -> {
                listener.removerTarefaButtonClicked((Tarefa) itemId);
            });

            removeButton.setEnabled(listener.verificaPermissaoAcessoRemocaoTarefa((Tarefa) itemId));
            removeButton.setIcon(FontAwesome.TRASH_O);

            return removeButton;
        });

        tarefaTable.setPageLength(5);
        tarefaTable.setSelectable(true);
        tarefaTable.setImmediate(true);

        return tarefaTable;

    }

    /**
     * Build the target table with its columns and initial parameters
     *
     * @return the target table
     */
    private Table buildTargetTable() {

        targetTable = new TreeTable() {
            {
                this.alwaysRecalculateColumnWidths = true;
            }
        };

        GestorPresenter.configuraExpansaoColunaCodigo(targetTable, GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.cod"));
        targetTable.setWidth("100%");

        targetTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.cod"), Button.class, "");
        targetTable.setColumnWidth(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.cod"), 100);
        targetTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.title"), Button.class, "");
        targetTable.setColumnWidth(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.title"), 100);
        targetTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.name"), Button.class, "");
        targetTable.setColumnWidth(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.name"), 250);
        targetTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.company"), String.class, "");
        targetTable.setColumnWidth(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.company"), 200);
        targetTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.requestor"), String.class, "");
        targetTable.setColumnWidth(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.requestor"), 80);
        targetTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.assingee"), String.class, "");
        targetTable.setColumnWidth(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.assingee"), 80);
        targetTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.startDate"), String.class, "");
        targetTable.setColumnWidth(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.startDate"), 80);
        targetTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.endDate"), String.class, "");
        targetTable.setColumnWidth(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.endDate"), 80);
        /**
         * COMENTADO: Projeção postergada para v2
         * targetTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.forecast"),
         * Character.class, "");
         */

//        Essa linha estava fazendo as metas não serem exibidas na table
//        targetTable.setColumnWidth(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.forecast"), 30);
        targetTable.addContainerProperty(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.email"), Button.class, "");
        targetTable.setColumnWidth(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.email"), 30);
        targetTable.addGeneratedColumn(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.colunaBotaoRemover"), (Table source, final Object itemId, Object columnId) -> {
            Button removeButton = new Button();
            removeButton.addClickListener((ClickEvent event) -> {
                if (itemId instanceof Meta) {
                    listener.removerMetaButtonClicked((Meta) itemId);
                } else {
                    listener.removerTarefaButtonClicked((Tarefa) itemId);
                }
            });
            if (itemId instanceof Meta) {
                removeButton.setEnabled(listener.verificaPermissaoAcessoRemocaoMeta((Meta) itemId));
            } else {
                removeButton.setEnabled(listener.verificaPermissaoAcessoRemocaoTarefa((Tarefa) itemId));
            }

            removeButton.setIcon(FontAwesome.TRASH_O);

            return removeButton;
        });
        targetTable.setColumnWidth(GestorPresenter.getMENSAGENS().getString("DashboardView.targetTable.colunaBotaoRemover"), 30);

        targetTable.setPageLength(15);
        targetTable.setSelectable(true);
        targetTable.setImmediate(true);

        return targetTable;

    }

    /**
     * Builds the bottom container with its 3 inner components (tasks,
     * forecasts, invites).
     *
     * The containers are populatted on DashboardPresenter.
     * carregaVisualizacaoInicial();
     *
     * @return the bottom container
     */
    private Component buildBottomContainer() {

        bottomContainer = new HorizontalLayout();
        bottomContainer.setSpacing(true);
        bottomContainer.setWidth("100%");
        bottomContainer.setHeight(null);

        bottomTasksContainer = new VerticalLayout();
        bottomTasksContainer.setStyleName("blue");
        bottomTasksContainer.setWidth("20%");
        bottomContainer.addComponent(bottomTasksContainer);

        bottomForecastsContainer = new VerticalLayout();
        bottomForecastsContainer.setWidth("20%");
        bottomContainer.addComponent(bottomForecastsContainer);

        /**
         * COMENTADO: Projeção postergada para v2 Button projecaoButton; for
         * (int i = 0; i < 5; i++) { projecaoButton = new Button("Projecao " +
         * (i + 1)); projecaoButton.setStyleName("v-button-link");
         * bottomForecastsContainer.addComponent(projecaoButton); }
         */
        bottomInvitesContainer = new VerticalLayout();
        bottomInvitesContainer.setStyleName("blue");
        bottomInvitesContainer.setWidth("20%");
        bottomContainer.addComponent(bottomInvitesContainer);

        return bottomContainer;
    }

    public void abrirPopUpSelecaoTemplates(List<Tarefa> templates) {

        Window templatesWindow = new Window("Escolha a tarefa modelo");

        templatesWindow.setModal(true);

        VerticalLayout content = new VerticalLayout();
        content.setMargin(true);
        content.setSpacing(true);
        content.setSizeUndefined();

        listaTemplates = new ListSelect();
        listaTemplates.setMultiSelect(false);
        listaTemplates.setWidth("300px");

        for (Tarefa template : templates) {
            listaTemplates.addItem(template);
            listaTemplates.setItemCaption(template, template.getNome());
        }

        content.addComponent(listaTemplates);

        Button cancelar = new Button("Cancelar", (Button.ClickEvent event) -> {
            templatesWindow.close();
        });

        Button criar = new Button("Criar Tarefa", (Button.ClickEvent event) -> {

            Tarefa template = (Tarefa) listaTemplates.getValue();
            listener.criarTarefaPorTemplate(template);

        });
        content.addComponent(new HorizontalLayout(cancelar, criar));

        templatesWindow.setContent(content);

        templatesWindow.center();

        UI.getCurrent().addWindow(templatesWindow);

    }

    // ------------------------------------------------------------------------------------------------
// GETTERS TO EXTERNAL ACCESS
// ------------------------------------------------------------------------------------------------
    public Map<Empresa, MenuBar.MenuItem> getMapEmpresasMenuItemCriar() {
        return mapEmpresasMenuItemCriar;
    }

    public TreeTable getTarefaTable() {
        return tarefaTable;
    }

    public TreeTable getTargetTable() {
        return targetTable;
    }

    public MenuBar.MenuItem getCreateNewByTemplate() {
        return createNewByTemplate;
    }

    public OptionGroup getAssigneesFilterOptionGroup() {
        return assigneeFilterOptionGroup;
    }

    public OptionGroup getRequestorsFilterOptionGroup() {
        return requestorsFilterOptionGroup;
    }

    public OptionGroup getFollowersFilterOptionGroup() {
        return followersFilterOptionGroup;
    }

    public OptionGroup getCompanyFilterOptionGroup() {
        return companyFilterOptionGroup;
    }

    /**
     * COMENTADO: Projeção postergada para v2 public OptionGroup
     * getForecastFilterOptionGroup() { return forecastFilterOptionGroup; }
     */
    public OptionGroup getSwitchAndOrFilters() {
        return switchAndOrFilters;
    }

    public VerticalLayout getBottomTasksContainer() {
        return bottomTasksContainer;
    }

    public VerticalLayout getBottomInvitesContainer() {
        return bottomInvitesContainer;
    }

    public InlineDateField getEndDateFilterDateField() {
        return endDateFilterDateField;
    }

    public Button getCleanFiltersButton() {
        return cleanFiltersButton;
    }

    public ListSelect getListaTemplates() {
        return listaTemplates;
    }

}
