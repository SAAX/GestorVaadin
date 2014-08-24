package com.saax.gestorweb.util;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.dao.UsuarioDAO;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.vaadin.ui.UI;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author rodrigo
 */
public class TestUtils {

    public UI configureUI() {

        if (UI.getCurrent() == null) {
            GestorMDI mdi = new GestorMDI();
            UI.setCurrent(mdi);

            UserData userData = new UserData();

            // loga com usuario default de testes
            UsuarioDAO dao = new UsuarioDAO(PostgresConnection.getInstance().getEntityManagerFactory());
            Usuario usuarioTeste = null;
            //Usuario usuarioTeste = dao.findByLogin("teste-user@gmail.com");
            userData.setUsuarioLogado(usuarioTeste);
            
            
            // obtém o arquivo de mensagens de acordo com o locale do usuário
              ResourceBundle mensagens = ResourceBundle.getBundle("ResourceBundles.Mensagens.Mensagens", new Locale("pt_BR"));
            userData.setMensagens(mensagens);

            //obtém os cookies da sessão
            CookiesManager cookieManager = new CookiesManager();
            userData.setCookies(cookieManager);

            // obtém e armazena as imagens
            GestorWebImagens gestorWebImagens = new GestorWebImagens();
            userData.setImagens(gestorWebImagens);

            mdi.setUserData(userData);
        }
        
        return UI.getCurrent();
    }
}
