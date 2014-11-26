package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.LoginModel;
import com.saax.gestorweb.model.datamodel.Tarefa;
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
import java.util.List;
import java.util.ResourceBundle;
import javax.persistence.EntityManager;
import org.vaadin.hene.popupbutton.PopupButton;

/**
 *
 * @author Rodrigo
 */
public class DashBoardView extends VerticalLayout {

    // Referencia ao recurso das mensagens:
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();

    // A view mantem acesso ao listener (Presenter) para notificar os eventos
    // Este acesso se dá por uma interface para manter a abstração das camadas
    private DashboardViewListenter listener;
    private OptionGroup permutacaoPesquisaOptionGroup;
    private MenuBar.MenuItem criarNovoMenuItem;
    private MenuBar.MenuItem criarPadraoMenuItem;
    private MenuBar.MenuItem criarViaTemplateMenuItem;

    public void setListener(DashboardViewListenter listener) {
        this.listener = listener;
    }

    // ------------------------------------------------------------------------
    // menuSuperiorContainer: 
    private HorizontalLayout menuSuperiorContainer;
    private MenuBar menuSuperior;

    // filtrosPesquisaContainer:
    private HorizontalLayout filtrosPesquisaContainer;

    // filtrosPesquisaContainer -> filtrosPesquisaEsquerdaContainer:
    private HorizontalLayout filtrosPesquisaEsquerdaContainer;
    private PopupButton filtroUsuarioButton;
    private OptionGroup filtroUsuarioResponsavelOptionGroup;
    private OptionGroup filtroUsuarioSolicitanteOptionGroup;
    private OptionGroup filtroUsuarioParticipanteOptionGroup;
    private PopupButton filtroEmpresaButton;
    private OptionGroup filtroEmpresaOptionGroup;
    private PopupButton filtroDataFimButton;
    private OptionGroup filtroProjecaoOptionGroup;
    private InlineDateField filtroDataFimDateField;
    private PopupButton filtroProjecaoButton;
    private Button removerFiltroPesquisa;

    // filtrosPesquisaContainer -> filtrosPesquisaEsquerdaContainer:
    private VerticalLayout filtrosPesquisaDireitaContainer;
    private TextField filtroPesquisaRapidaTextField;
    private Button filtroPesquisaAvancadaButton;

    // dataAtualContainer
    private HorizontalLayout dataAtualContainer;
    private Label labelDataAtual;

    // abasContainer
    private HorizontalLayout abasContainer;
    private TabSheet painelAbas;
    private TreeTable tarefasTable;
    private TreeTable metasTable;

    // rodapeContainer
    private HorizontalLayout rodapeContainer;
    private VerticalLayout principaisTarefasContainer;
    private VerticalLayout principaisProjecoesContainer;
    private VerticalLayout principaisConvitesContainer;

    // ------------------------------------------------------------------------
    public DashBoardView() {

        setMargin(true);
        setSpacing(true);
        setWidth("100%");
        setHeight(null);

        addComponent(buildComboAlteraUsuario());

        addComponent(buildMenuSuperiorContainer());

        addComponent(buildFiltrosPesquisaContainer());

        addComponent(buildDataAtualContainer());

        addComponent(buildAbasContainer());

        addComponent(buildPaineisRodape());

    }

    private Table buildTarefasTable() {

        tarefasTable = new TreeTable();
        getTarefasTable().setWidth("100%");
        getTarefasTable().addContainerProperty("Cod", Button.class, "");
        getTarefasTable().setColumnWidth("Cod", 70);
        getTarefasTable().addContainerProperty("Título", Button.class, "");
        getTarefasTable().setColumnWidth("Título", 50);
        getTarefasTable().addContainerProperty("Nome", Button.class, "");
        getTarefasTable().setColumnWidth("Nome", 250);
        getTarefasTable().addContainerProperty("Empresa/Filial", String.class, "");
        getTarefasTable().setColumnWidth("Empresa/Filial", 200);
        getTarefasTable().addContainerProperty("Solicitante", String.class, "");
        getTarefasTable().setColumnWidth("Solicitante", 80);
        getTarefasTable().addContainerProperty("Responsável", String.class, "");
        getTarefasTable().setColumnWidth("Responsável", 80);
        getTarefasTable().addContainerProperty("Data Início", String.class, "");
        getTarefasTable().setColumnWidth("Data Início", 80);
        getTarefasTable().addContainerProperty("Data Fim", String.class, "");
        getTarefasTable().setColumnWidth("Data Fim", 80);
        getTarefasTable().addContainerProperty("Status", PopupButton.class, "");
        getTarefasTable().setColumnWidth("Status", 200);
        getTarefasTable().addContainerProperty("Projeção", Character.class, "");
        getTarefasTable().setColumnWidth("Proj.", 30);
        getTarefasTable().addContainerProperty("Email", Button.class, "");
        getTarefasTable().setColumnWidth("Email", 30);
        getTarefasTable().addContainerProperty("Chat", Button.class, "");
        getTarefasTable().setColumnWidth("Chat", 30);

        getTarefasTable().setPageLength(7);
        getTarefasTable().setSelectable(true);
        getTarefasTable().setImmediate(true);

        return getTarefasTable();

    }
    
