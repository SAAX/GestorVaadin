/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.util;

import com.vaadin.server.VaadinSession;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rodrigo
 */
public class GestorSession {

    private static final Map<String, Object> attributes = new HashMap<>();

    public static void setAttribute(SessionAttributesEnum name, Object value) {

        // se execução normal
        if (VaadinSession.getCurrent() != null) {
            VaadinSession.getCurrent().setAttribute(name.getAttributeName(), value);
        } else {
            // se em testes 
            setLocalAttribute(name, value);
        }
    }

    private static void setLocalAttribute(SessionAttributesEnum name, Object value) {
        if (name == null) {
            throw new IllegalArgumentException("name can not be null");
        }
        if (value != null) {
            attributes.put(name.getAttributeName(), value);
        } else {
            attributes.remove(name.getAttributeName());
        }

    }

    public static Object getAttribute(SessionAttributesEnum name){
        
        // se execução normal
        if (VaadinSession.getCurrent() != null) {
            return VaadinSession.getCurrent().getAttribute(name.getAttributeName());
        } else {
            // se em testes 
            return getLocalAttribute(name);
        }
    }
    private static Object getLocalAttribute(SessionAttributesEnum name) {

        if (name == null) {
            throw new IllegalArgumentException("name can not be null");
        }
        return attributes.get(name.getAttributeName());
    }
}
