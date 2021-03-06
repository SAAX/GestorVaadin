package com.saax.gestorweb.model.datamodel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entity bean da tabela Empresa com as namequerys configuradas<br><br>
 *
 * O objetivo desta entidade e armazenar os nossos Clientes<br><br>
 *
 * Esta será a classe chave para segmentação dos dados armezenados.<br>
 *
 *
 * @author rodrigo
 */
@Entity
@Table(name = "empresa")
@NamedQueries({
    @NamedQuery(name = "Empresa.findAll", query = "SELECT e FROM Empresa e"),
    @NamedQuery(name = "Empresa.findById", query = "SELECT e FROM Empresa e WHERE e.id = :id"),
    @NamedQuery(name = "Empresa.findByNome", query = "SELECT e FROM Empresa e WHERE e.nome = :nome"),
    @NamedQuery(name = "Empresa.findByRazaoSocial", query = "SELECT e FROM Empresa e WHERE e.razaoSocial = :RazaoSocial"),
    @NamedQuery(name = "Empresa.findByTipopessoa", query = "SELECT e FROM Empresa e WHERE e.tipoPessoa = :tipopessoa"),
    @NamedQuery(name = "Empresa.findByCnpj", query = "SELECT e FROM Empresa e WHERE e.cnpj = :cnpj"),
    @NamedQuery(name = "Empresa.findByCpf", query = "SELECT e FROM Empresa e WHERE e.cpf = :cpf"),
    @NamedQuery(name = "Empresa.findByAtiva", query = "SELECT e FROM Empresa e WHERE e.ativa = :ativa")})
public class Empresa implements Serializable {

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
    @Column(name = "idempresa")
    private Integer id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nome")
    private String nome;

    @Size(max = 18)
    @Column(name = "cnpj")
    private String cnpj;

    @Size(max = 14)
    @Column(name = "cpf")
    private String cpf;

    @Basic(optional = false)
    @NotNull
    @Column(name = "ativa")
    private boolean ativa;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "matriz")
    private List<FilialEmpresa> filiais;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "empresa")
    private List<CentroCusto> centrosDeCusto;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "empresa")
    private List<UsuarioEmpresa> usuarios;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "empresa")
    private List<Meta> metas;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "empresa")
    private List<Publicacao> publicacoes;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "empresa")
    private List<Departamento> departamentos;

    @OneToMany(mappedBy = "empresaPrincipal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Empresa> subEmpresas;

    @JoinColumn(name = "idempresaprincipal", referencedColumnName = "idempresa")
    @ManyToOne
    private Empresa empresaPrincipal;

    @Basic(optional = false)
    @Size(min = 1, max = 150)
    @Column(name = "razaosocial")
    private String razaoSocial;

    @Basic(optional = false)
    @NotNull
    @Column(name = "tipopessoa")
    private Character tipoPessoa;

    @JoinColumn(name = "idendereco", referencedColumnName = "idendereco")
    @ManyToOne(cascade = CascadeType.ALL)
    private Endereco endereco;

    @Column(name = "datahorainclusao")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime dataHoraInclusao;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "empresa")
    private List<Tarefa> tarefas;

    @JoinColumn(name = "idusuarioinclusao", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario usuarioInclusao;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "empresa")
    private List<EmpresaCliente> clientes;

    public Empresa() {
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

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public List<FilialEmpresa> getFiliais() {
        return filiais;
    }

    public void setFiliais(List<FilialEmpresa> filiais) {
        this.filiais = filiais;
    }

    public List<CentroCusto> getCentrosDeCusto() {
        return centrosDeCusto;
    }

    public void setCentrosDeCusto(List<CentroCusto> centrosDeCusto) {
        this.centrosDeCusto = centrosDeCusto;
    }

    public List<UsuarioEmpresa> getUsuarios() {
        return usuarios;
    }

    public void setPublicacoes(List<Publicacao> publicacoes) {
        this.publicacoes = publicacoes;
    }

    public List<Publicacao> getPublicacoes() {
        return publicacoes;
    }

    public void setUsuarios(List<UsuarioEmpresa> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Meta> getMetas() {
        if (metas == null){
            metas = new ArrayList<>();
        }
        return metas;
    }

    public void setMetas(List<Meta> metas) {
        this.metas = metas;
    }

    public List<Departamento> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<Departamento> departamentos) {
        this.departamentos = departamentos;
    }

    public List<Empresa> getSubEmpresas() {
        return subEmpresas;
    }

    public void setSubEmpresas(List<Empresa> subEmpresas) {
        this.subEmpresas = subEmpresas;
    }

    public Empresa getEmpresaPrincipal() {
        return empresaPrincipal;
    }

    public void setEmpresaPrincipal(Empresa empresaPrincipal) {
        this.empresaPrincipal = empresaPrincipal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Empresa)) {
            return false;
        }
        Empresa other = (Empresa) object;

        // se o ID estiver setado, compara por ele
        if (this.getId() != null && other.getId() != null) {
            return !((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id)));

        } else if (this.getCnpj() != null && other.getCnpj() != null) {
            // senao compara pelo cnpj
            return this.getCnpj().equals(other.getCnpj());

        } else {

            // senao compara pelo ponteiro
            return super.equals(object);
        }

    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public Character getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(Character tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    @Override
    public String toString() {
        return nome;
    }

    @XmlTransient
    public List<EmpresaCliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<EmpresaCliente> clientes) {
        this.clientes = clientes;
    }

    public LocalDateTime getDataHoraInclusao() {
        return dataHoraInclusao;
    }

    public void setDataHoraInclusao(LocalDateTime dataHoraInclusao) {
        this.dataHoraInclusao = dataHoraInclusao;
    }

    public List<Tarefa> getTarefas() {
        if (tarefas == null) {
            tarefas = new ArrayList<>();
        }
        return tarefas;
    }

    public void setTarefas(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }

    public Usuario getUsuarioInclusao() {
        return usuarioInclusao;
    }

    public void setUsuarioInclusao(Usuario usuarioInclusao) {
        this.usuarioInclusao = usuarioInclusao;
    }

}
