package com.saax.gestorweb.dao;

import com.saax.gestorweb.dao.exceptions.IllegalOrphanException;
import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.model.datamodel.UsuarioEmpresa;
import java.util.ArrayList;
import java.util.Collection;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.FavoritosTarefaMeta;
import java.util.List;
import com.saax.gestorweb.model.datamodel.CentroCusto;
import com.saax.gestorweb.model.datamodel.Endereco;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.ParicipanteTarefa;
import com.saax.gestorweb.model.datamodel.FilialCliente;
import com.saax.gestorweb.model.datamodel.FilialEmpresa;
import com.saax.gestorweb.model.datamodel.AvaliacaoMetaTarefa;
import com.saax.gestorweb.model.datamodel.OrcamentoTarefa;
import com.saax.gestorweb.model.datamodel.ApontamentoTarefa;
import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.AnexoTarefa;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * DAO para o entity bean: Usuario <br><br>
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
        if (usuario.getMetasResponsaveis() == null) {
            usuario.setMetasResponsaveis(new ArrayList<Meta>());
        }
        if (usuario.getFavoritosTarefaMetaList() == null) {
            usuario.setFavoritosTarefaMetaList(new ArrayList<FavoritosTarefaMeta>());
        }
        if (usuario.getCentroCustoList() == null) {
            usuario.setCentroCustoList(new ArrayList<CentroCusto>());
        }
        if (usuario.getEnderecoList() == null) {
            usuario.setEnderecoList(new ArrayList<Endereco>());
        }
        if (usuario.getTarefaList() == null) {
            usuario.setTarefaList(new ArrayList<Tarefa>());
        }
        if (usuario.getTarefaList1() == null) {
            usuario.setTarefaList1(new ArrayList<Tarefa>());
        }
        if (usuario.getTarefaList2() == null) {
            usuario.setTarefaList2(new ArrayList<Tarefa>());
        }
        if (usuario.getParicipanteTarefaList() == null) {
            usuario.setParicipanteTarefaList(new ArrayList<ParicipanteTarefa>());
        }
        if (usuario.getParicipanteTarefaList1() == null) {
            usuario.setParicipanteTarefaList1(new ArrayList<ParicipanteTarefa>());
        }
        if (usuario.getFilialClienteList() == null) {
            usuario.setFilialClienteList(new ArrayList<FilialCliente>());
        }
        if (usuario.getFilialEmpresaList() == null) {
            usuario.setFilialEmpresaList(new ArrayList<FilialEmpresa>());
        }
        if (usuario.getAvaliacaoMetaTarefaList() == null) {
            usuario.setAvaliacaoMetaTarefaList(new ArrayList<AvaliacaoMetaTarefa>());
        }
        if (usuario.getAvaliacaoMetaTarefaList1() == null) {
            usuario.setAvaliacaoMetaTarefaList1(new ArrayList<AvaliacaoMetaTarefa>());
        }
        if (usuario.getAvaliacaoMetaTarefaList2() == null) {
            usuario.setAvaliacaoMetaTarefaList2(new ArrayList<AvaliacaoMetaTarefa>());
        }
        if (usuario.getOrcamentoTarefaList() == null) {
            usuario.setOrcamentoTarefaList(new ArrayList<OrcamentoTarefa>());
        }
        if (usuario.getApontamentoTarefaList() == null) {
            usuario.setApontamentoTarefaList(new ArrayList<ApontamentoTarefa>());
        }
        if (usuario.getDepartamentoList() == null) {
            usuario.setDepartamentoList(new ArrayList<Departamento>());
        }
        if (usuario.getUsuarioList() == null) {
            usuario.setUsuarioList(new ArrayList<Usuario>());
        }
        if (usuario.getEmpresaList() == null) {
            usuario.setEmpresaList(new ArrayList<Empresa>());
        }
        if (usuario.getEmpresaClienteList() == null) {
            usuario.setEmpresaClienteList(new ArrayList<EmpresaCliente>());
        }
        if (usuario.getAnexoTarefaList() == null) {
            usuario.setAnexoTarefaList(new ArrayList<AnexoTarefa>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario idusuarioinclusao = usuario.getIdusuarioinclusao();
            if (idusuarioinclusao != null) {
                idusuarioinclusao = em.getReference(idusuarioinclusao.getClass(), idusuarioinclusao.getId());
                usuario.setIdusuarioinclusao(idusuarioinclusao);
            }
            Collection<UsuarioEmpresa> attachedEmpresas = new ArrayList<UsuarioEmpresa>();
            for (UsuarioEmpresa empresasUsuarioEmpresaToAttach : usuario.getEmpresas()) {
                empresasUsuarioEmpresaToAttach = em.getReference(empresasUsuarioEmpresaToAttach.getClass(), empresasUsuarioEmpresaToAttach.getId());
                attachedEmpresas.add(empresasUsuarioEmpresaToAttach);
            }
            usuario.setEmpresas(attachedEmpresas);
            Collection<Meta> attachedMetasResponsaveis = new ArrayList<Meta>();
            for (Meta metasResponsaveisMetaToAttach : usuario.getMetasResponsaveis()) {
                metasResponsaveisMetaToAttach = em.getReference(metasResponsaveisMetaToAttach.getClass(), metasResponsaveisMetaToAttach.getId());
                attachedMetasResponsaveis.add(metasResponsaveisMetaToAttach);
            }
            usuario.setMetasResponsaveis(attachedMetasResponsaveis);
            List<FavoritosTarefaMeta> attachedFavoritosTarefaMetaList = new ArrayList<FavoritosTarefaMeta>();
            for (FavoritosTarefaMeta favoritosTarefaMetaListFavoritosTarefaMetaToAttach : usuario.getFavoritosTarefaMetaList()) {
                favoritosTarefaMetaListFavoritosTarefaMetaToAttach = em.getReference(favoritosTarefaMetaListFavoritosTarefaMetaToAttach.getClass(), favoritosTarefaMetaListFavoritosTarefaMetaToAttach.getId());
                attachedFavoritosTarefaMetaList.add(favoritosTarefaMetaListFavoritosTarefaMetaToAttach);
            }
            usuario.setFavoritosTarefaMetaList(attachedFavoritosTarefaMetaList);
            List<CentroCusto> attachedCentroCustoList = new ArrayList<CentroCusto>();
            for (CentroCusto centroCustoListCentroCustoToAttach : usuario.getCentroCustoList()) {
                centroCustoListCentroCustoToAttach = em.getReference(centroCustoListCentroCustoToAttach.getClass(), centroCustoListCentroCustoToAttach.getId());
                attachedCentroCustoList.add(centroCustoListCentroCustoToAttach);
            }
            usuario.setCentroCustoList(attachedCentroCustoList);
            List<Endereco> attachedEnderecoList = new ArrayList<Endereco>();
            for (Endereco enderecoListEnderecoToAttach : usuario.getEnderecoList()) {
                enderecoListEnderecoToAttach = em.getReference(enderecoListEnderecoToAttach.getClass(), enderecoListEnderecoToAttach.getId());
                attachedEnderecoList.add(enderecoListEnderecoToAttach);
            }
            usuario.setEnderecoList(attachedEnderecoList);
            List<Tarefa> attachedTarefaList = new ArrayList<Tarefa>();
            for (Tarefa tarefaListTarefaToAttach : usuario.getTarefaList()) {
                tarefaListTarefaToAttach = em.getReference(tarefaListTarefaToAttach.getClass(), tarefaListTarefaToAttach.getId());
                attachedTarefaList.add(tarefaListTarefaToAttach);
            }
            usuario.setTarefaList(attachedTarefaList);
            List<Tarefa> attachedTarefaList1 = new ArrayList<Tarefa>();
            for (Tarefa tarefaList1TarefaToAttach : usuario.getTarefaList1()) {
                tarefaList1TarefaToAttach = em.getReference(tarefaList1TarefaToAttach.getClass(), tarefaList1TarefaToAttach.getId());
                attachedTarefaList1.add(tarefaList1TarefaToAttach);
            }
            usuario.setTarefaList1(attachedTarefaList1);
            List<Tarefa> attachedTarefaList2 = new ArrayList<Tarefa>();
            for (Tarefa tarefaList2TarefaToAttach : usuario.getTarefaList2()) {
                tarefaList2TarefaToAttach = em.getReference(tarefaList2TarefaToAttach.getClass(), tarefaList2TarefaToAttach.getId());
                attachedTarefaList2.add(tarefaList2TarefaToAttach);
            }
            usuario.setTarefaList2(attachedTarefaList2);
            List<ParicipanteTarefa> attachedParicipanteTarefaList = new ArrayList<ParicipanteTarefa>();
            for (ParicipanteTarefa paricipanteTarefaListParicipanteTarefaToAttach : usuario.getParicipanteTarefaList()) {
                paricipanteTarefaListParicipanteTarefaToAttach = em.getReference(paricipanteTarefaListParicipanteTarefaToAttach.getClass(), paricipanteTarefaListParicipanteTarefaToAttach.getId());
                attachedParicipanteTarefaList.add(paricipanteTarefaListParicipanteTarefaToAttach);
            }
            usuario.setParicipanteTarefaList(attachedParicipanteTarefaList);
            List<ParicipanteTarefa> attachedParicipanteTarefaList1 = new ArrayList<ParicipanteTarefa>();
            for (ParicipanteTarefa paricipanteTarefaList1ParicipanteTarefaToAttach : usuario.getParicipanteTarefaList1()) {
                paricipanteTarefaList1ParicipanteTarefaToAttach = em.getReference(paricipanteTarefaList1ParicipanteTarefaToAttach.getClass(), paricipanteTarefaList1ParicipanteTarefaToAttach.getId());
                attachedParicipanteTarefaList1.add(paricipanteTarefaList1ParicipanteTarefaToAttach);
            }
            usuario.setParicipanteTarefaList1(attachedParicipanteTarefaList1);
            List<FilialCliente> attachedFilialClienteList = new ArrayList<FilialCliente>();
            for (FilialCliente filialClienteListFilialClienteToAttach : usuario.getFilialClienteList()) {
                filialClienteListFilialClienteToAttach = em.getReference(filialClienteListFilialClienteToAttach.getClass(), filialClienteListFilialClienteToAttach.getId());
                attachedFilialClienteList.add(filialClienteListFilialClienteToAttach);
            }
            usuario.setFilialClienteList(attachedFilialClienteList);
            List<FilialEmpresa> attachedFilialEmpresaList = new ArrayList<FilialEmpresa>();
            for (FilialEmpresa filialEmpresaListFilialEmpresaToAttach : usuario.getFilialEmpresaList()) {
                filialEmpresaListFilialEmpresaToAttach = em.getReference(filialEmpresaListFilialEmpresaToAttach.getClass(), filialEmpresaListFilialEmpresaToAttach.getId());
                attachedFilialEmpresaList.add(filialEmpresaListFilialEmpresaToAttach);
            }
            usuario.setFilialEmpresaList(attachedFilialEmpresaList);
            List<AvaliacaoMetaTarefa> attachedAvaliacaoMetaTarefaList = new ArrayList<AvaliacaoMetaTarefa>();
            for (AvaliacaoMetaTarefa avaliacaoMetaTarefaListAvaliacaoMetaTarefaToAttach : usuario.getAvaliacaoMetaTarefaList()) {
                avaliacaoMetaTarefaListAvaliacaoMetaTarefaToAttach = em.getReference(avaliacaoMetaTarefaListAvaliacaoMetaTarefaToAttach.getClass(), avaliacaoMetaTarefaListAvaliacaoMetaTarefaToAttach.getId());
                attachedAvaliacaoMetaTarefaList.add(avaliacaoMetaTarefaListAvaliacaoMetaTarefaToAttach);
            }
            usuario.setAvaliacaoMetaTarefaList(attachedAvaliacaoMetaTarefaList);
            List<AvaliacaoMetaTarefa> attachedAvaliacaoMetaTarefaList1 = new ArrayList<AvaliacaoMetaTarefa>();
            for (AvaliacaoMetaTarefa avaliacaoMetaTarefaList1AvaliacaoMetaTarefaToAttach : usuario.getAvaliacaoMetaTarefaList1()) {
                avaliacaoMetaTarefaList1AvaliacaoMetaTarefaToAttach = em.getReference(avaliacaoMetaTarefaList1AvaliacaoMetaTarefaToAttach.getClass(), avaliacaoMetaTarefaList1AvaliacaoMetaTarefaToAttach.getId());
                attachedAvaliacaoMetaTarefaList1.add(avaliacaoMetaTarefaList1AvaliacaoMetaTarefaToAttach);
            }
            usuario.setAvaliacaoMetaTarefaList1(attachedAvaliacaoMetaTarefaList1);
            List<AvaliacaoMetaTarefa> attachedAvaliacaoMetaTarefaList2 = new ArrayList<AvaliacaoMetaTarefa>();
            for (AvaliacaoMetaTarefa avaliacaoMetaTarefaList2AvaliacaoMetaTarefaToAttach : usuario.getAvaliacaoMetaTarefaList2()) {
                avaliacaoMetaTarefaList2AvaliacaoMetaTarefaToAttach = em.getReference(avaliacaoMetaTarefaList2AvaliacaoMetaTarefaToAttach.getClass(), avaliacaoMetaTarefaList2AvaliacaoMetaTarefaToAttach.getId());
                attachedAvaliacaoMetaTarefaList2.add(avaliacaoMetaTarefaList2AvaliacaoMetaTarefaToAttach);
            }
            usuario.setAvaliacaoMetaTarefaList2(attachedAvaliacaoMetaTarefaList2);
            List<OrcamentoTarefa> attachedOrcamentoTarefaList = new ArrayList<OrcamentoTarefa>();
            for (OrcamentoTarefa orcamentoTarefaListOrcamentoTarefaToAttach : usuario.getOrcamentoTarefaList()) {
                orcamentoTarefaListOrcamentoTarefaToAttach = em.getReference(orcamentoTarefaListOrcamentoTarefaToAttach.getClass(), orcamentoTarefaListOrcamentoTarefaToAttach.getId());
                attachedOrcamentoTarefaList.add(orcamentoTarefaListOrcamentoTarefaToAttach);
            }
            usuario.setOrcamentoTarefaList(attachedOrcamentoTarefaList);
            List<ApontamentoTarefa> attachedApontamentoTarefaList = new ArrayList<ApontamentoTarefa>();
            for (ApontamentoTarefa apontamentoTarefaListApontamentoTarefaToAttach : usuario.getApontamentoTarefaList()) {
                apontamentoTarefaListApontamentoTarefaToAttach = em.getReference(apontamentoTarefaListApontamentoTarefaToAttach.getClass(), apontamentoTarefaListApontamentoTarefaToAttach.getId());
                attachedApontamentoTarefaList.add(apontamentoTarefaListApontamentoTarefaToAttach);
            }
            usuario.setApontamentoTarefaList(attachedApontamentoTarefaList);
            List<Departamento> attachedDepartamentoList = new ArrayList<Departamento>();
            for (Departamento departamentoListDepartamentoToAttach : usuario.getDepartamentoList()) {
                departamentoListDepartamentoToAttach = em.getReference(departamentoListDepartamentoToAttach.getClass(), departamentoListDepartamentoToAttach.getId());
                attachedDepartamentoList.add(departamentoListDepartamentoToAttach);
            }
            usuario.setDepartamentoList(attachedDepartamentoList);
            List<Usuario> attachedUsuarioList = new ArrayList<Usuario>();
            for (Usuario usuarioListUsuarioToAttach : usuario.getUsuarioList()) {
                usuarioListUsuarioToAttach = em.getReference(usuarioListUsuarioToAttach.getClass(), usuarioListUsuarioToAttach.getId());
                attachedUsuarioList.add(usuarioListUsuarioToAttach);
            }
            usuario.setUsuarioList(attachedUsuarioList);
            List<Empresa> attachedEmpresaList = new ArrayList<Empresa>();
            for (Empresa empresaListEmpresaToAttach : usuario.getEmpresaList()) {
                empresaListEmpresaToAttach = em.getReference(empresaListEmpresaToAttach.getClass(), empresaListEmpresaToAttach.getId());
                attachedEmpresaList.add(empresaListEmpresaToAttach);
            }
            usuario.setEmpresaList(attachedEmpresaList);
            List<EmpresaCliente> attachedEmpresaClienteList = new ArrayList<EmpresaCliente>();
            for (EmpresaCliente empresaClienteListEmpresaClienteToAttach : usuario.getEmpresaClienteList()) {
                empresaClienteListEmpresaClienteToAttach = em.getReference(empresaClienteListEmpresaClienteToAttach.getClass(), empresaClienteListEmpresaClienteToAttach.getId());
                attachedEmpresaClienteList.add(empresaClienteListEmpresaClienteToAttach);
            }
            usuario.setEmpresaClienteList(attachedEmpresaClienteList);
            List<AnexoTarefa> attachedAnexoTarefaList = new ArrayList<AnexoTarefa>();
            for (AnexoTarefa anexoTarefaListAnexoTarefaToAttach : usuario.getAnexoTarefaList()) {
                anexoTarefaListAnexoTarefaToAttach = em.getReference(anexoTarefaListAnexoTarefaToAttach.getClass(), anexoTarefaListAnexoTarefaToAttach.getId());
                attachedAnexoTarefaList.add(anexoTarefaListAnexoTarefaToAttach);
            }
            usuario.setAnexoTarefaList(attachedAnexoTarefaList);
            em.persist(usuario);
            if (idusuarioinclusao != null) {
                idusuarioinclusao.getUsuarioList().add(usuario);
                idusuarioinclusao = em.merge(idusuarioinclusao);
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
            for (Meta metasResponsaveisMeta : usuario.getMetasResponsaveis()) {
                Usuario oldResponsavelOfMetasResponsaveisMeta = metasResponsaveisMeta.getResponsavel();
                metasResponsaveisMeta.setResponsavel(usuario);
                metasResponsaveisMeta = em.merge(metasResponsaveisMeta);
                if (oldResponsavelOfMetasResponsaveisMeta != null) {
                    oldResponsavelOfMetasResponsaveisMeta.getMetasResponsaveis().remove(metasResponsaveisMeta);
                    oldResponsavelOfMetasResponsaveisMeta = em.merge(oldResponsavelOfMetasResponsaveisMeta);
                }
            }
            for (FavoritosTarefaMeta favoritosTarefaMetaListFavoritosTarefaMeta : usuario.getFavoritosTarefaMetaList()) {
                Usuario oldIdusuarioinclusaoOfFavoritosTarefaMetaListFavoritosTarefaMeta = favoritosTarefaMetaListFavoritosTarefaMeta.getIdusuarioinclusao();
                favoritosTarefaMetaListFavoritosTarefaMeta.setIdusuarioinclusao(usuario);
                favoritosTarefaMetaListFavoritosTarefaMeta = em.merge(favoritosTarefaMetaListFavoritosTarefaMeta);
                if (oldIdusuarioinclusaoOfFavoritosTarefaMetaListFavoritosTarefaMeta != null) {
                    oldIdusuarioinclusaoOfFavoritosTarefaMetaListFavoritosTarefaMeta.getFavoritosTarefaMetaList().remove(favoritosTarefaMetaListFavoritosTarefaMeta);
                    oldIdusuarioinclusaoOfFavoritosTarefaMetaListFavoritosTarefaMeta = em.merge(oldIdusuarioinclusaoOfFavoritosTarefaMetaListFavoritosTarefaMeta);
                }
            }
            for (CentroCusto centroCustoListCentroCusto : usuario.getCentroCustoList()) {
                Usuario oldIdusuarioinclusaoOfCentroCustoListCentroCusto = centroCustoListCentroCusto.getIdusuarioinclusao();
                centroCustoListCentroCusto.setIdusuarioinclusao(usuario);
                centroCustoListCentroCusto = em.merge(centroCustoListCentroCusto);
                if (oldIdusuarioinclusaoOfCentroCustoListCentroCusto != null) {
                    oldIdusuarioinclusaoOfCentroCustoListCentroCusto.getCentroCustoList().remove(centroCustoListCentroCusto);
                    oldIdusuarioinclusaoOfCentroCustoListCentroCusto = em.merge(oldIdusuarioinclusaoOfCentroCustoListCentroCusto);
                }
            }
            for (Endereco enderecoListEndereco : usuario.getEnderecoList()) {
                Usuario oldIdusuarioinclusaoOfEnderecoListEndereco = enderecoListEndereco.getIdusuarioinclusao();
                enderecoListEndereco.setIdusuarioinclusao(usuario);
                enderecoListEndereco = em.merge(enderecoListEndereco);
                if (oldIdusuarioinclusaoOfEnderecoListEndereco != null) {
                    oldIdusuarioinclusaoOfEnderecoListEndereco.getEnderecoList().remove(enderecoListEndereco);
                    oldIdusuarioinclusaoOfEnderecoListEndereco = em.merge(oldIdusuarioinclusaoOfEnderecoListEndereco);
                }
            }
            for (Tarefa tarefaListTarefa : usuario.getTarefaList()) {
                Usuario oldIdusuarioinclusaoOfTarefaListTarefa = tarefaListTarefa.getIdusuarioinclusao();
                tarefaListTarefa.setIdusuarioinclusao(usuario);
                tarefaListTarefa = em.merge(tarefaListTarefa);
                if (oldIdusuarioinclusaoOfTarefaListTarefa != null) {
                    oldIdusuarioinclusaoOfTarefaListTarefa.getTarefaList().remove(tarefaListTarefa);
                    oldIdusuarioinclusaoOfTarefaListTarefa = em.merge(oldIdusuarioinclusaoOfTarefaListTarefa);
                }
            }
            for (Tarefa tarefaList1Tarefa : usuario.getTarefaList1()) {
                Usuario oldIdusuariosolicitanteOfTarefaList1Tarefa = tarefaList1Tarefa.getIdusuariosolicitante();
                tarefaList1Tarefa.setIdusuariosolicitante(usuario);
                tarefaList1Tarefa = em.merge(tarefaList1Tarefa);
                if (oldIdusuariosolicitanteOfTarefaList1Tarefa != null) {
                    oldIdusuariosolicitanteOfTarefaList1Tarefa.getTarefaList1().remove(tarefaList1Tarefa);
                    oldIdusuariosolicitanteOfTarefaList1Tarefa = em.merge(oldIdusuariosolicitanteOfTarefaList1Tarefa);
                }
            }
            for (Tarefa tarefaList2Tarefa : usuario.getTarefaList2()) {
                Usuario oldIdusuarioresponsavelOfTarefaList2Tarefa = tarefaList2Tarefa.getIdusuarioresponsavel();
                tarefaList2Tarefa.setIdusuarioresponsavel(usuario);
                tarefaList2Tarefa = em.merge(tarefaList2Tarefa);
                if (oldIdusuarioresponsavelOfTarefaList2Tarefa != null) {
                    oldIdusuarioresponsavelOfTarefaList2Tarefa.getTarefaList2().remove(tarefaList2Tarefa);
                    oldIdusuarioresponsavelOfTarefaList2Tarefa = em.merge(oldIdusuarioresponsavelOfTarefaList2Tarefa);
                }
            }
            for (ParicipanteTarefa paricipanteTarefaListParicipanteTarefa : usuario.getParicipanteTarefaList()) {
                Usuario oldIdusuarioinclusaoOfParicipanteTarefaListParicipanteTarefa = paricipanteTarefaListParicipanteTarefa.getIdusuarioinclusao();
                paricipanteTarefaListParicipanteTarefa.setIdusuarioinclusao(usuario);
                paricipanteTarefaListParicipanteTarefa = em.merge(paricipanteTarefaListParicipanteTarefa);
                if (oldIdusuarioinclusaoOfParicipanteTarefaListParicipanteTarefa != null) {
                    oldIdusuarioinclusaoOfParicipanteTarefaListParicipanteTarefa.getParicipanteTarefaList().remove(paricipanteTarefaListParicipanteTarefa);
                    oldIdusuarioinclusaoOfParicipanteTarefaListParicipanteTarefa = em.merge(oldIdusuarioinclusaoOfParicipanteTarefaListParicipanteTarefa);
                }
            }
            for (ParicipanteTarefa paricipanteTarefaList1ParicipanteTarefa : usuario.getParicipanteTarefaList1()) {
                Usuario oldIdusuarioparticipanteOfParicipanteTarefaList1ParicipanteTarefa = paricipanteTarefaList1ParicipanteTarefa.getIdusuarioparticipante();
                paricipanteTarefaList1ParicipanteTarefa.setIdusuarioparticipante(usuario);
                paricipanteTarefaList1ParicipanteTarefa = em.merge(paricipanteTarefaList1ParicipanteTarefa);
                if (oldIdusuarioparticipanteOfParicipanteTarefaList1ParicipanteTarefa != null) {
                    oldIdusuarioparticipanteOfParicipanteTarefaList1ParicipanteTarefa.getParicipanteTarefaList1().remove(paricipanteTarefaList1ParicipanteTarefa);
                    oldIdusuarioparticipanteOfParicipanteTarefaList1ParicipanteTarefa = em.merge(oldIdusuarioparticipanteOfParicipanteTarefaList1ParicipanteTarefa);
                }
            }
            for (FilialCliente filialClienteListFilialCliente : usuario.getFilialClienteList()) {
                Usuario oldIdusuarioinclusaoOfFilialClienteListFilialCliente = filialClienteListFilialCliente.getIdusuarioinclusao();
                filialClienteListFilialCliente.setIdusuarioinclusao(usuario);
                filialClienteListFilialCliente = em.merge(filialClienteListFilialCliente);
                if (oldIdusuarioinclusaoOfFilialClienteListFilialCliente != null) {
                    oldIdusuarioinclusaoOfFilialClienteListFilialCliente.getFilialClienteList().remove(filialClienteListFilialCliente);
                    oldIdusuarioinclusaoOfFilialClienteListFilialCliente = em.merge(oldIdusuarioinclusaoOfFilialClienteListFilialCliente);
                }
            }
            for (FilialEmpresa filialEmpresaListFilialEmpresa : usuario.getFilialEmpresaList()) {
                Usuario oldIdusuarioinclusaoOfFilialEmpresaListFilialEmpresa = filialEmpresaListFilialEmpresa.getIdusuarioinclusao();
                filialEmpresaListFilialEmpresa.setIdusuarioinclusao(usuario);
                filialEmpresaListFilialEmpresa = em.merge(filialEmpresaListFilialEmpresa);
                if (oldIdusuarioinclusaoOfFilialEmpresaListFilialEmpresa != null) {
                    oldIdusuarioinclusaoOfFilialEmpresaListFilialEmpresa.getFilialEmpresaList().remove(filialEmpresaListFilialEmpresa);
                    oldIdusuarioinclusaoOfFilialEmpresaListFilialEmpresa = em.merge(oldIdusuarioinclusaoOfFilialEmpresaListFilialEmpresa);
                }
            }
            for (AvaliacaoMetaTarefa avaliacaoMetaTarefaListAvaliacaoMetaTarefa : usuario.getAvaliacaoMetaTarefaList()) {
                Usuario oldIdusuarioinclusaoOfAvaliacaoMetaTarefaListAvaliacaoMetaTarefa = avaliacaoMetaTarefaListAvaliacaoMetaTarefa.getIdusuarioinclusao();
                avaliacaoMetaTarefaListAvaliacaoMetaTarefa.setIdusuarioinclusao(usuario);
                avaliacaoMetaTarefaListAvaliacaoMetaTarefa = em.merge(avaliacaoMetaTarefaListAvaliacaoMetaTarefa);
                if (oldIdusuarioinclusaoOfAvaliacaoMetaTarefaListAvaliacaoMetaTarefa != null) {
                    oldIdusuarioinclusaoOfAvaliacaoMetaTarefaListAvaliacaoMetaTarefa.getAvaliacaoMetaTarefaList().remove(avaliacaoMetaTarefaListAvaliacaoMetaTarefa);
                    oldIdusuarioinclusaoOfAvaliacaoMetaTarefaListAvaliacaoMetaTarefa = em.merge(oldIdusuarioinclusaoOfAvaliacaoMetaTarefaListAvaliacaoMetaTarefa);
                }
            }
            for (AvaliacaoMetaTarefa avaliacaoMetaTarefaList1AvaliacaoMetaTarefa : usuario.getAvaliacaoMetaTarefaList1()) {
                Usuario oldIdusuarioavaliadorOfAvaliacaoMetaTarefaList1AvaliacaoMetaTarefa = avaliacaoMetaTarefaList1AvaliacaoMetaTarefa.getIdusuarioavaliador();
                avaliacaoMetaTarefaList1AvaliacaoMetaTarefa.setIdusuarioavaliador(usuario);
                avaliacaoMetaTarefaList1AvaliacaoMetaTarefa = em.merge(avaliacaoMetaTarefaList1AvaliacaoMetaTarefa);
                if (oldIdusuarioavaliadorOfAvaliacaoMetaTarefaList1AvaliacaoMetaTarefa != null) {
                    oldIdusuarioavaliadorOfAvaliacaoMetaTarefaList1AvaliacaoMetaTarefa.getAvaliacaoMetaTarefaList1().remove(avaliacaoMetaTarefaList1AvaliacaoMetaTarefa);
                    oldIdusuarioavaliadorOfAvaliacaoMetaTarefaList1AvaliacaoMetaTarefa = em.merge(oldIdusuarioavaliadorOfAvaliacaoMetaTarefaList1AvaliacaoMetaTarefa);
                }
            }
            for (AvaliacaoMetaTarefa avaliacaoMetaTarefaList2AvaliacaoMetaTarefa : usuario.getAvaliacaoMetaTarefaList2()) {
                Usuario oldIdusuarioavaliadoOfAvaliacaoMetaTarefaList2AvaliacaoMetaTarefa = avaliacaoMetaTarefaList2AvaliacaoMetaTarefa.getIdusuarioavaliado();
                avaliacaoMetaTarefaList2AvaliacaoMetaTarefa.setIdusuarioavaliado(usuario);
                avaliacaoMetaTarefaList2AvaliacaoMetaTarefa = em.merge(avaliacaoMetaTarefaList2AvaliacaoMetaTarefa);
                if (oldIdusuarioavaliadoOfAvaliacaoMetaTarefaList2AvaliacaoMetaTarefa != null) {
                    oldIdusuarioavaliadoOfAvaliacaoMetaTarefaList2AvaliacaoMetaTarefa.getAvaliacaoMetaTarefaList2().remove(avaliacaoMetaTarefaList2AvaliacaoMetaTarefa);
                    oldIdusuarioavaliadoOfAvaliacaoMetaTarefaList2AvaliacaoMetaTarefa = em.merge(oldIdusuarioavaliadoOfAvaliacaoMetaTarefaList2AvaliacaoMetaTarefa);
                }
            }
            for (OrcamentoTarefa orcamentoTarefaListOrcamentoTarefa : usuario.getOrcamentoTarefaList()) {
                Usuario oldIdusuarioinclusaoOfOrcamentoTarefaListOrcamentoTarefa = orcamentoTarefaListOrcamentoTarefa.getIdusuarioinclusao();
                orcamentoTarefaListOrcamentoTarefa.setIdusuarioinclusao(usuario);
                orcamentoTarefaListOrcamentoTarefa = em.merge(orcamentoTarefaListOrcamentoTarefa);
                if (oldIdusuarioinclusaoOfOrcamentoTarefaListOrcamentoTarefa != null) {
                    oldIdusuarioinclusaoOfOrcamentoTarefaListOrcamentoTarefa.getOrcamentoTarefaList().remove(orcamentoTarefaListOrcamentoTarefa);
                    oldIdusuarioinclusaoOfOrcamentoTarefaListOrcamentoTarefa = em.merge(oldIdusuarioinclusaoOfOrcamentoTarefaListOrcamentoTarefa);
                }
            }
            for (ApontamentoTarefa apontamentoTarefaListApontamentoTarefa : usuario.getApontamentoTarefaList()) {
                Usuario oldIdusuarioinclusaoOfApontamentoTarefaListApontamentoTarefa = apontamentoTarefaListApontamentoTarefa.getIdUsuarioInclusao();
                apontamentoTarefaListApontamentoTarefa.setIdUsuarioInclusao(usuario);
                apontamentoTarefaListApontamentoTarefa = em.merge(apontamentoTarefaListApontamentoTarefa);
                if (oldIdusuarioinclusaoOfApontamentoTarefaListApontamentoTarefa != null) {
                    oldIdusuarioinclusaoOfApontamentoTarefaListApontamentoTarefa.getApontamentoTarefaList().remove(apontamentoTarefaListApontamentoTarefa);
                    oldIdusuarioinclusaoOfApontamentoTarefaListApontamentoTarefa = em.merge(oldIdusuarioinclusaoOfApontamentoTarefaListApontamentoTarefa);
                }
            }
            for (Departamento departamentoListDepartamento : usuario.getDepartamentoList()) {
                Usuario oldIdusuarioinclusaoOfDepartamentoListDepartamento = departamentoListDepartamento.getIdusuarioinclusao();
                departamentoListDepartamento.setIdusuarioinclusao(usuario);
                departamentoListDepartamento = em.merge(departamentoListDepartamento);
                if (oldIdusuarioinclusaoOfDepartamentoListDepartamento != null) {
                    oldIdusuarioinclusaoOfDepartamentoListDepartamento.getDepartamentoList().remove(departamentoListDepartamento);
                    oldIdusuarioinclusaoOfDepartamentoListDepartamento = em.merge(oldIdusuarioinclusaoOfDepartamentoListDepartamento);
                }
            }
            for (Usuario usuarioListUsuario : usuario.getUsuarioList()) {
                Usuario oldIdusuarioinclusaoOfUsuarioListUsuario = usuarioListUsuario.getIdusuarioinclusao();
                usuarioListUsuario.setIdusuarioinclusao(usuario);
                usuarioListUsuario = em.merge(usuarioListUsuario);
                if (oldIdusuarioinclusaoOfUsuarioListUsuario != null) {
                    oldIdusuarioinclusaoOfUsuarioListUsuario.getUsuarioList().remove(usuarioListUsuario);
                    oldIdusuarioinclusaoOfUsuarioListUsuario = em.merge(oldIdusuarioinclusaoOfUsuarioListUsuario);
                }
            }
            for (Empresa empresaListEmpresa : usuario.getEmpresaList()) {
                Usuario oldIdusuarioinclusaoOfEmpresaListEmpresa = empresaListEmpresa.getIdusuarioinclusao();
                empresaListEmpresa.setIdusuarioinclusao(usuario);
                empresaListEmpresa = em.merge(empresaListEmpresa);
                if (oldIdusuarioinclusaoOfEmpresaListEmpresa != null) {
                    oldIdusuarioinclusaoOfEmpresaListEmpresa.getEmpresaList().remove(empresaListEmpresa);
                    oldIdusuarioinclusaoOfEmpresaListEmpresa = em.merge(oldIdusuarioinclusaoOfEmpresaListEmpresa);
                }
            }
            for (EmpresaCliente empresaClienteListEmpresaCliente : usuario.getEmpresaClienteList()) {
                Usuario oldIdusuarioinclusaoOfEmpresaClienteListEmpresaCliente = empresaClienteListEmpresaCliente.getIdusuarioinclusao();
                empresaClienteListEmpresaCliente.setIdusuarioinclusao(usuario);
                empresaClienteListEmpresaCliente = em.merge(empresaClienteListEmpresaCliente);
                if (oldIdusuarioinclusaoOfEmpresaClienteListEmpresaCliente != null) {
                    oldIdusuarioinclusaoOfEmpresaClienteListEmpresaCliente.getEmpresaClienteList().remove(empresaClienteListEmpresaCliente);
                    oldIdusuarioinclusaoOfEmpresaClienteListEmpresaCliente = em.merge(oldIdusuarioinclusaoOfEmpresaClienteListEmpresaCliente);
                }
            }
            for (AnexoTarefa anexoTarefaListAnexoTarefa : usuario.getAnexoTarefaList()) {
                Usuario oldIdusuarioinclusaoOfAnexoTarefaListAnexoTarefa = anexoTarefaListAnexoTarefa.getIdUsuarioInclusao();
                anexoTarefaListAnexoTarefa.setIdUsuarioInclusao(usuario);
                anexoTarefaListAnexoTarefa = em.merge(anexoTarefaListAnexoTarefa);
                if (oldIdusuarioinclusaoOfAnexoTarefaListAnexoTarefa != null) {
                    oldIdusuarioinclusaoOfAnexoTarefaListAnexoTarefa.getAnexoTarefaList().remove(anexoTarefaListAnexoTarefa);
                    oldIdusuarioinclusaoOfAnexoTarefaListAnexoTarefa = em.merge(oldIdusuarioinclusaoOfAnexoTarefaListAnexoTarefa);
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
            Usuario idusuarioinclusaoOld = persistentUsuario.getIdusuarioinclusao();
            Usuario idusuarioinclusaoNew = usuario.getIdusuarioinclusao();
            Collection<UsuarioEmpresa> empresasOld = persistentUsuario.getEmpresas();
            Collection<UsuarioEmpresa> empresasNew = usuario.getEmpresas();
            Collection<Meta> metasResponsaveisOld = persistentUsuario.getMetasResponsaveis();
            Collection<Meta> metasResponsaveisNew = usuario.getMetasResponsaveis();
            List<FavoritosTarefaMeta> favoritosTarefaMetaListOld = persistentUsuario.getFavoritosTarefaMetaList();
            List<FavoritosTarefaMeta> favoritosTarefaMetaListNew = usuario.getFavoritosTarefaMetaList();
            List<CentroCusto> centroCustoListOld = persistentUsuario.getCentroCustoList();
            List<CentroCusto> centroCustoListNew = usuario.getCentroCustoList();
            List<Endereco> enderecoListOld = persistentUsuario.getEnderecoList();
            List<Endereco> enderecoListNew = usuario.getEnderecoList();
            List<Tarefa> tarefaListOld = persistentUsuario.getTarefaList();
            List<Tarefa> tarefaListNew = usuario.getTarefaList();
            List<Tarefa> tarefaList1Old = persistentUsuario.getTarefaList1();
            List<Tarefa> tarefaList1New = usuario.getTarefaList1();
            List<Tarefa> tarefaList2Old = persistentUsuario.getTarefaList2();
            List<Tarefa> tarefaList2New = usuario.getTarefaList2();
            List<ParicipanteTarefa> paricipanteTarefaListOld = persistentUsuario.getParicipanteTarefaList();
            List<ParicipanteTarefa> paricipanteTarefaListNew = usuario.getParicipanteTarefaList();
            List<ParicipanteTarefa> paricipanteTarefaList1Old = persistentUsuario.getParicipanteTarefaList1();
            List<ParicipanteTarefa> paricipanteTarefaList1New = usuario.getParicipanteTarefaList1();
            List<FilialCliente> filialClienteListOld = persistentUsuario.getFilialClienteList();
            List<FilialCliente> filialClienteListNew = usuario.getFilialClienteList();
            List<FilialEmpresa> filialEmpresaListOld = persistentUsuario.getFilialEmpresaList();
            List<FilialEmpresa> filialEmpresaListNew = usuario.getFilialEmpresaList();
            List<AvaliacaoMetaTarefa> avaliacaoMetaTarefaListOld = persistentUsuario.getAvaliacaoMetaTarefaList();
            List<AvaliacaoMetaTarefa> avaliacaoMetaTarefaListNew = usuario.getAvaliacaoMetaTarefaList();
            List<AvaliacaoMetaTarefa> avaliacaoMetaTarefaList1Old = persistentUsuario.getAvaliacaoMetaTarefaList1();
            List<AvaliacaoMetaTarefa> avaliacaoMetaTarefaList1New = usuario.getAvaliacaoMetaTarefaList1();
            List<AvaliacaoMetaTarefa> avaliacaoMetaTarefaList2Old = persistentUsuario.getAvaliacaoMetaTarefaList2();
            List<AvaliacaoMetaTarefa> avaliacaoMetaTarefaList2New = usuario.getAvaliacaoMetaTarefaList2();
            List<OrcamentoTarefa> orcamentoTarefaListOld = persistentUsuario.getOrcamentoTarefaList();
            List<OrcamentoTarefa> orcamentoTarefaListNew = usuario.getOrcamentoTarefaList();
            List<ApontamentoTarefa> apontamentoTarefaListOld = persistentUsuario.getApontamentoTarefaList();
            List<ApontamentoTarefa> apontamentoTarefaListNew = usuario.getApontamentoTarefaList();
            List<Departamento> departamentoListOld = persistentUsuario.getDepartamentoList();
            List<Departamento> departamentoListNew = usuario.getDepartamentoList();
            List<Usuario> usuarioListOld = persistentUsuario.getUsuarioList();
            List<Usuario> usuarioListNew = usuario.getUsuarioList();
            List<Empresa> empresaListOld = persistentUsuario.getEmpresaList();
            List<Empresa> empresaListNew = usuario.getEmpresaList();
            List<EmpresaCliente> empresaClienteListOld = persistentUsuario.getEmpresaClienteList();
            List<EmpresaCliente> empresaClienteListNew = usuario.getEmpresaClienteList();
            List<AnexoTarefa> anexoTarefaListOld = persistentUsuario.getAnexoTarefaList();
            List<AnexoTarefa> anexoTarefaListNew = usuario.getAnexoTarefaList();
            List<String> illegalOrphanMessages = null;
            for (UsuarioEmpresa empresasOldUsuarioEmpresa : empresasOld) {
                if (!empresasNew.contains(empresasOldUsuarioEmpresa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UsuarioEmpresa " + empresasOldUsuarioEmpresa + " since its usuario field is not nullable.");
                }
            }
            for (Meta metasResponsaveisOldMeta : metasResponsaveisOld) {
                if (!metasResponsaveisNew.contains(metasResponsaveisOldMeta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Meta " + metasResponsaveisOldMeta + " since its responsavel field is not nullable.");
                }
            }
            for (FavoritosTarefaMeta favoritosTarefaMetaListOldFavoritosTarefaMeta : favoritosTarefaMetaListOld) {
                if (!favoritosTarefaMetaListNew.contains(favoritosTarefaMetaListOldFavoritosTarefaMeta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain FavoritosTarefaMeta " + favoritosTarefaMetaListOldFavoritosTarefaMeta + " since its idusuarioinclusao field is not nullable.");
                }
            }
            for (CentroCusto centroCustoListOldCentroCusto : centroCustoListOld) {
                if (!centroCustoListNew.contains(centroCustoListOldCentroCusto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CentroCusto " + centroCustoListOldCentroCusto + " since its idusuarioinclusao field is not nullable.");
                }
            }
            for (Endereco enderecoListOldEndereco : enderecoListOld) {
                if (!enderecoListNew.contains(enderecoListOldEndereco)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Endereco " + enderecoListOldEndereco + " since its idusuarioinclusao field is not nullable.");
                }
            }
            for (Tarefa tarefaList1OldTarefa : tarefaList1Old) {
                if (!tarefaList1New.contains(tarefaList1OldTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tarefa " + tarefaList1OldTarefa + " since its idusuariosolicitante field is not nullable.");
                }
            }
            for (Tarefa tarefaList2OldTarefa : tarefaList2Old) {
                if (!tarefaList2New.contains(tarefaList2OldTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tarefa " + tarefaList2OldTarefa + " since its idusuarioresponsavel field is not nullable.");
                }
            }
            for (ParicipanteTarefa paricipanteTarefaListOldParicipanteTarefa : paricipanteTarefaListOld) {
                if (!paricipanteTarefaListNew.contains(paricipanteTarefaListOldParicipanteTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ParicipanteTarefa " + paricipanteTarefaListOldParicipanteTarefa + " since its idusuarioinclusao field is not nullable.");
                }
            }
            for (ParicipanteTarefa paricipanteTarefaList1OldParicipanteTarefa : paricipanteTarefaList1Old) {
                if (!paricipanteTarefaList1New.contains(paricipanteTarefaList1OldParicipanteTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ParicipanteTarefa " + paricipanteTarefaList1OldParicipanteTarefa + " since its idusuarioparticipante field is not nullable.");
                }
            }
            for (FilialCliente filialClienteListOldFilialCliente : filialClienteListOld) {
                if (!filialClienteListNew.contains(filialClienteListOldFilialCliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain FilialCliente " + filialClienteListOldFilialCliente + " since its idusuarioinclusao field is not nullable.");
                }
            }
            for (FilialEmpresa filialEmpresaListOldFilialEmpresa : filialEmpresaListOld) {
                if (!filialEmpresaListNew.contains(filialEmpresaListOldFilialEmpresa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain FilialEmpresa " + filialEmpresaListOldFilialEmpresa + " since its idusuarioinclusao field is not nullable.");
                }
            }
            for (AvaliacaoMetaTarefa avaliacaoMetaTarefaListOldAvaliacaoMetaTarefa : avaliacaoMetaTarefaListOld) {
                if (!avaliacaoMetaTarefaListNew.contains(avaliacaoMetaTarefaListOldAvaliacaoMetaTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AvaliacaoMetaTarefa " + avaliacaoMetaTarefaListOldAvaliacaoMetaTarefa + " since its idusuarioinclusao field is not nullable.");
                }
            }
            for (AvaliacaoMetaTarefa avaliacaoMetaTarefaList1OldAvaliacaoMetaTarefa : avaliacaoMetaTarefaList1Old) {
                if (!avaliacaoMetaTarefaList1New.contains(avaliacaoMetaTarefaList1OldAvaliacaoMetaTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AvaliacaoMetaTarefa " + avaliacaoMetaTarefaList1OldAvaliacaoMetaTarefa + " since its idusuarioavaliador field is not nullable.");
                }
            }
            for (AvaliacaoMetaTarefa avaliacaoMetaTarefaList2OldAvaliacaoMetaTarefa : avaliacaoMetaTarefaList2Old) {
                if (!avaliacaoMetaTarefaList2New.contains(avaliacaoMetaTarefaList2OldAvaliacaoMetaTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AvaliacaoMetaTarefa " + avaliacaoMetaTarefaList2OldAvaliacaoMetaTarefa + " since its idusuarioavaliado field is not nullable.");
                }
            }
            for (OrcamentoTarefa orcamentoTarefaListOldOrcamentoTarefa : orcamentoTarefaListOld) {
                if (!orcamentoTarefaListNew.contains(orcamentoTarefaListOldOrcamentoTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain OrcamentoTarefa " + orcamentoTarefaListOldOrcamentoTarefa + " since its idusuarioinclusao field is not nullable.");
                }
            }
            for (ApontamentoTarefa apontamentoTarefaListOldApontamentoTarefa : apontamentoTarefaListOld) {
                if (!apontamentoTarefaListNew.contains(apontamentoTarefaListOldApontamentoTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ApontamentoTarefa " + apontamentoTarefaListOldApontamentoTarefa + " since its idusuarioinclusao field is not nullable.");
                }
            }
            for (Departamento departamentoListOldDepartamento : departamentoListOld) {
                if (!departamentoListNew.contains(departamentoListOldDepartamento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Departamento " + departamentoListOldDepartamento + " since its idusuarioinclusao field is not nullable.");
                }
            }
            for (Empresa empresaListOldEmpresa : empresaListOld) {
                if (!empresaListNew.contains(empresaListOldEmpresa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Empresa " + empresaListOldEmpresa + " since its idusuarioinclusao field is not nullable.");
                }
            }
            for (EmpresaCliente empresaClienteListOldEmpresaCliente : empresaClienteListOld) {
                if (!empresaClienteListNew.contains(empresaClienteListOldEmpresaCliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain EmpresaCliente " + empresaClienteListOldEmpresaCliente + " since its idusuarioinclusao field is not nullable.");
                }
            }
            for (AnexoTarefa anexoTarefaListOldAnexoTarefa : anexoTarefaListOld) {
                if (!anexoTarefaListNew.contains(anexoTarefaListOldAnexoTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AnexoTarefa " + anexoTarefaListOldAnexoTarefa + " since its idusuarioinclusao field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idusuarioinclusaoNew != null) {
                idusuarioinclusaoNew = em.getReference(idusuarioinclusaoNew.getClass(), idusuarioinclusaoNew.getId());
                usuario.setIdusuarioinclusao(idusuarioinclusaoNew);
            }
            Collection<UsuarioEmpresa> attachedEmpresasNew = new ArrayList<UsuarioEmpresa>();
            for (UsuarioEmpresa empresasNewUsuarioEmpresaToAttach : empresasNew) {
                empresasNewUsuarioEmpresaToAttach = em.getReference(empresasNewUsuarioEmpresaToAttach.getClass(), empresasNewUsuarioEmpresaToAttach.getId());
                attachedEmpresasNew.add(empresasNewUsuarioEmpresaToAttach);
            }
            empresasNew = attachedEmpresasNew;
            usuario.setEmpresas(empresasNew);
            Collection<Meta> attachedMetasResponsaveisNew = new ArrayList<Meta>();
            for (Meta metasResponsaveisNewMetaToAttach : metasResponsaveisNew) {
                metasResponsaveisNewMetaToAttach = em.getReference(metasResponsaveisNewMetaToAttach.getClass(), metasResponsaveisNewMetaToAttach.getId());
                attachedMetasResponsaveisNew.add(metasResponsaveisNewMetaToAttach);
            }
            metasResponsaveisNew = attachedMetasResponsaveisNew;
            usuario.setMetasResponsaveis(metasResponsaveisNew);
            List<FavoritosTarefaMeta> attachedFavoritosTarefaMetaListNew = new ArrayList<FavoritosTarefaMeta>();
            for (FavoritosTarefaMeta favoritosTarefaMetaListNewFavoritosTarefaMetaToAttach : favoritosTarefaMetaListNew) {
                favoritosTarefaMetaListNewFavoritosTarefaMetaToAttach = em.getReference(favoritosTarefaMetaListNewFavoritosTarefaMetaToAttach.getClass(), favoritosTarefaMetaListNewFavoritosTarefaMetaToAttach.getId());
                attachedFavoritosTarefaMetaListNew.add(favoritosTarefaMetaListNewFavoritosTarefaMetaToAttach);
            }
            favoritosTarefaMetaListNew = attachedFavoritosTarefaMetaListNew;
            usuario.setFavoritosTarefaMetaList(favoritosTarefaMetaListNew);
            List<CentroCusto> attachedCentroCustoListNew = new ArrayList<CentroCusto>();
            for (CentroCusto centroCustoListNewCentroCustoToAttach : centroCustoListNew) {
                centroCustoListNewCentroCustoToAttach = em.getReference(centroCustoListNewCentroCustoToAttach.getClass(), centroCustoListNewCentroCustoToAttach.getId());
                attachedCentroCustoListNew.add(centroCustoListNewCentroCustoToAttach);
            }
            centroCustoListNew = attachedCentroCustoListNew;
            usuario.setCentroCustoList(centroCustoListNew);
            List<Endereco> attachedEnderecoListNew = new ArrayList<Endereco>();
            for (Endereco enderecoListNewEnderecoToAttach : enderecoListNew) {
                enderecoListNewEnderecoToAttach = em.getReference(enderecoListNewEnderecoToAttach.getClass(), enderecoListNewEnderecoToAttach.getId());
                attachedEnderecoListNew.add(enderecoListNewEnderecoToAttach);
            }
            enderecoListNew = attachedEnderecoListNew;
            usuario.setEnderecoList(enderecoListNew);
            List<Tarefa> attachedTarefaListNew = new ArrayList<Tarefa>();
            for (Tarefa tarefaListNewTarefaToAttach : tarefaListNew) {
                tarefaListNewTarefaToAttach = em.getReference(tarefaListNewTarefaToAttach.getClass(), tarefaListNewTarefaToAttach.getId());
                attachedTarefaListNew.add(tarefaListNewTarefaToAttach);
            }
            tarefaListNew = attachedTarefaListNew;
            usuario.setTarefaList(tarefaListNew);
            List<Tarefa> attachedTarefaList1New = new ArrayList<Tarefa>();
            for (Tarefa tarefaList1NewTarefaToAttach : tarefaList1New) {
                tarefaList1NewTarefaToAttach = em.getReference(tarefaList1NewTarefaToAttach.getClass(), tarefaList1NewTarefaToAttach.getId());
                attachedTarefaList1New.add(tarefaList1NewTarefaToAttach);
            }
            tarefaList1New = attachedTarefaList1New;
            usuario.setTarefaList1(tarefaList1New);
            List<Tarefa> attachedTarefaList2New = new ArrayList<Tarefa>();
            for (Tarefa tarefaList2NewTarefaToAttach : tarefaList2New) {
                tarefaList2NewTarefaToAttach = em.getReference(tarefaList2NewTarefaToAttach.getClass(), tarefaList2NewTarefaToAttach.getId());
                attachedTarefaList2New.add(tarefaList2NewTarefaToAttach);
            }
            tarefaList2New = attachedTarefaList2New;
            usuario.setTarefaList2(tarefaList2New);
            List<ParicipanteTarefa> attachedParicipanteTarefaListNew = new ArrayList<ParicipanteTarefa>();
            for (ParicipanteTarefa paricipanteTarefaListNewParicipanteTarefaToAttach : paricipanteTarefaListNew) {
                paricipanteTarefaListNewParicipanteTarefaToAttach = em.getReference(paricipanteTarefaListNewParicipanteTarefaToAttach.getClass(), paricipanteTarefaListNewParicipanteTarefaToAttach.getId());
                attachedParicipanteTarefaListNew.add(paricipanteTarefaListNewParicipanteTarefaToAttach);
            }
            paricipanteTarefaListNew = attachedParicipanteTarefaListNew;
            usuario.setParicipanteTarefaList(paricipanteTarefaListNew);
            List<ParicipanteTarefa> attachedParicipanteTarefaList1New = new ArrayList<ParicipanteTarefa>();
            for (ParicipanteTarefa paricipanteTarefaList1NewParicipanteTarefaToAttach : paricipanteTarefaList1New) {
                paricipanteTarefaList1NewParicipanteTarefaToAttach = em.getReference(paricipanteTarefaList1NewParicipanteTarefaToAttach.getClass(), paricipanteTarefaList1NewParicipanteTarefaToAttach.getId());
                attachedParicipanteTarefaList1New.add(paricipanteTarefaList1NewParicipanteTarefaToAttach);
            }
            paricipanteTarefaList1New = attachedParicipanteTarefaList1New;
            usuario.setParicipanteTarefaList1(paricipanteTarefaList1New);
            List<FilialCliente> attachedFilialClienteListNew = new ArrayList<FilialCliente>();
            for (FilialCliente filialClienteListNewFilialClienteToAttach : filialClienteListNew) {
                filialClienteListNewFilialClienteToAttach = em.getReference(filialClienteListNewFilialClienteToAttach.getClass(), filialClienteListNewFilialClienteToAttach.getId());
                attachedFilialClienteListNew.add(filialClienteListNewFilialClienteToAttach);
            }
            filialClienteListNew = attachedFilialClienteListNew;
            usuario.setFilialClienteList(filialClienteListNew);
            List<FilialEmpresa> attachedFilialEmpresaListNew = new ArrayList<FilialEmpresa>();
            for (FilialEmpresa filialEmpresaListNewFilialEmpresaToAttach : filialEmpresaListNew) {
                filialEmpresaListNewFilialEmpresaToAttach = em.getReference(filialEmpresaListNewFilialEmpresaToAttach.getClass(), filialEmpresaListNewFilialEmpresaToAttach.getId());
                attachedFilialEmpresaListNew.add(filialEmpresaListNewFilialEmpresaToAttach);
            }
            filialEmpresaListNew = attachedFilialEmpresaListNew;
            usuario.setFilialEmpresaList(filialEmpresaListNew);
            List<AvaliacaoMetaTarefa> attachedAvaliacaoMetaTarefaListNew = new ArrayList<AvaliacaoMetaTarefa>();
            for (AvaliacaoMetaTarefa avaliacaoMetaTarefaListNewAvaliacaoMetaTarefaToAttach : avaliacaoMetaTarefaListNew) {
                avaliacaoMetaTarefaListNewAvaliacaoMetaTarefaToAttach = em.getReference(avaliacaoMetaTarefaListNewAvaliacaoMetaTarefaToAttach.getClass(), avaliacaoMetaTarefaListNewAvaliacaoMetaTarefaToAttach.getId());
                attachedAvaliacaoMetaTarefaListNew.add(avaliacaoMetaTarefaListNewAvaliacaoMetaTarefaToAttach);
            }
            avaliacaoMetaTarefaListNew = attachedAvaliacaoMetaTarefaListNew;
            usuario.setAvaliacaoMetaTarefaList(avaliacaoMetaTarefaListNew);
            List<AvaliacaoMetaTarefa> attachedAvaliacaoMetaTarefaList1New = new ArrayList<AvaliacaoMetaTarefa>();
            for (AvaliacaoMetaTarefa avaliacaoMetaTarefaList1NewAvaliacaoMetaTarefaToAttach : avaliacaoMetaTarefaList1New) {
                avaliacaoMetaTarefaList1NewAvaliacaoMetaTarefaToAttach = em.getReference(avaliacaoMetaTarefaList1NewAvaliacaoMetaTarefaToAttach.getClass(), avaliacaoMetaTarefaList1NewAvaliacaoMetaTarefaToAttach.getId());
                attachedAvaliacaoMetaTarefaList1New.add(avaliacaoMetaTarefaList1NewAvaliacaoMetaTarefaToAttach);
            }
            avaliacaoMetaTarefaList1New = attachedAvaliacaoMetaTarefaList1New;
            usuario.setAvaliacaoMetaTarefaList1(avaliacaoMetaTarefaList1New);
            List<AvaliacaoMetaTarefa> attachedAvaliacaoMetaTarefaList2New = new ArrayList<AvaliacaoMetaTarefa>();
            for (AvaliacaoMetaTarefa avaliacaoMetaTarefaList2NewAvaliacaoMetaTarefaToAttach : avaliacaoMetaTarefaList2New) {
                avaliacaoMetaTarefaList2NewAvaliacaoMetaTarefaToAttach = em.getReference(avaliacaoMetaTarefaList2NewAvaliacaoMetaTarefaToAttach.getClass(), avaliacaoMetaTarefaList2NewAvaliacaoMetaTarefaToAttach.getId());
                attachedAvaliacaoMetaTarefaList2New.add(avaliacaoMetaTarefaList2NewAvaliacaoMetaTarefaToAttach);
            }
            avaliacaoMetaTarefaList2New = attachedAvaliacaoMetaTarefaList2New;
            usuario.setAvaliacaoMetaTarefaList2(avaliacaoMetaTarefaList2New);
            List<OrcamentoTarefa> attachedOrcamentoTarefaListNew = new ArrayList<OrcamentoTarefa>();
            for (OrcamentoTarefa orcamentoTarefaListNewOrcamentoTarefaToAttach : orcamentoTarefaListNew) {
                orcamentoTarefaListNewOrcamentoTarefaToAttach = em.getReference(orcamentoTarefaListNewOrcamentoTarefaToAttach.getClass(), orcamentoTarefaListNewOrcamentoTarefaToAttach.getId());
                attachedOrcamentoTarefaListNew.add(orcamentoTarefaListNewOrcamentoTarefaToAttach);
            }
            orcamentoTarefaListNew = attachedOrcamentoTarefaListNew;
            usuario.setOrcamentoTarefaList(orcamentoTarefaListNew);
            List<ApontamentoTarefa> attachedApontamentoTarefaListNew = new ArrayList<ApontamentoTarefa>();
            for (ApontamentoTarefa apontamentoTarefaListNewApontamentoTarefaToAttach : apontamentoTarefaListNew) {
                apontamentoTarefaListNewApontamentoTarefaToAttach = em.getReference(apontamentoTarefaListNewApontamentoTarefaToAttach.getClass(), apontamentoTarefaListNewApontamentoTarefaToAttach.getId());
                attachedApontamentoTarefaListNew.add(apontamentoTarefaListNewApontamentoTarefaToAttach);
            }
            apontamentoTarefaListNew = attachedApontamentoTarefaListNew;
            usuario.setApontamentoTarefaList(apontamentoTarefaListNew);
            List<Departamento> attachedDepartamentoListNew = new ArrayList<Departamento>();
            for (Departamento departamentoListNewDepartamentoToAttach : departamentoListNew) {
                departamentoListNewDepartamentoToAttach = em.getReference(departamentoListNewDepartamentoToAttach.getClass(), departamentoListNewDepartamentoToAttach.getId());
                attachedDepartamentoListNew.add(departamentoListNewDepartamentoToAttach);
            }
            departamentoListNew = attachedDepartamentoListNew;
            usuario.setDepartamentoList(departamentoListNew);
            List<Usuario> attachedUsuarioListNew = new ArrayList<Usuario>();
            for (Usuario usuarioListNewUsuarioToAttach : usuarioListNew) {
                usuarioListNewUsuarioToAttach = em.getReference(usuarioListNewUsuarioToAttach.getClass(), usuarioListNewUsuarioToAttach.getId());
                attachedUsuarioListNew.add(usuarioListNewUsuarioToAttach);
            }
            usuarioListNew = attachedUsuarioListNew;
            usuario.setUsuarioList(usuarioListNew);
            List<Empresa> attachedEmpresaListNew = new ArrayList<Empresa>();
            for (Empresa empresaListNewEmpresaToAttach : empresaListNew) {
                empresaListNewEmpresaToAttach = em.getReference(empresaListNewEmpresaToAttach.getClass(), empresaListNewEmpresaToAttach.getId());
                attachedEmpresaListNew.add(empresaListNewEmpresaToAttach);
            }
            empresaListNew = attachedEmpresaListNew;
            usuario.setEmpresaList(empresaListNew);
            List<EmpresaCliente> attachedEmpresaClienteListNew = new ArrayList<EmpresaCliente>();
            for (EmpresaCliente empresaClienteListNewEmpresaClienteToAttach : empresaClienteListNew) {
                empresaClienteListNewEmpresaClienteToAttach = em.getReference(empresaClienteListNewEmpresaClienteToAttach.getClass(), empresaClienteListNewEmpresaClienteToAttach.getId());
                attachedEmpresaClienteListNew.add(empresaClienteListNewEmpresaClienteToAttach);
            }
            empresaClienteListNew = attachedEmpresaClienteListNew;
            usuario.setEmpresaClienteList(empresaClienteListNew);
            List<AnexoTarefa> attachedAnexoTarefaListNew = new ArrayList<AnexoTarefa>();
            for (AnexoTarefa anexoTarefaListNewAnexoTarefaToAttach : anexoTarefaListNew) {
                anexoTarefaListNewAnexoTarefaToAttach = em.getReference(anexoTarefaListNewAnexoTarefaToAttach.getClass(), anexoTarefaListNewAnexoTarefaToAttach.getId());
                attachedAnexoTarefaListNew.add(anexoTarefaListNewAnexoTarefaToAttach);
            }
            anexoTarefaListNew = attachedAnexoTarefaListNew;
            usuario.setAnexoTarefaList(anexoTarefaListNew);
            usuario = em.merge(usuario);
            if (idusuarioinclusaoOld != null && !idusuarioinclusaoOld.equals(idusuarioinclusaoNew)) {
                idusuarioinclusaoOld.getUsuarioList().remove(usuario);
                idusuarioinclusaoOld = em.merge(idusuarioinclusaoOld);
            }
            if (idusuarioinclusaoNew != null && !idusuarioinclusaoNew.equals(idusuarioinclusaoOld)) {
                idusuarioinclusaoNew.getUsuarioList().add(usuario);
                idusuarioinclusaoNew = em.merge(idusuarioinclusaoNew);
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
            for (Meta metasResponsaveisNewMeta : metasResponsaveisNew) {
                if (!metasResponsaveisOld.contains(metasResponsaveisNewMeta)) {
                    Usuario oldResponsavelOfMetasResponsaveisNewMeta = metasResponsaveisNewMeta.getResponsavel();
                    metasResponsaveisNewMeta.setResponsavel(usuario);
                    metasResponsaveisNewMeta = em.merge(metasResponsaveisNewMeta);
                    if (oldResponsavelOfMetasResponsaveisNewMeta != null && !oldResponsavelOfMetasResponsaveisNewMeta.equals(usuario)) {
                        oldResponsavelOfMetasResponsaveisNewMeta.getMetasResponsaveis().remove(metasResponsaveisNewMeta);
                        oldResponsavelOfMetasResponsaveisNewMeta = em.merge(oldResponsavelOfMetasResponsaveisNewMeta);
                    }
                }
            }
            for (FavoritosTarefaMeta favoritosTarefaMetaListNewFavoritosTarefaMeta : favoritosTarefaMetaListNew) {
                if (!favoritosTarefaMetaListOld.contains(favoritosTarefaMetaListNewFavoritosTarefaMeta)) {
                    Usuario oldIdusuarioinclusaoOfFavoritosTarefaMetaListNewFavoritosTarefaMeta = favoritosTarefaMetaListNewFavoritosTarefaMeta.getIdusuarioinclusao();
                    favoritosTarefaMetaListNewFavoritosTarefaMeta.setIdusuarioinclusao(usuario);
                    favoritosTarefaMetaListNewFavoritosTarefaMeta = em.merge(favoritosTarefaMetaListNewFavoritosTarefaMeta);
                    if (oldIdusuarioinclusaoOfFavoritosTarefaMetaListNewFavoritosTarefaMeta != null && !oldIdusuarioinclusaoOfFavoritosTarefaMetaListNewFavoritosTarefaMeta.equals(usuario)) {
                        oldIdusuarioinclusaoOfFavoritosTarefaMetaListNewFavoritosTarefaMeta.getFavoritosTarefaMetaList().remove(favoritosTarefaMetaListNewFavoritosTarefaMeta);
                        oldIdusuarioinclusaoOfFavoritosTarefaMetaListNewFavoritosTarefaMeta = em.merge(oldIdusuarioinclusaoOfFavoritosTarefaMetaListNewFavoritosTarefaMeta);
                    }
                }
            }
            for (CentroCusto centroCustoListNewCentroCusto : centroCustoListNew) {
                if (!centroCustoListOld.contains(centroCustoListNewCentroCusto)) {
                    Usuario oldIdusuarioinclusaoOfCentroCustoListNewCentroCusto = centroCustoListNewCentroCusto.getIdusuarioinclusao();
                    centroCustoListNewCentroCusto.setIdusuarioinclusao(usuario);
                    centroCustoListNewCentroCusto = em.merge(centroCustoListNewCentroCusto);
                    if (oldIdusuarioinclusaoOfCentroCustoListNewCentroCusto != null && !oldIdusuarioinclusaoOfCentroCustoListNewCentroCusto.equals(usuario)) {
                        oldIdusuarioinclusaoOfCentroCustoListNewCentroCusto.getCentroCustoList().remove(centroCustoListNewCentroCusto);
                        oldIdusuarioinclusaoOfCentroCustoListNewCentroCusto = em.merge(oldIdusuarioinclusaoOfCentroCustoListNewCentroCusto);
                    }
                }
            }
            for (Endereco enderecoListNewEndereco : enderecoListNew) {
                if (!enderecoListOld.contains(enderecoListNewEndereco)) {
                    Usuario oldIdusuarioinclusaoOfEnderecoListNewEndereco = enderecoListNewEndereco.getIdusuarioinclusao();
                    enderecoListNewEndereco.setIdusuarioinclusao(usuario);
                    enderecoListNewEndereco = em.merge(enderecoListNewEndereco);
                    if (oldIdusuarioinclusaoOfEnderecoListNewEndereco != null && !oldIdusuarioinclusaoOfEnderecoListNewEndereco.equals(usuario)) {
                        oldIdusuarioinclusaoOfEnderecoListNewEndereco.getEnderecoList().remove(enderecoListNewEndereco);
                        oldIdusuarioinclusaoOfEnderecoListNewEndereco = em.merge(oldIdusuarioinclusaoOfEnderecoListNewEndereco);
                    }
                }
            }
            for (Tarefa tarefaListOldTarefa : tarefaListOld) {
                if (!tarefaListNew.contains(tarefaListOldTarefa)) {
                    tarefaListOldTarefa.setIdusuarioinclusao(null);
                    tarefaListOldTarefa = em.merge(tarefaListOldTarefa);
                }
            }
            for (Tarefa tarefaListNewTarefa : tarefaListNew) {
                if (!tarefaListOld.contains(tarefaListNewTarefa)) {
                    Usuario oldIdusuarioinclusaoOfTarefaListNewTarefa = tarefaListNewTarefa.getIdusuarioinclusao();
                    tarefaListNewTarefa.setIdusuarioinclusao(usuario);
                    tarefaListNewTarefa = em.merge(tarefaListNewTarefa);
                    if (oldIdusuarioinclusaoOfTarefaListNewTarefa != null && !oldIdusuarioinclusaoOfTarefaListNewTarefa.equals(usuario)) {
                        oldIdusuarioinclusaoOfTarefaListNewTarefa.getTarefaList().remove(tarefaListNewTarefa);
                        oldIdusuarioinclusaoOfTarefaListNewTarefa = em.merge(oldIdusuarioinclusaoOfTarefaListNewTarefa);
                    }
                }
            }
            for (Tarefa tarefaList1NewTarefa : tarefaList1New) {
                if (!tarefaList1Old.contains(tarefaList1NewTarefa)) {
                    Usuario oldIdusuariosolicitanteOfTarefaList1NewTarefa = tarefaList1NewTarefa.getIdusuariosolicitante();
                    tarefaList1NewTarefa.setIdusuariosolicitante(usuario);
                    tarefaList1NewTarefa = em.merge(tarefaList1NewTarefa);
                    if (oldIdusuariosolicitanteOfTarefaList1NewTarefa != null && !oldIdusuariosolicitanteOfTarefaList1NewTarefa.equals(usuario)) {
                        oldIdusuariosolicitanteOfTarefaList1NewTarefa.getTarefaList1().remove(tarefaList1NewTarefa);
                        oldIdusuariosolicitanteOfTarefaList1NewTarefa = em.merge(oldIdusuariosolicitanteOfTarefaList1NewTarefa);
                    }
                }
            }
            for (Tarefa tarefaList2NewTarefa : tarefaList2New) {
                if (!tarefaList2Old.contains(tarefaList2NewTarefa)) {
                    Usuario oldIdusuarioresponsavelOfTarefaList2NewTarefa = tarefaList2NewTarefa.getIdusuarioresponsavel();
                    tarefaList2NewTarefa.setIdusuarioresponsavel(usuario);
                    tarefaList2NewTarefa = em.merge(tarefaList2NewTarefa);
                    if (oldIdusuarioresponsavelOfTarefaList2NewTarefa != null && !oldIdusuarioresponsavelOfTarefaList2NewTarefa.equals(usuario)) {
                        oldIdusuarioresponsavelOfTarefaList2NewTarefa.getTarefaList2().remove(tarefaList2NewTarefa);
                        oldIdusuarioresponsavelOfTarefaList2NewTarefa = em.merge(oldIdusuarioresponsavelOfTarefaList2NewTarefa);
                    }
                }
            }
            for (ParicipanteTarefa paricipanteTarefaListNewParicipanteTarefa : paricipanteTarefaListNew) {
                if (!paricipanteTarefaListOld.contains(paricipanteTarefaListNewParicipanteTarefa)) {
                    Usuario oldIdusuarioinclusaoOfParicipanteTarefaListNewParicipanteTarefa = paricipanteTarefaListNewParicipanteTarefa.getIdusuarioinclusao();
                    paricipanteTarefaListNewParicipanteTarefa.setIdusuarioinclusao(usuario);
                    paricipanteTarefaListNewParicipanteTarefa = em.merge(paricipanteTarefaListNewParicipanteTarefa);
                    if (oldIdusuarioinclusaoOfParicipanteTarefaListNewParicipanteTarefa != null && !oldIdusuarioinclusaoOfParicipanteTarefaListNewParicipanteTarefa.equals(usuario)) {
                        oldIdusuarioinclusaoOfParicipanteTarefaListNewParicipanteTarefa.getParicipanteTarefaList().remove(paricipanteTarefaListNewParicipanteTarefa);
                        oldIdusuarioinclusaoOfParicipanteTarefaListNewParicipanteTarefa = em.merge(oldIdusuarioinclusaoOfParicipanteTarefaListNewParicipanteTarefa);
                    }
                }
            }
            for (ParicipanteTarefa paricipanteTarefaList1NewParicipanteTarefa : paricipanteTarefaList1New) {
                if (!paricipanteTarefaList1Old.contains(paricipanteTarefaList1NewParicipanteTarefa)) {
                    Usuario oldIdusuarioparticipanteOfParicipanteTarefaList1NewParicipanteTarefa = paricipanteTarefaList1NewParicipanteTarefa.getIdusuarioparticipante();
                    paricipanteTarefaList1NewParicipanteTarefa.setIdusuarioparticipante(usuario);
                    paricipanteTarefaList1NewParicipanteTarefa = em.merge(paricipanteTarefaList1NewParicipanteTarefa);
                    if (oldIdusuarioparticipanteOfParicipanteTarefaList1NewParicipanteTarefa != null && !oldIdusuarioparticipanteOfParicipanteTarefaList1NewParicipanteTarefa.equals(usuario)) {
                        oldIdusuarioparticipanteOfParicipanteTarefaList1NewParicipanteTarefa.getParicipanteTarefaList1().remove(paricipanteTarefaList1NewParicipanteTarefa);
                        oldIdusuarioparticipanteOfParicipanteTarefaList1NewParicipanteTarefa = em.merge(oldIdusuarioparticipanteOfParicipanteTarefaList1NewParicipanteTarefa);
                    }
                }
            }
            for (FilialCliente filialClienteListNewFilialCliente : filialClienteListNew) {
                if (!filialClienteListOld.contains(filialClienteListNewFilialCliente)) {
                    Usuario oldIdusuarioinclusaoOfFilialClienteListNewFilialCliente = filialClienteListNewFilialCliente.getIdusuarioinclusao();
                    filialClienteListNewFilialCliente.setIdusuarioinclusao(usuario);
                    filialClienteListNewFilialCliente = em.merge(filialClienteListNewFilialCliente);
                    if (oldIdusuarioinclusaoOfFilialClienteListNewFilialCliente != null && !oldIdusuarioinclusaoOfFilialClienteListNewFilialCliente.equals(usuario)) {
                        oldIdusuarioinclusaoOfFilialClienteListNewFilialCliente.getFilialClienteList().remove(filialClienteListNewFilialCliente);
                        oldIdusuarioinclusaoOfFilialClienteListNewFilialCliente = em.merge(oldIdusuarioinclusaoOfFilialClienteListNewFilialCliente);
                    }
                }
            }
            for (FilialEmpresa filialEmpresaListNewFilialEmpresa : filialEmpresaListNew) {
                if (!filialEmpresaListOld.contains(filialEmpresaListNewFilialEmpresa)) {
                    Usuario oldIdusuarioinclusaoOfFilialEmpresaListNewFilialEmpresa = filialEmpresaListNewFilialEmpresa.getIdusuarioinclusao();
                    filialEmpresaListNewFilialEmpresa.setIdusuarioinclusao(usuario);
                    filialEmpresaListNewFilialEmpresa = em.merge(filialEmpresaListNewFilialEmpresa);
                    if (oldIdusuarioinclusaoOfFilialEmpresaListNewFilialEmpresa != null && !oldIdusuarioinclusaoOfFilialEmpresaListNewFilialEmpresa.equals(usuario)) {
                        oldIdusuarioinclusaoOfFilialEmpresaListNewFilialEmpresa.getFilialEmpresaList().remove(filialEmpresaListNewFilialEmpresa);
                        oldIdusuarioinclusaoOfFilialEmpresaListNewFilialEmpresa = em.merge(oldIdusuarioinclusaoOfFilialEmpresaListNewFilialEmpresa);
                    }
                }
            }
            for (AvaliacaoMetaTarefa avaliacaoMetaTarefaListNewAvaliacaoMetaTarefa : avaliacaoMetaTarefaListNew) {
                if (!avaliacaoMetaTarefaListOld.contains(avaliacaoMetaTarefaListNewAvaliacaoMetaTarefa)) {
                    Usuario oldIdusuarioinclusaoOfAvaliacaoMetaTarefaListNewAvaliacaoMetaTarefa = avaliacaoMetaTarefaListNewAvaliacaoMetaTarefa.getIdusuarioinclusao();
                    avaliacaoMetaTarefaListNewAvaliacaoMetaTarefa.setIdusuarioinclusao(usuario);
                    avaliacaoMetaTarefaListNewAvaliacaoMetaTarefa = em.merge(avaliacaoMetaTarefaListNewAvaliacaoMetaTarefa);
                    if (oldIdusuarioinclusaoOfAvaliacaoMetaTarefaListNewAvaliacaoMetaTarefa != null && !oldIdusuarioinclusaoOfAvaliacaoMetaTarefaListNewAvaliacaoMetaTarefa.equals(usuario)) {
                        oldIdusuarioinclusaoOfAvaliacaoMetaTarefaListNewAvaliacaoMetaTarefa.getAvaliacaoMetaTarefaList().remove(avaliacaoMetaTarefaListNewAvaliacaoMetaTarefa);
                        oldIdusuarioinclusaoOfAvaliacaoMetaTarefaListNewAvaliacaoMetaTarefa = em.merge(oldIdusuarioinclusaoOfAvaliacaoMetaTarefaListNewAvaliacaoMetaTarefa);
                    }
                }
            }
            for (AvaliacaoMetaTarefa avaliacaoMetaTarefaList1NewAvaliacaoMetaTarefa : avaliacaoMetaTarefaList1New) {
                if (!avaliacaoMetaTarefaList1Old.contains(avaliacaoMetaTarefaList1NewAvaliacaoMetaTarefa)) {
                    Usuario oldIdusuarioavaliadorOfAvaliacaoMetaTarefaList1NewAvaliacaoMetaTarefa = avaliacaoMetaTarefaList1NewAvaliacaoMetaTarefa.getIdusuarioavaliador();
                    avaliacaoMetaTarefaList1NewAvaliacaoMetaTarefa.setIdusuarioavaliador(usuario);
                    avaliacaoMetaTarefaList1NewAvaliacaoMetaTarefa = em.merge(avaliacaoMetaTarefaList1NewAvaliacaoMetaTarefa);
                    if (oldIdusuarioavaliadorOfAvaliacaoMetaTarefaList1NewAvaliacaoMetaTarefa != null && !oldIdusuarioavaliadorOfAvaliacaoMetaTarefaList1NewAvaliacaoMetaTarefa.equals(usuario)) {
                        oldIdusuarioavaliadorOfAvaliacaoMetaTarefaList1NewAvaliacaoMetaTarefa.getAvaliacaoMetaTarefaList1().remove(avaliacaoMetaTarefaList1NewAvaliacaoMetaTarefa);
                        oldIdusuarioavaliadorOfAvaliacaoMetaTarefaList1NewAvaliacaoMetaTarefa = em.merge(oldIdusuarioavaliadorOfAvaliacaoMetaTarefaList1NewAvaliacaoMetaTarefa);
                    }
                }
            }
            for (AvaliacaoMetaTarefa avaliacaoMetaTarefaList2NewAvaliacaoMetaTarefa : avaliacaoMetaTarefaList2New) {
                if (!avaliacaoMetaTarefaList2Old.contains(avaliacaoMetaTarefaList2NewAvaliacaoMetaTarefa)) {
                    Usuario oldIdusuarioavaliadoOfAvaliacaoMetaTarefaList2NewAvaliacaoMetaTarefa = avaliacaoMetaTarefaList2NewAvaliacaoMetaTarefa.getIdusuarioavaliado();
                    avaliacaoMetaTarefaList2NewAvaliacaoMetaTarefa.setIdusuarioavaliado(usuario);
                    avaliacaoMetaTarefaList2NewAvaliacaoMetaTarefa = em.merge(avaliacaoMetaTarefaList2NewAvaliacaoMetaTarefa);
                    if (oldIdusuarioavaliadoOfAvaliacaoMetaTarefaList2NewAvaliacaoMetaTarefa != null && !oldIdusuarioavaliadoOfAvaliacaoMetaTarefaList2NewAvaliacaoMetaTarefa.equals(usuario)) {
                        oldIdusuarioavaliadoOfAvaliacaoMetaTarefaList2NewAvaliacaoMetaTarefa.getAvaliacaoMetaTarefaList2().remove(avaliacaoMetaTarefaList2NewAvaliacaoMetaTarefa);
                        oldIdusuarioavaliadoOfAvaliacaoMetaTarefaList2NewAvaliacaoMetaTarefa = em.merge(oldIdusuarioavaliadoOfAvaliacaoMetaTarefaList2NewAvaliacaoMetaTarefa);
                    }
                }
            }
            for (OrcamentoTarefa orcamentoTarefaListNewOrcamentoTarefa : orcamentoTarefaListNew) {
                if (!orcamentoTarefaListOld.contains(orcamentoTarefaListNewOrcamentoTarefa)) {
                    Usuario oldIdusuarioinclusaoOfOrcamentoTarefaListNewOrcamentoTarefa = orcamentoTarefaListNewOrcamentoTarefa.getIdusuarioinclusao();
                    orcamentoTarefaListNewOrcamentoTarefa.setIdusuarioinclusao(usuario);
                    orcamentoTarefaListNewOrcamentoTarefa = em.merge(orcamentoTarefaListNewOrcamentoTarefa);
                    if (oldIdusuarioinclusaoOfOrcamentoTarefaListNewOrcamentoTarefa != null && !oldIdusuarioinclusaoOfOrcamentoTarefaListNewOrcamentoTarefa.equals(usuario)) {
                        oldIdusuarioinclusaoOfOrcamentoTarefaListNewOrcamentoTarefa.getOrcamentoTarefaList().remove(orcamentoTarefaListNewOrcamentoTarefa);
                        oldIdusuarioinclusaoOfOrcamentoTarefaListNewOrcamentoTarefa = em.merge(oldIdusuarioinclusaoOfOrcamentoTarefaListNewOrcamentoTarefa);
                    }
                }
            }
            for (ApontamentoTarefa apontamentoTarefaListNewApontamentoTarefa : apontamentoTarefaListNew) {
                if (!apontamentoTarefaListOld.contains(apontamentoTarefaListNewApontamentoTarefa)) {
                    Usuario oldIdusuarioinclusaoOfApontamentoTarefaListNewApontamentoTarefa = apontamentoTarefaListNewApontamentoTarefa.getIdUsuarioInclusao();
                    apontamentoTarefaListNewApontamentoTarefa.setIdUsuarioInclusao(usuario);
                    apontamentoTarefaListNewApontamentoTarefa = em.merge(apontamentoTarefaListNewApontamentoTarefa);
                    if (oldIdusuarioinclusaoOfApontamentoTarefaListNewApontamentoTarefa != null && !oldIdusuarioinclusaoOfApontamentoTarefaListNewApontamentoTarefa.equals(usuario)) {
                        oldIdusuarioinclusaoOfApontamentoTarefaListNewApontamentoTarefa.getApontamentoTarefaList().remove(apontamentoTarefaListNewApontamentoTarefa);
                        oldIdusuarioinclusaoOfApontamentoTarefaListNewApontamentoTarefa = em.merge(oldIdusuarioinclusaoOfApontamentoTarefaListNewApontamentoTarefa);
                    }
                }
            }
            for (Departamento departamentoListNewDepartamento : departamentoListNew) {
                if (!departamentoListOld.contains(departamentoListNewDepartamento)) {
                    Usuario oldIdusuarioinclusaoOfDepartamentoListNewDepartamento = departamentoListNewDepartamento.getIdusuarioinclusao();
                    departamentoListNewDepartamento.setIdusuarioinclusao(usuario);
                    departamentoListNewDepartamento = em.merge(departamentoListNewDepartamento);
                    if (oldIdusuarioinclusaoOfDepartamentoListNewDepartamento != null && !oldIdusuarioinclusaoOfDepartamentoListNewDepartamento.equals(usuario)) {
                        oldIdusuarioinclusaoOfDepartamentoListNewDepartamento.getDepartamentoList().remove(departamentoListNewDepartamento);
                        oldIdusuarioinclusaoOfDepartamentoListNewDepartamento = em.merge(oldIdusuarioinclusaoOfDepartamentoListNewDepartamento);
                    }
                }
            }
            for (Usuario usuarioListOldUsuario : usuarioListOld) {
                if (!usuarioListNew.contains(usuarioListOldUsuario)) {
                    usuarioListOldUsuario.setIdusuarioinclusao(null);
                    usuarioListOldUsuario = em.merge(usuarioListOldUsuario);
                }
            }
            for (Usuario usuarioListNewUsuario : usuarioListNew) {
                if (!usuarioListOld.contains(usuarioListNewUsuario)) {
                    Usuario oldIdusuarioinclusaoOfUsuarioListNewUsuario = usuarioListNewUsuario.getIdusuarioinclusao();
                    usuarioListNewUsuario.setIdusuarioinclusao(usuario);
                    usuarioListNewUsuario = em.merge(usuarioListNewUsuario);
                    if (oldIdusuarioinclusaoOfUsuarioListNewUsuario != null && !oldIdusuarioinclusaoOfUsuarioListNewUsuario.equals(usuario)) {
                        oldIdusuarioinclusaoOfUsuarioListNewUsuario.getUsuarioList().remove(usuarioListNewUsuario);
                        oldIdusuarioinclusaoOfUsuarioListNewUsuario = em.merge(oldIdusuarioinclusaoOfUsuarioListNewUsuario);
                    }
                }
            }
            for (Empresa empresaListNewEmpresa : empresaListNew) {
                if (!empresaListOld.contains(empresaListNewEmpresa)) {
                    Usuario oldIdusuarioinclusaoOfEmpresaListNewEmpresa = empresaListNewEmpresa.getIdusuarioinclusao();
                    empresaListNewEmpresa.setIdusuarioinclusao(usuario);
                    empresaListNewEmpresa = em.merge(empresaListNewEmpresa);
                    if (oldIdusuarioinclusaoOfEmpresaListNewEmpresa != null && !oldIdusuarioinclusaoOfEmpresaListNewEmpresa.equals(usuario)) {
                        oldIdusuarioinclusaoOfEmpresaListNewEmpresa.getEmpresaList().remove(empresaListNewEmpresa);
                        oldIdusuarioinclusaoOfEmpresaListNewEmpresa = em.merge(oldIdusuarioinclusaoOfEmpresaListNewEmpresa);
                    }
                }
            }
            for (EmpresaCliente empresaClienteListNewEmpresaCliente : empresaClienteListNew) {
                if (!empresaClienteListOld.contains(empresaClienteListNewEmpresaCliente)) {
                    Usuario oldIdusuarioinclusaoOfEmpresaClienteListNewEmpresaCliente = empresaClienteListNewEmpresaCliente.getIdusuarioinclusao();
                    empresaClienteListNewEmpresaCliente.setIdusuarioinclusao(usuario);
                    empresaClienteListNewEmpresaCliente = em.merge(empresaClienteListNewEmpresaCliente);
                    if (oldIdusuarioinclusaoOfEmpresaClienteListNewEmpresaCliente != null && !oldIdusuarioinclusaoOfEmpresaClienteListNewEmpresaCliente.equals(usuario)) {
                        oldIdusuarioinclusaoOfEmpresaClienteListNewEmpresaCliente.getEmpresaClienteList().remove(empresaClienteListNewEmpresaCliente);
                        oldIdusuarioinclusaoOfEmpresaClienteListNewEmpresaCliente = em.merge(oldIdusuarioinclusaoOfEmpresaClienteListNewEmpresaCliente);
                    }
                }
            }
            for (AnexoTarefa anexoTarefaListNewAnexoTarefa : anexoTarefaListNew) {
                if (!anexoTarefaListOld.contains(anexoTarefaListNewAnexoTarefa)) {
                    Usuario oldIdusuarioinclusaoOfAnexoTarefaListNewAnexoTarefa = anexoTarefaListNewAnexoTarefa.getIdUsuarioInclusao();
                    anexoTarefaListNewAnexoTarefa.setIdUsuarioInclusao(usuario);
                    anexoTarefaListNewAnexoTarefa = em.merge(anexoTarefaListNewAnexoTarefa);
                    if (oldIdusuarioinclusaoOfAnexoTarefaListNewAnexoTarefa != null && !oldIdusuarioinclusaoOfAnexoTarefaListNewAnexoTarefa.equals(usuario)) {
                        oldIdusuarioinclusaoOfAnexoTarefaListNewAnexoTarefa.getAnexoTarefaList().remove(anexoTarefaListNewAnexoTarefa);
                        oldIdusuarioinclusaoOfAnexoTarefaListNewAnexoTarefa = em.merge(oldIdusuarioinclusaoOfAnexoTarefaListNewAnexoTarefa);
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
            Collection<Meta> metasResponsaveisOrphanCheck = usuario.getMetasResponsaveis();
            for (Meta metasResponsaveisOrphanCheckMeta : metasResponsaveisOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Meta " + metasResponsaveisOrphanCheckMeta + " in its metasResponsaveis field has a non-nullable responsavel field.");
            }
            List<FavoritosTarefaMeta> favoritosTarefaMetaListOrphanCheck = usuario.getFavoritosTarefaMetaList();
            for (FavoritosTarefaMeta favoritosTarefaMetaListOrphanCheckFavoritosTarefaMeta : favoritosTarefaMetaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the FavoritosTarefaMeta " + favoritosTarefaMetaListOrphanCheckFavoritosTarefaMeta + " in its favoritosTarefaMetaList field has a non-nullable idusuarioinclusao field.");
            }
            List<CentroCusto> centroCustoListOrphanCheck = usuario.getCentroCustoList();
            for (CentroCusto centroCustoListOrphanCheckCentroCusto : centroCustoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the CentroCusto " + centroCustoListOrphanCheckCentroCusto + " in its centroCustoList field has a non-nullable idusuarioinclusao field.");
            }
            List<Endereco> enderecoListOrphanCheck = usuario.getEnderecoList();
            for (Endereco enderecoListOrphanCheckEndereco : enderecoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Endereco " + enderecoListOrphanCheckEndereco + " in its enderecoList field has a non-nullable idusuarioinclusao field.");
            }
            List<Tarefa> tarefaList1OrphanCheck = usuario.getTarefaList1();
            for (Tarefa tarefaList1OrphanCheckTarefa : tarefaList1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Tarefa " + tarefaList1OrphanCheckTarefa + " in its tarefaList1 field has a non-nullable idusuariosolicitante field.");
            }
            List<Tarefa> tarefaList2OrphanCheck = usuario.getTarefaList2();
            for (Tarefa tarefaList2OrphanCheckTarefa : tarefaList2OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Tarefa " + tarefaList2OrphanCheckTarefa + " in its tarefaList2 field has a non-nullable idusuarioresponsavel field.");
            }
            List<ParicipanteTarefa> paricipanteTarefaListOrphanCheck = usuario.getParicipanteTarefaList();
            for (ParicipanteTarefa paricipanteTarefaListOrphanCheckParicipanteTarefa : paricipanteTarefaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the ParicipanteTarefa " + paricipanteTarefaListOrphanCheckParicipanteTarefa + " in its paricipanteTarefaList field has a non-nullable idusuarioinclusao field.");
            }
            List<ParicipanteTarefa> paricipanteTarefaList1OrphanCheck = usuario.getParicipanteTarefaList1();
            for (ParicipanteTarefa paricipanteTarefaList1OrphanCheckParicipanteTarefa : paricipanteTarefaList1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the ParicipanteTarefa " + paricipanteTarefaList1OrphanCheckParicipanteTarefa + " in its paricipanteTarefaList1 field has a non-nullable idusuarioparticipante field.");
            }
            List<FilialCliente> filialClienteListOrphanCheck = usuario.getFilialClienteList();
            for (FilialCliente filialClienteListOrphanCheckFilialCliente : filialClienteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the FilialCliente " + filialClienteListOrphanCheckFilialCliente + " in its filialClienteList field has a non-nullable idusuarioinclusao field.");
            }
            List<FilialEmpresa> filialEmpresaListOrphanCheck = usuario.getFilialEmpresaList();
            for (FilialEmpresa filialEmpresaListOrphanCheckFilialEmpresa : filialEmpresaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the FilialEmpresa " + filialEmpresaListOrphanCheckFilialEmpresa + " in its filialEmpresaList field has a non-nullable idusuarioinclusao field.");
            }
            List<AvaliacaoMetaTarefa> avaliacaoMetaTarefaListOrphanCheck = usuario.getAvaliacaoMetaTarefaList();
            for (AvaliacaoMetaTarefa avaliacaoMetaTarefaListOrphanCheckAvaliacaoMetaTarefa : avaliacaoMetaTarefaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the AvaliacaoMetaTarefa " + avaliacaoMetaTarefaListOrphanCheckAvaliacaoMetaTarefa + " in its avaliacaoMetaTarefaList field has a non-nullable idusuarioinclusao field.");
            }
            List<AvaliacaoMetaTarefa> avaliacaoMetaTarefaList1OrphanCheck = usuario.getAvaliacaoMetaTarefaList1();
            for (AvaliacaoMetaTarefa avaliacaoMetaTarefaList1OrphanCheckAvaliacaoMetaTarefa : avaliacaoMetaTarefaList1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the AvaliacaoMetaTarefa " + avaliacaoMetaTarefaList1OrphanCheckAvaliacaoMetaTarefa + " in its avaliacaoMetaTarefaList1 field has a non-nullable idusuarioavaliador field.");
            }
            List<AvaliacaoMetaTarefa> avaliacaoMetaTarefaList2OrphanCheck = usuario.getAvaliacaoMetaTarefaList2();
            for (AvaliacaoMetaTarefa avaliacaoMetaTarefaList2OrphanCheckAvaliacaoMetaTarefa : avaliacaoMetaTarefaList2OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the AvaliacaoMetaTarefa " + avaliacaoMetaTarefaList2OrphanCheckAvaliacaoMetaTarefa + " in its avaliacaoMetaTarefaList2 field has a non-nullable idusuarioavaliado field.");
            }
            List<OrcamentoTarefa> orcamentoTarefaListOrphanCheck = usuario.getOrcamentoTarefaList();
            for (OrcamentoTarefa orcamentoTarefaListOrphanCheckOrcamentoTarefa : orcamentoTarefaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the OrcamentoTarefa " + orcamentoTarefaListOrphanCheckOrcamentoTarefa + " in its orcamentoTarefaList field has a non-nullable idusuarioinclusao field.");
            }
            List<ApontamentoTarefa> apontamentoTarefaListOrphanCheck = usuario.getApontamentoTarefaList();
            for (ApontamentoTarefa apontamentoTarefaListOrphanCheckApontamentoTarefa : apontamentoTarefaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the ApontamentoTarefa " + apontamentoTarefaListOrphanCheckApontamentoTarefa + " in its apontamentoTarefaList field has a non-nullable idusuarioinclusao field.");
            }
            List<Departamento> departamentoListOrphanCheck = usuario.getDepartamentoList();
            for (Departamento departamentoListOrphanCheckDepartamento : departamentoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Departamento " + departamentoListOrphanCheckDepartamento + " in its departamentoList field has a non-nullable idusuarioinclusao field.");
            }
            List<Empresa> empresaListOrphanCheck = usuario.getEmpresaList();
            for (Empresa empresaListOrphanCheckEmpresa : empresaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Empresa " + empresaListOrphanCheckEmpresa + " in its empresaList field has a non-nullable idusuarioinclusao field.");
            }
            List<EmpresaCliente> empresaClienteListOrphanCheck = usuario.getEmpresaClienteList();
            for (EmpresaCliente empresaClienteListOrphanCheckEmpresaCliente : empresaClienteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the EmpresaCliente " + empresaClienteListOrphanCheckEmpresaCliente + " in its empresaClienteList field has a non-nullable idusuarioinclusao field.");
            }
            List<AnexoTarefa> anexoTarefaListOrphanCheck = usuario.getAnexoTarefaList();
            for (AnexoTarefa anexoTarefaListOrphanCheckAnexoTarefa : anexoTarefaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the AnexoTarefa " + anexoTarefaListOrphanCheckAnexoTarefa + " in its anexoTarefaList field has a non-nullable idusuarioinclusao field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuario idusuarioinclusao = usuario.getIdusuarioinclusao();
            if (idusuarioinclusao != null) {
                idusuarioinclusao.getUsuarioList().remove(usuario);
                idusuarioinclusao = em.merge(idusuarioinclusao);
            }
            List<Tarefa> tarefaList = usuario.getTarefaList();
            for (Tarefa tarefaListTarefa : tarefaList) {
                tarefaListTarefa.setIdusuarioinclusao(null);
                tarefaListTarefa = em.merge(tarefaListTarefa);
            }
            List<Usuario> usuarioList = usuario.getUsuarioList();
            for (Usuario usuarioListUsuario : usuarioList) {
                usuarioListUsuario.setIdusuarioinclusao(null);
                usuarioListUsuario = em.merge(usuarioListUsuario);
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

        /**
     * Busca um Usuairo pelo seu Login
     * @param login
     * @return 
     */
    public Usuario findByLogin(String login) {
        EntityManager em = getEntityManager();

        try {
            return (Usuario) em.createNamedQuery("Usuario.findByLogin")
                    .setParameter("login", login)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }


}
