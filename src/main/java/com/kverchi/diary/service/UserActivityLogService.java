package com.kverchi.diary.service;

import com.kverchi.diary.domain.UserActivityLog;

import java.util.List;

/**
 * Created by Liudmyla Melnychuk on 26.9.2017.
 */
public interface UserActivityLogService {
    void addUserActivityLog(UserActivityLog userActivityLog);
    UserActivityLog getUserActivity(String session_id);
    List<UserActivityLog> getLastUserActivity(int user_id);
    void updateUserActivityLog(UserActivityLog userActivityLog);
}
