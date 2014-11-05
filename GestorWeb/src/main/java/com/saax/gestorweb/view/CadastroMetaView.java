package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.view.converter.DateToLocalDateConverter;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * Cria a view do cadastro de Metas
 * 
 * @author Rodrigo
 */
public class CadastroMetaView extends Window {

    // Referencia ao recurso das mensagens:
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();

    // A view mantem acesso ao listener (Presenter) para notificar os eventos
    // Este acesso se dá por uma interface para manter a abstração das camadas
    private CadastroMetaViewListener listener;

    // componentes visuais da view (redundancia = on)
    @PropertyId("nome")
    private TextField nomeMetaTextField;

    @PropertyId("dataInicio")
    private PopupDateField dataInicioDateField;

    @PropertyId("dataFim")
    private PopupDateField dataFimDateField;
    
    @PropertyId("dataTermino")
    private PopupDateField dataTerminoDateField;
    
    @PropertyId("descricao")
    private RichTextArea descricaoMeta;
    
    @PropertyId("empresa")
    private ComboBox empresaCombo;

    @PropertyId("usuarioResponsavel")
    private ComboBox responsavelCombo;
    
    @PropertyId("empresaCliente")
    private ComboBox empresaClienteCombo;
    
    @PropertyId("departamento")
    private ComboBox departamentoCombo;

    @PropertyId("centroCusto")
    private ComboBox centroCustoCombo;
    
    @PropertyId("hierarquia")
    private ComboBox hierarquiaCombo;

    private Panel containerTabelaTarefas;
    
    private TreeTable tarefasTable;
    private BeanItem<Meta> metaBeanItem;
    private FieldGroup metaFieldGroup;

    public void setListener(CadastroMetaViewListener listener) {
        this.listener = listener;
    }

