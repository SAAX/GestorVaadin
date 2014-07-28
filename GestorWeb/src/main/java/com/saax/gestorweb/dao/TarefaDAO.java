package com.saax.gestorweb.dao;

import com.saax.gestorweb.dao.exceptions.IllegalOrphanException;
import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.saax.gestorweb.model.datamodel.CentroCusto;
import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
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
        if (tarefa.getFavoritosTarefaMetaList() == null) {
            tarefa.setFavoritosTarefaMetaList(new ArrayList<FavoritosTarefaMeta>());
        }
        if (tarefa.getTarefaList() == null) {
            tarefa.setTarefaList(new ArrayList<Tarefa>());
        }
        if (tarefa.getTarefaList1() == null) {
            tarefa.setTarefaList1(new ArrayList<Tarefa>());
        }
        if (tarefa.getTarefaList2() == null) {
            tarefa.setTarefaList2(new ArrayList<Tarefa>());
        }
        if (tarefa.getParicipanteTarefaList() == null) {
            tarefa.setParicipanteTarefaList(new ArrayList<ParicipanteTarefa>());
        }
        if (tarefa.getAvaliacaoMetaTarefaList() == null) {
            tarefa.setAvaliacaoMetaTarefaList(new ArrayList<AvaliacaoMetaTarefa>());
        }
        if (tarefa.getOrcamentoTarefaList() == null) {
            tarefa.setOrcamentoTarefaList(new ArrayList<OrcamentoTarefa>());
        }
        if (tarefa.getApontamentoTarefaList() == null) {
            tarefa.setApontamentoTarefaList(new ArrayList<ApontamentoTarefa>());
        }
        if (tarefa.getAnexoTarefaList() == null) {
            tarefa.setAnexoTarefaList(new ArrayList<AnexoTarefa>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CentroCusto idcentrocusto = tarefa.getIdcentrocusto();
            if (idcentrocusto != null) {
                idcentrocusto = em.getReference(idcentrocusto.getClass(), idcentrocusto.getId());
                tarefa.setIdcentrocusto(idcentrocusto);
            }
            Departamento iddepartamento = tarefa.getIddepartamento();
            if (iddepartamento != null) {
                iddepartamento = em.getReference(iddepartamento.getClass(), iddepartamento.getId());
                tarefa.setIddepartamento(iddepartamento);
            }
            Empresa idempresa = tarefa.getIdempresa();
            if (idempresa != null) {
                idempresa = em.getReference(idempresa.getClass(), idempresa.getId());
                tarefa.setIdempresa(idempresa);
            }
            EmpresaCliente idempresacliente = tarefa.getIdempresacliente();
            if (idempresacliente != null) {
                idempresacliente = em.getReference(idempresacliente.getClass(), idempresacliente.getId());
                tarefa.setIdempresacliente(idempresacliente);
            }
            Tarefa idtarefaproxima = tarefa.getIdtarefaproxima();
            if (idtarefaproxima != null) {
                idtarefaproxima = em.getReference(idtarefaproxima.getClass(), idtarefaproxima.getId());
                tarefa.setIdtarefaproxima(idtarefaproxima);
            }
            Tarefa idtarefaanterior = tarefa.getIdtarefaanterior();
            if (idtarefaanterior != null) {
                idtarefaanterior = em.getReference(idtarefaanterior.getClass(), idtarefaanterior.getId());
                tarefa.setIdtarefaanterior(idtarefaanterior);
            }
            Tarefa idtarefapai = tarefa.getIdtarefapai();
            if (idtarefapai != null) {
                idtarefapai = em.getReference(idtarefapai.getClass(), idtarefapai.getId());
                tarefa.setIdtarefapai(idtarefapai);
            }
            Usuario idusuarioinclusao = tarefa.getIdusuarioinclusao();
            if (idusuarioinclusao != null) {
                idusuarioinclusao = em.getReference(idusuarioinclusao.getClass(), idusuarioinclusao.getId());
                tarefa.setIdusuarioinclusao(idusuarioinclusao);
            }
            Usuario idusuariosolicitante = tarefa.getIdusuariosolicitante();
            if (idusuariosolicitante != null) {
                idusuariosolicitante = em.getReference(idusuariosolicitante.getClass(), idusuariosolicitante.getId());
                tarefa.setIdusuariosolicitante(idusuariosolicitante);
            }
            Usuario idusuarioresponsavel = tarefa.getIdusuarioresponsavel();
            if (idusuarioresponsavel != null) {
                idusuarioresponsavel = em.getReference(idusuarioresponsavel.getClass(), idusuarioresponsavel.getId());
                tarefa.setIdusuarioresponsavel(idusuarioresponsavel);
            }
            List<FavoritosTarefaMeta> attachedFavoritosTarefaMetaList = new ArrayList<FavoritosTarefaMeta>();
            for (FavoritosTarefaMeta favoritosTarefaMetaListFavoritosTarefaMetaToAttach : tarefa.getFavoritosTarefaMetaList()) {
                favoritosTarefaMetaListFavoritosTarefaMetaToAttach = em.getReference(favoritosTarefaMetaListFavoritosTarefaMetaToAttach.getClass(), favoritosTarefaMetaListFavoritosTarefaMetaToAttach.getId());
                attachedFavoritosTarefaMetaList.add(favoritosTarefaMetaListFavoritosTarefaMetaToAttach);
            }
            tarefa.setFavoritosTarefaMetaList(attachedFavoritosTarefaMetaList);
            List<Tarefa> attachedTarefaList = new ArrayList<Tarefa>();
            for (Tarefa tarefaListTarefaToAttach : tarefa.getTarefaList()) {
                tarefaListTarefaToAttach = em.getReference(tarefaListTarefaToAttach.getClass(), tarefaListTarefaToAttach.getId());
                attachedTarefaList.add(tarefaListTarefaToAttach);
            }
            tarefa.setTarefaList(attachedTarefaList);
            List<Tarefa> attachedTarefaList1 = new ArrayList<Tarefa>();
            for (Tarefa tarefaList1TarefaToAttach : tarefa.getTarefaList1()) {
                tarefaList1TarefaToAttach = em.getReference(tarefaList1TarefaToAttach.getClass(), tarefaList1TarefaToAttach.getId());
                attachedTarefaList1.add(tarefaList1TarefaToAttach);
            }
            tarefa.setTarefaList1(attachedTarefaList1);
            List<Tarefa> attachedTarefaList2 = new ArrayList<Tarefa>();
            for (Tarefa tarefaList2TarefaToAttach : tarefa.getTarefaList2()) {
                tarefaList2TarefaToAttach = em.getReference(tarefaList2TarefaToAttach.getClass(), tarefaList2TarefaToAttach.getId());
                attachedTarefaList2.add(tarefaList2TarefaToAttach);
            }
            tarefa.setTarefaList2(attachedTarefaList2);
            List<ParicipanteTarefa> attachedParicipanteTarefaList = new ArrayList<ParicipanteTarefa>();
            for (ParicipanteTarefa paricipanteTarefaListParicipanteTarefaToAttach : tarefa.getParicipanteTarefaList()) {
                paricipanteTarefaListParicipanteTarefaToAttach = em.getReference(paricipanteTarefaListParicipanteTarefaToAttach.getClass(), paricipanteTarefaListParicipanteTarefaToAttach.getId());
                attachedParicipanteTarefaList.add(paricipanteTarefaListParicipanteTarefaToAttach);
            }
            tarefa.setParicipanteTarefaList(attachedParicipanteTarefaList);
            List<AvaliacaoMetaTarefa> attachedAvaliacaoMetaTarefaList = new ArrayList<AvaliacaoMetaTarefa>();
            for (AvaliacaoMetaTarefa avaliacaoMetaTarefaListAvaliacaoMetaTarefaToAttach : tarefa.getAvaliacaoMetaTarefaList()) {
                avaliacaoMetaTarefaListAvaliacaoMetaTarefaToAttach = em.getReference(avaliacaoMetaTarefaListAvaliacaoMetaTarefaToAttach.getClass(), avaliacaoMetaTarefaListAvaliacaoMetaTarefaToAttach.getId());
                attachedAvaliacaoMetaTarefaList.add(avaliacaoMetaTarefaListAvaliacaoMetaTarefaToAttach);
            }
            tarefa.setAvaliacaoMetaTarefaList(attachedAvaliacaoMetaTarefaList);
            List<OrcamentoTarefa> attachedOrcamentoTarefaList = new ArrayList<OrcamentoTarefa>();
            for (OrcamentoTarefa orcamentoTarefaListOrcamentoTarefaToAttach : tarefa.getOrcamentoTarefaList()) {
                orcamentoTarefaListOrcamentoTarefaToAttach = em.getReference(orcamentoTarefaListOrcamentoTarefaToAttach.getClass(), orcamentoTarefaListOrcamentoTarefaToAttach.getId());
                attachedOrcamentoTarefaList.add(orcamentoTarefaListOrcamentoTarefaToAttach);
            }
            tarefa.setOrcamentoTarefaList(attachedOrcamentoTarefaList);
            List<ApontamentoTarefa> attachedApontamentoTarefaList = new ArrayList<ApontamentoTarefa>();
            for (ApontamentoTarefa apontamentoTarefaListApontamentoTarefaToAttach : tarefa.getApontamentoTarefaList()) {
                apontamentoTarefaListApontamentoTarefaToAttach = em.getReference(apontamentoTarefaListApontamentoTarefaToAttach.getClass(), apontamentoTarefaListApontamentoTarefaToAttach.getId());
                attachedApontamentoTarefaList.add(apontamentoTarefaListApontamentoTarefaToAttach);
            }
            tarefa.setApontamentoTarefaList(attachedApontamentoTarefaList);
            List<AnexoTarefa> attachedAnexoTarefaList = new ArrayList<AnexoTarefa>();
            for (AnexoTarefa anexoTarefaListAnexoTarefaToAttach : tarefa.getAnexoTarefaList()) {
                anexoTarefaListAnexoTarefaToAttach = em.getReference(anexoTarefaListAnexoTarefaToAttach.getClass(), anexoTarefaListAnexoTarefaToAttach.getId());
                attachedAnexoTarefaList.add(anexoTarefaListAnexoTarefaToAttach);
            }
            tarefa.setAnexoTarefaList(attachedAnexoTarefaList);
            em.persist(tarefa);
            if (idcentrocusto != null) {
                idcentrocusto.getTarefaList().add(tarefa);
                idcentrocusto = em.merge(idcentrocusto);
            }
            if (iddepartamento != null) {
                iddepartamento.getTarefaList().add(tarefa);
                iddepartamento = em.merge(iddepartamento);
            }
            if (idempresa != null) {
                idempresa.getTarefaList().add(tarefa);
                idempresa = em.merge(idempresa);
            }
            if (idempresacliente != null) {
                idempresacliente.getTarefaList().add(tarefa);
                idempresacliente = em.merge(idempresacliente);
            }
            if (idtarefaproxima != null) {
                idtarefaproxima.getTarefaList().add(tarefa);
                idtarefaproxima = em.merge(idtarefaproxima);
            }
            if (idtarefaanterior != null) {
                idtarefaanterior.getTarefaList().add(tarefa);
                idtarefaanterior = em.merge(idtarefaanterior);
            }
            if (idtarefapai != null) {
                idtarefapai.getTarefaList().add(tarefa);
                idtarefapai = em.merge(idtarefapai);
            }
            if (idusuarioinclusao != null) {
                idusuarioinclusao.getTarefaList().add(tarefa);
                idusuarioinclusao = em.merge(idusuarioinclusao);
            }
            if (idusuariosolicitante != null) {
                idusuariosolicitante.getTarefaList().add(tarefa);
                idusuariosolicitante = em.merge(idusuariosolicitante);
            }
            if (idusuarioresponsavel != null) {
                idusuarioresponsavel.getTarefaList().add(tarefa);
                idusuarioresponsavel = em.merge(idusuarioresponsavel);
            }
            for (FavoritosTarefaMeta favoritosTarefaMetaListFavoritosTarefaMeta : tarefa.getFavoritosTarefaMetaList()) {
                Tarefa oldIdtarefaOfFavoritosTarefaMetaListFavoritosTarefaMeta = favoritosTarefaMetaListFavoritosTarefaMeta.getIdtarefa();
                favoritosTarefaMetaListFavoritosTarefaMeta.setIdtarefa(tarefa);
                favoritosTarefaMetaListFavoritosTarefaMeta = em.merge(favoritosTarefaMetaListFavoritosTarefaMeta);
                if (oldIdtarefaOfFavoritosTarefaMetaListFavoritosTarefaMeta != null) {
                    oldIdtarefaOfFavoritosTarefaMetaListFavoritosTarefaMeta.getFavoritosTarefaMetaList().remove(favoritosTarefaMetaListFavoritosTarefaMeta);
                    oldIdtarefaOfFavoritosTarefaMetaListFavoritosTarefaMeta = em.merge(oldIdtarefaOfFavoritosTarefaMetaListFavoritosTarefaMeta);
                }
            }
            for (Tarefa tarefaListTarefa : tarefa.getTarefaList()) {
                Tarefa oldIdtarefaproximaOfTarefaListTarefa = tarefaListTarefa.getIdtarefaproxima();
                tarefaListTarefa.setIdtarefaproxima(tarefa);
                tarefaListTarefa = em.merge(tarefaListTarefa);
                if (oldIdtarefaproximaOfTarefaListTarefa != null) {
                    oldIdtarefaproximaOfTarefaListTarefa.getTarefaList().remove(tarefaListTarefa);
                    oldIdtarefaproximaOfTarefaListTarefa = em.merge(oldIdtarefaproximaOfTarefaListTarefa);
                }
            }
            for (Tarefa tarefaList1Tarefa : tarefa.getTarefaList1()) {
                Tarefa oldIdtarefaanteriorOfTarefaList1Tarefa = tarefaList1Tarefa.getIdtarefaanterior();
                tarefaList1Tarefa.setIdtarefaanterior(tarefa);
                tarefaList1Tarefa = em.merge(tarefaList1Tarefa);
                if (oldIdtarefaanteriorOfTarefaList1Tarefa != null) {
                    oldIdtarefaanteriorOfTarefaList1Tarefa.getTarefaList1().remove(tarefaList1Tarefa);
                    oldIdtarefaanteriorOfTarefaList1Tarefa = em.merge(oldIdtarefaanteriorOfTarefaList1Tarefa);
                }
            }
            for (Tarefa tarefaList2Tarefa : tarefa.getTarefaList2()) {
                Tarefa oldIdtarefapaiOfTarefaList2Tarefa = tarefaList2Tarefa.getIdtarefapai();
                tarefaList2Tarefa.setIdtarefapai(tarefa);
                tarefaList2Tarefa = em.merge(tarefaList2Tarefa);
                if (oldIdtarefapaiOfTarefaList2Tarefa != null) {
                    oldIdtarefapaiOfTarefaList2Tarefa.getTarefaList2().remove(tarefaList2Tarefa);
                    oldIdtarefapaiOfTarefaList2Tarefa = em.merge(oldIdtarefapaiOfTarefaList2Tarefa);
                }
            }
            for (ParicipanteTarefa paricipanteTarefaListParicipanteTarefa : tarefa.getParicipanteTarefaList()) {
                Tarefa oldIdtarefaOfParicipanteTarefaListParicipanteTarefa = paricipanteTarefaListParicipanteTarefa.getIdtarefa();
                paricipanteTarefaListParicipanteTarefa.setIdtarefa(tarefa);
                paricipanteTarefaListParicipanteTarefa = em.merge(paricipanteTarefaListParicipanteTarefa);
                if (oldIdtarefaOfParicipanteTarefaListParicipanteTarefa != null) {
                    oldIdtarefaOfParicipanteTarefaListParicipanteTarefa.getParicipanteTarefaList().remove(paricipanteTarefaListParicipanteTarefa);
                    oldIdtarefaOfParicipanteTarefaListParicipanteTarefa = em.merge(oldIdtarefaOfParicipanteTarefaListParicipanteTarefa);
                }
            }
            for (AvaliacaoMetaTarefa avaliacaoMetaTarefaListAvaliacaoMetaTarefa : tarefa.getAvaliacaoMetaTarefaList()) {
                Tarefa oldIdtarefaOfAvaliacaoMetaTarefaListAvaliacaoMetaTarefa = avaliacaoMetaTarefaListAvaliacaoMetaTarefa.getIdtarefa();
                avaliacaoMetaTarefaListAvaliacaoMetaTarefa.setIdtarefa(tarefa);
                avaliacaoMetaTarefaListAvaliacaoMetaTarefa = em.merge(avaliacaoMetaTarefaListAvaliacaoMetaTarefa);
                if (oldIdtarefaOfAvaliacaoMetaTarefaListAvaliacaoMetaTarefa != null) {
                    oldIdtarefaOfAvaliacaoMetaTarefaListAvaliacaoMetaTarefa.getAvaliacaoMetaTarefaList().remove(avaliacaoMetaTarefaListAvaliacaoMetaTarefa);
                    oldIdtarefaOfAvaliacaoMetaTarefaListAvaliacaoMetaTarefa = em.merge(oldIdtarefaOfAvaliacaoMetaTarefaListAvaliacaoMetaTarefa);
                }
            }
            for (OrcamentoTarefa orcamentoTarefaListOrcamentoTarefa : tarefa.getOrcamentoTarefaList()) {
                Tarefa oldIdtarefaOfOrcamentoTarefaListOrcamentoTarefa = orcamentoTarefaListOrcamentoTarefa.getIdtarefa();
                orcamentoTarefaListOrcamentoTarefa.setIdtarefa(tarefa);
                orcamentoTarefaListOrcamentoTarefa = em.merge(orcamentoTarefaListOrcamentoTarefa);
                if (oldIdtarefaOfOrcamentoTarefaListOrcamentoTarefa != null) {
                    oldIdtarefaOfOrcamentoTarefaListOrcamentoTarefa.getOrcamentoTarefaList().remove(orcamentoTarefaListOrcamentoTarefa);
                    oldIdtarefaOfOrcamentoTarefaListOrcamentoTarefa = em.merge(oldIdtarefaOfOrcamentoTarefaListOrcamentoTarefa);
                }
            }
            for (ApontamentoTarefa apontamentoTarefaListApontamentoTarefa : tarefa.getApontamentoTarefaList()) {
                Tarefa oldIdtarefaOfApontamentoTarefaListApontamentoTarefa = apontamentoTarefaListApontamentoTarefa.getIdTarefa();
                apontamentoTarefaListApontamentoTarefa.setIdTarefa(tarefa);
                apontamentoTarefaListApontamentoTarefa = em.merge(apontamentoTarefaListApontamentoTarefa);
                if (oldIdtarefaOfApontamentoTarefaListApontamentoTarefa != null) {
                    oldIdtarefaOfApontamentoTarefaListApontamentoTarefa.getApontamentoTarefaList().remove(apontamentoTarefaListApontamentoTarefa);
                    oldIdtarefaOfApontamentoTarefaListApontamentoTarefa = em.merge(oldIdtarefaOfApontamentoTarefaListApontamentoTarefa);
                }
            }
            for (AnexoTarefa anexoTarefaListAnexoTarefa : tarefa.getAnexoTarefaList()) {
                Tarefa oldIdtarefaOfAnexoTarefaListAnexoTarefa = anexoTarefaListAnexoTarefa.getIdTarefa();
                anexoTarefaListAnexoTarefa.setIdTarefa(tarefa);
                anexoTarefaListAnexoTarefa = em.merge(anexoTarefaListAnexoTarefa);
                if (oldIdtarefaOfAnexoTarefaListAnexoTarefa != null) {
                    oldIdtarefaOfAnexoTarefaListAnexoTarefa.getAnexoTarefaList().remove(anexoTarefaListAnexoTarefa);
                    oldIdtarefaOfAnexoTarefaListAnexoTarefa = em.merge(oldIdtarefaOfAnexoTarefaListAnexoTarefa);
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
            CentroCusto idcentrocustoOld = persistentTarefa.getIdcentrocusto();
            CentroCusto idcentrocustoNew = tarefa.getIdcentrocusto();
            Departamento iddepartamentoOld = persistentTarefa.getIddepartamento();
            Departamento iddepartamentoNew = tarefa.getIddepartamento();
            Empresa idempresaOld = persistentTarefa.getIdempresa();
            Empresa idempresaNew = tarefa.getIdempresa();
            EmpresaCliente idempresaclienteOld = persistentTarefa.getIdempresacliente();
            EmpresaCliente idempresaclienteNew = tarefa.getIdempresacliente();
            Tarefa idtarefaproximaOld = persistentTarefa.getIdtarefaproxima();
            Tarefa idtarefaproximaNew = tarefa.getIdtarefaproxima();
            Tarefa idtarefaanteriorOld = persistentTarefa.getIdtarefaanterior();
            Tarefa idtarefaanteriorNew = tarefa.getIdtarefaanterior();
            Tarefa idtarefapaiOld = persistentTarefa.getIdtarefapai();
            Tarefa idtarefapaiNew = tarefa.getIdtarefapai();
            Usuario idusuarioinclusaoOld = persistentTarefa.getIdusuarioinclusao();
            Usuario idusuarioinclusaoNew = tarefa.getIdusuarioinclusao();
            Usuario idusuariosolicitanteOld = persistentTarefa.getIdusuariosolicitante();
            Usuario idusuariosolicitanteNew = tarefa.getIdusuariosolicitante();
            Usuario idusuarioresponsavelOld = persistentTarefa.getIdusuarioresponsavel();
            Usuario idusuarioresponsavelNew = tarefa.getIdusuarioresponsavel();
            List<FavoritosTarefaMeta> favoritosTarefaMetaListOld = persistentTarefa.getFavoritosTarefaMetaList();
            List<FavoritosTarefaMeta> favoritosTarefaMetaListNew = tarefa.getFavoritosTarefaMetaList();
            List<Tarefa> tarefaListOld = persistentTarefa.getTarefaList();
            List<Tarefa> tarefaListNew = tarefa.getTarefaList();
            List<Tarefa> tarefaList1Old = persistentTarefa.getTarefaList1();
            List<Tarefa> tarefaList1New = tarefa.getTarefaList1();
            List<Tarefa> tarefaList2Old = persistentTarefa.getTarefaList2();
            List<Tarefa> tarefaList2New = tarefa.getTarefaList2();
            List<ParicipanteTarefa> paricipanteTarefaListOld = persistentTarefa.getParicipanteTarefaList();
            List<ParicipanteTarefa> paricipanteTarefaListNew = tarefa.getParicipanteTarefaList();
            List<AvaliacaoMetaTarefa> avaliacaoMetaTarefaListOld = persistentTarefa.getAvaliacaoMetaTarefaList();
            List<AvaliacaoMetaTarefa> avaliacaoMetaTarefaListNew = tarefa.getAvaliacaoMetaTarefaList();
            List<OrcamentoTarefa> orcamentoTarefaListOld = persistentTarefa.getOrcamentoTarefaList();
            List<OrcamentoTarefa> orcamentoTarefaListNew = tarefa.getOrcamentoTarefaList();
            List<ApontamentoTarefa> apontamentoTarefaListOld = persistentTarefa.getApontamentoTarefaList();
            List<ApontamentoTarefa> apontamentoTarefaListNew = tarefa.getApontamentoTarefaList();
            List<AnexoTarefa> anexoTarefaListOld = persistentTarefa.getAnexoTarefaList();
            List<AnexoTarefa> anexoTarefaListNew = tarefa.getAnexoTarefaList();
            List<String> illegalOrphanMessages = null;
            for (FavoritosTarefaMeta favoritosTarefaMetaListOldFavoritosTarefaMeta : favoritosTarefaMetaListOld) {
                if (!favoritosTarefaMetaListNew.contains(favoritosTarefaMetaListOldFavoritosTarefaMeta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain FavoritosTarefaMeta " + favoritosTarefaMetaListOldFavoritosTarefaMeta + " since its idtarefa field is not nullable.");
                }
            }
            for (Tarefa tarefaListOldTarefa : tarefaListOld) {
                if (!tarefaListNew.contains(tarefaListOldTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tarefa " + tarefaListOldTarefa + " since its idtarefaproxima field is not nullable.");
                }
            }
            for (Tarefa tarefaList1OldTarefa : tarefaList1Old) {
                if (!tarefaList1New.contains(tarefaList1OldTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tarefa " + tarefaList1OldTarefa + " since its idtarefaanterior field is not nullable.");
                }
            }
            for (ParicipanteTarefa paricipanteTarefaListOldParicipanteTarefa : paricipanteTarefaListOld) {
                if (!paricipanteTarefaListNew.contains(paricipanteTarefaListOldParicipanteTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ParicipanteTarefa " + paricipanteTarefaListOldParicipanteTarefa + " since its idtarefa field is not nullable.");
                }
            }
            for (AvaliacaoMetaTarefa avaliacaoMetaTarefaListOldAvaliacaoMetaTarefa : avaliacaoMetaTarefaListOld) {
                if (!avaliacaoMetaTarefaListNew.contains(avaliacaoMetaTarefaListOldAvaliacaoMetaTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AvaliacaoMetaTarefa " + avaliacaoMetaTarefaListOldAvaliacaoMetaTarefa + " since its idtarefa field is not nullable.");
                }
            }
            for (OrcamentoTarefa orcamentoTarefaListOldOrcamentoTarefa : orcamentoTarefaListOld) {
                if (!orcamentoTarefaListNew.contains(orcamentoTarefaListOldOrcamentoTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain OrcamentoTarefa " + orcamentoTarefaListOldOrcamentoTarefa + " since its idtarefa field is not nullable.");
                }
            }
            for (ApontamentoTarefa apontamentoTarefaListOldApontamentoTarefa : apontamentoTarefaListOld) {
                if (!apontamentoTarefaListNew.contains(apontamentoTarefaListOldApontamentoTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ApontamentoTarefa " + apontamentoTarefaListOldApontamentoTarefa + " since its idtarefa field is not nullable.");
                }
            }
            for (AnexoTarefa anexoTarefaListOldAnexoTarefa : anexoTarefaListOld) {
                if (!anexoTarefaListNew.contains(anexoTarefaListOldAnexoTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AnexoTarefa " + anexoTarefaListOldAnexoTarefa + " since its idtarefa field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idcentrocustoNew != null) {
                idcentrocustoNew = em.getReference(idcentrocustoNew.getClass(), idcentrocustoNew.getId());
                tarefa.setIdcentrocusto(idcentrocustoNew);
            }
            if (iddepartamentoNew != null) {
                iddepartamentoNew = em.getReference(iddepartamentoNew.getClass(), iddepartamentoNew.getId());
                tarefa.setIddepartamento(iddepartamentoNew);
            }
            if (idempresaNew != null) {
                idempresaNew = em.getReference(idempresaNew.getClass(), idempresaNew.getId());
                tarefa.setIdempresa(idempresaNew);
            }
            if (idempresaclienteNew != null) {
                idempresaclienteNew = em.getReference(idempresaclienteNew.getClass(), idempresaclienteNew.getId());
                tarefa.setIdempresacliente(idempresaclienteNew);
            }
            if (idtarefaproximaNew != null) {
                idtarefaproximaNew = em.getReference(idtarefaproximaNew.getClass(), idtarefaproximaNew.getId());
                tarefa.setIdtarefaproxima(idtarefaproximaNew);
            }
            if (idtarefaanteriorNew != null) {
                idtarefaanteriorNew = em.getReference(idtarefaanteriorNew.getClass(), idtarefaanteriorNew.getId());
                tarefa.setIdtarefaanterior(idtarefaanteriorNew);
            }
            if (idtarefapaiNew != null) {
                idtarefapaiNew = em.getReference(idtarefapaiNew.getClass(), idtarefapaiNew.getId());
                tarefa.setIdtarefapai(idtarefapaiNew);
            }
            if (idusuarioinclusaoNew != null) {
                idusuarioinclusaoNew = em.getReference(idusuarioinclusaoNew.getClass(), idusuarioinclusaoNew.getId());
                tarefa.setIdusuarioinclusao(idusuarioinclusaoNew);
            }
            if (idusuariosolicitanteNew != null) {
                idusuariosolicitanteNew = em.getReference(idusuariosolicitanteNew.getClass(), idusuariosolicitanteNew.getId());
                tarefa.setIdusuariosolicitante(idusuariosolicitanteNew);
            }
            if (idusuarioresponsavelNew != null) {
                idusuarioresponsavelNew = em.getReference(idusuarioresponsavelNew.getClass(), idusuarioresponsavelNew.getId());
                tarefa.setIdusuarioresponsavel(idusuarioresponsavelNew);
            }
            List<FavoritosTarefaMeta> attachedFavoritosTarefaMetaListNew = new ArrayList<FavoritosTarefaMeta>();
            for (FavoritosTarefaMeta favoritosTarefaMetaListNewFavoritosTarefaMetaToAttach : favoritosTarefaMetaListNew) {
                favoritosTarefaMetaListNewFavoritosTarefaMetaToAttach = em.getReference(favoritosTarefaMetaListNewFavoritosTarefaMetaToAttach.getClass(), favoritosTarefaMetaListNewFavoritosTarefaMetaToAttach.getId());
                attachedFavoritosTarefaMetaListNew.add(favoritosTarefaMetaListNewFavoritosTarefaMetaToAttach);
            }
            favoritosTarefaMetaListNew = attachedFavoritosTarefaMetaListNew;
            tarefa.setFavoritosTarefaMetaList(favoritosTarefaMetaListNew);
            List<Tarefa> attachedTarefaListNew = new ArrayList<Tarefa>();
            for (Tarefa tarefaListNewTarefaToAttach : tarefaListNew) {
                tarefaListNewTarefaToAttach = em.getReference(tarefaListNewTarefaToAttach.getClass(), tarefaListNewTarefaToAttach.getId());
                attachedTarefaListNew.add(tarefaListNewTarefaToAttach);
            }
            tarefaListNew = attachedTarefaListNew;
            tarefa.setTarefaList(tarefaListNew);
            List<Tarefa> attachedTarefaList1New = new ArrayList<Tarefa>();
            for (Tarefa tarefaList1NewTarefaToAttach : tarefaList1New) {
                tarefaList1NewTarefaToAttach = em.getReference(tarefaList1NewTarefaToAttach.getClass(), tarefaList1NewTarefaToAttach.getId());
                attachedTarefaList1New.add(tarefaList1NewTarefaToAttach);
            }
            tarefaList1New = attachedTarefaList1New;
            tarefa.setTarefaList1(tarefaList1New);
            List<Tarefa> attachedTarefaList2New = new ArrayList<Tarefa>();
            for (Tarefa tarefaList2NewTarefaToAttach : tarefaList2New) {
                tarefaList2NewTarefaToAttach = em.getReference(tarefaList2NewTarefaToAttach.getClass(), tarefaList2NewTarefaToAttach.getId());
                attachedTarefaList2New.add(tarefaList2NewTarefaToAttach);
            }
            tarefaList2New = attachedTarefaList2New;
            tarefa.setTarefaList2(tarefaList2New);
            List<ParicipanteTarefa> attachedParicipanteTarefaListNew = new ArrayList<ParicipanteTarefa>();
            for (ParicipanteTarefa paricipanteTarefaListNewParicipanteTarefaToAttach : paricipanteTarefaListNew) {
                paricipanteTarefaListNewParicipanteTarefaToAttach = em.getReference(paricipanteTarefaListNewParicipanteTarefaToAttach.getClass(), paricipanteTarefaListNewParicipanteTarefaToAttach.getId());
                attachedParicipanteTarefaListNew.add(paricipanteTarefaListNewParicipanteTarefaToAttach);
            }
            paricipanteTarefaListNew = attachedParicipanteTarefaListNew;
            tarefa.setParicipanteTarefaList(paricipanteTarefaListNew);
            List<AvaliacaoMetaTarefa> attachedAvaliacaoMetaTarefaListNew = new ArrayList<AvaliacaoMetaTarefa>();
            for (AvaliacaoMetaTarefa avaliacaoMetaTarefaListNewAvaliacaoMetaTarefaToAttach : avaliacaoMetaTarefaListNew) {
                avaliacaoMetaTarefaListNewAvaliacaoMetaTarefaToAttach = em.getReference(avaliacaoMetaTarefaListNewAvaliacaoMetaTarefaToAttach.getClass(), avaliacaoMetaTarefaListNewAvaliacaoMetaTarefaToAttach.getId());
                attachedAvaliacaoMetaTarefaListNew.add(avaliacaoMetaTarefaListNewAvaliacaoMetaTarefaToAttach);
            }
            avaliacaoMetaTarefaListNew = attachedAvaliacaoMetaTarefaListNew;
            tarefa.setAvaliacaoMetaTarefaList(avaliacaoMetaTarefaListNew);
            List<OrcamentoTarefa> attachedOrcamentoTarefaListNew = new ArrayList<OrcamentoTarefa>();
            for (OrcamentoTarefa orcamentoTarefaListNewOrcamentoTarefaToAttach : orcamentoTarefaListNew) {
                orcamentoTarefaListNewOrcamentoTarefaToAttach = em.getReference(orcamentoTarefaListNewOrcamentoTarefaToAttach.getClass(), orcamentoTarefaListNewOrcamentoTarefaToAttach.getId());
                attachedOrcamentoTarefaListNew.add(orcamentoTarefaListNewOrcamentoTarefaToAttach);
            }
            orcamentoTarefaListNew = attachedOrcamentoTarefaListNew;
            tarefa.setOrcamentoTarefaList(orcamentoTarefaListNew);
            List<ApontamentoTarefa> attachedApontamentoTarefaListNew = new ArrayList<ApontamentoTarefa>();
            for (ApontamentoTarefa apontamentoTarefaListNewApontamentoTarefaToAttach : apontamentoTarefaListNew) {
                apontamentoTarefaListNewApontamentoTarefaToAttach = em.getReference(apontamentoTarefaListNewApontamentoTarefaToAttach.getClass(), apontamentoTarefaListNewApontamentoTarefaToAttach.getId());
                attachedApontamentoTarefaListNew.add(apontamentoTarefaListNewApontamentoTarefaToAttach);
            }
            apontamentoTarefaListNew = attachedApontamentoTarefaListNew;
            tarefa.setApontamentoTarefaList(apontamentoTarefaListNew);
            List<AnexoTarefa> attachedAnexoTarefaListNew = new ArrayList<AnexoTarefa>();
            for (AnexoTarefa anexoTarefaListNewAnexoTarefaToAttach : anexoTarefaListNew) {
                anexoTarefaListNewAnexoTarefaToAttach = em.getReference(anexoTarefaListNewAnexoTarefaToAttach.getClass(), anexoTarefaListNewAnexoTarefaToAttach.getId());
                attachedAnexoTarefaListNew.add(anexoTarefaListNewAnexoTarefaToAttach);
            }
            anexoTarefaListNew = attachedAnexoTarefaListNew;
            tarefa.setAnexoTarefaList(anexoTarefaListNew);
            tarefa = em.merge(tarefa);
            if (idcentrocustoOld != null && !idcentrocustoOld.equals(idcentrocustoNew)) {
                idcentrocustoOld.getTarefaList().remove(tarefa);
                idcentrocustoOld = em.merge(idcentrocustoOld);
            }
            if (idcentrocustoNew != null && !idcentrocustoNew.equals(idcentrocustoOld)) {
                idcentrocustoNew.getTarefaList().add(tarefa);
                idcentrocustoNew = em.merge(idcentrocustoNew);
            }
            if (iddepartamentoOld != null && !iddepartamentoOld.equals(iddepartamentoNew)) {
                iddepartamentoOld.getTarefaList().remove(tarefa);
                iddepartamentoOld = em.merge(iddepartamentoOld);
            }
            if (iddepartamentoNew != null && !iddepartamentoNew.equals(iddepartamentoOld)) {
                iddepartamentoNew.getTarefaList().add(tarefa);
                iddepartamentoNew = em.merge(iddepartamentoNew);
            }
            if (idempresaOld != null && !idempresaOld.equals(idempresaNew)) {
                idempresaOld.getTarefaList().remove(tarefa);
                idempresaOld = em.merge(idempresaOld);
            }
            if (idempresaNew != null && !idempresaNew.equals(idempresaOld)) {
                idempresaNew.getTarefaList().add(tarefa);
                idempresaNew = em.merge(idempresaNew);
            }
            if (idempresaclienteOld != null && !idempresaclienteOld.equals(idempresaclienteNew)) {
                idempresaclienteOld.getTarefaList().remove(tarefa);
                idempresaclienteOld = em.merge(idempresaclienteOld);
            }
            if (idempresaclienteNew != null && !idempresaclienteNew.equals(idempresaclienteOld)) {
                idempresaclienteNew.getTarefaList().add(tarefa);
                idempresaclienteNew = em.merge(idempresaclienteNew);
            }
            if (idtarefaproximaOld != null && !idtarefaproximaOld.equals(idtarefaproximaNew)) {
                idtarefaproximaOld.getTarefaList().remove(tarefa);
                idtarefaproximaOld = em.merge(idtarefaproximaOld);
            }
            if (idtarefaproximaNew != null && !idtarefaproximaNew.equals(idtarefaproximaOld)) {
                idtarefaproximaNew.getTarefaList().add(tarefa);
                idtarefaproximaNew = em.merge(idtarefaproximaNew);
            }
            if (idtarefaanteriorOld != null && !idtarefaanteriorOld.equals(idtarefaanteriorNew)) {
                idtarefaanteriorOld.getTarefaList().remove(tarefa);
                idtarefaanteriorOld = em.merge(idtarefaanteriorOld);
            }
            if (idtarefaanteriorNew != null && !idtarefaanteriorNew.equals(idtarefaanteriorOld)) {
                idtarefaanteriorNew.getTarefaList().add(tarefa);
                idtarefaanteriorNew = em.merge(idtarefaanteriorNew);
            }
            if (idtarefapaiOld != null && !idtarefapaiOld.equals(idtarefapaiNew)) {
                idtarefapaiOld.getTarefaList().remove(tarefa);
                idtarefapaiOld = em.merge(idtarefapaiOld);
            }
            if (idtarefapaiNew != null && !idtarefapaiNew.equals(idtarefapaiOld)) {
                idtarefapaiNew.getTarefaList().add(tarefa);
                idtarefapaiNew = em.merge(idtarefapaiNew);
            }
            if (idusuarioinclusaoOld != null && !idusuarioinclusaoOld.equals(idusuarioinclusaoNew)) {
                idusuarioinclusaoOld.getTarefaList().remove(tarefa);
                idusuarioinclusaoOld = em.merge(idusuarioinclusaoOld);
            }
            if (idusuarioinclusaoNew != null && !idusuarioinclusaoNew.equals(idusuarioinclusaoOld)) {
                idusuarioinclusaoNew.getTarefaList().add(tarefa);
                idusuarioinclusaoNew = em.merge(idusuarioinclusaoNew);
            }
            if (idusuariosolicitanteOld != null && !idusuariosolicitanteOld.equals(idusuariosolicitanteNew)) {
                idusuariosolicitanteOld.getTarefaList().remove(tarefa);
                idusuariosolicitanteOld = em.merge(idusuariosolicitanteOld);
            }
            if (idusuariosolicitanteNew != null && !idusuariosolicitanteNew.equals(idusuariosolicitanteOld)) {
                idusuariosolicitanteNew.getTarefaList().add(tarefa);
                idusuariosolicitanteNew = em.merge(idusuariosolicitanteNew);
            }
            if (idusuarioresponsavelOld != null && !idusuarioresponsavelOld.equals(idusuarioresponsavelNew)) {
                idusuarioresponsavelOld.getTarefaList().remove(tarefa);
                idusuarioresponsavelOld = em.merge(idusuarioresponsavelOld);
            }
            if (idusuarioresponsavelNew != null && !idusuarioresponsavelNew.equals(idusuarioresponsavelOld)) {
                idusuarioresponsavelNew.getTarefaList().add(tarefa);
                idusuarioresponsavelNew = em.merge(idusuarioresponsavelNew);
            }
            for (FavoritosTarefaMeta favoritosTarefaMetaListNewFavoritosTarefaMeta : favoritosTarefaMetaListNew) {
                if (!favoritosTarefaMetaListOld.contains(favoritosTarefaMetaListNewFavoritosTarefaMeta)) {
                    Tarefa oldIdtarefaOfFavoritosTarefaMetaListNewFavoritosTarefaMeta = favoritosTarefaMetaListNewFavoritosTarefaMeta.getIdtarefa();
                    favoritosTarefaMetaListNewFavoritosTarefaMeta.setIdtarefa(tarefa);
                    favoritosTarefaMetaListNewFavoritosTarefaMeta = em.merge(favoritosTarefaMetaListNewFavoritosTarefaMeta);
                    if (oldIdtarefaOfFavoritosTarefaMetaListNewFavoritosTarefaMeta != null && !oldIdtarefaOfFavoritosTarefaMetaListNewFavoritosTarefaMeta.equals(tarefa)) {
                        oldIdtarefaOfFavoritosTarefaMetaListNewFavoritosTarefaMeta.getFavoritosTarefaMetaList().remove(favoritosTarefaMetaListNewFavoritosTarefaMeta);
                        oldIdtarefaOfFavoritosTarefaMetaListNewFavoritosTarefaMeta = em.merge(oldIdtarefaOfFavoritosTarefaMetaListNewFavoritosTarefaMeta);
                    }
                }
            }
            for (Tarefa tarefaListNewTarefa : tarefaListNew) {
                if (!tarefaListOld.contains(tarefaListNewTarefa)) {
                    Tarefa oldIdtarefaproximaOfTarefaListNewTarefa = tarefaListNewTarefa.getIdtarefaproxima();
                    tarefaListNewTarefa.setIdtarefaproxima(tarefa);
                    tarefaListNewTarefa = em.merge(tarefaListNewTarefa);
                    if (oldIdtarefaproximaOfTarefaListNewTarefa != null && !oldIdtarefaproximaOfTarefaListNewTarefa.equals(tarefa)) {
                        oldIdtarefaproximaOfTarefaListNewTarefa.getTarefaList().remove(tarefaListNewTarefa);
                        oldIdtarefaproximaOfTarefaListNewTarefa = em.merge(oldIdtarefaproximaOfTarefaListNewTarefa);
                    }
                }
            }
            for (Tarefa tarefaList1NewTarefa : tarefaList1New) {
                if (!tarefaList1Old.contains(tarefaList1NewTarefa)) {
                    Tarefa oldIdtarefaanteriorOfTarefaList1NewTarefa = tarefaList1NewTarefa.getIdtarefaanterior();
                    tarefaList1NewTarefa.setIdtarefaanterior(tarefa);
                    tarefaList1NewTarefa = em.merge(tarefaList1NewTarefa);
                    if (oldIdtarefaanteriorOfTarefaList1NewTarefa != null && !oldIdtarefaanteriorOfTarefaList1NewTarefa.equals(tarefa)) {
                        oldIdtarefaanteriorOfTarefaList1NewTarefa.getTarefaList1().remove(tarefaList1NewTarefa);
                        oldIdtarefaanteriorOfTarefaList1NewTarefa = em.merge(oldIdtarefaanteriorOfTarefaList1NewTarefa);
                    }
                }
            }
            for (Tarefa tarefaList2OldTarefa : tarefaList2Old) {
                if (!tarefaList2New.contains(tarefaList2OldTarefa)) {
                    tarefaList2OldTarefa.setIdtarefapai(null);
                    tarefaList2OldTarefa = em.merge(tarefaList2OldTarefa);
                }
            }
            for (Tarefa tarefaList2NewTarefa : tarefaList2New) {
                if (!tarefaList2Old.contains(tarefaList2NewTarefa)) {
                    Tarefa oldIdtarefapaiOfTarefaList2NewTarefa = tarefaList2NewTarefa.getIdtarefapai();
                    tarefaList2NewTarefa.setIdtarefapai(tarefa);
                    tarefaList2NewTarefa = em.merge(tarefaList2NewTarefa);
                    if (oldIdtarefapaiOfTarefaList2NewTarefa != null && !oldIdtarefapaiOfTarefaList2NewTarefa.equals(tarefa)) {
                        oldIdtarefapaiOfTarefaList2NewTarefa.getTarefaList2().remove(tarefaList2NewTarefa);
                        oldIdtarefapaiOfTarefaList2NewTarefa = em.merge(oldIdtarefapaiOfTarefaList2NewTarefa);
                    }
                }
            }
            for (ParicipanteTarefa paricipanteTarefaListNewParicipanteTarefa : paricipanteTarefaListNew) {
                if (!paricipanteTarefaListOld.contains(paricipanteTarefaListNewParicipanteTarefa)) {
                    Tarefa oldIdtarefaOfParicipanteTarefaListNewParicipanteTarefa = paricipanteTarefaListNewParicipanteTarefa.getIdtarefa();
                    paricipanteTarefaListNewParicipanteTarefa.setIdtarefa(tarefa);
                    paricipanteTarefaListNewParicipanteTarefa = em.merge(paricipanteTarefaListNewParicipanteTarefa);
                    if (oldIdtarefaOfParicipanteTarefaListNewParicipanteTarefa != null && !oldIdtarefaOfParicipanteTarefaListNewParicipanteTarefa.equals(tarefa)) {
                        oldIdtarefaOfParicipanteTarefaListNewParicipanteTarefa.getParicipanteTarefaList().remove(paricipanteTarefaListNewParicipanteTarefa);
                        oldIdtarefaOfParicipanteTarefaListNewParicipanteTarefa = em.merge(oldIdtarefaOfParicipanteTarefaListNewParicipanteTarefa);
                    }
                }
            }
            for (AvaliacaoMetaTarefa avaliacaoMetaTarefaListNewAvaliacaoMetaTarefa : avaliacaoMetaTarefaListNew) {
                if (!avaliacaoMetaTarefaListOld.contains(avaliacaoMetaTarefaListNewAvaliacaoMetaTarefa)) {
                    Tarefa oldIdtarefaOfAvaliacaoMetaTarefaListNewAvaliacaoMetaTarefa = avaliacaoMetaTarefaListNewAvaliacaoMetaTarefa.getIdtarefa();
                    avaliacaoMetaTarefaListNewAvaliacaoMetaTarefa.setIdtarefa(tarefa);
                    avaliacaoMetaTarefaListNewAvaliacaoMetaTarefa = em.merge(avaliacaoMetaTarefaListNewAvaliacaoMetaTarefa);
                    if (oldIdtarefaOfAvaliacaoMetaTarefaListNewAvaliacaoMetaTarefa != null && !oldIdtarefaOfAvaliacaoMetaTarefaListNewAvaliacaoMetaTarefa.equals(tarefa)) {
                        oldIdtarefaOfAvaliacaoMetaTarefaListNewAvaliacaoMetaTarefa.getAvaliacaoMetaTarefaList().remove(avaliacaoMetaTarefaListNewAvaliacaoMetaTarefa);
                        oldIdtarefaOfAvaliacaoMetaTarefaListNewAvaliacaoMetaTarefa = em.merge(oldIdtarefaOfAvaliacaoMetaTarefaListNewAvaliacaoMetaTarefa);
                    }
                }
            }
            for (OrcamentoTarefa orcamentoTarefaListNewOrcamentoTarefa : orcamentoTarefaListNew) {
                if (!orcamentoTarefaListOld.contains(orcamentoTarefaListNewOrcamentoTarefa)) {
                    Tarefa oldIdtarefaOfOrcamentoTarefaListNewOrcamentoTarefa = orcamentoTarefaListNewOrcamentoTarefa.getIdtarefa();
                    orcamentoTarefaListNewOrcamentoTarefa.setIdtarefa(tarefa);
                    orcamentoTarefaListNewOrcamentoTarefa = em.merge(orcamentoTarefaListNewOrcamentoTarefa);
                    if (oldIdtarefaOfOrcamentoTarefaListNewOrcamentoTarefa != null && !oldIdtarefaOfOrcamentoTarefaListNewOrcamentoTarefa.equals(tarefa)) {
                        oldIdtarefaOfOrcamentoTarefaListNewOrcamentoTarefa.getOrcamentoTarefaList().remove(orcamentoTarefaListNewOrcamentoTarefa);
                        oldIdtarefaOfOrcamentoTarefaListNewOrcamentoTarefa = em.merge(oldIdtarefaOfOrcamentoTarefaListNewOrcamentoTarefa);
                    }
                }
            }
            for (ApontamentoTarefa apontamentoTarefaListNewApontamentoTarefa : apontamentoTarefaListNew) {
                if (!apontamentoTarefaListOld.contains(apontamentoTarefaListNewApontamentoTarefa)) {
                    Tarefa oldIdtarefaOfApontamentoTarefaListNewApontamentoTarefa = apontamentoTarefaListNewApontamentoTarefa.getIdTarefa();
                    apontamentoTarefaListNewApontamentoTarefa.setIdTarefa(tarefa);
                    apontamentoTarefaListNewApontamentoTarefa = em.merge(apontamentoTarefaListNewApontamentoTarefa);
                    if (oldIdtarefaOfApontamentoTarefaListNewApontamentoTarefa != null && !oldIdtarefaOfApontamentoTarefaListNewApontamentoTarefa.equals(tarefa)) {
                        oldIdtarefaOfApontamentoTarefaListNewApontamentoTarefa.getApontamentoTarefaList().remove(apontamentoTarefaListNewApontamentoTarefa);
                        oldIdtarefaOfApontamentoTarefaListNewApontamentoTarefa = em.merge(oldIdtarefaOfApontamentoTarefaListNewApontamentoTarefa);
                    }
                }
            }
            for (AnexoTarefa anexoTarefaListNewAnexoTarefa : anexoTarefaListNew) {
                if (!anexoTarefaListOld.contains(anexoTarefaListNewAnexoTarefa)) {
                    Tarefa oldIdtarefaOfAnexoTarefaListNewAnexoTarefa = anexoTarefaListNewAnexoTarefa.getIdTarefa();
                    anexoTarefaListNewAnexoTarefa.setIdTarefa(tarefa);
                    anexoTarefaListNewAnexoTarefa = em.merge(anexoTarefaListNewAnexoTarefa);
                    if (oldIdtarefaOfAnexoTarefaListNewAnexoTarefa != null && !oldIdtarefaOfAnexoTarefaListNewAnexoTarefa.equals(tarefa)) {
                        oldIdtarefaOfAnexoTarefaListNewAnexoTarefa.getAnexoTarefaList().remove(anexoTarefaListNewAnexoTarefa);
                        oldIdtarefaOfAnexoTarefaListNewAnexoTarefa = em.merge(oldIdtarefaOfAnexoTarefaListNewAnexoTarefa);
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
            List<FavoritosTarefaMeta> favoritosTarefaMetaListOrphanCheck = tarefa.getFavoritosTarefaMetaList();
            for (FavoritosTarefaMeta favoritosTarefaMetaListOrphanCheckFavoritosTarefaMeta : favoritosTarefaMetaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tarefa (" + tarefa + ") cannot be destroyed since the FavoritosTarefaMeta " + favoritosTarefaMetaListOrphanCheckFavoritosTarefaMeta + " in its favoritosTarefaMetaList field has a non-nullable idtarefa field.");
            }
            List<Tarefa> tarefaListOrphanCheck = tarefa.getTarefaList();
            for (Tarefa tarefaListOrphanCheckTarefa : tarefaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tarefa (" + tarefa + ") cannot be destroyed since the Tarefa " + tarefaListOrphanCheckTarefa + " in its tarefaList field has a non-nullable idtarefaproxima field.");
            }
            List<Tarefa> tarefaList1OrphanCheck = tarefa.getTarefaList1();
            for (Tarefa tarefaList1OrphanCheckTarefa : tarefaList1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tarefa (" + tarefa + ") cannot be destroyed since the Tarefa " + tarefaList1OrphanCheckTarefa + " in its tarefaList1 field has a non-nullable idtarefaanterior field.");
            }
            List<ParicipanteTarefa> paricipanteTarefaListOrphanCheck = tarefa.getParicipanteTarefaList();
            for (ParicipanteTarefa paricipanteTarefaListOrphanCheckParicipanteTarefa : paricipanteTarefaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tarefa (" + tarefa + ") cannot be destroyed since the ParicipanteTarefa " + paricipanteTarefaListOrphanCheckParicipanteTarefa + " in its paricipanteTarefaList field has a non-nullable idtarefa field.");
            }
            List<AvaliacaoMetaTarefa> avaliacaoMetaTarefaListOrphanCheck = tarefa.getAvaliacaoMetaTarefaList();
            for (AvaliacaoMetaTarefa avaliacaoMetaTarefaListOrphanCheckAvaliacaoMetaTarefa : avaliacaoMetaTarefaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tarefa (" + tarefa + ") cannot be destroyed since the AvaliacaoMetaTarefa " + avaliacaoMetaTarefaListOrphanCheckAvaliacaoMetaTarefa + " in its avaliacaoMetaTarefaList field has a non-nullable idtarefa field.");
            }
            List<OrcamentoTarefa> orcamentoTarefaListOrphanCheck = tarefa.getOrcamentoTarefaList();
            for (OrcamentoTarefa orcamentoTarefaListOrphanCheckOrcamentoTarefa : orcamentoTarefaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tarefa (" + tarefa + ") cannot be destroyed since the OrcamentoTarefa " + orcamentoTarefaListOrphanCheckOrcamentoTarefa + " in its orcamentoTarefaList field has a non-nullable idtarefa field.");
            }
            List<ApontamentoTarefa> apontamentoTarefaListOrphanCheck = tarefa.getApontamentoTarefaList();
            for (ApontamentoTarefa apontamentoTarefaListOrphanCheckApontamentoTarefa : apontamentoTarefaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tarefa (" + tarefa + ") cannot be destroyed since the ApontamentoTarefa " + apontamentoTarefaListOrphanCheckApontamentoTarefa + " in its apontamentoTarefaList field has a non-nullable idtarefa field.");
            }
            List<AnexoTarefa> anexoTarefaListOrphanCheck = tarefa.getAnexoTarefaList();
            for (AnexoTarefa anexoTarefaListOrphanCheckAnexoTarefa : anexoTarefaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tarefa (" + tarefa + ") cannot be destroyed since the AnexoTarefa " + anexoTarefaListOrphanCheckAnexoTarefa + " in its anexoTarefaList field has a non-nullable idtarefa field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            CentroCusto idcentrocusto = tarefa.getIdcentrocusto();
            if (idcentrocusto != null) {
                idcentrocusto.getTarefaList().remove(tarefa);
                idcentrocusto = em.merge(idcentrocusto);
            }
            Departamento iddepartamento = tarefa.getIddepartamento();
            if (iddepartamento != null) {
                iddepartamento.getTarefaList().remove(tarefa);
                iddepartamento = em.merge(iddepartamento);
            }
            Empresa idempresa = tarefa.getIdempresa();
            if (idempresa != null) {
                idempresa.getTarefaList().remove(tarefa);
                idempresa = em.merge(idempresa);
            }
            EmpresaCliente idempresacliente = tarefa.getIdempresacliente();
            if (idempresacliente != null) {
                idempresacliente.getTarefaList().remove(tarefa);
                idempresacliente = em.merge(idempresacliente);
            }
            Tarefa idtarefaproxima = tarefa.getIdtarefaproxima();
            if (idtarefaproxima != null) {
                idtarefaproxima.getTarefaList().remove(tarefa);
                idtarefaproxima = em.merge(idtarefaproxima);
            }
            Tarefa idtarefaanterior = tarefa.getIdtarefaanterior();
            if (idtarefaanterior != null) {
                idtarefaanterior.getTarefaList().remove(tarefa);
                idtarefaanterior = em.merge(idtarefaanterior);
            }
            Tarefa idtarefapai = tarefa.getIdtarefapai();
            if (idtarefapai != null) {
                idtarefapai.getTarefaList().remove(tarefa);
                idtarefapai = em.merge(idtarefapai);
            }
            Usuario idusuarioinclusao = tarefa.getIdusuarioinclusao();
            if (idusuarioinclusao != null) {
                idusuarioinclusao.getTarefaList().remove(tarefa);
                idusuarioinclusao = em.merge(idusuarioinclusao);
            }
            Usuario idusuariosolicitante = tarefa.getIdusuariosolicitante();
            if (idusuariosolicitante != null) {
                idusuariosolicitante.getTarefaList().remove(tarefa);
                idusuariosolicitante = em.merge(idusuariosolicitante);
            }
            Usuario idusuarioresponsavel = tarefa.getIdusuarioresponsavel();
            if (idusuarioresponsavel != null) {
                idusuarioresponsavel.getTarefaList().remove(tarefa);
                idusuarioresponsavel = em.merge(idusuarioresponsavel);
            }
            List<Tarefa> tarefaList2 = tarefa.getTarefaList2();
            for (Tarefa tarefaList2Tarefa : tarefaList2) {
                tarefaList2Tarefa.setIdtarefapai(null);
                tarefaList2Tarefa = em.merge(tarefaList2Tarefa);
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
