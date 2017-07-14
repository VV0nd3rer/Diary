package com.kverchi.diary.controller;

import org.apache.log4j.Logger;
import org.springframework.aop.aspectj.annotation.ReflectiveAspectJAdvisorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.kverchi.diary.dao.UserDao;
import com.kverchi.diary.domain.ServiceResponse;
import com.kverchi.diary.domain.User;
import com.kverchi.diary.form.ForgotPasswordForm;
import com.kverchi.diary.form.NewPasswordForm;
import com.kverchi.diary.form.RegistrationForm;
import com.kverchi.diary.service.UserService;

@RestController
@RequestMapping("users")
public class UserController {
	final static Logger logger = Logger.getLogger(UserController.class);
	@Autowired
	UserService userService;

	@RequestMapping(value="/signup")
	public ModelAndView signup() {
		ModelAndView mv = new ModelAndView("signup");
		return mv;
	}

	@RequestMapping(value="/username-exists/{username}")
	public boolean isUsernamePresent(@PathVariable("username") String username) {
		boolean isUsernamePresent = userService.isValuePresent("username", username);
		return isUsernamePresent;
	}

	/*If use GET request with email as part of URL, need to specify a content negotiation manager
	  https://blog.georgovassilis.com/2015/10/29/spring-mvc-rest-controller-says-406-when-emails-are-part-url-path/
	 */
	@RequestMapping(value="/email-exists", method=RequestMethod.POST, headers = "content-type=text/*")
	public boolean isEmailPresent(@RequestBody String email) {
		boolean isEmailPresent = userService.isValuePresent("email", email);
		logger.debug("isEmailPresent: " + isEmailPresent);
		return isEmailPresent;
	}

	@RequestMapping(value="/add-user", method = RequestMethod.POST)
	public ModelAndView addUser(@RequestBody RegistrationForm regForm) throws ServiceException {
		ModelAndView mv = new ModelAndView("fragment/signup-result :: content");
		ServiceResponse response = userService.registerAccount(regForm); /*userService.testRegisterAccount(regForm);*/
		if(response.getRespCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
			throw new ServiceException(response.getRespMsg());
		}
		else if(response.getRespCode() == HttpStatus.PRECONDITION_FAILED) {
			throw new ServiceException(response.getRespMsg());
		}
		mv.addObject("message", response);
		return mv;
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
//		TODO return error info with error page
			return new ModelAndView("error/generic-error");
	}
	@RequestMapping(value="/login")
	public ModelAndView login() {
		return new ModelAndView("login");
	}
	@RequestMapping(value="/forgot-password")
	public ModelAndView recoverPassword() {
		ModelAndView mv = new ModelAndView("forgot-password");
		mv.addObject("forgotPasswordForm", new ForgotPasswordForm());
		return mv;
	}

	@RequestMapping(value="/reset-password", method = RequestMethod.POST)
	public ModelAndView resetPassword(@ModelAttribute ForgotPasswordForm forgotPasswordForm) {
		String email = forgotPasswordForm.getEmail();
		boolean res = userService.createAndSendResetPasswordToken(email);
		if(res) {
			//TODO return info about reset link
			return new ModelAndView("login");
		}
//		TODO return error info with error page
		return new ModelAndView("error/generic-error");
	}
	@RequestMapping(value="/change-password/{UUID}")
	public ModelAndView changePassword(@PathVariable String UUID) {
		User user = userService.getResetPasswordToken(UUID);
		if(user != null) {
			ModelAndView mv = new ModelAndView("new-password");
			NewPasswordForm newPassForm = new NewPasswordForm();
			newPassForm.setUserId(user.getUserId());
			newPassForm.setUsername(user.getUsername());
			mv.addObject("newPasswordForm", newPassForm);
			return mv;
		}
//		TODO return error info with error page
		return new ModelAndView("error/generic-error");
	}
	@RequestMapping(value="/update-password", method = RequestMethod.POST)
	public ModelAndView updatePassword(@ModelAttribute NewPasswordForm newPasswordForm) {
		User user = new User();
		user.setUserId(newPasswordForm.getUserId());
		user.setPassword(newPasswordForm.getPassword());
		boolean res = userService.updatePassword(user);
		if(res) {
			//TODO return info about reset link
			return new ModelAndView("login");
		}
//		TODO return error info with error page
		return new ModelAndView("error/generic-error");
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
