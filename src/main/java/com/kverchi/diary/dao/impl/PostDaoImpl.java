package com.kverchi.diary.dao.impl;


import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kverchi.diary.dao.PostDao;
import com.kverchi.diary.domain.Post;

@Repository
public class PostDaoImpl extends GenericDaoImpl<Post> implements PostDao {
	
	
	final static Logger logger = Logger.getLogger(PostDaoImpl.class);

	@Override
	public List<Post> getSightPosts(int sight_id) {
		Session session = null;
		List<Post> sight_posts = null;
		try { 
	    	   session = sessionFactory.openSession();
	    	   /* String query = "select Country.country_code, Country.country_name, CountriesSight.sight_label, CountriesSight.sight_description "
	    	   + "from Country, CountriesSight"
	    	   + "where  Country.country_code=CountriesSight.country_code and Country.country_code = :code";*/
	    	   String query = " FROM Post p WHERE p.sight_id = :sight_id";
	    	   Query hQuery = session.createQuery(query);
	    	   hQuery.setParameter("sight_id", sight_id);   
	    	   sight_posts = hQuery.list();
	    	   
		}
		catch (Exception e) {
			logger.error(e.getMessage());
		} 
		finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return sight_posts;
	}
	
	
	

}
