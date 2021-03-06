package com.saax.gestorweb.model.datamodel;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
 * Entity bean da tabela Meta com as namequerys configuradas.<br><br>
 * 
 * O objetivo desta entidade e armazenar as Metas gerenciadas<br><br>
 *
 * @author rodrigo
 */
@Entity
@Table(name = "meta")
@NamedQueries({
    @NamedQuery(name = "Meta.findAll", query = "SELECT m FROM Meta m WHERE m.empresa = :empresa AND m.dataHoraRemocao IS NULL"),
    @NamedQuery(name = "Meta.findByNome", query = "SELECT m FROM Meta m WHERE m.empresa = :empresa AND m.nome = :nome AND m.dataHoraRemocao IS NULL"),
    @NamedQuery(name = "Meta.findMetaRemovidas", query = "SELECT m FROM Meta m WHERE m.usuarioRemocao = :usuarioRemocao ORDER BY m.dataHoraRemocao DESC "),    
    @NamedQuery(name = "Meta.findByDescricao", query = "SELECT m FROM Meta m WHERE m.empresa = :empresa AND m.descricao = :descricao AND m.dataHoraRemocao IS NULL"),
    @NamedQuery(name = "Meta.findByDatainicio", query = "SELECT m FROM Meta m WHERE m.empresa = :empresa AND m.dataInicio = :datainicio AND m.dataHoraRemocao IS NULL"),
    @NamedQuery(name = "Meta.findByDataFim", query = "SELECT m FROM Meta m WHERE m.empresa = :empresa AND m.dataFim <= :datafim AND m.dataHoraRemocao IS NULL"),
    @NamedQuery(name = "Meta.findByUsuarioResponsavelDashboard", query = "SELECT m FROM Meta m WHERE m.usuarioResponsavel = :usuarioResponsavel AND m.dataHoraRemocao IS NULL"),
    @NamedQuery(name = "Meta.findByDatatermino", query = "SELECT m FROM Meta m WHERE m.empresa = :empresa AND m.dataTermino = :datatermino AND m.dataHoraRemocao IS NULL"),
})
public class Meta implements Serializable {
/**
     * Apresentação tratada do ID
     */
    @Transient
    private String globalID;

    public String getGlobalID() {
        globalID = GlobalIdMgr.instance().getID(getId(), this.getClass());

        return globalID;
    }
    
    private static long serialVersionUID = 1L;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idmeta")
    private Integer id;
    
    @NotNull(message = "Informe a empresa")
    @JoinColumn(name = "idempresa", referencedColumnName = "idempresa")
    @ManyToOne(optional = false)
    private Empresa empresa;
    
    @JoinColumn(name = "idfilialempresa", referencedColumnName = "idfilialempresa")
    @ManyToOne()
    private FilialEmpresa filialEmpresa;
    
    @Basic(optional = false)
    @NotNull(message = "Informe o nome da meta")
    @Size(min = 5, max = 100, message = "Nome deve ter de 5 a 100 letras.")
    @Column(name = "nome")
    private String nome;

    @Basic(optional = false)
    @NotNull(message = "Informe a data de início")
    @Column(name = "datainicio")
    @Convert(converter = LocalDatePersistenceConverter.class)
    private LocalDate dataInicio;
    
    @Column(name = "datafim")
    @Convert(converter = LocalDatePersistenceConverter.class)
    private LocalDate dataFim;
    
    @Column(name = "datatermino")
    @Convert(converter = LocalDatePersistenceConverter.class)
    private LocalDate dataTermino;
    
    @Column(name = "descricao")
    private String descricao;
    
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Informe a prioridade da meta: Baixa, Normal ou Alta.")
    private PrioridadeMeta prioridade;

    @Enumerated(EnumType.STRING)
    @NotNull()
    private StatusMeta status;
    
