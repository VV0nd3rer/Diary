package com.kverchi.diary.controller;

import com.kverchi.diary.model.enums.ServiceMessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.kverchi.diary.model.entity.User;
import com.kverchi.diary.model.LoginStatus;


import java.security.Principal;

/**
 * Created by Liudmyla Melnychuk on 12.12.2018.
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    AuthenticationProvider authenticationProvider;

    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }

    @PostMapping(value = "/login")
    @ResponseBody
    public LoginStatus processLogin(@RequestBody User requestUser) {
        Authentication authentication = null;
        UsernamePasswordAuthenticationToken token = new
                UsernamePasswordAuthenticationToken(requestUser.getUsername(), requestUser.getPassword());
        try {
            authentication = this.authenticationProvider.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();
            user.setPassword(null);
            return new LoginStatus(ServiceMessageResponse.OK.name().toString(),
                    ServiceMessageResponse.OK.toString());

        } catch (BadCredentialsException ex) {
            return new LoginStatus(ServiceMessageResponse.NO_USER_WITH_USERNAME.name().toString(),
                    ServiceMessageResponse.NO_USER_WITH_USERNAME.toString());
        }
    }

}
