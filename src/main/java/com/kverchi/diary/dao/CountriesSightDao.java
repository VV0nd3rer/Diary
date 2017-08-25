package com.kverchi.diary.dao;

import java.util.List;

import com.kverchi.diary.custom.exception.DatabaseException;
import com.kverchi.diary.domain.CountriesSight;

public interface CountriesSightDao extends GenericDao<CountriesSight>, SearchDao {
	List<CountriesSight> getCountrySights(String contry_code) throws DatabaseException;
	CountriesSight getSightByCoord(float x, float y) throws DatabaseException;

}
