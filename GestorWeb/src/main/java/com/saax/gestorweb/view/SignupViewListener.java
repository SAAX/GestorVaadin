package com.saax.gestorweb.view;

/**
 * Interface com todos os eventos disparados pela janela de criação de conta
 *
 * @author rodrigo
 */
public interface SignupViewListener {

    public void cancelButtonClicked();
    public void incluirUsuario();
    public void incluirColigada(String nomeColigada, String cnpjColigada, String id);
    public void incluirFilial(String nomeFilial, String cnpjFilial, String id);
    public void okButtonClicked();
    public void personTypeSelected();

    public void estadoSelecionado();
    
    
}
