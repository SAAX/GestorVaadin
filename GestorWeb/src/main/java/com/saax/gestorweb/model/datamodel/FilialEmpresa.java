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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity bean da tabela Filial Empresa com as namequerys configuradas.<br><br>
 * 
 * O objetivo desta entidade e armazenar as Filiais dos nossos clientes<br><br>
 * 
 * @author rodrigo
 */
@Entity
@Table(name = "filialempresa")
@NamedQueries({
    @NamedQuery(name = "FilialEmpresa.findAll", query = "SELECT f FROM FilialEmpresa f"),
    @NamedQuery(name = "FilialEmpresa.findByIdfilialempresa", query = "SELECT f FROM FilialEmpresa f WHERE f.id = :idfilialempresa"),
    @NamedQuery(name = "FilialEmpresa.findByNome", query = "SELECT f FROM FilialEmpresa f WHERE f.nome = :nome"),
    @NamedQuery(name = "FilialEmpresa.findByCNPJ", query = "SELECT f FROM FilialEmpresa f WHERE f.cnpj = :cnpj"),
    @NamedQuery(name = "FilialEmpresa.findByAtiva", query = "SELECT f FROM FilialEmpresa f WHERE f.ativa = :ativa")})
public class FilialEmpresa implements Serializable {

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
    @Column(name = "idfilialempresa")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nome")
    private String nome;
    
    @Size(max = 18)
    @Column(name = "cnpj")
    private String cnpj;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "ativa")
    private boolean ativa;
    
    @JoinColumn(name = "idempresa", referencedColumnName = "idempresa")
    @ManyToOne(optional = false)
    private Empresa matriz;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresa")
    private List<Tarefa> tarefas;

    @Basic(optional = false)
    @NotNull
    @Column(name = "datahorainclusao")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime dataHoraInclusao;
    
    @JoinColumn(name = "idusuarioinclusao", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario usuarioInclusao;
    

    public FilialEmpresa() {
    }

    public FilialEmpresa(Integer idfilialempresa) {
        this.id = idfilialempresa;
    }

    public FilialEmpresa(Integer idfilialempresa, String nome, boolean ativa) {
        this.id = idfilialempresa;
        this.nome = nome;
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

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public Empresa getMatriz() {
        return matriz;
    }

    public void setMatriz(Empresa matriz) {
        this.matriz = matriz;
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
        if (!(object instanceof FilialEmpresa)) {
            return false;
        }
        FilialEmpresa other = (FilialEmpresa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.model.datamodel.FilialEmpresa[ idfilialempresa=" + id + " ]";
    }

    public LocalDateTime getDataHoraInclusao() {
        return dataHoraInclusao;
    }

    public void setDataHoraInclusao(LocalDateTime dataHoraInclusao) {
        this.dataHoraInclusao = dataHoraInclusao;
    }

    public Usuario getUsuarioInclusao() {
        return usuarioInclusao;
    }

    public void setUsuarioInclusao(Usuario usuarioInclusao) {
        this.usuarioInclusao = usuarioInclusao;
    }

    public void setTarefas(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }

    public List<Tarefa> getTarefas() {
        return tarefas;
    }
    
    
}
