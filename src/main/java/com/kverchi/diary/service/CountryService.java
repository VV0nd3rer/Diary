package com.kverchi.diary.service;

import java.util.List;

import com.kverchi.diary.domain.CountriesSight;
import com.kverchi.diary.domain.Country;

public interface CountryService {
	public List<Country> getAllCountries();
	public Country getCountryById(String country_id);
	public List<CountriesSight> getCountrySights(String country_id);
}
