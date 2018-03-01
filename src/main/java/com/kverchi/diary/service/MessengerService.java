package com.kverchi.diary.service;

import com.kverchi.diary.domain.*;

import java.util.List;

/**
 * Created by Liudmyla Melnychuk on 21.11.2017.
 */
public interface MessengerService {
    void saveMessage(com.kverchi.diary.domain.Message message);
    int getUnreadMessagesCount(int userId);
    List getUnreadMessages(int userId);
    List getRecentMessagesFromAllUserConversations(int userId);
    List getMessagesByConversationId(int userId, int conversationId, int currentPage);
    void setMessagesAsRead(List<Integer> readMessagesId);
    /* MessagePaginatedResponce instead of MessageSearchResults */
   /* MessageSearchResults getMessagesByConversationId(MessageSearchAttributes searchAttributes);*/
    Conversation getConversation(int conversationId);
}
