package com.kverchi.diary.service.impl;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.kverchi.diary.custom.exception.ServiceException;
import com.kverchi.diary.dao.RoleDao;
import com.kverchi.diary.dao.UserDao;
import com.kverchi.diary.domain.ServiceResponse;
import com.kverchi.diary.domain.User;
import com.kverchi.diary.enums.ServiceMessageResponse;
import com.kverchi.diary.form.RegistrationForm;
import com.kverchi.diary.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	final static Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	@Autowired 
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private JavaMailSender mailSender;
	 
	
	@Override
	public User getUserByUsername(String username) {
		User user = userDao.getUserByUsername(username);
		return user;
	}
	@Override
	public ServiceResponse registerAccount(RegistrationForm user) throws ServiceException {
		/*if(!user.getPassword().equals(user.getMatchingPassword())) {
			logger.debug("Passwords are not matching. ");
			//throw new ServiceException("Passwords are not matching.");
			
		}*/
		ServiceResponse response = 
				new ServiceResponse(HttpStatus.INTERNAL_SERVER_ERROR, ServiceMessageResponse.UKNOWN_PROBLEM.toString());
		if(userDao.getUserByUsername(user.getUsername()) != null) {
			logger.debug("Username " + user.getUsername() + " already exists.");
			//throw new ServiceException("Username " + user.getUsername() + " already exists.");
			response.setRespCode(HttpStatus.PRECONDITION_FAILED);
			response.setRespMsg(ServiceMessageResponse.USER_USERNAME_ALREADY_EXIST.toString());
			return response;
		}
		if(userDao.getUserByEmail(user.getEmail()) != null) {
			logger.debug("Email " + user.getEmail() + " already exists.");
			//throw new ServiceException("Email " + user.getEmail() + " already exists.");
			response.setRespCode(HttpStatus.PRECONDITION_FAILED);
			response.setRespMsg(ServiceMessageResponse.USER_EMAIL_ALREADY_EXIST.toString());
			return response;
		}
		
		// Sending email implementation needs to be here as 'isRegistrationEmailSent'
		boolean isRegistrationEmailSent = false;
		
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
				mimeMessage.setFrom(new InternetAddress("kverchi24@gmail.com"));
				mimeMessage.setText(
						"Dear " + user.getUsername() + ", thank you for your registration. " +
						"Please activate your account by clicking <a href='http://localhost:8080/Diary/users/confirm-registration/" + user.getUsername() + "'>here</a>.");
			}
		};
		try {
			mailSender.send(preparator);
			isRegistrationEmailSent = true;
		} catch(MailException ex) {
			logger.error(ex);
		}
		
		
		if(isRegistrationEmailSent) {
			//create an account
			User newAccount = new User();
			newAccount.setUsername(user.getUsername());
			//TODO
			//Password encoding + salt
			newAccount.setPassword(user.getPassword());
			newAccount.setEmail(user.getEmail());
			newAccount.setRoles(Arrays.asList(roleDao.getByName("ROLE_USER")));
			newAccount.setEnabled(false);
			int res = userDao.create(newAccount);
			logger.debug("'Create' method returned ID: " + res);
			if(res != 0) {
				response.setRespCode(HttpStatus.OK);
				response.setRespMsg(ServiceMessageResponse.OK.toString());
				return response;
			}
			else  {
				response.setRespCode(HttpStatus.INTERNAL_SERVER_ERROR);
				response.setRespMsg(ServiceMessageResponse.TRANSACTION_PROBLEM.toString());
				return response;
			}
				
		}
		else {
			response.setRespCode(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setRespMsg(ServiceMessageResponse.EMAIL_SENDING_PROBLEM.toString());
			return response;	
		}
		//return response;
	}
	
	private String generateSecureToken() {
		String token = new String();
		try {
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			token = new Integer(sr.nextInt()).toString();
		} catch (NoSuchAlgorithmException e) {
			logger.error(e);
		}
		return token;
	}
	@Override
	public void activateAccount(User user) {
		user.setEnabled(true);
		userDao.update(user);
	}
}
