/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.model.datamodel;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author rodrigo
 */
@Entity
@Table(name = "meta")
@NamedQueries({
    @NamedQuery(name = "Meta.findAll", query = "SELECT m FROM Meta m"),
    @NamedQuery(name = "Meta.findByIdmeta", query = "SELECT m FROM Meta m WHERE m.idMeta = :idmeta"),
    @NamedQuery(name = "Meta.findByNome", query = "SELECT m FROM Meta m WHERE m.nome = :nome"),
    @NamedQuery(name = "Meta.findByDescricao", query = "SELECT m FROM Meta m WHERE m.descricao = :descricao"),
    @NamedQuery(name = "Meta.findByDatainicio", query = "SELECT m FROM Meta m WHERE m.dataInicio = :datainicio"),
    @NamedQuery(name = "Meta.findByDatafim", query = "SELECT m FROM Meta m WHERE m.dataFim = :datafim"),
    @NamedQuery(name = "Meta.findByDatatermino", query = "SELECT m FROM Meta m WHERE m.dataTermino = :datatermino"),
    @NamedQuery(name = "Meta.findByHorasestimadas", query = "SELECT m FROM Meta m WHERE m.horasEstimadas = :horasestimadas"),
    @NamedQuery(name = "Meta.findByHorasrealizadas", query = "SELECT m FROM Meta m WHERE m.horasRealizadas = :horasrealizadas")})
public class Meta implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idmeta")
    private Integer idMeta;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nome")
    private String nome;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "descricao")
    private String descricao;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "datainicio")
    @Temporal(TemporalType.DATE)
    private Date dataInicio;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "datafim")
    @Temporal(TemporalType.DATE)
    private Date dataFim;
    
    @Column(name = "datatermino")
    @Temporal(TemporalType.DATE)
    private Date dataTermino;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "horasestimadas")
    @Temporal(TemporalType.TIME)
    private Date horasEstimadas;
    
    @Column(name = "horasrealizadas")
    @Temporal(TemporalType.TIME)
    private Date horasRealizadas;
    
    @JoinColumn(name = "idCentroCusto", referencedColumnName = "idCentroCusto")
    @ManyToOne
    private CentroCusto centroCusto;
    
    @JoinColumn(name = "idDepartamento", referencedColumnName = "idDepartamento")
    @ManyToOne
    private Departamento departamento;
    
    @JoinColumn(name = "idEmpresa", referencedColumnName = "idEmpresa")
    @ManyToOne(optional = false)
    private Empresa empresa;
    
    @JoinColumn(name = "idempresacliente", referencedColumnName = "idEmpresa")
    @ManyToOne(optional = false)
    private Empresa empresaCliente;
    
    @JoinColumn(name = "idusuarioresponsavel", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario usuarioResponsavel;

    
    public Meta() {
    }

    public Meta(Integer idmeta) {
        this.idMeta = idmeta;
    }

    public Meta(Integer idmeta, String nome, String descricao, Date datainicio, Date datafim, Date horasestimadas) {
        this.idMeta = idmeta;
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = datainicio;
        this.dataFim = datafim;
        this.horasEstimadas = horasestimadas;
    }

    public Integer getIdMeta() {
        return idMeta;
    }

    public void setIdMeta(Integer idMeta) {
        this.idMeta = idMeta;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Date getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(Date dataTermino) {
        this.dataTermino = dataTermino;
    }

    public Date getHorasEstimadas() {
        return horasEstimadas;
    }

    public void setHorasEstimadas(Date horasEstimadas) {
        this.horasEstimadas = horasEstimadas;
    }

    public Date getHorasRealizadas() {
        return horasRealizadas;
    }

    public void setHorasRealizadas(Date horasRealizadas) {
        this.horasRealizadas = horasRealizadas;
    }

    public CentroCusto getCentroCusto() {
        return centroCusto;
    }

    public void setCentroCusto(CentroCusto centroCusto) {
        this.centroCusto = centroCusto;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Empresa getEmpresaCliente() {
        return empresaCliente;
    }

    public void setEmpresaCliente(Empresa empresaCliente) {
        this.empresaCliente = empresaCliente;
    }

    public Usuario getUsuarioResponsavel() {
        return usuarioResponsavel;
    }

    public void setUsuarioResponsavel(Usuario usuarioResponsavel) {
        this.usuarioResponsavel = usuarioResponsavel;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMeta != null ? idMeta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Meta)) {
            return false;
        }
        Meta other = (Meta) object;
        if ((this.idMeta == null && other.idMeta != null) || (this.idMeta != null && !this.idMeta.equals(other.idMeta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.model.datamodel.Meta[ idmeta=" + idMeta + " ]";
    }

}
