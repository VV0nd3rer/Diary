package com.kverchi.diary.domain;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Kverchi on 18.9.2017.
 */
@Entity
@Table(name="user_activity_log")
public class UserActivityLog {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="log_id")
    private int log_id;
    @Column(name="user_id")
    private int user_id;
    @Basic(optional = false)
    @Column(name="login_time", insertable = false, updatable = false)
    private ZonedDateTime login_time;
    private String login_ip;
    private String user_hostname;



    public int getLog_id() {
        return log_id;
    }

    public void setLog_id(int log_id) {
        this.log_id = log_id;
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
