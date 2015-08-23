package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.model.datamodel.UsuarioEmpresa;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.SessionAttributesEnum;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

/**
 * Classe de negócios do Login Esta classe é responsável por implementar todas
 * as regras de negócio do Login para isto ela acessa (pede ajuda) as classes de
 * DAO
 *
 * @author Rodrigo
 */
public class LoginModel {

    // models auxiliares
    private UsuarioModel usuarioModel;
    
    public LoginModel(){
        usuarioModel = new UsuarioModel();
    }
    
    /**
     * Obtém a empresa do usuario logado Só pode have uma
     *
     * @return empresa
     * @throws Runtime se empresa não for encontrada ou existir mais que uma
     */
    public Empresa getEmpresaUsuarioLogado() {

        return usuarioModel.getEmpresaUsuarioLogado();
    }

    /**
     * Verifica se um usuario está cadastrado pelo seu login
     *
     * @param login
     * @return
     */
    public Boolean verificaLoginExistente(String login) {

        List<Usuario> usuarios = null;

        EntityManager em = GestorEntityManagerProvider.getEntityManager();
        try {
            usuarios = em.createNamedQuery("Usuario.findByLogin")
                    .setParameter("login", login)
                    .getResultList();

            return (!usuarios.isEmpty());

        } catch (Exception e) {

            Logger.getLogger(LoginModel.class.getName()).log(Level.SEVERE, "", e);

        }

        return null;

    }

    /**
     * Obtém um usuário pelo seu login
     *
     * @param login
     * @return
     */
    public Usuario getUsuario(String login) {

        return usuarioModel.findByLogin(login);
        

    }

}
