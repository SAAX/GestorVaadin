package com.saax.gestorweb.callback;

import com.saax.gestorweb.model.datamodel.Tarefa;
import java.util.List;

/**
 * Call back listener interface disparada ao criar ou atualizar uma tarefa <br>
 * As classes que implentam esta interface devem tratar os eventos disparados
 *
 * @author rodrigo
 */
public interface TarefaCallBackListener {

    /**
     * Trata o evento disparado ao concluir a criação de uma nova tarefa ou
     * alteração de uma
     *
     * @param tarefaCriada
     */
    public void atualizarApresentacaoTarefa(Tarefa tarefaCriada);

    /**
     * Trata o evento disparado via callback quando uma tarefa é removida
     *
     * @param tarefaRemovida
     */
    public void tarefaRemovida(Tarefa tarefaRemovida);

    /**
     * retorna a lista de callback ja registrados
     * @return 
     */
    public List<TarefaCallBackListener> getCallbackListeneres();
}
