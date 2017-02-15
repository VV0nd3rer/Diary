package com.kverchi.diary.dao.impl;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.kverchi.diary.dao.UserDao;
import com.kverchi.diary.domain.User;

@Repository
public class UserDaoImpl extends GenericDaoImpl<User> implements UserDao{
	final static Logger logger = Logger.getLogger(UserDaoImpl.class);
	
	@Override
	public User getUserByUsername(String username) {
		Session session = null;
		User user = null;
		try { 
	    	   session = sessionFactory.openSession();
	    	   String query = " FROM User u WHERE u.username = :username";
	    	   Query hQuery = session.createQuery(query);
	    	   hQuery.setParameter("username", username);   
	    	   if(hQuery.list().size() > 0) {
	    		   user = (User)hQuery.list().get(0);
	    	   }
	           
		} catch (Exception e) {
			logger.error(e.getMessage());
		} 
		finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return user;
	}

	@Override
	public User getUserByEmail(String email) {
		Session session = null;
		User user = null;
		try { 
	    	   session = sessionFactory.openSession();
	    	   String query = " FROM User u WHERE u.email = :email";
	    	   Query hQuery = session.createQuery(query);
	    	   hQuery.setParameter("email", email);   
	    	   if(hQuery.list().size() > 0) {
	    		   user = (User)hQuery.list().get(0);
	    	   }
	           
		} catch (Exception e) {
			logger.error(e.getMessage());
		} 
		finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return user;
	}

}
