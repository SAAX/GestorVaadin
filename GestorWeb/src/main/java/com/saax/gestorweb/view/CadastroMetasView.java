package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.UsuarioEmpresa;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

 /**
  * 
  * Cria a view do cadastro de Metas com a estrutura abaixo:
  * 
  * containerPrincipal (V)
  *      + containerCabecalho
  *         - empresaCombo
  *         - nomeMetaTextField
  *      + containerSuperior (V)
  *          + contaierCamposDeData (H)
  *              - dataInicioTextField
  *              - dataFimTextField
  *              - dataTerminoTextField
  *      + accordion (Abas)
  *         + container1aAbaAccordion (H)
  *             + containerDetalhes (V) width = 30%
  *                 - responsavelCombo
  *                 - empresaClienteCombo
  *                 - departamentoCombo
  *                 - centroCustoCombo
  *                 - horasEstimadasTextField
  *                 - horasRealizadasTextField
  *             + containerDescricaoMeta (H) width = 70%
  *                 - descricaoMeta
  *         + containerTabelaTarefas (Painel)
  *             - tarefasTable
  * @author Rodrigo
  */
public class CadastroMetasView extends Window {

    // Referencia ao recurso das mensagens:
    transient private ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getUserData().getMensagens();

    // A view mantem acesso ao listener (Presenter) para notificar os eventos
    // Este acesso se dá por uma interface para manter a abstração das camadas
    private CadastroMetasViewListener listener;

    // componentes visuais da view (redundancia = on)
    private TextField nomeMetaTextField;
    private TextField dataInicioTextField;
    private TextField dataFimTextField;
    private TextField dataTerminoTextField;
    private RichTextArea descricaoMeta;
    private ComboBox empresaCombo;
    private ComboBox responsavelCombo;
    private ComboBox empresaClienteCombo;
    private ComboBox departamentoCombo;
    private ComboBox centroCustoCombo;
    private TextField horasEstimadasTextField;
    private TextField horasRealizadasTextField;
    private Panel containerTabelaTarefas;
    private Table tarefasTable;

    public void setListener(CadastroMetasViewListener listener) {
        this.listener = listener;
    }

    /**
     * Cria o pop-up de login, com campos para usuário e senha
     *
     */
    public CadastroMetasView() {
        super();

        setCaption(mensagens.getString("CadastroMetasView.titulo"));
        setModal(true);
        setWidth(800, Unit.PIXELS);
        setHeight(500, Unit.PIXELS);

        // Container principal, que armazenará todos os demais containeres 
        VerticalLayout containerPrincipal = buildContainerPrincipal();
        setContent(containerPrincipal);
      
        center();
        
    }
    
    /**
     * Constrói o container principal da view, que terá todos os outros containers dentro
     * Layout:
     *      + containerPrincipal (V)
     *          + containerCabecalho
     *              - empresaCombo
     *              - nomeMetaTextField
     *          + containerSuperior
     *          + accordion
     *        
     * @return containerPrincipal
     */
    private VerticalLayout buildContainerPrincipal(){
        
        VerticalLayout containerPrincipal = new VerticalLayout();
        containerPrincipal.setMargin(true);
        containerPrincipal.setSpacing(true);
        containerPrincipal.setSizeFull(); // ocupar todo espaço disponível

        
        HorizontalLayout containerCabecalho = new HorizontalLayout();
        containerCabecalho.setSpacing(true);
        containerCabecalho.setWidth("100%");// ocupar todo espaço disponível na largura
        
        // combo de seleção da empresa
        empresaCombo = new ComboBox("Empresa");
        containerCabecalho.addComponent(empresaCombo);
        containerCabecalho.setExpandRatio(empresaCombo,0);
        
        // TextField: Nome da meta 
        nomeMetaTextField = new TextField("Nome da meta");
        nomeMetaTextField.setWidth("100%");// ocupar todo espaço disponível na largura
        containerCabecalho.addComponent(nomeMetaTextField);
        containerCabecalho.setExpandRatio(nomeMetaTextField,1);
        
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
        containerPrincipal.setExpandRatio(containerCabecalho,0);
        containerPrincipal.setExpandRatio(containerSuperior,0);
        containerPrincipal.setExpandRatio(accordion,1);
        
        return containerPrincipal;
    }
    
