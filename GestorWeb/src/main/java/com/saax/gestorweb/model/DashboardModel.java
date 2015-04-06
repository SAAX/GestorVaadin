package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.FilialEmpresa;
import com.saax.gestorweb.model.datamodel.HierarquiaProjeto;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.ProjecaoTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.presenter.DashboardPresenter;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import com.saax.gestorweb.util.GestorSession;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.commons.beanutils.BeanUtils;

/**
 * Classe de negócios do Dasboard
 *
 * @author Rodrigo
 */
public class DashboardModel {

    
        // Classes do modelo acessórias acessadas por este model
    private final UsuarioModel usuarioModel;
    private final EmpresaModel empresaModel;

    public DashboardModel() {
        usuarioModel = new UsuarioModel();
        empresaModel = new EmpresaModel();
        
    }


    /**
     * Obtém as tarefas sob responsabilidade do usuário logado
     *
     * @param loggedUser
     * @return
     */
    public List<Tarefa> listarTarefas(Usuario loggedUser) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        List<Tarefa> tarefas = em.createNamedQuery("Tarefa.findByUsuarioResponsavelDashboard")
                .setParameter("usuarioResponsavel", loggedUser)
                //.setParameter("empresa", loggedUser.getEmpresaAtiva())
                .getResultList();

