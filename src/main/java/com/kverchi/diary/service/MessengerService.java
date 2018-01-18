package com.kverchi.diary.service;

import com.kverchi.diary.domain.ChatMessage;

import java.util.List;

/**
 * Created by Liudmyla Melnychuk on 21.11.2017.
 */
public interface MessengerService {
    void saveMessage(ChatMessage message);
    int getUnreadMessagesCount(int userId);
    List getUnreadMessages(int userId);
    List getRecentMessagesFromAllUsers(int receiverId);
    List getConversationMessages(int userId, int companionId);
}
