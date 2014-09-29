/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.StatusTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import com.saax.gestorweb.util.GestorException;
import com.vaadin.ui.Upload;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

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
     *
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
                    if (cliente.getAtiva()) {
                        clientes.add(cliente);
                    }
                }
            }
        } catch (GestorException ex) {
            Logger.getLogger(CadastroTarefaModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return clientes;

    }

    public void gravarTarefa(Tarefa tarefa) {
        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        try {

            em.getTransaction().begin();

            em.persist(tarefa);

            em.getTransaction().commit();

        } catch (Exception ex) {
            Logger.getLogger(SignupModel.class.getName()).log(Level.SEVERE, null, ex);
            GestorEntityManagerProvider.getEntityManager().getTransaction().rollback();
        }

    }

    public Tarefa criarNovaTarefa() {

        UsuarioModel usuarioModel = new UsuarioModel();

        Tarefa tarefa = new Tarefa();
        tarefa.setStatus(StatusTarefa.NAO_ACEITA);
        tarefa.setUsuarioInclusao(usuarioModel.getUsuarioLogado());
        tarefa.setAndamento(0);
        tarefa.setAndamentos(new ArrayList<>());
        tarefa.setAnexos(new ArrayList<>());
        tarefa.setApontamentoHoras(false);
        tarefa.setApontamentos(new ArrayList<>());
        tarefa.setAvaliacoes(new ArrayList<>());
        tarefa.setBloqueios(new ArrayList<>());
        tarefa.setFavoritados(new ArrayList<>());
        tarefa.setHistorico(new ArrayList<>());
        tarefa.setNivel(1);
        tarefa.setOrcamentoControlado(false);
        tarefa.setOrcamentos(new ArrayList<>());
        tarefa.setParticipantes(new ArrayList<>());
        tarefa.setSubTarefas(new ArrayList<>());
        tarefa.setTitulo("Tarefa");

        return tarefa;
    }

    /**
     * Valida o arquivo que será enviado ao servidor. <br>
     * Regras: 
     * 1. O arquivo deve existir
     * 2. O arquivo deve ter menos que 10 mb
     * 3. O arquivo nao pode ser executavel por risco de virus
     * 
     * NOTA AO FERNANDAO: Veja que este metodo nao tem try-catch, isto significa que 
     * ele não trata a exceção, apenas PROPAGA pra cima até quem o chamou.
     * 
     * NOTA AO FERNANDAO (2): IllegalArgumentException e SecurityException são exceções não verificadas
     * (sub classes de runtime) portando não precisam estar na cláusula "throws".
     * Mas se forem lançadas irão parar a execução do mesmo jeito.
     * 
     * 
     * @throws java.io.FileNotFoundException
     * @throws IllegalArgumentException 
     * @throws SecurityException 
     * @param event
     */
    public void validarArquivo(Upload.StartedEvent event) throws FileNotFoundException {
        
        if (event == null) {
            throw new IllegalArgumentException("Parâmetro inválido: Event");
        }
        
        
        if (!new File(event.getFilename()).exists()){
            throw new FileNotFoundException("Arquivo não encontrado");
        }
        
        double tamanho = event.getContentLength();
        
        // tamanho em KB
        tamanho = tamanho / 1024;
        // tamanho em MB
        tamanho = tamanho / 1024;
        
        if (tamanho > 10D){
            throw new IllegalArgumentException("Arquivo deve ter menos que 10mb");
        }
        
        
        String extensao = event.getFilename().substring(event.getFilename().lastIndexOf('.'));
        
        Set<String> extensoesProibidas = new HashSet<>();
        extensoesProibidas.add("exe");
        extensoesProibidas.add("bat");
        extensoesProibidas.add("sh");
        
        if (extensoesProibidas.contains(extensao)){
            throw new SecurityException("Arquivo não permitido: "+extensao);
        }
        
    }

}
