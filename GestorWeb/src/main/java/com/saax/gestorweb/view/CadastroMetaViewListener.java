package com.saax.gestorweb.view;

import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.HierarquiaProjetoDetalhe;
import com.saax.gestorweb.model.datamodel.Meta;

/**
 * Interface com todos os eventos disparados pelo painel administrativo
 * 
 * @author Rodrigo
 */
public interface CadastroMetaViewListener {

    public void empresaSelecionada(Empresa empresa);
    
    public void setCallBackListener(CadastroMetaCallBackListener callback);

    public void gravarButtonClicked();

    public void cancelarButtonClicked();
    
    public void edit(Meta metaToEdit);   

    public void hierarquiaSelecionada(HierarquiaProjetoDetalhe hierarquiaProjetoDetalhe);

    public void addTaskButtonClicked();

    public void chatButtonClicked();

    public void forecastButtonClickedd();
    
    
}
