package com.saax.gestorweb.util;

import com.vaadin.server.ClassResource;
import com.vaadin.ui.Image;
import java.io.Serializable;

/**
 * Esta classe é responsável por reunir em um só lugar e armazenar todas as imagens de recursos do sistema
 * com o intuito de evitar recursos espalhados
 * @author Rodrigo
 */
public class GestorWebImagens implements Serializable {
    
    // listagem com todas as imagens disponíveis na app
    
    private final Image PAGINAINICIAL_LOGO;

    // metodos para encapsular acesso as imagens
    
    public Image getPAGINAINICIAL_LOGO() {
        return PAGINAINICIAL_LOGO;
    }


    // carrega os recursos de imagem
    public GestorWebImagens() {
        PAGINAINICIAL_LOGO = new Image(null, new ClassResource("/imagens/profit_growth-512.png"));
        
    }
    
    
    
    
}
