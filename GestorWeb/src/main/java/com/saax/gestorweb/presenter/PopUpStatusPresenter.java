package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.PopUpStatusModel;
import com.saax.gestorweb.model.datamodel.AvaliacaoMetaTarefa;
import com.saax.gestorweb.model.datamodel.BloqueioTarefa;
import com.saax.gestorweb.model.datamodel.HistoricoTarefa;
import com.saax.gestorweb.model.datamodel.ParametroAndamentoTarefa;
import com.saax.gestorweb.model.datamodel.StatusTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.view.PopUpStatusView;
import com.saax.gestorweb.view.PopUpStatusViewListener;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.view.PopUpStatusListener;
import com.vaadin.ui.UI;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import org.vaadin.hene.popupbutton.PopupButton;

/**
 * Presenter do componente responsável por controlar as evoluções de status das
 * tarefas, avaliaçao, bloqueio e apontamentos
 *
 * @author rodrigo
 */
public class PopUpStatusPresenter implements Serializable, PopUpStatusViewListener {

    // Todo presenter mantem acesso à view e ao model
    private final transient PopUpStatusView view;
    private final transient PopUpStatusModel model;

    // Referencia ao recurso das mensagens e imagens:
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final transient GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();

    private Tarefa tarefa = null;
    private PopupButton statusButton = null;
    private final Usuario usuario;
    private PopUpStatusListener listener;

    /**
     * Cria o pop-up ligando view e presenter
     *
     * @param view
     * @param model
     */
    public PopUpStatusPresenter(PopUpStatusView view, PopUpStatusModel model) {
        this.view = view;
        this.model = model;
        usuario = (Usuario) GestorSession.getAttribute("loggedUser");
        view.setListener(this);

    }

    /**
     * Carrega o pop-up configurando a visualização de acordo com o
     * relacionamento entre o usuario e a tarefa, e o status da mesma 
     *
     * @param tarefa
     * @param statusButton
     * @param listener
     */
    @Override
    public void load(Tarefa tarefa, PopupButton statusButton, PopUpStatusListener listener) {

        this.listener = listener;
        
        this.tarefa = tarefa;

        if (statusButton != null){
            this.statusButton = statusButton;
            this.statusButton.setCaption(getStatusTarefaDescription(tarefa));
            
        } else {
            this.statusButton = new PopupButton(getStatusTarefaDescription(tarefa));
        }


        // vincula o botão a tarefa
        this.statusButton.setId(tarefa.getGlobalID());
        this.statusButton.setData(tarefa);

        configurarView();

        // configura o conteudo
        this.statusButton.setContent(view);

    }

    /**
     * Comando para fechar pop-up
     */
    private void closePopUpButton() {

        statusButton.setPopupVisible(false);
        statusButton.setCaption(getStatusTarefaDescription(tarefa));
        configurarView();
        statusButton.setContent(view);
        listener.taskStatusChanged(tarefa);

    }

