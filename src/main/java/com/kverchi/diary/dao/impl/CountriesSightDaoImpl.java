package com.kverchi.diary.dao.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.kverchi.diary.domain.CountriesSight_;
import com.kverchi.diary.domain.Pagination;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import javax.persistence.metamodel.EntityType;
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

	@Override
	public int getRowsNumber(Map<String, Object> hasAttributes, Map<String, String> containsAttributes) {
		return 0;
	}

	@Override
	public List search(Map<String, Object> hasAttributes, Map<String, String> containsAttributes, Pagination pagination) {
		return null;
	}


	/*@Override
	public List searchRows(Map<String, Object> search_criteria, int limit, int offset) throws DatabaseException {
		EntityManager entityManager = null;
		List<CountriesSight> result = null;
		try {
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			//order by post_datetime
			StringBuilder str_query = new StringBuilder("FROM CountriesSight");
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
			//str_query.append(" order by post_datetime desc");
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
	}*/

	/*@Override
	public List searchRows(String search_str) throws DatabaseException {
		EntityManager entityManager = null;
		List<CountriesSight> result = null;
		try {
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<CountriesSight> criteriaQuery = criteriaBuilder.createQuery(CountriesSight.class);
			Root<CountriesSight> resultsRoot = criteriaQuery.from(CountriesSight.class);
			//EntityType<CountriesSight> CountriesSight_ = resultsRoot.getModel();
			criteriaQuery.where(criteriaBuilder.like(resultsRoot.get(CountriesSight_.sight_label), '%'+search_str+'%'));
			Query query = entityManager.createQuery(criteriaQuery);
			result = query.getResultList();
			for(CountriesSight sight : result) {
				logger.debug("sight label: " + sight.getSight_label());
				logger.debug("sight ID: " + sight.getSight_id());
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
		return result;
	}*/

}
