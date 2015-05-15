package com.saax.gestorweb.model;

import com.saax.gestorweb.model.datamodel.ChatTarefa;
import com.saax.gestorweb.model.datamodel.Task;
import com.saax.gestorweb.model.datamodel.Usuario;
import com.saax.gestorweb.util.FormatterUtil;
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
 * Singleton responsible for coordinating the group chats
 *
 * @author Rodrigo
 */
public class ChatSingletonModel implements SharedChat.ChatListener {

    private static ChatSingletonModel instance;
    private final Map<Integer, SharedChat> chats;

    /**
     * Private builder to ensure uniqueness
     */
    private ChatSingletonModel() {
        chats = new HashMap<>();
    }

    /**
     * Creates the singleton instance, if none, and returns
     *
     * @return the only instance of ChatSingletonModel
     */
    public static ChatSingletonModel getInstance() {

        if (instance == null) {
            instance = new ChatSingletonModel();
        }

        return instance;
    }

    public SharedChat getChat(Task task) {
        if (!chats.containsKey(task.getId())) {
            SharedChat chat = new SharedChat();
            loadHistory(chat, task);
            chats.put(task.getId(), chat);
        }
        return chats.get(task.getId());
    }

    public String buildID(Usuario user, Task task, boolean history) {
        return task.getId() + ":" + user.getId() + ":" + history;
    }

    private void loadHistory(SharedChat chat, Task task) {

        EntityManager em = GestorEntityManagerProvider.getEntityManager();
        List<ChatTarefa> history = em.createNamedQuery("ChatTarefa.findByTarefa")
                .setParameter("tarefa", task)
                .getResultList();

        for (ChatTarefa message : history) {

            ChatUser user = new ChatUser(buildID(message.getUsuario(), task, true), message.getUsuario().getNome(), "user1");
            ChatLine line = new ChatLine(message.getMensagem()+"   às "+FormatterUtil.formatDateTime(message.getDataHoraInclusao()).toString(), user);
            chat.addLine(line);
        }

    }

    private Task getTask(String chatID) {
        Integer idTask = Integer.parseInt(chatID.split(":")[0]);
            EntityManager em = GestorEntityManagerProvider.getEntityManager();
            Task task = em.find(Task.class, idTask);
            return task;
    }

    private Usuario getUser(String chatID) {
        Integer idUser = Integer.parseInt(chatID.split(":")[1]);
            EntityManager em = GestorEntityManagerProvider.getEntityManager();
            Usuario user = em.find(Usuario.class, idUser);
            return user;
    }

    private boolean getHistoryFlag(String chatID) {
        boolean flag = Boolean.parseBoolean(chatID.split(":")[2]);
        return flag;
    }

    @Override
    public void lineAdded(ChatLine line) {

        boolean history = getHistoryFlag(line.getUser().getId());

        if (!history) {
            ChatTarefa newMessage = new ChatTarefa();
            newMessage.setMensagem(line.getText());
            newMessage.setUsuario(getUser(line.getUser().getId()));
            newMessage.setTarefa(getTask(line.getUser().getId()));
            newMessage.setDataHoraInclusao(LocalDateTime.now());

            EntityManager em = GestorEntityManagerProvider.getEntityManager();

            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
            em.persist(newMessage);

            em.getTransaction().commit();
        }

    }
}
