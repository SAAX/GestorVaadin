package com.saax.gestorweb.view;

import com.saax.gestorweb.model.datamodel.Tarefa;

/**
 * Interface com todos os eventos disparados pela View do dashboard
 * 
 * @author Rodrigo
 */
public interface DashboardViewListenter {
   
    void logout();
    void removerFiltrosPesquisa();
    void carregarListaTarefasUsuarioLogado();
    void carregarListaMetasUsuarioLogado();
    void aplicarFiltroPesquisa();
    void createsNewTaskByTemplate();
    void init();
    void usuarioLogadoAlteradoAPENASTESTE();
    Tarefa criarTarefaPorTemplate(Tarefa template);
    void removerTarefaButtonClicked(Tarefa tarefa);
    boolean verificaPermissaoAcessoRemocaoTarefa(Tarefa tarefa);
    void trashButtonPressed();

    

    
}
