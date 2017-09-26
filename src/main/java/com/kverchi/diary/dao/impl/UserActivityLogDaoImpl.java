package com.kverchi.diary.dao.impl;

import com.kverchi.diary.custom.exception.DatabaseException;
import com.kverchi.diary.dao.UserActivityDao;
import com.kverchi.diary.domain.UserActivityLog;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by Kverchi on 18.9.2017.
 */
@Repository
public class UserActivityLogDaoImpl extends GenericDaoImpl<UserActivityLog> implements UserActivityDao {
    @Override
    public UserActivityLog getLastUserActivity(int user_id) throws DatabaseException {
        EntityManager entityManager = null;
        UserActivityLog result = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

            CriteriaQuery<UserActivityLog> criteriaQuery = criteriaBuilder.createQuery(UserActivityLog.class);
            Root<UserActivityLog> activityLogRoot = criteriaQuery.from(UserActivityLog.class);
            Predicate predicate = criteriaBuilder.equal(activityLogRoot.get("user_id"), user_id);
            criteriaQuery.select(activityLogRoot).where(predicate);
            criteriaQuery.orderBy(criteriaBuilder.desc(activityLogRoot.get("login_time")));

            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery(criteriaQuery);
            query.setMaxResults(1);
            result = (UserActivityLog)query.getSingleResult();
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
