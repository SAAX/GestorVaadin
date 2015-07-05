/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.view;

import com.saax.gestorweb.model.datamodel.Tarefa;

/**
 *
 * @author rodrigo
 */
public interface PopUpStatusListener {

    
    public void taskStatusChanged(Tarefa task);
    
}
