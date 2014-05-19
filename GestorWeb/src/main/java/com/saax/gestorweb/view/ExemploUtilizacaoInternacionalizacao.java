package com.saax.gestorweb.view;

import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.ResourceBundle;

/**
 * Este é apenas um exemplo do uso da internacionalização das mensagens
 * esta classe não deve constar no software final
 * @author Rodrigo
 */
public class ExemploUtilizacaoInternacionalizacao extends VerticalLayout {
    
    // Manter uma referencia ao recurso das mensagens:
    ResourceBundle mensagens = ((GestorMDI)UI.getCurrent()).getMensagens();
    
    public ExemploUtilizacaoInternacionalizacao(){
     
        // Acessar através do método getString
        addComponent(new Label(mensagens.getString("GestorMDI.titulo")));
    }
            
    
    
}
