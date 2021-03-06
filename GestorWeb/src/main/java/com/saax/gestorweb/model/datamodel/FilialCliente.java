package com.saax.gestorweb.model.datamodel;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Basic;
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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity bean da tabela FilialCliente com as namequerys configuradas<br><br>
 * 
 * O objetivo desta entidade e armazenar as filiais dos nossos Clientes<br><br>
 *
 * @author rodrigo
 */
@Entity
@Table(name = "filialcliente")
@NamedQueries({
    @NamedQuery(name = "FilialCliente.findAll", query = "SELECT f FROM FilialCliente f"),
    @NamedQuery(name = "FilialCliente.findByNome", query = "SELECT f FROM FilialCliente f WHERE f.nome = :nome"),
    @NamedQuery(name = "FilialCliente.findByCnpj", query = "SELECT f FROM FilialCliente f WHERE f.cnpj = :cnpj"),
    @NamedQuery(name = "FilialCliente.findByAtiva", query = "SELECT f FROM FilialCliente f WHERE f.ativa = :ativa")})
public class FilialCliente implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idfilialcliente")
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
    
    @JoinColumn(name = "idempresacliente", referencedColumnName = "idempresacliente")
    @ManyToOne(optional = false)
    private EmpresaCliente empresaCliente;

    @Column(name = "datahorainclusao")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime dataHoraInclusao;
    
    @JoinColumn(name = "idusuarioinclusao", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario usuarioInclusao;

    
    public FilialCliente() {
    }

    public FilialCliente(Integer idfilialcliente) {
        this.id = idfilialcliente;
    }

    public FilialCliente(Integer idfilialcliente, String nome, boolean ativa) {
        this.id = idfilialcliente;
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

    public boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public EmpresaCliente getEmpresaCliente() {
        return empresaCliente;
    }

    public void setEmpresaCliente(EmpresaCliente empresaCliente) {
        this.empresaCliente = empresaCliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FilialCliente)) {
            return false;
        }
        FilialCliente other = (FilialCliente) object;
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
        return "com.saax.gestorweb.model.datamodel.FilialCliente[ idfilialcliente=" + id + " ]";
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
    
}
