package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.model.datamodel.UsuarioEmpresa;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import com.vaadin.server.VaadinSession;
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

    
/**
     * Obtém a empresa do usuario logado
     * Só pode have uma
     * @return empresa
     * @throws Runtime se empresa não for encontrada ou existir mais que uma
     */
    public  Empresa getEmpresaUsuarioLogado()  {
        
        // obtem o usuario logado
        Usuario usuario = (Usuario) VaadinSession.getCurrent().getAttribute("usuarioLogado");
        
        // obtem a empresa ativa do usuario logado 
        // so pode haver uma
        Empresa empresa = null;
        for (UsuarioEmpresa usuarioEmpresa : usuario.getEmpresas()) {
            
            if (usuarioEmpresa.getAtivo()){
                if (empresa == null){
                    empresa = usuarioEmpresa.getEmpresa();
                    
                } else {
                    throw new RuntimeException("Usuario esta ativo em mais de uma empresa.");
                }
            }
        }

        // dispara exceção se nao encontrar a empresa do usuario logado
        if (empresa==null){
            throw new RuntimeException("Não foram encontrados os usuarios da empresa");
        }
        

        return empresa;
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
        
        EntityManager em = GestorEntityManagerProvider.getEntityManager();
        
        Usuario usuario = null;
        
        try {
            usuario = (Usuario) em.createNamedQuery("Usuario.findByLogin")
                    .setParameter("login", login)
                    .getSingleResult();
            
        } catch (Exception e) {

            Logger.getLogger(LoginModel.class.getName()).log(Level.SEVERE, "", e);

        }
        
        return usuario;
        
    }

}
