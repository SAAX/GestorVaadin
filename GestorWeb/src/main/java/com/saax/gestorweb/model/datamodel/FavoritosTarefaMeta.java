package com.saax.gestorweb.model.datamodel;

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
 * Entity bean da tabela Favoritos Tarefa Meta com as namequerys configuradas<br><br>
 * 
 * O objetivo desta entidade e armazenar as metas e tarefas favoritas dos usuários<br><br>
 *
 * @author rodrigo
 */
@Entity
@Table(name = "favoritostarefameta")
@NamedQueries({
    @NamedQuery(name = "FavoritosTarefaMeta.findAll", query = "SELECT f FROM FavoritosTarefaMeta f"),
    @NamedQuery(name = "FavoritosTarefaMeta.findByDatahorainclusao", query = "SELECT f FROM FavoritosTarefaMeta f WHERE f.dataHoraInclusao = :dataHoraInclusao")})
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
    private LocalDateTime dataHoraInclusao;
    
    @JoinColumn(name = "idmeta", referencedColumnName = "idmeta")
    @ManyToOne(optional = false)
    private Meta idMeta;
    
    @JoinColumn(name = "idtarefa", referencedColumnName = "idtarefa")
    @ManyToOne(optional = false)
    private Tarefa idTarefa;
    
    @JoinColumn(name = "idusuarioinclusao", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario idUsuarioInclusao;

    public FavoritosTarefaMeta() {
    }

    public FavoritosTarefaMeta(Integer idfavoritostarefameta) {
        this.id = idfavoritostarefameta;
    }

    public FavoritosTarefaMeta(Integer idfavoritostarefameta, LocalDateTime dataHoraInclusao) {
        this.id = idfavoritostarefameta;
        this.dataHoraInclusao = dataHoraInclusao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDataHoraInclusao() {
        return dataHoraInclusao;
    }

    public void setDataHoraInclusao(LocalDateTime dataHoraInclusao) {
        this.dataHoraInclusao = dataHoraInclusao;
    }

    public Meta getIdMeta() {
        return idMeta;
    }

    public void setIdMeta(Meta idMeta) {
        this.idMeta = idMeta;
    }

    public Tarefa getIdTarefa() {
        return idTarefa;
    }

    public void setIdTarefa(Tarefa idTarefa) {
        this.idTarefa = idTarefa;
    }

    public Usuario getIdUsuarioInclusao() {
        return idUsuarioInclusao;
    }

    public void setIdUsuarioInclusao(Usuario idUsuarioInclusao) {
        this.idUsuarioInclusao = idUsuarioInclusao;
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
