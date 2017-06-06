package com.kverchi.diary.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kverchi.diary.dao.CountryDao;
import com.kverchi.diary.domain.CountriesSight;
import com.kverchi.diary.domain.Country;
import com.kverchi.diary.service.CountryService;
@Service
public class CountryServiceImpl implements CountryService {
	@Autowired 
	CountryDao countryDao;
	@Override
	public List<Country> getAllCountries() {
		List<Country> list = countryDao.getAllRecords();
		return list;
	}
	@Override
	public Country getCountryById(String country_id) {
		Country res = countryDao.getById(country_id);
		return res;
	}
	@Override
	public void addCountry(Country country) {
		countryDao.persist(country);
	}
	

}
