package com.saax.gestorweb.view;

/**
 * Interface com todos os eventos disparados pela View do dashboard
 * 
 * @author Rodrigo
 */
public interface DashboardViewListenter {
   
    void logout();
    void carregaVisualizacaoInicial();
    void aplicarFiltroPesquisa();

    public void removerFiltrosPesquisa();
}
