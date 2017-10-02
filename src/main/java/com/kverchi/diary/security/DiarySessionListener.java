package com.kverchi.diary.security;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@WebListener
public class DiarySessionListener implements HttpSessionListener {
	final static Logger logger = Logger.getLogger(DiarySessionListener.class);
	
	private static int totalActiveSessions;

    public static int getTotalActiveSession(){
          return totalActiveSessions;
    }
	
	@Override
    public void sessionCreated(HttpSessionEvent arg0) {
           totalActiveSessions++;
           logger.info("sessionCreated - add one session into counter");
           logger.info("totalActiveSessions" + totalActiveSessions);
           printCounter(arg0);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent arg0) {
           totalActiveSessions--;
           logger.info("sessionDestroyed - deduct one session from counter");
           logger.info("totalActiveSessions" + totalActiveSessions);
           printCounter(arg0);
    }

    private void printCounter(HttpSessionEvent sessionEvent){

        HttpSession session = sessionEvent.getSession();

        ApplicationContext ctx =
              WebApplicationContextUtils.
                    getWebApplicationContext(session.getServletContext());

        /*CounterService counterService =
                    (CounterService) ctx.getBean("counterService");

        counterService.printCounter(totalActiveSessions);*/
   }
}