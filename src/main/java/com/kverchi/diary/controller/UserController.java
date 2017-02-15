package com.kverchi.diary.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.kverchi.diary.custom.exception.ServiceException;
import com.kverchi.diary.domain.ServiceResponse;
import com.kverchi.diary.domain.User;
import com.kverchi.diary.form.RegistrationForm;
import com.kverchi.diary.service.UserService;

@RestController
@RequestMapping("users")
public class UserController {
	final static Logger logger = Logger.getLogger(UserController.class);
	@Autowired
	UserService userService;
	
	@RequestMapping(value="/registration")
	public ModelAndView registration() {
		ModelAndView mv = new ModelAndView("registration");
		mv.addObject("regForm", new RegistrationForm());
		return mv;
	}
	@RequestMapping(value="/add-user", method = RequestMethod.POST)
	public ServiceResponse addUser(@RequestBody RegistrationForm regForm) throws ServiceException {
		ServiceResponse response = userService.registerAccount(regForm);
		if(response.getRespCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
			throw new ServiceException(response.getRespMsg()); 
		}
		return response;
	}
	@RequestMapping(value="/confirm-registration/{username}")
	public ModelAndView confirmRegistration(@PathVariable("username") String username) {
		User registeredUser = new User();
		registeredUser = userService.getUserByUsername(username);
		if (registeredUser != null) {
			if(!registeredUser.isEnabled()) {
				userService.activateAccount(registeredUser);
				return new ModelAndView("login");
			}
		}
			//TODO resend on Error Page, confirmation link is not valid anymore
			return new ModelAndView("../posts/test-me");
	}
	@RequestMapping(value="/login")
	public ModelAndView login() {
		return new ModelAndView("login");
	}
	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<String> exceptionHandler(Exception ex) {
		/*ResponseEntity<String> response = new ResponseEntity<String>();
		ServiceResponse errorResp = new ServiceResponse();
		errorResp.setRespCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		errorResp.setRespMsg(ex.getMessage());*/
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
}
