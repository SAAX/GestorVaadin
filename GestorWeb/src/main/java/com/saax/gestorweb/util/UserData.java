package com.saax.gestorweb.util;

import com.saax.gestorweb.model.datamodel.Usuario;
import java.util.ResourceBundle;

/**
 * Esta classe é responsável por armazenar todos os dados do usuário e da sessão em andamento
 * É um JavaBean (POJO) que reune em um só lugar todos os dados de sessão
 * @author Rodrigo
 */
public class UserData {
    
    // Usuário logado
    private Usuario usuarioLogado;
    // arquivo de mensagens
    private ResourceBundle mensagens;
    // cookies da sessão
    private CookiesManager cookies;
    // recursos de imagens
    private GestorWebImagens imagens;
    
    

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
     * @return the mensagens
     */
    public ResourceBundle getMensagens() {
        return mensagens;
    }

    /**
     * @param mensagens the mensagens to set
     */
    public void setMensagens(ResourceBundle mensagens) {
        this.mensagens = mensagens;
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

    /**
     * @return the imagens
     */
    public GestorWebImagens getImagens() {
        return imagens;
    }

    /**
     * @param imagens the imagens to set
     */
    public void setImagens(GestorWebImagens imagens) {
        this.imagens = imagens;
    }
    
    
    
    
}
