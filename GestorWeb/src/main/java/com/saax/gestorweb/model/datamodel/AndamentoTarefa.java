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
@Table(name = "andamentotarefa")
@NamedQueries({
    @NamedQuery(name = "AndamentoTarefa.findAll", query = "SELECT a FROM AndamentoTarefa a"),
    @NamedQuery(name = "AndamentoTarefa.findByIdandamentotarefa", query = "SELECT a FROM AndamentoTarefa a WHERE a.id = :id"),
    @NamedQuery(name = "AndamentoTarefa.findByComentario", query = "SELECT a FROM AndamentoTarefa a WHERE a.comentario = :comentario"),
    @NamedQuery(name = "AndamentoTarefa.findByAndamentoatual", query = "SELECT a FROM AndamentoTarefa a WHERE a.andamentoatual = :andamentoatual")
    })
public class AndamentoTarefa implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idandamentotarefa")
    private Integer id;
    
    @Size(max = 50)
    @Column(name = "comentario")
    private String comentario;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "andamentoatual")
    private int andamentoatual;
    
    @JoinColumn(name = "idusuarioinclusao", referencedColumnName = "idusuario")
    @ManyToOne
    private Usuario usuarioInclusao;
    
    @Column(name = "datahorainclusao")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime dataHoraInclusao;

    @JoinColumn(name = "idtarefa", referencedColumnName = "idtarefa")
    @ManyToOne(optional = false)
    private Task tarefa;

    
    public AndamentoTarefa() {
    }

    public AndamentoTarefa(Integer idandamentotarefa) {
        this.id = idandamentotarefa;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getAndamentoatual() {
        return andamentoatual;
    }

    public void setAndamentoatual(int andamentoatual) {
        this.andamentoatual = andamentoatual;
    }

    public LocalDateTime getDataHoraInclusao() {
        return dataHoraInclusao;
    }

    public void setDataHoraInclusao(LocalDateTime dataHoraInclusao) {
        this.dataHoraInclusao = dataHoraInclusao;
    }

    

    public void setUsuarioInclusao(Usuario usuarioInclusao) {
        this.usuarioInclusao = usuarioInclusao;
    }

    public Usuario getUsuarioInclusao() {
        return usuarioInclusao;
    }

    public Task getTarefa() {
        return tarefa;
    }

    public void setTarefa(Task tarefa) {
        this.tarefa = tarefa;
    }

    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AndamentoTarefa)) {
            return false;
        }
        AndamentoTarefa other = (AndamentoTarefa) object;
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
        return "com.saax.gestorweb.AndamentoTarefa[ idandamentotarefa=" + id + " ]";
    }
    
  
    
}
