
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
 * Entity bean da tabela Participante Tarefa com as namequerys configuradas.<br><br>
 * 
 * O objetivo desta entidade é armazenar os usuários Participantes das tarefas/subs <br><br>
 *
 * @author rodrigo
 */
@Entity
@Table(name = "paricipantetarefa")
@NamedQueries({
    @NamedQuery(name = "ParicipanteTarefa.findAll", query = "SELECT p FROM ParicipanteTarefa p"),
    @NamedQuery(name = "ParicipanteTarefa.findByDatahorainclusao", query = "SELECT p FROM ParicipanteTarefa p WHERE p.dataHoraInclusao = :dataHoraInclusao")})
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
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime dataHoraInclusao;
    
    @JoinColumn(name = "idtarefa", referencedColumnName = "idtarefa")
    @ManyToOne(optional = false)
    private Tarefa idTarefa;
    
    @JoinColumn(name = "idusuarioinclusao", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario idUsuarioInclusao;
    
    @JoinColumn(name = "idusuarioparticipante", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario idUsuarioParticipante;

    public ParicipanteTarefa() {
    }

    public ParicipanteTarefa(Integer idparicipantetarefa) {
        this.id = idparicipantetarefa;
    }

    public ParicipanteTarefa(Integer idparicipantetarefa, LocalDateTime dataHoraInclusao) {
        this.id = idparicipantetarefa;
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

    public Usuario getIdUsuarioParticipante() {
        return idUsuarioParticipante;
    }

    public void setIdUsuarioParticipante(Usuario idUsuarioParticipante) {
        this.idUsuarioParticipante = idUsuarioParticipante;
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
