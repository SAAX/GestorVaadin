package com.saax.gestorweb.model.datamodel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity bean da tabela Centro Custo com as namequerys configuradas<br>
 * O objetivo desta entidade e armazenar os Centros de Custo para indicação nas metas<br>
 * 
 * @author rodrigo
 */
@Entity
@Table(name = "centrocusto")
@NamedQueries({
    @NamedQuery(name = "CentroCusto.findAll", query = "SELECT c FROM CentroCusto c"),
    @NamedQuery(name = "CentroCusto.findById", query = "SELECT c FROM CentroCusto c WHERE c.id = :id"),
    @NamedQuery(name = "CentroCusto.findByEmpresa", query = "SELECT c FROM CentroCusto c WHERE c.empresa = :empresa"),
    @NamedQuery(name = "CentroCusto.findByEmpresaAtivo", query = "SELECT d FROM CentroCusto d WHERE d.ativo = true AND d.empresa = :empresa"),
    @NamedQuery(name = "CentroCusto.findByCentrocusto", query = "SELECT c FROM CentroCusto c WHERE c.centroCusto = :centrocusto"),
    @NamedQuery(name = "CentroCusto.findByAtivo", query = "SELECT c FROM CentroCusto c WHERE c.ativo = :ativo")})
public class CentroCusto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idcentrocusto")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "centrocusto")
    private String centroCusto;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "ativo")
    private boolean ativo;
    
    @JoinColumn(name = "idempresa", referencedColumnName = "idempresa")
    @ManyToOne(optional = false)
    private Empresa empresa;
    
    @OneToMany(mappedBy = "centroCusto")
    private Collection<Meta> metas;

    @Column(name = "datahorainclusao")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime dataHoraInclusao;
    
    @JoinColumn(name = "idusuarioinclusao", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario usuarioInclusao;
    
    @OneToMany(mappedBy = "centroCusto")
    private List<Tarefa> tarefas;

    public CentroCusto() {
    }

    public CentroCusto(Integer idcentrocusto) {
        this.id = idcentrocusto;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer idcentrocusto) {
        this.id = idcentrocusto;
    }


    public String getCentroCusto() {
        return centroCusto;
    }

    public void setCentroCusto(String centroCusto) {
        this.centroCusto = centroCusto;
    }

    public boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Collection<Meta> getMetas() {
        return metas;
    }

    public void setMetas(Collection<Meta> metas) {
        this.metas = metas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CentroCusto)) {
            return false;
        }
        CentroCusto other = (CentroCusto) object;
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
        return "com.saax.gestorweb.model.datamodel.CentroCusto[ idcentrocusto=" + id + " ]";
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
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

    public List<Tarefa> getTarefas() {
        return tarefas;
    }

    public void setTarefas(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }
    
}
