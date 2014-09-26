package com.saax.gestorweb.model.datamodel;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity bean da tabela Meta com as namequerys configuradas.<br><br>
 * 
 * O objetivo desta entidade e armazenar as Metas gerenciadas<br><br>
 *
 * @author rodrigo
 */
@Entity
@Table(name = "meta")
@NamedQueries({
    @NamedQuery(name = "Meta.findAll", query = "SELECT m FROM Meta m"),
    @NamedQuery(name = "Meta.findById", query = "SELECT m FROM Meta m WHERE m.id= :id"),
    @NamedQuery(name = "Meta.findByNome", query = "SELECT m FROM Meta m WHERE m.nome = :nome"),
    @NamedQuery(name = "Meta.findByDescricao", query = "SELECT m FROM Meta m WHERE m.descricao = :descricao"),
    @NamedQuery(name = "Meta.findByDatainicio", query = "SELECT m FROM Meta m WHERE m.dataInicio = :datainicio"),
    @NamedQuery(name = "Meta.findByDatafim", query = "SELECT m FROM Meta m WHERE m.dataFim = :datafim"),
    @NamedQuery(name = "Meta.findByDatatermino", query = "SELECT m FROM Meta m WHERE m.dataTermino = :datatermino"),
    @NamedQuery(name = "Meta.findByHorasestimadas", query = "SELECT m FROM Meta m WHERE m.horasEstimadas = :horasestimadas"),
    @NamedQuery(name = "Meta.findByHorasrealizadas", query = "SELECT m FROM Meta m WHERE m.horasRealizadas = :horasrealizadas")})
public class Meta implements Serializable {

    private static final long serialVersionUID = 1L;
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idmeta")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nome")
    private String nome;
    
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "descricao")
    private String descricao;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "datainicio")
    @Convert(converter = LocalDatePersistenceConverter.class)
    private LocalDate dataInicio;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "datafim")
    @Convert(converter = LocalDatePersistenceConverter.class)
    private LocalDate dataFim;
    
    @Column(name = "datatermino")
    @Convert(converter = LocalDatePersistenceConverter.class)
    private LocalDate dataTermino;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "horasestimadas")
    @Convert(converter = LocalTimePersistenceConverter.class)
    private LocalTime horasEstimadas;
    
    @Column(name = "horasrealizadas")
    @Convert(converter = LocalTimePersistenceConverter.class)
    private LocalTime horasRealizadas;
    
    @JoinColumn(name = "idcentrocusto", referencedColumnName = "idcentrocusto")
    @ManyToOne
    private CentroCusto centroCusto;
    
    @JoinColumn(name = "iddepartamento", referencedColumnName = "iddepartamento")
    @ManyToOne
    private Departamento departamento;
    
    @JoinColumn(name = "idempresa", referencedColumnName = "idempresa")
    @ManyToOne(optional = false)
    private Empresa empresa;
    
    @JoinColumn(name = "idempresacliente", referencedColumnName = "idempresacliente")
    @ManyToOne(optional = false)
    private EmpresaCliente cliente;
    
    @JoinColumn(name = "idusuarioresponsavel", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario usuarioResponsavel;

    @Column(name = "datahorainclusao")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime dataHoraInclusao;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "meta")
    private List<FavoritosTarefaMeta> favoritados;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "meta")
    private List<AvaliacaoMetaTarefa> avaliacoes;
    
    @JoinColumn(name = "idusuarioinclusao", referencedColumnName = "idusuario")
    @ManyToOne
    private Usuario usuarioInclusao;

    public Meta() {
    }

    public Meta(Integer idmeta) {
        this.id = idmeta;
    }

    public Meta(Integer idmeta, String nome, String descricao, LocalDate datainicio, LocalDate datafim, LocalTime horasestimadas) {
        this.id = idmeta;
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = datainicio;
        this.dataFim = datafim;
        this.horasEstimadas = horasestimadas;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public LocalDate getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(LocalDate dataTermino) {
        this.dataTermino = dataTermino;
    }

    public LocalTime getHorasEstimadas() {
        return horasEstimadas;
    }

    public void setHorasEstimadas(LocalTime horasEstimadas) {
        this.horasEstimadas = horasEstimadas;
    }

    public LocalTime getHorasRealizadas() {
        return horasRealizadas;
    }

    public void setHorasRealizadas(LocalTime horasRealizadas) {
        this.horasRealizadas = horasRealizadas;
    }

    public CentroCusto getCentroCusto() {
        return centroCusto;
    }

    public void setCentroCusto(CentroCusto centroCusto) {
        this.centroCusto = centroCusto;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public EmpresaCliente getCliente() {
        return cliente;
    }

    public void setCliente(EmpresaCliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getUsuarioResponsavel() {
        return usuarioResponsavel;
    }

    public void setUsuarioResponsavel(Usuario usuarioResponsavel) {
        this.usuarioResponsavel = usuarioResponsavel;
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
        if (!(object instanceof Meta)) {
            return false;
        }
        Meta other = (Meta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.model.datamodel.Meta[ idmeta=" + id + " ]";
    }

    public LocalDateTime getDataHoraInclusao() {
        return dataHoraInclusao;
    }

    public void setDataHoraInclusao(LocalDateTime dataHoraInclusao) {
        this.dataHoraInclusao = dataHoraInclusao;
    }

    public List<FavoritosTarefaMeta> getFavoritados() {
        return favoritados;
    }

    public void setFavoritados(List<FavoritosTarefaMeta> favoritados) {
        this.favoritados = favoritados;
    }

    public List<AvaliacaoMetaTarefa> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<AvaliacaoMetaTarefa> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }

    public Usuario getUsuarioInclusao() {
        return usuarioInclusao;
    }

    public void setUsuarioInclusao(Usuario usuarioInclusao) {
        this.usuarioInclusao = usuarioInclusao;
    }

}
