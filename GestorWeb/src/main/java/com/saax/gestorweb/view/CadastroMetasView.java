package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.ResourceBundle;

 /**
  * 
  * Cria a view do cadastro de Metas com a estrutura abaixo:
  * 
  * containerPrincipal (V)
  *      - nomeMetaTextField
  *      + containerSuperior (V)
  *          + containerBlocosSuperiores (H) 
  *              + contaierCamposDeData (V)
  *                  - dataInicioTextField
  *                  - dataFimTextField
  *              + containerStatusPrioridade (V)
  *                  - prioridadeComboBox
  *                  - statusComboBox
  *              + containerCamposAviso (H)
  *                  - alertaComboBox
  *                  - tempoAlertaComboBox
  *      + accordion (Abas)
  *         + containerParticipantes (H)
  *             - responsavelCombo
  *             + containerSelecaoParticipantes (V)
  *                 - participantesCombo
  *                 - addParticipanteButton
  *                 - removeParticipanteButton
  *             - listaParticipantes
  *         - descricaoMeta
  *         + containerTabelaTarefas (Painel)
  *             - tarefasTable
  * @author Rodrigo
  */
public class CadastroMetasView extends Window {

    // Referencia ao recurso das mensagens:
    ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getUserData().getMensagens();

    // A view mantem acesso ao listener (Presenter) para notificar os eventos
    // Este acesso se dá por uma interface para manter a abstração das camadas
    private CadastroMetasViewListener listener;

    // componentes visuais da view (redundancia = on)
    private TextField nomeMetaTextField;
    private TextField dataInicioTextField;
    private TextField dataFimTextField;
    private ComboBox prioridadeComboBox;
    private ComboBox statusComboBox;
    private ComboBox alertaComboBox;
    private ComboBox tempoAlertaComboBox;
    private RichTextArea descricaoMeta;
    private ComboBox responsavelCombo;
    private ListSelect listaParticipantes;
    private ComboBox participantesCombo;
    private Button addParticipanteButton;
    private Button removeParticipanteButton;
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

        // TODO setCaption(mensagens.getString("CadastroMetasView.titulo"));
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
     *      + containerPrincipal
     *          - nomeMetaTextField
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
        
        // TextField: Nome da meta 
        nomeMetaTextField = new TextField("Nome da meta");
        nomeMetaTextField.setWidth("100%");
        containerPrincipal.addComponent(nomeMetaTextField);
        
        // O container principal terá uma parte fixa, sempre visivel
        // e outra em abas (accordion)
        // parte superior, sempre visivel do form
        VerticalLayout containerSuperior = buildContainerSuperior();
        containerPrincipal.addComponent(containerSuperior);

        // container do accordion: o accordiion será colocado abaixo do painel superior
        Accordion accordion = buildAccordion();
        containerPrincipal.addComponent(accordion);
        
        // configuração para que apenas o accordion expanda verticalmente, deixando o painel superior travado
        containerPrincipal.setExpandRatio(nomeMetaTextField,0);
        containerPrincipal.setExpandRatio(containerSuperior,0);
        containerPrincipal.setExpandRatio(accordion,1);
        
