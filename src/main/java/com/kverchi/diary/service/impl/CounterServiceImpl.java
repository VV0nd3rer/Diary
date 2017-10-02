package com.kverchi.diary.service.impl;

import com.kverchi.diary.dao.SightVisitCounterDao;
import com.kverchi.diary.dao.SightWishCounterDao;
import com.kverchi.diary.domain.ServiceResponse;
import com.kverchi.diary.domain.SightVisitCounter;
import com.kverchi.diary.domain.SightWishCounter;
import com.kverchi.diary.enums.Counter;
import com.kverchi.diary.enums.ServiceMessageResponse;
import com.kverchi.diary.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Created by Liudmyla Melnychuk on 13.9.2017.
 */
@Service
public class CounterServiceImpl implements CounterService {
    @Autowired
    private SightWishCounterDao sightWishCounterDao;
    @Autowired
    private SightVisitCounterDao sightVisitCounterDao;

    @Override
    public int getCounterValue(int sight_id, Counter counter) {
        int result = 0;
        switch (counter) {
            case WISHES:
                result = sightWishCounterDao.getCounterValue(sight_id);
                break;
            case VISITS:
                result = sightVisitCounterDao.getCounterValue(sight_id);
                break;
        }
        return result;
    }

    @Override
    public ServiceResponse addCounterValue(int sight_id, int user_id, Counter counter) {
        ServiceResponse response =
                new ServiceResponse(HttpStatus.INTERNAL_SERVER_ERROR, ServiceMessageResponse.UKNOWN_PROBLEM.toString());
        Object counterObj = null;
        switch (counter) {
            case WISHES:
                counterObj = sightWishCounterDao.persist( new SightWishCounter(sight_id, user_id));
                break;
            case VISITS:
                counterObj = sightVisitCounterDao.persist( new SightVisitCounter(sight_id, user_id));
                break;
        }
        if(counterObj == null) {
            response.setRespCode(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setRespMsg(ServiceMessageResponse.TRANSACTION_PROBLEM.toString());
        } else {
            response.setRespCode(HttpStatus.OK);
            response.setRespMsg(ServiceMessageResponse.OK.toString());
        }
        return response;
    }

    @Override
    public boolean isCounterValueExists(int sight_id, int user_id, Counter counter) {
        boolean result = false;
        switch (counter) {
            case WISHES:
                result = sightWishCounterDao.isValueExist(sight_id, user_id);
                break;
            case VISITS:
                result = sightVisitCounterDao.isValueExist(sight_id, user_id);
                break;
        }
        return result;
    }
}
