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

import com.kverchi.diary.domain.Pagination;
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
    public int getRowsNumber(Map<String, Object> hasAttributes, Map<String, String> containsAttributes) throws DatabaseException {
        EntityManager entityManager = null;
        int rowsNumber = 0;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            StringBuilder str_query = new StringBuilder("select count(*) from Post");
            if (hasAttributes != null && !hasAttributes.isEmpty()) {

                for (String key : hasAttributes.keySet()) {
                    if(str_query.indexOf(" where") == -1) {
                        str_query.append(" where ");
                    }
                    else {
                        str_query.append(" and ");
                    }
                    str_query.append(key + "= :" + key);
                }
            }
            if(containsAttributes != null && !containsAttributes.isEmpty()) {
                for(String key : containsAttributes.keySet()) {
                    if(str_query.indexOf(" where") == -1) {
                        str_query.append(" where ");
                    }
                    else {
                        str_query.append(" and ");
                    }
                    str_query.append(key + " like ?");
                }

            }
            Query query = entityManager.createQuery(str_query.toString());
            if (hasAttributes != null && !hasAttributes.isEmpty()) {
                for (Map.Entry<String, Object> entry : hasAttributes.entrySet()) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }
            if (containsAttributes != null && !containsAttributes.isEmpty()) {
                for (Map.Entry<String, String> entry : containsAttributes.entrySet()) {
                    query.setParameter(entry.getKey(), "%"+entry.getValue()+"%");
                }
            }
            logger.debug("single result: " + query.getSingleResult());
            rowsNumber = ((Long) query.getSingleResult()).intValue();
            entityManager.getTransaction().commit();
        } catch (PersistenceException e) {
            logger.error("DBException: message -> " + e.getMessage() + " cause -> " + e.getCause());
            throw new DatabaseException(e);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
        return rowsNumber;
    }

    @Override
    public List search(Map<String, Object> hasAttributes, Map<String, String> containsAttributes, Pagination pagination) {
        EntityManager entityManager = null;
        List<Post> limitedPosts = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            //order by post_datetime
            StringBuilder str_query = new StringBuilder("FROM Post");
            if (hasAttributes != null && !hasAttributes.isEmpty()) {
                for (String key : hasAttributes.keySet()) {
                    if (str_query.indexOf(" where") == -1) {
                        str_query.append(" where ");
                    } else {
                        str_query.append(" and ");
                    }
                    str_query.append(key + "= :" + key);
                }
            }
            if(containsAttributes != null && !containsAttributes.isEmpty()) {
                for (String key : containsAttributes.keySet()) {
                    if(str_query.indexOf(" where") == -1) {
                        str_query.append(" where ");
                    }
                    else {
                        str_query.append(" and ");
                    }
                    str_query.append(key + " like ?");
                }

            }
            str_query.append(" order by post_datetime desc");
            Query query = entityManager.createQuery(str_query.toString());
            if (hasAttributes != null && !hasAttributes.isEmpty()) {
                for (Map.Entry<String, Object> entry : hasAttributes.entrySet()) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }
            if (containsAttributes != null && !containsAttributes.isEmpty()) {
                for (Map.Entry<String, String> entry : containsAttributes.entrySet()) {
                    query.setParameter(entry.getKey(), "%"+entry.getValue()+"%");
                }
            }
            query.setFirstResult(pagination.getOffset());
            if(pagination.getPageSize() != 0) {
                query.setMaxResults(pagination.getPageSize());
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


}
