package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.datamodel.ApontamentoTarefa;
import com.saax.gestorweb.model.datamodel.AvaliacaoMetaTarefa;
import com.saax.gestorweb.model.datamodel.HistoricoTarefa;
import com.saax.gestorweb.model.datamodel.ParametroAndamentoTarefa;
import com.saax.gestorweb.model.datamodel.StatusTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.presenter.PresenterUtils;
import com.saax.gestorweb.util.FormatterUtil;
import com.saax.gestorweb.util.GestorWebImagens;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import org.vaadin.teemu.ratingstars.RatingStars;

/**
 *
 * @author rodrigo
 */
public class PopUpStatusView extends CustomComponent {

    // Referencia ao recurso das mensagens e imagens:
    private final transient ResourceBundle mensagens = PresenterUtils.getInstance().getMensagensResource();

    // A view mantem acesso ao listener (Presenter) para notificar os eventos
    // Este acesso se dá por uma interface para manter a abstração das camadas
    private PopUpStatusViewListener listener;
    private ComboBox andamentoTarefaCombo;
    private Button bloquearTarefaButton;
    private Button recusarTarefaButton;
    private Button historicoTarefaButton;
    private TextArea motivoBloqueioTextArea;
    private Button confirmarBloqueio;
    private Button cancelarBloqueio;
    private TextArea motivoRecusaTextArea;
    private Button confirmarRecusa;
    private Button cancelarRecusa;
    private Button removerRecusaTarefaButton;
    private Button adiarTarefaButton;
    private Button cancelarTarefaButton;
    private Button removerBloqueioTarefaButton;
    private Button aceitarTarefaButton;
    private Button reabrirTarefaButton;
    private Button reativarTarefaButton;
    private TextField comentarioAvaliacaoTextField;
    private Table historicoTable;
    private BeanItemContainer<HistoricoTarefa> historicoContainer;
    private TextArea comentarioTextArea;
    private Button confirmarAvaliacaoButton;
    private Button confirmarAlteracaoHistorico;
    private Button cancelarAlteracaoHistorico;
    private RatingStars avaliarTarefaRatingStars;

    public void setListener(PopUpStatusViewListener listener) {
        this.listener = listener;
    }

    private final VerticalLayout main;

