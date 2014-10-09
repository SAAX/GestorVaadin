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
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * Entity bean da tabela Apontamento Tarefa com as namequerys configuradas.<br><br>
 
 O objetivo desta entidade e armazenar os apontamentos de creditoHoras das tarefas / subs <br><br>
 *
 * @author rodrigo
 */
@Entity
@Table(name = "apontamentotarefa")
public class ApontamentoTarefa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idapontamentotarefa")
    private Integer id;

    /** Horas imputadas pelo usuário.
     * campo transiente, pois as horas serão gravadas em "creditoHoras" ou "debitoHoras"
     */
    private transient String inputHoras;
    
    @Column(name = "creditohoras")
    @Convert(converter = LocalTimePersistenceConverter.class)
    private LocalTime creditoHoras;

    @Column(name = "debitohoras")
    @Convert(converter = LocalTimePersistenceConverter.class)
    private LocalTime debitoHoras;

    @Column(name = "saldohoras")
    @Convert(converter = LocalTimePersistenceConverter.class)
    private LocalTime saldoHoras;
        
    @Column(name = "custohora", precision = 10, scale = 3)
    private BigDecimal custoHora;
    
    @Column(name = "creditovalor", precision = 10, scale = 2)
    private BigDecimal creditoValor;
    
    @Column(name = "debitovalor", precision = 10, scale = 2)
    private BigDecimal debitoValor;
    
    @Column(name = "saldovalor", precision = 10, scale = 2)
    private BigDecimal saldoValor;
    
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
        this.dataHoraInclusao = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalTime getCreditoHoras() {
        return creditoHoras;
    }

    public void setCreditoHoras(LocalTime creditoHoras) {
        this.creditoHoras = creditoHoras;
    }

    public LocalTime getDebitoHoras() {
        return debitoHoras;
    }

    public void setDebitoHoras(LocalTime debitoHoras) {
        this.debitoHoras = debitoHoras;
    }

    public LocalTime getSaldoHoras() {
        return saldoHoras;
    }

    public void setSaldoHoras(LocalTime saldoHoras) {
        this.saldoHoras = saldoHoras;
    }

    public BigDecimal getCustoHora() {
        return custoHora;
    }

    public void setCustoHora(BigDecimal custoHora) {
        this.custoHora = custoHora;
    }

    public BigDecimal getCreditoValor() {
        return creditoValor;
    }

    public void setCreditoValor(BigDecimal creditoValor) {
        this.creditoValor = creditoValor;
    }

    public BigDecimal getDebitoValor() {
        return debitoValor;
    }

    public void setDebitoValor(BigDecimal debitoValor) {
        this.debitoValor = debitoValor;
    }

    public BigDecimal getSaldoValor() {
        return saldoValor;
    }

    public void setSaldoValor(BigDecimal saldoValor) {
        this.saldoValor = saldoValor;
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

    public void setInputHoras(String inputHoras) {
        this.inputHoras = inputHoras;
    }

    public String getInputHoras() {
        return inputHoras;
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
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.ApontamentoTarefa[ idapontamentotarefa=" + id + " ]";
    }

    
}
