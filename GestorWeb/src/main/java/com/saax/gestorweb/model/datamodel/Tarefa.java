package com.saax.gestorweb.model.datamodel;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
 * Entity bean da tabela Tarefa com as namequerys configuradas.<br><br>
 * 
 * O objetivo desta entidade é armazenar as Tarefas e subs do sistema <br><br>
 *
 * @author rodrigo
 */
@Entity
@Table(name = "tarefa")
@NamedQueries({
    @NamedQuery(name = "Tarefa.findAll", query = "SELECT t FROM Tarefa t"),
    @NamedQuery(name = "Tarefa.findByNivel", query = "SELECT t FROM Tarefa t WHERE t.nivel = :nivel"),
    @NamedQuery(name = "Tarefa.findByTitulo", query = "SELECT t FROM Tarefa t WHERE t.titulo = :titulo"),
    @NamedQuery(name = "Tarefa.findByNome", query = "SELECT t FROM Tarefa t WHERE t.nome = :nome"),
    @NamedQuery(name = "Tarefa.findByPrioridade", query = "SELECT t FROM Tarefa t WHERE t.prioridade = :prioridade"),
    @NamedQuery(name = "Tarefa.findByDataInicio", query = "SELECT t FROM Tarefa t WHERE t.dataInicio = :dataInicio"),
    @NamedQuery(name = "Tarefa.findByDataFim", query = "SELECT t FROM Tarefa t WHERE t.dataFim = :dataFim"),
    @NamedQuery(name = "Tarefa.findByDataTermino", query = "SELECT t FROM Tarefa t WHERE t.dataTermino = :dataTermino"),
    @NamedQuery(name = "Tarefa.findByStatus", query = "SELECT t FROM Tarefa t WHERE t.status = :status"),
    @NamedQuery(name = "Tarefa.findByAndamento", query = "SELECT t FROM Tarefa t WHERE t.andamento = :andamento"),
    @NamedQuery(name = "Tarefa.findByDescricao", query = "SELECT t FROM Tarefa t WHERE t.descricao = :descricao"),
    @NamedQuery(name = "Tarefa.findByApontamentohoras", query = "SELECT t FROM Tarefa t WHERE t.apontamentoHoras = :apontamentohoras"),
    @NamedQuery(name = "Tarefa.findByOrcamentocontrolado", query = "SELECT t FROM Tarefa t WHERE t.orcamentoControlado = :orcamentocontrolado"),
    @NamedQuery(name = "Tarefa.findByDatahorainclusao", query = "SELECT t FROM Tarefa t WHERE t.dataHoraInclusao = :dataHoraInclusao")})
