/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.model.datamodel;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
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
    @NamedQuery(name = "Tarefa.findByTipo", query = "SELECT t FROM Tarefa t WHERE t.tipo = :tipo"),
    @NamedQuery(name = "Tarefa.findByStatus", query = "SELECT t FROM Tarefa t WHERE t.status = :status"),
    @NamedQuery(name = "Tarefa.findByAndamento", query = "SELECT t FROM Tarefa t WHERE t.andamento = :andamento"),
    @NamedQuery(name = "Tarefa.findByDatainicio", query = "SELECT t FROM Tarefa t WHERE t.datainicio = :datainicio"),
    @NamedQuery(name = "Tarefa.findByDatafim", query = "SELECT t FROM Tarefa t WHERE t.datafim = :datafim"),
    @NamedQuery(name = "Tarefa.findByDatatermino", query = "SELECT t FROM Tarefa t WHERE t.datatermino = :datatermino"),
    @NamedQuery(name = "Tarefa.findByDescricao", query = "SELECT t FROM Tarefa t WHERE t.descricao = :descricao"),
    @NamedQuery(name = "Tarefa.findByApontamentohoras", query = "SELECT t FROM Tarefa t WHERE t.apontamentohoras = :apontamentohoras"),
    @NamedQuery(name = "Tarefa.findByOrcamentocontrolado", query = "SELECT t FROM Tarefa t WHERE t.orcamentocontrolado = :orcamentocontrolado"),
    @NamedQuery(name = "Tarefa.findByDatahorainclusao", query = "SELECT t FROM Tarefa t WHERE t.datahorainclusao = :datahorainclusao")})