        return containerPrincipal;
    }
    
    /**
     * Constrói o container superior com layout abaixo
     * 
     * Layout:
     *      + containerSuperior (V)
     *          + containerBlocosSuperiores (H) 
     *              + contaierCamposDeData (V)
     *                  - dataInicioTextField
     *                  - dataFimTextField
     *              + containerStatusPrioridade (V)
     *                  - prioridadeComboBox
     *                  - statusComboBox
     *              + containerCamposAviso (H)
     *                  - alertaComboBox
     *                  - tempoAlertaComboBox
     * @return containerSuperior
     */
    private VerticalLayout buildContainerSuperior(){
        
        VerticalLayout containerSuperior = new VerticalLayout();
        containerSuperior.setSizeUndefined();

        // container horizontal que vai receber todos os blocos de campos do painel superior e vai coloca-los lado a ladao
        HorizontalLayout containerBlocosSuperiores = new HorizontalLayout();
        containerBlocosSuperiores.setSizeUndefined(); // ocupa apenas o espaço necessário para os componentes internos
        containerBlocosSuperiores.setSpacing(true); // coloca um espaçamento entre os elementos internos (30px)
        containerSuperior.addComponent(containerBlocosSuperiores);

        // Container para armazenar os dois campos de data (interno ao superior)
        VerticalLayout contaierCamposDeData = new VerticalLayout();
        contaierCamposDeData.setSizeUndefined(); // ocupa apenas o espaço necessário para os componentes internos
        contaierCamposDeData.setSpacing(true); // coloca um espaçamento entre os elementos internos (30px)
        containerBlocosSuperiores.addComponent(contaierCamposDeData); // adiciona o container de datas no superior

        // TextField: Data de Inicio 
        dataInicioTextField = new TextField("Data Inicio");
        contaierCamposDeData.addComponent(dataInicioTextField);

        dataFimTextField = new TextField("Data Fim");
        contaierCamposDeData.addComponent(dataFimTextField);

        // Container para armazenar os dois combos: de prioridade e status (interno ao superior)
        VerticalLayout containerStatusPrioridade = new VerticalLayout();
        containerStatusPrioridade.setSizeUndefined(); // ocupa apenas o espaço necessário para os componentes internos
        containerStatusPrioridade.setSpacing(true); // coloca um espaçamento entre os elementos internos (30px)
        containerBlocosSuperiores.addComponent(containerStatusPrioridade); // adiciona o container ao superior

        // Combo: Prioridade
        prioridadeComboBox = new ComboBox("Prioridade");
        containerStatusPrioridade.addComponent(prioridadeComboBox);

        statusComboBox = new ComboBox("Status");
        containerStatusPrioridade.addComponent(statusComboBox);

        // Container para armazenar os dois campos: "me avise em" em "tempo" (interno ao superior)
        HorizontalLayout containerCamposAviso = new HorizontalLayout();
        containerCamposAviso.setSizeUndefined(); // ocupa apenas o espaço necessário para os componentes internos
        containerCamposAviso.setSpacing(true); // coloca um espaçamento entre os elementos internos (30px)
        containerBlocosSuperiores.addComponent(containerCamposAviso); // adiciona o container ao superior
        containerBlocosSuperiores.setComponentAlignment(containerCamposAviso, Alignment.BOTTOM_CENTER); // adiciona o container ao superior

        // Combo: "Me avise em"
        alertaComboBox = new ComboBox("Me avise em");
        containerCamposAviso.addComponent(alertaComboBox);

        tempoAlertaComboBox = new ComboBox("");
        containerCamposAviso.addComponent(tempoAlertaComboBox);

        return containerSuperior;
    }

    /**
     * Constrói o container do accordion 
     * Layout:
     *      + accordion
     *         + containerParticipantes (H)
     *         - descricaoMeta
     *         + containerTabelaTarefas
     *             - tarefasTable
     * 
     * @return accordion
     */
    private Accordion buildAccordion(){
        
        Accordion accordion = new Accordion();
        
        // estica o accordion para ocupar todo o espaço restante
        accordion.setSizeFull();

        // 1o. componente colocado no accordion: container com os campos para seleção de participantes e responsavel
        HorizontalLayout containerParticipantes = buildContainerParticipantes();
        accordion.addTab(containerParticipantes, "Responsavel / Participantes", null);
        
        
        // 2o. componente colocado no accordion: descrição da meta
        descricaoMeta = new RichTextArea("");
        accordion.addTab(descricaoMeta, "Descrição da Meta", null);
        descricaoMeta.setSizeFull();

        // 3o. componente colocado no accordion: tabela de tarefas e subs (dentro de um painel para habilitar o scroll )
        containerTabelaTarefas = new Panel();
        containerTabelaTarefas.setSizeFull();
        
        tarefasTable = new Table();
        containerTabelaTarefas.setContent(tarefasTable);
        
        tarefasTable.addContainerProperty("Cod", Integer.class, null);
        tarefasTable.addContainerProperty("Nome", String.class, null);
        tarefasTable.addContainerProperty("Responsável", String.class, null);

        
        tarefasTable.addItem(new Object[] { 1,"Tarefa 1","Joao"}, 1);
        tarefasTable.addItem(new Object[] { 2,"Tarefa 2","Antonio"}, 2);
        
        accordion.addTab(containerTabelaTarefas, "Tarefas / Sub", null);
        
        return accordion;
    }

    /**
     * Layout:
     *     + containerParticipantes (H)
     *         - responsavelCombo
     *         + containerSelecaoParticipantes (V)
     *             - participantesCombo
     *             - addParticipanteButton
     *             - removeParticipanteButton
     *         - listaParticipantes
     * 
     * @return 
     */
    private HorizontalLayout buildContainerParticipantes() {
                HorizontalLayout containerParticipantes = new HorizontalLayout();
        containerParticipantes.setSizeUndefined();
        containerParticipantes.setSpacing(true);
        
        // combo de seleção do responsavel        
        responsavelCombo = new ComboBox("Responsavel");
        containerParticipantes.addComponent(responsavelCombo);

        // container para a lista de seleção de participantes + botões
        VerticalLayout containerSelecaoParticipantes = new VerticalLayout();
        containerSelecaoParticipantes.setSpacing(true);
        containerParticipantes.addComponent(containerSelecaoParticipantes);
        
        // combo de seleção dos participantes
        participantesCombo = new ComboBox("Participantes");
        containerSelecaoParticipantes.addComponent(participantesCombo);
        
        // botão para adicionar participantes da listagen
        addParticipanteButton = new Button(">>");
        containerSelecaoParticipantes.addComponent(addParticipanteButton);
        
        // botão para remover participantes da listagen
        removeParticipanteButton = new Button("<<");
        containerSelecaoParticipantes.addComponent(removeParticipanteButton);
        
        // lista dos participantes selecionados 
        listaParticipantes = new ListSelect("Participantes");
        listaParticipantes.setWidth(100, Unit.PIXELS);
        listaParticipantes.addItem("Joao");
        listaParticipantes.addItem("Antonio");
        listaParticipantes.addItem("Fernando");
        
        containerParticipantes.addComponent(listaParticipantes);
       
        return containerParticipantes;

    }
    

}
