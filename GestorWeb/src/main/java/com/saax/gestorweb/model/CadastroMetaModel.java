/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.model.datamodel.UsuarioEmpresa;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import com.saax.gestorweb.util.GestorException;
import com.saax.gestorweb.util.GestorSession;
import java.security.InvalidParameterException;
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
public class CadastroMetaModel {

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

    public void criarNovaMeta(HierarquiaProjetoDetalhe categoria) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
