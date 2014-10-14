/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.view;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.util.GestorWebImagens;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
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
    // Este acesso se dÃƒÆ’Ã‚Â¡ por uma interface para manter a abstraÃƒÆ’Ã‚Â§ÃƒÆ’Ã‚Â£o das camadas
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
     * Cria o pop-up de login, com campos para usuÃƒÆ’Ã‚Â¡rio e senha
     *
     */
    public ChatView() {
        super();

        setCaption(mensagens.getString("ChatView.titulo"));
        setModal(true);
        setWidth(300, Unit.PIXELS);
        setHeight(600, Unit.PIXELS);

        // Container que armazena os elementos visuais (campos de login e senha)       
        VerticalLayout container = new VerticalLayout();
        container.setMargin(true);
        setContent(container);
        
        VerticalLayout tabela = containerTabelaUsuarios();
      
        
        //monta a grid para armazenar a tabela dos funcionÃ¡rios e as textAreas
        GridLayout layout = new GridLayout(2, 2);
        layout.setMargin(false);
        layout.setSpacing(false);
        layout.setWidth("300px");
        layout.setHeight("600px");

        layout.addComponent(tabela, 0, 0);
        
                
        
        container.addComponent(layout);
        
              
        // barra dos botoes
        HorizontalLayout barraBotoes = buildBarraBotoes();
        container.addComponent(barraBotoes);
        container.setComponentAlignment(barraBotoes, Alignment.MIDDLE_CENTER);
                       
        center();
    }
    
    /**
     * Cria e retorna a barra de botoes
     * @return barra com o botÃƒÂ£o Fechar
     */
    private HorizontalLayout buildBarraBotoes(){
        
        HorizontalLayout barraBotoes = new HorizontalLayout();
        
           
                    
        // botão para cancelar
        final Button cancelButton = new Button(mensagens.getString("SignupView.cancelButton.label"), (Button.ClickEvent event) -> {
            getListener().cancelButtonClicked();
        });

       
        barraBotoes.addComponent(cancelButton);
        
        return barraBotoes;
    } 
    
    
    private VerticalLayout containerTabelaUsuarios(){
        
        VerticalLayout usuarios = new VerticalLayout();
        
        containerTabelaUsuarios = new Panel();
        containerTabelaUsuarios.setWidth("300px");

        usuariosTable = new Table();
        containerTabelaUsuarios.setContent(usuariosTable);
        usuariosTable.setSizeFull();

        usuariosTable.addContainerProperty("", String.class, null);
        usuariosTable.addContainerProperty("Usuario", String.class, null);
        usuariosTable.addContainerProperty("Ativo", String.class, null);
        getUsuariosTable().addItem(new Object[]{"X", "Fernando Stávale", "On-Line"}, "Fernando");
        getUsuariosTable().addItem(new Object[]{"X", "Daniel Stávale", "Off-Line"}, "Daniel");
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

