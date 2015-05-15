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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author rodrigo
 */
@Entity
@Table(name = "bloqueiotarefa")
@NamedQueries({
    @NamedQuery(name = "BloqueioTarefa.findAll", query = "SELECT b FROM BloqueioTarefa b"),
    @NamedQuery(name = "BloqueioTarefa.findById", query = "SELECT b FROM BloqueioTarefa b WHERE b.id= :id"),
    @NamedQuery(name = "BloqueioTarefa.findByTarefa", query = "SELECT b FROM BloqueioTarefa b WHERE b.tarefa = :tarefa"),
    @NamedQuery(name = "BloqueioTarefa.findByMotivo", query = "SELECT b FROM BloqueioTarefa b WHERE b.motivo = :motivo")})
public class BloqueioTarefa implements Serializable {
    
    private static long serialVersionUID = 1L;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idbloqueiotarefa")
    private Integer id;
    
    @Size(max = 50)
    @Column(name = "motivo")
    private String motivo;
    
    @Column(name = "datahorainclusao")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime dataHoraInclusao;
    
    @Column(name = "datahoraremocao")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime dataHoraRemocao;
    
    @Enumerated(EnumType.STRING)
    private StatusTarefa status;

    @JoinColumn(name = "idtarefa", referencedColumnName = "idtarefa")
    @ManyToOne(optional = false)
    private Task tarefa;
    
    @JoinColumn(name = "idusuarioinclusao", referencedColumnName = "idusuario")
    @ManyToOne
    private Usuario usuarioInclusao;
    
    @JoinColumn(name = "idusuarioremocao", referencedColumnName = "idusuario")
    @ManyToOne
    private Usuario usuarioRemocao;
    
    public BloqueioTarefa() {
    }

    public BloqueioTarefa(Integer idbloqueiotarefa) {
        this.id = idbloqueiotarefa;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }


    public Task getTarefa() {
        return tarefa;
    }

    public void setTarefa(Task tarefa) {
        this.tarefa = tarefa;
    }

    public StatusTarefa getStatus() {
        return status;
    }

    public void setStatus(StatusTarefa status) {
        this.status = status;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BloqueioTarefa)) {
            return false;
        }
        BloqueioTarefa other = (BloqueioTarefa) object;
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
        return "com.saax.gestorweb.model.datamodel.BloqueioTarefa[ idbloqueiotarefa=" + getId() + " ]";
    }

    /**
     * @return the dataHoraInclusao
     */
    public LocalDateTime getDataHoraInclusao() {
        return dataHoraInclusao;
    }

    /**
     * @param dataHoraInclusao the dataHoraInclusao to set
     */
    public void setDataHoraInclusao(LocalDateTime dataHoraInclusao) {
        this.dataHoraInclusao = dataHoraInclusao;
    }

    /**
     * @return the dataHoraRemocao
     */
    public LocalDateTime getDataHoraRemocao() {
        return dataHoraRemocao;
    }

    /**
     * @param dataHoraRemocao the dataHoraRemocao to set
     */
    public void setDataHoraRemocao(LocalDateTime dataHoraRemocao) {
        this.dataHoraRemocao = dataHoraRemocao;
    }

    /**
     * @return the usuarioInclusao
     */
    public Usuario getUsuarioInclusao() {
        return usuarioInclusao;
    }

    /**
     * @param usuarioInclusao the usuarioInclusao to set
     */
    public void setUsuarioInclusao(Usuario usuarioInclusao) {
        this.usuarioInclusao = usuarioInclusao;
    }

    public Usuario getUsuarioRemocao() {
        return usuarioRemocao;
    }

    public void setUsuarioRemocao(Usuario usuarioRemocao) {
        this.usuarioRemocao = usuarioRemocao;
    }

    
    
  
    
    
}
