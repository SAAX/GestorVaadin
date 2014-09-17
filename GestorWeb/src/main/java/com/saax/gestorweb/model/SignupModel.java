package com.saax.gestorweb.model;

import com.saax.gestorweb.dao.CidadeDAO;
import com.saax.gestorweb.dao.EmpresaDAO;
import com.saax.gestorweb.dao.EnderecoDAO;
import com.saax.gestorweb.dao.FilialEmpresaDAO;
import com.saax.gestorweb.dao.UsuarioDAO;
import com.saax.gestorweb.dao.UsuarioEmpresaDAO;
import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author rodrigo
 */
public class SignupModel {

    private final EmpresaDAO empresaDAO;
    private final FilialEmpresaDAO filialEmpresaDAO;
    private final EnderecoDAO enderecoDAO;
    private final UsuarioDAO usuarioDAO;
    private final UsuarioEmpresaDAO usuarioEmpresaDAO;

    /**
     * Cria o model e conecta ao DAO
     */
    public SignupModel() {
        empresaDAO = new EmpresaDAO(PostgresConnection.getInstance().getEntityManagerFactory());
        filialEmpresaDAO = new FilialEmpresaDAO(PostgresConnection.getInstance().getEntityManagerFactory());
        enderecoDAO = new EnderecoDAO(PostgresConnection.getInstance().getEntityManagerFactory());
        usuarioDAO = new UsuarioDAO(PostgresConnection.getInstance().getEntityManagerFactory());
        usuarioEmpresaDAO = new UsuarioEmpresaDAO(PostgresConnection.getInstance().getEntityManagerFactory());

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

        if (!FormatterUtil.validarCNPJ(cnpj)) {
            throw new GestorException("CNPJ fora do formato correto (##.###.###/####-##): " + cnpj);
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

        // senha pode ser nula no caso de usuarios adicionados por outro usuario
        if (senha != null) {
            // criptografa a senha informada
            String senhaCriptografada;
            try {
                senhaCriptografada = new Cipher().md5Sum(senha);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(SignupModel.class.getName()).log(Level.SEVERE, "Não encontrado algorítimo MD5", ex);
                return null;
            }

            usuario.setSenha(senhaCriptografada);
        }
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
    public UsuarioEmpresa relacionarUsuarioEmpresa(Usuario usuario, Empresa empresa, boolean administrador) {

        UsuarioEmpresa usuarioEmpresa = new UsuarioEmpresa();
        usuarioEmpresa.setUsuario(usuario);
        usuarioEmpresa.setEmpresa(empresa);
        usuarioEmpresa.setAtivo(true);
        usuarioEmpresa.setContratacao(LocalDate.now());

        usuarioEmpresa.setAdministrador(administrador);

        if (usuario.getEmpresas() == null) {
            usuario.setEmpresas(new ArrayList<>());
        }

        usuario.getEmpresas().add(usuarioEmpresa);

        if (empresa.getUsuarios() == null) {
            empresa.setUsuarios(new ArrayList<>());
        }

        empresa.getUsuarios().add(usuarioEmpresa);

        return usuarioEmpresa;
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

        if (tipoPessoa == 'F') {
            empresa.setCpf(cnpjCpf);
        } else {
            empresa.setCnpj(cnpjCpf);
        }
        empresa.setTipoPessoa(tipoPessoa);
        empresa.setAtiva(true);

        return empresa;

    }

    /**
     * Cria e retorna uma empresa coligada com os parametros informados
     *
     * @param nomeFantasia
     * @param cnpjCpf
     *
     * @return nova empresa criada
     */
    public Empresa criarNovaEmpresaColigada(String nomeFantasia, String cnpjCpf) {
        Empresa empresa = new Empresa();

        empresa.setNome(nomeFantasia);
        empresa.setCpf(cnpjCpf);

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

        empresa.getSubEmpresas().add(subempresa);

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

        filialEmpresa.setMatriz(empresa);

    }

    /**
     * Cadastra um novo usuario com a empresa (conta) principal, sub empresas
     * filiais e demais usuarios.
     *
     * @param empresaPrincipal
     * @param endereco
     * @param usuarioAdm
     * @param subEmpresas
     * @param filiaisEmpresa
     * @param usuarios
     * @return
     */
    public Empresa criarNovaConta(
            Usuario usuarioAdm,
            Empresa empresaPrincipal, 
            Endereco endereco, 
            List<Empresa> subEmpresas, 
            List<FilialEmpresa> filiaisEmpresa, 
            List<UsuarioEmpresa> usuarios) throws GestorException {

        try {

            // Criar o usuario ADM
            usuarioAdm.setDataHoraInclusao(LocalDateTime.now());
            usuarioDAO.create(usuarioAdm);
            usuarioAdm.setUsuarioInclusao(usuarioAdm);
            usuarioDAO.edit(usuarioAdm);

            // Criar a empresa principal
            empresaPrincipal.setDataHoraInclusao(LocalDateTime.now());
            empresaPrincipal.setUsuarioInclusao(usuarioAdm);
            empresaDAO.create(empresaPrincipal);

            usuarioEmpresaDAO.create(relacionarUsuarioEmpresa(usuarioAdm, empresaPrincipal, true));
            
            // Criar endereco da empresa
            relacionarEmpresaEndereco(empresaPrincipal, endereco);
            endereco.setUsuarioInclusao(usuarioAdm);
            endereco.setDataHoraInclusao(LocalDateTime.now());
            endereco.setCidade(new CidadeDAO(PostgresConnection.getInstance().getEntityManagerFactory()).findCidade(1));
            enderecoDAO.create(endereco);

            // criar as sub empresas
            for (Empresa subEmpresa : subEmpresas) {
                relacionarEmpresaColigada(empresaPrincipal, subEmpresa);
                subEmpresa.setUsuarioInclusao(usuarioAdm);
                subEmpresa.setDataHoraInclusao(LocalDateTime.now());
                empresaDAO.create(subEmpresa);
            }

            // criar as filiais
            for (FilialEmpresa filial : filiaisEmpresa) {
                relacionarEmpresaFilial(empresaPrincipal, filial);
                filial.setUsuarioInclusao(usuarioAdm);
                filial.setDataHoraInclusao(LocalDateTime.now());
                filialEmpresaDAO.create(filial);

            }

            // criar demais usuarios
            for (UsuarioEmpresa usuarioEmpresa : usuarios) {
                
                Usuario usuario = usuarioEmpresa.getUsuario();
                usuario.setDataHoraInclusao(LocalDateTime.now());
                usuario.setUsuarioInclusao(usuarioAdm);
            
                usuarioDAO.create(usuario);
                
                usuarioEmpresaDAO.create(relacionarUsuarioEmpresa(usuario, empresaPrincipal, usuarioEmpresa.getAdministrador()));
            }


            /*
             // percorre todos os demais usuarios empresa, criando cada um
             for (UsuarioEmpresa usuarioEmpresa : empresaPrincipal.getUsuarios()) {
              
             Usuario usuario = usuarioEmpresa.getUsuario();
             usuario.setUsuarioInclusao(usuarioAdm);
             usuario.setDataHoraInclusao(LocalDateTime.now());
                
             usuarioDAO.create(usuario);
                
             usuarioEmpresa.getUsuario().setUsuarioInclusao(usuarioAdm);
              
             }
             relacionarUsuarioEmpresa(usuarioAdm, empresaPrincipal, true);
             */
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(SignupModel.class.getName()).log(Level.SEVERE, null, ex);
            throw new GestorException(ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(SignupModel.class.getName()).log(Level.SEVERE, null, ex);
            throw new GestorException(ex.getMessage());
        }

        /*
        
         for (UsuarioEmpresa usuarioEmpresa : empresaPrincipal.getUsuarios()) {
         usuarioEmpresa.getUsuario().setUsuarioInclusao(usuarioAdm);
         }
         Usuario usuario = empresaPrincipal.getUsuarios().
        
         //Usuario usuario = (Usuario) VaadinSession.getCurrent().getAttribute("usuarioLogado");
         */
        /*    
         empresaPrincipal.setUsuarioInclusao(usuarioAdm);
         empresaPrincipal.setDataHoraInclusao(LocalDateTime.now());
        
         Endereco endereco = empresaPrincipal.getEndereco();
         endereco.setUsuarioInclusao(usuarioAdm);
         endereco.setDataHoraInclusao(LocalDateTime.now());
        
         // ATENCAO REMOVER
        
         endereco.setCidade(new CidadeDAO(PostgresConnection.getInstance().getEntityManagerFactory()).findCidade(1));
        
        
         // ATENCAO REMOVER -- FIM
        
         enderecoDAO.create(empresaPrincipal.getEndereco());
        
         empresaDAO.create(empresaPrincipal);
         */
        return empresaPrincipal;

    }

    /**
     * Cria um endereco com os parametros informados
     *
     * @param logradouro
     * @param numero
     * @param complemento
     * @param cep
     * @param cidade
     * @return endereco criado
     */
    public Endereco criarEndereco(String logradouro, String numero, String complemento,
            String cep, Cidade cidade) {
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

        if (endereco.getEmpresas() == null) {
            endereco.setEmpresas(new ArrayList<>());
        }

        empresa.setEndereco(endereco);
        endereco.getEmpresas().add(empresa);

    }

}
