package com.saax.gestorweb.dao;

import com.saax.gestorweb.model.datamodel.Empresa;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Customização do DAO de Empresa criado pelo netbeans <br>
 * Esta classe substitui o DAO principal.
 * 
 * @author rodrigo
 */
public class EmpresaDAOCustom extends EmpresaDAO {

    public EmpresaDAOCustom(EntityManagerFactory emf) {
        super(emf);
    }
    
    /**
     * Busca uma empresa pelo CNPJ
     * @param cnpj
     * @return empresa 
     */
    public Empresa findByCNPJ(String cnpj) {
        EntityManager em = getEntityManager();

        try {
            return (Empresa) em.createNamedQuery("Empresa.findByCnpj")
                    .setParameter("cnpj", cnpj)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * Busca uma empresa pelo CPF
     * @param cpf
     * @return empresa 
     */
    public Empresa findByCPF(String cpf) {
        EntityManager em = getEntityManager();

        try {
            return (Empresa) em.createNamedQuery("Empresa.findByCpf")
                    .setParameter("cpf", cpf)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }


    
}
