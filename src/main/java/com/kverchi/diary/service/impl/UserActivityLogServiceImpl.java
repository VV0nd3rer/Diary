package com.kverchi.diary.service.impl;

import com.kverchi.diary.model.entity.User;
import com.kverchi.diary.model.entity.UserActivityLog;
import com.kverchi.diary.model.entity.WebVisitingLog;
import com.kverchi.diary.repository.UserActivityRepository;
import com.kverchi.diary.repository.WebVisitingRepository;
import com.kverchi.diary.security.UserDetailsImpl;
import com.kverchi.diary.service.UserActivityLogService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Liudmyla Melnychuk on 26.9.2017.
 */
@Service("userActivityLogService")
public class UserActivityLogServiceImpl implements UserActivityLogService {
    private final static Logger logger = LogManager.getLogger(UserActivityLogServiceImpl.class);
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserActivityRepository userActivityRepository;
    @Autowired
    private WebVisitingRepository webVisitingRepository;

    @Override
    public void addUserActivityLog(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) authentication.getDetails();
        String ip = webAuthenticationDetails.getRemoteAddress();
        String session_id = RequestContextHolder.currentRequestAttributes().getSessionId();
        ((WebAuthenticationDetails) authentication.getDetails()).getSessionId();
        User user = userDetails.getUser();
        UserActivityLog userActivityLog = new UserActivityLog();
        userActivityLog.setUserId(user.getUserId());
        userActivityLog.setSessionId(session_id);
        userActivityLog.setLoginIp(ip);
        userActivityLog.setActiveSession(true);
        userActivityLog.setUserAgentInfo(request.getHeader("User-Agent"));
        String  userAgentInfo  =   request.getHeader("User-Agent").toLowerCase();

        userActivityLog.setOsInfo(getOSInformation(userAgentInfo));
        userActivityLog.setBrowserInfo(getBrowserInformation(userAgentInfo));

        userActivityRepository.save(userActivityLog);
    }

    @Override
    public UserActivityLog getUserActivity(String session_id) {
        return userActivityRepository.getOne(session_id);
    }

    @Override
    public List<UserActivityLog> getLastUserActivity(int userId) {
        List<UserActivityLog> userActivityLogList = userActivityRepository.findByUserIdOrderByLoginTimeDesc(userId);
        return userActivityLogList;
    }

    @Override
    public void updateUserActivityLog(String sessionId) {
        UserActivityLog userActivityLog = getUserActivity(sessionId);
        userActivityLog.setActiveSession(false);
        userActivityRepository.save(userActivityLog);
    }

    @Override
    public String getUserInfoFromHttpRequest() {
        return request.getHeader("User-Agent");
    }

    @Override
    public void saveUserInfoFromHttpRequest() {
        String ipAddress = null;
        String gateWay = request.getHeader("VIA");
        ipAddress = request.getHeader("X-FORWARDED-FOR");
        if(ipAddress == null)
        {
            ipAddress = request.getRemoteAddr();
        }
        logger.debug("IP address: " + ipAddress);
        WebVisitingLog webVisitingLog = new WebVisitingLog();
        webVisitingLog.setUserAgent(request.getHeader("User-Agent"));
        webVisitingLog.setIpAddress(ipAddress);
        webVisitingRepository.save(webVisitingLog);
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
