package com.kverchi.diary.dao.impl;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.kverchi.diary.custom.exception.DatabaseException;
import com.kverchi.diary.dao.GenericDao;

public class GenericDaoImpl<T> implements GenericDao<T> {
	
	final static Logger logger = Logger.getLogger(GenericDaoImpl.class);
	
	@Autowired
	protected EntityManagerFactory entityManagerFactory;
	
	private Class<T> type;
    
	@SuppressWarnings("unchecked")
	public GenericDaoImpl() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        type = (Class<T>) pt.getActualTypeArguments()[0];
    }
	@Transactional
	@Override
	public T persist(T t) throws DatabaseException {
		EntityManager entityManager = null; 
		try {
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			entityManager.persist(t);
			entityManager.getTransaction().commit();
		} catch(PersistenceException  e) {
			logger.error("DBException: message -> " +  e.getMessage() + " cause -> " + e.getCause());
			throw new DatabaseException(e);
		} finally {
			if (entityManager != null && entityManager.isOpen()) {
				entityManager.close();
			}
		}
		return t;
	}
	@Transactional
	@Override
	public T getById(Serializable id) throws DatabaseException {
		EntityManager entityManager = null; 
		T obj = null;
		try {
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			obj = (T) entityManager.find(type, id);
			entityManager.getTransaction().commit();
		}catch(PersistenceException  e) {
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
	public boolean update(T t) throws DatabaseException {
		EntityManager entityManager = null; 
		boolean isUpdated = false;
		try {
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			entityManager.merge(t);
			entityManager.getTransaction().commit();
			isUpdated = true;
		} catch(PersistenceException  e) {
			logger.error("DBException: message -> " +  e.getMessage() + " cause -> " + e.getCause());
			throw new DatabaseException(e);
		} finally {
			if (entityManager != null && entityManager.isOpen()) {
				entityManager.close();
	           }
		}
		return isUpdated;
	}

	@Transactional
	@Override
	public boolean updateBatch(List list) throws DatabaseException {
		EntityManager entityManager = null;
		boolean isUpdated = false;
		try {
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			for(Object entity : list) {
				entityManager.merge(entity);
			}
			entityManager.getTransaction().commit();
			isUpdated = true;
		} catch(PersistenceException  e) {
			logger.error("DBException: message -> " +  e.getMessage() + " cause -> " + e.getCause());
			throw new DatabaseException(e);
		} finally {
			if (entityManager != null && entityManager.isOpen()) {
				entityManager.close();
			}
		}
		return isUpdated;
	}

	@Transactional
	@Override
	public void delete(T t) throws DatabaseException {
		EntityManager entityManager = null; 
		try {
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			entityManager.remove(entityManager.merge(t));
			entityManager.getTransaction().commit();
			
		} catch(PersistenceException  e) {
			logger.error("DBException: message -> " +  e.getMessage() + " cause -> " + e.getCause());
			throw new DatabaseException(e);
		} finally {
			if (entityManager != null && entityManager.isOpen()) {
				entityManager.close();
	           }
		}
		
	}
	@Transactional
	@Override
	public List<T> getAllRecords() throws DatabaseException {
	 EntityManager entityManager = null; 
	 List<T> objList = null;
	 try {
		 entityManager = entityManagerFactory.createEntityManager();
		 entityManager.getTransaction().begin();
		 CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		 CriteriaQuery<T> criteriaQuery = builder.createQuery(type);
		 Root<T> obj = criteriaQuery.from(type);
		 criteriaQuery.select(obj);
		 TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
		 objList = query.getResultList();
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
	public boolean isRecordPresent(String key, Object value) throws DatabaseException {
		EntityManager entityManager = null;
		boolean isRecordPresent = false;
		try {
			entityManager = entityManagerFactory.createEntityManager();
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
			Root<T> c = criteriaQuery.from(type);
			EntityType<T> obj = c.getModel();
			criteriaQuery.select(builder.countDistinct(c));
			criteriaQuery.where(
					builder.equal(c.get(obj.getSingularAttribute(key)), value)
			);
			long count = entityManager.createQuery(criteriaQuery).getSingleResult();
			if(count > 0) {
				isRecordPresent = true;
			}

		} catch(PersistenceException  e) {
			logger.error("DBException: message -> " +  e.getMessage() + " cause -> " + e.getCause());
			throw new DatabaseException(e);
		}
		finally {
			if (entityManager != null && entityManager.isOpen()) {
				entityManager.close();
			}
		}
		return isRecordPresent;
	}
}
