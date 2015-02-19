/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.util;


/**
 * Designed to inject the EntityManager into requests that arrive via websockets
 * (Vaadin Push) as these do not go through the standard servlet filter
 * mechanism.
 * 
* This class is installed by adding a paraeter to the VaadinServlet mapping in
 * web.xml.
 * 
*/
public class AtmosphereFilter {} /*extends AtmosphereInterceptorAdapter {

        @Override
        public Action inspect(AtmosphereResource r) {
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
    }*/

