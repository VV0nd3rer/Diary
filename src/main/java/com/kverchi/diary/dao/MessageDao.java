package com.kverchi.diary.dao;

import com.kverchi.diary.domain.Message;
import com.kverchi.diary.domain.Pagination;

import java.util.List;

/**
 * Created by Liudmyla Melnychuk on 21.12.2017.
 */
public interface MessageDao extends GenericDao<Message>, SearchDao {
    int getUnreadMessagesCount(int receiverId);
    List getUnreadMessages(int receiverId);
    List getConversations(int userId);
    List getMessagesByConversationId(int userId, int conversationId, Pagination pagination);
    void updateMessagesReadStatus(List<Integer> messagesIds);
}