public class Tarefa implements Serializable {

    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idtarefa")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "nivel")
    private int nivel;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "titulo")
    private String titulo;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "nome")
    private String nome;
    
    @JoinColumn(name = "prioridade", referencedColumnName = "prioridadetarefa")
    @ManyToOne(optional = false)
    private PrioridadeTarefa prioridade;
    
    @JoinColumn(name = "status", referencedColumnName = "statustarefa")
    @ManyToOne(optional = false)
    private StatusTarefa status;
        
    @Basic(optional = false)
    @NotNull
    @Column(name = "andamento")
    private int andamento;

    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "descricao")
    private String descricao;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "apontamentohoras")
    private boolean apontamentoHoras;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "orcamentocontrolado")
    private boolean orcamentoControlado;
    
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTarefa")
    private List<FavoritosTarefaMeta> favoritados;
    
    @JoinColumn(name = "idcentrocusto", referencedColumnName = "idcentrocusto")
    @ManyToOne
    private CentroCusto idCentroCusto;
    
    @JoinColumn(name = "iddepartamento", referencedColumnName = "iddepartamento")
    @ManyToOne
    private Departamento idDepartamento;
    
    @JoinColumn(name = "idempresa", referencedColumnName = "idempresa")
    @ManyToOne(optional = false)
    private Empresa idEmpresa;
    
    @JoinColumn(name = "idempresacliente", referencedColumnName = "idempresacliente")
    @ManyToOne
    private EmpresaCliente empresaCliente;
    
    @OneToMany(mappedBy = "idTarefaPai")
    private List<Tarefa> subTarefas;
    
    @JoinColumn(name = "idproximatarefa", referencedColumnName = "idtarefa")
    @ManyToOne(optional = false)
    private Tarefa idProximaTarefa;
    
    @JoinColumn(name = "tipo", referencedColumnName = "tipotarefa")
    @ManyToOne(optional = false)
    private TipoTarefa tipo;
    
    @JoinColumn(name = "idtarefapai", referencedColumnName = "idtarefa")
    @ManyToOne
    private Tarefa idTarefaPai;
    
    @JoinColumn(name = "idusuarioinclusao", referencedColumnName = "idusuario")
    @ManyToOne
    private Usuario idUsuarioInclusao;

    @JoinColumn(name = "idusuariosolicitante", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario idUsuarioSolicitante;
    
    @JoinColumn(name = "idusuarioresponsavel", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario idUsuarioResponsavel;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTarefa")
    private List<ParicipanteTarefa> paricipantes;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTarefa")
    private List<AvaliacaoMetaTarefa> avaliacoes;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTarefa")
    private List<OrcamentoTarefa> orcamentos;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTarefa")
    private List<ApontamentoTarefa> apontamentoTarefaList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTarefa")
    private List<AnexoTarefa> anexos;

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
    @Column(name = "datahorainclusao")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime dataHoraInclusao;

    public Tarefa() {
    }

    public Tarefa(Integer idtarefa) {
        this.id = idtarefa;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public PrioridadeTarefa getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(PrioridadeTarefa prioridade) {
        this.prioridade = prioridade;
    }

    public int getAndamento() {
        return andamento;
    }

    public void setAndamento(int andamento) {
        this.andamento = andamento;
    }


    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean getApontamentoHoras() {
        return apontamentoHoras;
    }

    public void setApontamentoHoras(boolean apontamentoHoras) {
        this.apontamentoHoras = apontamentoHoras;
    }

    public boolean getOrcamentoControlado() {
        return orcamentoControlado;
    }

    public void setOrcamentoControlado(boolean orcamentoControlado) {
        this.orcamentoControlado = orcamentoControlado;
    }


    public List<FavoritosTarefaMeta> getFavoritados() {
        return favoritados;
    }

    public void setFavoritados(List<FavoritosTarefaMeta> favoritados) {
        this.favoritados = favoritados;
    }

    public CentroCusto getIdCentroCusto() {
        return idCentroCusto;
    }

    public void setIdCentroCusto(CentroCusto idCentroCusto) {
        this.idCentroCusto = idCentroCusto;
    }

    public Departamento getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(Departamento idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public Empresa getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Empresa idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public EmpresaCliente getEmpresaCliente() {
        return empresaCliente;
    }

    public void setEmpresaCliente(EmpresaCliente empresaCliente) {
        this.empresaCliente = empresaCliente;
    }

    public List<Tarefa> getSubTarefas() {
        return subTarefas;
    }

    public void setSubTarefas(List<Tarefa> subTarefas) {
        this.subTarefas = subTarefas;
    }

    public Tarefa getIdTarefaPai() {
        return idTarefaPai;
    }

    public void setIdTarefaPai(Tarefa idTarefaPai) {
        this.idTarefaPai = idTarefaPai;
    }

    public Usuario getIdUsuarioInclusao() {
        return idUsuarioInclusao;
    }

    public void setIdUsuarioInclusao(Usuario idUsuarioInclusao) {
        this.idUsuarioInclusao = idUsuarioInclusao;
    }

    public Usuario getIdUsuarioSolicitante() {
        return idUsuarioSolicitante;
    }

    public void setIdUsuarioSolicitante(Usuario idUsuarioSolicitante) {
        this.idUsuarioSolicitante = idUsuarioSolicitante;
    }

    public Usuario getIdUsuarioResponsavel() {
        return idUsuarioResponsavel;
    }

    public void setIdUsuarioResponsavel(Usuario idUsuarioResponsavel) {
        this.idUsuarioResponsavel = idUsuarioResponsavel;
    }

    public List<ParicipanteTarefa> getParicipantes() {
        return paricipantes;
    }

    public void setParicipantes(List<ParicipanteTarefa> paricipantes) {
        this.paricipantes = paricipantes;
    }

    public List<AvaliacaoMetaTarefa> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<AvaliacaoMetaTarefa> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }

    public List<OrcamentoTarefa> getOrcamentos() {
        return orcamentos;
    }

    public void setOrcamentos(List<OrcamentoTarefa> orcamentos) {
        this.orcamentos = orcamentos;
    }

    public List<ApontamentoTarefa> getApontamentoTarefaList() {
        return apontamentoTarefaList;
    }

    public void setApontamentoTarefaList(List<ApontamentoTarefa> apontamentoTarefaList) {
        this.apontamentoTarefaList = apontamentoTarefaList;
    }

    public List<AnexoTarefa> getAnexos() {
        return anexos;
    }

    public void setAnexos(List<AnexoTarefa> anexos) {
        this.anexos = anexos;
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
        if (!(object instanceof Tarefa)) {
            return false;
        }
        Tarefa other = (Tarefa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.Tarefa[ idtarefa=" + id + " ]";
    }

    public StatusTarefa getStatus() {
        return status;
    }

    public void setStatus(StatusTarefa status) {
        this.status = status;
    }

    public Tarefa getIdProximaTarefa() {
        return idProximaTarefa;
    }

    public void setIdProximaTarefa(Tarefa idProximaTarefa) {
        this.idProximaTarefa = idProximaTarefa;
    }

    public TipoTarefa getTipo() {
        return tipo;
    }

    public void setTipo(TipoTarefa tipo) {
        this.tipo = tipo;
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

    public LocalDateTime getDataHoraInclusao() {
        return dataHoraInclusao;
    }

    public void setDataHoraInclusao(LocalDateTime dataHoraInclusao) {
        this.dataHoraInclusao = dataHoraInclusao;
    }
    
}