    private Table buildMetasTable() {

        metasTable = new TreeTable();
        getMetasTable().setWidth("100%");
        getMetasTable().addContainerProperty("Cod", Button.class, "");
        getMetasTable().setColumnWidth("Cod", 70);
        getMetasTable().addContainerProperty("Título", Button.class, "");
        getMetasTable().setColumnWidth("Título", 150);
        //getMetasTable().addContainerProperty("Nome", Button.class, "");
        //getMetasTable().setColumnWidth("Nome", 250);
        getMetasTable().addContainerProperty("Empresa/Filial", String.class, "");
        getMetasTable().setColumnWidth("Empresa/Filial", 200);
        getMetasTable().addContainerProperty("Solicitante", String.class, "");
        getMetasTable().setColumnWidth("Solicitante", 100);
        getMetasTable().addContainerProperty("Responsável", String.class, "");
        getMetasTable().setColumnWidth("Responsável", 100);
        getMetasTable().addContainerProperty("Data Início", String.class, "");
        getMetasTable().setColumnWidth("Data Início", 80);
        getMetasTable().addContainerProperty("Data Fim", String.class, "");
        getMetasTable().setColumnWidth("Data Fim", 80);
        //getMetasTable().addContainerProperty("Status", PopupButton.class, "");
        //getMetasTable().setColumnWidth("Status", 200);
        //getMetasTable().addContainerProperty("Projeção", Character.class, "");
        //getMetasTable().setColumnWidth("Proj.", 30);
        getMetasTable().addContainerProperty("Email", Button.class, "");
        getMetasTable().setColumnWidth("Email", 50);
        //getMetasTable().addContainerProperty("Chat", Button.class, "");
        //getMetasTable().setColumnWidth("Chat", 30);

        getMetasTable().setPageLength(7);
        getMetasTable().setSelectable(true);
        getMetasTable().setImmediate(true);

        return getMetasTable();

    }

