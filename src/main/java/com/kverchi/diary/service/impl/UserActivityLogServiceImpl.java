package com.kverchi.diary.service.impl;

import com.kverchi.diary.dao.UserActivityDao;
import com.kverchi.diary.domain.User;
import com.kverchi.diary.domain.UserActivityLog;
import com.kverchi.diary.security.UserDetailsImpl;
import com.kverchi.diary.service.UserActivityLogService;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

/**
 * Created by Liudmyla Melnychuk on 26.9.2017.
 */
@Service("userActivityLogService")
public class UserActivityLogServiceImpl implements UserActivityLogService {
    private final static Logger logger = Logger.getLogger(UserActivityLogServiceImpl.class);
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserActivityDao userActivityDao;

    @Override
    public void addUserActivityLog(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) authentication.getDetails();
        String ip = webAuthenticationDetails.getRemoteAddress();
        String session_id = RequestContextHolder.currentRequestAttributes().getSessionId();
        ((WebAuthenticationDetails) authentication.getDetails()).getSessionId();
        User user = userDetails.getUser();
        UserActivityLog userActivityLog = new UserActivityLog();
        userActivityLog.setUser_id(user.getUserId());
        userActivityLog.setSession_id(session_id);
        userActivityLog.setLogin_ip(ip);
        userActivityLog.setActive_session(true);
        userActivityLog.setUserAgentInfo(request.getHeader("User-Agent"));
        String  userAgentInfo  =   request.getHeader("User-Agent").toLowerCase();

        userActivityLog.setOsInfo(getOSInformation(userAgentInfo));
        userActivityLog.setBrowserInfo(getBrowserInformation(userAgentInfo));

        userActivityDao.persist(userActivityLog);
    }

    @Override
    public UserActivityLog getUserActivity(String session_id) {
        return userActivityDao.getById(session_id);
    }

    @Override
    public List<UserActivityLog> getLastUserActivity(int user_id) {
        List<UserActivityLog> userActivityLogList = userActivityDao.getLastUserActivity(user_id);
        return userActivityLogList;
    }

    @Override
    public void updateUserActivityLog(String sessionId) {
        UserActivityLog userActivityLog = getUserActivity(sessionId);
        userActivityLog.setActive_session(false);
        userActivityDao.update(userActivityLog);
    }
    private String getOSInformation(String userAgentInfo) {

        String os;
        if (userAgentInfo.indexOf("win") >= 0 )
        {
            os = "Windows";
        } else if(userAgentInfo.indexOf("mac") >= 0)
        {
            os = "Mac";
        } else if(userAgentInfo.indexOf("nix") >= 0
                || userAgentInfo.indexOf("nux") >= 0
                || userAgentInfo.indexOf("aix") >= 0)
        {
            os = "Linux or Unix";
        } else if(userAgentInfo.indexOf("sunos") >= 0)
        {
            os = "Solaris";
        } else if(userAgentInfo.indexOf("android") >= 0)
        {
            os = "Android";
        } else if(userAgentInfo.indexOf("iphone") >= 0)
        {
            os = "IPhone";
        } else {
            os = "Unknown, More-Info: " + userAgentInfo;
        }
        return os;
    }
    private String getBrowserInformation(String userAgetnInfo) {
        userAgetnInfo = userAgetnInfo.toLowerCase();
        String browser;
        if (userAgetnInfo.contains("chrome"))
        {
            browser = "Chrome";
        } else if (userAgetnInfo.contains("firefox"))
        {
            browser = "Firefox";
        } else if ( userAgetnInfo.contains("opr") || userAgetnInfo.contains("opera"))
        {
            browser = "Opera";
        } else if (userAgetnInfo.contains("msie") || userAgetnInfo.contains("rv"))
        {
            browser = "Internet Explorer";
        }   else if (userAgetnInfo.contains("safari"))
        {
            browser = "Safari";
        } else if ((userAgetnInfo.indexOf("mozilla/7.0") > -1)
                || (userAgetnInfo.indexOf("netscape6") != -1)
                || (userAgetnInfo.indexOf("mozilla/4.7") != -1)
                || (userAgetnInfo.indexOf("mozilla/4.78") != -1)
                || (userAgetnInfo.indexOf("mozilla/4.08") != -1)
                || (userAgetnInfo.indexOf("mozilla/3") != -1) )
        {
            browser = "Netscape";

        } else
        {
            browser = "Unknown";
        }
        return browser;
    }

}
