package com.kverchi.diary.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Kverchi on 20.6.2018.
 */
@RestController
@RequestMapping("posts")
public class PostController {
    @RequestMapping("/main")
    public ModelAndView showMain(
            @RequestParam(value="name", required=false, defaultValue="Guest") String name) {
        ModelAndView mv = new ModelAndView("main");
        mv.addObject("message", "Welcome");
        mv.addObject("name", name);
        return mv;
    }
}
