package com.saax.gestorweb.model.dashboard;

import com.saax.gestorweb.model.datamodel.AndamentoTarefa;
import com.saax.gestorweb.model.DashboardModel;
import com.saax.gestorweb.model.datamodel.AvaliacaoMetaTarefa;
import com.saax.gestorweb.model.datamodel.BloqueioTarefa;
import com.saax.gestorweb.model.datamodel.HistoricoTarefa;
import com.saax.gestorweb.model.datamodel.StatusTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
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
public class PopUpEvolucaoStatusModel {

    /**
     * Registra o andamento de uma tarefa
     *
     * @param usuarioLogado
     * @param idTarefa
     * @param andamento
     * @param comentarioAndamento
     * @return
     */
    public Tarefa atualizarAndamentoTarefa(Usuario usuarioLogado, Integer idTarefa, Integer andamento, String comentarioAndamento) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        try {

            em.getTransaction().begin();

            Tarefa tarefa = em.find(Tarefa.class, idTarefa);

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

            em.persist(new HistoricoTarefa(historico.toString(), usuarioLogado, tarefa, LocalDateTime.now()));

            em.persist(andamentoTarefa);
            em.merge(tarefa);

            em.getTransaction().commit();

            return em.find(Tarefa.class, idTarefa);

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
    public Tarefa concluirTarefa(Integer idTarefa) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        try {

            em.getTransaction().begin();

            Tarefa tarefa = em.find(Tarefa.class, idTarefa);

            tarefa.setStatus(StatusTarefa.CONCLUIDA);
            tarefa.setDataTermino(LocalDate.now());

            em.persist(tarefa);

            return tarefa;

        } catch (Exception ex) {
            Logger.getLogger(PopUpEvolucaoStatusModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Registra o bloqueio de uma tarefa
     *
     * @param idTarefa
     * @param motivoBloqueio
     * @param usuarioLogado
     * @return
     */
    public Tarefa bloquearTarefa(Integer idTarefa, String motivoBloqueio, Usuario usuarioLogado) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        try {

            em.getTransaction().begin();

            Tarefa tarefa = em.find(Tarefa.class, idTarefa);

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

            em.persist(new HistoricoTarefa(historico.toString(), usuarioLogado, tarefa, LocalDateTime.now()));
            em.persist(bloqueioTarefa);

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
    public Tarefa adiarTarefa(Integer id, Usuario usuario) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        try {

            em.getTransaction().begin();

            Tarefa tarefa = em.find(Tarefa.class, id);

            tarefa.setStatus(StatusTarefa.ADIADA);
            tarefa.setDataTermino(LocalDate.now());

            em.merge(tarefa);

            em.persist(new HistoricoTarefa("Tarefa ADIADA!", usuario, tarefa, LocalDateTime.now()));

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
    public BloqueioTarefa obterBloqueioAtivo(Tarefa tarefa) {

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
    public Tarefa removerBloqueioTarefa(Integer idTarefa, Usuario usuario) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        try {

            em.getTransaction().begin();

            Tarefa tarefa = em.find(Tarefa.class, idTarefa);

            BloqueioTarefa bloqueioAtivo = obterBloqueioAtivo(tarefa);

            bloqueioAtivo.setDataHoraRemocao(LocalDateTime.now());
            bloqueioAtivo.setUsuarioRemocao(usuario);

            tarefa.setStatus(bloqueioAtivo.getStatus());

            StringBuilder historico = new StringBuilder();

            historico.append("BLOQUEIO Removido!");
            historico.append(" Tarefa voltou ao status: ");
            historico.append(tarefa.getStatus());

            em.persist(new HistoricoTarefa(historico.toString(), usuario, tarefa, LocalDateTime.now()));

            em.persist(bloqueioAtivo);

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
    public Tarefa aceitarTarefa(Integer idTarefa, Usuario usuario) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        try {

            em.getTransaction().begin();

            Tarefa tarefa = em.find(Tarefa.class, idTarefa);

            tarefa.setStatus(StatusTarefa.NAO_INICIADA);

            em.persist(new HistoricoTarefa("Tarefa ACEITA!", usuario, tarefa, LocalDateTime.now()));

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
    public Tarefa reabrirTarefa(Integer idTarefa, Usuario usuario) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        try {

            em.getTransaction().begin();

            Tarefa tarefa = em.find(Tarefa.class, idTarefa);

            tarefa.setStatus(StatusTarefa.NAO_INICIADA);

            StringBuilder historico = new StringBuilder();

            historico.append("Tarefa REABERTA!");
            historico.append(" Tarefa voltou ao status: ");
            historico.append(tarefa.getStatus());

            em.persist(new HistoricoTarefa(historico.toString(), usuario, tarefa, LocalDateTime.now()));

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
    public Tarefa reativarTarefa(Integer idTarefa, Usuario usuario) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        try {

            em.getTransaction().begin();

            Tarefa tarefa = em.find(Tarefa.class, idTarefa);

            tarefa.setStatus(StatusTarefa.NAO_ACEITA);

            StringBuilder historico = new StringBuilder();

            historico.append("Tarefa REATIVADA!");
            historico.append(" Tarefa voltou ao status: ");
            historico.append(tarefa.getStatus());

            em.persist(new HistoricoTarefa(historico.toString(), usuario, tarefa, LocalDateTime.now()));

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
    public Tarefa avaliarTarefa(Integer idTarefa, Integer avaliacao, String observacaoAvaliacao, Usuario usuario) {

        boolean reavaliacao;

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        try {

            em.getTransaction().begin();

            Tarefa tarefa = em.find(Tarefa.class, idTarefa);

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

            em.persist(new HistoricoTarefa(historico.toString(), usuario, tarefa, LocalDateTime.now()));

            if (reavaliacao) {
                em.merge(avaliacaoTarefa);
            } else {
                em.persist(avaliacaoTarefa);
            }

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
    public Tarefa iniciarTarefa(Usuario usuario, Integer idTarefa, Integer andamento, String comentarioAndamento) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        try {

            em.getTransaction().begin();

            Tarefa tarefa = em.find(Tarefa.class, idTarefa);

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

            em.persist(new HistoricoTarefa(historico.toString(), usuario, tarefa, LocalDateTime.now()));

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
    public Tarefa cancelarTarefa(Integer idTarefa, Usuario usuario) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        try {

            em.getTransaction().begin();

            Tarefa tarefa = em.find(Tarefa.class, idTarefa);

            tarefa.setStatus(StatusTarefa.CANCELADA);
            tarefa.setDataTermino(LocalDate.now());

            em.persist(new HistoricoTarefa("Tarefa CANCELADA!", usuario, tarefa, LocalDateTime.now()));

            em.merge(tarefa);

            em.getTransaction().commit();

            return tarefa;

        } catch (Exception ex) {
            em.getTransaction().rollback();
            Logger.getLogger(DashboardModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }

}
