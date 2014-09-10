package com.saax.gestorweb.view.dashboard;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.datamodel.AvaliacaoMetaTarefa;
import com.saax.gestorweb.model.datamodel.HistoricoTarefa;
import com.saax.gestorweb.model.datamodel.StatusTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
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
import java.text.MessageFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * @author rodrigo
 */
public class PopUpEvolucaoStatusView extends CustomComponent {

    // Referencia ao recurso das mensagens e imagens:
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();

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
    private Button removerBloqueioTarefaButton;
    private Button aceitarTarefaButton;
    private Button reabrirTarefaButton;
    private Button reativarTarefaButton;
    private ComboBox avaliarTarefaCombo;
    private TextField comentarioAvaliacaoTextField;

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

    public void apresentaMensagemComStatus(Tarefa tarefa) {

        HorizontalLayout linhaUmContainer = new HorizontalLayout();

        linhaUmContainer.setSpacing(true);

        linhaUmContainer.addComponent(new Label(
                "<h3> "+mensagens.getString("PopUpEvolucaoStatusView.MensagemComStatus.Tarefa.label") + tarefa.getStatus().toString() + "</h3>",
                ContentMode.HTML
        ));

        if (tarefa.getStatus() == StatusTarefa.AVALIADA){
            
            linhaUmContainer.addComponent(new Label(
                    
                    "<h4> " + MessageFormat.format(mensagens.getString("PopUpEvolucaoStatusView.MensagemComStatus.TarefaAvaliada.label"), 
                            tarefa.getAvaliacoes().get(0).getAvaliacao()) + "  </h4>",
                    ContentMode.HTML
            ));
            
        }
        
        linhaUmContainer.addComponent(buildHistoricoTarefaButton());

        main.addComponent(linhaUmContainer);

    }


    /**
     * constroi o botao comum para visualizaçao do historico da tarefa
     * @return 
     */
    private Button buildHistoricoTarefaButton(){
        historicoTarefaButton = new Button(mensagens.getString("PopUpEvolucaoStatusView.historicoTarefaButton.label"));
        historicoTarefaButton.addClickListener((Button.ClickEvent event) -> {
            listener.historicoTarefaClicked();
        });
        return historicoTarefaButton;
    }
    
    private TextField comentarioAndamento;

