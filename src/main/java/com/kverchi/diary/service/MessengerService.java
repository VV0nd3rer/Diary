package com.kverchi.diary.service;

import com.kverchi.diary.domain.*;

import java.util.List;

/**
 * Created by Liudmyla Melnychuk on 21.11.2017.
 */
public interface MessengerService {
    void saveMessage(com.kverchi.diary.domain.Message message);
    int geAllUnreadUserMessagesCount(int userId);
    List getAllUnreadUserMessages(int userId);
    List getRecentMessagesFromAllUserConversations(int userId);
    List getUserMessagesByConversationId(int userId, int conversationId, int currentPage);
    void setUserMessagesAsRead(List<Integer> readMessagesId);
    /* MessagePaginatedResponce instead of MessageSearchResults */
   /* MessageSearchResults getMessagesByConversationId(MessageSearchAttributes searchAttributes);*/
    Conversation getConversation(int conversationId);
    int getUnreadMessagesCountByConversationId(int conversationId, int receiverId);
}
