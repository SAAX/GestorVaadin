package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.model.datamodel.UsuarioEmpresa;
import com.saax.gestorweb.util.GestorEntityManagerProvider;

import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.SessionAttributesEnum;
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
     * Obtém o relacionamento entre um usuário e uma empresa 
     * A empresa informada
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
        
        if (empresa == null){
            return new ArrayList<>();
        }
        
        Usuario loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO);

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

}
