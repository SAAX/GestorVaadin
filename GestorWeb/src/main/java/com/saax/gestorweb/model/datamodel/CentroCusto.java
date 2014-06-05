/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.model.datamodel;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author rodrigo
 */
@Entity
@Table(name = "centrocusto")
@NamedQueries({
    @NamedQuery(name = "CentroCusto.findAll", query = "SELECT c FROM CentroCusto c"),
    @NamedQuery(name = "CentroCusto.findByIdcentrocusto", query = "SELECT c FROM CentroCusto c WHERE c.idCentroCusto = :idcentrocusto"),
    @NamedQuery(name = "CentroCusto.findByEmpresa", query = "SELECT c FROM CentroCusto c WHERE c.empresa = :empresa"),
    @NamedQuery(name = "CentroCusto.findByCentrocusto", query = "SELECT c FROM CentroCusto c WHERE c.centroCusto = :centrocusto"),
    @NamedQuery(name = "CentroCusto.findByAtivo", query = "SELECT c FROM CentroCusto c WHERE c.ativo = :ativo")})
public class CentroCusto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idcentrocusto")
    private Integer idCentroCusto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "centrocusto")
    private String centroCusto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ativo")
    private boolean ativo;
    @JoinColumn(name = "idempresa", referencedColumnName = "idempresa")
    @ManyToOne(optional = false)
    private Empresa empresa;
    @OneToMany(mappedBy = "centroCusto")
    private Collection<Meta> metas;

    public CentroCusto() {
    }

    public CentroCusto(Integer idcentrocusto) {
        this.idCentroCusto = idcentrocusto;
    }


    public Integer getIdCentroCusto() {
        return idCentroCusto;
    }

    public void setIdCentroCusto(Integer idcentrocusto) {
        this.idCentroCusto = idcentrocusto;
    }


    public String getCentroCusto() {
        return centroCusto;
    }

    public void setCentroCusto(String centroCusto) {
        this.centroCusto = centroCusto;
    }

    public boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Collection<Meta> getMetas() {
        return metas;
    }

    public void setMetas(Collection<Meta> metas) {
        this.metas = metas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCentroCusto != null ? idCentroCusto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CentroCusto)) {
            return false;
        }
        CentroCusto other = (CentroCusto) object;
        if ((this.idCentroCusto == null && other.idCentroCusto != null) || (this.idCentroCusto != null && !this.idCentroCusto.equals(other.idCentroCusto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.model.datamodel.CentroCusto[ idcentrocusto=" + idCentroCusto + " ]";
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    
}
