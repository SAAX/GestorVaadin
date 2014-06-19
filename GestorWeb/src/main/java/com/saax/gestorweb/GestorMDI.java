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
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
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
@Theme("my-chameleon")
@SuppressWarnings("serial")
@PreserveOnRefresh
public class GestorMDI extends UI {

  

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = GestorMDI.class, widgetset = "com.saax.gestorweb.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    // Dados do usuário da sessão
    private UserData userData;

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public UserData getUserData() {
        return userData;
    }
    
    

    public void carregarDashBoard() {

        // Cria a pagina inical
        DashboardModel paginaInicialModel = new DashboardModel();
        DashBoardView paginaInicialView = new DashBoardView();

        // O presenter liga model e view
        new DashboardPresenter(paginaInicialModel, paginaInicialView);

        // adiciona a visualização à UI
        setContent(paginaInicialView);

    }

    
    /**
     * Método disparado ao ser acessada a aplicação
     *
     * @param request
     */
    @Override
    protected void init(VaadinRequest request) {

        Logger.getLogger(GestorMDI.class.getName()).log(Level.INFO,"Iniciando atendimento de requisição.");
        
        userData = new UserData();
        
        Logger.getLogger(GestorMDI.class.getName()).log(Level.INFO, "Carregando arquivo de mensagens para o locale: {0}", request.getLocale());

        // obtém o arquivo de mensagens de acordo com o locale do usuário
        ResourceBundle mensagens = ResourceBundle.getBundle("ResourceBundles.Mensagens.Mensagens", request.getLocale());
        userData.setMensagens(mensagens);

        //obtém os cookies da sessão
        CookiesManager cookieManager = new CookiesManager();
        userData.setCookies(cookieManager);
        
        // obtém e armazena as imagens
        GestorWebImagens gestorWebImagens = new GestorWebImagens();
        userData.setImagens(gestorWebImagens);
        
        Logger.getLogger(GestorMDI.class.getName()).log(Level.INFO, "Carregando arquivo de mensagens carregado");
        
        // Cria a pagina inical
        PaginaInicialModel paginaInicialModel = new PaginaInicialModel();
        PaginaInicialView paginaInicialView = new PaginaInicialView();

        // O presenter liga model e view
        new PaginaInicialPresenter(paginaInicialModel, paginaInicialView);

        // adiciona a visualização à UI
        setContent(paginaInicialView);

        setSizeFull();

        Logger.getLogger(GestorMDI.class.getName()).log(Level.INFO,"Atendimento de requisição concluído.");
    }

    /**
     * Cria o container com os elementos visuais da página inicial de
     * apresentação
     *
     */
    private VerticalLayout buildPaginaInicial() {
        VerticalLayout layout = new VerticalLayout();

        // ocupar todo espaço disponível
        layout.setSizeFull();

        // inserir imagens iniciais
        // @TODO
        Panel panel = new Panel();

        return layout;

    }


}
