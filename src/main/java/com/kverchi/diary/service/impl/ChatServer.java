package com.kverchi.diary.service.impl;

import com.kverchi.diary.domain.ChatMessage;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by Liudmyla Melnychuk on 15.11.2017.
 */
@ServerEndpoint(value = "/chat/{username}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class ChatServer {
    private Session session;
    private static final Set<ChatServer> chatEndpoints = new CopyOnWriteArraySet<>();
    private static HashMap<String, String> users = new HashMap<>();
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException, EncodeException {

        this.session = session;
        chatEndpoints.add(this);
        users.put(session.getId(), username);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setFrom(username);
        chatMessage.setContent("Connected!");
        broadcast(chatMessage);
    }

    @OnMessage
    public void onMessage(Session session, ChatMessage chatMessage) throws IOException, EncodeException {
        chatMessage.setFrom(users.get(session.getId()));
        broadcast(chatMessage);
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        chatEndpoints.remove(this);
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setFrom(users.get(session.getId()));
        chatMessage.setContent("Disconnected!");
        broadcast(chatMessage);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }

    private static void broadcast(ChatMessage chatMessage) throws IOException, EncodeException {
        chatEndpoints.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote()
                            .sendObject(chatMessage);
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
