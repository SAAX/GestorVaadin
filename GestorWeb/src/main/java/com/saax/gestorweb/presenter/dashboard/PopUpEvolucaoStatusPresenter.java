package com.saax.gestorweb.presenter.dashboard;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.dashboard.PopUpEvolucaoStatusModel;
import com.saax.gestorweb.model.datamodel.BloqueioTarefa;
import com.saax.gestorweb.model.datamodel.HistoricoTarefa;
import com.saax.gestorweb.model.datamodel.StatusTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.view.dashboard.PopUpEvolucaoStatusView;
import com.saax.gestorweb.view.dashboard.PopUpEvolucaoStatusViewListener;
import com.vaadin.ui.UI;
import java.util.List;
import java.util.ResourceBundle;
import org.vaadin.hene.popupbutton.PopupButton;

/**
 * Presenter do componente responsável por controlar as evoluções de status das
 * tarefas, avaliaçao, bloqueio e apontamentos
 *
 * @author rodrigo
 */
public class PopUpEvolucaoStatusPresenter implements PopUpEvolucaoStatusViewListener {

    // Todo presenter mantem acesso à view e ao model
    private final transient PopUpEvolucaoStatusView view;
    private final transient PopUpEvolucaoStatusModel model;

    // Referencia ao recurso das mensagens:
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getUserData().getMensagens();

    private Tarefa tarefa = null;
    private PopupButton statusButton = null;
    private final Usuario usuario = ((GestorMDI) UI.getCurrent()).getUserData().getUsuarioLogado();

    public PopUpEvolucaoStatusPresenter(PopUpEvolucaoStatusView view, PopUpEvolucaoStatusModel model) {
        this.view = view;
        this.model = model;
        view.setListener(this);

    }

    @Override
    public void load(Tarefa tarefa) {

        this.tarefa = tarefa;

        statusButton = new PopupButton(getStatusTarefaDescription(tarefa));

        // vincula o botão a tarefa
        statusButton.setId(tarefa.getGlobalID());

        configurarView();

        // configura o conteudo
        statusButton.setContent(view);

    }

    /**
     * Configura a view de acordo com o perfil do usário e o status da tarefa
     */
    private void configurarView() {

        // Avalia se o usuario eh responsavel ou o solicitante e abre o perfil de acordo
        if (usuario.equals(tarefa.getUsuarioResponsavel())) {

            if (tarefa.getStatus() == StatusTarefa.EM_ANDAMENTO) {
                view.apresentaPerfilUsuarioResponsavelTarefaEmAndamento();
                view.selecionaComboAndamento(tarefa.getAndamento());
            }

        } else if (usuario.equals(tarefa.getUsuarioSolicitante())) {

            if (tarefa.getStatus() == StatusTarefa.EM_ANDAMENTO) {
                view.apresentaPerfilUsuarioSolicitante(tarefa.getAndamento(), null);
                
            } else if (tarefa.getStatus() == StatusTarefa.BLOQUEADA) {
                
                BloqueioTarefa ultimoBloqueioTarefa = tarefa.getBloqueios().get(tarefa.getBloqueios().size() - 1);
                String motivo = ultimoBloqueioTarefa.getMotivo();
                
                view.apresentaPerfilUsuarioSolicitante(tarefa.getAndamento(), motivo);
            }

        } else {
            view.apresentaMensagemComStatus(tarefa.getStatus());
        }

    }

    @Override
    public void processarAlteracaoAndamento() {

        if (statusButton.isPopupVisible()) {

            Integer idTarefa = tarefa.getId();

            Integer andamento = (Integer) view.getAndamentoTarefaCombo().getValue();
            String comentarioAndamento = view.getComentarioAndamento().getValue();

            tarefa = model.atualizarAndamentoTarefa(usuario, idTarefa, andamento, comentarioAndamento);

            statusButton.setPopupVisible(false);

            statusButton.setCaption(getStatusTarefaDescription(tarefa));

            if (andamento == 100) {

                model.concluirTarefa(tarefa);
                statusButton.setCaption(getStatusTarefaDescription(tarefa));

            }
        }
    }

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

    @Override
    public void bloquearTarefaClicked() {

        view.apresentaPopUpMotivoBloqueio();

    }

    @Override
    public void historicoTarefaClicked() {
        List<HistoricoTarefa> historico = tarefa.getHistorico();

        view.apresentaHistorico(historico);

    }

    @Override
    public void confirmarBloqueioClicked() {

        String motivoBloqueio = view.getMotivoBloqueioTextArea().getValue();

        model.bloquearTarefa(tarefa.getId(), motivoBloqueio, usuario);
    }

    @Override
    public void adiarTarefaClicked() {
        model.adiarTarefa(tarefa.getId(), usuario);
    }

    @Override
    public void cancelarTarefaClicked() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
