package com.kverchi.diary.controller;

import com.kverchi.diary.domain.ChatMessage;
import com.kverchi.diary.domain.Conversation;
import com.kverchi.diary.domain.User;
import com.kverchi.diary.security.UserDetailsImpl;
import com.kverchi.diary.service.MessengerService;
import com.kverchi.diary.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

/**
 * Created by Liudmyla Melnychuk on 24.11.2017.
 */
@RestController
@RequestMapping("messages")
@SessionAttributes("currentConversation")
public class MessengerController {
    final static Logger logger = Logger.getLogger(MessengerController.class);
    @Autowired
    UserService userService;
    @Autowired
    MessengerService messengerService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @ModelAttribute("currentConversation")
    public Conversation getCurrentConversation() {
        return new Conversation();
    }

    @RequestMapping("/show")
    public ModelAndView showMessenger(@ModelAttribute("currentConversation") Conversation currentConversation) {
        ModelAndView mv = new ModelAndView("messenger");
        User user = userService.getUserFromSession();
        if (user != null) {
            logger.debug("currentConversation: " + currentConversation.getConversationId());
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

    @RequestMapping("/conversation/{conversationId}")
    public ModelAndView openConversation(@PathVariable("conversationId") int conversationId) {
        ModelAndView mv = new ModelAndView("fragment/messenger::conversation");
        User user = userService.getUserFromSession();
        if (user != null) {
            List<com.kverchi.diary.domain.Message> conversationMessages =
                    messengerService.getConversationMessages(user.getUserId(), conversationId);
            Conversation currentConversation = messengerService.getConversation(conversationId);
            mv.addObject("conversationMessages", conversationMessages);
            mv.addObject("currentConversation", currentConversation);
        }
        return mv;
    }

    @MessageMapping("/send-msg")
    //@SendTo("/topic/receive-msg")
    public void greeting(Message<Object> completeWSMessage, com.kverchi.diary.domain.Message message,
                         @ModelAttribute("currentConversation") Conversation currentConversation
                         /*, @DestinationVariable String to*/) throws Exception {
        logger.debug(messagingTemplate);
        logger.debug("current conversation ID: " + currentConversation.getConversationId());
        Principal principal = completeWSMessage.getHeaders().get(SimpMessageHeaderAccessor.USER_HEADER, Principal.class);
        logger.debug(principal.getName());
        if (principal instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) principal;
            int senderId = userDetails.getUser().getUserId();
            logger.debug("User ID: " + senderId);
            message.setUser(userDetails.getUser());
            message.setConversation(currentConversation);

            //messageSender.sendMessage(message);
            int user1 = currentConversation.getUser1().getUserId();
            int user2 = currentConversation.getUser2().getUserId();
            String receiverUsername;
            if(currentConversation.getUser1().getUserId() != senderId) {
                receiverUsername = currentConversation.getUser1().getUsername();
            } else {
                receiverUsername = currentConversation.getUser2().getUsername();
            }
            messagingTemplate.convertAndSendToUser(receiverUsername, "/queue/receive-msg", message);
            messengerService.saveMessage(message);
        }
    }
}
