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

    public static void setAttribute(String name, Object value) {

        // se execução normal
        if (VaadinSession.getCurrent() != null) {
            VaadinSession.getCurrent().setAttribute(name, value);
        } else {
            // se em testes 
            setLocalAttribute(name, value);
        }
    }

    private static void setLocalAttribute(String name, Object value) {
        if (name == null) {
            throw new IllegalArgumentException("name can not be null");
        }
        if (value != null) {
            attributes.put(name, value);
        } else {
            attributes.remove(name);
        }

    }

    public static Object getAttribute(String name){
        
        // se execução normal
        if (VaadinSession.getCurrent() != null) {
            return VaadinSession.getCurrent().getAttribute(name);
        } else {
            // se em testes 
            return getLocalAttribute(name);
        }
    }
    private static Object getLocalAttribute(String name) {

        if (name == null) {
            throw new IllegalArgumentException("name can not be null");
        }
        return attributes.get(name);
    }
}
