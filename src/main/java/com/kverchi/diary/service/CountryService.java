package com.kverchi.diary.service;

import java.util.List;

import com.kverchi.diary.domain.CountriesSight;
import com.kverchi.diary.domain.Country;

public interface CountryService {
	List<Country> getAllCountries();
	Country getCountryById(String country_id);
	void addCountry(Country country);
}
