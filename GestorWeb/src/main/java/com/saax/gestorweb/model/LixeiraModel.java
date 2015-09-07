/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.StatusMeta;
import com.saax.gestorweb.model.datamodel.StatusTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rodrigo
 */
public class LixeiraModel {
    
    public static List<Tarefa> listarTarefasRemovidas(Usuario usuarioLogado) {
        return TarefaModel.listarTarefasRemovidas(usuarioLogado);
    }

    public static List<Meta> listarMetasRemovidas(Usuario usuarioLogado) {
        return MetaModel.listarMetasRemovidas(usuarioLogado);
    }

    public static void restaurarTarefa(Tarefa tarefa, Usuario usuarioLogado) {
        TarefaModel.restaurarTarefa(tarefa, usuarioLogado);
        TarefaModel.gravarTarefa(tarefa);
    }

    public static void restaurarMeta(Meta meta, Usuario usuarioLogado) {
        MetaModel.restaurarMeta(meta, usuarioLogado);
        MetaModel.gravarMeta(meta);
    }

    public static void removerTarefa(Tarefa tarefa, Usuario usuarioLogado) {
        TarefaModel.removerTarefa(tarefa, usuarioLogado);
        TarefaModel.gravarTarefa(tarefa);

    }
    
    public static void removerMeta(Meta meta, Usuario usuarioLogado) {
        MetaModel.removerMeta(meta, usuarioLogado);
        MetaModel.gravarMeta(meta);

    }
    
    /**
     * Verifica se o usuário tem permissao para remover uma tarefa De acordo com
     * a RN: RN: REMOÇÃO DE METAS E TAREFAS
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

    /**
     * Verifica se o usuário tem permissao para remover uma meta <br> 
     * De acordo com a RN: REMOÇÃO DE METAS E TAREFAS
     *
     * @param usuarioLogado
     * @return
     */
    public static boolean verificaPermissaoAcessoRemocaoMeta(Meta meta, Usuario usuarioLogado) {
        if (!usuarioLogado.equals(meta.getUsuarioSolicitante())) {
            return false;
        }

        if (meta.getDataHoraRemocao() != null) {
            return false;
        }

        if (meta.getStatus().equals(StatusMeta.CONCLUIDA)) {
            return false;
        }

        return true;
    }

    /**
     * Percorre a lista de tarefas e monta uma nova lista, apenas com as tarefas que não estão na lixeira (datahoraremoção nula) <br>
     * 
     * @param subTarefas
     * @return uma lista com as tarefas não removidas
     */
    public static List<Tarefa> filtrarTarefasRemovidas(List<Tarefa> subTarefas){
        
        List<Tarefa> tarefasAtivas = new ArrayList<>();
        
        for (Tarefa subTarefa : subTarefas) {
            if (subTarefa.getDataHoraRemocao()==null){
                tarefasAtivas.add(subTarefa);
            }
            if (!subTarefa.getSubTarefas().isEmpty()){
                subTarefa.setSubTarefas(filtrarTarefasRemovidas(subTarefa.getSubTarefas()));
            }
        }
        return tarefasAtivas;
    }
    
    

}
