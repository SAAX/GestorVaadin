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
    
    public void gravarButtonClicked();

    public void cancelarButtonClicked();
    
    public void editarMeta(Meta metaToEdit);   

    public void addTaskButtonClicked();

//    public void forecastButtonClickedd();

    public void adicionarParticipante(Usuario usuario);

    public void removerParticipante(Participante participante);

    public void removerMetaButtonClicked(Meta meta);
    
    
}
