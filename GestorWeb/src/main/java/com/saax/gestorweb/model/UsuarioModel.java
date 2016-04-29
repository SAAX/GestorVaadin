package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.model.datamodel.UsuarioEmpresa;
import com.saax.gestorweb.util.Cipher;
import com.saax.gestorweb.util.GestorEntityManagerProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

/**
 * Classe de negócios do objeto Usuario
 *
 * @author rodrigo
 */
public class UsuarioModel {

    /**
     * Obtém o relacionamento entre um usuário e uma empresa A empresa informada
     * pode ser coligada a uma matriz
     *
     * @param usuario
     * @param empresa
     * @return
     */
    public static UsuarioEmpresa getRelacionamentoUsuarioEmpresa(Usuario usuario, Empresa empresa) {

        for (UsuarioEmpresa empresaUsuario : usuario.getEmpresas()) {
            if (empresaUsuario.getEmpresa().equals(empresa) || empresaUsuario.getEmpresa().equals(empresa.getEmpresaPrincipal())) {
                if (empresaUsuario.getAtivo()) {
                    return empresaUsuario;
                }
            }
        }
        return null;
    }

    /**
     * Listar todos os usuários ativos da mesma empresa do usuário logado
     *
     * @param empresa
     * @return
     */
    public static List<Usuario> listarUsuariosEmpresa(Empresa empresa) {

        if (empresa == null) {
            return new ArrayList<>();
        }

        List<Usuario> usuarios = new ArrayList<>();

        for (UsuarioEmpresa usuarioEmpresa : empresa.getUsuarios()) {
            if (usuarioEmpresa.getAtivo()) {
                usuarios.add(usuarioEmpresa.getUsuario());
            }
        }

        return usuarios;
    }

    public static Usuario findByID(Integer idUsuario) {
        EntityManager em = GestorEntityManagerProvider.getEntityManager();
        return (Usuario) em.createNamedQuery("Usuario.findById")
                .setParameter("id", idUsuario)
                .getSingleResult();

    }

    /**
     * Obtém um usuário pelo seu login
     *
     * @param login
     * @return
     */
    public static Usuario findByLogin(String login) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        Usuario usuario = null;

        try {
            usuario = (Usuario) em.createNamedQuery("Usuario.findByLogin")
                    .setParameter("login", login)
                    .getSingleResult();

        }
        catch (Exception e) {

            Logger.getLogger(LoginModel.class.getName()).log(Level.SEVERE, "", e);

        }

        return usuario;

    }

    /**
     * Altera a senha de um usuário, dado seu Login
     *
     * @param login
     * @param passwd
     */
    public static void alteraSenhaUsuario(String login, String passwd) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        Usuario usuario = null;

        System.err.println("Alteração de senha\n\n usuario:" + login + "\tsenha:" + passwd);

        try {
            usuario = (Usuario) em.createNamedQuery("Usuario.findByLogin")
                    .setParameter("login", login)
                    .getSingleResult();

            String senhaCriptografada = new Cipher().md5Sum(passwd);

            usuario.setSenha(senhaCriptografada);
            usuario.setPrimeiroLogin(false);

            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }

            em.merge(usuario);
            em.getTransaction().commit();

        }
        catch (Exception e) {

            Logger.getLogger(LoginModel.class.getName()).log(Level.SEVERE, "", e);

        }
    }
}
