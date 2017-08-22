package com.kverchi.diary.dao.impl;


import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kverchi.diary.custom.exception.DatabaseException;
import com.kverchi.diary.dao.PaginationDao;
import com.kverchi.diary.dao.PostDao;
import com.kverchi.diary.domain.Post;

@Repository
public class PostDaoImpl extends GenericDaoImpl<Post> implements PostDao {


    final static Logger logger = Logger.getLogger(PostDaoImpl.class);

    @Override
    public Post getById(Serializable id) throws DatabaseException {
        EntityManager entityManager = null;
        Post obj = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            obj = (Post) entityManager.find(Post.class, id);
            Hibernate.initialize(obj.getPost_comments());
            Hibernate.initialize(obj.getCountriesSight());
            Hibernate.initialize(obj.getUser());
            entityManager.getTransaction().commit();
        } catch (PersistenceException e) {
            logger.error("DBException: message -> " + e.getMessage() + " cause -> " + e.getCause());
            throw new DatabaseException(e);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
        return obj;
    }

    @Transactional
    @Override
    public List<Post> getAllRecords() throws DatabaseException {
        EntityManager entityManager = null;
        List<Post> objList = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Post> criteriaQuery = builder.createQuery(Post.class);
            Root<Post> obj = criteriaQuery.from(Post.class);
            criteriaQuery.select(obj);
            criteriaQuery.orderBy(builder.desc(obj.get("post_datetime")));
            TypedQuery<Post> query = entityManager.createQuery(criteriaQuery);
            objList = query.getResultList();

            for (Post post : objList) {
                Hibernate.initialize(post.getPost_comments());
                Hibernate.initialize(post.getCountriesSight());
                Hibernate.initialize(post.getUser());
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
        return objList;
    }

    @Override
    public List<Post> getSightPosts(int sight_id) throws DatabaseException {
        EntityManager entityManager = null;
        List<Post> sight_posts = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            String str_query = "FROM Post p WHERE p.countriesSight.sight_id = :sight_id";
            Query query = entityManager.createQuery(str_query);
            query.setParameter("sight_id", sight_id);
            sight_posts = query.getResultList();

            for (Post post : sight_posts) {
                Hibernate.initialize(post.getPost_comments());
                Hibernate.initialize(post.getCountriesSight());
                Hibernate.initialize(post.getUser());
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
        return sight_posts;
    }

    @Override
    public int getNumOfRows(Map<String, Object> search_criteria) throws DatabaseException {
        EntityManager entityManager = null;
        int numOfRows = 0;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            String str_query = "select count(*) from Post";
            if (search_criteria != null && !search_criteria.isEmpty()) {
                str_query += " where ";
                int i = 0;
                for (Map.Entry<String, Object> entry : search_criteria.entrySet()) {
                    if (i != 0) {
                        str_query += " and ";
                    }
                    str_query += entry.getKey() + "= :" + entry.getKey();
                    i++;
                }
            }
            Query query = entityManager.createQuery(str_query);
            if (search_criteria != null && !search_criteria.isEmpty()) {
                for (Map.Entry<String, Object> entry : search_criteria.entrySet()) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }
            logger.debug("single result: " + query.getSingleResult());
            numOfRows = ((Long) query.getSingleResult()).intValue();
            entityManager.getTransaction().commit();
        } catch (PersistenceException e) {
            logger.error("DBException: message -> " + e.getMessage() + " cause -> " + e.getCause());
            throw new DatabaseException(e);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
        return numOfRows;
    }

    @Override
    public int getNumOfRows() throws DatabaseException {
        EntityManager entityManager = null;
        int numOfRows = 0;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            String str_query = "select count(*) from Post";

            Query query = entityManager.createQuery(str_query);

            logger.debug("single result: " + query.getSingleResult());
            numOfRows = ((Long) query.getSingleResult()).intValue();
            entityManager.getTransaction().commit();
        } catch (PersistenceException e) {
            logger.error("DBException: message -> " + e.getMessage() + " cause -> " + e.getCause());
            throw new DatabaseException(e);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
        return numOfRows;
    }
    /*@Override
    public List strBasedSearch(Map<String, Object> search_criteria, int limit, int offset) throws DatabaseException {
        EntityManager entityManager = null;
        List<Post> results = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Post> criteriaQuery = criteriaBuilder.createQuery(Post.class);
            Root<Post> resultsRoot = criteriaQuery.from(Post.class);
            List<Predicate> predicates = new ArrayList<>();
            if (search_criteria != null && !search_criteria.isEmpty()) {
                for (Map.Entry<String, Object> entry : search_criteria.entrySet()) {
                    predicates.add(criteriaBuilder.equal(resultsRoot.get(entry.getKey()), entry.getValue()));
                }
            }
            criteriaQuery.select(resultsRoot)
                    .where(predicates.toArray(new Predicate[]{}));
            Query query = entityManager.createQuery(criteriaQuery);
            query.setFirstResult(offset);
            if(limit != 0) {
                query.setMaxResults(limit);
            }
            results = query.getResultList();

            for (Post post : results) {
                Hibernate.initialize(post.getPost_comments());
                Hibernate.initialize(post.getCountriesSight());
                Hibernate.initialize(post.getUser());
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
        return results;
    }*/

    @Override
    public List searchRows(Map<String, Object> search_criteria, int limit, int offset) throws DatabaseException {
        EntityManager entityManager = null;
        List<Post> limitedPosts = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            //order by post_datetime
            StringBuilder str_query = new StringBuilder("FROM Post");
            if (search_criteria != null && !search_criteria.isEmpty()) {
                str_query.append(" where ");
                //final long[] i = {0};
                //search_criteria.forEach((k, v) -> i[0] += k + v);
                //search_criteria.forEach((k,v)->System.out.println("Key : " + k + " Value : " + v));
                int i = 0;
                for (String key : search_criteria.keySet()) {
                    if (i != 0) {
                        str_query.append(" and ");
                    }
                    str_query.append(key + "= :" + key);
                    i++;
                }
            }
            str_query.append(" order by post_datetime desc");
            Query query = entityManager.createQuery(str_query.toString());
            if (search_criteria != null && !search_criteria.isEmpty()) {
                for (Map.Entry<String, Object> entry : search_criteria.entrySet()) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }
            query.setFirstResult(offset);
            if(limit != 0) {
                query.setMaxResults(limit);
            }
            limitedPosts = query.getResultList();

            for (Post post : limitedPosts) {
                Hibernate.initialize(post.getPost_comments());
                Hibernate.initialize(post.getCountriesSight());
                Hibernate.initialize(post.getUser());
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
        return limitedPosts;
    }

    @Override
    public List getLimitRows(int limit, int offset) throws DatabaseException {
        EntityManager entityManager = null;
        List<Post> limitedPosts = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            //order by post_datetime
            String str_query = "FROM Post";

            str_query += " order by post_datetime desc";
            Query query = entityManager.createQuery(str_query);

            query.setFirstResult(offset);
            query.setMaxResults(limit);
            limitedPosts = query.getResultList();

            for (Post post : limitedPosts) {
                Hibernate.initialize(post.getPost_comments());
                Hibernate.initialize(post.getCountriesSight());
                Hibernate.initialize(post.getUser());
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
        return limitedPosts;
    }
}
