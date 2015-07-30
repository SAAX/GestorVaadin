package com.saax.gestorweb.model.datamodel;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity bean da tabela Usuario com as namequerys configuradas.<br><br>
 * 
 * O objetivo desta entidade e armazenar os Usuarios para controle de acesso<br><br>
 *
 * @author rodrigo
 */
@Entity
@Table(name = "usuario")
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u"),
    @NamedQuery(name = "Usuario.findById", query = "SELECT u FROM Usuario u WHERE u.id = :id"),
    @NamedQuery(name = "Usuario.findByNome", query = "SELECT u FROM Usuario u WHERE u.nome = :nome"),
    @NamedQuery(name = "Usuario.findByLogin", query = "SELECT u FROM Usuario u WHERE u.login = :login"),
    @NamedQuery(name = "Usuario.findBySenha", query = "SELECT u FROM Usuario u WHERE u.senha = :senha")})
public class Usuario implements Serializable {
    
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
    @Column(name = "idusuario")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nome")
    private String nome;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "sobrenome")
    private String sobrenome;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "login")
    
    private String login;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "senha")
    private String senha;

    private transient Empresa empresaAtiva;
        
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "usuario")
    private Collection<UsuarioEmpresa> empresas;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "usuarioResponsavel")
    private Collection<Meta> metasSobResponsabilidade;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "usuarioSolicitante")
    private Collection<Meta> metasSolicitadas;

    @Column(name = "datahorainclusao")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime dataHoraInclusao;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "usuarioInclusao")
    private List<FavoritosTarefaMeta> favoritosIncluidos;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "usuarioInclusao")
    private List<CentroCusto> centrosCustoIncluidos;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "usuarioInclusao")
    private List<Endereco> enderecosIncluidos;
    
    @OneToMany(mappedBy = "usuarioInclusao")
    private List<Tarefa> tarefasIncluidas;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "usuarioSolicitante")
    private List<Tarefa> tarefasSolicitadas;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "usuarioResponsavel")
    private List<Tarefa> tarefasSobResponsabilidade;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "usuarioInclusao")
    private List<Participante> paricipacoesIncluidas;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "usuarioParticipante")
    private List<Participante> tarefasParticipantes;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "usuarioInclusao")
    private List<FilialCliente> filiaisClientesIncluidas;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "usuarioInclusao")
    private List<FilialEmpresa> filiaisEmpresaIncluidas;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "usuarioInclusao")
    private List<AvaliacaoMetaTarefa> avaliacoesIncluidas;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "usuarioAvaliador")
    private List<AvaliacaoMetaTarefa> avaliacoesSubmetidas;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "usuarioAvaliado")
    private List<AvaliacaoMetaTarefa> avaliacoesRecebidas;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "usuarioInclusao")
    private List<OrcamentoTarefa> orcamentosIncluidos;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "usuarioInclusao")
    private List<ApontamentoTarefa> apontamentosIncluidos;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "usuarioInclusao")
    private List<Departamento> departamentosIncluidos;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "usuario")
    private List<HistoricoTarefa> historicosTarefa;
    
    @OneToMany(mappedBy = "usuarioInclusao")
    private List<Usuario> usuariosIncluidos;
    
    @JoinColumn(name = "idusuarioinclusao", referencedColumnName = "idusuario")
    @ManyToOne
    private Usuario usuarioInclusao;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "usuarioInclusao")
    private List<Empresa> empresasIncluidas;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "usuarioInclusao")
    private List<EmpresaCliente> empresasClienteIncluidas;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "usuarioInclusao")
    private List<AnexoTarefa> anexosTarefaIncluidos;
    
    
    public Usuario() {
    }
    

    public Usuario(Integer idusuario) {
        this.id = idusuario;
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

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }
    
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
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
        return "com.saax.gestorweb.model.datamodel.Usuario[ idusuario=" + id + " ]";
    }

    public Collection<UsuarioEmpresa> getEmpresas() {
        return empresas;
    }

    public void setEmpresas(Collection<UsuarioEmpresa> empresas) {
        this.empresas = empresas;
    }

    public Collection<Meta> getMetasSobResponsabilidade() {
        return metasSobResponsabilidade;
    }

    public void setMetasSobResponsabilidade(Collection<Meta> metasSobResponsabilidade) {
        this.metasSobResponsabilidade = metasSobResponsabilidade;
    }

    public LocalDateTime getDataHoraInclusao() {
        return dataHoraInclusao;
    }

    public void setDataHoraInclusao(LocalDateTime dataHoraInclusao) {
        this.dataHoraInclusao = dataHoraInclusao;
    }

    public List<FavoritosTarefaMeta> getFavoritosIncluidos() {
        return favoritosIncluidos;
    }

    public void setFavoritosIncluidos(List<FavoritosTarefaMeta> favoritosIncluidos) {
        this.favoritosIncluidos = favoritosIncluidos;
    }

    public List<CentroCusto> getCentrosCustoIncluidos() {
        return centrosCustoIncluidos;
    }

    public void setCentrosCustoIncluidos(List<CentroCusto> centrosCustoIncluidos) {
        this.centrosCustoIncluidos = centrosCustoIncluidos;
    }

    public List<Endereco> getEnderecosIncluidos() {
        return enderecosIncluidos;
    }

    public void setEnderecosIncluidos(List<Endereco> enderecosIncluidos) {
        this.enderecosIncluidos = enderecosIncluidos;
    }

    public List<Tarefa> getTarefasIncluidas() {
        return tarefasIncluidas;
    }

    public void setTarefasIncluidas(List<Tarefa> tarefasIncluidas) {
        this.tarefasIncluidas = tarefasIncluidas;
    }

    public List<Tarefa> getTarefasSolicitadas() {
        return tarefasSolicitadas;
    }

    public void setTarefasSolicitadas(List<Tarefa> tarefasSolicitadas) {
        this.tarefasSolicitadas = tarefasSolicitadas;
    }

    public List<Tarefa> getTarefasSobResponsabilidade() {
        return tarefasSobResponsabilidade;
    }

    public void setTarefasSobResponsabilidade(List<Tarefa> tarefasSobResponsabilidade) {
        this.tarefasSobResponsabilidade = tarefasSobResponsabilidade;
    }

    public List<Participante> getParicipacoesIncluidas() {
        return paricipacoesIncluidas;
    }

    public void setParicipacoesIncluidas(List<Participante> paricipacoesIncluidas) {
        this.paricipacoesIncluidas = paricipacoesIncluidas;
    }

    public List<Participante> getTarefasParticipantes() {
        return tarefasParticipantes;
    }

    public void setTarefasParticipantes(List<Participante> tarefasParticipantes) {
        this.tarefasParticipantes = tarefasParticipantes;
    }

    public List<FilialCliente> getFiliaisClientesIncluidas() {
        return filiaisClientesIncluidas;
    }

    public void setFiliaisClientesIncluidas(List<FilialCliente> filiaisClientesIncluidas) {
        this.filiaisClientesIncluidas = filiaisClientesIncluidas;
    }

    public List<FilialEmpresa> getFiliaisEmpresaIncluidas() {
        return filiaisEmpresaIncluidas;
    }

    public void setFiliaisEmpresaIncluidas(List<FilialEmpresa> filiaisEmpresaIncluidas) {
        this.filiaisEmpresaIncluidas = filiaisEmpresaIncluidas;
    }

    public List<AvaliacaoMetaTarefa> getAvaliacoesIncluidas() {
        return avaliacoesIncluidas;
    }

    public void setAvaliacoesIncluidas(List<AvaliacaoMetaTarefa> avaliacoesIncluidas) {
        this.avaliacoesIncluidas = avaliacoesIncluidas;
    }

    public List<AvaliacaoMetaTarefa> getAvaliacoesSubmetidas() {
        return avaliacoesSubmetidas;
    }

    public void setAvaliacoesSubmetidas(List<AvaliacaoMetaTarefa> avaliacoesSubmetidas) {
        this.avaliacoesSubmetidas = avaliacoesSubmetidas;
    }

    public List<AvaliacaoMetaTarefa> getAvaliacoesRecebidas() {
        return avaliacoesRecebidas;
    }

    public void setAvaliacoesRecebidas(List<AvaliacaoMetaTarefa> avaliacoesRecebidas) {
        this.avaliacoesRecebidas = avaliacoesRecebidas;
    }

    public List<OrcamentoTarefa> getOrcamentosIncluidos() {
        return orcamentosIncluidos;
    }

    public void setOrcamentosIncluidos(List<OrcamentoTarefa> orcamentosIncluidos) {
        this.orcamentosIncluidos = orcamentosIncluidos;
    }

    public List<ApontamentoTarefa> getApontamentosIncluidos() {
        return apontamentosIncluidos;
    }

    public void setApontamentosIncluidos(List<ApontamentoTarefa> apontamentosIncluidos) {
        this.apontamentosIncluidos = apontamentosIncluidos;
    }

    public List<Departamento> getDepartamentosIncluidos() {
        return departamentosIncluidos;
    }

    public void setDepartamentosIncluidos(List<Departamento> departamentosIncluidos) {
        this.departamentosIncluidos = departamentosIncluidos;
    }

    public List<Usuario> getUsuariosIncluidos() {
        return usuariosIncluidos;
    }

    public void setUsuariosIncluidos(List<Usuario> usuariosIncluidos) {
        this.usuariosIncluidos = usuariosIncluidos;
    }

    public Usuario getUsuarioInclusao() {
        return usuarioInclusao;
    }

    public void setUsuarioInclusao(Usuario usuarioInclusao) {
        this.usuarioInclusao = usuarioInclusao;
    }
    
    
    public List<Empresa> getEmpresasIncluidas() {
        return empresasIncluidas;
    }

    public void setEmpresasIncluidas(List<Empresa> empresasIncluidas) {
        this.empresasIncluidas = empresasIncluidas;
    }

    public List<EmpresaCliente> getEmpresasClienteIncluidas() {
        return empresasClienteIncluidas;
    }

    public void setEmpresasClienteIncluidas(List<EmpresaCliente> empresasClienteIncluidas) {
        this.empresasClienteIncluidas = empresasClienteIncluidas;
    }

    public List<AnexoTarefa> getAnexosTarefaIncluidos() {
        return anexosTarefaIncluidos;
    }

    public void setAnexosTarefaIncluidos(List<AnexoTarefa> anexosTarefaIncluidos) {
        this.anexosTarefaIncluidos = anexosTarefaIncluidos;
    }

    public List<HistoricoTarefa> getHistoricosTarefa() {
        return historicosTarefa;
    }

    public void setHistoricosTarefa(List<HistoricoTarefa> historicosTarefa) {
        this.historicosTarefa = historicosTarefa;
    }

    public void setEmpresaAtiva(Empresa empresaAtiva) {
        this.empresaAtiva = empresaAtiva;
    }

    public Empresa getEmpresaAtiva() {
        return empresaAtiva;
    }

    public void setMetasSolicitadas(Collection<Meta> metasSolicitadas) {
        this.metasSolicitadas = metasSolicitadas;
    }

    public Collection<Meta> getMetasSolicitadas() {
        return metasSolicitadas;
    }

    
    
    
}
