package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.PopUpStatusModel;
import com.saax.gestorweb.model.datamodel.AvaliacaoMetaTarefa;
import com.saax.gestorweb.model.datamodel.BloqueioTarefa;
import com.saax.gestorweb.model.datamodel.HistoricoTarefa;
import com.saax.gestorweb.model.datamodel.ParametroAndamentoTarefa;
import com.saax.gestorweb.model.datamodel.StatusTarefa;
import static com.saax.gestorweb.model.datamodel.StatusTarefa.ADIADA;
import static com.saax.gestorweb.model.datamodel.StatusTarefa.AVALIADA;
import static com.saax.gestorweb.model.datamodel.StatusTarefa.BLOQUEADA;
import static com.saax.gestorweb.model.datamodel.StatusTarefa.CANCELADA;
import static com.saax.gestorweb.model.datamodel.StatusTarefa.CONCLUIDA;
import static com.saax.gestorweb.model.datamodel.StatusTarefa.EM_ANDAMENTO;
import static com.saax.gestorweb.model.datamodel.StatusTarefa.NAO_ACEITA;
import static com.saax.gestorweb.model.datamodel.StatusTarefa.NAO_INICIADA;
import static com.saax.gestorweb.model.datamodel.StatusTarefa.RECUSADA;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.view.PopUpStatusView;
import com.saax.gestorweb.view.PopUpStatusViewListener;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.SessionAttributesEnum;
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

    // Todo presenter mantem acesso à view 
    private final transient PopUpStatusView view;

    // Referencia ao recurso das mensagens e imagens:
    private final transient ResourceBundle mensagens = GestorPresenter.getMENSAGENS();

    private Tarefa tarefa = null;
    private PopupButton statusButton = null;
    private final Usuario usuario;
    private PopUpStatusListener listener;

    /**
     * Cria o pop-up ligando view e presenter
     *
     * @param view
     */
    public PopUpStatusPresenter(PopUpStatusView view) {
        this.view = view;
        usuario = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO);
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
        if (listener!=null){
            listener.taskStatusChanged(tarefa);
        }

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
                    ultimoBloqueioTarefa = PopUpStatusModel.obterBloqueioAtivo(tarefa);
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
                    ultimoBloqueioTarefa = PopUpStatusModel.obterBloqueioAtivo(tarefa);
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
                    ultimoBloqueioTarefa = PopUpStatusModel.obterBloqueioAtivo(tarefa);
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
                    ultimoBloqueioTarefa = PopUpStatusModel.obterBloqueioAtivo(tarefa);
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
                tarefa = PopUpStatusModel.iniciarTarefa(usuario, idTarefa, andamento, comentarioAndamento);
            } else {
                tarefa = PopUpStatusModel.atualizarAndamentoTarefa(usuario, idTarefa, andamento, comentarioAndamento);
            }

            if (andamento == 100) {

                PopUpStatusModel.concluirTarefa(tarefa.getId());
                PopUpStatusModel.notifyRequestor(tarefa);

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

        tarefa = PopUpStatusModel.bloquearTarefa(tarefa.getId(), motivoBloqueio, usuario);

        closePopUpButton();

    }

    /**
     * Evento disparado ao ser confirmada a recusa da tarefa
     */
    @Override
    public void confirmarRecusaClicked() {

        String motivoRecusa = view.getMotivoRecusaTextArea().getValue();

        tarefa = PopUpStatusModel.recusarTarefa(tarefa.getId(), motivoRecusa, usuario);

        closePopUpButton();

    }

    /**
     * Evento disparado ao ser solicitado para adiar a tarefa
     */
    @Override
    public void adiarTarefaClicked() {
        tarefa = PopUpStatusModel.adiarTarefa(tarefa.getId(), usuario);
        closePopUpButton();
    }

    /**
     * Evento disparado ao ser solicitado para cancelar a tarefa
     */
    @Override
    public void cancelarTarefaClicked() {
        tarefa = PopUpStatusModel.cancelarTarefa(tarefa.getId(), usuario);
        closePopUpButton();

    }

    /**
     * Evento disparado ao ser solicitado para remover o bloqueio da tarefa
     */
    @Override
    public void removerBloqueioTarefaClicked() {
        tarefa = PopUpStatusModel.removerBloqueioTarefa(tarefa.getId(), usuario);
        closePopUpButton();
    }

    /**
     * Evento disparado ao ser solicitado para remover a recusa da tarefa
     */
    @Override
    public void removerRecusaTarefaClicked() {
        tarefa = PopUpStatusModel.removerRecusaTarefa(tarefa.getId(), usuario);
        closePopUpButton();
    }

    /**
     * Evento disparado ao ser aceita a tarefa para execução
     */
    @Override
    public void aceitarTarefaClicked() {
        tarefa = PopUpStatusModel.aceitarTarefa(tarefa.getId(), usuario);
        closePopUpButton();
    }

    /**
     * Evento disparado ao ser solicitado para reabrir uma tarefa concluida que
     * ainda não foi avaliada
     */
    @Override
    public void reabrirTarefaClicked() {
        tarefa = PopUpStatusModel.reabrirTarefa(tarefa.getId(), usuario);
        closePopUpButton();

    }

    /**
     * Evento disparado ao ser solicitado para reativar uma tarefa cancelada ou
     * adiada
     */
    @Override
    public void reativarTarefaClicked() {
        tarefa = PopUpStatusModel.reativarTarefa(tarefa.getId(), usuario);
        closePopUpButton();
    }

    /**
     * Evento disparado ao ser avaliada uma tarefa pelo usuario solicitante
     */
    @Override
    public void processarAvaliacao() {
        if (view.getAvaliarTarefaRatingStars().getValue() == null){
            throw new IllegalArgumentException("Informe a avaliação");
        }
        
        Integer avaliacao = (Integer) view.getAvaliarTarefaRatingStars().getValue().intValue();
        
        if (view.getComentarioAvaliacaoTextField() == null){
            throw new IllegalArgumentException("Informe o comentário");
        }
        
        String observacaoAvaliacao = view.getComentarioAvaliacaoTextField().getValue();
        tarefa = PopUpStatusModel.avaliarTarefa(tarefa.getId(), avaliacao, observacaoAvaliacao, usuario);
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
        PopUpStatusModel.atualizarHistorico(historicoTarefa.getId(), novoComentario);
        view.getHistoricoContainer().removeAllItems();
        view.getHistoricoContainer().addAll(historicoTarefa.getTarefa().getHistorico());

    }

    @Override
    public List<ParametroAndamentoTarefa> listAndamento() {
        return PopUpStatusModel.listParametroAndamentoTarefa(tarefa.getEmpresa());
    }
   
}
