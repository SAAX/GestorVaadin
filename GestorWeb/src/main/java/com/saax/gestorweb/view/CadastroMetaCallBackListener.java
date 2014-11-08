package com.saax.gestorweb.view;

import com.saax.gestorweb.model.datamodel.Meta;

/**
 *
 * @author rodrigo
 */
public interface CadastroMetaCallBackListener {

    void cadastroMetaConcluido(Meta metaCriada);

    public void edicaoMetaConcluida(Meta meta);
}
