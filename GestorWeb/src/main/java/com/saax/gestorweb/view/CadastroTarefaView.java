package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.datamodel.AnexoTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.util.GestorWebImagens;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
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
    private ComboBox departamentoCombo;
    private ComboBox centroCustoCombo;
    private Upload adicionarAnexoButton;
    private Table anexosAdicionadosTable;
    private TextField custoHoraTextField;
    private TextField imputarHorasTextField;
    private TextField observacaoHorasTextField;
    private Button imputarHorasButton;
    private TextField horasEstimadasTextField;
    private TextField horasRealizadasTextField;

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
        accordion.addTab(buildAbaControleHorasEOrcamento(), mensagens.getString("CadastroTarefaView.AbaControleHorasEOrcamento.titulo"), null);

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
        

        descricaoTarefaTextArea = new RichTextArea();
        descricaoTarefaTextArea.setSizeFull();
        
        responsavelCombo = new ComboBox(mensagens.getString("CadastroTarefaView.responsavelCombo.label"));

        participantesCombo = new ComboBox(mensagens.getString("CadastroTarefaView.participantesCombo.label"));
        
        participantesTable = new Table();
        participantesTable.addContainerProperty(mensagens.getString("CadastroTarefaView.participantesTable.colunaParticipante"), String.class, "");
        participantesTable.setColumnWidth(mensagens.getString("CadastroTarefaView.participantesTable.colunaParticipante"), 70);
        participantesTable.addContainerProperty(mensagens.getString("CadastroTarefaView.participantesTable.colunaBotaoRemover"), Button.class, "");
        participantesTable.setColumnWidth(mensagens.getString("CadastroTarefaView.participantesTable.colunaBotaoRemover"), 20);
        participantesTable.setSelectable(true);
        participantesTable.setImmediate(true);

        
        empresaClienteCombo = new ComboBox(mensagens.getString("CadastroTarefaView.empresaClienteCombo.label"));

        // do layout :
        GridLayout layout = new GridLayout(2, 4);
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setSizeFull();
        
        layout.addComponent(responsavelCombo, 0, 0);
        layout.addComponent(participantesCombo, 0, 1);
        layout.addComponent(participantesTable, 0, 2);
        layout.addComponent(empresaClienteCombo, 0, 3);
        layout.setColumnExpandRatio(1, 1);
        layout.setRowExpandRatio(3, 1);
        
        layout.addComponent(descricaoTarefaTextArea, 1, 0, 1, 3);
        
        return layout;
    }

    private Component buildAbaDetalhes() {
        
        departamentoCombo = new ComboBox("Departamento");

        centroCustoCombo = new ComboBox("Centro de Custo");

        adicionarAnexoButton = new Upload();
        
        anexosAdicionadosTable = new Table();
        anexosAdicionadosTable.addContainerProperty(mensagens.getString("CadastroTarefaView.anexosAdicionadosTable.colunaNome"), String.class, "");
        anexosAdicionadosTable.setColumnWidth(mensagens.getString("CadastroTarefaView.anexosAdicionadosTable.colunaNome"), 100);
        anexosAdicionadosTable.addContainerProperty(mensagens.getString("CadastroTarefaView.anexosAdicionadosTable.colunaArquivo"), String.class, "");
        anexosAdicionadosTable.setColumnWidth(mensagens.getString("CadastroTarefaView.anexosAdicionadosTable.colunaArquivo"), 70);
        anexosAdicionadosTable.addContainerProperty(mensagens.getString("CadastroTarefaView.anexosAdicionadosTable.colunaBotaoRemover"), Button.class, "");
        anexosAdicionadosTable.setColumnWidth(mensagens.getString("CadastroTarefaView.anexosAdicionadosTable.colunaBotaoRemover"), 20);
        anexosAdicionadosTable.setSelectable(true);
        anexosAdicionadosTable.setImmediate(true);
        
        
        // Do layout:
        GridLayout layout = new GridLayout(2, 2);
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.setSizeFull();

        layout.addComponent(departamentoCombo, 0, 0);
        layout.addComponent(centroCustoCombo, 0, 1);
        layout.addComponent(adicionarAnexoButton, 1, 0);
        layout.addComponent(anexosAdicionadosTable, 1, 1);

        return layout;
    }

    private Component buildAbaControleHorasEOrcamento() {

        // Campos do controle de horas
        custoHoraTextField = new TextField(mensagens.getString("CadastroTarefaView.custoHoraTextField.caption"));
        
        imputarHorasTextField = new TextField(mensagens.getString("CadastroTarefaView.imputarHorasTextField.caption"));
        imputarHorasTextField.setInputPrompt(mensagens.getString("CadastroTarefaView.imputarHorasTextField.inputPrompt"));
        
        observacaoHorasTextField = new TextField();
        observacaoHorasTextField.setInputPrompt(mensagens.getString("CadastroTarefaView.observacaoHorasTextField.inputPrompt"));
        
        imputarHorasButton = new Button(mensagens.getString("CadastroTarefaView.imputarHorasButton.caption"));
        
        horasEstimadasTextField = new TextField(mensagens.getString("CadastroTarefaView.horasEstimadasTextField.caption"));
        horasRealizadasTextField = new TextField(mensagens.getString("CadastroTarefaView.horasRealizadasTextField.caption"));
        
        // Do layout:
        HorizontalLayout controleHorasContainer = new HorizontalLayout();
        controleHorasContainer.setSpacing(true);
        controleHorasContainer.addComponent(custoHoraTextField);
        controleHorasContainer.addComponent(imputarHorasTextField);
        controleHorasContainer.addComponent(observacaoHorasTextField);
        controleHorasContainer.addComponent(imputarHorasButton);
        
        HorizontalLayout orcamentoContainer = new HorizontalLayout();
        
        
        
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        layout.addComponent(controleHorasContainer);
        layout.addComponent(orcamentoContainer);
        

        return layout;
    }

    public ComboBox getStatusTarefa() {
        return statusTarefaCombo;
    }

    public ComboBox getTipoRecorrenciaCombo() {
        return tipoRecorrenciaCombo;
    }

    
}
