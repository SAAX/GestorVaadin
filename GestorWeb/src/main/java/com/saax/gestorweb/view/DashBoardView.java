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
    private final ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getUserData().getMensagens();
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
        tarefasTable.setWidth("100%");
        tarefasTable.addContainerProperty("Cod", String.class, "");
        tarefasTable.addContainerProperty("Nome", String.class, "");
        tarefasTable.addContainerProperty("Empresa", String.class, "");
        tarefasTable.addContainerProperty("Solicitante", String.class, "");
        tarefasTable.addContainerProperty("Responsável", String.class, "");
        tarefasTable.addContainerProperty("Data Início", String.class, "");
        tarefasTable.addContainerProperty("Data Fim", String.class, "");
        tarefasTable.addContainerProperty("Apontamento", ComboBox.class, "");
        tarefasTable.addContainerProperty("Projeção", Image.class, "");
        tarefasTable.addContainerProperty("Email", Button.class, "");
        tarefasTable.addContainerProperty("Chat", Button.class, "");

        // adiciona 10 tarefas de exemplo
        ComboBox apontamento;

        // tarefa #1
        apontamento = new ComboBox();
        apontamento.addItems("Não Aceito", "Não iniciado", "Iniciado", "25%", "50%", "75%", "Concluído");
        tarefasTable.addItem(new Object[]{
            "T001", "Tarefa 1", "SAAX", "Daniel", "Fernando", "25/03/2015", "26/06/2015", apontamento, null, new Button("E"), new Button("C")}, "T001");

        // Sub 1 da tarefa 1
        apontamento = new ComboBox();
        apontamento.addItems("Não Aceito", "Não iniciado", "Iniciado", "25%", "50%", "75%", "Concluído");
        tarefasTable.addItem(new Object[]{
            "S001", "Sub Tarefa 1 - Tarefa 1", "SAAX", "Daniel", "Fernando", "25/03/2015", "26/06/2015", apontamento, null, new Button("E"), new Button("C")}, "S001");
        tarefasTable.setParent("S001", "T001");

        // Sub 2 da tarefa 1
        apontamento = new ComboBox();
        apontamento.addItems("Não Aceito", "Não iniciado", "Iniciado", "25%", "50%", "75%", "Concluído");
        tarefasTable.addItem(new Object[]{
            "S002", "Sub Tarefa 2 - Tarefa 1", "SAAX", "Daniel", "Fernando", "25/03/2015", "26/06/2015", apontamento, null, new Button("E"), new Button("C")}, "S002");

        tarefasTable.setParent("S002", "T001");

        // Sub 1 da sub 2 da tarefa 1
        apontamento = new ComboBox();
        apontamento.addItems("Não Aceito", "Não iniciado", "Iniciado", "25%", "50%", "75%", "Concluído");
        tarefasTable.addItem(new Object[]{
            "D001", "Detalhe 1 da Sub 2 - Tarefa 1", "SAAX", "Daniel", "Fernando", "25/03/2015", "26/06/2015", apontamento, null, new Button("E"), new Button("C")}, "D001");

        tarefasTable.setParent("D001", "S002");

        // tarefa #2
        apontamento = new ComboBox();
        apontamento.addItems("Não Aceito", "Não iniciado", "Iniciado", "25%", "50%", "75%", "Concluído");
        tarefasTable.addItem(new Object[]{
            "T002", "Tarefa 2", "SAAX", "Daniel", "Fernando", "25/03/2015", "26/06/2015", apontamento, null, new Button("E"), new Button("C")}, "T002");

        // Sub 1 da tarefa 2
        apontamento = new ComboBox();
        apontamento.addItems("Não Aceito", "Não iniciado", "Iniciado", "25%", "50%", "75%", "Concluído");
        tarefasTable.addItem(new Object[]{
            "S003", "Sub Tarefa 1 - Tarefa 2", "SAAX", "Daniel", "Fernando", "25/03/2015", "26/06/2015", apontamento, null, new Button("E"), new Button("C")}, "S003");
        tarefasTable.setParent("S003", "T002");

        // tarefa #3
        apontamento = new ComboBox();
        apontamento.addItems("Não Aceito", "Não iniciado", "Iniciado", "25%", "50%", "75%", "Concluído");
        tarefasTable.addItem(new Object[]{
            "T003", "Tarefa 3", "SAAX", "Daniel", "Fernando", "25/03/2015", "26/06/2015", apontamento, null, new Button("E"), new Button("C")}, "T003");

        // Sub 1 da tarefa 3
        apontamento = new ComboBox();
        apontamento.addItems("Não Aceito", "Não iniciado", "Iniciado", "25%", "50%", "75%", "Concluído");
        tarefasTable.addItem(new Object[]{
            "S004", "Sub Tarefa 1 - Tarefa 3", "SAAX", "Daniel", "Fernando", "25/03/2015", "26/06/2015", apontamento, null, new Button("E"), new Button("C")}, "S004");
        tarefasTable.setParent("S004", "T003");

        // Sub 2 da tarefa 3
        apontamento = new ComboBox();
        apontamento.addItems("Não Aceito", "Não iniciado", "Iniciado", "25%", "50%", "75%", "Concluído");
        tarefasTable.addItem(new Object[]{
            "S005", "Sub Tarefa 2 - Tarefa 3", "SAAX", "Daniel", "Fernando", "25/03/2015", "26/06/2015", apontamento, null, new Button("E"), new Button("C")}, "S005");
        tarefasTable.setParent("S005", "T003");

        tarefasTable.setPageLength(7);

        return tarefasTable;

    }

    private Component buildMenuSuperiorContainer() {

        menuSuperiorContainer = new HorizontalLayout();
        menuSuperiorContainer.setSpacing(true);
        menuSuperiorContainer.setWidth("100%");
        menuSuperiorContainer.setHeight("50px");

        
        menuSuperior = new MenuBar();
        menuSuperior.setHeight("100%");
        menuSuperior.setHtmlContentAllowed(true);

        MenuBar.MenuItem criar = menuSuperior.addItem("<h3>Criar</h3>", null, null);
        MenuBar.MenuItem criarTarefas = criar.addItem("Tarefas/Sub", null, null);
        MenuBar.MenuItem criarMetas = criar.addItem("Metas", null, null);

        MenuBar.MenuItem publicacoes = menuSuperior.addItem("<h3>Publicações</h3>", null, null);

        MenuBar.MenuItem relatorios = menuSuperior.addItem("<h3>Relatórios</h3>", null, null);

        MenuBar.MenuItem config = menuSuperior.addItem("<h3>Config</h3>", null, null);
        MenuBar.MenuItem config1 = config.addItem("Config 1", null, null);
        MenuBar.MenuItem config2 = config.addItem("Config 2", null, null);
        MenuBar.MenuItem config3 = config.addItem("Config 3", null, null);

        menuSuperiorContainer.addComponent(menuSuperior);
        menuSuperiorContainer.setComponentAlignment(menuSuperior, Alignment.MIDDLE_RIGHT);
        
        return menuSuperiorContainer;
    }

    private Component buildFiltrosPesquisaContainer() {

        
        filtrosPesquisaContainer = new HorizontalLayout();
        filtrosPesquisaContainer.setWidth("100%");
        filtrosPesquisaContainer.setHeight(null);

        filtrosPesquisaEsquerdaContainer = new HorizontalLayout();
        filtrosPesquisaEsquerdaContainer.setSizeUndefined();
        
        filtroUsuarioOptionGroup = new OptionGroup();
        filtroUsuarioOptionGroup.setMultiSelect(true);
        filtroUsuarioOptionGroup.addItem("Daniel");
        filtroUsuarioOptionGroup.addItem("Fernando");
        filtroUsuarioOptionGroup.addItem("Rodrigo");
        
        filtroUsuarioButton = new PopupButton("Usuario");
        filtroUsuarioButton.setContent(filtroUsuarioOptionGroup);
        filtrosPesquisaEsquerdaContainer.addComponent(filtroUsuarioButton);
        
        
        filtroEmpresaOptionGroup = new OptionGroup();
        filtroEmpresaOptionGroup.setMultiSelect(true);
        filtroEmpresaOptionGroup.addItem("SAAX");
        filtroEmpresaOptionGroup.addItem("Vale Rio Doce");
        filtroEmpresaOptionGroup.addItem("Coca-Cola");
        
        filtroEmpresaButton = new PopupButton("Empresa");
        filtroEmpresaButton.setContent(filtroEmpresaOptionGroup);
        filtrosPesquisaEsquerdaContainer.addComponent(filtroEmpresaButton);
        
        filtroDataFimButton = new PopupButton("Data Fim");
        filtroDataFimDateField = new InlineDateField();
        filtroDataFimButton.setContent(filtroDataFimDateField);
        filtrosPesquisaEsquerdaContainer.addComponent(filtroDataFimButton);
        
        filtroProjecaoButton = new PopupButton("Projeçao");
        filtroProjecaoButton.setContent(new Label("nao sei o que por aqui"));
        filtrosPesquisaEsquerdaContainer.addComponent(filtroProjecaoButton);

        filtrosPesquisaDireitaContainer = new VerticalLayout();
        filtrosPesquisaDireitaContainer.setSizeUndefined();
        
        filtroPesquisaRapidaTextField = new TextField();
        filtroPesquisaRapidaTextField.setInputPrompt("pesquisar...");
        filtrosPesquisaDireitaContainer.addComponent(filtroPesquisaRapidaTextField);
        
        filtroPesquisaAvancadaButton = new Button("Pesquisa Avançada");
        filtroPesquisaAvancadaButton.setStyleName("link");
        filtrosPesquisaDireitaContainer.addComponent(filtroPesquisaAvancadaButton);

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
        labelDataAtual.setContentMode(ContentMode.HTML);
        
        dataAtualContainer.addComponent(labelDataAtual);

        return dataAtualContainer;
    }

    private Component buildAbasContainer() {
        
        abasContainer = new HorizontalLayout();
        abasContainer.setSpacing(true);
        abasContainer.setWidth("100%");
        abasContainer.setHeight(null);
        
        painelAbas = new TabSheet();
        painelAbas.setWidth("100%");
        painelAbas.setHeight("100%");
        painelAbas.addTab(buildTarefasTable(), "Tarefa");
        painelAbas.addTab(new HorizontalLayout(), "Meta");
        painelAbas.addTab(new HorizontalLayout(), "Publicações");

        abasContainer.addComponent(painelAbas);
        
        return abasContainer;
    }

    private Component buildPaineisRodape() {

        rodapeContainer = new HorizontalLayout();
        rodapeContainer.setSpacing(true);
        rodapeContainer.setWidth("100%");
        rodapeContainer.setHeight(null);
        
        principaisTarefasContainer = new VerticalLayout();
        principaisTarefasContainer.setStyleName("blue");
        rodapeContainer.addComponent(principaisTarefasContainer);
        
        Button tarefaButton;
        for (int i = 0; i < 5; i++) {
            tarefaButton = new Button("Tarefa "+(i+1));
            tarefaButton.setStyleName("v-button-link");
            principaisTarefasContainer.addComponent(tarefaButton);
        }
        
        principaisProjecoesContainer = new VerticalLayout();
        rodapeContainer.addComponent(principaisProjecoesContainer);
        
        Button projecaoButton;
        for (int i = 0; i < 5; i++) {
            projecaoButton = new Button("Projecao "+(i+1));
            projecaoButton.setStyleName("v-button-link");
            principaisProjecoesContainer.addComponent(projecaoButton);
        }
        
        principaisConvitesContainer = new VerticalLayout();
        principaisConvitesContainer.setStyleName("blue");
        rodapeContainer.addComponent(principaisConvitesContainer);

        Button conviteButton;
        for (int i = 0; i < 5; i++) {
            conviteButton = new Button("Convite  "+(i+1));
            conviteButton.setStyleName("v-button-link");
            principaisConvitesContainer.addComponent(conviteButton);
        }
        
        return rodapeContainer;
    }
    
  
}
