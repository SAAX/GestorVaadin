package com.saax.gestorweb.model.datamodel;

import java.io.Serializable;
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
 * Entity bean da tabela EmpresaCliente com as namequerys configuradas<br><br>
 * 
 * O objetivo desta entidade e armazenar os Clientes dos nossos Clientes<br><br>
 *
 * @author rodrigo
 */
@Entity
@Table(name = "empresacliente")
@NamedQueries({
    @NamedQuery(name = "EmpresaCliente.findAll", query = "SELECT e FROM EmpresaCliente e"),
    @NamedQuery(name = "EmpresaCliente.findByNome", query = "SELECT e FROM EmpresaCliente e WHERE e.nome = :nome"),
    @NamedQuery(name = "EmpresaCliente.findByEmpresa", query = "SELECT e FROM EmpresaCliente e WHERE e.empresa = :empresa"),
    @NamedQuery(name = "EmpresaCliente.findByRazaosocial", query = "SELECT e FROM EmpresaCliente e WHERE e.razaoSocial = :razaosocial"),
    @NamedQuery(name = "EmpresaCliente.findByTipopessoa", query = "SELECT e FROM EmpresaCliente e WHERE e.tipoPessoa = :tipopessoa"),
    @NamedQuery(name = "EmpresaCliente.findByCnpj", query = "SELECT e FROM EmpresaCliente e WHERE e.cnpj = :cnpj"),
    @NamedQuery(name = "EmpresaCliente.findByCpf", query = "SELECT e FROM EmpresaCliente e WHERE e.cpf = :cpf"),
    @NamedQuery(name = "EmpresaCliente.findByAtiva", query = "SELECT e FROM EmpresaCliente e WHERE e.ativa = :ativa")})
public class EmpresaCliente implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idempresacliente")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nome")
    private String nome;
    
    @Basic(optional = false)
    @Size(min = 1, max = 150)
    @NotNull
    @Column(name = "razaosocial")
    private String razaoSocial;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "tipopessoa")
    private Character tipoPessoa;
    
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
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "empresaCliente")
    private List<FilialCliente> filiais;
    
    @JoinColumn(name = "idempresa", referencedColumnName = "idempresa")
    @ManyToOne(optional = false)
    private Empresa empresa;
    
    @JoinColumn(name = "idendereco", referencedColumnName = "idendereco")
    @ManyToOne
    private Endereco endereco;

    @Column(name = "datahorainclusao")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime dataHoraInclusao;
    
    @OneToMany(mappedBy = "empresaCliente")
    private List<Tarefa> tarefas;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "cliente")
    private List<Meta> metas;
    
    @JoinColumn(name = "idusuarioinclusao", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario usuarioInclusao;


    public EmpresaCliente() {
    }

    public EmpresaCliente(Integer idempresacliente) {
        this.id = idempresacliente;
    }

    public EmpresaCliente(Integer idempresacliente, String nome, String razaosocial, Character tipopessoa, boolean ativa) {
        this.id = idempresacliente;
        this.nome = nome;
        this.razaoSocial = razaosocial;
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

    public List<FilialCliente> getFiliais() {
        return filiais;
    }

    public void setFiliais(List<FilialCliente> filiais) {
        this.filiais = filiais;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EmpresaCliente)) {
            return false;
        }
        EmpresaCliente other = (EmpresaCliente) object;
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
        return "com.saax.gestorweb.model.datamodel.EmpresaCliente[ idempresacliente=" + id + " ]";
    }

    public LocalDateTime getDataHoraInclusao() {
        return dataHoraInclusao;
    }

    public void setDataHoraInclusao(LocalDateTime dataHoraInclusao) {
        this.dataHoraInclusao = dataHoraInclusao;
    }

    public List<Tarefa> getTarefas() {
        return tarefas;
    }

    public void setTarefas(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }

    public List<Meta> getMetas() {
        return metas;
    }

    public void setMetas(List<Meta> metas) {
        this.metas = metas;
    }

    public Usuario getUsuarioInclusao() {
        return usuarioInclusao;
    }

    public void setUsuarioInclusao(Usuario usuarioInclusao) {
        this.usuarioInclusao = usuarioInclusao;
    }
    
}
