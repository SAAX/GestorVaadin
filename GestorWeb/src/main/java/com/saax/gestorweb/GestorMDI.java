package com.saax.gestorweb;

import com.saax.gestorweb.model.DashboardModel;
import com.saax.gestorweb.model.PaginaInicialModel;
import com.saax.gestorweb.presenter.DashboardPresenter;
import com.saax.gestorweb.presenter.PaginaInicialPresenter;
import com.saax.gestorweb.util.CookiesManager;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.GestorWebImagens;

import com.saax.gestorweb.view.DashboardView;
import com.saax.gestorweb.view.PaginaInicialView;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Page;
import com.vaadin.server.Page.Styles;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Classe de acesso inicial da aplicação Esta é a 1a. classe que é executada ao
 * ser acessada a aplicação Ela é responsavél por efetuar todas as configurações
 * iniciais necessárias ao funcionamento do sistema e carregar a aplicação.
 *
 * @author Rodrigo
 */
@Theme("mytheme")
@Push
public class GestorMDI extends UI {

    private transient PaginaInicialModel paginaInicialModel;
    private transient PaginaInicialView paginaInicialView;
    private transient PaginaInicialPresenter paginaInicialPresenter;
    private ResourceBundle mensagens;
    private Properties application;
    private GestorWebImagens gestorWebImagens;

    @WebServlet(value = "/*", asyncSupported = true, initParams = {
        @WebInitParam(name = "org.atmosphere.cpr.AtmosphereInterceptor", value = "com.saax.gestorweb.util.AtmosphereFilter")
    })
    @VaadinServletConfiguration(productionMode = false, ui = GestorMDI.class, widgetset = "com.saax.gestorweb.AppWidgetSet")
    public static class Servlet extends VaadinServlet {

        @Override
        protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

            try {

                super.service(req, resp);

            } finally {
                // Fecha o entity manger ao fim da requisição
                if (GestorEntityManagerProvider.getEntityManager() != null) {
                    Logger.getLogger(GestorMDI.class.getName()).log(Level.INFO, "Fechando EM no service");
                    GestorEntityManagerProvider.getEntityManager().close();
                    // Libera a variável da thread
                    GestorEntityManagerProvider.remove();
                }
            }
        }

    }

    public void loadPaginaInicial() {
        // Cria a pagina inical
        paginaInicialModel = new PaginaInicialModel();
        paginaInicialView = new PaginaInicialView();

        // O presenter liga model e view
        paginaInicialPresenter = new PaginaInicialPresenter(paginaInicialModel, paginaInicialView);

        // adiciona a visualização à UI
        setContent(paginaInicialView);

        Styles styles = Page.getCurrent().getStyles();
        // inject the new color as a style
        styles.add("$v-background-color: hsl(200, 50%, 50%)");
    }

    public void carregarDashBoard() {

        // Cria a pagina inical
        DashboardModel dashboradModel = new DashboardModel();
        DashboardView dashboardView = new DashboardView();

        // O presenter liga model e view
        DashboardPresenter dashboardPresenter = new DashboardPresenter(dashboradModel, dashboardView);

        // adiciona a visualização à UI
        setContent(dashboardView);

        setSizeFull();

        dashboardPresenter.init();
    }

    /**
     * Método disparado ao ser acessada a aplicação
     *
     * @param request
     */
    @Override
    protected void init(VaadinRequest request) {

        setStyleName("blue");
        
        // obtém o arquivo de mensagens de acordo com o locale do usuário
        mensagens = (ResourceBundle.getBundle("ResourceBundles.Mensagens.Mensagens", request.getLocale()));

        application = new Properties();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("Application.properties");
        if (inputStream == null) {
            throw new RuntimeException("property file 'Application.properties' not found in the classpath");
        }
        try {
            application.load(inputStream);
        } catch (IOException ex) {
            Logger.getLogger(GestorMDI.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

        
        //obtém os cookies da sessão
        CookiesManager cookieManager = new CookiesManager();
        GestorSession.setAttribute("cookieManager", cookieManager);
        

        // obtém e armazena as imagens
        gestorWebImagens = new GestorWebImagens();

        loadPaginaInicial();

        setSizeFull();

    }

    /**
     * Logout geral
     */
    public void logout() {

        (UI.getCurrent()).getPage().setLocation("/GestorWeb");

        // Close the VaadinServiceSession
        (UI.getCurrent()).getSession().close();

    }

    public GestorWebImagens getGestorWebImagens() {
        return gestorWebImagens;
    }

    public ResourceBundle getMensagens() {
        return mensagens;
    }

    public Properties getApplication() {
        return application;
    }

    
    
}
