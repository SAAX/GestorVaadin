/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.model.datamodel;

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
@Table(name = "paricipantetarefa")
@NamedQueries({
    @NamedQuery(name = "ParicipanteTarefa.findAll", query = "SELECT p FROM ParicipanteTarefa p"),
    @NamedQuery(name = "ParicipanteTarefa.findByIdparicipantetarefa", query = "SELECT p FROM ParicipanteTarefa p WHERE p.idparicipantetarefa = :idparicipantetarefa"),
    @NamedQuery(name = "ParicipanteTarefa.findByDatahorainclusao", query = "SELECT p FROM ParicipanteTarefa p WHERE p.datahorainclusao = :datahorainclusao")})
public class ParicipanteTarefa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idparicipantetarefa")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datahorainclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime datahorainclusao;
    @JoinColumn(name = "idtarefa", referencedColumnName = "idtarefa")
    @ManyToOne(optional = false)
    private Tarefa idtarefa;
    @JoinColumn(name = "idusuarioinclusao", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario idusuarioinclusao;
    @JoinColumn(name = "idusuarioparticipante", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario idusuarioparticipante;

    public ParicipanteTarefa() {
    }

    public ParicipanteTarefa(Integer idparicipantetarefa) {
        this.id = idparicipantetarefa;
    }

    public ParicipanteTarefa(Integer idparicipantetarefa, LocalDateTime datahorainclusao) {
        this.id = idparicipantetarefa;
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

    public Usuario getIdusuarioparticipante() {
        return idusuarioparticipante;
    }

    public void setIdusuarioparticipante(Usuario idusuarioparticipante) {
        this.idusuarioparticipante = idusuarioparticipante;
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
        if (!(object instanceof ParicipanteTarefa)) {
            return false;
        }
        ParicipanteTarefa other = (ParicipanteTarefa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.ParicipanteTarefa[ idparicipantetarefa=" + id + " ]";
    }
    
}
