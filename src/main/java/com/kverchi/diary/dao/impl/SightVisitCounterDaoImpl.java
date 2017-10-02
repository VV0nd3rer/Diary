package com.kverchi.diary.dao.impl;

import com.kverchi.diary.custom.exception.DatabaseException;
import com.kverchi.diary.dao.SightVisitCounterDao;
import com.kverchi.diary.domain.SightVisitCounter;
import com.kverchi.diary.domain.SightWishCounter;
import org.apache.log4j.Logger;
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

import static java.lang.Math.toIntExact;

/**
 * Created by Liudmyla Melnychuk on 14.9.2017.
 */
@Repository
public class SightVisitCounterDaoImpl extends GenericDaoImpl<SightVisitCounter> implements SightVisitCounterDao {
    final static Logger logger = Logger.getLogger(SightVisitCounterDaoImpl.class);

    @Autowired
    protected EntityManagerFactory entityManagerFactory;
    @Override
    public int getCounterValue(int counter_obj_id) throws DatabaseException {
        EntityManager entityManager = null;
        int result;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> criteriaQueryCount = criteriaBuilder.createQuery(Long.class);
            Root<SightVisitCounter> visitCounterRoot = criteriaQueryCount.from(SightVisitCounter.class);
            criteriaQueryCount.select(criteriaBuilder.count(visitCounterRoot));
            criteriaQueryCount.where(criteriaBuilder.equal(visitCounterRoot.get("countriesSight").get("sight_id"), counter_obj_id));
            Query query = entityManager.createQuery(criteriaQueryCount);
            result = toIntExact((Long)query.getSingleResult());

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
    public boolean isValueExist(int counter_obj_id, int user_id) throws DatabaseException {
        EntityManager entityManager = null;
        boolean result = false;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> criteriaQueryCount = criteriaBuilder.createQuery(Long.class);
            Root<SightVisitCounter> visitCounterRoot = criteriaQueryCount.from(SightVisitCounter.class);
            criteriaQueryCount.select(criteriaBuilder.count(visitCounterRoot));
            Predicate andPredicate = criteriaBuilder.and(
                    criteriaBuilder.equal(visitCounterRoot.get("countriesSight").get("sight_id"), counter_obj_id),
                    criteriaBuilder.equal(visitCounterRoot.get("user").get("userId"), user_id));
            criteriaQueryCount.where(andPredicate);
            Query query = entityManager.createQuery(criteriaQueryCount);
            int singleResult = toIntExact((Long)query.getSingleResult());
            entityManager.getTransaction().commit();
            if(singleResult == 1) {
                result = true;
            }

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
