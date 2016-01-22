package com.saax.gestorweb.model.datamodel;

import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.SessionAttributesEnum;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.beanutils.BeanUtils;

/**
 * Entity bean da tabela Tarefa com as namequerys configuradas.<br><br>
 *
 * O objetivo desta entidade é armazenar as Taks e subs do sistema <br><br>
 *
 * @author rodrigo
 */
@Entity
@Table(name = "tarefa")
@NamedQueries({
    @NamedQuery(name = "Tarefa.findAll", query = "SELECT t FROM Tarefa t WHERE t.empresa = :empresa AND t.dataHoraRemocao IS NULL"),
    @NamedQuery(name = "Tarefa.findTarefasRemovidas", query = "SELECT t FROM Tarefa t WHERE t.usuarioRemocao = :usuarioRemocao ORDER BY t.dataHoraRemocao DESC"),
    @NamedQuery(name = "Tarefa.findTarefasPrincipais", query = "SELECT t FROM Tarefa t WHERE t.empresa = :empresa AND t.usuarioSolicitante = :usuarioSolicitante AND t.dataHoraRemocao IS NULL ORDER BY t.dataFim DESC"),
    @NamedQuery(name = "Tarefa.findByNome", query = "SELECT t FROM Tarefa t WHERE t.empresa = :empresa AND  t.nome = :nome AND t.dataHoraRemocao IS NULL"),
    @NamedQuery(name = "Tarefa.findByEmpresa", query = "SELECT t FROM Tarefa t WHERE t.empresa = :empresa AND t.dataHoraRemocao IS NULL"),
    @NamedQuery(name = "Tarefa.findByPrioridade", query = "SELECT t FROM Tarefa t WHERE t.empresa = :empresa AND  t.prioridade = :prioridade AND t.dataHoraRemocao IS NULL"),
    @NamedQuery(name = "Tarefa.findByRecurrencyID", query = "SELECT t FROM Tarefa t WHERE t.recurrencyID = :recurrencyID AND t.dataHoraRemocao IS NULL ORDER BY t.dataInicio"),
    @NamedQuery(name = "Tarefa.findByDataInicio", query = "SELECT t FROM Tarefa t WHERE t.empresa = :empresa AND  t.dataInicio >= :dataInicio AND t.dataHoraRemocao IS NULL"),
    @NamedQuery(name = "Tarefa.findByDataFim", query = "SELECT t FROM Tarefa t WHERE t.empresa = :empresa AND  t.dataFim <= :dataFim AND t.dataHoraRemocao IS NULL"),
    @NamedQuery(name = "Tarefa.findByDataTermino", query = "SELECT t FROM Tarefa t WHERE t.empresa = :empresa AND  t.dataTermino = :dataTermino AND t.dataHoraRemocao IS NULL"),
    @NamedQuery(name = "Tarefa.findByStatus", query = "SELECT t FROM Tarefa t WHERE t.empresa = :empresa AND  t.status = :status AND t.dataHoraRemocao IS NULL"),
    @NamedQuery(name = "Tarefa.findByProjecao", query = "SELECT t FROM Tarefa t WHERE t.empresa = :empresa AND  t.projecao = :projecao AND t.dataHoraRemocao IS NULL"),
    @NamedQuery(name = "Tarefa.findByAndamento", query = "SELECT t FROM Tarefa t WHERE t.empresa = :empresa AND  t.andamento = :andamento AND t.dataHoraRemocao IS NULL"),
    @NamedQuery(name = "Tarefa.findByDescricao", query = "SELECT t FROM Tarefa t WHERE t.empresa = :empresa AND  t.descricao = :descrica AND t.dataHoraRemocao IS NULL"),
    @NamedQuery(name = "Tarefa.findByTemplate", query = "SELECT t FROM Tarefa t WHERE t.empresa = :empresa AND  t.template = :template AND t.dataHoraRemocao IS NULL"),
    @NamedQuery(name = "Tarefa.findByApontamentohoras", query = "SELECT t FROM Tarefa t WHERE t.empresa = :empresa AND  t.apontamentoHoras = :apontamentohoras AND t.dataHoraRemocao IS NULL"),
    @NamedQuery(name = "Tarefa.findByUsuarioResponsavel", query = "SELECT t FROM Tarefa t WHERE t.empresa = :empresa AND  t.usuarioResponsavel = :usuarioResponsavel AND t.dataHoraRemocao IS NULL"),
    @NamedQuery(name = "Tarefa.findByUsuarioResponsavelDashboard", query = "SELECT t FROM Tarefa t WHERE t.usuarioResponsavel = :usuarioResponsavel AND t.dataHoraRemocao IS NULL ORDER BY t.dataInicio"),
    @NamedQuery(name = "Tarefa.findByUsuarioSolicitante", query = "SELECT t FROM Tarefa t WHERE t.empresa = :empresa AND  t.usuarioSolicitante = :usuarioSolicitante AND t.dataHoraRemocao IS NULL"),
    @NamedQuery(name = "Tarefa.findByOrcamentocontrolado", query = "SELECT t FROM Tarefa t WHERE t.empresa = :empresa AND  t.orcamentoControlado = :orcamentocontrolado AND t.dataHoraRemocao IS NULL"),
    @NamedQuery(name = "Tarefa.findByDatahorainclusao", query = "SELECT t FROM Tarefa t WHERE t.empresa = :empresa AND  t.dataHoraInclusao = :dataHoraInclusao AND t.dataHoraRemocao IS NULL")})
