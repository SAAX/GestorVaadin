package com.saax.gestorweb.view;

import com.saax.gestorweb.model.datamodel.Usuario;

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
    void usuarioLogadoAlteradoAPENASTESTE();

    
}
