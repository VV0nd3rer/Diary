package com.kverchi.diary.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import com.kverchi.diary.domain.*;
import org.apache.log4j.Logger;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.kverchi.diary.custom.exception.DatabaseException;
import com.kverchi.diary.dao.UserDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoImpl extends GenericDaoImpl<User> implements UserDao{
	final static Logger logger = Logger.getLogger(UserDaoImpl.class);
	
	@Override
	public User getUserByUsername(String username) throws DatabaseException {
		EntityManager entityManager = null; 
		User user = null;
		try { 
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			String str_query = " FROM User u WHERE u.username = :username";
			Query query = entityManager.createQuery(str_query);
			query.setParameter("username", username);
			List<User> res_list = query.getResultList();
			if(!res_list.isEmpty()) {
				user = res_list.get(0);
			}
	    	entityManager.getTransaction().commit();   
		} catch(PersistenceException  e) {
			logger.error("DBException: message -> " +  e.getMessage() + " cause -> " + e.getCause());
			throw new DatabaseException(e);
		} 
		finally {
			if (entityManager != null && entityManager.isOpen()) {
				entityManager.close();
			}
		}
		return user;
	}

	@Override
	public User getUserByEmail(String email) throws DatabaseException {
		EntityManager entityManager = null; 
		User user = null;
		try { 
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			String str_query = " FROM User u WHERE u.email = :email";
			Query query = entityManager.createQuery(str_query);
			query.setParameter("email", email);
			List<User> res_list = query.getResultList();
			if(!res_list.isEmpty()) {
				user = res_list.get(0);
			}
	    	entityManager.getTransaction().commit();
		} catch(PersistenceException  e) {
			logger.error("DBException: message -> " +  e.getMessage() + " cause -> " + e.getCause());
			throw new DatabaseException(e);
		} 
		finally {
			if (entityManager != null && entityManager.isOpen()) {
				entityManager.close();
			}
		}
		return user;
	}

	@Override
	public void updateUserInfo(int userId, String info) throws DatabaseException {
		EntityManager entityManager = null;
		try {
			entityManager = entityManagerFactory.createEntityManager();
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaUpdate<User> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(User.class);
			Root<User> userRoot = criteriaUpdate.from(User.class);
			Predicate predicate = criteriaBuilder.equal(userRoot.get("userId"), userId);
			criteriaUpdate.where(predicate);
			criteriaUpdate.set(userRoot.get("information"), info);
			entityManager.getTransaction().begin();
			Query query = entityManager.createQuery(criteriaUpdate);
			int rowsAffected = query.executeUpdate();
			entityManager.getTransaction().commit();

		} catch (PersistenceException e) {
			logger.error("DBException: message -> " + e.getMessage() + " cause -> " + e.getCause());
			throw new DatabaseException(e);
		} finally {
			if (entityManager != null && entityManager.isOpen()) {
				entityManager.close();
			}
		}
	}

	@Override
	public List gerUserWishedSights(int userId) throws DatabaseException {
		EntityManager entityManager = null;
		List<SightWishCounter> resultList = null;
		try {
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			StringBuilder strQuery = generateUserFavoriteCounterSqlString("SightWishCounter");
			strQuery.append(" ORDER BY counter.wishDatetime desc");
			Query query = entityManager.createQuery(strQuery.toString());
			query.setParameter("userId", userId);
			query.setMaxResults(3);
			resultList = query.getResultList();
			for(SightWishCounter counter : resultList) {
				Hibernate.initialize(counter.getCountriesSight());
				Hibernate.initialize(counter.getUser());
			}
			entityManager.getTransaction().commit();
		} catch(PersistenceException  e) {
			logger.error("DBException: message -> " +  e.getMessage() + " cause -> " + e.getCause());
			throw new DatabaseException(e);
		}
		finally {
			if (entityManager != null && entityManager.isOpen()) {
				entityManager.close();
			}
		}
		return resultList;
	}

	@Override
	public List getUserVisitedSights(int userId) throws DatabaseException {
		EntityManager entityManager = null;
		List<SightVisitCounter> resultList = null;
		try {
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			StringBuilder strQuery = generateUserFavoriteCounterSqlString("SightVisitCounter");
			strQuery.append(" ORDER BY counter.visitDatetime desc");
			Query query = entityManager.createQuery(strQuery.toString());
			query.setParameter("userId", userId);
			query.setMaxResults(3);
			resultList = query.getResultList();
			for(SightVisitCounter counter : resultList) {
				Hibernate.initialize(counter.getCountriesSight());
				Hibernate.initialize(counter.getUser());
			}
			entityManager.getTransaction().commit();
		} catch(PersistenceException  e) {
			logger.error("DBException: message -> " +  e.getMessage() + " cause -> " + e.getCause());
			throw new DatabaseException(e);
		}
		finally {
			if (entityManager != null && entityManager.isOpen()) {
				entityManager.close();
			}
		}
		return resultList;
	}
	private StringBuilder generateUserFavoriteCounterSqlString(String counter) {
		/*String strQuery = "SELECT sight.sightId, sight.label, sight.description, country.name" +
				" FROM CountriesSight sight, " + counter + " counter, Country country," +
				" User user" +
				" WHERE sight.sightId = counter.countriesSight.sightId AND user.userId = counter.user.userId " +
				" AND sight.country.countryCode = country.countryCode AND counter.user.userId =:userId";*/
		StringBuilder strQuery = new StringBuilder();
		strQuery.append("SELECT counter FROM CountriesSight sight, " + counter + " counter, Country country," +
				" User user" +
				" WHERE sight.sightId = counter.countriesSight.sightId AND user.userId = counter.user.userId " +
				" AND sight.country.countryCode = country.countryCode AND counter.user.userId =:userId");
		return strQuery;
	}
}
