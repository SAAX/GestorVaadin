package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.util.GestorWebImagens;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
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
import java.util.ResourceBundle;
import org.vaadin.hene.popupbutton.PopupButton;

/**
 * Raiz
 *  + menuSuperiorContainer
 *      - menuSuperior
 *  + filtrosPesquisaContainer 
 *      + filtrosPesquisaEsquerdaContainer
 *          + filtroUsuarioButton
 *              - filtroUsuarioOptionGroup
 *          + filtroEmpresaButton
 *              - filtroEmpresaOptionGroup
 *          + filtroDataFimButton
 *              - filtroDataFimDateField
 *          + filtroProjecaoButton
 *              - "não sei o que por aqui"
 *      + filtrosPesquisaDireitaContainer
 *          - filtroPesquisaRapidaTextField
 *          - filtroPesquisaAvancadaButton
 *  + dataAtualContainer
 *      - labelDataAtual
 *  + abasContainer
 *      + painelAbas
 *          - tarefasTable
 *  + abasContainer
 *      - principaisTarefasContainer
 *      - principaisProjecoesContainer
 *      - principaisConvitesContainer
 * 
 * @author Rodrigo
 */
public class DashBoardView extends VerticalLayout {

    // Referencia ao recurso das mensagens:
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getUserData().getMensagens();
    private final GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getUserData().getImagens();

    // A view mantem acesso ao listener (Presenter) para notificar os eventos
    // Este acesso se dá por uma interface para manter a abstração das camadas
    private DashboardViewListenter listener;
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
    private OptionGroup filtroUsuarioOptionGroup;
    private PopupButton filtroEmpresaButton;
    private OptionGroup filtroEmpresaOptionGroup;
    private PopupButton filtroDataFimButton;
    private InlineDateField filtroDataFimDateField;
    private PopupButton filtroProjecaoButton;
    private Button aplicarFiltroPesquisa;
    
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
        

        addComponent(buildMenuSuperiorContainer());
        
        addComponent(buildFiltrosPesquisaContainer());

        addComponent(buildDataAtualContainer());

        addComponent(buildAbasContainer());

