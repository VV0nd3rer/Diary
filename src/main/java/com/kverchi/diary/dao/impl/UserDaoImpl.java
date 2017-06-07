package com.kverchi.diary.dao.impl;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import javax.persistence.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.kverchi.diary.dao.UserDao;
import com.kverchi.diary.domain.User;

@Repository
public class UserDaoImpl extends GenericDaoImpl<User> implements UserDao{
	final static Logger logger = Logger.getLogger(UserDaoImpl.class);
	
	@Override
	public User getUserByUsername(String username) {
		EntityManager entityManager = null; 
		User user = null;
		try { 
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			String str_query = " FROM User u WHERE u.username = :username";
			Query query = entityManager.createQuery(str_query);
			query.setParameter("username", username);   
			user = (User)query.getSingleResult();
	    	entityManager.getTransaction().commit();   
		} catch (Exception e) {
			logger.error(e.getMessage());
			e. printStackTrace();
		} 
		finally {
			if (entityManager != null && entityManager.isOpen()) {
				entityManager.close();
			}
		}
		return user;
	}

	@Override
	public User getUserByEmail(String email) {
		EntityManager entityManager = null; 
		User user = null;
		try { 
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			String str_query = " FROM User u WHERE u.email = :email";
			Query query = entityManager.createQuery(str_query);
			query.setParameter("email", email);   
			user = (User)query.getSingleResult();
	    	entityManager.getTransaction().commit();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e. printStackTrace();
		} 
		finally {
			if (entityManager != null && entityManager.isOpen()) {
				entityManager.close();
			}
		}
		return user;
	}

}
