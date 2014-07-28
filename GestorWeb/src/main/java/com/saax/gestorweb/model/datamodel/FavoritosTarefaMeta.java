package com.saax.gestorweb.model.datamodel;

import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.Usuario;
import java.io.Serializable;
import java.time.LocalDateTime;
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
@Table(name = "favoritostarefameta")
@NamedQueries({
    @NamedQuery(name = "FavoritosTarefaMeta.findAll", query = "SELECT f FROM FavoritosTarefaMeta f"),
    @NamedQuery(name = "FavoritosTarefaMeta.findByIdfavoritostarefameta", query = "SELECT f FROM FavoritosTarefaMeta f WHERE f.idfavoritostarefameta = :idfavoritostarefameta"),
    @NamedQuery(name = "FavoritosTarefaMeta.findByDatahorainclusao", query = "SELECT f FROM FavoritosTarefaMeta f WHERE f.datahorainclusao = :datahorainclusao")})
public class FavoritosTarefaMeta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idfavoritostarefameta")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datahorainclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime datahorainclusao;
    @JoinColumn(name = "idmeta", referencedColumnName = "idmeta")
    @ManyToOne(optional = false)
    private Meta idmeta;
    @JoinColumn(name = "idtarefa", referencedColumnName = "idtarefa")
    @ManyToOne(optional = false)
    private Tarefa idtarefa;
    @JoinColumn(name = "idusuarioinclusao", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario idusuarioinclusao;

    public FavoritosTarefaMeta() {
    }

    public FavoritosTarefaMeta(Integer idfavoritostarefameta) {
        this.id = idfavoritostarefameta;
    }

    public FavoritosTarefaMeta(Integer idfavoritostarefameta, LocalDateTime datahorainclusao) {
        this.id = idfavoritostarefameta;
        this.datahorainclusao = datahorainclusao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDatahorainclusao() {
        return datahorainclusao;
    }

    public void setDatahorainclusao(LocalDateTime datahorainclusao) {
        this.datahorainclusao = datahorainclusao;
    }

    public Meta getIdmeta() {
        return idmeta;
    }

    public void setIdmeta(Meta idmeta) {
        this.idmeta = idmeta;
    }

    public Tarefa getIdtarefa() {
        return idtarefa;
    }

    public void setIdtarefa(Tarefa idtarefa) {
        this.idtarefa = idtarefa;
    }

    public Usuario getIdusuarioinclusao() {
        return idusuarioinclusao;
    }

    public void setIdusuarioinclusao(Usuario idusuarioinclusao) {
        this.idusuarioinclusao = idusuarioinclusao;
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
        if (!(object instanceof FavoritosTarefaMeta)) {
            return false;
        }
        FavoritosTarefaMeta other = (FavoritosTarefaMeta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.FavoritosTarefaMeta[ idfavoritostarefameta=" + id + " ]";
    }
    
}
