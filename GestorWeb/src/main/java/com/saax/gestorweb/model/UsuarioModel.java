package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.model.datamodel.UsuarioEmpresa;
import com.saax.gestorweb.util.GestorSession;
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
     */
    public List<Usuario> listarUsuariosEmpresa() {

        Usuario usuarioLogado = (Usuario) GestorSession.getAttribute("usuarioLogado");
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
