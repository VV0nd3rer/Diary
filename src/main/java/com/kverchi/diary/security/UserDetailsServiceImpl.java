package com.kverchi.diary.security;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kverchi.diary.domain.User;
import com.kverchi.diary.service.UserService;


@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
	final static Logger logger = Logger.getLogger(UserDetailsImpl.class);
	
	@Autowired
	private UserService userService;
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
		logger.info("principal user: " + principal.getUsername());
		return principal;
	}

}
