package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.datamodel.CentroCusto;
import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.Participante;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.ErrorUtils;
import com.saax.gestorweb.util.FormatterUtil;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.view.converter.DateToLocalDateConverter;
import com.saax.gestorweb.view.validator.DataFimValidator;
import com.saax.gestorweb.view.validator.DataInicioValidator;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Pop-up Window do cadastro de Metas
 * A visualização será em uma estrutura com:
 *
 * @author rodrigo
 */
public class MetaView  extends Window implements Serializable {

    // Message resource bunlde
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    // Images resource
    private final transient GestorWebImagens images = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();

    // Presenter (listener)
    private MetaViewListener listener;

    // List of required fields
    private final List<AbstractField> requiredFields;

    private boolean editAllowed = true;
    
    // -------------------------------------------------------------------------
    // Top button's bar
    // -------------------------------------------------------------------------
    private HorizontalLayout topButtonsBar;
    private CheckBox templateCheckBox;
    private Button addTaskButton;
    private Button chatButton;
    private Button forecast​Button;

    // -----------------------------------------------------------------------------------
    // Bean Biding
    // -----------------------------------------------------------------------------------
    private BeanItem<Meta> metaBeanItem;
    private FieldGroup metaFieldGroup;

    // -----------------------------------------------------------------------------------
    // Basic fields
    // -----------------------------------------------------------------------------------

    @PropertyId("empresa")
    private ComboBox empresaCombo;

    @PropertyId("nome")
    private TextField nomeMetaTextField;

    @PropertyId("categoria")
    private ComboBox hierarquiaCombo;

    @PropertyId("dataInicio")
    private PopupDateField dataInicioDateField;

    @PropertyId("dataFim")
    private PopupDateField dataFimDateField;
    
    @PropertyId("dataTermino")
    private PopupDateField dataTerminoDateField;
    
    // -----------------------------------------------------------------------------------
    // Informações Adicionais e Descrição
    // -----------------------------------------------------------------------------------
    
    @PropertyId("usuarioResponsavel")
    private ComboBox responsavelCombo;

    private ComboBox participantesCombo;

    private BeanItemContainer<Participante> participantesContainer;
    
    
    @PropertyId("cliente")
    private ComboBox empresaClienteCombo;
    
    @PropertyId("departamento")
    private ComboBox departamentoCombo;

    @PropertyId("centroCusto")
    private ComboBox centroCustoCombo;
    
    @PropertyId("descricao")
    private RichTextArea descricaoMeta;
    
    // -----------------------------------------------------------------------------------
    // Tabela de Tarefas
    // -----------------------------------------------------------------------------------

    private TreeTable tarefasTable;
    private Panel containerTabelaTarefas;
    
    // -------------------------------------------------------------------------
    // Barra de botoes inferior
    // -------------------------------------------------------------------------
    private Button gravarButton;
    private Button cancelarButton;
    private VerticalLayout containerCabecalhoVisivel;
    
    

    /**
     * Cria o pop-up de login, com campos para usuário e senha
     *
     */
    public MetaView() {
        super();
       
        requiredFields = new ArrayList();

        setCaption(mensagens.getString("CadastroMetaView.tituloBase"));
        setModal(true);
        setWidth(1000, Unit.PIXELS);
        setHeight(600, Unit.PIXELS);

        // Container principal, que armazenará todos os demais containeres 
        VerticalLayout containerPrincipal = buildContainerPrincipal();
        containerPrincipal.setSpacing(true);
        setContent(containerPrincipal);

        center();

        setValidatorsVisible(false);
    }
    
    /**
     * Configura o listener de eventos da view
     *
     * @param listener
     */
    public void setListener(MetaViewListener listener) {
        this.listener = listener;
    }


    /**
     * Bind (liga) a meta ao formulário
     *
     * @param meta
     */
    public void setMeta(Meta meta) {

        metaBeanItem = new BeanItem<>(meta);
        metaFieldGroup = new FieldGroup(metaBeanItem);

        metaFieldGroup.bindMemberFields(this);

        
    }
    
    /**
     * Obtem a tarefa ligada (binding) ao form
     *
     * @return
     */
    public Meta getMeta() {
        return metaBeanItem.getBean();
    }

