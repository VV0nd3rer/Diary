package com.kverchi.diary.dao;

import com.kverchi.diary.custom.exception.DatabaseException;
import com.kverchi.diary.domain.User;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Map;

public interface UserDao extends GenericDao<User> {
	User getUserByUsername(String user) throws DatabaseException;
	User getUserByEmail(String email) throws DatabaseException;
	boolean isRecordPresent(String key, Object value) throws DatabaseException;
	void updateUserInfo(int userId, String info) throws DatabaseException;
	List gerUserWishedSights(int userId) throws DatabaseException;
	List getUserVisitedSights(int userId) throws DatabaseException;
}
