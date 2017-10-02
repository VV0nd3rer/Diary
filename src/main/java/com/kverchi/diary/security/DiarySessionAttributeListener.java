package com.kverchi.diary.security;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;

import com.kverchi.diary.domain.User;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.kverchi.diary.domain.UserActivityLog;
import com.kverchi.diary.service.UserActivityLogService;

@WebListener
public class DiarySessionAttributeListener implements HttpSessionAttributeListener {
	final static Logger logger = Logger.getLogger(DiarySessionAttributeListener.class);

	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		String attributeName = event.getName();
		Object attributeValue = event.getValue();
		logger.info("Attribute added : " + attributeName + " : " + attributeValue);

		if(attributeName
				.equals(
						org.springframework.security.web.context.HttpSessionSecurityContextRepository.
								SPRING_SECURITY_CONTEXT_KEY)) {
			SecurityContext securityContext = (SecurityContext) event.getValue();
			addUserActivityLog(securityContext, event);

		}
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		String attributeName = event.getName();
		Object attributeValue = event.getValue();
		logger.info("Attribute removed : " + attributeName + " : " + attributeValue);
		
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		String attributeName = event.getName();
		Object attributeValue = event.getValue();
		logger.info("Attribute replaced : " + attributeName + " : " + attributeValue);
		
	}
	private void addUserActivityLog(SecurityContext securityContext, HttpSessionBindingEvent event) {
		Authentication authentication = securityContext.getAuthentication();
		WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) authentication.getDetails();
		String rem_addr = webAuthenticationDetails.getRemoteAddress();
		String session_id = RequestContextHolder.currentRequestAttributes().getSessionId();
		logger.debug("rem_addr: " + rem_addr + ", session_id: " + session_id);
		Object principal = authentication.getPrincipal();
		if (principal instanceof UserDetailsImpl) {
			UserDetailsImpl userDetails = (UserDetailsImpl) principal;
			User user = userDetails.getUser();
			UserActivityLog userActivityLog = new UserActivityLog();
			userActivityLog.setUser_id(user.getUserId());
			userActivityLog.setSession_id(session_id);
			userActivityLog.setLogin_ip(rem_addr);
			UserActivityLogService userActivityLogService =
					(UserActivityLogService)injectBean(event, "userActivityLogService");
			userActivityLogService.addUserActivityLog(userActivityLog);
		}
	}
	private Object injectBean(HttpSessionEvent sessionEvent, String beanName){

        HttpSession session = sessionEvent.getSession();

        ApplicationContext ctx =
              WebApplicationContextUtils.
                    getWebApplicationContext(session.getServletContext());

        Object obj = ctx.getBean(beanName);
		return obj;
   }
}
