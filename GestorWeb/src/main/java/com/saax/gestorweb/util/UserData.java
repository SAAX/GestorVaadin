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
    private Usuario usuarioLogado;
    // cookies da sessão
    private CookiesManager cookies;
    
    

    /**
     * @return the usuarioLogado
     */
    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    /**
     * @param usuarioLogado the usuarioLogado to set
     */
    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
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
