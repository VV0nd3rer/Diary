package com.kverchi.diary.service;

import com.kverchi.diary.domain.ServiceResponse;
import com.kverchi.diary.domain.SightWishCounter;
import com.kverchi.diary.enums.Counter;

/**
 * Created by Kverchi on 13.9.2017.
 */
public interface CounterService {
    int getCounterValue(int sight_id, Counter counter);
    ServiceResponse addCounterValue(int sight_id, int user_id, Counter counter);
    boolean isCounterValueExists(int sight_id, int user_id, Counter counter);
}
