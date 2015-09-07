/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.CentroCusto;
import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.FilialEmpresa;
import com.saax.gestorweb.model.datamodel.HierarquiaProjeto;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.Participante;
import com.saax.gestorweb.model.datamodel.PrioridadeMeta;
import com.saax.gestorweb.model.datamodel.ProjecaoTarefa;
import com.saax.gestorweb.model.datamodel.StatusMeta;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.presenter.DashboardPresenter;
import com.saax.gestorweb.util.GestorEntityManagerProvider;

import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.SessionAttributesEnum;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Classe de negócios do cadastro de Metas <br><br>
 *
 * Esta classe é responsável por implementar todas as regras de negócio do
 * cadastro de metas para isto ela acessa (pede ajuda) as classes de DAO<br>
 *
 * @author Rodrigo
 */
public class MetaModel {

    /**
     * Cria uma nova meta na categoria informada, com valores default
     *
     * @param categoria
     * @param loggedUser
     * @return a meta criada
     */
    public static Meta criarNovaMeta(HierarquiaProjetoDetalhe categoria, Usuario loggedUser) {
        Meta meta = new Meta();
        meta.setCategoria(categoria);
        meta.setDataHoraInclusao(LocalDateTime.now());
        meta.setUsuarioSolicitante(loggedUser);
        meta.setUsuarioInclusao(loggedUser);
        meta.setStatus(StatusMeta.NAO_INICIADA);
        meta.setPrioridade(PrioridadeMeta.ALTA);
        return meta;

    }

    /**
     * Listar todos os usuários ativos da mesma empresa do usuário logado Delega
     * chamada ao model responsavel (UsuarioModel)
     *
     * @return
     */
    public static List<Usuario> listarUsuariosEmpresa() {
        return UsuarioModel.listarUsuariosEmpresa();
    }

    /**
     * Delega chamada ao model responsavel (EmpresaModel)
     *
     * @param loggedUser
     * @return lista de EmpresaCliente
     */
    public static List<EmpresaCliente> listarEmpresasCliente(Usuario loggedUser) {
        return EmpresaModel.listarEmpresasCliente(loggedUser);
    }

    /**
     * Delega chamada ao model responsavel (EmpresaModel)
     *
     * @param empresa
     * @return
     */
    public static List<Departamento> obterListaDepartamentosAtivos(Empresa empresa) {
        return EmpresaModel.obterListaDepartamentosAtivos(empresa);
    }

    /**
     * Delega chamada ao model responsavel (EmpresaModel)
     *
     * @param empresa
     * @return
     */
    public static List<CentroCusto> obterListaCentroCustosAtivos(Empresa empresa) {
        return EmpresaModel.obterListaCentroCustosAtivos(empresa);
    }

    /**
     * Persiste (Grava) uma meta
     *
     * @param meta
     * @return a meta gravada se sucesso, null caso contrario
     *
     */
    public static Meta gravarMeta(Meta meta) {
        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        if (meta == null) {
            throw new IllegalArgumentException("Meta NULA para persistencia");
        }

        try {

            // so abre transação na gravacao da tarefa pai
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }

            if (meta.getId() == null) {
                em.persist(meta);
            } else {
                em.merge(meta);
            }

            em.getTransaction().commit();

        } catch (RuntimeException ex) {
            // Caso a persistencia falhe, efetua rollback no banco
            if (GestorEntityManagerProvider.getEntityManager().getTransaction().isActive()) {
                GestorEntityManagerProvider.getEntityManager().getTransaction().rollback();
            }
            // propaga a exceção pra cima
            throw ex;
        }

