package com.saax.gestorweb.model;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.model.datamodel.UsuarioEmpresa;
import com.saax.gestorweb.util.GestorException;
import com.vaadin.ui.UI;

/**
 * Classe de negócios do objeto Usuario
 * @author rodrigo
 */
public class UsuarioModel {
    
    /**
     * Obtém a empresa do usuario logado
     * Só pode have uma
     * @return empresa
     * @throws GestorException se empresa não for encontrada ou existir mais que uma
     */
    public  Empresa getEmpresaUsuarioLogado() throws GestorException{
        
        // obtem o usuario logado
        Usuario usuario = ((GestorMDI) UI.getCurrent()).getUserData().getUsuarioLogado();
        
        // obtem a empresa ativa do usuario logado 
        // so pode haver uma
        Empresa empresa = null;
        for (UsuarioEmpresa usuarioEmpresa : usuario.getEmpresas()) {
            
            if (usuarioEmpresa.getAtivo()){
                if (empresa == null){
                    empresa = usuarioEmpresa.getEmpresa();
                    
                }else {
                    throw new GestorException("Usuario esta ativo em mais de uma empresa.");
                }
            }
        }

        // dispara exceção se nao encontrar a empresa do usuario logado
        if (empresa==null){
            throw new GestorException("Não foram encontrados os usuarios da empresa");
        }
        

        return empresa;
    }
}
