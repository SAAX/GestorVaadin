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
 * Entity bean da tabela Departamento com as namequerys configuradas<br>
 * 
 * O objetivo desta entidade e armazenar os Departamentos para indicação nas metas<br>
 * 
 * @author rodrigo
 */
@Entity
@Table(name = "departamento")
@NamedQueries({
    @NamedQuery(name = "Departamento.findAll", query = "SELECT d FROM Departamento d"),
    @NamedQuery(name = "Departamento.findById", query = "SELECT d FROM Departamento d WHERE d.id = :id"),
    @NamedQuery(name = "Departamento.findByEmpresa", query = "SELECT d FROM Departamento d WHERE d.empresa = :empresa"),
    @NamedQuery(name = "Departamento.findByDepartamento", query = "SELECT d FROM Departamento d WHERE d.departamento = :departamento"),
    @NamedQuery(name = "Departamento.findByAtivo", query = "SELECT d FROM Departamento d WHERE d.ativo = :ativo")})
public class Departamento implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "iddepartamento")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "departamento")
    private String departamento;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "ativo")
    private boolean ativo;
    
    @JoinColumn(name = "idempresa", referencedColumnName = "idempresa")
    @ManyToOne(optional = false)
    private Empresa empresa;
    
    @OneToMany(mappedBy = "departamento")
    private Collection<Meta> metas;

    public Departamento() {
    }

    public Departamento(Integer iddepartamento) {
        this.id = iddepartamento;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    
    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Departamento)) {
            return false;
        }
        Departamento other = (Departamento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.model.datamodel.Departamento[ iddepartamento=" + id + " ]";
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    
}