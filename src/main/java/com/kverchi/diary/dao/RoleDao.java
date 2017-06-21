package com.kverchi.diary.dao;

import com.kverchi.diary.custom.exception.DatabaseException;
import com.kverchi.diary.domain.Role;

public interface RoleDao extends GenericDao<Role> {
	Role getByName(String role_name) throws DatabaseException;
}