public class Tarefa implements Serializable {
    @JoinColumn(name = "status", referencedColumnName = "statustarefa")
    @ManyToOne(optional = false)
    private Statustarefa status;
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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "prioridade")
    private String prioridade;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "andamento")
    private int andamento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datainicio")
    @Temporal(TemporalType.DATE)
    private LocalDate datainicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datafim")
    @Temporal(TemporalType.DATE)
    private LocalDate datafim;
    @Column(name = "datatermino")
    @Temporal(TemporalType.DATE)
    private LocalDate datatermino;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "descricao")
    private String descricao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "apontamentohoras")
    private boolean apontamentohoras;
    @Basic(optional = false)
    @NotNull
    @Column(name = "orcamentocontrolado")
    private boolean orcamentocontrolado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datahorainclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime datahorainclusao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idtarefa")
    private List<FavoritosTarefaMeta> favoritosTarefaMetaList;
    @JoinColumn(name = "idcentrocusto", referencedColumnName = "idcentrocusto")
    @ManyToOne
    private CentroCusto idcentrocusto;
    @JoinColumn(name = "iddepartamento", referencedColumnName = "iddepartamento")
    @ManyToOne
    private Departamento iddepartamento;
    @JoinColumn(name = "idempresa", referencedColumnName = "idempresa")
    @ManyToOne(optional = false)
    private Empresa idempresa;
    @JoinColumn(name = "idempresacliente", referencedColumnName = "idempresacliente")
    @ManyToOne
    private EmpresaCliente idempresacliente;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idtarefaproxima")
    private List<Tarefa> tarefaList;
    @JoinColumn(name = "idtarefaproxima", referencedColumnName = "idtarefa")
    @ManyToOne(optional = false)
    private Tarefa idtarefaproxima;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idtarefaanterior")
    private List<Tarefa> tarefaList1;
    @JoinColumn(name = "idtarefaanterior", referencedColumnName = "idtarefa")
    @ManyToOne(optional = false)
    private Tarefa idtarefaanterior;
    @OneToMany(mappedBy = "idtarefapai")
    private List<Tarefa> tarefaList2;
    @JoinColumn(name = "idtarefapai", referencedColumnName = "idtarefa")
    @ManyToOne
    private Tarefa idtarefapai;
    @JoinColumn(name = "idusuarioinclusao", referencedColumnName = "idusuario")
    @ManyToOne
    private Usuario idusuarioinclusao;
    @JoinColumn(name = "idusuariosolicitante", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario idusuariosolicitante;
    @JoinColumn(name = "idusuarioresponsavel", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario idusuarioresponsavel;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idtarefa")
    private List<ParicipanteTarefa> paricipanteTarefaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idtarefa")
    private List<AvaliacaoMetaTarefa> avaliacaoMetaTarefaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idtarefa")
    private List<OrcamentoTarefa> orcamentoTarefaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTarefa")
    private List<ApontamentoTarefa> apontamentoTarefaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTarefa")
    private List<AnexoTarefa> anexoTarefaList;

    public Tarefa() {
    }

    public Tarefa(Integer idtarefa) {
        this.id = idtarefa;
    }

    public Tarefa(Integer idtarefa, int nivel, String titulo, String nome, String prioridade, String tipo, int andamento, LocalDate datainicio, LocalDate datafim, String descricao, boolean apontamentohoras, boolean orcamentocontrolado, LocalDateTime datahorainclusao) {
        this.id = idtarefa;
        this.nivel = nivel;
        this.titulo = titulo;
        this.nome = nome;
        this.prioridade = prioridade;
        this.tipo = tipo;
        this.andamento = andamento;
        this.datainicio = datainicio;
        this.datafim = datafim;
        this.descricao = descricao;
        this.apontamentohoras = apontamentohoras;
        this.orcamentocontrolado = orcamentocontrolado;
        this.datahorainclusao = datahorainclusao;
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

    public String getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    public int getAndamento() {
        return andamento;
    }

    public void setAndamento(int andamento) {
        this.andamento = andamento;
    }

    public LocalDate getDatainicio() {
        return datainicio;
    }

    public void setDatainicio(LocalDate datainicio) {
        this.datainicio = datainicio;
    }

    public LocalDate getDatafim() {
        return datafim;
    }

    public void setDatafim(LocalDate datafim) {
        this.datafim = datafim;
    }

    public LocalDate getDatatermino() {
        return datatermino;
    }

    public void setDatatermino(LocalDate datatermino) {
        this.datatermino = datatermino;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean getApontamentohoras() {
        return apontamentohoras;
    }

    public void setApontamentohoras(boolean apontamentohoras) {
        this.apontamentohoras = apontamentohoras;
    }

    public boolean getOrcamentocontrolado() {
        return orcamentocontrolado;
    }

    public void setOrcamentocontrolado(boolean orcamentocontrolado) {
        this.orcamentocontrolado = orcamentocontrolado;
    }

    public LocalDateTime getDatahorainclusao() {
        return datahorainclusao;
    }

    public void setDatahorainclusao(LocalDateTime datahorainclusao) {
        this.datahorainclusao = datahorainclusao;
    }

    public List<FavoritosTarefaMeta> getFavoritosTarefaMetaList() {
        return favoritosTarefaMetaList;
    }

    public void setFavoritosTarefaMetaList(List<FavoritosTarefaMeta> favoritosTarefaMetaList) {
        this.favoritosTarefaMetaList = favoritosTarefaMetaList;
    }

    public CentroCusto getIdcentrocusto() {
        return idcentrocusto;
    }

    public void setIdcentrocusto(CentroCusto idcentrocusto) {
        this.idcentrocusto = idcentrocusto;
    }

    public Departamento getIddepartamento() {
        return iddepartamento;
    }

    public void setIddepartamento(Departamento iddepartamento) {
        this.iddepartamento = iddepartamento;
    }

    public Empresa getIdempresa() {
        return idempresa;
    }

    public void setIdempresa(Empresa idempresa) {
        this.idempresa = idempresa;
    }

    public EmpresaCliente getIdempresacliente() {
        return idempresacliente;
    }

    public void setIdempresacliente(EmpresaCliente idempresacliente) {
        this.idempresacliente = idempresacliente;
    }

    public List<Tarefa> getTarefaList() {
        return tarefaList;
    }

    public void setTarefaList(List<Tarefa> tarefaList) {
        this.tarefaList = tarefaList;
    }

    public Tarefa getIdtarefaproxima() {
        return idtarefaproxima;
    }

    public void setIdtarefaproxima(Tarefa idtarefaproxima) {
        this.idtarefaproxima = idtarefaproxima;
    }

    public List<Tarefa> getTarefaList1() {
        return tarefaList1;
    }

    public void setTarefaList1(List<Tarefa> tarefaList1) {
        this.tarefaList1 = tarefaList1;
    }

    public Tarefa getIdtarefaanterior() {
        return idtarefaanterior;
    }

    public void setIdtarefaanterior(Tarefa idtarefaanterior) {
        this.idtarefaanterior = idtarefaanterior;
    }

    public List<Tarefa> getTarefaList2() {
        return tarefaList2;
    }

    public void setTarefaList2(List<Tarefa> tarefaList2) {
        this.tarefaList2 = tarefaList2;
    }

    public Tarefa getIdtarefapai() {
        return idtarefapai;
    }

    public void setIdtarefapai(Tarefa idtarefapai) {
        this.idtarefapai = idtarefapai;
    }

    public Usuario getIdusuarioinclusao() {
        return idusuarioinclusao;
    }

    public void setIdusuarioinclusao(Usuario idusuarioinclusao) {
        this.idusuarioinclusao = idusuarioinclusao;
    }

    public Usuario getIdusuariosolicitante() {
        return idusuariosolicitante;
    }

    public void setIdusuariosolicitante(Usuario idusuariosolicitante) {
        this.idusuariosolicitante = idusuariosolicitante;
    }

    public Usuario getIdusuarioresponsavel() {
        return idusuarioresponsavel;
    }

    public void setIdusuarioresponsavel(Usuario idusuarioresponsavel) {
        this.idusuarioresponsavel = idusuarioresponsavel;
    }

    public List<ParicipanteTarefa> getParicipanteTarefaList() {
        return paricipanteTarefaList;
    }

    public void setParicipanteTarefaList(List<ParicipanteTarefa> paricipanteTarefaList) {
        this.paricipanteTarefaList = paricipanteTarefaList;
    }

    public List<AvaliacaoMetaTarefa> getAvaliacaoMetaTarefaList() {
        return avaliacaoMetaTarefaList;
    }

    public void setAvaliacaoMetaTarefaList(List<AvaliacaoMetaTarefa> avaliacaoMetaTarefaList) {
        this.avaliacaoMetaTarefaList = avaliacaoMetaTarefaList;
    }

    public List<OrcamentoTarefa> getOrcamentoTarefaList() {
        return orcamentoTarefaList;
    }

    public void setOrcamentoTarefaList(List<OrcamentoTarefa> orcamentoTarefaList) {
        this.orcamentoTarefaList = orcamentoTarefaList;
    }

    public List<ApontamentoTarefa> getApontamentoTarefaList() {
        return apontamentoTarefaList;
    }

    public void setApontamentoTarefaList(List<ApontamentoTarefa> apontamentoTarefaList) {
        this.apontamentoTarefaList = apontamentoTarefaList;
    }

    public List<AnexoTarefa> getAnexoTarefaList() {
        return anexoTarefaList;
    }

    public void setAnexoTarefaList(List<AnexoTarefa> anexoTarefaList) {
        this.anexoTarefaList = anexoTarefaList;
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

    public Statustarefa getStatus() {
        return status;
    }

    public void setStatus(Statustarefa status) {
        this.status = status;
    }
    
}