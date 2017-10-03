package com.kverchi.diary.service.impl;

import com.kverchi.diary.dao.UserActivityDao;
import com.kverchi.diary.domain.UserActivityLog;
import com.kverchi.diary.service.UserActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Liudmyla Melnychuk on 26.9.2017.
 */
@Service("userActivityLogService")
public class UserActivityLogServiceImpl implements UserActivityLogService {
    @Autowired
    private UserActivityDao userActivityDao;

    @Override
    public void addUserActivityLog(UserActivityLog userActivityLog) {
        userActivityDao.persist(userActivityLog);
    }

    @Override
    public UserActivityLog getUserActivity(String session_id) {
        return userActivityDao.getById(session_id);
    }

    @Override
    public UserActivityLog getLastUserActivity(int user_id) {
        return userActivityDao.getLastUserActivity(user_id);
    }

    @Override
    public void updateUserActivityLog(UserActivityLog userActivityLog) {
        userActivityDao.update(userActivityLog);
    }

}
