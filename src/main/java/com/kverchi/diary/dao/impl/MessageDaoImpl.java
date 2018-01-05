package com.kverchi.diary.dao.impl;

import com.kverchi.diary.custom.exception.DatabaseException;
import com.kverchi.diary.dao.MessageDao;
import com.kverchi.diary.domain.Message;
import com.kverchi.diary.domain.Message_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;

import static java.lang.Math.toIntExact;

/**
 * Created by Liudmyla Melnychuk on 21.12.2017.
 */
@Repository
public class MessageDaoImpl extends GenericDaoImpl<Message> implements MessageDao {
    @Autowired
    protected EntityManagerFactory entityManagerFactory;
    @Override
    public int getUnreadMessagesCount(int receiverId) {
        EntityManager entityManager = null;
        int result;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> criteriaQueryCount = criteriaBuilder.createQuery(Long.class);
            Root<Message> messageRoot = criteriaQueryCount.from(Message.class);
            criteriaQueryCount.select(criteriaBuilder.count(messageRoot));
            criteriaQueryCount.where(criteriaBuilder.equal(messageRoot.get(Message_.receiverId), receiverId),
                    (criteriaBuilder.equal(messageRoot.get(Message_.read), false)));
            Query query = entityManager.createQuery(criteriaQueryCount);
            result = toIntExact((Long)query.getSingleResult());
            entityManager.getTransaction().commit();
        }
        catch (PersistenceException e) {
            logger.error("DBException: message -> " + e.getMessage() + " cause -> " + e.getCause());
            throw new DatabaseException(e);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
        return result;
    }

    @Override
    public List getUnreadMessages(int receiverId) {
        EntityManager entityManager = null;
        List<Message> result;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Message.class);
            Root<Message> messageRoot = criteriaQuery.from(Message.class);
            criteriaQuery.select(messageRoot);
            criteriaQuery.where(criteriaBuilder.equal(messageRoot.get(Message_.receiverId), receiverId),
                    (criteriaBuilder.equal(messageRoot.get(Message_.read), false)));
            Query query = entityManager.createQuery(criteriaQuery);
            result = query.getResultList();
        } catch (PersistenceException e) {
            logger.error("DBException: message -> " + e.getMessage() + " cause -> " + e.getCause());
            throw new DatabaseException(e);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return result;
    }

    @Override
    public List getRecentMessagesFromAllUsers(int receiverId) {
        EntityManager entityManager = null;
        List<Message> messageList = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            /*String str_query = "FROM Message msg1, User user WHERE " +
                    "msg1.messageDatetime = " +
                    "(SELECT max(msg2.messageDatetime) FROM Message msg2 " +
                    "WHERE msg2.senderId = msg1.senderId) " +
                    "AND msg1.senderId = user.userId " +
                    "AND msg1.receiverId = :receiverId " +
                    "ORDER BY msg1.messageDatetime DESC";*/
            String str_query = "FROM Message msg1 WHERE " +
                    "msg1.messageDatetime = " +
                    "(SELECT max(msg2.messageDatetime) FROM Message msg2 " +
                    "WHERE msg2.user.userId = msg1.user.userId) " +
                    "AND msg1.receiverId = :receiverId " +
                    "ORDER BY msg1.messageDatetime DESC";
            Query query = entityManager.createQuery(str_query);
            query.setParameter("receiverId", receiverId);
            messageList = query.getResultList();

            entityManager.getTransaction().commit();
        } catch (PersistenceException e) {
            logger.error("DBException: message -> " + e.getMessage() + " cause -> " + e.getCause());
            throw new DatabaseException(e);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
        return messageList;
    }
}
