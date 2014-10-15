/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.ChatModel;
import com.saax.gestorweb.model.SignupModel;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.view.ChatView;
import com.saax.gestorweb.view.ChatViewListener;
import com.saax.gestorweb.view.SignupView;
import com.saax.gestorweb.view.SignupViewListener;
import com.vaadin.ui.UI;
import java.util.ResourceBundle;

/**
 * SignUP Presenter <br>
 * Esta classe é responsável captar todos os eventos que ocorrem na View <br>
 * e dar o devido tratamento, utilizando para isto o modelo
 *
 *
 * @author Rodrigo
 */
public class ChatPresenter implements ChatViewListener {

    // Referencia ao recurso das mensagens:
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();

    // Todo presenter mantem acesso à view e ao model
    private final ChatView view;
    private final ChatModel model;

    /**
     * Cria o presenter ligando o Model ao View
     *
     * @param model
     * @param view
     */
    public ChatPresenter(ChatModel model, ChatView view) {

        this.model = model;
        this.view = view;

        view.setListener(this);

    }
    public void open() {
     

    }
    
    
    
    @Override
    public void cancelButtonClicked() {
        ((GestorMDI) UI.getCurrent()).logout();
    }
    
     @Override
    public void mensagemButtonClicked() {
        
        //Cria o pop up para registrar a conta (model e viw)
        ChatModel chatModel = new ChatModel();
        ChatView chatView = new ChatView();
        
       //o presenter liga model e view
        ChatPresenter chatPresenter;
        chatPresenter = new ChatPresenter(chatModel, chatView);
        //adiciona a visualização à UI
        UI.getCurrent().addWindow(chatView);
        chatPresenter.open();
    }
}