package com.kverchi.diary.dao;

import com.kverchi.diary.domain.User;

public interface UserDao extends GenericDao<User> {
	User getUserByUsername(String user);
	User getUserByEmail(String email);
}