    /**
     * Cria o pop-up de login, com campos para usuário e senha
     *
     */
    public CadastroMetaView() {
        super();


        setCaption(mensagens.getString("CadastroMetaView.titulo"));
        setModal(true);
        setWidth(800, Unit.PIXELS);
        setHeight(500, Unit.PIXELS);

        // Container principal, que armazenará todos os demais containeres 
        VerticalLayout containerPrincipal = buildContainerPrincipal();
        setContent(containerPrincipal);

        center();

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
     * Constrói o container principal da view, que terá todos os outros
     * containers dentro Layout: + containerPrincipal (V) + containerCabecalho -
     * empresaCombo - nomeMetaTextField + containerSuperior + accordion
     *
     * @return containerPrincipal
     */
    private VerticalLayout buildContainerPrincipal() {

        VerticalLayout containerPrincipal = new VerticalLayout();
        containerPrincipal.setMargin(true);
        containerPrincipal.setSpacing(true);
        containerPrincipal.setSizeFull(); // ocupar todo espaço disponível

        HorizontalLayout containerCabecalho = new HorizontalLayout();
        containerCabecalho.setSpacing(true);
        containerCabecalho.setWidth("100%");// ocupar todo espaço disponível na largura

        // combo de seleção da empresa
        empresaCombo = new ComboBox("Empresa");
        empresaCombo.addValueChangeListener((Property.ValueChangeEvent event) -> {
            listener.empresaSelecionada((Empresa) event.getProperty().getValue());
        });
        containerCabecalho.addComponent(empresaCombo);
        containerCabecalho.setExpandRatio(empresaCombo, 0);
        

        // TextField: Nome da meta 
        nomeMetaTextField = new TextField("Nome da meta");
        nomeMetaTextField.setWidth("100%");// ocupar todo espaço disponível na largura
        containerCabecalho.addComponent(nomeMetaTextField);
        containerCabecalho.setExpandRatio(nomeMetaTextField, 1);

        containerPrincipal.addComponent(containerCabecalho);

        // O container principal terá uma parte fixa, sempre visivel
        // e outra em abas (accordion)
        // parte superior, sempre visivel do form
        VerticalLayout containerSuperior = buildContainerSuperior();
        containerPrincipal.addComponent(containerSuperior);

        // container do accordion: o accordiion será colocado abaixo do painel superior
        Accordion accordion = buildAccordion();
        containerPrincipal.addComponent(accordion);

        // configuração para que apenas o accordion expanda verticalmente, deixando o painel superior travado
        containerPrincipal.setExpandRatio(containerCabecalho, 0);
        containerPrincipal.setExpandRatio(containerSuperior, 0);
        containerPrincipal.setExpandRatio(accordion, 1);

        return containerPrincipal;
    }

    /**
     * Constrói o container superior com layout abaixo
     *
     * Layout: + containerSuperior (V) + contaierCamposDeData (H) -
     * dataInicioTextField - dataFimTextField - dataTerminoTextField
     *
     * @return containerSuperior
     */
    private VerticalLayout buildContainerSuperior() {

        VerticalLayout containerSuperior = new VerticalLayout();
        containerSuperior.setSizeUndefined();

        // Container para armazenar os dois campos de data (interno ao superior)
        HorizontalLayout contaierCamposDeData = new HorizontalLayout();
        contaierCamposDeData.setSizeFull();
        contaierCamposDeData.setSpacing(true); // coloca um espaçamento entre os elementos internos (30px)
        containerSuperior.addComponent(contaierCamposDeData); // adiciona o container de datas no superior

        // TextField: Data de Inicio 
        dataInicioDateField = new PopupDateField(mensagens.getString("CadastroMetaView.dataInicioTextField.label"));
        dataInicioDateField.setWidth("100%");
        dataInicioDateField.setInputPrompt(mensagens.getString("CadastroMetaView.dataInicioDateField.inputPrompt"));
        dataInicioDateField.setConverter(new DateToLocalDateConverter());
        dataInicioDateField.addValidator(new BeanValidator(Meta.class, "dataInicio"));
        contaierCamposDeData.addComponent(dataInicioDateField);
        
        // TextField: Data Fim
        dataFimDateField = new PopupDateField(mensagens.getString("CadastroMetaView.dataFimTextField.label"));
        dataFimDateField.setWidth("100%");
        dataFimDateField.setInputPrompt(mensagens.getString("CadastroMetaView.dataFimDateField.inputPrompt"));
        dataFimDateField.setConverter(new DateToLocalDateConverter());
        dataFimDateField.addValidator(new BeanValidator(Meta.class, "dataFim"));
        contaierCamposDeData.addComponent(dataFimDateField);

        // TextField: Data Termino
        dataTerminoDateField = new PopupDateField(mensagens.getString("CadastroMetaView.dataTerminoDateField.label"));
        dataTerminoDateField.setWidth("100%");
        dataTerminoDateField.setConverter(new DateToLocalDateConverter());
        contaierCamposDeData.addComponent(dataFimDateField);

        return containerSuperior;
    }

    /**
     * Constrói o container do accordion Layout: + accordion (Abas)
     *
     * + container1aAbaAccordion (H) + containerDetalhes (V) width = 30% -
     * responsavelCombo - empresaClienteCombo - departamentoCombo -
     * centroCustoCombo - horasEstimadasTextField - horasRealizadasTextField +
     * containerDescricaoMeta (H) width = 70% - descricaoMeta +
     * containerTabelaTarefas (Painel) - tarefasTable
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
        descricaoMeta.setNullRepresentation("Informe a descrição da meta");

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
        tarefasTable.addContainerProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaStatus"), String.class, "");
        tarefasTable.setColumnWidth(mensagens.getString("CadastroMetaView.tarefasTable.colunaStatus"), 200);
        tarefasTable.addContainerProperty(mensagens.getString("CadastroMetaView.tarefasTable.colunaProjecao"), Character.class, "");
        tarefasTable.setColumnWidth(mensagens.getString("CadastroMetaView.tarefasTable.colunaProjecao"), 30);
        tarefasTable.addContainerProperty("[E]", Button.class, "");
        tarefasTable.setColumnWidth("[E]", 30);
        tarefasTable.addContainerProperty("[C]", Button.class, "");
        tarefasTable.setColumnWidth("[C]", 30);

        tarefasTable.setPageLength(7);
        tarefasTable.setSelectable(true);
        tarefasTable.setImmediate(true);


        accordion.addTab(containerTabelaTarefas, "Tarefas / Sub", null);

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

    public ComboBox getEmpresaCombo() {
        return empresaCombo;
    }

    public ComboBox getDepartamentoCombo() {
        return departamentoCombo;
    }

    

    
    
}
