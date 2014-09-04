package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.util.GestorWebImagens;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
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
    private VerticalLayout dataContainer;
    private VerticalLayout recorrenciaStatusContainer;
    private VerticalLayout prioridadeContainer;
    private TextField dataInicioTextField;
    private TextField dataFimTextField;

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
        accordion.addTab(buildAbaDadosIniciais(), "Criando uma tarefa", null);
        accordion.addTab(buildAbaDescricaoEResponsaveis(), "Descrição e Responsáveis", null);
        accordion.addTab(buildAbaDetalhes(), "Detalhes", null);

        containerPrincipal.addComponent(accordion);
        
        return containerPrincipal;

    }

    private Component buildAbaDadosIniciais() {

        HorizontalLayout containerCabecalho = new HorizontalLayout();
        containerCabecalho.setSpacing(true);
        containerCabecalho.setWidth("100%");// ocupar todo espaço disponível na largura

        // Combo: Empresa
        empresaCombo = new ComboBox("Empresa");
        containerCabecalho.addComponent(empresaCombo);
        containerCabecalho.setExpandRatio(empresaCombo, 0);

        // TextField: Nome da Tarefa
        nomeTarefaTextField = new TextField("Nome:");
        nomeTarefaTextField.setWidth("100%");// ocupar todo espaço disponível na largura
        containerCabecalho.addComponent(nomeTarefaTextField);
        containerCabecalho.setExpandRatio(nomeTarefaTextField, 1);
        
        
        dataContainer = new VerticalLayout();

        // TextField: Data de Inicio 
        dataInicioTextField = new TextField("Data Inicio");
        dataContainer.addComponent(dataInicioTextField);

        // TextField: Data Fim
        dataFimTextField = new TextField("Data Fim");
        dataContainer.addComponent(dataFimTextField);

        recorrenciaStatusContainer = new VerticalLayout();
        
        

        // Layout
        prioridadeContainer = new VerticalLayout();
        
        HorizontalLayout camposContainer = new HorizontalLayout();
        camposContainer.addComponent(dataContainer);
        camposContainer.addComponent(recorrenciaStatusContainer);
        camposContainer.addComponent(prioridadeContainer);
        
        return containerCabecalho;
    }

    private Component buildBarraBotoes() {
        barraBotoesSuperior = new HorizontalLayout();
        
        addSubButton = new Button("Add Sub");
        barraBotoesSuperior.addComponent(addSubButton);
        
        chatButton = new Button("Chat");
        barraBotoesSuperior.addComponent(chatButton);
        
        projecaoButton = new Button("Projeção");
        barraBotoesSuperior.addComponent(projecaoButton);
        
        return barraBotoesSuperior;
    }

    private Component buildAbaDescricaoEResponsaveis() {
        return new VerticalLayout();
    }

    private Component buildAbaDetalhes() {
        return new VerticalLayout();
    }

}
