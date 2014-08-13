package com.saax.gestorweb.model;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.dao.TarefaDAO;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.FilialEmpresa;
import com.saax.gestorweb.model.datamodel.ProjecaoTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.model.datamodel.UsuarioEmpresa;
import com.saax.gestorweb.util.GestorException;
import com.saax.gestorweb.util.PostgresConnection;
import com.vaadin.ui.UI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
        
        Empresa empresa = new UsuarioModel().getEmpresaUsuarioLogado();

        List<Usuario> usuarios = new ArrayList<>();
        
        for (UsuarioEmpresa usuarioEmpresa : empresa.getUsuarios()) {
            if (usuarioEmpresa.getAtivo()){
                usuarios.add(usuarioEmpresa.getUsuario());
            }
        }
        
        
        
        return usuarios;
    }

    
    
    /**
     * Listar as coligadas (se existirem)
     * @return 
     * @throws com.saax.gestorweb.util.GestorException 
     */
    public List<Empresa> listarEmpresasRelacionadas() throws GestorException {
        
       
        Empresa empresa = new UsuarioModel().getEmpresaUsuarioLogado();
        
        List<Empresa> empresas = new ArrayList<>();
        
        // adiciona as coligadas
        empresa.getSubEmpresas().stream().filter((subempresa) -> (subempresa.getAtiva())).forEach((subempresa) -> {
            empresas.add(subempresa);
        });
        
        return empresas;
    }

    /**
     * Lista as tarefas que correspondam aos filtros informados
     * @param usuariosResponsaveis
     * @param usuariosSolicitantes
     * @param usuariosParticipantes
     * @param empresas
     * @param filiais
     * @param dataFim
     * @param projecoes
     * @return 
     */
    public List<Tarefa> listarTarefas(List<Usuario> usuariosResponsaveis, List<Usuario> usuariosSolicitantes, List<Usuario> usuariosParticipantes, List<Empresa> empresas, List<FilialEmpresa> filiais, LocalDate dataFim, List<ProjecaoTarefa> projecoes) {
        
        List<Tarefa> tarefas;

        TarefaDAO tarefaDAO = new TarefaDAO(PostgresConnection.getInstance().getEntityManagerFactory());

        tarefas = tarefaDAO.findTarefaEntities();
        
        if (usuariosResponsaveis!=null){
            tarefas = filtrarUsuarioResponsavel(tarefas, usuariosResponsaveis);
        }
        
        //tarefas = tarefaDAO.findTarefas(usuariosResponsaveis, usuariosSolicitantes, usuariosParticipantes, empresas, filiais, dataFim, projecoes);
        
        return tarefas;
    }
    
    public List<Tarefa> filtrarUsuarioResponsavel(List<Tarefa> tarefas, List<Usuario> usuariosResponsaveis){
        List<Tarefa> tarefasFiltradas = new ArrayList<>();
        tarefas.stream().filter((tarefa) -> (usuariosResponsaveis.contains(tarefa.getUsuarioResponsavel()))).forEach((tarefa) -> {
            tarefasFiltradas.add(tarefa);
        });
        
        return tarefasFiltradas;
    }


    
}
