/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.GestorException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rodrigo
 */
public class CadastroTarefaModel {

    /**
     * Listar todos os usuários ativos da mesma empresa do usuário logado
     *
     * @return
     * @throws com.saax.gestorweb.util.GestorException
     */
    public List<Usuario> listarUsuariosEmpresa() throws GestorException {
        UsuarioModel usuarioModel = new UsuarioModel();
        return usuarioModel.listarUsuariosEmpresa();
    }

    /**
     * Lista e retorna todos os clientes de todas as empresas de usuario logado
     * @return 
     */
    public List<EmpresaCliente> listarEmpresasCliente() {

        List<EmpresaCliente> clientes = new ArrayList<>();
        try {
            
            EmpresaModel empresaModel = new EmpresaModel();
            
            // obtem as coligadas a empresa do usuario logado
            for (Empresa empresa : empresaModel.listarEmpresasRelacionadas()) {
                // obtem os clientes destas empresas
                for (EmpresaCliente cliente : empresa.getClientes()) {
                    // verifica se o cliente é ativo e adiciona na lista de retorno
                    if (cliente.getAtiva()){
                        clientes.add(cliente);
                    }
                }
            }
        } catch (GestorException ex) {
            Logger.getLogger(CadastroTarefaModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return clientes;
                

    }
    
}
