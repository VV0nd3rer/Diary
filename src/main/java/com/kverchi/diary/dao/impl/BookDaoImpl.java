package com.kverchi.diary.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kverchi.diary.custom.exception.DatabaseException;
import com.kverchi.diary.domain.Book_;
import com.kverchi.diary.domain.Pagination;
import com.kverchi.diary.domain.SearchAttributes;
import org.springframework.stereotype.Repository;

import com.kverchi.diary.dao.BookDao;
import com.kverchi.diary.domain.Book;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static java.lang.Math.toIntExact;

@Repository
public class BookDaoImpl extends GenericDaoImpl<Book> implements BookDao {


    @Override
    public int getRowsNumberWithAttributes(Map<String, Object> hasAttributes, Map<String, String> includingAttributes, Map<String, String> choosingAttributes) {
        EntityManager entityManager = null;
        int result;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            //CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);
            CriteriaQuery<Long> criteriaQueryCount = criteriaBuilder.createQuery(Long.class);
            Root<Book> bookRoot = criteriaQueryCount.from(Book.class);
            List<Predicate> predicates = new ArrayList();
            if (hasAttributes != null && !hasAttributes.isEmpty()) {
                for (Map.Entry<String, Object> entry : hasAttributes.entrySet()) {
                    Predicate predicate = criteriaBuilder.equal(bookRoot.get(entry.getKey()), entry.getValue());
                    predicates.add(predicate);
                }
            }
            if (includingAttributes != null && !includingAttributes.isEmpty()) {
                for (Map.Entry<String, String> entry : includingAttributes.entrySet()) {
                    Predicate predicate = criteriaBuilder.like(bookRoot.get(entry.getKey()), "%"+entry.getValue()+"%");
                    predicates.add(predicate);
                }
            }
            if (choosingAttributes != null && !choosingAttributes.isEmpty()) {
                for (Map.Entry<String, String> entry : choosingAttributes.entrySet()) {
                    Predicate predicate = criteriaBuilder.like(bookRoot.get(entry.getKey()), "%"+entry.getValue()+"%");
                    predicates.add(predicate);
                }
            }
            criteriaQueryCount.select(criteriaBuilder.count(bookRoot));
            criteriaQueryCount.where(predicates.toArray(new Predicate[] {}));
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
    public List searchWithAttributes(Map<String, Object> hasAttributes, Map<String, String> includingAttributes,
                                     Map<String, String> choosingAttributes, Pagination pagination) {
        EntityManager entityManager = null;
        List<Book> result = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);
            Root<Book> bookRoot = criteriaQuery.from(Book.class);
            List<Predicate> includingPredicates = new ArrayList();
            List<Predicate> choosingPredicates = new ArrayList();
            if (hasAttributes != null && !hasAttributes.isEmpty()) {
                for (Map.Entry<String, Object> entry : hasAttributes.entrySet()) {
                    Predicate predicate = criteriaBuilder.equal(bookRoot.get(entry.getKey()), entry.getValue());
                    includingPredicates.add(predicate);
                }
            }
            if (includingAttributes != null && !includingAttributes.isEmpty()) {
                for (Map.Entry<String, String> entry : includingAttributes.entrySet()) {
                    Predicate predicate = criteriaBuilder.like(bookRoot.get(entry.getKey()), "%"+entry.getValue()+"%");
                    includingPredicates.add(predicate);
                }
            }
            if (choosingAttributes != null && !choosingAttributes.isEmpty()) {
                for (Map.Entry<String, String> entry : choosingAttributes.entrySet()) {
                    Predicate predicate = criteriaBuilder.like(bookRoot.get(entry.getKey()), "%"+entry.getValue()+"%");
                    choosingPredicates.add(predicate);
                }
            }
            if(!includingPredicates.isEmpty() && !choosingPredicates.isEmpty()) {
                Predicate and = criteriaBuilder.and(includingPredicates.toArray(new Predicate[] {}));
                Predicate or = criteriaBuilder.or(choosingPredicates.toArray(new Predicate[] {}));
                criteriaQuery.select(bookRoot).where(and, or);
            } else if(!includingPredicates.isEmpty()) {
                Predicate and = criteriaBuilder.and(includingPredicates.toArray(new Predicate[] {}));
                criteriaQuery.select(bookRoot).where(and);
            } else if(!choosingPredicates.isEmpty()) {
                Predicate or = criteriaBuilder.or(choosingPredicates.toArray(new Predicate[] {}));
                criteriaQuery.select(bookRoot).where(or);
            } else {
                criteriaQuery.select(bookRoot);
            }
            criteriaQuery.orderBy(criteriaBuilder.desc(bookRoot.get(Book_.bookId)));
            Query query = entityManager.createQuery(criteriaQuery);
            query.setFirstResult(pagination.getOffset());
            if(pagination.getPageSize() != 0) {
                query.setMaxResults(pagination.getPageSize());
            }

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
    public List searchAndSortWithAttributes(Map<String, Object> hasAttributes,
                                            Map<String, String> includingAttributes,
                                            Map<String, String> choosingAttributes, String sortType,
                                            Pagination pagination) {
        return null;
    }
}
