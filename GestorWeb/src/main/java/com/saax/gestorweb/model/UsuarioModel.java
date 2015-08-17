package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.model.datamodel.UsuarioEmpresa;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.SessionAttributesEnum;
import java.util.ArrayList;
import java.util.List;
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

        Usuario loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO.getAttributeName());
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

}
