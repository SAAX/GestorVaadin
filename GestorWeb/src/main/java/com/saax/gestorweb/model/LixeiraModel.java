/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.StatusTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.presenter.PresenterUtils;
import java.util.List;

/**
 *
 * @author rodrigo
 */
public class LixeiraModel {
    
    public static List<Tarefa> listarTarefasRemovidas(Usuario usuarioLogado) {
        return TarefaModel.listarTarefasRemovidas(usuarioLogado);
    }

    public static void restaurarTarefa(Tarefa tarefa, Usuario usuarioLogado) {
        TarefaModel.restaurarTarefa(tarefa, usuarioLogado);
        TarefaModel.gravarTarefa(tarefa);
    }

    public static void removerTarefa(Tarefa tarefa, Usuario usuarioLogado) {
        TarefaModel.removerTarefa(tarefa, usuarioLogado);
        TarefaModel.gravarTarefa(tarefa);

    }
    
    /**
     * Verifica se o usuário tem permissao para remover uma tarefa De acordo com
     * a RN: REMOÇÃO DE TAREFAS
     *
     * @param tarefa
     * @param usuarioLogado
     * @return
     */
    public static boolean verificaPermissaoAcessoRemocaoTarefa(Tarefa tarefa, Usuario usuarioLogado) {

        if (!usuarioLogado.equals(tarefa.getUsuarioSolicitante())) {
            return false;
        }

        if (tarefa.getDataHoraRemocao() != null) {
            return false;
        }

        if (tarefa.getStatus().equals(StatusTarefa.CONCLUIDA)) {
            return false;
        }

        return true;

    }
    

}
