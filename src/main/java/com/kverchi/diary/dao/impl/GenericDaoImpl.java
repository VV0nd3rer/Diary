package com.kverchi.diary.dao.impl;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
	public T persist(T t) {
		EntityManager entityManager = null; 
		try {
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			entityManager.persist(t);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (entityManager != null && entityManager.isOpen()) {
				entityManager.close();
			}
		}
		return t;
	}
	@Transactional
	@Override
	public T getById(Serializable id) {
		EntityManager entityManager = null; 
		T obj = null;
		try {
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			obj = (T) entityManager.find(type, id);
			entityManager.getTransaction().commit();
		} catch(Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (entityManager != null && entityManager.isOpen()) {
				entityManager.close();
	        }
		}
		return obj;
	}
	
	@Transactional
	@Override
	public boolean update(T t) {
		EntityManager entityManager = null; 
		try {
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			entityManager.merge(t);
			entityManager.getTransaction().commit();
			return true;
		} catch(Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (entityManager != null && entityManager.isOpen()) {
				entityManager.close();
	           }
		}
		return false;
	}
	@Transactional
	@Override
	public void delete(T t) {
		EntityManager entityManager = null; 
		try {
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			entityManager.remove(entityManager.merge(t));
			entityManager.getTransaction().commit();
			
		} catch(Exception e) {
			logger.error(e.getMessage());	
			e.printStackTrace();
		} finally {
			if (entityManager != null && entityManager.isOpen()) {
				entityManager.close();
	           }
		}
		
	}
	@Transactional
	@Override
	public List<T> getAllRecords() {
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
	 } catch (Exception e) {
		 logger.error(e.getMessage());
		 e.printStackTrace();
	 } finally {
		 if (entityManager != null && entityManager.isOpen()) {
				entityManager.close();
	     }
	 }
	 return objList;
	}
	
}
