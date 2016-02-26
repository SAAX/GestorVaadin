package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Rodrigo
 */
public class GestorModel {

    /**
     * Obtém a lista de categorias de meta possíveis para uma empresa.  <br>
     * A lista inclui as categorias da empresa selecionada além das categorias padrão
     * @param empresa
     * @return
     */
    public static List<HierarquiaProjetoDetalhe> getCategoriasPorNivel(Empresa empresa, int nivel) {
        EntityManager em = GestorEntityManagerProvider.getEntityManager();
        List<HierarquiaProjetoDetalhe> categoriasList = em.createNamedQuery("HierarquiaProjetoDetalhe.findByNivel").setParameter("nivel", nivel).getResultList();
        return categoriasList;
    }
    
}
