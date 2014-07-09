/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.model;

import com.saax.gestorweb.dao.EmpresaDAOCustom;
import com.saax.gestorweb.dao.UsuarioDAOCustom;
import com.saax.gestorweb.dao.exceptions.IllegalOrphanException;
import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.FilialEmpresa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.Cipher;
import com.saax.gestorweb.util.DBConnect;
import com.saax.gestorweb.util.GestorException;
import com.saax.gestorweb.util.PostgresConnection;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author rodrigo
 */
public class SignupModelTest {
    
    public SignupModelTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        DBConnect.getInstance().assertConnection();
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of verificaLoginExistente method, of class SignupModel.
     */
    @Test
    public void testVerificaLoginExistente() {
        System.out.println("verificaLoginExistente");
        
        SignupModel instance = new SignupModel();

        String login = "jose@uol.com";
        boolean expResult = false;
        boolean result = instance.verificaLoginExistente(login);
        assertEquals(expResult, result);

        // Cria um usuario para teste
        UsuarioDAOCustom dao = new UsuarioDAOCustom(PostgresConnection.getInstance().getEntityManagerFactory());

        try {
            dao.create(new Usuario(1, "Joao", "da Silva","joao@uol.com", new Cipher().md5Sum("123")));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SignupModelTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        login = "joao@uol.com";
        expResult = true;
        result = instance.verificaLoginExistente(login);
        assertEquals(expResult, result);

        try {
            dao.destroy(1);
        } catch (IllegalOrphanException | NonexistentEntityException ex) {
            Logger.getLogger(SignupModelTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    /**
     * Test of verificaEmpresaExistente method, of class SignupModel.
     */
    @Test
    public void testVerificaEmpresaExistente() {
        System.out.println("verificaEmpresaExistente");
        
        SignupModel instance = new SignupModel();
        EmpresaDAOCustom dao = new EmpresaDAOCustom(PostgresConnection.getInstance().getEntityManagerFactory());

        // Teste #1 : CNPJ Não existente
        String cpf_cnpj = "12.345.678/0001-01";
        char tipoPessoa = 'J';
        boolean expResult = false;
        boolean result = false;
        try {
            result = instance.verificaEmpresaExistente(cpf_cnpj, tipoPessoa);
        } catch (GestorException ex) {
            fail("exceção não esperada: "+ex.getMessage());
        }
        assertEquals(expResult, result);
        
        
        // Teste #2 : CPF Não existente
        cpf_cnpj = "123.456.789-01";
        tipoPessoa = 'F';
        expResult = false;
        result = false;
        try {
            result = instance.verificaEmpresaExistente(cpf_cnpj,tipoPessoa);
        } catch (GestorException ex) {
            fail("exceção não esperada: "+ex.getMessage());
        }
        assertEquals(expResult, result);
        
        // Teste #3 : CNPJ Nulo
        cpf_cnpj = null;
        tipoPessoa = 'J';
        expResult = false;
        try {
            result = instance.verificaEmpresaExistente(cpf_cnpj, tipoPessoa);
        } catch (GestorException ex) {
            fail("exceção não esperada: "+ex.getMessage());
        }
        assertEquals(expResult, result);
        
        // Teste #4 : CPF Nulo
        cpf_cnpj = null;
        tipoPessoa = 'F';
        expResult = false;
        try {
            result = instance.verificaEmpresaExistente(cpf_cnpj, tipoPessoa);
        } catch (GestorException ex) {
            fail("exceção não esperada: "+ex.getMessage());
        }
        assertEquals(expResult, result);
        
        // Teste #5 : CNPJ Não formatado -> esperada exceção
        cpf_cnpj = "12-345.6780/00101";
        tipoPessoa = 'J';
        expResult = true;
        try {
            instance.verificaEmpresaExistente(cpf_cnpj, tipoPessoa);
            result = false;
        } catch (GestorException ex) {
            result = true;
        }
        assertEquals(expResult, result);

        // Teste #6 : CPF Não formatado -> esperada exceção
        cpf_cnpj = "123.456-789.01";
        tipoPessoa = 'F';
        expResult = true;
        try {
            instance.verificaEmpresaExistente(cpf_cnpj,tipoPessoa);
            result = false;
        } catch (GestorException ex) {
            result = true;
        }
        assertEquals(expResult, result);

        
        // Teste #7 : CNPJ Existente
        cpf_cnpj = "12.345.678/0001-01";
        tipoPessoa = 'J';
        expResult = true;

        dao.create(new Empresa(1, cpf_cnpj, tipoPessoa));
        try {
            result = instance.verificaEmpresaExistente(cpf_cnpj,tipoPessoa);
        } catch (GestorException ex) {
            fail("exceção não esperada: "+ex.getMessage());
          }
        
        assertEquals(expResult, result);
  
        try {
            dao.destroy(dao.findByCNPJ(cpf_cnpj).getId());
        } catch (IllegalOrphanException | NonexistentEntityException ex) {
            fail("exceção não esperada: "+ex.getMessage());
        }
        
        // Teste #8 : CPF Existente
        cpf_cnpj = "123.456.789-01";
        tipoPessoa = 'F';
        expResult = true;

        dao.create(new Empresa(1, cpf_cnpj, tipoPessoa));
        try {
            result = instance.verificaEmpresaExistente(cpf_cnpj,tipoPessoa);
        } catch (GestorException ex) {
            fail("exceção não esperada: "+ex.getMessage());
          }
        
        assertEquals(expResult, result);
  
        try {
            dao.destroy(dao.findByCPF(cpf_cnpj).getId());
        } catch (IllegalOrphanException | NonexistentEntityException ex) {
            fail("exceção não esperada: "+ex.getMessage());
        }
        
        
    }

    /**
     * Test of criarNovoUsuario method, of class SignupModel.
     */
    @Test
    @Ignore
    public void testCriarNovoUsuario() {
        System.out.println("criarNovoUsuario");
        String nome = "";
        String sobreNome = "";
        String email = "";
        String senha = "";
        SignupModel instance = new SignupModel();
        Usuario expResult = null;
        Usuario result = instance.criarNovoUsuario(nome, sobreNome, email, senha);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of relacionarUsuarioEmpresa method, of class SignupModel.
     */
    @Test
    @Ignore
    public void testRelacionarUsuarioEmpresa() {
        System.out.println("relacionarUsuarioEmpresa");
        Usuario usuario = null;
        Empresa empresa = null;
        boolean administrador = false;
        SignupModel instance = new SignupModel();
        instance.relacionarUsuarioEmpresa(usuario, empresa, administrador);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of criarNovaEmpresa method, of class SignupModel.
     */
    @Test
    @Ignore
    public void testCriarNovaEmpresa() {
        System.out.println("criarNovaEmpresa");
        String nomeFantasia = "";
        String razaosocial = "";
        String cnpj = "";
        String cpf = "";
        SignupModel instance = new SignupModel();
        Empresa expResult = null;
        Empresa result = instance.criarNovaEmpresa(nomeFantasia, razaosocial, cnpj, cpf);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of criarFilialEmpresa method, of class SignupModel.
     */
    @Test
    @Ignore
    public void testCriarFilialEmpresa() {
        System.out.println("criarFilialEmpresa");
        String nome = "";
        SignupModel instance = new SignupModel();
        FilialEmpresa expResult = null;
        FilialEmpresa result = instance.criarFilialEmpresa(nome);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of relacionarEmpresaColigada method, of class SignupModel.
     */
    @Test
    @Ignore
    public void testRelacionarEmpresaColigada() {
        System.out.println("relacionarEmpresaColigada");
        Empresa empresa = null;
        Empresa subempresa = null;
        SignupModel instance = new SignupModel();
        instance.relacionarEmpresaColigada(empresa, subempresa);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of relacionarEmpresaFilial method, of class SignupModel.
     */
    @Test
    @Ignore
    public void testRelacionarEmpresaFilial() {
        System.out.println("relacionarEmpresaFilial");
        Empresa empresa = null;
        FilialEmpresa filialEmpresa = null;
        SignupModel instance = new SignupModel();
        instance.relacionarEmpresaFilial(empresa, filialEmpresa);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of cadatrarNovoUsuario method, of class SignupModel.
     */
    @Test
    @Ignore
    public void testCadatrarNovoUsuario() {
        System.out.println("cadatrarNovoUsuario");
        Usuario usuarioADM = null;
        SignupModel instance = new SignupModel();
        instance.cadatrarNovoUsuario(usuarioADM);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
