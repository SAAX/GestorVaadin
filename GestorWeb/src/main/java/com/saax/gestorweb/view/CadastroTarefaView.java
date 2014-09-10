package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.util.GestorWebImagens;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.ResourceBundle;

/**
 * A visualização será em uma estrutura de accordion com tres abas: 1. Dados
 * inicias da tarefa 2. Descrição e responsáveis 3. Detalhes
 *
 * @author rodrigo
 */
public class CadastroTarefaView extends Window {

    // Referencia ao recurso das mensagens:
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();

    // A view mantem acesso ao listener (Presenter) para notificar os eventos
    // Este acesso se dá por uma interface para manter a abstração das camadas
    private CadastroTarefaViewListener listener;
    private TextField nomeTarefaTextField;
    private ComboBox empresaCombo;
    private TextField tituloTarefaTextField;
    private HorizontalLayout barraBotoesSuperior;
    private Button addSubButton;
    private Button chatButton;
    private Button projecaoButton;
    private TextField dataInicioTextField;
    private TextField dataFimTextField;
    private ComboBox tipoRecorrenciaCombo;
    private ComboBox statusTarefaCombo;
    private ComboBox prioridadeCombo;
    private ComboBox statusCombo;
    private Button avisoButton;
    private ComboBox responsavelCombo;
    private ComboBox participantesCombo;
    private Table participantesTable;
    private ComboBox empresaClienteCombo;
    private RichTextArea descricaoTarefaTextArea;

    public void setListener(CadastroTarefaViewListener listener) {
        this.listener = listener;
    }

    // componentes visuais da view (redundancia = on)
    /**
     * Cria a view
     *
     */
    public CadastroTarefaView() {
        super();

        setCaption("Tarefas");
        setModal(true);
        setWidth(800, Unit.PIXELS);
        setHeight(500, Unit.PIXELS);

        // Container principal, que armazenará todos os demais containeres 
        VerticalLayout containerPrincipal = buildContainerPrincipal();
        containerPrincipal.setSpacing(true);
        setContent(containerPrincipal);

        center();

    }

    private VerticalLayout buildContainerPrincipal() {

        VerticalLayout containerPrincipal = new VerticalLayout();
        containerPrincipal.setMargin(true);
        containerPrincipal.setSpacing(true);
        containerPrincipal.setSizeFull(); // ocupar todo espaço disponível

        containerPrincipal.addComponent(buildBarraBotoes());
        containerPrincipal.setComponentAlignment(barraBotoesSuperior, Alignment.MIDDLE_RIGHT);

        Accordion accordion = new Accordion();
        accordion.setSizeFull();
        accordion.addTab(buildAbaDadosIniciais(), mensagens.getString("CadastroTarefaView.AbaDadosIniciais.titulo"), null);
        accordion.addTab(buildAbaDescricaoEResponsaveis(), mensagens.getString("CadastroTarefaView.AbaDescricaoEResponsaveis.titulo"), null);
        accordion.addTab(buildAbaDetalhes(), mensagens.getString("CadastroTarefaView.AbaDetalhes.titulo"), null);

        containerPrincipal.addComponent(accordion);
        containerPrincipal.setExpandRatio(accordion,1);
        
        return containerPrincipal;

    }

    /**
     * Constroi a aba de dados inicias:
     * @return 
     */
    private Component buildAbaDadosIniciais() {

        
        // Combo: Empresa
        empresaCombo = new ComboBox(mensagens.getString("CadastroTarefaView.empresaCombo.label"));
        empresaCombo.setWidth("100%");

        // TextField: Nome da Tarefa
        nomeTarefaTextField = new TextField(mensagens.getString("CadastroTarefaView.nomeTarefaTextField.label"));
        nomeTarefaTextField.setWidth("100%");
   
        // TextField: Data de Inicio 
        dataInicioTextField = new TextField(mensagens.getString("CadastroTarefaView.dataInicioTextField.label"));
        dataInicioTextField.setWidth("100%");
        
        // Combo Recorrencia
        tipoRecorrenciaCombo = new ComboBox(mensagens.getString("CadastroTarefaView.tipoRecorrenciaCombo.label"));
        tipoRecorrenciaCombo.setWidth("100%");

        // Combo Prioridade
        prioridadeCombo = new ComboBox(mensagens.getString("CadastroTarefaView.prioridadeCombo.label"));
        prioridadeCombo.setWidth("100%");
        
        // Combo Status
        statusTarefaCombo = new ComboBox(mensagens.getString("CadastroTarefaView.statusTarefaCombo.label"));
        statusTarefaCombo.setWidth("100%");

        // TextField: Data Fim
        dataFimTextField = new TextField(mensagens.getString("CadastroTarefaView.dataFimTextField.label"));
        dataFimTextField.setWidth("100%");

        // Componente de aviso
        avisoButton = new Button("Me avise em...");
        avisoButton.setWidth("100%");
        
        
        // grid para os campos: Data Inicio, Data Fim, Recorrencia, Status, Prioridade, Alerta
        GridLayout grid = new GridLayout(3, 3);
        grid.setWidth("100%");
        grid.setSpacing(true);
        
        grid.addComponent(empresaCombo);
        grid.addComponent(nomeTarefaTextField, 1, 0, 2, 0);
        grid.addComponent(dataInicioTextField);
        grid.addComponent(tipoRecorrenciaCombo);
        grid.addComponent(prioridadeCombo);
        grid.addComponent(dataFimTextField);
        grid.addComponent(statusTarefaCombo);
        grid.addComponent(avisoButton);

        return grid;
    }

    private Component buildBarraBotoes() {
        barraBotoesSuperior = new HorizontalLayout();
        barraBotoesSuperior.setSizeUndefined();
        
        addSubButton = new Button("Add Sub");
        barraBotoesSuperior.addComponent(addSubButton);
        
        chatButton = new Button("Chat");
        barraBotoesSuperior.addComponent(chatButton);
        
        projecaoButton = new Button("Projeção");
        barraBotoesSuperior.addComponent(projecaoButton);
        
        return barraBotoesSuperior;
    }

    private Component buildAbaDescricaoEResponsaveis() {
        
        GridLayout layout = new GridLayout(2, 4);
        layout.setSpacing(true);
        layout.setSizeFull();

        descricaoTarefaTextArea = new RichTextArea();
        descricaoTarefaTextArea.setSizeFull();
        layout.addComponent(descricaoTarefaTextArea, 1, 0, 1, 3);
        
        responsavelCombo = new ComboBox(mensagens.getString("CadastroTarefaView.responsavelCombo.label"));
        layout.addComponent(responsavelCombo, 0, 0);

        participantesCombo = new ComboBox(mensagens.getString("CadastroTarefaView.participantesCombo.label"));
        layout.addComponent(participantesCombo, 0, 1);
        
        participantesTable = new Table();
        layout.addComponent(participantesTable, 0, 2);
        
        empresaClienteCombo = new ComboBox(mensagens.getString("CadastroTarefaView.empresaClienteCombo.label"));
        layout.addComponent(empresaClienteCombo, 0, 3);

        layout.setColumnExpandRatio(1, 1);
        layout.setRowExpandRatio(3, 1);
        
        return layout;
    }

    private Component buildAbaDetalhes() {
        return new VerticalLayout();
    }

    public ComboBox getStatusTarefa() {
        return statusTarefaCombo;
    }

    public ComboBox getTipoRecorrenciaCombo() {
        return tipoRecorrenciaCombo;
    }

    
}
