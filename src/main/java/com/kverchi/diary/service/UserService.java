package com.kverchi.diary.service;

import com.kverchi.diary.domain.UserActivityLog;
import org.springframework.stereotype.Service;

import com.kverchi.diary.custom.exception.ServiceException;
import com.kverchi.diary.domain.ServiceResponse;
import com.kverchi.diary.domain.User;
import com.kverchi.diary.enums.ServiceMessageResponse;
import com.kverchi.diary.form.RegistrationForm;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {
	User getUserByUsername(String username);
	List<User> getAllUsers();
	ServiceResponse registerAccount(RegistrationForm user) throws ServiceException;
	ServiceResponse testRegisterAccount(RegistrationForm user) throws ServiceException;
	void activateAccount(User user);
	boolean updatePassword(User user);
	boolean createAndSendResetPasswordToken(String email);
	User getResetPasswordToken(String token);
	User getUserFromSession();
	boolean isValuePresent(String key, Object value);
	void saveUserInfo(int user_id, String info);
}