    /**
     * Constrói o container principal da view, que terá todos os outros
     * containers dentro 
     *
     * @return containerPrincipal
     */
    private VerticalLayout buildContainerPrincipal() {

        VerticalLayout containerPrincipal = new VerticalLayout();
        containerPrincipal.setMargin(true);
        containerPrincipal.setSpacing(true);
        containerPrincipal.setSizeFull(); // ocupar todo espaço disponível

        // build and add the "Top Button's Bar" right justifying
        containerPrincipal.addComponent(buildTopButtonsBar());
        containerPrincipal.setComponentAlignment(topButtonsBar, Alignment.MIDDLE_RIGHT);

        containerPrincipal.addComponent(buildContainerCabecalho());

        // container do accordion: o accordiion será colocado abaixo do painel superior
        Accordion accordion = buildAccordion();
        containerPrincipal.addComponent(accordion);

        containerPrincipal.addComponent(buildBarraBotoesInferior());
        
        // configuração para que apenas o accordion expanda verticalmente, deixando o painel superior travado
        containerPrincipal.setExpandRatio(containerCabecalhoVisivel, 0);
        containerPrincipal.setExpandRatio(accordion, 1);

        
        return containerPrincipal;
    }

    /**
     * Build and return the top button's bar with options: <br>
     * <ul>
     *  <li>Templatebox </li>
     *  <li>Add Task </li>
     *  <li>Chat </li>
     *  <li>Forecast​ </li>
     * </ul>
     * @return
     */
    private Component buildTopButtonsBar() {

        topButtonsBar = new HorizontalLayout();
        topButtonsBar.setSpacing(true);
        topButtonsBar.setSizeUndefined();

        templateCheckBox = new CheckBox(mensagens.getString("CadastroMetaView.templateCheckBox.caption"));

        topButtonsBar.addComponent(templateCheckBox);

        addTaskButton = new Button("[Add Task]", (Button.ClickEvent event) -> {
            listener.addTaskButtonClicked();
        });
        topButtonsBar.addComponent(addTaskButton);

        chatButton = new Button("[Chat]", (Button.ClickEvent event) -> {
            listener.chatButtonClicked();
        });
        topButtonsBar.addComponent(chatButton);

//       Projeção será inserida na V2           
//        forecast​Button = new Button("[Projeção]", (Button.ClickEvent event) -> {
//            listener.forecastButtonClickedd();
//        });
//        topButtonsBar.addComponent(forecast​Button);

        return topButtonsBar;
    }
    
