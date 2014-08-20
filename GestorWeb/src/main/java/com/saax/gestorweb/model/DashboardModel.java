package com.saax.gestorweb.model;

import com.saax.gestorweb.dao.GenericDAO;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.FilialEmpresa;
import com.saax.gestorweb.model.datamodel.ProjecaoTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.model.datamodel.UsuarioEmpresa;
import com.saax.gestorweb.util.GestorException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de negócios do Dasboard
 *
 * @author Rodrigo
 */
public class DashboardModel {

    /**
     *
     * @param usuarioLogado
     * @return
     */
    public List<Tarefa> listarTarefas(Usuario usuarioLogado) {

        List<Tarefa> tarefas = new GenericDAO().listByNamedQuery("Tarefa.findByUsuarioResponsavel", "usuarioResponsavel", usuarioLogado);

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
     * Listar as coligadas (se existirem)
     *
     * @return
     * @throws com.saax.gestorweb.util.GestorException
     */
    public List<Empresa> listarEmpresasRelacionadas() throws GestorException {

        Empresa empresa = new UsuarioModel().getEmpresaUsuarioLogado();

        List<Empresa> empresas = new ArrayList<>();

        empresas.add(empresa);

        for (Empresa subempresa : empresa.getSubEmpresas()) {
            if (subempresa.getAtiva()) {
                empresas.add(subempresa);

            }

        }

        return empresas;
    }

    /**
     * Lista as tarefas que correspondam aos filtros informados
     *
     * @param usuariosResponsaveis
     * @param usuariosSolicitantes
     * @param usuariosParticipantes
     * @param empresas
     * @param filiais
     * @param dataFim
     * @param projecoes
     * @return
     */
    public List<Tarefa> listarTarefas(List<Usuario> usuariosResponsaveis,
            List<Usuario> usuariosSolicitantes, List<Usuario> usuariosParticipantes, List<Empresa> empresas, List<FilialEmpresa> filiais, LocalDate dataFim, List<ProjecaoTarefa> projecoes) {

        List<Tarefa> tarefas = new ArrayList<>();

        usuariosResponsaveis.stream().forEach((usuario) -> {
            // refresh
            usuario = new GenericDAO().merge(usuario);
            tarefas.addAll(usuario.getTarefasSobResponsabilidade());

        });

        usuariosSolicitantes.stream().forEach((usuario) -> {
            usuario = new GenericDAO().merge(usuario);
            tarefas.addAll(usuario.getTarefasSolicitadas());
        });

        for (Usuario usuario : usuariosParticipantes) {
            usuario = new GenericDAO().merge(usuario);
            usuario.getTarefasParticipantes().stream().forEach((participanteTarefa) -> {
                tarefas.add(participanteTarefa.getTarefa());
            });
        }

        empresas.stream().forEach((empresa) -> {
            empresa = new GenericDAO().merge(empresa);
            tarefas.addAll(empresa.getTarefas());
        });

        filiais.stream().forEach((filial) -> {
            filial = new GenericDAO().merge(filial);
            tarefas.addAll(filial.getTarefas());
        });

        if (dataFim != null) {
            tarefas.addAll(new GenericDAO().listByNamedQuery("Tarefa.findByDataFim", "dataFim", dataFim));
        }

        projecoes.stream().forEach((projecao) -> {
            tarefas.addAll(new GenericDAO().listByNamedQuery("Tarefa.findByProjecao", "projecao", projecao));
        });

        /*      
         if (usuariosResponsaveis!=null){
         tarefas = filtrarUsuarioResponsavel(tarefas, usuariosResponsaveis);
         }
        
         if (empresas!=null){
         tarefas = filtrarEmpresas(tarefas, empresas);
         }
        
         if (filiais!=null){
         tarefas = filtrarUsuarioResponsavel(tarefas, usuariosResponsaveis);
         }
        
         //tarefas = tarefaDAO.findTarefas(usuariosResponsaveis, usuariosSolicitantes, usuariosParticipantes, empresas, filiais, dataFim, projecoes);
         */
        return tarefas;
    }

    public List<Tarefa> filtrarEmpresas(List<Tarefa> tarefas, List<Empresa> empresas) {
        List<Tarefa> tarefasFiltradas = new ArrayList<>();
        tarefas.stream().filter((tarefa) -> (empresas.contains(tarefa.getEmpresa()))).forEach((tarefa) -> {
            tarefasFiltradas.add(tarefa);
        });

        return tarefasFiltradas;
    }

    /*
     public List<Tarefa> filtrarUsuarioResponsavel(List<Tarefa> tarefas, List<Usuario> usuariosResponsaveis){
     List<Tarefa> tarefasFiltradas = new ArrayList<>();
     tarefas.stream().filter((tarefa) -> (usuariosResponsaveis.contains(tarefa.getUsuarioResponsavel()))).forEach((tarefa) -> {
     tarefasFiltradas.add(tarefa);
     });
        
     return tarefasFiltradas;
     }

     public List<Tarefa> filtrarEmpresas(List<Tarefa> tarefas, List<Empresa> empresas){
     List<Tarefa> tarefasFiltradas = new ArrayList<>();
     tarefas.stream().filter((tarefa) -> (empresas.contains(tarefa.getEmpresa()))).forEach((tarefa) -> {
     tarefasFiltradas.add(tarefa);
     });
        
     return tarefasFiltradas;
     }

     public List<Tarefa> filtrarFiliais(List<Tarefa> tarefas, List<Filialem> empresas){
     List<Tarefa> tarefasFiltradas = new ArrayList<>();
     tarefas.stream().filter((tarefa) -> (empresas.contains(tarefa.getEmpresa()))).forEach((tarefa) -> {
     tarefasFiltradas.add(tarefa);
     });
        
     return tarefasFiltradas;
     }

     */
   
}
