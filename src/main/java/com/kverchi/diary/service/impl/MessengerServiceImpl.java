package com.kverchi.diary.service.impl;

import com.kverchi.diary.dao.MessageDao;
import com.kverchi.diary.domain.ChatMessage;
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

    @Override
    public void saveMessage(ChatMessage message) {
        User sender = userService.getUserByUsername(message.getFrom());
        User receiver = userService.getUserByUsername(message.getTo());
        Message persistentMessage = new Message();
        persistentMessage.setUser(sender);
        persistentMessage.setReceiverId(receiver.getUserId());
        persistentMessage.setText(message.getContent());
        messageDao.persist(persistentMessage);
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
        return messageDao.getRecentMessagesFromAllUsers(receiverId);
    }

    @Override
    public List getConversationMessages(int userId, int companionId) {
        return messageDao.getConversationMessages(userId, companionId);
    }
}
