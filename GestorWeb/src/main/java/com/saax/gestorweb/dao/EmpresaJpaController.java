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
import com.saax.gestorweb.model.datamodel.UsuarioEmpresa;
import java.util.ArrayList;
import java.util.Collection;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.RelacionamentoEmpresaCliente;
import com.saax.gestorweb.model.datamodel.CentroCusto;
import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author rodrigo
 */
public class EmpresaJpaController implements Serializable {

    public EmpresaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empresa empresa) {
        if (empresa.getUsuarios() == null) {
            empresa.setUsuarios(new ArrayList<UsuarioEmpresa>());
        }
        if (empresa.getMetasProprietarias() == null) {
            empresa.setMetasProprietarias(new ArrayList<Meta>());
        }
        if (empresa.getMetasAtribuidas() == null) {
            empresa.setMetasAtribuidas(new ArrayList<Meta>());
        }
        if (empresa.getEmpresasGestoras() == null) {
            empresa.setEmpresasGestoras(new ArrayList<RelacionamentoEmpresaCliente>());
        }
        if (empresa.getEmpresasGerenciadas() == null) {
            empresa.setEmpresasGerenciadas(new ArrayList<RelacionamentoEmpresaCliente>());
        }
        if (empresa.getCentrosDeCusto() == null) {
            empresa.setCentrosDeCusto(new ArrayList<CentroCusto>());
        }
        if (empresa.getDepartamentos() == null) {
            empresa.setDepartamentos(new ArrayList<Departamento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<UsuarioEmpresa> attachedUsuarios = new ArrayList<UsuarioEmpresa>();
            for (UsuarioEmpresa usuariosUsuarioEmpresaToAttach : empresa.getUsuarios()) {
                usuariosUsuarioEmpresaToAttach = em.getReference(usuariosUsuarioEmpresaToAttach.getClass(), usuariosUsuarioEmpresaToAttach.getIdUsuarioEmpresa());
                attachedUsuarios.add(usuariosUsuarioEmpresaToAttach);
            }
            empresa.setUsuarios(attachedUsuarios);
            Collection<Meta> attachedMetasProprietarias = new ArrayList<Meta>();
            for (Meta metasProprietariasMetaToAttach : empresa.getMetasProprietarias()) {
                metasProprietariasMetaToAttach = em.getReference(metasProprietariasMetaToAttach.getClass(), metasProprietariasMetaToAttach.getIdMeta());
                attachedMetasProprietarias.add(metasProprietariasMetaToAttach);
            }
            empresa.setMetasProprietarias(attachedMetasProprietarias);
            Collection<Meta> attachedMetasAtribuidas = new ArrayList<Meta>();
            for (Meta metasAtribuidasMetaToAttach : empresa.getMetasAtribuidas()) {
                metasAtribuidasMetaToAttach = em.getReference(metasAtribuidasMetaToAttach.getClass(), metasAtribuidasMetaToAttach.getIdMeta());
                attachedMetasAtribuidas.add(metasAtribuidasMetaToAttach);
            }
            empresa.setMetasAtribuidas(attachedMetasAtribuidas);
            Collection<RelacionamentoEmpresaCliente> attachedEmpresasGestoras = new ArrayList<RelacionamentoEmpresaCliente>();
            for (RelacionamentoEmpresaCliente empresasGestorasRelacionamentoEmpresaClienteToAttach : empresa.getEmpresasGestoras()) {
                empresasGestorasRelacionamentoEmpresaClienteToAttach = em.getReference(empresasGestorasRelacionamentoEmpresaClienteToAttach.getClass(), empresasGestorasRelacionamentoEmpresaClienteToAttach.getIdRelacionamentoEmpresaCliente());
                attachedEmpresasGestoras.add(empresasGestorasRelacionamentoEmpresaClienteToAttach);
            }
            empresa.setEmpresasGestoras(attachedEmpresasGestoras);
            Collection<RelacionamentoEmpresaCliente> attachedEmpresasGerenciadas = new ArrayList<RelacionamentoEmpresaCliente>();
            for (RelacionamentoEmpresaCliente empresasGerenciadasRelacionamentoEmpresaClienteToAttach : empresa.getEmpresasGerenciadas()) {
                empresasGerenciadasRelacionamentoEmpresaClienteToAttach = em.getReference(empresasGerenciadasRelacionamentoEmpresaClienteToAttach.getClass(), empresasGerenciadasRelacionamentoEmpresaClienteToAttach.getIdRelacionamentoEmpresaCliente());
                attachedEmpresasGerenciadas.add(empresasGerenciadasRelacionamentoEmpresaClienteToAttach);
            }
            empresa.setEmpresasGerenciadas(attachedEmpresasGerenciadas);
            Collection<CentroCusto> attachedCentrosDeCusto = new ArrayList<CentroCusto>();
            for (CentroCusto centrosDeCustoCentroCustoToAttach : empresa.getCentrosDeCusto()) {
                centrosDeCustoCentroCustoToAttach = em.getReference(centrosDeCustoCentroCustoToAttach.getClass(), centrosDeCustoCentroCustoToAttach.getIdCentroCusto());
                attachedCentrosDeCusto.add(centrosDeCustoCentroCustoToAttach);
            }
            empresa.setCentrosDeCusto(attachedCentrosDeCusto);
            Collection<Departamento> attachedDepartamentos = new ArrayList<Departamento>();
            for (Departamento departamentosDepartamentoToAttach : empresa.getDepartamentos()) {
                departamentosDepartamentoToAttach = em.getReference(departamentosDepartamentoToAttach.getClass(), departamentosDepartamentoToAttach.getIdDepartamento());
                attachedDepartamentos.add(departamentosDepartamentoToAttach);
            }
            empresa.setDepartamentos(attachedDepartamentos);
            em.persist(empresa);
            for (UsuarioEmpresa usuariosUsuarioEmpresa : empresa.getUsuarios()) {
                Empresa oldEmpresaOfUsuariosUsuarioEmpresa = usuariosUsuarioEmpresa.getEmpresa();
                usuariosUsuarioEmpresa.setEmpresa(empresa);
                usuariosUsuarioEmpresa = em.merge(usuariosUsuarioEmpresa);
                if (oldEmpresaOfUsuariosUsuarioEmpresa != null) {
                    oldEmpresaOfUsuariosUsuarioEmpresa.getUsuarios().remove(usuariosUsuarioEmpresa);
                    oldEmpresaOfUsuariosUsuarioEmpresa = em.merge(oldEmpresaOfUsuariosUsuarioEmpresa);
                }
            }
            for (Meta metasProprietariasMeta : empresa.getMetasProprietarias()) {
                Empresa oldEmpresaOfMetasProprietariasMeta = metasProprietariasMeta.getEmpresa();
                metasProprietariasMeta.setEmpresa(empresa);
                metasProprietariasMeta = em.merge(metasProprietariasMeta);
                if (oldEmpresaOfMetasProprietariasMeta != null) {
                    oldEmpresaOfMetasProprietariasMeta.getMetasProprietarias().remove(metasProprietariasMeta);
                    oldEmpresaOfMetasProprietariasMeta = em.merge(oldEmpresaOfMetasProprietariasMeta);
                }
            }
            for (Meta metasAtribuidasMeta : empresa.getMetasAtribuidas()) {
                Empresa oldEmpresaClienteOfMetasAtribuidasMeta = metasAtribuidasMeta.getEmpresaCliente();
                metasAtribuidasMeta.setEmpresaCliente(empresa);
                metasAtribuidasMeta = em.merge(metasAtribuidasMeta);
                if (oldEmpresaClienteOfMetasAtribuidasMeta != null) {
                    oldEmpresaClienteOfMetasAtribuidasMeta.getMetasAtribuidas().remove(metasAtribuidasMeta);
                    oldEmpresaClienteOfMetasAtribuidasMeta = em.merge(oldEmpresaClienteOfMetasAtribuidasMeta);
                }
            }
            for (RelacionamentoEmpresaCliente empresasGestorasRelacionamentoEmpresaCliente : empresa.getEmpresasGestoras()) {
                Empresa oldEmpresaGestoraOfEmpresasGestorasRelacionamentoEmpresaCliente = empresasGestorasRelacionamentoEmpresaCliente.getEmpresaGestora();
                empresasGestorasRelacionamentoEmpresaCliente.setEmpresaGestora(empresa);
                empresasGestorasRelacionamentoEmpresaCliente = em.merge(empresasGestorasRelacionamentoEmpresaCliente);
                if (oldEmpresaGestoraOfEmpresasGestorasRelacionamentoEmpresaCliente != null) {
                    oldEmpresaGestoraOfEmpresasGestorasRelacionamentoEmpresaCliente.getEmpresasGestoras().remove(empresasGestorasRelacionamentoEmpresaCliente);
                    oldEmpresaGestoraOfEmpresasGestorasRelacionamentoEmpresaCliente = em.merge(oldEmpresaGestoraOfEmpresasGestorasRelacionamentoEmpresaCliente);
                }
            }
            for (RelacionamentoEmpresaCliente empresasGerenciadasRelacionamentoEmpresaCliente : empresa.getEmpresasGerenciadas()) {
                Empresa oldEmpresaClienteOfEmpresasGerenciadasRelacionamentoEmpresaCliente = empresasGerenciadasRelacionamentoEmpresaCliente.getEmpresaCliente();
                empresasGerenciadasRelacionamentoEmpresaCliente.setEmpresaCliente(empresa);
                empresasGerenciadasRelacionamentoEmpresaCliente = em.merge(empresasGerenciadasRelacionamentoEmpresaCliente);
                if (oldEmpresaClienteOfEmpresasGerenciadasRelacionamentoEmpresaCliente != null) {
                    oldEmpresaClienteOfEmpresasGerenciadasRelacionamentoEmpresaCliente.getEmpresasGerenciadas().remove(empresasGerenciadasRelacionamentoEmpresaCliente);
                    oldEmpresaClienteOfEmpresasGerenciadasRelacionamentoEmpresaCliente = em.merge(oldEmpresaClienteOfEmpresasGerenciadasRelacionamentoEmpresaCliente);
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
            for (Departamento departamentosDepartamento : empresa.getDepartamentos()) {
                Empresa oldEmpresaOfDepartamentosDepartamento = departamentosDepartamento.getEmpresa();
                departamentosDepartamento.setEmpresa(empresa);
                departamentosDepartamento = em.merge(departamentosDepartamento);
                if (oldEmpresaOfDepartamentosDepartamento != null) {
                    oldEmpresaOfDepartamentosDepartamento.getDepartamentos().remove(departamentosDepartamento);
                    oldEmpresaOfDepartamentosDepartamento = em.merge(oldEmpresaOfDepartamentosDepartamento);
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
            Empresa persistentEmpresa = em.find(Empresa.class, empresa.getIdEmpresa());
            Collection<UsuarioEmpresa> usuariosOld = persistentEmpresa.getUsuarios();
            Collection<UsuarioEmpresa> usuariosNew = empresa.getUsuarios();
            Collection<Meta> metasProprietariasOld = persistentEmpresa.getMetasProprietarias();
            Collection<Meta> metasProprietariasNew = empresa.getMetasProprietarias();
            Collection<Meta> metasAtribuidasOld = persistentEmpresa.getMetasAtribuidas();
            Collection<Meta> metasAtribuidasNew = empresa.getMetasAtribuidas();
            Collection<RelacionamentoEmpresaCliente> empresasGestorasOld = persistentEmpresa.getEmpresasGestoras();
            Collection<RelacionamentoEmpresaCliente> empresasGestorasNew = empresa.getEmpresasGestoras();
            Collection<RelacionamentoEmpresaCliente> empresasGerenciadasOld = persistentEmpresa.getEmpresasGerenciadas();
            Collection<RelacionamentoEmpresaCliente> empresasGerenciadasNew = empresa.getEmpresasGerenciadas();
            Collection<CentroCusto> centrosDeCustoOld = persistentEmpresa.getCentrosDeCusto();
            Collection<CentroCusto> centrosDeCustoNew = empresa.getCentrosDeCusto();
            Collection<Departamento> departamentosOld = persistentEmpresa.getDepartamentos();
            Collection<Departamento> departamentosNew = empresa.getDepartamentos();
            List<String> illegalOrphanMessages = null;
            for (UsuarioEmpresa usuariosOldUsuarioEmpresa : usuariosOld) {
                if (!usuariosNew.contains(usuariosOldUsuarioEmpresa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UsuarioEmpresa " + usuariosOldUsuarioEmpresa + " since its empresa field is not nullable.");
                }
            }
            for (Meta metasProprietariasOldMeta : metasProprietariasOld) {
                if (!metasProprietariasNew.contains(metasProprietariasOldMeta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Meta " + metasProprietariasOldMeta + " since its empresa field is not nullable.");
                }
            }
            for (Meta metasAtribuidasOldMeta : metasAtribuidasOld) {
                if (!metasAtribuidasNew.contains(metasAtribuidasOldMeta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Meta " + metasAtribuidasOldMeta + " since its empresaCliente field is not nullable.");
                }
            }
            for (RelacionamentoEmpresaCliente empresasGestorasOldRelacionamentoEmpresaCliente : empresasGestorasOld) {
                if (!empresasGestorasNew.contains(empresasGestorasOldRelacionamentoEmpresaCliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RelacionamentoEmpresaCliente " + empresasGestorasOldRelacionamentoEmpresaCliente + " since its empresaGestora field is not nullable.");
                }
            }
            for (RelacionamentoEmpresaCliente empresasGerenciadasOldRelacionamentoEmpresaCliente : empresasGerenciadasOld) {
                if (!empresasGerenciadasNew.contains(empresasGerenciadasOldRelacionamentoEmpresaCliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RelacionamentoEmpresaCliente " + empresasGerenciadasOldRelacionamentoEmpresaCliente + " since its empresaCliente field is not nullable.");
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
            Collection<UsuarioEmpresa> attachedUsuariosNew = new ArrayList<UsuarioEmpresa>();
            for (UsuarioEmpresa usuariosNewUsuarioEmpresaToAttach : usuariosNew) {
                usuariosNewUsuarioEmpresaToAttach = em.getReference(usuariosNewUsuarioEmpresaToAttach.getClass(), usuariosNewUsuarioEmpresaToAttach.getIdUsuarioEmpresa());
                attachedUsuariosNew.add(usuariosNewUsuarioEmpresaToAttach);
            }
            usuariosNew = attachedUsuariosNew;
            empresa.setUsuarios(usuariosNew);
            Collection<Meta> attachedMetasProprietariasNew = new ArrayList<Meta>();
            for (Meta metasProprietariasNewMetaToAttach : metasProprietariasNew) {
                metasProprietariasNewMetaToAttach = em.getReference(metasProprietariasNewMetaToAttach.getClass(), metasProprietariasNewMetaToAttach.getIdMeta());
                attachedMetasProprietariasNew.add(metasProprietariasNewMetaToAttach);
            }
            metasProprietariasNew = attachedMetasProprietariasNew;
            empresa.setMetasProprietarias(metasProprietariasNew);
            Collection<Meta> attachedMetasAtribuidasNew = new ArrayList<Meta>();
            for (Meta metasAtribuidasNewMetaToAttach : metasAtribuidasNew) {
                metasAtribuidasNewMetaToAttach = em.getReference(metasAtribuidasNewMetaToAttach.getClass(), metasAtribuidasNewMetaToAttach.getIdMeta());
                attachedMetasAtribuidasNew.add(metasAtribuidasNewMetaToAttach);
            }
            metasAtribuidasNew = attachedMetasAtribuidasNew;
            empresa.setMetasAtribuidas(metasAtribuidasNew);
            Collection<RelacionamentoEmpresaCliente> attachedEmpresasGestorasNew = new ArrayList<RelacionamentoEmpresaCliente>();
            for (RelacionamentoEmpresaCliente empresasGestorasNewRelacionamentoEmpresaClienteToAttach : empresasGestorasNew) {
                empresasGestorasNewRelacionamentoEmpresaClienteToAttach = em.getReference(empresasGestorasNewRelacionamentoEmpresaClienteToAttach.getClass(), empresasGestorasNewRelacionamentoEmpresaClienteToAttach.getIdRelacionamentoEmpresaCliente());
                attachedEmpresasGestorasNew.add(empresasGestorasNewRelacionamentoEmpresaClienteToAttach);
            }
            empresasGestorasNew = attachedEmpresasGestorasNew;
            empresa.setEmpresasGestoras(empresasGestorasNew);
            Collection<RelacionamentoEmpresaCliente> attachedEmpresasGerenciadasNew = new ArrayList<RelacionamentoEmpresaCliente>();
            for (RelacionamentoEmpresaCliente empresasGerenciadasNewRelacionamentoEmpresaClienteToAttach : empresasGerenciadasNew) {
                empresasGerenciadasNewRelacionamentoEmpresaClienteToAttach = em.getReference(empresasGerenciadasNewRelacionamentoEmpresaClienteToAttach.getClass(), empresasGerenciadasNewRelacionamentoEmpresaClienteToAttach.getIdRelacionamentoEmpresaCliente());
                attachedEmpresasGerenciadasNew.add(empresasGerenciadasNewRelacionamentoEmpresaClienteToAttach);
            }
            empresasGerenciadasNew = attachedEmpresasGerenciadasNew;
            empresa.setEmpresasGerenciadas(empresasGerenciadasNew);
            Collection<CentroCusto> attachedCentrosDeCustoNew = new ArrayList<CentroCusto>();
            for (CentroCusto centrosDeCustoNewCentroCustoToAttach : centrosDeCustoNew) {
                centrosDeCustoNewCentroCustoToAttach = em.getReference(centrosDeCustoNewCentroCustoToAttach.getClass(), centrosDeCustoNewCentroCustoToAttach.getIdCentroCusto());
                attachedCentrosDeCustoNew.add(centrosDeCustoNewCentroCustoToAttach);
            }
            centrosDeCustoNew = attachedCentrosDeCustoNew;
            empresa.setCentrosDeCusto(centrosDeCustoNew);
            Collection<Departamento> attachedDepartamentosNew = new ArrayList<Departamento>();
            for (Departamento departamentosNewDepartamentoToAttach : departamentosNew) {
                departamentosNewDepartamentoToAttach = em.getReference(departamentosNewDepartamentoToAttach.getClass(), departamentosNewDepartamentoToAttach.getIdDepartamento());
                attachedDepartamentosNew.add(departamentosNewDepartamentoToAttach);
            }
            departamentosNew = attachedDepartamentosNew;
            empresa.setDepartamentos(departamentosNew);
            empresa = em.merge(empresa);
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
            for (Meta metasProprietariasNewMeta : metasProprietariasNew) {
                if (!metasProprietariasOld.contains(metasProprietariasNewMeta)) {
                    Empresa oldEmpresaOfMetasProprietariasNewMeta = metasProprietariasNewMeta.getEmpresa();
                    metasProprietariasNewMeta.setEmpresa(empresa);
                    metasProprietariasNewMeta = em.merge(metasProprietariasNewMeta);
                    if (oldEmpresaOfMetasProprietariasNewMeta != null && !oldEmpresaOfMetasProprietariasNewMeta.equals(empresa)) {
                        oldEmpresaOfMetasProprietariasNewMeta.getMetasProprietarias().remove(metasProprietariasNewMeta);
                        oldEmpresaOfMetasProprietariasNewMeta = em.merge(oldEmpresaOfMetasProprietariasNewMeta);
                    }
                }
            }
            for (Meta metasAtribuidasNewMeta : metasAtribuidasNew) {
                if (!metasAtribuidasOld.contains(metasAtribuidasNewMeta)) {
                    Empresa oldEmpresaClienteOfMetasAtribuidasNewMeta = metasAtribuidasNewMeta.getEmpresaCliente();
                    metasAtribuidasNewMeta.setEmpresaCliente(empresa);
                    metasAtribuidasNewMeta = em.merge(metasAtribuidasNewMeta);
                    if (oldEmpresaClienteOfMetasAtribuidasNewMeta != null && !oldEmpresaClienteOfMetasAtribuidasNewMeta.equals(empresa)) {
                        oldEmpresaClienteOfMetasAtribuidasNewMeta.getMetasAtribuidas().remove(metasAtribuidasNewMeta);
                        oldEmpresaClienteOfMetasAtribuidasNewMeta = em.merge(oldEmpresaClienteOfMetasAtribuidasNewMeta);
                    }
                }
            }
            for (RelacionamentoEmpresaCliente empresasGestorasNewRelacionamentoEmpresaCliente : empresasGestorasNew) {
                if (!empresasGestorasOld.contains(empresasGestorasNewRelacionamentoEmpresaCliente)) {
                    Empresa oldEmpresaGestoraOfEmpresasGestorasNewRelacionamentoEmpresaCliente = empresasGestorasNewRelacionamentoEmpresaCliente.getEmpresaGestora();
                    empresasGestorasNewRelacionamentoEmpresaCliente.setEmpresaGestora(empresa);
                    empresasGestorasNewRelacionamentoEmpresaCliente = em.merge(empresasGestorasNewRelacionamentoEmpresaCliente);
                    if (oldEmpresaGestoraOfEmpresasGestorasNewRelacionamentoEmpresaCliente != null && !oldEmpresaGestoraOfEmpresasGestorasNewRelacionamentoEmpresaCliente.equals(empresa)) {
                        oldEmpresaGestoraOfEmpresasGestorasNewRelacionamentoEmpresaCliente.getEmpresasGestoras().remove(empresasGestorasNewRelacionamentoEmpresaCliente);
                        oldEmpresaGestoraOfEmpresasGestorasNewRelacionamentoEmpresaCliente = em.merge(oldEmpresaGestoraOfEmpresasGestorasNewRelacionamentoEmpresaCliente);
                    }
                }
            }
            for (RelacionamentoEmpresaCliente empresasGerenciadasNewRelacionamentoEmpresaCliente : empresasGerenciadasNew) {
                if (!empresasGerenciadasOld.contains(empresasGerenciadasNewRelacionamentoEmpresaCliente)) {
                    Empresa oldEmpresaClienteOfEmpresasGerenciadasNewRelacionamentoEmpresaCliente = empresasGerenciadasNewRelacionamentoEmpresaCliente.getEmpresaCliente();
                    empresasGerenciadasNewRelacionamentoEmpresaCliente.setEmpresaCliente(empresa);
                    empresasGerenciadasNewRelacionamentoEmpresaCliente = em.merge(empresasGerenciadasNewRelacionamentoEmpresaCliente);
                    if (oldEmpresaClienteOfEmpresasGerenciadasNewRelacionamentoEmpresaCliente != null && !oldEmpresaClienteOfEmpresasGerenciadasNewRelacionamentoEmpresaCliente.equals(empresa)) {
                        oldEmpresaClienteOfEmpresasGerenciadasNewRelacionamentoEmpresaCliente.getEmpresasGerenciadas().remove(empresasGerenciadasNewRelacionamentoEmpresaCliente);
                        oldEmpresaClienteOfEmpresasGerenciadasNewRelacionamentoEmpresaCliente = em.merge(oldEmpresaClienteOfEmpresasGerenciadasNewRelacionamentoEmpresaCliente);
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
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = empresa.getIdEmpresa();
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
                empresa.getIdEmpresa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empresa with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<UsuarioEmpresa> usuariosOrphanCheck = empresa.getUsuarios();
            for (UsuarioEmpresa usuariosOrphanCheckUsuarioEmpresa : usuariosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empresa (" + empresa + ") cannot be destroyed since the UsuarioEmpresa " + usuariosOrphanCheckUsuarioEmpresa + " in its usuarios field has a non-nullable empresa field.");
            }
            Collection<Meta> metasProprietariasOrphanCheck = empresa.getMetasProprietarias();
            for (Meta metasProprietariasOrphanCheckMeta : metasProprietariasOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empresa (" + empresa + ") cannot be destroyed since the Meta " + metasProprietariasOrphanCheckMeta + " in its metasProprietarias field has a non-nullable empresa field.");
            }
            Collection<Meta> metasAtribuidasOrphanCheck = empresa.getMetasAtribuidas();
            for (Meta metasAtribuidasOrphanCheckMeta : metasAtribuidasOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empresa (" + empresa + ") cannot be destroyed since the Meta " + metasAtribuidasOrphanCheckMeta + " in its metasAtribuidas field has a non-nullable empresaCliente field.");
            }
            Collection<RelacionamentoEmpresaCliente> empresasGestorasOrphanCheck = empresa.getEmpresasGestoras();
            for (RelacionamentoEmpresaCliente empresasGestorasOrphanCheckRelacionamentoEmpresaCliente : empresasGestorasOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empresa (" + empresa + ") cannot be destroyed since the RelacionamentoEmpresaCliente " + empresasGestorasOrphanCheckRelacionamentoEmpresaCliente + " in its empresasGestoras field has a non-nullable empresaGestora field.");
            }
            Collection<RelacionamentoEmpresaCliente> empresasGerenciadasOrphanCheck = empresa.getEmpresasGerenciadas();
            for (RelacionamentoEmpresaCliente empresasGerenciadasOrphanCheckRelacionamentoEmpresaCliente : empresasGerenciadasOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empresa (" + empresa + ") cannot be destroyed since the RelacionamentoEmpresaCliente " + empresasGerenciadasOrphanCheckRelacionamentoEmpresaCliente + " in its empresasGerenciadas field has a non-nullable empresaCliente field.");
            }
            Collection<CentroCusto> centrosDeCustoOrphanCheck = empresa.getCentrosDeCusto();
            for (CentroCusto centrosDeCustoOrphanCheckCentroCusto : centrosDeCustoOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empresa (" + empresa + ") cannot be destroyed since the CentroCusto " + centrosDeCustoOrphanCheckCentroCusto + " in its centrosDeCusto field has a non-nullable empresa field.");
            }
            Collection<Departamento> departamentosOrphanCheck = empresa.getDepartamentos();
            for (Departamento departamentosOrphanCheckDepartamento : departamentosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empresa (" + empresa + ") cannot be destroyed since the Departamento " + departamentosOrphanCheckDepartamento + " in its departamentos field has a non-nullable empresa field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
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
