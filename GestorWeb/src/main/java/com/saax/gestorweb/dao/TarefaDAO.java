package com.saax.gestorweb.dao;

import com.saax.gestorweb.dao.exceptions.IllegalOrphanException;
import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.saax.gestorweb.model.datamodel.PrioridadeTarefa;
import com.saax.gestorweb.model.datamodel.CentroCusto;
import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.model.datamodel.StatusTarefa;
import com.saax.gestorweb.model.datamodel.TipoTarefa;
import com.saax.gestorweb.model.datamodel.FavoritosTarefaMeta;
import java.util.ArrayList;
import java.util.List;
import com.saax.gestorweb.model.datamodel.ParicipanteTarefa;
import com.saax.gestorweb.model.datamodel.AvaliacaoMetaTarefa;
import com.saax.gestorweb.model.datamodel.OrcamentoTarefa;
import com.saax.gestorweb.model.datamodel.ApontamentoTarefa;
import com.saax.gestorweb.model.datamodel.AnexoTarefa;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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

    public void create(Tarefa tarefa) {
        if (tarefa.getFavoritados() == null) {
            tarefa.setFavoritados(new ArrayList<FavoritosTarefaMeta>());
        }
        if (tarefa.getSubTarefas() == null) {
            tarefa.setSubTarefas(new ArrayList<Tarefa>());
        }
        if (tarefa.getParicipantes() == null) {
            tarefa.setParicipantes(new ArrayList<ParicipanteTarefa>());
        }
        if (tarefa.getAvaliacoes() == null) {
            tarefa.setAvaliacoes(new ArrayList<AvaliacaoMetaTarefa>());
        }
        if (tarefa.getOrcamentos() == null) {
            tarefa.setOrcamentos(new ArrayList<OrcamentoTarefa>());
        }
        if (tarefa.getApontamentoTarefaList() == null) {
            tarefa.setApontamentoTarefaList(new ArrayList<ApontamentoTarefa>());
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
            CentroCusto idCentroCusto = tarefa.getIdCentroCusto();
            if (idCentroCusto != null) {
                idCentroCusto = em.getReference(idCentroCusto.getClass(), idCentroCusto.getId());
                tarefa.setIdCentroCusto(idCentroCusto);
            }
            Departamento idDepartamento = tarefa.getIdDepartamento();
            if (idDepartamento != null) {
                idDepartamento = em.getReference(idDepartamento.getClass(), idDepartamento.getId());
                tarefa.setIdDepartamento(idDepartamento);
            }
            Empresa idEmpresa = tarefa.getIdEmpresa();
            if (idEmpresa != null) {
                idEmpresa = em.getReference(idEmpresa.getClass(), idEmpresa.getId());
                tarefa.setIdEmpresa(idEmpresa);
            }
            EmpresaCliente empresaCliente = tarefa.getEmpresaCliente();
            if (empresaCliente != null) {
                empresaCliente = em.getReference(empresaCliente.getClass(), empresaCliente.getId());
                tarefa.setEmpresaCliente(empresaCliente);
            }
            Tarefa idTarefaPai = tarefa.getIdTarefaPai();
            if (idTarefaPai != null) {
                idTarefaPai = em.getReference(idTarefaPai.getClass(), idTarefaPai.getId());
                tarefa.setIdTarefaPai(idTarefaPai);
            }
            Usuario idUsuarioInclusao = tarefa.getIdUsuarioInclusao();
            if (idUsuarioInclusao != null) {
                idUsuarioInclusao = em.getReference(idUsuarioInclusao.getClass(), idUsuarioInclusao.getId());
                tarefa.setIdUsuarioInclusao(idUsuarioInclusao);
            }
            Usuario idUsuarioSolicitante = tarefa.getIdUsuarioSolicitante();
            if (idUsuarioSolicitante != null) {
                idUsuarioSolicitante = em.getReference(idUsuarioSolicitante.getClass(), idUsuarioSolicitante.getId());
                tarefa.setIdUsuarioSolicitante(idUsuarioSolicitante);
            }
            Usuario idUsuarioResponsavel = tarefa.getIdUsuarioResponsavel();
            if (idUsuarioResponsavel != null) {
                idUsuarioResponsavel = em.getReference(idUsuarioResponsavel.getClass(), idUsuarioResponsavel.getId());
                tarefa.setIdUsuarioResponsavel(idUsuarioResponsavel);
            }
            StatusTarefa status = tarefa.getStatus();
            if (status != null) {
                status = em.getReference(status.getClass(), status.getStatustarefa());
                tarefa.setStatus(status);
            }
            Tarefa idProximaTarefa = tarefa.getIdProximaTarefa();
            if (idProximaTarefa != null) {
                idProximaTarefa = em.getReference(idProximaTarefa.getClass(), idProximaTarefa.getId());
                tarefa.setIdProximaTarefa(idProximaTarefa);
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
            List<ParicipanteTarefa> attachedParicipantes = new ArrayList<ParicipanteTarefa>();
            for (ParicipanteTarefa paricipantesParicipanteTarefaToAttach : tarefa.getParicipantes()) {
                paricipantesParicipanteTarefaToAttach = em.getReference(paricipantesParicipanteTarefaToAttach.getClass(), paricipantesParicipanteTarefaToAttach.getId());
                attachedParicipantes.add(paricipantesParicipanteTarefaToAttach);
            }
            tarefa.setParicipantes(attachedParicipantes);
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
            List<ApontamentoTarefa> attachedApontamentoTarefaList = new ArrayList<ApontamentoTarefa>();
            for (ApontamentoTarefa apontamentoTarefaListApontamentoTarefaToAttach : tarefa.getApontamentoTarefaList()) {
                apontamentoTarefaListApontamentoTarefaToAttach = em.getReference(apontamentoTarefaListApontamentoTarefaToAttach.getClass(), apontamentoTarefaListApontamentoTarefaToAttach.getId());
                attachedApontamentoTarefaList.add(apontamentoTarefaListApontamentoTarefaToAttach);
            }
            tarefa.setApontamentoTarefaList(attachedApontamentoTarefaList);
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
            if (idCentroCusto != null) {
                idCentroCusto.getTarefas().add(tarefa);
                idCentroCusto = em.merge(idCentroCusto);
            }
            if (idDepartamento != null) {
                idDepartamento.getTarefas().add(tarefa);
                idDepartamento = em.merge(idDepartamento);
            }
            if (idEmpresa != null) {
                idEmpresa.getTarefas().add(tarefa);
                idEmpresa = em.merge(idEmpresa);
            }
            if (empresaCliente != null) {
                empresaCliente.getTarefas().add(tarefa);
                empresaCliente = em.merge(empresaCliente);
            }
            if (idTarefaPai != null) {
                idTarefaPai.getSubTarefas().add(tarefa);
                idTarefaPai = em.merge(idTarefaPai);
            }
            if (idUsuarioInclusao != null) {
                idUsuarioInclusao.getTarefaList().add(tarefa);
                idUsuarioInclusao = em.merge(idUsuarioInclusao);
            }
            if (idUsuarioSolicitante != null) {
                idUsuarioSolicitante.getTarefaList().add(tarefa);
                idUsuarioSolicitante = em.merge(idUsuarioSolicitante);
            }
            if (idUsuarioResponsavel != null) {
                idUsuarioResponsavel.getTarefaList().add(tarefa);
                idUsuarioResponsavel = em.merge(idUsuarioResponsavel);
            }
            if (status != null) {
                status.getTarefaSet().add(tarefa);
                status = em.merge(status);
            }
            if (idProximaTarefa != null) {
                idProximaTarefa.getSubTarefas().add(tarefa);
                idProximaTarefa = em.merge(idProximaTarefa);
            }
            if (tipo != null) {
                tipo.getTarefas().add(tarefa);
                tipo = em.merge(tipo);
            }
            for (FavoritosTarefaMeta favoritadosFavoritosTarefaMeta : tarefa.getFavoritados()) {
                Tarefa oldIdTarefaOfFavoritadosFavoritosTarefaMeta = favoritadosFavoritosTarefaMeta.getIdTarefa();
                favoritadosFavoritosTarefaMeta.setIdTarefa(tarefa);
                favoritadosFavoritosTarefaMeta = em.merge(favoritadosFavoritosTarefaMeta);
                if (oldIdTarefaOfFavoritadosFavoritosTarefaMeta != null) {
                    oldIdTarefaOfFavoritadosFavoritosTarefaMeta.getFavoritados().remove(favoritadosFavoritosTarefaMeta);
                    oldIdTarefaOfFavoritadosFavoritosTarefaMeta = em.merge(oldIdTarefaOfFavoritadosFavoritosTarefaMeta);
                }
            }
            for (Tarefa subTarefasTarefa : tarefa.getSubTarefas()) {
                Tarefa oldIdTarefaPaiOfSubTarefasTarefa = subTarefasTarefa.getIdTarefaPai();
                subTarefasTarefa.setIdTarefaPai(tarefa);
                subTarefasTarefa = em.merge(subTarefasTarefa);
                if (oldIdTarefaPaiOfSubTarefasTarefa != null) {
                    oldIdTarefaPaiOfSubTarefasTarefa.getSubTarefas().remove(subTarefasTarefa);
                    oldIdTarefaPaiOfSubTarefasTarefa = em.merge(oldIdTarefaPaiOfSubTarefasTarefa);
                }
            }
            for (ParicipanteTarefa paricipantesParicipanteTarefa : tarefa.getParicipantes()) {
                Tarefa oldIdTarefaOfParicipantesParicipanteTarefa = paricipantesParicipanteTarefa.getIdTarefa();
                paricipantesParicipanteTarefa.setIdTarefa(tarefa);
                paricipantesParicipanteTarefa = em.merge(paricipantesParicipanteTarefa);
                if (oldIdTarefaOfParicipantesParicipanteTarefa != null) {
                    oldIdTarefaOfParicipantesParicipanteTarefa.getParicipantes().remove(paricipantesParicipanteTarefa);
                    oldIdTarefaOfParicipantesParicipanteTarefa = em.merge(oldIdTarefaOfParicipantesParicipanteTarefa);
                }
            }
            for (AvaliacaoMetaTarefa avaliacoesAvaliacaoMetaTarefa : tarefa.getAvaliacoes()) {
                Tarefa oldIdTarefaOfAvaliacoesAvaliacaoMetaTarefa = avaliacoesAvaliacaoMetaTarefa.getIdTarefa();
                avaliacoesAvaliacaoMetaTarefa.setIdTarefa(tarefa);
                avaliacoesAvaliacaoMetaTarefa = em.merge(avaliacoesAvaliacaoMetaTarefa);
                if (oldIdTarefaOfAvaliacoesAvaliacaoMetaTarefa != null) {
                    oldIdTarefaOfAvaliacoesAvaliacaoMetaTarefa.getAvaliacoes().remove(avaliacoesAvaliacaoMetaTarefa);
                    oldIdTarefaOfAvaliacoesAvaliacaoMetaTarefa = em.merge(oldIdTarefaOfAvaliacoesAvaliacaoMetaTarefa);
                }
            }
            for (OrcamentoTarefa orcamentosOrcamentoTarefa : tarefa.getOrcamentos()) {
                Tarefa oldIdTarefaOfOrcamentosOrcamentoTarefa = orcamentosOrcamentoTarefa.getIdTarefa();
                orcamentosOrcamentoTarefa.setIdTarefa(tarefa);
                orcamentosOrcamentoTarefa = em.merge(orcamentosOrcamentoTarefa);
                if (oldIdTarefaOfOrcamentosOrcamentoTarefa != null) {
                    oldIdTarefaOfOrcamentosOrcamentoTarefa.getOrcamentos().remove(orcamentosOrcamentoTarefa);
                    oldIdTarefaOfOrcamentosOrcamentoTarefa = em.merge(oldIdTarefaOfOrcamentosOrcamentoTarefa);
                }
            }
            for (ApontamentoTarefa apontamentoTarefaListApontamentoTarefa : tarefa.getApontamentoTarefaList()) {
                Tarefa oldIdTarefaOfApontamentoTarefaListApontamentoTarefa = apontamentoTarefaListApontamentoTarefa.getIdTarefa();
                apontamentoTarefaListApontamentoTarefa.setIdTarefa(tarefa);
                apontamentoTarefaListApontamentoTarefa = em.merge(apontamentoTarefaListApontamentoTarefa);
                if (oldIdTarefaOfApontamentoTarefaListApontamentoTarefa != null) {
                    oldIdTarefaOfApontamentoTarefaListApontamentoTarefa.getApontamentoTarefaList().remove(apontamentoTarefaListApontamentoTarefa);
                    oldIdTarefaOfApontamentoTarefaListApontamentoTarefa = em.merge(oldIdTarefaOfApontamentoTarefaListApontamentoTarefa);
                }
            }
            for (AnexoTarefa anexosAnexoTarefa : tarefa.getAnexos()) {
                Tarefa oldIdTarefaOfAnexosAnexoTarefa = anexosAnexoTarefa.getIdTarefa();
                anexosAnexoTarefa.setIdTarefa(tarefa);
                anexosAnexoTarefa = em.merge(anexosAnexoTarefa);
                if (oldIdTarefaOfAnexosAnexoTarefa != null) {
                    oldIdTarefaOfAnexosAnexoTarefa.getAnexos().remove(anexosAnexoTarefa);
                    oldIdTarefaOfAnexosAnexoTarefa = em.merge(oldIdTarefaOfAnexosAnexoTarefa);
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
            CentroCusto idCentroCustoOld = persistentTarefa.getIdCentroCusto();
            CentroCusto idCentroCustoNew = tarefa.getIdCentroCusto();
            Departamento idDepartamentoOld = persistentTarefa.getIdDepartamento();
            Departamento idDepartamentoNew = tarefa.getIdDepartamento();
            Empresa idEmpresaOld = persistentTarefa.getIdEmpresa();
            Empresa idEmpresaNew = tarefa.getIdEmpresa();
            EmpresaCliente empresaClienteOld = persistentTarefa.getEmpresaCliente();
            EmpresaCliente empresaClienteNew = tarefa.getEmpresaCliente();
            Tarefa idTarefaPaiOld = persistentTarefa.getIdTarefaPai();
            Tarefa idTarefaPaiNew = tarefa.getIdTarefaPai();
            Usuario idUsuarioInclusaoOld = persistentTarefa.getIdUsuarioInclusao();
            Usuario idUsuarioInclusaoNew = tarefa.getIdUsuarioInclusao();
            Usuario idUsuarioSolicitanteOld = persistentTarefa.getIdUsuarioSolicitante();
            Usuario idUsuarioSolicitanteNew = tarefa.getIdUsuarioSolicitante();
            Usuario idUsuarioResponsavelOld = persistentTarefa.getIdUsuarioResponsavel();
            Usuario idUsuarioResponsavelNew = tarefa.getIdUsuarioResponsavel();
            StatusTarefa statusOld = persistentTarefa.getStatus();
            StatusTarefa statusNew = tarefa.getStatus();
            Tarefa idProximaTarefaOld = persistentTarefa.getIdProximaTarefa();
            Tarefa idProximaTarefaNew = tarefa.getIdProximaTarefa();
            TipoTarefa tipoOld = persistentTarefa.getTipo();
            TipoTarefa tipoNew = tarefa.getTipo();
            List<FavoritosTarefaMeta> favoritadosOld = persistentTarefa.getFavoritados();
            List<FavoritosTarefaMeta> favoritadosNew = tarefa.getFavoritados();
            List<Tarefa> subTarefasOld = persistentTarefa.getSubTarefas();
            List<Tarefa> subTarefasNew = tarefa.getSubTarefas();
            List<ParicipanteTarefa> paricipantesOld = persistentTarefa.getParicipantes();
            List<ParicipanteTarefa> paricipantesNew = tarefa.getParicipantes();
            List<AvaliacaoMetaTarefa> avaliacoesOld = persistentTarefa.getAvaliacoes();
            List<AvaliacaoMetaTarefa> avaliacoesNew = tarefa.getAvaliacoes();
            List<OrcamentoTarefa> orcamentosOld = persistentTarefa.getOrcamentos();
            List<OrcamentoTarefa> orcamentosNew = tarefa.getOrcamentos();
            List<ApontamentoTarefa> apontamentoTarefaListOld = persistentTarefa.getApontamentoTarefaList();
            List<ApontamentoTarefa> apontamentoTarefaListNew = tarefa.getApontamentoTarefaList();
            List<AnexoTarefa> anexosOld = persistentTarefa.getAnexos();
            List<AnexoTarefa> anexosNew = tarefa.getAnexos();
            List<String> illegalOrphanMessages = null;
            for (FavoritosTarefaMeta favoritadosOldFavoritosTarefaMeta : favoritadosOld) {
                if (!favoritadosNew.contains(favoritadosOldFavoritosTarefaMeta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain FavoritosTarefaMeta " + favoritadosOldFavoritosTarefaMeta + " since its idTarefa field is not nullable.");
                }
            }
            for (ParicipanteTarefa paricipantesOldParicipanteTarefa : paricipantesOld) {
                if (!paricipantesNew.contains(paricipantesOldParicipanteTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ParicipanteTarefa " + paricipantesOldParicipanteTarefa + " since its idTarefa field is not nullable.");
                }
            }
            for (AvaliacaoMetaTarefa avaliacoesOldAvaliacaoMetaTarefa : avaliacoesOld) {
                if (!avaliacoesNew.contains(avaliacoesOldAvaliacaoMetaTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AvaliacaoMetaTarefa " + avaliacoesOldAvaliacaoMetaTarefa + " since its idTarefa field is not nullable.");
                }
            }
            for (OrcamentoTarefa orcamentosOldOrcamentoTarefa : orcamentosOld) {
                if (!orcamentosNew.contains(orcamentosOldOrcamentoTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain OrcamentoTarefa " + orcamentosOldOrcamentoTarefa + " since its idTarefa field is not nullable.");
                }
            }
            for (ApontamentoTarefa apontamentoTarefaListOldApontamentoTarefa : apontamentoTarefaListOld) {
                if (!apontamentoTarefaListNew.contains(apontamentoTarefaListOldApontamentoTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ApontamentoTarefa " + apontamentoTarefaListOldApontamentoTarefa + " since its idTarefa field is not nullable.");
                }
            }
            for (AnexoTarefa anexosOldAnexoTarefa : anexosOld) {
                if (!anexosNew.contains(anexosOldAnexoTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AnexoTarefa " + anexosOldAnexoTarefa + " since its idTarefa field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (prioridadeNew != null) {
                prioridadeNew = em.getReference(prioridadeNew.getClass(), prioridadeNew.getPrioridadetarefa());
                tarefa.setPrioridade(prioridadeNew);
            }
            if (idCentroCustoNew != null) {
                idCentroCustoNew = em.getReference(idCentroCustoNew.getClass(), idCentroCustoNew.getId());
                tarefa.setIdCentroCusto(idCentroCustoNew);
            }
            if (idDepartamentoNew != null) {
                idDepartamentoNew = em.getReference(idDepartamentoNew.getClass(), idDepartamentoNew.getId());
                tarefa.setIdDepartamento(idDepartamentoNew);
            }
            if (idEmpresaNew != null) {
                idEmpresaNew = em.getReference(idEmpresaNew.getClass(), idEmpresaNew.getId());
                tarefa.setIdEmpresa(idEmpresaNew);
            }
            if (empresaClienteNew != null) {
                empresaClienteNew = em.getReference(empresaClienteNew.getClass(), empresaClienteNew.getId());
                tarefa.setEmpresaCliente(empresaClienteNew);
            }
            if (idTarefaPaiNew != null) {
                idTarefaPaiNew = em.getReference(idTarefaPaiNew.getClass(), idTarefaPaiNew.getId());
                tarefa.setIdTarefaPai(idTarefaPaiNew);
            }
            if (idUsuarioInclusaoNew != null) {
                idUsuarioInclusaoNew = em.getReference(idUsuarioInclusaoNew.getClass(), idUsuarioInclusaoNew.getId());
                tarefa.setIdUsuarioInclusao(idUsuarioInclusaoNew);
            }
            if (idUsuarioSolicitanteNew != null) {
                idUsuarioSolicitanteNew = em.getReference(idUsuarioSolicitanteNew.getClass(), idUsuarioSolicitanteNew.getId());
                tarefa.setIdUsuarioSolicitante(idUsuarioSolicitanteNew);
            }
            if (idUsuarioResponsavelNew != null) {
                idUsuarioResponsavelNew = em.getReference(idUsuarioResponsavelNew.getClass(), idUsuarioResponsavelNew.getId());
                tarefa.setIdUsuarioResponsavel(idUsuarioResponsavelNew);
            }
            if (statusNew != null) {
                statusNew = em.getReference(statusNew.getClass(), statusNew.getStatustarefa());
                tarefa.setStatus(statusNew);
            }
            if (idProximaTarefaNew != null) {
                idProximaTarefaNew = em.getReference(idProximaTarefaNew.getClass(), idProximaTarefaNew.getId());
                tarefa.setIdProximaTarefa(idProximaTarefaNew);
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
            List<ParicipanteTarefa> attachedParicipantesNew = new ArrayList<ParicipanteTarefa>();
            for (ParicipanteTarefa paricipantesNewParicipanteTarefaToAttach : paricipantesNew) {
                paricipantesNewParicipanteTarefaToAttach = em.getReference(paricipantesNewParicipanteTarefaToAttach.getClass(), paricipantesNewParicipanteTarefaToAttach.getId());
                attachedParicipantesNew.add(paricipantesNewParicipanteTarefaToAttach);
            }
            paricipantesNew = attachedParicipantesNew;
            tarefa.setParicipantes(paricipantesNew);
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
            List<ApontamentoTarefa> attachedApontamentoTarefaListNew = new ArrayList<ApontamentoTarefa>();
            for (ApontamentoTarefa apontamentoTarefaListNewApontamentoTarefaToAttach : apontamentoTarefaListNew) {
                apontamentoTarefaListNewApontamentoTarefaToAttach = em.getReference(apontamentoTarefaListNewApontamentoTarefaToAttach.getClass(), apontamentoTarefaListNewApontamentoTarefaToAttach.getId());
                attachedApontamentoTarefaListNew.add(apontamentoTarefaListNewApontamentoTarefaToAttach);
            }
            apontamentoTarefaListNew = attachedApontamentoTarefaListNew;
            tarefa.setApontamentoTarefaList(apontamentoTarefaListNew);
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
            if (idCentroCustoOld != null && !idCentroCustoOld.equals(idCentroCustoNew)) {
                idCentroCustoOld.getTarefas().remove(tarefa);
                idCentroCustoOld = em.merge(idCentroCustoOld);
            }
            if (idCentroCustoNew != null && !idCentroCustoNew.equals(idCentroCustoOld)) {
                idCentroCustoNew.getTarefas().add(tarefa);
                idCentroCustoNew = em.merge(idCentroCustoNew);
            }
            if (idDepartamentoOld != null && !idDepartamentoOld.equals(idDepartamentoNew)) {
                idDepartamentoOld.getTarefas().remove(tarefa);
                idDepartamentoOld = em.merge(idDepartamentoOld);
            }
            if (idDepartamentoNew != null && !idDepartamentoNew.equals(idDepartamentoOld)) {
                idDepartamentoNew.getTarefas().add(tarefa);
                idDepartamentoNew = em.merge(idDepartamentoNew);
            }
            if (idEmpresaOld != null && !idEmpresaOld.equals(idEmpresaNew)) {
                idEmpresaOld.getTarefas().remove(tarefa);
                idEmpresaOld = em.merge(idEmpresaOld);
            }
            if (idEmpresaNew != null && !idEmpresaNew.equals(idEmpresaOld)) {
                idEmpresaNew.getTarefas().add(tarefa);
                idEmpresaNew = em.merge(idEmpresaNew);
            }
            if (empresaClienteOld != null && !empresaClienteOld.equals(empresaClienteNew)) {
                empresaClienteOld.getTarefas().remove(tarefa);
                empresaClienteOld = em.merge(empresaClienteOld);
            }
            if (empresaClienteNew != null && !empresaClienteNew.equals(empresaClienteOld)) {
                empresaClienteNew.getTarefas().add(tarefa);
                empresaClienteNew = em.merge(empresaClienteNew);
            }
            if (idTarefaPaiOld != null && !idTarefaPaiOld.equals(idTarefaPaiNew)) {
                idTarefaPaiOld.getSubTarefas().remove(tarefa);
                idTarefaPaiOld = em.merge(idTarefaPaiOld);
            }
            if (idTarefaPaiNew != null && !idTarefaPaiNew.equals(idTarefaPaiOld)) {
                idTarefaPaiNew.getSubTarefas().add(tarefa);
                idTarefaPaiNew = em.merge(idTarefaPaiNew);
            }
            if (idUsuarioInclusaoOld != null && !idUsuarioInclusaoOld.equals(idUsuarioInclusaoNew)) {
                idUsuarioInclusaoOld.getTarefaList().remove(tarefa);
                idUsuarioInclusaoOld = em.merge(idUsuarioInclusaoOld);
            }
            if (idUsuarioInclusaoNew != null && !idUsuarioInclusaoNew.equals(idUsuarioInclusaoOld)) {
                idUsuarioInclusaoNew.getTarefaList().add(tarefa);
                idUsuarioInclusaoNew = em.merge(idUsuarioInclusaoNew);
            }
            if (idUsuarioSolicitanteOld != null && !idUsuarioSolicitanteOld.equals(idUsuarioSolicitanteNew)) {
                idUsuarioSolicitanteOld.getTarefaList().remove(tarefa);
                idUsuarioSolicitanteOld = em.merge(idUsuarioSolicitanteOld);
            }
            if (idUsuarioSolicitanteNew != null && !idUsuarioSolicitanteNew.equals(idUsuarioSolicitanteOld)) {
                idUsuarioSolicitanteNew.getTarefaList().add(tarefa);
                idUsuarioSolicitanteNew = em.merge(idUsuarioSolicitanteNew);
            }
            if (idUsuarioResponsavelOld != null && !idUsuarioResponsavelOld.equals(idUsuarioResponsavelNew)) {
                idUsuarioResponsavelOld.getTarefaList().remove(tarefa);
                idUsuarioResponsavelOld = em.merge(idUsuarioResponsavelOld);
            }
            if (idUsuarioResponsavelNew != null && !idUsuarioResponsavelNew.equals(idUsuarioResponsavelOld)) {
                idUsuarioResponsavelNew.getTarefaList().add(tarefa);
                idUsuarioResponsavelNew = em.merge(idUsuarioResponsavelNew);
            }
            if (statusOld != null && !statusOld.equals(statusNew)) {
                statusOld.getTarefaSet().remove(tarefa);
                statusOld = em.merge(statusOld);
            }
            if (statusNew != null && !statusNew.equals(statusOld)) {
                statusNew.getTarefaSet().add(tarefa);
                statusNew = em.merge(statusNew);
            }
            if (idProximaTarefaOld != null && !idProximaTarefaOld.equals(idProximaTarefaNew)) {
                idProximaTarefaOld.getSubTarefas().remove(tarefa);
                idProximaTarefaOld = em.merge(idProximaTarefaOld);
            }
            if (idProximaTarefaNew != null && !idProximaTarefaNew.equals(idProximaTarefaOld)) {
                idProximaTarefaNew.getSubTarefas().add(tarefa);
                idProximaTarefaNew = em.merge(idProximaTarefaNew);
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
                    Tarefa oldIdTarefaOfFavoritadosNewFavoritosTarefaMeta = favoritadosNewFavoritosTarefaMeta.getIdTarefa();
                    favoritadosNewFavoritosTarefaMeta.setIdTarefa(tarefa);
                    favoritadosNewFavoritosTarefaMeta = em.merge(favoritadosNewFavoritosTarefaMeta);
                    if (oldIdTarefaOfFavoritadosNewFavoritosTarefaMeta != null && !oldIdTarefaOfFavoritadosNewFavoritosTarefaMeta.equals(tarefa)) {
                        oldIdTarefaOfFavoritadosNewFavoritosTarefaMeta.getFavoritados().remove(favoritadosNewFavoritosTarefaMeta);
                        oldIdTarefaOfFavoritadosNewFavoritosTarefaMeta = em.merge(oldIdTarefaOfFavoritadosNewFavoritosTarefaMeta);
                    }
                }
            }
            for (Tarefa subTarefasOldTarefa : subTarefasOld) {
                if (!subTarefasNew.contains(subTarefasOldTarefa)) {
                    subTarefasOldTarefa.setIdTarefaPai(null);
                    subTarefasOldTarefa = em.merge(subTarefasOldTarefa);
                }
            }
            for (Tarefa subTarefasNewTarefa : subTarefasNew) {
                if (!subTarefasOld.contains(subTarefasNewTarefa)) {
                    Tarefa oldIdTarefaPaiOfSubTarefasNewTarefa = subTarefasNewTarefa.getIdTarefaPai();
                    subTarefasNewTarefa.setIdTarefaPai(tarefa);
                    subTarefasNewTarefa = em.merge(subTarefasNewTarefa);
                    if (oldIdTarefaPaiOfSubTarefasNewTarefa != null && !oldIdTarefaPaiOfSubTarefasNewTarefa.equals(tarefa)) {
                        oldIdTarefaPaiOfSubTarefasNewTarefa.getSubTarefas().remove(subTarefasNewTarefa);
                        oldIdTarefaPaiOfSubTarefasNewTarefa = em.merge(oldIdTarefaPaiOfSubTarefasNewTarefa);
                    }
                }
            }
            for (ParicipanteTarefa paricipantesNewParicipanteTarefa : paricipantesNew) {
                if (!paricipantesOld.contains(paricipantesNewParicipanteTarefa)) {
                    Tarefa oldIdTarefaOfParicipantesNewParicipanteTarefa = paricipantesNewParicipanteTarefa.getIdTarefa();
                    paricipantesNewParicipanteTarefa.setIdTarefa(tarefa);
                    paricipantesNewParicipanteTarefa = em.merge(paricipantesNewParicipanteTarefa);
                    if (oldIdTarefaOfParicipantesNewParicipanteTarefa != null && !oldIdTarefaOfParicipantesNewParicipanteTarefa.equals(tarefa)) {
                        oldIdTarefaOfParicipantesNewParicipanteTarefa.getParicipantes().remove(paricipantesNewParicipanteTarefa);
                        oldIdTarefaOfParicipantesNewParicipanteTarefa = em.merge(oldIdTarefaOfParicipantesNewParicipanteTarefa);
                    }
                }
            }
            for (AvaliacaoMetaTarefa avaliacoesNewAvaliacaoMetaTarefa : avaliacoesNew) {
                if (!avaliacoesOld.contains(avaliacoesNewAvaliacaoMetaTarefa)) {
                    Tarefa oldIdTarefaOfAvaliacoesNewAvaliacaoMetaTarefa = avaliacoesNewAvaliacaoMetaTarefa.getIdTarefa();
                    avaliacoesNewAvaliacaoMetaTarefa.setIdTarefa(tarefa);
                    avaliacoesNewAvaliacaoMetaTarefa = em.merge(avaliacoesNewAvaliacaoMetaTarefa);
                    if (oldIdTarefaOfAvaliacoesNewAvaliacaoMetaTarefa != null && !oldIdTarefaOfAvaliacoesNewAvaliacaoMetaTarefa.equals(tarefa)) {
                        oldIdTarefaOfAvaliacoesNewAvaliacaoMetaTarefa.getAvaliacoes().remove(avaliacoesNewAvaliacaoMetaTarefa);
                        oldIdTarefaOfAvaliacoesNewAvaliacaoMetaTarefa = em.merge(oldIdTarefaOfAvaliacoesNewAvaliacaoMetaTarefa);
                    }
                }
            }
            for (OrcamentoTarefa orcamentosNewOrcamentoTarefa : orcamentosNew) {
                if (!orcamentosOld.contains(orcamentosNewOrcamentoTarefa)) {
                    Tarefa oldIdTarefaOfOrcamentosNewOrcamentoTarefa = orcamentosNewOrcamentoTarefa.getIdTarefa();
                    orcamentosNewOrcamentoTarefa.setIdTarefa(tarefa);
                    orcamentosNewOrcamentoTarefa = em.merge(orcamentosNewOrcamentoTarefa);
                    if (oldIdTarefaOfOrcamentosNewOrcamentoTarefa != null && !oldIdTarefaOfOrcamentosNewOrcamentoTarefa.equals(tarefa)) {
                        oldIdTarefaOfOrcamentosNewOrcamentoTarefa.getOrcamentos().remove(orcamentosNewOrcamentoTarefa);
                        oldIdTarefaOfOrcamentosNewOrcamentoTarefa = em.merge(oldIdTarefaOfOrcamentosNewOrcamentoTarefa);
                    }
                }
            }
            for (ApontamentoTarefa apontamentoTarefaListNewApontamentoTarefa : apontamentoTarefaListNew) {
                if (!apontamentoTarefaListOld.contains(apontamentoTarefaListNewApontamentoTarefa)) {
                    Tarefa oldIdTarefaOfApontamentoTarefaListNewApontamentoTarefa = apontamentoTarefaListNewApontamentoTarefa.getIdTarefa();
                    apontamentoTarefaListNewApontamentoTarefa.setIdTarefa(tarefa);
                    apontamentoTarefaListNewApontamentoTarefa = em.merge(apontamentoTarefaListNewApontamentoTarefa);
                    if (oldIdTarefaOfApontamentoTarefaListNewApontamentoTarefa != null && !oldIdTarefaOfApontamentoTarefaListNewApontamentoTarefa.equals(tarefa)) {
                        oldIdTarefaOfApontamentoTarefaListNewApontamentoTarefa.getApontamentoTarefaList().remove(apontamentoTarefaListNewApontamentoTarefa);
                        oldIdTarefaOfApontamentoTarefaListNewApontamentoTarefa = em.merge(oldIdTarefaOfApontamentoTarefaListNewApontamentoTarefa);
                    }
                }
            }
            for (AnexoTarefa anexosNewAnexoTarefa : anexosNew) {
                if (!anexosOld.contains(anexosNewAnexoTarefa)) {
                    Tarefa oldIdTarefaOfAnexosNewAnexoTarefa = anexosNewAnexoTarefa.getIdTarefa();
                    anexosNewAnexoTarefa.setIdTarefa(tarefa);
                    anexosNewAnexoTarefa = em.merge(anexosNewAnexoTarefa);
                    if (oldIdTarefaOfAnexosNewAnexoTarefa != null && !oldIdTarefaOfAnexosNewAnexoTarefa.equals(tarefa)) {
                        oldIdTarefaOfAnexosNewAnexoTarefa.getAnexos().remove(anexosNewAnexoTarefa);
                        oldIdTarefaOfAnexosNewAnexoTarefa = em.merge(oldIdTarefaOfAnexosNewAnexoTarefa);
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
                illegalOrphanMessages.add("This Tarefa (" + tarefa + ") cannot be destroyed since the FavoritosTarefaMeta " + favoritadosOrphanCheckFavoritosTarefaMeta + " in its favoritados field has a non-nullable idTarefa field.");
            }
            List<ParicipanteTarefa> paricipantesOrphanCheck = tarefa.getParicipantes();
            for (ParicipanteTarefa paricipantesOrphanCheckParicipanteTarefa : paricipantesOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tarefa (" + tarefa + ") cannot be destroyed since the ParicipanteTarefa " + paricipantesOrphanCheckParicipanteTarefa + " in its paricipantes field has a non-nullable idTarefa field.");
            }
            List<AvaliacaoMetaTarefa> avaliacoesOrphanCheck = tarefa.getAvaliacoes();
            for (AvaliacaoMetaTarefa avaliacoesOrphanCheckAvaliacaoMetaTarefa : avaliacoesOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tarefa (" + tarefa + ") cannot be destroyed since the AvaliacaoMetaTarefa " + avaliacoesOrphanCheckAvaliacaoMetaTarefa + " in its avaliacoes field has a non-nullable idTarefa field.");
            }
            List<OrcamentoTarefa> orcamentosOrphanCheck = tarefa.getOrcamentos();
            for (OrcamentoTarefa orcamentosOrphanCheckOrcamentoTarefa : orcamentosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tarefa (" + tarefa + ") cannot be destroyed since the OrcamentoTarefa " + orcamentosOrphanCheckOrcamentoTarefa + " in its orcamentos field has a non-nullable idTarefa field.");
            }
            List<ApontamentoTarefa> apontamentoTarefaListOrphanCheck = tarefa.getApontamentoTarefaList();
            for (ApontamentoTarefa apontamentoTarefaListOrphanCheckApontamentoTarefa : apontamentoTarefaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tarefa (" + tarefa + ") cannot be destroyed since the ApontamentoTarefa " + apontamentoTarefaListOrphanCheckApontamentoTarefa + " in its apontamentoTarefaList field has a non-nullable idTarefa field.");
            }
            List<AnexoTarefa> anexosOrphanCheck = tarefa.getAnexos();
            for (AnexoTarefa anexosOrphanCheckAnexoTarefa : anexosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tarefa (" + tarefa + ") cannot be destroyed since the AnexoTarefa " + anexosOrphanCheckAnexoTarefa + " in its anexos field has a non-nullable idTarefa field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            PrioridadeTarefa prioridade = tarefa.getPrioridade();
            if (prioridade != null) {
                prioridade.getTarefas().remove(tarefa);
                prioridade = em.merge(prioridade);
            }
            CentroCusto idCentroCusto = tarefa.getIdCentroCusto();
            if (idCentroCusto != null) {
                idCentroCusto.getTarefas().remove(tarefa);
                idCentroCusto = em.merge(idCentroCusto);
            }
            Departamento idDepartamento = tarefa.getIdDepartamento();
            if (idDepartamento != null) {
                idDepartamento.getTarefas().remove(tarefa);
                idDepartamento = em.merge(idDepartamento);
            }
            Empresa idEmpresa = tarefa.getIdEmpresa();
            if (idEmpresa != null) {
                idEmpresa.getTarefas().remove(tarefa);
                idEmpresa = em.merge(idEmpresa);
            }
            EmpresaCliente empresaCliente = tarefa.getEmpresaCliente();
            if (empresaCliente != null) {
                empresaCliente.getTarefas().remove(tarefa);
                empresaCliente = em.merge(empresaCliente);
            }
            Tarefa idTarefaPai = tarefa.getIdTarefaPai();
            if (idTarefaPai != null) {
                idTarefaPai.getSubTarefas().remove(tarefa);
                idTarefaPai = em.merge(idTarefaPai);
            }
            Usuario idUsuarioInclusao = tarefa.getIdUsuarioInclusao();
            if (idUsuarioInclusao != null) {
                idUsuarioInclusao.getTarefaList().remove(tarefa);
                idUsuarioInclusao = em.merge(idUsuarioInclusao);
            }
            Usuario idUsuarioSolicitante = tarefa.getIdUsuarioSolicitante();
            if (idUsuarioSolicitante != null) {
                idUsuarioSolicitante.getTarefaList().remove(tarefa);
                idUsuarioSolicitante = em.merge(idUsuarioSolicitante);
            }
            Usuario idUsuarioResponsavel = tarefa.getIdUsuarioResponsavel();
            if (idUsuarioResponsavel != null) {
                idUsuarioResponsavel.getTarefaList().remove(tarefa);
                idUsuarioResponsavel = em.merge(idUsuarioResponsavel);
            }
            StatusTarefa status = tarefa.getStatus();
            if (status != null) {
                status.getTarefaSet().remove(tarefa);
                status = em.merge(status);
            }
            Tarefa idProximaTarefa = tarefa.getIdProximaTarefa();
            if (idProximaTarefa != null) {
                idProximaTarefa.getSubTarefas().remove(tarefa);
                idProximaTarefa = em.merge(idProximaTarefa);
            }
            TipoTarefa tipo = tarefa.getTipo();
            if (tipo != null) {
                tipo.getTarefas().remove(tarefa);
                tipo = em.merge(tipo);
            }
            List<Tarefa> subTarefas = tarefa.getSubTarefas();
            for (Tarefa subTarefasTarefa : subTarefas) {
                subTarefasTarefa.setIdTarefaPai(null);
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
