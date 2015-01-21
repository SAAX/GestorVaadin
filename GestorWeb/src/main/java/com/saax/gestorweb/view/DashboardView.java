package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.LoginModel;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.GestorWebImagens;
import com.vaadin.data.Property;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.ResourceBundle;
import javax.persistence.EntityManager;
import org.vaadin.hene.popupbutton.PopupButton;

/**
 * MVP View tier of the Dashboard.
 * Static struture with vaadin visual components
 * @author Rodrigo
 */
public class DashboardView extends VerticalLayout {

    // message resource
    private final transient ResourceBundle messages = ((GestorMDI) UI.getCurrent()).getMensagens();
    // image resource
    private final GestorWebImagens images = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();
    // listener interface (presenter in MVP)
    private DashboardViewListenter listener;
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
    private MenuBar.MenuItem createNewByCategoryMenuItem;
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
    private PopupButton forecastFilterButton;
    private OptionGroup forecastFilterOptionGroup;
    private Button cleanFiltersButton;
    private OptionGroup switchAndOrFilters;
    // right container
    private VerticalLayout autoFilterRigthContainer;
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
    private TreeTable taskTable;
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
            GestorSession.setAttribute("loggedUser", loggedUser);
            loggedUser.setEmpresaAtiva(new LoginModel().getEmpresaUsuarioLogado());
            listener.usuarioLogadoAlteradoAPENASTESTE();
            listener.removerFiltrosPesquisa();
        });
        switchUserContainer.addComponent(switchUserCombo);
        return switchUserContainer;

    }
    
    /**
     * Build the topContainer with the inner menu bar
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

        createNewByCategoryMenuItem = topMenu.addItem("<h3>"+messages.getString("DashboardView.createNewByCategoryMenuItem")+"</h3>", null, null);

        createNewByTemplate = createNewByCategoryMenuItem.addItem(messages.getString("DashboardView.createNewByTemplate"), (MenuBar.MenuItem selectedItem) -> {
            listener.createsNewTaskByTemplate();
        });

        MenuBar.MenuItem publicationsMenuItem = topMenu.addItem("<h3>"+messages.getString("DashboardView.publicationsMenuItem")+"</h3>", null, null);

        MenuBar.MenuItem reportsMenuItem = topMenu.addItem("<h3>"+messages.getString("DashboardView.reportsMenuItem")+"</h3>", null, null);

        MenuBar.MenuItem config = topMenu.addItem("<h3>Config</h3>", null, null);
        config.addItem("Config 1", null, null);
        config.addItem("Config 2", null, null);
        config.addItem("Config 3", null, null);

        topMenu.addItem("<h3>"+messages.getString("DashboardView.logoutMenuItem")+"</h3>", null, (MenuBar.MenuItem selectedItem) -> {
            listener.logout();
        });

        topContainer.addComponent(topMenu);
        topContainer.setComponentAlignment(topMenu, Alignment.MIDDLE_RIGHT);

        return topContainer;
    }

    /**
     * Builds the autoFiltersContainer with the options for autofilter
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
                listener.applyAutoFilter();
            }
        });

        requestorsFilterOptionGroup = new OptionGroup();
        requestorsFilterOptionGroup.setMultiSelect(true);
        requestorsFilterOptionGroup.addValueChangeListener((Property.ValueChangeEvent event) -> {
            if (requestorsFilterOptionGroup.getValue() != null) {
                listener.applyAutoFilter();
            }
        });

        followersFilterOptionGroup = new OptionGroup();
        followersFilterOptionGroup.setMultiSelect(true);
        followersFilterOptionGroup.addValueChangeListener((Property.ValueChangeEvent event) -> {
            if (followersFilterOptionGroup.getValue() != null) {
                listener.applyAutoFilter();
            }
        });

        userAccordion.addTab(assigneeFilterOptionGroup, messages.getString("DashboardView.assigneesFilterOptionGroup"));
        userAccordion.addTab(requestorsFilterOptionGroup, messages.getString("DashboardView.requestorsFilterOptionGroup"));
        userAccordion.addTab(followersFilterOptionGroup, messages.getString("DashboardView.followerFilterOptionGroup"));

        userFilterPopupButton = new PopupButton(messages.getString("DashboardView.userFilterPopupButton"));
        userFilterPopupButton.setContent(userAccordion);

        autoFilterLeftContainer.addComponent(userFilterPopupButton);

        // filtro por empresa
        companyFilterOptionGroup = new OptionGroup();
        companyFilterOptionGroup.setMultiSelect(true);
        companyFilterOptionGroup.setMultiSelect(true);
        companyFilterOptionGroup.addValueChangeListener((Property.ValueChangeEvent event) -> {
            if (companyFilterOptionGroup.getValue() != null) {
                listener.applyAutoFilter();
            }
        });

        companyFilterPopupButton = new PopupButton(messages.getString("DashboardView.companyFilterPopupButton"));
        companyFilterPopupButton.setContent(companyFilterOptionGroup);
        autoFilterLeftContainer.addComponent(companyFilterPopupButton);

        // filtro por data fim 
        endDateFilterPopupButton = new PopupButton(messages.getString("DashboardView.endDateFilterPopupButton"));
        endDateFilterDateField = new InlineDateField();
        endDateFilterDateField.addValueChangeListener((Property.ValueChangeEvent event) -> {
            if (endDateFilterDateField.getValue() != null) {
                listener.applyAutoFilter();
            }
        });

        endDateFilterPopupButton.setContent(endDateFilterDateField);
        autoFilterLeftContainer.addComponent(endDateFilterPopupButton);

        forecastFilterOptionGroup = new OptionGroup();
        forecastFilterOptionGroup.setMultiSelect(true);
        forecastFilterOptionGroup.addValueChangeListener((Property.ValueChangeEvent event) -> {
            if (forecastFilterOptionGroup.getValue() != null) {
                listener.applyAutoFilter();
            }
        });

        forecastFilterButton = new PopupButton(messages.getString("DashboardView.forecastFilterButton"));
        forecastFilterButton.setContent(forecastFilterOptionGroup);

        autoFilterLeftContainer.addComponent(forecastFilterButton);

        switchAndOrFilters = new OptionGroup(messages.getString("DashboardView.switchAndOrFilters"));
        switchAndOrFilters.setMultiSelect(false);
        switchAndOrFilters.addStyleName("horizontal");;
        switchAndOrFilters.addValueChangeListener((Property.ValueChangeEvent event) -> {
            if (switchAndOrFilters.getValue() != null) {
                listener.applyAutoFilter();
            }
        });

        autoFilterLeftContainer.addComponent(switchAndOrFilters);
        switchAndOrFilters.setVisible(false);

        cleanFiltersButton = new Button(messages.getString("DashboardView.cleanFiltersButton"), (Button.ClickEvent event) -> {
            listener.removerFiltrosPesquisa();
        });
        cleanFiltersButton.setStyleName("link");
        autoFilterLeftContainer.addComponent(cleanFiltersButton);
        cleanFiltersButton.setVisible(false);

        autoFilterRigthContainer = new VerticalLayout();
        autoFilterRigthContainer.setSizeUndefined();

        quickSeachTextField = new TextField();
        quickSeachTextField.setInputPrompt(messages.getString("DashboardView.quickSeachTextField"));
        autoFilterRigthContainer.addComponent(quickSeachTextField);

        advancedSearchButton = new Button(messages.getString("DashboardView.advancedSearchButton"));
        advancedSearchButton.setStyleName("link");
        autoFilterRigthContainer.addComponent(advancedSearchButton);

        autoFiltersContainer.addComponent(autoFilterLeftContainer);
        autoFiltersContainer.setComponentAlignment(autoFilterLeftContainer, Alignment.MIDDLE_LEFT);
        autoFiltersContainer.addComponent(autoFilterRigthContainer);
        autoFiltersContainer.setComponentAlignment(autoFilterRigthContainer, Alignment.MIDDLE_RIGHT);

        return autoFiltersContainer;

    }

    /**
     * Builds the currentDateContainer with an inner label with the current date
     * @return the currentDateContainer
     */
    private Component buildCurrentDateContainer() {
    
        currentDateContainer = new HorizontalLayout();
        currentDateContainer.setSpacing(true);
        currentDateContainer.setWidth("100%");
        currentDateContainer.setHeight(null);

        currentDateLabel = new Label("<h1>"+LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))+"</h1>");
        currentDateLabel.setContentMode(ContentMode.HTML);

        currentDateContainer.addComponent(currentDateLabel);

        return currentDateContainer;
    }

    /**
     * Builds the tabSheetContainer with all inner tables (task and target)
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
        tabSheet.addTab(buildTaskTable(), messages.getString("DashboardView.taskTab"));
        tabSheet.addTab(buildTargetTable(), messages.getString("DashboardView.targetTab"));
        tabSheet.addTab(new HorizontalLayout(), messages.getString("DashboardView.publicationsTab"));

        tabSheetContainer.addComponent(tabSheet);

        return tabSheetContainer;
    }
    
    /**
     * Build the task table with its columns and initial parameters
     * @return the task table
     */
    private Table buildTaskTable() {

        taskTable = new TreeTable();
        taskTable.setWidth("100%");
        taskTable.addContainerProperty(messages.getString("DashboardView.taskTable.cod"), Button.class, "");
        taskTable.setColumnWidth(messages.getString("DashboardView.taskTable.cod"), 70);
        taskTable.addContainerProperty(messages.getString("DashboardView.taskTable.title"), Button.class, "");
        taskTable.setColumnWidth(messages.getString("DashboardView.taskTable.title"), 50);
        taskTable.addContainerProperty(messages.getString("DashboardView.taskTable.name"), Button.class, "");
        taskTable.setColumnWidth(messages.getString("DashboardView.taskTable.name"), 250);
        taskTable.addContainerProperty(messages.getString("DashboardView.taskTable.company"), String.class, "");
        taskTable.setColumnWidth(messages.getString("DashboardView.taskTable.company"), 200);
        taskTable.addContainerProperty(messages.getString("DashboardView.taskTable.requestor"), String.class, "");
        taskTable.setColumnWidth(messages.getString("DashboardView.taskTable.requestor"), 80);
        taskTable.addContainerProperty(messages.getString("DashboardView.taskTable.assingee"), String.class, "");
        taskTable.setColumnWidth(messages.getString("DashboardView.taskTable.assingee"), 80);
        taskTable.addContainerProperty(messages.getString("DashboardView.taskTable.startDate"), String.class, "");
        taskTable.setColumnWidth(messages.getString("DashboardView.taskTable.startDate"), 80);
        taskTable.addContainerProperty(messages.getString("DashboardView.taskTable.endDate"), String.class, "");
        taskTable.setColumnWidth(messages.getString("DashboardView.taskTable.endDate"), 80);
        taskTable.addContainerProperty(messages.getString("DashboardView.taskTable.state"), PopupButton.class, "");
        taskTable.setColumnWidth(messages.getString("DashboardView.taskTable.state"), 200);
        taskTable.addContainerProperty(messages.getString("DashboardView.taskTable.forecast"), Character.class, "");
        taskTable.setColumnWidth(messages.getString("DashboardView.taskTable.forecast"), 30);
        taskTable.addContainerProperty(messages.getString("DashboardView.taskTable.email"), Button.class, "");
        taskTable.setColumnWidth(messages.getString("DashboardView.taskTable.email"), 30);
        taskTable.addContainerProperty(messages.getString("DashboardView.taskTable.chat"), Button.class, "");
        taskTable.setColumnWidth(messages.getString("DashboardView.taskTable.chat"), 30);

        taskTable.setPageLength(0);
        taskTable.setSelectable(true);
        taskTable.setImmediate(true);

        return taskTable;

    }
    
    /**
     * Build the target table with its columns and initial parameters
     * @return the target table
     */
    private Table buildTargetTable() {

        targetTable = new TreeTable();
        targetTable.setWidth("100%");
        
        targetTable.addContainerProperty(messages.getString("DashboardView.targetTable.cod"), Button.class, "");
        targetTable.setColumnWidth(messages.getString("DashboardView.targetTable.cod"), 70);
        targetTable.addContainerProperty(messages.getString("DashboardView.targetTable.title"), Button.class, "");
        targetTable.setColumnWidth(messages.getString("DashboardView.targetTable.title"), 50);
        targetTable.addContainerProperty(messages.getString("DashboardView.targetTable.name"), Button.class, "");
        targetTable.setColumnWidth(messages.getString("DashboardView.targetTable.name"), 250);
        targetTable.addContainerProperty(messages.getString("DashboardView.targetTable.company"), String.class, "");
        targetTable.setColumnWidth(messages.getString("DashboardView.targetTable.company"), 200);
        targetTable.addContainerProperty(messages.getString("DashboardView.targetTable.requestor"), String.class, "");
        targetTable.setColumnWidth(messages.getString("DashboardView.targetTable.requestor"), 80);
        targetTable.addContainerProperty(messages.getString("DashboardView.targetTable.assingee"), String.class, "");
        targetTable.setColumnWidth(messages.getString("DashboardView.targetTable.assingee"), 80);
        targetTable.addContainerProperty(messages.getString("DashboardView.targetTable.startDate"), String.class, "");
        targetTable.setColumnWidth(messages.getString("DashboardView.targetTable.startDate"), 80);
        targetTable.addContainerProperty(messages.getString("DashboardView.targetTable.endDate"), String.class, "");
        targetTable.setColumnWidth(messages.getString("DashboardView.targetTable.endDate"), 80);
        targetTable.addContainerProperty(messages.getString("DashboardView.targetTable.forecast"), Character.class, "");
        targetTable.setColumnWidth(messages.getString("DashboardView.targetTable.forecast"), 30);
        targetTable.addContainerProperty(messages.getString("DashboardView.targetTable.email"), Button.class, "");
        targetTable.setColumnWidth(messages.getString("DashboardView.targetTable.email"), 30);
        
        
        
        targetTable.setPageLength(0);
        targetTable.setSelectable(true);
        targetTable.setImmediate(true);

        return targetTable;

    }

    /**
     * Builds the bottom container with its 3 inner components (tasks, forecasts, invites)
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

        Button projecaoButton;
        for (int i = 0; i < 5; i++) {
            projecaoButton = new Button("Projecao " + (i + 1));
            projecaoButton.setStyleName("v-button-link");
            bottomForecastsContainer.addComponent(projecaoButton);
        }

        bottomInvitesContainer = new VerticalLayout();
        bottomInvitesContainer.setStyleName("blue");
        bottomInvitesContainer.setWidth("20%");
        bottomContainer.addComponent(bottomInvitesContainer);

        Button conviteButton;
        for (int i = 0; i < 5; i++) {
            conviteButton = new Button("Convite  " + (i + 1));
            conviteButton.setStyleName("v-button-link");
            bottomInvitesContainer.addComponent(conviteButton);
        }

        return bottomContainer;
    }

    
    // ------------------------------------------------------------------------------------------------
    // GETTERS TO EXTERNAL ACCESS
    // ------------------------------------------------------------------------------------------------
    
    public MenuBar.MenuItem getCreateNewByCategoryMenuItem() {
        return createNewByCategoryMenuItem;
    }

    public TreeTable getTaskTable() {
        return taskTable;
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

    public OptionGroup getForecastFilterOptionGroup() {
        return forecastFilterOptionGroup;
    }

    public OptionGroup getSwitchAndOrFilters() {
        return switchAndOrFilters;
    }

    public VerticalLayout getBottomTasksContainer() {
        return bottomTasksContainer;
    }

    public InlineDateField getEndDateFilterDateField() {
        return endDateFilterDateField;
    }

    public Button getCleanFiltersButton() {
        return cleanFiltersButton;
    }
    
    

}
