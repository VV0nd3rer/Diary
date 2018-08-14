package com.kverchi.diary.security;

import com.kverchi.diary.model.entity.UserActivityLog;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kverchi.diary.model.entity.User;
import com.kverchi.diary.service.UserService;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;


@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
	final static Logger logger = LogManager.getLogger(UserDetailsImpl.class);

	@Autowired
	private UserService userService;
	@Autowired
	private HttpServletRequest httpServletRequest;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.getUserByUsername(username);
		if (user == null) {
			logger.info("No user in DB with username " + username);
			throw new UsernameNotFoundException("No such user: " + username);
		}  
		if (user.getRoles().isEmpty()) {
			logger.info("User " + username + " has no role.");
			//throw new UsernameNotFoundException("User " + username + " has no authorities");
		}
		if (!user.isEnabled())
			logger.info(username + " is disabled.");
		
		UserDetailsImpl principal = new UserDetailsImpl(user);
		/*UserActivityLog userActivityLog = generateActivityLog(user.getUserId());
		userService.addUserActivityLog(userActivityLog);*/
		logger.debug("----- session ID: " + RequestContextHolder.currentRequestAttributes().getSessionId() + " -------");
		logger.info("principal user: " + principal.getUsername());
		return principal;
	}

	private UserActivityLog generateActivityLog(int user_id) {
		UserActivityLog userActivityLog = new UserActivityLog();
		userActivityLog.setUserId(user_id);
		String ip = getUserRequestInfo();
		String user_hostname = httpServletRequest.getRemoteHost();
		String rem_user = httpServletRequest.getRemoteUser();
		String user_local_hostname = httpServletRequest.getLocalName();
		logger.debug("user_hostname: " + user_hostname);
		logger.debug("rem_user: " + rem_user);
		logger.debug("user_local_hostname: " + user_local_hostname);
		userActivityLog.setLoginIp(ip);
		userActivityLog.setHostname(user_hostname);
		return userActivityLog;
	}

	private String getUserRequestInfo() {
		String ipAddress = httpServletRequest.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = httpServletRequest.getRemoteAddr();
		}
		return ipAddress;
	}
}
