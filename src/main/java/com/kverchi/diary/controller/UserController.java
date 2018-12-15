package com.kverchi.diary.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kverchi.diary.model.entity.User;

import java.security.Principal;

/**
 * Created by Liudmyla Melnychuk on 12.12.2018.
 */
@RestController
public class UserController {
    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }
}
