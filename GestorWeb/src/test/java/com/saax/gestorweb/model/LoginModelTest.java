package com.saax.gestorweb.model;

import com.saax.gestorweb.dao.UsuarioDAOCustom;
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
        UsuarioDAOCustom dao = new UsuarioDAOCustom(PostgresConnection.getInstance().getEntityManagerFactory());

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

    /**
     * Teste de sucesso ao verificar login não existente
     */
    @org.junit.Test
    public void testVerificaLoginExistente_LoginNaoExistente() {

        System.out.println("VerificaLoginExistente_LoginNaoExistente");

        LoginModel instance = new LoginModel();

        String login = "jose@uol.com";
        boolean expResult = false;
        boolean result = instance.verificaLoginExistente(login);
        assertEquals(expResult, result);
        
    }


    /**
     * Teste de sucesso ao verificar login existente
     */
    @org.junit.Test
    public void testVerificaLoginExistente_LoginExistente() {

        System.out.println("VerificaLoginExistente_LoginExistente");

        UsuarioDAOCustom dao = new UsuarioDAOCustom(PostgresConnection.getInstance().getEntityManagerFactory());

        try {
            // Cria um usuario para teste
            dao.create(new Usuario(1, "Joao", "da Silva","joao@uol.com", new Cipher().md5Sum("123")));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LoginModelTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        LoginModel instance = new LoginModel();

        String login = "joao@uol.com";
        boolean expResult = true;
        boolean result = instance.verificaLoginExistente(login);
        assertEquals(expResult, result);

        try {
            dao.destroy(1);
        } catch (IllegalOrphanException | NonexistentEntityException ex) {
            Logger.getLogger(LoginModelTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    /**
     * Teste de sucesso ao verificar login não existente
     */
    @org.junit.Test
    public void testGetUsuario_LoginNaoExistente() {

        System.out.println("GetUsuario_LoginNaoExistente");
        
        LoginModel instance = new LoginModel();

        String login = "jose@uol.com";
        assertNull(instance.getUsuario(login));
        
    }


    /**
     * Teste de sucesso ao verificar login existente
     */
    @org.junit.Test
    public void testGetUsuario_LoginExistente() {

        System.out.println("GetUsuario_LoginExistente");
        
        UsuarioDAOCustom dao = new UsuarioDAOCustom(PostgresConnection.getInstance().getEntityManagerFactory());

        try {
            // Cria um usuario para teste
            dao.create(new Usuario(1, "Joao", "da Silva","joao@uol.com", new Cipher().md5Sum("123")));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LoginModelTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        LoginModel instance = new LoginModel();

        String login = "joao@uol.com";
        assertNotNull(instance.verificaLoginExistente(login));
        
    }


}
