package com.saax.gestorweb.model;

import com.saax.gestorweb.dao.EmpresaDAOCustom;
import com.saax.gestorweb.dao.FilialEmpresaDAOCustom;
import com.saax.gestorweb.model.datamodel.Cidade;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.Endereco;
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
    private final FilialEmpresaDAOCustom filialEmpresaDAO;

    /**
     * Cria o model e conecta ao DAO
     */
    public SignupModel() {
        empresaDAO = new EmpresaDAOCustom(PostgresConnection.getInstance().getEntityManagerFactory());
        filialEmpresaDAO = new FilialEmpresaDAOCustom(PostgresConnection.getInstance().getEntityManagerFactory());
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
     * Verifica se uma empresa (conta) esta cadastrada no sistema pelo seu CNPJ
     * / CPF
     *
     * @param cpf_cnpj CPF ou CNPJ da empresa
     * @param tipoPessoa Tipo da pessoa: Fisica / Juridica
     * @return true se a empresa está cadastrada
     * @throws com.saax.gestorweb.util.GestorException
     */
    public boolean verificaEmpresaExistente(String cpf_cnpj, char tipoPessoa) throws GestorException {
        if (StringUtils.isBlank(cpf_cnpj)) {
            return false;
        }

        if (tipoPessoa == 'J') {
            if (!FormatterUtil.validarCNPJ(cpf_cnpj)) {
                throw new GestorException("CNPJ fora do formato correto (##.###.###/####-##): " + cpf_cnpj);
            }
            Empresa e = empresaDAO.findByCNPJ(cpf_cnpj);
            return (e != null);

        } else if (tipoPessoa == 'F') {
            if (!FormatterUtil.validarCPF(cpf_cnpj)) {
                throw new GestorException("CPF fora do formato correto (###.###.###-##): " + cpf_cnpj);
            }
            Empresa e = empresaDAO.findByCPF(cpf_cnpj);
            return (e != null);
        } else {

            throw new GestorException("Tipo Pessoa fora do formato correto (F/J)");

        }

    }

    /**
     * Verifica se uma filial de empresa está cadastrada no sistema pelo seu
     * CNPJ
     *
     * @param cnpj CNPJ da filial
     * @return true se a filial está cadastrada
     * @throws com.saax.gestorweb.util.GestorException
     */
    public boolean verificaFilialExistente(String cnpj) throws GestorException {
        if (StringUtils.isBlank(cnpj)) {
            return false;
        }

        FilialEmpresa e = filialEmpresaDAO.findByCNPJ(cnpj);
        return (e != null);

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
     * Cria e retorna um usuario com os parametros informados e com senha
     * default
     *
     * @param nome
     * @param sobreNome
     * @param email
     * @return novo usuario criado
     */
    public Usuario criarNovoUsuario(String nome, String sobreNome, String email) {

        String senha = null;

        return criarNovoUsuario(nome, sobreNome, email, senha);
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
     * @param cnpjCpf 
     * @param tipoPessoa 
     *
     * @return nova empresa criada
     */
    public Empresa criarNovaEmpresa(String nomeFantasia, String razaosocial, String cnpjCpf, char tipoPessoa) {
        Empresa empresa = new Empresa();

        empresa.setNome(nomeFantasia);
        empresa.setRazaoSocial(razaosocial);
        
        if (tipoPessoa == 'F'){
            empresa.setCpf(cnpjCpf);
        } else {
            empresa.setCnpj(cnpjCpf);
        }
        empresa.setTipoPessoa(tipoPessoa);
        empresa.setAtiva(true);

        return empresa;

    }

    /**
     * Cria e retorna uma filial de empresa com os parametros informados
     *
     * @param nome
     * @param cnpj
     *
     * @return nova empresa criada
     */
    public FilialEmpresa criarFilialEmpresa(String nome, String cnpj) {

        FilialEmpresa filialEmpresa = new FilialEmpresa();
        filialEmpresa.setNome(nome);
        filialEmpresa.setCnpj(cnpj);
        filialEmpresa.setAtiva(true);

        return filialEmpresa;
    }

    /**
     * Cria o relacionamento entre uma empresa principal e sua coligada
     * (subempresa)
     *
     * @param empresa empresa princiapaç
     * @param subempresa empresa coligada
     */
    public void relacionarEmpresaColigada(Empresa empresa, Empresa subempresa) {

        subempresa.setEmpresaPrincipal(empresa);

        if (empresa.getSubEmpresas() == null) {
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

        if (empresa.getFiliais() == null) {
            empresa.setFiliais(new ArrayList<>());
        }

        empresa.getFiliais().add(filialEmpresa);

    }

    public void cadatrarNovoUsuario(Usuario usuarioADM) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Cria um endereco com os parametros informados
     * @param logradouro
     * @param numero
     * @param complemento
     * @param cep
     * @param cidade
     * @return endereco criado
     */
    public Endereco criarEndereco(String logradouro, String numero, String complemento, 
            String cep, Cidade cidade ) {
        Endereco endereco = new Endereco();
        endereco.setLogradouro(logradouro);
        endereco.setComplemento(complemento);
        endereco.setNumero(numero);
        endereco.setCep(cep);
        endereco.setCidade(cidade);
        
        return endereco;
    }

    /**
     * Cria o relacionamento entre uma empresa e seu endereco
     *
     * @param empresa empresa princiapal
     * @param endereco 
     */
    public void relacionarEmpresaEndereco(Empresa empresa, Endereco endereco) {

        if (endereco.getEmpresaList() == null) {
            endereco.setEmpresaList(new ArrayList<>());
        }

        empresa.setEndereco(endereco);
        endereco.getEmpresaList().add(empresa);

    }

}
