package com.kverchi.diary.service.impl;

import com.kverchi.diary.dao.ConversationDao;
import com.kverchi.diary.dao.MessageDao;
import com.kverchi.diary.domain.*;
import com.kverchi.diary.service.MessengerService;
import com.kverchi.diary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public void saveMessage(com.kverchi.diary.domain.Message message) {
        messageDao.persist(message);
    }

    @Override
    public int getUnreadMessagesCount(int userId) {
        return messageDao.getUnreadMessagesCount(userId);
    }

    @Override
    public List getUnreadMessages(int userId) {
        return messageDao.getUnreadMessages(userId);
    }

    @Override
    public List getRecentMessagesFromAllUsers(int receiverId) {
        return messageDao.getConversations(receiverId);
    }

    @Override
    public List getMessagesByConversationId(int userId, int companionId, int currentPage) {
        Pagination pagination = new Pagination(currentPage);
        List<Message> messages = messageDao.getMessagesByConversationId(userId, companionId, pagination);
        List<Message> readMessages = new ArrayList();
        for(Message message : messages) {
            if(message.isRead() != true) {
                int senderId = message.getSender().getUserId();
                if (senderId != userId) {
                    Message updatedMessage = message;
                    updatedMessage.setRead(true);
                    readMessages.add(updatedMessage);
                }
            }
        }
        if(!readMessages.isEmpty()) {
            messageDao.updateBatch(readMessages);
        }
        return messages;
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

}
