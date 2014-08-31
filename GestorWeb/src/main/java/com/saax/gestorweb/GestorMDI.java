package com.saax.gestorweb;

import com.saax.gestorweb.model.DashboardModel;
import com.saax.gestorweb.model.PaginaInicialModel;
import com.saax.gestorweb.presenter.DashboardPresenter;
import com.saax.gestorweb.presenter.PaginaInicialPresenter;
import com.saax.gestorweb.util.CookiesManager;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.util.UserData;
import com.saax.gestorweb.view.DashBoardView;
import com.saax.gestorweb.view.PaginaInicialView;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;

/**
 * Classe de acesso inicial da aplicação Esta é a 1a. classe que é executada ao
 * ser acessada a aplicação Ela é responsavél por efetuar todas as configurações
 * iniciais necessárias ao funcionamento do sistema e carregar a aplicação.
 *
 * @author Rodrigo
 */
@Theme("mytheme")
@PreserveOnRefresh
public class GestorMDI extends UI {


    private transient PaginaInicialModel paginaInicialModel;
    private transient PaginaInicialView paginaInicialView;
    private transient PaginaInicialPresenter paginaInicialPresenter;
    private ResourceBundle mensagens;
    private GestorWebImagens gestorWebImagens;
    
    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = GestorMDI.class, widgetset = "com.saax.gestorweb.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    public void loadPaginaInicial() {
        // Cria a pagina inical
        paginaInicialModel = new PaginaInicialModel();
        paginaInicialView = new PaginaInicialView();

        // O presenter liga model e view
        paginaInicialPresenter = new PaginaInicialPresenter(paginaInicialModel, paginaInicialView);
        
        // adiciona a visualização à UI
        setContent(paginaInicialView);
        
    }
    
    
    

    public void carregarDashBoard() {

        // Cria a pagina inical
        DashboardModel dashboradModel = new DashboardModel();
        DashBoardView dashboardView = new DashBoardView();

        // O presenter liga model e view
        DashboardPresenter dashboardPresenter = new DashboardPresenter(dashboradModel, dashboardView);

        // adiciona a visualização à UI
        setContent(dashboardView);

        setSizeFull();
        
        dashboardPresenter.carregaVisualizacaoInicial();
    }

    /**
     * Método disparado ao ser acessada a aplicação
     *
     * @param request
     */
    @Override
    protected void init(VaadinRequest request) {

        setStyleName("blue");
        
        Logger.getLogger(GestorMDI.class.getName()).log(Level.INFO,"Iniciando atendimento de requisição.");
        
        Logger.getLogger(GestorMDI.class.getName()).log(Level.INFO, "Carregando arquivo de mensagens para o locale: {0}", request.getLocale());

        // obtém o arquivo de mensagens de acordo com o locale do usuário
        mensagens = (ResourceBundle.getBundle("ResourceBundles.Mensagens.Mensagens", new Locale("pt", "br")));

        //obtém os cookies da sessão
        CookiesManager cookieManager = new CookiesManager();
        getSession().setAttribute("cookieManager", cookieManager);
        
        // obtém e armazena as imagens
        gestorWebImagens = new GestorWebImagens();
        
        Logger.getLogger(GestorMDI.class.getName()).log(Level.INFO, "Carregando arquivo de mensagens carregado");

        loadPaginaInicial();

        setSizeFull();

        Logger.getLogger(GestorMDI.class.getName()).log(Level.INFO,"Atendimento de requisição concluído.");
        
    }

    public GestorWebImagens getGestorWebImagens() {
        return gestorWebImagens;
    }

    public ResourceBundle getMensagens() {
        return mensagens;
    }
    
    
}