    /**
     * Constroi o layout do cabeçalho sempre visivel da view
     * onde vão os campos de informações básicas
     * @return 
     */
    private VerticalLayout buildContainerCabecalho() {

        // layout principal que sera retornado:
        containerCabecalhoVisivel = new VerticalLayout();
        containerCabecalhoVisivel.setWidth("100%");

        // ----------------------------------------------------------------------------------------------------------
        // Linha 1: Empresa e Nome
        // ----------------------------------------------------------------------------------------------------------
        HorizontalLayout containerCabecalhoLinha1 = new HorizontalLayout();
        containerCabecalhoLinha1.setSpacing(true);
        containerCabecalhoLinha1.setWidth("100%");// ocupar todo espaço disponível na largura

        // combo de seleção da empresa
        empresaCombo = new ComboBox(mensagens.getString("CadastroMetaView.empresaCombo.label"));
        empresaCombo.setInputPrompt(mensagens.getString("CadastroMetaView.empresaCombo.inputPrompt"));
        empresaCombo.addValueChangeListener((Property.ValueChangeEvent event) -> {
            listener.empresaSelecionada((Empresa) event.getProperty().getValue());
        });
        empresaCombo.addValidator(new BeanValidator(Meta.class, "empresa"));
        requiredFields.add(empresaCombo);
        
        containerCabecalhoLinha1.addComponent(empresaCombo);
        containerCabecalhoLinha1.setExpandRatio(empresaCombo, 0);
        
        // TextField: Nome da meta 
        nomeMetaTextField = new TextField(mensagens.getString("CadastroMetaView.nomeMetaTextField.caption"));
        nomeMetaTextField.setWidth("100%");// ocupar todo espaço disponível na largura
        nomeMetaTextField.setInputPrompt(mensagens.getString("CadastroMetaView.nomeMetaTextField.inputPrompt"));
        nomeMetaTextField.setNullRepresentation("");
        nomeMetaTextField.addValidator(new BeanValidator(Meta.class, "nome"));
        requiredFields.add(nomeMetaTextField);

        containerCabecalhoLinha1.addComponent(nomeMetaTextField);
        containerCabecalhoLinha1.setExpandRatio(nomeMetaTextField, 1);

        containerCabecalhoVisivel.addComponent(containerCabecalhoLinha1);

        // ----------------------------------------------------------------------------------------------------------
        // Linha 2: Categoria e Datas
        // ----------------------------------------------------------------------------------------------------------

        HorizontalLayout containerCabecalhoLinha2 = new HorizontalLayout();
        containerCabecalhoLinha2.setSizeUndefined();
        containerCabecalhoLinha2.setSpacing(true); // coloca um espaçamento entre os elementos internos (30px)

        // Combo: Categoria
        hierarquiaCombo = new ComboBox(mensagens.getString("CadastroMetaView.hierarquiaCombo.label"));
        hierarquiaCombo.addValidator(new BeanValidator(Meta.class, "categoria"));
        requiredFields.add(hierarquiaCombo);
        containerCabecalhoLinha2.addComponent(hierarquiaCombo);

        
        // TextField: Data de Inicio 
        dataInicioDateField = new PopupDateField(mensagens.getString("CadastroMetaView.dataInicioTextField.label"));
        dataInicioDateField.setInputPrompt(mensagens.getString("CadastroMetaView.dataInicioDateField.inputPrompt"));
        dataInicioDateField.setConverter(new DateToLocalDateConverter());
        dataInicioDateField.addValidator(new BeanValidator(Meta.class, "dataInicio"));
        requiredFields.add(dataInicioDateField);
        containerCabecalhoLinha2.addComponent(dataInicioDateField);
        
        // TextField: Data Fim
        dataFimDateField = new PopupDateField(mensagens.getString("CadastroMetaView.dataFimTextField.label"));
        dataFimDateField.setInputPrompt(mensagens.getString("CadastroMetaView.dataFimDateField.inputPrompt"));
        dataFimDateField.setConverter(new DateToLocalDateConverter());
        containerCabecalhoLinha2.addComponent(dataFimDateField);

        dataInicioDateField.addValidator(new DataInicioValidator(dataFimDateField, "Data Inicio"));
        dataFimDateField.addValidator(new DataFimValidator(dataInicioDateField, "Data Fim"));

        // TextField: Data Termino
        dataTerminoDateField = new PopupDateField(mensagens.getString("CadastroMetaView.dataTerminoDateField.label"));
        dataTerminoDateField.setWidth("100%");
        dataTerminoDateField.setConverter(new DateToLocalDateConverter());
        containerCabecalhoLinha2.addComponent(dataFimDateField);
        
        containerCabecalhoVisivel.addComponent(containerCabecalhoLinha2);

        return containerCabecalhoVisivel;
    }

