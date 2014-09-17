package com.saax.gestorweb.view;

/**
 * Interface com todos os eventos disparados pela janela de criação de conta
 *
 * @author rodrigo
 */
public interface SignupViewListener {

    public void cancelButtonClicked();
    public void incluirUsuario();
    public void incluirColigadas();
    public void incluirFiliais();
    public void okButtonClicked();

    public void estadoSelecionado();
    
}
