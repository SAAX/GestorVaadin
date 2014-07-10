/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.dao;

import com.saax.gestorweb.dao.exceptions.NonexistentEntityException;
import com.saax.gestorweb.model.datamodel.CentroCusto;
import com.saax.gestorweb.util.DBConnect;
import com.saax.gestorweb.util.PostgresConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rodrigo
 */
public class CentroCustoDAOTest {

    public CentroCustoDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        DBConnect.getInstance().assertConnection();
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getEntityManager method, of class CentroCustoDAO.
     */
    @Test
    public void testGetEntityManager() {
        System.out.println("getEntityManager");
        CentroCustoDAO instance = new CentroCustoDAO(PostgresConnection.getInstance().getEntityManagerFactory());
        EntityManager result = instance.getEntityManager();
        assertNotNull(result);
    }

    /**
     * Test of create method, of class CentroCustoDAO.
     */
    @Test
    public void testCreate() {
        System.out.println("create");

        // Teste #1 - sucesso
        CentroCusto centroCusto = new CentroCusto();
        centroCusto.setCentroCusto("NomeCC");

        CentroCustoDAO instance = new CentroCustoDAO(PostgresConnection.getInstance().getEntityManagerFactory());
        instance.create(centroCusto);

        assertEquals(instance.findCentroCusto(centroCusto.getId()), centroCusto);

        try {
            instance.destroy(centroCusto.getId());
        } catch (NonexistentEntityException ex) {
            fail("Exceção não esperada");
        }

        // Teste #2 - falha
        try {
            instance.create(null);
            fail("Exceção esperada");
        } catch (Exception ex) {
        }

    }

    /**
     * Test of edit method, of class CentroCustoDAO.
     */
    @Test
    public void testEdit() throws Exception {

        System.out.println("edit");

        // Teste #1 - sucesso
        CentroCusto centroCusto = new CentroCusto();
        centroCusto.setCentroCusto("NomeCC");

        CentroCustoDAO instance = new CentroCustoDAO(PostgresConnection.getInstance().getEntityManagerFactory());
        instance.create(centroCusto);

        String expResult = "Nome CC 2";
        centroCusto.setCentroCusto(expResult);
        instance.edit(centroCusto);

        String result = instance.findCentroCusto(centroCusto.getId()).getCentroCusto();
        assertEquals(expResult, result);

        try {
            instance.destroy(centroCusto.getId());
        } catch (NonexistentEntityException ex) {
            fail("Exceção não esperada");
        }

        // Teste #2 - falha
        try {
            instance.edit(new CentroCusto());
            fail("Exceção esperada");
        } catch (Exception ex) {
        }

    }

    /**
     * Test of destroy method, of class CentroCustoDAO.
     */
    @Test
    public void testDestroy() throws Exception {
        System.out.println("destroy");

        CentroCusto centroCusto = new CentroCusto();
        centroCusto.setCentroCusto("NomeCC");

        CentroCustoDAO instance = new CentroCustoDAO(PostgresConnection.getInstance().getEntityManagerFactory());
        instance.create(centroCusto);

        try {
            instance.destroy(centroCusto.getId());

        } catch (NonexistentEntityException ex) {
            fail("Exceção não esperada");
        }

        // tenta remover novamente o mesmo elemento
        // deve lançar exceção
        try {
            instance.destroy(centroCusto.getId());
            fail("Exceção esperada");
        } catch (NonexistentEntityException ex) {
        }

    }

