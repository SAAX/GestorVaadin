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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity bean da tabela Empresa Cliente com as namequerys configuradas<br><br>
 * 
 * O objetivo desta entidade e armazenar os clientes do nossos clientes<br><br>
 * 
 * ATENÇÃO: Esta classe ainda não está completa, mais campos serão adicionados 
 * quando for criado o cadastro de empresas e clientes<br>
 * 
 * @author rodrigo
 */
@Entity
@Table(name = "empresacliente")
@NamedQueries({
    @NamedQuery(name = "Empresacliente.findAll", query = "SELECT e FROM EmpresaCliente e"),
    @NamedQuery(name = "Empresacliente.findById", query = "SELECT e FROM EmpresaCliente e WHERE e.id = :id"),
    @NamedQuery(name = "Empresacliente.findByNome", query = "SELECT e FROM EmpresaCliente e WHERE e.nome = :nome"),
    @NamedQuery(name = "Empresacliente.findByCnpj", query = "SELECT e FROM EmpresaCliente e WHERE e.cnpj = :cnpj"),
    @NamedQuery(name = "Empresacliente.findByAtiva", query = "SELECT e FROM EmpresaCliente e WHERE e.ativa = :ativa")})
public class EmpresaCliente implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idempresacliente")
    private Integer id;
    
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
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cliente")
    private Collection<Meta> metas;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "matriz")
    private Collection<FilialCliente> filiais;
    
    @OneToMany(mappedBy = "empresaPrincipal")
    private Collection<EmpresaCliente> subEmpresas;
    
    @JoinColumn(name = "idempresaclienteprincipal", referencedColumnName = "idempresacliente")
    @ManyToOne
    private EmpresaCliente empresaPrincipal;

    public EmpresaCliente() {
    }

    public EmpresaCliente(Integer idempresacliente) {
        this.id = idempresacliente;
    }

    public EmpresaCliente(Integer idempresacliente, String nome, String cnpj, boolean ativa) {
        this.id = idempresacliente;
        this.nome = nome;
        this.cnpj = cnpj;
        this.ativa = ativa;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Collection<Meta> getMetas() {
        return metas;
    }

    public void setMetas(Collection<Meta> metas) {
        this.metas = metas;
    }

    public Collection<FilialCliente> getFiliais() {
        return filiais;
    }

    public void setFiliais(Collection<FilialCliente> filiais) {
        this.filiais = filiais;
    }

    public Collection<EmpresaCliente> getSubEmpresas() {
        return subEmpresas;
    }

    public void setSubEmpresas(Collection<EmpresaCliente> subEmpresas) {
        this.subEmpresas = subEmpresas;
    }

    public EmpresaCliente getEmpresaPrincipal() {
        return empresaPrincipal;
    }

    public void setEmpresaPrincipal(EmpresaCliente empresaPrincipal) {
        this.empresaPrincipal = empresaPrincipal;
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
        if (!(object instanceof EmpresaCliente)) {
            return false;
        }
        EmpresaCliente other = (EmpresaCliente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.model.datamodel.Empresacliente[ idempresacliente=" + id + " ]";
    }
    
}
