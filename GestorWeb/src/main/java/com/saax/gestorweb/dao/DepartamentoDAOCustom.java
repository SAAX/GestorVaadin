package com.saax.gestorweb.dao;

import com.saax.gestorweb.model.datamodel.Departamento;
import com.saax.gestorweb.model.datamodel.Empresa;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Customização do DAO de Departamentos criado pelo netbeans <br>
 * Esta classe substitui o DAO principal.
 * 
 * @author rodrigo
 */
public class DepartamentoDAOCustom extends DepartamentoDAO {

    public DepartamentoDAOCustom(EntityManagerFactory emf) {
        super(emf);
    }
    
    /**
     * Obtém a lista de departamentos cadastrados para a empresa
     * @param empresa
     * @return lista de departamentos
     */
    public List<Departamento> obterDepartamentosPorEmpresa(Empresa empresa) {
            EntityManager em = getEntityManager();

        try {
            return (List<Departamento>) em.createNamedQuery("Departamento.findByEmpresa")
                    .setParameter("empresa", empresa)
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<Departamento>();
        }

    }


    
}
