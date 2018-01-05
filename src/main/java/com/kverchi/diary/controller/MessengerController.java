package com.kverchi.diary.controller;

import com.kverchi.diary.domain.ChatMessage;
import com.kverchi.diary.domain.User;
import com.kverchi.diary.service.MessengerService;
import com.kverchi.diary.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

/**
 * Created by Liudmyla Melnychuk on 24.11.2017.
 */
@RestController
@RequestMapping("messages")
public class MessengerController {
    final static Logger logger = Logger.getLogger(MessengerController.class);
    @Autowired
    UserService userService;
    @Autowired
    MessengerService messengerService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    /*
    @Autowired
    MessageSender messageSender;*/

    @RequestMapping("/show")
    public ModelAndView showMessenger() {
        ModelAndView mv = new ModelAndView("messenger");
        User user = userService.getUserFromSession();
        if(user != null) {
            int receiverUserId = user.getUserId();
            int msgCount = messengerService.getUnreadMessagesCount(receiverUserId);
            mv.addObject("msgCount", msgCount);
            List<com.kverchi.diary.domain.Message> unreadMessages = messengerService.getUnreadMessages(receiverUserId);
            mv.addObject("unreadMessages", unreadMessages);
            List<com.kverchi.diary.domain.Message> recentMessages = messengerService.getRecentMessagesFromAllUsers(receiverUserId);
            mv.addObject("recentMessages", recentMessages);
        }
        return mv;
    }
    @MessageMapping("/send-msg")
    //@SendTo("/topic/receive-msg")
    public void greeting(Message<Object> msg, ChatMessage message/*, @DestinationVariable String to*/) throws Exception {
        //Thread.sleep(1000); // simulated delay
        logger.debug(messagingTemplate);
        Principal principal = msg.getHeaders().get(SimpMessageHeaderAccessor.USER_HEADER, Principal.class);
        logger.debug(principal.getName());
        message.setFrom(principal.getName());
        logger.debug("msg to : " + message.getTo());
        //message.setContent("Hello :) ");
        //messageSender.sendMessage(message);
        messagingTemplate.convertAndSendToUser(message.getTo(), "/queue/receive-msg", message);
        messengerService.saveMessage(message);
    }
}
