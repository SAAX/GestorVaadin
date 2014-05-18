/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.util;

/**
 *
 * @author Rodrigo
 */
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

/**
 * Web application lifecycle listener.
 *
 * @author Rodrigo
 */
@WebListener()
public class GestorServletContextListener implements ServletContextListener  {


    @Resource(name="jdbc/postgres", authenticationType = Resource.AuthenticationType.CONTAINER) DataSource ds;  
    @Override
    public void contextInitialized(ServletContextEvent e) {

        
        EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("GestorWebPU");

        PostgresConnection connection = PostgresConnection.getInstance();
        connection.setEntityManagerFactory(emf);
        
        System.out.println("contextInitialized");
    }
 
    // Release the EntityManagerFactory:
    @Override
    public void contextDestroyed(ServletContextEvent e) {

        PostgresConnection connection = PostgresConnection.getInstance();
        
        EntityManagerFactory emf = connection.getEntityManagerFactory();
        if (emf!=null) {
            emf.close();
            System.out.println("contextDestroyed");
        }
        
    }
}
    

