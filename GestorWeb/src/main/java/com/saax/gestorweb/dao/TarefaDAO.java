package com.saax.gestorweb.dao;

import com.saax.gestorweb.dao.exceptions.IllegalOrphanException;
import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import com.saax.gestorweb.model.datamodel.AnexoTarefa;
import com.saax.gestorweb.model.datamodel.ApontamentoTarefa;
import com.saax.gestorweb.model.datamodel.AvaliacaoMetaTarefa;
import com.saax.gestorweb.model.datamodel.CentroCusto;
import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.FavoritosTarefaMeta;
import com.saax.gestorweb.model.datamodel.OrcamentoTarefa;
import com.saax.gestorweb.model.datamodel.ParticipanteTarefa;
import com.saax.gestorweb.model.datamodel.PrioridadeTarefa;
import com.saax.gestorweb.model.datamodel.StatusTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.TipoTarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * DAO para o entity bean: Tarefa <br><br>
 * 
 * @author rodrigo
 */
public class TarefaDAO implements Serializable {

    public TarefaDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * metodo padrao modificado para gravar data/hora de inclusao
     * @param tarefa 
     */
    public void create(Tarefa tarefa) {
        tarefa.setDataHoraInclusao(LocalDateTime.now());
        if (tarefa.getFavoritados() == null) {
            tarefa.setFavoritados(new ArrayList<FavoritosTarefaMeta>());
        }
        if (tarefa.getSubTarefas() == null) {
            tarefa.setSubTarefas(new ArrayList<Tarefa>());
        }
        if (tarefa.getParticipantes() == null) {
            tarefa.setParticipantes(new ArrayList<ParticipanteTarefa>());
        }
        if (tarefa.getAvaliacoes() == null) {
            tarefa.setAvaliacoes(new ArrayList<AvaliacaoMetaTarefa>());
        }
        if (tarefa.getOrcamentos() == null) {
            tarefa.setOrcamentos(new ArrayList<OrcamentoTarefa>());
        }
        if (tarefa.getApontamentos() == null) {
            tarefa.setApontamentos(new ArrayList<ApontamentoTarefa>());
        }
        if (tarefa.getAnexos() == null) {
            tarefa.setAnexos(new ArrayList<AnexoTarefa>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PrioridadeTarefa prioridade = tarefa.getPrioridade();
            if (prioridade != null) {
                prioridade = em.getReference(prioridade.getClass(), prioridade.getPrioridadetarefa());
                tarefa.setPrioridade(prioridade);
            }
            CentroCusto centroCusto = tarefa.getCentroCusto();
            if (centroCusto != null) {
                centroCusto = em.getReference(centroCusto.getClass(), centroCusto.getId());
                tarefa.setCentroCusto(centroCusto);
            }
            Departamento departamento = tarefa.getDepartamento();
            if (departamento != null) {
                departamento = em.getReference(departamento.getClass(), departamento.getId());
                tarefa.setDepartamento(departamento);
            }
            Empresa empresa = tarefa.getEmpresa();
            if (empresa != null) {
                empresa = em.getReference(empresa.getClass(), empresa.getId());
                tarefa.setEmpresa(empresa);
            }
            EmpresaCliente empresaCliente = tarefa.getEmpresaCliente();
            if (empresaCliente != null) {
                empresaCliente = em.getReference(empresaCliente.getClass(), empresaCliente.getId());
                tarefa.setEmpresaCliente(empresaCliente);
            }
            Tarefa tarefaPai = tarefa.getTarefaPai();
            if (tarefaPai != null) {
                tarefaPai = em.getReference(tarefaPai.getClass(), tarefaPai.getId());
                tarefa.setTarefaPai(tarefaPai);
            }
            Usuario usuarioInclusao = tarefa.getUsuarioInclusao();
            if (usuarioInclusao != null) {
                usuarioInclusao = em.getReference(usuarioInclusao.getClass(), usuarioInclusao.getId());
                tarefa.setUsuarioInclusao(usuarioInclusao);
            }
            Usuario usuarioSolicitante = tarefa.getUsuarioSolicitante();
            if (usuarioSolicitante != null) {
                usuarioSolicitante = em.getReference(usuarioSolicitante.getClass(), usuarioSolicitante.getId());
                tarefa.setUsuarioSolicitante(usuarioSolicitante);
            }
            Usuario usuarioResponsavel = tarefa.getUsuarioResponsavel();
            if (usuarioResponsavel != null) {
                usuarioResponsavel = em.getReference(usuarioResponsavel.getClass(), usuarioResponsavel.getId());
                tarefa.setUsuarioResponsavel(usuarioResponsavel);
            }
            StatusTarefa status = tarefa.getStatus();
            if (status != null) {
                status = em.getReference(status.getClass(), status.getStatustarefa());
                tarefa.setStatus(status);
            }
            Tarefa proximaTarefa = tarefa.getProximaTarefa();
            if (proximaTarefa != null) {
                proximaTarefa = em.getReference(proximaTarefa.getClass(), proximaTarefa.getId());
                tarefa.setProximaTarefa(proximaTarefa);
            }
            TipoTarefa tipo = tarefa.getTipo();
            if (tipo != null) {
                tipo = em.getReference(tipo.getClass(), tipo.getTipotarefa());
                tarefa.setTipo(tipo);
            }
            List<FavoritosTarefaMeta> attachedFavoritados = new ArrayList<FavoritosTarefaMeta>();
            for (FavoritosTarefaMeta favoritadosFavoritosTarefaMetaToAttach : tarefa.getFavoritados()) {
                favoritadosFavoritosTarefaMetaToAttach = em.getReference(favoritadosFavoritosTarefaMetaToAttach.getClass(), favoritadosFavoritosTarefaMetaToAttach.getId());
                attachedFavoritados.add(favoritadosFavoritosTarefaMetaToAttach);
            }
            tarefa.setFavoritados(attachedFavoritados);
            List<Tarefa> attachedSubTarefas = new ArrayList<Tarefa>();
            for (Tarefa subTarefasTarefaToAttach : tarefa.getSubTarefas()) {
                subTarefasTarefaToAttach = em.getReference(subTarefasTarefaToAttach.getClass(), subTarefasTarefaToAttach.getId());
                attachedSubTarefas.add(subTarefasTarefaToAttach);
            }
            tarefa.setSubTarefas(attachedSubTarefas);
            List<ParticipanteTarefa> attachedParicipantes = new ArrayList<ParticipanteTarefa>();
            for (ParticipanteTarefa paricipantesParicipanteTarefaToAttach : tarefa.getParticipantes()) {
                paricipantesParicipanteTarefaToAttach = em.getReference(paricipantesParicipanteTarefaToAttach.getClass(), paricipantesParicipanteTarefaToAttach.getId());
                attachedParicipantes.add(paricipantesParicipanteTarefaToAttach);
            }
            tarefa.setParticipantes(attachedParicipantes);
            List<AvaliacaoMetaTarefa> attachedAvaliacoes = new ArrayList<AvaliacaoMetaTarefa>();
            for (AvaliacaoMetaTarefa avaliacoesAvaliacaoMetaTarefaToAttach : tarefa.getAvaliacoes()) {
                avaliacoesAvaliacaoMetaTarefaToAttach = em.getReference(avaliacoesAvaliacaoMetaTarefaToAttach.getClass(), avaliacoesAvaliacaoMetaTarefaToAttach.getId());
                attachedAvaliacoes.add(avaliacoesAvaliacaoMetaTarefaToAttach);
            }
            tarefa.setAvaliacoes(attachedAvaliacoes);
            List<OrcamentoTarefa> attachedOrcamentos = new ArrayList<OrcamentoTarefa>();
            for (OrcamentoTarefa orcamentosOrcamentoTarefaToAttach : tarefa.getOrcamentos()) {
                orcamentosOrcamentoTarefaToAttach = em.getReference(orcamentosOrcamentoTarefaToAttach.getClass(), orcamentosOrcamentoTarefaToAttach.getId());
                attachedOrcamentos.add(orcamentosOrcamentoTarefaToAttach);
            }
            tarefa.setOrcamentos(attachedOrcamentos);
            List<ApontamentoTarefa> attachedApontamentos = new ArrayList<ApontamentoTarefa>();
            for (ApontamentoTarefa apontamentosApontamentoTarefaToAttach : tarefa.getApontamentos()) {
                apontamentosApontamentoTarefaToAttach = em.getReference(apontamentosApontamentoTarefaToAttach.getClass(), apontamentosApontamentoTarefaToAttach.getId());
                attachedApontamentos.add(apontamentosApontamentoTarefaToAttach);
            }
            tarefa.setApontamentos(attachedApontamentos);
            List<AnexoTarefa> attachedAnexos = new ArrayList<AnexoTarefa>();
            for (AnexoTarefa anexosAnexoTarefaToAttach : tarefa.getAnexos()) {
                anexosAnexoTarefaToAttach = em.getReference(anexosAnexoTarefaToAttach.getClass(), anexosAnexoTarefaToAttach.getId());
                attachedAnexos.add(anexosAnexoTarefaToAttach);
            }
            tarefa.setAnexos(attachedAnexos);
            em.persist(tarefa);
            if (prioridade != null) {
                prioridade.getTarefas().add(tarefa);
                prioridade = em.merge(prioridade);
            }
            if (centroCusto != null) {
                centroCusto.getTarefas().add(tarefa);
                centroCusto = em.merge(centroCusto);
            }
            if (departamento != null) {
                departamento.getTarefas().add(tarefa);
                departamento = em.merge(departamento);
            }
            if (empresa != null) {
                empresa.getTarefas().add(tarefa);
                empresa = em.merge(empresa);
            }
            if (empresaCliente != null) {
                empresaCliente.getTarefas().add(tarefa);
                empresaCliente = em.merge(empresaCliente);
            }
            if (tarefaPai != null) {
                tarefaPai.getSubTarefas().add(tarefa);
                tarefaPai = em.merge(tarefaPai);
            }
            if (usuarioInclusao != null) {
                usuarioInclusao.getTarefasIncluidas().add(tarefa);
                usuarioInclusao = em.merge(usuarioInclusao);
            }
            if (usuarioSolicitante != null) {
                usuarioSolicitante.getTarefasIncluidas().add(tarefa);
                usuarioSolicitante = em.merge(usuarioSolicitante);
            }
            if (usuarioResponsavel != null) {
                usuarioResponsavel.getTarefasIncluidas().add(tarefa);
                usuarioResponsavel = em.merge(usuarioResponsavel);
            }
            if (status != null) {
                status.getTarefas().add(tarefa);
                status = em.merge(status);
            }
            if (proximaTarefa != null) {
                proximaTarefa.getSubTarefas().add(tarefa);
                proximaTarefa = em.merge(proximaTarefa);
            }
            if (tipo != null) {
                tipo.getTarefas().add(tarefa);
                tipo = em.merge(tipo);
            }
            for (FavoritosTarefaMeta favoritadosFavoritosTarefaMeta : tarefa.getFavoritados()) {
                Tarefa oldTarefaOfFavoritadosFavoritosTarefaMeta = favoritadosFavoritosTarefaMeta.getTarefa();
                favoritadosFavoritosTarefaMeta.setTarefa(tarefa);
                favoritadosFavoritosTarefaMeta = em.merge(favoritadosFavoritosTarefaMeta);
                if (oldTarefaOfFavoritadosFavoritosTarefaMeta != null) {
                    oldTarefaOfFavoritadosFavoritosTarefaMeta.getFavoritados().remove(favoritadosFavoritosTarefaMeta);
                    oldTarefaOfFavoritadosFavoritosTarefaMeta = em.merge(oldTarefaOfFavoritadosFavoritosTarefaMeta);
                }
            }
            for (Tarefa subTarefasTarefa : tarefa.getSubTarefas()) {
                Tarefa oldTarefaPaiOfSubTarefasTarefa = subTarefasTarefa.getTarefaPai();
                subTarefasTarefa.setTarefaPai(tarefa);
                subTarefasTarefa = em.merge(subTarefasTarefa);
                if (oldTarefaPaiOfSubTarefasTarefa != null) {
                    oldTarefaPaiOfSubTarefasTarefa.getSubTarefas().remove(subTarefasTarefa);
                    oldTarefaPaiOfSubTarefasTarefa = em.merge(oldTarefaPaiOfSubTarefasTarefa);
                }
            }
            for (ParticipanteTarefa paricipantesParicipanteTarefa : tarefa.getParticipantes()) {
                Tarefa oldTarefaOfParicipantesParicipanteTarefa = paricipantesParicipanteTarefa.getTarefa();
                paricipantesParicipanteTarefa.setTarefa(tarefa);
                paricipantesParicipanteTarefa = em.merge(paricipantesParicipanteTarefa);
                if (oldTarefaOfParicipantesParicipanteTarefa != null) {
                    oldTarefaOfParicipantesParicipanteTarefa.getParticipantes().remove(paricipantesParicipanteTarefa);
                    oldTarefaOfParicipantesParicipanteTarefa = em.merge(oldTarefaOfParicipantesParicipanteTarefa);
                }
            }
            for (AvaliacaoMetaTarefa avaliacoesAvaliacaoMetaTarefa : tarefa.getAvaliacoes()) {
                Tarefa oldTarefaOfAvaliacoesAvaliacaoMetaTarefa = avaliacoesAvaliacaoMetaTarefa.getTarefa();
                avaliacoesAvaliacaoMetaTarefa.setTarefa(tarefa);
                avaliacoesAvaliacaoMetaTarefa = em.merge(avaliacoesAvaliacaoMetaTarefa);
                if (oldTarefaOfAvaliacoesAvaliacaoMetaTarefa != null) {
                    oldTarefaOfAvaliacoesAvaliacaoMetaTarefa.getAvaliacoes().remove(avaliacoesAvaliacaoMetaTarefa);
                    oldTarefaOfAvaliacoesAvaliacaoMetaTarefa = em.merge(oldTarefaOfAvaliacoesAvaliacaoMetaTarefa);
                }
            }
            for (OrcamentoTarefa orcamentosOrcamentoTarefa : tarefa.getOrcamentos()) {
                Tarefa oldTarefaOfOrcamentosOrcamentoTarefa = orcamentosOrcamentoTarefa.getTarefa();
                orcamentosOrcamentoTarefa.setTarefa(tarefa);
                orcamentosOrcamentoTarefa = em.merge(orcamentosOrcamentoTarefa);
                if (oldTarefaOfOrcamentosOrcamentoTarefa != null) {
                    oldTarefaOfOrcamentosOrcamentoTarefa.getOrcamentos().remove(orcamentosOrcamentoTarefa);
                    oldTarefaOfOrcamentosOrcamentoTarefa = em.merge(oldTarefaOfOrcamentosOrcamentoTarefa);
                }
            }
            for (ApontamentoTarefa apontamentosApontamentoTarefa : tarefa.getApontamentos()) {
                Tarefa oldTarefaOfApontamentosApontamentoTarefa = apontamentosApontamentoTarefa.getTarefa();
                apontamentosApontamentoTarefa.setTarefa(tarefa);
                apontamentosApontamentoTarefa = em.merge(apontamentosApontamentoTarefa);
                if (oldTarefaOfApontamentosApontamentoTarefa != null) {
                    oldTarefaOfApontamentosApontamentoTarefa.getApontamentos().remove(apontamentosApontamentoTarefa);
                    oldTarefaOfApontamentosApontamentoTarefa = em.merge(oldTarefaOfApontamentosApontamentoTarefa);
                }
            }
            for (AnexoTarefa anexosAnexoTarefa : tarefa.getAnexos()) {
                Tarefa oldTarefaOfAnexosAnexoTarefa = anexosAnexoTarefa.getTarefa();
                anexosAnexoTarefa.setTarefa(tarefa);
                anexosAnexoTarefa = em.merge(anexosAnexoTarefa);
                if (oldTarefaOfAnexosAnexoTarefa != null) {
                    oldTarefaOfAnexosAnexoTarefa.getAnexos().remove(anexosAnexoTarefa);
                    oldTarefaOfAnexosAnexoTarefa = em.merge(oldTarefaOfAnexosAnexoTarefa);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tarefa tarefa) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarefa persistentTarefa = em.find(Tarefa.class, tarefa.getId());
            PrioridadeTarefa prioridadeOld = persistentTarefa.getPrioridade();
            PrioridadeTarefa prioridadeNew = tarefa.getPrioridade();
            CentroCusto centroCustoOld = persistentTarefa.getCentroCusto();
            CentroCusto centroCustoNew = tarefa.getCentroCusto();
            Departamento departamentoOld = persistentTarefa.getDepartamento();
            Departamento departamentoNew = tarefa.getDepartamento();
            Empresa empresaOld = persistentTarefa.getEmpresa();
            Empresa empresaNew = tarefa.getEmpresa();
            EmpresaCliente empresaClienteOld = persistentTarefa.getEmpresaCliente();
            EmpresaCliente empresaClienteNew = tarefa.getEmpresaCliente();
            Tarefa tarefaPaiOld = persistentTarefa.getTarefaPai();
            Tarefa tarefaPaiNew = tarefa.getTarefaPai();
            Usuario usuarioInclusaoOld = persistentTarefa.getUsuarioInclusao();
            Usuario usuarioInclusaoNew = tarefa.getUsuarioInclusao();
            Usuario usuarioSolicitanteOld = persistentTarefa.getUsuarioSolicitante();
            Usuario usuarioSolicitanteNew = tarefa.getUsuarioSolicitante();
            Usuario usuarioResponsavelOld = persistentTarefa.getUsuarioResponsavel();
            Usuario usuarioResponsavelNew = tarefa.getUsuarioResponsavel();
            StatusTarefa statusOld = persistentTarefa.getStatus();
            StatusTarefa statusNew = tarefa.getStatus();
            Tarefa proximaTarefaOld = persistentTarefa.getProximaTarefa();
            Tarefa proximaTarefaNew = tarefa.getProximaTarefa();
            TipoTarefa tipoOld = persistentTarefa.getTipo();
            TipoTarefa tipoNew = tarefa.getTipo();
            List<FavoritosTarefaMeta> favoritadosOld = persistentTarefa.getFavoritados();
            List<FavoritosTarefaMeta> favoritadosNew = tarefa.getFavoritados();
            List<Tarefa> subTarefasOld = persistentTarefa.getSubTarefas();
            List<Tarefa> subTarefasNew = tarefa.getSubTarefas();
            List<ParticipanteTarefa> paricipantesOld = persistentTarefa.getParticipantes();
            List<ParticipanteTarefa> paricipantesNew = tarefa.getParticipantes();
            List<AvaliacaoMetaTarefa> avaliacoesOld = persistentTarefa.getAvaliacoes();
            List<AvaliacaoMetaTarefa> avaliacoesNew = tarefa.getAvaliacoes();
            List<OrcamentoTarefa> orcamentosOld = persistentTarefa.getOrcamentos();
            List<OrcamentoTarefa> orcamentosNew = tarefa.getOrcamentos();
            List<ApontamentoTarefa> apontamentosOld = persistentTarefa.getApontamentos();
            List<ApontamentoTarefa> apontamentosNew = tarefa.getApontamentos();
            List<AnexoTarefa> anexosOld = persistentTarefa.getAnexos();
            List<AnexoTarefa> anexosNew = tarefa.getAnexos();
            List<String> illegalOrphanMessages = null;
            for (FavoritosTarefaMeta favoritadosOldFavoritosTarefaMeta : favoritadosOld) {
                if (!favoritadosNew.contains(favoritadosOldFavoritosTarefaMeta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain FavoritosTarefaMeta " + favoritadosOldFavoritosTarefaMeta + " since its tarefa field is not nullable.");
                }
            }
            for (ParticipanteTarefa paricipantesOldParicipanteTarefa : paricipantesOld) {
                if (!paricipantesNew.contains(paricipantesOldParicipanteTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ParicipanteTarefa " + paricipantesOldParicipanteTarefa + " since its tarefa field is not nullable.");
                }
            }
            for (AvaliacaoMetaTarefa avaliacoesOldAvaliacaoMetaTarefa : avaliacoesOld) {
                if (!avaliacoesNew.contains(avaliacoesOldAvaliacaoMetaTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AvaliacaoMetaTarefa " + avaliacoesOldAvaliacaoMetaTarefa + " since its tarefa field is not nullable.");
                }
            }
            for (OrcamentoTarefa orcamentosOldOrcamentoTarefa : orcamentosOld) {
                if (!orcamentosNew.contains(orcamentosOldOrcamentoTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain OrcamentoTarefa " + orcamentosOldOrcamentoTarefa + " since its tarefa field is not nullable.");
                }
            }
            for (ApontamentoTarefa apontamentosOldApontamentoTarefa : apontamentosOld) {
                if (!apontamentosNew.contains(apontamentosOldApontamentoTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ApontamentoTarefa " + apontamentosOldApontamentoTarefa + " since its tarefa field is not nullable.");
                }
            }
            for (AnexoTarefa anexosOldAnexoTarefa : anexosOld) {
                if (!anexosNew.contains(anexosOldAnexoTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AnexoTarefa " + anexosOldAnexoTarefa + " since its tarefa field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (prioridadeNew != null) {
                prioridadeNew = em.getReference(prioridadeNew.getClass(), prioridadeNew.getPrioridadetarefa());
                tarefa.setPrioridade(prioridadeNew);
            }
            if (centroCustoNew != null) {
                centroCustoNew = em.getReference(centroCustoNew.getClass(), centroCustoNew.getId());
                tarefa.setCentroCusto(centroCustoNew);
            }
            if (departamentoNew != null) {
                departamentoNew = em.getReference(departamentoNew.getClass(), departamentoNew.getId());
                tarefa.setDepartamento(departamentoNew);
            }
            if (empresaNew != null) {
                empresaNew = em.getReference(empresaNew.getClass(), empresaNew.getId());
                tarefa.setEmpresa(empresaNew);
            }
            if (empresaClienteNew != null) {
                empresaClienteNew = em.getReference(empresaClienteNew.getClass(), empresaClienteNew.getId());
                tarefa.setEmpresaCliente(empresaClienteNew);
            }
            if (tarefaPaiNew != null) {
                tarefaPaiNew = em.getReference(tarefaPaiNew.getClass(), tarefaPaiNew.getId());
                tarefa.setTarefaPai(tarefaPaiNew);
            }
            if (usuarioInclusaoNew != null) {
                usuarioInclusaoNew = em.getReference(usuarioInclusaoNew.getClass(), usuarioInclusaoNew.getId());
                tarefa.setUsuarioInclusao(usuarioInclusaoNew);
            }
            if (usuarioSolicitanteNew != null) {
                usuarioSolicitanteNew = em.getReference(usuarioSolicitanteNew.getClass(), usuarioSolicitanteNew.getId());
                tarefa.setUsuarioSolicitante(usuarioSolicitanteNew);
            }
            if (usuarioResponsavelNew != null) {
                usuarioResponsavelNew = em.getReference(usuarioResponsavelNew.getClass(), usuarioResponsavelNew.getId());
                tarefa.setUsuarioResponsavel(usuarioResponsavelNew);
            }
            if (statusNew != null) {
                statusNew = em.getReference(statusNew.getClass(), statusNew.getStatustarefa());
                tarefa.setStatus(statusNew);
            }
            if (proximaTarefaNew != null) {
                proximaTarefaNew = em.getReference(proximaTarefaNew.getClass(), proximaTarefaNew.getId());
                tarefa.setProximaTarefa(proximaTarefaNew);
            }
            if (tipoNew != null) {
                tipoNew = em.getReference(tipoNew.getClass(), tipoNew.getTipotarefa());
                tarefa.setTipo(tipoNew);
            }
            List<FavoritosTarefaMeta> attachedFavoritadosNew = new ArrayList<FavoritosTarefaMeta>();
            for (FavoritosTarefaMeta favoritadosNewFavoritosTarefaMetaToAttach : favoritadosNew) {
                favoritadosNewFavoritosTarefaMetaToAttach = em.getReference(favoritadosNewFavoritosTarefaMetaToAttach.getClass(), favoritadosNewFavoritosTarefaMetaToAttach.getId());
                attachedFavoritadosNew.add(favoritadosNewFavoritosTarefaMetaToAttach);
            }
            favoritadosNew = attachedFavoritadosNew;
            tarefa.setFavoritados(favoritadosNew);
            List<Tarefa> attachedSubTarefasNew = new ArrayList<Tarefa>();
            for (Tarefa subTarefasNewTarefaToAttach : subTarefasNew) {
                subTarefasNewTarefaToAttach = em.getReference(subTarefasNewTarefaToAttach.getClass(), subTarefasNewTarefaToAttach.getId());
                attachedSubTarefasNew.add(subTarefasNewTarefaToAttach);
            }
            subTarefasNew = attachedSubTarefasNew;
            tarefa.setSubTarefas(subTarefasNew);
            List<ParticipanteTarefa> attachedParicipantesNew = new ArrayList<ParticipanteTarefa>();
            for (ParticipanteTarefa paricipantesNewParicipanteTarefaToAttach : paricipantesNew) {
                paricipantesNewParicipanteTarefaToAttach = em.getReference(paricipantesNewParicipanteTarefaToAttach.getClass(), paricipantesNewParicipanteTarefaToAttach.getId());
                attachedParicipantesNew.add(paricipantesNewParicipanteTarefaToAttach);
            }
            paricipantesNew = attachedParicipantesNew;
            tarefa.setParticipantes(paricipantesNew);
            List<AvaliacaoMetaTarefa> attachedAvaliacoesNew = new ArrayList<AvaliacaoMetaTarefa>();
            for (AvaliacaoMetaTarefa avaliacoesNewAvaliacaoMetaTarefaToAttach : avaliacoesNew) {
                avaliacoesNewAvaliacaoMetaTarefaToAttach = em.getReference(avaliacoesNewAvaliacaoMetaTarefaToAttach.getClass(), avaliacoesNewAvaliacaoMetaTarefaToAttach.getId());
                attachedAvaliacoesNew.add(avaliacoesNewAvaliacaoMetaTarefaToAttach);
            }
            avaliacoesNew = attachedAvaliacoesNew;
            tarefa.setAvaliacoes(avaliacoesNew);
            List<OrcamentoTarefa> attachedOrcamentosNew = new ArrayList<OrcamentoTarefa>();
            for (OrcamentoTarefa orcamentosNewOrcamentoTarefaToAttach : orcamentosNew) {
                orcamentosNewOrcamentoTarefaToAttach = em.getReference(orcamentosNewOrcamentoTarefaToAttach.getClass(), orcamentosNewOrcamentoTarefaToAttach.getId());
                attachedOrcamentosNew.add(orcamentosNewOrcamentoTarefaToAttach);
            }
            orcamentosNew = attachedOrcamentosNew;
            tarefa.setOrcamentos(orcamentosNew);
            List<ApontamentoTarefa> attachedApontamentosNew = new ArrayList<ApontamentoTarefa>();
            for (ApontamentoTarefa apontamentosNewApontamentoTarefaToAttach : apontamentosNew) {
                apontamentosNewApontamentoTarefaToAttach = em.getReference(apontamentosNewApontamentoTarefaToAttach.getClass(), apontamentosNewApontamentoTarefaToAttach.getId());
                attachedApontamentosNew.add(apontamentosNewApontamentoTarefaToAttach);
            }
            apontamentosNew = attachedApontamentosNew;
            tarefa.setApontamentos(apontamentosNew);
            List<AnexoTarefa> attachedAnexosNew = new ArrayList<AnexoTarefa>();
            for (AnexoTarefa anexosNewAnexoTarefaToAttach : anexosNew) {
                anexosNewAnexoTarefaToAttach = em.getReference(anexosNewAnexoTarefaToAttach.getClass(), anexosNewAnexoTarefaToAttach.getId());
                attachedAnexosNew.add(anexosNewAnexoTarefaToAttach);
            }
            anexosNew = attachedAnexosNew;
            tarefa.setAnexos(anexosNew);
            tarefa = em.merge(tarefa);
            if (prioridadeOld != null && !prioridadeOld.equals(prioridadeNew)) {
                prioridadeOld.getTarefas().remove(tarefa);
                prioridadeOld = em.merge(prioridadeOld);
            }
            if (prioridadeNew != null && !prioridadeNew.equals(prioridadeOld)) {
                prioridadeNew.getTarefas().add(tarefa);
                prioridadeNew = em.merge(prioridadeNew);
            }
            if (centroCustoOld != null && !centroCustoOld.equals(centroCustoNew)) {
                centroCustoOld.getTarefas().remove(tarefa);
                centroCustoOld = em.merge(centroCustoOld);
            }
            if (centroCustoNew != null && !centroCustoNew.equals(centroCustoOld)) {
                centroCustoNew.getTarefas().add(tarefa);
                centroCustoNew = em.merge(centroCustoNew);
            }
            if (departamentoOld != null && !departamentoOld.equals(departamentoNew)) {
                departamentoOld.getTarefas().remove(tarefa);
                departamentoOld = em.merge(departamentoOld);
            }
            if (departamentoNew != null && !departamentoNew.equals(departamentoOld)) {
                departamentoNew.getTarefas().add(tarefa);
                departamentoNew = em.merge(departamentoNew);
            }
            if (empresaOld != null && !empresaOld.equals(empresaNew)) {
                empresaOld.getTarefas().remove(tarefa);
                empresaOld = em.merge(empresaOld);
            }
            if (empresaNew != null && !empresaNew.equals(empresaOld)) {
                empresaNew.getTarefas().add(tarefa);
                empresaNew = em.merge(empresaNew);
            }
            if (empresaClienteOld != null && !empresaClienteOld.equals(empresaClienteNew)) {
                empresaClienteOld.getTarefas().remove(tarefa);
                empresaClienteOld = em.merge(empresaClienteOld);
            }
            if (empresaClienteNew != null && !empresaClienteNew.equals(empresaClienteOld)) {
                empresaClienteNew.getTarefas().add(tarefa);
                empresaClienteNew = em.merge(empresaClienteNew);
            }
            if (tarefaPaiOld != null && !tarefaPaiOld.equals(tarefaPaiNew)) {
                tarefaPaiOld.getSubTarefas().remove(tarefa);
                tarefaPaiOld = em.merge(tarefaPaiOld);
            }
            if (tarefaPaiNew != null && !tarefaPaiNew.equals(tarefaPaiOld)) {
                tarefaPaiNew.getSubTarefas().add(tarefa);
                tarefaPaiNew = em.merge(tarefaPaiNew);
            }
            if (usuarioInclusaoOld != null && !usuarioInclusaoOld.equals(usuarioInclusaoNew)) {
                usuarioInclusaoOld.getTarefasIncluidas().remove(tarefa);
                usuarioInclusaoOld = em.merge(usuarioInclusaoOld);
            }
            if (usuarioInclusaoNew != null && !usuarioInclusaoNew.equals(usuarioInclusaoOld)) {
                usuarioInclusaoNew.getTarefasIncluidas().add(tarefa);
                usuarioInclusaoNew = em.merge(usuarioInclusaoNew);
            }
            if (usuarioSolicitanteOld != null && !usuarioSolicitanteOld.equals(usuarioSolicitanteNew)) {
                usuarioSolicitanteOld.getTarefasIncluidas().remove(tarefa);
                usuarioSolicitanteOld = em.merge(usuarioSolicitanteOld);
            }
            if (usuarioSolicitanteNew != null && !usuarioSolicitanteNew.equals(usuarioSolicitanteOld)) {
                usuarioSolicitanteNew.getTarefasIncluidas().add(tarefa);
                usuarioSolicitanteNew = em.merge(usuarioSolicitanteNew);
            }
            if (usuarioResponsavelOld != null && !usuarioResponsavelOld.equals(usuarioResponsavelNew)) {
                usuarioResponsavelOld.getTarefasIncluidas().remove(tarefa);
                usuarioResponsavelOld = em.merge(usuarioResponsavelOld);
            }
            if (usuarioResponsavelNew != null && !usuarioResponsavelNew.equals(usuarioResponsavelOld)) {
                usuarioResponsavelNew.getTarefasIncluidas().add(tarefa);
                usuarioResponsavelNew = em.merge(usuarioResponsavelNew);
            }
            if (statusOld != null && !statusOld.equals(statusNew)) {
                statusOld.getTarefas().remove(tarefa);
                statusOld = em.merge(statusOld);
            }
            if (statusNew != null && !statusNew.equals(statusOld)) {
                statusNew.getTarefas().add(tarefa);
                statusNew = em.merge(statusNew);
            }
            if (proximaTarefaOld != null && !proximaTarefaOld.equals(proximaTarefaNew)) {
                proximaTarefaOld.getSubTarefas().remove(tarefa);
                proximaTarefaOld = em.merge(proximaTarefaOld);
            }
            if (proximaTarefaNew != null && !proximaTarefaNew.equals(proximaTarefaOld)) {
                proximaTarefaNew.getSubTarefas().add(tarefa);
                proximaTarefaNew = em.merge(proximaTarefaNew);
            }
            if (tipoOld != null && !tipoOld.equals(tipoNew)) {
                tipoOld.getTarefas().remove(tarefa);
                tipoOld = em.merge(tipoOld);
            }
            if (tipoNew != null && !tipoNew.equals(tipoOld)) {
                tipoNew.getTarefas().add(tarefa);
                tipoNew = em.merge(tipoNew);
            }
            for (FavoritosTarefaMeta favoritadosNewFavoritosTarefaMeta : favoritadosNew) {
                if (!favoritadosOld.contains(favoritadosNewFavoritosTarefaMeta)) {
                    Tarefa oldTarefaOfFavoritadosNewFavoritosTarefaMeta = favoritadosNewFavoritosTarefaMeta.getTarefa();
                    favoritadosNewFavoritosTarefaMeta.setTarefa(tarefa);
                    favoritadosNewFavoritosTarefaMeta = em.merge(favoritadosNewFavoritosTarefaMeta);
                    if (oldTarefaOfFavoritadosNewFavoritosTarefaMeta != null && !oldTarefaOfFavoritadosNewFavoritosTarefaMeta.equals(tarefa)) {
                        oldTarefaOfFavoritadosNewFavoritosTarefaMeta.getFavoritados().remove(favoritadosNewFavoritosTarefaMeta);
                        oldTarefaOfFavoritadosNewFavoritosTarefaMeta = em.merge(oldTarefaOfFavoritadosNewFavoritosTarefaMeta);
                    }
                }
            }
            for (Tarefa subTarefasOldTarefa : subTarefasOld) {
                if (!subTarefasNew.contains(subTarefasOldTarefa)) {
                    subTarefasOldTarefa.setTarefaPai(null);
                    subTarefasOldTarefa = em.merge(subTarefasOldTarefa);
                }
            }
            for (Tarefa subTarefasNewTarefa : subTarefasNew) {
                if (!subTarefasOld.contains(subTarefasNewTarefa)) {
                    Tarefa oldTarefaPaiOfSubTarefasNewTarefa = subTarefasNewTarefa.getTarefaPai();
                    subTarefasNewTarefa.setTarefaPai(tarefa);
                    subTarefasNewTarefa = em.merge(subTarefasNewTarefa);
                    if (oldTarefaPaiOfSubTarefasNewTarefa != null && !oldTarefaPaiOfSubTarefasNewTarefa.equals(tarefa)) {
                        oldTarefaPaiOfSubTarefasNewTarefa.getSubTarefas().remove(subTarefasNewTarefa);
                        oldTarefaPaiOfSubTarefasNewTarefa = em.merge(oldTarefaPaiOfSubTarefasNewTarefa);
                    }
                }
            }
            for (ParticipanteTarefa paricipantesNewParicipanteTarefa : paricipantesNew) {
                if (!paricipantesOld.contains(paricipantesNewParicipanteTarefa)) {
                    Tarefa oldTarefaOfParicipantesNewParicipanteTarefa = paricipantesNewParicipanteTarefa.getTarefa();
                    paricipantesNewParicipanteTarefa.setTarefa(tarefa);
                    paricipantesNewParicipanteTarefa = em.merge(paricipantesNewParicipanteTarefa);
                    if (oldTarefaOfParicipantesNewParicipanteTarefa != null && !oldTarefaOfParicipantesNewParicipanteTarefa.equals(tarefa)) {
                        oldTarefaOfParicipantesNewParicipanteTarefa.getParticipantes().remove(paricipantesNewParicipanteTarefa);
                        oldTarefaOfParicipantesNewParicipanteTarefa = em.merge(oldTarefaOfParicipantesNewParicipanteTarefa);
                    }
                }
            }
            for (AvaliacaoMetaTarefa avaliacoesNewAvaliacaoMetaTarefa : avaliacoesNew) {
                if (!avaliacoesOld.contains(avaliacoesNewAvaliacaoMetaTarefa)) {
                    Tarefa oldTarefaOfAvaliacoesNewAvaliacaoMetaTarefa = avaliacoesNewAvaliacaoMetaTarefa.getTarefa();
                    avaliacoesNewAvaliacaoMetaTarefa.setTarefa(tarefa);
                    avaliacoesNewAvaliacaoMetaTarefa = em.merge(avaliacoesNewAvaliacaoMetaTarefa);
                    if (oldTarefaOfAvaliacoesNewAvaliacaoMetaTarefa != null && !oldTarefaOfAvaliacoesNewAvaliacaoMetaTarefa.equals(tarefa)) {
                        oldTarefaOfAvaliacoesNewAvaliacaoMetaTarefa.getAvaliacoes().remove(avaliacoesNewAvaliacaoMetaTarefa);
                        oldTarefaOfAvaliacoesNewAvaliacaoMetaTarefa = em.merge(oldTarefaOfAvaliacoesNewAvaliacaoMetaTarefa);
                    }
                }
            }
            for (OrcamentoTarefa orcamentosNewOrcamentoTarefa : orcamentosNew) {
                if (!orcamentosOld.contains(orcamentosNewOrcamentoTarefa)) {
                    Tarefa oldTarefaOfOrcamentosNewOrcamentoTarefa = orcamentosNewOrcamentoTarefa.getTarefa();
                    orcamentosNewOrcamentoTarefa.setTarefa(tarefa);
                    orcamentosNewOrcamentoTarefa = em.merge(orcamentosNewOrcamentoTarefa);
                    if (oldTarefaOfOrcamentosNewOrcamentoTarefa != null && !oldTarefaOfOrcamentosNewOrcamentoTarefa.equals(tarefa)) {
                        oldTarefaOfOrcamentosNewOrcamentoTarefa.getOrcamentos().remove(orcamentosNewOrcamentoTarefa);
                        oldTarefaOfOrcamentosNewOrcamentoTarefa = em.merge(oldTarefaOfOrcamentosNewOrcamentoTarefa);
                    }
                }
            }
            for (ApontamentoTarefa apontamentosNewApontamentoTarefa : apontamentosNew) {
                if (!apontamentosOld.contains(apontamentosNewApontamentoTarefa)) {
                    Tarefa oldTarefaOfApontamentosNewApontamentoTarefa = apontamentosNewApontamentoTarefa.getTarefa();
                    apontamentosNewApontamentoTarefa.setTarefa(tarefa);
                    apontamentosNewApontamentoTarefa = em.merge(apontamentosNewApontamentoTarefa);
                    if (oldTarefaOfApontamentosNewApontamentoTarefa != null && !oldTarefaOfApontamentosNewApontamentoTarefa.equals(tarefa)) {
                        oldTarefaOfApontamentosNewApontamentoTarefa.getApontamentos().remove(apontamentosNewApontamentoTarefa);
                        oldTarefaOfApontamentosNewApontamentoTarefa = em.merge(oldTarefaOfApontamentosNewApontamentoTarefa);
                    }
                }
            }
            for (AnexoTarefa anexosNewAnexoTarefa : anexosNew) {
                if (!anexosOld.contains(anexosNewAnexoTarefa)) {
                    Tarefa oldTarefaOfAnexosNewAnexoTarefa = anexosNewAnexoTarefa.getTarefa();
                    anexosNewAnexoTarefa.setTarefa(tarefa);
                    anexosNewAnexoTarefa = em.merge(anexosNewAnexoTarefa);
                    if (oldTarefaOfAnexosNewAnexoTarefa != null && !oldTarefaOfAnexosNewAnexoTarefa.equals(tarefa)) {
                        oldTarefaOfAnexosNewAnexoTarefa.getAnexos().remove(anexosNewAnexoTarefa);
                        oldTarefaOfAnexosNewAnexoTarefa = em.merge(oldTarefaOfAnexosNewAnexoTarefa);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tarefa.getId();
                if (findTarefa(id) == null) {
                    throw new NonexistentEntityException("The tarefa with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarefa tarefa;
            try {
                tarefa = em.getReference(Tarefa.class, id);
                tarefa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tarefa with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<FavoritosTarefaMeta> favoritadosOrphanCheck = tarefa.getFavoritados();
            for (FavoritosTarefaMeta favoritadosOrphanCheckFavoritosTarefaMeta : favoritadosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tarefa (" + tarefa + ") cannot be destroyed since the FavoritosTarefaMeta " + favoritadosOrphanCheckFavoritosTarefaMeta + " in its favoritados field has a non-nullable tarefa field.");
            }
            List<ParticipanteTarefa> paricipantesOrphanCheck = tarefa.getParticipantes();
            for (ParticipanteTarefa paricipantesOrphanCheckParicipanteTarefa : paricipantesOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tarefa (" + tarefa + ") cannot be destroyed since the ParicipanteTarefa " + paricipantesOrphanCheckParicipanteTarefa + " in its paricipantes field has a non-nullable tarefa field.");
            }
            List<AvaliacaoMetaTarefa> avaliacoesOrphanCheck = tarefa.getAvaliacoes();
            for (AvaliacaoMetaTarefa avaliacoesOrphanCheckAvaliacaoMetaTarefa : avaliacoesOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tarefa (" + tarefa + ") cannot be destroyed since the AvaliacaoMetaTarefa " + avaliacoesOrphanCheckAvaliacaoMetaTarefa + " in its avaliacoes field has a non-nullable tarefa field.");
            }
            List<OrcamentoTarefa> orcamentosOrphanCheck = tarefa.getOrcamentos();
            for (OrcamentoTarefa orcamentosOrphanCheckOrcamentoTarefa : orcamentosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tarefa (" + tarefa + ") cannot be destroyed since the OrcamentoTarefa " + orcamentosOrphanCheckOrcamentoTarefa + " in its orcamentos field has a non-nullable tarefa field.");
            }
            List<ApontamentoTarefa> apontamentosOrphanCheck = tarefa.getApontamentos();
            for (ApontamentoTarefa apontamentosOrphanCheckApontamentoTarefa : apontamentosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tarefa (" + tarefa + ") cannot be destroyed since the ApontamentoTarefa " + apontamentosOrphanCheckApontamentoTarefa + " in its apontamentos field has a non-nullable tarefa field.");
            }
            List<AnexoTarefa> anexosOrphanCheck = tarefa.getAnexos();
            for (AnexoTarefa anexosOrphanCheckAnexoTarefa : anexosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tarefa (" + tarefa + ") cannot be destroyed since the AnexoTarefa " + anexosOrphanCheckAnexoTarefa + " in its anexos field has a non-nullable tarefa field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            PrioridadeTarefa prioridade = tarefa.getPrioridade();
            if (prioridade != null) {
                prioridade.getTarefas().remove(tarefa);
                prioridade = em.merge(prioridade);
            }
            CentroCusto centroCusto = tarefa.getCentroCusto();
            if (centroCusto != null) {
                centroCusto.getTarefas().remove(tarefa);
                centroCusto = em.merge(centroCusto);
            }
            Departamento departamento = tarefa.getDepartamento();
            if (departamento != null) {
                departamento.getTarefas().remove(tarefa);
                departamento = em.merge(departamento);
            }
            Empresa empresa = tarefa.getEmpresa();
            if (empresa != null) {
                empresa.getTarefas().remove(tarefa);
                empresa = em.merge(empresa);
            }
            EmpresaCliente empresaCliente = tarefa.getEmpresaCliente();
            if (empresaCliente != null) {
                empresaCliente.getTarefas().remove(tarefa);
                empresaCliente = em.merge(empresaCliente);
            }
            Tarefa tarefaPai = tarefa.getTarefaPai();
            if (tarefaPai != null) {
                tarefaPai.getSubTarefas().remove(tarefa);
                tarefaPai = em.merge(tarefaPai);
            }
            Usuario usuarioInclusao = tarefa.getUsuarioInclusao();
            if (usuarioInclusao != null) {
                usuarioInclusao.getTarefasIncluidas().remove(tarefa);
                usuarioInclusao = em.merge(usuarioInclusao);
            }
            Usuario usuarioSolicitante = tarefa.getUsuarioSolicitante();
            if (usuarioSolicitante != null) {
                usuarioSolicitante.getTarefasIncluidas().remove(tarefa);
                usuarioSolicitante = em.merge(usuarioSolicitante);
            }
            Usuario usuarioResponsavel = tarefa.getUsuarioResponsavel();
            if (usuarioResponsavel != null) {
                usuarioResponsavel.getTarefasIncluidas().remove(tarefa);
                usuarioResponsavel = em.merge(usuarioResponsavel);
            }
            StatusTarefa status = tarefa.getStatus();
            if (status != null) {
                status.getTarefas().remove(tarefa);
                status = em.merge(status);
            }
            Tarefa proximaTarefa = tarefa.getProximaTarefa();
            if (proximaTarefa != null) {
                proximaTarefa.getSubTarefas().remove(tarefa);
                proximaTarefa = em.merge(proximaTarefa);
            }
            TipoTarefa tipo = tarefa.getTipo();
            if (tipo != null) {
                tipo.getTarefas().remove(tarefa);
                tipo = em.merge(tipo);
            }
            List<Tarefa> subTarefas = tarefa.getSubTarefas();
            for (Tarefa subTarefasTarefa : subTarefas) {
                subTarefasTarefa.setTarefaPai(null);
                subTarefasTarefa = em.merge(subTarefasTarefa);
            }
            em.remove(tarefa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tarefa> findTarefaEntities() {
        return findTarefaEntities(true, -1, -1);
    }

    public List<Tarefa> findTarefaEntities(int maxResults, int firstResult) {
        return findTarefaEntities(false, maxResults, firstResult);
    }

    private List<Tarefa> findTarefaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tarefa.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Tarefa findTarefa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tarefa.class, id);
        } finally {
            em.close();
        }
    }

    public int getTarefaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tarefa> rt = cq.from(Tarefa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    
}
