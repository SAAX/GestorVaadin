package com.saax.gestorweb.model.datamodel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
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
@Table(name = "hierarquiaprojeto")
@NamedQueries({
    @NamedQuery(name = "HierarquiaProjeto.findAll", query = "SELECT h FROM HierarquiaProjeto h"),
    @NamedQuery(name = "HierarquiaProjeto.findAllDefault", query = "SELECT h FROM HierarquiaProjeto h WHERE h.empresa IS NULL"),
    @NamedQuery(name = "HierarquiaProjeto.findByEmpresa", query = "SELECT h FROM HierarquiaProjeto h WHERE h.empresa = :empresa"),
    @NamedQuery(name = "HierarquiaProjeto.findByNome", query = "SELECT h FROM HierarquiaProjeto h WHERE h.nome = :nome")})

public class HierarquiaProjeto implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idhierarquiaprojeto")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nome")
    private String nome;
    
    @JoinColumn(name = "idempresa", referencedColumnName = "idempresa")
    @ManyToOne()
    private Empresa empresa;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hierarquia")
    private List<HierarquiaProjetoDetalhe> categorias;

    @Column(name = "datahorainclusao")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime dataHoraInclusao;
    
    @JoinColumn(name = "idusuarioinclusao", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario usuarioInclusao;
    
    public HierarquiaProjeto() {
    }

    public HierarquiaProjeto(Integer idhierarquiaprojeto) {
        this.id = idhierarquiaprojeto;
    }

    public HierarquiaProjeto(Integer idhierarquiaprojeto, String nome) {
        this.id = idhierarquiaprojeto;
        this.nome = nome;
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

    public List<HierarquiaProjetoDetalhe> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<HierarquiaProjetoDetalhe> categorias) {
        this.categorias = categorias;
    }

    public LocalDateTime getDataHoraInclusao() {
        return dataHoraInclusao;
    }

    public void setDataHoraInclusao(LocalDateTime dataHoraInclusao) {
        this.dataHoraInclusao = dataHoraInclusao;
    }

    public Usuario getUsuarioInclusao() {
        return usuarioInclusao;
    }

    public void setUsuarioInclusao(Usuario usuarioInclusao) {
        this.usuarioInclusao = usuarioInclusao;
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
        if (!(object instanceof HierarquiaProjeto)) {
            return false;
        }
        HierarquiaProjeto other = (HierarquiaProjeto) object;
        if ( this == other) return true;

        // se o ID estiver setado, compara por ele
        if ( this.getId() != null) {
            return !((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id)));
            
        } else {
            // senao compara por campos setados na criação da tarefa
            return this.getUsuarioInclusao().equals(other.getUsuarioInclusao())
                   && this. getDataHoraInclusao().equals(other.getDataHoraInclusao())
                   && this. getNome().equals(other.getNome());

        }
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.model.datamodel.HierarquiaProjeto[ idhierarquiaprojeto=" + id + " ]";
    }
    
}
