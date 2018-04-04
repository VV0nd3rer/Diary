package com.kverchi.diary.controller;

import com.kverchi.diary.domain.*;
import com.kverchi.diary.enums.ServiceMessageResponse;
import com.kverchi.diary.service.UserActivityLogService;
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
import com.kverchi.diary.form.ForgotPasswordForm;
import com.kverchi.diary.form.NewPasswordForm;
import com.kverchi.diary.form.RegistrationForm;
import com.kverchi.diary.service.UserService;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {
	final static Logger logger = Logger.getLogger(UserController.class);
	private final static String LOGIN = "login";
	private final static String REDIRECT_TO_POSTS = "redirect:/posts/list";
	@Autowired
	UserService userService;
	@Autowired
	UserActivityLogService userActivityLogService;

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
		/*if(response.getRespCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
			throw new ServiceException(response.getRespMsg());
		}
		else if(response.getRespCode() == HttpStatus.PRECONDITION_FAILED) {
			throw new ServiceException(response.getRespMsg());
		}*/
		mv.addObject("message", response);
		return mv;
	}

	@RequestMapping(value="/confirm-registration/{username}")
	public ModelAndView confirmRegistration(@PathVariable("username") String username) {
		User registeredUser;
		registeredUser = userService.getUserByUsername(username);
		if (registeredUser != null) {
			if(!registeredUser.isEnabled()) {
				userService.activateAccount(registeredUser);
				return new ModelAndView(LOGIN);
			}
		}
//		TODO return error info with error page
			return new ModelAndView("error/generic-error");
	}
	@RequestMapping(value="/login")
	public ModelAndView login() {
		return new ModelAndView(LOGIN);
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
			return new ModelAndView(LOGIN);
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
		//AngularJS checks password matching too
		if(!newPasswordForm.getPassword().equals(newPasswordForm.getMatchingPassword())) {
			return new ModelAndView("error/generic-error");
		}
		//---//
		User user = new User();
		user.setUserId(newPasswordForm.getUserId());
		user.setPassword(newPasswordForm.getPassword());
		boolean res = userService.updatePassword(user);
		if(res) {
			//TODO return info about reset link
			return new ModelAndView(LOGIN);
		}
//		TODO return error info with error page
		return new ModelAndView("error/generic-error");
	}
	@RequestMapping(value = "/update-password-in-session", method = RequestMethod.POST)
	public ServiceMessageResponse updatePasswordInSession(@RequestBody NewPasswordForm newPasswordForm) {
		ServiceMessageResponse response = ServiceMessageResponse.ERROR;
		User user = userService.getUserFromSession();
		if (user == null) {
			return response;
		}
		boolean isCurrentPassValid =
				userService.verifyPassword(newPasswordForm.getCurrentPassword(), user.getPassword());
		if(!isCurrentPassValid){
			return response;
		}
		if(newPasswordForm.getCurrentPassword().equals(newPasswordForm.getPassword())) {
			response = ServiceMessageResponse.NEW_PASSWORD_IS_THE_SAME;
			return response;
		}
		if(!newPasswordForm.getPassword().equals(newPasswordForm.getMatchingPassword())) {
			response = ServiceMessageResponse.NEW_PASSWORD_MISMATCHED;
			return response;
		}
		user.setPassword(newPasswordForm.getPassword());
		boolean res = userService.updatePassword(user);
		if(res) {
			response = ServiceMessageResponse.OK;
			return response;
		} else {
			response = ServiceMessageResponse.TRANSACTION_PROBLEM;
			return response;
		}
	}
	@RequestMapping(value = "/profile")
	public ModelAndView showProfile() {
		ModelAndView mv = new ModelAndView("profile");
		User user = userService.getUserFromSession();
		if(user == null) {
			return new ModelAndView(LOGIN);
		}
		mv.addObject(user);
		return mv;
	}
	@RequestMapping(value = "/profile/{username}")
	public ModelAndView showProfile(@PathVariable("username") String username) {
		ModelAndView mv = new ModelAndView("profile");
		User user = userService.getUserByUsername(username);
		if(user == null) {
			return new ModelAndView(REDIRECT_TO_POSTS);
		}
		mv.addObject(user);
		return mv;
	}
	@RequestMapping(value = "/user-info/{username}")
	public ModelAndView showUserInfo(@PathVariable("username") String username) {
		ModelAndView mv = new ModelAndView("fragment/user-menu::userInfo");
		User user = userService.getUserByUsername(username);
		if(user == null) {
			return new ModelAndView(REDIRECT_TO_POSTS);
		}
		mv.addObject(user);
		return mv;
	}

	@RequestMapping(value = "/user-statistic")
	public ModelAndView showUserStatistic() {
		ModelAndView mv = new ModelAndView("fragment/user-menu::userStatistic");
		User user = userService.getUserFromSession();
		if(user == null) {
			return new ModelAndView(LOGIN);
		}
		List<UserActivityLog> userActivityLogList= userActivityLogService.getLastUserActivity(user.getUserId());
		mv.addObject(user);
		mv.addObject("userActivityLogList", userActivityLogList);
		return mv;
	}
	@RequestMapping(value="/user-favorite/{username}")
	public ModelAndView showUserFavorite(@PathVariable("username") String username) {
		ModelAndView mv = new ModelAndView("fragment/user-menu::userFavorite");
		User user = userService.getUserByUsername(username);
		if(user == null) {
			return new ModelAndView(REDIRECT_TO_POSTS);
		}
		List<SightWishCounter> userWishedList = userService.getUserWishedSights(user.getUserId());
		List<CountriesSight> userVisitedList = userService.getUserVisitedSights(user.getUserId());
		mv.addObject(user);
		mv.addObject("userWishedSights", userWishedList);
		mv.addObject("userVisitedSights", userVisitedList);
		return mv;
	}

	@RequestMapping(value = "/save-info", method = RequestMethod.POST, consumes = "text/plain")
	public String saveInfo(@RequestBody(required=false)  String info) {
		User currentUser = userService.getUserFromSession();
		userService.saveUserInfo(currentUser.getUserId(), info);
		return "OK";
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
