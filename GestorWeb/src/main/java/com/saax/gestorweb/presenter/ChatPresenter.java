/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saax.gestorweb.presenter;

import com.saax.gestorweb.GestorMDI;
import com.saax.gestorweb.model.ChatModel;
import com.saax.gestorweb.model.datamodel.ChatTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.FormatterUtil;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import com.saax.gestorweb.util.GestorSession;
import com.saax.gestorweb.util.GestorWebImagens;
import com.saax.gestorweb.view.ChatView;
import com.saax.gestorweb.view.ChatViewListener;
import com.vaadin.ui.UI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import javax.persistence.EntityManager;
import org.vaadin.chatbox.SharedChat;
import org.vaadin.chatbox.SharedChat.ChatListener;
import org.vaadin.chatbox.client.ChatLine;
import org.vaadin.chatbox.client.ChatUser;

/**
 * SignUP Presenter <br>
 * Esta classe é responsável captar todos os eventos que ocorrem na View <br>
 * e dar o devido tratamento, utilizando para isto o modelo
 *
 *
 * @author Rodrigo
 */
public class ChatPresenter implements ChatViewListener, ChatListener {

    // Referencia ao recurso das mensagens:
    private final transient ResourceBundle mensagens = ((GestorMDI) UI.getCurrent()).getMensagens();
    private final GestorWebImagens imagens = ((GestorMDI) UI.getCurrent()).getGestorWebImagens();
    private final Usuario usuarioLogado;

    // Todo presenter mantem acesso à view e ao model
    private final ChatView view;
    private final ChatModel model;
    private Tarefa tarefa;

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
        usuarioLogado = (Usuario) GestorSession.getAttribute("usuarioLogado");
    }

    public void open(Tarefa tarefa) {

        carregarTabela(tarefa);
        carregarHistorico(tarefa, view.chat);
        this.tarefa = tarefa;

    }

    /**
     * Carrega as informações para preenchimento da tabela de Usuários
     */
    public void carregarTabela(Tarefa tarefa) {
        view.getUsuariosTable().addItem(new Object[]{tarefa.getUsuarioSolicitante().getNome(), "Solicitante"}, "Solicitante");
        view.getUsuariosTable().addItem(new Object[]{tarefa.getUsuarioResponsavel().getNome(), "Responsável"}, "Responsável");

    }

    /**
     * Carrega as informações para preenchimento do histórico
     */
    public void carregarHistorico(Tarefa tarefa, SharedChat chat) {

        view.chat.removeListener(this);

        EntityManager em = GestorEntityManagerProvider.getEntityManager();
        List<ChatTarefa> mensagens = em.createNamedQuery("ChatTarefa.findByTarefa")
                .setParameter("tarefa", tarefa)
                .getResultList();

        for (ChatTarefa mensagem : mensagens) {

            ChatLine line = new ChatLine(mensagem.getUsuario().getNome() + ": " + mensagem.getMensagem() + " - "
                    + FormatterUtil.formatDateTime(mensagem.getDataHoraInclusao()));
            chat.addLine(line);
        }

       view.chat.addListener(this);

    }

    @Override
    public void cancelButtonClicked() {
        ((GestorMDI) UI.getCurrent()).logout();
    }

    @Override
    public void lineAdded(ChatLine line) {
        
        // ignora linhas de historico
        if (line.getUser()==null){
            return ;
        }
        
        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }

        ChatTarefa novaMensagem = new ChatTarefa();
        novaMensagem.setMensagem(line.getText());
        novaMensagem.setUsuario(usuarioLogado);
        novaMensagem.setTarefa(tarefa);
        novaMensagem.setDataHoraInclusao(LocalDateTime.now());
        em.persist(novaMensagem);

        em.getTransaction().commit();

    }

}
