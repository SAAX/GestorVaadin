package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.util.GestorWebImagens;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.ResourceBundle;
import org.vaadin.hene.popupbutton.PopupButton;

/**
 *
 * @author Rodrigo
 */
public class DashBoardView extends Panel {

    // Referencia ao recurso das mensagens:
    private final ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getUserData().getMensagens();
    private final GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getUserData().getImagens();

    // A view mantem acesso ao listener (Presenter) para notificar os eventos
    // Este acesso se dá por uma interface para manter a abstração das camadas
    private DashboardViewListenter listener;
    private OptionGroup filtroUsuarioOptionGroup;
    private OptionGroup filtroEmpresaOptionGroup;
    private InlineDateField filtroDataFim;

    public void setListener(DashboardViewListenter listener) {
        this.listener = listener;
    }

    // container raiz
    private final VerticalLayout main;

    // barra de botoes suerior
    private HorizontalLayout barraBotoesSuperior;
    private PopupButton criarButton;
    private Button publicacoesButton;
    private Button relatoriosButton;
    private PopupButton configButton;

    // barra com filtros de pesquisa
    private HorizontalLayout barraFiltrosPesquisa;
    private PopupButton usuarioButton;
    private PopupButton empresaButton;
    private PopupButton dataFimButton;
    private PopupButton projecaoButton;
    private TextField pesquisarTextField;
    private Button pesquisaAvancadaButton;

    // barra com data atual
    private HorizontalLayout barraDataAtual;
    private Label dataAtualLabel;

    // paine de abas
    private TabSheet painelAbas;
    private TreeTable tarefasTable;

    // paineis no rodape
    private HorizontalLayout paineisRodape;
    private Table tarefasRodapeTable;
    private Table projecoesRodapeTable;
    private Table convitesRodapeTable;

    public DashBoardView() {

        setSizeFull();

        main = new VerticalLayout();
        main.setSpacing(true);
        main.setSizeFull();

        setContent(main);

        main.addComponent(buildBarraBotoesSuperiorOpcaoA());
        main.addComponent(buildBarraBotoesSuperiorOpcaoB());

        main.addComponent(buildFiltrosPesquisa());

        main.addComponent(buildLabelDataAtual());

        main.addComponent(buildPainelAbas());

        main.addComponent(buildPaineisRodape());

    }

