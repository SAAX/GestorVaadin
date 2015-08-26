/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.util;

/**
 *
 * @author rodrigo
 */
public enum SessionAttributesEnum {

    TEST_MODE("testMode"),
    COOKIES_MANAGER("cookieManager"),
    USUARIO_LOGADO("loggedUser");
    
    private final String attributeName;
    
    private SessionAttributesEnum(String nome){
        this.attributeName = nome;
    }

    public String getAttributeName() {
        return attributeName;
    }
    
    
}
