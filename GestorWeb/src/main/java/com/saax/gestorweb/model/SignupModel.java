package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.Cidade;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.Endereco;
import com.saax.gestorweb.model.datamodel.FilialEmpresa;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.model.datamodel.UsuarioEmpresa;
import com.saax.gestorweb.util.Cipher;
import com.saax.gestorweb.util.FormatterUtil;
import com.saax.gestorweb.util.GestorEntityManagerProvider;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author rodrigo
 */
public class SignupModel {

    /**
     * Repassa ao model de login a verificação do email do usuario
     *
     * @param login
     * @return true se existe este email cadastrado como login
     */
    public boolean verificaLoginExistente(String login) {
        return LoginModel.verificaLoginExistente(login);
    }

    /**
     * Verifica se uma empresa (conta) esta cadastrada no sistema pelo seu CNPJ
     * / CPF
     *
     * @param cpf_cnpj CPF ou CNPJ da empresa
     * @param tipoPessoa Tipo da pessoa: Fisica / Juridica
     * @return true se a empresa está cadastrada
     */
    public boolean verificaEmpresaExistente(String cpf_cnpj, char tipoPessoa) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        if (StringUtils.isBlank(cpf_cnpj)) {
            return false;
        }

        if (tipoPessoa == 'J') {
            if (!FormatterUtil.validarCNPJ(cpf_cnpj)) {
                throw new RuntimeException("CNPJ fora do formato correto (##.###.###/####-##): " + cpf_cnpj);
            }

            List<Empresa> empresas = null;

            empresas = em.createNamedQuery("Empresa.findByCnpj")
                    .setParameter("cnpj", cpf_cnpj)
                    .getResultList();

            return (!empresas.isEmpty());

        } else if (tipoPessoa == 'F') {
            if (!FormatterUtil.validarCPF(cpf_cnpj)) {
                throw new RuntimeException("CPF fora do formato correto (###.###.###-##): " + cpf_cnpj);
            }
            Empresa e = (Empresa) em.createNamedQuery("Empresa.findByCpf")
                    .setParameter("cpf", cpf_cnpj)
                    .getSingleResult();

            return (e != null);
        } else {

            throw new RuntimeException("Tipo Pessoa fora do formato correto (F/J)");

        }

    }

    /**
     * Verifica se uma filial de empresa está cadastrada no sistema pelo seu
     * CNPJ
     *
     * @param cnpj CNPJ da filial
     * @return true se a filial está cadastrada
     */
    public boolean verificaFilialExistente(String cnpj) {
        if (StringUtils.isBlank(cnpj)) {
            return false;
        }

        if (!FormatterUtil.validarCNPJ(cnpj)) {
            throw new RuntimeException("CNPJ fora do formato correto (##.###.###/####-##): " + cnpj);
        }

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        List<FilialEmpresa> e = em.createNamedQuery("FilialEmpresa.findByCNPJ")
                .setParameter("cnpj", cnpj)
                .getResultList();

        if (e.isEmpty()) {
            return (false);
        } else {
            return (true);
        }

    }

    public Usuario atualizaUsuario(Usuario usuario, String nome, String sobreNome, String email, String senha) {

        usuario.setNome(nome);
        usuario.setSobrenome(sobreNome);
        usuario.setLogin(email);

        // senha pode ser nula no caso de usuarios adicionados por outro usuario
        if (senha != null) {
            // criptografa a senha informada
            String senhaCriptografada;
            try {
                senhaCriptografada = new Cipher().md5Sum(senha);

            }
            catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(SignupModel.class
                        .getName()).log(Level.SEVERE, "Não encontrado algorítimo MD5", ex);

                return null;
            }

            usuario.setSenha(senhaCriptografada);
        }

        return usuario;

    }

    /**
     * Cria e retorna um usuario com os parametros informados
     *
     * @param nome
     * @param sobreNome
     * @param email
     * @param senha
     * @param usuarioAdm
     * @return novo usuario criado
     */
    public Usuario criarNovoUsuario(String nome, String sobreNome, String email, String senha, Usuario usuarioAdm) {

        Usuario usuario = atualizaUsuario(new Usuario(), nome, sobreNome, email, senha);

        usuario.setUsuarioInclusao(usuarioAdm == null ? usuario : usuarioAdm);
        usuario.setDataHoraInclusao(LocalDateTime.now());

        return usuario;
    }

    /**
     * Cria e retorna um usuario com os parametros informados e com senha
     * default
     *
     * @param nome
     * @param sobreNome
     * @param email
     * @param usuarioAdm
     * @return novo usuario criado
     */
    public Usuario criarNovoUsuario(String nome, String sobreNome, String email, Usuario usuarioAdm) {

        String senha = "123456";

        return criarNovoUsuario(nome, sobreNome, email, senha, usuarioAdm);
    }

    /**
     * Cria o relacionamento entre o usuario e a empresa
     *
     * @param usuario
     * @param empresa
     * @param administrador
     * @param usuarioADM
     * @return
     */
    public UsuarioEmpresa relacionarUsuarioEmpresa(Usuario usuario, Empresa empresa, boolean administrador, Usuario usuarioADM) {

        UsuarioEmpresa usuarioEmpresa = new UsuarioEmpresa();
        usuarioEmpresa.setUsuario(usuario);
        usuarioEmpresa.setEmpresa(empresa);
        usuarioEmpresa.setAtivo(true);
        usuarioEmpresa.setContratacao(LocalDate.now());

        usuarioEmpresa.setAdministrador(administrador);
        usuarioEmpresa.setUsuarioInclusao(usuarioADM);
        usuarioEmpresa.setDataHoraInclusao(LocalDateTime.now());

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
     * @param usuarioADM
     *
     * @return nova empresa criada
     */
    public Empresa criarNovaEmpresa(String razaosocial, String cnpjCpf, char tipoPessoa, Usuario usuarioADM) {
        Empresa empresa = new Empresa();
        empresa.setUsuarioInclusao(usuarioADM);
        empresa.setDataHoraInclusao(LocalDateTime.now());
        empresa = atualizarEmpresa(empresa, razaosocial, cnpjCpf, tipoPessoa, usuarioADM);

        return empresa;

    }

    public Empresa atualizarEmpresa(Empresa empresa,
            String razaosocial, String cpfCnpj, char tipoPessoa, Usuario usuarioADM) {
        empresa.setRazaoSocial(razaosocial);

        if (tipoPessoa == 'F') {
            empresa.setCpf((FormatterUtil.removeNonDigitChars(cpfCnpj)));
            empresa.setCnpj(null);
        } else {
            empresa.setCnpj((FormatterUtil.removeNonDigitChars(cpfCnpj)));
            empresa.setCpf(null);
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
     * @param usuarioADM
     *
     * @return nova empresa criada
     */
    public Empresa criarNovaEmpresaColigada(String nomeFantasia, String cnpjCpf, Usuario usuarioADM) {
        Empresa empresa = new Empresa();

        char tipoPessoa = 'J';

        empresa.setNome(nomeFantasia);
        empresa.setCpf(FormatterUtil.removeNonDigitChars(cnpjCpf));
        empresa.setTipoPessoa(tipoPessoa);
        empresa.setRazaoSocial(nomeFantasia);
        empresa.setAtiva(true);
        empresa.setUsuarioInclusao(usuarioADM);
        empresa.setDataHoraInclusao(LocalDateTime.now());

        return empresa;

    }

    /**
     * Cria e retorna uma filial de empresa com os parametros informados
     *
     * @param nome
     * @param cnpj
     * @param usuarioADM
     *
     * @return nova empresa criada
     */
    public FilialEmpresa criarFilialEmpresa(String nome, String cnpj, Usuario usuarioADM) {

        FilialEmpresa filialEmpresa = new FilialEmpresa();
        filialEmpresa.setNome(nome);
        filialEmpresa.setCnpj(FormatterUtil.removeNonDigitChars(cnpj));
        filialEmpresa.setAtiva(true);
        filialEmpresa.setUsuarioInclusao(usuarioADM);
        filialEmpresa.setDataHoraInclusao(LocalDateTime.now());

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
     * @param coligadasRemovidasNaEdicao
     * @return
     */
    public Empresa gravarConta(Empresa empresaPrincipal, List<Empresa> coligadasRemovidasNaEdicao) throws RuntimeException {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        // Verifica se o parametro é válido antes de começar a transação
        // assim, se não for válido já para o método
        if (empresaPrincipal == null) {
            throw new IllegalArgumentException("Empresa para persistencia está NULA.");
        }

        try {

            em.getTransaction().begin();

            for (Empresa coligada : coligadasRemovidasNaEdicao) {
                moverTarefasMetasParaMatriz(coligada, empresaPrincipal);
                desrelacionarEmpresaColigada(empresaPrincipal, coligada);
                em.remove(coligada);
            }

//            Usuario loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO);
//
//            for (UsuarioEmpresa ue : loggedUser.getEmpresas()) {
//                if (!ue.getEmpresa().equals(empresaPrincipal)) {
//                    em.find(ue.getEmpresa().getClass(), ue.getId());
//                }
//            }
            if (empresaPrincipal.getId() == 0) {
                em.persist(empresaPrincipal);

            } else {
                em.merge(empresaPrincipal);
            }

            em.getTransaction().commit();

        }
        catch (RuntimeException ex) {
            // Caso ocorra alguma exceção durante a transação, efetua o rollback
            GestorEntityManagerProvider.getEntityManager().getTransaction().rollback();
            // E propaga a exceção pra cima:
            throw ex; // Nota: caso não fosse necessário fazer o rollback, também não seria necessário o try-catch, nem o throw
        }

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
            String cep, Cidade cidade, Usuario usuarioADM) {
        Endereco endereco = new Endereco();
        endereco.setUsuarioInclusao(usuarioADM);
        endereco.setDataHoraInclusao(LocalDateTime.now());
        endereco = atualizarEndereco(endereco, logradouro, numero, complemento, cep, cidade, usuarioADM);
        return endereco;
    }

    public Endereco atualizarEndereco(Endereco endereco, String logradouro, String numero, String complemento, String cep, Cidade cidade, Usuario usuarioADM) {
        endereco.setLogradouro(logradouro);
        endereco.setComplemento(complemento);
        endereco.setNumero(numero);
        endereco.setCep(FormatterUtil.removeNonDigitChars(cep));
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

    public boolean verificaTarefasColigadaRemocao(Empresa empresa, String idColigadaString) {
        // se a empresa é nula significa que este é um cadastro novo, e portanto a coligada 
        // (que nem existe ainda) não tem nenhuma tarefa.
        if (empresa == null) {
            return false;
        }

        // verifica se a coligada nao é nova (tem id temporario)
        // e neste caso também não terá tarefas
        if (idColigadaString.startsWith("TEMP_")) {
            return false;
        }

        // caso nenhumas das opções acima ocorra,
        // a coligada já existe na base e o sistema verifica
        // se ela possui tarefas
        Empresa coligada = getColigada(empresa, idColigadaString);
        if (!coligada.getTarefas().isEmpty()) {
            return true;
        }
        if (!coligada.getMetas().isEmpty()) {
            return true;
        }

        return false;

    }

    public Empresa getColigada(Empresa empresa, String idColigadaString) {

        int idColigada = Integer.parseInt(idColigadaString);
        for (Empresa coligada : empresa.getSubEmpresas()) {
            if (coligada.getId().equals(idColigada)) {
                return coligada;
            }
        }

        return null;
    }

    private void moverTarefasMetasParaMatriz(Empresa coligada, Empresa matriz) {
        for (Tarefa tarefa : coligada.getTarefas()) {
            tarefa.setEmpresa(matriz);
        }
        for (Meta meta : coligada.getMetas()) {
            meta.setEmpresa(matriz);
        }
    }

    private void desrelacionarEmpresaColigada(Empresa empresaPrincipal, Empresa coligada) {
        coligada.setEmpresaPrincipal(null);
        empresaPrincipal.getSubEmpresas().remove(coligada);
    }

    public FilialEmpresa getFilial(Empresa empresa, String id) {
        int idColigada = Integer.parseInt(id);
        for (FilialEmpresa filial : empresa.getFiliais()) {
            if (filial.getId().equals(idColigada)) {
                return filial;
            }
        }

        return null;
    }

    public UsuarioEmpresa getUsuario(Empresa empresa, String email) {
        for (UsuarioEmpresa u : empresa.getUsuarios()) {
            if (u.getUsuario().getLogin().equals(email)) {
                return u;
            }
        }
        return null;
    }

}