    /**
     * Configura a view de acordo com o perfil do usário e o status da tarefa
     * <br>
     * Ao clicar no status da tarefa vai abrir um pop-up para evolução do status
     * / andamento da tarefa. <br>
     * <br>
     * <p>
     * Neste pop-up o usuario responsável pela tarefa poderá:</p>
     * <ul>
     * <li>indicar um andamento (informando opcionalmente uma observação)</li>
     * <li>bloquear a tarefa (informando o motivo do bloqueio)</li>
     * <li>remover o bloqueio de uma tarefa</li>
     * <li>aceitar a tarefa (se esta nao foi aceita)</li>
     * <li>reabrir a tarefa se estiver concluida, mas ainda nao avaliada</li>
     * </ul>
     * <p>
     * Neste pop-up o usuário solicitante poderá:</p>
     * <ul>
     * <li>Adiar a tarefa</li>
     * <li>Cancelar a tarefa</li>
     * <li>Ver o motivo do bloqueio ( se ela estiver bloqueada )</li>
     * </ul>
     * <p>
     * O pop-up não terá botão de OK ou Cancelar. <br>
     * Quando o usuário informar o status ou andamento o pop-up fechará
     * automaticamente e gravará a informação.
     */
    private void configurarView() {
        
        BloqueioTarefa ultimoBloqueioTarefa;
        String motivo;
        
        // Avalia se o usuario eh responsavel ou o solicitante e abre o perfil de acordo
        if (usuario.equals(tarefa.getUsuarioResponsavel())) {

            // Se usuário for o RESPONSAVEL
            switch (tarefa.getStatus()) {
                case ADIADA:

                    view.apresentaMensagemComStatus(tarefa);

                    break;
                case BLOQUEADA:

                    // Se a tarefa estiver bloqueada, possibilita:
                    // * remover bloqueio
                    // * ver histórico
                    ultimoBloqueioTarefa = model.obterBloqueioAtivo(tarefa);
                    motivo = ultimoBloqueioTarefa.getMotivo();
                    view.apresentaPerfilUsuarioResponsavelTarefaBloqueada(motivo);

                    break;
                case CANCELADA:

                    view.apresentaMensagemComStatus(tarefa);

                    break;
                case CONCLUIDA:

                    // Se a tarefa estiver concluida, possibilita:
                    // * reabrir
                    // * ver histórico
                    view.apresentaPerfilUsuarioResponsavelTarefaConcluida();

                    break;

                case AVALIADA:

                    view.apresentaMensagemComStatus(tarefa);

                    break;
                case EM_ANDAMENTO:

                    // Se a tarefa estiver em andamento, possibilita:
                    // * indicar o andamento
                    // * bloquear
                    // * ver histórico
                    view.apresentaPerfilUsuarioResponsavelTarefaEmAndamento();
                    view.selecionaComboAndamento(tarefa.getAndamento());

                    break;
                    
                case RECUSADA:

                    // Se a tarefa estiver recusada, possibilita:
                    // * remover o bloqueio de recusa
                    // * ver histórico
                    ultimoBloqueioTarefa = model.obterBloqueioAtivo(tarefa);
                    motivo = ultimoBloqueioTarefa.getMotivo();
                    view.apresentaPerfilUsuarioResponsavelTarefaRecusada(motivo);
                    
                    break;
                case NAO_ACEITA:

                    // Se a tarefa ainda não estiver aceita, possibilita:
                    // * aceitar a tarefa
                    // * recusar a tarefa
                    // * ver histórico
                    view.apresentaPerfilUsuarioResponsavelTarefaNaoAceita();
                    break;
                case NAO_INICIADA:

                    // Se a tarefa estiver aceita, mas nao iniciada, possibilita:
                    // * iniciar o andamento (0%)
                    // * bloquear
                    // * ver histórico
                    view.apresentaPerfilUsuarioResponsavelTarefaEmAndamento();

                    break;
                default:
                    throw new AssertionError();
            }

        } else if (usuario.equals(tarefa.getUsuarioSolicitante())) {

            // Se usuário for o SOLICITANTE
            switch (tarefa.getStatus()) {
                case ADIADA:

                    // Se a tarefa estiver adiada, possibilitara
                    // * voltar para nao aceita
                    // * ver histórico
                    view.apresentaPerfilUsuarioSolicitanteTarefaParada();

                    break;
                case BLOQUEADA:

                    // Se a tarefa estiver bloqueada, possibilita:
                    // * visualizar o motivo do bloqueio
                    // * ver histórico
                    ultimoBloqueioTarefa = model.obterBloqueioAtivo(tarefa);
                    motivo = ultimoBloqueioTarefa.getMotivo();

                    view.apresentaPerfilUsuarioSolicitanteTarefaEmAndamento(tarefa.getAndamento(), motivo);

                    break;

                case CANCELADA:

                    // Se a tarefa estiver cancelada, possibilitara
                    // * voltar para nao aceita
                    // * ver histórico
                    view.apresentaPerfilUsuarioSolicitanteTarefaParada();

                    break;
                case CONCLUIDA:

                    // Se a tarefa estiver conjcluida, possibilitara
                    // * avaliar
                    // * ver histórico
                    view.apresentaPerfilUsuarioSolicitanteTarefaConcluida(null);

                    break;

                case AVALIADA:

                    // Se a tarefa estiver conjcluida, possibilitara
                    // * rever avaliaçao
                    // * ver histórico
                    AvaliacaoMetaTarefa avaliacaoTarefa = tarefa.getAvaliacoes().iterator().next();
                    view.apresentaPerfilUsuarioSolicitanteTarefaConcluida(avaliacaoTarefa);

                    break;

                case EM_ANDAMENTO:

                    // Se a tarefa estiver em Andamento, possibilitará
                    // * Adiar a tarefa
                    // * Cancelar a tarefa
                    // * Visualizar o andamento da tarefa
                    // * ver histórico
                    view.apresentaPerfilUsuarioSolicitanteTarefaEmAndamento(tarefa.getAndamento(), null);

                    break;
                case NAO_ACEITA:

                    view.apresentaPerfilUsuarioSolicitanteTarefaParada(StatusTarefa.NAO_ACEITA, null);

                    break;

                case RECUSADA:

                    // Se a tarefa estiver recusada, possibilita:
                    // * ver histórico
                    ultimoBloqueioTarefa = model.obterBloqueioAtivo(tarefa);
                    motivo = ultimoBloqueioTarefa.getMotivo();
                    view.apresentaPerfilUsuarioSolicitanteTarefaParada(StatusTarefa.RECUSADA,motivo);
                    
                    break;
                case NAO_INICIADA:

                    view.apresentaPerfilUsuarioSolicitanteTarefaParada(StatusTarefa.NAO_INICIADA, null);
                    
                    break;
                default:
                    throw new AssertionError();
            }

        } else {

            view.apresentaMensagemComStatus(tarefa);
        }

    }

    
    
