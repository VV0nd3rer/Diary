package com.kverchi.diary.security;

import org.springframework.stereotype.Service;

/**
 * Created by Liudmyla Melnychuk on 21.2.2019.
 */
public enum SecretsManager {
    //TODO Implement security for sensitive information from code
    DIARY_EMAIL(Constants.DIARY_EMAIL),
    ENCODING_KEY(Constants.ENCODING_KEY);


    SecretsManager(final String attribute) {
    }
    public static class Constants {
        public static final String DIARY_EMAIL = "insert_your_email";
        public static final String ENCODING_KEY = "insert_your_key";
    }
}
