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
import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.PrioridadeTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.presenter.TarefaPresenter;
import com.saax.gestorweb.view.TarefaView;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.UI;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
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
    public static void removeTodasTarefas() {

        Usuario loggedUser = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO);
        GestorEntityManagerProvider.getEntityManager().getTransaction().begin();
        
        List<Tarefa> tarefas = GestorEntityManagerProvider.getEntityManager().createQuery("SELECT t FROM Tarefa t").getResultList();
        
        for (Tarefa tarefa : tarefas) {
            if (tarefa.getTarefaPai() == null) {
                tarefa = GestorEntityManagerProvider.getEntityManager().getReference(Tarefa.class, tarefa.getId());
                GestorEntityManagerProvider.getEntityManager().remove(tarefa);
            }
        }
        GestorEntityManagerProvider.getEntityManager().getTransaction().commit();

    }

    public static Usuario getUsuarioTeste() {
        UsuarioModel usuarioModel = new UsuarioModel();
        return usuarioModel.findByLogin("teste-user@gmail.com");
    }

    public static Usuario getUsuarioFernando() {
        UsuarioModel usuarioModel = new UsuarioModel();
        return usuarioModel.findByLogin("fernando.saax@gmail.com");
    }

    public static Usuario getUsuarioRodrigo() {
        UsuarioModel usuarioModel = new UsuarioModel();
        return usuarioModel.findByLogin("rodrigo.ccn2005@gmail.com");
    }

    public static void setUsuarioLogado(Usuario usuario) {
        GestorSession.setAttribute(SessionAttributesEnum.USUARIO_LOGADO, usuario);
        usuario.setEmpresaAtiva(new LoginModel().getEmpresaUsuarioLogado());
    }

    public static Usuario getUsuarioLogado() {
        return (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO);
    }

    public static Tarefa cadastrarTarefaSimples(String nome) {

        TarefaView view = new TarefaView();
        TarefaModel model = new TarefaModel();
        TarefaPresenter presenter = new TarefaPresenter(model, view);

        Usuario loggedUser = TestUtils.getUsuarioLogado();

        HierarquiaProjetoDetalhe categoriaDefaultMeta = model.getCategoriaDefaultTarefa();
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

}
