/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.ChatSingletonModel;
import com.saax.gestorweb.model.datamodel.AnexoTarefa;
import com.saax.gestorweb.model.datamodel.Participante;
import com.saax.gestorweb.model.datamodel.Tarefa;
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
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import java.io.Serializable;
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
    private Tarefa task;

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

    public void open(Tarefa task) {

        view.chatConfigure(task, ChatSingletonModel.getInstance().getChat(task));
        ChatSingletonModel.getInstance().getChat(task).addListener(ChatSingletonModel.getInstance());
        loadingTable(task);
        this.task = task;

        Table attachmentsTable = view.getAttachmentsAddedTable();
        List<AnexoTarefa> anexosTarefa = task.getAnexos();
        for (AnexoTarefa anexo : anexosTarefa) {

            attachmentsTable.addItem(anexo);
            attachmentsTable.getContainerProperty(anexo, "Arquivo:").setValue(anexo.getNome());
            attachmentsTable.getContainerProperty(anexo, "Enviado em:").setValue(anexo.getUsuarioInclusao().getNome() + " às " + (FormatterUtil.formatDateTime(anexo.getDataHoraInclusao())));
            attachmentsTable.getContainerProperty(anexo, "Download:").setValue(buildButtonDownload(anexo));
        }

    }

    /**
     * Cria botao para download de anexo
     */
    private Button buildButtonDownload(AnexoTarefa anexos) {
        Button exportar = new Button();
        exportar.setIcon(FontAwesome.DOWNLOAD);
        exportar.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        exportar.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        FileDownloader fd = new FileDownloader(new FileResource(new File(anexos.getCaminhoCompleto())));
        fd.extend(exportar);
        return exportar;

    }

    /**
     * Carries the information to fill the Users table
     */
    public void loadingTable(Tarefa task) {
        view.getUserTable().addItem(new Object[]{task.getUsuarioSolicitante().getNome(), messages.getString("ChatPresenter.solicitante")}, messages.getString("ChatPresenter.solicitante"));
        if (task.getUsuarioResponsavel() != null) {
            view.getUserTable().addItem(new Object[]{task.getUsuarioResponsavel().getNome(), messages.getString("ChatPresenter.responsavel")}, messages.getString("ChatPresenter.responsavel"));
        }

        List<Participante> participants = task.getParticipantes();

        for (int i = 0; i < participants.size(); i++) {
            view.getUserTable().addItem(new Object[]{participants.get(i).getUsuarioParticipante().getNome(), messages.getString("ChatPresenter.participante")}, messages.getString("ChatPresenter.participante"));
        }

    }

    @Override
    public void cancelButtonClicked() {
        ((GestorMDI) UI.getCurrent()).logout();
    }

}
