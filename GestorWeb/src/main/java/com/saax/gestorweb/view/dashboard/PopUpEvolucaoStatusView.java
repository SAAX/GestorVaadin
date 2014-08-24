package com.saax.gestorweb.view.dashboard;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.datamodel.HistoricoTarefa;
import com.saax.gestorweb.model.datamodel.StatusTarefa;
import com.saax.gestorweb.util.GestorWebImagens;
import com.vaadin.data.Property;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.time.format.DateTimeFormatter;
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
    private Button bloquearTarefaButton;
    private Button historicoTarefaButton;
    private TextArea motivoBloqueioTextArea;
    private Button confirmarBloqueio;
    private Button cancelarBloqueio;
    private Button adiarTarefaButton;
    private Button cancelarTarefaButton;

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
     * Carrega o modo de visualização onde o usuário responsável pode alterar
     * uma tarefa em andamento São apresentados:
     *
     * 1. Um campo de texto opcional para informar o andamento 2. O combo de
     * andamento 3. Botão para alterar o status para: CONCLUIDA 4. Botão para
     * bloquear a tarefa, com campo de texto para o motivo.
     *
     */
    public void apresentaPerfilUsuarioResponsavelTarefaEmAndamento() {

        main.removeAllComponents();

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

        Window subWindow = new Window("Informe o motivo do bloqueio");
        subWindow.setWidth("400px");
        subWindow.setHeight("200px");

        VerticalLayout subContent = new VerticalLayout();
        subContent.setMargin(true);
        subWindow.setContent(subContent);

        motivoBloqueioTextArea = new TextArea();

        subContent.addComponent(motivoBloqueioTextArea);
        motivoBloqueioTextArea.setSizeFull();

        HorizontalLayout barraDeBotoesContainer = new HorizontalLayout();
        barraDeBotoesContainer.setWidth("100%");
        barraDeBotoesContainer.setSpacing(true);

        cancelarBloqueio = new Button("Cancelar Bloqueio", (Button.ClickEvent event) -> {
            subWindow.close();
        });
        barraDeBotoesContainer.addComponent(cancelarBloqueio);
        barraDeBotoesContainer.setComponentAlignment(cancelarBloqueio, Alignment.BOTTOM_LEFT);

        confirmarBloqueio = new Button("Confirmar Bloqueio", (Button.ClickEvent event) -> {
            listener.confirmarBloqueioClicked();
            subWindow.close();
        });
        barraDeBotoesContainer.addComponent(confirmarBloqueio);
        barraDeBotoesContainer.setComponentAlignment(confirmarBloqueio, Alignment.BOTTOM_RIGHT);

        // Center it in the browser window
        subWindow.center();

        // Open it in the UI
        UI.getCurrent().addWindow(subWindow);

    }

    public void selecionaComboAndamento(int andamento) {
        andamentoTarefaCombo.select(andamento);
    }

    public void apresentaHistorico(List<HistoricoTarefa> historico) {

        Window subWindow = new Window("Histórico");
        subWindow.setWidth("400px");
        subWindow.setHeight("200px");

        VerticalLayout subContent = new VerticalLayout();
        subContent.setMargin(true);
        subWindow.setContent(subContent);

        // Put some components in it
        ListSelect historicoList = new ListSelect();

        for (HistoricoTarefa historicoEl : historico) {

            historicoList.addItem(historicoEl);
            historicoList.setItemCaption(historicoEl,
                    historicoEl.getDatahora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                    + " [" + historicoEl.getUsuario().getNome() + "]"
                    + " :: " + historicoEl.getEvento());
        }

        historicoList.setNullSelectionAllowed(true);
        historicoList.setSizeFull();

        subContent.addComponent(historicoList);

        // Center it in the browser window
        subWindow.center();

        // Open it in the UI
        UI.getCurrent().addWindow(subWindow);

    }

    public TextArea getMotivoBloqueioTextArea() {
        return motivoBloqueioTextArea;
    }

    /**
     * Apresenta um pop-up ao usuario solicitante permitindo:   <br>
     * 1. Adiar a tarefa <br>
     * 2. Cancelar a tarefa  <br>
     * 3. Ver histórico 
     */
    public void apresentaPerfilUsuarioSolicitante(int andamento, String motivoBloqueio) {

        main.removeAllComponents();

        main.setSpacing(true);

        // ---------------------------------------------------------------------
        // Campos de andamento da tarefa
        // ---------------------------------------------------------------------
        HorizontalLayout comboAndamentoContainer = new HorizontalLayout();
        comboAndamentoContainer.setSpacing(true);

        comboAndamentoContainer.addComponent(new Label("Tarefa: "));

        comboAndamentoContainer.addComponent(buildAndamentoTarefaCombo());
        comboAndamentoContainer.setEnabled(false);

        comboAndamentoContainer.addComponent(new Label("concluída."));

        main.addComponent(comboAndamentoContainer);

        // ---------------------------------------------------------------------
        // exibe motivo do bloqueio (se estiver bloqueada)
        // ---------------------------------------------------------------------
        if (motivoBloqueio != null) {
            main.addComponent(new Label("<h2>Tarefa BLOQUEADA.</h2>", ContentMode.HTML));
            main.addComponent(new Label("<p>Motivo: " + motivoBloqueio + "</p>", ContentMode.HTML));

        }

        // ---------------------------------------------------------------------
        // Campos para alterar status / Historico
        // ---------------------------------------------------------------------
        HorizontalLayout alterarStatusContainer = new HorizontalLayout();

        alterarStatusContainer.setSpacing(true);

        adiarTarefaButton = new Button("Adiar Tarefa");
        adiarTarefaButton.addClickListener((Button.ClickEvent event) -> {
            listener.adiarTarefaClicked();
        });
        alterarStatusContainer.addComponent(adiarTarefaButton);

        cancelarTarefaButton = new Button("Cancelar Tarefa");
        cancelarTarefaButton.addClickListener((Button.ClickEvent event) -> {
            listener.cancelarTarefaClicked();
        });
        alterarStatusContainer.addComponent(cancelarTarefaButton);

        historicoTarefaButton = new Button("Histórico");
        historicoTarefaButton.addClickListener((Button.ClickEvent event) -> {
            listener.historicoTarefaClicked();
        });
        alterarStatusContainer.addComponent(historicoTarefaButton);

        main.addComponent(alterarStatusContainer);

    }

    
}