    public PopUpStatusView() {

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
     * @param motivoBloqueio
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
    public void apresentaPerfilUsuarioResponsavelTarefaRecusada(String motivoBloqueio) {

        main.removeAllComponents();

        main.setSpacing(true);

        
        main.addComponent(new Label("<h2>Tarefa RECUSADA.</h2>", ContentMode.HTML));
        main.addComponent(new Label("<p>Motivo: " + motivoBloqueio + "</p>", ContentMode.HTML));
        
        // ---------------------------------------------------------------------
        // Campos para alterar status / Historico
        // ---------------------------------------------------------------------
        HorizontalLayout alterarStatusContainer = new HorizontalLayout();

        alterarStatusContainer.setSpacing(true);

        removerRecusaTarefaButton = new Button("Remover Recusa");
        removerRecusaTarefaButton.addClickListener((Button.ClickEvent event) -> {
            listener.removerRecusaTarefaClicked();
        });
        alterarStatusContainer.addComponent(removerRecusaTarefaButton);

        alterarStatusContainer.addComponent(buildHistoricoTarefaButton());

        main.addComponent(alterarStatusContainer);

    }

    private ComboBox buildAndamentoTarefaCombo() {

        andamentoTarefaCombo = new ComboBox();
        
        List<ParametroAndamentoTarefa> parametros = listener.listAndamento();
        
        for (ParametroAndamentoTarefa parametro : parametros) {
            andamentoTarefaCombo.addItem(parametro.getPercentualandamento());
            andamentoTarefaCombo.setItemCaption(parametro.getPercentualandamento(), parametro.getDescricaoandamento());
        }

        andamentoTarefaCombo.setPageLength(parametros.size());
        andamentoTarefaCombo.setWidth("100px");
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
    
    public void apresentaPopUpMotivoRecusa() {

        Window subWindow = new Window("Informe o motivo da recusa");
        subWindow.setWidth("400px");
        subWindow.setHeight("200px");

        VerticalLayout subContent = new VerticalLayout();
        subContent.setMargin(true);
        subWindow.setContent(subContent);

        motivoRecusaTextArea = new TextArea();
        

        subContent.addComponent(motivoRecusaTextArea);
        motivoRecusaTextArea.setSizeFull();

        HorizontalLayout barraDeBotoesContainer = new HorizontalLayout();
        barraDeBotoesContainer.setWidth("100%");
        barraDeBotoesContainer.setSpacing(true);

        cancelarRecusa = new Button("Cancelar Recusa", (Button.ClickEvent event) -> {
            subWindow.close();
        });
        barraDeBotoesContainer.addComponent(cancelarRecusa);
        barraDeBotoesContainer.setComponentAlignment(cancelarRecusa, Alignment.BOTTOM_LEFT);

        confirmarRecusa = new Button("Confirmar Recusa", (Button.ClickEvent event) -> {
            listener.confirmarRecusaClicked();
            subWindow.close();
        });
        barraDeBotoesContainer.addComponent(confirmarRecusa);
        barraDeBotoesContainer.setComponentAlignment(confirmarRecusa, Alignment.BOTTOM_RIGHT);

        subContent.addComponent(barraDeBotoesContainer);
        
        // Center it in the browser window
        subWindow.center();

        // Open it in the UI
        UI.getCurrent().addWindow(subWindow);

    }

    public void selecionaComboAndamento(int andamento) {
        andamentoTarefaCombo.select(andamento);
    }

    public void apresentaHistorico() {

        Window subWindow = new Window("Histórico");
        subWindow.setWidth("800px");
        subWindow.setHeight("300px");

        VerticalLayout subContent = new VerticalLayout();
        subContent.setMargin(true);
        subWindow.setContent(subContent);

        historicoContainer = new BeanItemContainer<>(HistoricoTarefa.class);
        
        
        historicoTable = new Table() {
            @Override
            protected String formatPropertyValue(Object rowId,
                    Object colId, Property property) {
                // Format by property type
                if (property.getType() == LocalDateTime.class) {

                    return FormatterUtil.formatDateTime((LocalDateTime) property.getValue());
                } else if (property.getType() == Usuario.class) {

                    return ((Usuario) property.getValue()).getNome() + " " + ((Usuario) property.getValue()).getSobrenome();
                }

                return super.formatPropertyValue(rowId, colId, property);
            }
        };
        
        historicoTable.setContainerDataSource(historicoContainer);

        historicoTable.setColumnWidth("dataHora", 120);
        historicoTable.setColumnHeader("dataHora", mensagens.getString("PopUpEvolucaoStatusView.historicoTable.colunaData"));
        
        historicoTable.setColumnWidth("usuario", 100);
        historicoTable.setColumnHeader("usuario", mensagens.getString("PopUpEvolucaoStatusView.historicoTable.colunaUsuario"));
        
        historicoTable.setColumnWidth("evento", 200);
        historicoTable.setColumnHeader("evento", mensagens.getString("PopUpEvolucaoStatusView.historicoTable.colunaEvento"));
        
        historicoTable.setColumnWidth("comentario", 100);
        historicoTable.setColumnHeader("comentario", mensagens.getString("PopUpEvolucaoStatusView.historicoTable.colunaComentario"));
        
        historicoTable.setVisibleColumns("dataHora", "usuario", "evento", "comentario");
        
        historicoTable.addGeneratedColumn("Editar", (Table source, final Object itemId, Object columnId) -> {
            Button editarButton = new Button("...");
            editarButton.addClickListener((ClickEvent event) -> {
                
                listener.editarHistorico((HistoricoTarefa) itemId);

            });
            
            editarButton.setEnabled(listener.historicoEditavel((HistoricoTarefa) itemId));
            
            return editarButton;
        });

        historicoTable.setColumnWidth("Editar", 30);
        
        historicoTable.setSelectable(true);
        historicoTable.setImmediate(true);
        historicoTable.setPageLength(10);
        historicoTable.setWidth("100%");
        
        subContent.addComponent(historicoTable);

        // Center it in the browser window
        subWindow.center();

        // Open it in the UI
        UI.getCurrent().addWindow(subWindow);

    }

    public TextArea getMotivoBloqueioTextArea() {
        return motivoBloqueioTextArea;
    }
    
    public TextArea getMotivoRecusaTextArea() {
        return motivoRecusaTextArea;
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
    
        recusarTarefaButton = new Button("Recusar Tarefa");
        recusarTarefaButton.addClickListener((Button.ClickEvent event) -> {
            listener.recusarTarefaClicked();;
        });
        alterarStatusContainer.addComponent(recusarTarefaButton);

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

        avaliarTarefaRatingStars = new RatingStars();
        avaliarTarefaRatingStars.setAnimated(true);
        avaliarTarefaRatingStars.setMaxValue(5);
        
        comboAvaliacaoContainer.addComponent(avaliarTarefaRatingStars);

        // Reavaliacao
        if (avaliacaoTarefa != null){
            avaliarTarefaRatingStars.setValue((double)avaliacaoTarefa.getAvaliacao());
        }

        confirmarAvaliacaoButton = new Button("Confirmar");
        confirmarAvaliacaoButton.addClickListener((Button.ClickEvent event) -> {
            listener.processarAvaliacao();
        });
        
        comboAvaliacaoContainer.addComponent(confirmarAvaliacaoButton);
        
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

    public RatingStars getAvaliarTarefaRatingStars() {
        return avaliarTarefaRatingStars;
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
    public void apresentaPerfilUsuarioSolicitanteTarefaParada(StatusTarefa status, String motivoBloqueio) {
        
        main.removeAllComponents();

        main.setSpacing(true);

        // ---------------------------------------------------------------------
        // exibe o status
        // ---------------------------------------------------------------------
        String statusString =  "<h3> Tarefa: " + status.toString() + "</h3>";
        
        if (status == StatusTarefa.RECUSADA){
            statusString += "<p> Motivo: " + motivoBloqueio + "</p>";
        }
            
        
        main.addComponent(new Label(
                statusString,
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

    public BeanItemContainer<HistoricoTarefa> getHistoricoContainer() {
        return historicoContainer;
    }

    public void apresentaPopUpAlteracaoComentario(HistoricoTarefa historicoTarefa) {

        Window subWindow = new Window("Informe o novo comentário");
        subWindow.setModal(true);
        subWindow.setWidth("300px");
        subWindow.setHeight("100px");

        VerticalLayout subContent = new VerticalLayout();
        subContent.setMargin(true);
        subContent.setSizeFull();
        subWindow.setContent(subContent);

        comentarioTextArea = new TextArea();
        comentarioTextArea.setValue(historicoTarefa.getComentario() == null ? "" : historicoTarefa.getComentario());

        subContent.addComponent(comentarioTextArea);
        comentarioTextArea.setWidth("100%");

        HorizontalLayout barraDeBotoesContainer = new HorizontalLayout();
        barraDeBotoesContainer.setWidth("100%");
        barraDeBotoesContainer.setSpacing(true);

        cancelarAlteracaoHistorico = new Button("Cancelar", (Button.ClickEvent event) -> {
            subWindow.close();
        });
        barraDeBotoesContainer.addComponent(cancelarAlteracaoHistorico);
        barraDeBotoesContainer.setComponentAlignment(cancelarAlteracaoHistorico, Alignment.BOTTOM_LEFT);
        
        confirmarAlteracaoHistorico = new Button("Confirmar", (Button.ClickEvent event) -> {
            listener.confirmarAlteracaoHistoricoClicked(historicoTarefa);
            subWindow.close();
        });
        barraDeBotoesContainer.addComponent(confirmarAlteracaoHistorico);
        barraDeBotoesContainer.setComponentAlignment(confirmarAlteracaoHistorico, Alignment.BOTTOM_RIGHT);

        subContent.addComponent(barraDeBotoesContainer);
        
        // Center it in the browser window
        subWindow.center();

        // Open it in the UI
        UI.getCurrent().addWindow(subWindow);

    }

    public TextArea getComentarioTextArea() {
        return comentarioTextArea;
    }

    public Button getCancelarRecusa() {
        return cancelarRecusa;
    }

    public Button getConfirmarRecusa() {
        return confirmarRecusa;
    }
    
    

    
    
}
