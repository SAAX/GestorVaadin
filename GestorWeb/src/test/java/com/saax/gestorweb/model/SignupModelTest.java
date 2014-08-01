/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.model;

import com.saax.gestorweb.dao.CidadeDAO;
import com.saax.gestorweb.dao.EmpresaDAO;
import com.saax.gestorweb.dao.EnderecoDAO;
import com.saax.gestorweb.dao.EstadoDAO;
import com.saax.gestorweb.dao.FilialEmpresaDAO;
import com.saax.gestorweb.dao.UsuarioDAO;
import com.saax.gestorweb.dao.UsuarioEmpresaDAO;
import com.saax.gestorweb.dao.exceptions.IllegalOrphanException;
import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import com.saax.gestorweb.model.datamodel.Cidade;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.Endereco;
import com.saax.gestorweb.model.datamodel.Estado;
import com.saax.gestorweb.model.datamodel.FilialEmpresa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.model.datamodel.UsuarioEmpresa;
import com.saax.gestorweb.util.Cipher;
import com.saax.gestorweb.util.DBConnect;
import com.saax.gestorweb.util.GestorException;
import com.saax.gestorweb.util.PostgresConnection;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
        UsuarioDAO dao = new UsuarioDAO(PostgresConnection.getInstance().getEntityManagerFactory());

        try {
            Usuario u = new Usuario(1);
            u.setLogin(login);
            u.setNome("Joao");
            u.setSobrenome("da Silva");
            u.setSenha(new Cipher().md5Sum("123"));
            dao.create(u);
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
        EmpresaDAO dao = new EmpresaDAO(PostgresConnection.getInstance().getEntityManagerFactory());

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

        Empresa e = new Empresa();
        e.setId(1);
        e.setCnpj(cpf_cnpj);
        e.setTipoPessoa(tipoPessoa);
        
        dao.create(e);
        
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

        e = new Empresa();
        e.setId(1);
        e.setCpf(cpf_cnpj);
        e.setTipoPessoa(tipoPessoa);
        dao.create(e);
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
     * Test of relacionarUsuarioEmpresa method, of class SignupModel.
     */
    @Test
    public void testRelacionarUsuarioEmpresa() {

        System.out.println("relacionarUsuarioEmpresa");
        
        String nome = "rodrigo";
        String sobreNome = "moreira";
        String email = "rodrigo@provedor.com.br";
        SignupModel instance = new SignupModel();
        

        Usuario usuario = new Usuario(1);
            usuario.setLogin(email);
            usuario.setNome(nome);
            usuario.setSobrenome(sobreNome);
            usuario.setDataHoraInclusao(LocalDateTime.now());
        
        String nomeFantasia = "saax";
        String razaosocial = "saax int inc";
        String cnpjCpf = "12.345.678/0001-01";
        char tipoPessoa = 'J';

        Empresa empresa = new Empresa();

        empresa.setNome(nomeFantasia);
        empresa.setRazaoSocial(razaosocial);

        if (tipoPessoa == 'F') {
            empresa.setCpf(cnpjCpf);
        } else {
            empresa.setCnpj(cnpjCpf);
        }
        empresa.setTipoPessoa(tipoPessoa);
        empresa.setAtiva(true);

        boolean administrador = true;
        
        instance.relacionarUsuarioEmpresa(usuario, empresa, administrador);
        
        boolean relacionamentoOK = false;
        for (UsuarioEmpresa usuarioEmpresa : usuario.getEmpresas()) {
            if (usuarioEmpresa.getEmpresa().getCnpj().equals(cnpjCpf)){
                relacionamentoOK = true;
            }
        }
        
        assertTrue(relacionamentoOK);
        
        relacionamentoOK = false;
        for (UsuarioEmpresa usuarioEmpresa : empresa.getUsuarios()) {
            
            if (usuarioEmpresa.getUsuario().getLogin().equals(email)){
                relacionamentoOK = true;
            }
        }
        
        assertTrue(relacionamentoOK);
        
        
        
    }

    /**
     * Test of criarNovaEmpresa method, of class SignupModel.
     */
    @Test
    public void testCriarNovaEmpresa() {
        System.out.println("criarNovaEmpresa");
        
        String nomeFantasia = "saax";
        String razaosocial = "saax int inc";
        String cnpjCpf = "12.345.678/0001-01";
        char tipoPessoa = 'J';
        
        SignupModel instance = new SignupModel();
        
        Empresa expResult = new Empresa();

        expResult.setNome(nomeFantasia);
        expResult.setRazaoSocial(razaosocial);

        if (tipoPessoa == 'F') {
            expResult.setCpf(cnpjCpf);
        } else {
            expResult.setCnpj(cnpjCpf);
        }
        expResult.setTipoPessoa(tipoPessoa);
        expResult.setAtiva(true);

        Empresa result = instance.criarNovaEmpresa(nomeFantasia, razaosocial, cnpjCpf, tipoPessoa);
        
        assertEquals(expResult.getNome(), result.getNome());
        assertEquals(expResult.getRazaoSocial(), result.getRazaoSocial());
        assertEquals(expResult.getCnpj(), result.getCnpj());
        assertEquals(expResult.getCpf(), result.getCpf());
        assertTrue(result.getAtiva());
        
    }

    /**
     * Test of criarFilialEmpresa method, of class SignupModel.
     */
    @Test
    public void testCriarFilialEmpresa() {
        System.out.println("criarFilialEmpresa");
        
        String nome = "filial1";
        String cnpj = null;
        
        SignupModel instance = new SignupModel();
        FilialEmpresa expResult = new FilialEmpresa();
        
        expResult.setNome(nome);
        expResult.setCnpj(cnpj);
        
        FilialEmpresa result = instance.criarFilialEmpresa(nome, cnpj);
        assertEquals(expResult.getNome(), result.getNome());
        assertTrue(result.isAtiva());
        
        nome = "filial2";
        cnpj = "12.345.678/0001-01";
        
        expResult = new FilialEmpresa();
        
        expResult.setNome(nome);
        expResult.setCnpj(cnpj);
        
        result = instance.criarFilialEmpresa(nome, cnpj);
        assertEquals(expResult.getNome(), result.getNome());
        assertEquals(expResult.getCnpj(), result.getCnpj());
        assertTrue(result.isAtiva());
        
    }

    /**
     * Test of relacionarEmpresaColigada method, of class SignupModel.
     */
    @Test
    public void testRelacionarEmpresaColigada() {
        System.out.println("relacionarEmpresaColigada");

        String nomeFantasia = "saax";
        String razaosocial = "saax int inc";
        String cnpjCpf = "12.345.678/0001-01";
        char tipoPessoa = 'J';
        
        
        Empresa empresa = new Empresa();

        empresa.setNome(nomeFantasia);
        empresa.setRazaoSocial(razaosocial);

        if (tipoPessoa == 'F') {
            empresa.setCpf(cnpjCpf);
        } else {
            empresa.setCnpj(cnpjCpf);
        }
        empresa.setTipoPessoa(tipoPessoa);
        empresa.setAtiva(true);

        SignupModel instance = new SignupModel();

        Empresa subempresa = new Empresa();

        nomeFantasia = "coligada a saax";
        razaosocial = "coligada a saax int inc";
        cnpjCpf = "12.345.678/0002-01";
        tipoPessoa = 'J';
        
        subempresa.setNome(nomeFantasia);
        subempresa.setRazaoSocial(razaosocial);

        if (tipoPessoa == 'F') {
            subempresa.setCpf(cnpjCpf);
        } else {
            subempresa.setCnpj(cnpjCpf);
        }
        subempresa.setTipoPessoa(tipoPessoa);
        subempresa.setAtiva(true);
        
        instance.relacionarEmpresaColigada(empresa, subempresa);

        assertEquals(subempresa.getEmpresaPrincipal().getCnpj(), empresa.getCnpj());
        assertEquals(empresa.getSubEmpresas().get(0).getCnpj(), subempresa.getCnpj());
        
    
    
    }

    /**
     * Test of relacionarEmpresaFilial method, of class SignupModel.
     */
    @Test
    public void testRelacionarEmpresaFilial() {
        System.out.println("relacionarEmpresaFilial");

                String nomeFantasia = "saax";
        String razaosocial = "saax int inc";
        String cnpjCpf = "12.345.678/0001-01";
        char tipoPessoa = 'J';
        
        
        Empresa empresa = new Empresa();

        empresa.setNome(nomeFantasia);
        empresa.setRazaoSocial(razaosocial);

        if (tipoPessoa == 'F') {
            empresa.setCpf(cnpjCpf);
        } else {
            empresa.setCnpj(cnpjCpf);
        }
        empresa.setTipoPessoa(tipoPessoa);
        empresa.setAtiva(true);

        String nome = "filial1";
        String cnpj = "12.345.678/0002-02";
        
        FilialEmpresa filialEmpresa = new FilialEmpresa();
        
        filialEmpresa.setNome(nome);
        filialEmpresa.setCnpj(cnpj);

        SignupModel instance = new SignupModel();
        instance.relacionarEmpresaFilial(empresa, filialEmpresa);
        
        assertEquals(empresa.getFiliais().get(0).getNome(), filialEmpresa.getNome());
        assertEquals(filialEmpresa.getMatriz().getCnpj(), empresa.getCnpj());
        
    }

    /**
     * Test of criarNovaConta method, of class SignupModel.
     */
    @Test
    public void testCriarNovaConta() {
    
        System.out.println("cadatrarNovoUsuario");
        
        SignupModel instance = new SignupModel();
        UsuarioDAO usuarioDAO = new UsuarioDAO(PostgresConnection.getInstance().getEntityManagerFactory());
        EmpresaDAO empresaDAO = new EmpresaDAO(PostgresConnection.getInstance().getEntityManagerFactory());
        FilialEmpresaDAO filialEmpresaDAO = new FilialEmpresaDAO(PostgresConnection.getInstance().getEntityManagerFactory());
        UsuarioEmpresaDAO usuarioEmpresaDAO = new UsuarioEmpresaDAO(PostgresConnection.getInstance().getEntityManagerFactory());
        
        // ---------------------------------------------------------------------
        // Teste #1 : usuario simples com a empresa
        // ---------------------------------------------------------------------
        
        // criar usuario princiapal de teste
        Usuario usuarioADM = instance.criarNovoUsuario("rodrigo", "soares", "rodrigo@gmail.com", "123");        
        
        // criar empresa principal
        Empresa empresaPrincipal = instance.criarNovaEmpresa("saax", "saax inc", "12.345.678/0001-01", 'J');
        instance.relacionarUsuarioEmpresa(usuarioADM, empresaPrincipal, true);
        
        // teste gravar #1
        empresaPrincipal = instance.criarNovaConta(empresaPrincipal);
        
        // Assert
        Object expResult = usuarioADM;
        Object result = usuarioDAO.findByLogin("rodrigo@gmail.com");
        assertEquals(expResult, result);
        
        // remover
        try {
            
            UsuarioEmpresa usuarioEmpresa = ((Usuario)result).getEmpresas().iterator().next();
            
            usuarioEmpresaDAO.destroy(usuarioEmpresa.getId());
            
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(SignupModelTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("exceção não esperada: "+ex.getMessage());
        }
        
        // ---------------------------------------------------------------------
        // Teste #2 : usuario simples com a empresa + filial
        // ---------------------------------------------------------------------
        
        // criar usuario princiapal de teste
        usuarioADM = instance.criarNovoUsuario("rodrigo", "soares", "rodrigo@gmail.com", "123");        
        
        // criar empresa principal
        empresaPrincipal = instance.criarNovaEmpresa("saax", "saax inc", "12.345.678/0001-01", 'J');
        instance.relacionarUsuarioEmpresa(usuarioADM, empresaPrincipal, true);
        
        // filiais
        FilialEmpresa filialEmpresa = instance.criarFilialEmpresa("filial 1", "12.345.678/0001-02");
        instance.relacionarEmpresaFilial(empresaPrincipal, filialEmpresa);

        // teste gravar #2
        empresaPrincipal = instance.criarNovaConta(empresaPrincipal);
        
        // Assert
        expResult = empresaPrincipal;
        result = filialEmpresaDAO.findByCNPJ("12.345.678/0001-02").getMatriz();
        assertEquals(expResult, result);
        
        // Assert
        expResult = filialEmpresa.getNome();
        result = filialEmpresaDAO.findByCNPJ("12.345.678/0001-02").getNome();
        assertEquals(expResult, result);
               
       
        // remover
        try {
            
            UsuarioEmpresa usuarioEmpresa = empresaPrincipal.getUsuarios().iterator().next();
            usuarioEmpresaDAO.destroy(usuarioEmpresa.getId());
           
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(SignupModelTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("exceção não esperada: "+ex.getMessage());
        }
        
        // ---------------------------------------------------------------------
        // Teste #3 : usuario simples com a empresa + filial + coligadas
        // ---------------------------------------------------------------------
        
        // criar usuario princiapal de teste
        usuarioADM = instance.criarNovoUsuario("rodrigo", "soares", "rodrigo@gmail.com", "123");        
        
        // criar empresa principal
        empresaPrincipal = instance.criarNovaEmpresa("saax", "saax inc", "12.345.678/0001-01", 'J');
        instance.relacionarUsuarioEmpresa(usuarioADM, empresaPrincipal, true);
        
        // filiais
        filialEmpresa = instance.criarFilialEmpresa("filial 1", "12.345.678/0001-02");
        instance.relacionarEmpresaFilial(empresaPrincipal, filialEmpresa);

        // coligadas
        Empresa subempresa = instance.criarNovaEmpresa("coligada 1",
                    "coligada 1 ltda", "12.345.678/0001-03", 'J');
        instance.relacionarEmpresaColigada(empresaPrincipal, subempresa);

        // teste gravar #3
        empresaPrincipal = instance.criarNovaConta(empresaPrincipal);
        
        // Assert
        expResult = empresaPrincipal;
        result = empresaDAO.findByCNPJ("12.345.678/0001-03").getEmpresaPrincipal();
        assertEquals(expResult, result);
        
        // Assert
        expResult = subempresa.getCnpj();
        result = empresaDAO.findByCNPJ("12.345.678/0001-01").getSubEmpresas().iterator().next().getCnpj();
        assertEquals(expResult, result);
               
       
        // remover
       try {
            
            UsuarioEmpresa usuarioEmpresa = empresaPrincipal.getUsuarios().iterator().next();
            usuarioEmpresaDAO.destroy(usuarioEmpresa.getId());
           
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(SignupModelTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("exceção não esperada: "+ex.getMessage());
        }
        
        // ---------------------------------------------------------------------
        // Teste #4 : usuario com a empresa + filial + coligadas + demais usuarios
        // ---------------------------------------------------------------------
        
        expResult = new Usuario[3];
        
        // criar usuario princiapal de teste
        usuarioADM = instance.criarNovoUsuario("rodrigo", "soares", "rodrigo@gmail.com", "123");        
        ((Usuario[])expResult)[0] = usuarioADM;
        
        // criar empresa principal
        empresaPrincipal = instance.criarNovaEmpresa("saax", "saax inc", "12.345.678/0001-01", 'J');
        instance.relacionarUsuarioEmpresa(usuarioADM, empresaPrincipal, true);
        
        // filiais
        filialEmpresa = instance.criarFilialEmpresa("filial 1", "12.345.678/0001-02");
        instance.relacionarEmpresaFilial(empresaPrincipal, filialEmpresa);

        // coligadas
        subempresa = instance.criarNovaEmpresa("coligada 1",
                    "coligada 1 ltda", "12.345.678/0001-03", 'J');
        instance.relacionarEmpresaColigada(empresaPrincipal, subempresa);

        // demais usuarios
        Usuario usuario1 = instance.criarNovoUsuario("nome usuario 1", "sobrenome u 1", "email@usuario1.com");
        instance.relacionarUsuarioEmpresa(usuario1, empresaPrincipal, false);        
        ((Usuario[])expResult)[1] = usuario1;
        
        Usuario usuario2 = instance.criarNovoUsuario("nome usuario 2(adm)", "sobrenome u 2", "email@usuario2.com");
        instance.relacionarUsuarioEmpresa(usuario2, empresaPrincipal, true);        
        ((Usuario[])expResult)[2] = usuario2;
        
        // teste gravar #3
        empresaPrincipal = instance.criarNovaConta(empresaPrincipal);
        
        // Assert
        result = new Usuario[3];
        ((Usuario[])result)[0] = empresaPrincipal.getUsuarios().get(0).getUsuario();
        ((Usuario[])result)[1] = empresaPrincipal.getUsuarios().get(1).getUsuario();
        ((Usuario[])result)[2] = empresaPrincipal.getUsuarios().get(2).getUsuario();
        
        assertArrayEquals(((Usuario[])expResult), ((Usuario[])result));
        
       
        // remover
       try {
            
            UsuarioEmpresa usuarioEmpresa = empresaPrincipal.getUsuarios().iterator().next();
            usuarioEmpresaDAO.destroy(usuarioEmpresa.getId());
           
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(SignupModelTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("exceção não esperada: "+ex.getMessage());
        }
        
        
        // ---------------------------------------------------------------------
        // Teste #5 : cadastro completo
        // ---------------------------------------------------------------------
        
        expResult = new Usuario[3];
        
        // criar usuario princiapal de teste
        usuarioADM = instance.criarNovoUsuario("rodrigo", "soares", "rodrigo@gmail.com", "123");        
        ((Usuario[])expResult)[0] = usuarioADM;
        
        // criar empresa principal
        empresaPrincipal = instance.criarNovaEmpresa("saax", "saax inc", "12.345.678/0001-01", 'J');
        instance.relacionarUsuarioEmpresa(usuarioADM, empresaPrincipal, true);
        
        // filiais
        filialEmpresa = instance.criarFilialEmpresa("filial 1", "12.345.678/0001-02");
        instance.relacionarEmpresaFilial(empresaPrincipal, filialEmpresa);

        // coligadas
        subempresa = instance.criarNovaEmpresa("coligada 1",
                    "coligada 1 ltda", "12.345.678/0001-03", 'J');
        instance.relacionarEmpresaColigada(empresaPrincipal, subempresa);

        // demais usuarios
        usuario1 = instance.criarNovoUsuario("nome usuario 1", "sobrenome u 1", "email@usuario1.com");
        instance.relacionarUsuarioEmpresa(usuario1, empresaPrincipal, false);        
        ((Usuario[])expResult)[1] = usuario1;
        
        usuario2 = instance.criarNovoUsuario("nome usuario 2(adm)", "sobrenome u 2", "email@usuario2.com");
        instance.relacionarUsuarioEmpresa(usuario2, empresaPrincipal, true);        
        ((Usuario[])expResult)[2] = usuario2;
        
        // criar o endereco
        String logradouro = "av. antonio afonso";
        String numero = "123";
        String complemento = "";
        String cep = "12.480-000";
        
        Estado sp = new Estado(null, "sao paulo", "sp");
        new EstadoDAO(PostgresConnection.getInstance().getEntityManagerFactory()).create(sp);
        
        Cidade cidade = new Cidade(null, "limeira");
        cidade.setEstado(sp);
        new CidadeDAO(PostgresConnection.getInstance().getEntityManagerFactory()).create(cidade);
        
        Endereco endereco = instance.criarEndereco(logradouro, numero, complemento, cep, cidade);
        empresaPrincipal.setEndereco(endereco);
        
        
        // teste gravar #5
        empresaPrincipal = instance.criarNovaConta(empresaPrincipal);
        
        // Assert
        result = empresaPrincipal.getEndereco().getLogradouro();
        expResult = endereco.getLogradouro();
        
        assertEquals(result, expResult);
        
       
        // remover
       try {
            
            UsuarioEmpresa usuarioEmpresa = empresaPrincipal.getUsuarios().iterator().next();
            usuarioEmpresaDAO.destroy(usuarioEmpresa.getId());
           
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(SignupModelTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("exceção não esperada: "+ex.getMessage());
        }
        
        
      
    }

    /**
     * Test of verificaFilialExistente method, of class SignupModel.
     */
    @Test
    public void testVerificaFilialExistente() throws Exception {
        
        System.out.println("verificaFilialExistente");
        
        String cnpj = "12.345.678/0001-01";
        SignupModel instance = new SignupModel();

        FilialEmpresaDAO dao = new FilialEmpresaDAO(PostgresConnection.getInstance().getEntityManagerFactory());

        // Teste #1 : CNPJ Não existente
        boolean expResult = false;
        boolean result = false;
        try {
            result = instance.verificaFilialExistente(cnpj);
        } catch (GestorException ex) {
            fail("exceção não esperada: "+ex.getMessage());
        }
        assertEquals(expResult, result);
        
        
        // Teste #3 : CNPJ Nulo
        cnpj = null;
        expResult = false;
        try {
            result = instance.verificaFilialExistente(cnpj);
        } catch (GestorException ex) {
            fail("exceção não esperada: "+ex.getMessage());
        }
        assertEquals(expResult, result);
        
        // Teste #5 : CNPJ Não formatado -> esperada exceção
        cnpj = "12-345.6780/00101";
        expResult = true;
        try {
            instance.verificaFilialExistente(cnpj);
            result = false;
        } catch (GestorException ex) {
            result = true;
        }
        assertEquals(expResult, result);

        // Teste #7 : CNPJ Existente
        cnpj = "12.345.678/0001-01";
        expResult = true;

        dao.create(instance.criarFilialEmpresa("filial", cnpj));
        try {
            result = instance.verificaFilialExistente(cnpj);
        } catch (GestorException ex) {
            fail("exceção não esperada: "+ex.getMessage());
        }
        
        assertEquals(expResult, result);
  
        try {
            dao.destroy(dao.findByCNPJ(cnpj).getId());
        } catch (NonexistentEntityException ex) {
            fail("exceção não esperada: "+ex.getMessage());
        }
        
    }

    /**
     * Test of criarEndereco method, of class SignupModel.
     */
    @Test
    public void testCriarEndereco() {
        System.out.println("criarEndereco");
        
        String logradouro = "av. antonio afonso";
        String numero = "123";
        String complemento = "";
        String cep = "12.480-000";
        
        Estado sp = new Estado(1, "sao paulo", "sp");
        Cidade cidade = new Cidade(1, "limeira");
        cidade.setEstado(sp);
        
        SignupModel instance = new SignupModel();
        Endereco expResult = new Endereco(1, logradouro, numero, complemento, cep);
        Endereco result = instance.criarEndereco(logradouro, numero, complemento, cep, cidade);
        
        assertEquals(expResult.getLogradouro(), result.getLogradouro());
        assertEquals(expResult.getNumero(), result.getNumero());
        
    }

    /**
     * Test of relacionarEmpresaEndereco method, of class SignupModel.
     */
    @Test
    public void testRelacionarEmpresaEndereco() {
        System.out.println("relacionarEmpresaEndereco");

        SignupModel instance = new SignupModel();
        

        // criar o endereco
        String logradouro = "av. antonio afonso";
        String numero = "123";
        String complemento = "";
        String cep = "12.480-000";
        
        Estado sp = new Estado(1, "sao paulo", "sp");
        Cidade cidade = new Cidade(1, "limeira");
        cidade.setEstado(sp);
        
        Endereco endereco = instance.criarEndereco(logradouro, numero, complemento, cep, cidade);

        Empresa empresa = instance.criarNovaEmpresa("saax", "saax inc", "12.345.678/0001-01", 'J');

        instance.relacionarEmpresaEndereco(empresa, endereco);

        
        assertEquals(empresa.getEndereco().getLogradouro(), endereco.getLogradouro());
        assertEquals(endereco.getEmpresas().get(0).getCnpj(), empresa.getCnpj());
        
        
    }

    /**
     * Test of criarNovoUsuario method, of class SignupModel.
     */
    @Test
    public void testCriarNovoUsuario_4args() {
        System.out.println("criarNovoUsuario");

        String nome = "rodrigo";
        String sobreNome = "moreira";
        String email = "rodrigo@provedor.com.br";
        String senha = "123";
        SignupModel instance = new SignupModel();

        
        Usuario expResult = new Usuario(1);
            expResult.setLogin(email);
            expResult.setNome(nome);
            expResult.setSobrenome(sobreNome);
        try {
            expResult.setSenha(new Cipher().md5Sum(senha));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SignupModelTest.class.getName()).log(Level.SEVERE, null, ex);
        }
            expResult.setDataHoraInclusao(LocalDateTime.now());

        Usuario result = instance.criarNovoUsuario(nome, sobreNome, email, senha);
        
        assertEquals(expResult.getLogin(), result.getLogin());

    }

    /**
     * Test of criarNovoUsuario method, of class SignupModel.
     */
    @Test
    public void testCriarNovoUsuario_3args() {
        System.out.println("criarNovoUsuario");

        String nome = "rodrigo";
        String sobreNome = "moreira";
        String email = "rodrigo@provedor.com.br";
        SignupModel instance = new SignupModel();
        
        Usuario expResult = new Usuario(1);
            expResult.setLogin(email);
            expResult.setNome(nome);
            expResult.setSobrenome(sobreNome);
            expResult.setDataHoraInclusao(LocalDateTime.now());
        Usuario result = instance.criarNovoUsuario(nome, sobreNome, email);
        
        assertEquals(expResult.getLogin(), result.getLogin());
        
        assertNull(expResult.getSenha());
        
    }
    
}