        return meta;
    }

    /**
     * Given a Target category, searches and returns its firsts tasks
     * categories.
     * <br>
     * For example, given the structure below, its'll returns Task Type 1 and 2:
     * <br>
     * <dl>
     * <dt>Target</dt>
     * <dd>
     * <dl>
     * <dt>Task Type 1</dt>
     * <dd>
     * <dl>
     * <dt>SubTask Type 1b</dt>
     * <dt>SubTask Type 1c</dt>
     * </dl>
     * </dd>
     * <dt>Task Type 2</dt>
     * <dd>
     * <dl>
     * <dt>SubTask Type 2a</dt>
     * <dt>SubTask Type 2b</dt>
     * </dl>
     * </dd>
     * </dl>
     * </dd>
     * </dl>
     *
     * @param TargetCategory of the Target
     * @return a list of categories with every task category child of the give
     * TargetCategory
     */
    public static List<HierarquiaProjetoDetalhe> getFirstsTaskCategories(HierarquiaProjetoDetalhe TargetCategory) {

        // the task's categories
        List<HierarquiaProjetoDetalhe> tasksCategories = new ArrayList<>();

        // retrieves the category set of the Target category
        HierarquiaProjeto categorySet = TargetCategory.getHierarquia();

        // the task level into the category set
        final int TASK_LEVEL = 2;

        // gets every level 2 category an puts into tasksCategories
        for (HierarquiaProjetoDetalhe category : categorySet.getCategorias()) {
            if (category.getNivel() == TASK_LEVEL) {
                tasksCategories.add(category);
            }
        }

        return tasksCategories;
    }

    public static Participante criarParticipante(Usuario usuario, Meta meta) {

        Usuario loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO);

        Participante participanteTarefa = new Participante();
        participanteTarefa.setMeta(meta);
        participanteTarefa.setUsuarioInclusao(loggedUser);
        participanteTarefa.setUsuarioParticipante(usuario);
        participanteTarefa.setDataHoraInclusao(LocalDateTime.now());

        return participanteTarefa;
    }

    public static boolean userHasAccessToTarget(Usuario loggedUser, Meta target) {

        if (target.getUsuarioRemocao()!=null) {
            return true;
        }
        if (target.getUsuarioInclusao().equals(loggedUser)) {
            return true;
        }
        if (target.getUsuarioResponsavel().equals(loggedUser)) {
            return true;
        }
        if (target.getUsuarioSolicitante().equals(loggedUser)) {
            return true;
        }

        List<Usuario> followers = new ArrayList<>();

        for (Participante pt : target.getParticipantes()) {
            followers.add(pt.getUsuarioParticipante());
        }
        if (followers.contains(loggedUser)) {
            return true;
        }

        return false;

    }

    public static List<Meta> filtrarMetas(DashboardPresenter.TipoPesquisa tipoPesquisa, List<Usuario> usuariosResponsaveis, List<Usuario> usuariosSolicitantes, List<Usuario> usuariosParticipantes, List<Empresa> empresas, List<FilialEmpresa> filiais, LocalDate dataFim, List<ProjecaoTarefa> projecoes, Usuario loggedUser) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        final List<Meta> metasUsuarioResponsavel = new ArrayList<>();

        for (Usuario usuarioResponsavel : usuariosResponsaveis) {

            usuarioResponsavel = em.find(Usuario.class, usuarioResponsavel.getId());
            metasUsuarioResponsavel.addAll(usuarioResponsavel.getMetasSobResponsabilidade());

        }

        List<Meta> metasUsuarioSolicitante = new ArrayList<>();
        for (Usuario usuariosSolicitante : usuariosSolicitantes) {
            usuariosSolicitante = em.find(Usuario.class, usuariosSolicitante.getId());
            metasUsuarioSolicitante.addAll(usuariosSolicitante.getMetasSolicitadas());
        }

        List<Meta> metasUsuariosParticipantes = new ArrayList<>();
        for (Usuario usuarioParticipante : usuariosParticipantes) {
            usuarioParticipante = em.find(Usuario.class, usuarioParticipante.getId());

            for (Participante participanteMeta : usuarioParticipante.getTarefasParticipantes()) {
                if (participanteMeta.getMeta() != null) {
                    metasUsuariosParticipantes.add(participanteMeta.getMeta());
                }
            }
        }

        List<Meta> metasEmpresa = new ArrayList<>();
        for (Empresa empresa : empresas) {
            empresa = em.find(Empresa.class, empresa.getId());
            metasEmpresa.addAll(empresa.getMetas());
        }

        List<Meta> metasFiliais = new ArrayList<>();
        for (FilialEmpresa filial : filiais) {
            filial = em.find(FilialEmpresa.class, filial.getId());
            metasFiliais.addAll(filial.getMetas());
        }

        List<Meta> metasDataFim = new ArrayList<>();
        if (dataFim != null) {

            metasDataFim.addAll(em.createNamedQuery("Meta.findByDataFim")
                    .setParameter("datafim", dataFim)
                    .getResultList());
        }

        List<Meta> metas = new ArrayList<>();
        if (tipoPesquisa == DashboardPresenter.TipoPesquisa.INCLUSIVA_OU) {

            metas.addAll(metasUsuarioResponsavel);

            metas.addAll(metasUsuarioSolicitante);

            metas.addAll(metasUsuariosParticipantes);

            metas.addAll(metasEmpresa);

            metas.addAll(metasFiliais);

            metas.addAll(metasDataFim);

        } else if (tipoPesquisa == DashboardPresenter.TipoPesquisa.EXCLUSIVA_E) {

            metas.addAll(em.createNamedQuery("Meta.findAll")
                    .setParameter("empresa", loggedUser.getEmpresaAtiva())
                    .getResultList());

            if (!metasUsuarioResponsavel.isEmpty()) {
                metas.retainAll(metasUsuarioResponsavel);
            }
            if (!metasUsuarioSolicitante.isEmpty()) {
                metas.retainAll(metasUsuarioSolicitante);
            }
            if (!metasUsuariosParticipantes.isEmpty()) {
                metas.retainAll(metasUsuariosParticipantes);
            }
            if (!metasEmpresa.isEmpty()) {
                metas.retainAll(metasEmpresa);
            }
            if (!metasFiliais.isEmpty()) {
                metas.retainAll(metasFiliais);
            }
            if (!metasDataFim.isEmpty()) {
                metas.retainAll(metasDataFim);
            }

        }

        List<Meta> result = new ArrayList<>();
        for (Meta meta : metas) {
            if (userHasAccessToTarget(loggedUser, meta)) {
                // filtra as tarefas removidas
                meta.setTarefas(LixeiraModel.filtrarTarefasRemovidas(meta.getTarefas()));
                result.add(meta);
            }
        }

        return result;
    }

    /**
     * Obtém as metas sob responsabilidade do usuário logado <br>
     * filtrando para não exibir as metas removidas <br>
     * ou as tarefas removidas
     * @param loggedUser
     * @return
     */
    public static List<Meta> listarMetas(Usuario loggedUser) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        List<Meta> metas = em.createNamedQuery("Meta.findByUsuarioResponsavelDashboard")
                .setParameter("usuarioResponsavel", loggedUser)
                .getResultList();

        metas.stream().forEach((meta) -> {
            meta.setTarefas(LixeiraModel.filtrarTarefasRemovidas(meta.getTarefas()));
        });
        return metas;

    }

    /**
     * Remove uma meta
     *
     * @param meta
     * @param usuarioLogado
     */
    public static void removerMeta(Meta meta, Usuario usuarioLogado) {

        meta.setUsuarioRemocao(usuarioLogado);
        meta.setDataHoraRemocao(LocalDateTime.now());

        if (meta.getTarefas() != null) {
            for (Tarefa subtarefa : meta.getTarefas()) {
                TarefaModel.removerTarefa(subtarefa, usuarioLogado);
            }
        }
    }

    public static List<Meta> listarMetasRemovidas(Usuario usuarioLogado) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        Empresa empresa = usuarioLogado.getEmpresaAtiva();

        Query q = em.createNamedQuery("Meta.findMetaRemovidas", Meta.class)
                .setParameter("empresa", empresa)
                .setParameter("usuarioRemocao", usuarioLogado);

        return q.getResultList();
    }

    public static void restaurarMeta(Meta meta, Usuario usuarioLogado) {
        meta.setUsuarioRemocao(null);
        meta.setDataHoraRemocao(null);

        for (Tarefa subtarefa : meta.getTarefas()) {
            TarefaModel.restaurarTarefa(subtarefa, usuarioLogado);
        }

    }

    public static Meta refresh(Meta meta) {
        if (meta.getId() != null) {
            EntityManager em = GestorEntityManagerProvider.getEntityManager();
            Meta m = em.find(Meta.class, meta.getId());
            m.setTarefas(LixeiraModel.filtrarTarefasRemovidas(m.getTarefas()));
            return m;
        } else {
            return meta;
        }

    }
}