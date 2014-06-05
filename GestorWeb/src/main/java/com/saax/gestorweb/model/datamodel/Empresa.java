/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.model.datamodel;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author rodrigo
 */
@Entity
@Table(name = "empresa")
@NamedQueries({
    @NamedQuery(name = "Empresa.findAll", query = "SELECT e FROM Empresa e"),
    @NamedQuery(name = "Empresa.findByIdempresa", query = "SELECT e FROM Empresa e WHERE e.idEmpresa = :idempresa"),
    @NamedQuery(name = "Empresa.findByNome", query = "SELECT e FROM Empresa e WHERE e.nome = :nome"),
    @NamedQuery(name = "Empresa.findByCnpj", query = "SELECT e FROM Empresa e WHERE e.cnpj = :cnpj"),
    @NamedQuery(name = "Empresa.findByAtiva", query = "SELECT e FROM Empresa e WHERE e.ativa = :ativa")})
public class Empresa implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idempresa")
    private Integer idEmpresa;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nome")
    private String nome;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 18)
    @Column(name = "cnpj")
    private String cnpj;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "ativa")
    private boolean ativa;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresa")
    private Collection<CentroCusto> centrosDeCusto;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresa")
    private Collection<Departamento> departamentos;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresa")
    private Collection<UsuarioEmpresa> usuarios;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresa")
    private Collection<Meta> metasProprietarias;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresaCliente")
    private Collection<Meta> metasAtribuidas;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresaGestora")
    private Collection<RelacionamentoEmpresaCliente> empresasGestoras;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresaCliente")
    private Collection<RelacionamentoEmpresaCliente> empresasGerenciadas;
    

    public Empresa() {
    }

    public Empresa(Integer idempresa) {
        this.idEmpresa = idempresa;
    }

    public Empresa(Integer idempresa, String nome, String cnpj, boolean ativa) {
        this.idEmpresa = idempresa;
        this.nome = nome;
        this.cnpj = cnpj;
        this.ativa = ativa;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public Collection<UsuarioEmpresa> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Collection<UsuarioEmpresa> usuarios) {
        this.usuarios = usuarios;
    }

    public Collection<Meta> getMetasProprietarias() {
        return metasProprietarias;
    }

    public void setMetasProprietarias(Collection<Meta> metasProprietarias) {
        this.metasProprietarias = metasProprietarias;
    }

    public Collection<Meta> getMetasAtribuidas() {
        return metasAtribuidas;
    }

    public void setMetasAtribuidas(Collection<Meta> metasAtribuidas) {
        this.metasAtribuidas = metasAtribuidas;
    }

    public Collection<RelacionamentoEmpresaCliente> getEmpresasGestoras() {
        return empresasGestoras;
    }

    public void setEmpresasGestoras(Collection<RelacionamentoEmpresaCliente> empresasGestoras) {
        this.empresasGestoras = empresasGestoras;
    }

    public Collection<RelacionamentoEmpresaCliente> getEmpresasGerenciadas() {
        return empresasGerenciadas;
    }

    public void setEmpresasGerenciadas(Collection<RelacionamentoEmpresaCliente> empresasGerenciadas) {
        this.empresasGerenciadas = empresasGerenciadas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEmpresa != null ? idEmpresa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empresa)) {
            return false;
        }
        Empresa other = (Empresa) object;
        if ((this.idEmpresa == null && other.idEmpresa != null) || (this.idEmpresa != null && !this.idEmpresa.equals(other.idEmpresa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.model.datamodel.Empresa[ idempresa=" + idEmpresa + " ]";
    }

    public Collection<CentroCusto> getCentrosDeCusto() {
        return centrosDeCusto;
    }

    public void setCentrosDeCusto(Collection<CentroCusto> centrosDeCusto) {
        this.centrosDeCusto = centrosDeCusto;
    }

    public Collection<Departamento> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(Collection<Departamento> departamentos) {
        this.departamentos = departamentos;
    }
    
}