public class Tarefa implements Serializable {

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

    // ----------------------------------------------------------------------------------------------------------------
    // ATRIBUTOS PERSISTIDOS
    // ----------------------------------------------------------------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idtarefa")
    private Integer id;

    @Basic(optional = false)
    @NotNull(message = "Informe o nome da tarefa.")
    @Size(min = 5, max = 150, message = "Nome da tarefa deve ter de 5 a 150 caracteres.")
    @Column(name = "nome")
    private String nome;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Informe a prioridade da tarefa: Baixa, Normal ou Alta.")
    private PrioridadeTarefa prioridade;

    @Enumerated(EnumType.STRING)
    private StatusTarefa status;

    @Enumerated(EnumType.STRING)
    private ProjecaoTarefa projecao;

    @Basic(optional = false)
    @NotNull
    @Column(name = "andamento")
    private int andamento;

    @Column(name = "recurrencyMessage")
    private String recurrencyMessage;

    @Column(name = "template")
    private boolean template;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tarefa", orphanRemoval = true)
    private List<FavoritosTarefaMeta> favoritados;

    @JoinColumn(name = "idmeta", referencedColumnName = "idmeta")
    @ManyToOne
    private Meta meta;

    @JoinColumn(name = "idcentrocusto", referencedColumnName = "idcentrocusto")
    @ManyToOne
    private CentroCusto centroCusto;

    @JoinColumn(name = "iddepartamento", referencedColumnName = "iddepartamento")
    @ManyToOne
    private Departamento departamento;

    @NotNull(message = "Informe a empresa")
    @JoinColumn(name = "idempresa", referencedColumnName = "idempresa")
    @ManyToOne(optional = false)
    private Empresa empresa;

    @JoinColumn(name = "idfilialempresa", referencedColumnName = "idfilialempresa")
    @ManyToOne
    private FilialEmpresa filialEmpresa;

    @JoinColumn(name = "idempresacliente", referencedColumnName = "idempresacliente")
    @ManyToOne(fetch = FetchType.EAGER)
    private EmpresaCliente empresaCliente;

    @OneToMany(mappedBy = "tarefaPai", cascade = CascadeType.ALL)
    private List<Tarefa> subTarefas;

    @JoinColumn(name = "idproximatarefa", referencedColumnName = "idtarefa")
    @ManyToOne(optional = true)
    private Tarefa proximaTarefa;

