package com.saax.gestorweb.model;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.dao.TarefaDAO;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.model.datamodel.UsuarioEmpresa;
import com.saax.gestorweb.util.GestorException;
import com.saax.gestorweb.util.PostgresConnection;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de negócios do Dasboard
 * @author Rodrigo
 */
public class DashboardModel {

    /**
     * 
     * @param usuarioLogado 
     * @return  
     */
    public List<Tarefa> listarTarefas(Usuario usuarioLogado) {
        
        TarefaDAO tarefaDAO = new TarefaDAO(PostgresConnection.getInstance().getEntityManagerFactory());

        List<Tarefa> tarefas = tarefaDAO.listByUsuarioResponsavel(usuarioLogado);

        return tarefas;
        
        
    }

    /**
     * Listar todos os usuários ativos da mesma empresa do usuário logado
     * @return 
     * @throws com.saax.gestorweb.util.GestorException 
     */
    public List<Usuario> listarUsuariosEmpresa() throws GestorException {
        
        Usuario usuario = ((GestorMDI) UI.getCurrent()).getUserData().getUsuarioLogado();
        
        Empresa empresa = null;
        for (UsuarioEmpresa usuarioEmpresa : usuario.getEmpresas()) {
            if (usuarioEmpresa.getAtivo()){
                empresa = usuarioEmpresa.getEmpresa();
            }
        }

        if (empresa==null){
            throw new GestorException("Não foram encontrados os usuarios da empresa");
        }
        List<Usuario> usuarios = new ArrayList<>();
        for (UsuarioEmpresa usuarioEmpresa : empresa.getUsuarios()) {
            if (usuarioEmpresa.getAtivo()){
                usuarios.add(usuarioEmpresa.getUsuario());
            }
        }
        
        return usuarios;
    }

    
}
