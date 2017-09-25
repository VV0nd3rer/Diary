package com.kverchi.diary.dao;

import com.kverchi.diary.custom.exception.DatabaseException;
import com.kverchi.diary.domain.User;

import java.util.Map;

public interface UserDao extends GenericDao<User> {
	User getUserByUsername(String user) throws DatabaseException;
	User getUserByEmail(String email) throws DatabaseException;
	boolean isRecordPresent(String key, Object value) throws DatabaseException;
	void updateUserInfo(int user_id, String info) throws DatabaseException;
}
