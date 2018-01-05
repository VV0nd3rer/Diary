package com.kverchi.diary.dao;

import com.kverchi.diary.domain.ChatMessage;
import com.kverchi.diary.domain.Message;

import java.util.List;

/**
 * Created by Liudmyla Melnychuk on 21.12.2017.
 */
public interface MessageDao extends GenericDao<Message> {
    int getUnreadMessagesCount(int receiverId);
    List getUnreadMessages(int receiverId);
    List getRecentMessagesFromAllUsers(int receiverId);
}
