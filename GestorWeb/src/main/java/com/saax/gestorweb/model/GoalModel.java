/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.CentroCusto;
import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.EmpresaCliente;
import com.saax.gestorweb.model.datamodel.HierarquiaProjeto;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.PrioridadeMeta;
import com.saax.gestorweb.model.datamodel.StatusMeta;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Classe de negócios do cadastro de Metas <br><br>
 *
 * Esta classe é responsável por implementar todas as regras de negócio do
 * cadastro de metas para isto ela acessa (pede ajuda) as classes de DAO<br>
 *
 * @author Rodrigo
 */
public class GoalModel {

    // Classes do modelo acessórias acessadas por este model
    private final UsuarioModel usuarioModel;
    private final CompanyModel empresaModel;

    public GoalModel() {
        usuarioModel = new UsuarioModel();
        empresaModel = new CompanyModel();

    }

    /**
     * Cria uma nova meta na categoria informada, com valores default
     *
     * @param categoria
     * @param loggedUser
     * @return a meta criada
     */
    public Meta criarNovaMeta(HierarquiaProjetoDetalhe categoria, Usuario loggedUser) {
        Meta meta = new Meta();
        meta.setCategoria(categoria);
        meta.setDataHoraInclusao(LocalDateTime.now());
        meta.setUsuarioSolicitante(loggedUser);
        meta.setUsuarioInclusao(loggedUser);
        meta.setStatus(StatusMeta.NAO_INICIADA);
        meta.setPrioridade(PrioridadeMeta.ALTA);
        return meta;

    }

    /**
     * Listar todos os usuários ativos da mesma empresa do usuário logado Delega
     * chamada ao model responsavel (UsuarioModel)
     *
     * @return
     */
    public List<Usuario> listarUsuariosEmpresa() {
        return usuarioModel.listarUsuariosEmpresa();
    }

    /**
     * Delega chamada ao model responsavel (CompanyModel)
     *
     * @param loggedUser
     * @return lista de EmpresaCliente
     */
    public List<EmpresaCliente> listarEmpresasCliente(Usuario loggedUser) {
        return empresaModel.listarEmpresasCliente(loggedUser);
    }

    /**
     * Delega chamada ao model responsavel (CompanyModel)
     *
     * @param empresa
     * @return
     */
    public List<Departamento> obterListaDepartamentosAtivos(Empresa empresa) {
        return empresaModel.obterListaDepartamentosAtivos(empresa);
    }

    /**
     * Delega chamada ao model responsavel (CompanyModel)
     *
     * @param empresa
     * @return
     */
    public List<CentroCusto> obterListaCentroCustosAtivos(Empresa empresa) {
        return empresaModel.obterListaCentroCustosAtivos(empresa);
    }

    /**
     * Persiste (Grava) uma meta
     *
     * @param meta
     * @return a meta gravada se sucesso, null caso contrario
     *
     */
    public Meta gravarMeta(Meta meta) {
        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        if (meta == null) {
            throw new IllegalArgumentException("Meta NULA para persistencia");
        }

        try {

            // so abre transação na gravacao da tarefa pai
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }

            if (meta.getId() == null) {
                em.persist(meta);
            } else {
                em.merge(meta);
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

        return meta;
    }

    /**
     * Given a Target category, searches and returns its firsts tasks categories.
     * <br>
     * For example, given the structure below, its'll returns Task Type 1 and 2: <br>
     * <dl>
     *      <dt>Target</dt>
     *      <dd>
     *          <dl>
     *              <dt>Task Type 1</dt>
     *              <dd>
     *                  <dl>
     *                      <dt>SubTask Type 1b</dt>
     *                      <dt>SubTask Type 1c</dt>
     *                  </dl>
     *              </dd>
     *              <dt>Task Type 2</dt>
     *              <dd>
     *                  <dl>
     *                      <dt>SubTask Type 2a</dt>
     *                      <dt>SubTask Type 2b</dt>
     *                  </dl>
     *              </dd>
     *          </dl>
     *      </dd>
     * </dl>
     *
     * @param TargetCategory of the Target
     * @return a list of categories with every task category child of the give TargetCategory
     */
    public List<HierarquiaProjetoDetalhe> getFirstsTaskCategories(HierarquiaProjetoDetalhe TargetCategory) {
        
        // the task's categories
        List<HierarquiaProjetoDetalhe> tasksCategories = new ArrayList<>();
        
        // retrieves the category set of the Target category
        HierarquiaProjeto categorySet = TargetCategory.getHierarquia();
        
        // the task level into the category set
        final int TASK_LEVEL = 2;
        
        // gets every level 2 category an puts into tasksCategories
        for (HierarquiaProjetoDetalhe category : categorySet.getCategorias()) {
            if (category.getNivel() == TASK_LEVEL){
            tasksCategories.add(category);
            }
        }
        
        return tasksCategories;
    }

}
