package com.kverchi.diary.service.impl;

import com.google.gson.Gson;
import com.kverchi.diary.domain.ChatMessage;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Created by Liudmyla Melnychuk on 15.11.2017.
 */
public class MessageEncoder implements Encoder.Text<ChatMessage> {

    private static Gson gson = new Gson();

    @Override
    public String encode(ChatMessage message) throws EncodeException {
        String json = gson.toJson(message);
        return json;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // Custom initialization logic
    }

    @Override
    public void destroy() {
        // Close resources
    }
}
