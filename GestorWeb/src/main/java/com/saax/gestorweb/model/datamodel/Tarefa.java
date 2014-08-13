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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
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
    @NamedQuery(name = "Tarefa.findByEmpresa", query = "SELECT t FROM Tarefa t WHERE t.empresa = :empresa"),
    @NamedQuery(name = "Tarefa.findByPrioridade", query = "SELECT t FROM Tarefa t WHERE t.prioridade = :prioridade"),
    @NamedQuery(name = "Tarefa.findByDataInicio", query = "SELECT t FROM Tarefa t WHERE t.dataInicio = :dataInicio"),
    @NamedQuery(name = "Tarefa.findByDataFim", query = "SELECT t FROM Tarefa t WHERE t.dataFim = :dataFim"),
    @NamedQuery(name = "Tarefa.findByDataTermino", query = "SELECT t FROM Tarefa t WHERE t.dataTermino = :dataTermino"),
    @NamedQuery(name = "Tarefa.findByStatus", query = "SELECT t FROM Tarefa t WHERE t.status = :status"),
    @NamedQuery(name = "Tarefa.findByProjecao", query = "SELECT t FROM Tarefa t WHERE t.projecao = :projecao"),
    @NamedQuery(name = "Tarefa.findByAndamento", query = "SELECT t FROM Tarefa t WHERE t.andamento = :andamento"),
    @NamedQuery(name = "Tarefa.findByDescricao", query = "SELECT t FROM Tarefa t WHERE t.descricao = :descricao"),
    @NamedQuery(name = "Tarefa.findByApontamentohoras", query = "SELECT t FROM Tarefa t WHERE t.apontamentoHoras = :apontamentohoras"),
    @NamedQuery(name = "Tarefa.findByUsuarioResponsavel", query = "SELECT t FROM Tarefa t WHERE t.usuarioResponsavel = :usuarioResponsavel"),
    @NamedQuery(name = "Tarefa.findByOrcamentocontrolado", query = "SELECT t FROM Tarefa t WHERE t.orcamentoControlado = :orcamentocontrolado"),
    @NamedQuery(name = "Tarefa.findByDatahorainclusao", query = "SELECT t FROM Tarefa t WHERE t.dataHoraInclusao = :dataHoraInclusao")})
public class Tarefa implements Serializable {

    @Transient
    private String globalID;

    public String getGlobalID() {
        globalID = GlobalIdMgr.instance().getID(getId(), this.getClass());
        return globalID;
    }
    
    
    
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
    
    @Enumerated(EnumType.STRING)
    private PrioridadeTarefa prioridade;

    @Enumerated(EnumType.STRING)
    private StatusTarefa status;
        
    @Enumerated(EnumType.STRING)
    private ProjecaoTarefa projecao;
        
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
    
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tarefa")
    private List<FavoritosTarefaMeta> favoritados;
    
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
    @ManyToOne
    private EmpresaCliente empresaCliente;
    
    @OneToMany(mappedBy = "tarefaPai")
    private List<Tarefa> subTarefas;
    
    @JoinColumn(name = "idproximatarefa", referencedColumnName = "idtarefa")
    @ManyToOne(optional = false)
    private Tarefa proximaTarefa;
    
    @Enumerated(EnumType.STRING)
    private TipoTarefa tipo;
    
    @JoinColumn(name = "idtarefapai", referencedColumnName = "idtarefa")
    @ManyToOne
    private Tarefa tarefaPai;
    
    @JoinColumn(name = "idusuarioinclusao", referencedColumnName = "idusuario")
    @ManyToOne
    private Usuario usuarioInclusao;

    @JoinColumn(name = "idusuariosolicitante", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario usuarioSolicitante;
    
    @JoinColumn(name = "idusuarioresponsavel", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario usuarioResponsavel;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tarefa")
    private List<ParticipanteTarefa> participantes;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tarefa")
    private List<AvaliacaoMetaTarefa> avaliacoes;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tarefa")
    private List<OrcamentoTarefa> orcamentos;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tarefa")
    private List<ApontamentoTarefa> apontamentos;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tarefa")
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

    public Tarefa getTarefaPai() {
        return tarefaPai;
    }

    public void setTarefaPai(Tarefa tarefaPai) {
        this.tarefaPai = tarefaPai;
    }

    public Usuario getUsuarioInclusao() {
        return usuarioInclusao;
    }

    public void setUsuarioInclusao(Usuario usuarioInclusao) {
        this.usuarioInclusao = usuarioInclusao;
    }

    public Usuario getUsuarioSolicitante() {
        return usuarioSolicitante;
    }

    public void setUsuarioSolicitante(Usuario usuarioSolicitante) {
        this.usuarioSolicitante = usuarioSolicitante;
    }

    public Usuario getUsuarioResponsavel() {
        return usuarioResponsavel;
    }

    public void setUsuarioResponsavel(Usuario usuarioResponsavel) {
        this.usuarioResponsavel = usuarioResponsavel;
    }

    public List<ParticipanteTarefa> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<ParticipanteTarefa> participantes) {
        this.participantes = participantes;
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

    public List<ApontamentoTarefa> getApontamentos() {
        return apontamentos;
    }

    public void setApontamentos(List<ApontamentoTarefa> apontamentos) {
        this.apontamentos = apontamentos;
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

    public ProjecaoTarefa getProjecao() {
        return projecao;
    }

    public void setProjecao(ProjecaoTarefa projecao) {
        this.projecao = projecao;
    }

    public Tarefa getProximaTarefa() {
        return proximaTarefa;
    }

    public void setProximaTarefa(Tarefa proximaTarefa) {
        this.proximaTarefa = proximaTarefa;
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
