/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.model.datamodel.UsuarioEmpresa;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import com.saax.gestorweb.util.GestorException;
import com.saax.gestorweb.util.GestorSession;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

/**
 * Classe de negócios do cadastro de Metas <br><br>
 *
 * Esta classe é responsável por implementar todas as regras de negócio do
 * cadastro de metas para isto ela acessa (pede ajuda) as classes de DAO<br>
 *
 * @author Rodrigo
 */
public class CadastroMetasModel {

    /**
     * Obtém e retorna a lista de departamentos ativos de uma data empresa <br>
     *
     * @param empresa
     * @return
     */
    public List<Departamento> obterListaDepartamentosAtivos(Empresa empresa) throws GestorException {

        // validação de parâmetros
        if (empresa == null) {
            throw new GestorException("Parâmetro inválido: Empresa is null");
        }

        EntityManager em = GestorEntityManagerProvider.getEntityManager();
        List<Departamento> departamentos = em.createNamedQuery("Departamento.findAll").getResultList();

        if (departamentos.isEmpty()) {
            Logger.getLogger(CadastroMetasModel.class.getName()).log(Level.WARNING, "Não foram encontrados departamentos para empresa: {0}", empresa.getId());
            throw new GestorException("Não foram encontrados departamentos para empresa");
        }

        return departamentos;
    }

    /**
     * Obtém e retorna a lista de empresas ativas relacionadas ao usuário logado
     * São consideradas as empresas diretamente ligadas ao usuário, bem como as
     * subempresas no caso de corporações
     *
     * @return lista de empresas
     */
    public List<Empresa> obterListaEmpresasUsuarioLogado() {

        // Usuário logado na sessão
        Usuario usuario = (Usuario) GestorSession.getAttribute("usuarioLogado");

        List<Empresa> empresas = new ArrayList<Empresa>();
        for (UsuarioEmpresa empresaUsuario : usuario.getEmpresas()) {
            if (empresaUsuario.getAtivo()) {
                Empresa empresa = empresaUsuario.getEmpresa();
                // adiciona a empresa
                empresas.add(empresa);
                // a empresa é a principal de uma corporação ?
                if (!empresa.getSubEmpresas().isEmpty()){
                    // neste caso adiciona também as sub empresas
                    for (Empresa subempresa : empresa.getSubEmpresas()) {
                        empresas.add(subempresa);
                    }
                }
            }
        }

        return empresas;
    }

}
