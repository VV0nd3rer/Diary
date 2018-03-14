package com.kverchi.diary.service.impl;

import com.kverchi.diary.dao.ConversationDao;
import com.kverchi.diary.dao.MessageDao;
import com.kverchi.diary.domain.*;
import com.kverchi.diary.service.MessengerService;
import com.kverchi.diary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Liudmyla Melnychuk on 21.12.2017.
 */
@Service
public class MessengerServiceImpl implements MessengerService {
    @Autowired
    UserService userService;
    @Autowired
    MessageDao messageDao;
    @Autowired
    ConversationDao conversationDao;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void saveMessage(com.kverchi.diary.domain.Message message, Conversation currentConversation, User sender) {
        message.setSender(sender);
        message.setConversation(currentConversation);
        String receiverUsername;
        int senderId = sender.getUserId();
        if (currentConversation.getUser1().getUserId() != senderId) {
            receiverUsername = currentConversation.getUser1().getUsername();
        } else {
            receiverUsername = currentConversation.getUser2().getUsername();
        }
        messageDao.persist(message);
        messagingTemplate.convertAndSendToUser(receiverUsername, "/queue/receive-msg", message);
    }
    @Override
    public Conversation createConversation(Conversation newConversation) {
        return conversationDao.persist(newConversation);
    }

    @Override
    public int geAllUnreadUserMessagesCount(int userId) {
        return messageDao.getUnreadMessagesCount(userId);
    }

    @Override
    public List getAllUnreadUserMessages(int userId) {
        return messageDao.getUnreadMessages(userId);
    }

    @Override
    public List getRecentMessagesFromAllUserConversations(int userId) {
        return messageDao.getRecentMessagesFromAllUserConversations(userId);
    }

    @Override
    public List getUserMessagesByConversationId(int userId, int conversationId, int currentPage) {
        Pagination pagination = new Pagination(currentPage);
        List<Message> messages = messageDao.getMessagesByConversationId(userId, conversationId, pagination);

        /*List<Integer> userReadMessagesIds = new ArrayList();
        for(Message message : messages) {
            if(message.isRead() != true) {
                int senderId = message.getSender().getUserId();
                if (senderId != userId) {
                    userReadMessagesIds.add(message.getMessageId());
                }
            }
        }
        messageDao.updateMessagesReadStatus(userReadMessagesIds);*/
        return messages;
    }

    @Override
    public Conversation getConversationByUsersIds(int user1Id, int user2Id) {
        return conversationDao.getConversationByUsersIds(user1Id, user2Id);
    }

    @Override
    public void setUserMessagesAsRead(List<Integer> readMessagesId) {
        messageDao.updateMessagesReadStatus(readMessagesId);
    }

    /*@Override
    public MessageSearchResults getMessagesByConversationId(MessageSearchAttributes searchAttributes) {
        MessageSearchResults searchResults = new MessageSearchResults();
        Pagination pagination = new Pagination(searchAttributes.getPageSize(), searchAttributes.getCurrentPage());
        Map<MessageSearchAttributes.MessageSearchType, Object> searchCriteria = searchAttributes.getSearchCriteria();
        Map<String, Object> hasAttributes = new HashMap<>();
        Map<String, String> includingAttributes = new HashMap<>();
        Map<String, Object> choosingAttributes = new HashMap<>();

        if (searchCriteria != null && !searchCriteria.isEmpty()) {
            for (Map.Entry<MessageSearchAttributes.MessageSearchType, Object> entry : searchCriteria.entrySet()) {
                switch (entry.getKey()) {
                    case BY_USER_ID:
                        choosingAttributes.put("userId", entry.getValue());
                        break;
                    case BY_CONVERSATION_ID:
                        hasAttributes.put("conversationId", entry.getValue());
                        break;
                }
            }
        }
        int totalRows;
        totalRows = messageDao.getRowsNumberWithAttributes(hasAttributes, includingAttributes, choosingAttributes);

        pagination.setTotalRows(totalRows);
        searchResults.setTotalPages(pagination.getTotalPages());
        List results;
        results = messageDao.searchWithAttributes(hasAttributes, includingAttributes, choosingAttributes, pagination);
        searchResults.setResults(results);
        return searchResults;
    }*/

    @Override
    public Conversation getConversation(int conversationId) {
        return conversationDao.getById(conversationId);
    }

    @Override
    public int getUnreadMessagesCountByConversationId(int conversationId, int receiverId) {
        return messageDao.getUnreadMessagesCountByConversationId(conversationId, receiverId);
    }

}
