package com.saax.gestorweb.view;

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
    void criarNovaTarefaViaTemplate();
    void init();

    
}
