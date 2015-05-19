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
public class CompanyModel {

    /**
     * Obtem a lista de empresas possiveis de seleção para o usuário logado
     *
     * @param loggedUser
     * @return a empresa principal do usuário + as coligadas (se existirem)
     */
    public List<Empresa> listarEmpresasParaSelecao(Usuario loggedUser) {

        Empresa empresa = loggedUser.getEmpresaAtiva();

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
     * @param loggedUser
     * @return lista de EmpresaCliente
     */
    public List<EmpresaCliente> listarEmpresasCliente(Usuario loggedUser) {

        List<EmpresaCliente> clientes = new ArrayList<>();

        CompanyModel empresaModel = new CompanyModel();

        // obtem as coligadas a empresa do usuario logado
        for (Empresa empresa : empresaModel.listarEmpresasParaSelecao(loggedUser)) {
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

        return centroCustos;
    }

}
