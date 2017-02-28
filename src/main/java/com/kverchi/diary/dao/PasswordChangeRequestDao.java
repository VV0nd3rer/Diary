package com.kverchi.diary.dao;

import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import com.kverchi.diary.domain.PasswordChangeRequest;

public interface PasswordChangeRequestDao extends GenericDao<PasswordChangeRequest> {

}
