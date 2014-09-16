package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.util.GestorWebImagens;
import com.vaadin.data.Property;
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
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.ResourceBundle;
import org.vaadin.hene.popupbutton.PopupButton;

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
    private ComboBox prioridadeCombo;
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
    private Table controleHorasTable;
    private TextField imputarOrcamentoTextField;
    private TextField observacaoOrcamentoTextField;
    private Button imputarOrcamentoButton;
    private Table controleOrcamentoTable;
    private Button gravarButton;
    private Button cancelarButton;
    private TreeTable subTarefasTable;
    private PopupButton statusTarefaPopUpButton;
    private HorizontalLayout controleHorasContainer;

    public void setListener(CadastroTarefaViewListener listener) {
        this.listener = listener;
    }

    /**
     * Cria a view
     *
     */
    public CadastroTarefaView() {
        super();

        setCaption(mensagens.getString("CadastroTarefaView.titulo"));
        setModal(true);
        setWidth(1000, Unit.PIXELS);
        setHeight(800, Unit.PIXELS);

        // Container principal, que armazenará todos os demais containeres 
        VerticalLayout containerPrincipal = buildContainerPrincipal();
        containerPrincipal.setSpacing(true);
        setContent(containerPrincipal);

        center();

    }

    
    /**
     * Constroi o container principal que armazenará todos os demais
     * @return 
     */
    private VerticalLayout buildContainerPrincipal() {

        VerticalLayout containerPrincipal = new VerticalLayout();
        containerPrincipal.setMargin(true);
        containerPrincipal.setSpacing(true);
        containerPrincipal.setSizeFull(); 

        // adiciona a barra de botoes superior (botões de chat, adicionar sub, etc)
        containerPrincipal.addComponent(buildBarraBotoesSuperior());
        containerPrincipal.setComponentAlignment(barraBotoesSuperior, Alignment.MIDDLE_RIGHT);

        // cria o acordeon de abas e adiciona as abas
        Accordion accordion = new Accordion();
        accordion.setSizeFull();
        // adiciona a aba de dados iniciais
        accordion.addTab(buildAbaDadosIniciais(), mensagens.getString("CadastroTarefaView.AbaDadosIniciais.titulo"), null);
        // adiciona a aba de descrição e responsáveis
        accordion.addTab(buildAbaDescricaoEResponsaveis(), mensagens.getString("CadastroTarefaView.AbaDescricaoEResponsaveis.titulo"), null);
        // adiciona a aba de detalhes da tarefa
        accordion.addTab(buildAbaDetalhes(), mensagens.getString("CadastroTarefaView.AbaDetalhes.titulo"), null);
        // adiciona a aba opcional de controle de horas
        accordion.addTab(buildAbaControleHoras(), mensagens.getString("CadastroTarefaView.AbaControleHoras.titulo"), null);
        // adiciona a aba opcional de controle de orçamento
        accordion.addTab(buildAbaOrcamento(), mensagens.getString("CadastroTarefaView.AbaOrcamento.titulo"), null);
        // adiciona a aba sub tarefas
        accordion.addTab(buildAbaSubTarefas(), mensagens.getString("CadastroTarefaView.AbaSubTarefas.titulo"), null);

        containerPrincipal.addComponent(accordion);
        containerPrincipal.setExpandRatio(accordion,1);
        
        // adiciona a barra de botoes inferior, com os botões de salvar e cancelas
        Component barraInferior = buildBarraBotoesInferior();
        containerPrincipal.addComponent(barraInferior);
        containerPrincipal.setComponentAlignment(barraInferior, Alignment.MIDDLE_CENTER);
        // containerPrincipal.addComponent(buildSubTarefasTable());
        
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
        
        // Pop-up de status
        statusTarefaPopUpButton = new PopupButton();
        statusTarefaPopUpButton.setWidth("100%");

        // TextField: Data Fim
        dataFimTextField = new TextField(mensagens.getString("CadastroTarefaView.dataFimTextField.label"));
        dataFimTextField.setWidth("100%");

        // Componente de aviso
        avisoButton = new Button(mensagens.getString("CadastroTarefaView.avisoButton.caption"), (Button.ClickEvent event) -> {
            listener.avisoButtonClicked();
        });
        avisoButton.setWidth("100%");
        
        
        // configura o layout usando uma grid
        GridLayout grid = new GridLayout(3, 3);
        grid.setSpacing(true);
        grid.setMargin(true);
        grid.setWidth("100%");
        
        grid.addComponent(empresaCombo);
        grid.addComponent(nomeTarefaTextField, 1, 0, 2, 0);
        grid.addComponent(dataInicioTextField);
        grid.addComponent(tipoRecorrenciaCombo);
        grid.addComponent(prioridadeCombo);
        grid.addComponent(dataFimTextField);
        grid.addComponent(statusTarefaPopUpButton);
        grid.addComponent(avisoButton);

        return grid;
    }

    /**
     * Constrói e retorna a barra de botões superior
     * @return 
     */
    private Component buildBarraBotoesSuperior() {
        
        barraBotoesSuperior = new HorizontalLayout();
        barraBotoesSuperior.setSizeUndefined();
        
        addSubButton = new Button("[Add Sub]", (Button.ClickEvent event) -> {
            listener.addSubButtonClicked();
        });
        barraBotoesSuperior.addComponent(addSubButton);
        
        chatButton = new Button("[Chat]", (Button.ClickEvent event) -> {
            listener.chatButtonClicked();
        });
        barraBotoesSuperior.addComponent(chatButton);
        
        projecaoButton = new Button("[Projeção]", (Button.ClickEvent event) -> {
            listener.projecaoButtonClicked();
        });
        barraBotoesSuperior.addComponent(projecaoButton);
        
        return barraBotoesSuperior;
    }

    /**
     * Constrói e retorna a barra de botões superior
     * @return 
     */
    private Component buildBarraBotoesInferior() {
    
        HorizontalLayout barraBotoesInferior = new HorizontalLayout();
        barraBotoesInferior.setSizeUndefined();
        barraBotoesInferior.setSpacing(true);
        
        gravarButton = new Button(mensagens.getString("CadastroTarefaView.gravarButton.caption"), (Button.ClickEvent event) -> {
            listener.gravarButtonClicked();
        });
        barraBotoesInferior.addComponent(gravarButton);
        
        cancelarButton = new Button(mensagens.getString("CadastroTarefaView.cancelarButton.caption"), (Button.ClickEvent event) -> {
            listener.cancelarButtonClicked();
        });
        barraBotoesInferior.addComponent(cancelarButton);
        
        return barraBotoesInferior;
    }

    /**
     * Constrói e retorna a aba de descrição e responsáveis
     * @return 
     */
    private Component buildAbaDescricaoEResponsaveis() {
        

        descricaoTarefaTextArea = new RichTextArea();
        descricaoTarefaTextArea.setSizeFull();
        
        responsavelCombo = new ComboBox(mensagens.getString("CadastroTarefaView.responsavelCombo.label"));

        participantesCombo = new ComboBox(mensagens.getString("CadastroTarefaView.participantesCombo.label"));
        
        participantesCombo.addValueChangeListener((Property.ValueChangeEvent event) -> {
            listener.addParticipante();
        });
        
        participantesTable = new Table();
        participantesTable.addContainerProperty(mensagens.getString("CadastroTarefaView.participantesTable.colunaParticipante"), String.class, "");
        participantesTable.setColumnWidth(mensagens.getString("CadastroTarefaView.participantesTable.colunaParticipante"), 70);
        participantesTable.addContainerProperty(mensagens.getString("CadastroTarefaView.participantesTable.colunaBotaoRemover"), Button.class, "");
        participantesTable.setColumnWidth(mensagens.getString("CadastroTarefaView.participantesTable.colunaBotaoRemover"), 20);
        participantesTable.setSelectable(true);
        participantesTable.setImmediate(true);
        participantesTable.setWidth("100%");
        participantesTable.setPageLength(5);

        
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
        
        // TODO: Tarefa atual: criando listeners de eventos e passando para o presenter
        // Parei aki!!!
        
        departamentoCombo = new ComboBox(mensagens.getString("CadastroTarefaView.departamentoCombo.caption"));

        centroCustoCombo = new ComboBox(mensagens.getString("CadastroTarefaView.centroCustoCombo.caption"));

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
        anexosAdicionadosTable.setWidth("100%");
        anexosAdicionadosTable.setHeight("100%");
        
        
        // Do layout:
        GridLayout layout = new GridLayout(2, 2);
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.setSizeFull();

        layout.addComponent(departamentoCombo, 0, 0);
        layout.addComponent(centroCustoCombo, 0, 1);
        layout.addComponent(adicionarAnexoButton, 1, 0);
        layout.addComponent(anexosAdicionadosTable, 1, 1);
        layout.setComponentAlignment(anexosAdicionadosTable, Alignment.TOP_RIGHT);

        layout.setRowExpandRatio(0, 0);
        layout.setRowExpandRatio(1, 1);
        
        layout.setColumnExpandRatio(0, 0);
        layout.setColumnExpandRatio(1, 1);
        
        return layout;
    }

    private Component buildAbaControleHoras() {

        // Campos do controle de horas
        custoHoraTextField = new TextField();
        custoHoraTextField.setInputPrompt(mensagens.getString("CadastroTarefaView.custoHoraTextField.inputPrompt"));
        
        imputarHorasTextField = new TextField();
        imputarHorasTextField.setInputPrompt(mensagens.getString("CadastroTarefaView.imputarHorasTextField.inputPrompt"));
        
        observacaoHorasTextField = new TextField();
        observacaoHorasTextField.setInputPrompt(mensagens.getString("CadastroTarefaView.observacaoHorasTextField.inputPrompt"));
        
        imputarHorasButton = new Button(mensagens.getString("CadastroTarefaView.imputarHorasButton.caption"));
        
        controleHorasTable = new Table();
        controleHorasTable.addContainerProperty(mensagens.getString("CadastroTarefaView.controleHorasTable.colunaData"), String.class, "");
        controleHorasTable.setColumnWidth(mensagens.getString("CadastroTarefaView.controleHorasTable.colunaData"), 80);
        controleHorasTable.addContainerProperty(mensagens.getString("CadastroTarefaView.controleHorasTable.colunaObservacoes"), String.class, "");
        controleHorasTable.setColumnWidth(mensagens.getString("CadastroTarefaView.controleHorasTable.colunaObservacoes"), 150);
        controleHorasTable.addContainerProperty(mensagens.getString("CadastroTarefaView.controleHorasTable.colunaCreditoHoras"), String.class, "");
        controleHorasTable.setColumnWidth(mensagens.getString("CadastroTarefaView.controleHorasTable.colunaCreditoHoras"), 80);
        controleHorasTable.addContainerProperty(mensagens.getString("CadastroTarefaView.controleHorasTable.colunaDebitoHoras"), String.class, "");
        controleHorasTable.setColumnWidth(mensagens.getString("CadastroTarefaView.controleHorasTable.colunaDebitoHoras"), 80);
        controleHorasTable.addContainerProperty(mensagens.getString("CadastroTarefaView.controleHorasTable.colunaSaldoHoras"), String.class, "");
        controleHorasTable.setColumnWidth(mensagens.getString("CadastroTarefaView.controleHorasTable.colunaSaldoHoras"), 80);
        controleHorasTable.addContainerProperty(mensagens.getString("CadastroTarefaView.controleHorasTable.colunaCreditoValor"), String.class, "");
        controleHorasTable.setColumnWidth(mensagens.getString("CadastroTarefaView.controleHorasTable.colunaCreditoValor"), 80);
        controleHorasTable.addContainerProperty(mensagens.getString("CadastroTarefaView.controleHorasTable.colunaDebitoValor"), String.class, "");
        controleHorasTable.setColumnWidth(mensagens.getString("CadastroTarefaView.controleHorasTable.colunaDebitoValor"), 80);
        controleHorasTable.addContainerProperty(mensagens.getString("CadastroTarefaView.controleHorasTable.colunaSaldoValor"), String.class, "");
        controleHorasTable.setColumnWidth(mensagens.getString("CadastroTarefaView.controleHorasTable.colunaSaldoValor"), 80);

        controleHorasTable.addContainerProperty(mensagens.getString("CadastroTarefaView.controleHorasTable.colunaBotaoRemover"), Button.class, "");
        controleHorasTable.setColumnWidth(mensagens.getString("CadastroTarefaView.controleHorasTable.colunaBotaoRemover"), 20);
        controleHorasTable.setSelectable(true);
        controleHorasTable.setImmediate(true);
        controleHorasTable.setPageLength(5);
        controleHorasTable.setWidth("100%");

        // Do layout:
        controleHorasContainer = new HorizontalLayout();
        controleHorasContainer.setSpacing(true);
        controleHorasContainer.addComponent(custoHoraTextField);
        controleHorasContainer.addComponent(imputarHorasTextField);
        controleHorasContainer.addComponent(observacaoHorasTextField);
        controleHorasContainer.addComponent(imputarHorasButton);
        
        
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        layout.addComponent(controleHorasContainer);
        layout.addComponent(controleHorasTable);

        return layout;
    }
    
    public void ocultarAbaControleHoras(){
        controleHorasContainer.setVisible(false);
    }
    
    public void revelarAbaControleHoras(){
        controleHorasContainer.setVisible(true);
    }
    
    

    private Component buildAbaOrcamento() {

        // Campos do controle de orçamento
        imputarOrcamentoTextField = new TextField();
        imputarOrcamentoTextField.setInputPrompt(mensagens.getString("CadastroTarefaView.imputarOrcamentoTextField.inputPrompt"));
        
        observacaoOrcamentoTextField = new TextField();
        observacaoOrcamentoTextField.setInputPrompt(mensagens.getString("CadastroTarefaView.observacaoOrcamentoTextField.inputPrompt"));
        
        imputarOrcamentoButton = new Button(mensagens.getString("CadastroTarefaView.imputarOrcamentoButton.caption"));
        
        controleOrcamentoTable = new Table();
        controleOrcamentoTable.addContainerProperty(mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaData"), String.class, "");
        controleOrcamentoTable.setColumnWidth(mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaData"), 80);
        controleOrcamentoTable.addContainerProperty(mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaObservacoes"), String.class, "");
        controleOrcamentoTable.setColumnWidth(mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaObservacoes"), 150);
        controleOrcamentoTable.addContainerProperty(mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaCredito"), String.class, "");
        controleOrcamentoTable.setColumnWidth(mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaCredito"), 80);
        controleOrcamentoTable.addContainerProperty(mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaDebito"), String.class, "");
        controleOrcamentoTable.setColumnWidth(mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaDebito"), 80);
        controleOrcamentoTable.addContainerProperty(mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaSaldo"), String.class, "");
        controleOrcamentoTable.setColumnWidth(mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaSaldo"), 80);

        controleOrcamentoTable.addContainerProperty(mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaBotaoRemover"), Button.class, "");
        controleOrcamentoTable.setColumnWidth(mensagens.getString("CadastroTarefaView.controleOrcamentoTable.colunaBotaoRemover"), 20);
        controleOrcamentoTable.setSelectable(true);
        controleOrcamentoTable.setImmediate(true);
        controleOrcamentoTable.setPageLength(5);
        controleOrcamentoTable.setWidth("100%");
        
        

        // Do layout:
        HorizontalLayout orcamentoContainer = new HorizontalLayout();
        orcamentoContainer.setSpacing(true);
        orcamentoContainer.addComponent(imputarOrcamentoTextField);
        orcamentoContainer.addComponent(observacaoOrcamentoTextField);
        orcamentoContainer.addComponent(imputarOrcamentoButton);
        
        
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        layout.addComponent(orcamentoContainer);
        layout.addComponent(controleOrcamentoTable);
        

        return layout;
    }

    public ComboBox getTipoRecorrenciaCombo() {
        return tipoRecorrenciaCombo;
    }

    private Component buildAbaSubTarefas() {
        
        
        subTarefasTable = new TreeTable();
        subTarefasTable.setWidth("100%");
        subTarefasTable.addContainerProperty("Cod", String.class, "");
        subTarefasTable.setColumnWidth("Cod", 70);
        subTarefasTable.addContainerProperty("Título", String.class, "");
        subTarefasTable.setColumnWidth("Título", 50);
        subTarefasTable.addContainerProperty("Nome", String.class, "");
        subTarefasTable.setColumnWidth("Nome", 250);
        subTarefasTable.addContainerProperty("Empresa/Filial", String.class, "");
        subTarefasTable.setColumnWidth("Empresa/Filial", 200);
        subTarefasTable.addContainerProperty("Solicitante", String.class, "");
        subTarefasTable.setColumnWidth("Solicitante", 80);
        subTarefasTable.addContainerProperty("Responsável", String.class, "");
        subTarefasTable.setColumnWidth("Responsável", 80);
        subTarefasTable.addContainerProperty("Data Início", String.class, "");
        subTarefasTable.setColumnWidth("Data Início", 80);
        subTarefasTable.addContainerProperty("Data Fim", String.class, "");
        subTarefasTable.setColumnWidth("Data Fim", 80);
        subTarefasTable.addContainerProperty("Status", PopupButton.class, "");
        subTarefasTable.setColumnWidth("Status", 200);
        subTarefasTable.addContainerProperty("Projeção", Character.class, "");
        subTarefasTable.setColumnWidth("Proj.", 30);
        subTarefasTable.addContainerProperty("Email", Button.class, "");
        subTarefasTable.setColumnWidth("Email", 30);
        subTarefasTable.addContainerProperty("Chat", Button.class, "");
        subTarefasTable.setColumnWidth("Chat", 30);

        subTarefasTable.setPageLength(4);
        subTarefasTable.setSelectable(true);
        subTarefasTable.setImmediate(true);

        return subTarefasTable;
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
    public CadastroTarefaViewListener getListener() {
        return listener;
    }

    /**
     * @return the nomeTarefaTextField
     */
    public TextField getNomeTarefaTextField() {
        return nomeTarefaTextField;
    }

    /**
     * @return the empresaCombo
     */
    public ComboBox getEmpresaCombo() {
        return empresaCombo;
    }

    /**
     * @return the tituloTarefaTextField
     */
    public TextField getTituloTarefaTextField() {
        return tituloTarefaTextField;
    }

    /**
     * @return the barraBotoesSuperior
     */
    public HorizontalLayout getBarraBotoesSuperior() {
        return barraBotoesSuperior;
    }

    /**
     * @return the addSubButton
     */
    public Button getAddSubButton() {
        return addSubButton;
    }

    /**
     * @return the chatButton
     */
    public Button getChatButton() {
        return chatButton;
    }

    /**
     * @return the projecaoButton
     */
    public Button getProjecaoButton() {
        return projecaoButton;
    }

    /**
     * @return the dataInicioTextField
     */
    public TextField getDataInicioTextField() {
        return dataInicioTextField;
    }

    /**
     * @return the dataFimTextField
     */
    public TextField getDataFimTextField() {
        return dataFimTextField;
    }

    /**
     * @return the prioridadeCombo
     */
    public ComboBox getPrioridadeCombo() {
        return prioridadeCombo;
    }

    /**
     * @return the avisoButton
     */
    public Button getAvisoButton() {
        return avisoButton;
    }

    /**
     * @return the responsavelCombo
     */
    public ComboBox getResponsavelCombo() {
        return responsavelCombo;
    }

    /**
     * @return the participantesCombo
     */
    public ComboBox getParticipantesCombo() {
        return participantesCombo;
    }

    /**
     * @return the participantesTable
     */
    public Table getParticipantesTable() {
        return participantesTable;
    }

    /**
     * @return the empresaClienteCombo
     */
    public ComboBox getEmpresaClienteCombo() {
        return empresaClienteCombo;
    }

    /**
     * @return the descricaoTarefaTextArea
     */
    public RichTextArea getDescricaoTarefaTextArea() {
        return descricaoTarefaTextArea;
    }

    /**
     * @return the departamentoCombo
     */
    public ComboBox getDepartamentoCombo() {
        return departamentoCombo;
    }

    /**
     * @return the centroCustoCombo
     */
    public ComboBox getCentroCustoCombo() {
        return centroCustoCombo;
    }

    /**
     * @return the adicionarAnexoButton
     */
    public Upload getAdicionarAnexoButton() {
        return adicionarAnexoButton;
    }

    /**
     * @return the anexosAdicionadosTable
     */
    public Table getAnexosAdicionadosTable() {
        return anexosAdicionadosTable;
    }

    /**
     * @return the custoHoraTextField
     */
    public TextField getCustoHoraTextField() {
        return custoHoraTextField;
    }

    /**
     * @return the imputarHorasTextField
     */
    public TextField getImputarHorasTextField() {
        return imputarHorasTextField;
    }

    /**
     * @return the observacaoHorasTextField
     */
    public TextField getObservacaoHorasTextField() {
        return observacaoHorasTextField;
    }

    /**
     * @return the imputarHorasButton
     */
    public Button getImputarHorasButton() {
        return imputarHorasButton;
    }

    /**
     * @return the controleHorasTable
     */
    public Table getControleHorasTable() {
        return controleHorasTable;
    }

    /**
     * @return the imputarOrcamentoTextField
     */
    public TextField getImputarOrcamentoTextField() {
        return imputarOrcamentoTextField;
    }

    /**
     * @return the observacaoOrcamentoTextField
     */
    public TextField getObservacaoOrcamentoTextField() {
        return observacaoOrcamentoTextField;
    }

    /**
     * @return the imputarOrcamentoButton
     */
    public Button getImputarOrcamentoButton() {
        return imputarOrcamentoButton;
    }

    /**
     * @return the controleOrcamentoTable
     */
    public Table getControleOrcamentoTable() {
        return controleOrcamentoTable;
    }

    /**
     * @return the gravarButton
     */
    public Button getGravarButton() {
        return gravarButton;
    }

    /**
     * @return the cancelarButton
     */
    public Button getCancelarButton() {
        return cancelarButton;
    }

    /**
     * @return the subTarefasTable
     */
    public TreeTable getSubTarefasTable() {
        return subTarefasTable;
    }

    /**
     * @return the statusTarefaPopUpButton
     */
    public PopupButton getStatusTarefaPopUpButton() {
        return statusTarefaPopUpButton;
    }

    
}
