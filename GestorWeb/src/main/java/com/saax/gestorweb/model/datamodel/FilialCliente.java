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
 * Entity bean da tabela Filial Cliente com as namequerys configuradas<br><br>
 * 
 * O objetivo desta entidade e armazenar as Filiais dos clientes dos nossos clientes<br><br>
 * 
 * ATENÇÃO: Esta classe ainda não está completa, mais campos serão adicionados 
 * quando for criado o cadastro de empresas e clientes<br>
 * 
 * @author rodrigo
 */
@Entity
@Table(name = "filialcliente")
@NamedQueries({
    @NamedQuery(name = "FilialCliente.findAll", query = "SELECT f FROM FilialCliente f"),
    @NamedQuery(name = "FilialCliente.findByIdfilialcliente", query = "SELECT f FROM FilialCliente f WHERE f.id = :id"),
    @NamedQuery(name = "FilialCliente.findByNome", query = "SELECT f FROM FilialCliente f WHERE f.nome = :nome"),
    @NamedQuery(name = "FilialCliente.findByAtiva", query = "SELECT f FROM FilialCliente f WHERE f.ativa = :ativa")})
public class FilialCliente implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idfilialcliente")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nome")
    private String nome;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "ativa")
    private boolean ativa;
    
    @JoinColumn(name = "idempresacliente", referencedColumnName = "idempresacliente")
    @ManyToOne(optional = false)
    private EmpresaCliente matriz;

    public FilialCliente() {
    }

    public FilialCliente(Integer idfilialcliente) {
        this.id = idfilialcliente;
    }

    public FilialCliente(Integer idfilialcliente, String nome, boolean ativa) {
        this.id = idfilialcliente;
        this.nome = nome;
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

    public boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public EmpresaCliente getMatriz() {
        return matriz;
    }

    public void setMatriz(EmpresaCliente matriz) {
        this.matriz = matriz;
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
        if (!(object instanceof FilialCliente)) {
            return false;
        }
        FilialCliente other = (FilialCliente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.model.datamodel.FilialCliente[ idfilialcliente=" + id + " ]";
    }
    
}