    @JoinColumn(name = "idempresacliente", referencedColumnName = "idempresacliente")
    @ManyToOne()
    private EmpresaCliente cliente;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "meta", orphanRemoval = true)
    private List<Participante> participantes;
    
    @Column(name = "template")
    private boolean template;
    
    @JoinColumn(name = "idcentrocusto", referencedColumnName = "idcentrocusto")
    @ManyToOne
    private CentroCusto centroCusto;
    
    @JoinColumn(name = "iddepartamento", referencedColumnName = "iddepartamento")
    @ManyToOne
    private Departamento departamento;
    
    @JoinColumn(name = "idusuarioinclusao", referencedColumnName = "idusuario")
    @ManyToOne
    private Usuario usuarioInclusao;

    @JoinColumn(name = "idusuariosolicitante", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario usuarioSolicitante;

    @JoinColumn(name = "idusuarioresponsavel", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario usuarioResponsavel;

    @Column(name = "datahorainclusao")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime dataHoraInclusao;

    @JoinColumn(name = "idusuarioremocao", referencedColumnName = "idusuario")
    @ManyToOne
    private Usuario usuarioRemocao;

    @Column(name = "datahoraremocao")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime dataHoraRemocao;

    @NotNull(message = "Informe a categoria")
    @JoinColumn(name = "idhierarquiaprojetodetalhe", referencedColumnName = "idhierarquiaprojetodetalhe")
    @ManyToOne(optional = false)
    private HierarquiaProjetoDetalhe categoria;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "meta")
    private List<Tarefa> tarefas;
    
    public Meta() {
    }

    public Meta(Integer idmeta) {
        this.id = idmeta;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Meta)) {
            return false;
        }
        Meta other = (Meta) object;
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
        return "com.saax.gestorweb.model.datamodel.Meta[ idmeta=" + id + " ]";
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the empresa
     */
    public Empresa getEmpresa() {
        return empresa;
    }

    /**
     * @param empresa the empresa to set
     */
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    /**
     * @return the filialEmpresa
     */
    public FilialEmpresa getFilialEmpresa() {
        return filialEmpresa;
    }

    /**
     * @param filialEmpresa the filialEmpresa to set
     */
    public void setFilialEmpresa(FilialEmpresa filialEmpresa) {
        this.filialEmpresa = filialEmpresa;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the dataInicio
     */
    public LocalDate getDataInicio() {
        return dataInicio;
    }

    /**
     * @param dataInicio the dataInicio to set
     */
    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    /**
     * @return the dataFim
     */
    public LocalDate getDataFim() {
        return dataFim;
    }

    /**
     * @param dataFim the dataFim to set
     */
    public void setDataFim(LocalDate dataFim) {
       this.dataFim = dataFim;
    }

    /**
     * @return the dataTermino
     */
    public LocalDate getDataTermino() {
        return dataTermino;
    }

    /**
     * @param dataTermino the dataTermino to set
     */
    public void setDataTermino(LocalDate dataTermino) {
        this.dataTermino = dataTermino;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the prioridade
     */
    public PrioridadeMeta getPrioridade() {
        return prioridade;
    }

    /**
     * @param prioridade the prioridade to set
     */
    public void setPrioridade(PrioridadeMeta prioridade) {
        this.prioridade = prioridade;
    }

    /**
     * @return the status
     */
    public StatusMeta getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(StatusMeta status) {
        this.status = status;
    }

    /**
     * @return the cliente
     */
    public EmpresaCliente getCliente() {
        return cliente;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(EmpresaCliente cliente) {
        this.cliente = cliente;
    }

    /**
     * @return the template
     */
    public boolean isTemplate() {
        return template;
    }

    /**
     * @param template the template to set
     */
    public void setTemplate(boolean template) {
        this.template = template;
    }

    /**
     * @return the centroCusto
     */
    public CentroCusto getCentroCusto() {
        return centroCusto;
    }

    /**
     * @param centroCusto the centroCusto to set
     */
    public void setCentroCusto(CentroCusto centroCusto) {
        this.centroCusto = centroCusto;
    }

    /**
     * @return the departamento
     */
    public Departamento getDepartamento() {
        return departamento;
    }

    /**
     * @param departamento the departamento to set
     */
    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    /**
     * @return the usuarioInclusao
     */
    public Usuario getUsuarioInclusao() {
        return usuarioInclusao;
    }

    /**
     * @param usuarioInclusao the usuarioInclusao to set
     */
    public void setUsuarioInclusao(Usuario usuarioInclusao) {
        this.usuarioInclusao = usuarioInclusao;
    }

    /**
     * @return the usuarioSolicitante
     */
    public Usuario getUsuarioSolicitante() {
        return usuarioSolicitante;
    }

    /**
     * @param usuarioSolicitante the usuarioSolicitante to set
     */
    public void setUsuarioSolicitante(Usuario usuarioSolicitante) {
        this.usuarioSolicitante = usuarioSolicitante;
    }

    /**
     * @return the usuarioResponsavel
     */
    public Usuario getUsuarioResponsavel() {
        return usuarioResponsavel;
    }

    /**
     * @param usuarioResponsavel the usuarioResponsavel to set
     */
    public void setUsuarioResponsavel(Usuario usuarioResponsavel) {
        this.usuarioResponsavel = usuarioResponsavel;
    }

    /**
     * @return the dataHoraInclusao
     */
    public LocalDateTime getDataHoraInclusao() {
        return dataHoraInclusao;
    }

    /**
     * @param dataHoraInclusao the dataHoraInclusao to set
     */
    public void setDataHoraInclusao(LocalDateTime dataHoraInclusao) {
        this.dataHoraInclusao = dataHoraInclusao;
    }

    public void setUsuarioRemocao(Usuario usuarioRemocao) {
        this.usuarioRemocao = usuarioRemocao;
    }

    public Usuario getUsuarioRemocao() {
        return usuarioRemocao;
    }

    public void setDataHoraRemocao(LocalDateTime dataHoraRemocao) {
        this.dataHoraRemocao = dataHoraRemocao;
    }

    public LocalDateTime getDataHoraRemocao() {
        return dataHoraRemocao;
    }

    
    /**
     * @return the categoria
     */
    public HierarquiaProjetoDetalhe getCategoria() {
        return categoria;
    }

    /**
     * @param categoria the categoria to set
     */
    public void setCategoria(HierarquiaProjetoDetalhe categoria) {
        this.categoria = categoria;
    }

    /**
     * @return the tarefas
     */
    public List<Tarefa> getTarefas() {
        if (tarefas==null){
            setTarefas(new ArrayList<>());
        }
        return tarefas;
    }

    /**
     * @param tarefas the tarefas to set
     */
    public void setTarefas(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }

    /**
     * Adds a task to the task list
     * @param task to be added
     */
    public void addTask(Tarefa task) {
        if(tarefas==null){
            tarefas = new ArrayList<>();
        }
        tarefas.add(task);
    }

    public List<Participante> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<Participante> participantes) {
        if (participantes==null){
            setParticipantes(new ArrayList<>());
        }
        this.participantes = participantes;
    }
    
    
    
    

}
