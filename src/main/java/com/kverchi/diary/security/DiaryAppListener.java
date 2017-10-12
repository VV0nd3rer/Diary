package com.kverchi.diary.security;

import com.kverchi.diary.domain.User;
import com.kverchi.diary.domain.UserActivityLog;
import com.kverchi.diary.service.UserActivityLogService;
import com.kverchi.diary.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import java.util.List;

/**
 * Created by Liudmyla Melnychuk on 3.10.2017.
 */
@Component
public class DiaryAppListener implements ApplicationListener<ApplicationEvent> {
    final static Logger logger = Logger.getLogger(DiaryAppListener.class);

    @Override
    public void onApplicationEvent (ApplicationEvent applicationEvent) {
        if(applicationEvent instanceof AuthenticationSuccessEvent) {
            AuthenticationSuccessEvent event = (AuthenticationSuccessEvent) applicationEvent;
            Authentication authentication = event.getAuthentication();

            logger.debug("---- AuthenticationSuccessEvent ------");
            if(authentication.getPrincipal() instanceof UserDetailsImpl) {
                logger.debug("---- AuthenticationSuccessEvent, principal instanceof UserDetailsImpl ------");
                logger.debug("----- session ID: " + RequestContextHolder.currentRequestAttributes().getSessionId() + " -------");
                addUserActivityLog(authentication);
            }
        }
        if(applicationEvent instanceof SessionDestroyedEvent) {
            logger.debug("---- SessionDestroyedEvent ------");
            SessionDestroyedEvent event = (SessionDestroyedEvent) applicationEvent;
            List<SecurityContext> securityContexts = event.getSecurityContexts();
            for (SecurityContext securityContext : securityContexts) {
                Object principal = securityContext.getAuthentication().getPrincipal();
                if (principal instanceof UserDetailsImpl) {
                    logger.debug(" ----- SessionDestroyedEvent, principal instanceof UserDetailsImpl ------");
                    HttpSession eventSession = (HttpSession)event.getSource();
                    String sessionId = eventSession.getId();
                    logger.debug("----- SessionDestroyedEvent, session ID: " + sessionId + " -------");
                    updateUserActivityLog(sessionId);
                }
            }


        }

    }
    private void addUserActivityLog(Authentication authentication) {
        UserActivityLogService userActivityLogService =
                (UserActivityLogService)injectBean("userActivityLogService");
        userActivityLogService.addUserActivityLog(authentication);
    }
    private void updateUserActivityLog(String sessionId) {
        UserActivityLogService userActivityLogService =
                (UserActivityLogService)injectBean("userActivityLogService");
            userActivityLogService.updateUserActivityLog(sessionId);


    }
    private Object injectBean(String beanName){
        WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        Object obj = context.getBean(beanName);
        return obj;
    }
}
