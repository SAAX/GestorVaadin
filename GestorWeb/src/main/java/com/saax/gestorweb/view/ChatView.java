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
import com.vaadin.ui.TextArea;
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
    
    // Referencia ao recurso das mensagens:
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();
    private final Usuario usuarioLogado;
    
    // A view mantem acesso ao listener (Presenter) para notificar os eventos
    // Este acesso se dÃƒÆ’Ã‚Â¡ por uma interface para manter a abstraÃƒÆ’Ã‚Â§ÃƒÆ’Ã‚Â£o das camadas
    private ChatViewListener listener;
    
    // Create the selection component
    private Panel containerTabelaUsuarios;
    private Table usuariosTable;
    
    private TextArea textoEnviar;
    private TextArea historico;
    
    private final HorizontalSplitPanel hsplit;
    
    

    
    public void setListener(ChatViewListener listener) {
        this.listener = listener;
    }
    
    
    /**
     * Cria o pop-up do Chat
     *
     */
    public ChatView() {
        super();
        
        setCaption(mensagens.getString("ChatView.titulo"));
        setModal(true);
        setWidth(800, Unit.PIXELS);
        setHeight(400, Unit.PIXELS);
        
        Panel panel = new Panel("Chat");
        
        VerticalLayout container = new VerticalLayout();
        container.setMargin(true);
        setContent(container);
        
        usuarioLogado = (Usuario) GestorSession.getAttribute("usuarioLogado");
        
        HorizontalLayout hlayout = new HorizontalLayout();
        
        // Have a horizontal split panel as its content
        hsplit = new HorizontalSplitPanel();
        hsplit.setWidth("800px");
        
        // Put a component in the left panel
        hsplit.setFirstComponent(containerTabelaUsuarios());
        hsplit.getFirstComponent().setWidth("300px");
        // A static variable so that everybody gets the same instance.
        
        panel.setContent(hsplit);
        hlayout.addComponent(panel);
        container.addComponent(hlayout);
        
    }

    public void configurarChat(Tarefa tarefa, SharedChat chat){
        
        ChatBox cb = new ChatBox(chat);
     
        ChatUser user = new ChatUser(ChatSingletonModel.getInstance().buildID(usuarioLogado, tarefa, false), usuarioLogado.getNome(), "user1");
        cb.setUser(user);
        cb.setWidth("500px");
                
        hsplit.setSecondComponent(cb);
        hsplit.getSecondComponent().setWidth("500px");
        
    }
       
    
    
    private VerticalLayout containerTabelaUsuarios(){
        
        VerticalLayout usuarios = new VerticalLayout();
        
        containerTabelaUsuarios = new Panel();
        containerTabelaUsuarios.setWidth("300px");

        usuariosTable = new Table();
        containerTabelaUsuarios.setContent(usuariosTable);
        

        usuariosTable.addContainerProperty("Usuario", String.class, null);
        usuariosTable.addContainerProperty("Função na Tarefa", String.class, null);
        
        usuariosTable.setImmediate(true);
        usuariosTable.setSelectable(true);
        
        usuarios.addComponent(containerTabelaUsuarios);
        
        return usuarios;
    }
    
       

    /**
     * @return the listener
     */
    public ChatViewListener getListener() {
        return listener;
    }
    
     /**
     * @return the usuariosTable
     */
    public Table getUsuariosTable() {
        return usuariosTable;
    }

    /**
     * @param usuariosTable the UsuariosTable to set
     */
    public void setUsuariosTable(Table usuariosTable) {
        this.usuariosTable = usuariosTable;
    }
    
}

