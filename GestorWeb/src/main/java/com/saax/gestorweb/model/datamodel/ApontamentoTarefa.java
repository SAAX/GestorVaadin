package com.saax.gestorweb.model.datamodel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
 * Entity bean da tabela Apontamento Tarefa com as namequerys configuradas.<br><br>
 * 
 * O objetivo desta entidade e armazenar os apontamentos de horas das tarefas / subs <br><br>
 *
 * @author rodrigo
 */
@Entity
@Table(name = "apontamentotarefa")
@NamedQueries({
    @NamedQuery(name = "ApontamentoTarefa.findAll", query = "SELECT a FROM ApontamentoTarefa a"),
    @NamedQuery(name = "ApontamentoTarefa.findByHoras", query = "SELECT a FROM ApontamentoTarefa a WHERE a.horas = :horas"),
    @NamedQuery(name = "ApontamentoTarefa.findBySentido", query = "SELECT a FROM ApontamentoTarefa a WHERE a.sentido = :sentido"),
    @NamedQuery(name = "ApontamentoTarefa.findByObservacoes", query = "SELECT a FROM ApontamentoTarefa a WHERE a.observacoes = :observacoes"),
    @NamedQuery(name = "ApontamentoTarefa.findByDatahorainclusao", query = "SELECT a FROM ApontamentoTarefa a WHERE a.dataHoraInclusao = :datahorainclusao")})
public class ApontamentoTarefa implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idapontamentotarefa")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "horas")
    @Convert(converter = LocalTimePersistenceConverter.class)
    private LocalTime horas;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "sentido")
    private Character sentido;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "observacoes")
    private String observacoes;
    
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

    public ApontamentoTarefa() {
    }

    public ApontamentoTarefa(Integer idapontamentotarefa) {
        this.id = idapontamentotarefa;
    }

    public ApontamentoTarefa(Integer idapontamentotarefa, LocalTime horas, Character sentido, String observacoes, LocalDateTime dataHoraInclusao) {
        this.id = idapontamentotarefa;
        this.horas = horas;
        this.sentido = sentido;
        this.observacoes = observacoes;
        this.dataHoraInclusao = dataHoraInclusao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalTime getHoras() {
        return horas;
    }

    public void setHoras(LocalTime horas) {
        this.horas = horas;
    }

    public Character getSentido() {
        return sentido;
    }

    public void setSentido(Character sentido) {
        this.sentido = sentido;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ApontamentoTarefa)) {
            return false;
        }
        ApontamentoTarefa other = (ApontamentoTarefa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.ApontamentoTarefa[ idapontamentotarefa=" + id + " ]";
    }
    
}
