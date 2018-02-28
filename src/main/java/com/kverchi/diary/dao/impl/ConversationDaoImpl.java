package com.kverchi.diary.dao.impl;

import com.kverchi.diary.dao.ConversationDao;
import com.kverchi.diary.domain.Conversation;
import com.kverchi.diary.domain.Conversation_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Created by Liudmyla Melnychuk on 2.2.2018.
 */
@Repository
public class ConversationDaoImpl extends GenericDaoImpl<Conversation> implements ConversationDao {
}
