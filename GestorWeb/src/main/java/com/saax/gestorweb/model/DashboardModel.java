package com.saax.gestorweb.model;

import com.saax.gestorweb.dao.GenericDAO;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.FilialEmpresa;
import com.saax.gestorweb.model.datamodel.ProjecaoTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.model.datamodel.UsuarioEmpresa;
import com.saax.gestorweb.presenter.DashboardPresenter;
import com.saax.gestorweb.util.GestorException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;

/**
 * Classe de negócios do Dasboard
 *
 * @author Rodrigo
 */
public class DashboardModel {

    /**
     * Obtém as tarefas sob responsabilidade do usuário logado
     * @param usuarioLogado
     * @return
     */
    public List<Tarefa> listarTarefas(Usuario usuarioLogado) {

        List<Tarefa> tarefas = new GenericDAO().listByNamedQueryEmpresa("Tarefa.findByUsuarioResponsavel", "usuarioResponsavel", usuarioLogado);

        return tarefas;

    }

    /**
     * Listar todos os usuários ativos da mesma empresa do usuário logado
     *
     * @return
     * @throws com.saax.gestorweb.util.GestorException
     */
    public List<Usuario> listarUsuariosEmpresa() throws GestorException {

        Empresa empresa = new UsuarioModel().getEmpresaUsuarioLogado();

        List<Usuario> usuarios = new ArrayList<>();

        for (UsuarioEmpresa usuarioEmpresa : empresa.getUsuarios()) {
            if (usuarioEmpresa.getAtivo()) {
                usuarios.add(usuarioEmpresa.getUsuario());
            }
        }

        return usuarios;
    }

    /**
     * Lista as tarefas que correspondam aos filtros informados
     *
     * @param tipoPesquisa
     * @param usuariosResponsaveis
     * @param usuariosSolicitantes
     * @param usuariosParticipantes
     * @param empresas
     * @param filiais
     * @param dataFim
     * @param projecoes
     * @return
     */
    public List<Tarefa> filtrarTarefas(DashboardPresenter.TipoPesquisa tipoPesquisa, List<Usuario> usuariosResponsaveis,
            List<Usuario> usuariosSolicitantes, List<Usuario> usuariosParticipantes, List<Empresa> empresas, List<FilialEmpresa> filiais, LocalDate dataFim, List<ProjecaoTarefa> projecoes) {

        

        final List<Tarefa> tarefasUsuarioResponsavel = new ArrayList<>();
        usuariosResponsaveis.stream().forEach((usuario) -> {
            // refresh
            usuario = new GenericDAO().merge(usuario);
            tarefasUsuarioResponsavel.addAll(usuario.getTarefasSobResponsabilidade());

        });

        List<Tarefa> tarefasUsuarioSolicitante = new ArrayList<>();
        usuariosSolicitantes.stream().forEach((usuario) -> {
            usuario = new GenericDAO().merge(usuario);
            tarefasUsuarioSolicitante.addAll(usuario.getTarefasSolicitadas());
        });

        
        
        List<Tarefa> tarefasUsuariosParticipantes = new ArrayList<>();
        for (Usuario usuario : usuariosParticipantes) {
            usuario = new GenericDAO().merge(usuario);
            usuario.getTarefasParticipantes().stream().forEach((participanteTarefa) -> {
                tarefasUsuariosParticipantes.add(participanteTarefa.getTarefa());
            });
        }


        List<Tarefa> tarefasEmpresa = new ArrayList<>();
        empresas.stream().forEach((empresa) -> {
            empresa = new GenericDAO().merge(empresa);
            tarefasEmpresa.addAll(empresa.getTarefas());
        });

        
        List<Tarefa> tarefasFiliais = new ArrayList<>();
        filiais.stream().forEach((filial) -> {
            filial = new GenericDAO().merge(filial);
            tarefasFiliais.addAll(filial.getTarefas());
        });

        List<Tarefa> tarefasDataFim = new ArrayList<>();
        if (dataFim != null) {
            tarefasDataFim.addAll(new GenericDAO().listByNamedQueryEmpresa("Tarefa.findByDataFim", "dataFim", dataFim));
        }
        
        List<Tarefa> tarefasProjecao = new ArrayList<>();
        projecoes.stream().forEach((projecao) -> {
            tarefasProjecao.addAll(new GenericDAO().listByNamedQueryEmpresa("Tarefa.findByProjecao", "projecao", projecao));
        });
        
        List<Tarefa> tarefas = new ArrayList<>();
        if (tipoPesquisa == DashboardPresenter.TipoPesquisa.INCLUSIVA_OU){
        
            tarefas.addAll(tarefasUsuarioResponsavel);
          
            tarefas.addAll(tarefasUsuarioSolicitante);

            tarefas.addAll(tarefasUsuariosParticipantes);
        
            tarefas.addAll(tarefasEmpresa);
            
            tarefas.addAll(tarefasFiliais);
            
            tarefas.addAll(tarefasDataFim);
            
            tarefas.addAll(tarefasProjecao);
            
        } else if (tipoPesquisa == DashboardPresenter.TipoPesquisa.EXCLUSIVA_E){
        
            tarefas.addAll(new GenericDAO().listByNamedQueryEmpresa("Tarefa.findAll", null, null));
            
            if (!tarefasUsuarioResponsavel.isEmpty()){
                tarefas.retainAll(tarefasUsuarioResponsavel);
            }
            if (!tarefasUsuarioSolicitante.isEmpty()){
                tarefas.retainAll(tarefasUsuarioSolicitante);
            }
            if (!tarefasUsuariosParticipantes.isEmpty()){
                tarefas.retainAll(tarefasUsuariosParticipantes);
            }
            if (!tarefasEmpresa.isEmpty()){
                tarefas.retainAll(tarefasEmpresa);
            }
            if (!tarefasFiliais.isEmpty()){
                tarefas.retainAll(tarefasFiliais);
            }
            if (!tarefasDataFim.isEmpty()){
                tarefas.retainAll(tarefasDataFim);
            }
            if (!tarefasProjecao.isEmpty()){
                tarefas.retainAll(tarefasProjecao);
            }
            
        }

        return tarefas;
    }


    /**
     * Obtém as tarefas solicitadas pelo usuário logado, ordenadas por data FIM
     * @param usuarioLogado
     * @return 
     */
    public List<Tarefa> listarTarefasPrincipais(Usuario usuarioLogado) {
      
        try {
            
            String sql = "SELECT t FROM Tarefa t WHERE t.empresa = :empresa AND  t.usuarioSolicitante = :usuarioSolicitante ORDER BY t.dataFim DESC";
            Query q = new GenericDAO().createQuery(sql);
            
            Empresa empresa = new UsuarioModel().getEmpresaUsuarioLogado();
            q.setParameter("empresa", empresa);
            q.setParameter("usuarioSolicitante", usuarioLogado);
            
            List<Tarefa> tarefas = q.getResultList();
            
            return tarefas;
        } catch (GestorException ex) {
            Logger.getLogger(DashboardModel.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
   
}
