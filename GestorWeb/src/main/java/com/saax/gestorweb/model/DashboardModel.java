package com.saax.gestorweb.model;

import com.saax.gestorweb.dao.MetaJpaController;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.util.PostgresConnection;
import java.util.List;

/**
 * Classe de negócios do Dasboard
 * @author Rodrigo
 */
public class DashboardModel {

    public List<Meta> getMetas() {
        
        MetaJpaController dao = new MetaJpaController(PostgresConnection.getInstance().getEntityManagerFactory());
        
        
        return dao.findMetaEntities();
    }
    
}
