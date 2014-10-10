/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.util.GestorWebImagens;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.ResourceBundle;

import org.vaadin.hene.popupbutton.PopupButton;

/**
 *
 * @author Fernando
 */
public class ChatView extends Window{
    
    // Referencia ao recurso das mensagens:
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();
    
    // A view mantem acesso ao listener (Presenter) para notificar os eventos
    // Este acesso se dÃƒÂ¡ por uma interface para manter a abstraÃƒÂ§ÃƒÂ£o das camadas
    private ChatViewListener listener;
    
    // Create the selection component
    private Panel containerTabelaUsuarios;
    private Table usuariosTable;
    
    private TextArea textoEnviar;
    private TextArea historico;
    
    public void setListener(ChatViewListener listener) {
        this.listener = listener;
    }
    
    
    /**
     * Cria o pop-up de login, com campos para usuÃƒÂ¡rio e senha
     *
     */
    public ChatView() {
        super();

        setCaption(mensagens.getString("ChatView.titulo"));
        setModal(true);
        setWidth(600, Unit.PIXELS);
        setHeight(600, Unit.PIXELS);

        // Container que armazena os elementos visuais (campos de login e senha)       
        VerticalLayout container = new VerticalLayout();
        container.setMargin(true);
        setContent(container);
        
        VerticalLayout tabela = containerTabelaUsuarios();
        VerticalLayout mensagens = mensagensChat();
        
        //monta a grid para armazenar a tabela dos funcionários e as textAreas
        GridLayout layout = new GridLayout(2, 2);
        layout.setMargin(false);
        layout.setSpacing(false);
        layout.setWidth("600px");
        layout.setHeight("600px");

        layout.addComponent(tabela, 0, 0);
        layout.addComponent(mensagens, 1, 0);
                
        
        container.addComponent(layout);
        
              
        // barra dos botoes
        HorizontalLayout barraBotoes = buildBarraBotoes();
        container.addComponent(barraBotoes);
        container.setComponentAlignment(barraBotoes, Alignment.MIDDLE_CENTER);
                       
        center();
    }
    
    /**
     * Cria e retorna a barra de botoes
     * @return barra com o botÃ£o Fechar
     */
    private HorizontalLayout buildBarraBotoes(){
        
        HorizontalLayout barraBotoes = new HorizontalLayout();
        
                    
        // botÃƒÂ£o para cancelar
        final Button cancelButton = new Button(mensagens.getString("SignupView.cancelButton.label"), (Button.ClickEvent event) -> {
            getListener().cancelButtonClicked();
        });

       
        barraBotoes.addComponent(cancelButton);
        
        return barraBotoes;
    } 
    
    
    private VerticalLayout containerTabelaUsuarios(){
        
        VerticalLayout usuarios = new VerticalLayout();
        
        containerTabelaUsuarios = new Panel();
        containerTabelaUsuarios.setSizeFull();

        usuariosTable = new Table();
        containerTabelaUsuarios.setContent(usuariosTable);
        usuariosTable.setSizeFull();

        usuariosTable.addContainerProperty("", String.class, null);
        usuariosTable.addContainerProperty("Usuario", String.class, null);
        usuariosTable.addContainerProperty("Ativo", String.class, null);
        usuariosTable.setImmediate(true);
        usuariosTable.setSelectable(true);
        
         
        
        
        usuarios.addComponent(containerTabelaUsuarios);
        
        return usuarios;
    }
    
    private VerticalLayout mensagensChat(){
        
        VerticalLayout mensagens = new VerticalLayout();
        
        historico = new TextArea();
        historico.setWordwrap(false);
        historico.setValue("Fernando diz: se Deus quiser logo aprendo o Vaadin!");
        historico.setSizeFull();
        
        textoEnviar = new TextArea();
        textoEnviar.setSizeFull();
        
        
        mensagens.addComponent(historico);
        mensagens.addComponent(textoEnviar);
        
        return mensagens;
    }
    
    /**
     * @return the listener
     */
    public ChatViewListener getListener() {
        return listener;
    }
    
    
}
