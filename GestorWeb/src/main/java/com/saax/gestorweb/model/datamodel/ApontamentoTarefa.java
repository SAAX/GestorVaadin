package com.saax.gestorweb.model.datamodel;

import java.io.Serializable;
import java.math.BigDecimal;
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
    
    private static long serialVersionUID = 1L;

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
    
    @Column(name = "custohora", precision = 10, scale = 3)
    private BigDecimal custoHora;
    
    @Column(name = "custototal", precision = 10, scale = 2)
    private BigDecimal custoTotal;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "observacoes")
    private String observacoes;
    
    @Column(name = "datahorainclusao")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime dataHoraInclusao;
    
    @JoinColumn(name = "idtarefa", referencedColumnName = "idtarefa")
    @ManyToOne(optional = false)
    private Tarefa tarefa;
    
    @JoinColumn(name = "idusuarioinclusao", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario usuarioInclusao;

    public ApontamentoTarefa() {
    }

    public ApontamentoTarefa(Integer idapontamentotarefa) {
        this.id = idapontamentotarefa;
    }

    public ApontamentoTarefa(Tarefa tarefa, Usuario usuarioInclusao) {
        this.tarefa = tarefa;
        this.usuarioInclusao = usuarioInclusao;
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

    public Tarefa getTarefa() {
        return tarefa;
    }

    public void setTarefa(Tarefa tarefa) {
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

    /**
     * @return the custoHora
     */
    public BigDecimal getCustoHora() {
        return custoHora;
    }

    /**
     * @param custoHora the custoHora to set
     */
    public void setCustoHora(BigDecimal custoHora) {
        this.custoHora = custoHora;
    }

    /**
     * @return the custoTotal
     */
    public BigDecimal getCustoTotal() {
        return custoTotal;
    }

    /**
     * @param custoTotal the custoTotal to set
     */
    public void setCustoTotal(BigDecimal custoTotal) {
        this.custoTotal = custoTotal;
    }

    
}
