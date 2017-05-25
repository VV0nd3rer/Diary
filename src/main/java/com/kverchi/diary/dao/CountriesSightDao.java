package com.kverchi.diary.dao;

import java.util.List;

import com.kverchi.diary.domain.CountriesSight;

public interface CountriesSightDao extends GenericDao<CountriesSight> {
	List<CountriesSight> getCountrySights(String contry_code);
	CountriesSight getSightByCoord(float x, float y);

}
