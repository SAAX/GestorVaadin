package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.CentroCusto;
import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

/**
 *
 * @author rodrigo
 */
public class EmpresaModel {

    /**
     * Obtem a lista de empresas possiveis de seleção para o usuário logado
     *
     * @param usuarioLogado
     * @return a empresa principal do usuário + as coligadas (se existirem)
     */
    public List<Empresa> listarEmpresasParaSelecao(Usuario usuarioLogado) {

        Empresa empresa = usuarioLogado.getEmpresaAtiva();

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
     * Lista e retorna todos os clientes (EmpresaCliente) de todas as empresas
     * de usuario logado
     *
     * @param usuarioLogado
     * @return lista de EmpresaCliente
     */
    public List<EmpresaCliente> listarEmpresasCliente(Usuario usuarioLogado) {

        List<EmpresaCliente> clientes = new ArrayList<>();

        EmpresaModel empresaModel = new EmpresaModel();

        // obtem as coligadas a empresa do usuario logado
        for (Empresa empresa : empresaModel.listarEmpresasParaSelecao(usuarioLogado)) {
            // obtem os clientes destas empresas
            for (EmpresaCliente cliente : empresa.getClientes()) {
                // verifica se o cliente é ativo e adiciona na lista de retorno
                if (cliente.getAtiva()) {
                    clientes.add(cliente);
                }
            }
        }

        return clientes;

    }

    /**
     * Obtém e retorna a lista de departamentos ativos de uma data empresa <br>
     *
     * @param empresa
     * @return
     */
    public List<Departamento> obterListaDepartamentosAtivos(Empresa empresa) {

        // validação de parâmetros
        if (empresa == null) {
            throw new InvalidParameterException("Parâmetro inválido: Empresa is null");
        }

        EntityManager em = GestorEntityManagerProvider.getEntityManager();
        List<Departamento> departamentos = em.createNamedQuery("Departamento.findByEmpresaAtivo")
                .setParameter("empresa", empresa)
                .getResultList();

        if (departamentos.isEmpty()) {
            Logger.getLogger(CadastroMetaModel.class.getName()).log(Level.WARNING, "Não foram encontrados departamentos para empresa: {0}", empresa.getId());
            throw new IllegalStateException("Não foram encontrados departamentos para empresa");
        }

        return departamentos;
    }
    
    /**
     * Obtém e retorna a lista de centro de custos ativos de uma data empresa <br>
     *
     * @param empresa
     * @return
     */
    public List<CentroCusto> obterListaCentroCustosAtivos(Empresa empresa) {

        // validação de parâmetros
        if (empresa == null) {
            throw new InvalidParameterException("Parâmetro inválido: Empresa is null");
        }

        EntityManager em = GestorEntityManagerProvider.getEntityManager();
        List<CentroCusto> centroCustos = em.createNamedQuery("CentroCusto.findByEmpresaAtivo")
                .setParameter("empresa", empresa)
                .getResultList();

        if (centroCustos.isEmpty()) {
            Logger.getLogger(CadastroMetaModel.class.getName()).log(Level.WARNING, "Não foram encontrados centros de custos para empresa: {0}", empresa.getId());
            throw new IllegalStateException("Não foram encontrados departamentos para empresa");
        }

        return centroCustos;
    }

}
