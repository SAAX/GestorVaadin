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
@Table(name = "usuarioempresa")
@NamedQueries({
    @NamedQuery(name = "UsuarioEmpresa.findAll", query = "SELECT u FROM UsuarioEmpresa u"),
    @NamedQuery(name = "UsuarioEmpresa.findByIdusuarioempresa", 
            query = "SELECT u FROM UsuarioEmpresa u WHERE u.idUsuarioEmpresa = :idusuarioempresa"),
    @NamedQuery(name = "UsuarioEmpresa.findByAdministrador", query = "SELECT u FROM UsuarioEmpresa u WHERE u.administrador = :administrador"),
    @NamedQuery(name = "UsuarioEmpresa.findByContratacao", query = "SELECT u FROM UsuarioEmpresa u WHERE u.contratacao = :contratacao"),
    @NamedQuery(name = "UsuarioEmpresa.findByDesligamento", query = "SELECT u FROM UsuarioEmpresa u WHERE u.desligamento = :desligamento")})
public class UsuarioEmpresa implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idusuarioempresa")
    private Integer idUsuarioEmpresa;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "administrador")
    private boolean administrador;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "contratacao")
    @Temporal(TemporalType.DATE)
    private Date contratacao;
    
    @Column(name = "desligamento")
    @Temporal(TemporalType.DATE)
    private Date desligamento;
    
    @JoinColumn(name = "idempresa", referencedColumnName = "idempresa")
    @ManyToOne(optional = false)
    private Empresa empresa;
    
    @JoinColumn(name = "idusuario", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario usuario;

    public UsuarioEmpresa() {
    }

    public UsuarioEmpresa(Integer idusuarioempresa) {
        this.idUsuarioEmpresa = idusuarioempresa;
    }

    public UsuarioEmpresa(Integer idusuarioempresa, boolean administrador, Date contratacao) {
        this.idUsuarioEmpresa = idusuarioempresa;
        this.administrador = administrador;
        this.contratacao = contratacao;
    }

    public Integer getIdUsuarioEmpresa() {
        return idUsuarioEmpresa;
    }

    public void setIdUsuarioEmpresa(Integer idUsuarioEmpresa) {
        this.idUsuarioEmpresa = idUsuarioEmpresa;
    }

    public boolean getAdministrador() {
        return administrador;
    }

    public void setAdministrador(boolean administrador) {
        this.administrador = administrador;
    }

    public Date getContratacao() {
        return contratacao;
    }

    public void setContratacao(Date contratacao) {
        this.contratacao = contratacao;
    }

    public Date getDesligamento() {
        return desligamento;
    }

    public void setDesligamento(Date desligamento) {
        this.desligamento = desligamento;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuarioEmpresa != null ? idUsuarioEmpresa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioEmpresa)) {
            return false;
        }
        UsuarioEmpresa other = (UsuarioEmpresa) object;
        if ((this.idUsuarioEmpresa == null && other.idUsuarioEmpresa != null) || (this.idUsuarioEmpresa != null && !this.idUsuarioEmpresa.equals(other.idUsuarioEmpresa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.model.datamodel.UsuarioEmpresa[ idusuarioempresa=" + idUsuarioEmpresa + " ]";
    }
    
}
