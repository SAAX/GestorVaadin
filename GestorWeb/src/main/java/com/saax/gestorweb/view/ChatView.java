/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.ChatSingletonModel;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.GestorWebImagens;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.ResourceBundle;
import org.vaadin.chatbox.ChatBox;
import org.vaadin.chatbox.SharedChat;
import org.vaadin.chatbox.client.ChatUser;


/**
 *
 * @author Fernando
 */
public class ChatView extends Window{
    
    // Reference to feature messages
    private final transient ResourceBundle messages = ((GestorMDI) UI.getCurrent()).getMensagens();
    
    private final GestorWebImagens images = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();
    private final Usuario userLogged;
    
    // The view maintains access to the listener (Presenter) to notify events
    private ChatViewListener listener;
    
    // Create the selection component
    private Panel containerUserTable;
    private Table userTable;
    
    private final HorizontalSplitPanel hsplit;
    
    public void setListener(ChatViewListener listener) {
        this.listener = listener;
    }
    
    
    /**
     * Chat Pop-Up
     *
     */
    public ChatView() {
        super();
        
        setCaption(messages.getString("ChatView.titulo"));
        setModal(true);
        setWidth("70%");
        setHeight("60%");
        setResizable(false);
        
        VerticalLayout container = new VerticalLayout();
        container.setMargin(true);
        container.setWidth("100%");
        container.setHeight("100%");
        setContent(container);
        
        userLogged = (Usuario) GestorSession.getAttribute("usuarioLogado");
        
        HorizontalLayout hlayout = new HorizontalLayout();
        
        // Have a horizontal split panel as its content
        hsplit = new HorizontalSplitPanel();
        hsplit.setSizeFull();
        // Put a component in the left panel
        hsplit.setFirstComponent(containerUserTable());
        hsplit.getFirstComponent().setWidth("100%");
        // A static variable so that everybody gets the same instance.
        
        container.addComponent(hsplit);
        
    }

    public void chatConfigure(Tarefa task, SharedChat chat){
        
        ChatBox cb = new ChatBox(chat);
     
        ChatUser user = new ChatUser(ChatSingletonModel.getInstance().buildID(userLogged, task, false), userLogged.getNome(), "user1");
        cb.setUser(user);
        cb.setSizeFull();
                
        hsplit.setSecondComponent(cb);
        hsplit.getSecondComponent().setWidth("100%");
        
    }
       
    
    
    private VerticalLayout containerUserTable(){
        
        VerticalLayout users = new VerticalLayout();
        
        containerUserTable = new Panel();
        containerUserTable.setWidth("100%");

        userTable = new Table();
        containerUserTable.setContent(userTable);
        

        userTable.addContainerProperty(messages.getString("ChatView.usuario"), String.class, null);
        userTable.addContainerProperty(messages.getString("ChatView.funcao"), String.class, null);
        
        userTable.setImmediate(true);
        userTable.setSelectable(true);
        
        users.addComponent(containerUserTable);
        
        return users;
    }
    
    /**
     * @return the listener
     */
    public ChatViewListener getListener() {
        return listener;
    }

    public Panel getContainerUserTable() {
        return containerUserTable;
    }

    public void setContainerUserTable(Panel containerUserTable) {
        this.containerUserTable = containerUserTable;
    }

    public Table getUserTable() {
        return userTable;
    }

    public void setUserTable(Table userTable) {
        this.userTable = userTable;
    }
    
    
}

