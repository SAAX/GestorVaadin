package com.saax.gestorweb.model.datamodel;

import java.io.Serializable;
import java.util.Collection;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity bean da tabela Empresa com as namequerys configuradas<br><br>
 * 
 * O objetivo desta entidade e armazenar os nossos Clientes<br><br>
 * 
 * Esta será a classe chave para segmentação dos dados armezenados.<br>
 *
 * ATENÇÃO: Esta classe ainda não está completa, mais campos serão adicionados
 * quando for criado o cadastro de empresas e clientes
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
    @NamedQuery(name = "Empresa.findByTipopessoa", query = "SELECT e FROM Empresa e WHERE e.tipoPessoa = :tipopessoa"),
    @NamedQuery(name = "Empresa.findByCnpj", query = "SELECT e FROM Empresa e WHERE e.cnpj = :cnpj"),
    @NamedQuery(name = "Empresa.findByCpf", query = "SELECT e FROM Empresa e WHERE e.cpf = :cpf"),
    @NamedQuery(name = "Empresa.findByAtiva", query = "SELECT e FROM Empresa e WHERE e.ativa = :ativa")})
public class Empresa implements Serializable {

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

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "tipopessoa")
    private String tipoPessoa;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "matriz")
    private Collection<FilialEmpresa> filiais;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresa")
    private Collection<CentroCusto> centrosDeCusto;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresa")
    private Collection<UsuarioEmpresa> usuarios;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresa")
    private Collection<Meta> metas;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresa")
    private Collection<Departamento> departamentos;

    @OneToMany(mappedBy = "empresaPrincipal")
    private Collection<Empresa> subEmpresas;

    @JoinColumn(name = "idempresaprincipal", referencedColumnName = "idempresa")
    @ManyToOne
    private Empresa empresaPrincipal;

    public Empresa() {
    }

    public Empresa(Integer idempresa) {
        this.id = idempresa;
    }

    public Empresa(Integer idempresa, String nome, String tipopessoa, boolean ativa) {
        this.id = idempresa;
        this.nome = nome;
        this.tipoPessoa = tipopessoa;
        this.ativa = ativa;
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

    public String getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
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

    public Collection<FilialEmpresa> getFiliais() {
        return filiais;
    }

    public void setFiliais(Collection<FilialEmpresa> filiais) {
        this.filiais = filiais;
    }

    public Collection<CentroCusto> getCentrosDeCusto() {
        return centrosDeCusto;
    }

    public void setCentrosDeCusto(Collection<CentroCusto> centrosDeCusto) {
        this.centrosDeCusto = centrosDeCusto;
    }

    public Collection<UsuarioEmpresa> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Collection<UsuarioEmpresa> usuarios) {
        this.usuarios = usuarios;
    }

    public Collection<Meta> getMetas() {
        return metas;
    }

    public void setMetas(Collection<Meta> metas) {
        this.metas = metas;
    }

    public Collection<Departamento> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(Collection<Departamento> departamentos) {
        this.departamentos = departamentos;
    }

    public Collection<Empresa> getSubEmpresas() {
        return subEmpresas;
    }

    public void setSubEmpresas(Collection<Empresa> subEmpresas) {
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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empresa)) {
            return false;
        }
        Empresa other = (Empresa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.model.datamodel.Empresa[ idempresa=" + id + " ]";
    }

}
