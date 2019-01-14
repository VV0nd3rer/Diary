package com.kverchi.diary.service.impl;

import com.kverchi.diary.model.ServiceResponse;
import com.kverchi.diary.model.entity.User;
import com.kverchi.diary.model.enums.ServiceMessageResponse;
import com.kverchi.diary.repository.UserRepository;
import com.kverchi.diary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Kverchi on 20.7.2018.
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationProvider authenticationProvider;

    @Override
    public ServiceResponse login(User requestUser) {
        Authentication authentication = null;
        UsernamePasswordAuthenticationToken token = new
                UsernamePasswordAuthenticationToken(requestUser.getUsername(), requestUser.getPassword());
        try {
            authentication = this.authenticationProvider.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            /*User user = (User) authentication.getPrincipal();
            user.setPassword(null);*/

            return new ServiceResponse(HttpStatus.OK, ServiceMessageResponse.OK);

        } catch (BadCredentialsException ex) {
            return new ServiceResponse(HttpStatus.BAD_REQUEST, ServiceMessageResponse.NO_USER_WITH_USERNAME);
        }
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void activateAccount(User user) {

    }

    @Override
    public boolean updatePassword(User user) {
        return false;
    }

    @Override
    public boolean createAndSendResetPasswordToken(String email) {
        return false;
    }

    @Override
    public User getResetPasswordToken(String token) {
        return null;
    }

    @Override
    public User getUserFromSession() {
        return null;
    }

    @Override
    public boolean isValuePresent(String key, Object value) {
        return false;
    }

    @Override
    public void saveUserInfo(int userId, String info) {

    }

    @Override
    public boolean verifyPassword(String rawPass, String encodedPass) {
        return false;
    }
}
