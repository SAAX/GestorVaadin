package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.model.datamodel.UsuarioEmpresa;
import com.saax.gestorweb.util.GestorException;
import com.vaadin.server.VaadinSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de negócios do objeto Usuario
 * @author rodrigo
 */
public class UsuarioModel {
    
    
    /**
     * Listar todos os usuários ativos da mesma empresa do usuário logado
     *
     * @return
     * @throws com.saax.gestorweb.util.GestorException
     */
    public List<Usuario> listarUsuariosEmpresa() throws GestorException {

        Usuario usuarioLogado = (Usuario) VaadinSession.getCurrent().getAttribute("usuarioLogado");
        Empresa empresa = usuarioLogado.getEmpresaAtiva();

        List<Usuario> usuarios = new ArrayList<>();

        for (UsuarioEmpresa usuarioEmpresa : empresa.getUsuarios()) {
            if (usuarioEmpresa.getAtivo()) {
                usuarios.add(usuarioEmpresa.getUsuario());
            }
        }

        return usuarios;
    }

}
