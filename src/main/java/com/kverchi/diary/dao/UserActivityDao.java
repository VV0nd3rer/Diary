package com.kverchi.diary.dao;

import com.kverchi.diary.custom.exception.DatabaseException;
import com.kverchi.diary.domain.UserActivityLog;

/**
 * Created by Kverchi on 18.9.2017.
 */
public interface UserActivityDao extends GenericDao<UserActivityLog> {
    UserActivityLog getLastUserActivity(int user_id) throws DatabaseException;
}
