package com.kverchi.diary.controller;

import com.kverchi.diary.domain.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Liudmyla Melnychuk on 24.11.2017.
 */
@RestController
@RequestMapping("messages")
public class MessengerController {
    @RequestMapping("/show")
    public ModelAndView showMessenger() {
        ModelAndView mv = new ModelAndView("messenger");
        return mv;
    }
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public ChatMessage greeting(ChatMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        message.setContent("Hello :) ");
        return message;
    }
}
