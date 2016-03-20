package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.FilialEmpresa;
import com.saax.gestorweb.model.datamodel.HierarquiaProjeto;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.ProjecaoTarefa;
import com.saax.gestorweb.model.datamodel.StatusTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.model.datamodel.UsuarioEmpresa;
import com.saax.gestorweb.presenter.DashboardPresenter;
import com.saax.gestorweb.util.GestorEntityManagerProvider;

import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.SessionAttributesEnum;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import org.apache.commons.beanutils.BeanUtils;

/**
 * Classe de negócios do Dasboard
 *
 * @author Rodrigo
 */
public class DashboardModel {

    // Classes do modelo acessórias acessadas por este model
    public static List<Tarefa> getTarefasTemplate() {
        Usuario loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO);
        List<Tarefa> templates = null;
        for (UsuarioEmpresa usuarioEmpresa : loggedUser.getEmpresas()) {
            if (usuarioEmpresa.getAtivo()) {
                Empresa empresa = usuarioEmpresa.getEmpresa();
                templates = GestorEntityManagerProvider.getEntityManager().createNamedQuery("Tarefa.findByTemplate", Tarefa.class)
                        .setParameter("empresa", empresa)
                        .setParameter("template", true)
                        .getResultList();

                for (Empresa subEmpresa : empresa.getSubEmpresas()) {
                    templates.addAll(GestorEntityManagerProvider.getEntityManager().createNamedQuery("Tarefa.findByTemplate", Tarefa.class)
                            .setParameter("empresa", subEmpresa)
                            .setParameter("template", true)
                            .getResultList());
                }

            }
        }
        return templates;
    }

    /**
     * Obtem as hierarquias que podem ser selecionadas pelo usuário (até nível
     * 2)
     *
     * @param empresa
     * @return
     */
    public static List<HierarquiaProjeto> getHierarquiasProjeto(Empresa empresa) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        // Obtem as hiearquias genericas (de todas as empresas)
        List<HierarquiaProjeto> hierarquiasGenericas = em.createNamedQuery("HierarquiaProjeto.findAllDefault")
                .getResultList();

        // Obtem as hiearquias da empresa do usuário logad
        List<HierarquiaProjeto> hierarquiasEmpresa = em.createNamedQuery("HierarquiaProjeto.findByEmpresa")
                .setParameter("empresa", empresa)
                .getResultList();

        List<HierarquiaProjeto> hierarquiasCadastradas = new ArrayList<>();
        if (empresa == null) {
            hierarquiasCadastradas.addAll(hierarquiasGenericas);
        } else {
            hierarquiasCadastradas.addAll(hierarquiasEmpresa);
        }

        List<HierarquiaProjeto> hierarquiasParaSelecao = new ArrayList<>();

        // Remove os detalhes de nivel maior que 2, pois não podem ser criados diretamente
        for (HierarquiaProjeto hierarquiasCadastrada : hierarquiasCadastradas) {
            try {
                HierarquiaProjeto hierarquiaParaSelecao = (HierarquiaProjeto) BeanUtils.cloneBean(hierarquiasCadastrada);
                hierarquiaParaSelecao.setCategorias(new ArrayList<>());

                for (HierarquiaProjetoDetalhe hierarquiaProjetoDetalhe : hierarquiasCadastrada.getCategorias()) {
                    if (hierarquiaProjetoDetalhe.getNivel() <= 2) {
                        hierarquiaParaSelecao.getCategorias().add(hierarquiaProjetoDetalhe);
                    }

                }

                hierarquiasParaSelecao.add(hierarquiaParaSelecao);

            }
            catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
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
    public static HierarquiaProjetoDetalhe getCategoriaDefaultTarefa() {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        HierarquiaProjeto hierarquiaDefault = (HierarquiaProjeto) em.createNamedQuery("HierarquiaProjeto.findByNome")
                .setParameter("nome", "Meta")
                .getSingleResult();

        for (HierarquiaProjetoDetalhe categoria : hierarquiaDefault.getCategorias()) {
            if (categoria.getNivel() == 2) {
                return categoria;
            }
        }

        return null;
    }

    public static List<Tarefa> listarTarefas(Usuario loggedUser) {
        return TarefaModel.listarTarefas(loggedUser);
    }

    public static List<Tarefa> listarTarefasPrincipais(Usuario loggedUser) {
        return TarefaModel.listarTarefasPrincipais(loggedUser);
    }

    public static List<Tarefa> listarTarefasAguardandoAceite(Usuario loggedUser) {
        return TarefaModel.listarTarefasAguardandoAceite(loggedUser);
    }

    public static List<Tarefa> filtrarTarefas(DashboardPresenter.TipoPesquisa tipoPesquisa, List<Usuario> usuariosResponsaveis, List<Usuario> usuariosSolicitantes, List<Usuario> usuariosParticipantes, List<Empresa> empresas, List<FilialEmpresa> filiais, LocalDate dataFim, List<ProjecaoTarefa> projecoes, Usuario loggedUser) {
        return TarefaModel.pesquisarTarefas(tipoPesquisa, usuariosResponsaveis, usuariosSolicitantes, usuariosParticipantes, empresas, filiais, dataFim, projecoes, loggedUser);
    }

    public static List<Meta> filtrarMetas(DashboardPresenter.TipoPesquisa tipoPesquisa, List<Usuario> usuariosResponsaveis, List<Usuario> usuariosSolicitantes, List<Usuario> usuariosParticipantes, List<Empresa> empresas, List<FilialEmpresa> filiais, LocalDate dataFim, List<ProjecaoTarefa> projecoes, Usuario loggedUser) {
        return MetaModel.filtrarMetas(tipoPesquisa, usuariosResponsaveis, usuariosSolicitantes, usuariosParticipantes, empresas, filiais, dataFim, projecoes, loggedUser);
    }

    public static List<Meta> listarMetas(Usuario loggedUser) {
        return MetaModel.listarMetas(loggedUser);
    }

    public static Tarefa criarNovaTarefaPeloTemplate(Tarefa template) {
        return TarefaModel.criarNovaTarefaPeloTemplate(template);

    }

}
