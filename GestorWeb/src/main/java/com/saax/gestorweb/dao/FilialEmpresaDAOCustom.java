package com.saax.gestorweb.dao;

import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.FilialEmpresa;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Customização do DAO de Filial Empresa criado pelo netbeans <br>
 * Esta classe substitui o DAO principal.
 * 
 * @author rodrigo
 */
public class FilialEmpresaDAOCustom extends FilialEmpresaDAO {

    public FilialEmpresaDAOCustom(EntityManagerFactory emf) {
        super(emf);
    }
    
    /**
     * Busca uma filial pelo CNPJ
     * @param cnpj
     * @return filial
     */
    public FilialEmpresa findByCNPJ(String cnpj) {
        EntityManager em = getEntityManager();

        try {
            return (FilialEmpresa) em.createNamedQuery("FilialEmpresa.findByCnpj")
                    .setParameter("cnpj", cnpj)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }

    
}
