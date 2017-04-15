package com.kverchi.diary.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kverchi.diary.dao.CountryDao;
import com.kverchi.diary.domain.CountriesSight;
import com.kverchi.diary.domain.Country;
@Repository
public class CountryDaoImpl extends GenericDaoImpl<Country> implements CountryDao {
	
}
