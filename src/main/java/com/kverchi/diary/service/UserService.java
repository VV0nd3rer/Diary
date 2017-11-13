package com.kverchi.diary.service;

import com.kverchi.diary.custom.exception.ServiceException;
import com.kverchi.diary.domain.ServiceResponse;
import com.kverchi.diary.domain.User;
import com.kverchi.diary.form.RegistrationForm;

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
	void saveUserInfo(int userId, String info);
	List getUserWishedSights(int userId);
	List getUserVisitedSights(int userId);
	boolean verifyPassword(String rawPass, String encodedPass);
}
