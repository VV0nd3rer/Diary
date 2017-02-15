package com.kverchi.diary.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.kverchi.diary.dao.RoleDao;
import com.kverchi.diary.domain.Role;
import com.kverchi.diary.domain.User;

@Repository
public class RoleDaoImpl extends GenericDaoImpl<Role> implements RoleDao {

	@Override
	public Role getByName(String role_name) {
		Session session = null;
		Role role = null;
		try { 
	    	   session = sessionFactory.openSession();
	    	   String query = " FROM Role r WHERE r.role = :role_name";
	    	   Query hQuery = session.createQuery(query);
	    	   hQuery.setParameter("role", role_name);   
	    	   if(hQuery.list().size() > 0) {
	    		   role = (Role)hQuery.list().get(0);
	    	   }
	           
		} catch (Exception e) {
			logger.error(e.getMessage());
		} 
		finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return role;
	}
	
}
