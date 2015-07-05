package com.saax.gestorweb.view;

import com.saax.gestorweb.model.datamodel.Tarefa;

/**
 * Call back listener interface disparada ao criar ou atualizar uma tarefa <br>
 * As classes que implentam esta interface devem tratar os eventos disparados
 * @author rodrigo
 */
public interface TarefaCallBackListener {

    /**
     * Trata o evento disparado ao concluir a criação de uma nova tarefa
     * @param tarefaCriada 
     */
    public void tarefaCriada(Tarefa tarefaCriada);

    /**
     * Trata o evento disparado ao concluir a edição de uma tarefa
     * @param tarefaAtualizada 
     */
    public void tarefaAtualizada(Tarefa tarefaAtualizada);

}
