package com.kverchi.diary.service;


import com.kverchi.diary.model.entity.User;

import java.util.List;

public interface UserService {
	User getUserByUsername(String username);
	List<User> findAll();
	/*ServiceResponse registerAccount(RegistrationForm user) throws ServiceException;*/
	void activateAccount(User user);
	boolean updatePassword(User user);
	boolean createAndSendResetPasswordToken(String email);
	User getResetPasswordToken(String token);
	User getUserFromSession();
	boolean isValuePresent(String key, Object value);
	void saveUserInfo(int userId, String info);
	//TODO is it correct place for these two methods? Or would it be better to use them in Sight layer?
	/*List getUserWishedSights(int userId);
	List getUserVisitedSights(int userId);*/
	boolean verifyPassword(String rawPass, String encodedPass);
}
