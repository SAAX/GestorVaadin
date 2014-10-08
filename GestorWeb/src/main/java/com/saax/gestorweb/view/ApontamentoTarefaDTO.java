package com.saax.gestorweb.view;

import com.saax.gestorweb.model.datamodel.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ApontamentoTarefaDTO implements Serializable {

    private Integer id;
    
    private BigDecimal custoHora;
    private LocalTime horas;
    private String observacoes;

    public ApontamentoTarefaDTO() {
    }

    
    
    public ApontamentoTarefaDTO(BigDecimal custoHora, LocalTime horas, String observacoes) {
        this.custoHora = custoHora;
        this.horas = horas;
        this.observacoes = observacoes;
    }

    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getCustoHora() {
        return custoHora;
    }

    public void setCustoHora(BigDecimal custoHora) {
        this.custoHora = custoHora;
    }

    public LocalTime getHoras() {
        return horas;
    }

    public void setHoras(LocalTime horas) {
        this.horas = horas;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
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
        if (!(object instanceof ApontamentoTarefaDTO)) {
            return false;
        }
        ApontamentoTarefaDTO other = (ApontamentoTarefaDTO) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.ApontamentoTarefa[ idapontamentotarefa=" + id + " ]";
    }

    
}
