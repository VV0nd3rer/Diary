package com.kverchi.diary.dao.impl;

import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kverchi.diary.dao.CountryDao;
import com.kverchi.diary.domain.CountriesSight;
import com.kverchi.diary.domain.Country;
@Repository
public class CountryDaoImpl extends GenericDaoImpl<Country> implements CountryDao {
	final static Logger logger = Logger.getLogger(CountryDaoImpl.class);
	@Transactional
	@Override
	public List<CountriesSight> getCountrySights(String contry_code) {
		Session session = null;
		List<CountriesSight> sights = null;
		try {
	    	   session = sessionFactory.openSession();
	    	  /* String query = "select Country.country_code, Country.country_name, CountriesSight.sight_label, CountriesSight.sight_description "
	    	   + "from Country, CountriesSight"
	    	   + "where  Country.country_code=CountriesSight.country_code and Country.country_code = :code";*/
	    	   String query = " FROM CountriesSight cs WHERE cs.country_code = :code";
	    	   Query hQuery = session.createQuery(query);
	    	   hQuery.setParameter("code", contry_code);   
	           sights = hQuery.list();
	          
	       } 
	       catch (Exception e) {
	    	   logger.error(e.getMessage());
	       } 
	       finally {
	           if (session != null && session.isOpen()) {
	               session.close();
	           }
	       }
		return sights;
	}

}
