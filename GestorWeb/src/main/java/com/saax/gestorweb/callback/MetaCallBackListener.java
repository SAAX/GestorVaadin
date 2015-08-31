package com.saax.gestorweb.callback;

import com.saax.gestorweb.model.datamodel.Meta;

/**
 *
 * @author rodrigo
 */
public interface MetaCallBackListener {

    void metaCriada(Meta metaCriada);

    public void metaAlterada(Meta meta);
}