        return tarefas;

    }
    
    /**
     * Obtém as metas sob responsabilidade do usuário logado
     *
     * @param loggedUser
     * @return
     */
    public List<Meta> listarMetas(Usuario loggedUser) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        List<Meta> metas = em.createNamedQuery("Meta.findByUsuarioResponsavelDashboard")
                .setParameter("usuarioResponsavel", loggedUser)
                //.setParameter("empresa", loggedUser.getEmpresaAtiva())
                .getResultList();

        return metas;

    }

    /**
     * Listar todos os usuários ativos da mesma empresa do usuário logado
     *
     * @return
     */
    public List<Usuario> listarUsuariosEmpresa() {
        return usuarioModel.listarUsuariosEmpresa();
    }

    /**
     * Lista as tarefas que correspondam aos filtros informados
     *
     * @param tipoPesquisa
     * @param usuariosResponsaveis
     * @param usuariosSolicitantes
     * @param usuariosParticipantes
     * @param empresas
     * @param filiais
     * @param dataFim
     * @param projecoes
     * @return
     */
    public List<Tarefa> filtrarTarefas(DashboardPresenter.TipoPesquisa tipoPesquisa, List<Usuario> usuariosResponsaveis,
            List<Usuario> usuariosSolicitantes, List<Usuario> usuariosParticipantes, List<Empresa> empresas, List<FilialEmpresa> filiais, LocalDate dataFim, List<ProjecaoTarefa> projecoes, Usuario loggedUser) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        final List<Tarefa> tarefasUsuarioResponsavel = new ArrayList<>();
        usuariosResponsaveis.stream().forEach((usuario) -> {
            // refresh
            usuario = em.find(Usuario.class, usuario.getId());
            tarefasUsuarioResponsavel.addAll(usuario.getTarefasSobResponsabilidade());

        });

        List<Tarefa> tarefasUsuarioSolicitante = new ArrayList<>();
        usuariosSolicitantes.stream().forEach((usuario) -> {
            usuario = em.find(Usuario.class, usuario.getId());
            tarefasUsuarioSolicitante.addAll(usuario.getTarefasSolicitadas());
        });

        List<Tarefa> tarefasUsuariosParticipantes = new ArrayList<>();
        for (Usuario usuario : usuariosParticipantes) {
            usuario = em.find(Usuario.class, usuario.getId());
            usuario.getTarefasParticipantes().stream().forEach((participanteTarefa) -> {
                tarefasUsuariosParticipantes.add(participanteTarefa.getTarefa());
            });
        }

        List<Tarefa> tarefasEmpresa = new ArrayList<>();
        empresas.stream().forEach((empresa) -> {
            empresa = em.find(Empresa.class, empresa.getId());
            tarefasEmpresa.addAll(empresa.getTarefas());
        });

        List<Tarefa> tarefasFiliais = new ArrayList<>();
        filiais.stream().forEach((filial) -> {
            filial = em.find(FilialEmpresa.class, filial.getId());
            tarefasFiliais.addAll(filial.getTarefas());
        });

        List<Tarefa> tarefasDataFim = new ArrayList<>();
        if (dataFim != null) {

            tarefasDataFim.addAll(em.createNamedQuery("Tarefa.findByDataFim")
                    .setParameter("dataFim", dataFim)
                    .getResultList());
        }

        List<Tarefa> tarefasProjecao = new ArrayList<>();
        projecoes.stream().forEach((projecao) -> {
            tarefasProjecao.addAll(
                    em.createNamedQuery("Tarefa.findByProjecao")
                    .setParameter("projecao", projecao)
                    .getResultList());
        });

        List<Tarefa> tarefas = new ArrayList<>();
        if (tipoPesquisa == DashboardPresenter.TipoPesquisa.INCLUSIVA_OU) {

            tarefas.addAll(tarefasUsuarioResponsavel);

            tarefas.addAll(tarefasUsuarioSolicitante);

            tarefas.addAll(tarefasUsuariosParticipantes);

            tarefas.addAll(tarefasEmpresa);

            tarefas.addAll(tarefasFiliais);

            tarefas.addAll(tarefasDataFim);

            tarefas.addAll(tarefasProjecao);

        } else if (tipoPesquisa == DashboardPresenter.TipoPesquisa.EXCLUSIVA_E) {

            tarefas.addAll(em.createNamedQuery("Tarefa.findAll")
                    .setParameter("empresa",loggedUser.getEmpresaAtiva())
                    .getResultList());

            if (!tarefasUsuarioResponsavel.isEmpty()) {
                tarefas.retainAll(tarefasUsuarioResponsavel);
            }
            if (!tarefasUsuarioSolicitante.isEmpty()) {
                tarefas.retainAll(tarefasUsuarioSolicitante);
            }
            if (!tarefasUsuariosParticipantes.isEmpty()) {
                tarefas.retainAll(tarefasUsuariosParticipantes);
            }
            if (!tarefasEmpresa.isEmpty()) {
                tarefas.retainAll(tarefasEmpresa);
            }
            if (!tarefasFiliais.isEmpty()) {
                tarefas.retainAll(tarefasFiliais);
            }
            if (!tarefasDataFim.isEmpty()) {
                tarefas.retainAll(tarefasDataFim);
            }
            if (!tarefasProjecao.isEmpty()) {
                tarefas.retainAll(tarefasProjecao);
            }

        }

        return tarefas;
    }

    /**
     * Obtém as tarefas solicitadas pelo usuário logado, ordenadas por data FIM
     *
     * @param loggedUser
     * @return
     */
    public List<Tarefa> listarTarefasPrincipais(Usuario loggedUser) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        Empresa empresa = loggedUser.getEmpresaAtiva();

        String sql = "SELECT t FROM Tarefa t WHERE t.empresa = :empresa AND  t.usuarioSolicitante = :usuarioSolicitante ORDER BY t.dataFim DESC";

        Query q = em.createQuery(sql)
                .setParameter("empresa", empresa)
                .setParameter("usuarioSolicitante", loggedUser);

        List<Tarefa> tarefas = q.getResultList();

        return tarefas;

    }

    public List<Tarefa> getTarefasTemplate() {
        Usuario loggedUser = (Usuario) GestorSession.getAttribute("loggedUser");
        List<Tarefa> templates = GestorEntityManagerProvider.getEntityManager().createNamedQuery("Tarefa.findByTemplate", Tarefa.class)
                .setParameter("empresa", loggedUser.getEmpresaAtiva())
                .setParameter("template", true)
                .getResultList();

        for (Empresa subEmpresa : loggedUser.getEmpresaAtiva().getSubEmpresas()) {
            templates.addAll(
                    GestorEntityManagerProvider.getEntityManager().createNamedQuery("Tarefa.findByTemplate", Tarefa.class)
                    .setParameter("empresa", subEmpresa)
                    .setParameter("template", true)
                    .getResultList());
        }

        return templates;
    }

    /**
     * Obtem as hierarquias que podem ser selecionadas pelo usuário (até nível 2)
     * @return 
     */
    public List<HierarquiaProjeto> getHierarquiasProjeto() {
        
        
        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        
        // Obtem as hiearquias genericas (de todas as empresas)
        List<HierarquiaProjeto> hierarquiasGenericas = em.createNamedQuery("HierarquiaProjeto.findAllDefault")
                .getResultList();

        // Obtem as hiearquias da empresa do usuário logad
        Usuario loggedUser = (Usuario) GestorSession.getAttribute("loggedUser");
        List<HierarquiaProjeto> hierarquiasEmpresa = em.createNamedQuery("HierarquiaProjeto.findByEmpresa")
                .setParameter("empresa", loggedUser.getEmpresaAtiva())
                .getResultList();
        
        List<HierarquiaProjeto> hierarquiasCadastradas = new ArrayList<>();
        hierarquiasCadastradas.addAll(hierarquiasGenericas);
        hierarquiasCadastradas.addAll(hierarquiasEmpresa);
        
        List<HierarquiaProjeto> hierarquiasParaSelecao = new ArrayList<>();

        // Remove os detalhes de nivel maior que 2, pois não podem ser criados diretamente
        for (HierarquiaProjeto hierarquiasCadastrada : hierarquiasCadastradas) {
            try {
                HierarquiaProjeto hierarquiaParaSelecao = (HierarquiaProjeto) BeanUtils.cloneBean(hierarquiasCadastrada);
                hierarquiaParaSelecao.setCategorias(new ArrayList<>());
                
                for (HierarquiaProjetoDetalhe hierarquiaProjetoDetalhe : hierarquiasCadastrada.getCategorias()) {
                    if (hierarquiaProjetoDetalhe.getNivel() <= 2){
                        hierarquiaParaSelecao.getCategorias().add(hierarquiaProjetoDetalhe);
                    }
                    
                }
                
                hierarquiasParaSelecao.add(hierarquiaParaSelecao);
                
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
                Logger.getLogger(DashboardModel.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex);
            }
        }
        return hierarquiasParaSelecao;
    }

    /**
     * 
     * @return 
     */
    public HierarquiaProjetoDetalhe getCategoriaDefaultTarefa() {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();
        
        HierarquiaProjeto hierarquiaDefault = (HierarquiaProjeto) em.createNamedQuery("HierarquiaProjeto.findByNome")
                .setParameter("nome", "Meta")
                .getSingleResult();
        
        for (HierarquiaProjetoDetalhe categoria : hierarquiaDefault.getCategorias()) {
            if (categoria.getNivel()==2){
                return categoria;
            }
        }
        
        return null;
    }

    public List<Meta> filtrarMetas(DashboardPresenter.TipoPesquisa tipoPesquisa, List<Usuario> usuariosResponsaveis, List<Usuario> usuariosSolicitantes, List<Usuario> usuariosParticipantes, List<Empresa> empresas, List<FilialEmpresa> filiais, LocalDate dataFim, List<ProjecaoTarefa> projecoes, Usuario loggedUser) {
        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        final List<Meta> metasUsuarioResponsavel = new ArrayList<>();
        usuariosResponsaveis.stream().forEach((usuario) -> {
            // refresh
            usuario = em.find(Usuario.class, usuario.getId());
            metasUsuarioResponsavel.addAll(usuario.getMetasSobResponsabilidade());

        });

        List<Meta> metasUsuarioSolicitante = new ArrayList<>();
        usuariosSolicitantes.stream().forEach((usuario) -> {
            usuario = em.find(Usuario.class, usuario.getId());
            metasUsuarioSolicitante.addAll(usuario.getMetasSolicitadas());
        });

        List<Meta> metasEmpresa = new ArrayList<>();
        empresas.stream().forEach((empresa) -> {
            empresa = em.find(Empresa.class, empresa.getId());
            metasEmpresa.addAll(empresa.getMetas());
        });

        List<Meta> metasFiliais = new ArrayList<>();
        filiais.stream().forEach((filial) -> {
            filial = em.find(FilialEmpresa.class, filial.getId());
            metasFiliais.addAll(filial.getMetas());
        });

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

            metas.addAll(metasEmpresa);

            metas.addAll(metasFiliais);

            metas.addAll(metasDataFim);

        } else if (tipoPesquisa == DashboardPresenter.TipoPesquisa.EXCLUSIVA_E) {

            metas.addAll(em.createNamedQuery("Meta.findAll")
                    .setParameter("empresa",loggedUser.getEmpresaAtiva())
                    .getResultList());

            if (!metasUsuarioResponsavel.isEmpty()) {
                metas.retainAll(metasUsuarioResponsavel);
            }
            if (!metasUsuarioSolicitante.isEmpty()) {
                metas.retainAll(metasUsuarioSolicitante);
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

        return metas;
    }

}
