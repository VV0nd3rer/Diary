package com.kverchi.diary.security;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
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
		injectBean(event);
		
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
	private void injectBean(HttpSessionEvent sessionEvent){

        HttpSession session = sessionEvent.getSession();

        ApplicationContext ctx =
              WebApplicationContextUtils.
                    getWebApplicationContext(session.getServletContext());

        UserActivityLogService userActivityLogService =
                    (UserActivityLogService) ctx.getBean("userActivityLogService");

        userActivityLogService.addUserActivityLog(new UserActivityLog());
   }
}
