package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.AndamentoTarefa;
import com.saax.gestorweb.model.datamodel.AvaliacaoMetaTarefa;
import com.saax.gestorweb.model.datamodel.BloqueioTarefa;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.HistoricoTarefa;
import com.saax.gestorweb.model.datamodel.ParametroAndamentoTarefa;
import com.saax.gestorweb.model.datamodel.StatusTarefa;
import com.saax.gestorweb.model.datamodel.Task;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

/**
 * Classe de negócios do pop up de evolução de status
 *
 * @author rodrigo
 */
public class PopUpStatusModel {

    /**
     * Registra o andamento de uma tarefa
     *
     * @param loggedUser
     * @param idTarefa
     * @param andamento
     * @param comentarioAndamento
     * @return
     */
    public Task atualizarAndamentoTarefa(Usuario loggedUser, Integer idTarefa, Integer andamento, String comentarioAndamento) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        try {

            em.getTransaction().begin();

            Task tarefa = em.find(Task.class, idTarefa);

            tarefa.setAndamento(andamento);

            AndamentoTarefa andamentoTarefa = new AndamentoTarefa();
            andamentoTarefa.setAndamentoatual(andamento);
            andamentoTarefa.setComentario(comentarioAndamento);
            andamentoTarefa.setUsuarioInclusao(loggedUser);
            andamentoTarefa.setDataHoraInclusao(LocalDateTime.now());
            andamentoTarefa.setTarefa(tarefa);

            tarefa.addAndamento(andamentoTarefa);

            StringBuilder historico = new StringBuilder();

            historico.append("Registrado andamento de ");
            historico.append(andamento);
            historico.append("% ");

            HistoricoTarefa historicoTarefa = new HistoricoTarefa(historico.toString(), comentarioAndamento, loggedUser, tarefa, LocalDateTime.now());

            tarefa.addHistorico(historicoTarefa);

            em.merge(tarefa);

            em.getTransaction().commit();

            return em.find(Task.class, idTarefa);

        } catch (Exception ex) {
            GestorEntityManagerProvider.getEntityManager().getTransaction().rollback();
            Logger.getLogger(DashboardModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * Marca uma tarefa como concluída
     *
     * @param idTarefa
     * @return
     */
    public Task concluirTarefa(Integer idTarefa) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        try {

            em.getTransaction().begin();
            Task tarefa = em.find(Task.class, idTarefa);

            tarefa.setStatus(StatusTarefa.CONCLUIDA);
            tarefa.setDataTermino(LocalDate.now());

            em.merge(tarefa);

            em.getTransaction().commit();
            return tarefa;

        } catch (Exception ex) {
            Logger.getLogger(PopUpStatusModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Registra o bloqueio de uma tarefa
     *
     * @param idTarefa
     * @param motivoBloqueio
     * @param loggedUser
     * @return
     */
    public Task bloquearTarefa(Integer idTarefa, String motivoBloqueio, Usuario loggedUser) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        try {

            em.getTransaction().begin();

            Task tarefa = em.find(Task.class, idTarefa);

            BloqueioTarefa bloqueioTarefa = new BloqueioTarefa();
            bloqueioTarefa.setTarefa(tarefa);
            bloqueioTarefa.setMotivo(motivoBloqueio);
            bloqueioTarefa.setStatus(tarefa.getStatus());
            bloqueioTarefa.setDataHoraInclusao(LocalDateTime.now());
            bloqueioTarefa.setUsuarioInclusao(loggedUser);

            tarefa.setStatus(StatusTarefa.BLOQUEADA);

            tarefa.addBloqueio(bloqueioTarefa);

            StringBuilder historico = new StringBuilder();
            historico.append("Tarefa BLOQUEADA!");

            HistoricoTarefa historicoTarefa = new HistoricoTarefa(historico.toString(), motivoBloqueio, loggedUser, tarefa, LocalDateTime.now());

            tarefa.addHistorico(historicoTarefa);

            em.merge(tarefa);

            em.getTransaction().commit();

            return tarefa;

        } catch (Exception ex) {
            em.getTransaction().rollback();
            Logger.getLogger(DashboardModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
    
    /**
     * Registra a recusa de uma tarefa
     *
     * @param idTarefa
     * @param motivoBloqueio
     * @param loggedUser
     * @return
     */
    public Task recusarTarefa(Integer idTarefa, String motivoRecusa, Usuario loggedUser) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        try {

            em.getTransaction().begin();

            Task tarefa = em.find(Task.class, idTarefa);

            BloqueioTarefa bloqueioTarefa = new BloqueioTarefa();
            bloqueioTarefa.setTarefa(tarefa);
            bloqueioTarefa.setMotivo(motivoRecusa);
            bloqueioTarefa.setStatus(tarefa.getStatus());
            bloqueioTarefa.setDataHoraInclusao(LocalDateTime.now());
            bloqueioTarefa.setUsuarioInclusao(loggedUser);

            tarefa.setStatus(StatusTarefa.RECUSADA);

            tarefa.addBloqueio(bloqueioTarefa);

            StringBuilder historico = new StringBuilder();
            historico.append("Tarefa RECUSADA!");

            HistoricoTarefa historicoTarefa = new HistoricoTarefa(historico.toString(), motivoRecusa, loggedUser, tarefa, LocalDateTime.now());

            tarefa.addHistorico(historicoTarefa);

            em.merge(tarefa);

            em.getTransaction().commit();

            return tarefa;

        } catch (Exception ex) {
            em.getTransaction().rollback();
            Logger.getLogger(DashboardModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * Registra o adiamento de uma tarefa
     *
     * @param id
     * @param usuario
     * @return
     */
    public Task adiarTarefa(Integer id, Usuario usuario) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        try {
        em.getTransaction().begin();

            Task tarefa = em.find(Task.class, id);

            tarefa.setStatus(StatusTarefa.ADIADA);
            tarefa.setDataTermino(LocalDate.now());

            HistoricoTarefa historicoTarefa = new HistoricoTarefa("Tarefa ADIADA!", null, usuario, tarefa, LocalDateTime.now());

            tarefa.addHistorico(historicoTarefa);

            em.merge(tarefa);

        em.getTransaction().commit();
            return tarefa;

        } catch (Exception ex) {
            em.getTransaction().rollback();
            Logger.getLogger(DashboardModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * Obtém o bloqueio ativo de uma tarefa
     *
     * @param tarefa
     * @return
     */
    public BloqueioTarefa obterBloqueioAtivo(Task tarefa) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        List<BloqueioTarefa> bloqueios = em.createNamedQuery("BloqueioTarefa.findByTarefa")
                .setParameter("tarefa", tarefa)
                .getResultList();

        for (BloqueioTarefa bloqueio : bloqueios) {
            if (bloqueio.getDataHoraRemocao() == null) {
                return bloqueio;
            }
        }
        return null;
    }

    /**
     * Libera uma tarefa bloqueada
     *
     * @param idTarefa
     * @param usuario
     * @return
     */
    public Task removerBloqueioTarefa(Integer idTarefa, Usuario usuario) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        try {

            em.getTransaction().begin();

            Task tarefa = em.find(Task.class, idTarefa);

            BloqueioTarefa bloqueioAtivo = obterBloqueioAtivo(tarefa);

            bloqueioAtivo.setDataHoraRemocao(LocalDateTime.now());
            bloqueioAtivo.setUsuarioRemocao(usuario);

            tarefa.setStatus(bloqueioAtivo.getStatus());

            StringBuilder historico = new StringBuilder();

            historico.append("BLOQUEIO Removido!");
            historico.append(" Tarefa voltou ao status: ");
            historico.append(tarefa.getStatus());

            HistoricoTarefa historicoTarefa = new HistoricoTarefa(historico.toString(), null, usuario, tarefa, LocalDateTime.now());

            tarefa.addHistorico(historicoTarefa);

            em.merge(tarefa);

            em.getTransaction().commit();

            return tarefa;

        } catch (Exception ex) {
            em.getTransaction().rollback();
            Logger.getLogger(DashboardModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
    
    /**
     * Libera uma tarefa recusada
     *
     * @param idTarefa
     * @param usuario
     * @return
     */
    public Task removerRecusaTarefa(Integer idTarefa, Usuario usuario) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        try {

            em.getTransaction().begin();

            Task tarefa = em.find(Task.class, idTarefa);

            BloqueioTarefa bloqueioAtivo = obterBloqueioAtivo(tarefa);

            bloqueioAtivo.setDataHoraRemocao(LocalDateTime.now());
            bloqueioAtivo.setUsuarioRemocao(usuario);

            tarefa.setStatus(bloqueioAtivo.getStatus());

            StringBuilder historico = new StringBuilder();

            historico.append("Recusa Removida!");
            historico.append(" Tarefa voltou ao status: ");
            historico.append(tarefa.getStatus());

            HistoricoTarefa historicoTarefa = new HistoricoTarefa(historico.toString(), null, usuario, tarefa, LocalDateTime.now());

            tarefa.addHistorico(historicoTarefa);

            em.merge(tarefa);

            em.getTransaction().commit();

            return tarefa;

        } catch (Exception ex) {
            em.getTransaction().rollback();
            Logger.getLogger(DashboardModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     *
     * @param idTarefa
     * @param usuario
     * @return
     */
    public Task aceitarTarefa(Integer idTarefa, Usuario usuario) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        try {

            em.getTransaction().begin();

            Task tarefa = em.find(Task.class, idTarefa);

            tarefa.setStatus(StatusTarefa.NAO_INICIADA);

            HistoricoTarefa historico = new HistoricoTarefa("Tarefa ACEITA!", null, usuario, tarefa, LocalDateTime.now());

            tarefa.addHistorico(historico);

            em.merge(tarefa);

            em.getTransaction().commit();

            return tarefa;

        } catch (Exception ex) {
            em.getTransaction().rollback();
            Logger.getLogger(DashboardModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }

    /**
     * Reabre uma tarefa concluida mas que nao foi avaliada
     *
     * @param idTarefa
     * @param usuario
     * @return
     */
    public Task reabrirTarefa(Integer idTarefa, Usuario usuario) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        try {

            em.getTransaction().begin();

            Task tarefa = em.find(Task.class, idTarefa);

            tarefa.setStatus(StatusTarefa.NAO_INICIADA);

            StringBuilder historico = new StringBuilder();

            historico.append("Tarefa REABERTA!");
            historico.append(" Tarefa voltou ao status: ");
            historico.append(tarefa.getStatus());

            HistoricoTarefa historicoTarefa = new HistoricoTarefa(historico.toString(), null, usuario, tarefa, LocalDateTime.now());

            tarefa.addHistorico(historicoTarefa);

            em.merge(tarefa);

            em.getTransaction().commit();

            return tarefa;

        } catch (Exception ex) {
            em.getTransaction().rollback();
            Logger.getLogger(DashboardModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }

    /**
     * Reativa uma tarefa parada: Cancelada ou Adiada
     *
     * @param idTarefa
     * @param usuario
     * @return
     */
    public Task reativarTarefa(Integer idTarefa, Usuario usuario) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        try {

            em.getTransaction().begin();

            Task tarefa = em.find(Task.class, idTarefa);

            tarefa.setStatus(StatusTarefa.NAO_ACEITA);

            StringBuilder historico = new StringBuilder();

            historico.append("Tarefa REATIVADA!");
            historico.append(" Tarefa voltou ao status: ");
            historico.append(tarefa.getStatus());

            HistoricoTarefa historicoTarefa = new HistoricoTarefa(historico.toString(), null, usuario, tarefa, LocalDateTime.now());

            tarefa.addHistorico(historicoTarefa);

            em.merge(tarefa);

            em.getTransaction().commit();

            return tarefa;

        } catch (Exception ex) {
            em.getTransaction().rollback();
            Logger.getLogger(DashboardModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }

    /**
     * Registra a avaliação de uma tarefa concluida
     *
     * @param idTarefa
     * @param avaliacao
     * @param observacaoAvaliacao
     * @param usuario
     * @return
     */
    public Task avaliarTarefa(Integer idTarefa, Integer avaliacao, String observacaoAvaliacao, Usuario usuario) {

        boolean reavaliacao;

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        try {

            em.getTransaction().begin();

            Task tarefa = em.find(Task.class, idTarefa);

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

            HistoricoTarefa historicoTarefa = new HistoricoTarefa(historico.toString(), observacaoAvaliacao, usuario, tarefa, LocalDateTime.now());

            tarefa.addHistorico(historicoTarefa);

            em.merge(tarefa);

            em.getTransaction().commit();

            return tarefa;

        } catch (Exception ex) {
            em.getTransaction().rollback();
            Logger.getLogger(DashboardModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * Inicia uma tarefa
     *
     * @param usuario
     * @param idTarefa
     * @param andamento
     * @param comentarioAndamento
     * @return
     */
    public Task iniciarTarefa(Usuario usuario, Integer idTarefa, Integer andamento, String comentarioAndamento) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        try {

            em.getTransaction().begin();

            Task tarefa = em.find(Task.class, idTarefa);

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

            HistoricoTarefa historicoTarefa = new HistoricoTarefa(historico.toString(), comentarioAndamento, usuario, tarefa, LocalDateTime.now());

            tarefa.addHistorico(historicoTarefa);

            em.merge(tarefa);

            em.getTransaction().commit();

            return tarefa;

        } catch (Exception ex) {
            em.getTransaction().rollback();
            Logger.getLogger(DashboardModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * Cancela uma tarefa
     *
     * @param idTarefa
     * @param usuario
     * @return
     */
    public Task cancelarTarefa(Integer idTarefa, Usuario usuario) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        try {

            em.getTransaction().begin();

            Task tarefa = em.find(Task.class, idTarefa);

            tarefa.setStatus(StatusTarefa.CANCELADA);
            tarefa.setDataTermino(LocalDate.now());

            HistoricoTarefa historicoTarefa = new HistoricoTarefa("Tarefa CANCELADA!", null, usuario, tarefa, LocalDateTime.now());

            tarefa.addHistorico(historicoTarefa);

            em.merge(tarefa);

            em.getTransaction().commit();

            return tarefa;

        } catch (Exception ex) {
            em.getTransaction().rollback();
            Logger.getLogger(DashboardModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }

    public void atualizarHistorico(Integer idHistoricoTarefa, String novoComentario) {
        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        HistoricoTarefa historicoTarefa = em.find(HistoricoTarefa.class, idHistoricoTarefa);
        historicoTarefa.setComentario(novoComentario);

        em.getTransaction().begin();

        em.merge(historicoTarefa);

        em.getTransaction().commit();
    }

    public List<ParametroAndamentoTarefa> listParametroAndamentoTarefa(Empresa empresa){
        EntityManager em = GestorEntityManagerProvider.getEntityManager();
        List<ParametroAndamentoTarefa> list = em.createNamedQuery("ParametroAndamentoTarefa.findByEmpresa")
                .setParameter("empresa", empresa)
                .getResultList();
        
        if (list.isEmpty()){
            list = em.createNamedQuery("ParametroAndamentoTarefa.findDefault")
                .getResultList();
        }
        
        return list;
    }
    
}
