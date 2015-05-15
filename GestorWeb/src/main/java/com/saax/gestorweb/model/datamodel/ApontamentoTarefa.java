package com.saax.gestorweb.model.datamodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
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
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * Entity bean da tabela Apontamento Task com as namequerys configuradas.<br><br>
 
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
    @Convert(converter = DurationConverter.class)
    private Duration creditoHoras;

    @Column(name = "debitohoras")
    @Convert(converter = DurationConverter.class)
    private Duration debitoHoras;

    @Column(name = "saldohoras")
    @Convert(converter = DurationConverter.class)
    private Duration saldoHoras;
        
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
    private Task tarefa;
    
    @JoinColumn(name = "idusuarioinclusao", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario usuarioInclusao;

    public ApontamentoTarefa() {
    }

    public ApontamentoTarefa(Integer idapontamentotarefa) {
        this.id = idapontamentotarefa;
    }

    public ApontamentoTarefa(Task tarefa, Usuario usuarioInclusao) {
        this.tarefa = tarefa;
        this.usuarioInclusao = usuarioInclusao;
        this.dataHoraInclusao = LocalDateTime.now();
        if (tarefa.getCustoHoraApontamento()!=null){
            this.custoHora = tarefa.getCustoHoraApontamento();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Duration getCreditoHoras() {
        return creditoHoras;
    }

    public void setCreditoHoras(Duration creditoHoras) {
        this.creditoHoras = creditoHoras;
    }

    public Duration getDebitoHoras() {
        return debitoHoras;
    }

    public void setDebitoHoras(Duration debitoHoras) {
        this.debitoHoras = debitoHoras;
    }

    public Duration getSaldoHoras() {
        return saldoHoras;
    }

    public void setSaldoHoras(Duration saldoHoras) {
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

    public Task getTarefa() {
        return tarefa;
    }

    public void setTarefa(Task tarefa) {
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
        if (!(object instanceof ApontamentoTarefa)) {
            return false;
        }
        ApontamentoTarefa other = (ApontamentoTarefa) object;
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
        return "com.saax.gestorweb.ApontamentoTarefa[ idapontamentotarefa=" + id + " ]";
    }

    
}
