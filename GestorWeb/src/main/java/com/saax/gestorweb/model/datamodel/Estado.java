package com.saax.gestorweb.model.datamodel;

import java.io.Serializable;
import java.util.List;
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
 * Entity bean da tabela Estado com as namequerys configuradas<br><br>
 * 
 * O objetivo desta entidade e armazenar os Estados dos enderecos<br><br>
 *
 * @author rodrigo
 */
@Entity
@Table(name = "estado")
@NamedQueries({
    @NamedQuery(name = "Estado.findAll", query = "SELECT e FROM Estado e"),
    @NamedQuery(name = "Estado.findByNome", query = "SELECT e FROM Estado e WHERE e.nome = :nome"),
    @NamedQuery(name = "Estado.findByUf", query = "SELECT e FROM Estado e WHERE e.uf = :uf")})
public class Estado implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idestado")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nome")
    private String nome;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "uf")
    private String uf;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "estado")
    private List<Cidade> cidades;

    public Estado() {
    }

    public Estado(Integer idestado) {
        this.id = idestado;
    }

    public Estado(Integer idestado, String nome, String uf) {
        this.id = idestado;
        this.nome = nome;
        this.uf = uf;
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

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public List<Cidade> getCidades() {
        return cidades;
    }

    public void setCidades(List<Cidade> cidades) {
        this.cidades = cidades;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Estado)) {
            return false;
        }
        Estado other = (Estado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;

    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.model.datamodel.Estado[ idestado=" + id + " ]";
    }
    
}
