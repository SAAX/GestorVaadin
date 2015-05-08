package com.saax.gestorweb;

import com.saax.gestorweb.model.DashboardModel;
import com.saax.gestorweb.model.StartPageModel;
import com.saax.gestorweb.presenter.DashboardPresenter;
import com.saax.gestorweb.presenter.StartPagePresenter;
import com.saax.gestorweb.util.CookiesManager;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.view.DashboardView;
import com.saax.gestorweb.view.StartPageView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Page;
import com.vaadin.server.Page.Styles;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Main access point of application <br>
 * This class is responsible for all app's initial configuration
 *
 * @author Rodrigo
 */
@Theme("valo-light")
public class GestorMDI extends UI {

    private transient StartPageModel startPageModel;
    private transient StartPageView startPageView;
    private transient ResourceBundle mensagens;
    private transient Properties application;
    private transient GestorWebImagens gestorWebImagens;

    @WebServlet(value = "/*", asyncSupported = true, initParams = { /*@WebInitParam(name = "org.atmosphere.cpr.AtmosphereInterceptor", value = "com.saax.gestorweb.util.AtmosphereFilter")*/})
    @VaadinServletConfiguration(productionMode = true, ui = GestorMDI.class, widgetset = "com.saax.gestorweb.AppWidgetSet")
    public static class Servlet extends VaadinServlet {

        @Override
        protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

            try {

                super.service(req, resp);

            } finally {
                // Fecha o entity manger ao fim da requisição
                if (GestorEntityManagerProvider.getEntityManager() != null) {
                    Logger.getLogger(GestorMDI.class.getName()).log(Level.INFO, "Closing EM on service method");
                    GestorEntityManagerProvider.getEntityManager().close();
                    GestorEntityManagerProvider.remove();
                }
            }
        }

    }

    public void loadstartPage() {
        // Cria a pagina inical
        startPageModel = new StartPageModel();
        startPageView = new StartPageView();

        // O presenter liga model e view
        new StartPagePresenter(startPageModel, startPageView);

        // adiciona a visualização à UI
        setContent(startPageView);

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

    @Override
    protected void init(VaadinRequest request) {

        // obtém o arquivo de mensagens de acordo com o locale do usuário
        mensagens = (ResourceBundle.getBundle("ResourceBundles.Messages.Messages", request.getLocale()));

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

        loadstartPage();

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
