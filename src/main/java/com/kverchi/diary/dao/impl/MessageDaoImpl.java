package com.kverchi.diary.dao.impl;

        import com.kverchi.diary.custom.exception.DatabaseException;
        import com.kverchi.diary.dao.MessageDao;
        import com.kverchi.diary.domain.*;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Repository;

        import javax.persistence.EntityManager;
        import javax.persistence.EntityManagerFactory;
        import javax.persistence.PersistenceException;
        import javax.persistence.Query;
        import javax.persistence.criteria.*;
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

            Predicate predicateOnUser1Id = criteriaBuilder.equal(
                    messageRoot.get(Message_.conversation).get(Conversation_.user1).get(User_.userId), receiverId);
            Predicate predicateOnUser2Id = criteriaBuilder.equal(
                    messageRoot.get(Message_.conversation).get(Conversation_.user2).get(User_.userId), receiverId);
            Predicate predicateOnSenderId = criteriaBuilder.notEqual(
                    messageRoot.get(Message_.user).get(User_.userId), receiverId);
            Predicate predicateOnMessageStatus = criteriaBuilder.equal(messageRoot.get(Message_.read), false);
            Predicate predicateOnFinalCondition = criteriaBuilder.and(
                    predicateOnMessageStatus,
                    criteriaBuilder.or(predicateOnUser1Id, predicateOnUser2Id),
                    predicateOnSenderId);

            criteriaQueryCount.where(predicateOnFinalCondition);
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


            Predicate predicateOnUser1Id = criteriaBuilder.equal(
                    messageRoot.get(Message_.conversation).get(Conversation_.user1).get(User_.userId), receiverId);
            Predicate predicateOnUser2Id = criteriaBuilder.equal(
                    messageRoot.get(Message_.conversation).get(Conversation_.user2).get(User_.userId), receiverId);
            Predicate predicateOnSenderId = criteriaBuilder.notEqual(
                    messageRoot.get(Message_.user).get(User_.userId), receiverId);
            Predicate predicateOnMessageStatus = criteriaBuilder.equal(messageRoot.get(Message_.read), false);
            Predicate predicateOnFinalCondition = criteriaBuilder.and(
                    predicateOnMessageStatus,
                    criteriaBuilder.or(predicateOnUser1Id, predicateOnUser2Id),
                    predicateOnSenderId);
            criteriaQuery.where(predicateOnFinalCondition);
            Query query = entityManager.createQuery(criteriaQuery);
            result = query.getResultList();
            entityManager.getTransaction().commit();
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
    public List getConversations(int userId) {
        EntityManager entityManager = null;
        List<Message> messageList = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            String str_query = "FROM Message msg1 WHERE " +
                    "msg1.sentDatetime = " +
                    "(SELECT max(msg2.sentDatetime) FROM Message msg2 " +
                    "WHERE msg2.conversation.conversationId = msg1.conversation.conversationId) " +
                    "AND (msg1.conversation.user1.userId = :userId " +
                    "OR msg1.conversation.user2.userId = :userId) " +
                    "ORDER BY msg1.sentDatetime DESC";
            Query query = entityManager.createQuery(str_query);
            query.setParameter("userId", userId);
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

    @Override
    public List getConversationMessages(int userId, int conversationId) {
        EntityManager entityManager = null;
        List<Message> result;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Message.class);
            Root<Message> messageRoot = criteriaQuery.from(Message.class);
            criteriaQuery.select(messageRoot);
            Predicate predicateOnConversationId =
                    criteriaBuilder.equal(
                            messageRoot.get(Message_.conversation).get(Conversation_.conversationId), conversationId);
            Predicate predicateOnUser1Id = criteriaBuilder.equal(
                    messageRoot.get(Message_.conversation).get(Conversation_.user1).get(User_.userId), userId);
            Predicate predicateOnUser2Id = criteriaBuilder.equal(
                    messageRoot.get(Message_.conversation).get(Conversation_.user2).get(User_.userId), userId);
            Predicate predicateOnFinalCondition = criteriaBuilder.and(
                    predicateOnConversationId,
                    criteriaBuilder.or(predicateOnUser1Id, predicateOnUser2Id));
            criteriaQuery.where(predicateOnFinalCondition);

            Query query = entityManager.createQuery(criteriaQuery);
            result = query.getResultList();
            entityManager.getTransaction().commit();
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

}
