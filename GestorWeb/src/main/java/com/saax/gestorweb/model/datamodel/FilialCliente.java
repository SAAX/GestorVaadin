package com.saax.gestorweb.model.datamodel;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity bean da tabela FilialCliente com as namequerys configuradas<br><br>
 * 
 * O objetivo desta entidade e armazenar as filiais dos nossos Clientes<br><br>
 *
 * @author rodrigo
 */
@Entity
@Table(name = "filialcliente")
@NamedQueries({
    @NamedQuery(name = "FilialCliente.findAll", query = "SELECT f FROM FilialCliente f"),
    @NamedQuery(name = "FilialCliente.findByIdfilialcliente", query = "SELECT f FROM FilialCliente f WHERE f.idFilialCliente = :idfilialcliente"),
    @NamedQuery(name = "FilialCliente.findByNome", query = "SELECT f FROM FilialCliente f WHERE f.nome = :nome"),
    @NamedQuery(name = "FilialCliente.findByCnpj", query = "SELECT f FROM FilialCliente f WHERE f.cnpj = :cnpj"),
    @NamedQuery(name = "FilialCliente.findByAtiva", query = "SELECT f FROM FilialCliente f WHERE f.ativa = :ativa")})
public class FilialCliente implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idfilialcliente")
    private Integer idFilialCliente;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nome")
    private String nome;
    
    @Size(max = 18)
    @Column(name = "cnpj")
    private String cnpj;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "ativa")
    private boolean ativa;
    
    @JoinColumn(name = "idempresacliente", referencedColumnName = "idempresacliente")
    @ManyToOne(optional = false)
    private EmpresaCliente empresaCliente;

    public FilialCliente() {
    }

    public FilialCliente(Integer idfilialcliente) {
        this.idFilialCliente = idfilialcliente;
    }

    public FilialCliente(Integer idfilialcliente, String nome, boolean ativa) {
        this.idFilialCliente = idfilialcliente;
        this.nome = nome;
        this.ativa = ativa;
    }

    public Integer getIdFilialCliente() {
        return idFilialCliente;
    }

    public void setIdFilialCliente(Integer idFilialCliente) {
        this.idFilialCliente = idFilialCliente;
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

    public EmpresaCliente getEmpresaCliente() {
        return empresaCliente;
    }

    public void setEmpresaCliente(EmpresaCliente empresaCliente) {
        this.empresaCliente = empresaCliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFilialCliente != null ? idFilialCliente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FilialCliente)) {
            return false;
        }
        FilialCliente other = (FilialCliente) object;
        if ((this.idFilialCliente == null && other.idFilialCliente != null) || (this.idFilialCliente != null && !this.idFilialCliente.equals(other.idFilialCliente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.model.datamodel.FilialCliente[ idfilialcliente=" + idFilialCliente + " ]";
    }
    
}
