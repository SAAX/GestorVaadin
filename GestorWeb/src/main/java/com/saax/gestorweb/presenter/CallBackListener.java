/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.presenter;

import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.RecurrencySet;
import com.saax.gestorweb.model.datamodel.Tarefa;
import java.util.List;

/**
 *
 * @author rodrigo
 */
public interface CallBackListener {
    /**
     * Trata o evento disparado via callback quando uma tarefa é removida
     *
     * @param tarefaRemovida
     */
    public void tarefaRemovida(Tarefa tarefaRemovida);

    public void metaRemovida(Meta meta);        
    /**
     * Handles the event thrown when the sub windows with all recurrency parameters is closed
     * @param recurrencySet
     */
    public void recurrencyCreationDone(RecurrencySet recurrencySet);

    public void recurrencyRemoved(Tarefa task);
    
    /**
     * Trata o evento disparado ao concluir a criação de uma nova tarefa ou
     * alteração de uma
     *
     * @param tarefaCriada
     */
    public void atualizarApresentacaoTarefa(Tarefa tarefaCriada);


    /**
     * retorna a lista de callback ja registrados
     * @return 
     */
    public List<CallBackListener> getCallbackListeneres();

    public void addCallbackListener(CallBackListener callBackListener);

    public void atualizarApresentacaoMeta(Meta meta);

    public void tarefaRestaurada(Tarefa tarefa);

    public void metaRestaurada(Meta meta);
}