    private int recurrencyID;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Informe se a tarefa é recorrente ou única")
    @Column(name = "tipo")
    private TipoTarefa tipoRecorrencia;

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
    @NotNull(message = "Informe o usuário reponsável.")
    @ManyToOne(optional = false)
    private Usuario usuarioResponsavel;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tarefa", orphanRemoval = true)
    private List<Participante> participantes;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tarefa", orphanRemoval = true)
    private List<AvaliacaoMetaTarefa> avaliacoes;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tarefa", orphanRemoval = true)
    @OrderBy("dataHoraInclusao ASC")
    private List<OrcamentoTarefa> orcamentos;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tarefa", orphanRemoval = true)
    @OrderBy("dataHoraInclusao ASC")
    private List<ApontamentoTarefa> apontamentos;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tarefa", orphanRemoval = true)
    private List<Anexo> anexos;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tarefa", orphanRemoval = true)
    private List<AndamentoTarefa> andamentos;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tarefa", orphanRemoval = true)
    private List<BloqueioTarefa> bloqueios;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tarefa", orphanRemoval = true)
    private List<HistoricoTarefa> historico;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tarefa", orphanRemoval = true)
    private List<ChatTarefa> chat;

    @Column(name = "datatermino")
    @Convert(converter = LocalDatePersistenceConverter.class)
    private LocalDate dataTermino;

    @Column(name = "datainicio")
    @NotNull(message = "Informe a data de início.")
    @Convert(converter = LocalDatePersistenceConverter.class)
    private LocalDate dataInicio;

    @Column(name = "datafim")
    @Convert(converter = LocalDatePersistenceConverter.class)
    private LocalDate dataFim;

    @Column(name = "datahorainclusao")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime dataHoraInclusao;

    @NotNull(message = "Informe a categoria")
    @JoinColumn(name = "idhierarquiaprojetodetalhe", referencedColumnName = "idhierarquiaprojetodetalhe")
    @ManyToOne(optional = false)
    private HierarquiaProjetoDetalhe hierarquia;

    @JoinColumn(name = "idusuarioremocao", referencedColumnName = "idusuario")
    @ManyToOne
    private Usuario usuarioRemocao;

    @Column(name = "datahoraremocao")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime dataHoraRemocao;

    // ----------------------------------------------------------------------------------------------------------------
    // ATRIBUTOS TRANSIENTES
    // ----------------------------------------------------------------------------------------------------------------
    /**
     * Custo de hora para todos os apontamentos
     */
    private transient BigDecimal custoHoraApontamento;

    // ----------------------------------------------------------------------------------------------------------------
    // CONSTRUTORES
    // ----------------------------------------------------------------------------------------------------------------
    public Tarefa() {
    }

    public Tarefa(Integer idtarefa) {
        this.id = idtarefa;
    }

