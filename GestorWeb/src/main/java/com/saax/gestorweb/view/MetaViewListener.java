package com.saax.gestorweb.view;

import com.saax.gestorweb.model.datamodel.Empresa;
import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.Participante;
import com.saax.gestorweb.model.datamodel.Usuario;

/**
 * Interface com todos os eventos disparados pelo painel administrativo
 * 
 * @author Rodrigo
 */
public interface MetaViewListener {

    public void empresaSelecionada(Empresa empresa);
    
    public void setCallBackListener(CadastroMetaCallBackListener callback);

    public void gravarButtonClicked();

    public void cancelarButtonClicked();
    
    public void edit(Meta metaToEdit);   

    public void addTaskButtonClicked();

    public void chatButtonClicked();

//    public void forecastButtonClickedd();

    public void adicionarParticipante(Usuario usuario);

    public void removerParticipante(Participante participante);
    
    
}
