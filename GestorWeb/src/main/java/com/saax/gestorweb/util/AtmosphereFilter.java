/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.util;

import com.saax.gestorweb.GestorMDI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

import org.atmosphere.cpr.Action;
import org.atmosphere.cpr.AtmosphereInterceptorAdapter;
import org.atmosphere.cpr.AtmosphereResource;

/**
 * Designed to inject the EntityManager into requests that arrive via websockets
 * (Vaadin Push) as these do not go through the standard servlet filter
 * mechanism.
 * 
* This class is installed by adding a paraeter to the VaadinServlet mapping in
 * web.xml.
 * 
*/
public class AtmosphereFilter extends AtmosphereInterceptorAdapter {

    class MyAtmosphereInterceptor extends AtmosphereInterceptorAdapter {

        @Override
        public Action inspect(AtmosphereResource r) {
//            // do pre-request stuff
//
//            // Cria um entity manager por requisição
//            EntityManager em = PostgresConnection.getInstance().getEntityManagerFactory().createEntityManager();
//            // Armazena na thread
//            GestorEntityManagerProvider.setCurrentEntityManager(em);
//
//                        Logger.getLogger(GestorMDI.class.getName()).log(Level.INFO, "Criando EM na requisicao");
//
            return super.inspect(r);
        }

        @Override
        public void postInspect(AtmosphereResource r) {
            // Fecha o entity manger ao fim da requisição
            if (GestorEntityManagerProvider.getEntityManager() != null) {
                Logger.getLogger(GestorMDI.class.getName()).log(Level.INFO, "Fechando EM no Filter");
                GestorEntityManagerProvider.getEntityManager().close();
                // Libera a variável da thread
                GestorEntityManagerProvider.remove();
            }
        }
    }

//    
//    <init-param>
//    <param-name>org.atmosphere.cpr.AtmosphereInterceptor</param-name>
//    <!-- comma-separated list of fully-qualified class names -->
//    <param-value>com.example.MyAtmosphereInterceptor/param-value>
//</init-param>
}
