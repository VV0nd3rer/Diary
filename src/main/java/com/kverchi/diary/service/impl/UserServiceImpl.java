package com.kverchi.diary.service.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.kverchi.diary.custom.exception.ServiceException;
import com.kverchi.diary.dao.PasswordChangeRequestDao;
import com.kverchi.diary.dao.RoleDao;
import com.kverchi.diary.dao.UserDao;
import com.kverchi.diary.domain.PasswordChangeRequest;
import com.kverchi.diary.domain.ServiceResponse;
import com.kverchi.diary.domain.User;
import com.kverchi.diary.enums.ServiceMessageResponse;
import com.kverchi.diary.form.RegistrationForm;
import com.kverchi.diary.security.UserDetailsImpl;
import com.kverchi.diary.service.EmailService;
import com.kverchi.diary.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	final static Logger logger = Logger.getLogger(UserServiceImpl.class);
	final static String CHANGE_PASS_LINK = "users/change-password/";
	final static String REGISTER_USER_LINK = "users/confirm-registration/";
	final static String EMAIL_FORGOTPASS_TEMPLATE = "email-forgotpass";
	final static String EMAIL_REGISTER_TEMPLATE="email-registration";
	
	@Autowired
	ServletContext context;  
	
	@Autowired 
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private PasswordChangeRequestDao passwordChangeRequestDao;
	@Autowired
	private EmailService emailService;
	@Autowired
    private TemplateEngine emailTemplateEngine;
	@Autowired
	private PasswordEncoder passwordEncoder;
	 
	
	@Override
	public User getUserByUsername(String username) {
		User user = userDao.getUserByUsername(username);
		return user;
	}

	@Override
	public boolean isValuePresent(String key, Object value) {
		boolean isValuePresent = userDao.isRecordPresent(key, value);
		return isValuePresent;
	}
	@Override
	public ServiceResponse testRegisterAccount(RegistrationForm user) throws ServiceException {
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
		boolean isRegistrationEmailSent = false;

		String link = "https://super-diary.herokuapp.com/" + REGISTER_USER_LINK + user.getUsername();

		final Context ctx = new Context();
		ctx.setVariable("name", user.getUsername());
		ctx.setVariable("link", link);
		final String emailContent = emailTemplateEngine.process(EMAIL_REGISTER_TEMPLATE, ctx);
		isRegistrationEmailSent = emailService.sendSimpleHTMLEmail(user.getEmail(), "Registration", emailContent);

		//TODO if account is created then send an email
		//in case of some internal error email will not be sent
		if(isRegistrationEmailSent) {
			response.setRespCode(HttpStatus.OK);
			response.setRespMsg(ServiceMessageResponse.OK.toString());

		}
		return response;
	}

	@Override
	public ServiceResponse registerAccount(RegistrationForm user) throws ServiceException {
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
		boolean isRegistrationEmailSent = false;

		String link = "https://super-diary.herokuapp.com/" + REGISTER_USER_LINK + user.getUsername();

		final Context ctx = new Context();
		ctx.setVariable("name", user.getUsername());
		ctx.setVariable("link", link);
		final String emailContent = emailTemplateEngine.process(EMAIL_REGISTER_TEMPLATE, ctx);
		isRegistrationEmailSent = emailService.sendSimpleHTMLEmail(user.getEmail(), "Registration", emailContent);

		//TODO if account is created then send an email 
		//in case of some internal error email will not be sent
		if(isRegistrationEmailSent) {
			//create an account
			User newAccount = new User();
			newAccount.setUsername(user.getUsername());
			//TODO
			//Salt
			newAccount.setPassword(
					passwordEncoder.encode(user.getPassword()));
			newAccount.setEmail(user.getEmail());
			newAccount.setRoles(Arrays.asList(roleDao.getByName("ROLE_USER")));
			newAccount.setEnabled(false);
			//int res = (Integer)userDao.create(newAccount);
			User added_user = (User)userDao.persist(newAccount);
			if(added_user == null) {
				response.setRespCode(HttpStatus.INTERNAL_SERVER_ERROR);
				response.setRespMsg(ServiceMessageResponse.TRANSACTION_PROBLEM.toString());
				return response;
			}
			else  {
				response.setRespCode(HttpStatus.OK);
				response.setRespMsg(ServiceMessageResponse.OK.toString());
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
	
	@Override
	public boolean createAndSendResetPasswordToken(String email) {
		User user = null;
		user = userDao.getUserByEmail(email);
		if(user == null) {
			return false;
		}
		String token = generateSecureToken();
		PasswordChangeRequest passChangeReq = new PasswordChangeRequest();
		passChangeReq.setUUID(token);
		passChangeReq.setUserId(user.getUserId());
		passChangeReq.setCreatedTime(new Date());
		//String id = (String)passwordChangeRequestDao.create(passChangeReq);
		passwordChangeRequestDao.persist(passChangeReq);
		String link = "https://super-diary.herokuapp.com/" + CHANGE_PASS_LINK + token; 
		final Context ctx = new Context();
		ctx.setVariable("name", user.getUsername());
		ctx.setVariable("link", link);
		final String emailContent = this.emailTemplateEngine.process(EMAIL_FORGOTPASS_TEMPLATE, ctx);
		boolean isSended = emailService.sendSimpleHTMLEmail(user.getEmail(), "Resetting password", emailContent);
		return isSended;
		
	}
	@Override
	public User getResetPasswordToken(String token) {
		PasswordChangeRequest passChangeReq = null;
		passChangeReq = passwordChangeRequestDao.getById(token);
		if(passChangeReq == null) {
			return null;
		}
		//TODO check date and time validity
		//passChangeReq.getCreatedTime()...
		if(passChangeReq.isUUIDused()) {
			return null;
		}
		passChangeReq.setUUIDused(true);
		passwordChangeRequestDao.update(passChangeReq);
		User user = userDao.getById(passChangeReq.getUserId());
		return user;
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
	@Override
	public boolean updatePassword(User user) {
		User updUsr = userDao.getById(user.getUserId());
		if(updUsr == null) {
			return false;
		}
		updUsr.setPassword(passwordEncoder.encode(user.getPassword()));
		userDao.update(updUsr);
		return true;
	}
	@Override
	public User getUserFromSession() {
		SecurityContext context = SecurityContextHolder.getContext();
	      Authentication authentication = context.getAuthentication();
	      Object principal = authentication.getPrincipal();
	      if (principal instanceof UserDetailsImpl) {
	    	  UserDetailsImpl userDetails = (UserDetailsImpl) principal;
	          return userDetails.getUser();
	      }
	      return null;
	}
}
