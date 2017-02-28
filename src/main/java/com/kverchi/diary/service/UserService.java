package com.kverchi.diary.service;

import org.springframework.stereotype.Service;

import com.kverchi.diary.custom.exception.ServiceException;
import com.kverchi.diary.domain.ServiceResponse;
import com.kverchi.diary.domain.User;
import com.kverchi.diary.enums.ServiceMessageResponse;
import com.kverchi.diary.form.RegistrationForm;

public interface UserService {
	User getUserByUsername(String username);
	ServiceResponse registerAccount(RegistrationForm user) throws ServiceException;
	void activateAccount(User user);
	boolean updatePassword(User user);
	boolean createResetPasswordToken(String email);
	User getResetPasswordToken(String token);
}
