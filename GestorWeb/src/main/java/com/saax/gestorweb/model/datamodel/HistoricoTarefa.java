/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.model.datamodel;

import java.time.LocalDateTime;

/**
 *
 * @author rodrigo
 */
public class HistoricoTarefa implements Comparable<HistoricoTarefa>{

    private final LocalDateTime momento;
    private final String descricao;
    private final Usuario usuario;

    public HistoricoTarefa(LocalDateTime momento, String descricao, Usuario usuario) {
        this.momento = momento;
        this.descricao = descricao;
        this.usuario = usuario;
    }

    public LocalDateTime getMomento() {
        return momento;
    }

    public String getDescricao() {
        return descricao;
    }

    public Usuario getUsuario() {
        return usuario;
    }


    @Override
    public int compareTo(HistoricoTarefa o) {
        return momento.compareTo(o.momento);
    }
    
    
}
