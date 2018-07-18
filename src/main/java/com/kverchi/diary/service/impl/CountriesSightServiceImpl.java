package com.kverchi.diary.service.impl;

import com.kverchi.diary.model.entity.CountriesSight;
import com.kverchi.diary.repository.CountriesSightRepository;
import com.kverchi.diary.service.CountriesSightService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Kverchi on 3.7.2018.
 */
@Service
public class CountriesSightServiceImpl implements CountriesSightService {
    final static Logger logger = LogManager.getLogger(CountriesSightServiceImpl.class);
    @Autowired
    CountriesSightRepository countriesSightRepository;

    @Override
    public List<CountriesSight> findAll() {
        return countriesSightRepository.findAll();
    }

    @Override
    public List<CountriesSight> findByCountryCode(String countryCode) {
        return countriesSightRepository.findByCountryCountryCode(countryCode);
    }

    @Override
    public CountriesSight findByMapCoordXAndMapCoordY(float mapCoordX, float mapCoordY) {
        return countriesSightRepository.findByMapCoordXAndMapCoordY(mapCoordX, mapCoordY);
    }
}
