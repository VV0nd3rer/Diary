package com.kverchi.diary.service;

import java.util.List;

import com.kverchi.diary.domain.CountriesSight;

public interface CountriesSightService {
	CountriesSight getSightById(int sight_id);
	//List<CountriesSight> getCountrySights(String country_id);
	void deleteSight(int sight_id);
	CountriesSight updateSight(CountriesSight sight);
	CountriesSight addSight(CountriesSight sight);
}
