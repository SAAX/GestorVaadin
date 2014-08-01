package com.saax.gestorweb.model;

import com.saax.gestorweb.dao.UsuarioDAO;
import com.saax.gestorweb.dao.exceptions.IllegalOrphanException;
import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.Cipher;
import com.saax.gestorweb.util.DBConnect;
import com.saax.gestorweb.util.PostgresConnection;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

/**
 *
 * @author rodrigo
 */
public class LoginModelTest {

    public LoginModelTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        DBConnect.getInstance().assertConnection();

    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Remove todos os usuarios cadastrados
     */
    private void limparBase() {
        UsuarioDAO dao = new UsuarioDAO(PostgresConnection.getInstance().getEntityManagerFactory());

        dao.findUsuarioEntities().stream().forEach((usuario) -> {
            try {
                dao.destroy(usuario.getId());
            } catch (IllegalOrphanException | NonexistentEntityException ex) {
                Logger.getLogger(LoginModelTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    @Before
    public void setUp() {
        limparBase();

    }

    @After
    public void tearDown() {

        limparBase();

    }

    @org.junit.Test
    public void testVerificaLoginExistente() {

        // Teste #1 : Login não existente
        LoginModel instance = new LoginModel();

        String login = "jose@uol.com";
        boolean expResult = false;
        boolean result = instance.verificaLoginExistente(login);
        assertEquals(expResult, result);

        // Teste #2 : Login existente
        UsuarioDAO dao = new UsuarioDAO(PostgresConnection.getInstance().getEntityManagerFactory());

        login = "joao@uol.com";
        try {
            Usuario u = new Usuario(1);
            u.setLogin(login);
            u.setNome("Joao");
            u.setSobrenome("da Silva");
            u.setSenha(new Cipher().md5Sum("123"));

            // Cria um usuario para teste
            dao.create(u);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LoginModelTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        expResult = true;
        result = instance.verificaLoginExistente(login);
        assertEquals(expResult, result);

        try {
            dao.destroy(1);
        } catch (IllegalOrphanException | NonexistentEntityException ex) {
            Logger.getLogger(LoginModelTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @org.junit.Test
    public void testGetUsuario() {

        // Teste #1 : Login não existente
        LoginModel instance = new LoginModel();

        String login = "jose@uol.com";
        assertNull(instance.getUsuario(login));

        // Teste #2 : Login existente
        UsuarioDAO dao = new UsuarioDAO(PostgresConnection.getInstance().getEntityManagerFactory());

        try {
            // Cria um usuario para teste
            Usuario u = new Usuario(1);
            u.setLogin("joao@uol.com");
            u.setNome("Joao");
            u.setSobrenome("da Silva");
            u.setSenha(new Cipher().md5Sum("123"));

            dao.create(u);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LoginModelTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        instance = new LoginModel();

        login = "joao@uol.com";

        assertNotNull(instance.verificaLoginExistente(login));

    }

}
