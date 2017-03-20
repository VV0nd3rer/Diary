package com.kverchi.diary.dao.impl;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.List;

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
	protected SessionFactory sessionFactory;
	
	private Class<T> type;
    
	   
	@SuppressWarnings("unchecked")
	public GenericDaoImpl() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        type = (Class<T>) pt.getActualTypeArguments()[0];
    }
	@Transactional
	@Override
	public Serializable create(T t) {
		Session session = null;
		//Transaction tx = null;
		Serializable id = 0;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			//tx = session.beginTransaction();
			id = session.save(t);
		    //tx.commit();
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (session != null && session.isOpen()) {
	               session.close();
	           }
		}
		return id;
	}
	@Transactional
	@Override
	public T getById(Serializable id) {
		Session session = null;
		T obj = null;
		try {
			session = sessionFactory.openSession();
			obj = (T) session.get(type,id);
		} catch(Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (session != null && session.isOpen()) {
	               session.close();
	           }
		}
		return obj;
	}
	@Transactional
	@Override
	public void update(T t) {
		Session session = null;	
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.update(t);
			session.getTransaction().commit();
		} catch(Exception e) {
			logger.error(e.getMessage());
			
		} finally {
			if (session != null && session.isOpen()) {
	               session.close();
	           }
		}
	}
	@Transactional
	@Override
	public void delete(T t) {
		Session session = null;
		
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.delete(t);
			session.getTransaction().commit();
			
		} catch(Exception e) {
			logger.error(e.getMessage());	
		} finally {
			if (session != null && session.isOpen()) {
	               session.close();
	        }
		}
		
	}
	@Transactional
	@Override
	public List<T> getAllRecords() {
	 Session session = null;
	 List<T> objList = null;
	 try {
	    session = sessionFactory.openSession();
	    objList = session.createCriteria(type).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	 } catch (Exception e) {
		 logger.error(e.getMessage());
	 } finally {
	    if (session != null && session.isOpen()) {
	       session.close();
	    }
	 }
	 return objList;
	}

}
