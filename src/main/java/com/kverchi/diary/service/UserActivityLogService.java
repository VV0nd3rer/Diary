package com.kverchi.diary.service;

import com.kverchi.diary.domain.UserActivityLog;

/**
 * Created by Kverchi on 26.9.2017.
 */
public interface UserActivityLogService {
    void addUserActivityLog(UserActivityLog userActivityLog);
    UserActivityLog getLastUserActivity(int user_id);
}
