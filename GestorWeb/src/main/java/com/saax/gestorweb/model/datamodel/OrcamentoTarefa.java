package com.saax.gestorweb.model.datamodel;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * Entity bean da tabela Orcamento Tarefa com as namequerys configuradas.<br><br>
 * 
 * O objetivo desta entidade e armazenar os orçamentos de valores (orçado/realizado) das tarefas / subs <br><br>
 *
 * @author rodrigo
 */
@Entity
@Table(name = "orcamentotarefa")
@NamedQueries({
    @NamedQuery(name = "OrcamentoTarefa.findAll", query = "SELECT o FROM OrcamentoTarefa o"),
    @NamedQuery(name = "OrcamentoTarefa.findByObservacoes", query = "SELECT o FROM OrcamentoTarefa o WHERE o.observacoes = :observacoes"),
    @NamedQuery(name = "OrcamentoTarefa.findByDatahorainclusao", query = "SELECT o FROM OrcamentoTarefa o WHERE o.dataHoraInclusao = :dataHoraInclusao")})
public class OrcamentoTarefa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idorcamentotarefa")
    private Integer id;
    
    /** Valor imputado pelo usuário.
     * campo transiente, pois o valor será gravado em "credito" ou "debito"
     */
    @NotNull(message = "Informe o valor à creditar / debitar")
    private transient String inputValor;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "credito", precision = 10, scale = 2)
    private BigDecimal credito;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "debito", precision = 10, scale = 2)
    private BigDecimal debito;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "saldo", precision = 10, scale = 2)
    private BigDecimal saldo;
    
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

    public OrcamentoTarefa() {
    }

    public OrcamentoTarefa(Integer idorcamentotarefa) {
        this.id = idorcamentotarefa;
    }

    public OrcamentoTarefa(Tarefa tarefa, Usuario usuarioInclusao) {
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

    public BigDecimal getCredito() {
        return credito;
    }

    public void setCredito(BigDecimal credito) {
        this.credito = credito;
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

    public void setDebito(BigDecimal debito) {
        this.debito = debito;
    }

    public BigDecimal getDebito() {
        return debito;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setInputValor(String inputValor) {
        this.inputValor = inputValor;
    }

    public String getInputValor() {
        return inputValor;
    }

    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OrcamentoTarefa)) {
            return false;
        }
        OrcamentoTarefa other = (OrcamentoTarefa) object;
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
        return "com.saax.gestorweb.OrcamentoTarefa[ idorcamentotarefa=" + id + " ]";
    }
    
}
