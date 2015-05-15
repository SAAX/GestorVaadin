package com.saax.gestorweb.model.datamodel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author rodrigo
 */
@Entity
@Table(name = "hierarquiaprojetodetalhe")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HierarquiaProjetoDetalhe.findAll", query = "SELECT h FROM HierarquiaProjetoDetalhe h"),
    @NamedQuery(name = "HierarquiaProjetoDetalhe.findByNivel", query = "SELECT h FROM HierarquiaProjetoDetalhe h WHERE h.nivel = :nivel"),
    @NamedQuery(name = "HierarquiaProjetoDetalhe.findByCategoria", query = "SELECT h FROM HierarquiaProjetoDetalhe h WHERE h.categoria = :categoria")})
public class HierarquiaProjetoDetalhe implements Serializable, Comparable<HierarquiaProjetoDetalhe> {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idhierarquiaprojetodetalhe")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "nivel")
    private int nivel;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "categoria")
    private String categoria;
    
    @Column(name = "datahorainclusao")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime dataHoraInclusao;
    
    @JoinColumn(name = "idusuarioinclusao", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario usuarioInclusao;

    @JoinColumn(name = "idhierarquiaprojeto", referencedColumnName = "idhierarquiaprojeto")
    @ManyToOne(optional = false)
    private HierarquiaProjeto hierarquia;

    
    @OneToMany(mappedBy = "hierarquia")
    private List<Task> tarefas;
    
    public HierarquiaProjetoDetalhe() {
    }

    public HierarquiaProjetoDetalhe(Integer idhierarquiaprojetodetalhe) {
        this.id = idhierarquiaprojetodetalhe;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public HierarquiaProjeto getHierarquia() {
        return hierarquia;
    }

    public void setHierarquia(HierarquiaProjeto hierarquia) {
        this.hierarquia = hierarquia;
    }

    public void setUsuarioInclusao(Usuario usuarioInclusao) {
        this.usuarioInclusao = usuarioInclusao;
    }

    public Usuario getUsuarioInclusao() {
        return usuarioInclusao;
    }

    public void setDataHoraInclusao(LocalDateTime dataHoraInclusao) {
        this.dataHoraInclusao = dataHoraInclusao;
    }

    public LocalDateTime getDataHoraInclusao() {
        return dataHoraInclusao;
    }

    public void setTarefas(List<Task> tarefas) {
        this.tarefas = tarefas;
    }

    public List<Task> getTarefas() {
        return tarefas;
    }

    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof HierarquiaProjetoDetalhe)) {
            return false;
        }
        HierarquiaProjetoDetalhe other = (HierarquiaProjetoDetalhe) object;
        if ( this == other) return true;

        // se o ID estiver setado, compara por ele
        if ( this.getId() != null) {
            return !((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id)));
            
        } else {
            // senao compara por campos setados na criação da tarefa
            return this.getUsuarioInclusao().equals(other.getUsuarioInclusao())
                   && this. getDataHoraInclusao().equals(other.getDataHoraInclusao());

        }
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe[ idhierarquiaprojetodetalhe=" + id + " ]";
    }

    @Override
    public int compareTo(HierarquiaProjetoDetalhe o) {
        return Integer.compare(getNivel(), o.getNivel());
    }
    
}
