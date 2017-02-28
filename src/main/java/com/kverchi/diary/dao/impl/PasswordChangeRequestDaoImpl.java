package com.kverchi.diary.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kverchi.diary.dao.PasswordChangeRequestDao;

import com.kverchi.diary.domain.PasswordChangeRequest;
@Repository
public class PasswordChangeRequestDaoImpl extends GenericDaoImpl<PasswordChangeRequest> implements PasswordChangeRequestDao {
	
}
