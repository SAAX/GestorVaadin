package com.saax.gestorweb.model;

import com.saax.gestorweb.dao.EmpresaDAOCustom;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.FilialEmpresa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.model.datamodel.UsuarioEmpresa;
import com.saax.gestorweb.util.Cipher;
import com.saax.gestorweb.util.FormatterUtil;
import com.saax.gestorweb.util.GestorException;
import com.saax.gestorweb.util.PostgresConnection;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author rodrigo
 */
public class SignupModel {

    private final EmpresaDAOCustom empresaDAO;

    /**
     * Cria o model e conecta ao DAO
     */
    public SignupModel() {
        empresaDAO = new EmpresaDAOCustom(PostgresConnection.getInstance().getEntityManagerFactory());
    }

    /**
     * Repassa ao model de login a verificação do email do usuario
     *
     * @param login
     * @return true se existe este email cadastrado como login
     */
    public boolean verificaLoginExistente(String login) {
        return new LoginModel().verificaLoginExistente(login);
    }

    /**
     * Verifica se uma empresa (conta) esta cadastrada no sistema pelo seu CNPJ / CPF
     *
     * @param cpf_cnpj CPF ou CNPJ da empresa
     * @param tipoPessoa Tipo da pessoa: Fisica / Juridica
     * @return true se a empresa está cadastrada
     * @throws com.saax.gestorweb.util.GestorException
     */
    public boolean verificaEmpresaExistente(String cpf_cnpj, char tipoPessoa) throws GestorException {
        if (StringUtils.isBlank(cpf_cnpj)){
            return false;
        }
        
        if (tipoPessoa == 'J') {
            if (!FormatterUtil.validarCNPJ(cpf_cnpj)){
                throw new GestorException("CNPJ fora do formato correto (##.###.###/####-##): "+cpf_cnpj);
            }
            Empresa e = empresaDAO.findByCNPJ(cpf_cnpj);
            return (e != null);

        } else if (tipoPessoa == 'F') {
            if (!FormatterUtil.validarCPF(cpf_cnpj)){
                throw new GestorException("CPF fora do formato correto (###.###.###-##): "+cpf_cnpj);
            }
            Empresa e = empresaDAO.findByCPF(cpf_cnpj);
            return (e != null);
        } else {
            
            throw new GestorException("Tipo Pessoa fora do formato correto (F/J)");
            
        }
        
    }

    /**
     * Cria e retorna um usuario com os parametros informados
     *
     * @param nome
     * @param sobreNome
     * @param email
     * @param senha
     * @return novo usuario criado
     */
    public Usuario criarNovoUsuario(String nome, String sobreNome, String email, String senha) {

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setSobrenome(sobreNome);
        usuario.setLogin(email);

        // criptografa a senha informada
        String senhaCriptografada;
        try {
            senhaCriptografada = new Cipher().md5Sum(senha);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SignupModel.class.getName()).log(Level.SEVERE, "Não encontrado algorítimo MD5", ex);
            return null;
        }

        usuario.setSenha(senhaCriptografada);

        return usuario;
    }

    /**
     * Cria o relacionamento entre o usuario e a empresa
     *
     * @param usuario
     * @param empresa
     * @param administrador
     */
    public void relacionarUsuarioEmpresa(Usuario usuario, Empresa empresa, boolean administrador) {

        UsuarioEmpresa usuarioEmpresa = new UsuarioEmpresa();
        usuarioEmpresa.setUsuario(usuario);
        usuarioEmpresa.setEmpresa(empresa);
        usuarioEmpresa.setAtivo(true);
        usuarioEmpresa.setContratacao(new Date());
        
        usuarioEmpresa.setAdministrador(administrador);

        if (usuario.getEmpresas() == null) {
            usuario.setEmpresas(new ArrayList<>());
        }

        usuario.getEmpresas().add(usuarioEmpresa);

        if (empresa.getUsuarios() == null) {
            empresa.setUsuarios(new ArrayList<>());
        }

        empresa.getUsuarios().add(usuarioEmpresa);

    }

    /**
     * Cria e retorna uma empresa com os parametros informados
     *
     * @param nomeFantasia
     * @param razaosocial
     * @param cnpj
     * @param cpf
     * 
     * @return nova empresa criada
     */
    public Empresa criarNovaEmpresa(String nomeFantasia, String razaosocial, String cnpj, String cpf) {
        Empresa empresa = new Empresa();

        empresa.setNome(nomeFantasia);
        empresa.setRazaoSocial(razaosocial);
        empresa.setCnpj(cnpj);
        empresa.setCpf(cpf);
        
        if (StringUtils.isNotBlank(cnpj)){
            empresa.setTipoPessoa('J');
        } else {
            empresa.setTipoPessoa('F');
        }
        empresa.setAtiva(true);
        
        
        return empresa;

    }

    
    /**
     * Cria e retorna uma filial de empresa com os parametros informados
     *
     * @param nome
     * 
     * @return nova empresa criada
     */
    public FilialEmpresa criarFilialEmpresa(String nome) {

        FilialEmpresa filialEmpresa = new FilialEmpresa();
        filialEmpresa.setNome(nome);
        filialEmpresa.setAtiva(true);
        
        return filialEmpresa;
    }

    /**
     * Cria o relacionamento entre uma empresa principal e sua coligada (subempresa)
     *
     * @param empresa empresa princiapaç
     * @param subempresa empresa coligada
     */
    public void relacionarEmpresaColigada(Empresa empresa, Empresa subempresa) {
        
        subempresa.setEmpresaPrincipal(empresa);
        
        if (empresa.getSubEmpresas()==null){
            empresa.setSubEmpresas(new ArrayList<>());
        }
        
        empresa.getSubEmpresas().add(empresa);
        
    }

    /**
     * Cria o relacionamento entre uma empresa principal e sua filial
     *
     * @param empresa empresa princiapaç
     * @param filialEmpresa filial
     */
    public void relacionarEmpresaFilial(Empresa empresa, FilialEmpresa filialEmpresa) {
        
        if (empresa.getFiliais()==null){
            empresa.setFiliais(new ArrayList<>());
        }
        
        empresa.getFiliais().add(filialEmpresa);
        
    }

    public void cadatrarNovoUsuario(Usuario usuarioADM) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