    /**
     * Constrói o container do accordion Layout
     *
     * @return accordion
     */
    private Accordion buildAccordion() {

        Accordion accordion = new Accordion();

        // estica o accordion para ocupar todo o espaço restante
        accordion.setSizeFull();

        // Na 1a aba do accordion é colocado um container horizontal para guardar 
        // os paineis de detalhes e de descrição da meta
        HorizontalLayout container1aAbaAccordion = new HorizontalLayout();
        container1aAbaAccordion.setMargin(true);
        container1aAbaAccordion.setSpacing(true);
        container1aAbaAccordion.setSizeFull();

        // 1o. componente colocado no container1aAbaAccordion:
        // container com os campos de detalhes
        VerticalLayout containerDetalhes = buildContainerDetalhes();
        containerDetalhes.setSpacing(true);
        containerDetalhes.setWidth("200px");
        container1aAbaAccordion.addComponent(containerDetalhes);
        container1aAbaAccordion.setExpandRatio(containerDetalhes, 0);

        // 2o. componente colocado no container1aAbaAccordion: descrição da meta
        VerticalLayout containerDescricaoMeta = new VerticalLayout();
        containerDescricaoMeta.setSpacing(true);
        containerDescricaoMeta.setSizeFull();

        descricaoMeta = new RichTextArea("");
        descricaoMeta.setSizeFull();
        descricaoMeta.setNullRepresentation("");

        containerDescricaoMeta.addComponent(descricaoMeta);
        container1aAbaAccordion.addComponent(containerDescricaoMeta);
        container1aAbaAccordion.setExpandRatio(containerDescricaoMeta, 1);

        accordion.addTab(container1aAbaAccordion, "Detalhes", null);

        // 2a. Aba do accordion: tabela de tarefas e subs (dentro de um painel para habilitar o scroll )
        containerTabelaTarefas = new Panel();
        containerTabelaTarefas.setSizeFull();

        tarefasTable = new TreeTable();
        containerTabelaTarefas.setContent(tarefasTable);
        tarefasTable.setSizeFull();

        tarefasTable.addContainerProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaCod"), Button.class, "");
        tarefasTable.setColumnWidth(mensagens.getString("CadastroMetaView.tarefasTable.colunaCod"), 70);
        tarefasTable.addContainerProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaTitulo"), Button.class, "");
        tarefasTable.setColumnWidth(mensagens.getString("CadastroMetaView.tarefasTable.colunaTitulo"), 50);
        tarefasTable.addContainerProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaNome"), Button.class, "");
        tarefasTable.setColumnWidth(mensagens.getString("CadastroMetaView.tarefasTable.colunaNome"), 250);
        tarefasTable.addContainerProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaEmpresaFilial"), String.class, "");
        tarefasTable.setColumnWidth(mensagens.getString("CadastroMetaView.tarefasTable.colunaEmpresaFilial"), 200);
        tarefasTable.addContainerProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaSolicitante"), String.class, "");
        tarefasTable.setColumnWidth(mensagens.getString("CadastroMetaView.tarefasTable.colunaSolicitante"), 80);
        tarefasTable.addContainerProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaResponsavel"), String.class, "");
        tarefasTable.setColumnWidth(mensagens.getString("CadastroMetaView.tarefasTable.colunaResponsavel"), 80);
        tarefasTable.addContainerProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaDataInicio"), String.class, "");
        tarefasTable.setColumnWidth(mensagens.getString("CadastroMetaView.tarefasTable.colunaDataInicio"), 80);
        tarefasTable.addContainerProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaDataFim"), String.class, "");
        tarefasTable.setColumnWidth(mensagens.getString("CadastroMetaView.tarefasTable.colunaDataFim"), 80);
        tarefasTable.addContainerProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaProjecao"), Character.class, "");
        tarefasTable.setColumnWidth(mensagens.getString("CadastroMetaView.tarefasTable.colunaProjecao"), 30);
        tarefasTable.addContainerProperty("[E]", Button.class, "");
        tarefasTable.setColumnWidth("[E]", 30);
        tarefasTable.addContainerProperty("[C]", Button.class, "");
        tarefasTable.setColumnWidth("[C]", 30);

        tarefasTable.setPageLength(7);
        tarefasTable.setSelectable(true);
        tarefasTable.setImmediate(true);


        accordion.addTab(containerTabelaTarefas, "Tarefas", null);

        return accordion;
    }

