package com.saax.gestorweb;

import com.saax.gestorweb.model.StartPageModel;
import com.saax.gestorweb.model.TarefaModel;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.presenter.DashboardPresenter;
import com.saax.gestorweb.presenter.LoginPresenter;
import com.saax.gestorweb.presenter.StartPagePresenter;
import com.saax.gestorweb.util.CookiesManager;
import com.saax.gestorweb.util.GestorEntityManagerProvider;

import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.util.PostgresConnection;
import com.saax.gestorweb.util.SessionAttributesEnum;
import com.saax.gestorweb.view.DashboardView;
import com.saax.gestorweb.view.LoginView;
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
import java.net.URI;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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
@Theme("valo-default")
public class GestorMDI extends UI {

    private transient StartPageModel startPageModel;
    private transient StartPageView startPageView;
    private transient ResourceBundle mensagens;
    private transient Properties application;
    private transient GestorWebImagens gestorWebImagens;
    
    
    public URI getLocation() {
        if ((boolean)GestorSession.getAttribute(SessionAttributesEnum.TEST_MODE)){
            return null;
        } else {
            return UI.getCurrent().getPage().getLocation();
        }
    }

    @WebServlet(value = "/*", asyncSupported = true, initParams = { /*@WebInitParam(name = "org.atmosphere.cpr.AtmosphereInterceptor", value = "com.saax.gestorweb.util.AtmosphereFilter")*/})
    @VaadinServletConfiguration(productionMode = false, ui = GestorMDI.class, widgetset = "com.saax.gestorweb.AppWidgetSet")
    public static class Servlet extends VaadinServlet {

        @Override
        protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

            EntityManagerFactory entityManagerFactory;
            EntityManager entityManager = null;

            entityManagerFactory = PostgresConnection.getInstance().getEntityManagerFactory();

            entityManager = entityManagerFactory.createEntityManager();

            // Create and set the entity manager
            GestorEntityManagerProvider.setCurrentEntityManager(entityManager);

            try {
                super.service(req, resp);

            } finally {

                if (entityManager.isOpen()) {
                    entityManager.close();
                }
                // Reset the entity manager
                GestorEntityManagerProvider.setCurrentEntityManager(null);
            }

        }
    };

    public void loadstartPage() {
        // Cria a pagina inical
        startPageModel = new StartPageModel();
        startPageView = new StartPageView();

        // O presenter liga model e view
        StartPagePresenter startPagePresenter = new StartPagePresenter(startPageModel, startPageView);

        // adiciona a visualização à UI
        setContent(startPageView);

        Styles styles = Page.getCurrent().getStyles();
        // inject the new color as a style
        styles.add("$v-background-color: hsl(200, 50%, 50%)");
    }

    public void carregarDashBoard() {
        carregarDashBoard(null);
    }

    public void carregarDashBoard(Tarefa taskToOpen) {

        // Cria a pagina inical
        DashboardView dashboardView = new DashboardView();

        // O presenter liga model e view
        DashboardPresenter dashboardPresenter = new DashboardPresenter(dashboardView);
        if (taskToOpen != null) {
            dashboardPresenter.openTask(taskToOpen);
        }
        // adiciona a visualização à UI
        setContent(dashboardView);

        setSizeFull();

        dashboardPresenter.init();
    }

    @Override
    public void init(VaadinRequest request) {

        // obtém o arquivo de mensagens de acordo com o locale do usuário
        mensagens = (ResourceBundle.getBundle("ResourceBundles.Messages.Messages", new Locale("pt", "BR")));
        //mensagens = (ResourceBundle.getBundle("ResourceBundles.Messages.Messages", request.getLocale()));

        application = new Properties();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("Application.properties");
        if (inputStream == null) {
            throw new RuntimeException("property file 'Application.properties' not found in the classpath");
        }
        try {
            application.load(inputStream);

        } catch (IOException ex) {
            Logger.getLogger(GestorMDI.class
                    .getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

        //obtém os cookies da sessão
        CookiesManager cookieManager = new CookiesManager();
        GestorSession.setAttribute(SessionAttributesEnum.COOKIES_MANAGER, cookieManager);

        // obtém e armazena as imagens
        gestorWebImagens = new GestorWebImagens();

        if (request != null) {
            // Delega a classe TrataParametros a identificação e tratamento
            // dos parâmetros de entrada
            TrataParametros trataParametros = new TrataParametros(this);
            trataParametros.trataParametrosDeEntrada(request);
        }

        setSizeFull();
        
        GestorSession.setAttribute(SessionAttributesEnum.TEST_MODE,request==null);

    }

    /**
     * Logout geral
     */
    public void logout() {


        // Close the VaadinServiceSession
        (UI.getCurrent()).getSession().close();
        (UI.getCurrent()).getPage().reload();
        
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
