package com.saax.gestorweb.util;

import com.vaadin.server.VaadinService;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.Cookie;

/**
 * Classe responsável pelo gerenciamento dos cookies de sessão.
 *
 * @author Rodrigo
 */
public class CookiesManager implements Serializable {

    private final Set<Cookie> cookies;

    /**
     * Enumeração com todos os cookies da app Esta enum deve reunir todos os
     * cookies para uim gerenciamento mais efetivo
     */
    public enum GestorWebCookieEnum {

        NOME_USUARIO("GestorWEB.nomeUsuario");

        private final String name;

        private GestorWebCookieEnum(String cookieName) {
            this.name = cookieName;
        }

        public String getName() {
            return name;
        }

    };

    /**
     * Grava um cookie para a sessão da aplicação
     *
     * @param gestorWebCookie cookie a ser gravado
     * @param valor
     */
    public void setCookie(GestorWebCookieEnum gestorWebCookie, String valor) {

        // verifica se o cookie já existe
        Cookie cookie = getCookie(gestorWebCookie);

        // se já existe seta o valor, senão cria um novo
        if (cookie != null) {
            cookie.setValue(valor);

        } else {
            cookie = new Cookie(gestorWebCookie.getName(), valor);

        }

        if (VaadinService.getCurrentRequest() != null) {
            cookie.setPath(VaadinService.getCurrentRequest().getContextPath());
            VaadinService.getCurrentResponse().addCookie(cookie);
        } else {
            cookies.add(cookie);
        }

    }

    public CookiesManager() {
        cookies = new HashSet<>();
        if (VaadinService.getCurrentRequest() != null && VaadinService.getCurrentRequest().getCookies() != null
                && VaadinService.getCurrentRequest().getCookies().length > 0) {
            cookies.addAll(Arrays.asList(VaadinService.getCurrentRequest().getCookies()));
        }
    }

    /**
     * Obtém um cookie pelo nome Metodo privado: só deve ser acessado
     * internamente
     *
     * @param gestorWebCookie
     * @return cookie
     */
    private Cookie getCookie(GestorWebCookieEnum gestorWebCookie) {

        for (Cookie cookie : cookies) {
            if (gestorWebCookie.getName().equals(cookie.getName())) {
                return cookie;
            }
        }

        return null;
    }

    /**
     * Obtém o valor de um cookie gravado na sessão
     *
     * @param gestorWebCookie
     * @return valor do cookie
     */
    public String getCookieValue(GestorWebCookieEnum gestorWebCookie) {
        Cookie cookie = getCookie(gestorWebCookie);
        if (cookie == null || cookie.getValue() == null || cookie.getValue().equals("")) {
            return null;
        } else {
            return cookie.getValue();
        }

    }

    /**
     * Limpa o valor de um cookie
     *
     * @param gestorWebCookieEnum
     */
    public void destroyCookie(GestorWebCookieEnum gestorWebCookieEnum) {
        setCookie(gestorWebCookieEnum, "");
    }

}
