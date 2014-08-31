package com.saax.gestorweb.model.dashboard;

import com.saax.gestorweb.model.datamodel.AndamentoTarefa;
import com.saax.gestorweb.dao.AndamentoTarefaDAO;
import com.saax.gestorweb.dao.AvaliacaoMetaTarefaDAO;
import com.saax.gestorweb.dao.BloqueioTarefaDAO;
import com.saax.gestorweb.dao.GenericDAO;
import com.saax.gestorweb.dao.HistoricoTarefaDAO;
import com.saax.gestorweb.dao.TarefaDAO;
import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import com.saax.gestorweb.model.DashboardModel;
import com.saax.gestorweb.model.datamodel.AvaliacaoMetaTarefa;
import com.saax.gestorweb.model.datamodel.BloqueioTarefa;
import com.saax.gestorweb.model.datamodel.HistoricoTarefa;
import com.saax.gestorweb.model.datamodel.StatusTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.PostgresConnection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final GenericDAO genericDAO;
    private final AvaliacaoMetaTarefaDAO avaliacaoTarefaDAO;

    public PopUpEvolucaoStatusModel() {

        tarefaDAO = new TarefaDAO(PostgresConnection.getInstance().getEntityManagerFactory());
        andamentoTarefaDAO = new AndamentoTarefaDAO(PostgresConnection.getInstance().getEntityManagerFactory());
        bloqueioTarefaDAO = new BloqueioTarefaDAO(PostgresConnection.getInstance().getEntityManagerFactory());
        historicoTarefaDAO = new HistoricoTarefaDAO(PostgresConnection.getInstance().getEntityManagerFactory());
        avaliacaoTarefaDAO = new AvaliacaoMetaTarefaDAO(PostgresConnection.getInstance().getEntityManagerFactory());
        genericDAO = new GenericDAO();

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

        return tarefaDAO.findTarefa(idTarefa);
    }

    public Tarefa concluirTarefa(Tarefa tarefa) {

        try {

            tarefa.setStatus(StatusTarefa.CONCLUIDA);
            tarefa.setDataTermino(LocalDate.now());

            tarefaDAO.edit(tarefa);

            return tarefaDAO.findTarefa(tarefa.getId());

        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PopUpEvolucaoStatusModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PopUpEvolucaoStatusModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Tarefa bloquearTarefa(Integer idTarefa, String motivoBloqueio, Usuario usuarioLogado) {
        try {

            Tarefa tarefa = tarefaDAO.findTarefa(idTarefa);

            BloqueioTarefa bloqueioTarefa = new BloqueioTarefa();
            bloqueioTarefa.setTarefa(tarefa);
            bloqueioTarefa.setMotivo(motivoBloqueio);
            bloqueioTarefa.setStatus(tarefa.getStatus());
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
            return tarefaDAO.findTarefa(tarefa.getId());
            
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PopUpEvolucaoStatusModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PopUpEvolucaoStatusModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Tarefa adiarTarefa(Integer id, Usuario usuario) {

        try {

            Tarefa tarefa = tarefaDAO.findTarefa(id);

            tarefa.setStatus(StatusTarefa.ADIADA);
            tarefa.setDataTermino(LocalDate.now());

            tarefaDAO.edit(tarefa);
            
            return tarefaDAO.findTarefa(tarefa.getId());
            
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PopUpEvolucaoStatusModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PopUpEvolucaoStatusModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public BloqueioTarefa obterBloqueioAtivo(Tarefa tarefa) {
        List<BloqueioTarefa> bloqueios = genericDAO.listByNamedQuery("BloqueioTarefa.findByTarefa", "tarefa", tarefa);
        for (BloqueioTarefa bloqueio : bloqueios) {
            if (bloqueio.getDataHoraRemocao() == null) {
                return bloqueio;
            }
        }
        return null;
    }

    public Tarefa removerBloqueioTarefa(Integer idTarefa, Usuario usuario) {
        try {

            Tarefa tarefa = tarefaDAO.findTarefa(idTarefa);

            BloqueioTarefa bloqueioAtivo = obterBloqueioAtivo(tarefa);

            bloqueioAtivo.setDataHoraRemocao(LocalDateTime.now());
            bloqueioAtivo.setUsuarioRemocao(usuario);

            tarefa.setStatus(bloqueioAtivo.getStatus());

            StringBuilder historico = new StringBuilder();

            historico.append("BLOQUEIO Removido!");
            historico.append(" Tarefa voltou ao status: ");
            historico.append(tarefa.getStatus());

            historicoTarefaDAO.create(new HistoricoTarefa(historico.toString(), usuario, tarefa, LocalDateTime.now()));

            bloqueioTarefaDAO.edit(bloqueioAtivo);

            tarefaDAO.edit(tarefa);
            return tarefaDAO.findTarefa(tarefa.getId());
            
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PopUpEvolucaoStatusModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PopUpEvolucaoStatusModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Tarefa aceitarTarefa(Integer idTarefa, Usuario usuario) {
        try {

            Tarefa tarefa = tarefaDAO.findTarefa(idTarefa);

            tarefa.setStatus(StatusTarefa.NAO_INICIADA);

            StringBuilder historico = new StringBuilder();

            historico.append("Tarefa ACEITA!");

            historicoTarefaDAO.create(new HistoricoTarefa(historico.toString(), usuario, tarefa, LocalDateTime.now()));

            tarefaDAO.edit(tarefa);
            return tarefaDAO.findTarefa(tarefa.getId());
            
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PopUpEvolucaoStatusModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PopUpEvolucaoStatusModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Tarefa reabrirTarefa(Integer idTarefa, Usuario usuario) {
        try {

            Tarefa tarefa = tarefaDAO.findTarefa(idTarefa);

            tarefa.setStatus(StatusTarefa.NAO_INICIADA);

            StringBuilder historico = new StringBuilder();

            historico.append("Tarefa REABERTA!");
            historico.append(" Tarefa voltou ao status: ");
            historico.append(tarefa.getStatus());

            historicoTarefaDAO.create(new HistoricoTarefa(historico.toString(), usuario, tarefa, LocalDateTime.now()));

            tarefaDAO.edit(tarefa);
            return tarefaDAO.findTarefa(tarefa.getId());
            
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PopUpEvolucaoStatusModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PopUpEvolucaoStatusModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Tarefa reativarTarefa(Integer idTarefa, Usuario usuario) {
        try {

            Tarefa tarefa = tarefaDAO.findTarefa(idTarefa);

            tarefa.setStatus(StatusTarefa.NAO_ACEITA);

            StringBuilder historico = new StringBuilder();

            historico.append("Tarefa REATIVADA!");
            historico.append(" Tarefa voltou ao status: ");
            historico.append(tarefa.getStatus());

            historicoTarefaDAO.create(new HistoricoTarefa(historico.toString(), usuario, tarefa, LocalDateTime.now()));

            tarefaDAO.edit(tarefa);

            return tarefaDAO.findTarefa(tarefa.getId());

        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PopUpEvolucaoStatusModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PopUpEvolucaoStatusModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Tarefa avaliarTarefa(Integer idTarefa, Integer avaliacao, String observacaoAvaliacao, Usuario usuario) {

        boolean reavaliacao;
        try {

            Tarefa tarefa = tarefaDAO.findTarefa(idTarefa);

            AvaliacaoMetaTarefa avaliacaoTarefa;

            reavaliacao = tarefa.getStatus() == StatusTarefa.AVALIADA;

            avaliacaoTarefa = (reavaliacao ? tarefa.getAvaliacoes().iterator().next() : new AvaliacaoMetaTarefa());

            avaliacaoTarefa.setAvaliacao(avaliacao);
            avaliacaoTarefa.setComentario(observacaoAvaliacao);
            avaliacaoTarefa.setDataHoraInclusao(LocalDateTime.now());
            avaliacaoTarefa.setTarefa(tarefa);
            avaliacaoTarefa.setUsuarioInclusao(usuario);
            avaliacaoTarefa.setUsuarioAvaliador(usuario);
            avaliacaoTarefa.setUsuarioAvaliado(tarefa.getUsuarioResponsavel());

            StringBuilder historico = new StringBuilder();
            historico.append((reavaliacao ? "Tarefa REAVALIADA com " : "Tarefa AVALIADA com "));
            historico.append(avaliacaoTarefa);
            historico.append(" estrelas.");

            tarefa.setStatus(StatusTarefa.AVALIADA);

            if (reavaliacao) {
                tarefa.getAvaliacoes().set(0, avaliacaoTarefa);
            } else {
                tarefa.setAvaliacoes(new ArrayList<>());
                tarefa.getAvaliacoes().add(avaliacaoTarefa);

            }

            historicoTarefaDAO.create(new HistoricoTarefa(historico.toString(), usuario, tarefa, LocalDateTime.now()));

            if (reavaliacao) {
                avaliacaoTarefaDAO.edit(avaliacaoTarefa);
            } else {
                avaliacaoTarefaDAO.create(avaliacaoTarefa);
            }

            tarefaDAO.edit(tarefa);
            return tarefaDAO.findTarefa(tarefa.getId());
            
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PopUpEvolucaoStatusModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PopUpEvolucaoStatusModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Tarefa iniciarTarefa(Usuario usuario, Integer idTarefa, Integer andamento, String comentarioAndamento) {
        Tarefa tarefa = tarefaDAO.findTarefa(idTarefa);

        tarefa.setStatus(StatusTarefa.EM_ANDAMENTO);
        tarefa.setAndamento(andamento);

        AndamentoTarefa andamentoTarefa = new AndamentoTarefa();
        andamentoTarefa.setAndamentoatual(andamento);
        andamentoTarefa.setComentario(comentarioAndamento);
        andamentoTarefa.setUsuarioInclusao(usuario);
        andamentoTarefa.setDataHoraInclusao(LocalDateTime.now());
        andamentoTarefa.setTarefa(tarefa);

        tarefa.addAndamento(andamentoTarefa);

        StringBuilder historico = new StringBuilder();

        historico.append("Tarefa iniciada com andamento de ");
        historico.append(andamento);
        historico.append("% ");
        if (comentarioAndamento != null) {
            historico.append("- ");
            historico.append(comentarioAndamento);
        }

        try {
            historicoTarefaDAO.create(new HistoricoTarefa(historico.toString(), usuario, tarefa, LocalDateTime.now()));
            andamentoTarefaDAO.create(andamentoTarefa);
            tarefaDAO.edit(tarefa);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(DashboardModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DashboardModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tarefaDAO.findTarefa(idTarefa);
    }

}
