package com.saax.gestorweb.view.dashboard;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.datamodel.HistoricoTarefa;
import com.saax.gestorweb.model.datamodel.StatusTarefa;
import com.saax.gestorweb.util.GestorWebImagens;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 
 * @author rodrigo
 */
public class PopUpEvolucaoStatusView extends CustomComponent {

    // Referencia ao recurso das mensagens:
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getUserData().getMensagens();
    private final GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getUserData().getImagens();

    // A view mantem acesso ao listener (Presenter) para notificar os eventos
    // Este acesso se dá por uma interface para manter a abstração das camadas
    private PopUpEvolucaoStatusViewListener listener;
    private ComboBox andamentoTarefaCombo;
    private Button tarefaConcluidaButton;
    private Button bloquearTarefaButton;
    private PopupView popUpMotivoBloqueio;
    private Button historicoTarefaButton;
    
    public void setListener(PopUpEvolucaoStatusViewListener listener) {
        this.listener = listener;
    }

    private final VerticalLayout main;

    public PopUpEvolucaoStatusView() {

        main = new VerticalLayout();

        // A layout structure used for composition
        Panel panel = new Panel();
        panel.setContent(main);

        // Set the size as undefined at all levels
        panel.getContent().setSizeUndefined();
        panel.setSizeUndefined();
        setSizeUndefined();

        // The composition root MUST be set
        setCompositionRoot(panel);

    }

    public void apresentaMensagemComStatus(StatusTarefa status) {
        main.addComponent(new Label(status.toString()));
    }

    
    private TextField comentarioAndamento;
    
    /**
     * Carrega o modo de visualização onde o usuário responsável
     * pode alterar uma tarefa em andamento
     * São apresentados:
     * 
     * 1. Um campo de texto opcional para informar o andamento
     * 2. O combo de andamento
     * 3. Botão para alterar o status para: CONCLUIDA 
     * 4. Botão para bloquear a tarefa, com campo de texto para o motivo.
     * 
     */
    public void apresentaPerfilUsuarioResponsavelTarefaEmAndamento() {
    
        main.setSpacing(true);
        
        // ---------------------------------------------------------------------
        // Campos de andamento da tarefa
        // ---------------------------------------------------------------------
        
        
        HorizontalLayout comboAndamentoContainer = new HorizontalLayout();
        comboAndamentoContainer.setSpacing(true);

        comentarioAndamento = new TextField();
        comentarioAndamento.setInputPrompt("Informe o progresso...");
        
        comboAndamentoContainer.addComponent(comentarioAndamento);
                
        comboAndamentoContainer.addComponent(buildAndamentoTarefaCombo());
        
        main.addComponent(comboAndamentoContainer);

        // ---------------------------------------------------------------------
        // Campos para alterar status / Historico
        // ---------------------------------------------------------------------
        
        HorizontalLayout alterarStatusContainer = new HorizontalLayout();
        
        alterarStatusContainer.setSpacing(true);
        
        tarefaConcluidaButton = new Button("Tarefa Conluída");
        tarefaConcluidaButton.addClickListener((Button.ClickEvent event) -> {
            listener.concluirTarefaClicked();
        });
        alterarStatusContainer.addComponent(tarefaConcluidaButton);
        
        bloquearTarefaButton = new Button("Bloquear Tarefa");
        bloquearTarefaButton.addClickListener((Button.ClickEvent event) -> {
            listener.bloquearTarefaClicked();
        });
        alterarStatusContainer.addComponent(bloquearTarefaButton);
        
        historicoTarefaButton = new Button("Histórico");
        historicoTarefaButton.addClickListener((Button.ClickEvent event) -> {
            listener.historicoTarefaClicked();
        });
        alterarStatusContainer.addComponent(historicoTarefaButton);
        
        main.addComponent(alterarStatusContainer);
        
    }

    private ComboBox buildAndamentoTarefaCombo() {

        andamentoTarefaCombo = new ComboBox();

        andamentoTarefaCombo.addItem(0);
        andamentoTarefaCombo.setItemCaption(0, "0%");

        andamentoTarefaCombo.addItem(25);
        andamentoTarefaCombo.setItemCaption(25, "25%");

        andamentoTarefaCombo.addItem(50);
        andamentoTarefaCombo.setItemCaption(50, "50%");

        andamentoTarefaCombo.addItem(75);
        andamentoTarefaCombo.setItemCaption(75, "75%");

        andamentoTarefaCombo.addItem(100);
        andamentoTarefaCombo.setItemCaption(100, "100%");

        andamentoTarefaCombo.setWidth("100px");

        andamentoTarefaCombo.addValueChangeListener((Property.ValueChangeEvent event) -> {
            listener.processarAlteracaoAndamento();
        });

        return andamentoTarefaCombo;

    }

    
    public ComboBox getAndamentoTarefaCombo() {
        return andamentoTarefaCombo;
    }

    public TextField getComentarioAndamento() {
        return comentarioAndamento;
    }


    public void apresentaPopUpMotivoBloqueio() {
        popUpMotivoBloqueio = new PopupView("click", new TextField("Informe o motivo do bloqueio"));
        popUpMotivoBloqueio.setPopupVisible(true);
    }

    public PopupView getPopUpMotivoBloqueio() {
        return popUpMotivoBloqueio;
    }

    public void selecionaComboAndamento(int andamento) {
        andamentoTarefaCombo.select(andamento);
    }

    public void apresentaHistorico(List<HistoricoTarefa> historico) {
         
        Window subWindow = new Window("histórico");

        VerticalLayout subContent = new VerticalLayout();
        subContent.setMargin(true);
        subWindow.setContent(subContent);
        
        // Put some components in it
        subContent.addComponent(new Label("Meatball sub"));
        subContent.addComponent(new Button("Awlright"));
        
        ListSelect sample = new ListSelect();
        
        for (HistoricoTarefa historicoEl : historico) {
            
            sample.addItem(historicoEl);
            sample.setItemCaption(historicoEl, historicoEl.getMomento().toString() + "\t" + historicoEl.getUsuario().getNome() 
                    + "\t" + historicoEl.getDescricao());
        }

        sample.setRows(6); // perfect length in out case
        sample.setNullSelectionAllowed(true); // user can not 'unselect'

        // Center it in the browser window
        subWindow.center();
        
        // Open it in the UI
        UI.getCurrent().addWindow(subWindow);
 
    }

    
    
    
    // Create a dynamically updating content for the popup
    class PopupTextFieldContent implements PopupView.Content {
        private final TextField textField = new TextField("Minimized HTML content", "Click to edit");

        @Override
        public final Component getPopupComponent() {
            return textField;
        }

        @Override
        public final String getMinimizedValueAsHTML() {
            return textField.getValue();
        }
    };    

}
