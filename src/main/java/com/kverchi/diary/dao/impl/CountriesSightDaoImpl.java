package com.kverchi.diary.dao.impl;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kverchi.diary.custom.exception.DatabaseException;
import com.kverchi.diary.dao.CountriesSightDao;
import com.kverchi.diary.domain.CountriesSight;

@Repository
public class CountriesSightDaoImpl extends GenericDaoImpl<CountriesSight>implements CountriesSightDao {
	final static Logger logger = Logger.getLogger(CountriesSightDaoImpl.class);
	
	@Transactional
	@Override
	public List<CountriesSight> getCountrySights(String contry_code) throws DatabaseException {
		EntityManager entityManager = null; 
		List<CountriesSight> sights = null;
		try {
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			String str_query = " FROM CountriesSight cs WHERE cs.country_code = :code";
	    	Query query = entityManager.createQuery(str_query);
	    	query.setParameter("code", contry_code);   
	        sights = query.getResultList();
	        entityManager.getTransaction().commit();
	       } 
	       catch (PersistenceException e) {
	    	   logger.error("DBException: message -> " +  e.getMessage() + " cause -> " + e.getCause());
	    	   throw new DatabaseException(e);
	       } 
	       finally {
	    	   if (entityManager != null && entityManager.isOpen()) {
					entityManager.close();
		       }
	       }
		return sights;
	}

	@Override
	public CountriesSight getSightByCoord(float x, float y) throws DatabaseException {
		EntityManager entityManager = null; 
		CountriesSight sight = null;
		try {
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			String str_query = " FROM CountriesSight cs WHERE cs.map_coord_x = :x AND cs.map_coord_y = :y";
	    	Query query = entityManager.createQuery(str_query);
	    	query.setParameter("x", x);   
	    	query.setParameter("y", y);
	        sight = (CountriesSight) query.getSingleResult();
	        entityManager.getTransaction().commit();
	       } 
	       catch (PersistenceException e) {
	    	   logger.error("DBException: message -> " +  e.getMessage() + " cause -> " + e.getCause());
	    	   throw new DatabaseException(e);
	       } 
	       finally {
	    	   if (entityManager != null && entityManager.isOpen()) {
					entityManager.close();
		       }
	       }
		return sight;
	}
}
