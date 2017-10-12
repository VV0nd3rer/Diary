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

        String  browserDetails  =   request.getHeader("User-Agent");
        userActivityLog.setOsInfo(browserDetails);
        logger.debug("browserDetails: " + browserDetails);
        if(ip.equals("0:0:0:0:0:0:0:1")) {
            userActivityLog.setCity("home");
            userActivityLog.setPostal("home");
            userActivityLog.setState("localhost");
        } else {
            //Load ip info from GeoLite database
            try {
                ClassLoader classLoader = getClass().getClassLoader();
                File file = new File(classLoader.getResource("GeoLite2-City.mmdb").getFile());
                DatabaseReader databaseReader = new DatabaseReader.Builder(file).build();
                InetAddress inetAddress = InetAddress.getByName(ip);
                CityResponse cityResponse = databaseReader.city(inetAddress);
                String cityName = cityResponse.getCity().getName();
                String postal = cityResponse.getPostal().getCode();
                String state = cityResponse.getCountry().getName();
                logger.debug("-------------- User's IP detailed information -------------------");
                logger.debug("City: " + cityName);
                logger.debug("postal: " + postal);
                logger.debug("state: " + state);
                userActivityLog.setCity(cityName);
                userActivityLog.setPostal(postal);
                userActivityLog.setState(state);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GeoIp2Exception e) {
                e.printStackTrace();
            }
        }
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

}
