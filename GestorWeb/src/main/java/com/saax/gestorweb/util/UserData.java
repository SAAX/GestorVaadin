package com.saax.gestorweb.util;

import com.saax.gestorweb.model.datamodel.Usuario;
import java.io.Serializable;
import java.util.ResourceBundle;

/**
 * Esta classe é responsável por armazenar todos os dados do usuário e da sessão em andamento
 * É um JavaBean (POJO) que reune em um só lugar todos os dados de sessão
 * @author Rodrigo
 */
public class UserData implements Serializable {
    
    // Usuário logado
    private Usuario loggedUser;
    // cookies da sessão
    private CookiesManager cookies;
    
    

    /**
     * @return the loggedUser
     */
    public Usuario getUsuarioLogado() {
        return loggedUser;
    }

    /**
     * @param loggedUser the loggedUser to set
     */
    public void setUsuarioLogado(Usuario loggedUser) {
        this.loggedUser = loggedUser;
    }

    /**
     * @return the cookies
     */
    public CookiesManager getCookies() {
        return cookies;
    }

    /**
     * @param cookies the cookies to set
     */
    public void setCookies(CookiesManager cookies) {
        this.cookies = cookies;
    }

}
