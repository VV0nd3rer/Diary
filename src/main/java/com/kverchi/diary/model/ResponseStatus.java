package com.kverchi.diary.model;

import com.kverchi.diary.model.entity.User;

/**
 * Created by Liudmyla Melnychuk on 8.1.2019.
 */
public class ResponseStatus {
    private String code;
    private String message;
    private User user;

    public ResponseStatus(){

    }

    public ResponseStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseStatus(String code, String message, User user) {
        this.code = code;
        this.message = message;
        this.user = user;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
