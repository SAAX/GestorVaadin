/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
import javax.validation.constraints.Size;

/**
 *
 * @author rodrigo
 */
@Entity
@Table(name = "historicotarefa")
@NamedQueries({
    @NamedQuery(name = "HistoricoTarefa.findAll", query = "SELECT h FROM HistoricoTarefa h"),
    @NamedQuery(name = "HistoricoTarefa.findByIdhistoricotarefa", query = "SELECT h FROM HistoricoTarefa h WHERE h.id = :idhistoricotarefa"),
    @NamedQuery(name = "HistoricoTarefa.findByEvento", query = "SELECT h FROM HistoricoTarefa h WHERE h.evento = :evento"),
    @NamedQuery(name = "HistoricoTarefa.findByDatahora", query = "SELECT h FROM HistoricoTarefa h WHERE h.datahora = :datahora")})
public class HistoricoTarefa implements Serializable, Comparable<HistoricoTarefa> {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idhistoricotarefa")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Size(max = 100)
    @Column(name = "evento")
    private String evento;
    
    @JoinColumn(name = "idusuario", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario usuario;

    @JoinColumn(name = "idtarefa", referencedColumnName = "idtarefa")
    @ManyToOne(optional = false)
    private Tarefa tarefa;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "datahora")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime datahora;

    
    public HistoricoTarefa() {
    }

    public HistoricoTarefa(Integer idhistoricotarefa) {
        this.id = idhistoricotarefa;
    }

    public HistoricoTarefa(String evento, Usuario usuario, Tarefa tarefa, LocalDateTime datahora) {
        this.evento = evento;
        this.usuario = usuario;
        this.tarefa = tarefa;
        this.datahora = datahora;
    }

    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public LocalDateTime getDatahora() {
        return datahora;
    }

    public void setDatahora(LocalDateTime datahora) {
        this.datahora = datahora;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setTarefa(Tarefa tarefa) {
        this.tarefa = tarefa;
    }

    public Tarefa getTarefa() {
        return tarefa;
    }
    
    
 
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof HistoricoTarefa)) {
            return false;
        }
        HistoricoTarefa other = (HistoricoTarefa) object;
        if ( this == other) return true;

        // se o ID estiver setado, compara por ele
        if ( this.getId() != null) {
            return !((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id)));
            
        } else {
            // senao compara por campos setados na criação da tarefa
            return this.getUsuario().equals(other.getUsuario())
                   && this.getDatahora().equals(other.getDatahora());

        }
    }
    
    

    @Override
    public String toString() {
        return "com.saax.gestorweb.model.datamodel.HistoricoTarefa[ idhistoricotarefa=" + id + " ]";
    }

      @Override
    public int compareTo(HistoricoTarefa o) {
        return getDatahora().compareTo(o.getDatahora());
    }
    
}
