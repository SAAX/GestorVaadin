/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.ChatSingletonModel;
import com.saax.gestorweb.model.datamodel.AnexoTarefa;
import com.saax.gestorweb.model.datamodel.ParticipanteTarefa;
import com.saax.gestorweb.model.datamodel.Task;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.FormatterUtil;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.view.ChatView;
import com.saax.gestorweb.view.ChatViewListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * SignUP Presenter <br>
 * This class is responsible capture all events that occur in View <br>
 * and provide appropriate treatment, using the model
 *
 *
 * @author Rodrigo
 */
public class ChatPresenter implements Serializable, ChatViewListener {

    // Reference to the use of the messages:
    private final transient ResourceBundle messages = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final transient GestorWebImagens images = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();
    private final Usuario userLogged;

    // Every presenter keeps access to view and model
    private final transient ChatView view;
    private final transient ChatSingletonModel model;
    private Task task;

    /**
     * Creates the presenter linking the Model View
     *
     * @param model
     * @param view
     */
    public ChatPresenter(ChatSingletonModel model, ChatView view) {

        this.model = model;
        this.view = view;
        view.setListener(this);
        userLogged = (Usuario) GestorSession.getAttribute("loggedUser");
    }

    public void open(Task task) {

        view.chatConfigure(task, ChatSingletonModel.getInstance().getChat(task));
        ChatSingletonModel.getInstance().getChat(task).addListener(ChatSingletonModel.getInstance());
        loadingTable(task);
        this.task = task;

        for (int i = 0; i < task.getAnexos().size(); i++) {
            view.getAttachmentsAddedTable().addItem(task.getAnexos().get(i).getId());
            view.getAttachmentsAddedTable().getContainerProperty(task.getAnexos().get(i).getId(), "Arquivo:").setValue(task.getAnexos().get(i).getNome());
            view.getAttachmentsAddedTable().getContainerProperty(task.getAnexos().get(i).getId(), "Enviado em:").setValue(task.getAnexos().get(i).getUsuarioInclusao().getNome() + " às " + (FormatterUtil.formatDateTime(task.getAnexos().get(i).getDataHoraInclusao())));
            view.getAttachmentsAddedTable().getContainerProperty(task.getAnexos().get(i).getId(), "Download:").setValue(buildButtonDownload(task.getAnexos().get(i)));
        }
        
            
            
    }
    
    private Button buildButtonDownload(AnexoTarefa anexos) {
        Button exportar = new Button();
        exportar.setIcon(FontAwesome.DOWNLOAD);
        exportar.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        exportar.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        FileDownloader fd = new FileDownloader(new FileResource(new File(anexos.getCaminhoCompleto())));
        fd.extend(exportar);
        //Notification.show(("Download realizado com sucesso!"), Notification.Type.TRAY_NOTIFICATION);
        return exportar;
        
    }

    /**
     * Carries the information to fill the Users table
     */
    public void loadingTable(Task task) {                                              
        view.getUserTable().addItem(new Object[]{task.getUsuarioSolicitante().getNome(), messages.getString("ChatPresenter.solicitante")}, messages.getString("ChatPresenter.solicitante"));
        if (task.getUsuarioResponsavel()!=null){
            view.getUserTable().addItem(new Object[]{task.getUsuarioResponsavel().getNome(), messages.getString("ChatPresenter.responsavel")}, messages.getString("ChatPresenter.responsavel"));
        }
        
        List<ParticipanteTarefa> participants = task.getParticipantes();
        
        for (int i = 0; i < participants.size(); i++) {
            view.getUserTable().addItem(new Object[]{participants.get(i).getUsuarioParticipante().getNome(), messages.getString("ChatPresenter.participante")}, messages.getString("ChatPresenter.participante"));
        }
        

    }

    @Override
    public void cancelButtonClicked() {
        ((GestorMDI) UI.getCurrent()).logout();
    }
    

}
