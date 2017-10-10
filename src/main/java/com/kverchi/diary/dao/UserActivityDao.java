package com.kverchi.diary.dao;

import com.kverchi.diary.custom.exception.DatabaseException;
import com.kverchi.diary.domain.UserActivityLog;

import java.util.List;

/**
 * Created by Liudmyla Melnychuk on 18.9.2017.
 */
public interface UserActivityDao extends GenericDao<UserActivityLog> {
    List<UserActivityLog> getLastUserActivity(int user_id) throws DatabaseException;
}
