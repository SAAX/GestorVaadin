/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb;

import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.presenter.PresenterUtils;
import com.saax.gestorweb.util.TestUtils;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 *
 * @author rodrigo
 */
public class PresenterUtilsTest {
 
    @BeforeClass
    public static void setUpClass() {

        TestUtils.connectDB();


        TestUtils.createGestorMDI();
        

    }

    @AfterClass
    public static void tearDownClass() {
        
    }
    
    public void getUsuarioLogadoTest(){

        Usuario rodrigo = TestUtils.getUsuarioRodrigo();
        TestUtils.setUsuarioLogado(rodrigo);
        
        Assert.assertEquals(rodrigo,PresenterUtils.getInstance().getUsuarioLogado());
        
        
    }
}
