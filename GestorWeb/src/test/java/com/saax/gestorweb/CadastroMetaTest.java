/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb;

import com.saax.gestorweb.model.CadastroMetaModel;
import com.saax.gestorweb.model.LoginModel;
import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.HierarquiaProjeto;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.presenter.CadastroMetaPresenter;
import com.saax.gestorweb.util.DBConnect;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.PostgresConnection;
import com.saax.gestorweb.view.CadastroMetaView;
import com.vaadin.ui.UI;
import java.util.List;
import java.util.ResourceBundle;
import javax.persistence.EntityManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author rodrigo
 */
public class CadastroMetaTest {

    private static ResourceBundle mensagens;
    private static Usuario usuarioLogado;
    private CadastroMetaView view;
    private CadastroMetaModel model;
    private CadastroMetaPresenter presenter;
    private EntityManager em;
    

    @BeforeClass
    public static void setUpClass() {

        // connect to database
        DBConnect.getInstance().assertConnection();
        EntityManager em = PostgresConnection.getInstance().getEntityManagerFactory().createEntityManager();
        GestorEntityManagerProvider.setCurrentEntityManager(em);

        // set logged user
        usuarioLogado = (Usuario) em.createNamedQuery("Usuario.findByLogin").setParameter("login", "teste-user@gmail.com").getSingleResult();
        GestorSession.setAttribute("usuarioLogado", usuarioLogado);
        usuarioLogado.setEmpresaAtiva(new LoginModel().getEmpresaUsuarioLogado());

        // creates UI
        GestorMDI gestor = new GestorMDI();
        UI.setCurrent(gestor);
        gestor.init(null);

        mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();

    }

    @AfterClass
    public static void tearDownClass() {

    }

    @Before
    public void setUp() {

        view = new CadastroMetaView();
        model = new CadastroMetaModel();
        presenter = new CadastroMetaPresenter(model, view);

        em = GestorEntityManagerProvider.getEntityManager();
    }

    @After
    public void tearDown() {

    }
    
    
    
    @Test
    public void criarNovaMetaSimples(){
        
        HierarquiaProjeto h = (HierarquiaProjeto) em.createNamedQuery("HierarquiaProjeto.findByNome")
                .setParameter("nome", "Projeto")
                .getSingleResult();
        
        HierarquiaProjetoDetalhe categoria = null;
        for (HierarquiaProjetoDetalhe c : h.getCategorias()) {
            if (c.getNivel()==1){
                categoria = c;
            }
        }

        
        presenter.criarNovaMeta(categoria);
        
        Assert.assertEquals(2, view.getEmpresaCombo().getItemIds().size());
        
        view.getEmpresaCombo().setValue(usuarioLogado.getEmpresaAtiva());
        
        List<Departamento> departamentos = em.createNamedQuery("Departamento.findByEmpresaAtivo")
                .setParameter("empresa", usuarioLogado.getEmpresaAtiva())
                .getResultList();
        
        Assert.assertArrayEquals(departamentos.toArray(), view.getDepartamentoCombo().getItemIds().toArray());
        
        
        
    }

}
