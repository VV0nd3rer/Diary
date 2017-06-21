package com.kverchi.diary.dao;

import com.kverchi.diary.custom.exception.DatabaseException;
import com.kverchi.diary.domain.User;

public interface UserDao extends GenericDao<User> {
	User getUserByUsername(String user) throws DatabaseException;
	User getUserByEmail(String email) throws DatabaseException;
}