    /**
     * Layout: + containerDetalhes (V) width = 30% - empresaCombo -
     * responsavelCombo - empresaClienteCombo - departamentoCombo -
     * centroCustoCombo - horasEstimadasTextField - horasRealizadasTextField
     *
     * @return
     */
    private VerticalLayout buildContainerDetalhes() {

        VerticalLayout containerDetalhes = new VerticalLayout();

        // combo de seleção do responsavel        
        responsavelCombo = new ComboBox("Responsavel");
        responsavelCombo.setWidth("100%");
        containerDetalhes.addComponent(responsavelCombo);

        // combo de seleção da empresa cliente
        empresaClienteCombo = new ComboBox("Cliente");
        empresaClienteCombo.setWidth("100%");
        containerDetalhes.addComponent(empresaClienteCombo);

        participantesCombo = new ComboBox("Participantes");

        participantesCombo.addValueChangeListener((Property.ValueChangeEvent event) -> {
            listener.adicionarParticipante((Usuario) event.getProperty().getValue());
        });

        participantesContainer = new BeanItemContainer<>(Participante.class);

        Table participantesTable = new Table() {
            @Override
            protected String formatPropertyValue(Object rowId,
                    Object colId, Property property) {
                // Format by property type
                if (property.getType() == Usuario.class) {

                    return ((Usuario) property.getValue()).getNome() + " " + ((Usuario) property.getValue()).getSobrenome();

                }

                return super.formatPropertyValue(rowId, colId, property);
            }
        };

        participantesTable.setContainerDataSource(participantesContainer);

        participantesTable.setColumnWidth("usuarioParticipante", 120);
        participantesTable.setColumnHeader("usuarioParticipante", "Participante");

        participantesTable.setVisibleColumns("usuarioParticipante");

        
        // Adicionar coluna do botão "remover"
        participantesTable.addGeneratedColumn("X", (Table source, final Object itemId, Object columnId) -> {
            Button removeButton = new Button("X");
            removeButton.addClickListener((ClickEvent event) -> {
                listener.removerParticipante((Participante) itemId);
            });

            removeButton.setEnabled(editAllowed);
            return removeButton;
        });

        participantesTable.setSelectable(true);
        participantesTable.setImmediate(true);
        participantesTable.setWidth("100%");
        participantesTable.setPageLength(3);
        
        containerDetalhes.addComponent(participantesCombo);
        containerDetalhes.addComponent(participantesTable);
        
        
        // combo de seleção do departamento
        departamentoCombo = new ComboBox("Departamento");
        departamentoCombo.setWidth("100%");
        containerDetalhes.addComponent(departamentoCombo);

        // combo de seleção do Centro de custo
        centroCustoCombo = new ComboBox("Centro de Custo");
        centroCustoCombo.setWidth("100%");
        containerDetalhes.addComponent(centroCustoCombo);

        // bloco horizontal para as horas estimadas e realizadas
        HorizontalLayout containerHorasEstimadasRealizadas = new HorizontalLayout();
        containerHorasEstimadasRealizadas.setSpacing(true);
        containerHorasEstimadasRealizadas.setWidth("100%");
        containerDetalhes.addComponent(containerHorasEstimadasRealizadas);


        return containerDetalhes;

    }

    /**
     * Constrói e retorna a barra de botões superior
     *
     * @return
     */
    private Component buildBarraBotoesInferior() {

        HorizontalLayout barraBotoesInferior = new HorizontalLayout();
        barraBotoesInferior.setSizeUndefined();
        barraBotoesInferior.setSpacing(true);

        gravarButton = new Button(mensagens.getString("CadastroMetaView.gravarButton.caption"), (Button.ClickEvent event) -> {
            try {
                setValidatorsVisible(true);
                metaFieldGroup.commit();
                listener.gravarButtonClicked();
            } catch (Exception ex) {
                
                ErrorUtils.showComponentErrors(this.metaFieldGroup.getFields());
                Logger.getLogger(TaskView.class.getName()).log(Level.WARNING, null, ex);
                
            }
        });

        barraBotoesInferior.addComponent(gravarButton);

        cancelarButton = new Button(mensagens.getString("CadastroMetaView.cancelarButton.caption"), (Button.ClickEvent event) -> {
            listener.cancelarButtonClicked();
        });
        barraBotoesInferior.addComponent(cancelarButton);

        return barraBotoesInferior;
    }

    /**
     * Carrega o combo de seleção de empresas
     *
     * @param empresas
     */
    public void carregarComboEmpresas(Collection<Empresa> empresas) {
        for (Empresa empresa : empresas) {
            empresaCombo.addItem(empresa.getNome());
        }
    }

    /**
     * Pre selecinada a empresa indicada
     *
     * @param empresa a ser selecinada
     */
    public void selecionarEmpresa(Empresa empresa) {
        empresaCombo.select(empresa.getNome());
    }

