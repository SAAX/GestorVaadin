package com.saax.gestorweb.view;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import java.util.ResourceBundle;

/**
 * Classe de acesso inicial da aplicação Esta é a classe que é executada ao ser
 * acessada a aplicação Ela é responsavél por efetuar todas as configurações
 * iniciais necessárias ao funcionamento do sistema e carregar a aplicação.
 *
 * @author Rodrigo
 */
@Theme("mytheme")
@SuppressWarnings("serial")
public class GestorMDI extends UI {

    // Garante a existencia de apenas uma instancia do recurso de mensagens por sessão
    private ResourceBundle mensagens;

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = GestorMDI.class, widgetset = "com.saax.gestorweb.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
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

        // Exemplo de utilização do ResourceBundle (remover depois)
        setContent(new ExemploUtilizacaoInternacionalizacao());

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