    /**
     * Test of findCentroCustoEntities method, of class CentroCustoDAO.
     */
    @Test
    public void testFindCentroCustoEntities_0args() {
        System.out.println("findCentroCustoEntities");

        List<CentroCusto> expResult = new ArrayList<>();

        expResult.add(new CentroCusto());
        expResult.add(new CentroCusto());
        expResult.add(new CentroCusto());
        expResult.add(new CentroCusto());
        expResult.add(new CentroCusto());

        CentroCustoDAO instance = new CentroCustoDAO(PostgresConnection.getInstance().getEntityManagerFactory());

        // apaga tudo
        instance.findCentroCustoEntities().stream().forEach((centroCusto) -> {
            try {
                instance.destroy(centroCusto.getId());
            } catch (NonexistentEntityException ex) {
                fail("Exceção não esperada");
            }
        });

        expResult.stream().forEach((centroCusto) -> {
            instance.create(centroCusto);
        });

        List<CentroCusto> result = instance.findCentroCustoEntities();

        assertArrayEquals(expResult.toArray(), result.toArray());

        try {

            for (CentroCusto centroCusto : result) {
                instance.destroy(centroCusto.getId());

            }

        } catch (NonexistentEntityException ex) {
            fail("Exceção não esperada");
        }

    }

    /**
     * Test of findCentroCustoEntities method, of class CentroCustoDAO.
     */
    @Test
    public void testFindCentroCustoEntities_int_int() {
        System.out.println("findCentroCustoEntities");

        int maxResults = 2;
        int firstResult = 1;

        CentroCustoDAO instance = new CentroCustoDAO(PostgresConnection.getInstance().getEntityManagerFactory());

        // apaga tudo
        instance.findCentroCustoEntities().stream().forEach((centroCusto) -> {
            try {
                instance.destroy(centroCusto.getId());
            } catch (NonexistentEntityException ex) {
                fail("Exceção não esperada");
            }
        });

        // apaga tudo
        instance.findCentroCustoEntities().stream().forEach((centroCusto) -> {
            try {
                instance.destroy(centroCusto.getId());
            } catch (NonexistentEntityException ex) {
                fail("Exceção não esperada");
            }
        });

        List<CentroCusto> allElements = new ArrayList<>();
        allElements.add(new CentroCusto());
        allElements.add(new CentroCusto());
        allElements.add(new CentroCusto());
        allElements.add(new CentroCusto());
        allElements.add(new CentroCusto());

        allElements.stream().forEach((centroCusto) -> {
            instance.create(centroCusto);
        });

        CentroCusto[] expResult = new CentroCusto[maxResults];
        expResult[0] = allElements.get(1);
        expResult[1] = allElements.get(2);

        List<CentroCusto> result = instance.findCentroCustoEntities(maxResults, firstResult);

        assertArrayEquals(expResult, result.toArray());

        try {

            for (CentroCusto centroCusto : allElements) {
                instance.destroy(centroCusto.getId());

            }

        } catch (NonexistentEntityException ex) {
            fail("Exceção não esperada");
        }

    }

    /**
     * Test of findCentroCusto method, of class CentroCustoDAO.
     */
    @Test
    public void testFindCentroCusto() {
        System.out.println("findCentroCusto");

        CentroCusto expResult = new CentroCusto();
        expResult.setCentroCusto("NomeCC");

        CentroCustoDAO instance = new CentroCustoDAO(PostgresConnection.getInstance().getEntityManagerFactory());
        instance.create(expResult);

        CentroCusto result = instance.findCentroCusto(expResult.getId());

        assertEquals(expResult, result);

        try {
            instance.destroy(result.getId());

        } catch (NonexistentEntityException ex) {
            fail("Exceção não esperada");
        }

    }

    /**
     * Test of getCentroCustoCount method, of class CentroCustoDAO.
     */
    @Test
    public void testGetCentroCustoCount() {
        System.out.println("getCentroCustoCount");

        CentroCustoDAO instance = new CentroCustoDAO(PostgresConnection.getInstance().getEntityManagerFactory());

        // apaga tudo
        try {
            for (CentroCusto centroCusto : instance.findCentroCustoEntities()) {
                instance.destroy(centroCusto.getId());
            }
        } catch (NonexistentEntityException ex) {
            fail("Exceção não esperada");
        }

        int expResult = 5;

        instance.create(new CentroCusto());
        instance.create(new CentroCusto());
        instance.create(new CentroCusto());
        instance.create(new CentroCusto());
        instance.create(new CentroCusto());

        int result = instance.getCentroCustoCount();
        assertEquals(expResult, result);

    }

}