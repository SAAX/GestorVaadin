package com.saax.gestorweb.model.dashboard;

import com.saax.gestorweb.model.datamodel.AndamentoTarefa;
import com.saax.gestorweb.dao.AndamentoTarefaDAO;
import com.saax.gestorweb.dao.BloqueioTarefaDAO;
import com.saax.gestorweb.dao.HistoricoTarefaDAO;
import com.saax.gestorweb.dao.TarefaDAO;
import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import com.saax.gestorweb.model.DashboardModel;
import com.saax.gestorweb.model.datamodel.BloqueioTarefa;
import com.saax.gestorweb.model.datamodel.HistoricoTarefa;
import com.saax.gestorweb.model.datamodel.StatusTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.PostgresConnection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rodrigo
 */
public class PopUpEvolucaoStatusModel {

    private final TarefaDAO tarefaDAO;
    private final AndamentoTarefaDAO andamentoTarefaDAO;
    private final HistoricoTarefaDAO historicoTarefaDAO;
    private final BloqueioTarefaDAO bloqueioTarefaDAO;

    public PopUpEvolucaoStatusModel() {

        tarefaDAO = new TarefaDAO(PostgresConnection.getInstance().getEntityManagerFactory());
        andamentoTarefaDAO = new AndamentoTarefaDAO(PostgresConnection.getInstance().getEntityManagerFactory());
        bloqueioTarefaDAO = new BloqueioTarefaDAO(PostgresConnection.getInstance().getEntityManagerFactory());
        historicoTarefaDAO = new HistoricoTarefaDAO(PostgresConnection.getInstance().getEntityManagerFactory());

    }

    public Tarefa atualizarAndamentoTarefa(Usuario usuarioLogado, Integer idTarefa, Integer andamento, String comentarioAndamento) {

        Tarefa tarefa = tarefaDAO.findTarefa(idTarefa);

        tarefa.setAndamento(andamento);

        AndamentoTarefa andamentoTarefa = new AndamentoTarefa();
        andamentoTarefa.setAndamentoatual(andamento);
        andamentoTarefa.setComentario(comentarioAndamento);
        andamentoTarefa.setUsuarioInclusao(usuarioLogado);
        andamentoTarefa.setDataHoraInclusao(LocalDateTime.now());
        andamentoTarefa.setTarefa(tarefa);

        tarefa.addAndamento(andamentoTarefa);

        StringBuilder historico = new StringBuilder();

        historico.append("Registrado andamento de ");
        historico.append(andamento);
        historico.append("% ");
        if (comentarioAndamento != null) {
            historico.append("- ");
            historico.append(comentarioAndamento);
        }

        try {
            historicoTarefaDAO.create(new HistoricoTarefa(historico.toString(), usuarioLogado, tarefa, LocalDateTime.now()));
            andamentoTarefaDAO.create(andamentoTarefa);
            tarefaDAO.edit(tarefa);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(DashboardModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DashboardModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tarefa;
    }

    public void concluirTarefa(Tarefa tarefa) {

        try {

            tarefa.setStatus(StatusTarefa.CONCLUIDA);
            tarefa.setDataTermino(LocalDate.now());

            tarefaDAO.edit(tarefa);

        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PopUpEvolucaoStatusModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PopUpEvolucaoStatusModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void bloquearTarefa(Integer idTarefa, String motivoBloqueio, Usuario usuarioLogado) {
        try {

            Tarefa tarefa = tarefaDAO.findTarefa(idTarefa);

            BloqueioTarefa bloqueioTarefa = new BloqueioTarefa();
            bloqueioTarefa.setTarefa(tarefa);
            bloqueioTarefa.setMotivo(motivoBloqueio);
            bloqueioTarefa.setDataHoraInclusao(LocalDateTime.now());
            bloqueioTarefa.setUsuarioInclusao(usuarioLogado);

            tarefa.setStatus(StatusTarefa.BLOQUEADA);

            tarefa.addBloqueio(bloqueioTarefa);

            StringBuilder historico = new StringBuilder();

            historico.append("Tarefa BLOQUEADA!");
            historico.append(" com motivo: ");
            historico.append(motivoBloqueio);

            historicoTarefaDAO.create(new HistoricoTarefa(historico.toString(), usuarioLogado, tarefa, LocalDateTime.now()));
            bloqueioTarefaDAO.create(bloqueioTarefa);

            tarefaDAO.edit(tarefa);

        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PopUpEvolucaoStatusModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PopUpEvolucaoStatusModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void adiarTarefa(Integer id, Usuario usuario) {

        try {

            Tarefa tarefa = tarefaDAO.findTarefa(id);

            tarefa.setStatus(StatusTarefa.ADIADA);
            tarefa.setDataTermino(LocalDate.now());

            tarefaDAO.edit(tarefa);

        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PopUpEvolucaoStatusModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PopUpEvolucaoStatusModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

  
}
