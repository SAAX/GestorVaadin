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
    @NamedQuery(name = "OrcamentoTarefa.findByValor", query = "SELECT o FROM OrcamentoTarefa o WHERE o.valor = :valor"),
    @NamedQuery(name = "OrcamentoTarefa.findBySentido", query = "SELECT o FROM OrcamentoTarefa o WHERE o.sentido = :sentido"),
    @NamedQuery(name = "OrcamentoTarefa.findByObservacoes", query = "SELECT o FROM OrcamentoTarefa o WHERE o.observacoes = :observacoes"),
    @NamedQuery(name = "OrcamentoTarefa.findByDatahorainclusao", query = "SELECT o FROM OrcamentoTarefa o WHERE o.dataHoraInclusao = :dataHoraInclusao")})
public class OrcamentoTarefa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idorcamentotarefa")
    private Integer id;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor", precision = 10, scale = 2)
    private BigDecimal valor;
    
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

    public OrcamentoTarefa() {
    }

    public OrcamentoTarefa(Integer idorcamentotarefa) {
        this.id = idorcamentotarefa;
    }

    public OrcamentoTarefa(Integer idorcamentotarefa, BigDecimal valor, Character sentido, String observacoes, LocalDateTime dataHoraInclusao) {
        this.id = idorcamentotarefa;
        this.valor = valor;
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

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
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
        if (!(object instanceof OrcamentoTarefa)) {
            return false;
        }
        OrcamentoTarefa other = (OrcamentoTarefa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.OrcamentoTarefa[ idorcamentotarefa=" + id + " ]";
    }
    
}
