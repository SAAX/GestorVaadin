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
     * Listar todos os usuários ativos da mesma empresa do usuário logado
     *
     * @return
     */
    public List<Usuario> listarUsuariosEmpresa() {

        Usuario loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO);
        Empresa empresa = loggedUser.getEmpresaAtiva();

        List<Usuario> usuarios = new ArrayList<>();

        for (UsuarioEmpresa usuarioEmpresa : empresa.getUsuarios()) {
            if (usuarioEmpresa.getAtivo()) {
                usuarios.add(usuarioEmpresa.getUsuario());
            }
        }

        return usuarios;
    }

    public Usuario findByID(Integer idUsuario) {
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
    public Usuario findByLogin(String login) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        Usuario usuario = null;

        try {
            usuario = (Usuario) em.createNamedQuery("Usuario.findByLogin")
                    .setParameter("login", login)
                    .getSingleResult();

            usuario.setEmpresaAtiva(getEmpresaAtiva(usuario));
            
        } catch (Exception e) {

            Logger.getLogger(LoginModel.class.getName()).log(Level.SEVERE, "", e);

        }

        return usuario;

    }
    
    /**
     * Obtém a empresa do usuario logado Só pode have uma
     *
     * @return empresa
     * @throws Runtime se empresa não for encontrada ou existir mais que uma
     */
    public Empresa getEmpresaUsuarioLogado() {

        // obtem o usuario logado
        Usuario usuario = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO);

        return getEmpresaAtiva(usuario);
    }

    
    /**
     * Obtém a empresa do usuario passado por parametro Só pode have uma
     *
     * @param usuario
     * @return empresa
     * @throws Runtime se empresa não for encontrada ou existir mais que uma
     */
    public Empresa getEmpresaAtiva(Usuario usuario) {

        // obtem a empresa ativa do usuario logado 
        // so pode haver uma
        Empresa empresa = null;
        if (usuario.getEmpresas() != null) {
            for (UsuarioEmpresa usuarioEmpresa : usuario.getEmpresas()) {

                if (usuarioEmpresa.getAtivo()) {
                    if (empresa == null) {
                        empresa = usuarioEmpresa.getEmpresa();

                    } else {
                        throw new RuntimeException("Usuario esta ativo em mais de uma empresa.");
                    }
                }
            }
        }

        // dispara exceção se nao encontrar a empresa do usuario logado
        if (empresa == null) {
            throw new RuntimeException("Não foram encontrados os usuarios da empresa");
        }

        return empresa;
    }
    
}
