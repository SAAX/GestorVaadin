
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

/**
 * Entity bean da tabela Participante Tarefa com as namequerys configuradas.<br><br>
 * 
 * O objetivo desta entidade é armazenar os usuários Participantes das tarefas/subs <br><br>
 *
 * @author rodrigo
 */
@Entity
@Table(name = "participante")
@NamedQueries({
    @NamedQuery(name = "Participante.findAll", query = "SELECT p FROM Participante p"),
    @NamedQuery(name = "Participante.findByDatahorainclusao", query = "SELECT p FROM Participante p WHERE p.dataHoraInclusao = :dataHoraInclusao")})
public class Participante implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idparticipante")
    private Integer id;
    
    @Column(name = "datahorainclusao")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime dataHoraInclusao;
    
    @JoinColumn(name = "idtarefa", referencedColumnName = "idtarefa")
    @ManyToOne()
    private Tarefa tarefa;

    @JoinColumn(name = "idmeta", referencedColumnName = "idmeta")
    @ManyToOne()
    private Meta meta;
    
    @JoinColumn(name = "idusuarioinclusao", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario usuarioInclusao;
    
    @JoinColumn(name = "idusuarioparticipante", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario usuarioParticipante;

    public Participante() {
    }

    public Participante(Integer idparicipantetarefa) {
        this.id = idparicipantetarefa;
    }

    public Participante(Integer idparicipantetarefa, LocalDateTime dataHoraInclusao) {
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

    public Tarefa getTarefa() {
        return tarefa;
    }

    public void setTarefa(Tarefa tarefa) {
        this.tarefa = tarefa;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta Meta) {
        this.meta = Meta;
    }

    public Usuario getUsuarioInclusao() {
        return usuarioInclusao;
    }

    public void setUsuarioInclusao(Usuario usuarioInclusao) {
        this.usuarioInclusao = usuarioInclusao;
    }

    public Usuario getUsuarioParticipante() {
        return usuarioParticipante;
    }

    public void setUsuarioParticipante(Usuario usuarioParticipante) {
        this.usuarioParticipante = usuarioParticipante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Participante)) {
            return false;
        }
        Participante other = (Participante) object;
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
        return "com.saax.gestorweb.ParicipanteTarefa[ idparicipantetarefa=" + id + " ]";
    }
    
}
