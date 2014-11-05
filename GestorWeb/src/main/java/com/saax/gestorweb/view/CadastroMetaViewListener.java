package com.saax.gestorweb.view;

import com.saax.gestorweb.model.datamodel.Empresa;

/**
 * Interface com todos os eventos disparados pelo painel administrativo
 * 
 * @author Rodrigo
 */
public interface CadastroMetaViewListener {

    public void empresaSelecionada(Empresa empresa);
    
}
