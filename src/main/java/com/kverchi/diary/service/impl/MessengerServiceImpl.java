package com.kverchi.diary.service.impl;

import com.kverchi.diary.dao.ConversationDao;
import com.kverchi.diary.dao.MessageDao;
import com.kverchi.diary.domain.ChatMessage;
import com.kverchi.diary.domain.Conversation;
import com.kverchi.diary.domain.Message;
import com.kverchi.diary.domain.User;
import com.kverchi.diary.service.MessengerService;
import com.kverchi.diary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List getConversationMessages(int userId, int companionId) {
        return messageDao.getConversationMessages(userId, companionId);
    }

    @Override
    public Conversation getConversation(int conversationId) {
        return conversationDao.getById(conversationId);
    }

}
