package com.saax.gestorweb.model.datamodel;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity bean da tabela Tipo Tarefa com as namequerys configuradas.<br><br>
 * 
 * O objetivo desta entidade é armazenar a enumeração dos tipos das tarefas <br><br>
 *
 * @author rodrigo
 */
@Entity
@Table(name = "tipotarefa")
@NamedQueries({
    @NamedQuery(name = "TipoTarefa.findAll", query = "SELECT t FROM TipoTarefa t"),
    @NamedQuery(name = "TipoTarefa.findByTipotarefa", query = "SELECT t FROM TipoTarefa t WHERE t.tipotarefa = :tipotarefa")})
public class TipoTarefa implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "tipotarefa")
    private String tipotarefa;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipo")
    private List<Tarefa> tarefas;

    public TipoTarefa() {
    }

    public TipoTarefa(String tipotarefa) {
        this.tipotarefa = tipotarefa;
    }

    public String getTipotarefa() {
        return tipotarefa;
    }

    public void setTipotarefa(String tipotarefa) {
        this.tipotarefa = tipotarefa;
    }

    public List<Tarefa> getTarefas() {
        return tarefas;
    }

    public void setTarefas(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tipotarefa != null ? tipotarefa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoTarefa)) {
            return false;
        }
        TipoTarefa other = (TipoTarefa) object;
        if ((this.tipotarefa == null && other.tipotarefa != null) || (this.tipotarefa != null && !this.tipotarefa.equals(other.tipotarefa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.model.datamodel.TipoTarefa[ tipotarefa=" + tipotarefa + " ]";
    }
    
}
