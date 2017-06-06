package com.kverchi.diary.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kverchi.diary.dao.CountriesSightDao;
import com.kverchi.diary.domain.CountriesSight;
import com.kverchi.diary.domain.Country;
import com.kverchi.diary.service.CountriesSightService;
import com.kverchi.diary.service.CountryService;

@Service
public class CountriesSightServiceImpl implements CountriesSightService {
	@Autowired
	private CountriesSightDao countriesSightDao; 
	@Autowired
	private CountryService countryService;
	
	@Override
	public CountriesSight getSightById(int sight_id) {
		return countriesSightDao.getById(sight_id);
	}
	@Override
	public List<CountriesSight> getCountrySights(String country_id) {
		List<CountriesSight> countries_sight = countriesSightDao.getCountrySights(country_id);
		return countries_sight;
	}
	@Override
	public void deleteSight(int sight_id) {
		CountriesSight sightToDel = getSightById(sight_id);
		countriesSightDao.delete(sightToDel);
	}

	@Override
	public CountriesSight updateSight(CountriesSight sight) {
		countriesSightDao.update(sight);
		CountriesSight updatedSight = countriesSightDao.getById(sight.getSight_id());
		return updatedSight;
	}

	@Override
	public CountriesSight addSight(CountriesSight sight) {
		CountriesSight addedSight = null;
		Country country = sight.getCountry(); 
		if(country == null) {
			return addedSight;
		}
		Country countryFromDb = countryService.getCountryById(country.getCountry_code());
		if(countryFromDb == null) {
			countryService.addCountry(country);
			sight.setCountry(country);
		}
		addedSight = (CountriesSight)countriesSightDao.persist(sight);
		return addedSight;
	}
	@Override
	public CountriesSight getSightByCoord(float x, float y) {
		CountriesSight sight = countriesSightDao.getSightByCoord(x, y);
		return sight;
	}
	@Override
	public List<CountriesSight> getAllSights() {
		List <CountriesSight> sights = countriesSightDao.getAllRecords();
		return sights;
	}

}
