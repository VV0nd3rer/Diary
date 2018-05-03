package com.kverchi.diary.service;

        import com.kverchi.diary.domain.UserActivityLog;
        import org.springframework.security.core.Authentication;

        import java.util.List;

/**
 * Created by Liudmyla Melnychuk on 26.9.2017.
 */
public interface UserActivityLogService {
    void addUserActivityLog(Authentication authentication);
    UserActivityLog getUserActivity(String session_id);
    List<UserActivityLog> getLastUserActivity(int user_id);
    void updateUserActivityLog(String sessionId);
    String getUserInfoFromHttpRequest();
    void saveUserInfoFromHttpRequest();
}