    /**
     * Trata o evento disparado quando o usuario informa o andamento da tarefa
     * <br>
     * Obtém os dados e passao ao model para atualização
     */
    @Override
    public void processarAlteracaoAndamento() {

        if (statusButton.isPopupVisible()) {

            Integer idTarefa = tarefa.getId();

            Integer andamento = (Integer) view.getAndamentoTarefaCombo().getValue();
            String comentarioAndamento = view.getComentarioAndamento().getValue();

            if (tarefa.getStatus() == StatusTarefa.NAO_INICIADA) {
                tarefa = model.iniciarTarefa(usuario, idTarefa, andamento, comentarioAndamento);
            } else {
                tarefa = model.atualizarAndamentoTarefa(usuario, idTarefa, andamento, comentarioAndamento);
            }

            if (andamento == 100) {

                model.concluirTarefa(tarefa.getId());
                model.notifyRequestor(tarefa);

            }
            closePopUpButton();
        }

    }

    /**
     * Obtém a descrição internacionalizada do status da tarefa
     *
     * @param tarefa
     * @return
     */
    private String getStatusTarefaDescription(Tarefa tarefa) {

        StatusTarefa statusTarefa = tarefa.getStatus();

        switch (statusTarefa) {
            case NAO_ACEITA:
                return mensagens.getString("StatusTarefa.NAO_ACEITA");
            case NAO_INICIADA:
                return mensagens.getString("StatusTarefa.NAO_INICIADA");
            case EM_ANDAMENTO:
                return mensagens.getString("StatusTarefa.EM_ANDAMENTO") + tarefa.getAndamento() + "%";
            case ADIADA:
                return mensagens.getString("StatusTarefa.ADIADA");
            case BLOQUEADA:
                return mensagens.getString("StatusTarefa.BLOQUEADA");
            case RECUSADA:
                return mensagens.getString("StatusTarefa.RECUSADA");
            case AVALIADA:
                return mensagens.getString("StatusTarefa.AVALIADA");
            case CONCLUIDA:
                return mensagens.getString("StatusTarefa.CONCLUIDA");
            case CANCELADA:
                return mensagens.getString("StatusTarefa.CANCELADA");
            default:
                throw new AssertionError();
        }

    }

    public PopupButton getStatusButton() {
        return statusButton;
    }

    /**
     * Evento disparado ao ser solicitado o bloqueio da tarefa
     */
    @Override
    public void bloquearTarefaClicked() {

        view.apresentaPopUpMotivoBloqueio();

    }

    /**
     * Evento disparado ao ser solicitada a recusa da tarefa
     */
    @Override
    public void recusarTarefaClicked() {

        view.apresentaPopUpMotivoRecusa();

    }

    /**
     * Evento disparado ao ser solicitado o histórico da tarefa
     */
    @Override
    public void historicoTarefaClicked() {
        view.apresentaHistorico();
        view.getHistoricoContainer().addAll(tarefa.getHistorico());

    }

    /**
     * Evento disparado ao ser confirmado o bloqueio da tarefa
     */
    @Override
    public void confirmarBloqueioClicked() {

        String motivoBloqueio = view.getMotivoBloqueioTextArea().getValue();

        tarefa = model.bloquearTarefa(tarefa.getId(), motivoBloqueio, usuario);

        closePopUpButton();

    }

