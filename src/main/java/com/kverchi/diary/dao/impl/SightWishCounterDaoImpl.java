package com.kverchi.diary.dao.impl;

import com.kverchi.diary.custom.exception.DatabaseException;
import com.kverchi.diary.dao.SightWishCounterDao;
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
 * Created by Liudmyla Melnychuk on 13.9.2017.
 */
@Repository
public class SightWishCounterDaoImpl extends GenericDaoImpl<SightWishCounter> implements SightWishCounterDao {

    final static Logger logger = Logger.getLogger(SightWishCounterDaoImpl.class);

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
            Root<SightWishCounter> wishCounterRoot = criteriaQueryCount.from(SightWishCounter.class);
            criteriaQueryCount.select(criteriaBuilder.count(wishCounterRoot));
            criteriaQueryCount.where(criteriaBuilder.equal(wishCounterRoot.get("countriesSight").get("sightId"), counter_obj_id));
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
            Root<SightWishCounter> wishCounterRoot = criteriaQueryCount.from(SightWishCounter.class);
            criteriaQueryCount.select(criteriaBuilder.count(wishCounterRoot));
            Predicate andPredicate = criteriaBuilder.and(
                    criteriaBuilder.equal(wishCounterRoot.get("countriesSight").get("sightId"), counter_obj_id),
                    criteriaBuilder.equal(wishCounterRoot.get("user").get("userId"), user_id));
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
