package com.kverchi.diary.controller;

import com.kverchi.diary.domain.ChatMessage;
import com.kverchi.diary.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

/**
 * Created by Liudmyla Melnychuk on 24.11.2017.
 */
@RestController
@RequestMapping("messages")
public class MessengerController {
    final static Logger logger = Logger.getLogger(MessengerController.class);
    @Autowired
    UserService userService;
    /*
    @Autowired
    MessageSender messageSender;*/

    @RequestMapping("/show")
    public ModelAndView showMessenger() {
        logger.debug("user service: " + userService);
        logger.debug(userService.getUserFromSession());
        ModelAndView mv = new ModelAndView("messenger");
        return mv;
    }
    @MessageMapping("/send-msg/{to}")
    @SendTo("/topic/{to}")
    public ChatMessage greeting(Message<Object> msg, ChatMessage message, @DestinationVariable String to) throws Exception {
        //Thread.sleep(1000); // simulated delay

        Principal principal = msg.getHeaders().get(SimpMessageHeaderAccessor.USER_HEADER, Principal.class);
        logger.debug(principal.getName());
        message.setFrom(principal.getName());
        logger.debug("msg to : " + message.getTo());
        //message.setContent("Hello :) ");
        //messageSender.sendMessage(message);
        return message;
    }
}
