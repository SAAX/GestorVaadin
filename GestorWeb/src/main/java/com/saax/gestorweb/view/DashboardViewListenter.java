package com.saax.gestorweb.view;

import com.saax.gestorweb.presenter.DashboardPresenter;

/**
 * Interface com todos os eventos disparados pela View do dashboard
 * 
 * @author Rodrigo
 */
public interface DashboardViewListenter {
   
    void logout();
    void carregaVisualizacaoInicial();
    void removerFiltrosPesquisa();
    void carregarListaTarefasUsuarioLogado();
    void aplicarFiltroPesquisa();
}
