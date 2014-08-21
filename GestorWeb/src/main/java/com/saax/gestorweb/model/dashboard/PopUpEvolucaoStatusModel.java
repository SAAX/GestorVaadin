package com.saax.gestorweb.model.dashboard;

import com.saax.gestorweb.model.datamodel.AndamentoTarefa;
import com.saax.gestorweb.dao.AndamentoTarefaDAO;
import com.saax.gestorweb.dao.TarefaDAO;
import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import com.saax.gestorweb.model.DashboardModel;
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

    public PopUpEvolucaoStatusModel() {

        tarefaDAO = new TarefaDAO(PostgresConnection.getInstance().getEntityManagerFactory());
        andamentoTarefaDAO = new AndamentoTarefaDAO(PostgresConnection.getInstance().getEntityManagerFactory());

    }

    public Tarefa atualizarAndamentoTarefa(Usuario usuarioLogado, Integer idTarefa, Integer andamento, String comentarioAndamento) {

        Tarefa tarefa = tarefaDAO.findTarefa(idTarefa);

        tarefa.setAndamento(andamento);

        AndamentoTarefa andamentoTarefa = new AndamentoTarefa();
        andamentoTarefa.setAndamentoatual(andamento);
        andamentoTarefa.setComentario(comentarioAndamento);
        andamentoTarefa.setUsuarioInclusao(usuarioLogado);
        andamentoTarefa.setDataHoraInclusao(LocalDateTime.now());

        tarefa.addAndamento(andamentoTarefa);

        try {
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

    public void bloquearTarefa(Tarefa tarefa) {
        try {

            tarefa.setStatus(StatusTarefa.BLOQUEADA);
            
            tarefaDAO.edit(tarefa);

        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PopUpEvolucaoStatusModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PopUpEvolucaoStatusModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