    private Component buildMenuSuperiorContainer() {

        menuSuperiorContainer = new HorizontalLayout();
        getMenuSuperiorContainer().setSpacing(true);
        getMenuSuperiorContainer().setWidth("100%");
        getMenuSuperiorContainer().setHeight("50px");

        menuSuperior = new MenuBar();
        getMenuSuperior().setHeight("100%");
        getMenuSuperior().setHtmlContentAllowed(true);

        // menu: Criar
        criarNovoMenuItem = getMenuSuperior().addItem("<h3>Criar</h3>", null, null);

        // menu: Criar -> Projeto
        criarViaTemplateMenuItem = criarNovoMenuItem.addItem("Via Template", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                listener.criarNovaTarefaViaTemplate();
            }
        });

        MenuBar.MenuItem publicacoes = getMenuSuperior().addItem("<h3>Publicações</h3>", null, null);

        MenuBar.MenuItem relatorios = getMenuSuperior().addItem("<h3>Relatórios</h3>", null, null);

        MenuBar.MenuItem config = getMenuSuperior().addItem("<h3>Config</h3>", null, null);
        config.addItem("Config 1", null, null);
        config.addItem("Config 2", null, null);
        config.addItem("Config 3", null, null);

        getMenuSuperior().addItem("<h3>Sair</h3>", null, (MenuBar.MenuItem selectedItem) -> {
            getListener().logout();
        });

        getMenuSuperiorContainer().addComponent(getMenuSuperior());
        getMenuSuperiorContainer().setComponentAlignment(getMenuSuperior(), Alignment.MIDDLE_RIGHT);

        return getMenuSuperiorContainer();
    }

    private Accordion filtroUsuarioAccordion;
    private Accordion filtroEmpresaAccordion;

    private Component buildFiltrosPesquisaContainer() {

        // Container principal da barra de filtros
        filtrosPesquisaContainer = new HorizontalLayout();
        getFiltrosPesquisaContainer().setWidth("100%");
        getFiltrosPesquisaContainer().setHeight(null);

        // lado esquerdo com filtros
        filtrosPesquisaEsquerdaContainer = new HorizontalLayout();
        getFiltrosPesquisaEsquerdaContainer().setSizeUndefined();

        // filtro por usuarios
        filtroUsuarioAccordion = new Accordion();

        filtroUsuarioResponsavelOptionGroup = new OptionGroup();
        getFiltroUsuarioResponsavelOptionGroup().setMultiSelect(true);
        getFiltroUsuarioResponsavelOptionGroup().addValueChangeListener((Property.ValueChangeEvent event) -> {
            if (getFiltroUsuarioResponsavelOptionGroup().getValue() != null) {
                getListener().aplicarFiltroPesquisa();
            }
        });

        filtroUsuarioSolicitanteOptionGroup = new OptionGroup();
        getFiltroUsuarioSolicitanteOptionGroup().setMultiSelect(true);
        getFiltroUsuarioSolicitanteOptionGroup().addValueChangeListener((Property.ValueChangeEvent event) -> {
            if (getFiltroUsuarioSolicitanteOptionGroup().getValue() != null) {
                getListener().aplicarFiltroPesquisa();
            }
        });

        filtroUsuarioParticipanteOptionGroup = new OptionGroup();
        getFiltroUsuarioParticipanteOptionGroup().setMultiSelect(true);
        getFiltroUsuarioParticipanteOptionGroup().addValueChangeListener((Property.ValueChangeEvent event) -> {
            if (getFiltroUsuarioParticipanteOptionGroup().getValue() != null) {
                getListener().aplicarFiltroPesquisa();
            }
        });

        getFiltroUsuarioAccordion().addTab(getFiltroUsuarioResponsavelOptionGroup(), "Responsável");
        getFiltroUsuarioAccordion().addTab(getFiltroUsuarioSolicitanteOptionGroup(), "Solicitante");
        getFiltroUsuarioAccordion().addTab(getFiltroUsuarioParticipanteOptionGroup(), "Participante");

        filtroUsuarioButton = new PopupButton("Usuario");
        getFiltroUsuarioButton().setContent(getFiltroUsuarioAccordion());

        getFiltrosPesquisaEsquerdaContainer().addComponent(getFiltroUsuarioButton());

        // filtro por empresa
        filtroEmpresaOptionGroup = new OptionGroup();
        getFiltroEmpresaOptionGroup().setMultiSelect(true);
        getFiltroEmpresaOptionGroup().setMultiSelect(true);
        getFiltroEmpresaOptionGroup().addValueChangeListener((Property.ValueChangeEvent event) -> {
            if (getFiltroEmpresaOptionGroup().getValue() != null) {
                getListener().aplicarFiltroPesquisa();
            }
        });

        filtroEmpresaButton = new PopupButton("Empresa");
        getFiltroEmpresaButton().setContent(getFiltroEmpresaOptionGroup());
        getFiltrosPesquisaEsquerdaContainer().addComponent(getFiltroEmpresaButton());

        // filtro por data fim 
        filtroDataFimButton = new PopupButton("Data Fim");
        filtroDataFimDateField = new InlineDateField();
        getFiltroDataFimDateField().addValueChangeListener((Property.ValueChangeEvent event) -> {
            if (getFiltroDataFimDateField().getValue() != null) {
                getListener().aplicarFiltroPesquisa();
            }
        });

        getFiltroDataFimButton().setContent(getFiltroDataFimDateField());
        getFiltrosPesquisaEsquerdaContainer().addComponent(getFiltroDataFimButton());

        filtroProjecaoOptionGroup = new OptionGroup();
        getFiltroProjecaoOptionGroup().setMultiSelect(true);
        getFiltroProjecaoOptionGroup().addValueChangeListener((Property.ValueChangeEvent event) -> {
            if (getFiltroProjecaoOptionGroup().getValue() != null) {
                getListener().aplicarFiltroPesquisa();
            }
        });

        filtroProjecaoButton = new PopupButton("Projeçao");
        getFiltroProjecaoButton().setContent(getFiltroProjecaoOptionGroup());

        getFiltrosPesquisaEsquerdaContainer().addComponent(getFiltroProjecaoButton());

        permutacaoPesquisaOptionGroup = new OptionGroup("Apresentar tarefas que correspondam a...");
        getPermutacaoPesquisaOptionGroup().setMultiSelect(false);
        getPermutacaoPesquisaOptionGroup().addStyleName("horizontal");;
        getPermutacaoPesquisaOptionGroup().addValueChangeListener((Property.ValueChangeEvent event) -> {
            if (getPermutacaoPesquisaOptionGroup().getValue() != null) {
                getListener().aplicarFiltroPesquisa();
            }
        });

        getFiltrosPesquisaEsquerdaContainer().addComponent(getPermutacaoPesquisaOptionGroup());
        getPermutacaoPesquisaOptionGroup().setVisible(false);

        removerFiltroPesquisa = new Button("Remover Filtros", (Button.ClickEvent event) -> {
            getListener().removerFiltrosPesquisa();
        });
        getRemoverFiltroPesquisa().setStyleName("link");
        getFiltrosPesquisaEsquerdaContainer().addComponent(getRemoverFiltroPesquisa());
        getRemoverFiltroPesquisa().setVisible(false);

        filtrosPesquisaDireitaContainer = new VerticalLayout();
        getFiltrosPesquisaDireitaContainer().setSizeUndefined();

        filtroPesquisaRapidaTextField = new TextField();
        getFiltroPesquisaRapidaTextField().setInputPrompt("pesquisar...");
        getFiltrosPesquisaDireitaContainer().addComponent(getFiltroPesquisaRapidaTextField());

        filtroPesquisaAvancadaButton = new Button("Pesquisa Avançada");
        getFiltroPesquisaAvancadaButton().setStyleName("link");
        getFiltrosPesquisaDireitaContainer().addComponent(getFiltroPesquisaAvancadaButton());

        getFiltrosPesquisaContainer().addComponent(getFiltrosPesquisaEsquerdaContainer());
        getFiltrosPesquisaContainer().setComponentAlignment(getFiltrosPesquisaEsquerdaContainer(), Alignment.MIDDLE_LEFT);
        getFiltrosPesquisaContainer().addComponent(getFiltrosPesquisaDireitaContainer());
        getFiltrosPesquisaContainer().setComponentAlignment(getFiltrosPesquisaDireitaContainer(), Alignment.MIDDLE_RIGHT);

        return getFiltrosPesquisaContainer();

    }

    private Component buildDataAtualContainer() {
        dataAtualContainer = new HorizontalLayout();
        getDataAtualContainer().setSpacing(true);
        getDataAtualContainer().setWidth("100%");
        getDataAtualContainer().setHeight(null);

        labelDataAtual = new Label("<h1>Hoje, 27 de maio de 2014.</h1>");
        getLabelDataAtual().setContentMode(ContentMode.HTML);

        getDataAtualContainer().addComponent(getLabelDataAtual());

        return getDataAtualContainer();
    }

    private Component buildAbasContainer() {

        abasContainer = new HorizontalLayout();
        getAbasContainer().setSpacing(true);
        getAbasContainer().setWidth("100%");
        getAbasContainer().setHeight(null);

        painelAbas = new TabSheet();
        getPainelAbas().setWidth("100%");
        getPainelAbas().setHeight("100%");
        getPainelAbas().addTab(buildTarefasTable(), "Tarefa");
        getPainelAbas().addTab(buildMetasTable(), "Meta");
        getPainelAbas().addTab(new HorizontalLayout(), "Publicações");

        getAbasContainer().addComponent(getPainelAbas());

        return getAbasContainer();
    }

    public void setListaTarefasPrincipais(List<Tarefa> tarefasPrincipais) {
        getPrincipaisTarefasContainer().removeAllComponents();
        for (Tarefa tarefa : tarefasPrincipais) {
            Button tarefaButton = new Button(tarefa.getDescricao());
            tarefaButton.setStyleName("v-button-link");
            getPrincipaisTarefasContainer().addComponent(tarefaButton);
        }
    }

    private Component buildPaineisRodape() {

        rodapeContainer = new HorizontalLayout();
        getRodapeContainer().setSpacing(true);
        getRodapeContainer().setWidth("100%");
        getRodapeContainer().setHeight(null);

        principaisTarefasContainer = new VerticalLayout();
        getPrincipaisTarefasContainer().setStyleName("blue");
        getPrincipaisTarefasContainer().setWidth("20%");
        getRodapeContainer().addComponent(getPrincipaisTarefasContainer());

        principaisProjecoesContainer = new VerticalLayout();
        getPrincipaisProjecoesContainer().setWidth("20%");
        getRodapeContainer().addComponent(getPrincipaisProjecoesContainer());

        Button projecaoButton;
        for (int i = 0; i < 5; i++) {
            projecaoButton = new Button("Projecao " + (i + 1));
            projecaoButton.setStyleName("v-button-link");
            getPrincipaisProjecoesContainer().addComponent(projecaoButton);
        }

        principaisConvitesContainer = new VerticalLayout();
        getPrincipaisConvitesContainer().setStyleName("blue");
        getPrincipaisConvitesContainer().setWidth("20%");
        getRodapeContainer().addComponent(getPrincipaisConvitesContainer());

        Button conviteButton;
        for (int i = 0; i < 5; i++) {
            conviteButton = new Button("Convite  " + (i + 1));
            conviteButton.setStyleName("v-button-link");
            getPrincipaisConvitesContainer().addComponent(conviteButton);
        }

        return getRodapeContainer();
    }

    /**
     * @return the menuSuperior
     */
    public MenuBar getMenuSuperior() {
        return menuSuperior;
    }

    /**
     * @return the filtroUsuarioButton
     */
    public PopupButton getFiltroUsuarioButton() {
        return filtroUsuarioButton;
    }

    /**
     * @return the filtroEmpresaButton
     */
    public PopupButton getFiltroEmpresaButton() {
        return filtroEmpresaButton;
    }

    /**
     * @return the filtroEmpresaOptionGroup
     */
    public OptionGroup getFiltroEmpresaOptionGroup() {
        return filtroEmpresaOptionGroup;
    }

    /**
     * @return the filtroDataFimButton
     */
    public PopupButton getFiltroDataFimButton() {
        return filtroDataFimButton;
    }

    /**
     * @return the filtroDataFimDateField
     */
    public InlineDateField getFiltroDataFimDateField() {
        return filtroDataFimDateField;
    }

    /**
     * @return the filtroProjecaoButton
     */
    public PopupButton getFiltroProjecaoButton() {
        return filtroProjecaoButton;
    }

    /**
     * @return the filtroPesquisaRapidaTextField
     */
    public TextField getFiltroPesquisaRapidaTextField() {
        return filtroPesquisaRapidaTextField;
    }

    /**
     * @return the filtroPesquisaAvancadaButton
     */
    public Button getFiltroPesquisaAvancadaButton() {
        return filtroPesquisaAvancadaButton;
    }

    /**
     * @return the labelDataAtual
     */
    public Label getLabelDataAtual() {
        return labelDataAtual;
    }

    /**
     * @return the painelAbas
     */
    public TabSheet getPainelAbas() {
        return painelAbas;
    }

    /**
     * @return the tarefasTable
     */
    public TreeTable getTarefasTable() {
        return tarefasTable;
    }
    
      /**
     * @return the metasTable
     */
    public TreeTable getMetasTable() {
        return metasTable;
    }

    public OptionGroup getFiltroProjecaoOptionGroup() {
        return filtroProjecaoOptionGroup;
    }

    public Button getRemoverFiltroPesquisa() {
        return removerFiltroPesquisa;
    }

    /**
     * @return the mensagens
     */
    public ResourceBundle getMensagens() {
        return mensagens;
    }

    /**
     * @return the imagens
     */
    public GestorWebImagens getImagens() {
        return imagens;
    }

    /**
     * @return the listener
     */
    public DashboardViewListenter getListener() {
        return listener;
    }

    /**
     * @return the menuSuperiorContainer
     */
    public HorizontalLayout getMenuSuperiorContainer() {
        return menuSuperiorContainer;
    }

    /**
     * @return the filtrosPesquisaContainer
     */
    public HorizontalLayout getFiltrosPesquisaContainer() {
        return filtrosPesquisaContainer;
    }

    /**
     * @return the filtrosPesquisaEsquerdaContainer
     */
    public HorizontalLayout getFiltrosPesquisaEsquerdaContainer() {
        return filtrosPesquisaEsquerdaContainer;
    }

    /**
     * @return the filtroUsuarioResponsavelOptionGroup
     */
    public OptionGroup getFiltroUsuarioResponsavelOptionGroup() {
        return filtroUsuarioResponsavelOptionGroup;
    }

    /**
     * @return the filtroUsuarioSolicitanteOptionGroup
     */
    public OptionGroup getFiltroUsuarioSolicitanteOptionGroup() {
        return filtroUsuarioSolicitanteOptionGroup;
    }

    /**
     * @return the filtroUsuarioParticipanteOptionGroup
     */
    public OptionGroup getFiltroUsuarioParticipanteOptionGroup() {
        return filtroUsuarioParticipanteOptionGroup;
    }

    /**
     * @return the filtrosPesquisaDireitaContainer
     */
    public VerticalLayout getFiltrosPesquisaDireitaContainer() {
        return filtrosPesquisaDireitaContainer;
    }

    /**
     * @return the dataAtualContainer
     */
    public HorizontalLayout getDataAtualContainer() {
        return dataAtualContainer;
    }

    /**
     * @return the abasContainer
     */
    public HorizontalLayout getAbasContainer() {
        return abasContainer;
    }

    /**
     * @return the rodapeContainer
     */
    public HorizontalLayout getRodapeContainer() {
        return rodapeContainer;
    }

    /**
     * @return the principaisTarefasContainer
     */
    public VerticalLayout getPrincipaisTarefasContainer() {
        return principaisTarefasContainer;
    }

    /**
     * @return the principaisProjecoesContainer
     */
    public VerticalLayout getPrincipaisProjecoesContainer() {
        return principaisProjecoesContainer;
    }

    /**
     * @return the principaisConvitesContainer
     */
    public VerticalLayout getPrincipaisConvitesContainer() {
        return principaisConvitesContainer;
    }

    /**
     * @return the filtroUsuarioAccordion
     */
    public Accordion getFiltroUsuarioAccordion() {
        return filtroUsuarioAccordion;
    }

    public OptionGroup getPermutacaoPesquisaOptionGroup() {
        return permutacaoPesquisaOptionGroup;
    }

    private Component buildComboAlteraUsuario() {

        VerticalLayout comboAlteraUsuarioContainer = new VerticalLayout();

        comboAlteraUsuarioContainer.addComponent(new Label("ATENÇÃO ESTA OPÇÃO É APENAS PARA TESTE:"));

        ComboBox comboAlteraUsuarioLogado = new ComboBox("Altere usuário logado:");

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        List<Usuario> lista = em.createNamedQuery("Usuario.findAll")
                .getResultList();

        for (Usuario usuario : lista) {
            comboAlteraUsuarioLogado.addItem(usuario);
            comboAlteraUsuarioLogado.setItemCaption(usuario, usuario.getNome());

        }

        comboAlteraUsuarioLogado.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {

                Usuario usuarioLogado = (Usuario) comboAlteraUsuarioLogado.getValue();
                GestorSession.setAttribute("usuarioLogado", usuarioLogado);
                usuarioLogado.setEmpresaAtiva(new LoginModel().getEmpresaUsuarioLogado());
                getListener().removerFiltrosPesquisa();
            }
        });
        comboAlteraUsuarioContainer.addComponent(comboAlteraUsuarioLogado);
        return comboAlteraUsuarioContainer;

    }

    public MenuBar.MenuItem getCriarNovoMenuItem() {
        return criarNovoMenuItem;
    }

    public MenuBar.MenuItem getCriarViaTemplateMenuItem() {
        return criarViaTemplateMenuItem;
    }

}
