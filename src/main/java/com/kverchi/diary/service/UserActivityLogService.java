package com.kverchi.diary.service;

import com.kverchi.diary.domain.UserActivityLog;

/**
 * Created by Liudmyla Melnychuk on 26.9.2017.
 */
public interface UserActivityLogService {
    void addUserActivityLog(UserActivityLog userActivityLog);
    UserActivityLog getUserActivity(String session_id);
    UserActivityLog getLastUserActivity(int user_id);
    void updateUserActivityLog(UserActivityLog userActivityLog);
}
