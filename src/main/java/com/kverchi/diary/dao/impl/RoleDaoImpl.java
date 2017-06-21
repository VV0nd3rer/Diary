package com.kverchi.diary.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.kverchi.diary.custom.exception.DatabaseException;
import com.kverchi.diary.dao.RoleDao;
import com.kverchi.diary.domain.Role;
import com.kverchi.diary.domain.User;

@Repository
public class RoleDaoImpl extends GenericDaoImpl<Role> implements RoleDao {

	@Override
	public Role getByName(String role_name) throws DatabaseException {
		EntityManager entityManager = null; 
		Role role = null;
		try { 
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			String str_query = " FROM Role r WHERE r.role = :role";
			Query query = entityManager.createQuery(str_query);
			query.setParameter("role", role_name);   
			role = (Role)query.getSingleResult();
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
		return role;
	}
	
}
