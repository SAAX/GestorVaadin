package com.saax.gestorweb;

import com.saax.gestorweb.model.LoginModel;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.DBConnect;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.PostgresConnection;
import com.vaadin.ui.UI;
import javax.persistence.EntityManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author rodrigo
 */
public class DashboardTest {
    

    @BeforeClass
    public static void setUpClass() {
        // connect to database
        DBConnect.getInstance().assertConnection();
        EntityManager em = PostgresConnection.getInstance().getEntityManagerFactory().createEntityManager();
        GestorEntityManagerProvider.setCurrentEntityManager(em);
        
        // set logged user
        Usuario usuario = (Usuario) em.createNamedQuery("Usuario.findByLogin").setParameter("login", "teste-user@gmail.com").getSingleResult();
        GestorSession.setAttribute("usuarioLogado", usuario);
        usuario.setEmpresaAtiva(new LoginModel().getEmpresaUsuarioLogado());
        
        // creates UI
        GestorMDI gestor = new GestorMDI();
        UI.setCurrent(gestor);
        gestor.init(null);

    }

    @AfterClass
    public static void tearDownClass() {
        // disconnect
        GestorEntityManagerProvider.getEntityManager().close();
        GestorEntityManagerProvider.setCurrentEntityManager(null);
    }

    
    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }
    
    @Test
    public void cadastrarNovaTarefa(){
        
    }

}
