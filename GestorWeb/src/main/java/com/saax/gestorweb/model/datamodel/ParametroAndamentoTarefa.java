/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.model.datamodel;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author rodrigo
 */
@Entity
@Table(name = "parametroandamentotarefa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ParametroAndamentoTarefa.findAll", query = "SELECT p FROM ParametroAndamentoTarefa p"),
    @NamedQuery(name = "ParametroAndamentoTarefa.findDefault", query = "SELECT p FROM ParametroAndamentoTarefa p WHERE p.empresa IS NULL"),
    @NamedQuery(name = "ParametroAndamentoTarefa.findByEmpresa", query = "SELECT p FROM ParametroAndamentoTarefa p WHERE p.empresa = :empresa"),
    @NamedQuery(name = "ParametroAndamentoTarefa.findByPercentualandamento", query = "SELECT p FROM ParametroAndamentoTarefa p WHERE p.percentualandamento = :percentualandamento"),
    @NamedQuery(name = "ParametroAndamentoTarefa.findByDescricaoandamento", query = "SELECT p FROM ParametroAndamentoTarefa p WHERE p.descricaoandamento = :descricaoandamento")})
public class ParametroAndamentoTarefa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idparametroandamentotarefa")
    private Integer id;

    @JoinColumn(name = "idempresa", referencedColumnName = "idempresa")
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Empresa empresa;

    @Basic(optional = false)
    @NotNull
    @Column(name = "percentualandamento")
    private int percentualandamento;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "descricaoandamento")
    private String descricaoandamento;

    public ParametroAndamentoTarefa() {
    }

    public ParametroAndamentoTarefa(Integer idparametroandamentotarefa) {
        this.id = idparametroandamentotarefa;
    }

    public ParametroAndamentoTarefa(Integer idparametroandamentotarefa, int percentualandamento, String descricaoandamento) {
        this.id = idparametroandamentotarefa;
        this.percentualandamento = percentualandamento;
        this.descricaoandamento = descricaoandamento;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getPercentualandamento() {
        return percentualandamento;
    }

    public void setPercentualandamento(int percentualandamento) {
        this.percentualandamento = percentualandamento;
    }

    public String getDescricaoandamento() {
        return descricaoandamento;
    }

    public void setDescricaoandamento(String descricaoandamento) {
        this.descricaoandamento = descricaoandamento;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ParametroAndamentoTarefa)) {
            return false;
        }
        ParametroAndamentoTarefa other = (ParametroAndamentoTarefa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.model.datamodel.ParametroAndamentoTarefa[ idparametroandamentotarefa=" + id + " ]";
    }

}
