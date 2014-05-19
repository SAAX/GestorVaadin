package com.saax.gestorweb.util;

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
 */
@WebListener()
public class GestorServletContextListener implements ServletContextListener {

    @Resource(name = "jdbc/postgres", authenticationType = Resource.AuthenticationType.CONTAINER)
    DataSource ds;
    
    /**
     * Inicilizador do contexto
     * Este método é chamado toda vez a aplicação é iniciada no servidor
     * Atualmente este método é responsável por estabelecer a conexão com a base de dados
     * @param e
     */
    @Override
    public void contextInitialized(ServletContextEvent e) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("GestorWebPU");

        PostgresConnection connection = PostgresConnection.getInstance();
        connection.setEntityManagerFactory(emf);

    }

    /**
     * Finalizador do contexto
     * Este método é chamado toda vez a aplicação é encerrada no servidor
     * Atualmente este método é responsável por encerrar a conexão com a base de dados
     * @param e
     */
    @Override
    public void contextDestroyed(ServletContextEvent e) {

        PostgresConnection connection = PostgresConnection.getInstance();

        EntityManagerFactory emf = connection.getEntityManagerFactory();
        if (emf != null) {
            emf.close();
            System.out.println("contextDestroyed");
        }

    }
}
