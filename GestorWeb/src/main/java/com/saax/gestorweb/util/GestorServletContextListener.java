package com.saax.gestorweb.util;

import com.saax.gestorweb.GestorMDI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Web application lifecycle listener.
 *
 */
@WebListener
public class GestorServletContextListener implements ServletContextListener {

    /**
     * Inicilizador do contexto
     * Este método é chamado toda vez a aplicação é iniciada no servidor
     * Atualmente este método é responsável por estabelecer a conexão com a base de dados
     * @param e
     */
    @Override
    public void contextInitialized(ServletContextEvent e) {

        Logger.getLogger(GestorMDI.class.getName()).log(Level.INFO, "Iniciando contexto...");
        Logger.getLogger(GestorMDI.class.getName()).log(Level.INFO, "Criando e armazenando EMF...");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("GestorWebPU");

        PostgresConnection connection = PostgresConnection.getInstance();
        Logger.getLogger(GestorMDI.class.getName()).log(Level.INFO, "EMF criada e armazenada!");
        connection.setEntityManagerFactory(emf);
        Logger.getLogger(GestorMDI.class.getName()).log(Level.INFO, "Contexto inicializado!");
        

    }

    /**
     * Finalizador do contexto
     * Este método é chamado toda vez a aplicação é encerrada no servidor
     * Atualmente este método é responsável por encerrar a conexão com a base de dados
     * @param e
     */
    @Override
    public void contextDestroyed(ServletContextEvent e) {

        Logger.getLogger(GestorMDI.class.getName()).log(Level.INFO, "Destruindo contexto...");
        PostgresConnection connection = PostgresConnection.getInstance();

        EntityManagerFactory emf = connection.getEntityManagerFactory();
        if (emf != null) {
            Logger.getLogger(GestorMDI.class.getName()).log(Level.INFO, "Fechando conexões...");
            emf.close();
            Logger.getLogger(GestorMDI.class.getName()).log(Level.INFO, "Contexto destruído");
        }

    }
}
