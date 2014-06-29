/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.dao;

import com.saax.gestorweb.dao.exceptions.IllegalOrphanException;
import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.Endereco;
import com.saax.gestorweb.model.datamodel.FilialEmpresa;
import java.util.ArrayList;
import java.util.List;
import com.saax.gestorweb.model.datamodel.CentroCusto;
import com.saax.gestorweb.model.datamodel.UsuarioEmpresa;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.Departamento;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author rodrigo
 */
public class EmpresaDAO implements Serializable {

    public EmpresaDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empresa empresa) {
        if (empresa.getFiliais() == null) {
            empresa.setFiliais(new ArrayList<FilialEmpresa>());
        }
        if (empresa.getCentrosDeCusto() == null) {
            empresa.setCentrosDeCusto(new ArrayList<CentroCusto>());
        }
        if (empresa.getUsuarios() == null) {
            empresa.setUsuarios(new ArrayList<UsuarioEmpresa>());
        }
        if (empresa.getMetas() == null) {
            empresa.setMetas(new ArrayList<Meta>());
        }
        if (empresa.getDepartamentos() == null) {
            empresa.setDepartamentos(new ArrayList<Departamento>());
        }
        if (empresa.getSubEmpresas() == null) {
            empresa.setSubEmpresas(new ArrayList<Empresa>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa empresaPrincipal = empresa.getEmpresaPrincipal();
            if (empresaPrincipal != null) {
                empresaPrincipal = em.getReference(empresaPrincipal.getClass(), empresaPrincipal.getId());
                empresa.setEmpresaPrincipal(empresaPrincipal);
            }
            Endereco endereco = empresa.getEndereco();
            if (endereco != null) {
                endereco = em.getReference(endereco.getClass(), endereco.getIdEndereco());
                empresa.setEndereco(endereco);
            }
            List<FilialEmpresa> attachedFiliais = new ArrayList<FilialEmpresa>();
            for (FilialEmpresa filiaisFilialEmpresaToAttach : empresa.getFiliais()) {
                filiaisFilialEmpresaToAttach = em.getReference(filiaisFilialEmpresaToAttach.getClass(), filiaisFilialEmpresaToAttach.getId());
                attachedFiliais.add(filiaisFilialEmpresaToAttach);
            }
            empresa.setFiliais(attachedFiliais);
            List<CentroCusto> attachedCentrosDeCusto = new ArrayList<CentroCusto>();
            for (CentroCusto centrosDeCustoCentroCustoToAttach : empresa.getCentrosDeCusto()) {
                centrosDeCustoCentroCustoToAttach = em.getReference(centrosDeCustoCentroCustoToAttach.getClass(), centrosDeCustoCentroCustoToAttach.getId());
                attachedCentrosDeCusto.add(centrosDeCustoCentroCustoToAttach);
            }
            empresa.setCentrosDeCusto(attachedCentrosDeCusto);
            List<UsuarioEmpresa> attachedUsuarios = new ArrayList<UsuarioEmpresa>();
            for (UsuarioEmpresa usuariosUsuarioEmpresaToAttach : empresa.getUsuarios()) {
                usuariosUsuarioEmpresaToAttach = em.getReference(usuariosUsuarioEmpresaToAttach.getClass(), usuariosUsuarioEmpresaToAttach.getId());
                attachedUsuarios.add(usuariosUsuarioEmpresaToAttach);
            }
            empresa.setUsuarios(attachedUsuarios);
            List<Meta> attachedMetas = new ArrayList<Meta>();
            for (Meta metasMetaToAttach : empresa.getMetas()) {
                metasMetaToAttach = em.getReference(metasMetaToAttach.getClass(), metasMetaToAttach.getId());
                attachedMetas.add(metasMetaToAttach);
            }
            empresa.setMetas(attachedMetas);
            List<Departamento> attachedDepartamentos = new ArrayList<Departamento>();
            for (Departamento departamentosDepartamentoToAttach : empresa.getDepartamentos()) {
                departamentosDepartamentoToAttach = em.getReference(departamentosDepartamentoToAttach.getClass(), departamentosDepartamentoToAttach.getId());
                attachedDepartamentos.add(departamentosDepartamentoToAttach);
            }
            empresa.setDepartamentos(attachedDepartamentos);
            List<Empresa> attachedSubEmpresas = new ArrayList<Empresa>();
            for (Empresa subEmpresasEmpresaToAttach : empresa.getSubEmpresas()) {
                subEmpresasEmpresaToAttach = em.getReference(subEmpresasEmpresaToAttach.getClass(), subEmpresasEmpresaToAttach.getId());
                attachedSubEmpresas.add(subEmpresasEmpresaToAttach);
            }
            empresa.setSubEmpresas(attachedSubEmpresas);
            em.persist(empresa);
            if (empresaPrincipal != null) {
                empresaPrincipal.getSubEmpresas().add(empresa);
                empresaPrincipal = em.merge(empresaPrincipal);
            }
            if (endereco != null) {
                endereco.getEmpresaList().add(empresa);
                endereco = em.merge(endereco);
            }
            for (FilialEmpresa filiaisFilialEmpresa : empresa.getFiliais()) {
                Empresa oldMatrizOfFiliaisFilialEmpresa = filiaisFilialEmpresa.getMatriz();
                filiaisFilialEmpresa.setMatriz(empresa);
                filiaisFilialEmpresa = em.merge(filiaisFilialEmpresa);
                if (oldMatrizOfFiliaisFilialEmpresa != null) {
                    oldMatrizOfFiliaisFilialEmpresa.getFiliais().remove(filiaisFilialEmpresa);
                    oldMatrizOfFiliaisFilialEmpresa = em.merge(oldMatrizOfFiliaisFilialEmpresa);
                }
            }
            for (CentroCusto centrosDeCustoCentroCusto : empresa.getCentrosDeCusto()) {
                Empresa oldEmpresaOfCentrosDeCustoCentroCusto = centrosDeCustoCentroCusto.getEmpresa();
                centrosDeCustoCentroCusto.setEmpresa(empresa);
                centrosDeCustoCentroCusto = em.merge(centrosDeCustoCentroCusto);
                if (oldEmpresaOfCentrosDeCustoCentroCusto != null) {
                    oldEmpresaOfCentrosDeCustoCentroCusto.getCentrosDeCusto().remove(centrosDeCustoCentroCusto);
                    oldEmpresaOfCentrosDeCustoCentroCusto = em.merge(oldEmpresaOfCentrosDeCustoCentroCusto);
                }
            }
            for (UsuarioEmpresa usuariosUsuarioEmpresa : empresa.getUsuarios()) {
                Empresa oldEmpresaOfUsuariosUsuarioEmpresa = usuariosUsuarioEmpresa.getEmpresa();
                usuariosUsuarioEmpresa.setEmpresa(empresa);
                usuariosUsuarioEmpresa = em.merge(usuariosUsuarioEmpresa);
                if (oldEmpresaOfUsuariosUsuarioEmpresa != null) {
                    oldEmpresaOfUsuariosUsuarioEmpresa.getUsuarios().remove(usuariosUsuarioEmpresa);
                    oldEmpresaOfUsuariosUsuarioEmpresa = em.merge(oldEmpresaOfUsuariosUsuarioEmpresa);
                }
            }
            for (Meta metasMeta : empresa.getMetas()) {
                Empresa oldEmpresaOfMetasMeta = metasMeta.getEmpresa();
                metasMeta.setEmpresa(empresa);
                metasMeta = em.merge(metasMeta);
                if (oldEmpresaOfMetasMeta != null) {
                    oldEmpresaOfMetasMeta.getMetas().remove(metasMeta);
                    oldEmpresaOfMetasMeta = em.merge(oldEmpresaOfMetasMeta);
                }
            }
            for (Departamento departamentosDepartamento : empresa.getDepartamentos()) {
                Empresa oldEmpresaOfDepartamentosDepartamento = departamentosDepartamento.getEmpresa();
                departamentosDepartamento.setEmpresa(empresa);
                departamentosDepartamento = em.merge(departamentosDepartamento);
                if (oldEmpresaOfDepartamentosDepartamento != null) {
                    oldEmpresaOfDepartamentosDepartamento.getDepartamentos().remove(departamentosDepartamento);
                    oldEmpresaOfDepartamentosDepartamento = em.merge(oldEmpresaOfDepartamentosDepartamento);
                }
            }
            for (Empresa subEmpresasEmpresa : empresa.getSubEmpresas()) {
                Empresa oldEmpresaPrincipalOfSubEmpresasEmpresa = subEmpresasEmpresa.getEmpresaPrincipal();
                subEmpresasEmpresa.setEmpresaPrincipal(empresa);
                subEmpresasEmpresa = em.merge(subEmpresasEmpresa);
                if (oldEmpresaPrincipalOfSubEmpresasEmpresa != null) {
                    oldEmpresaPrincipalOfSubEmpresasEmpresa.getSubEmpresas().remove(subEmpresasEmpresa);
                    oldEmpresaPrincipalOfSubEmpresasEmpresa = em.merge(oldEmpresaPrincipalOfSubEmpresasEmpresa);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empresa empresa) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa persistentEmpresa = em.find(Empresa.class, empresa.getId());
            Empresa empresaPrincipalOld = persistentEmpresa.getEmpresaPrincipal();
            Empresa empresaPrincipalNew = empresa.getEmpresaPrincipal();
            Endereco enderecoOld = persistentEmpresa.getEndereco();
            Endereco enderecoNew = empresa.getEndereco();
            List<FilialEmpresa> filiaisOld = persistentEmpresa.getFiliais();
            List<FilialEmpresa> filiaisNew = empresa.getFiliais();
            List<CentroCusto> centrosDeCustoOld = persistentEmpresa.getCentrosDeCusto();
            List<CentroCusto> centrosDeCustoNew = empresa.getCentrosDeCusto();
            List<UsuarioEmpresa> usuariosOld = persistentEmpresa.getUsuarios();
            List<UsuarioEmpresa> usuariosNew = empresa.getUsuarios();
            List<Meta> metasOld = persistentEmpresa.getMetas();
            List<Meta> metasNew = empresa.getMetas();
            List<Departamento> departamentosOld = persistentEmpresa.getDepartamentos();
            List<Departamento> departamentosNew = empresa.getDepartamentos();
            List<Empresa> subEmpresasOld = persistentEmpresa.getSubEmpresas();
            List<Empresa> subEmpresasNew = empresa.getSubEmpresas();
            List<String> illegalOrphanMessages = null;
            for (FilialEmpresa filiaisOldFilialEmpresa : filiaisOld) {
                if (!filiaisNew.contains(filiaisOldFilialEmpresa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain FilialEmpresa " + filiaisOldFilialEmpresa + " since its matriz field is not nullable.");
                }
            }
            for (CentroCusto centrosDeCustoOldCentroCusto : centrosDeCustoOld) {
                if (!centrosDeCustoNew.contains(centrosDeCustoOldCentroCusto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CentroCusto " + centrosDeCustoOldCentroCusto + " since its empresa field is not nullable.");
                }
            }
            for (UsuarioEmpresa usuariosOldUsuarioEmpresa : usuariosOld) {
                if (!usuariosNew.contains(usuariosOldUsuarioEmpresa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UsuarioEmpresa " + usuariosOldUsuarioEmpresa + " since its empresa field is not nullable.");
                }
            }
            for (Meta metasOldMeta : metasOld) {
                if (!metasNew.contains(metasOldMeta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Meta " + metasOldMeta + " since its empresa field is not nullable.");
                }
            }
            for (Departamento departamentosOldDepartamento : departamentosOld) {
                if (!departamentosNew.contains(departamentosOldDepartamento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Departamento " + departamentosOldDepartamento + " since its empresa field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (empresaPrincipalNew != null) {
                empresaPrincipalNew = em.getReference(empresaPrincipalNew.getClass(), empresaPrincipalNew.getId());
                empresa.setEmpresaPrincipal(empresaPrincipalNew);
            }
            if (enderecoNew != null) {
                enderecoNew = em.getReference(enderecoNew.getClass(), enderecoNew.getIdEndereco());
                empresa.setEndereco(enderecoNew);
            }
            List<FilialEmpresa> attachedFiliaisNew = new ArrayList<FilialEmpresa>();
            for (FilialEmpresa filiaisNewFilialEmpresaToAttach : filiaisNew) {
                filiaisNewFilialEmpresaToAttach = em.getReference(filiaisNewFilialEmpresaToAttach.getClass(), filiaisNewFilialEmpresaToAttach.getId());
                attachedFiliaisNew.add(filiaisNewFilialEmpresaToAttach);
            }
            filiaisNew = attachedFiliaisNew;
            empresa.setFiliais(filiaisNew);
            List<CentroCusto> attachedCentrosDeCustoNew = new ArrayList<CentroCusto>();
            for (CentroCusto centrosDeCustoNewCentroCustoToAttach : centrosDeCustoNew) {
                centrosDeCustoNewCentroCustoToAttach = em.getReference(centrosDeCustoNewCentroCustoToAttach.getClass(), centrosDeCustoNewCentroCustoToAttach.getId());
                attachedCentrosDeCustoNew.add(centrosDeCustoNewCentroCustoToAttach);
            }
            centrosDeCustoNew = attachedCentrosDeCustoNew;
            empresa.setCentrosDeCusto(centrosDeCustoNew);
            List<UsuarioEmpresa> attachedUsuariosNew = new ArrayList<UsuarioEmpresa>();
            for (UsuarioEmpresa usuariosNewUsuarioEmpresaToAttach : usuariosNew) {
                usuariosNewUsuarioEmpresaToAttach = em.getReference(usuariosNewUsuarioEmpresaToAttach.getClass(), usuariosNewUsuarioEmpresaToAttach.getId());
                attachedUsuariosNew.add(usuariosNewUsuarioEmpresaToAttach);
            }
            usuariosNew = attachedUsuariosNew;
            empresa.setUsuarios(usuariosNew);
            List<Meta> attachedMetasNew = new ArrayList<Meta>();
            for (Meta metasNewMetaToAttach : metasNew) {
                metasNewMetaToAttach = em.getReference(metasNewMetaToAttach.getClass(), metasNewMetaToAttach.getId());
                attachedMetasNew.add(metasNewMetaToAttach);
            }
            metasNew = attachedMetasNew;
            empresa.setMetas(metasNew);
            List<Departamento> attachedDepartamentosNew = new ArrayList<Departamento>();
            for (Departamento departamentosNewDepartamentoToAttach : departamentosNew) {
                departamentosNewDepartamentoToAttach = em.getReference(departamentosNewDepartamentoToAttach.getClass(), departamentosNewDepartamentoToAttach.getId());
                attachedDepartamentosNew.add(departamentosNewDepartamentoToAttach);
            }
            departamentosNew = attachedDepartamentosNew;
            empresa.setDepartamentos(departamentosNew);
            List<Empresa> attachedSubEmpresasNew = new ArrayList<Empresa>();
            for (Empresa subEmpresasNewEmpresaToAttach : subEmpresasNew) {
                subEmpresasNewEmpresaToAttach = em.getReference(subEmpresasNewEmpresaToAttach.getClass(), subEmpresasNewEmpresaToAttach.getId());
                attachedSubEmpresasNew.add(subEmpresasNewEmpresaToAttach);
            }
            subEmpresasNew = attachedSubEmpresasNew;
            empresa.setSubEmpresas(subEmpresasNew);
            empresa = em.merge(empresa);
            if (empresaPrincipalOld != null && !empresaPrincipalOld.equals(empresaPrincipalNew)) {
                empresaPrincipalOld.getSubEmpresas().remove(empresa);
                empresaPrincipalOld = em.merge(empresaPrincipalOld);
            }
            if (empresaPrincipalNew != null && !empresaPrincipalNew.equals(empresaPrincipalOld)) {
                empresaPrincipalNew.getSubEmpresas().add(empresa);
                empresaPrincipalNew = em.merge(empresaPrincipalNew);
            }
            if (enderecoOld != null && !enderecoOld.equals(enderecoNew)) {
                enderecoOld.getEmpresaList().remove(empresa);
                enderecoOld = em.merge(enderecoOld);
            }
            if (enderecoNew != null && !enderecoNew.equals(enderecoOld)) {
                enderecoNew.getEmpresaList().add(empresa);
                enderecoNew = em.merge(enderecoNew);
            }
            for (FilialEmpresa filiaisNewFilialEmpresa : filiaisNew) {
                if (!filiaisOld.contains(filiaisNewFilialEmpresa)) {
                    Empresa oldMatrizOfFiliaisNewFilialEmpresa = filiaisNewFilialEmpresa.getMatriz();
                    filiaisNewFilialEmpresa.setMatriz(empresa);
                    filiaisNewFilialEmpresa = em.merge(filiaisNewFilialEmpresa);
                    if (oldMatrizOfFiliaisNewFilialEmpresa != null && !oldMatrizOfFiliaisNewFilialEmpresa.equals(empresa)) {
                        oldMatrizOfFiliaisNewFilialEmpresa.getFiliais().remove(filiaisNewFilialEmpresa);
                        oldMatrizOfFiliaisNewFilialEmpresa = em.merge(oldMatrizOfFiliaisNewFilialEmpresa);
                    }
                }
            }
            for (CentroCusto centrosDeCustoNewCentroCusto : centrosDeCustoNew) {
                if (!centrosDeCustoOld.contains(centrosDeCustoNewCentroCusto)) {
                    Empresa oldEmpresaOfCentrosDeCustoNewCentroCusto = centrosDeCustoNewCentroCusto.getEmpresa();
                    centrosDeCustoNewCentroCusto.setEmpresa(empresa);
                    centrosDeCustoNewCentroCusto = em.merge(centrosDeCustoNewCentroCusto);
                    if (oldEmpresaOfCentrosDeCustoNewCentroCusto != null && !oldEmpresaOfCentrosDeCustoNewCentroCusto.equals(empresa)) {
                        oldEmpresaOfCentrosDeCustoNewCentroCusto.getCentrosDeCusto().remove(centrosDeCustoNewCentroCusto);
                        oldEmpresaOfCentrosDeCustoNewCentroCusto = em.merge(oldEmpresaOfCentrosDeCustoNewCentroCusto);
                    }
                }
            }
            for (UsuarioEmpresa usuariosNewUsuarioEmpresa : usuariosNew) {
                if (!usuariosOld.contains(usuariosNewUsuarioEmpresa)) {
                    Empresa oldEmpresaOfUsuariosNewUsuarioEmpresa = usuariosNewUsuarioEmpresa.getEmpresa();
                    usuariosNewUsuarioEmpresa.setEmpresa(empresa);
                    usuariosNewUsuarioEmpresa = em.merge(usuariosNewUsuarioEmpresa);
                    if (oldEmpresaOfUsuariosNewUsuarioEmpresa != null && !oldEmpresaOfUsuariosNewUsuarioEmpresa.equals(empresa)) {
                        oldEmpresaOfUsuariosNewUsuarioEmpresa.getUsuarios().remove(usuariosNewUsuarioEmpresa);
                        oldEmpresaOfUsuariosNewUsuarioEmpresa = em.merge(oldEmpresaOfUsuariosNewUsuarioEmpresa);
                    }
                }
            }
            for (Meta metasNewMeta : metasNew) {
                if (!metasOld.contains(metasNewMeta)) {
                    Empresa oldEmpresaOfMetasNewMeta = metasNewMeta.getEmpresa();
                    metasNewMeta.setEmpresa(empresa);
                    metasNewMeta = em.merge(metasNewMeta);
                    if (oldEmpresaOfMetasNewMeta != null && !oldEmpresaOfMetasNewMeta.equals(empresa)) {
                        oldEmpresaOfMetasNewMeta.getMetas().remove(metasNewMeta);
                        oldEmpresaOfMetasNewMeta = em.merge(oldEmpresaOfMetasNewMeta);
                    }
                }
            }
            for (Departamento departamentosNewDepartamento : departamentosNew) {
                if (!departamentosOld.contains(departamentosNewDepartamento)) {
                    Empresa oldEmpresaOfDepartamentosNewDepartamento = departamentosNewDepartamento.getEmpresa();
                    departamentosNewDepartamento.setEmpresa(empresa);
                    departamentosNewDepartamento = em.merge(departamentosNewDepartamento);
                    if (oldEmpresaOfDepartamentosNewDepartamento != null && !oldEmpresaOfDepartamentosNewDepartamento.equals(empresa)) {
                        oldEmpresaOfDepartamentosNewDepartamento.getDepartamentos().remove(departamentosNewDepartamento);
                        oldEmpresaOfDepartamentosNewDepartamento = em.merge(oldEmpresaOfDepartamentosNewDepartamento);
                    }
                }
            }
            for (Empresa subEmpresasOldEmpresa : subEmpresasOld) {
                if (!subEmpresasNew.contains(subEmpresasOldEmpresa)) {
                    subEmpresasOldEmpresa.setEmpresaPrincipal(null);
                    subEmpresasOldEmpresa = em.merge(subEmpresasOldEmpresa);
                }
            }
            for (Empresa subEmpresasNewEmpresa : subEmpresasNew) {
                if (!subEmpresasOld.contains(subEmpresasNewEmpresa)) {
                    Empresa oldEmpresaPrincipalOfSubEmpresasNewEmpresa = subEmpresasNewEmpresa.getEmpresaPrincipal();
                    subEmpresasNewEmpresa.setEmpresaPrincipal(empresa);
                    subEmpresasNewEmpresa = em.merge(subEmpresasNewEmpresa);
                    if (oldEmpresaPrincipalOfSubEmpresasNewEmpresa != null && !oldEmpresaPrincipalOfSubEmpresasNewEmpresa.equals(empresa)) {
                        oldEmpresaPrincipalOfSubEmpresasNewEmpresa.getSubEmpresas().remove(subEmpresasNewEmpresa);
                        oldEmpresaPrincipalOfSubEmpresasNewEmpresa = em.merge(oldEmpresaPrincipalOfSubEmpresasNewEmpresa);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = empresa.getId();
                if (findEmpresa(id) == null) {
                    throw new NonexistentEntityException("The empresa with id " + id + " no longer exists.");
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
            Empresa empresa;
            try {
                empresa = em.getReference(Empresa.class, id);
                empresa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empresa with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<FilialEmpresa> filiaisOrphanCheck = empresa.getFiliais();
            for (FilialEmpresa filiaisOrphanCheckFilialEmpresa : filiaisOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empresa (" + empresa + ") cannot be destroyed since the FilialEmpresa " + filiaisOrphanCheckFilialEmpresa + " in its filiais field has a non-nullable matriz field.");
            }
            List<CentroCusto> centrosDeCustoOrphanCheck = empresa.getCentrosDeCusto();
            for (CentroCusto centrosDeCustoOrphanCheckCentroCusto : centrosDeCustoOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empresa (" + empresa + ") cannot be destroyed since the CentroCusto " + centrosDeCustoOrphanCheckCentroCusto + " in its centrosDeCusto field has a non-nullable empresa field.");
            }
            List<UsuarioEmpresa> usuariosOrphanCheck = empresa.getUsuarios();
            for (UsuarioEmpresa usuariosOrphanCheckUsuarioEmpresa : usuariosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empresa (" + empresa + ") cannot be destroyed since the UsuarioEmpresa " + usuariosOrphanCheckUsuarioEmpresa + " in its usuarios field has a non-nullable empresa field.");
            }
            List<Meta> metasOrphanCheck = empresa.getMetas();
            for (Meta metasOrphanCheckMeta : metasOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empresa (" + empresa + ") cannot be destroyed since the Meta " + metasOrphanCheckMeta + " in its metas field has a non-nullable empresa field.");
            }
            List<Departamento> departamentosOrphanCheck = empresa.getDepartamentos();
            for (Departamento departamentosOrphanCheckDepartamento : departamentosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empresa (" + empresa + ") cannot be destroyed since the Departamento " + departamentosOrphanCheckDepartamento + " in its departamentos field has a non-nullable empresa field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Empresa empresaPrincipal = empresa.getEmpresaPrincipal();
            if (empresaPrincipal != null) {
                empresaPrincipal.getSubEmpresas().remove(empresa);
                empresaPrincipal = em.merge(empresaPrincipal);
            }
            Endereco endereco = empresa.getEndereco();
            if (endereco != null) {
                endereco.getEmpresaList().remove(empresa);
                endereco = em.merge(endereco);
            }
            List<Empresa> subEmpresas = empresa.getSubEmpresas();
            for (Empresa subEmpresasEmpresa : subEmpresas) {
                subEmpresasEmpresa.setEmpresaPrincipal(null);
                subEmpresasEmpresa = em.merge(subEmpresasEmpresa);
            }
            em.remove(empresa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empresa> findEmpresaEntities() {
        return findEmpresaEntities(true, -1, -1);
    }

    public List<Empresa> findEmpresaEntities(int maxResults, int firstResult) {
        return findEmpresaEntities(false, maxResults, firstResult);
    }

    private List<Empresa> findEmpresaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empresa.class));
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

    public Empresa findEmpresa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empresa.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpresaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empresa> rt = cq.from(Empresa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
