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

/**
 *
 * @author rodrigo
 */
@Entity
@Table(name = "relacionamentoempresacliente")
@NamedQueries({
    @NamedQuery(name = "RelacionamentoEmpresaCliente.findAll", query = "SELECT r FROM RelacionamentoEmpresaCliente r"),
    @NamedQuery(name = "RelacionamentoEmpresaCliente.findByIdrelacionamentoempresacliente", 
            query = "SELECT r FROM RelacionamentoEmpresaCliente r WHERE r.idRelacionamentoEmpresaCliente = :idrelacionamentoempresacliente"),
    @NamedQuery(name = "RelacionamentoEmpresaCliente.findByContratoativo", 
            query = "SELECT r FROM RelacionamentoEmpresaCliente r WHERE r.contratoAtivo = :contratoativo"),
    @NamedQuery(name = "RelacionamentoEmpresaCliente.findByIniciocontrato", 
            query = "SELECT r FROM RelacionamentoEmpresaCliente r WHERE r.inicioContrato = :iniciocontrato"),
    @NamedQuery(name = "RelacionamentoEmpresaCliente.findByTerminocontrato", 
            query = "SELECT r FROM RelacionamentoEmpresaCliente r WHERE r.terminoContrato = :terminocontrato")})
public class RelacionamentoEmpresaCliente implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idrelacionamentoempresacliente")
    private Integer idRelacionamentoEmpresaCliente;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "contratoativo")
    private boolean contratoAtivo;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "iniciocontrato")
    @Temporal(TemporalType.DATE)
    private Date inicioContrato;
    
    @Column(name = "terminocontrato")
    @Temporal(TemporalType.DATE)
    private Date terminoContrato;
    
    @JoinColumn(name = "idempresagestora", referencedColumnName = "idempresa")
    @ManyToOne(optional = false)
    private Empresa empresaGestora;
    
    @JoinColumn(name = "idempresacliente", referencedColumnName = "idempresa")
    @ManyToOne(optional = false)
    private Empresa empresaCliente;

    public RelacionamentoEmpresaCliente() {
    }

    public RelacionamentoEmpresaCliente(Integer idrelacionamentoempresacliente) {
        this.idRelacionamentoEmpresaCliente = idrelacionamentoempresacliente;
    }

    public RelacionamentoEmpresaCliente(Integer idrelacionamentoempresacliente, boolean contratoativo, Date iniciocontrato) {
        this.idRelacionamentoEmpresaCliente = idrelacionamentoempresacliente;
        this.contratoAtivo = contratoativo;
        this.inicioContrato = iniciocontrato;
    }

    public Integer getIdRelacionamentoEmpresaCliente() {
        return idRelacionamentoEmpresaCliente;
    }

    public void setIdRelacionamentoEmpresaCliente(Integer idRelacionamentoEmpresaCliente) {
        this.idRelacionamentoEmpresaCliente = idRelacionamentoEmpresaCliente;
    }

    public boolean getContratoativo() {
        return contratoAtivo;
    }

    public void setContratoAtivo(boolean contratoAtivo) {
        this.contratoAtivo = contratoAtivo;
    }

    public Date getInicioContrato() {
        return inicioContrato;
    }

    public void setInicioContrato(Date inicioContrato) {
        this.inicioContrato = inicioContrato;
    }

    public Date getTerminoContrato() {
        return terminoContrato;
    }

    public void setTerminoContrato(Date terminoContrato) {
        this.terminoContrato = terminoContrato;
    }

    public Empresa getEmpresaGestora() {
        return empresaGestora;
    }

    public void setEmpresaGestora(Empresa empresaGestora) {
        this.empresaGestora = empresaGestora;
    }

    public Empresa getEmpresaCliente() {
        return empresaCliente;
    }

    public void setEmpresaCliente(Empresa empresaCliente) {
        this.empresaCliente = empresaCliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRelacionamentoEmpresaCliente != null ? idRelacionamentoEmpresaCliente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RelacionamentoEmpresaCliente)) {
            return false;
        }
        RelacionamentoEmpresaCliente other = (RelacionamentoEmpresaCliente) object;
        if ((this.idRelacionamentoEmpresaCliente == null && other.idRelacionamentoEmpresaCliente != null) || (this.idRelacionamentoEmpresaCliente != null && !this.idRelacionamentoEmpresaCliente.equals(other.idRelacionamentoEmpresaCliente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.model.datamodel.RelacionamentoEmpresaCliente[ idrelacionamentoempresacliente=" + idRelacionamentoEmpresaCliente + " ]";
    }
    
}