    /**
     * Constrói o container superior com layout abaixo
     * 
     * Layout:
     *      + containerSuperior (V)
     *          + contaierCamposDeData (H)
     *              - dataInicioTextField
     *              - dataFimTextField
     *              - dataTerminoTextField
     * @return containerSuperior
     */
    private VerticalLayout buildContainerSuperior(){
        
        VerticalLayout containerSuperior = new VerticalLayout();
        containerSuperior.setSizeUndefined();

        // Container para armazenar os dois campos de data (interno ao superior)
        HorizontalLayout contaierCamposDeData = new HorizontalLayout();
        contaierCamposDeData.setSizeFull();
        contaierCamposDeData.setSpacing(true); // coloca um espaçamento entre os elementos internos (30px)
        containerSuperior.addComponent(contaierCamposDeData); // adiciona o container de datas no superior

        // TextField: Data de Inicio 
        dataInicioTextField = new TextField("Data Inicio");
        contaierCamposDeData.addComponent(dataInicioTextField);

        // TextField: Data Fim
        dataFimTextField = new TextField("Data Fim (Previsto)");
        contaierCamposDeData.addComponent(dataFimTextField);

        // TextField: Data Termino
        dataTerminoTextField = new TextField("Data Término (Real)");
        contaierCamposDeData.addComponent(dataTerminoTextField);

        return containerSuperior;
    }

    /**
     * Constrói o container do accordion 
     * Layout:
     *      + accordion (Abas)

     *         + container1aAbaAccordion (H)
     *             + containerDetalhes (V) width = 30%
     *                 - responsavelCombo
     *                 - empresaClienteCombo
     *                 - departamentoCombo
     *                 - centroCustoCombo
     *                 - horasEstimadasTextField
     *                 - horasRealizadasTextField
     *             + containerDescricaoMeta (H) width = 70%
     *                 - descricaoMeta
     *         + containerTabelaTarefas (Painel)
     *             - tarefasTable
     * @return accordion
     */
    private Accordion buildAccordion(){
        
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
        container1aAbaAccordion.setExpandRatio(containerDetalhes,0);
        
        // 2o. componente colocado no container1aAbaAccordion: descrição da meta
        VerticalLayout containerDescricaoMeta = new VerticalLayout();
        containerDescricaoMeta.setSpacing(true);
        containerDescricaoMeta.setSizeFull();
                
        descricaoMeta = new RichTextArea("");
        descricaoMeta.setSizeFull();
        descricaoMeta.setNullRepresentation("Informe a descrição da meta");
        
        containerDescricaoMeta.addComponent(descricaoMeta);
        container1aAbaAccordion.addComponent(containerDescricaoMeta);
        container1aAbaAccordion.setExpandRatio(containerDescricaoMeta,1);
                
        accordion.addTab(container1aAbaAccordion, "Detalhes", null);
        

        // 2a. Aba do accordion: tabela de tarefas e subs (dentro de um painel para habilitar o scroll )
        containerTabelaTarefas = new Panel();
        containerTabelaTarefas.setSizeFull();
        
        tarefasTable = new Table();
        containerTabelaTarefas.setContent(tarefasTable);
        tarefasTable.setSizeFull();
        
        tarefasTable.addContainerProperty("Cod", Integer.class, null);
        tarefasTable.addContainerProperty("Nome", String.class, null);
        tarefasTable.addContainerProperty("Responsável", String.class, null);
        tarefasTable.addContainerProperty("Status", String.class, null);
        tarefasTable.addContainerProperty("Andamento", String.class, null);

        accordion.addTab(containerTabelaTarefas, "Tarefas / Sub", null);
        
        return accordion;
    }

    /**
     * Layout:
     *             + containerDetalhes (V) width = 30%
     *                 - empresaCombo
     *                 - responsavelCombo
     *                 - empresaClienteCombo
     *                 - departamentoCombo
     *                 - centroCustoCombo
     *                 - horasEstimadasTextField
     *                 - horasRealizadasTextField
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
        
        // campo de texto para as horas estimadas
        horasEstimadasTextField = new TextField("H. Estimadas");
        horasEstimadasTextField.setWidth("50%");
        containerHorasEstimadasRealizadas.addComponent(horasEstimadasTextField);
        
        horasRealizadasTextField = new TextField("H. Realizadas");
        horasRealizadasTextField.setWidth("50%");
        containerHorasEstimadasRealizadas.addComponent(horasRealizadasTextField);
        
        
       
        return containerDetalhes;

    }

    /**
     * Carrega o combo de seleção de empresas
     * @param empresas 
     */
    public void carregarComboEmpresas(Collection<Empresa> empresas) {
        for (Empresa empresa : empresas) {
            empresaCombo.addItem(empresa.getNome());
        }
    }

    /**
     * Pre selecinada a empresa indicada 
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
     * @param departamentos  
     */
    public void carregarComboDepartamentos(List<Departamento> departamentos) {
        for (Departamento departamento : departamentos) {
            departamentoCombo.addItem(departamento.getDepartamento());
        }
    }

    /**
     * Exibe uma mensagem no combo de departamentos
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
    

}
