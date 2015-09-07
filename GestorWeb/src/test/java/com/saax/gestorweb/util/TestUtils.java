/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.util;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.LoginModel;
import com.saax.gestorweb.model.TarefaModel;
import com.saax.gestorweb.model.UsuarioModel;
import com.saax.gestorweb.model.datamodel.HierarquiaProjeto;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.PrioridadeTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.presenter.MetaPresenter;
import com.saax.gestorweb.presenter.TarefaPresenter;
import com.saax.gestorweb.view.MetaView;
import com.saax.gestorweb.view.TarefaView;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.UI;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.Assert;
import static org.junit.Assert.fail;

/**
 *
 * @author rodrigo
 */
public class TestUtils {

    public static void createGestorMDI() {

        // creates UI
        GestorMDI gestor = new GestorMDI();
        UI.setCurrent(gestor);
        gestor.init(null);

    }

    public static void connectDB() {
        // connect to database
        DBConnect.getInstance().assertConnection();
        EntityManager em = PostgresConnection.getInstance().getEntityManagerFactory().createEntityManager();
        GestorEntityManagerProvider.setCurrentEntityManager(em);

    }

    /**
     * metodo utilitario para remover todas as tarefas cadastradas
     */
    public static void limpaBase() {

        Usuario loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO);
        GestorEntityManagerProvider.getEntityManager().getTransaction().begin();

        List<Tarefa> tarefas = GestorEntityManagerProvider.getEntityManager().createQuery("SELECT t FROM Tarefa t").getResultList();

        for (Tarefa tarefa : tarefas) {
            if (tarefa.getTarefaPai() == null) {
                tarefa = GestorEntityManagerProvider.getEntityManager().getReference(Tarefa.class, tarefa.getId());
                GestorEntityManagerProvider.getEntityManager().remove(tarefa);
            }
        }

        List<Meta> metas = GestorEntityManagerProvider.getEntityManager().createQuery("SELECT m FROM Meta m").getResultList();

        for (Meta meta : metas) {
            meta = GestorEntityManagerProvider.getEntityManager().getReference(Meta.class, meta.getId());
            GestorEntityManagerProvider.getEntityManager().remove(meta);
        }
        GestorEntityManagerProvider.getEntityManager().getTransaction().commit();

    }

    public static Usuario getUsuarioTeste() {
        return UsuarioModel.findByLogin("teste-user@gmail.com");
    }

    public static Usuario getUsuarioFernando() {
        return UsuarioModel.findByLogin("fernando.saax@gmail.com");
    }

    public static Usuario getUsuarioRodrigo() {
        return UsuarioModel.findByLogin("rodrigo.ccn2005@gmail.com");
    }

    public static void setUsuarioLogado(Usuario usuario) {
        GestorSession.setAttribute(SessionAttributesEnum.USUARIO_LOGADO, usuario);
        usuario.setEmpresaAtiva(LoginModel.getEmpresaUsuarioLogado());
    }

    public static Usuario getUsuarioLogado() {
        return (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO);
    }

    public static Tarefa cadastrarTarefaSimples(String nome) {

        TarefaView view = new TarefaView();
        TarefaPresenter presenter = new TarefaPresenter(view);

        Usuario loggedUser = TestUtils.getUsuarioLogado();

        HierarquiaProjetoDetalhe categoriaDefaultMeta = TarefaModel.getCategoriaDefaultTarefa();
        presenter.createTask(categoriaDefaultMeta);

        view.getTaskNameTextField().setValue(nome);
        view.getPriorityCombo().setValue(PrioridadeTarefa.ALTA);
        view.getStartDateDateField().setValue(new Date());
        view.getCompanyCombo().setValue(loggedUser.getEmpresaAtiva());
        try {
            view.getTaskFieldGroup().commit();
        } catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        presenter.gravarButtonClicked();

        Tarefa t = (Tarefa) GestorEntityManagerProvider.getEntityManager().createNamedQuery("Tarefa.findByNome")
                .setParameter("nome", nome)
                .setParameter("empresa", loggedUser.getEmpresaAtiva())
                .getSingleResult();

        return t;

    }

    public static Meta cadastrarMetaSimples(String nome){
        
        // prepare
        String nomeEsperado = nome;
        HierarquiaProjeto h = (HierarquiaProjeto) PostgresConnection.getInstance().getEntityManagerFactory().createEntityManager().createNamedQuery("HierarquiaProjeto.findByNome")
                .setParameter("nome", "Projeto")
                .getSingleResult();
        
        HierarquiaProjetoDetalhe categoria = null;
        for (HierarquiaProjetoDetalhe c : h.getCategorias()) {
            if (c.getNivel()==1){
                categoria = c;
            }
        }
        
        MetaView view = new MetaView();
        MetaPresenter presenter = new MetaPresenter(view);
        
        presenter.criarNovaMeta(categoria);
        
        view.getEmpresaCombo().setValue(TestUtils.getUsuarioLogado().getEmpresaAtiva());
        view.getNomeMetaTextField().setValue(nomeEsperado);
        view.getDataInicioDateField().setValue(new Date());
        
        // commit 
        try {
            view.getMetaFieldGroup().commit();
        } catch (FieldGroup.CommitException ex) {
            fail(ex.getMessage());
        }
        
        presenter.gravarButtonClicked();
        
        // gets the persisted Target (to compare)
        Meta m = (Meta) PostgresConnection.getInstance().getEntityManagerFactory().createEntityManager()
                .createNamedQuery("Meta.findByNome")
                .setParameter("nome", nomeEsperado)
                .setParameter("empresa", TestUtils.getUsuarioLogado().getEmpresaAtiva())
                .getSingleResult();

        return m;
    }

    
    
}
