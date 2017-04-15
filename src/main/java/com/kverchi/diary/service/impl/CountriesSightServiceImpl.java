package com.kverchi.diary.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kverchi.diary.dao.CountriesSightDao;
import com.kverchi.diary.domain.CountriesSight;
import com.kverchi.diary.service.CountriesSightService;

@Service
public class CountriesSightServiceImpl implements CountriesSightService {
	@Autowired
	private CountriesSightDao countriesSightDao; 
	
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
		/*int addedId = (Integer)countriesSightDao.create(sight);
		CountriesSight addedSight = countriesSightDao.getById(addedId);*/
		CountriesSight addedSight = (CountriesSight)countriesSightDao.persist(sight);
		return addedSight;
	}

}
