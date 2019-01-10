package com.kverchi.diary.model;

import com.kverchi.diary.model.entity.User;

/**
 * Created by Liudmyla Melnychuk on 8.1.2019.
 */
public class LoginStatus {
    private String code;
    private String message;


    public LoginStatus(){

    }

    public LoginStatus(String code, String message) {
        this.code = code;
        this.message = message;
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

}
