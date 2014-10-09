/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.ApontamentoTarefa;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import com.saax.gestorweb.util.GestorException;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Upload;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
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

    /**
     * Persiste (Grava) uma tarefa
     * @param tarefa 
     * @return a tarefa grava se sucesso, null caso contrario 
     * 
     */
    public Tarefa gravarTarefa(Tarefa tarefa) {
        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        if (tarefa==null){
            throw new IllegalArgumentException("Tarefa NULA para persistencia");
        }
        try {

            em.getTransaction().begin();

            if (tarefa.getId()==null){
                em.persist(tarefa);
            } else {
                em.merge(em);
            }
                

            em.getTransaction().commit();

            
        } catch (RuntimeException ex) {
            // Caso a persistencia falhe, efetua rollback no banco
            if (GestorEntityManagerProvider.getEntityManager().getTransaction().isActive()) {
                GestorEntityManagerProvider.getEntityManager().getTransaction().rollback();
            }
            // propaga a exceção pra cima
            throw ex;
        }

        return tarefa;
    }

    
    /**
     * Valida o arquivo que será enviado ao servidor. <br>
     * Regras: 1. O arquivo deve existir 2. O arquivo deve ter menos que 10 mb
     * 3. O arquivo nao pode ser executavel por risco de virus
     *
     * NOTA AO FERNANDAO: Veja que este metodo nao tem try-catch, isto significa
     * que ele não trata a exceção, apenas PROPAGA pra cima até quem o chamou.
     *
     * NOTA AO FERNANDAO (2): IllegalArgumentException e SecurityException são
     * exceções não verificadas (sub classes de runtime) portando não precisam
     * estar na cláusula "throws". Mas se forem lançadas irão parar a execução
     * do mesmo jeito.
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

        if (!new File(event.getFilename()).exists()) {
            throw new FileNotFoundException("Arquivo não encontrado");
        }

        double tamanho = event.getContentLength();

        // tamanho em KB
        tamanho = tamanho / 1024;
        // tamanho em MB
        tamanho = tamanho / 1024;

        if (tamanho > 10D) {
            throw new IllegalArgumentException("Arquivo deve ter menos que 10mb");
        }

        String extensao = event.getFilename().substring(event.getFilename().lastIndexOf('.'));

        Set<String> extensoesProibidas = new HashSet<>();
        extensoesProibidas.add("exe");
        extensoesProibidas.add("bat");
        extensoesProibidas.add("sh");

        if (extensoesProibidas.contains(extensao)) {
            throw new SecurityException("Arquivo não permitido: " + extensao);
        }

    }

    /**
     * Configura os campos de credito e débido do apontamento de acordo com o usuário logado.
     * @param apontamentoTarefa
     * @return 
     */
    public ApontamentoTarefa configuraApontamento(ApontamentoTarefa apontamentoTarefa) {
        
        // Identifica os usuários relacionados ao apontamento e a tarefa
        Usuario usuarioApontamento = (Usuario) VaadinSession.getCurrent().getAttribute("usuarioLogado");
        Usuario usuarioResponsavel = apontamentoTarefa.getTarefa().getUsuarioResponsavel();
        Usuario usuarioSolicitante = apontamentoTarefa.getTarefa().getUsuarioSolicitante();
        
        LocalTime inputHoras = null;
        try {
            inputHoras = LocalTime.parse(apontamentoTarefa.getInputHoras(), DateTimeFormatter.ofPattern("[HH]:mm"));
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Hora deve ser informada como HH:MM");
        }
        
        // se o usuário for o responsavel as horas inputadas são "debito"
        if (usuarioApontamento.equals(usuarioResponsavel)){
            
            apontamentoTarefa.setDebitoHoras(inputHoras);
            apontamentoTarefa.setDebitoValor(calculaCustoTotalHora(apontamentoTarefa.getCustoHora(), inputHoras));
        } else if (usuarioApontamento.equals(usuarioSolicitante)){
            // se o usuário for o solicitante as horas inputadas são "credito"
            apontamentoTarefa.setCreditoHoras(inputHoras);
            apontamentoTarefa.setCreditoValor(calculaCustoTotalHora(apontamentoTarefa.getCustoHora(), inputHoras));
        } else {
            // TODO: remover comentario
            // se não for nem um nem outro, o usuário não deveria ter acesso ao apontamento.
            // throw new IllegalStateException("Usuário não deveria ter acesso aos apontamentos.");
                        apontamentoTarefa.setCreditoHoras(inputHoras);
                        apontamentoTarefa.setCreditoValor(calculaCustoTotalHora(apontamentoTarefa.getCustoHora(), inputHoras));

        }
        
                
        return apontamentoTarefa;
        
    }


    /**
     * Calcula o custo total como: custo / hora * quantidade de horas
     * @param custoHora
     * @param horas
     * @return 
     */
    private BigDecimal calculaCustoTotalHora(BigDecimal custoHora, LocalTime horas){
        double tempo = horas.getHour() + (horas.getMinute() / 60);
        return new BigDecimal(tempo).multiply(custoHora);
    }
}
