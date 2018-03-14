package com.kverchi.diary.dao.impl;

import com.kverchi.diary.custom.exception.DatabaseException;
import com.kverchi.diary.dao.ConversationDao;
import com.kverchi.diary.domain.Conversation;
import com.kverchi.diary.domain.Conversation_;
import com.kverchi.diary.domain.User_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by Liudmyla Melnychuk on 2.2.2018.
 */
@Repository
public class ConversationDaoImpl extends GenericDaoImpl<Conversation> implements ConversationDao {

    @Override
    public Conversation getConversationByUsersIds(int user1Id, int user2Id) {
        EntityManager entityManager = null;
        Conversation result = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Conversation.class);
            Root<Conversation> messageRoot = criteriaQuery.from(Conversation.class);
            criteriaQuery.select(messageRoot);

            Predicate predicateOnUser1Id = criteriaBuilder.or(
                    criteriaBuilder.equal(
                            messageRoot.get(Conversation_.user1).get(User_.userId), user1Id),
                    criteriaBuilder.equal(
                            messageRoot.get(Conversation_.user2).get(User_.userId), user1Id)
            );
            Predicate predicateOnUser2Id = criteriaBuilder.or(
                    criteriaBuilder.equal(
                            messageRoot.get(Conversation_.user1).get(User_.userId), user2Id),
                    criteriaBuilder.equal(
                            messageRoot.get(Conversation_.user2).get(User_.userId), user2Id)
                    );
            criteriaQuery.where(predicateOnUser1Id, predicateOnUser2Id);

            Query query = entityManager.createQuery(criteriaQuery);
            if(!query.getResultList().isEmpty()) {
                result = (Conversation) query.getSingleResult();
            }
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
