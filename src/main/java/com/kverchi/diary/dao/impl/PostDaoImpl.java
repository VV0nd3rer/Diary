package com.kverchi.diary.dao.impl;


import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
			Hibernate.initialize(obj.getUser());
			entityManager.getTransaction().commit();
		} catch(PersistenceException  e) {
			logger.error("DBException: message -> " +  e.getMessage() + " cause -> " + e.getCause());
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
		TypedQuery<Post> query = entityManager.createQuery(criteriaQuery);
	    objList = query.getResultList();
		
	    for(Post post : objList) {
 		   Hibernate.initialize(post.getPost_comments());
 		   Hibernate.initialize(post.getUser());
 	    }
	    entityManager.getTransaction().commit();
	 } catch(PersistenceException  e) {
		 logger.error("DBException: message -> " +  e.getMessage() + " cause -> " + e.getCause());
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
	    	String str_query = " FROM Post p WHERE p.sight_id = :sight_id";
	    	Query query = entityManager.createQuery(str_query);
	    	query.setParameter("sight_id", sight_id);   
	    	sight_posts = query.getResultList();

	    	for(Post post : sight_posts) {
	    		Hibernate.initialize(post.getPost_comments());
	    		Hibernate.initialize(post.getUser());
	    	}
	    	entityManager.getTransaction().commit();
		}
		catch(PersistenceException  e) {
			logger.error("DBException: message -> " +  e.getMessage() + " cause -> " + e.getCause());
			throw new DatabaseException(e);
		} 
		finally {
			if (entityManager != null && entityManager.isOpen()) {
				entityManager.close();
			}
		}
		return sight_posts;
	}

	@Override
	public int getNumOfPosts(Map<String, Object> search_criteria) throws DatabaseException {
		EntityManager entityManager = null; 
		int numOfRows = 0;
		try { 
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
	    	String str_query = "select count(*) from Post";
	    	if(search_criteria != null && !search_criteria.isEmpty()) {
	    		str_query += " where ";
	    		int i = 0;
				for (Map.Entry<String, Object> entry : search_criteria.entrySet())
				{
				   if(i != 0) {
					   str_query += " and ";
				   }
				   str_query += entry.getKey() + "= :" + entry.getKey();
				   i++;
				}
	    	}
	    	Query query = entityManager.createQuery(str_query);
	    	if(search_criteria != null && !search_criteria.isEmpty()) {
	    		for (Map.Entry<String, Object> entry : search_criteria.entrySet()) {
	    			query.setParameter(entry.getKey(), entry.getValue());  
	    		}
	    	}
	    	numOfRows = ((Long)query.getSingleResult()).intValue();
	    	entityManager.getTransaction().commit();
		}
		catch(PersistenceException  e) {
			logger.error("DBException: message -> " +  e.getMessage() + " cause -> " + e.getCause());
			throw new DatabaseException(e);
		} 
		finally {
			if (entityManager != null && entityManager.isOpen()) {
				entityManager.close();
			}
		}
		return numOfRows;
	}

	@Override
	public List<Post> getLimitPosts(int limit, int offset, Map<String, Object> search_criteria) throws DatabaseException {
		EntityManager entityManager = null; 
		List<Post> limitedPosts = null;
		try { 
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			//order by post_datetime
			String str_query = "FROM Post";
			if(search_criteria != null && !search_criteria.isEmpty()) {
				str_query += " where ";
				//final long[] i = {0};
				//search_criteria.forEach((k, v) -> i[0] += k + v);
				//search_criteria.forEach((k,v)->System.out.println("Key : " + k + " Value : " + v));
				int i = 0;
				for (Map.Entry<String, Object> entry : search_criteria.entrySet())
				{
				   if(i != 0) {
					   str_query += " and ";
				   }
				   str_query += entry.getKey() + "= :" + entry.getKey();
				   i++;
				}
			}
			str_query += " order by post_datetime";
	    	Query query = entityManager.createQuery(str_query);
	    	if(search_criteria != null && !search_criteria.isEmpty()) {
	    		for (Map.Entry<String, Object> entry : search_criteria.entrySet()) {
	    			query.setParameter(entry.getKey(), entry.getValue());  
	    		}
	    	}
	    	query.setFirstResult(offset);
	    	query.setMaxResults(limit);
	    	limitedPosts = query.getResultList();

	    	for(Post post : limitedPosts) {
	    		Hibernate.initialize(post.getPost_comments());
	    		Hibernate.initialize(post.getUser());
	    	}
	    	entityManager.getTransaction().commit();
		}
		catch(PersistenceException  e) {
			logger.error("DBException: message -> " +  e.getMessage() + " cause -> " + e.getCause());
			throw new DatabaseException(e);
		} 
		finally {
			if (entityManager != null && entityManager.isOpen()) {
				entityManager.close();
			}
		}
		return limitedPosts;
	}
}
