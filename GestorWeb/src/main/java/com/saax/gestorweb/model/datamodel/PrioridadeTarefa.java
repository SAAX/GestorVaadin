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
 * Entity bean da tabela Prioridade Tarefa com as namequerys configuradas.<br><br>
 * 
 * O objetivo desta entidade é armazenar as enumareções das prioridades das Tarefas e subs <br><br>
 *
 * @author rodrigo
 */
@Entity
@Table(name = "prioridadetarefa")
@NamedQueries({
    @NamedQuery(name = "PrioridadeTarefa.findAll", query = "SELECT p FROM PrioridadeTarefa p"),
    @NamedQuery(name = "PrioridadeTarefa.findByPrioridadetarefa", query = "SELECT p FROM PrioridadeTarefa p WHERE p.prioridadetarefa = :prioridadetarefa")})
public class PrioridadeTarefa implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "prioridadetarefa")
    private String prioridadetarefa;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "prioridade")
    private List<Tarefa> tarefas;

    public PrioridadeTarefa() {
    }

    public PrioridadeTarefa(String prioridadetarefa) {
        this.prioridadetarefa = prioridadetarefa;
    }

    public String getPrioridadetarefa() {
        return prioridadetarefa;
    }

    public void setPrioridadetarefa(String prioridadetarefa) {
        this.prioridadetarefa = prioridadetarefa;
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
        hash += (prioridadetarefa != null ? prioridadetarefa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrioridadeTarefa)) {
            return false;
        }
        PrioridadeTarefa other = (PrioridadeTarefa) object;
        if ((this.prioridadetarefa == null && other.prioridadetarefa != null) || (this.prioridadetarefa != null && !this.prioridadetarefa.equals(other.prioridadetarefa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.model.datamodel.PrioridadeTarefa[ prioridadetarefa=" + prioridadetarefa + " ]";
    }
    
}
