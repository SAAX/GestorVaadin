/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
import com.saax.gestorweb.model.datamodel.Endereco;
import com.saax.gestorweb.model.datamodel.FavoritosTarefaMeta;
import com.saax.gestorweb.model.datamodel.FilialCliente;
import com.saax.gestorweb.model.datamodel.FilialEmpresa;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.OrcamentoTarefa;
import com.saax.gestorweb.model.datamodel.ParticipanteTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.model.datamodel.UsuarioEmpresa;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author rodrigo
 */
public class UsuarioDAO implements Serializable {

    public UsuarioDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
        if (usuario.getEmpresas() == null) {
            usuario.setEmpresas(new ArrayList<UsuarioEmpresa>());
        }
        if (usuario.getMetasSobResponsabilidade() == null) {
            usuario.setMetasSobResponsabilidade(new ArrayList<Meta>());
        }
        if (usuario.getFavoritosIncluidos() == null) {
            usuario.setFavoritosIncluidos(new ArrayList<FavoritosTarefaMeta>());
        }
        if (usuario.getCentrosCustoIncluidos() == null) {
            usuario.setCentrosCustoIncluidos(new ArrayList<CentroCusto>());
        }
        if (usuario.getEnderecosIncluidos() == null) {
            usuario.setEnderecosIncluidos(new ArrayList<Endereco>());
        }
        if (usuario.getTarefasIncluidas() == null) {
            usuario.setTarefasIncluidas(new ArrayList<Tarefa>());
        }
        if (usuario.getTarefasSolicitadas() == null) {
            usuario.setTarefasSolicitadas(new ArrayList<Tarefa>());
        }
        if (usuario.getTarefasSobResponsabilidade() == null) {
            usuario.setTarefasSobResponsabilidade(new ArrayList<Tarefa>());
        }
        if (usuario.getParicipacoesIncluidas() == null) {
            usuario.setParicipacoesIncluidas(new ArrayList<ParticipanteTarefa>());
        }
        if (usuario.getTarefasParticipantes() == null) {
            usuario.setTarefasParticipantes(new ArrayList<ParticipanteTarefa>());
        }
        if (usuario.getFiliaisClientesIncluidas() == null) {
            usuario.setFiliaisClientesIncluidas(new ArrayList<FilialCliente>());
        }
        if (usuario.getFiliaisEmpresaIncluidas() == null) {
            usuario.setFiliaisEmpresaIncluidas(new ArrayList<FilialEmpresa>());
        }
        if (usuario.getAvaliacoesIncluidas() == null) {
            usuario.setAvaliacoesIncluidas(new ArrayList<AvaliacaoMetaTarefa>());
        }
        if (usuario.getAvaliacoesSubmetidas() == null) {
            usuario.setAvaliacoesSubmetidas(new ArrayList<AvaliacaoMetaTarefa>());
        }
        if (usuario.getAvaliacoesRecebidas() == null) {
            usuario.setAvaliacoesRecebidas(new ArrayList<AvaliacaoMetaTarefa>());
        }
        if (usuario.getOrcamentosIncluidos() == null) {
            usuario.setOrcamentosIncluidos(new ArrayList<OrcamentoTarefa>());
        }
        if (usuario.getApontamentosIncluidos() == null) {
            usuario.setApontamentosIncluidos(new ArrayList<ApontamentoTarefa>());
        }
        if (usuario.getDepartamentosIncluidos() == null) {
            usuario.setDepartamentosIncluidos(new ArrayList<Departamento>());
        }
        if (usuario.getUsuariosIncluidos() == null) {
            usuario.setUsuariosIncluidos(new ArrayList<Usuario>());
        }
        if (usuario.getEmpresasIncluidas() == null) {
            usuario.setEmpresasIncluidas(new ArrayList<Empresa>());
        }
        if (usuario.getEmpresasClienteIncluidas() == null) {
            usuario.setEmpresasClienteIncluidas(new ArrayList<EmpresaCliente>());
        }
        if (usuario.getAnexosTarefaIncluidos() == null) {
            usuario.setAnexosTarefaIncluidos(new ArrayList<AnexoTarefa>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuarioInclusao = usuario.getUsuarioInclusao();
            if (usuarioInclusao != null) {
                usuarioInclusao = em.getReference(usuarioInclusao.getClass(), usuarioInclusao.getId());
                usuario.setUsuarioInclusao(usuarioInclusao);
            }
            Collection<UsuarioEmpresa> attachedEmpresas = new ArrayList<UsuarioEmpresa>();
            for (UsuarioEmpresa empresasUsuarioEmpresaToAttach : usuario.getEmpresas()) {
                empresasUsuarioEmpresaToAttach = em.getReference(empresasUsuarioEmpresaToAttach.getClass(), empresasUsuarioEmpresaToAttach.getId());
                attachedEmpresas.add(empresasUsuarioEmpresaToAttach);
            }
            usuario.setEmpresas(attachedEmpresas);
            Collection<Meta> attachedMetasSobResponsabilidade = new ArrayList<Meta>();
            for (Meta metasSobResponsabilidadeMetaToAttach : usuario.getMetasSobResponsabilidade()) {
                metasSobResponsabilidadeMetaToAttach = em.getReference(metasSobResponsabilidadeMetaToAttach.getClass(), metasSobResponsabilidadeMetaToAttach.getId());
                attachedMetasSobResponsabilidade.add(metasSobResponsabilidadeMetaToAttach);
            }
            usuario.setMetasSobResponsabilidade(attachedMetasSobResponsabilidade);
            List<FavoritosTarefaMeta> attachedFavoritosIncluidos = new ArrayList<FavoritosTarefaMeta>();
            for (FavoritosTarefaMeta favoritosIncluidosFavoritosTarefaMetaToAttach : usuario.getFavoritosIncluidos()) {
                favoritosIncluidosFavoritosTarefaMetaToAttach = em.getReference(favoritosIncluidosFavoritosTarefaMetaToAttach.getClass(), favoritosIncluidosFavoritosTarefaMetaToAttach.getId());
                attachedFavoritosIncluidos.add(favoritosIncluidosFavoritosTarefaMetaToAttach);
            }
            usuario.setFavoritosIncluidos(attachedFavoritosIncluidos);
            List<CentroCusto> attachedCentrosCustoIncluidos = new ArrayList<CentroCusto>();
            for (CentroCusto centrosCustoIncluidosCentroCustoToAttach : usuario.getCentrosCustoIncluidos()) {
                centrosCustoIncluidosCentroCustoToAttach = em.getReference(centrosCustoIncluidosCentroCustoToAttach.getClass(), centrosCustoIncluidosCentroCustoToAttach.getId());
                attachedCentrosCustoIncluidos.add(centrosCustoIncluidosCentroCustoToAttach);
            }
            usuario.setCentrosCustoIncluidos(attachedCentrosCustoIncluidos);
            List<Endereco> attachedEnderecosIncluidos = new ArrayList<Endereco>();
            for (Endereco enderecosIncluidosEnderecoToAttach : usuario.getEnderecosIncluidos()) {
                enderecosIncluidosEnderecoToAttach = em.getReference(enderecosIncluidosEnderecoToAttach.getClass(), enderecosIncluidosEnderecoToAttach.getId());
                attachedEnderecosIncluidos.add(enderecosIncluidosEnderecoToAttach);
            }
            usuario.setEnderecosIncluidos(attachedEnderecosIncluidos);
            List<Tarefa> attachedTarefasIncluidas = new ArrayList<Tarefa>();
            for (Tarefa tarefasIncluidasTarefaToAttach : usuario.getTarefasIncluidas()) {
                tarefasIncluidasTarefaToAttach = em.getReference(tarefasIncluidasTarefaToAttach.getClass(), tarefasIncluidasTarefaToAttach.getId());
                attachedTarefasIncluidas.add(tarefasIncluidasTarefaToAttach);
            }
            usuario.setTarefasIncluidas(attachedTarefasIncluidas);
            List<Tarefa> attachedTarefasSolicitadas = new ArrayList<Tarefa>();
            for (Tarefa tarefasSolicitadasTarefaToAttach : usuario.getTarefasSolicitadas()) {
                tarefasSolicitadasTarefaToAttach = em.getReference(tarefasSolicitadasTarefaToAttach.getClass(), tarefasSolicitadasTarefaToAttach.getId());
                attachedTarefasSolicitadas.add(tarefasSolicitadasTarefaToAttach);
            }
            usuario.setTarefasSolicitadas(attachedTarefasSolicitadas);
            List<Tarefa> attachedTarefasSobResponsabilidade = new ArrayList<Tarefa>();
            for (Tarefa tarefasSobResponsabilidadeTarefaToAttach : usuario.getTarefasSobResponsabilidade()) {
                tarefasSobResponsabilidadeTarefaToAttach = em.getReference(tarefasSobResponsabilidadeTarefaToAttach.getClass(), tarefasSobResponsabilidadeTarefaToAttach.getId());
                attachedTarefasSobResponsabilidade.add(tarefasSobResponsabilidadeTarefaToAttach);
            }
            usuario.setTarefasSobResponsabilidade(attachedTarefasSobResponsabilidade);
            List<ParticipanteTarefa> attachedParicipacoesIncluidas = new ArrayList<ParticipanteTarefa>();
            for (ParticipanteTarefa paricipacoesIncluidasParticipanteTarefaToAttach : usuario.getParicipacoesIncluidas()) {
                paricipacoesIncluidasParticipanteTarefaToAttach = em.getReference(paricipacoesIncluidasParticipanteTarefaToAttach.getClass(), paricipacoesIncluidasParticipanteTarefaToAttach.getId());
                attachedParicipacoesIncluidas.add(paricipacoesIncluidasParticipanteTarefaToAttach);
            }
            usuario.setParicipacoesIncluidas(attachedParicipacoesIncluidas);
            List<ParticipanteTarefa> attachedTarefasParticipantes = new ArrayList<ParticipanteTarefa>();
            for (ParticipanteTarefa tarefasParticipantesParticipanteTarefaToAttach : usuario.getTarefasParticipantes()) {
                tarefasParticipantesParticipanteTarefaToAttach = em.getReference(tarefasParticipantesParticipanteTarefaToAttach.getClass(), tarefasParticipantesParticipanteTarefaToAttach.getId());
                attachedTarefasParticipantes.add(tarefasParticipantesParticipanteTarefaToAttach);
            }
            usuario.setTarefasParticipantes(attachedTarefasParticipantes);
            List<FilialCliente> attachedFiliaisClientesIncluidas = new ArrayList<FilialCliente>();
            for (FilialCliente filiaisClientesIncluidasFilialClienteToAttach : usuario.getFiliaisClientesIncluidas()) {
                filiaisClientesIncluidasFilialClienteToAttach = em.getReference(filiaisClientesIncluidasFilialClienteToAttach.getClass(), filiaisClientesIncluidasFilialClienteToAttach.getId());
                attachedFiliaisClientesIncluidas.add(filiaisClientesIncluidasFilialClienteToAttach);
            }
            usuario.setFiliaisClientesIncluidas(attachedFiliaisClientesIncluidas);
            List<FilialEmpresa> attachedFiliaisEmpresaIncluidas = new ArrayList<FilialEmpresa>();
            for (FilialEmpresa filiaisEmpresaIncluidasFilialEmpresaToAttach : usuario.getFiliaisEmpresaIncluidas()) {
                filiaisEmpresaIncluidasFilialEmpresaToAttach = em.getReference(filiaisEmpresaIncluidasFilialEmpresaToAttach.getClass(), filiaisEmpresaIncluidasFilialEmpresaToAttach.getId());
                attachedFiliaisEmpresaIncluidas.add(filiaisEmpresaIncluidasFilialEmpresaToAttach);
            }
            usuario.setFiliaisEmpresaIncluidas(attachedFiliaisEmpresaIncluidas);
            List<AvaliacaoMetaTarefa> attachedAvaliacoesIncluidas = new ArrayList<AvaliacaoMetaTarefa>();
            for (AvaliacaoMetaTarefa avaliacoesIncluidasAvaliacaoMetaTarefaToAttach : usuario.getAvaliacoesIncluidas()) {
                avaliacoesIncluidasAvaliacaoMetaTarefaToAttach = em.getReference(avaliacoesIncluidasAvaliacaoMetaTarefaToAttach.getClass(), avaliacoesIncluidasAvaliacaoMetaTarefaToAttach.getId());
                attachedAvaliacoesIncluidas.add(avaliacoesIncluidasAvaliacaoMetaTarefaToAttach);
            }
            usuario.setAvaliacoesIncluidas(attachedAvaliacoesIncluidas);
            List<AvaliacaoMetaTarefa> attachedAvaliacoesSubmetidas = new ArrayList<AvaliacaoMetaTarefa>();
            for (AvaliacaoMetaTarefa avaliacoesSubmetidasAvaliacaoMetaTarefaToAttach : usuario.getAvaliacoesSubmetidas()) {
                avaliacoesSubmetidasAvaliacaoMetaTarefaToAttach = em.getReference(avaliacoesSubmetidasAvaliacaoMetaTarefaToAttach.getClass(), avaliacoesSubmetidasAvaliacaoMetaTarefaToAttach.getId());
                attachedAvaliacoesSubmetidas.add(avaliacoesSubmetidasAvaliacaoMetaTarefaToAttach);
            }
            usuario.setAvaliacoesSubmetidas(attachedAvaliacoesSubmetidas);
            List<AvaliacaoMetaTarefa> attachedAvaliacoesRecebidas = new ArrayList<AvaliacaoMetaTarefa>();
            for (AvaliacaoMetaTarefa avaliacoesRecebidasAvaliacaoMetaTarefaToAttach : usuario.getAvaliacoesRecebidas()) {
                avaliacoesRecebidasAvaliacaoMetaTarefaToAttach = em.getReference(avaliacoesRecebidasAvaliacaoMetaTarefaToAttach.getClass(), avaliacoesRecebidasAvaliacaoMetaTarefaToAttach.getId());
                attachedAvaliacoesRecebidas.add(avaliacoesRecebidasAvaliacaoMetaTarefaToAttach);
            }
            usuario.setAvaliacoesRecebidas(attachedAvaliacoesRecebidas);
            List<OrcamentoTarefa> attachedOrcamentosIncluidos = new ArrayList<OrcamentoTarefa>();
            for (OrcamentoTarefa orcamentosIncluidosOrcamentoTarefaToAttach : usuario.getOrcamentosIncluidos()) {
                orcamentosIncluidosOrcamentoTarefaToAttach = em.getReference(orcamentosIncluidosOrcamentoTarefaToAttach.getClass(), orcamentosIncluidosOrcamentoTarefaToAttach.getId());
                attachedOrcamentosIncluidos.add(orcamentosIncluidosOrcamentoTarefaToAttach);
            }
            usuario.setOrcamentosIncluidos(attachedOrcamentosIncluidos);
            List<ApontamentoTarefa> attachedApontamentosIncluidos = new ArrayList<ApontamentoTarefa>();
            for (ApontamentoTarefa apontamentosIncluidosApontamentoTarefaToAttach : usuario.getApontamentosIncluidos()) {
                apontamentosIncluidosApontamentoTarefaToAttach = em.getReference(apontamentosIncluidosApontamentoTarefaToAttach.getClass(), apontamentosIncluidosApontamentoTarefaToAttach.getId());
                attachedApontamentosIncluidos.add(apontamentosIncluidosApontamentoTarefaToAttach);
            }
            usuario.setApontamentosIncluidos(attachedApontamentosIncluidos);
            List<Departamento> attachedDepartamentosIncluidos = new ArrayList<Departamento>();
            for (Departamento departamentosIncluidosDepartamentoToAttach : usuario.getDepartamentosIncluidos()) {
                departamentosIncluidosDepartamentoToAttach = em.getReference(departamentosIncluidosDepartamentoToAttach.getClass(), departamentosIncluidosDepartamentoToAttach.getId());
                attachedDepartamentosIncluidos.add(departamentosIncluidosDepartamentoToAttach);
            }
            usuario.setDepartamentosIncluidos(attachedDepartamentosIncluidos);
            List<Usuario> attachedUsuariosIncluidos = new ArrayList<Usuario>();
            for (Usuario usuariosIncluidosUsuarioToAttach : usuario.getUsuariosIncluidos()) {
                usuariosIncluidosUsuarioToAttach = em.getReference(usuariosIncluidosUsuarioToAttach.getClass(), usuariosIncluidosUsuarioToAttach.getId());
                attachedUsuariosIncluidos.add(usuariosIncluidosUsuarioToAttach);
            }
            usuario.setUsuariosIncluidos(attachedUsuariosIncluidos);
            List<Empresa> attachedEmpresasIncluidas = new ArrayList<Empresa>();
            for (Empresa empresasIncluidasEmpresaToAttach : usuario.getEmpresasIncluidas()) {
                empresasIncluidasEmpresaToAttach = em.getReference(empresasIncluidasEmpresaToAttach.getClass(), empresasIncluidasEmpresaToAttach.getId());
                attachedEmpresasIncluidas.add(empresasIncluidasEmpresaToAttach);
            }
            usuario.setEmpresasIncluidas(attachedEmpresasIncluidas);
            List<EmpresaCliente> attachedEmpresasClienteIncluidas = new ArrayList<EmpresaCliente>();
            for (EmpresaCliente empresasClienteIncluidasEmpresaClienteToAttach : usuario.getEmpresasClienteIncluidas()) {
                empresasClienteIncluidasEmpresaClienteToAttach = em.getReference(empresasClienteIncluidasEmpresaClienteToAttach.getClass(), empresasClienteIncluidasEmpresaClienteToAttach.getId());
                attachedEmpresasClienteIncluidas.add(empresasClienteIncluidasEmpresaClienteToAttach);
            }
            usuario.setEmpresasClienteIncluidas(attachedEmpresasClienteIncluidas);
            List<AnexoTarefa> attachedAnexosTarefaIncluidos = new ArrayList<AnexoTarefa>();
            for (AnexoTarefa anexosTarefaIncluidosAnexoTarefaToAttach : usuario.getAnexosTarefaIncluidos()) {
                anexosTarefaIncluidosAnexoTarefaToAttach = em.getReference(anexosTarefaIncluidosAnexoTarefaToAttach.getClass(), anexosTarefaIncluidosAnexoTarefaToAttach.getId());
                attachedAnexosTarefaIncluidos.add(anexosTarefaIncluidosAnexoTarefaToAttach);
            }
            usuario.setAnexosTarefaIncluidos(attachedAnexosTarefaIncluidos);
            em.persist(usuario);
            if (usuarioInclusao != null) {
                usuarioInclusao.getUsuariosIncluidos().add(usuario);
                usuarioInclusao = em.merge(usuarioInclusao);
            }
            for (UsuarioEmpresa empresasUsuarioEmpresa : usuario.getEmpresas()) {
                Usuario oldUsuarioOfEmpresasUsuarioEmpresa = empresasUsuarioEmpresa.getUsuario();
                empresasUsuarioEmpresa.setUsuario(usuario);
                empresasUsuarioEmpresa = em.merge(empresasUsuarioEmpresa);
                if (oldUsuarioOfEmpresasUsuarioEmpresa != null) {
                    oldUsuarioOfEmpresasUsuarioEmpresa.getEmpresas().remove(empresasUsuarioEmpresa);
                    oldUsuarioOfEmpresasUsuarioEmpresa = em.merge(oldUsuarioOfEmpresasUsuarioEmpresa);
                }
            }
            for (Meta metasSobResponsabilidadeMeta : usuario.getMetasSobResponsabilidade()) {
                Usuario oldUsuarioResponsavelOfMetasSobResponsabilidadeMeta = metasSobResponsabilidadeMeta.getUsuarioResponsavel();
                metasSobResponsabilidadeMeta.setUsuarioResponsavel(usuario);
                metasSobResponsabilidadeMeta = em.merge(metasSobResponsabilidadeMeta);
                if (oldUsuarioResponsavelOfMetasSobResponsabilidadeMeta != null) {
                    oldUsuarioResponsavelOfMetasSobResponsabilidadeMeta.getMetasSobResponsabilidade().remove(metasSobResponsabilidadeMeta);
                    oldUsuarioResponsavelOfMetasSobResponsabilidadeMeta = em.merge(oldUsuarioResponsavelOfMetasSobResponsabilidadeMeta);
                }
            }
            for (FavoritosTarefaMeta favoritosIncluidosFavoritosTarefaMeta : usuario.getFavoritosIncluidos()) {
                Usuario oldUsuarioInclusaoOfFavoritosIncluidosFavoritosTarefaMeta = favoritosIncluidosFavoritosTarefaMeta.getUsuarioInclusao();
                favoritosIncluidosFavoritosTarefaMeta.setUsuarioInclusao(usuario);
                favoritosIncluidosFavoritosTarefaMeta = em.merge(favoritosIncluidosFavoritosTarefaMeta);
                if (oldUsuarioInclusaoOfFavoritosIncluidosFavoritosTarefaMeta != null) {
                    oldUsuarioInclusaoOfFavoritosIncluidosFavoritosTarefaMeta.getFavoritosIncluidos().remove(favoritosIncluidosFavoritosTarefaMeta);
                    oldUsuarioInclusaoOfFavoritosIncluidosFavoritosTarefaMeta = em.merge(oldUsuarioInclusaoOfFavoritosIncluidosFavoritosTarefaMeta);
                }
            }
            for (CentroCusto centrosCustoIncluidosCentroCusto : usuario.getCentrosCustoIncluidos()) {
                Usuario oldUsuarioInclusaoOfCentrosCustoIncluidosCentroCusto = centrosCustoIncluidosCentroCusto.getUsuarioInclusao();
                centrosCustoIncluidosCentroCusto.setUsuarioInclusao(usuario);
                centrosCustoIncluidosCentroCusto = em.merge(centrosCustoIncluidosCentroCusto);
                if (oldUsuarioInclusaoOfCentrosCustoIncluidosCentroCusto != null) {
                    oldUsuarioInclusaoOfCentrosCustoIncluidosCentroCusto.getCentrosCustoIncluidos().remove(centrosCustoIncluidosCentroCusto);
                    oldUsuarioInclusaoOfCentrosCustoIncluidosCentroCusto = em.merge(oldUsuarioInclusaoOfCentrosCustoIncluidosCentroCusto);
                }
            }
            for (Endereco enderecosIncluidosEndereco : usuario.getEnderecosIncluidos()) {
                Usuario oldUsuarioInclusaoOfEnderecosIncluidosEndereco = enderecosIncluidosEndereco.getUsuarioInclusao();
                enderecosIncluidosEndereco.setUsuarioInclusao(usuario);
                enderecosIncluidosEndereco = em.merge(enderecosIncluidosEndereco);
                if (oldUsuarioInclusaoOfEnderecosIncluidosEndereco != null) {
                    oldUsuarioInclusaoOfEnderecosIncluidosEndereco.getEnderecosIncluidos().remove(enderecosIncluidosEndereco);
                    oldUsuarioInclusaoOfEnderecosIncluidosEndereco = em.merge(oldUsuarioInclusaoOfEnderecosIncluidosEndereco);
                }
            }
            for (Tarefa tarefasIncluidasTarefa : usuario.getTarefasIncluidas()) {
                Usuario oldUsuarioInclusaoOfTarefasIncluidasTarefa = tarefasIncluidasTarefa.getUsuarioInclusao();
                tarefasIncluidasTarefa.setUsuarioInclusao(usuario);
                tarefasIncluidasTarefa = em.merge(tarefasIncluidasTarefa);
                if (oldUsuarioInclusaoOfTarefasIncluidasTarefa != null) {
                    oldUsuarioInclusaoOfTarefasIncluidasTarefa.getTarefasIncluidas().remove(tarefasIncluidasTarefa);
                    oldUsuarioInclusaoOfTarefasIncluidasTarefa = em.merge(oldUsuarioInclusaoOfTarefasIncluidasTarefa);
                }
            }
            for (Tarefa tarefasSolicitadasTarefa : usuario.getTarefasSolicitadas()) {
                Usuario oldUsuarioSolicitanteOfTarefasSolicitadasTarefa = tarefasSolicitadasTarefa.getUsuarioSolicitante();
                tarefasSolicitadasTarefa.setUsuarioSolicitante(usuario);
                tarefasSolicitadasTarefa = em.merge(tarefasSolicitadasTarefa);
                if (oldUsuarioSolicitanteOfTarefasSolicitadasTarefa != null) {
                    oldUsuarioSolicitanteOfTarefasSolicitadasTarefa.getTarefasSolicitadas().remove(tarefasSolicitadasTarefa);
                    oldUsuarioSolicitanteOfTarefasSolicitadasTarefa = em.merge(oldUsuarioSolicitanteOfTarefasSolicitadasTarefa);
                }
            }
            for (Tarefa tarefasSobResponsabilidadeTarefa : usuario.getTarefasSobResponsabilidade()) {
                Usuario oldUsuarioResponsavelOfTarefasSobResponsabilidadeTarefa = tarefasSobResponsabilidadeTarefa.getUsuarioResponsavel();
                tarefasSobResponsabilidadeTarefa.setUsuarioResponsavel(usuario);
                tarefasSobResponsabilidadeTarefa = em.merge(tarefasSobResponsabilidadeTarefa);
                if (oldUsuarioResponsavelOfTarefasSobResponsabilidadeTarefa != null) {
                    oldUsuarioResponsavelOfTarefasSobResponsabilidadeTarefa.getTarefasSobResponsabilidade().remove(tarefasSobResponsabilidadeTarefa);
                    oldUsuarioResponsavelOfTarefasSobResponsabilidadeTarefa = em.merge(oldUsuarioResponsavelOfTarefasSobResponsabilidadeTarefa);
                }
            }
            for (ParticipanteTarefa paricipacoesIncluidasParticipanteTarefa : usuario.getParicipacoesIncluidas()) {
                Usuario oldUsuarioInclusaoOfParicipacoesIncluidasParticipanteTarefa = paricipacoesIncluidasParticipanteTarefa.getUsuarioInclusao();
                paricipacoesIncluidasParticipanteTarefa.setUsuarioInclusao(usuario);
                paricipacoesIncluidasParticipanteTarefa = em.merge(paricipacoesIncluidasParticipanteTarefa);
                if (oldUsuarioInclusaoOfParicipacoesIncluidasParticipanteTarefa != null) {
                    oldUsuarioInclusaoOfParicipacoesIncluidasParticipanteTarefa.getParicipacoesIncluidas().remove(paricipacoesIncluidasParticipanteTarefa);
                    oldUsuarioInclusaoOfParicipacoesIncluidasParticipanteTarefa = em.merge(oldUsuarioInclusaoOfParicipacoesIncluidasParticipanteTarefa);
                }
            }
            for (ParticipanteTarefa tarefasParticipantesParticipanteTarefa : usuario.getTarefasParticipantes()) {
                Usuario oldUsuarioParticipanteOfTarefasParticipantesParticipanteTarefa = tarefasParticipantesParticipanteTarefa.getUsuarioParticipante();
                tarefasParticipantesParticipanteTarefa.setUsuarioParticipante(usuario);
                tarefasParticipantesParticipanteTarefa = em.merge(tarefasParticipantesParticipanteTarefa);
                if (oldUsuarioParticipanteOfTarefasParticipantesParticipanteTarefa != null) {
                    oldUsuarioParticipanteOfTarefasParticipantesParticipanteTarefa.getTarefasParticipantes().remove(tarefasParticipantesParticipanteTarefa);
                    oldUsuarioParticipanteOfTarefasParticipantesParticipanteTarefa = em.merge(oldUsuarioParticipanteOfTarefasParticipantesParticipanteTarefa);
                }
            }
            for (FilialCliente filiaisClientesIncluidasFilialCliente : usuario.getFiliaisClientesIncluidas()) {
                Usuario oldUsuarioInclusaoOfFiliaisClientesIncluidasFilialCliente = filiaisClientesIncluidasFilialCliente.getUsuarioInclusao();
                filiaisClientesIncluidasFilialCliente.setUsuarioInclusao(usuario);
                filiaisClientesIncluidasFilialCliente = em.merge(filiaisClientesIncluidasFilialCliente);
                if (oldUsuarioInclusaoOfFiliaisClientesIncluidasFilialCliente != null) {
                    oldUsuarioInclusaoOfFiliaisClientesIncluidasFilialCliente.getFiliaisClientesIncluidas().remove(filiaisClientesIncluidasFilialCliente);
                    oldUsuarioInclusaoOfFiliaisClientesIncluidasFilialCliente = em.merge(oldUsuarioInclusaoOfFiliaisClientesIncluidasFilialCliente);
                }
            }
            for (FilialEmpresa filiaisEmpresaIncluidasFilialEmpresa : usuario.getFiliaisEmpresaIncluidas()) {
                Usuario oldUsuarioInclusaoOfFiliaisEmpresaIncluidasFilialEmpresa = filiaisEmpresaIncluidasFilialEmpresa.getUsuarioInclusao();
                filiaisEmpresaIncluidasFilialEmpresa.setUsuarioInclusao(usuario);
                filiaisEmpresaIncluidasFilialEmpresa = em.merge(filiaisEmpresaIncluidasFilialEmpresa);
                if (oldUsuarioInclusaoOfFiliaisEmpresaIncluidasFilialEmpresa != null) {
                    oldUsuarioInclusaoOfFiliaisEmpresaIncluidasFilialEmpresa.getFiliaisEmpresaIncluidas().remove(filiaisEmpresaIncluidasFilialEmpresa);
                    oldUsuarioInclusaoOfFiliaisEmpresaIncluidasFilialEmpresa = em.merge(oldUsuarioInclusaoOfFiliaisEmpresaIncluidasFilialEmpresa);
                }
            }
            for (AvaliacaoMetaTarefa avaliacoesIncluidasAvaliacaoMetaTarefa : usuario.getAvaliacoesIncluidas()) {
                Usuario oldUsuarioInclusaoOfAvaliacoesIncluidasAvaliacaoMetaTarefa = avaliacoesIncluidasAvaliacaoMetaTarefa.getUsuarioInclusao();
                avaliacoesIncluidasAvaliacaoMetaTarefa.setUsuarioInclusao(usuario);
                avaliacoesIncluidasAvaliacaoMetaTarefa = em.merge(avaliacoesIncluidasAvaliacaoMetaTarefa);
                if (oldUsuarioInclusaoOfAvaliacoesIncluidasAvaliacaoMetaTarefa != null) {
                    oldUsuarioInclusaoOfAvaliacoesIncluidasAvaliacaoMetaTarefa.getAvaliacoesIncluidas().remove(avaliacoesIncluidasAvaliacaoMetaTarefa);
                    oldUsuarioInclusaoOfAvaliacoesIncluidasAvaliacaoMetaTarefa = em.merge(oldUsuarioInclusaoOfAvaliacoesIncluidasAvaliacaoMetaTarefa);
                }
            }
            for (AvaliacaoMetaTarefa avaliacoesSubmetidasAvaliacaoMetaTarefa : usuario.getAvaliacoesSubmetidas()) {
                Usuario oldUsuarioAvaliadorOfAvaliacoesSubmetidasAvaliacaoMetaTarefa = avaliacoesSubmetidasAvaliacaoMetaTarefa.getUsuarioAvaliador();
                avaliacoesSubmetidasAvaliacaoMetaTarefa.setUsuarioAvaliador(usuario);
                avaliacoesSubmetidasAvaliacaoMetaTarefa = em.merge(avaliacoesSubmetidasAvaliacaoMetaTarefa);
                if (oldUsuarioAvaliadorOfAvaliacoesSubmetidasAvaliacaoMetaTarefa != null) {
                    oldUsuarioAvaliadorOfAvaliacoesSubmetidasAvaliacaoMetaTarefa.getAvaliacoesSubmetidas().remove(avaliacoesSubmetidasAvaliacaoMetaTarefa);
                    oldUsuarioAvaliadorOfAvaliacoesSubmetidasAvaliacaoMetaTarefa = em.merge(oldUsuarioAvaliadorOfAvaliacoesSubmetidasAvaliacaoMetaTarefa);
                }
            }
            for (AvaliacaoMetaTarefa avaliacoesRecebidasAvaliacaoMetaTarefa : usuario.getAvaliacoesRecebidas()) {
                Usuario oldUsuarioAvaliadoOfAvaliacoesRecebidasAvaliacaoMetaTarefa = avaliacoesRecebidasAvaliacaoMetaTarefa.getUsuarioAvaliado();
                avaliacoesRecebidasAvaliacaoMetaTarefa.setUsuarioAvaliado(usuario);
                avaliacoesRecebidasAvaliacaoMetaTarefa = em.merge(avaliacoesRecebidasAvaliacaoMetaTarefa);
                if (oldUsuarioAvaliadoOfAvaliacoesRecebidasAvaliacaoMetaTarefa != null) {
                    oldUsuarioAvaliadoOfAvaliacoesRecebidasAvaliacaoMetaTarefa.getAvaliacoesRecebidas().remove(avaliacoesRecebidasAvaliacaoMetaTarefa);
                    oldUsuarioAvaliadoOfAvaliacoesRecebidasAvaliacaoMetaTarefa = em.merge(oldUsuarioAvaliadoOfAvaliacoesRecebidasAvaliacaoMetaTarefa);
                }
            }
            for (OrcamentoTarefa orcamentosIncluidosOrcamentoTarefa : usuario.getOrcamentosIncluidos()) {
                Usuario oldUsuarioInclusaoOfOrcamentosIncluidosOrcamentoTarefa = orcamentosIncluidosOrcamentoTarefa.getUsuarioInclusao();
                orcamentosIncluidosOrcamentoTarefa.setUsuarioInclusao(usuario);
                orcamentosIncluidosOrcamentoTarefa = em.merge(orcamentosIncluidosOrcamentoTarefa);
                if (oldUsuarioInclusaoOfOrcamentosIncluidosOrcamentoTarefa != null) {
                    oldUsuarioInclusaoOfOrcamentosIncluidosOrcamentoTarefa.getOrcamentosIncluidos().remove(orcamentosIncluidosOrcamentoTarefa);
                    oldUsuarioInclusaoOfOrcamentosIncluidosOrcamentoTarefa = em.merge(oldUsuarioInclusaoOfOrcamentosIncluidosOrcamentoTarefa);
                }
            }
            for (ApontamentoTarefa apontamentosIncluidosApontamentoTarefa : usuario.getApontamentosIncluidos()) {
                Usuario oldUsuarioInclusaoOfApontamentosIncluidosApontamentoTarefa = apontamentosIncluidosApontamentoTarefa.getUsuarioInclusao();
                apontamentosIncluidosApontamentoTarefa.setUsuarioInclusao(usuario);
                apontamentosIncluidosApontamentoTarefa = em.merge(apontamentosIncluidosApontamentoTarefa);
                if (oldUsuarioInclusaoOfApontamentosIncluidosApontamentoTarefa != null) {
                    oldUsuarioInclusaoOfApontamentosIncluidosApontamentoTarefa.getApontamentosIncluidos().remove(apontamentosIncluidosApontamentoTarefa);
                    oldUsuarioInclusaoOfApontamentosIncluidosApontamentoTarefa = em.merge(oldUsuarioInclusaoOfApontamentosIncluidosApontamentoTarefa);
                }
            }
            for (Departamento departamentosIncluidosDepartamento : usuario.getDepartamentosIncluidos()) {
                Usuario oldUsuarioInclusaoOfDepartamentosIncluidosDepartamento = departamentosIncluidosDepartamento.getUsuarioInclusao();
                departamentosIncluidosDepartamento.setUsuarioInclusao(usuario);
                departamentosIncluidosDepartamento = em.merge(departamentosIncluidosDepartamento);
                if (oldUsuarioInclusaoOfDepartamentosIncluidosDepartamento != null) {
                    oldUsuarioInclusaoOfDepartamentosIncluidosDepartamento.getDepartamentosIncluidos().remove(departamentosIncluidosDepartamento);
                    oldUsuarioInclusaoOfDepartamentosIncluidosDepartamento = em.merge(oldUsuarioInclusaoOfDepartamentosIncluidosDepartamento);
                }
            }
            for (Usuario usuariosIncluidosUsuario : usuario.getUsuariosIncluidos()) {
                Usuario oldUsuarioInclusaoOfUsuariosIncluidosUsuario = usuariosIncluidosUsuario.getUsuarioInclusao();
                usuariosIncluidosUsuario.setUsuarioInclusao(usuario);
                usuariosIncluidosUsuario = em.merge(usuariosIncluidosUsuario);
                if (oldUsuarioInclusaoOfUsuariosIncluidosUsuario != null) {
                    oldUsuarioInclusaoOfUsuariosIncluidosUsuario.getUsuariosIncluidos().remove(usuariosIncluidosUsuario);
                    oldUsuarioInclusaoOfUsuariosIncluidosUsuario = em.merge(oldUsuarioInclusaoOfUsuariosIncluidosUsuario);
                }
            }
            for (Empresa empresasIncluidasEmpresa : usuario.getEmpresasIncluidas()) {
                Usuario oldUsuarioInclusaoOfEmpresasIncluidasEmpresa = empresasIncluidasEmpresa.getUsuarioInclusao();
                empresasIncluidasEmpresa.setUsuarioInclusao(usuario);
                empresasIncluidasEmpresa = em.merge(empresasIncluidasEmpresa);
                if (oldUsuarioInclusaoOfEmpresasIncluidasEmpresa != null) {
                    oldUsuarioInclusaoOfEmpresasIncluidasEmpresa.getEmpresasIncluidas().remove(empresasIncluidasEmpresa);
                    oldUsuarioInclusaoOfEmpresasIncluidasEmpresa = em.merge(oldUsuarioInclusaoOfEmpresasIncluidasEmpresa);
                }
            }
            for (EmpresaCliente empresasClienteIncluidasEmpresaCliente : usuario.getEmpresasClienteIncluidas()) {
                Usuario oldUsuarioInclusaoOfEmpresasClienteIncluidasEmpresaCliente = empresasClienteIncluidasEmpresaCliente.getUsuarioInclusao();
                empresasClienteIncluidasEmpresaCliente.setUsuarioInclusao(usuario);
                empresasClienteIncluidasEmpresaCliente = em.merge(empresasClienteIncluidasEmpresaCliente);
                if (oldUsuarioInclusaoOfEmpresasClienteIncluidasEmpresaCliente != null) {
                    oldUsuarioInclusaoOfEmpresasClienteIncluidasEmpresaCliente.getEmpresasClienteIncluidas().remove(empresasClienteIncluidasEmpresaCliente);
                    oldUsuarioInclusaoOfEmpresasClienteIncluidasEmpresaCliente = em.merge(oldUsuarioInclusaoOfEmpresasClienteIncluidasEmpresaCliente);
                }
            }
            for (AnexoTarefa anexosTarefaIncluidosAnexoTarefa : usuario.getAnexosTarefaIncluidos()) {
                Usuario oldUsuarioInclusaoOfAnexosTarefaIncluidosAnexoTarefa = anexosTarefaIncluidosAnexoTarefa.getUsuarioInclusao();
                anexosTarefaIncluidosAnexoTarefa.setUsuarioInclusao(usuario);
                anexosTarefaIncluidosAnexoTarefa = em.merge(anexosTarefaIncluidosAnexoTarefa);
                if (oldUsuarioInclusaoOfAnexosTarefaIncluidosAnexoTarefa != null) {
                    oldUsuarioInclusaoOfAnexosTarefaIncluidosAnexoTarefa.getAnexosTarefaIncluidos().remove(anexosTarefaIncluidosAnexoTarefa);
                    oldUsuarioInclusaoOfAnexosTarefaIncluidosAnexoTarefa = em.merge(oldUsuarioInclusaoOfAnexosTarefaIncluidosAnexoTarefa);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getId());
            Usuario usuarioInclusaoOld = persistentUsuario.getUsuarioInclusao();
            Usuario usuarioInclusaoNew = usuario.getUsuarioInclusao();
            Collection<UsuarioEmpresa> empresasOld = persistentUsuario.getEmpresas();
            Collection<UsuarioEmpresa> empresasNew = usuario.getEmpresas();
            Collection<Meta> metasSobResponsabilidadeOld = persistentUsuario.getMetasSobResponsabilidade();
            Collection<Meta> metasSobResponsabilidadeNew = usuario.getMetasSobResponsabilidade();
            List<FavoritosTarefaMeta> favoritosIncluidosOld = persistentUsuario.getFavoritosIncluidos();
            List<FavoritosTarefaMeta> favoritosIncluidosNew = usuario.getFavoritosIncluidos();
            List<CentroCusto> centrosCustoIncluidosOld = persistentUsuario.getCentrosCustoIncluidos();
            List<CentroCusto> centrosCustoIncluidosNew = usuario.getCentrosCustoIncluidos();
            List<Endereco> enderecosIncluidosOld = persistentUsuario.getEnderecosIncluidos();
            List<Endereco> enderecosIncluidosNew = usuario.getEnderecosIncluidos();
            List<Tarefa> tarefasIncluidasOld = persistentUsuario.getTarefasIncluidas();
            List<Tarefa> tarefasIncluidasNew = usuario.getTarefasIncluidas();
            List<Tarefa> tarefasSolicitadasOld = persistentUsuario.getTarefasSolicitadas();
            List<Tarefa> tarefasSolicitadasNew = usuario.getTarefasSolicitadas();
            List<Tarefa> tarefasSobResponsabilidadeOld = persistentUsuario.getTarefasSobResponsabilidade();
            List<Tarefa> tarefasSobResponsabilidadeNew = usuario.getTarefasSobResponsabilidade();
            List<ParticipanteTarefa> paricipacoesIncluidasOld = persistentUsuario.getParicipacoesIncluidas();
            List<ParticipanteTarefa> paricipacoesIncluidasNew = usuario.getParicipacoesIncluidas();
            List<ParticipanteTarefa> tarefasParticipantesOld = persistentUsuario.getTarefasParticipantes();
            List<ParticipanteTarefa> tarefasParticipantesNew = usuario.getTarefasParticipantes();
            List<FilialCliente> filiaisClientesIncluidasOld = persistentUsuario.getFiliaisClientesIncluidas();
            List<FilialCliente> filiaisClientesIncluidasNew = usuario.getFiliaisClientesIncluidas();
            List<FilialEmpresa> filiaisEmpresaIncluidasOld = persistentUsuario.getFiliaisEmpresaIncluidas();
            List<FilialEmpresa> filiaisEmpresaIncluidasNew = usuario.getFiliaisEmpresaIncluidas();
            List<AvaliacaoMetaTarefa> avaliacoesIncluidasOld = persistentUsuario.getAvaliacoesIncluidas();
            List<AvaliacaoMetaTarefa> avaliacoesIncluidasNew = usuario.getAvaliacoesIncluidas();
            List<AvaliacaoMetaTarefa> avaliacoesSubmetidasOld = persistentUsuario.getAvaliacoesSubmetidas();
            List<AvaliacaoMetaTarefa> avaliacoesSubmetidasNew = usuario.getAvaliacoesSubmetidas();
            List<AvaliacaoMetaTarefa> avaliacoesRecebidasOld = persistentUsuario.getAvaliacoesRecebidas();
            List<AvaliacaoMetaTarefa> avaliacoesRecebidasNew = usuario.getAvaliacoesRecebidas();
            List<OrcamentoTarefa> orcamentosIncluidosOld = persistentUsuario.getOrcamentosIncluidos();
            List<OrcamentoTarefa> orcamentosIncluidosNew = usuario.getOrcamentosIncluidos();
            List<ApontamentoTarefa> apontamentosIncluidosOld = persistentUsuario.getApontamentosIncluidos();
            List<ApontamentoTarefa> apontamentosIncluidosNew = usuario.getApontamentosIncluidos();
            List<Departamento> departamentosIncluidosOld = persistentUsuario.getDepartamentosIncluidos();
            List<Departamento> departamentosIncluidosNew = usuario.getDepartamentosIncluidos();
            List<Usuario> usuariosIncluidosOld = persistentUsuario.getUsuariosIncluidos();
            List<Usuario> usuariosIncluidosNew = usuario.getUsuariosIncluidos();
            List<Empresa> empresasIncluidasOld = persistentUsuario.getEmpresasIncluidas();
            List<Empresa> empresasIncluidasNew = usuario.getEmpresasIncluidas();
            List<EmpresaCliente> empresasClienteIncluidasOld = persistentUsuario.getEmpresasClienteIncluidas();
            List<EmpresaCliente> empresasClienteIncluidasNew = usuario.getEmpresasClienteIncluidas();
            List<AnexoTarefa> anexosTarefaIncluidosOld = persistentUsuario.getAnexosTarefaIncluidos();
            List<AnexoTarefa> anexosTarefaIncluidosNew = usuario.getAnexosTarefaIncluidos();
            List<String> illegalOrphanMessages = null;
            for (UsuarioEmpresa empresasOldUsuarioEmpresa : empresasOld) {
                if (!empresasNew.contains(empresasOldUsuarioEmpresa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UsuarioEmpresa " + empresasOldUsuarioEmpresa + " since its usuario field is not nullable.");
                }
            }
            for (Meta metasSobResponsabilidadeOldMeta : metasSobResponsabilidadeOld) {
                if (!metasSobResponsabilidadeNew.contains(metasSobResponsabilidadeOldMeta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Meta " + metasSobResponsabilidadeOldMeta + " since its usuarioResponsavel field is not nullable.");
                }
            }
            for (FavoritosTarefaMeta favoritosIncluidosOldFavoritosTarefaMeta : favoritosIncluidosOld) {
                if (!favoritosIncluidosNew.contains(favoritosIncluidosOldFavoritosTarefaMeta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain FavoritosTarefaMeta " + favoritosIncluidosOldFavoritosTarefaMeta + " since its usuarioInclusao field is not nullable.");
                }
            }
            for (CentroCusto centrosCustoIncluidosOldCentroCusto : centrosCustoIncluidosOld) {
                if (!centrosCustoIncluidosNew.contains(centrosCustoIncluidosOldCentroCusto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CentroCusto " + centrosCustoIncluidosOldCentroCusto + " since its usuarioInclusao field is not nullable.");
                }
            }
            for (Endereco enderecosIncluidosOldEndereco : enderecosIncluidosOld) {
                if (!enderecosIncluidosNew.contains(enderecosIncluidosOldEndereco)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Endereco " + enderecosIncluidosOldEndereco + " since its usuarioInclusao field is not nullable.");
                }
            }
            for (Tarefa tarefasSolicitadasOldTarefa : tarefasSolicitadasOld) {
                if (!tarefasSolicitadasNew.contains(tarefasSolicitadasOldTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tarefa " + tarefasSolicitadasOldTarefa + " since its usuarioSolicitante field is not nullable.");
                }
            }
            for (Tarefa tarefasSobResponsabilidadeOldTarefa : tarefasSobResponsabilidadeOld) {
                if (!tarefasSobResponsabilidadeNew.contains(tarefasSobResponsabilidadeOldTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tarefa " + tarefasSobResponsabilidadeOldTarefa + " since its usuarioResponsavel field is not nullable.");
                }
            }
            for (ParticipanteTarefa paricipacoesIncluidasOldParticipanteTarefa : paricipacoesIncluidasOld) {
                if (!paricipacoesIncluidasNew.contains(paricipacoesIncluidasOldParticipanteTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ParticipanteTarefa " + paricipacoesIncluidasOldParticipanteTarefa + " since its usuarioInclusao field is not nullable.");
                }
            }
            for (ParticipanteTarefa tarefasParticipantesOldParticipanteTarefa : tarefasParticipantesOld) {
                if (!tarefasParticipantesNew.contains(tarefasParticipantesOldParticipanteTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ParticipanteTarefa " + tarefasParticipantesOldParticipanteTarefa + " since its usuarioParticipante field is not nullable.");
                }
            }
            for (FilialCliente filiaisClientesIncluidasOldFilialCliente : filiaisClientesIncluidasOld) {
                if (!filiaisClientesIncluidasNew.contains(filiaisClientesIncluidasOldFilialCliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain FilialCliente " + filiaisClientesIncluidasOldFilialCliente + " since its usuarioInclusao field is not nullable.");
                }
            }
            for (FilialEmpresa filiaisEmpresaIncluidasOldFilialEmpresa : filiaisEmpresaIncluidasOld) {
                if (!filiaisEmpresaIncluidasNew.contains(filiaisEmpresaIncluidasOldFilialEmpresa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain FilialEmpresa " + filiaisEmpresaIncluidasOldFilialEmpresa + " since its usuarioInclusao field is not nullable.");
                }
            }
            for (AvaliacaoMetaTarefa avaliacoesIncluidasOldAvaliacaoMetaTarefa : avaliacoesIncluidasOld) {
                if (!avaliacoesIncluidasNew.contains(avaliacoesIncluidasOldAvaliacaoMetaTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AvaliacaoMetaTarefa " + avaliacoesIncluidasOldAvaliacaoMetaTarefa + " since its usuarioInclusao field is not nullable.");
                }
            }
            for (AvaliacaoMetaTarefa avaliacoesSubmetidasOldAvaliacaoMetaTarefa : avaliacoesSubmetidasOld) {
                if (!avaliacoesSubmetidasNew.contains(avaliacoesSubmetidasOldAvaliacaoMetaTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AvaliacaoMetaTarefa " + avaliacoesSubmetidasOldAvaliacaoMetaTarefa + " since its usuarioAvaliador field is not nullable.");
                }
            }
            for (AvaliacaoMetaTarefa avaliacoesRecebidasOldAvaliacaoMetaTarefa : avaliacoesRecebidasOld) {
                if (!avaliacoesRecebidasNew.contains(avaliacoesRecebidasOldAvaliacaoMetaTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AvaliacaoMetaTarefa " + avaliacoesRecebidasOldAvaliacaoMetaTarefa + " since its usuarioAvaliado field is not nullable.");
                }
            }
            for (OrcamentoTarefa orcamentosIncluidosOldOrcamentoTarefa : orcamentosIncluidosOld) {
                if (!orcamentosIncluidosNew.contains(orcamentosIncluidosOldOrcamentoTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain OrcamentoTarefa " + orcamentosIncluidosOldOrcamentoTarefa + " since its usuarioInclusao field is not nullable.");
                }
            }
            for (ApontamentoTarefa apontamentosIncluidosOldApontamentoTarefa : apontamentosIncluidosOld) {
                if (!apontamentosIncluidosNew.contains(apontamentosIncluidosOldApontamentoTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ApontamentoTarefa " + apontamentosIncluidosOldApontamentoTarefa + " since its usuarioInclusao field is not nullable.");
                }
            }
            for (Departamento departamentosIncluidosOldDepartamento : departamentosIncluidosOld) {
                if (!departamentosIncluidosNew.contains(departamentosIncluidosOldDepartamento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Departamento " + departamentosIncluidosOldDepartamento + " since its usuarioInclusao field is not nullable.");
                }
            }
            for (Empresa empresasIncluidasOldEmpresa : empresasIncluidasOld) {
                if (!empresasIncluidasNew.contains(empresasIncluidasOldEmpresa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Empresa " + empresasIncluidasOldEmpresa + " since its usuarioInclusao field is not nullable.");
                }
            }
            for (EmpresaCliente empresasClienteIncluidasOldEmpresaCliente : empresasClienteIncluidasOld) {
                if (!empresasClienteIncluidasNew.contains(empresasClienteIncluidasOldEmpresaCliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain EmpresaCliente " + empresasClienteIncluidasOldEmpresaCliente + " since its usuarioInclusao field is not nullable.");
                }
            }
            for (AnexoTarefa anexosTarefaIncluidosOldAnexoTarefa : anexosTarefaIncluidosOld) {
                if (!anexosTarefaIncluidosNew.contains(anexosTarefaIncluidosOldAnexoTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AnexoTarefa " + anexosTarefaIncluidosOldAnexoTarefa + " since its usuarioInclusao field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (usuarioInclusaoNew != null) {
                usuarioInclusaoNew = em.getReference(usuarioInclusaoNew.getClass(), usuarioInclusaoNew.getId());
                usuario.setUsuarioInclusao(usuarioInclusaoNew);
            }
            Collection<UsuarioEmpresa> attachedEmpresasNew = new ArrayList<UsuarioEmpresa>();
            for (UsuarioEmpresa empresasNewUsuarioEmpresaToAttach : empresasNew) {
                empresasNewUsuarioEmpresaToAttach = em.getReference(empresasNewUsuarioEmpresaToAttach.getClass(), empresasNewUsuarioEmpresaToAttach.getId());
                attachedEmpresasNew.add(empresasNewUsuarioEmpresaToAttach);
            }
            empresasNew = attachedEmpresasNew;
            usuario.setEmpresas(empresasNew);
            Collection<Meta> attachedMetasSobResponsabilidadeNew = new ArrayList<Meta>();
            for (Meta metasSobResponsabilidadeNewMetaToAttach : metasSobResponsabilidadeNew) {
                metasSobResponsabilidadeNewMetaToAttach = em.getReference(metasSobResponsabilidadeNewMetaToAttach.getClass(), metasSobResponsabilidadeNewMetaToAttach.getId());
                attachedMetasSobResponsabilidadeNew.add(metasSobResponsabilidadeNewMetaToAttach);
            }
            metasSobResponsabilidadeNew = attachedMetasSobResponsabilidadeNew;
            usuario.setMetasSobResponsabilidade(metasSobResponsabilidadeNew);
            List<FavoritosTarefaMeta> attachedFavoritosIncluidosNew = new ArrayList<FavoritosTarefaMeta>();
            for (FavoritosTarefaMeta favoritosIncluidosNewFavoritosTarefaMetaToAttach : favoritosIncluidosNew) {
                favoritosIncluidosNewFavoritosTarefaMetaToAttach = em.getReference(favoritosIncluidosNewFavoritosTarefaMetaToAttach.getClass(), favoritosIncluidosNewFavoritosTarefaMetaToAttach.getId());
                attachedFavoritosIncluidosNew.add(favoritosIncluidosNewFavoritosTarefaMetaToAttach);
            }
            favoritosIncluidosNew = attachedFavoritosIncluidosNew;
            usuario.setFavoritosIncluidos(favoritosIncluidosNew);
            List<CentroCusto> attachedCentrosCustoIncluidosNew = new ArrayList<CentroCusto>();
            for (CentroCusto centrosCustoIncluidosNewCentroCustoToAttach : centrosCustoIncluidosNew) {
                centrosCustoIncluidosNewCentroCustoToAttach = em.getReference(centrosCustoIncluidosNewCentroCustoToAttach.getClass(), centrosCustoIncluidosNewCentroCustoToAttach.getId());
                attachedCentrosCustoIncluidosNew.add(centrosCustoIncluidosNewCentroCustoToAttach);
            }
            centrosCustoIncluidosNew = attachedCentrosCustoIncluidosNew;
            usuario.setCentrosCustoIncluidos(centrosCustoIncluidosNew);
            List<Endereco> attachedEnderecosIncluidosNew = new ArrayList<Endereco>();
            for (Endereco enderecosIncluidosNewEnderecoToAttach : enderecosIncluidosNew) {
                enderecosIncluidosNewEnderecoToAttach = em.getReference(enderecosIncluidosNewEnderecoToAttach.getClass(), enderecosIncluidosNewEnderecoToAttach.getId());
                attachedEnderecosIncluidosNew.add(enderecosIncluidosNewEnderecoToAttach);
            }
            enderecosIncluidosNew = attachedEnderecosIncluidosNew;
            usuario.setEnderecosIncluidos(enderecosIncluidosNew);
            List<Tarefa> attachedTarefasIncluidasNew = new ArrayList<Tarefa>();
            for (Tarefa tarefasIncluidasNewTarefaToAttach : tarefasIncluidasNew) {
                tarefasIncluidasNewTarefaToAttach = em.getReference(tarefasIncluidasNewTarefaToAttach.getClass(), tarefasIncluidasNewTarefaToAttach.getId());
                attachedTarefasIncluidasNew.add(tarefasIncluidasNewTarefaToAttach);
            }
            tarefasIncluidasNew = attachedTarefasIncluidasNew;
            usuario.setTarefasIncluidas(tarefasIncluidasNew);
            List<Tarefa> attachedTarefasSolicitadasNew = new ArrayList<Tarefa>();
            for (Tarefa tarefasSolicitadasNewTarefaToAttach : tarefasSolicitadasNew) {
                tarefasSolicitadasNewTarefaToAttach = em.getReference(tarefasSolicitadasNewTarefaToAttach.getClass(), tarefasSolicitadasNewTarefaToAttach.getId());
                attachedTarefasSolicitadasNew.add(tarefasSolicitadasNewTarefaToAttach);
            }
            tarefasSolicitadasNew = attachedTarefasSolicitadasNew;
            usuario.setTarefasSolicitadas(tarefasSolicitadasNew);
            List<Tarefa> attachedTarefasSobResponsabilidadeNew = new ArrayList<Tarefa>();
            for (Tarefa tarefasSobResponsabilidadeNewTarefaToAttach : tarefasSobResponsabilidadeNew) {
                tarefasSobResponsabilidadeNewTarefaToAttach = em.getReference(tarefasSobResponsabilidadeNewTarefaToAttach.getClass(), tarefasSobResponsabilidadeNewTarefaToAttach.getId());
                attachedTarefasSobResponsabilidadeNew.add(tarefasSobResponsabilidadeNewTarefaToAttach);
            }
            tarefasSobResponsabilidadeNew = attachedTarefasSobResponsabilidadeNew;
            usuario.setTarefasSobResponsabilidade(tarefasSobResponsabilidadeNew);
            List<ParticipanteTarefa> attachedParicipacoesIncluidasNew = new ArrayList<ParticipanteTarefa>();
            for (ParticipanteTarefa paricipacoesIncluidasNewParticipanteTarefaToAttach : paricipacoesIncluidasNew) {
                paricipacoesIncluidasNewParticipanteTarefaToAttach = em.getReference(paricipacoesIncluidasNewParticipanteTarefaToAttach.getClass(), paricipacoesIncluidasNewParticipanteTarefaToAttach.getId());
                attachedParicipacoesIncluidasNew.add(paricipacoesIncluidasNewParticipanteTarefaToAttach);
            }
            paricipacoesIncluidasNew = attachedParicipacoesIncluidasNew;
            usuario.setParicipacoesIncluidas(paricipacoesIncluidasNew);
            List<ParticipanteTarefa> attachedTarefasParticipantesNew = new ArrayList<ParticipanteTarefa>();
            for (ParticipanteTarefa tarefasParticipantesNewParticipanteTarefaToAttach : tarefasParticipantesNew) {
                tarefasParticipantesNewParticipanteTarefaToAttach = em.getReference(tarefasParticipantesNewParticipanteTarefaToAttach.getClass(), tarefasParticipantesNewParticipanteTarefaToAttach.getId());
                attachedTarefasParticipantesNew.add(tarefasParticipantesNewParticipanteTarefaToAttach);
            }
            tarefasParticipantesNew = attachedTarefasParticipantesNew;
            usuario.setTarefasParticipantes(tarefasParticipantesNew);
            List<FilialCliente> attachedFiliaisClientesIncluidasNew = new ArrayList<FilialCliente>();
            for (FilialCliente filiaisClientesIncluidasNewFilialClienteToAttach : filiaisClientesIncluidasNew) {
                filiaisClientesIncluidasNewFilialClienteToAttach = em.getReference(filiaisClientesIncluidasNewFilialClienteToAttach.getClass(), filiaisClientesIncluidasNewFilialClienteToAttach.getId());
                attachedFiliaisClientesIncluidasNew.add(filiaisClientesIncluidasNewFilialClienteToAttach);
            }
            filiaisClientesIncluidasNew = attachedFiliaisClientesIncluidasNew;
            usuario.setFiliaisClientesIncluidas(filiaisClientesIncluidasNew);
            List<FilialEmpresa> attachedFiliaisEmpresaIncluidasNew = new ArrayList<FilialEmpresa>();
            for (FilialEmpresa filiaisEmpresaIncluidasNewFilialEmpresaToAttach : filiaisEmpresaIncluidasNew) {
                filiaisEmpresaIncluidasNewFilialEmpresaToAttach = em.getReference(filiaisEmpresaIncluidasNewFilialEmpresaToAttach.getClass(), filiaisEmpresaIncluidasNewFilialEmpresaToAttach.getId());
                attachedFiliaisEmpresaIncluidasNew.add(filiaisEmpresaIncluidasNewFilialEmpresaToAttach);
            }
            filiaisEmpresaIncluidasNew = attachedFiliaisEmpresaIncluidasNew;
            usuario.setFiliaisEmpresaIncluidas(filiaisEmpresaIncluidasNew);
            List<AvaliacaoMetaTarefa> attachedAvaliacoesIncluidasNew = new ArrayList<AvaliacaoMetaTarefa>();
            for (AvaliacaoMetaTarefa avaliacoesIncluidasNewAvaliacaoMetaTarefaToAttach : avaliacoesIncluidasNew) {
                avaliacoesIncluidasNewAvaliacaoMetaTarefaToAttach = em.getReference(avaliacoesIncluidasNewAvaliacaoMetaTarefaToAttach.getClass(), avaliacoesIncluidasNewAvaliacaoMetaTarefaToAttach.getId());
                attachedAvaliacoesIncluidasNew.add(avaliacoesIncluidasNewAvaliacaoMetaTarefaToAttach);
            }
            avaliacoesIncluidasNew = attachedAvaliacoesIncluidasNew;
            usuario.setAvaliacoesIncluidas(avaliacoesIncluidasNew);
            List<AvaliacaoMetaTarefa> attachedAvaliacoesSubmetidasNew = new ArrayList<AvaliacaoMetaTarefa>();
            for (AvaliacaoMetaTarefa avaliacoesSubmetidasNewAvaliacaoMetaTarefaToAttach : avaliacoesSubmetidasNew) {
                avaliacoesSubmetidasNewAvaliacaoMetaTarefaToAttach = em.getReference(avaliacoesSubmetidasNewAvaliacaoMetaTarefaToAttach.getClass(), avaliacoesSubmetidasNewAvaliacaoMetaTarefaToAttach.getId());
                attachedAvaliacoesSubmetidasNew.add(avaliacoesSubmetidasNewAvaliacaoMetaTarefaToAttach);
            }
            avaliacoesSubmetidasNew = attachedAvaliacoesSubmetidasNew;
            usuario.setAvaliacoesSubmetidas(avaliacoesSubmetidasNew);
            List<AvaliacaoMetaTarefa> attachedAvaliacoesRecebidasNew = new ArrayList<AvaliacaoMetaTarefa>();
            for (AvaliacaoMetaTarefa avaliacoesRecebidasNewAvaliacaoMetaTarefaToAttach : avaliacoesRecebidasNew) {
                avaliacoesRecebidasNewAvaliacaoMetaTarefaToAttach = em.getReference(avaliacoesRecebidasNewAvaliacaoMetaTarefaToAttach.getClass(), avaliacoesRecebidasNewAvaliacaoMetaTarefaToAttach.getId());
                attachedAvaliacoesRecebidasNew.add(avaliacoesRecebidasNewAvaliacaoMetaTarefaToAttach);
            }
            avaliacoesRecebidasNew = attachedAvaliacoesRecebidasNew;
            usuario.setAvaliacoesRecebidas(avaliacoesRecebidasNew);
            List<OrcamentoTarefa> attachedOrcamentosIncluidosNew = new ArrayList<OrcamentoTarefa>();
            for (OrcamentoTarefa orcamentosIncluidosNewOrcamentoTarefaToAttach : orcamentosIncluidosNew) {
                orcamentosIncluidosNewOrcamentoTarefaToAttach = em.getReference(orcamentosIncluidosNewOrcamentoTarefaToAttach.getClass(), orcamentosIncluidosNewOrcamentoTarefaToAttach.getId());
                attachedOrcamentosIncluidosNew.add(orcamentosIncluidosNewOrcamentoTarefaToAttach);
            }
            orcamentosIncluidosNew = attachedOrcamentosIncluidosNew;
            usuario.setOrcamentosIncluidos(orcamentosIncluidosNew);
            List<ApontamentoTarefa> attachedApontamentosIncluidosNew = new ArrayList<ApontamentoTarefa>();
            for (ApontamentoTarefa apontamentosIncluidosNewApontamentoTarefaToAttach : apontamentosIncluidosNew) {
                apontamentosIncluidosNewApontamentoTarefaToAttach = em.getReference(apontamentosIncluidosNewApontamentoTarefaToAttach.getClass(), apontamentosIncluidosNewApontamentoTarefaToAttach.getId());
                attachedApontamentosIncluidosNew.add(apontamentosIncluidosNewApontamentoTarefaToAttach);
            }
            apontamentosIncluidosNew = attachedApontamentosIncluidosNew;
            usuario.setApontamentosIncluidos(apontamentosIncluidosNew);
            List<Departamento> attachedDepartamentosIncluidosNew = new ArrayList<Departamento>();
            for (Departamento departamentosIncluidosNewDepartamentoToAttach : departamentosIncluidosNew) {
                departamentosIncluidosNewDepartamentoToAttach = em.getReference(departamentosIncluidosNewDepartamentoToAttach.getClass(), departamentosIncluidosNewDepartamentoToAttach.getId());
                attachedDepartamentosIncluidosNew.add(departamentosIncluidosNewDepartamentoToAttach);
            }
            departamentosIncluidosNew = attachedDepartamentosIncluidosNew;
            usuario.setDepartamentosIncluidos(departamentosIncluidosNew);
            List<Usuario> attachedUsuariosIncluidosNew = new ArrayList<Usuario>();
            for (Usuario usuariosIncluidosNewUsuarioToAttach : usuariosIncluidosNew) {
                usuariosIncluidosNewUsuarioToAttach = em.getReference(usuariosIncluidosNewUsuarioToAttach.getClass(), usuariosIncluidosNewUsuarioToAttach.getId());
                attachedUsuariosIncluidosNew.add(usuariosIncluidosNewUsuarioToAttach);
            }
            usuariosIncluidosNew = attachedUsuariosIncluidosNew;
            usuario.setUsuariosIncluidos(usuariosIncluidosNew);
            List<Empresa> attachedEmpresasIncluidasNew = new ArrayList<Empresa>();
            for (Empresa empresasIncluidasNewEmpresaToAttach : empresasIncluidasNew) {
                empresasIncluidasNewEmpresaToAttach = em.getReference(empresasIncluidasNewEmpresaToAttach.getClass(), empresasIncluidasNewEmpresaToAttach.getId());
                attachedEmpresasIncluidasNew.add(empresasIncluidasNewEmpresaToAttach);
            }
            empresasIncluidasNew = attachedEmpresasIncluidasNew;
            usuario.setEmpresasIncluidas(empresasIncluidasNew);
            List<EmpresaCliente> attachedEmpresasClienteIncluidasNew = new ArrayList<EmpresaCliente>();
            for (EmpresaCliente empresasClienteIncluidasNewEmpresaClienteToAttach : empresasClienteIncluidasNew) {
                empresasClienteIncluidasNewEmpresaClienteToAttach = em.getReference(empresasClienteIncluidasNewEmpresaClienteToAttach.getClass(), empresasClienteIncluidasNewEmpresaClienteToAttach.getId());
                attachedEmpresasClienteIncluidasNew.add(empresasClienteIncluidasNewEmpresaClienteToAttach);
            }
            empresasClienteIncluidasNew = attachedEmpresasClienteIncluidasNew;
            usuario.setEmpresasClienteIncluidas(empresasClienteIncluidasNew);
            List<AnexoTarefa> attachedAnexosTarefaIncluidosNew = new ArrayList<AnexoTarefa>();
            for (AnexoTarefa anexosTarefaIncluidosNewAnexoTarefaToAttach : anexosTarefaIncluidosNew) {
                anexosTarefaIncluidosNewAnexoTarefaToAttach = em.getReference(anexosTarefaIncluidosNewAnexoTarefaToAttach.getClass(), anexosTarefaIncluidosNewAnexoTarefaToAttach.getId());
                attachedAnexosTarefaIncluidosNew.add(anexosTarefaIncluidosNewAnexoTarefaToAttach);
            }
            anexosTarefaIncluidosNew = attachedAnexosTarefaIncluidosNew;
            usuario.setAnexosTarefaIncluidos(anexosTarefaIncluidosNew);
            usuario = em.merge(usuario);
            if (usuarioInclusaoOld != null && !usuarioInclusaoOld.equals(usuarioInclusaoNew)) {
                usuarioInclusaoOld.getUsuariosIncluidos().remove(usuario);
                usuarioInclusaoOld = em.merge(usuarioInclusaoOld);
            }
            if (usuarioInclusaoNew != null && !usuarioInclusaoNew.equals(usuarioInclusaoOld)) {
                usuarioInclusaoNew.getUsuariosIncluidos().add(usuario);
                usuarioInclusaoNew = em.merge(usuarioInclusaoNew);
            }
            for (UsuarioEmpresa empresasNewUsuarioEmpresa : empresasNew) {
                if (!empresasOld.contains(empresasNewUsuarioEmpresa)) {
                    Usuario oldUsuarioOfEmpresasNewUsuarioEmpresa = empresasNewUsuarioEmpresa.getUsuario();
                    empresasNewUsuarioEmpresa.setUsuario(usuario);
                    empresasNewUsuarioEmpresa = em.merge(empresasNewUsuarioEmpresa);
                    if (oldUsuarioOfEmpresasNewUsuarioEmpresa != null && !oldUsuarioOfEmpresasNewUsuarioEmpresa.equals(usuario)) {
                        oldUsuarioOfEmpresasNewUsuarioEmpresa.getEmpresas().remove(empresasNewUsuarioEmpresa);
                        oldUsuarioOfEmpresasNewUsuarioEmpresa = em.merge(oldUsuarioOfEmpresasNewUsuarioEmpresa);
                    }
                }
            }
            for (Meta metasSobResponsabilidadeNewMeta : metasSobResponsabilidadeNew) {
                if (!metasSobResponsabilidadeOld.contains(metasSobResponsabilidadeNewMeta)) {
                    Usuario oldUsuarioResponsavelOfMetasSobResponsabilidadeNewMeta = metasSobResponsabilidadeNewMeta.getUsuarioResponsavel();
                    metasSobResponsabilidadeNewMeta.setUsuarioResponsavel(usuario);
                    metasSobResponsabilidadeNewMeta = em.merge(metasSobResponsabilidadeNewMeta);
                    if (oldUsuarioResponsavelOfMetasSobResponsabilidadeNewMeta != null && !oldUsuarioResponsavelOfMetasSobResponsabilidadeNewMeta.equals(usuario)) {
                        oldUsuarioResponsavelOfMetasSobResponsabilidadeNewMeta.getMetasSobResponsabilidade().remove(metasSobResponsabilidadeNewMeta);
                        oldUsuarioResponsavelOfMetasSobResponsabilidadeNewMeta = em.merge(oldUsuarioResponsavelOfMetasSobResponsabilidadeNewMeta);
                    }
                }
            }
            for (FavoritosTarefaMeta favoritosIncluidosNewFavoritosTarefaMeta : favoritosIncluidosNew) {
                if (!favoritosIncluidosOld.contains(favoritosIncluidosNewFavoritosTarefaMeta)) {
                    Usuario oldUsuarioInclusaoOfFavoritosIncluidosNewFavoritosTarefaMeta = favoritosIncluidosNewFavoritosTarefaMeta.getUsuarioInclusao();
                    favoritosIncluidosNewFavoritosTarefaMeta.setUsuarioInclusao(usuario);
                    favoritosIncluidosNewFavoritosTarefaMeta = em.merge(favoritosIncluidosNewFavoritosTarefaMeta);
                    if (oldUsuarioInclusaoOfFavoritosIncluidosNewFavoritosTarefaMeta != null && !oldUsuarioInclusaoOfFavoritosIncluidosNewFavoritosTarefaMeta.equals(usuario)) {
                        oldUsuarioInclusaoOfFavoritosIncluidosNewFavoritosTarefaMeta.getFavoritosIncluidos().remove(favoritosIncluidosNewFavoritosTarefaMeta);
                        oldUsuarioInclusaoOfFavoritosIncluidosNewFavoritosTarefaMeta = em.merge(oldUsuarioInclusaoOfFavoritosIncluidosNewFavoritosTarefaMeta);
                    }
                }
            }
            for (CentroCusto centrosCustoIncluidosNewCentroCusto : centrosCustoIncluidosNew) {
                if (!centrosCustoIncluidosOld.contains(centrosCustoIncluidosNewCentroCusto)) {
                    Usuario oldUsuarioInclusaoOfCentrosCustoIncluidosNewCentroCusto = centrosCustoIncluidosNewCentroCusto.getUsuarioInclusao();
                    centrosCustoIncluidosNewCentroCusto.setUsuarioInclusao(usuario);
                    centrosCustoIncluidosNewCentroCusto = em.merge(centrosCustoIncluidosNewCentroCusto);
                    if (oldUsuarioInclusaoOfCentrosCustoIncluidosNewCentroCusto != null && !oldUsuarioInclusaoOfCentrosCustoIncluidosNewCentroCusto.equals(usuario)) {
                        oldUsuarioInclusaoOfCentrosCustoIncluidosNewCentroCusto.getCentrosCustoIncluidos().remove(centrosCustoIncluidosNewCentroCusto);
                        oldUsuarioInclusaoOfCentrosCustoIncluidosNewCentroCusto = em.merge(oldUsuarioInclusaoOfCentrosCustoIncluidosNewCentroCusto);
                    }
                }
            }
            for (Endereco enderecosIncluidosNewEndereco : enderecosIncluidosNew) {
                if (!enderecosIncluidosOld.contains(enderecosIncluidosNewEndereco)) {
                    Usuario oldUsuarioInclusaoOfEnderecosIncluidosNewEndereco = enderecosIncluidosNewEndereco.getUsuarioInclusao();
                    enderecosIncluidosNewEndereco.setUsuarioInclusao(usuario);
                    enderecosIncluidosNewEndereco = em.merge(enderecosIncluidosNewEndereco);
                    if (oldUsuarioInclusaoOfEnderecosIncluidosNewEndereco != null && !oldUsuarioInclusaoOfEnderecosIncluidosNewEndereco.equals(usuario)) {
                        oldUsuarioInclusaoOfEnderecosIncluidosNewEndereco.getEnderecosIncluidos().remove(enderecosIncluidosNewEndereco);
                        oldUsuarioInclusaoOfEnderecosIncluidosNewEndereco = em.merge(oldUsuarioInclusaoOfEnderecosIncluidosNewEndereco);
                    }
                }
            }
            for (Tarefa tarefasIncluidasOldTarefa : tarefasIncluidasOld) {
                if (!tarefasIncluidasNew.contains(tarefasIncluidasOldTarefa)) {
                    tarefasIncluidasOldTarefa.setUsuarioInclusao(null);
                    tarefasIncluidasOldTarefa = em.merge(tarefasIncluidasOldTarefa);
                }
            }
            for (Tarefa tarefasIncluidasNewTarefa : tarefasIncluidasNew) {
                if (!tarefasIncluidasOld.contains(tarefasIncluidasNewTarefa)) {
                    Usuario oldUsuarioInclusaoOfTarefasIncluidasNewTarefa = tarefasIncluidasNewTarefa.getUsuarioInclusao();
                    tarefasIncluidasNewTarefa.setUsuarioInclusao(usuario);
                    tarefasIncluidasNewTarefa = em.merge(tarefasIncluidasNewTarefa);
                    if (oldUsuarioInclusaoOfTarefasIncluidasNewTarefa != null && !oldUsuarioInclusaoOfTarefasIncluidasNewTarefa.equals(usuario)) {
                        oldUsuarioInclusaoOfTarefasIncluidasNewTarefa.getTarefasIncluidas().remove(tarefasIncluidasNewTarefa);
                        oldUsuarioInclusaoOfTarefasIncluidasNewTarefa = em.merge(oldUsuarioInclusaoOfTarefasIncluidasNewTarefa);
                    }
                }
            }
            for (Tarefa tarefasSolicitadasNewTarefa : tarefasSolicitadasNew) {
                if (!tarefasSolicitadasOld.contains(tarefasSolicitadasNewTarefa)) {
                    Usuario oldUsuarioSolicitanteOfTarefasSolicitadasNewTarefa = tarefasSolicitadasNewTarefa.getUsuarioSolicitante();
                    tarefasSolicitadasNewTarefa.setUsuarioSolicitante(usuario);
                    tarefasSolicitadasNewTarefa = em.merge(tarefasSolicitadasNewTarefa);
                    if (oldUsuarioSolicitanteOfTarefasSolicitadasNewTarefa != null && !oldUsuarioSolicitanteOfTarefasSolicitadasNewTarefa.equals(usuario)) {
                        oldUsuarioSolicitanteOfTarefasSolicitadasNewTarefa.getTarefasSolicitadas().remove(tarefasSolicitadasNewTarefa);
                        oldUsuarioSolicitanteOfTarefasSolicitadasNewTarefa = em.merge(oldUsuarioSolicitanteOfTarefasSolicitadasNewTarefa);
                    }
                }
            }
            for (Tarefa tarefasSobResponsabilidadeNewTarefa : tarefasSobResponsabilidadeNew) {
                if (!tarefasSobResponsabilidadeOld.contains(tarefasSobResponsabilidadeNewTarefa)) {
                    Usuario oldUsuarioResponsavelOfTarefasSobResponsabilidadeNewTarefa = tarefasSobResponsabilidadeNewTarefa.getUsuarioResponsavel();
                    tarefasSobResponsabilidadeNewTarefa.setUsuarioResponsavel(usuario);
                    tarefasSobResponsabilidadeNewTarefa = em.merge(tarefasSobResponsabilidadeNewTarefa);
                    if (oldUsuarioResponsavelOfTarefasSobResponsabilidadeNewTarefa != null && !oldUsuarioResponsavelOfTarefasSobResponsabilidadeNewTarefa.equals(usuario)) {
                        oldUsuarioResponsavelOfTarefasSobResponsabilidadeNewTarefa.getTarefasSobResponsabilidade().remove(tarefasSobResponsabilidadeNewTarefa);
                        oldUsuarioResponsavelOfTarefasSobResponsabilidadeNewTarefa = em.merge(oldUsuarioResponsavelOfTarefasSobResponsabilidadeNewTarefa);
                    }
                }
            }
            for (ParticipanteTarefa paricipacoesIncluidasNewParticipanteTarefa : paricipacoesIncluidasNew) {
                if (!paricipacoesIncluidasOld.contains(paricipacoesIncluidasNewParticipanteTarefa)) {
                    Usuario oldUsuarioInclusaoOfParicipacoesIncluidasNewParticipanteTarefa = paricipacoesIncluidasNewParticipanteTarefa.getUsuarioInclusao();
                    paricipacoesIncluidasNewParticipanteTarefa.setUsuarioInclusao(usuario);
                    paricipacoesIncluidasNewParticipanteTarefa = em.merge(paricipacoesIncluidasNewParticipanteTarefa);
                    if (oldUsuarioInclusaoOfParicipacoesIncluidasNewParticipanteTarefa != null && !oldUsuarioInclusaoOfParicipacoesIncluidasNewParticipanteTarefa.equals(usuario)) {
                        oldUsuarioInclusaoOfParicipacoesIncluidasNewParticipanteTarefa.getParicipacoesIncluidas().remove(paricipacoesIncluidasNewParticipanteTarefa);
                        oldUsuarioInclusaoOfParicipacoesIncluidasNewParticipanteTarefa = em.merge(oldUsuarioInclusaoOfParicipacoesIncluidasNewParticipanteTarefa);
                    }
                }
            }
            for (ParticipanteTarefa tarefasParticipantesNewParticipanteTarefa : tarefasParticipantesNew) {
                if (!tarefasParticipantesOld.contains(tarefasParticipantesNewParticipanteTarefa)) {
                    Usuario oldUsuarioParticipanteOfTarefasParticipantesNewParticipanteTarefa = tarefasParticipantesNewParticipanteTarefa.getUsuarioParticipante();
                    tarefasParticipantesNewParticipanteTarefa.setUsuarioParticipante(usuario);
                    tarefasParticipantesNewParticipanteTarefa = em.merge(tarefasParticipantesNewParticipanteTarefa);
                    if (oldUsuarioParticipanteOfTarefasParticipantesNewParticipanteTarefa != null && !oldUsuarioParticipanteOfTarefasParticipantesNewParticipanteTarefa.equals(usuario)) {
                        oldUsuarioParticipanteOfTarefasParticipantesNewParticipanteTarefa.getTarefasParticipantes().remove(tarefasParticipantesNewParticipanteTarefa);
                        oldUsuarioParticipanteOfTarefasParticipantesNewParticipanteTarefa = em.merge(oldUsuarioParticipanteOfTarefasParticipantesNewParticipanteTarefa);
                    }
                }
            }
            for (FilialCliente filiaisClientesIncluidasNewFilialCliente : filiaisClientesIncluidasNew) {
                if (!filiaisClientesIncluidasOld.contains(filiaisClientesIncluidasNewFilialCliente)) {
                    Usuario oldUsuarioInclusaoOfFiliaisClientesIncluidasNewFilialCliente = filiaisClientesIncluidasNewFilialCliente.getUsuarioInclusao();
                    filiaisClientesIncluidasNewFilialCliente.setUsuarioInclusao(usuario);
                    filiaisClientesIncluidasNewFilialCliente = em.merge(filiaisClientesIncluidasNewFilialCliente);
                    if (oldUsuarioInclusaoOfFiliaisClientesIncluidasNewFilialCliente != null && !oldUsuarioInclusaoOfFiliaisClientesIncluidasNewFilialCliente.equals(usuario)) {
                        oldUsuarioInclusaoOfFiliaisClientesIncluidasNewFilialCliente.getFiliaisClientesIncluidas().remove(filiaisClientesIncluidasNewFilialCliente);
                        oldUsuarioInclusaoOfFiliaisClientesIncluidasNewFilialCliente = em.merge(oldUsuarioInclusaoOfFiliaisClientesIncluidasNewFilialCliente);
                    }
                }
            }
            for (FilialEmpresa filiaisEmpresaIncluidasNewFilialEmpresa : filiaisEmpresaIncluidasNew) {
                if (!filiaisEmpresaIncluidasOld.contains(filiaisEmpresaIncluidasNewFilialEmpresa)) {
                    Usuario oldUsuarioInclusaoOfFiliaisEmpresaIncluidasNewFilialEmpresa = filiaisEmpresaIncluidasNewFilialEmpresa.getUsuarioInclusao();
                    filiaisEmpresaIncluidasNewFilialEmpresa.setUsuarioInclusao(usuario);
                    filiaisEmpresaIncluidasNewFilialEmpresa = em.merge(filiaisEmpresaIncluidasNewFilialEmpresa);
                    if (oldUsuarioInclusaoOfFiliaisEmpresaIncluidasNewFilialEmpresa != null && !oldUsuarioInclusaoOfFiliaisEmpresaIncluidasNewFilialEmpresa.equals(usuario)) {
                        oldUsuarioInclusaoOfFiliaisEmpresaIncluidasNewFilialEmpresa.getFiliaisEmpresaIncluidas().remove(filiaisEmpresaIncluidasNewFilialEmpresa);
                        oldUsuarioInclusaoOfFiliaisEmpresaIncluidasNewFilialEmpresa = em.merge(oldUsuarioInclusaoOfFiliaisEmpresaIncluidasNewFilialEmpresa);
                    }
                }
            }
            for (AvaliacaoMetaTarefa avaliacoesIncluidasNewAvaliacaoMetaTarefa : avaliacoesIncluidasNew) {
                if (!avaliacoesIncluidasOld.contains(avaliacoesIncluidasNewAvaliacaoMetaTarefa)) {
                    Usuario oldUsuarioInclusaoOfAvaliacoesIncluidasNewAvaliacaoMetaTarefa = avaliacoesIncluidasNewAvaliacaoMetaTarefa.getUsuarioInclusao();
                    avaliacoesIncluidasNewAvaliacaoMetaTarefa.setUsuarioInclusao(usuario);
                    avaliacoesIncluidasNewAvaliacaoMetaTarefa = em.merge(avaliacoesIncluidasNewAvaliacaoMetaTarefa);
                    if (oldUsuarioInclusaoOfAvaliacoesIncluidasNewAvaliacaoMetaTarefa != null && !oldUsuarioInclusaoOfAvaliacoesIncluidasNewAvaliacaoMetaTarefa.equals(usuario)) {
                        oldUsuarioInclusaoOfAvaliacoesIncluidasNewAvaliacaoMetaTarefa.getAvaliacoesIncluidas().remove(avaliacoesIncluidasNewAvaliacaoMetaTarefa);
                        oldUsuarioInclusaoOfAvaliacoesIncluidasNewAvaliacaoMetaTarefa = em.merge(oldUsuarioInclusaoOfAvaliacoesIncluidasNewAvaliacaoMetaTarefa);
                    }
                }
            }
            for (AvaliacaoMetaTarefa avaliacoesSubmetidasNewAvaliacaoMetaTarefa : avaliacoesSubmetidasNew) {
                if (!avaliacoesSubmetidasOld.contains(avaliacoesSubmetidasNewAvaliacaoMetaTarefa)) {
                    Usuario oldUsuarioAvaliadorOfAvaliacoesSubmetidasNewAvaliacaoMetaTarefa = avaliacoesSubmetidasNewAvaliacaoMetaTarefa.getUsuarioAvaliador();
                    avaliacoesSubmetidasNewAvaliacaoMetaTarefa.setUsuarioAvaliador(usuario);
                    avaliacoesSubmetidasNewAvaliacaoMetaTarefa = em.merge(avaliacoesSubmetidasNewAvaliacaoMetaTarefa);
                    if (oldUsuarioAvaliadorOfAvaliacoesSubmetidasNewAvaliacaoMetaTarefa != null && !oldUsuarioAvaliadorOfAvaliacoesSubmetidasNewAvaliacaoMetaTarefa.equals(usuario)) {
                        oldUsuarioAvaliadorOfAvaliacoesSubmetidasNewAvaliacaoMetaTarefa.getAvaliacoesSubmetidas().remove(avaliacoesSubmetidasNewAvaliacaoMetaTarefa);
                        oldUsuarioAvaliadorOfAvaliacoesSubmetidasNewAvaliacaoMetaTarefa = em.merge(oldUsuarioAvaliadorOfAvaliacoesSubmetidasNewAvaliacaoMetaTarefa);
                    }
                }
            }
            for (AvaliacaoMetaTarefa avaliacoesRecebidasNewAvaliacaoMetaTarefa : avaliacoesRecebidasNew) {
                if (!avaliacoesRecebidasOld.contains(avaliacoesRecebidasNewAvaliacaoMetaTarefa)) {
                    Usuario oldUsuarioAvaliadoOfAvaliacoesRecebidasNewAvaliacaoMetaTarefa = avaliacoesRecebidasNewAvaliacaoMetaTarefa.getUsuarioAvaliado();
                    avaliacoesRecebidasNewAvaliacaoMetaTarefa.setUsuarioAvaliado(usuario);
                    avaliacoesRecebidasNewAvaliacaoMetaTarefa = em.merge(avaliacoesRecebidasNewAvaliacaoMetaTarefa);
                    if (oldUsuarioAvaliadoOfAvaliacoesRecebidasNewAvaliacaoMetaTarefa != null && !oldUsuarioAvaliadoOfAvaliacoesRecebidasNewAvaliacaoMetaTarefa.equals(usuario)) {
                        oldUsuarioAvaliadoOfAvaliacoesRecebidasNewAvaliacaoMetaTarefa.getAvaliacoesRecebidas().remove(avaliacoesRecebidasNewAvaliacaoMetaTarefa);
                        oldUsuarioAvaliadoOfAvaliacoesRecebidasNewAvaliacaoMetaTarefa = em.merge(oldUsuarioAvaliadoOfAvaliacoesRecebidasNewAvaliacaoMetaTarefa);
                    }
                }
            }
            for (OrcamentoTarefa orcamentosIncluidosNewOrcamentoTarefa : orcamentosIncluidosNew) {
                if (!orcamentosIncluidosOld.contains(orcamentosIncluidosNewOrcamentoTarefa)) {
                    Usuario oldUsuarioInclusaoOfOrcamentosIncluidosNewOrcamentoTarefa = orcamentosIncluidosNewOrcamentoTarefa.getUsuarioInclusao();
                    orcamentosIncluidosNewOrcamentoTarefa.setUsuarioInclusao(usuario);
                    orcamentosIncluidosNewOrcamentoTarefa = em.merge(orcamentosIncluidosNewOrcamentoTarefa);
                    if (oldUsuarioInclusaoOfOrcamentosIncluidosNewOrcamentoTarefa != null && !oldUsuarioInclusaoOfOrcamentosIncluidosNewOrcamentoTarefa.equals(usuario)) {
                        oldUsuarioInclusaoOfOrcamentosIncluidosNewOrcamentoTarefa.getOrcamentosIncluidos().remove(orcamentosIncluidosNewOrcamentoTarefa);
                        oldUsuarioInclusaoOfOrcamentosIncluidosNewOrcamentoTarefa = em.merge(oldUsuarioInclusaoOfOrcamentosIncluidosNewOrcamentoTarefa);
                    }
                }
            }
            for (ApontamentoTarefa apontamentosIncluidosNewApontamentoTarefa : apontamentosIncluidosNew) {
                if (!apontamentosIncluidosOld.contains(apontamentosIncluidosNewApontamentoTarefa)) {
                    Usuario oldUsuarioInclusaoOfApontamentosIncluidosNewApontamentoTarefa = apontamentosIncluidosNewApontamentoTarefa.getUsuarioInclusao();
                    apontamentosIncluidosNewApontamentoTarefa.setUsuarioInclusao(usuario);
                    apontamentosIncluidosNewApontamentoTarefa = em.merge(apontamentosIncluidosNewApontamentoTarefa);
                    if (oldUsuarioInclusaoOfApontamentosIncluidosNewApontamentoTarefa != null && !oldUsuarioInclusaoOfApontamentosIncluidosNewApontamentoTarefa.equals(usuario)) {
                        oldUsuarioInclusaoOfApontamentosIncluidosNewApontamentoTarefa.getApontamentosIncluidos().remove(apontamentosIncluidosNewApontamentoTarefa);
                        oldUsuarioInclusaoOfApontamentosIncluidosNewApontamentoTarefa = em.merge(oldUsuarioInclusaoOfApontamentosIncluidosNewApontamentoTarefa);
                    }
                }
            }
            for (Departamento departamentosIncluidosNewDepartamento : departamentosIncluidosNew) {
                if (!departamentosIncluidosOld.contains(departamentosIncluidosNewDepartamento)) {
                    Usuario oldUsuarioInclusaoOfDepartamentosIncluidosNewDepartamento = departamentosIncluidosNewDepartamento.getUsuarioInclusao();
                    departamentosIncluidosNewDepartamento.setUsuarioInclusao(usuario);
                    departamentosIncluidosNewDepartamento = em.merge(departamentosIncluidosNewDepartamento);
                    if (oldUsuarioInclusaoOfDepartamentosIncluidosNewDepartamento != null && !oldUsuarioInclusaoOfDepartamentosIncluidosNewDepartamento.equals(usuario)) {
                        oldUsuarioInclusaoOfDepartamentosIncluidosNewDepartamento.getDepartamentosIncluidos().remove(departamentosIncluidosNewDepartamento);
                        oldUsuarioInclusaoOfDepartamentosIncluidosNewDepartamento = em.merge(oldUsuarioInclusaoOfDepartamentosIncluidosNewDepartamento);
                    }
                }
            }
            for (Usuario usuariosIncluidosOldUsuario : usuariosIncluidosOld) {
                if (!usuariosIncluidosNew.contains(usuariosIncluidosOldUsuario)) {
                    usuariosIncluidosOldUsuario.setUsuarioInclusao(null);
                    usuariosIncluidosOldUsuario = em.merge(usuariosIncluidosOldUsuario);
                }
            }
            for (Usuario usuariosIncluidosNewUsuario : usuariosIncluidosNew) {
                if (!usuariosIncluidosOld.contains(usuariosIncluidosNewUsuario)) {
                    Usuario oldUsuarioInclusaoOfUsuariosIncluidosNewUsuario = usuariosIncluidosNewUsuario.getUsuarioInclusao();
                    usuariosIncluidosNewUsuario.setUsuarioInclusao(usuario);
                    usuariosIncluidosNewUsuario = em.merge(usuariosIncluidosNewUsuario);
                    if (oldUsuarioInclusaoOfUsuariosIncluidosNewUsuario != null && !oldUsuarioInclusaoOfUsuariosIncluidosNewUsuario.equals(usuario)) {
                        oldUsuarioInclusaoOfUsuariosIncluidosNewUsuario.getUsuariosIncluidos().remove(usuariosIncluidosNewUsuario);
                        oldUsuarioInclusaoOfUsuariosIncluidosNewUsuario = em.merge(oldUsuarioInclusaoOfUsuariosIncluidosNewUsuario);
                    }
                }
            }
            for (Empresa empresasIncluidasNewEmpresa : empresasIncluidasNew) {
                if (!empresasIncluidasOld.contains(empresasIncluidasNewEmpresa)) {
                    Usuario oldUsuarioInclusaoOfEmpresasIncluidasNewEmpresa = empresasIncluidasNewEmpresa.getUsuarioInclusao();
                    empresasIncluidasNewEmpresa.setUsuarioInclusao(usuario);
                    empresasIncluidasNewEmpresa = em.merge(empresasIncluidasNewEmpresa);
                    if (oldUsuarioInclusaoOfEmpresasIncluidasNewEmpresa != null && !oldUsuarioInclusaoOfEmpresasIncluidasNewEmpresa.equals(usuario)) {
                        oldUsuarioInclusaoOfEmpresasIncluidasNewEmpresa.getEmpresasIncluidas().remove(empresasIncluidasNewEmpresa);
                        oldUsuarioInclusaoOfEmpresasIncluidasNewEmpresa = em.merge(oldUsuarioInclusaoOfEmpresasIncluidasNewEmpresa);
                    }
                }
            }
            for (EmpresaCliente empresasClienteIncluidasNewEmpresaCliente : empresasClienteIncluidasNew) {
                if (!empresasClienteIncluidasOld.contains(empresasClienteIncluidasNewEmpresaCliente)) {
                    Usuario oldUsuarioInclusaoOfEmpresasClienteIncluidasNewEmpresaCliente = empresasClienteIncluidasNewEmpresaCliente.getUsuarioInclusao();
                    empresasClienteIncluidasNewEmpresaCliente.setUsuarioInclusao(usuario);
                    empresasClienteIncluidasNewEmpresaCliente = em.merge(empresasClienteIncluidasNewEmpresaCliente);
                    if (oldUsuarioInclusaoOfEmpresasClienteIncluidasNewEmpresaCliente != null && !oldUsuarioInclusaoOfEmpresasClienteIncluidasNewEmpresaCliente.equals(usuario)) {
                        oldUsuarioInclusaoOfEmpresasClienteIncluidasNewEmpresaCliente.getEmpresasClienteIncluidas().remove(empresasClienteIncluidasNewEmpresaCliente);
                        oldUsuarioInclusaoOfEmpresasClienteIncluidasNewEmpresaCliente = em.merge(oldUsuarioInclusaoOfEmpresasClienteIncluidasNewEmpresaCliente);
                    }
                }
            }
            for (AnexoTarefa anexosTarefaIncluidosNewAnexoTarefa : anexosTarefaIncluidosNew) {
                if (!anexosTarefaIncluidosOld.contains(anexosTarefaIncluidosNewAnexoTarefa)) {
                    Usuario oldUsuarioInclusaoOfAnexosTarefaIncluidosNewAnexoTarefa = anexosTarefaIncluidosNewAnexoTarefa.getUsuarioInclusao();
                    anexosTarefaIncluidosNewAnexoTarefa.setUsuarioInclusao(usuario);
                    anexosTarefaIncluidosNewAnexoTarefa = em.merge(anexosTarefaIncluidosNewAnexoTarefa);
                    if (oldUsuarioInclusaoOfAnexosTarefaIncluidosNewAnexoTarefa != null && !oldUsuarioInclusaoOfAnexosTarefaIncluidosNewAnexoTarefa.equals(usuario)) {
                        oldUsuarioInclusaoOfAnexosTarefaIncluidosNewAnexoTarefa.getAnexosTarefaIncluidos().remove(anexosTarefaIncluidosNewAnexoTarefa);
                        oldUsuarioInclusaoOfAnexosTarefaIncluidosNewAnexoTarefa = em.merge(oldUsuarioInclusaoOfAnexosTarefaIncluidosNewAnexoTarefa);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getId();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<UsuarioEmpresa> empresasOrphanCheck = usuario.getEmpresas();
            for (UsuarioEmpresa empresasOrphanCheckUsuarioEmpresa : empresasOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the UsuarioEmpresa " + empresasOrphanCheckUsuarioEmpresa + " in its empresas field has a non-nullable usuario field.");
            }
            Collection<Meta> metasSobResponsabilidadeOrphanCheck = usuario.getMetasSobResponsabilidade();
            for (Meta metasSobResponsabilidadeOrphanCheckMeta : metasSobResponsabilidadeOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Meta " + metasSobResponsabilidadeOrphanCheckMeta + " in its metasSobResponsabilidade field has a non-nullable usuarioResponsavel field.");
            }
            List<FavoritosTarefaMeta> favoritosIncluidosOrphanCheck = usuario.getFavoritosIncluidos();
            for (FavoritosTarefaMeta favoritosIncluidosOrphanCheckFavoritosTarefaMeta : favoritosIncluidosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the FavoritosTarefaMeta " + favoritosIncluidosOrphanCheckFavoritosTarefaMeta + " in its favoritosIncluidos field has a non-nullable usuarioInclusao field.");
            }
            List<CentroCusto> centrosCustoIncluidosOrphanCheck = usuario.getCentrosCustoIncluidos();
            for (CentroCusto centrosCustoIncluidosOrphanCheckCentroCusto : centrosCustoIncluidosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the CentroCusto " + centrosCustoIncluidosOrphanCheckCentroCusto + " in its centrosCustoIncluidos field has a non-nullable usuarioInclusao field.");
            }
            List<Endereco> enderecosIncluidosOrphanCheck = usuario.getEnderecosIncluidos();
            for (Endereco enderecosIncluidosOrphanCheckEndereco : enderecosIncluidosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Endereco " + enderecosIncluidosOrphanCheckEndereco + " in its enderecosIncluidos field has a non-nullable usuarioInclusao field.");
            }
            List<Tarefa> tarefasSolicitadasOrphanCheck = usuario.getTarefasSolicitadas();
            for (Tarefa tarefasSolicitadasOrphanCheckTarefa : tarefasSolicitadasOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Tarefa " + tarefasSolicitadasOrphanCheckTarefa + " in its tarefasSolicitadas field has a non-nullable usuarioSolicitante field.");
            }
            List<Tarefa> tarefasSobResponsabilidadeOrphanCheck = usuario.getTarefasSobResponsabilidade();
            for (Tarefa tarefasSobResponsabilidadeOrphanCheckTarefa : tarefasSobResponsabilidadeOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Tarefa " + tarefasSobResponsabilidadeOrphanCheckTarefa + " in its tarefasSobResponsabilidade field has a non-nullable usuarioResponsavel field.");
            }
            List<ParticipanteTarefa> paricipacoesIncluidasOrphanCheck = usuario.getParicipacoesIncluidas();
            for (ParticipanteTarefa paricipacoesIncluidasOrphanCheckParticipanteTarefa : paricipacoesIncluidasOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the ParticipanteTarefa " + paricipacoesIncluidasOrphanCheckParticipanteTarefa + " in its paricipacoesIncluidas field has a non-nullable usuarioInclusao field.");
            }
            List<ParticipanteTarefa> tarefasParticipantesOrphanCheck = usuario.getTarefasParticipantes();
            for (ParticipanteTarefa tarefasParticipantesOrphanCheckParticipanteTarefa : tarefasParticipantesOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the ParticipanteTarefa " + tarefasParticipantesOrphanCheckParticipanteTarefa + " in its tarefasParticipantes field has a non-nullable usuarioParticipante field.");
            }
            List<FilialCliente> filiaisClientesIncluidasOrphanCheck = usuario.getFiliaisClientesIncluidas();
            for (FilialCliente filiaisClientesIncluidasOrphanCheckFilialCliente : filiaisClientesIncluidasOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the FilialCliente " + filiaisClientesIncluidasOrphanCheckFilialCliente + " in its filiaisClientesIncluidas field has a non-nullable usuarioInclusao field.");
            }
            List<FilialEmpresa> filiaisEmpresaIncluidasOrphanCheck = usuario.getFiliaisEmpresaIncluidas();
            for (FilialEmpresa filiaisEmpresaIncluidasOrphanCheckFilialEmpresa : filiaisEmpresaIncluidasOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the FilialEmpresa " + filiaisEmpresaIncluidasOrphanCheckFilialEmpresa + " in its filiaisEmpresaIncluidas field has a non-nullable usuarioInclusao field.");
            }
            List<AvaliacaoMetaTarefa> avaliacoesIncluidasOrphanCheck = usuario.getAvaliacoesIncluidas();
            for (AvaliacaoMetaTarefa avaliacoesIncluidasOrphanCheckAvaliacaoMetaTarefa : avaliacoesIncluidasOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the AvaliacaoMetaTarefa " + avaliacoesIncluidasOrphanCheckAvaliacaoMetaTarefa + " in its avaliacoesIncluidas field has a non-nullable usuarioInclusao field.");
            }
            List<AvaliacaoMetaTarefa> avaliacoesSubmetidasOrphanCheck = usuario.getAvaliacoesSubmetidas();
            for (AvaliacaoMetaTarefa avaliacoesSubmetidasOrphanCheckAvaliacaoMetaTarefa : avaliacoesSubmetidasOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the AvaliacaoMetaTarefa " + avaliacoesSubmetidasOrphanCheckAvaliacaoMetaTarefa + " in its avaliacoesSubmetidas field has a non-nullable usuarioAvaliador field.");
            }
            List<AvaliacaoMetaTarefa> avaliacoesRecebidasOrphanCheck = usuario.getAvaliacoesRecebidas();
            for (AvaliacaoMetaTarefa avaliacoesRecebidasOrphanCheckAvaliacaoMetaTarefa : avaliacoesRecebidasOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the AvaliacaoMetaTarefa " + avaliacoesRecebidasOrphanCheckAvaliacaoMetaTarefa + " in its avaliacoesRecebidas field has a non-nullable usuarioAvaliado field.");
            }
            List<OrcamentoTarefa> orcamentosIncluidosOrphanCheck = usuario.getOrcamentosIncluidos();
            for (OrcamentoTarefa orcamentosIncluidosOrphanCheckOrcamentoTarefa : orcamentosIncluidosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the OrcamentoTarefa " + orcamentosIncluidosOrphanCheckOrcamentoTarefa + " in its orcamentosIncluidos field has a non-nullable usuarioInclusao field.");
            }
            List<ApontamentoTarefa> apontamentosIncluidosOrphanCheck = usuario.getApontamentosIncluidos();
            for (ApontamentoTarefa apontamentosIncluidosOrphanCheckApontamentoTarefa : apontamentosIncluidosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the ApontamentoTarefa " + apontamentosIncluidosOrphanCheckApontamentoTarefa + " in its apontamentosIncluidos field has a non-nullable usuarioInclusao field.");
            }
            List<Departamento> departamentosIncluidosOrphanCheck = usuario.getDepartamentosIncluidos();
            for (Departamento departamentosIncluidosOrphanCheckDepartamento : departamentosIncluidosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Departamento " + departamentosIncluidosOrphanCheckDepartamento + " in its departamentosIncluidos field has a non-nullable usuarioInclusao field.");
            }
            List<Empresa> empresasIncluidasOrphanCheck = usuario.getEmpresasIncluidas();
            for (Empresa empresasIncluidasOrphanCheckEmpresa : empresasIncluidasOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Empresa " + empresasIncluidasOrphanCheckEmpresa + " in its empresasIncluidas field has a non-nullable usuarioInclusao field.");
            }
            List<EmpresaCliente> empresasClienteIncluidasOrphanCheck = usuario.getEmpresasClienteIncluidas();
            for (EmpresaCliente empresasClienteIncluidasOrphanCheckEmpresaCliente : empresasClienteIncluidasOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the EmpresaCliente " + empresasClienteIncluidasOrphanCheckEmpresaCliente + " in its empresasClienteIncluidas field has a non-nullable usuarioInclusao field.");
            }
            List<AnexoTarefa> anexosTarefaIncluidosOrphanCheck = usuario.getAnexosTarefaIncluidos();
            for (AnexoTarefa anexosTarefaIncluidosOrphanCheckAnexoTarefa : anexosTarefaIncluidosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the AnexoTarefa " + anexosTarefaIncluidosOrphanCheckAnexoTarefa + " in its anexosTarefaIncluidos field has a non-nullable usuarioInclusao field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuario usuarioInclusao = usuario.getUsuarioInclusao();
            if (usuarioInclusao != null) {
                usuarioInclusao.getUsuariosIncluidos().remove(usuario);
                usuarioInclusao = em.merge(usuarioInclusao);
            }
            List<Tarefa> tarefasIncluidas = usuario.getTarefasIncluidas();
            for (Tarefa tarefasIncluidasTarefa : tarefasIncluidas) {
                tarefasIncluidasTarefa.setUsuarioInclusao(null);
                tarefasIncluidasTarefa = em.merge(tarefasIncluidasTarefa);
            }
            List<Usuario> usuariosIncluidos = usuario.getUsuariosIncluidos();
            for (Usuario usuariosIncluidosUsuario : usuariosIncluidos) {
                usuariosIncluidosUsuario.setUsuarioInclusao(null);
                usuariosIncluidosUsuario = em.merge(usuariosIncluidosUsuario);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
   
    
}
