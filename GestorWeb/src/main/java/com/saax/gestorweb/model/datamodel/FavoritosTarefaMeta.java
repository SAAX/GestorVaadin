package com.saax.gestorweb.model.datamodel;

import java.io.Serializable;
import java.time.LocalDateTime;
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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Entity bean da tabela Favoritos Task Meta com as namequerys configuradas<br><br>
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
    
    @Column(name = "datahorainclusao")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime dataHoraInclusao;
    
    @JoinColumn(name = "idmeta", referencedColumnName = "idmeta")
    @ManyToOne(optional = false)
    private Meta meta;
    
    @JoinColumn(name = "idtarefa", referencedColumnName = "idtarefa")
    @ManyToOne(optional = false)
    private Task tarefa;
    
    @JoinColumn(name = "idusuarioinclusao", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario usuarioInclusao;

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

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Task getTarefa() {
        return tarefa;
    }

    public void setTarefa(Task tarefa) {
        this.tarefa = tarefa;
    }

    public Usuario getUsuarioInclusao() {
        return usuarioInclusao;
    }

    public void setUsuarioInclusao(Usuario usuarioInclusao) {
        this.usuarioInclusao = usuarioInclusao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FavoritosTarefaMeta)) {
            return false;
        }
        FavoritosTarefaMeta other = (FavoritosTarefaMeta) object;
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
        return "com.saax.gestorweb.FavoritosTarefaMeta[ idfavoritostarefameta=" + id + " ]";
    }
    
}
