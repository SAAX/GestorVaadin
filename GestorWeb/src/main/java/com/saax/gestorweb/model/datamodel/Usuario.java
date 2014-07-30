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
    @Basic(optional = false)
    @NotNull
    @Column(name = "datahorainclusao")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime dataHoraInclusao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuarioInclusao")
    private List<FavoritosTarefaMeta> favoritosTarefaMetaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuarioInclusao")
    private List<CentroCusto> centroCustoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuarioInclusao")
    private List<Endereco> enderecoList;
    @OneToMany(mappedBy = "idUsuarioInclusao")
    private List<Tarefa> tarefaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuarioSolicitante")
    private List<Tarefa> tarefaList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuarioResponsavel")
    private List<Tarefa> tarefaList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuarioInclusao")
    private List<ParicipanteTarefa> paricipanteTarefaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuarioParticipante")
    private List<ParicipanteTarefa> paricipanteTarefaList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuarioInclusao")
    private List<FilialCliente> filialClienteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuarioInclusao")
    private List<FilialEmpresa> filialEmpresaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuarioInclusao")
    private List<AvaliacaoMetaTarefa> avaliacaoMetaTarefaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuarioAvaliador")
    private List<AvaliacaoMetaTarefa> avaliacaoMetaTarefaList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuarioAvaliado")
    private List<AvaliacaoMetaTarefa> avaliacaoMetaTarefaList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuarioInclusao")
    private List<OrcamentoTarefa> orcamentoTarefaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuarioInclusao")
    private List<ApontamentoTarefa> apontamentoTarefaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuarioInclusao")
    private List<Departamento> departamentoList;
    @OneToMany(mappedBy = "idUsuarioInclusao")
    private List<Usuario> usuarioList;
    @JoinColumn(name = "idusuarioinclusao", referencedColumnName = "idusuario")
    @ManyToOne
    private Usuario idUsuarioInclusao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuarioInclusao")
    private List<Empresa> empresaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuarioInclusao")
    private List<EmpresaCliente> empresaClienteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuarioInclusao")
    private List<AnexoTarefa> anexoTarefaList;
    
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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
    private Collection<UsuarioEmpresa> empresas;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "responsavel")
    private Collection<Meta> metasResponsaveis;
    
    
    public Usuario() {
    }

    public Usuario(Integer idusuario) {
        this.id = idusuario;
    }

    public Usuario(Integer idusuario, String nome, String sobrenome, String login, String senha) {
        this.id = idusuario;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.login = login;
        this.senha = senha;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
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

    public Collection<Meta> getMetasResponsaveis() {
        return metasResponsaveis;
    }

    public void setMetasResponsaveis(Collection<Meta> metasResponsaveis) {
        this.metasResponsaveis = metasResponsaveis;
    }

    public LocalDateTime getDataHoraInclusao() {
        return dataHoraInclusao;
    }

    public void setDataHoraInclusao(LocalDateTime dataHoraInclusao) {
        this.dataHoraInclusao = dataHoraInclusao;
    }

    public List<FavoritosTarefaMeta> getFavoritosTarefaMetaList() {
        return favoritosTarefaMetaList;
    }

    public void setFavoritosTarefaMetaList(List<FavoritosTarefaMeta> favoritosTarefaMetaList) {
        this.favoritosTarefaMetaList = favoritosTarefaMetaList;
    }

    public List<CentroCusto> getCentroCustoList() {
        return centroCustoList;
    }

    public void setCentroCustoList(List<CentroCusto> centroCustoList) {
        this.centroCustoList = centroCustoList;
    }

    public List<Endereco> getEnderecoList() {
        return enderecoList;
    }

    public void setEnderecoList(List<Endereco> enderecoList) {
        this.enderecoList = enderecoList;
    }

    public List<Tarefa> getTarefaList() {
        return tarefaList;
    }

    public void setTarefaList(List<Tarefa> tarefaList) {
        this.tarefaList = tarefaList;
    }

    public List<Tarefa> getTarefaList1() {
        return tarefaList1;
    }

    public void setTarefaList1(List<Tarefa> tarefaList1) {
        this.tarefaList1 = tarefaList1;
    }

    public List<Tarefa> getTarefaList2() {
        return tarefaList2;
    }

    public void setTarefaList2(List<Tarefa> tarefaList2) {
        this.tarefaList2 = tarefaList2;
    }

    public List<ParicipanteTarefa> getParicipanteTarefaList() {
        return paricipanteTarefaList;
    }

    public void setParicipanteTarefaList(List<ParicipanteTarefa> paricipanteTarefaList) {
        this.paricipanteTarefaList = paricipanteTarefaList;
    }

    public List<ParicipanteTarefa> getParicipanteTarefaList1() {
        return paricipanteTarefaList1;
    }

    public void setParicipanteTarefaList1(List<ParicipanteTarefa> paricipanteTarefaList1) {
        this.paricipanteTarefaList1 = paricipanteTarefaList1;
    }

    public List<FilialCliente> getFilialClienteList() {
        return filialClienteList;
    }

    public void setFilialClienteList(List<FilialCliente> filialClienteList) {
        this.filialClienteList = filialClienteList;
    }

    public List<FilialEmpresa> getFilialEmpresaList() {
        return filialEmpresaList;
    }

    public void setFilialEmpresaList(List<FilialEmpresa> filialEmpresaList) {
        this.filialEmpresaList = filialEmpresaList;
    }

    public List<AvaliacaoMetaTarefa> getAvaliacaoMetaTarefaList() {
        return avaliacaoMetaTarefaList;
    }

    public void setAvaliacaoMetaTarefaList(List<AvaliacaoMetaTarefa> avaliacaoMetaTarefaList) {
        this.avaliacaoMetaTarefaList = avaliacaoMetaTarefaList;
    }

    public List<AvaliacaoMetaTarefa> getAvaliacaoMetaTarefaList1() {
        return avaliacaoMetaTarefaList1;
    }

    public void setAvaliacaoMetaTarefaList1(List<AvaliacaoMetaTarefa> avaliacaoMetaTarefaList1) {
        this.avaliacaoMetaTarefaList1 = avaliacaoMetaTarefaList1;
    }

    public List<AvaliacaoMetaTarefa> getAvaliacaoMetaTarefaList2() {
        return avaliacaoMetaTarefaList2;
    }

    public void setAvaliacaoMetaTarefaList2(List<AvaliacaoMetaTarefa> avaliacaoMetaTarefaList2) {
        this.avaliacaoMetaTarefaList2 = avaliacaoMetaTarefaList2;
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

    public List<Departamento> getDepartamentoList() {
        return departamentoList;
    }

    public void setDepartamentoList(List<Departamento> departamentoList) {
        this.departamentoList = departamentoList;
    }

    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    public Usuario getIdUsuarioInclusao() {
        return idUsuarioInclusao;
    }

    public void setIdUsuarioInclusao(Usuario idUsuarioInclusao) {
        this.idUsuarioInclusao = idUsuarioInclusao;
    }

    public List<Empresa> getEmpresaList() {
        return empresaList;
    }

    public void setEmpresaList(List<Empresa> empresaList) {
        this.empresaList = empresaList;
    }

    public List<EmpresaCliente> getEmpresaClienteList() {
        return empresaClienteList;
    }

    public void setEmpresaClienteList(List<EmpresaCliente> empresaClienteList) {
        this.empresaClienteList = empresaClienteList;
    }

    public List<AnexoTarefa> getAnexoTarefaList() {
        return anexoTarefaList;
    }

    public void setAnexoTarefaList(List<AnexoTarefa> anexoTarefaList) {
        this.anexoTarefaList = anexoTarefaList;
    }

    
}
