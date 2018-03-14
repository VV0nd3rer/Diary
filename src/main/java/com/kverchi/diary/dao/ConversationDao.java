package com.kverchi.diary.dao;

import com.kverchi.diary.domain.Conversation;

import java.util.List;

/**
 * Created by Liudmyla Melnychuk on 2.2.2018.
 */
public interface ConversationDao extends GenericDao<Conversation> {
    Conversation getConversationByUsersIds(int user1Id, int user2Id);
}
