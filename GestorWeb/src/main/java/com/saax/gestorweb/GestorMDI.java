package com.saax.gestorweb;

import com.saax.gestorweb.model.PaginaInicialModel;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.presenter.PaginaInicialPresenter;
import com.saax.gestorweb.view.PaginaInicialView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.ResourceBundle;
import javax.servlet.annotation.WebServlet;

/**
 * Classe de acesso inicial da aplicação Esta é a 1a. classe que é executada ao
 * ser acessada a aplicação Ela é responsavél por efetuar todas as configurações
 * iniciais necessárias ao funcionamento do sistema e carregar a aplicação.
 *
 * @author Rodrigo
 */
@Theme("mytheme")
@SuppressWarnings("serial")
public class GestorMDI extends UI {

    // Garante a existencia de apenas uma instancia do recurso de mensagens por sessão
    private ResourceBundle mensagens;

    // Objeto de sessão com o usuario logado
    private Usuario usuario;

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = GestorMDI.class, widgetset = "com.saax.gestorweb.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    public void setUsuarioLogado(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuarioLogado() {
        return usuario;
    }

    public void carregarDashBoard() {

        // TODO
        // setContent(new Label("TESTE: usuario logado = "+usuario.getNome()));
    }

    
    /**
     * Método disparado ao ser acessada a aplicação
     *
     * @param request
     */
    @Override
    protected void init(VaadinRequest request) {

        // obtém o arquivo de mensagens de acordo com o locale do usuário
        mensagens = ResourceBundle.getBundle("ResourceBundles.Mensagens.Mensagens", request.getLocale());

        // Cria a pagina inical
        PaginaInicialModel paginaInicialModel = new PaginaInicialModel();
        PaginaInicialView paginaInicialView = new PaginaInicialView();

        // O presenter liga model e view
        new PaginaInicialPresenter(paginaInicialModel, paginaInicialView);

        // adiciona a visualização à UI
        setContent(paginaInicialView);

        setSizeFull();

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

    /**
     * Encapsula acesso ao ResourceBundle de mensagens da sessão
     *
     * @return Mensagens ResourceBundle
     */
    public ResourceBundle getMensagens() {

        return mensagens;
    }

}
