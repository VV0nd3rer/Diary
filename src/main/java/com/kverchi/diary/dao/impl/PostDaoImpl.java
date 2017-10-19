package com.kverchi.diary.dao.impl;


import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.kverchi.diary.domain.Pagination;
import com.kverchi.diary.domain.PostSearchAttributes;
import com.kverchi.diary.domain.SearchAttributes;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kverchi.diary.custom.exception.DatabaseException;
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
            Hibernate.initialize(obj.getPostComments());
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
            criteriaQuery.orderBy(builder.desc(obj.get("postDatetime")));
            TypedQuery<Post> query = entityManager.createQuery(criteriaQuery);
            objList = query.getResultList();

            for (Post post : objList) {
                Hibernate.initialize(post.getPostComments());
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
            String str_query = "FROM Post post WHERE post.countriesSight.sightId = :sightId";
            Query query = entityManager.createQuery(str_query);
            query.setParameter("sightId", sight_id);
            sight_posts = query.getResultList();

            for (Post post : sight_posts) {
                Hibernate.initialize(post.getPostComments());
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
    public int getRowsNumberWithAttributes(Map<String, Object> hasAttributes, Map<String, String> includingAttributes, Map<String, String> choosingAttributes) {
        EntityManager entityManager = null;
        int rowsNumber = 0;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            StringBuilder str_query = new StringBuilder("select count(*) from Post post");
            if (hasAttributes != null && !hasAttributes.isEmpty()) {
                str_query = generateExactAttrSqlString(str_query, hasAttributes.keySet());
            }
            if(includingAttributes != null && !includingAttributes.isEmpty()) {
                str_query = generateIncludingStringAttrSqlString(str_query, includingAttributes.keySet());
            }
            if(choosingAttributes != null && !choosingAttributes.isEmpty()) {
                str_query = generateChoosingStringAttrSqlString(str_query, choosingAttributes.keySet());
            }
            Query query = entityManager.createQuery(str_query.toString());
            if (hasAttributes != null && !hasAttributes.isEmpty()) {
                for (Map.Entry<String, Object> entry : hasAttributes.entrySet()) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }
            if (includingAttributes != null && !includingAttributes.isEmpty()) {
                for (Map.Entry<String, String> entry : includingAttributes.entrySet()) {
                    query.setParameter(entry.getKey(), "%"+entry.getValue().toString()+"%");
                }
            }
            if (choosingAttributes != null && !choosingAttributes.isEmpty()) {
                for (Map.Entry<String, String> entry : choosingAttributes.entrySet()) {
                    query.setParameter(entry.getKey(), "%"+entry.getValue().toString()+"%");
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
    public List searchWithAttributes(Map<String, Object> hasAttributes, Map<String, String> includingAttributes,
                                     Map<String, String> choosingAttributes, Pagination pagination) {
        EntityManager entityManager = null;
        List<Post> limitedPosts = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            StringBuilder sqlQuery = new StringBuilder("FROM Post post");
            if (hasAttributes != null && !hasAttributes.isEmpty()) {
                sqlQuery = generateExactAttrSqlString(sqlQuery, hasAttributes.keySet());
            }
            if(includingAttributes != null && !includingAttributes.isEmpty()) {
                sqlQuery = generateIncludingStringAttrSqlString(sqlQuery, includingAttributes.keySet());
            }
            if(choosingAttributes != null && !choosingAttributes.isEmpty()) {
                sqlQuery = generateChoosingStringAttrSqlString(sqlQuery, choosingAttributes.keySet());
            }
            sqlQuery.append(" order by postDatetime desc");
            Query query = entityManager.createQuery(sqlQuery.toString());
            if (hasAttributes != null && !hasAttributes.isEmpty()) {
                for (Map.Entry<String, Object> entry : hasAttributes.entrySet()) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }
            if (includingAttributes != null && !includingAttributes.isEmpty()) {
                for (Map.Entry<String, String> entry : includingAttributes.entrySet()) {
                    query.setParameter(entry.getKey(), "%"+entry.getValue().toString()+"%");
                }
            }
            if(choosingAttributes != null && !choosingAttributes.isEmpty()) {
                for (Map.Entry<String, String> entry : choosingAttributes.entrySet()) {
                    query.setParameter(entry.getKey(), "%" + entry.getValue().toString() + "%");
                }
            }
            query.setFirstResult(pagination.getOffset());
            if(pagination.getPageSize() != 0) {
                query.setMaxResults(pagination.getPageSize());
            }
            limitedPosts = query.getResultList();

            for (Post post : limitedPosts) {
                Hibernate.initialize(post.getPostComments());
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
    public List searchAndSortWithAttributes(Map<String, Object> hasAttributes,
                                            Map<String, String> includingAttributes,
                                            Map<String, String> choosingAttributes,
                                            String sortType, Pagination pagination) {
        EntityManager entityManager = null;
        List<Post> limitedPosts = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            StringBuilder sqlQuery = new StringBuilder("select post" +
                    " from Post post" +
                    " left join " + sortType + " counter " +
                    " on post.countriesSight.sightId = counter.countriesSight.sightId");
            if (hasAttributes != null && !hasAttributes.isEmpty()) {
                sqlQuery = generateExactAttrSqlString(sqlQuery, hasAttributes.keySet());
            }
            if(includingAttributes != null && !includingAttributes.isEmpty()) {
                sqlQuery = generateIncludingStringAttrSqlString(sqlQuery, includingAttributes.keySet());
            }
            if(choosingAttributes != null && !choosingAttributes.isEmpty()) {
                sqlQuery = generateChoosingStringAttrSqlString(sqlQuery, choosingAttributes.keySet());
            }
            sqlQuery.append(" group by post.postId" +
                    " order by count(counter.countriesSight.sightId) desc," +
                    " post.countriesSight.sightId");

            Query query = entityManager.createQuery(sqlQuery.toString());
            if (hasAttributes != null && !hasAttributes.isEmpty()) {
                for (Map.Entry<String, Object> entry : hasAttributes.entrySet()) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }
            if (includingAttributes != null && !includingAttributes.isEmpty()) {
                for (Map.Entry<String, String> entry : includingAttributes.entrySet()) {
                    query.setParameter(entry.getKey(), "%"+entry.getValue().toString()+"%");
                }
            }
            if(choosingAttributes != null && !choosingAttributes.isEmpty()) {
                for (Map.Entry<String, String> entry : choosingAttributes.entrySet()) {
                    query.setParameter(entry.getKey(), "%" + entry.getValue().toString() + "%");
                }
            }
            query.setFirstResult(pagination.getOffset());
            if(pagination.getPageSize() != 0) {
                query.setMaxResults(pagination.getPageSize());
            }
            limitedPosts = query.getResultList();

            for (Post post : limitedPosts) {
                Hibernate.initialize(post.getPostComments());
                Hibernate.initialize(post.getCountriesSight());
                Hibernate.initialize(post.getUser());
            }
            entityManager.getTransaction().commit();
        }catch (PersistenceException e) {
            logger.error("DBException: message -> " + e.getMessage() + " cause -> " + e.getCause());
            throw new DatabaseException(e);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
        return limitedPosts;
    }

    private StringBuilder generateExactAttrSqlString(StringBuilder str_query, Set<String> exactSet) {
        for (String key : exactSet) {
            if (str_query.indexOf(" where") == -1) {
                str_query.append(" where ");
            } else {
                str_query.append(" and ");
            }
            String column = key;
            if(key.equals("userId")) {
                column = "post.user."+key;
            } else
            if(key.equals("sightId")) {
                column = "post.countriesSight."+key;
            }
            str_query.append(column + "= :" + key);
        }
        return str_query;
    }
    private StringBuilder generateIncludingStringAttrSqlString(StringBuilder str_query, Set<String> includingSet) {
        for (String key : includingSet) {
            if(str_query.indexOf(" where") == -1) {
                str_query.append(" where ");
            }
            else {
                str_query.append(" and ");
            }
            str_query.append(key + " like :" + key);
        }
        return str_query;
    }
    private StringBuilder generateChoosingStringAttrSqlString(StringBuilder str_query, Set<String> choosingSet) {
        boolean isAlreadyAttrQuery = false;
        boolean isFirstItr = true;
        if(str_query.indexOf(" where") != -1) {
            isAlreadyAttrQuery = true;
            str_query.append(" and ( ");
        }

        for(String key : choosingSet) {

            if(str_query.indexOf(" where") == -1) {
                str_query.append(" where ");
            }
            else {
                if(!isFirstItr) {
                    str_query.append(" or ");
                }
            }
            str_query.append(key + " like :" + key);
            isFirstItr = false;
        }
        if(isAlreadyAttrQuery) {
            str_query.append(" ) ");
        }
        return str_query;
    }
}