    private Table buildTarefasTable() {

        tarefasTable = new TreeTable();
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

    private Component buildBarraBotoesSuperiorOpcaoA() {

        barraBotoesSuperior = new HorizontalLayout();
        barraBotoesSuperior.setMargin(true);
        barraBotoesSuperior.setSpacing(true);
        barraBotoesSuperior.setWidth("100%");

        HorizontalLayout vazio = new HorizontalLayout();
        barraBotoesSuperior.addComponent(vazio);
        barraBotoesSuperior.setExpandRatio(vazio, 1);
        
        MenuBar menuSuperior = new MenuBar();

        MenuBar.MenuItem criar = menuSuperior.addItem("Criar", null, null);
        MenuBar.MenuItem criarTarefas = criar.addItem("Tarefas/Sub", null, null);
        MenuBar.MenuItem criarMetas = criar.addItem("Metas", null, null);

        MenuBar.MenuItem publicacoes = menuSuperior.addItem("Publicações", null, null);

        MenuBar.MenuItem relatorios = menuSuperior.addItem("Relatórios", null, null);

        MenuBar.MenuItem config = menuSuperior.addItem("Config", null, null);
        MenuBar.MenuItem config1 = config.addItem("Config 1", null, null);
        MenuBar.MenuItem config2 = config.addItem("Config 2", null, null);
        MenuBar.MenuItem config3 = config.addItem("Config 3", null, null);

        barraBotoesSuperior.addComponent(new Label("opção A:"));
        barraBotoesSuperior.addComponent(menuSuperior);

        return barraBotoesSuperior;
    }

    private Component buildBarraBotoesSuperiorOpcaoB() {

        HorizontalLayout barraBotoesSuperiorB = new HorizontalLayout();
        barraBotoesSuperiorB.setMargin(true);
        barraBotoesSuperiorB.setSpacing(true);
        barraBotoesSuperiorB.setWidth("100%");

        HorizontalLayout vazio = new HorizontalLayout();
        barraBotoesSuperiorB.addComponent(vazio);
        barraBotoesSuperiorB.setExpandRatio(vazio, 1);

        barraBotoesSuperiorB.addComponent(new Label("opção B:"));
        criarButton = new PopupButton("Criar");

        Button criarTarefaButton = new Button("Tarefa/Sub");
        criarTarefaButton.setStyleName("link");

        Button criarMetaButton = new Button("Meta");
        criarMetaButton.setStyleName("v-button-link");

        criarButton.setContent(new VerticalLayout(
                criarTarefaButton,
                criarMetaButton
        ));

        barraBotoesSuperiorB.addComponent(criarButton);

        publicacoesButton = new Button("Publicações");
        barraBotoesSuperiorB.addComponent(publicacoesButton);

        relatoriosButton = new Button("Relatórios");
        barraBotoesSuperiorB.addComponent(relatoriosButton);

        Button config1Button = new Button("Config1");
        config1Button.setStyleName("link");

        Button config2Button = new Button("Config2");
        config2Button.setStyleName("link");

        configButton = new PopupButton("Config");
        configButton.setContent(new VerticalLayout(
                config1Button,
                config2Button
        ));
        barraBotoesSuperiorB.addComponent(configButton);

        return barraBotoesSuperiorB;
    }

    private Component buildFiltrosPesquisa() {

        barraFiltrosPesquisa = new HorizontalLayout();
        barraFiltrosPesquisa.setMargin(true);
        barraFiltrosPesquisa.setWidth("100%");

        filtroUsuarioOptionGroup = new OptionGroup();
        filtroUsuarioOptionGroup.setMultiSelect(true);
        filtroUsuarioOptionGroup.addItem("Daniel");
        filtroUsuarioOptionGroup.addItem("Fernando");
        filtroUsuarioOptionGroup.addItem("Rodrigo");
        
        usuarioButton = new PopupButton("Usuario");
        usuarioButton.setContent(filtroUsuarioOptionGroup);
        barraFiltrosPesquisa.addComponent(usuarioButton);
        
        filtroEmpresaOptionGroup = new OptionGroup();
        filtroEmpresaOptionGroup.setMultiSelect(true);
        filtroEmpresaOptionGroup.addItem("SAAX");
        filtroEmpresaOptionGroup.addItem("Vale Rio Doce");
        filtroEmpresaOptionGroup.addItem("Coca-Cola");
        
        empresaButton = new PopupButton("Empresa");
        empresaButton.setContent(filtroEmpresaOptionGroup);
        barraFiltrosPesquisa.addComponent(empresaButton);
        
        dataFimButton = new PopupButton("Data Fim");
        filtroDataFim = new InlineDateField();
        dataFimButton.setContent(filtroDataFim);
        barraFiltrosPesquisa.addComponent(dataFimButton);
        
        projecaoButton = new PopupButton("Projeçao");
        projecaoButton.setContent(new Label("nao sei o que por aqui"));
        barraFiltrosPesquisa.addComponent(projecaoButton);



        HorizontalLayout vazio = new HorizontalLayout();
        barraFiltrosPesquisa.addComponent(vazio);
        barraFiltrosPesquisa.setExpandRatio(vazio, 1);

        VerticalLayout pesquisa = new VerticalLayout();
        
        pesquisarTextField = new TextField();
        pesquisarTextField.setInputPrompt("pesquisar...");
        pesquisa.addComponent(pesquisarTextField);
        
        pesquisaAvancadaButton = new Button();
        pesquisa.addComponent(pesquisaAvancadaButton);

        barraFiltrosPesquisa.addComponent(pesquisa);
        
        return barraFiltrosPesquisa;

    }

    private Component buildLabelDataAtual() {
        barraDataAtual = new HorizontalLayout();
        barraDataAtual.setMargin(true);
        barraDataAtual.setSpacing(true);

        return barraDataAtual;
    }

    private Component buildPainelAbas() {
        
        painelAbas = new TabSheet();
        painelAbas.addTab(buildTarefasTable(), "Tarefa");
        painelAbas.addTab(new HorizontalLayout(), "Meta");
        painelAbas.addTab(new HorizontalLayout(), "Publicações");

        return painelAbas;
    }

    private Component buildPaineisRodape() {

        paineisRodape = new HorizontalLayout();
        paineisRodape.setMargin(true);
        paineisRodape.setSpacing(true);

        return paineisRodape;
    }

}
