package com.kverchi.diary.domain;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Liudmyla Melnychuk on 18.9.2017.
 */
@Entity
@Table(name="user_activity_log")
public class UserActivityLog {
    @Id
    @Column(name="session_id")
    private String session_id;
    @Column(name="user_id")
    private int user_id;
    @Basic(optional = false)
    @Column(name="login_time", insertable = false, updatable = false)
    private ZonedDateTime login_time;
    private String login_ip;
    private String user_hostname;
    private boolean active_session;

    @Column(name="os_info")
    private String osInfo;

    public String getOsInfo() {
        return osInfo;
    }

    public void setOsInfo(String osInfo) {
        this.osInfo = osInfo;
    }

    public boolean isActive_session() {
        return active_session;
    }

    public void setActive_session(boolean active_session) {
        this.active_session = active_session;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public ZonedDateTime getLogin_time() {
        DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
        String text = login_time.format(formatter);
        login_time = ZonedDateTime.parse(text, formatter);
        return login_time;
    }

    public void setLogin_time(ZonedDateTime login_time) {
        this.login_time = login_time;
    }

    public String getLogin_ip() {
        return login_ip;
    }

    public void setLogin_ip(String login_ip) {
        this.login_ip = login_ip;
    }
    public String getUser_hostname() {
        return user_hostname;
    }

    public void setUser_hostname(String user_hostname) {
        this.user_hostname = user_hostname;
    }
}
