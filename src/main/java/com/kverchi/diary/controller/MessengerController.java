package com.kverchi.diary.controller;

import com.kverchi.diary.domain.*;
import com.kverchi.diary.service.MessengerService;
import com.kverchi.diary.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView showMessenger(/*@ModelAttribute("currentConversation") Conversation currentConversation*/) {
        ModelAndView mv = new ModelAndView("messenger");
        User user = userService.getUserFromSession();
        if (user != null) {
            //logger.debug("currentConversation: " + currentConversation.getConversationId());
            int receiverUserId = user.getUserId();
            int msgCount = messengerService.geAllUnreadUserMessagesCount(receiverUserId);
            mv.addObject("msgCount", msgCount);
            List<com.kverchi.diary.domain.Message> unreadMessages = messengerService.getAllUnreadUserMessages(receiverUserId);
            mv.addObject("unreadMessages", unreadMessages);
            List<com.kverchi.diary.domain.Message> recentMessages = messengerService.getRecentMessagesFromAllUserConversations(receiverUserId);
            mv.addObject("recentMessages", recentMessages);
        }
        return mv;
    }

    @RequestMapping("/conversation/{conversationId}")
    public ModelAndView openConversation(@PathVariable ("conversationId") int conversationId) {
        ModelAndView mv = new ModelAndView("fragment/messenger::conversation");
        User user = userService.getUserFromSession();
        if (user != null) {
            int currentUserId = user.getUserId();
            List<com.kverchi.diary.domain.Message> conversationMessages =
                    messengerService.getUserMessagesByConversationId(currentUserId, conversationId, 1);
            Conversation currentConversation = messengerService.getConversation(conversationId);
            int unreadMessagesCount = messengerService.getUnreadMessagesCountByConversationId(
                    conversationId, currentUserId);
            mv.addObject("unreadMessagesCount", unreadMessagesCount);
            mv.addObject("conversationMessages", conversationMessages);
            mv.addObject("currentConversation", currentConversation);
        }
        return mv;
    }
    @RequestMapping(value = "/conversation/set-read", method = RequestMethod.POST)
    public void setMessagesAsRead(@RequestBody List<Integer> readMessagesId) {
        User user = userService.getUserFromSession();
        if(user != null) {
            messengerService.setUserMessagesAsRead(readMessagesId);

            //messagingTemplate.convertAndSendToUser(receiverUsername, "/queue/receive-msg", message);
        }
    }
    @RequestMapping("/conversation/more/{currentPage}")
    public ModelAndView showMoreConversation(@PathVariable("currentPage") int currentPage,
                                             @ModelAttribute("currentConversation") Conversation currentConversation) {
        logger.debug("Current conversation is " + currentConversation.getConversationId());
        ModelAndView mv = new ModelAndView("fragment/messenger::more");
        User user = userService.getUserFromSession();
        if(user != null) {
            List<com.kverchi.diary.domain.Message> conversationMessages =
                    messengerService.getUserMessagesByConversationId(user.getUserId(),
                            currentConversation.getConversationId(),
                            currentPage);
            mv.addObject("conversationMessages", conversationMessages);
        }
        return mv;
    }
    /*@RequestMapping("/conversation")
    public ModelAndView showConversation() {
        ModelAndView mv = new ModelAndView("fragment/messenger::conversation");
        return mv;
    }
    @RequestMapping(value = "/pagination-messages", method = RequestMethod.POST)
    public ModelAndView showPaginationPosts(@RequestBody MessageSearchAttributes searchAttributes) {
        ModelAndView mv = new ModelAndView("fragments :: messages-page");
        User user = userService.getUserFromSession();
        if (user != null) {
            searchAttributes.addSearchCriteria(MessageSearchAttributes.MessageSearchType.BY_USER_ID,
                    user.getUserId());
            MessageSearchResults results = messengerService.search(searchAttributes);
            Conversation currentConversation =
                    messengerService.getConversation(results.getResults().get(0).getConversation().getConversationId());
            mv.addObject("conversationMessages", results.getResults());
            mv.addObject("totalPages", results.getTotalPages());
            mv.addObject("currentConversation", currentConversation);
        }
        return mv;
    }*/

    @RequestMapping(value = "/send-message", method = RequestMethod.POST)
    public void sendMessage(@RequestBody com.kverchi.diary.domain.Message message,
                            @ModelAttribute("currentConversation") Conversation currentConversation) {
        User sender = userService.getUserFromSession();
        if(sender != null) {
            int senderId = sender.getUserId();
            logger.debug("User ID: " + senderId);
            logger.debug("Current conversation ID: " + currentConversation.getConversationId());
            message.setSender(sender);
            message.setConversation(currentConversation);
            String receiverUsername;
            if(currentConversation.getUser1().getUserId() != senderId) {
                receiverUsername = currentConversation.getUser1().getUsername();
            } else {
                receiverUsername = currentConversation.getUser2().getUsername();
            }
            logger.debug("receiver username: " + receiverUsername);
            messengerService.saveMessage(message);
            messagingTemplate.convertAndSendToUser(receiverUsername, "/queue/receive-msg", message);

        }

    }

    @MessageMapping("/send-msg")
    //@SendTo("/queue/receive-msg")
    public void greeting(com.kverchi.diary.domain.Message message) throws Exception {
        messagingTemplate.convertAndSendToUser("kverchi", "/queue/receive-msg", message);
    }
}
