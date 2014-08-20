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
@Table(name = "bloqueiotarefa", catalog = "gestor", schema = "public")
@NamedQueries({
    @NamedQuery(name = "BloqueioTarefa.findAll", query = "SELECT b FROM BloqueioTarefa b"),
    @NamedQuery(name = "BloqueioTarefa.findById", query = "SELECT b FROM BloqueioTarefa b WHERE b.id= :id"),
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
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "datahorainclusao")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime dataHoraInclusao;
    
    @Column(name = "datahoraremocao")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime dataHoraRemocao;
    
    @JoinColumn(name = "idtarefa", referencedColumnName = "idtarefa")
    @ManyToOne(optional = false)
    private Tarefa tarefa;

    
    @JoinColumn(name = "idusuarioinclusao", referencedColumnName = "idusuario")
    @ManyToOne
    private Usuario usuarioInclusao;
    
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


    public Tarefa getTarefa() {
        return tarefa;
    }

    public void setTarefa(Tarefa tarefa) {
        this.tarefa = tarefa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BloqueioTarefa)) {
            return false;
        }
        BloqueioTarefa other = (BloqueioTarefa) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
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
    
}
