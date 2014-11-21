package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.ChatTarefa;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.GestorEntityManagerProvider;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.vaadin.chatbox.SharedChat;
import org.vaadin.chatbox.client.ChatLine;
import org.vaadin.chatbox.client.ChatUser;

/**
 * Singleton responsável por coordenar os chats em grupo
 *
 * @author Rodrigo
 */
public class ChatSingleton implements SharedChat.ChatListener {

    private static ChatSingleton instance;
    private final Map<Integer, SharedChat> chats;
    private final Map<Integer, Usuario> usuarios;
    private final Map<Integer, Tarefa> tarefas;

    /**
     * Construtor privado para garantir singularidade
     */
    private ChatSingleton() {
        chats = new HashMap<>();
        usuarios = new HashMap<>();
        tarefas = new HashMap<>();
    }

    /**
     * Cria a instancia do singleton, caso não exista, e retorna
     *
     * @return a única instancia de ChatSingleton
     */
    public static ChatSingleton getInstance() {

        if (instance == null) {
            instance = new ChatSingleton();
        }

        return instance;
    }

    public SharedChat getChat(Tarefa tarefa) {
        if (!chats.containsKey(tarefa.getId())) {
            SharedChat chat = new SharedChat();
            loadHistorico(chat, tarefa);
            chats.put(tarefa.getId(), chat);
        }
        return chats.get(tarefa.getId());
    }

    public String buildID(Usuario usuario, Tarefa tarefa, boolean historico) {
        return tarefa.getId() + ":" + usuario.getId() + ":" + historico;
    }

    private void loadHistorico(SharedChat chat, Tarefa tarefa) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();
        List<ChatTarefa> historico = em.createNamedQuery("ChatTarefa.findByTarefa")
                .setParameter("tarefa", tarefa)
                .getResultList();

        for (ChatTarefa mensagem : historico) {

            ChatUser user = new ChatUser(buildID(mensagem.getUsuario(), tarefa, true), mensagem.getUsuario().getNome(), "user1");
            ChatLine line = new ChatLine(mensagem.getMensagem(), user);

            chat.addLine(line);
        }

    }

    private Tarefa getTarefa(String chatID) {
        Integer idTarefa = Integer.parseInt(chatID.split(":")[0]);
        if (tarefas.containsKey(idTarefa)) {
            return tarefas.get(idTarefa);
        } else {
            EntityManager em = GestorEntityManagerProvider.getEntityManager();
            Tarefa tarefa = em.find(Tarefa.class, idTarefa);
            tarefas.put(idTarefa, tarefa);
            return tarefa;
        }
    }

    private Usuario getUsuario(String chatID) {
        Integer idUsuario = Integer.parseInt(chatID.split(":")[1]);
        if (usuarios.containsKey(idUsuario)) {
            return usuarios.get(idUsuario);
        } else {
            EntityManager em = GestorEntityManagerProvider.getEntityManager();
            Usuario usuario = em.find(Usuario.class, idUsuario);
            usuarios.put(idUsuario, usuario);
            return usuario;
        }
    }

    private boolean getFlagHistorico(String chatID) {
        boolean flag = Boolean.parseBoolean(chatID.split(":")[2]);
        return flag;
    }

    @Override
    public void lineAdded(ChatLine line) {

        boolean historico = getFlagHistorico(line.getUser().getId());

        if (!historico) {
            ChatTarefa novaMensagem = new ChatTarefa();
            novaMensagem.setMensagem(line.getText());
            novaMensagem.setUsuario(getUsuario(line.getUser().getId()));
            novaMensagem.setTarefa(getTarefa(line.getUser().getId()));
            novaMensagem.setDataHoraInclusao(LocalDateTime.now());

            EntityManager em = GestorEntityManagerProvider.getEntityManager();

            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
            em.persist(novaMensagem);

            em.getTransaction().commit();
        }

    }
}
