/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.ChatSingletonModel;
import com.saax.gestorweb.model.datamodel.AnexoTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.util.SessionAttributesEnum;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
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
    
    private final transient GestorWebImagens images = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();
    private final Usuario userLogged;
    
    // The view maintains access to the listener (Presenter) to notify events
    private ChatViewListener listener;
    
    // Create the selection component
    private Panel containerUserTable;
    private Table userTable;
    private Table attachmentsAddedTable;
    private BeanItemContainer<AnexoTarefa> taskAttachContainer;
    
    private Accordion accordion;
    private final HorizontalSplitPanel hsplit;
    private HorizontalLayout uploadHorizontalLayout;
    
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
        setWidth("80%");
        setHeight("80%");
        setResizable(false);
        
        VerticalLayout container = new VerticalLayout();
        container.setMargin(true);
        container.setWidth("100%");
        container.setHeight("100%");
        setContent(container);
        
        userLogged = (Usuario) GestorSession.getAttribute(SessionAttributesEnum.USUARIO_LOGADO.getAttributeName());
        
        HorizontalLayout hlayout = new HorizontalLayout();
        
        // Have a horizontal split panel as its content
        hsplit = new HorizontalSplitPanel();
        hsplit.setSizeFull();
        // Put a component in the left panel
        hsplit.setFirstComponent(containerUserTable());
        hsplit.getFirstComponent().setWidth("100%");
        
        // A static variable so that everybody gets the same instance.
        
        //accordion
        container.addComponent(hsplit);
        accordion = new Accordion();
        accordion.setWidth("100%");
        accordion.addTab(buildAttachTable(), "Anexos", null);
        container.addComponent(accordion);
        //container.addComponent(buildAttachTable());
        
    }

    //Cria um novo ChatBox e um novo ChatUser
    public void chatConfigure(Tarefa task, SharedChat chat){
        
        ChatBox cb = new ChatBox(chat);
        ChatUser user = new ChatUser(ChatSingletonModel.getInstance().buildID(userLogged, task, false), userLogged.getNome(), "user1");
        cb.setUser(user);
        cb.setSizeFull();
        hsplit.setSecondComponent(cb);
        hsplit.getSecondComponent().setWidth("100%");
        
    }
       
    //Cria tabela com os arquivos anexos nesta conversa
    private Table buildAttachTable(){
        
        attachmentsAddedTable = new Table();
        
        attachmentsAddedTable.addContainerProperty("Arquivo:", String.class, null);
        attachmentsAddedTable.addContainerProperty("Enviado em:", String.class, null);
        attachmentsAddedTable.addContainerProperty("Download:", Button.class, null);
        
        attachmentsAddedTable.setColumnWidth("Arquivo:", 700);
        attachmentsAddedTable.setColumnWidth("Enviado em:", 300);
        attachmentsAddedTable.setColumnWidth("Download:", 80);
        
        attachmentsAddedTable.setWidth("100%");
        
        return attachmentsAddedTable;
    }
    
    //Cria camada vertical com os usuários disponíveis no chat
    private VerticalLayout containerUserTable(){
        
        VerticalLayout users = new VerticalLayout();
        users.setSizeFull();
        
        userTable = new Table();
        userTable.setWidth("100%");

        userTable.addContainerProperty(messages.getString("ChatView.usuario"), String.class, null);
        userTable.addContainerProperty(messages.getString("ChatView.funcao"), String.class, null);
        
        userTable.setImmediate(true);
        userTable.setSelectable(true);
        
        users.addComponent(userTable);
        
        uploadHorizontalLayout = new HorizontalLayout();
        uploadHorizontalLayout.setWidth("100%");

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
    
    public BeanItemContainer<AnexoTarefa> getTaskAttachContainer() {
        return taskAttachContainer;
    }

    public Table getAttachmentsAddedTable() {
        return attachmentsAddedTable;
    }

    public void setAttachmentsAddedTable(Table attachmentsAddedTable) {
        this.attachmentsAddedTable = attachmentsAddedTable;
    }
    
}

