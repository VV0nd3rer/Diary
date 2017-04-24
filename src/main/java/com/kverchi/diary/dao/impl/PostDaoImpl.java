package com.kverchi.diary.dao.impl;


import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
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

import com.kverchi.diary.dao.PostDao;
import com.kverchi.diary.domain.Post;

@Repository
public class PostDaoImpl extends GenericDaoImpl<Post> implements PostDao {
	
	
	final static Logger logger = Logger.getLogger(PostDaoImpl.class);

	@Override
	public Post getById(Serializable id) {
		EntityManager entityManager = null; 
		Post obj = null;
		try {
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			obj = (Post) entityManager.find(Post.class, id);
			Hibernate.initialize(obj.getPost_comments());
			Hibernate.initialize(obj.getUser());
			entityManager.getTransaction().commit();
		} catch(Exception e) {
			logger.error(e.getMessage());
			return obj;
		} finally {
			if (entityManager != null && entityManager.isOpen()) {
				entityManager.close();
	        }
		}
		return obj;
	}
	
	@Transactional
	@Override
	public List<Post> getAllRecords() {
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
	 } catch (Exception e) {
		 logger.error(e.getMessage());
		 return objList;
	 } finally {
		 if (entityManager != null && entityManager.isOpen()) {
				entityManager.close();
	     }
	 }
	 return objList;
	}

	@Override
	public List<Post> getSightPosts(int sight_id) {
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
		catch (Exception e) {
			logger.error(e.getMessage());
			return sight_posts;
		} 
		finally {
			if (entityManager != null && entityManager.isOpen()) {
				entityManager.close();
			}
		}
		return sight_posts;
	}
}