    /**
     * Cria uma nova tarefa usando esta como template Todos os campos da tarefa
     * de template são copiados (inclusive das subs), exceto:
     * <ul>
     * <li>ID</li>
     * <li>Usuario Inclusão = Usuario logado</li>
     * <li>Usuario Solicitante = Usuario logado</li>
     * <li>Data Hora Inclusão = Agora</li>
     * <li>Andamentos</li>
     * <li>Avaliações</li>
     * <li>Bloqueios</li>
     * <li>Histórico</li>
     * </ul>
     *
     * @return o clone
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public Tarefa clone() throws CloneNotSupportedException {

        Tarefa clone = null;
        try {
            clone = (Tarefa) BeanUtils.cloneBean(this);
            List<Tarefa> cloneSubs = new ArrayList<>();
            for (Tarefa sub : clone.getSubTarefas()) {
                Tarefa subClone = sub.clone();
                subClone.setTarefaPai(clone);
                cloneSubs.add(subClone);

            }
            clone.setSubTarefas(cloneSubs);

            Usuario loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO);

            clone.setId(null);
            clone.setUsuarioInclusao(loggedUser);
            clone.setUsuarioSolicitante(loggedUser);
            clone.setDataHoraInclusao(LocalDateTime.now());
            clone.setAndamento(0);
            clone.setAndamentos(new ArrayList<>());
            clone.setApontamentos(new ArrayList<>());
            clone.setAvaliacoes(new ArrayList<>());
            clone.setBloqueios(new ArrayList<>());
            clone.setFavoritados(new ArrayList<>());
            clone.setHistorico(new ArrayList<>());
            clone.setOrcamentos(new ArrayList<>());
            clone.setChat(new ArrayList<>());
            clone.setStatus(StatusTarefa.NAO_ACEITA);

        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
            throw new RuntimeException("Falha ao clonar tarefa");
        }

        return clone;
    }

    // ----------------------------------------------------------------------------------------------------------------
    // GETTERS AND SETTERS (com tratamento p/ NULL)
    // ----------------------------------------------------------------------------------------------------------------
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

    public void setApontamentoHoras(boolean apontamentoHoras) {
        this.apontamentoHoras = apontamentoHoras;
    }

    public void setOrcamentoControlado(boolean orcamentoControlado) {
        this.orcamentoControlado = orcamentoControlado;
    }

    public List<FavoritosTarefaMeta> getFavoritados() {
        if (favoritados == null) {
            setFavoritados(new ArrayList<>());
        }
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

    public FilialEmpresa getFilialEmpresa() {
        return filialEmpresa;
    }

    public void setFilialEmpresa(FilialEmpresa filialEmpresa) {
        this.filialEmpresa = filialEmpresa;
    }

    public EmpresaCliente getEmpresaCliente() {
        return empresaCliente;
    }

    public void setEmpresaCliente(EmpresaCliente empresaCliente) {
        this.empresaCliente = empresaCliente;
    }

    public List<Tarefa> getSubTarefas() {
        if (subTarefas == null) {
            setSubTarefas(new ArrayList<>());
        }
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

    public List<Participante> getParticipantes() {
        if (participantes == null) {
            setParticipantes(new ArrayList<>());
        }

        return participantes;
    }

    public void setParticipantes(List<Participante> participantes) {
        if (participantes == null) {
            setParticipantes(new ArrayList<>());
        }
        this.participantes = participantes;
    }

    public List<AvaliacaoMetaTarefa> getAvaliacoes() {
        if (avaliacoes == null) {
            setAvaliacoes(new ArrayList<>());
        }
        return avaliacoes;
    }

    public void setAvaliacoes(List<AvaliacaoMetaTarefa> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }

    public List<OrcamentoTarefa> getOrcamentos() {
        if (orcamentos == null) {
            setOrcamentos(new ArrayList<>());
        }
        
  
        return orcamentos;
    }

    public void setOrcamentos(List<OrcamentoTarefa> orcamentos) {
        this.orcamentos = orcamentos;
    }

    public List<ApontamentoTarefa> getApontamentos() {
        if (apontamentos == null) {
            setApontamentos(new ArrayList<>());
        } 
                

        
        return apontamentos;
    }

    public void setApontamentos(List<ApontamentoTarefa> apontamentos) {
        this.apontamentos = apontamentos;
    }

    public List<Anexo> getAnexos() {
        if (anexos == null) {
            setAnexos(new ArrayList<>());
        }
        return anexos;
    }

    public void setAnexos(List<Anexo> anexos) {
        this.anexos = anexos;
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

    public int getRecurrencyID() {
        return recurrencyID;
    }

    public void setRecurrencyID(int recurrencyID) {
        this.recurrencyID = recurrencyID;
    }

    public TipoTarefa getTipoRecorrencia() {
        return tipoRecorrencia;
    }

    public void setTipoRecorrencia(TipoTarefa tipoRecorrencia) {
        this.tipoRecorrencia = tipoRecorrencia;
    }

    public void setAndamentos(List<AndamentoTarefa> andamentos) {
        this.andamentos = andamentos;
    }

    public List<AndamentoTarefa> getAndamentos() {
        if (andamentos == null) {
            setAndamentos(new ArrayList<>());
        }
        return andamentos;
    }

    public void setDataTermino(LocalDate dataTermino) {
        this.dataTermino = dataTermino;
    }

    public List<BloqueioTarefa> getBloqueios() {
        if (bloqueios == null) {
            setBloqueios(new ArrayList<>());
        }
        return bloqueios;
    }

    public void setBloqueios(List<BloqueioTarefa> bloqueios) {
        this.bloqueios = bloqueios;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public boolean isApontamentoHoras() {
        return apontamentoHoras;
    }

    public boolean isOrcamentoControlado() {
        return orcamentoControlado;
    }

    public LocalDate getDataTermino() {
        return dataTermino;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataHoraInclusao() {
        return dataHoraInclusao;
    }

    public void setDataHoraInclusao(LocalDateTime dataHoraInclusao) {
        this.dataHoraInclusao = dataHoraInclusao;
    }

    public List<HistoricoTarefa> getHistorico() {
        if (historico == null) {
            setHistorico(new ArrayList<>());
        }
        return historico;
    }

    public void setHistorico(List<HistoricoTarefa> historico) {
        this.historico = historico;
    }

    public List<ChatTarefa> getChat() {
        if (chat == null) {
            setChat(new ArrayList<>());
        }
        return chat;
    }

    public void setChat(List<ChatTarefa> chat) {
        this.chat = chat;
    }

    public void setCustoHoraApontamento(BigDecimal custoHoraApontamento) {
        this.custoHoraApontamento = custoHoraApontamento;
    }

    public BigDecimal getCustoHoraApontamento() {
        return custoHoraApontamento;
    }

    public void setHierarquia(HierarquiaProjetoDetalhe hierarquia) {
        this.hierarquia = hierarquia;
    }

    public HierarquiaProjetoDetalhe getHierarquia() {
        return hierarquia;
    }

    public void setTemplate(boolean template) {
        this.template = template;
    }

    public boolean isTemplate() {
        return template;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Meta getMeta() {
        return meta;
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

    
    public void setRecurrencyMessage(String recurrencyMessage) {
        this.recurrencyMessage = recurrencyMessage;
    }

    public String getRecurrencyMessage() {
        return recurrencyMessage;
    }

    // ----------------------------------------------------------------------------------------------------------------
    // EQUALS E HASCODE
    // ----------------------------------------------------------------------------------------------------------------
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Tarefa)) {
            return false;
        }
        Tarefa other = (Tarefa) object;
        if (this == other) {
            return true;
        }

        // se o ID estiver setado, compara por ele
        if (this.getId() != null) {
            return !((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id)));

        } else {
            // senao compara por campos setados na criação da tarefa
            return this.getEmpresa().equals(other.getEmpresa())
                    && this.getUsuarioInclusao().equals(other.getUsuarioInclusao())
                    && this.getUsuarioSolicitante().equals(other.getUsuarioSolicitante())
                    && this.getDataHoraInclusao().equals(other.getDataHoraInclusao());

        }
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.Tarefa[ idtarefa=" + getId() + " ]";
    }

    // ----------------------------------------------------------------------------------------------------------------
    // METODO UTILITARIOS
    // ----------------------------------------------------------------------------------------------------------------
    public void addAndamento(AndamentoTarefa andamentoTarefa) {
        if (getAndamentos() == null) {
            setAndamentos(new ArrayList<>());
        }
        getAndamentos().add(andamentoTarefa);
    }

    public void addBloqueio(BloqueioTarefa bloqueioTarefa) {
        if (getBloqueios() == null) {
            setBloqueios(new ArrayList<>());
        }
        getBloqueios().add(bloqueioTarefa);
    }

    public void addHistorico(HistoricoTarefa historicoTarefa) {
        if (getHistorico() == null) {
            setHistorico(new ArrayList<>());
        }
        getHistorico().add(historicoTarefa);
    }

    public void addApontamento(ApontamentoTarefa apontamento) {
        if (getApontamentos() == null) {
            setApontamentos(new ArrayList<>());
        }
        getApontamentos().add(apontamento);
    }

    public void addOrcamento(OrcamentoTarefa orcamentoTarefa) {
        if (getOrcamentos() == null) {
            setOrcamentos(new ArrayList<>());
        }
        getOrcamentos().add(orcamentoTarefa);
    }

}
