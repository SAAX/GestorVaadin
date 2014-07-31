package com.saax.gestorweb.model.datamodel;

import java.io.Serializable;
import java.util.Set;
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
 * Entity bean da tabela Status Tarefa com as namequerys configuradas.<br><br>
 * 
 * O objetivo desta entidade é armazenar as situações (Status) das tarefas/subs <br><br>
 *
 * @author rodrigo
 */
@Entity
@Table(name = "statustarefa")
@NamedQueries({
    @NamedQuery(name = "Statustarefa.findAll", query = "SELECT s FROM StatusTarefa s"),
    @NamedQuery(name = "Statustarefa.findByStatustarefa", query = "SELECT s FROM StatusTarefa s WHERE s.statustarefa = :statustarefa")})
public class StatusTarefa implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "statustarefa")
    private String statustarefa;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "status")
    private Set<Tarefa> tarefas;

    public StatusTarefa() {
    }

    public StatusTarefa(String statustarefa) {
        this.statustarefa = statustarefa;
    }

    public String getStatustarefa() {
        return statustarefa;
    }

    public void setStatustarefa(String statustarefa) {
        this.statustarefa = statustarefa;
    }

    public Set<Tarefa> getTarefas() {
        return tarefas;
    }

    public void setTarefas(Set<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (statustarefa != null ? statustarefa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StatusTarefa)) {
            return false;
        }
        StatusTarefa other = (StatusTarefa) object;
        if ((this.statustarefa == null && other.statustarefa != null) || (this.statustarefa != null && !this.statustarefa.equals(other.statustarefa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.Statustarefa[ statustarefa=" + statustarefa + " ]";
    }
    
}