        addComponent(buildPaineisRodape());

    }

    private Table buildTarefasTable() {

        tarefasTable = new TreeTable();
        getTarefasTable().setWidth("100%");
        getTarefasTable().addContainerProperty("Cod", String.class, "");
        getTarefasTable().addContainerProperty("Título", String.class, "");
        getTarefasTable().addContainerProperty("Nome", String.class, "");
        getTarefasTable().addContainerProperty("Empresa", String.class, "");
        getTarefasTable().addContainerProperty("Solicitante", String.class, "");
        getTarefasTable().addContainerProperty("Responsável", String.class, "");
        getTarefasTable().addContainerProperty("Data Início", String.class, "");
        getTarefasTable().addContainerProperty("Data Fim", String.class, "");
        getTarefasTable().addContainerProperty("Status", String.class, "");
        getTarefasTable().addContainerProperty("Apontamento", ComboBox.class, "");
        getTarefasTable().addContainerProperty("Projeção", Image.class, "");
        getTarefasTable().addContainerProperty("Email", Button.class, "");
        getTarefasTable().addContainerProperty("Chat", Button.class, "");


        getTarefasTable().setPageLength(7);

        return getTarefasTable();

    }

    private Component buildMenuSuperiorContainer() {

        menuSuperiorContainer = new HorizontalLayout();
        menuSuperiorContainer.setSpacing(true);
        menuSuperiorContainer.setWidth("100%");
        menuSuperiorContainer.setHeight("50px");

        menuSuperior = new MenuBar();
        getMenuSuperior().setHeight("100%");
        getMenuSuperior().setHtmlContentAllowed(true);

        MenuBar.MenuItem criar = getMenuSuperior().addItem("<h3>Criar</h3>", null, null);
        MenuBar.MenuItem criarTarefas = criar.addItem("Tarefas/Sub", null, null);
        MenuBar.MenuItem criarMetas = criar.addItem("Metas", null, null);

        MenuBar.MenuItem publicacoes = getMenuSuperior().addItem("<h3>Publicações</h3>", null, null);

        MenuBar.MenuItem relatorios = getMenuSuperior().addItem("<h3>Relatórios</h3>", null, null);

        MenuBar.MenuItem config = getMenuSuperior().addItem("<h3>Config</h3>", null, null);
        config.addItem("Config 1", null, null);
        config.addItem("Config 2", null, null);
        config.addItem("Config 3", null, null);

        getMenuSuperior().addItem("<h3>Sair</h3>", null, (MenuBar.MenuItem selectedItem) -> {
            listener.logout();
        });
        
        menuSuperiorContainer.addComponent(getMenuSuperior());
        menuSuperiorContainer.setComponentAlignment(getMenuSuperior(), Alignment.MIDDLE_RIGHT);
        
        return menuSuperiorContainer;
    }

    private Component buildFiltrosPesquisaContainer() {

        
        filtrosPesquisaContainer = new HorizontalLayout();
        filtrosPesquisaContainer.setWidth("100%");
        filtrosPesquisaContainer.setHeight(null);

        filtrosPesquisaEsquerdaContainer = new HorizontalLayout();
        filtrosPesquisaEsquerdaContainer.setSizeUndefined();
        
        filtroUsuarioOptionGroup = new OptionGroup();
        getFiltroUsuarioOptionGroup().setMultiSelect(true);
    
        filtroUsuarioButton = new PopupButton("Usuario");
        getFiltroUsuarioButton().setContent(getFiltroUsuarioOptionGroup());
        filtrosPesquisaEsquerdaContainer.addComponent(getFiltroUsuarioButton());
        
        
        filtroEmpresaOptionGroup = new OptionGroup();
        getFiltroEmpresaOptionGroup().setMultiSelect(true);
        getFiltroEmpresaOptionGroup().addItem("SAAX");
        getFiltroEmpresaOptionGroup().addItem("Vale Rio Doce");
        getFiltroEmpresaOptionGroup().addItem("Coca-Cola");
        
        filtroEmpresaButton = new PopupButton("Empresa");
        getFiltroEmpresaButton().setContent(getFiltroEmpresaOptionGroup());
        filtrosPesquisaEsquerdaContainer.addComponent(getFiltroEmpresaButton());
        
        filtroDataFimButton = new PopupButton("Data Fim");
        filtroDataFimDateField = new InlineDateField();
        getFiltroDataFimButton().setContent(getFiltroDataFimDateField());
        filtrosPesquisaEsquerdaContainer.addComponent(getFiltroDataFimButton());
        
        filtroProjecaoButton = new PopupButton("Projeçao");
        getFiltroProjecaoButton().setContent(new Label("nao sei o que por aqui"));
        filtrosPesquisaEsquerdaContainer.addComponent(getFiltroProjecaoButton());

        aplicarFiltroPesquisa = new Button("Aplicar", (Button.ClickEvent event) -> {
            listener.aplicarFiltroPesquisa();
        });
        filtrosPesquisaEsquerdaContainer.addComponent(aplicarFiltroPesquisa);
        
        
        filtrosPesquisaDireitaContainer = new VerticalLayout();
        filtrosPesquisaDireitaContainer.setSizeUndefined();
        
        filtroPesquisaRapidaTextField = new TextField();
        getFiltroPesquisaRapidaTextField().setInputPrompt("pesquisar...");
        filtrosPesquisaDireitaContainer.addComponent(getFiltroPesquisaRapidaTextField());
        
        filtroPesquisaAvancadaButton = new Button("Pesquisa Avançada");
        getFiltroPesquisaAvancadaButton().setStyleName("link");
        filtrosPesquisaDireitaContainer.addComponent(getFiltroPesquisaAvancadaButton());

        filtrosPesquisaContainer.addComponent(filtrosPesquisaEsquerdaContainer);
        filtrosPesquisaContainer.setComponentAlignment(filtrosPesquisaEsquerdaContainer, Alignment.MIDDLE_LEFT);
        filtrosPesquisaContainer.addComponent(filtrosPesquisaDireitaContainer);
        filtrosPesquisaContainer.setComponentAlignment(filtrosPesquisaDireitaContainer, Alignment.MIDDLE_RIGHT);
        
        
        return filtrosPesquisaContainer;

    }

    private Component buildDataAtualContainer() {
        dataAtualContainer = new HorizontalLayout();
        dataAtualContainer.setSpacing(true);
        dataAtualContainer.setWidth("100%");
        dataAtualContainer.setHeight(null);
        
        labelDataAtual = new Label("<h1>Hoje, 27 de maio de 2014.</h1>");
        getLabelDataAtual().setContentMode(ContentMode.HTML);
        
        dataAtualContainer.addComponent(getLabelDataAtual());

        return dataAtualContainer;
    }

    private Component buildAbasContainer() {
        
        abasContainer = new HorizontalLayout();
        abasContainer.setSpacing(true);
        abasContainer.setWidth("100%");
        abasContainer.setHeight(null);
        
        painelAbas = new TabSheet();
        getPainelAbas().setWidth("100%");
        getPainelAbas().setHeight("100%");
        getPainelAbas().addTab(buildTarefasTable(), "Tarefa");
        getPainelAbas().addTab(new HorizontalLayout(), "Meta");
        getPainelAbas().addTab(new HorizontalLayout(), "Publicações");

        abasContainer.addComponent(getPainelAbas());
        
        return abasContainer;
    }

    private Component buildPaineisRodape() {

        rodapeContainer = new HorizontalLayout();
        rodapeContainer.setSpacing(true);
        rodapeContainer.setWidth("100%");
        rodapeContainer.setHeight(null);
        
        principaisTarefasContainer = new VerticalLayout();
        principaisTarefasContainer.setStyleName("blue");
        principaisTarefasContainer.setWidth("20%");
        rodapeContainer.addComponent(principaisTarefasContainer);
        
        Button tarefaButton;
        for (int i = 0; i < 5; i++) {
            tarefaButton = new Button("Tarefa "+(i+1));
            tarefaButton.setStyleName("v-button-link");
            principaisTarefasContainer.addComponent(tarefaButton);
        }
        
        principaisProjecoesContainer = new VerticalLayout();
        principaisProjecoesContainer.setWidth("20%");
        rodapeContainer.addComponent(principaisProjecoesContainer);
        
        Button projecaoButton;
        for (int i = 0; i < 5; i++) {
            projecaoButton = new Button("Projecao "+(i+1));
            projecaoButton.setStyleName("v-button-link");
            principaisProjecoesContainer.addComponent(projecaoButton);
        }
        
        principaisConvitesContainer = new VerticalLayout();
        principaisConvitesContainer.setStyleName("blue");
        principaisConvitesContainer.setWidth("20%");
        rodapeContainer.addComponent(principaisConvitesContainer);

        Button conviteButton;
        for (int i = 0; i < 5; i++) {
            conviteButton = new Button("Convite  "+(i+1));
            conviteButton.setStyleName("v-button-link");
            principaisConvitesContainer.addComponent(conviteButton);
        }
        
        return rodapeContainer;
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
     * @return the filtroUsuarioOptionGroup
     */
    public OptionGroup getFiltroUsuarioOptionGroup() {
        return filtroUsuarioOptionGroup;
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
     * 
     * @return aplicarFiltroPesquisa
     */
    public Button getAplicarFiltroPesquisa() {
        return aplicarFiltroPesquisa;
    }

   
    
    
    
    

    
  
}