    /**
     * Desabilita o combo de seleção de empresas
     */
    public void desabilitarSelecaoEmpresas() {
        empresaCombo.setEnabled(false);
    }

    /**
     * Carrega o combo de seleção de empresas
     *
     * @param departamentos
     */
    public void carregarComboDepartamentos(List<Departamento> departamentos) {
        for (Departamento departamento : departamentos) {
            departamentoCombo.addItem(departamento);
            departamentoCombo.setItemCaption(departamento, departamento.getDepartamento());
        }
    }
    
    /**
     * Carrega o combo de seleção de centro de Custo
     *
     * @param centro de Custos
     */
    public void carregarCentroCusto(List<CentroCusto> centroCustos) {
        for (CentroCusto centroCusto : centroCustos) {
            centroCustoCombo.addItem(centroCusto);
            centroCustoCombo.setItemCaption(centroCusto, centroCusto.getCentroCusto());
        }
    }

    /**
     * Exibe uma mensagem no combo de departamentos
     *
     * @param mensagem
     */
    public void exibirMensagemComboDepartamentos(String mensagem) {
        departamentoCombo.removeAllItems();
        departamentoCombo.addItem(mensagem);
        departamentoCombo.select(mensagem);
    }

    /**
     * Desabilita o combo de seleção de departamentos
     */
    public void desabilitarSelecaoDepartamentos() {
        departamentoCombo.setEnabled(false);
    }

    /**
     * Ocultar ou exibir validações
     *
     * @param visible
     */
    public void setValidatorsVisible(boolean visible) {
        requiredFields.stream().forEach((campo) -> {
            campo.setValidationVisible(visible);
        });
    }

    public void exibeTituloEdicao(Meta metapai) {
        setCaption(mensagens.getString("TaskView.titulo.edicao"));
    }
    

    // -------------------------------------------------------------------------
    // GETTERS AND SETTERS
    // -------------------------------------------------------------------------
    public ComboBox getHierarquiaCombo() {
        return hierarquiaCombo;
    }

    public PopupDateField getDataInicioDateField() {
        return dataInicioDateField;
    }

    public PopupDateField getDataFimDateField() {
        return dataFimDateField;
    }

    public FieldGroup getMetaFieldGroup() {
        return metaFieldGroup;
    }

    public TreeTable getTarefasTable() {
        return tarefasTable;
    }

    public Button getAddTaskButton() {
        return addTaskButton;
    }

    public TextField getNomeMetaTextField() {
        return nomeMetaTextField;
    }

    public ComboBox getEmpresaCombo() {
        return empresaCombo;
    }

    public ComboBox getDepartamentoCombo() {
        return departamentoCombo;
    }
    
    public ComboBox getCentroCustoCombo() {
        return centroCustoCombo;
    }

    public ComboBox getResponsavelCombo() {
        return responsavelCombo;
    }

    public ComboBox getEmpresaClienteCombo() {
        return empresaClienteCombo;
    }

    public ComboBox getParticipantesCombo() {
        return participantesCombo;
    }

    public BeanItemContainer<Participante> getParticipantesContainer() {
        return participantesContainer;
    }

    public void setEditAllowed(boolean editAllowed) {

        this.editAllowed = editAllowed;

        // Header:
        templateCheckBox.setEnabled(editAllowed);
        addTaskButton.setEnabled(editAllowed);

        // Basic Data:
        empresaCombo.setEnabled(editAllowed);
        hierarquiaCombo.setEnabled(editAllowed);
        nomeMetaTextField.setEnabled(editAllowed);
        dataInicioDateField.setEnabled(editAllowed);
        dataFimDateField.setEnabled(editAllowed);
        dataTerminoDateField.setEnabled(editAllowed);

        // Description tab of components and Responsible
        responsavelCombo.setEnabled(editAllowed);
        descricaoMeta.setEnabled(editAllowed);
        participantesCombo.setEnabled(editAllowed);
        empresaClienteCombo.setEnabled(editAllowed);

        // Componentes da Aba Detalhes
        departamentoCombo.setEnabled(editAllowed);
        centroCustoCombo.setEnabled(editAllowed);

    }

            
    
    
    
    

    
    
    
}