    /**
     * Evento disparado ao ser confirmada a recusa da tarefa
     */
    @Override
    public void confirmarRecusaClicked() {

        String motivoRecusa = view.getMotivoRecusaTextArea().getValue();

        tarefa = model.recusarTarefa(tarefa.getId(), motivoRecusa, usuario);

        closePopUpButton();

    }

    /**
     * Evento disparado ao ser solicitado para adiar a tarefa
     */
    @Override
    public void adiarTarefaClicked() {
        tarefa = model.adiarTarefa(tarefa.getId(), usuario);
        closePopUpButton();
    }

    /**
     * Evento disparado ao ser solicitado para cancelar a tarefa
     */
    @Override
    public void cancelarTarefaClicked() {
        tarefa = model.cancelarTarefa(tarefa.getId(), usuario);
        closePopUpButton();

    }

    /**
     * Evento disparado ao ser solicitado para remover o bloqueio da tarefa
     */
    @Override
    public void removerBloqueioTarefaClicked() {
        tarefa = model.removerBloqueioTarefa(tarefa.getId(), usuario);
        closePopUpButton();
    }

    /**
     * Evento disparado ao ser solicitado para remover a recusa da tarefa
     */
    @Override
    public void removerRecusaTarefaClicked() {
        tarefa = model.removerRecusaTarefa(tarefa.getId(), usuario);
        closePopUpButton();
    }

    /**
     * Evento disparado ao ser aceita a tarefa para execução
     */
    @Override
    public void aceitarTarefaClicked() {
        tarefa = model.aceitarTarefa(tarefa.getId(), usuario);
        closePopUpButton();
    }

    /**
     * Evento disparado ao ser solicitado para reabrir uma tarefa concluida que
     * ainda não foi avaliada
     */
    @Override
    public void reabrirTarefaClicked() {
        tarefa = model.reabrirTarefa(tarefa.getId(), usuario);
        closePopUpButton();

    }

    /**
     * Evento disparado ao ser solicitado para reativar uma tarefa cancelada ou
     * adiada
     */
    @Override
    public void reativarTarefaClicked() {
        tarefa = model.reativarTarefa(tarefa.getId(), usuario);
        closePopUpButton();
    }

    /**
     * Evento disparado ao ser avaliada uma tarefa pelo usuario solicitante
     */
    @Override
    public void processarAvaliacao() {
        if (view.getAvaliarTarefaCombo().getValue() == null){
            throw new IllegalArgumentException("Informe a avaliação");
        }
        
        Integer avaliacao = (Integer) view.getAvaliarTarefaCombo().getValue();
        
        if (view.getComentarioAvaliacaoTextField() == null){
            throw new IllegalArgumentException("Informe o comentário");
        }
        
        String observacaoAvaliacao = view.getComentarioAvaliacaoTextField().getValue();
        tarefa = model.avaliarTarefa(tarefa.getId(), avaliacao, observacaoAvaliacao, usuario);
        closePopUpButton();

    }

    @Override
    public void editarHistorico(HistoricoTarefa historicoTarefa) {
        view.apresentaPopUpAlteracaoComentario(historicoTarefa);
    }

    /**
     * Verifica se o historico pode ser editado. Regra: O histórico pode ser
     * editado se for gerado pelo próprio usuário logado
     *
     * @param historicoTarefa
     * @return true se o histórico puder ser editado e false caso contrário.
     */
    @Override
    public boolean historicoEditavel(HistoricoTarefa historicoTarefa) {
        return historicoTarefa.getUsuario().equals(usuario);
    }

    @Override
    public void confirmarAlteracaoHistoricoClicked(HistoricoTarefa historicoTarefa) {
        String novoComentario = view.getComentarioTextArea().getValue();
        historicoTarefa.setComentario(novoComentario);
        model.atualizarHistorico(historicoTarefa.getId(), novoComentario);
        view.getHistoricoContainer().removeAllItems();
        view.getHistoricoContainer().addAll(historicoTarefa.getTarefa().getHistorico());

    }

    @Override
    public List<ParametroAndamentoTarefa> listAndamento() {
        return model.listParametroAndamentoTarefa(usuario.getEmpresaAtiva());
    }

}