    /**
     * Carrega o modo de visualização onde o usuário responsável pode alterar
     * uma tarefa em andamento <br>
     * São apresentados: <br>
     * <br>
     * <ol>
     * <li>Um campo de texto opcional para informar o andamento</li>
     * <li>O combo de andamento </li>
     * <li>Botão para alterar o status para: CONCLUIDA </li>
     * <li>Botão para bloquear a tarefa, com campo de texto para o motivo.</li>
     * <li>Botão para listar historico</li>
     * </ol>
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
        comentarioAndamento.setInputPrompt(mensagens.getString("PopUpEvolucaoStatusView.comentarioAndamento.inputPrompt"));

        comboAndamentoContainer.addComponent(comentarioAndamento);

        comboAndamentoContainer.addComponent(buildAndamentoTarefaCombo());

        main.addComponent(comboAndamentoContainer);

        // ---------------------------------------------------------------------
        // Campos para alterar status / Historico
        // ---------------------------------------------------------------------
        HorizontalLayout alterarStatusContainer = new HorizontalLayout();

        alterarStatusContainer.setSpacing(true);

        bloquearTarefaButton = new Button(mensagens.getString("PopUpEvolucaoStatusView.bloquearTarefaButton.label"));
        bloquearTarefaButton.addClickListener((Button.ClickEvent event) -> {
            listener.bloquearTarefaClicked();
        });
        alterarStatusContainer.addComponent(bloquearTarefaButton);

        alterarStatusContainer.addComponent(buildHistoricoTarefaButton());

        main.addComponent(alterarStatusContainer);

    }

    /**
     * Carrega o modo de visualização onde o usuário responsável pode alterar
     * uma tarefa bloqueada <br>
     * São apresentados: <br>
     * <br>
     * <ol>
     * <li>O combo de andamento preenchido e desabilitado</li>
     * <li>Botão para desbloquear a tarefa</li>
     * <li>Botão para listar historico</li>
     * </ol>
     *
     */
    public void apresentaPerfilUsuarioResponsavelTarefaBloqueada(String motivoBloqueio) {

        main.removeAllComponents();

        main.setSpacing(true);

        
        main.addComponent(new Label("<h2>Tarefa BLOQUEADA.</h2>", ContentMode.HTML));
        main.addComponent(new Label("<p>Motivo: " + motivoBloqueio + "</p>", ContentMode.HTML));
        
        // ---------------------------------------------------------------------
        // Campos para alterar status / Historico
        // ---------------------------------------------------------------------
        HorizontalLayout alterarStatusContainer = new HorizontalLayout();

        alterarStatusContainer.setSpacing(true);

        removerBloqueioTarefaButton = new Button("Remover Bloqueio");
        removerBloqueioTarefaButton.addClickListener((Button.ClickEvent event) -> {
            listener.removerBloqueioTarefaClicked();
        });
        alterarStatusContainer.addComponent(removerBloqueioTarefaButton);

        alterarStatusContainer.addComponent(buildHistoricoTarefaButton());

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

        subContent.addComponent(barraDeBotoesContainer);
        
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
     * Carrega o modo de visualização onde o usuário solicitante pode visualizar
     * e alterar o status de uma tarefa<br>
     * São apresentados: <br>
     * <br>
     * <ol>
     * <li>O combo de andamento preenchido e desabilitado</li>
     * <li>Botão para adiar a tarefa</li>
     * <li>Botão para cancelar a tarefa</li>
     * <li>Botão para listar historico</li>
     * </ol>
     *
     * @param andamento
     * @param motivoBloqueio
     */
    public void apresentaPerfilUsuarioSolicitanteTarefaEmAndamento(int andamento, String motivoBloqueio) {

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

    /**
     * Carrega o modo de visualização onde o usuário responsável pode alterar
     * uma tarefa bloqueada <br>
     * São apresentados: <br>
     * <br>
     * <ol>
     * <li>O combo de andamento preenchido e desabilitado</li>
     * <li>Botão para desbloquear a tarefa</li>
     * <li>Botão para listar historico</li>
     * </ol>
     *
     */
    public void apresentaPerfilUsuarioResponsavelTarefaNaoAceita() {

        main.removeAllComponents();

        main.setSpacing(true);

        HorizontalLayout alterarStatusContainer = new HorizontalLayout();

        alterarStatusContainer.setSpacing(true);

        aceitarTarefaButton = new Button("Aceitar Tarefa");
        aceitarTarefaButton.addClickListener((Button.ClickEvent event) -> {
            listener.aceitarTarefaClicked();
        });
        alterarStatusContainer.addComponent(aceitarTarefaButton);

        historicoTarefaButton = new Button("Histórico");
        historicoTarefaButton.addClickListener((Button.ClickEvent event) -> {
            listener.historicoTarefaClicked();
        });
        alterarStatusContainer.addComponent(historicoTarefaButton);

        main.addComponent(alterarStatusContainer);

    }

    /**
     * Carrega o modo de visualização onde o usuário responsável pode reabrir
     * uma tarefa concluida mas que ainda não fora avaliada<br>
     * São apresentados: <br>
     * <br>
     * <ol>
     * <li>Botão para reabrir a tarefa</li>
     * <li>Botão para listar historico</li>
     * </ol>
     *
     */
    public void apresentaPerfilUsuarioResponsavelTarefaConcluida() {
        main.removeAllComponents();

        main.setSpacing(true);

        HorizontalLayout alterarStatusContainer = new HorizontalLayout();

        alterarStatusContainer.setSpacing(true);

        reabrirTarefaButton = new Button("Reabrir Tarefa");
        reabrirTarefaButton.addClickListener((Button.ClickEvent event) -> {
            listener.reabrirTarefaClicked();
        });
        alterarStatusContainer.addComponent(reabrirTarefaButton);

        historicoTarefaButton = new Button("Histórico");
        historicoTarefaButton.addClickListener((Button.ClickEvent event) -> {
            listener.historicoTarefaClicked();
        });
        alterarStatusContainer.addComponent(historicoTarefaButton);

        main.addComponent(alterarStatusContainer);
    }

    /**
     * Carrega o modo de visualização onde o usuário solicitante pode reativar
     * uma tarefa parada (CANCELADA ou ADIADA)<br>
     * São apresentados: <br>
     * <br>
     * <ol>
     * <li>Botão para reativar a tarefa</li>
     * <li>Botão para listar historico</li>
     * </ol>
     *
     */
    public void apresentaPerfilUsuarioSolicitanteTarefaParada() {
        
        main.removeAllComponents();

        main.setSpacing(true);

        HorizontalLayout alterarStatusContainer = new HorizontalLayout();

        alterarStatusContainer.setSpacing(true);

        reativarTarefaButton = new Button("Reativar Tarefa");
        reativarTarefaButton.addClickListener((Button.ClickEvent event) -> {
            listener.reativarTarefaClicked();
        });
        alterarStatusContainer.addComponent(reativarTarefaButton);

        historicoTarefaButton = new Button("Histórico");
        historicoTarefaButton.addClickListener((Button.ClickEvent event) -> {
            listener.historicoTarefaClicked();
        });
        alterarStatusContainer.addComponent(historicoTarefaButton);

        main.addComponent(alterarStatusContainer);
        

    }

    /**
     * Carrega o modo de visualização onde o usuário solicitante pode avaliar ou alterar a avaliação de
     * uma tarefa <br>
     * São apresentados: <br>
     * <br>
     * <ol>
     * <li>Avaliação atual (se existir)</li>
     * <li>Combo com a seleção da avaliação</li>
     * <li>Botão para listar historico</li>
     * </ol>
     *
     * @param avaliacaoTarefa avaliacao atual (se existir)
     */
    public void apresentaPerfilUsuarioSolicitanteTarefaConcluida(AvaliacaoMetaTarefa avaliacaoTarefa) {
        
        main.removeAllComponents();

        main.setSpacing(true);

        HorizontalLayout comboAvaliacaoContainer = new HorizontalLayout();
        comboAvaliacaoContainer.setSpacing(true);

        comentarioAvaliacaoTextField = new TextField();
        
        // 1a. avaliacao
        if (avaliacaoTarefa == null){
            comentarioAvaliacaoTextField.setInputPrompt("Informe a avaliação...");
            
        // Reavaliacao
        } else {
            comentarioAvaliacaoTextField.setValue(avaliacaoTarefa.getComentario());
            
        }

        comboAvaliacaoContainer.addComponent(comentarioAvaliacaoTextField);

        avaliarTarefaCombo = new ComboBox();
            
        avaliarTarefaCombo.addItem(1);
        avaliarTarefaCombo.setItemCaption(1, "Ícone 1 Estrela");

        avaliarTarefaCombo.addItem(2);
        avaliarTarefaCombo.setItemCaption(2, "Ícone 2 Estrelas");

        avaliarTarefaCombo.addItem(3);
        avaliarTarefaCombo.setItemCaption(3, "Ícone 3 Estrelas");

        avaliarTarefaCombo.addItem(4);
        avaliarTarefaCombo.setItemCaption(4, "Ícone 4 Estrelas");

        avaliarTarefaCombo.addItem(5);
        avaliarTarefaCombo.setItemCaption(5, "Ícone 5 Estrelas");

        avaliarTarefaCombo.addValueChangeListener((Property.ValueChangeEvent event) -> {
            listener.processarAvaliacao();
        });
        
        comboAvaliacaoContainer.addComponent(avaliarTarefaCombo);

        // Reavaliacao
        if (avaliacaoTarefa != null){
            avaliarTarefaCombo.select(avaliacaoTarefa.getAvaliacao());
        }

        main.addComponent(comboAvaliacaoContainer);
        
        
        HorizontalLayout alterarStatusContainer = new HorizontalLayout();

        alterarStatusContainer.setSpacing(true);



        historicoTarefaButton = new Button("Histórico");
        historicoTarefaButton.addClickListener((Button.ClickEvent event) -> {
            listener.historicoTarefaClicked();
        });
        alterarStatusContainer.addComponent(historicoTarefaButton);

        main.addComponent(alterarStatusContainer);
    }

    public TextField getComentarioAvaliacaoTextField() {
        return comentarioAvaliacaoTextField;
    }

    public ComboBox getAvaliarTarefaCombo() {
        return avaliarTarefaCombo;
    }
    
    
    /**
     * Carrega o modo de visualização onde o usuário solicitante pode visualizar
     * e alterar o status de uma tarefa<br>
     * São apresentados: <br>
     * <br>
     * <ol>
     * <li>Botão para adiar a tarefa</li>
     * <li>Botão para cancelar a tarefa</li>
     * <li>Botão para listar historico</li>
     * </ol>
     * @param status
     */
    public void apresentaPerfilUsuarioSolicitanteTarefaNaoAceitaOuNaoIniciada(StatusTarefa status) {
        
        main.removeAllComponents();

        main.setSpacing(true);

        // ---------------------------------------------------------------------
        // exibe o status
        // ---------------------------------------------------------------------
        main.addComponent(new Label(
                "<h3> Tarefa: " + status.toString() + "</h3>",
                ContentMode.HTML
        ));

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
